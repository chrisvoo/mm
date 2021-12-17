package models.scanner;

import models.Schema;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ScanOpErrorSchema extends Schema<ScanOpError> {
    public static final String ID = "id";
    public static final String SCAN_OP_ID = "scan_op_id";
    public static final String MESSAGE = "message";
    public static final String CREATED_AT = "created_at";

    public ScanOpErrorSchema() {
        this.fields = List.of(
            ScanOpErrorSchema.SCAN_OP_ID, ScanOpErrorSchema.MESSAGE, ScanOpErrorSchema.CREATED_AT
        );
        this.primaryKeys = List.of(ScanOpErrorSchema.ID);
    }

    public String tableName() {
        return "scan_ops_errors";
    }

    public ScanOpError getModelFromResultSet(ResultSet rs) throws SQLException {
        return new ScanOpError()
            .setId(this.getLong(rs, ScanOpErrorSchema.ID))
            .setScanOpId(rs.getLong(ScanOpErrorSchema.SCAN_OP_ID))
            .setMessage(rs.getString(ScanOpErrorSchema.MESSAGE))
            .setCreatedAt(rs.getTimestamp(ScanOpErrorSchema.CREATED_AT));
    }

    public void setStatementValues(PreparedStatement stmt, ScanOpError instance) throws SQLException {
        int index = 0;
        stmt.setLong(++index, instance.getScanOpId());
        stmt.setString(++index, instance.getMessage());
        this.setTimestamp(stmt, instance.getCreatedAt(), ++index);

        if (instance.getId() != null) {
            stmt.setLong(++index, instance.getId());
        }
    }
}