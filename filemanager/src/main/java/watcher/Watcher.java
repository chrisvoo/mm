package watcher;

import com.google.inject.Inject;
import models.files.MusicFile;
import services.MusicFileService;
import utils.FileUtils;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardWatchEventKinds.*;

public class Watcher extends Thread {
  private static final Logger logger = Logger.getLogger(Watcher.class.getName());
  private final WatchService watcher;
  private final Map<WatchKey,Path> keys;
  private boolean closed = false;
  @Inject private MusicFileService musicFileService;

  /**
   * Watches recursively or not a provided path.
   * @param dir The path
   * @throws IOException thrown by newWatchService or registerAll
   */
  public Watcher(Path dir) throws IOException {
    this.watcher = FileSystems.getDefault().newWatchService();
    this.keys = new HashMap<>();

    if (dir != null) {
        logger.info(String.format("Scanning %s ...\n", dir));
        registerAll(dir);
        logger.info("Done.");
    }
  }

  /**
   * It doesn't start registering files as the other constructors.
   * @throws IOException thrown by newWatchService or registerAll
   */
  public Watcher() throws IOException {
    this(null);
  }

  @SuppressWarnings("unchecked")
  static <T> WatchEvent<T> cast(WatchEvent<?> event) {
    return (WatchEvent<T>)event;
  }

  /**
   * Register the given directory, and all its subdirectories, with the
   * WatchService.
   * @param start Starting directory
   * @return the Watcher
   * @throws IOException if an I/O error is thrown by a visitor method of Files.walkFileTree
   */
  public Watcher registerAll(final Path start) throws IOException {
    logger.info(String.format("Scanning %s ...", start));

    // register directory and subdirectories
    Files.walkFileTree(start, new SimpleFileVisitor<>() {
      @Override
      public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
        throws IOException {
        register(dir);
        return FileVisitResult.CONTINUE;
      }
    });

    logger.info("Done.");
    return this;
  }

  /**
   * Register the given directory with the WatchService
   */
  public Watcher register(Path dir) throws IOException {
    WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE);
    keys.put(key, dir);
    return this;
  }

  /**
   * Process all events for keys queued to the watcher.
   * Note: renaming a file produces a ENTRY_DELETE event, followed by a ENTRY_CREATE one.
   */
  public void run() {
    for (;;) {

      // wait for key to be signalled
      WatchKey key;
      try {
        key = watcher.take();
      } catch (InterruptedException  x) {
        logger.severe(x.getMessage());
        return;
      } catch (ClosedWatchServiceException x) {
        if (!this.closed) {
          logger.severe("Watcher service has been closed!");
        }
        //   service has been closed, no need to log this
        return;
      }

      Path dir = keys.get(key);
      if (dir == null) {
        logger.severe("WatchKey not recognized!!");
        continue;
      }

      for (WatchEvent<?> event: key.pollEvents()) {
        WatchEvent.Kind<?> kind = event.kind();

        // TBD - provide example of how OVERFLOW event is handled
        if (kind == OVERFLOW) {
          continue;
        }

        // Context for directory entry event is the file name of entry
        WatchEvent<Path> ev = cast(event);
        Path name = ev.context();
        // Resolve the filename against the directory.
        Path child = dir.resolve(name);

        try {
          // if directory is created, then register it and its subdirectories
          if ((kind == ENTRY_CREATE)) {
              if (Files.isDirectory(child, NOFOLLOW_LINKS)) {
                  registerAll(child);
                  saveDirFiles(child);
              } else if (FileUtils.isMp3FileName(child)) {
                  MusicFile audioFile = MusicFile.fromPath(child);
                  musicFileService.upsert(audioFile);
                  logger.info("audioFile create: " + child);
              }
          } else if ((kind == ENTRY_DELETE)) {
              logger.info("audioFile delete: " + child);
              musicFileService.delete(child);
          }
        } catch (Exception x) {
          logger.severe(
            String.format(
              "Error for %s event %s, file %s: %s",
              x.getClass().getName(),
              kind.name(),
              child,
              x.getMessage())
          );
          x.printStackTrace();
        }
      }

      /* Reset the key -- this step is critical if you want to
       * receive further watch events.  If the key is no longer valid,
       * the directory is inaccessible so exit the loop. */
      boolean valid = key.reset();
      if (!valid) {
        keys.remove(key);

        // all directories are inaccessible
        if (keys.isEmpty()) {
          break;
        }
      }
    }
  }

  /**
   * We save the first-level MP3 files into the database. FileUtils::isMp3 also checks that the path is a regular file.
   * @param dir The resource
   */
  private void saveDirFiles(Path dir) {
    try {
      List<Path> files = FileUtils.listMP3Files(dir);
      logger.info("saveDirFiles: " + dir + ": " + files.size());

      List<MusicFile> musicFiles = new ArrayList<>();
      for (Path path : files) {
        musicFiles.add(MusicFile.fromPath(path.normalize()));
      }

      musicFileService.bulkSave(musicFiles);
    } catch (Exception e) {
      logger.severe("saveDirFiles: " + e.getMessage());
    }
  }

  /**
   * Stops this service
   */
  public void close() {
    try {
      this.closed = true;
      this.watcher.close();
    } catch (IOException e) {
      logger.warning("Couldn't stop Watcher service: " + e.getMessage());
    }
  }
}