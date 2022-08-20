package models.playlist;

import models.Schema;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class PlaylistTracksSchema extends Schema<PlaylistTracks> {
  public static final String PLAYLIST_ID = "playlist_id";
  public static final String TRACK_ID = "track_id";

  public static final String CREATED_AT = "created_at";

  public static final String TABLE_NAME = "playlist";

  /**
   * The MySQL table for this model
   *
   * @return The table's name.
   */
  @Override
  public String tableName() {
    return TABLE_NAME;
  }

  public PlaylistTracksSchema() {
    this.fields = List.of(
      PlaylistTracksSchema.PLAYLIST_ID, PlaylistTracksSchema.TRACK_ID, PlaylistTracksSchema.CREATED_AT
    );
  }

  /**
   * Builds a model from a resultset
   *
   * @param rs The resultset.
   * @return The model
   * @throws SQLException An exception that provides information on a database access error or other errors.
   */
  @Override
  public PlaylistTracks getModelFromResultSet(ResultSet rs) throws SQLException {
    return new PlaylistTracks()
      .setPlaylistId(this.getLong(rs, PlaylistTracksSchema.PLAYLIST_ID))
      .setTrackId(rs.getLong(PlaylistTracksSchema.TRACK_ID))
      .setCreatedAt(rs.getTimestamp(PlaylistTracksSchema.CREATED_AT));
  }

  /**
   * Sets the prepared statement's placeholders.
   *
   * @param stmt     The prepared statement.
   * @param instance A model to be initialized.
   * @throws SQLException An exception that provides information on a database access error or other errors.
   */
  @Override
  public void setStatementValues(PreparedStatement stmt, PlaylistTracks instance) throws SQLException {
    int index = 0;
    stmt.setLong(++index, instance.getPlaylistId());
    stmt.setLong(++index, instance.getTrackId());
    stmt.setTimestamp(++index, instance.getCreatedAt());
  }
}