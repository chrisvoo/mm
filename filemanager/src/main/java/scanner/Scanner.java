package scanner;

import com.google.inject.Inject;
import models.files.MusicFile;
import models.scanner.ScanOp;
import services.MusicFileService;
import services.ScannerService;
import utils.FileUtils;
import utils.di.GuiceUtils;
import utils.eyeD3.EyeD3;
import utils.logging.LoggerInterface;

import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

/**
 * Scan a directory for MP3 and store its metadata and paths inside the db
 */
public class Scanner extends Thread {
  @Inject private LoggerInterface logger;
  private ArrayList<Path> files = new ArrayList<>();
  private Path targetDirectory;
  @Inject private ScannerService scannerService;
  @Inject private MusicFileService musicFileService;

  public Scanner() {
    super("Scanner");
  }

  public ArrayList<Path> getScannedFiles() {
    return this.files;
  }

  public Scanner setTargetDir(String dir) {
    this.targetDirectory = Paths.get(dir);
    return this;
  }

  public Scanner setTargetDir(Path dir) {
    this.targetDirectory = dir;
    return this;
  }

  public void run() {
     this.scan();
  }

  /**
   * It creates a ForkJoinPool using the available processors to read the metadata of the
   * music files and store them inside the database.
   * @return a ScanResult instance containing the total files found, the total
   * size and eventual errors encountered during the process
   */
  public ScanOp scan() {
    return scan(true);
  }

  private ScanOp scan(boolean withForkJoin) {
    Instant start = Instant.now();
    boolean isListOk = listFiles(this.targetDirectory);
    if (!isListOk) {
      return null;
    }

    logger.info("Collected " + files.size() + " paths");

    if (withForkJoin) {
      int nThreads = Runtime.getRuntime().availableProcessors();

      logger.info(String.format("Running scanner with a pool of %d threads\n", nThreads));
      ForkJoinPool pool = new ForkJoinPool(nThreads);

      // this allows to inject all the things we need in ScanTask
      ScanTask task = GuiceUtils.getInstance(ScanTask.class);

      ScanOp result = pool.invoke(task.setPaths(this.files));
      pool.shutdown();

      Instant end = Instant.now();
      result
        .setTotalElapsedTime((short) Duration.between(start, end).getSeconds())
        .setStarted(start)
        .setFinished(end);

      return scannerService.save(result);
    } else {
      ScanOp result = new ScanOp();

      List<MusicFile> chunksList = new ArrayList<>();

      for (Path path: this.files) {
        MusicFile audioFile;

        try {
          // If for some reasons, metadata aren't readable, we just store the file path
          audioFile = EyeD3.parse(path);
        } catch (Exception e) {
          String filePath = path.normalize().toAbsolutePath().toString();
          audioFile = new MusicFile()
            .setAbsolutePath(filePath)
            .calculateSize(path);

          logger.severe(String.format("Error for %s: %s", path, e.getMessage()));
        }

        result
          .joinScannedFiles(1)
          .joinBytes(audioFile.getSize());

        chunksList.add(audioFile);

        try {
          if (chunksList.size() == 100) {
            this.musicFileService.bulkSave(chunksList);
            chunksList.clear();
          }
        } catch (Exception e) {
          logger.severe("Can't save list: " + e.getMessage());
        }
      }

      if (chunksList.size() > 0) {
        try {
          this.musicFileService.bulkSave(chunksList);
        } catch (Exception e) {
          logger.severe("Can't save list: " + e.getMessage());
        }
      }

      return result;
    }
  }

  /**
   * It recursively stores all MP3 paths found in a directory (and all its subdirs)
   * inside an ArrayList.
   * @param path A Path.
   * @return true if it was able to found the directory and read its files, false otherwise
   */
  public boolean listFiles(Path path) {
    try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
      for (Path entry : stream) {
        if (Files.isDirectory(entry)) {
          listFiles(entry);
        }

        if (FileUtils.isMp3(entry)) {
          files.add(entry);
        }
      }

      return true;
    } catch (Exception e) {
      logger.severe(e.getClass().getSimpleName() + ": " + e.getMessage());
      return false;
    }
  }
}