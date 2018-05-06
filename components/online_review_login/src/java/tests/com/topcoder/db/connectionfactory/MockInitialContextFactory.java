/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */



package com.topcoder.db.connectionfactory;

import java.io.BufferedInputStream;
import java.io.FileInputStream;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.util.Hashtable;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;

import javax.sql.DataSource;

/**
 * Extension of initial context that always only ever returns a DataSource to connect to the database used for personal
 * unit testing.
 */
class MockContext extends InitialContext {

    /**
     * Default ctor tells the parent to only lazily instnatiate itself.
     *
     * @throws NamingException
     *             if InitialContext does
     */
    MockContext() throws NamingException {
        super(true);
    }

    /**
     * In this simple mock implementation, this method only ever returns a DataSource object pointing at the database
     * used for unit testing.
     *
     * @param key
     *            Ignored
     * @return a DataSource object.
     */
    public Object lookup(final String key) throws NamingException {
        if (key.equals("throwNamingException")) {
            throw new NamingException();
        }

        return new MockDataSource();
    }
}


/**
 * Mock implementation of DataSource that only ever points at my personal copy of database (depends on the configuration
 * file) for unit testing purposes only.
 */
class MockDataSource implements DataSource {

    /**
     * The the configuration file to load the necessary configuration values to create a connection.
     */
    private static final String DB_CONFIG_FILE = "test_files/db.properties";

    /** The print writer maintained by get/setLogWriter. */
    private java.io.PrintWriter logWriter = null;

    /**
     * Returns a connection to the database.
     *
     * @return a Connection to the MySQL database
     * @throws SQLException
     *             if it cannot find com.mysql.jdbc.Driver class
     */
    public Connection getConnection() throws SQLException {
        Connection conn = null;

        try {

            // load the properties from configuration file
            Properties prop = new Properties();
            FileInputStream fs = new FileInputStream(DB_CONFIG_FILE);
            BufferedInputStream bs = new BufferedInputStream(fs);

            prop.load(bs);

            String dbdriver = prop.getProperty("dbdriver");
            String dburl = prop.getProperty("dburl");
            String dbuser = prop.getProperty("dbuser");
            String dbpwd = prop.getProperty("dbpassword");

            fs.close();
            bs.close();

            // load the driver class
            Class.forName(dbdriver);

            conn = DriverManager.getConnection(dburl, dbuser, dbpwd);
        } catch (Exception e) {
            System.err.println("error here - " + e);

            throw new SQLException(e.getMessage());
        }

        return conn;
    }

    /**
     * Returns a connection to the database.
     *
     * @param username
     *            ignored
     * @param password
     *            ignored
     * @return a Connection to the MySQL database
     * @throws SQLException
     *             if it cannot find com.mysql.jdbc.Driver class
     */
    public Connection getConnection(final String username, final String password) throws SQLException {
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
     * @param seconds
     *            ignored.
     */
    public void setLoginTimeout(final int seconds) {}

    /**
     * Keep the print writer parameter as the log writer for this object.
     *
     * @param out
     *            The logWriter that will be returned by getLogWriter.
     */
    public void setLogWriter(final java.io.PrintWriter out) {
        logWriter = out;
    }
}


/**
 * Mock initial context factory for unit testing purposes only. Currently the only thing in the context is the driver
 * for a configuration file, required for the IDGenerator.
 *
 * @author qiucx0161
 * @version 1.0
 */
public class MockInitialContextFactory implements InitialContextFactory {

    /**
     * The Hashtable used to get InitialContext.
     *
     * @since 1.1
     */
    public static Hashtable env;

    /**
     * Returns an instance of our mock context that merely points at my MySql database for unit testing purposes only.
     *
     * @param parm1
     *            ignored.
     * @return an initial context that points at the MySql database.
     * @throws NamingException
     *             Only if InitialContext's constructor does.
     */
    public Context getInitialContext(final Hashtable parm1) throws NamingException {
        env = parm1;

        return new MockContext();
    }
}
