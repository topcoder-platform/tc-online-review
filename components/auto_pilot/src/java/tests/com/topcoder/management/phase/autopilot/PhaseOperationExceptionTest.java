/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.phase.autopilot;

import java.util.Date;

import com.topcoder.date.workdays.DefaultWorkdays;
import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.Project;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>Unit test cases for PhaseOperationException.</p>
 *
 * <p>This class is pretty simple. The test cases simply verifies the exception
 * can be instantiated with the error message and cause properly propagated,
 * and that it comes with correct inheritance.</p>
 *
 * @author abelli
 * @version 1.0
 */
public class PhaseOperationExceptionTest extends TestCase {

    /** Exception message used for testing. */
    private static final String TEST_MSG = "Test";

    /** Exception cause used for testing. */
    private static final Exception TEST_EX = new Exception();

    /** PhaseOperationException instance default construct. */
    private static final PhaseOperationException EX_EMPTY = new PhaseOperationException();

    /** PhaseOperationException instance construct with message. */
    private static final PhaseOperationException EX_WITH_MSG = new PhaseOperationException(1, new Phase(
        new Project(new Date(), new DefaultWorkdays()), 3600 * 1000), TEST_MSG);

    /** PhaseOperationException instance construct with message & cause. */
    private static final PhaseOperationException EX_WITH_MSG_CAUSE = new PhaseOperationException(1, new Phase(
        new Project(new Date(), new DefaultWorkdays()), 3600 * 1000), TEST_MSG, TEST_EX);

    /**
     * <p>Test method for 'PhaseOperationException()'.</p>
     *
     * <p>Verifies the constructor accepts null message.</p>
     * <p>Verifies the message is propagated properly.</p>
     */
    public void testPhaseOperationException() {
        new PhaseOperationException();

        assertTrue("ctor()", EX_EMPTY instanceof PhaseOperationException);

        assertNull("ctor()", EX_EMPTY.getMessage());
        assertNull("ctor()", EX_EMPTY.getCause());
    }

    /**
     * <p>Test method for 'PhaseOperationException(String)'.</p>
     *
     * <p>Verifies the constructor accepts null message.</p>
     * <p>Verifies the message is propagated properly.</p>
     */
    public void testPhaseOperationExceptionString() {
        new PhaseOperationException(1, null, (String) null);

        assertTrue("ctor(msg)", EX_WITH_MSG instanceof PhaseOperationException);

        assertTrue("ctor(msg)", EX_WITH_MSG.getMessage().indexOf(TEST_MSG) >= 0);
        assertNull("ctor(msg)", EX_EMPTY.getCause());
    }

    /**
     * <p>Test method for 'PhaseOperationException(String, Throwable)'.</p>
     *
     * <p>Verifies the constructor accepts null message or cause.</p>
     * <p>Verifies the message and cause is propagated properly.</p>
     */
    public void testPhaseOperationExceptionStringThrowable() {
        new PhaseOperationException(1, null, null, TEST_EX);
        new PhaseOperationException(1, null, TEST_MSG, null);
        new PhaseOperationException(1, null, null, null);

        assertTrue("ctor(msg, cause)", EX_WITH_MSG_CAUSE instanceof PhaseOperationException);

        assertTrue("ctor(msg, cause)", EX_WITH_MSG_CAUSE.getMessage().indexOf(TEST_MSG) >= 0);
        assertEquals("ctor(msg, cause)", EX_WITH_MSG_CAUSE.getCause(), TEST_EX);
    }

    /**
     * <p>
     * Test method for getProjectId.
     * </p>
     */
    public void testGetProjectId() {
        assertEquals(-1, EX_EMPTY.getProjectId());
        assertEquals(1, EX_WITH_MSG.getProjectId());
        assertEquals(1, EX_WITH_MSG_CAUSE.getProjectId());
    }

    /**
     * <p>
     * Test method for getPhase.
     * </p>
     */
    public void testGetPhase() {
        assertNull(EX_EMPTY.getPhase());
        assertNotNull(EX_WITH_MSG.getPhase());
        assertNotNull(EX_WITH_MSG_CAUSE.getPhase());
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
        } catch (PhaseOperationException e) {
            // Good
        }

        try {
            throw EX_WITH_MSG_CAUSE;
        } catch (PhaseOperationException e) {
            // Good
        }
    }

    /**
     * Aggregates the test suite.
     * @return the test suite.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTestSuite(PhaseOperationExceptionTest.class);

        return suite;
    }
}
