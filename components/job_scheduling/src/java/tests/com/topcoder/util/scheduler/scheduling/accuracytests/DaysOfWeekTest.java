/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.scheduling.accuracytests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.util.scheduler.scheduling.DateUnit;
import com.topcoder.util.scheduler.scheduling.DaysOfWeek;

/**
 * <p>
 * Accuracy tests of <code>{@link DaysOfWeek}</code> class.
 * </p>
 *
 * @author mittu
 * @version 3.0
 */
public class DaysOfWeekTest extends TestCase {
    /**
     * <p>
     * Represents the <code>{@link DaysOfWeek}</code> instance for testing.
     * </p>
     */
    private DaysOfWeek daysOfWeek;

    /**
     * <p>
     * Represents the days used for testing.
     * </p>
     */
    private static final int[] DAYS = new int[] {3, 5, 2};

    /**
     * <p>
     * Sets up test environment.
     * </p>
     *
     */
    protected void setUp() {
        daysOfWeek = new DaysOfWeek(DAYS);
    }

    /**
     * <p>
     * Tears down test environment.
     * </p>
     *
     */
    protected void tearDown() {
        daysOfWeek = null;
    }

    /**
     * <p>
     * Returns all tests.
     * </p>
     *
     * @return all tests
     */
    public static Test suite() {
        return new TestSuite(DaysOfWeekTest.class);
    }

    /**
     * <p>
     * Accuracy test for <code>{@link DaysOfWeek#DaysOfWeek(int[])}</code>.
     * </p>
     * <p>
     * Newly created instance should not be <code>null</code>.
     * </p>
     * <p>
     * Also checks the inheritance of the class.
     * </p>
     */
    public void testDaysOfWeek() {
        assertNotNull("failed to create the DaysOfWeek.", daysOfWeek);
        // inheritance
        assertTrue("DaysOfWeek should inherit from DateUnit", daysOfWeek instanceof DateUnit);
    }

    /**
     * <p>
     * Accuracy test for <code>{@link DaysOfWeek#getDays()}</code>.
     * </p>
     * <p>
     * Also verifies the returned array is sorted.
     * </p>
     */
    public void testGetDays() {
        int[] actual = daysOfWeek.getDays();
        assertNotNull("failed to get the expected days", actual);
        assertEquals("failed to get the expected days", 3, actual.length);
        assertEquals("failed to get the expected days in order", DAYS[2], actual[0]);
        assertEquals("failed to get the expected days in order", DAYS[0], actual[1]);
        assertEquals("failed to get the expected days in order", DAYS[1], actual[2]);
    }

}
