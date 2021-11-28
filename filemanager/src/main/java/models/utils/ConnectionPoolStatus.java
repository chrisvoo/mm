package models.utils;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;

public class ConnectionPoolStatus {
    /**
     * The current number of active connections that have been allocated from this data source.
     */
    private int numActive;

    /**
     * The current number of idle connections that are waiting to be allocated from this data source.
     */
    private int numIdle;

    public ConnectionPoolStatus() {}

    public ConnectionPoolStatus(DataSource ds) {
        if (ds instanceof BasicDataSource bds) {
            this.numActive = bds.getNumActive();
            this.numIdle = bds.getNumIdle();
        }
    }

    public int getNumActive() {
        return numActive;
    }

    public ConnectionPoolStatus setNumActive(int numActive) {
        this.numActive = numActive;
        return this;
    }

    public int getNumIdle() {
        return numIdle;
    }

    public ConnectionPoolStatus setNumIdle(int numIdle) {
        this.numIdle = numIdle;
        return this;
    }
}
