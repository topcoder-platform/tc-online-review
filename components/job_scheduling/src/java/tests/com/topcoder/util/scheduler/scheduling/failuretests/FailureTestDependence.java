/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.scheduling.failuretests;

import com.topcoder.util.scheduler.scheduling.Dependence;
import com.topcoder.util.scheduler.scheduling.EventHandler;

import junit.framework.TestCase;

/**
 * <p>
 * Failure test cases for <code>Dependence</code>.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 3.0
 */
public class FailureTestDependence extends TestCase {
    /** Dependence instance of test. */
    private Dependence dependence = null;

    /**
     * Test the constructor with illegal arguments. IllegalArgumentException
     * will be thrown.
     */
    public void testDependenceStringStringintNull() {
        try {
            dependence = new Dependence(null, EventHandler.SUCCESSFUL, 100);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Expected exception raised
        }
    }

    /**
     * Test the constructor with illegal arguments. IllegalArgumentException
     * will be thrown.
     */
    public void testDependenceStringStringintNull2() {
        try {
            dependence = new Dependence("job2", null, 100);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Expected exception raised
        }
    }

    /**
     * Test the constructor with illegal arguments. Give an invalid event value.
     * IllegalArgumentException will be thrown.
     */
    public void testDependenceStringStringintIllegal() {
        try {
            dependence = new Dependence("job2", "1123123", 100);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Expected exception raised
        }
    }

    /**
     * Test the constructor with illegal arguments. Give a negetive delay value.
     * IllegalArgumentException will be thrown.
     */
    public void testDependenceStringStringintIllegal2() {
        try {
            dependence = new Dependence("job2", EventHandler.FAILED, -1);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Expected exception raised
        }
    }
}
