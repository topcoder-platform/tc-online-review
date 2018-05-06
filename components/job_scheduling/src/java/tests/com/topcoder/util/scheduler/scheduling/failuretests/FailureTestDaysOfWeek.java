/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.scheduling.failuretests;

import com.topcoder.util.scheduler.scheduling.DaysOfWeek;

import junit.framework.TestCase;

/**
 * This class contains unit tests for DaysOfWeek class.
 *
 * @author TCSDEVELOPER
 * @version 3.0
 */
public class FailureTestDaysOfWeek extends TestCase {
    /**
     * Tests <code>DaysOfWeek(int[] days)</code> method for failure with null
     * Days. IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testDaysOfWeekNullDays() throws Exception {
        try {
            new DaysOfWeek(null);
            fail("testDaysOfWeekNullDays is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests <code>DaysOfWeek(int[] days)</code> method for failure with
     * invalid Days. IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testDaysOfWeekInvalidDays1() throws Exception {
        try {
            new DaysOfWeek(new int[]{});
            fail("testDaysOfWeekInvalidDays1 is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests <code>DaysOfWeek(int[] days)</code> method for failure with
     * invalid Days. IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testDaysOfWeekInvalidDays2() throws Exception {
        try {
            new DaysOfWeek(new int[]{1, 2, 3, 4, 5, 6, 7, 8 });
            fail("testDaysOfWeekInvalidDays2 is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests <code>DaysOfWeek(int[] days)</code> method for failure with
     * invalid Days. IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testDaysOfWeekInvalidDays3() throws Exception {
        try {
            new DaysOfWeek(new int[]{1, 2, 0, 4, 5, 6 });
            fail("testDaysOfWeekInvalidDays3 is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests <code>DaysOfWeek(int[] days)</code> method for failure with
     * invalid Days. IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testDaysOfWeekInvalidDays4() throws Exception {
        try {
            new DaysOfWeek(new int[]{1, 2, 3, 4, 8, 6 });
            fail("testDaysOfWeekInvalidDays4 is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests <code>DaysOfWeek(int[] days)</code> method for failure with
     * invalid Days. IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testDaysOfWeekInvalidDays5() throws Exception {
        try {
            new DaysOfWeek(new int[]{1, 2, 3, 4, 5, 2 });
            fail("testDaysOfWeekInvalidDays5 is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }
}
