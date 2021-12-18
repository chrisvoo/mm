package models;

import models.scanner.ScanOp;
import models.scanner.ScanOpError;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

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
          .joinInsertedFiles(487)
          .joinErrors(List.of(
                new ScanOpError()
                   .setScanOpId(6L)
                  .setMessage("hey"))
          );

        ScanOp op2 = new ScanOp()
          .joinBytes(7)
          .joinScannedFiles(486)
          .joinInsertedFiles(486)
          .joinErrors(List.of(
            new ScanOpError()
              .setScanOpId(6L)
              .setMessage("hoy"))
          );

        ScanOp result = op.joinResult(op2);
        assertEquals(500 + 486, result.getTotalFilesScanned());
        assertEquals(487 + 486, result.getTotalFilesInserted());
        assertEquals(12, result.getTotalBytes());
        assertEquals(2, result.getScanErrors().size());
    }
}