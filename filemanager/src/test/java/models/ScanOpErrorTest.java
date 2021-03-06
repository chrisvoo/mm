package models;

import models.scanner.ScanOpError;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ScanOpErrorTest {
    @Test
    public void requiredFieldsTest() {
        ScanOpError sop = new ScanOpError();
        Assertions.assertFalse(sop.isValid());

        sop.setScanOpId(5L)
          .setMessage("hey")
            .setAbsolutePath("path");
        Assertions.assertTrue(sop.isValid());
    }
}