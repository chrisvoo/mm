package utils;

import exceptions.DbException;
import exceptions.FileManagerException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Db {
    private final EnvVars envVars;

    public Db(EnvVars envVars) throws FileManagerException {
        this.envVars = envVars;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new FileManagerException("Missing MySQL driver", DbException.MISSING_DRIVER);
        }
    }

    /**
     * Returns a new connection
     * @return Connection
     * @throws DbException if the connection fails
     */
    public Connection getConnection() {
        try {
            return DriverManager.getConnection(
                    envVars.getConnectionString(),
                    envVars.getMysqlUser(),
                    envVars.getMysqlPass()
            );
        } catch (SQLException e) {
            throw new FileManagerException(
                String.format("Can't connect: %s, [%d]", e.getMessage(), e.getErrorCode()),
                DbException.CONNECTION_ERROR
            );
        }
    }
}
