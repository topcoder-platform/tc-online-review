/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.scheduling;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Test;

/**
 * <p>
 * Unit test cases for DaysOfWeek.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 3.0
 */
public class DaysOfWeekTests extends TestCase {
    /**
     * <p>
     * The DaysOfWeek instance for testing.
     * </p>
     */
    private DaysOfWeek daysOfWeek;

    /**
     * <p>
     * The days array for testing.
     * </p>
     */
    private int[] days;

    /**
     * <p>
     * Sets up test environment.
     * </p>
     *
     */
    protected void setUp() {
        days = new int[] {1, 3};
        daysOfWeek = new DaysOfWeek(days);
    }

    /**
     * <p>
     * Tears down test environment.
     * </p>
     *
     */
    protected void tearDown() {
        daysOfWeek = null;
        days = null;
    }

    /**
     * <p>
     * Returns all tests.
     * </p>
     *
     * @return all tests
     */
    public static Test suite() {
        return new TestSuite(DaysOfWeekTests.class);
    }

    /**
     * <p>
     * Tests ctor DaysOfWeek#DaysOfWeek(int[]) for accuracy.
     * </p>
     *
     * <p>
     * It verifies the newly created DaysOfWeek instance should not be null.
     * </p>
     */
    public void testCtor() {
        assertNotNull("Failed to create a new DaysOfWeek instance.", daysOfWeek);
    }

    /**
     * <p>
     * Tests ctor DaysOfWeek#DaysOfWeek([int[]) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when days is null and expects IllegalArgumentException.
     * </p>
     */
    public void testCtor_NullDays() {
        try {
            new DaysOfWeek(null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests ctor DaysOfWeek#DaysOfWeek([int[]) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when days.length < 1 or days.length > 7 and
     * expects IllegalArgumentException.
     * </p>
     */
    public void testCtor_InvlidDaysLength() {
        days = new int[] {1, 3, 4, 5, 6, 7, 8, 9};
        try {
            new DaysOfWeek(days);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests ctor DaysOfWeek#DaysOfWeek([int[]) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when days having a day < 1 or days having a day > 7 and
     * expects IllegalArgumentException.
     * </p>
     */
    public void testCtor_InvlidDays() {
        days = new int[] {1, 3, 8, 9};
        try {
            new DaysOfWeek(days);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests ctor DaysOfWeek#DaysOfWeek([int[]) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when days having a duplicate day and
     * expects IllegalArgumentException.
     * </p>
     */
    public void testCtor_DuplicateDay() {
        days = new int[] {1, 3, 3, 5};
        try {
            new DaysOfWeek(days);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests DaysOfWeek#getDays() for accuracy.
     * </p>
     *
     * <p>
     * It verifies DaysOfWeek#getDays() is correct.
     * </p>
     */
    public void testGetDays() {
        int[] newDays = daysOfWeek.getDays();

        assertEquals("Excepted the length of the days is two.", 2, newDays.length);
        assertEquals("Failed to get the days correctly.", days[0], newDays[0]);
        assertEquals("Failed to get the days correctly.", days[1], newDays[1]);
    }

}