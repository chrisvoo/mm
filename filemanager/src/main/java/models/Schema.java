package models;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public abstract class Schema<T> {
    protected List<String> fieldsWithoutPK;
    private static final Logger logger = Logger.getLogger(Schema.class.getName());

    /**
     * The MySQL table for this model
     * @return The table's name.
     */
    public abstract String tableName();

    public String getSqlForInsert() {
        List<String> list = new ArrayList<>(Collections.nCopies(this.fieldsWithoutPK.size(), "%s"));
        String names = String.join(",", list);
        list.clear();

        list = new ArrayList<>(Collections.nCopies(this.fieldsWithoutPK.size(), "?"));
        String placeholders = String.join(",", list);

        String sql = String.format(
            "INSERT INTO %s (%s) VALUES (%s)",
            this.tableName(), names, placeholders
        );
        logger.fine(sql);
        return sql;
    }

    public String getSqlForUpdate() {
        List<String> list = new ArrayList<>(Collections.nCopies(this.fieldsWithoutPK.size(), "%s = ?"));
        String newValues = String.join(",", list);

        String sql = String.format(
            "UPDATE %s SET %s WHERE id = ?",
            this.tableName(), newValues
        );
        logger.fine(sql);
        return sql;
    }

    protected Short getYear(ResultSet rs, String field) throws SQLException {
        Date year = rs.getDate(field);
        if (year != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(year);
            return (short) cal.get(Calendar.YEAR);
        }

        return null;
    }

    protected void setYear(PreparedStatement stmt, Short year, int index) throws SQLException {
        if (year != null) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, year);
            stmt.setDate(index, (Date) cal.getTime());
        } else {
            stmt.setNull(index, Types.DATE);
        }
    }

    protected Long getLong(ResultSet rs, String field) throws SQLException {
        long num = rs.getLong(field);
        if (num == 0) {
            return null;
        }

        return num;
    }

    protected void setLong(PreparedStatement stmt, Long num, int index) throws SQLException {
        if (num != null) {
            stmt.setLong(index, num);
        } else {
            stmt.setNull(index, Types.BIGINT);
        }
    }

    protected Short getShort(ResultSet rs, String field) throws SQLException {
        short num = rs.getShort(field);
        if (num == 0) {
            return null;
        }

        return num;
    }

    protected void setShort(PreparedStatement stmt, Short num, int index) throws SQLException {
        if (num != null) {
            stmt.setShort(index, num);
        } else {
            stmt.setNull(index, Types.TINYINT);
        }
    }

    public abstract T getModelFromResultSet(ResultSet rs) throws SQLException;

    public abstract void setStatementValues(PreparedStatement stmt, T instance) throws SQLException;
}