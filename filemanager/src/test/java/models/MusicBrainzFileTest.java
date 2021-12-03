package models;

import models.files.MusicBrainzFile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MusicBrainzFileTest {
    @Test
    public void requiredFieldsTest() {
        MusicBrainzFile mbf = new MusicBrainzFile();
        Assertions.assertFalse(mbf.areRequiredFieldsValid());

        mbf.setMusicFileId(5L);
        Assertions.assertTrue(mbf.areRequiredFieldsValid());
    }
}
