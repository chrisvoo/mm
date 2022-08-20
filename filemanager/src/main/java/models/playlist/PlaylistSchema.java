package models.playlist;

import com.google.gson.Gson;
import models.Schema;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class PlaylistSchema extends Schema<Playlist> {
  public static final String ID = "id";
  public static final String NAME = "name";
  public static final String IMAGE = "image";
  public static final String RATING = "rating";
  public static final String TAGS = "tags";
  public static final String CREATED_AT = "created_at";
  public static final String UPDATED_AT = "updated_at";

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

  public PlaylistSchema() {
    this.fields = List.of(
      PlaylistSchema.NAME, PlaylistSchema.IMAGE, PlaylistSchema.RATING,
      PlaylistSchema.TAGS, PlaylistSchema.CREATED_AT, PlaylistSchema.UPDATED_AT
    );
    this.primaryKeys = List.of(PlaylistSchema.ID);
  }

  /**
   * Builds a model from a resultset
   *
   * @param rs The resultset.
   * @return The model
   * @throws SQLException An exception that provides information on a database access error or other errors.
   */
  @Override
  public Playlist getModelFromResultSet(ResultSet rs) throws SQLException {
    return new Playlist()
      .setId(this.getLong(rs, PlaylistSchema.ID))
      .setName(rs.getString(PlaylistSchema.NAME))
      .setImage(this.getBytes(rs, PlaylistSchema.IMAGE))
      .setRating(this.getInt(rs, PlaylistSchema.RATING))
      .setTags(rs.getString(PlaylistSchema.TAGS))
      .setCreatedAt(rs.getTimestamp(PlaylistSchema.CREATED_AT))
      .setUpdatedAt(rs.getTimestamp(PlaylistSchema.UPDATED_AT));
  }

  /**
   * Sets the prepared statement's placeholders.
   *
   * @param stmt     The prepared statement.
   * @param instance A model to be initialized.
   * @throws SQLException An exception that provides information on a database access error or other errors.
   */
  @Override
  public void setStatementValues(PreparedStatement stmt, Playlist instance) throws SQLException {
    int index = 0;
    stmt.setString(++index, instance.getName());
    this.setBytes(stmt, instance.getImage(), ++index);
    this.setInt(stmt, instance.getRating(), ++index);
    stmt.setString(++index, new Gson().toJson(instance.getTags()));
    stmt.setTimestamp(++index, instance.getCreatedAt());
    stmt.setTimestamp(++index, instance.getUpdatedAt());

    if (instance.getId() != null) {
      stmt.setLong(++index, instance.getId());
    }
  }
}