/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.termsofuse.dao.impl;

import java.sql.Connection;

import org.junit.After;
import org.junit.Before;

import com.cronos.termsofuse.TestsHelper;

/**
 * <p>
 * The base class for unit tests for.
 * </p>
 *
 * @author sparemax
 * @version 1.0
 */
public class BaseUnitTests {
    /**
     * <p>
     * Represents the connection used in tests.
     * </p>
     */
    private Connection connection;

    /**
     * <p>
     * Sets up the unit tests.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Before
    public void setUp() throws Exception {
        connection = TestsHelper.getConnection();
        TestsHelper.clearDB(connection);
        TestsHelper.loadDB(connection);
    }

    /**
     * <p>
     * Cleans up the unit tests.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @After
    public void tearDown() throws Exception {
        TestsHelper.clearDB(connection);
        TestsHelper.closeConnection(connection);
        connection = null;
    }

    /**
     * Gets the connection.
     *
     * @return the connection.
     */
    protected Connection getConnection() {
        return connection;
    }
}
