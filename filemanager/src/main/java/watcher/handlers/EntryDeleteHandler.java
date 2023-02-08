package watcher.handlers;

import models.files.MusicFile;
import models.scanner.ScanOp;
import scanner.Scanner;
import utils.eyeD3.EyeD3;

import java.nio.file.Path;
import java.util.ArrayList;

public class EntryDeleteHandler extends EntryHandler {

  public EntryDeleteHandler(Path child) {
    super(child);
  }

  private void delete() {
    logger.info("audioFile delete: " + child);
    musicFileService.delete(child);
  }

  public void handleDirectory() {
    delete();

    ScanOp result = new ScanOp();
    Scanner scan = new scanner.Scanner();
    if (scan.listFiles(child)) {
      ArrayList<Path> files = scan.getScannedFiles();
      for (Path entry: files) {
        MusicFile file = EyeD3.parse(entry.normalize());
        if (file != null) {
          result
            .joinBytes(-file.getSize())
            .joinInsertedFiles(-1)
            .joinScannedFiles(-1);
        }
      }

      if (result.getTotalFilesInserted() != 0) {
        statsService.save(result);
      }
    }
  }

  public void handleFile() {
    delete();

    MusicFile file = EyeD3.parse(child.normalize());
    if (file != null) {
      ScanOp result = new ScanOp()
        .joinBytes(-file.getSize())
        .joinInsertedFiles(-1)
        .joinScannedFiles(-1);
      statsService.save(result);
    }
  }
}