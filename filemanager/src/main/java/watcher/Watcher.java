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
  private final boolean recursive;
  private boolean trace = false;
  private boolean closed = false;
  @Inject private MusicFileService musicFileService;

  /**
   * Watches recursively or not a provided path.
   * @param dir The path
   * @param recursive if to watch also sub-folders and files.
   * @throws IOException thrown by newWatchService or registerAll
   */
  public Watcher(Path dir, boolean recursive) throws IOException {
    this.watcher = FileSystems.getDefault().newWatchService();
    this.keys = new HashMap<>();
    this.recursive = recursive;

    if (dir != null) {
      if (recursive) {
        logger.info(String.format("Scanning %s ...\n", dir));
        registerAll(dir);
        logger.info("Done.");
      } else {
        register(dir);
      }
    }


    // enable trace after initial registration
    this.trace = true;
  }

  /**
   * Watches recursively a provided path.
   * @param dir The path
   * @throws IOException thrown by newWatchService or registerAll
   */
  public Watcher(Path dir) throws IOException {
    this(dir, true);
  }

  /**
   * It doesn't start registering files as the other constructors.
   * @throws IOException thrown by newWatchService or registerAll
   */
  public Watcher() throws IOException {
    this(null, true);
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

    // register directory and sub-directories
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
    if (trace) {
      Path prev = keys.get(key);
      if (prev == null) {
        logger.fine(String.format("register: %s\n", dir));
      } else {
        if (!dir.equals(prev)) {
          logger.fine(String.format("update: %s -> %s\n", prev, dir));
        }
      }
    }
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
      } catch (InterruptedException x) {
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

        if (FileUtils.isMp3FileName(child)) {
          // print out event
          logger.info(String.format("%s: %s\n", event.kind().name(), child));

          switch (event.kind().name()) {
            case "ENTRY_CREATE" -> {
              MusicFile audioFile = MusicFile.fromPath(child);
              musicFileService.upsert(audioFile);
            }
            case "ENTRY_DELETE" -> musicFileService.deleteAll(child);
          }
        }

        // if directory is created, and watching recursively, then
        // register it and its sub-directories
        if (recursive && (kind == ENTRY_CREATE)) {
          try {
            if (Files.isDirectory(child, NOFOLLOW_LINKS)) {
              registerAll(child);
              saveDirFiles(child);
            }
          } catch (IOException x) {
            logger.severe(x.getMessage());
          }
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
      List<MusicFile> files = new ArrayList<>();
      for (Path path : Files.newDirectoryStream(dir, FileUtils::isMp3)) {
        files.add(MusicFile.fromPath(path.normalize()));
      }

      musicFileService.bulkSave(files);
    } catch (IOException e) {
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