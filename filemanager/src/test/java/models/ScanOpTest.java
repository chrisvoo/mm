package models;

import models.scanner.ScanOp;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ScanOpTest {
    @Test
    public void requiredFieldsTest() {
        ScanOp sop = new ScanOp();
        Assertions.assertTrue(sop.isValid());
    }
}
