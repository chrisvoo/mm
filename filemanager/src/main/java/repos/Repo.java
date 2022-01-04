package repos;

import com.google.inject.Inject;
import exceptions.DbException;
import models.Schema;
import utils.Db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

public class Repo {
  @Inject protected Db db;
  private static final Logger logger = Logger.getLogger(Repo.class.getName());

  protected Long count(String sql) {
    logger.fine("COUNT SQL: " + sql);
    try (
      Connection conn = db.getConnection();
      PreparedStatement stmt = conn.prepareStatement(sql)
    ) {
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          return rs.getLong(1);
        }

        return null;
      }
    } catch (SQLException e) {
      logger.severe(e.getMessage());
      throw new DbException("Cannot get the file", DbException.SQL_EXCEPTION);
    }
  }

  /**
   * Simple delete of an entry by single primary key
   * @param schema An instance that extends Schema
   * @param id The primary key
   * @return Always true if the operation was successful. It throws an exception if the resource wasn't found.
   */
  protected boolean delete(Schema<?> schema, long id) {
    String sql = schema.getSqlForDelete();

    try (
      Connection conn = db.getConnection();
      PreparedStatement stmt = conn.prepareStatement(sql)
    ) {
      stmt.setLong(1, id);
      int affectedRows = stmt.executeUpdate();
      logger.fine("MusicFile.delete, affected rows: " + affectedRows);

      if (affectedRows == 0) {
        throw new DbException(
          String.format("No music file with id %d was found", id),
          DbException.RESOURCE_NOT_FOUND
        );
      }

      return true;
    } catch (SQLException e) {
      logger.severe(e.getMessage());
      throw new DbException("Cannot get the music file", DbException.SQL_EXCEPTION);
    }
  }
}