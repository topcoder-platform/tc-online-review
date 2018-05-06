/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.scheduling.failuretests;

import com.topcoder.util.scheduler.scheduling.WeekOfMonth;

import junit.framework.TestCase;

/**
 * This class contains unit tests for WeekOfMonth class.
 *
 * @author TCSDEVELOPER
 * @version 3.0
 */
public class FailureTestWeekOfMonth extends TestCase {
    /**
     * Tests <code>WeekOfMonth(int day, int week)</code> method for failure
     * with invalid Day. IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testWeekOfMonthInvalidDay1() throws Exception {
        try {
            new WeekOfMonth(0, 2);
            fail("testWeekOfMonthInvalidDay1 is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests <code>WeekOfMonth(int day, int week)</code> method for failure
     * with invalid Day. IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testWeekOfMonthInvalidDay2() throws Exception {
        try {
            new WeekOfMonth(8, 2);
            fail("testWeekOfMonthInvalidDay1 is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests <code>WeekOfMonth(int day, int week)</code> method for failure
     * with invalid Week. IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testWeekOfMonthInvalidWeek() throws Exception {
        try {
            new WeekOfMonth(2, 0);

            fail("testWeekOfMonthInvalidWeek is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }
}
