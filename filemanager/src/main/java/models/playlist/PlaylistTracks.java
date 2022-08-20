package models.playlist;

import models.Model;

import java.sql.Timestamp;
import java.util.List;

public class PlaylistTracks extends Model<PlaylistTracks> {
  private Long playlistId;
  private Long trackId;
  private Timestamp createdAt;

  public PlaylistTracks() {
    this.requiredFields = List.of("playlistId", "trackId");
  }

  public Long getPlaylistId() {
    return playlistId;
  }

  public PlaylistTracks setPlaylistId(Long playlistId) {
    this.playlistId = playlistId;
    return this;
  }

  public Long getTrackId() {
    return trackId;
  }

  public PlaylistTracks setTrackId(Long trackId) {
    this.trackId = trackId;
    return this;
  }

  public Timestamp getCreatedAt() {
    return createdAt;
  }

  public PlaylistTracks setCreatedAt(Timestamp createdAt) {
    this.createdAt = createdAt;
    return this;
  }
}