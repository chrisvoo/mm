package services;

import models.scanner.ScanOp;

public interface ScannerService {
    /**
     * Add a new result of a scanning operation.
     * @param op op The operation
     * @return long The ScanOp primary key
     */
    ScanOp save(ScanOp op);

    /**
     * The scan op corresponding to the specified id.
     * @param id The scanOp primary key
     * @return ScanOp the operation instance
     */
    ScanOp getById(long id);

}