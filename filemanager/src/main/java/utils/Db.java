package utils;

import com.google.inject.Inject;
import exceptions.DbException;
import exceptions.FileManagerException;
import models.utils.ConnectionPoolStatus;
import org.apache.commons.dbcp2.BasicDataSource;
import utils.logging.LoggerInterface;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * The single instance of this class is managed by the dependency injection made with Guice.
 * The dataSource is static and final to be thread-safe
 */

public final class Db {
    private static EnvVars envVars;
    private final static BasicDataSource dataSource = Db.getDataSource();

    @Inject
    private LoggerInterface logger;

    public Db() throws FileManagerException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new FileManagerException("Missing MySQL driver", DbException.MISSING_DRIVER);
        }
    }

    /**
     * Creates and sets the connection pool properties
     * @return BasicDataSource
     */
    private static BasicDataSource getDataSource() {
        EnvVars envVars = new EnvVars();
        envVars.loadEnvVars();

        BasicDataSource ds = new BasicDataSource();
        // ds.setUrl("jdbc:mysql://129.9.100.16:3306/test?useUnicode=true&characterEncoding=utf-8&useSSL=false");
        ds.setUrl(envVars.getConnectionString());
        ds.setUsername(envVars.getMysqlUser());
        ds.setPassword(envVars.getMysqlPass());
        ds.setDefaultAutoCommit(true);
        ds.setAutoCommitOnReturn(true);
        ds.setConnectionProperties("useUnicode=true;characterEncoding=utf8;rewriteBatchedStatements=true;");
        ds.setDefaultQueryTimeout(1300);
        ds.setInitialSize(120); // The initial number of connections that are created when the pool is started.
        ds.setMaxTotal(-1);   // The maximum number of active connections that can be allocated from this pool at the same time, or negative for no limit.
        ds.setMaxIdle(-1); // The maximum number of connections that can remain idle in the pool, without extra ones being released, or negative for no limit.
        ds.setMinIdle(120); // The minimum number of connections that can remain idle in the pool, without extra ones being created, or zero to create none.
        ds.setMaxWaitMillis(-1); // The maximum number of milliseconds that the pool will wait (when there are no available connections) for a connection to be returned before throwing an exception, or -1 to wait indefinitely.
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setConnectionInitSqls(List.of("set names utf8mb4;" /*, "set names utf8;" */));

        System.out.println("Connection pool has been created");

        return ds;
    }

    /**
     * Returns the status of the connection pool
     * @return The connection pool's status.
     */
    public ConnectionPoolStatus getConnectionPoolStatus() {
        return new ConnectionPoolStatus(Db.dataSource);
    }

    /**
     * Returns a new connection
     * @return Connection
     * @throws DbException if the connection fails
     */
    public Connection getConnection() {
        try {
            return Db.dataSource.getConnection();
        } catch (SQLException e) {
            throw new DbException(
                String.format("Can't connect: %s, [%d]", e.getMessage(), e.getErrorCode()),
                DbException.CONNECTION_ERROR
            );
        }
    }

    /**
     * Closes the connection pool
     */
    public void close() {
        if (!Db.dataSource.isClosed()) {
            try {
                Db.dataSource.close();
            } catch (SQLException e) {
                throw new DbException(
                    String.format("Can't close connection: %s, [%d]", e.getMessage(), e.getErrorCode()),
                    DbException.CLOSING_CONNECTION_ERROR
                );
            }
        }
    }
}