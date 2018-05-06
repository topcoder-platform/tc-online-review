/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.log.accuracytests;

import com.topcoder.util.log.Log;
import com.topcoder.util.log.log4j.Log4jLogFactory;
import com.topcoder.util.log.log4j.Log4jLog;

import junit.framework.TestCase;

/**
 * <p>
 * Accuracy tests for <code>Log4jLogFactory</code>.
 * </p>
 *
 * @author tianniu
 * @version 2.0
 */
public class Log4jLogFactoryAccuracyTests extends TestCase {

    /** <code>Log4jLogFactory</code> instance used for testing. */
    private Log4jLogFactory factory;

    /**
     * <p>
     * Set up the test environment.
     * </p>
     */
    protected void setUp() {
        factory = new Log4jLogFactory();
    }

    /**
     * <p>
     * Release the test environment.
     * </p>
     */
    protected void tearDown() {
        factory = null;
    }

    /**
     * <p>
     * Accuracy test of constructor <code>Log4jLogFactory()</code>.
     * Should create the instance successfully.
     * </p>
     */
    public void testCtor1Accuracy() {
        assertNotNull("Should create the instance successfully.", factory);
    }

    /**
     * <p>
     * Accuracy test of constructor <code>Log4jLogFactory(boolean useLoggerLayout)</code>.
     * Should create the instance successfully.
     * </p>
     */
    public void testCtor2Accuracy1() {
        factory = new Log4jLogFactory(false);
        assertNotNull("Should create the instance successfully.", factory);
    }

    /**
     * <p>
     * Accuracy test of constructor <code>Log4jLogFactory(boolean useLoggerLayout)</code>.
     * Should create the instance successfully.
     * </p>
     */
    public void testCtor2Accuracy2() {
        factory = new Log4jLogFactory(true);
        assertNotNull("Should create the instance successfully.", factory);
    }

    /**
     * <p>
     * Accuracy test of method <code>createLog(String name)</code>.
     * With name.
     * Should return the proper value.
     * </p>
     */
    public void testCreateLogAccuracy() {
        Log log = factory.createLog("some name");
        assertNotNull("Should return the proper value.", log);
        assertEquals("some name", ((Log4jLog) log).getName());
    }

}
