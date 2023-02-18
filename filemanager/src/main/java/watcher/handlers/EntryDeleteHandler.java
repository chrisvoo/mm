package watcher.handlers;

import models.scanner.ScanOp;
import models.stats.Stats;

import java.nio.file.Path;

public class EntryDeleteHandler extends EntryHandler {

  public EntryDeleteHandler(Path child) {
    super(child);
  }

  private void delete() {
    logger.info("audioFile delete: " + child);
    musicFileService.delete(child);
  }

  /**
   * In this particular case, we deal with single files or directory in the same way
   */
  private void handleCommon() {
    Stats stats = musicFileService.getInfoByPath(this.child);

    if (stats != null) {
      ScanOp result = new ScanOp()
        .joinBytes(-stats.getTotalBytes())
        .joinInsertedFiles(-stats.getTotalFiles())
        .joinScannedFiles(-stats.getTotalFiles());
      delete();
      statsService.save(result);
    }
  }

  public void handleDirectory() {
    this.handleCommon();
  }

  public void handleFile() {
    this.handleCommon();
  }
}