/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.phase.autopilot;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * Unit tests for class <code>AutoPilotResult</code>.
 * </p>
 * @author abelli
 * @version 1.0
 */
public class AutoPilotResultTest extends TestCase {

    /** The AutoPilotResult instance used in test cases.*/
    private AutoPilotResult result;

    /**
     * <p>
     * Setup the test fixture. Create AutoPilotResult instance for test cases.
     * </p>
     * @see junit.framework.TestCase#setUp()
     * @throws Exception - to JUnit.
     */
    protected void setUp() throws Exception {
        super.setUp();
        result = new AutoPilotResult(1, 1, 2);
    }

    /**
     * Test method for {@link AutoPilotResult#AutoPilotResult(long, int, int)}.
     * Verifies ctor works properly.
     */
    public void testAutoPilotResult() {
        assertTrue(result instanceof AutoPilotResult);
    }

    /**
     * Test method for {@link AutoPilotResult#getProjectId()}.
     * Verifies return the correct projectId.
     */
    public void testGetProjectId() {
        assertEquals(1, result.getProjectId());
    }

    /**
     * Test method for {@link AutoPilotResult#getPhaseEndedCount()}.
     * Verifies return the correct ended count.
     */
    public void testGetPhaseEndedCount() {
        assertEquals(1, result.getPhaseEndedCount());
    }

    /**
     * Test method for {@link AutoPilotResult#getPhaseStartedCount()}.
     * Verifies return the correct started count.
     */
    public void testGetPhaseStartedCount() {
        assertEquals(2, result.getPhaseStartedCount());
    }

    /**
     * Test method for {@link AutoPilotResult#aggregate(AutoPilotResult)}.
     * Verifies return the correct aggregate result.
     */
    public void testAggregate() {
        AutoPilotResult result2 = new AutoPilotResult(1, 2, 3);
        result.aggregate(result2);
        assertEquals(3, result.getPhaseEndedCount());
        assertEquals(5, result.getPhaseStartedCount());
    }

    /**
     * Aggregates the test suite.
     * @return the test suite.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTestSuite(AutoPilotResultTest.class);

        return suite;
    }

}
