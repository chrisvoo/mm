package repos;

import com.google.inject.Inject;
import exceptions.DbException;
import exceptions.ModelException;
import models.scanner.ScanOp;
import models.scanner.ScanOpError;
import models.scanner.ScanOpErrorSchema;
import models.scanner.ScanOpSchema;
import services.ScannerService;
import utils.Db;

import java.sql.*;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class ScannerRepo implements ScannerService {
    private static final Logger logger = Logger.getLogger(ScannerRepo.class.getName());
    @Inject private Db db;
    @Inject private ScanOpSchema schema;
    @Inject private ScanOpErrorSchema errorSchema;


    /**
     * Add a new result of a scanning operation.
     *
     * @param op op The operation
     * @return long The ScanOp primary key
     */
    @Override
    public ScanOp save(ScanOp op) {
        if (!op.isValid()) {
            throw new ModelException(op.getErrors(), op.getErrorCode());
        }

        String sql;

        if (op.getId() != null) { // update
            sql = schema.getSqlForUpdate();

            try (
              Connection conn = db.getConnection();
              PreparedStatement stmt = conn.prepareStatement(sql)
            ) {
                schema.setStatementValues(stmt, op);
                int affectedRows = stmt.executeUpdate();
                logger.fine("ScanOp.update, affected rows: " + affectedRows);

                if (affectedRows == 0) {
                    throw new DbException(
                      String.format("No scanop with id %d was found", op.getId()),
                      DbException.RESOURCE_NOT_FOUND
                    );
                }

                return op;
            } catch (SQLException e) {
                logger.severe(e.getMessage());
                throw new DbException("Updating the scanop failed", DbException.SQL_EXCEPTION);
            }

        } else { // create
            sql = schema.getSqlForInsert();
            int autoGeneratedKeys = Statement.RETURN_GENERATED_KEYS;

            try (
              Connection conn = db.getConnection();
              PreparedStatement stmt = conn.prepareStatement(sql, autoGeneratedKeys)
            ) {
                schema.setStatementValues(stmt, op);
                int affectedRows = stmt.executeUpdate();
                logger.fine("ScanOp.create, affected rows: " + affectedRows);

                if (affectedRows == 0) {
                    throw new DbException(
                      "Insert new scanop failed",
                      DbException.SQL_EXCEPTION
                    );
                }

                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        op.setId(generatedKeys.getLong(1));
                        return op;
                    } else {
                        throw new SQLException("Creating scanop failed, no ID obtained.");
                    }
                }
            } catch (SQLException e) {
                logger.severe(e.getMessage());
                throw new DbException("Cannot get the scanop", DbException.SQL_EXCEPTION);
            }
        }
    }

    /**
     * Add a new error
     *
     * @param error The error.
     */
    @Override
    public void addError(ScanOpError error) {
        String sql = errorSchema.getSqlForInsert();
        int autoGeneratedKeys = Statement.RETURN_GENERATED_KEYS;

        try (
          Connection conn = db.getConnection();
          PreparedStatement stmt = conn.prepareStatement(sql, autoGeneratedKeys)
        ) {
            errorSchema.setStatementValues(stmt, error);
            int affectedRows = stmt.executeUpdate();
            logger.fine("ScanOpError.create, affected rows: " + affectedRows);

            if (affectedRows == 0) {
                throw new DbException(
                  "Insert new scanopError failed",
                  DbException.SQL_EXCEPTION
                );
            }
        } catch (SQLException e) {
            logger.severe(e.getMessage());
            throw new DbException("Cannot add the scanopError", DbException.SQL_EXCEPTION);
        }
    }

    /**
     * The scan op corresponding to the specified id.
     *
     * @param id The scanOp primary key
     * @return ScanOp the operation instance
     */
    @Override
    public ScanOp getById(long id) {
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
            throw new DbException("Failed to get the scanop", DbException.SQL_EXCEPTION);
        }
    }

    @Override
    public long bulkSave(List<ScanOpError> errors, long scanOpId) {
        String sql = errorSchema.getSqlForInsert();
        logger.fine(sql);

        try (
          Connection conn = db.getConnection();
          PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            for (ScanOpError error : errors) {
                errorSchema.setStatementValues(stmt, error.setScanOpId(scanOpId));
                stmt.addBatch();
            }

            // A number greater than or equal to zero -- indicates that the command was processed
            // successfully and is an update count giving the number of rows in the database that
            // were affected by the command's execution
            return Arrays.stream(
              stmt.executeBatch()
            ).boxed().filter(r -> r == 1).count();
        } catch (SQLException e) {
            logger.severe(e.getMessage());
            throw new DbException("Updating ScanOpError bulk failed", DbException.SQL_EXCEPTION);
        }
    }
}