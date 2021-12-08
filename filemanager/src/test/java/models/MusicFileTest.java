package models;

import models.band.Band;
import models.files.MusicFile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MusicFileTest {
    @Test
    public void requiredFieldsTest() {
        MusicFile mf = new MusicFile();
        Assertions.assertFalse(mf.isValid());

        mf.setAbsolutePath("a/path");
        Assertions.assertTrue(mf.isValid());
    }
}
