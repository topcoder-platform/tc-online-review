/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.commons.utils;

import java.io.File;
import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.util.Properties;

import junit.framework.JUnit4TestAdapter;

import org.apache.log4j.LogManager;
import org.apache.log4j.PropertyConfigurator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * <p>
 * Shows usage for the component.
 * </p>
 *
 * @author saarixx, sparemax
 * @version 1.0
 */
public class Demo {
    /**
     * <p>
     * Represents the Connection used in tests.
     * </p>
     */
    private Connection connection;

    /**
     * <p>
     * Adapter for earlier versions of JUnit.
     * </p>
     *
     * @return a test suite.
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(Demo.class);
    }

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
        PropertyConfigurator.configure("test_files/log4j.properties");

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
        LogManager.shutdown();
        new File(TestsHelper.LOG_FILE).delete();

        try {
            TestsHelper.clearDB(connection);
        } finally {
            TestsHelper.closeConnection(connection);
        }
    }

    /**
     * <p>
     * Demonstrates API usage of this component.
     * </p>
     *
     * <p>
     * It calls the IssuesTracker#updateIssue() method that uses most of the utilities defined in this component.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void testDemoAPI() throws Exception {
        IssuesTracker issuesTracker = new IssuesTracker();

        // Call IssuesTracker#updateIssue()
        issuesTracker.updateIssue("issue1", 1, new File(TestsHelper.CONFIG_FILE));

        try {
            // Call IssuesTracker#updateIssue() with the parameters file that does not exist
            // IllegalArgumentException will be thrown
            issuesTracker.updateIssue("issue1", 2, new File(TestsHelper.NOT_EXIST_FILE));
        } catch (IllegalArgumentException e) {
            // Ignore
        }
    }

    /**
     * <p>
     * Demonstrates usage of sub-configuration.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void testDemoSubConfiguration() throws Exception {
        // Get main configuration
        Properties properties = TestsHelper.loadProperties(new File(TestsHelper.CONFIG_FILE));

        // Get retriever class by its full name from configuration
        Class<?> retrieverClass = PropertiesUtility.getClassProperty(properties, "retrieverClassName", true,
            Exception.class);

        // Get constructor that accepts Properties instance
        Constructor<?> constructor = retrieverClass.getConstructor(Properties.class);

        // Get inner retriever configuration
        Properties retrieverConfig = PropertiesUtility.getSubConfiguration(properties, "retriever");
        // retrieverConfig should contain the following key/value pairs:
        // param1=12345
        // param2=ABC

        // Create retriever with use of reflection
        Retriever retriever = (Retriever) constructor.newInstance(retrieverConfig);
    }
}
