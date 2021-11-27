package models.scanner;

import java.sql.Timestamp;
import java.util.List;

/**
 * Scanner operation result
 */
public class ScanOp {
    private long id;
    private Timestamp started;
    private int filesFound;
    private long totalSize;
    private Timestamp finished;
    private boolean hasErrors;

    /**
     * Transient property to link the join table.
     */
    private List<ScanOpError> errors;

    public static String tableName() {
        return "scan_ops";
    }

    public ScanOp setErrors(List<ScanOpError> errors) {
        this.errors = errors;
        return this;
    }

    public List<ScanOpError> getErrors() {
        return this.errors;
    }

    public long getId() {
        return id;
    }

    public ScanOp setId(long id) {
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

    public int getFilesFound() {
        return filesFound;
    }

    public ScanOp setFilesFound(int filesFound) {
        this.filesFound = filesFound;
        return this;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public ScanOp setTotalSize(long totalSize) {
        this.totalSize = totalSize;
        return this;
    }

    public Timestamp getFinished() {
        return finished;
    }

    public ScanOp setFinished(Timestamp finished) {
        this.finished = finished;
        return this;
    }

    public boolean isHasErrors() {
        return hasErrors;
    }

    public ScanOp setHasErrors(boolean hasErrors) {
        this.hasErrors = hasErrors;
        return this;
    }
}