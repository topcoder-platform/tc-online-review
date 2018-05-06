/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.log.log4j;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.util.log.AbstractLog;
import com.topcoder.util.log.Log;

/**
 * <p>
 * Unit tests for {@link Log4jLogFactory} class.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 2.0
 */
public class Log4jLogFactoryTest extends TestCase {
    /**
     * <p>
     * Log4jLogFactory instance used for testing.
     * </p>
     */
    private Log4jLogFactory logFactory;

    /**
     * <p>
     * Returns the test suite of this class.
     * </p>
     *
     * @return the test suite of this class.
     */
    public static Test suite() {
        return new TestSuite(Log4jLogFactoryTest.class);
    }

    /**
     * <p>
     * Sets up the test environment.
     * </p>
     */
    protected void setUp() {
        logFactory = new Log4jLogFactory();
    }

    /**
     * <p>
     * Tears down the test environment.
     * </p>
     */
    protected void tearDown() {
        logFactory = null;
    }

    /**
     * <p>
     * Tests constructor for Log4jLogFactory() for accuracy.
     * </p>
     *
     * <p>
     * Verify that Log4jLogFactory() is correct.
     * </p>
     */
    public void testLog4jLogFactory() {
        assertNotNull("The Log4jLogFactory instance should not be null.", logFactory);
    }

    /**
     * <p>
     * Tests method for Log4jLogFactory(boolean) for accuracy.
     * </p>
     *
     * <p>
     * Verify that Log4jLogFactory(boolean) is correct.
     * </p>
     */
    public void testLog4jLogFactoryBoolean() {
        logFactory = new Log4jLogFactory(true);
        assertNotNull("The Log4jLogFactory instance should not be null.", logFactory);
    }

    /**
     * <p>
     * Tests method for createLog(String) for accuracy.
     * </p>
     *
     * <p>
     * Verify that createLog(String) is correct.
     * </p>
     */
    public void testCreateLog() {
        Log log = logFactory.createLog("name");
        assertTrue("The log should be of Log4jLog.", log instanceof Log4jLog);
        assertEquals("The name is incorrect.", "name", ((AbstractLog) log).getName());
    }
}
