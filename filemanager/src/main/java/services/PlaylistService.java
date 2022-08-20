package services;

import models.playlist.Playlist;

import java.util.List;

public interface PlaylistService {
  /**
   * Returns a playlist (without tracks) by its id.
   * @param id Playlist's id
   * @return The playlist.
   */
  Playlist getById(long id);

  List<Playlist> getPlaylists(int howmany, String cursor, String direction);

  /**
   * Deletes a playlist
   * @param id Playlist's id
   * @return true on success.
   */
  boolean delete(long id);
}