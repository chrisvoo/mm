package services;

import models.playlist.Playlist;

import java.util.List;
import java.util.Map;

public interface PlaylistService {
  /**
   * Returns a playlist (with or without tracks) by its id.
   * @param id Playlist's id
   * @return The playlist.
   */
  Playlist getById(long id, boolean includeTracks);

  /**
   * Returns a playlist (without tracks) by its id.
   * @param id Playlist's id
   * @return The playlist.
   */
  Playlist getById(long id);

  /**
   * Creates or updates a playlist with tracks.
   * @param list
   * @return
   */
  Playlist save(Playlist list);

  /**
   *
   * @param params Params to retrieve the playlist info. It may contain the following fields:
   *               int howmany, String cursor, String direction, boolean includeTracks
   * @return A list of playlists with or without their tracks.
   */
  List<Playlist> getAll(Map<String, Object> params);

  /**
   * Deletes a playlist
   * @param id Playlist's id
   * @return true on success.
   */
  boolean delete(long id);
}