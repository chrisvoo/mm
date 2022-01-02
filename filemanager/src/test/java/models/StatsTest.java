package models;

import models.stats.Stats;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StatsTest {
    @Test
    public void requiredFieldsTest() {
        Stats stats = new Stats();
        Assertions.assertTrue(stats.isValid());
    }
}