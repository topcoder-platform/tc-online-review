/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.db.connectionfactory.stresstests;

import java.io.PrintWriter;

import java.lang.reflect.InvocationTargetException;

import java.sql.Connection;

import javax.sql.DataSource;


/**
 * <p>
 * Dummy data source used in the test. Whenever it is requested to get a connection, a <code>DummyConnection</code>
 * instance is returned. This data source is also referenceable, so that it can be stored by JNDI file system
 * provider.
 * </p>
 *
 * @author magicpig
 * @version 1.0
 */
public final class DummyDataSource implements DataSource {
    /**
     * <p>
     * Information stored in this data source.
     * </p>
     */
    private String information;

    /**
     * <p>
     * Create a new instance of <code>DataSource</code> specifying the information stored in this data source.
     * </p>
     *
     * @param info information stored in this data source
     */
    public DummyDataSource(String info) {
        information = info;
    }

    /**
     * <p>
     * Create a new instance of <code>DataSource</code>. The information is &quot;DEFAULT&quot;.
     * </p>
     */
    public DummyDataSource() {
        information = "DEFAULT";
    }

    /**
     * <p>
     * Get the information stored in this data source.
     * </p>
     *
     * @return information stored in this data source
     */
    public String getInformation() {
        return information;
    }

    /**
     * <p>
     * Dummy implementation of <code>DataSource</code>.
     * </p>
     *
     * @return 0
     */
    public int getLoginTimeout() {
        return 0;
    }

    /**
     * <p>
     * Dummy implementation of <code>DataSource</code>.
     * </p>
     *
     * @param seconds dummy argument
     */
    public void setLoginTimeout(int seconds) {
    }

    /**
     * <p>
     * Dummy implementation of <code>DataSource</code>.
     * </p>
     *
     * @return <code>null</code>
     */
    public PrintWriter getLogWriter() {
        return null;
    }

    /**
     * <p>
     * Dummy implementation of <code>DataSource</code>.
     * </p>
     *
     * @param out dummy argument
     */
    public void setLogWriter(PrintWriter out) {
    }

    /**
     * <p>
     * Return a <code>DummyConnection</code> connection instance. The information in it is the information in this data
     * source.
     * </p>
     *
     * @return a <code>DummyConnection</code> instance
     */
    public Connection getConnection() {
        try {
            return (Connection) TestHelper.DUMMY_CONNECTION_IMPL.newInstance(new Object[] {information});
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
     * Return a <code>DummyConnection</code> connection instance. The information in it is composed as follows:<br>
     * &quot;&lt;information in dummy data source&gt;|&lt;username&gt;|&lt;password&gt;&quot; If username or password
     * is <code>null</code>, a &quot;!NULL!&quot; is used instead.
     * </p>
     *
     * @param username username used to create connection
     * @param password password used to create connection
     *
     * @return a <code>DummyConnection</code> instance
     */
    public Connection getConnection(String username, String password) {
        if (username == null) {
            username = "!NULL!";
        }

        if (password == null) {
            password = "!NULL!";
        }

        try {
            return (Connection) TestHelper.DUMMY_CONNECTION_IMPL.newInstance(new Object[] {
                    information + "|" + username + "|" + password
                });
        } catch (InstantiationException e) {
            return null;
        } catch (IllegalAccessException e) {
            return null;
        } catch (InvocationTargetException e) {
            return null;
        }
    }
}


