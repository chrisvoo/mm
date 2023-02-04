package models;

import models.scanner.ScanOp;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ScanOpTest {
    @Test
    public void requiredFieldsTest() {
        ScanOp sop = new ScanOp();
        Assertions.assertTrue(sop.isValid());
    }

    @Test
    public void operationsTest() {
        ScanOp op = new ScanOp()
          .joinBytes(5)
          .joinScannedFiles(500)
          .joinInsertedFiles(487);

        ScanOp op2 = new ScanOp()
          .joinBytes(7)
          .joinScannedFiles(486)
          .joinInsertedFiles(486);

        ScanOp result = op.joinResult(op2);
        assertEquals(500 + 486, result.getTotalFilesScanned());
        assertEquals(487 + 486, result.getTotalFilesInserted());
        assertEquals(12, result.getTotalBytes());
    }
}