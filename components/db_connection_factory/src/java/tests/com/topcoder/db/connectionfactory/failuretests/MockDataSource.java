/**
 * Copyright (c) 2004, TopCoder, Inc. All rights reserved
 */
package com.topcoder.db.connectionfactory.failuretests;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

/**
 * Mock implementation of DataSource that only ever points at my personal copy of database (depends on the
 * configuration file) for unit testing purposes only.
 *
 * @author magicpig
 * @version 1.0
 */
public class MockDataSource implements DataSource {
    /**
     * Returns <code>null</code>.
     *
     * @return <code>null</code>
     * @throws SQLException should not throw
     */
    public Connection getConnection() throws SQLException {
        return null;
    }

    /**
     * Returns <code>null</code>.
     *
     * @param username ignored
     * @param password ignored
     *
     * @return a <code>null</code>
     *
     * @throws SQLException if it cannot find com.mysql.jdbc.Driver class
     */
    public Connection getConnection(final String username, final String password)
        throws SQLException {
        return null;
    }

    /**
     * Does nothing.
     *
     * @return 0 always
     */
    public int getLoginTimeout() {
        return 0;
    }

    /**
     * Does nothing.
     *
     * @return <code>null</code>
     */
    public java.io.PrintWriter getLogWriter() {
        return null;
    }

    /**
     * Does nothing.
     *
     * @param seconds ignored.
     */
    public void setLoginTimeout(final int seconds) {
    }

    /**
     * Do nothing.
     *
     * @param out ignored
     */
    public void setLogWriter(final java.io.PrintWriter out) {
    }
}

