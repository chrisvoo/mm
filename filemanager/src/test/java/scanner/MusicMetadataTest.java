package scanner;

import com.mpatric.mp3agic.Mp3File;
import models.files.BitRateType;
import models.files.MusicFile;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class MusicMetadataTest {

    /**
     * It can read metadata from an MP3
     */
    @Test
    public void canReadMetadataTest() {
      try {
        Path path = Paths.get(Objects.requireNonNull(
          getClass().getResource("/tree/Under The Ice (Scene edit).mp3")
        ).toURI().getPath());
        MusicFile musicFile = new MusicFile(new Mp3File(path));
        assertEquals(BitRateType.CBR, musicFile.getBitRateType());
        assertEquals(5235428, musicFile.getSize());
        assertEquals(320, musicFile.getBitrate());
        assertTrue(
          musicFile.getAbsolutePath().contains(
            File.separator + "tree" + File.separator +
              "Under The Ice (Scene edit).mp3"
          )
        );
        assertEquals(129, musicFile.getDuration());
        assertEquals("Lives Of The Artists: Follow Me Down - Soundtrack", musicFile.getAlbum());
        assertEquals("image/jpeg", musicFile.getAlbumImageMimeType());
        assertNull(musicFile.getGenre());
        assertEquals("UNKLE", musicFile.getArtist());
        assertEquals("Under The Ice (Scene edit)", musicFile.getTitle());
        assertEquals(2010, musicFile.getYear().shortValue());
      } catch (Exception e) {
        fail("Cannot read the metadata", e);
      }
    }
}