package repos;

import com.google.inject.Inject;
import exceptions.DbException;
import exceptions.ModelException;
import models.band.Band;
import models.band.BandSchema;
import services.BandService;
import utils.Db;
import utils.logging.LoggerInterface;

import java.sql.*;

public class BandRepo extends Repo implements BandService {
    @Inject private LoggerInterface logger;
    @Inject private Db db;
    @Inject private BandSchema schema;

    /**
     * Return a band by its primary key
     *
     * @param id Band's primary key
     * @return Band
     */
    @Override
    public Band getById(long id) {
        String sql = String.format(
            "SELECT * FROM %s WHERE id = ?", schema.tableName()
        );
        try (
            Connection conn = db.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return schema.getModelFromResultSet(rs);
                }

                return null;
            }
        } catch (SQLException e) {
            logger.severe(e.getMessage());
            throw new DbException("Failed to get the band", DbException.SQL_EXCEPTION);
        }
    }

    /**
     * Upsert of a band into the database
     *
     * @param band The band.
     * @return The band
     */
    @Override
    public Band save(Band band) {
        if (!band.isValid()) {
            throw new ModelException(band.getErrors(), band.getErrorCode());
        }

        String sql;

        if (band.getId() != null) { // update
            sql = schema.getSqlForUpdate();

            try (
                Connection conn = db.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
            ) {
                schema.setStatementValues(stmt, band);
                int affectedRows = stmt.executeUpdate();
                logger.fine("Band.update, affected rows: " + affectedRows);

                if (affectedRows == 0) {
                    throw new DbException(
                        String.format("No band with id %d was found", band.getId()),
                        DbException.RESOURCE_NOT_FOUND
                    );
                }

                return band;
            } catch (SQLException e) {
                logger.severe(e.getMessage());
                throw new DbException("Updating the band failed", DbException.SQL_EXCEPTION);
            }

        } else { // create
            sql = schema.getSqlForInsert();
            int autoGeneratedKeys = Statement.RETURN_GENERATED_KEYS;

            try (
                Connection conn = db.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, autoGeneratedKeys)
            ) {
                schema.setStatementValues(stmt, band);
                int affectedRows = stmt.executeUpdate();
                logger.fine("Band.create, affected rows: " + affectedRows);

                if (affectedRows == 0) {
                    throw new DbException(
                        "Insert new band failed",
                        DbException.SQL_EXCEPTION
                    );
                }

                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        band.setId(generatedKeys.getLong(1));
                        return band;
                    } else {
                        throw new SQLException("Creating band failed, no ID obtained.");
                    }
                }
            } catch (SQLException e) {
                logger.severe(e.getMessage());
                throw new DbException("Cannot get the band", DbException.SQL_EXCEPTION);
            }
        }
    }

    /**
     * Deletes a band.
     *
     * @param id The band's id.
     * @return true if the operation was successful, false otherwise.
     */
    @Override
    public boolean delete(long id) {
        return this.delete(schema, id);
    }
}