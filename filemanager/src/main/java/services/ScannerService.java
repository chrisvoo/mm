package services;

import models.scanner.ScanOp;
import models.scanner.ScanOpError;

public interface ScannerService {
    /**
     * Add a new result of a scanning operation.
     * @param op op The operation
     * @return long The ScanOp primary key
     */
    public ScanOp save(ScanOp op);

    /**
     * Add a new error
     * @param error The error.
     */
    public void addError(ScanOpError error);

    /**
     * The scan op corresponding to the specified id.
     * @param id The scanOp primary key
     * @return ScanOp the operation instance
     */
    public ScanOp getById(long id);

    // TODO: cursor-based pagination
}