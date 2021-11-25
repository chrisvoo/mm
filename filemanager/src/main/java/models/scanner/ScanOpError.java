package models.scanner;

import java.sql.Timestamp;

public class ScanOpError {
    private long id;
    private long scanOpId;
    private String message;
    private Timestamp createdAt;

    public static String tableName() {
        return "scan_ops_errors";
    }

    public long getId() {
        return id;
    }

    public ScanOpError setId(long id) {
        this.id = id;
        return this;
    }

    public long getScanOpId() {
        return scanOpId;
    }

    public ScanOpError setScanOpId(long scanOpId) {
        this.scanOpId = scanOpId;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ScanOpError setMessage(String message) {
        this.message = message;
        return this;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public ScanOpError setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
        return this;
    }
}
