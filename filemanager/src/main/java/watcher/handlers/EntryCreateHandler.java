package watcher.handlers;

import models.files.MusicFile;
import models.scanner.ScanOp;
import utils.FileUtils;
import utils.eyeD3.EyeD3;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;

public class EntryCreateHandler extends EntryHandler {

  public EntryCreateHandler(Path child) {
    super(child);
  }

  private void register(Path dir) throws IOException {
    WatchKey key = dir.register(this.watcher.getService(), ENTRY_CREATE, ENTRY_DELETE);
    watcher.getKeys().put(key, dir);
  }

  public void handleDirectory() throws IOException {
    logger.info(String.format("Scanning %s...\n", child));
    // register directory and subdirectories
    Files.walkFileTree(child, new SimpleFileVisitor<>() {
      @Override
      public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
        throws IOException {
        register(dir);
        return FileVisitResult.CONTINUE;
      }
    });

    saveDirFiles(child);

    logger.info("Done.");
  }

  private void saveDirFiles(Path dir) {
    try {
      List<Path> files = FileUtils.listMP3Files(dir);
      logger.info("saveDirFiles: " + dir + ": " + files.size());

      ScanOp result = new ScanOp();
      List<MusicFile> musicFiles = new ArrayList<>();
      for (Path path : files) {
        MusicFile file = EyeD3.parse(path.normalize());
        if (file != null) {
          result
            .joinBytes(file.getSize())
            .joinInsertedFiles(1)
            .joinScannedFiles(1);
          musicFiles.add(file);
        }
      }

      if (!musicFiles.isEmpty()) {
        musicFileService.bulkSave(musicFiles);
        statsService.save(result);
      }
    } catch (Exception e) {
      logger.severe("saveDirFiles: " + e.getMessage());
    }
  }

  public void handleFile() {
    if (FileUtils.isMp3FileName(child)) {
      MusicFile audioFile = EyeD3.parse(child);
      if (audioFile != null) {
        int saved = musicFileService.upsert(audioFile);
        if (saved == 1) {
          logger.info("audioFile create: " + child);
          ScanOp result = new ScanOp()
            .joinInsertedFiles(1)
            .joinBytes(audioFile.getSize());
          statsService.save(result);
        }
      }
    }
  }
}