package models.scanner;

import com.google.gson.Gson;
import models.Model;
import models.stats.Stats;

import java.sql.Timestamp;
import java.time.Instant;

/**
 * Scanner operation result
 */
public class ScanOp extends Model<ScanOp> {

    private Long id;
    private Timestamp started;
    private int totalFilesScanned = 0;
    private int totalFilesInserted = 0;
    private short totalElapsedTime = 0;
    private long totalBytes = 0L;
    private Timestamp finished;

    public boolean isValid() {
        if (!super.isValid()) {
            // error types are already configured in the parent class.
            return false;
        }

        this.positiveNumberValidator("totalFilesScanned", (long)this.totalFilesScanned);
        this.positiveNumberValidator("totalFilesInserted", (long)this.totalFilesInserted);
        this.positiveNumberValidator("totalElapsedTime", (long)this.totalElapsedTime);
        this.positiveNumberValidator("totalBytes", this.totalBytes);

        return this.errorCode == null;
    }

    /**
     * It creates a new Stats instance from the current results
     * @return The Stats instance
     */
    public Stats getStats() {
        return new Stats()
          .setLastUpdate(Timestamp.from(Instant.now()))
          .setTotalBytes(this.getTotalBytes())
          .setTotalFiles(this.getTotalFilesInserted());
    }

    public int getTotalFilesInserted() {
        return totalFilesInserted;
    }

    public short getTotalElapsedTime() {
        return totalElapsedTime;
    }

    public ScanOp setTotalElapsedTime(short totalElapsedTime) {
        this.totalElapsedTime = totalElapsedTime;
        return this;
    }

    public ScanOp setTotalFilesInserted(int totalFilesInserted) {
        this.totalFilesInserted = totalFilesInserted;
        return this;
    }

    public Long getId() {
        return id;
    }

    public ScanOp setId(Long id) {
        this.id = id;
        return this;
    }

    public Timestamp getStarted() {
        return started;
    }

    public ScanOp setStarted(Instant started) {
        this.started = Timestamp.from(started);
        return this;
    }

    public ScanOp setStarted(Timestamp started) {
        this.started = started;
        return this;
    }

    public int getTotalFilesScanned() {
        return totalFilesScanned;
    }

    public ScanOp setTotalFilesScanned(int totalFilesScanned) {
        this.totalFilesScanned = totalFilesScanned;
        return this;
    }

    public long getTotalBytes() {
        return totalBytes;
    }

    public ScanOp setTotalBytes(long totalBytes) {
        this.totalBytes = totalBytes;
        return this;
    }

    public Timestamp getFinished() {
        return finished;
    }

    public ScanOp setFinished(Instant finished) {
        this.finished = Timestamp.from(finished);
        return this;
    }

    public ScanOp setFinished(Timestamp finished) {
        this.finished = finished;
        return this;
    }

    public static ScanOp fromJson(String json) {
        return new Gson().fromJson(json, ScanOp.class);
    }

    /**
     * It merges the total number of audio files found by the scanner in this result with the one of another result.
     * @param numFiles Total number of files.
     * @return This instance.
     */
    public ScanOp joinScannedFiles(int numFiles) {
        totalFilesScanned += numFiles;
        return this;
    }

    /**
     * It merges the total number of files inserted into the db by this instance with the one of another instance.
     * @param numFiles Total number of files.
     * @return This instance.
     */
    public ScanOp joinInsertedFiles(long numFiles) {
        totalFilesInserted += numFiles;
        return this;
    }

    /**
     * It merges the total number of bytes of all the files found by this result with the one of another result.
     * @param bytes The total bytes
     * @return This instance.
     */
    public ScanOp joinBytes(long bytes) {
        totalBytes += bytes;
        return this;
    }

    /**
     * Merge the results of this instance with the ones of another one, performing sums.
     * @param result Another result
     * @return This instance updated with the results of another instance
     */
    public ScanOp joinResult(ScanOp result) {
        if (result == null) {
            return this;
        }

        // we do not consider the total time elapsed, we just use that field in the Main class
        return this
          .joinScannedFiles(result.getTotalFilesScanned())
          .joinInsertedFiles(result.getTotalFilesInserted())
          .joinBytes(result.getTotalBytes());
    }
}