package models;

import models.band.Band;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BandTest {
    @Test
    public void requiredFieldsTest() {
        Band band = new Band();
        Assertions.assertFalse(band.areRequiredFieldsValid());

        band.setName(" ");
        Assertions.assertFalse(band.areRequiredFieldsValid());

        band.setName("AC/DC");
        Assertions.assertTrue(band.areRequiredFieldsValid());
    }
}
