/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.phase.autopilot.failuretests;

import com.topcoder.management.phase.autopilot.AutoPilotResult;

import junit.framework.TestCase;


/**
 * <p>
 * Failure test cases for <code>AutoPilotResult</code>.
 * </p>
 *
 * @author skatou
 * @version 1.0
 */
public class AutoPilotResultFailureTests extends TestCase {
    /**
     * Tests constructor with negative endedCount. IllegalArgumentException should be thrown.
     */
    public void testConstructorNegativeEndedCount() {
        try {
            new AutoPilotResult(2, -1, 2);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests constructor with negative startedCount. IllegalArgumentException should be thrown.
     */
    public void testConstructorNegativeStartedCount() {
        try {
            new AutoPilotResult(2, 16, -2);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests aggregate method with a null argument. IllegalArgumentException should be thrown.
     */
    public void testAggregateNull() {
        try {
            new AutoPilotResult(2, 16, 4).aggregate(null);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests aggregate method with a <code>AutoPilotResult</code> instance with a different project id.
     * IllegalArgumentException should be thrown.
     */
    public void testAggregateDifferentProjectId() {
        try {
            long projectId = 4;
            AutoPilotResult result = new AutoPilotResult(projectId, 16, 4);
            AutoPilotResult result2 = new AutoPilotResult(projectId + 1, 16, 4);
            result.aggregate(result2);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }
}
