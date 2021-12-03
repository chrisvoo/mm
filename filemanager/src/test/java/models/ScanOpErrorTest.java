package models;

import models.scanner.ScanOp;
import models.scanner.ScanOpError;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ScanOpErrorTest {
    @Test
    public void requiredFieldsTest() {
        ScanOpError sop = new ScanOpError();
        Assertions.assertFalse(sop.areRequiredFieldsValid());

        sop.setScanOpId(5L);
        Assertions.assertTrue(sop.areRequiredFieldsValid());
    }
}
