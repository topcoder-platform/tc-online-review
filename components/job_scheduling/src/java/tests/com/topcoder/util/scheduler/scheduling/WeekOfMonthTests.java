/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.scheduling;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Test;

/**
 * <p>
 * Unit test cases for WeekOfMonth.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 3.0
 */
public class WeekOfMonthTests extends TestCase {
    /**
     * <p>
     * The WeekOfMonth instance for testing.
     * </p>
     */
    private WeekOfMonth weekOfMonth;

    /**
     * <p>
     * Sets up test environment.
     * </p>
     *
     */
    protected void setUp() {
        weekOfMonth = new WeekOfMonth(2, 8);
    }

    /**
     * <p>
     * Tears down test environment.
     * </p>
     *
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
        return new TestSuite(WeekOfMonthTests.class);
    }

    /**
     * <p>
     * Tests ctor WeekOfMonth#WeekOfMonth(int,int) for accuracy.
     * </p>
     *
     * <p>
     * It verifies the newly created WeekOfMonth instance should not be null.
     * </p>
     */
    public void testCtor() {
        assertNotNull("Failed to create a new WeekOfMonth instance.", weekOfMonth);
    }

    /**
     * <p>
     * Tests ctor WeekOfMonth#WeekOfMonth(int,int) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when day &gt; 7 or day &lt; 1 and expects IllegalArgumentException.
     * </p>
     */
    public void testCtor_invalidDay() {
        try {
            new WeekOfMonth(9, 8);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests ctor WeekOfMonth#WeekOfMonth(int,int) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when week &lt; 1 and expects IllegalArgumentException.
     * </p>
     */
    public void testCtor_invalidWeek() {
        try {
            new WeekOfMonth(2, -5);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests WeekOfMonth#getWeek() for accuracy.
     * </p>
     *
     * <p>
     * It verifies WeekOfMonth#getWeek() is correct.
     * </p>
     */
    public void testGetWeek() {
        assertEquals("Failed to get the week correctly.", 8, weekOfMonth.getWeek());
    }

    /**
     * <p>
     * Tests WeekOfMonth#getDay() for accuracy.
     * </p>
     *
     * <p>
     * It verifies WeekOfMonth#getDay() is correct.
     * </p>
     */
    public void testGetDay() {
        assertEquals("Failed to get the day correctly.", 2, weekOfMonth.getDay());
    }

}