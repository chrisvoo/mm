package models;

import models.band.Musician;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MusicianTest {
    @Test
    public void requiredFieldsTest() {
        Musician mu = new Musician();
        Assertions.assertTrue(mu.isValid());
    }
}