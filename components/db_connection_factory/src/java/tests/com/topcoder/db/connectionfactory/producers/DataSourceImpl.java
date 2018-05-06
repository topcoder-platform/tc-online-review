/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.db.connectionfactory.producers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;

import javax.sql.DataSource;


/**
 * This class is used to test the ReflectingConnectionProducer class. It implements the DataSource
 * interface, and all its methods is dummy for implementing the methods of DataSource.
 *
 * @author qiucx0161
 * @version 1.0
 */
public class DataSourceImpl implements DataSource {
    /**
     * Dummy getConnection method. It will return a Connection instance which is created via the
     * DataSource instance.
     *
     * @return The Connection instance.
     *
     * @throws SQLException if any error in creating Connection.
     */
    public Connection getConnection() throws SQLException {
        String jndiname = "java:comp/env/jdbc/test";

        try {
            DataSource dataSource = (DataSource) new InitialContext().lookup(jndiname);

            return dataSource.getConnection();
        } catch (Exception e) {
            throw new SQLException();
        }
    }

    /**
     * Dummy getConnection method. It will return a Connection instance which is created via the
     * DataSource instance.
     *
     * @param username the username to connection the database.
     * @param password the password to connection the database.
     *
     * @return The Connection instance.
     *
     * @throws SQLException if any error in creating Connection.
     */
    public Connection getConnection(String username, String password)
        throws SQLException {
        if (username.equals("test") && password.equals("test")) {
            String jndiname = "java:comp/env/jdbc/tcs";

            try {
                DataSource dataSource = (DataSource) new InitialContext().lookup(jndiname);

                return dataSource.getConnection(username, password);
            } catch (Exception e) {
                throw new SQLException();
            }
        } else {
            return null;
        }
    }

    /**
     * Dummy getLoginTimeout method.
     *
     * @return the time limited of logining.
     */
    public int getLoginTimeout() {
        // Do nothing
        return 0;
    }

    /**
     * Dummy getLogWriter method.
     *
     * @return the PrintWriter instance.
     *
     * @throws SQLException if any error occurs in get log write.
     */
    public PrintWriter getLogWriter() throws SQLException {
        try {
            // Do nothing
            return new PrintWriter(new FileOutputStream(new File("path")));
        } catch (Exception e) {
            throw new SQLException();
        }
    }

    /**
     * Dummy setLoginTimeout method.
     *
     * @param seconds the time limite of logining.
     */
    public void setLoginTimeout(int seconds) {
        // Do nothing
    }

    /**
     * Dummy setLogWriter method.
     *
     * @param out the PrintWriter instance.
     */
    public void setLogWriter(PrintWriter out) {
        // Do nothing
    }
}
