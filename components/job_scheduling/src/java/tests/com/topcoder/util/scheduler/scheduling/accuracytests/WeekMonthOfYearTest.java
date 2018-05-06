/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.scheduling.accuracytests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.util.scheduler.scheduling.DateUnit;
import com.topcoder.util.scheduler.scheduling.WeekMonthOfYear;

/**
 * <p>
 * Accuracy tests of <code>{@link WeekMonthOfYear}</code> class.
 * </p>
 *
 * @author mittu
 * @version 3.0
 */
public class WeekMonthOfYearTest extends TestCase {
    /**
     * <p>
     * Represents the day used for testing.
     * </p>
     */
    private static final int DAY = 5;

    /**
     * <p>
     * Represents the week used for testing.
     * </p>
     */
    private static final int WEEK = 19;

    /**
     * <p>
     * Represents the month used for testing.
     * </p>
     */
    private static final int MONTH = 3;

    /**
     * <p>
     * Represents the <code>{@link WeekMonthOfYear}</code> for testing.
     * </p>
     */
    private WeekMonthOfYear weekMonthOfYear;

    /**
     * <p>
     * Sets up test environment.
     * </p>
     */
    protected void setUp() {
        weekMonthOfYear = new WeekMonthOfYear(DAY, WEEK, MONTH);
    }

    /**
     * <p>
     * Tears down test environment.
     * </p>
     */
    protected void tearDown() {
        weekMonthOfYear = null;
    }

    /**
     * <p>
     * Returns all tests.
     * </p>
     *
     * @return all tests
     */
    public static Test suite() {
        return new TestSuite(WeekMonthOfYearTest.class);
    }

    /**
     * <p>
     * Accuracy test for <code>{@link WeekMonthOfYear#WeekMonthOfYear(int, int, int)}</code>.
     * </p>
     * <p>
     * Newly created instance should not be <code>null</code>.
     * </p>
     * <p>
     * Also checks the inheritance of the class.
     * </p>
     */
    public void testWeekMonthOfYear() {
        assertNotNull("failed to create the WeekMonthOfYear.", weekMonthOfYear);

        // inheritance
        assertTrue("WeekMonthOfYear should inherit from DateUnit", weekMonthOfYear instanceof DateUnit);
    }

    /**
     * <p>
     * Accuracy test for <code>{@link WeekMonthOfYear#getDay()}</code>.
     * </p>
     * <p>
     * Expects the same which is set during construction.
     * </p>
     */
    public void testGetDay() {
        assertEquals("failed to get day", DAY, weekMonthOfYear.getDay());
    }

    /**
     * <p>
     * Accuracy test for <code>{@link WeekMonthOfYear#getWeek()}</code>.
     * </p>
     * <p>
     * Expects the same which is set during construction.
     * </p>
     */
    public void testGetWeek() {
        assertEquals("failed to get week", WEEK, weekMonthOfYear.getWeek());
    }

    /**
     * <p>
     * Accuracy test for <code>{@link WeekMonthOfYear#getMonth()}</code>.
     * </p>
     * <p>
     * Expects the same which is set during construction.
     * </p>
     */
    public void testGetMonth() {
        assertEquals("failed to get month", MONTH, weekMonthOfYear.getMonth());
    }

}
