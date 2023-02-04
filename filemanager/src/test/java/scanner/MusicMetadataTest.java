package scanner;

import models.files.MusicFile;
import org.junit.jupiter.api.Test;
import utils.eyeD3.EyeD3;

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

        MusicFile file = EyeD3.parse(path);
        assertNotNull(file);
        assertEquals(129, file.getDuration());
        assertEquals(5235428, file.getSize());
        assertEquals(path.toAbsolutePath().toString(), file.getAbsolutePath());
        assertEquals("Lives Of The Artists: Follow Me Down - Soundtrack", file.getAlbum());
        assertEquals("UNKLE", file.getArtist());
        assertEquals((short) 2010, file.getYear());
        assertNull(file.getGenre());
        assertEquals("Under The Ice (Scene edit)", file.getTitle());
      } catch (Exception e) {
        fail("Cannot read the metadata", e);
      }
    }
}