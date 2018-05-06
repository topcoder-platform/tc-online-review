/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.scheduling.failuretests;

import com.topcoder.util.scheduler.scheduling.WeekMonthOfYear;

import junit.framework.TestCase;

/**
 * This class contains unit tests for WeekMonthOfYear class.
 *
 * @author TCSDEVELOPER
 * @version 3.0
 */
public class FailureTestWeekMonthOfYear extends TestCase {
    /**
     * Tests <code>WeekMonthOfYear(int day, int week, int month)</code> method
     * for failure with invalid Day. IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testWeekMonthOfYearInvalidDay1() throws Exception {
        try {
            new WeekMonthOfYear(0, 2, 2);
            fail("testWeekMonthOfYearInvalidDay1 is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests <code>WeekMonthOfYear(int day, int week, int month)</code> method
     * for failure with invalid Day. IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testWeekMonthOfYearInvalidDay2() throws Exception {
        try {
            new WeekMonthOfYear(8, 2, 2);
            fail("testWeekMonthOfYearInvalidDay2 is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests <code>WeekMonthOfYear(int day, int week, int month)</code> method
     * for failure with invalid Week. IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testWeekMonthOfYearInvalidWeek() throws Exception {
        try {
            new WeekMonthOfYear(2, 0, 2);
            fail("testWeekMonthOfYearInvalidWeek is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests <code>WeekMonthOfYear(int day, int week, int month)</code> method
     * for failure with invalid Month. IllegalArgumentException should be
     * thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testWeekMonthOfYearInvalidMonth1() throws Exception {
        try {
            new WeekMonthOfYear(2, 2, 0);
            fail("testWeekMonthOfYearInvalidMonth1 is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests <code>WeekMonthOfYear(int day, int week, int month)</code> method
     * for failure with invalid Month. IllegalArgumentException should be
     * thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testWeekMonthOfYearInvalidMonth2() throws Exception {
        try {
            new WeekMonthOfYear(2, 2, 13);
            fail("testWeekMonthOfYearInvalidMonth2 is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }
}
