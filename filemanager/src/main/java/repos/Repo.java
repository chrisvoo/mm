package repos;

import com.google.inject.Inject;
import exceptions.DbException;
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
    try (
      Connection conn = db.getConnection();
      PreparedStatement stmt = conn.prepareStatement(sql);
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
}