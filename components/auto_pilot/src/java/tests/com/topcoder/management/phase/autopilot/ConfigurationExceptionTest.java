/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.phase.autopilot;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>Unit test cases for ConfigurationException.</p>
 *
 * <p>This class is pretty simple. The test cases simply verifies the exception
 * can be instantiated with the error message and cause properly propagated,
 * and that it comes with correct inheritance.</p>
 *
 * @author abelli
 * @version 1.0
 */
public class ConfigurationExceptionTest extends TestCase {

    /** Exception message used for testing. */
    private static final String TEST_MSG = "Test";

    /** Exception cause used for testing. */
    private static final Exception TEST_EX = new Exception();

    /** ConfigurationException instance default construct. */
    private static final Exception EX_EMPTY = new ConfigurationException();

    /** ConfigurationException instance construct with message. */
    private static final Exception EX_WITH_MSG = new ConfigurationException(TEST_MSG);

    /** ConfigurationException instance construct with message & cause. */
    private static final Exception EX_WITH_MSG_CAUSE = new ConfigurationException(TEST_MSG, TEST_EX);

    /**
     * <p>Test method for 'ConfigurationException()'.</p>
     *
     * <p>Verifies the constructor accepts null message.</p>
     * <p>Verifies the message is propagated properly.</p>
     */
    public void testConfigurationException() {
        new ConfigurationException();

        assertTrue("ctor()", EX_EMPTY instanceof ConfigurationException);

        assertNull("ctor()", EX_EMPTY.getMessage());
        assertNull("ctor()", EX_EMPTY.getCause());
    }

    /**
     * <p>Test method for 'ConfigurationException(String)'.</p>
     *
     * <p>Verifies the constructor accepts null message.</p>
     * <p>Verifies the message is propagated properly.</p>
     */
    public void testConfigurationExceptionString() {
        new ConfigurationException((String) null);

        assertTrue("ctor(msg)", EX_WITH_MSG instanceof ConfigurationException);

        assertTrue("ctor(msg)", EX_WITH_MSG.getMessage().indexOf(TEST_MSG) >= 0);
        assertNull("ctor(msg)", EX_EMPTY.getCause());
    }

    /**
     * <p>Test method for 'ConfigurationException(String, Throwable)'.</p>
     *
     * <p>Verifies the constructor accepts null message or cause.</p>
     * <p>Verifies the message and cause is propagated properly.</p>
     */
    public void testConfigurationExceptionStringThrowable() {
        new ConfigurationException(null, TEST_EX);
        new ConfigurationException(TEST_MSG, null);
        new ConfigurationException(null, null);

        assertTrue("ctor(msg, cause)", EX_WITH_MSG_CAUSE instanceof ConfigurationException);

        assertTrue("ctor(msg, cause)", EX_WITH_MSG_CAUSE.getMessage().indexOf(TEST_MSG) >= 0);
        assertEquals("ctor(msg, cause)", EX_WITH_MSG_CAUSE.getCause(), TEST_EX);
    }

    /**
     * <p>Test for throwing exceptions.</p>
     *
     * <p>Verifies the exception can be caught properly.</p>
     * @throws Exception pass unexpected exceptions to JUnit.
     */
    public void testThrowing() throws Exception {
        try {
            throw EX_WITH_MSG;
        } catch (ConfigurationException e) {
            // Good
        }

        try {
            throw EX_WITH_MSG_CAUSE;
        } catch (ConfigurationException e) {
            // Good
        }
    }

    /**
     * Aggregates the test suite.
     * @return the test suite.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTestSuite(ConfigurationExceptionTest.class);

        return suite;
    }
}
