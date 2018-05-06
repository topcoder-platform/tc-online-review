/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.phase.autopilot;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * Failure tests for class <code>AutoPilotResult</code>.
 * </p>
 * @author abelli
 * @version 1.0
 */
public class AutoPilotResultFailure extends TestCase {

    /**
     * Test method for {@link AutoPilotResult#AutoPilotResult(long, int, int)}.
     * Fails if negative ended count.
     */
    public void testAutoPilotResultNegativeEnded() {
        try {
            new AutoPilotResult(1, -1, 0);
            fail("negative ended count");
        } catch (IllegalArgumentException e) {
            // Good.
        }
    }

    /**
     * Test method for {@link AutoPilotResult#AutoPilotResult(long, int, int)}.
     * Fails if negative started count.
     */
    public void testAutoPilotResultNegativeStarted() {
        try {
            new AutoPilotResult(1, 0, -1);
            fail("negative started count");
        } catch (IllegalArgumentException e) {
            // Good.
        }
    }

    /**
     * Test method for {@link AutoPilotResult#aggregate(AutoPilotResult)}.
     * Fails if null result to be aggregated.
     */
    public void testAggregate() {
        AutoPilotResult result = new AutoPilotResult(1, 0, 0);
        try {
            result.aggregate(null);
            fail("null auto pilot result");
        } catch (IllegalArgumentException e) {
            // Good.
        }
    }

    /**
     * Test method for {@link AutoPilotResult#aggregate(AutoPilotResult)}.
     * Fails if result to be aggregated contains different project id.
     */
    public void testAggregateDifferentProjectId() {
        AutoPilotResult result = new AutoPilotResult(1, 0, 0);
        AutoPilotResult result2 = new AutoPilotResult(2, 0, 0);
        try {
            result.aggregate(result2);
            fail("different project id");
        } catch (IllegalArgumentException e) {
            // Good.
        }
    }

    /**
     * Aggregates the test suite.
     * @return the test suite.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTestSuite(AutoPilotResultFailure.class);

        return suite;
    }

}
