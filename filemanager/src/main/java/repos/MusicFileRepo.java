package repos;

import com.google.gson.Gson;
import com.google.inject.Inject;
import exceptions.DbException;
import exceptions.ModelException;
import models.files.MusicFile;
import models.files.MusicFileSchema;
import routes.utils.PaginatedResponse;
import routes.utils.Pagination;
import routes.utils.PaginationMetadata;
import services.MusicFileService;
import utils.FileUtils;
import utils.logging.LoggerInterface;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class MusicFileRepo extends Repo implements MusicFileService {
    @Inject private LoggerInterface logger;
    @Inject private MusicFileSchema schema;

    /**
     * Return a file by its primary key
     *
     * @param id file's primary key
     * @return MusicFile
     */
    @Override
    public MusicFile getById(long id) {
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
            throw new DbException("Cannot get the file", DbException.SQL_EXCEPTION);
        }
    }

    /**
     * Upsert of a file into the database
     *
     * @param file The file.
     * @return The file
     */
    @Override
    public MusicFile save(MusicFile file) {
        if (!file.isValid()) {
            throw new ModelException(file.getErrors(), file.getErrorCode());
        }

        String sql;

        if (file.getId() != null) { // update
            sql = schema.getSqlForUpdate();

            try (
              Connection conn = db.getConnection();
              PreparedStatement stmt = conn.prepareStatement(sql)
            ) {
                schema.setStatementValues(stmt, file);
                int affectedRows = stmt.executeUpdate();
                logger.fine("MusicFile.update, affected rows: " + affectedRows);

                if (affectedRows == 0) {
                    throw new DbException(
                      String.format("No file with id %d was found", file.getId()),
                      DbException.RESOURCE_NOT_FOUND
                    );
                }

                return file;
            } catch (SQLException e) {
                logger.severe(e.getMessage());
                throw new DbException("Updating the file failed", DbException.SQL_EXCEPTION);
            }

        } else { // create
            sql = schema.getSqlForInsert();
            int autoGeneratedKeys = Statement.RETURN_GENERATED_KEYS;

            try (
              Connection conn = db.getConnection();
              PreparedStatement stmt = conn.prepareStatement(sql, autoGeneratedKeys)
            ) {
                schema.setStatementValues(stmt, file);
                int affectedRows = stmt.executeUpdate();
                logger.fine("MusicFile.create, affected rows: " + affectedRows);

                if (affectedRows == 0) {
                    throw new DbException(
                      "Insert new file failed",
                      DbException.SQL_EXCEPTION
                    );
                }

                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        file.setId(generatedKeys.getLong(1));
                        return file;
                    } else {
                        throw new SQLException("Creating file failed, no ID obtained.");
                    }
                }
            } catch (SQLException e) {
                logger.severe(e.getMessage());
                throw new DbException("Cannot get the file", DbException.SQL_EXCEPTION);
            }
        }
    }

    public void upsert(MusicFile file) {
        String sql = schema.getSqlForUpsert();
        logger.fine(sql);

        try (
          Connection conn = db.getConnection();
          PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            schema.setStatementValuesForUpsert(stmt, file);
            int affectedRows = stmt.executeUpdate();
            logger.fine("MusicFile.upsert, affected rows: " + affectedRows);
        } catch (SQLException e) {
            logger.severe(e.getMessage());
            throw new DbException("Updating the file failed", DbException.SQL_EXCEPTION);
        }
    }

    @Override
    public long bulkSave(List<MusicFile> files) {
        String sql = schema.getSqlForUpsert();
        logger.fine(sql);
        try (
          Connection conn = db.getConnection();
          PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            schema.setStatementValuesForBatch(stmt, files);
            // A number greater than or equal to zero -- indicates that the command was processed
            // successfully and is an update count giving the number of rows in the database that
            // were affected by the command's execution
            return Arrays.stream(
              stmt.executeBatch()
            ).boxed().filter(r -> r == 1).count();
        } catch (SQLException e) {
            logger.severe(e.getMessage() + ", files: " + new Gson().toJson(files));
            throw new DbException("Updating the file failed", DbException.SQL_EXCEPTION);
        }
    }

    /**
     * Delete a file.
     *
     * @param id The file's id.
     * @return true if the operation was successful, false otherwise.
     */
    @Override
    public boolean delete(long id) {
        return this.delete(schema, id);
    }

    /**
     * Deletes the entries represented by this path. If it's a file, the WHERE condition will be a strict match, otherwise
     * if it's a directory, it will be used a LIKE condition.
     *
     * @param path The resource to be deleted.
     */
    @Override
    public void delete(Path path) {
        String sql = "";
        String value = "";
        if (path.toString().toLowerCase().endsWith(".mp3")) {
            sql = String.format(
              "DELETE FROM %s WHERE %s = ?",
              this.schema.tableName(),
              MusicFileSchema.ABSOLUTE_PATH
            );
            value = path.toAbsolutePath().toString();
        } else {
            sql = String.format(
              "DELETE FROM %s WHERE %s LIKE ?",
              this.schema.tableName(),
              MusicFileSchema.ABSOLUTE_PATH
            );
            value = path.toAbsolutePath().toString() + "%";
        }

        logger.fine(sql);
        try (
          Connection conn = db.getConnection();
          PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, value);

            int affectedRows = stmt.executeUpdate();
            logger.info("MusicFile.deleteAll, affected rows: " + affectedRows);
        } catch (SQLException e) {
            String msg = String.format(
              "Failure for deleteAll, path: %s, message: %s, SQL: %s", path, e.getMessage(), sql
            );
            logger.severe(msg);
            throw new DbException("Deleting the file failed", DbException.SQL_EXCEPTION);
        }
    }

    /**
     * Deletes both the physical file and the related database record.<br/>
     * <b>Note: it requires that the resource still exists! Useful if the delete operation starts from
     * the frontend</b>
     *
     * @param resource The absolute path to the file
     */
    @Override
    public void physicalDelete(Path resource) throws IOException {
        if (!Files.exists(resource)) {
            return;
        }

        List<Path> paths = FileUtils.listMP3Files(resource);
        String sql = String.format(
          "DELETE FROM %s WHERE %s IN (%s)",
          this.schema.tableName(),
          MusicFileSchema.ABSOLUTE_PATH,
          paths.stream()
             .map(v -> "?")
             .collect(Collectors.joining(", "))
        );

        // 1. Delete the record(s) from the database
        logger.fine(sql);
        try (
          Connection conn = db.getConnection();
          PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            for (int i = 0, index = 1; i < paths.size(); i++, index++) {
                stmt.setString(index, paths.get(i).toString());
            }

            int affectedRows = stmt.executeUpdate();
            logger.info("MusicFile.deleteAll, affected rows: " + affectedRows);
        } catch (SQLException e) {
            String msg = String.format(
              "Failure for deleteAll, paths size: %d, message: %s, SQL: %s", paths.size(), e.getMessage(), sql
            );
            logger.severe(msg);
            throw new DbException("Updating the file failed", DbException.SQL_EXCEPTION);
        }

        // 2. Delete the file(s) and eventual directories
        FileUtils.deleteResource(resource);
    }

    /**
     * A pagination-enabled list of music files.
     *
     * @param pagination How to retrieve the results.
     * @return The response with pagination info.
     */
    @Override
    public PaginatedResponse<MusicFile> getAll(Pagination pagination) {
        StringJoiner joiner = new StringJoiner(" ");
        joiner.add(String.format("SELECT * FROM %s", schema.tableName()));

        String cursor = pagination.getCursor(true);
        long value = 0;
        PaginationMetadata meta = new PaginationMetadata();

        /* Filters should be placed here */

        /* end filters */

        String countSql = joiner.toString().replace("*", "COUNT(*)");

        if (cursor != null) {
            value = Long.parseLong(cursor);
            String condition = (pagination.getSortDir().equals("desc")
                                ? "WHERE %s <= ?"
                                : "WHERE %s >= ?").formatted(pagination.getSortBy());
            joiner.add(condition);
        } else {
            // let's limit the count to the first page, the frontend will retain the value and we avoid useless
            // queries for the next pages
            meta.setTotalCount(this.count(countSql));
        }

        joiner.add("ORDER BY %s %s".formatted(pagination.getSortBy(), pagination.getSortDir()));

        // plus 1 just to know if there are more data
        joiner.add("LIMIT " + (pagination.getCount() + 1));
        logger.fine("GET ALL SQL: " + joiner);

        try (
          Connection conn = db.getConnection();
          PreparedStatement stmt = conn.prepareStatement(joiner.toString())
        ) {
            if (cursor != null) {
                stmt.setLong(1, value);
            }

            List<MusicFile> items = new ArrayList<>();

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    items.add(schema.getModelFromResultSet(rs));
                }

                PaginatedResponse<MusicFile> response = new PaginatedResponse<>();
                response.setItems(items);
                meta.setHasMoreData(items.size() > pagination.getCount());

                if (meta.hasMoreData()) {
                    long id = items.remove(pagination.getCount()).getId();
                    meta.setNextCursor(id + "");
                }

                return response.setMetadata(meta);
            }
        } catch (SQLException e) {
            logger.severe(e.getMessage() + ", SQL: " + joiner);
            throw new DbException("Cannot get list of files", DbException.SQL_EXCEPTION);
        }
    }
}