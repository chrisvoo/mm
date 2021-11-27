package utils;

import exceptions.DbException;
import exceptions.FileManagerException;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * The single instance of this class is managed by the dependency injection made with Guice.
 * No need for static fields/methods here.
 */
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

    private BasicDataSource getDataSource() {
        BasicDataSource ds = new BasicDataSource();
        ds.setUrl(envVars.getConnectionString());
        ds.setUsername(envVars.getMysqlUser());
        ds.setPassword(envVars.getMysqlPass());
        ds.setDefaultAutoCommit(true);
        ds.setAutoCommitOnReturn(true);
        ds.setDefaultQueryTimeout(5);
        ds.setInitialSize(0); // The initial number of connections that are created when the pool is started.
        ds.setMaxTotal(20);   // The maximum number of active connections that can be allocated from this pool at the same time, or negative for no limit.
        ds.setMaxIdle(20); // The maximum number of connections that can remain idle in the pool, without extra ones being released, or negative for no limit.
        ds.setMinIdle(0); // The minimum number of connections that can remain idle in the pool, without extra ones being created, or zero to create none.
        ds.setMaxWaitMillis(-1); // The maximum number of milliseconds that the pool will wait (when there are no available connections) for a connection to be returned before throwing an exception, or -1 to wait indefinitely.
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");

        return ds;
    }

    /**
     * Returns a new connection
     * @return Connection
     * @throws DbException if the connection fails
     */
    public Connection getConnection() {
        try {
            return this.getDataSource().getConnection();
        } catch (SQLException e) {
            throw new DbException(
                String.format("Can't connect: %s, [%d]", e.getMessage(), e.getErrorCode()),
                DbException.CONNECTION_ERROR
            );
        }
    }

    public void close() {
        if (!this.getDataSource().isClosed()) {
            try {
                this.getDataSource().close();
            } catch (SQLException e) {
                throw new DbException(
                    String.format("Can't close connection: %s, [%d]", e.getMessage(), e.getErrorCode()),
                    DbException.CLOSING_CONNECTION_ERROR
                );
            }
        }
    }
}
