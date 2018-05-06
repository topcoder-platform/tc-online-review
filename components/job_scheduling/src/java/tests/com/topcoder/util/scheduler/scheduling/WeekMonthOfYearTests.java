/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.scheduling;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Test;

/**
 * <p>
 * Unit test cases for WeekMonthOfYear.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 3.0
 */
public class WeekMonthOfYearTests extends TestCase {
    /**
     * <p>
     * The WeekMonthOfYear instance for testing.
     * </p>
     */
    private WeekMonthOfYear weekMonthOfYear;

    /**
     * <p>
     * Sets up test environment.
     * </p>
     *
     */
    protected void setUp() {
        weekMonthOfYear = new WeekMonthOfYear(2, 1, 8);
    }

    /**
     * <p>
     * Tears down test environment.
     * </p>
     *
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
        return new TestSuite(WeekMonthOfYearTests.class);
    }

    /**
     * <p>
     * Tests ctor WeekMonthOfYear#WeekMonthOfYear(int,int,int) for accuracy.
     * </p>
     *
     * <p>
     * It verifies the newly created WeekMonthOfYear instance should not be null.
     * </p>
     */
    public void testCtor() {
        assertNotNull("Failed to create a new WeekMonthOfYear instance.", weekMonthOfYear);
    }

    /**
     * <p>
     * Tests ctor WeekMonthOfYear#WeekMonthOfYear(int,int,int) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when day &gt; 7 or day &lt; 1 and expects IllegalArgumentException.
     * </p>
     */
    public void testCtor_invalidDay() {
        try {
            new WeekMonthOfYear(9, 1, 8);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests ctor WeekMonthOfYear#WeekMonthOfYear(int,int,int) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when week &lt; 1 and expects IllegalArgumentException.
     * </p>
     */
    public void testCtor_invalidWeek() {
        try {
            new WeekMonthOfYear(2, -1, 8);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests ctor WeekMonthOfYear#WeekMonthOfYear(int,int,int) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when month &lt; 1 or month &gt; 12 and expects IllegalArgumentException.
     * </p>
     */
    public void testCtor_invalidMonth() {
        try {
            new WeekMonthOfYear(2, 1, 15);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests WeekMonthOfYear#getWeek() for accuracy.
     * </p>
     *
     * <p>
     * It verifies WeekMonthOfYear#getWeek() is correct.
     * </p>
     */
    public void testGetWeek() {
        assertEquals("Failed to get the week correctly.", 1, weekMonthOfYear.getWeek());
    }

    /**
     * <p>
     * Tests WeekMonthOfYear#getMonth() for accuracy.
     * </p>
     *
     * <p>
     * It verifies WeekMonthOfYear#getMonth() is correct.
     * </p>
     */
    public void testGetMonth() {
        assertEquals("Failed to get the month correctly.", 8, weekMonthOfYear.getMonth());
    }

    /**
     * <p>
     * Tests WeekMonthOfYear#getDay() for accuracy.
     * </p>
     *
     * <p>
     * It verifies WeekMonthOfYear#getDay() is correct.
     * </p>
     */
    public void testGetDay() {
        assertEquals("Failed to get the day correctly.", 2, weekMonthOfYear.getDay());
    }

}