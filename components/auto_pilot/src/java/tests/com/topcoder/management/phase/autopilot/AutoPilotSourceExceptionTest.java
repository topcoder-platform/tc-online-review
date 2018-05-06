/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.phase.autopilot;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>Unit test cases for AutoPilotSourceException.</p>
 *
 * <p>This class is pretty simple. The test cases simply verifies the exception
 * can be instantiated with the error message and cause properly propagated,
 * and that it comes with correct inheritance.</p>
 *
 * @author abelli
 * @version 1.0
 */
public class AutoPilotSourceExceptionTest extends TestCase {

    /** Exception message used for testing. */
    private static final String TEST_MSG = "Test";

    /** Exception cause used for testing. */
    private static final Exception TEST_EX = new Exception();

    /** AutoPilotSourceException instance default construct. */
    private static final Exception EX_EMPTY = new AutoPilotSourceException();

    /** AutoPilotSourceException instance construct with message. */
    private static final Exception EX_WITH_MSG = new AutoPilotSourceException(TEST_MSG);

    /** AutoPilotSourceException instance construct with message & cause. */
    private static final Exception EX_WITH_MSG_CAUSE = new AutoPilotSourceException(TEST_MSG, TEST_EX);

    /**
     * <p>Test method for 'AutoPilotSourceException()'.</p>
     *
     * <p>Verifies the constructor accepts null message.</p>
     * <p>Verifies the message is propagated properly.</p>
     */
    public void testAutoPilotSourceException() {
        new AutoPilotSourceException();

        assertTrue("ctor()", EX_EMPTY instanceof AutoPilotSourceException);

        assertNull("ctor()", EX_EMPTY.getMessage());
        assertNull("ctor()", EX_EMPTY.getCause());
    }

    /**
     * <p>Test method for 'AutoPilotSourceException(String)'.</p>
     *
     * <p>Verifies the constructor accepts null message.</p>
     * <p>Verifies the message is propagated properly.</p>
     */
    public void testAutoPilotSourceExceptionString() {
        new AutoPilotSourceException((String) null);

        assertTrue("ctor(msg)", EX_WITH_MSG instanceof AutoPilotSourceException);

        assertTrue("ctor(msg)", EX_WITH_MSG.getMessage().indexOf(TEST_MSG) >= 0);
        assertNull("ctor(msg)", EX_EMPTY.getCause());
    }

    /**
     * <p>Test method for 'AutoPilotSourceException(String, Throwable)'.</p>
     *
     * <p>Verifies the constructor accepts null message or cause.</p>
     * <p>Verifies the message and cause is propagated properly.</p>
     */
    public void testAutoPilotSourceExceptionStringThrowable() {
        new AutoPilotSourceException(null, TEST_EX);
        new AutoPilotSourceException(TEST_MSG, null);
        new AutoPilotSourceException(null, null);

        assertTrue("ctor(msg, cause)", EX_WITH_MSG_CAUSE instanceof AutoPilotSourceException);

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
        } catch (AutoPilotSourceException e) {
            // Good
        }

        try {
            throw EX_WITH_MSG_CAUSE;
        } catch (AutoPilotSourceException e) {
            // Good
        }
    }

    /**
     * Aggregates the test suite.
     * @return the test suite.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTestSuite(AutoPilotSourceExceptionTest.class);

        return suite;
    }
}
