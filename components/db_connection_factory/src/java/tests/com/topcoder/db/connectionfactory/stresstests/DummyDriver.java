/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.db.connectionfactory.stresstests;

import java.lang.reflect.InvocationTargetException;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;

import java.util.Properties;


/**
 * <p>
 * Dummy JDBC 1.0 driver used in this test. Whenever it is called to create a connection, a
 * <code>DummyConnection</code> is created.
 * </p>
 *
 * @author magicpig
 * @version 1.0
 */
public final class DummyDriver implements Driver {
    /**
     * <p>
     * The instance registered to <code>DriverManager</code>.
     * </p>
     */
    private static final DummyDriver DRIVER_INSTANCE;

    /**
     * <p>
     * Register this dummy driver to JDBC 1.0 <code>DriverManager</code>.
     * </p>
     */
    static {
        DRIVER_INSTANCE = new DummyDriver();

        try {
            DriverManager.registerDriver(DRIVER_INSTANCE);
        } catch (SQLException e) {
            // ignore SQLException
        }
    }

    /**
     * <p>
     * Create a new instance of <code>DummyDriver</code>.
     * </p>
     */
    private DummyDriver() {
    }

    /**
     * <p>
     * Get the instance registered to <code>DriverManager</code>.
     * </p>
     *
     * @return instance registered to <code>DriverManager</code>
     */
    public static DummyDriver getInstance() {
        return DRIVER_INSTANCE;
    }

    /**
     * <p>
     * Get the major version of this driver. Always 1.
     * </p>
     *
     * @return 1
     */
    public int getMajorVersion() {
        return 1;
    }

    /**
     * <p>
     * Get the minor version of this driver. Always 0.
     * </p>
     *
     * @return 0
     */
    public int getMinorVersion() {
        return 0;
    }

    /**
     * <p>
     * Reports whether this driver is a genuine JDBC Compliant driver. Always <code>false</code>.
     * </p>
     *
     * @return <code>false</code>
     */
    public boolean jdbcCompliant() {
        return false;
    }

    /**
     * <p>
     * Decide whether a JDBC URL can be accepted by this dummy driver.
     * </p>
     *
     * @param url the JDBC URL to be examined
     *
     * @return <code>true</code> if the url starts with &quot;dummy:&quot;; <code>false</code> otherwise
     */
    public boolean acceptsURL(String url) {
        return url.startsWith("dummy:");
    }

    /**
     * <p>
     * Get a connection from the given JDBC URL.
     * </p>
     *
     * @param url the JDBC URL to get a connection
     * @param info properties, username and password are appended as the information if available
     *
     * @return a <code>DummyConnection</code> instance with the given URL as the stored information
     */
    public Connection connect(String url, Properties info) {
        if (!url.startsWith("dummy:")) {
            return null;
        }

        if (info.containsKey("user") || info.containsKey("password")) {
            String username = (String) info.get("user");
            String password = (String) info.get("password");
            url += ("|" + ((username == null) ? "!NULL!" : username) + "|"
            + ((password == null) ? "!NULL!" : password));
        }

        try {
            return (Connection) TestHelper.DUMMY_CONNECTION_IMPL.newInstance(new Object[] {url});
        } catch (InstantiationException e) {
            return null;
        } catch (IllegalAccessException e) {
            return null;
        } catch (InvocationTargetException e) {
            return null;
        }
    }

    /**
     * <p>
     * Dummy implementation of <code>Driver</code>.
     * </p>
     *
     * @param url dummy argument
     * @param info dummy argument
     *
     * @return empty array
     */
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) {
        return new DriverPropertyInfo[0];
    }
}

