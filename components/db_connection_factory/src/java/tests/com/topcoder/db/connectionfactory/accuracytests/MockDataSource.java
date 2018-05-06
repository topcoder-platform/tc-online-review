/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.db.connectionfactory.accuracytests;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;


/**
 * Mock implementation of DataSource that only ever points at my personal copy of database (depends on the
 * configuration file) for unit testing purposes only.
 *
 * @author cosherx
 * @version 1.0
 */
public class MockDataSource implements DataSource {

    /** The print writer maintained by get/setLogWriter. */
    private java.io.PrintWriter logWriter = null;

    /**
     * Returns a connection to the database.
     *
     * @return a Connection to the MySQL database
     *
     * @throws SQLException if it cannot find com.mysql.jdbc.Driver class
     */
    public Connection getConnection() throws SQLException {
        return AccuracyTestHelper.getConnection();
    }

    /**
     * Returns a connection to the database.
     *
     * @param username ignored
     * @param password ignored
     *
     * @return a Connection to the MySQL database
     *
     * @throws SQLException if it cannot find com.mysql.jdbc.Driver class
     */
    public Connection getConnection(final String username, final String password)
        throws SQLException {
        return getConnection();
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
     * @return the logWriter instance defined in setLogWriter.
     */
    public java.io.PrintWriter getLogWriter() {
        return logWriter;
    }

    /**
     * Does nothing.
     *
     * @param seconds ignored.
     */
    public void setLoginTimeout(final int seconds) {
    }

    /**
     * Keep the print writer parameter as the log writer for this object.
     *
     * @param out The logWriter that will be returned by getLogWriter.
     */
    public void setLogWriter(final java.io.PrintWriter out) {
        logWriter = out;
    }
}
