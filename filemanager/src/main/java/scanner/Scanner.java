package scanner;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import models.scanner.ScanOp;
import services.ScannerService;
import utils.FileManagerModule;
import utils.FileUtils;

import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.concurrent.ForkJoinPool;
import java.util.logging.Logger;

/**
 * Scan a directory for MP3 and store its metadata and paths inside the db
 */
public class Scanner extends Thread {
  private static final Logger logger = Logger.getLogger(Scanner.class.getName());
  private ArrayList<Path> files = new ArrayList<>();
  private Path targetDirectory;
  @Inject private ScannerService scannerService;

  public Scanner() {
    super("Scanner");
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
    Instant start = Instant.now();
    boolean isListOk = listFiles(this.targetDirectory);
    if (!isListOk) {
      return null;
    }

    logger.info("Collected " + files.size() + " paths");
    int nThreads = Runtime.getRuntime().availableProcessors();

    logger.info(String.format("Running scanner with a pool of %d threads\n", nThreads));
    ForkJoinPool pool = new ForkJoinPool(nThreads);

    // this allows to inject all the things we need in ScanTask
    Injector injector = Guice.createInjector(new FileManagerModule());
    ScanTask task = injector.getInstance(ScanTask.class);

    ScanOp result = pool.invoke(task.setPaths(this.files));
    Instant end = Instant.now();
    result.setTotalElapsedTime((short)Duration.between(start, end).getSeconds());
    ScanOp savedResult = scannerService.save(result);

    if (result.hasErrors()) {
      scannerService.bulkSave(result.getScanErrors(), savedResult.getId());
    }

    // save result in db
    return savedResult;
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