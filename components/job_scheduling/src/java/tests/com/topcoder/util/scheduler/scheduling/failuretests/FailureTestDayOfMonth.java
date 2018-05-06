/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.scheduling.failuretests;

import com.topcoder.util.scheduler.scheduling.DayOfMonth;

import junit.framework.TestCase;

/**
 * This class contains unit tests for DayOfMonth class.
 *
 * @author TCSDEVELOPER
 * @version 3.0
 */
public class FailureTestDayOfMonth extends TestCase {
    /**
     * Tests <code>DayOfMonth(int day)</code> method for failure with invalid
     * Day. IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testDayOfMonthInvalidDay() throws Exception {
        try {
            new DayOfMonth(0);
            fail("testDayOfMonthInvalidDay is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }
}
