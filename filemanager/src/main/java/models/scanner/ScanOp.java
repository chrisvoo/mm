package models.scanner;

import models.Model;

import java.sql.Timestamp;
import java.util.List;

/**
 * Scanner operation result
 */
public class ScanOp extends Model {
    private Long id;
    private Timestamp started;
    private Integer filesFound;
    private Long totalSize;
    private Timestamp finished;
    private Boolean hasErrors;

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

    public Integer getFilesFound() {
        return filesFound;
    }

    public ScanOp setFilesFound(Integer filesFound) {
        this.filesFound = filesFound;
        return this;
    }

    public Long getTotalSize() {
        return totalSize;
    }

    public ScanOp setTotalSize(Long totalSize) {
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

    public Boolean isHasErrors() {
        return hasErrors;
    }

    public ScanOp setHasErrors(Boolean hasErrors) {
        this.hasErrors = hasErrors;
        return this;
    }
}
