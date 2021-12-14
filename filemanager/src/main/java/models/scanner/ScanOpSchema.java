package models.scanner;

import models.Schema;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ScanOpSchema extends Schema<ScanOp> {
    public static final String ID = "id";
    public static final String STARTED = "started";
    public static final String TOTAL_FILES_SCANNED = "total_files_scanned";
    public static final String TOTAL_FILES_INSERTED = "total_files_inserted";
    public static final String TOTAL_ELAPSED_TIME = "total_elapsed_time";
    public static final String TOTAL_BYTES = "total_bytes";
    public static final String FINISHED = "finished";
    public static final String HAS_ERRORS = "has_errors";

    public ScanOpSchema() {
        this.fields = List.of(
            ScanOpSchema.STARTED, ScanOpSchema.TOTAL_FILES_SCANNED, ScanOpSchema.TOTAL_FILES_INSERTED,
            ScanOpSchema.TOTAL_ELAPSED_TIME, ScanOpSchema.TOTAL_BYTES, ScanOpSchema.FINISHED,
            ScanOpSchema.HAS_ERRORS
        );
        this.primaryKeys = List.of(ScanOpSchema.ID);
    }

    public String tableName() {
        return "scan_ops";
    }


    public ScanOp getModelFromResultSet(ResultSet rs) throws SQLException {
        return new ScanOp()
            .setId(this.getLong(rs, ScanOpSchema.ID))
            .setStarted(rs.getTimestamp(ScanOpSchema.STARTED))
            .setTotalFilesScanned(this.getInt(rs, ScanOpSchema.TOTAL_FILES_SCANNED))
            .setTotalFilesInserted(this.getInt(rs, ScanOpSchema.TOTAL_FILES_INSERTED))
            .setTotalElapsedTime(this.getShort(rs, ScanOpSchema.TOTAL_ELAPSED_TIME))
            .setTotalBytes(this.getLong(rs, ScanOpSchema.TOTAL_BYTES))
            .setFinished(rs.getTimestamp(ScanOpSchema.FINISHED))
            .setHasErrors(rs.getBoolean(ScanOpSchema.HAS_ERRORS));
    }

    public void setStatementValues(PreparedStatement stmt, ScanOp instance) throws SQLException {
        int index = 0;
        this.setTimestamp(stmt, instance.getStarted(), ++index);
        this.setInt(stmt, instance.getTotalFilesScanned(), ++index);
        this.setInt(stmt, instance.getTotalFilesInserted(), ++index);
        this.setShort(stmt, instance.getTotalElapsedTime(), ++index);
        this.setLong(stmt, instance.getTotalBytes(), ++index);
        this.setTimestamp(stmt, instance.getFinished(), ++index);

        stmt.setBoolean(++index, instance.getHasErrors());

        if (instance.getId() != null) {
            stmt.setLong(++index, instance.getId());
        }
    }
}