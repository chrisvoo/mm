package models;

import models.band.Musician;
import models.scanner.ScanOpError;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MusicianTest {
    @Test
    public void requiredFieldsTest() {
        Musician mu = new Musician();
        Assertions.assertTrue(mu.isValid());
    }
}
