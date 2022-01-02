package models.stats;

import models.Model;

import java.sql.Timestamp;

/**
 * Stats about the system
 */
public class Stats extends Model<Stats> {
  private Long id;
  private int totalFiles;
  private long totalBytes;
  private Timestamp lastUpdate;

  public boolean isValid() {
    if (!super.isValid()) {
      // error types are already configured in the parent class.
      return false;
    }

    this.positiveNumberValidator("totalFilesInserted", this.totalFiles);
    this.positiveNumberValidator("totalBytes", this.totalBytes);

    return this.errorCode == null;
  }

  public Long getId() {
    return id;
  }

  public Stats setId(Long id) {
    this.id = id;
    return this;
  }

  public int getTotalFiles() {
    return totalFiles;
  }

  public Stats setTotalFiles(int totalFiles) {
    this.totalFiles = totalFiles;
    return this;
  }

  public long getTotalBytes() {
    return totalBytes;
  }

  public Stats setTotalBytes(long totalBytes) {
    this.totalBytes = totalBytes;
    return this;
  }

  public Timestamp getLastUpdate() {
    return lastUpdate;
  }

  public Stats setLastUpdate(Timestamp lastUpdate) {
    this.lastUpdate = lastUpdate;
    return this;
  }
}