package src;

import com.google.inject.Guice;
import com.google.inject.Injector;
import exceptions.DbException;
import utils.Db;
import utils.FileManagerModule;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

public class DbHelper {
    private static Db db;
    private static final Logger logger = Logger.getLogger(DbHelper.class.getName());

    /**
     * Removes all records from a table.
     * @param table The table name.
     */
    public static void emptyTable(String table) {
        if (db == null) {
            Injector injector = Guice.createInjector(new FileManagerModule());
            db = injector.getInstance(Db.class);
        }

        String sql = String.format("DELETE FROM %s", table);

        try (
            Connection conn = db.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
        ) {
            int affectedRows = stmt.executeUpdate();
            logger.fine(String.format("Deleted %d rows from %s", affectedRows, table));
        } catch (SQLException e) {
            logger.severe(e.getMessage());
            throw new DbException(
                String.format("Cannot empty table %s", table),
                DbException.SQL_EXCEPTION
            );
        }
    }
}