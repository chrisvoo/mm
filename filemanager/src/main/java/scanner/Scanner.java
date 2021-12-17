package scanner;

import com.google.inject.Inject;
import models.scanner.ScanOp;
import utils.Db;

import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
  @Inject private Db db;

  public Scanner() {
    super("Scanner");
  }

  public Scanner setTargetDir(String dir) {
    this.targetDirectory = Paths.get(dir);
    return this;
  }

  public void run() {
     this.start(this.targetDirectory);
  }

  /**
   * It creates a ForkJoinPool using the available processors to read the metadata of the
   * music files and store them inside the database.
   * @return a ScanResult instance containing the total files found, the total
   * size and eventual errors encountered during the process
   */
  public ScanOp start(Path chosenPath) {
    boolean isListOk = listFiles(chosenPath);
    if (!isListOk) {
      return null;
    }

    logger.info("Collected " + files.size() + " paths");
    int nThreads = Runtime.getRuntime().availableProcessors();

    logger.info(String.format("Running scanner with a pool of %d threads\n", nThreads));
    ForkJoinPool pool = new ForkJoinPool(nThreads);

    ScanTask task = new ScanTask(files, db);
    return pool.invoke(task);
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

        if (entry.getFileName().toString().toLowerCase().endsWith(".mp3")) {
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