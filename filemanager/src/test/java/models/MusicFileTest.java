package models;

import exceptions.ModelException;
import models.files.BitRateType;
import models.files.MusicFile;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;

import static org.junit.jupiter.api.Assertions.*;

public class MusicFileTest {
    @Test
    public void requiredFieldsTest() {
        MusicFile mf = new MusicFile();
        assertFalse(mf.isValid());

        mf.setAbsolutePath("a/path");
        assertTrue(mf.isValid());
    }

    @Test
    public void fromJsonEnumThrowsException(TestReporter rep) {
        String json = """
            {
                "id": 5,
                "absolutePath": "/a/path/file.mp3",
                "size": 3236545,
                "bitrate": 320,
                "bitRateType": "Caio",
                "duration": 158,
                "artist": "Lucio",
                "album": "Album di Lucio",
                "year": 1986,
                "title": "A tazzulella e cafè"
            }     
        """;
        assertThrows(ModelException.class, () -> {
            MusicFile.fromJson(json);
        });
    }

    @Test
    public void fromJsonTest(TestReporter rep) {
        String json = """
            {
                "id": 5,
                "absolutePath": "/a/path/file.mp3",
                "size": 3236545,
                "bitrate": 320,
                "bitRateType": "CBR",
                "duration": 158,
                "artist": "Lucio",
                "album": "Album di Lucio",
                "year": 1986,
                "title": "A tazzulella e cafè"
            }     
        """;
        MusicFile file = MusicFile.fromJson(json);

        assertTrue(file.isValid());

        file.setBitRateType(null);
        assertTrue(file.isValid());

        file.setBitRateType(BitRateType.CBR);

        file.setDuration(-15);
        assertFalse(file.isValid());
        assertEquals(1, file.getErrors().size());
        assertEquals("Must be greater than or equal to 0", file.getErrors().values().toArray()[0]);

//        rep.publishEntry(file.getErrors());
//        rep.publishEntry(file.getErrorCode().toString());
        file.setDuration(158);

        assertEquals(5, file.getId());
        assertEquals("/a/path/file.mp3", file.getAbsolutePath());
        assertEquals(320, file.getBitrate().shortValue());
        assertEquals(1986, file.getYear().shortValue());
        assertEquals("CBR", file.getBitRateType().name());
        assertEquals("Lucio", file.getArtist());
        assertEquals("Album di Lucio", file.getAlbum());
        assertEquals("A tazzulella e cafè", file.getTitle());
        assertEquals(1986, file.getYear().shortValue());
        assertEquals(3236545, file.getSize());
    }
}