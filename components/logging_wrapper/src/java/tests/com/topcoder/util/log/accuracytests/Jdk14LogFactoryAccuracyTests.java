/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.log.accuracytests;

import com.topcoder.util.log.Log;
import com.topcoder.util.log.jdk14.Jdk14LogFactory;
import com.topcoder.util.log.jdk14.Jdk14Log;

import junit.framework.TestCase;

/**
 * <p>
 * Accuracy tests for <code>Jdk14LogFactory</code>.
 * </p>
 *
 * @author tianniu
 * @version 2.0
 */
public class Jdk14LogFactoryAccuracyTests extends TestCase {

    /**
     *  <code>Jdk14LogFactory</code> instance used for testing.
     */
    private Jdk14LogFactory factory;

    /**
     * <p>
     * Set up the test environment.
     * </p>
     *
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        factory = new Jdk14LogFactory();
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
     * Accuracy test of constructor <code>Jdk14LogFactory()</code>.
     * Should create the instance successfully.
     * </p>
     */
    public void testCtorAccuracy() {
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
        assertEquals("some name", ((Jdk14Log) log).getName());
    }

}
