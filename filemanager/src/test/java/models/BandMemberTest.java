package models;

import models.band.BandMember;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

public class BandMemberTest {

    @Test
    public void requiredFieldsTest() {
        BandMember bm = new BandMember();
        Assertions.assertFalse(bm.areRequiredFieldsValid());

        bm.setBandId(5L);
        bm.setMusicianId(5L);
        Assertions.assertTrue(bm.areRequiredFieldsValid());
    }
}
