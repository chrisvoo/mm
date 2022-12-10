package models.scanner;

import com.google.gson.Gson;
import models.Model;
import models.stats.Stats;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Scanner operation result
 */
public class ScanOp extends Model<ScanOp> {
    private Long id;
    private Timestamp started;
    private int totalFilesScanned = 0;
    private int totalFilesInserted = 0;
    private short totalElapsedTime = 0;
    private long totalBytes = 0;
    private Timestamp finished;
    private boolean hasErrors;

    /**
     * Transient property to link the join table.
     */
    private List<ScanOpError> scanErrors;

    public boolean isValid() {
        if (!super.isValid()) {
            // error types are already configured in the parent class.
            return false;
        }

        this.positiveNumberValidator("totalFilesScanned", this.totalFilesScanned);
        this.positiveNumberValidator("totalFilesInserted", this.totalFilesInserted);
        this.positiveNumberValidator("totalElapsedTime", this.totalElapsedTime);
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

    public ScanOp setScanErrors(List<ScanOpError> errors) {
        this.scanErrors = errors;
        return this;
    }

    public List<ScanOpError> getScanErrors() {
        return this.scanErrors;
    }

    public boolean hasErrors() {
        return (this.scanErrors != null && !this.scanErrors.isEmpty()) || this.hasErrors;
    }

    public ScanOp setHasErrors(boolean hasErrors) {
        this.hasErrors = hasErrors;
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
        if (bytes > 0) {
            totalBytes += bytes;
        } else {
            logger.warning("Passing negative amount of bytes: " + bytes);
        }
        return this;
    }

    public ScanOp joinError(ScanOpError error) {
        if (error == null) {
            return this;
        }

        if (this.scanErrors == null) {
            this.scanErrors = new ArrayList<>();
        }

        this.scanErrors.add(error);
        return this;
    }

    /**
     * It merges the current errors occurred for this result with the ones of another result.
     * @param errors A map of errors
     * @return This instance.
     */
    public ScanOp joinErrors(List<ScanOpError> errors) {
        if (errors == null) {
            return this;
        }

        if (!errors.isEmpty()) {
            if (this.scanErrors == null) {
                this.scanErrors = new ArrayList<>();
            }
            this.scanErrors.addAll(errors);
        }
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
        return this.joinErrors(result.getScanErrors())
          .joinScannedFiles(result.getTotalFilesScanned())
          .joinInsertedFiles(result.getTotalFilesInserted())
          .joinBytes(result.getTotalBytes());
    }
}