/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.log.accuracytests;

import com.topcoder.util.log.Log;
import com.topcoder.util.log.basic.BasicLogFactory;
import com.topcoder.util.log.basic.BasicLog;

import junit.framework.TestCase;

/**
 * <p>
 * Accuracy tests for <code>BasicLogFactory</code>.
 * </p>
 *
 * @author tianniu
 * @version 2.0
 */
public class BasicLogFactoryAccuracyTests extends TestCase {

    /** <code>BasicLogFactory</code> instance used for testing. */
    private BasicLogFactory factory;

    /**
     * <p>
     * Set up the test environment.
     * </p>
     */
    protected void setUp() {
        factory = new BasicLogFactory();
    }

    /**
     * <p>
     * Release the test environment.
     * </p>
     *
     * @throws Exception to JUnit
     */
    protected void tearDown() throws Exception {
        factory = null;
    }

    /**
     * <p>
     * Accuracy test of constructor <code>BasicLogFactory()</code>.
     * Should create the instance successfully.
     * </p>
     */
    public void testCtor1Accuracy() {
        assertNotNull("Should create the instance successfully.", factory);
    }

    /**
     * <p>
     * Accuracy test of constructor <code>BasicLogFactory(PrintStream printStream)</code>.
     * With valid printStream.
     * Should create the instance successfully.
     * </p>
     */
    public void testCtor2Accuracy() {
        factory = new BasicLogFactory(System.err);
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
        String logName = "some name";
        Log log = factory.createLog(logName);
        assertNotNull("Should return the proper value.", log);
        assertEquals(logName, ((BasicLog) log).getName());
    }

}
