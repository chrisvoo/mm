package models;

import models.band.BandActivity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BandActivityTest {
    @Test
    public void requiredFieldsTest() {
        BandActivity ba = new BandActivity();
        Assertions.assertFalse(ba.areRequiredFieldsValid());
    }
}
