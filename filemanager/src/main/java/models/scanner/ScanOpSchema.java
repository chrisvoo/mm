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

    public ScanOpSchema() {
        this.fields = List.of(
            ScanOpSchema.STARTED, ScanOpSchema.TOTAL_FILES_SCANNED, ScanOpSchema.TOTAL_FILES_INSERTED,
            ScanOpSchema.TOTAL_ELAPSED_TIME, ScanOpSchema.TOTAL_BYTES, ScanOpSchema.FINISHED
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
            .setTotalFilesScanned(rs.getInt(ScanOpSchema.TOTAL_FILES_SCANNED))
            .setTotalFilesInserted(rs.getInt(ScanOpSchema.TOTAL_FILES_INSERTED))
            .setTotalElapsedTime(rs.getShort(ScanOpSchema.TOTAL_ELAPSED_TIME))
            .setTotalBytes(rs.getLong(ScanOpSchema.TOTAL_BYTES))
            .setFinished(rs.getTimestamp(ScanOpSchema.FINISHED));
    }

    public void setStatementValues(PreparedStatement stmt, ScanOp instance) throws SQLException {
        int index = 0;
        this.setTimestamp(stmt, instance.getStarted(), ++index);
        stmt.setInt(++index, instance.getTotalFilesScanned());
        stmt.setInt(++index, instance.getTotalFilesInserted());
        stmt.setShort(++index, instance.getTotalElapsedTime());
        stmt.setLong(++index, instance.getTotalBytes());
        this.setTimestamp(stmt, instance.getFinished(), ++index);

        if (instance.getId() != null) {
            stmt.setLong(++index, instance.getId());
        }
    }
}