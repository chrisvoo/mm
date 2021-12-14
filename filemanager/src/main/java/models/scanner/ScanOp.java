package models.scanner;

import com.google.gson.Gson;
import models.Model;

import java.sql.Timestamp;
import java.util.List;

/**
 * Scanner operation result
 */
public class ScanOp extends Model {
    private Long id;
    private Timestamp started;
    private Integer totalFilesScanned;
    private Integer totalFilesInserted;
    private Short totalElapsedTime;
    private Long totalBytes;
    private Timestamp finished;
    private Boolean hasErrors;

    /**
     * Transient property to link the join table.
     */
    private List<ScanOpError> scanErrors;

    public static String tableName() {
        return "scan_ops";
    }

    public Integer getTotalFilesInserted() {
        return totalFilesInserted;
    }

    public Short getTotalElapsedTime() {
        return totalElapsedTime;
    }

    public ScanOp setTotalElapsedTime(Short totalElapsedTime) {
        this.totalElapsedTime = totalElapsedTime;
        return this;
    }

    public Boolean getHasErrors() {
        return hasErrors;
    }

    public ScanOp setTotalFilesInserted(Integer totalFilesInserted) {
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

    public ScanOp setStarted(Timestamp started) {
        this.started = started;
        return this;
    }

    public Integer getTotalFilesScanned() {
        return totalFilesScanned;
    }

    public ScanOp setTotalFilesScanned(Integer totalFilesScanned) {
        this.totalFilesScanned = totalFilesScanned;
        return this;
    }

    public Long getTotalBytes() {
        return totalBytes;
    }

    public ScanOp setTotalBytes(Long totalBytes) {
        this.totalBytes = totalBytes;
        return this;
    }

    public Timestamp getFinished() {
        return finished;
    }

    public ScanOp setFinished(Timestamp finished) {
        this.finished = finished;
        return this;
    }

    public Boolean isHasErrors() {
        return hasErrors;
    }

    public ScanOp setHasErrors(Boolean hasErrors) {
        this.hasErrors = hasErrors;
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
    public ScanOp joinScannedFiles(long numFiles) {
        totalFilesScanned += numFiles;
        return this;
    }

    /**
     * It merges the total number of files inserted into the db by this instance with the one of another instance.
     * @param numFiles Total number of files.
     * @return This instance.
     */
    public ScanOp joinInsertedFiles(int numFiles) {
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
     * It merges the current errors occurred for this result with the ones of another result.
     * @param errors A map of errors
     * @return This instance.
     */
    public ScanOp joinErrors(List<ScanOpError> errors) {
        if (errors == null) {
            return this;
        }

        if (!errors.isEmpty()) {
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