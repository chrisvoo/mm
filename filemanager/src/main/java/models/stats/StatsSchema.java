package models.stats;

import models.Schema;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

/**
 * Stats for the system. Please notice that we do not include last_update in fields property. MySQL will take care to
 * insert the current timestamp for us
 */
public class StatsSchema extends Schema<Stats> {
  public static final String ID = "id";
  public static final String TOTAL_FILES = "total_files";
  public static final String TOTAL_BYTES = "total_bytes";
  public static final String LAST_UPDATE = "last_update";

  public static final String TABLE_NAME = "stats";

  public StatsSchema() {
    this.fields = List.of(
      StatsSchema.TOTAL_FILES, StatsSchema.TOTAL_BYTES, StatsSchema.LAST_UPDATE
    );
    this.primaryKeys = List.of(StatsSchema.ID);
  }

  /**
   * The MySQL table for this model
   *
   * @return The table's name.
   */
  @Override
  public String tableName() {
    return TABLE_NAME;
  }

  /**
   * Builds a model from a resultset
   *
   * @param rs The resultset.
   * @return The model
   * @throws SQLException An exception that provides information on a database access error or other errors.
   */
  @Override
  public Stats getModelFromResultSet(ResultSet rs) throws SQLException {
    return new Stats()
      .setId(this.getLong(rs, StatsSchema.ID))
      .setTotalFiles(rs.getInt(StatsSchema.TOTAL_FILES))
      .setTotalBytes(rs.getLong(StatsSchema.TOTAL_BYTES))
      .setLastUpdate(rs.getTimestamp(StatsSchema.LAST_UPDATE));
  }

  /**
   * Sets the prepared statement's placeholders.
   *
   * @param stmt     The prepared statement.
   * @param instance A model to be initialized.
   * @throws SQLException An exception that provides information on a database access error or other errors.
   */
  @Override
  public void setStatementValues(PreparedStatement stmt, Stats instance) throws SQLException {
    int index = 0;
    stmt.setInt(++index, instance.getTotalFiles());
    stmt.setLong(++index, instance.getTotalBytes());
    stmt.setTimestamp(++index,
      instance.getLastUpdate() != null
        ? instance.getLastUpdate()
        : Timestamp.from(Instant.now())
    );

    if (instance.getId() != null) {
      stmt.setLong(++index, instance.getId());
    }
  }
}