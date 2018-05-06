/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.scheduling;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Test;

/**
 * <p>
 * Unit test cases for DayOfMonth.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 3.0
 */
public class DayOfMonthTests extends TestCase {
    /**
     * <p>
     * The DayOfMonth instance for testing.
     * </p>
     */
    private DayOfMonth dayOfMonth;

    /**
     * <p>
     * Sets up test environment.
     * </p>
     *
     */
    protected void setUp() {
        dayOfMonth = new DayOfMonth(2);
    }

    /**
     * <p>
     * Tears down test environment.
     * </p>
     *
     */
    protected void tearDown() {
        dayOfMonth = null;
    }

    /**
     * <p>
     * Returns all tests.
     * </p>
     *
     * @return all tests
     */
    public static Test suite() {
        return new TestSuite(DayOfMonthTests.class);
    }

    /**
     * <p>
     * Tests ctor DayOfMonth#DayOfMonth(int) for accuracy.
     * </p>
     *
     * <p>
     * It verifies the newly created DayOfMonth instance should not be null.
     * </p>
     */
    public void testCtor() {
        assertNotNull("Failed to create a new DayOfMonth instance.", dayOfMonth);
    }

    /**
     * <p>
     * Tests ctor DayOfMonth#DayOfMonth(int) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when day &lt; 1 and expects IllegalArgumentException.
     * </p>
     */
    public void testCtor_InvalidDay() {
        try {
            new DayOfMonth(0);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests DayOfMonth#getDay() for accuracy.
     * </p>
     *
     * <p>
     * It verifies DayOfMonth#getDay() is correct.
     * </p>
     */
    public void testGetDay() {
        assertEquals("Failed to get the day correctly.", 2, dayOfMonth.getDay());
    }

}