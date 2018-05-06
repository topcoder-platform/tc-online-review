/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.scheduling.accuracytests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.util.scheduler.scheduling.DateUnit;
import com.topcoder.util.scheduler.scheduling.WeekOfMonth;

/**
 * <p>
 * Accuracy tests of <code>{@link WeekOfMonth}</code> class.
 * </p>
 *
 * @author mittu
 * @version 3.0
 */
public class WeekOfMonthTest extends TestCase {
    /**
     * <p>
     * Represents the <code>{@link WeekOfMonth}</code> instance for testing.
     * </p>
     */
    private WeekOfMonth weekOfMonth;

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
    private static final int WEEK = 1;

    /**
     * <p>
     * Sets up test environment.
     * </p>
     */
    protected void setUp() {
        weekOfMonth = new WeekOfMonth(DAY, WEEK);
    }

    /**
     * <p>
     * Tears down test environment.
     * </p>
     */
    protected void tearDown() {
        weekOfMonth = null;
    }

    /**
     * <p>
     * Returns all tests.
     * </p>
     *
     * @return all tests
     */
    public static Test suite() {
        return new TestSuite(WeekOfMonthTest.class);
    }

    /**
     * <p>
     * Accuracy test for <code>{@link WeekOfMonth#WeekOfMonth(int, int)}</code>.
     * </p>
     * <p>
     * Newly created instance should not be <code>null</code>.
     * </p>
     * <p>
     * Also checks the inheritance of the class.
     * </p>
     */
    public void testWeekOfMonth() {
        assertNotNull("failed to create the WeekOfMonth.", weekOfMonth);

        // inheritance
        assertTrue("WeekOfMonth should inherit from DateUnit", weekOfMonth instanceof DateUnit);
    }

    /**
     * <p>
     * Accuracy test for <code>{@link WeekOfMonth#getDay()}</code>.
     * </p>
     * <p>
     * Expects the same which is set during construction.
     * </p>
     */
    public void testGetDay() {
        assertEquals("failed to get day", DAY, weekOfMonth.getDay());
    }

    /**
     * <p>
     * Accuracy test for <code>{@link WeekOfMonth#getWeek()}</code>.
     * </p>
     * <p>
     * Expects the same which is set during construction.
     * </p>
     */
    public void testGetWeek() {
        assertEquals("failed to get week", WEEK, weekOfMonth.getWeek());
    }

}
