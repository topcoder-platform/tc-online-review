/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.phase.autopilot;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>Unit test cases for AutoPilotException.</p>
 *
 * <p>This class is pretty simple. The test cases simply verifies the exception
 * can be instantiated with the error message and cause properly propagated,
 * and that it comes with correct inheritance.</p>
 *
 * @author abelli
 * @version 1.0
 */
public class AutoPilotExceptionTest extends TestCase {

    /** Exception message used for testing. */
    private static final String TEST_MSG = "Test";

    /** Exception cause used for testing. */
    private static final Exception TEST_EX = new Exception();

    /** AutoPilotException instance construct with message. */
    private static final Exception EX_WITH_MSG = new AutoPilotException(TEST_MSG);

    /** AutoPilotException instance construct with message & cause. */
    private static final Exception EX_WITH_MSG_CAUSE = new AutoPilotException(TEST_MSG, TEST_EX);

    /**
     * <p>Test method for 'AutoPilotException(String)'.</p>
     *
     * <p>Verifies the constructor accepts null message.</p>
     * <p>Verifies the message is propagated properly.</p>
     */
    public void testAutoPilotSourceExceptionString() {
        new AutoPilotException((String) null);

        assertTrue("ctor(msg)", EX_WITH_MSG instanceof AutoPilotException);

        assertTrue("ctor(msg)", EX_WITH_MSG.getMessage().indexOf(TEST_MSG) >= 0);
    }

    /**
     * <p>Test method for 'AutoPilotException(String, Throwable)'.</p>
     *
     * <p>Verifies the constructor accepts null message or cause.</p>
     * <p>Verifies the message and cause is propagated properly.</p>
     */
    public void testAutoPilotSourceExceptionStringThrowable() {
        new AutoPilotException(null, TEST_EX);
        new AutoPilotException(TEST_MSG, null);
        new AutoPilotException(null, null);

        assertTrue("ctor(msg, cause)", EX_WITH_MSG_CAUSE instanceof AutoPilotException);

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
        } catch (AutoPilotException e) {
            // Good
        }

        try {
            throw EX_WITH_MSG_CAUSE;
        } catch (AutoPilotException e) {
            // Good
        }
    }

    /**
     * Aggregates the test suite.
     * @return the test suite.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTestSuite(AutoPilotExceptionTest.class);

        return suite;
    }
}
