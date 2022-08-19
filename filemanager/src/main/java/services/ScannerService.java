package services;

import models.scanner.ScanOp;
import models.scanner.ScanOpError;

import java.util.List;

public interface ScannerService {
    /**
     * Add a new result of a scanning operation.
     * @param op op The operation
     * @return long The ScanOp primary key
     */
    ScanOp save(ScanOp op);

    /**
     * Add a new error
     * @param error The error.
     */
    void addError(ScanOpError error);

    /**
     * The scan op corresponding to the specified id.
     * @param id The scanOp primary key
     * @return ScanOp the operation instance
     */
    ScanOp getById(long id);

    long bulkSaveErrors(List<ScanOpError> errors, long scanOpId);

    // TODO: cursor-based pagination
}