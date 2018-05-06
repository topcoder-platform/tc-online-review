/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.scheduling;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Test;

/**
 * <p>
 * Unit test cases for DayOfYear.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 3.0
 */
public class DayOfYearTests extends TestCase {
    /**
     * <p>
     * The DayOfYear instance for testing.
     * </p>
     */
    private DayOfYear dayOfYear;

    /**
     * <p>
     * Sets up test environment.
     * </p>
     *
     */
    protected void setUp() {
        dayOfYear = new DayOfYear(2);
    }

    /**
     * <p>
     * Tears down test environment.
     * </p>
     *
     */
    protected void tearDown() {
        dayOfYear = null;
    }

    /**
     * <p>
     * Returns all tests.
     * </p>
     *
     * @return all tests
     */
    public static Test suite() {
        return new TestSuite(DayOfYearTests.class);
    }

    /**
     * <p>
     * Tests ctor DayOfYear#DayOfYear(int) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that when day &lt; 1 and expects IllegalArgumentException.
     * </p>
     */
    public void testCtor_InvalidDay() {
        try {
            new DayOfYear(0);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests ctor DayOfYear#DayOfYear(int) for accuracy.
     * </p>
     *
     * <p>
     * It verifies the newly created DayOfYear instance should not be null.
     * </p>
     */
    public void testCtor() {
        assertNotNull("Failed to create a new DayOfYear instance.", dayOfYear);
    }

    /**
     * <p>
     * Tests DayOfYear#getDay() for accuracy.
     * </p>
     *
     * <p>
     * It verifies DayOfYear#getDay() is correct.
     * </p>
     */
    public void testGetDay() {
        assertEquals("Failed to get the day correctly.", 2, dayOfYear.getDay());
    }

}