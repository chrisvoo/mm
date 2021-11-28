package models.scanner;

import models.Model;

import java.sql.Timestamp;

public class ScanOpError extends Model {
    private Long id;
    private Long scanOpId;
    private String message;
    private Timestamp createdAt;

    public static String tableName() {
        return "scan_ops_errors";
    }

    public Long getId() {
        return id;
    }

    public ScanOpError setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getScanOpId() {
        return scanOpId;
    }

    public ScanOpError setScanOpId(Long scanOpId) {
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
