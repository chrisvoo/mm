package models;

import models.playlist.PlaylistTracks;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlaylistTracksTest {
  @Test
  public void requiredFieldsTest() {
    PlaylistTracks play = new PlaylistTracks();
    assertFalse(play.isValid());

    play.setPlaylistId(1L).setTrackId(1L);
    assertTrue(play.isValid());
  }
}