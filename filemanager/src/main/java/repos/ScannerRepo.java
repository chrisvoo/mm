package repos;

import models.scanner.ScanOp;
import models.scanner.ScanOpError;
import services.ScannerService;

public class ScannerRepo implements ScannerService {
    /**
     * Add a new result of a scanning operation.
     *
     * @param op op The operation
     * @return long The ScanOp primary key
     */
    @Override
    public long addOperation(ScanOp op) {
        return 0;
    }

    /**
     * Add a new error
     *
     * @param error The error.
     */
    @Override
    public void addError(ScanOpError error) {

    }

    /**
     * The scan op corresponding to the specified id.
     *
     * @param id The scanOp primary key
     * @return ScanOp the operation instance
     */
    @Override
    public ScanOp getById(long id) {
        return null;
    }
}
