/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.log.jdk14;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.util.log.AbstractLog;
import com.topcoder.util.log.Log;

/**
 * <p>
 * Unit tests for {@link Jdk14LogFactory} class.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 2.0
 */
public class Jdk14LogFactoryTest extends TestCase {
    /**
     * <p>
     * Jdk14LogFactory instance used for testing.
     * </p>
     */
    private Jdk14LogFactory logFactory;

    /**
     * <p>
     * Returns the test suite of this class.
     * </p>
     *
     * @return the test suite of this class.
     */
    public static Test suite() {
        return new TestSuite(Jdk14LogFactoryTest.class);
    }

    /**
     * <p>
     * Sets up the test environment.
     * </p>
     */
    protected void setUp() {
        logFactory = new Jdk14LogFactory();
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
     * Tests constructor for Jdk14LogFactory() for accuracy.
     * </p>
     *
     * <p>
     * Verify that Jdk14LogFactory() is correct.
     * </p>
     */
    public void testJdk14LogFactory() {
        assertNotNull("The Jdk14LogFactory should not be null.", logFactory);
        Log log = logFactory.createLog("name");
        assertTrue("The log should be of Jdk14Log", log instanceof Jdk14Log);
        assertEquals("The name of the log should be 'name'.", "name", ((AbstractLog) log).getName());
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
        assertTrue("The log should be of Jdk14Log", log instanceof Jdk14Log);
        assertEquals("The name of the log should be 'name'.", "name", ((AbstractLog) log).getName());
    }
}
