package models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

    public abstract T getModelFromResultSet(ResultSet rs) throws SQLException;

    public abstract void setStatementValues(PreparedStatement stmt, T instance) throws SQLException;
}
