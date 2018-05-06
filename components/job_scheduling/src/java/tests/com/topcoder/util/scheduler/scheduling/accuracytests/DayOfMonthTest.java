/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.scheduling.accuracytests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.util.scheduler.scheduling.DateUnit;
import com.topcoder.util.scheduler.scheduling.DayOfMonth;

/**
 * <p>
 * Accuracy tests of <code>{@link DayOfMonth}</code> class.
 * </p>
 *
 * @author mittu
 * @version 3.0
 */
public class DayOfMonthTest extends TestCase {

    /**
     * <p>
     * Represents the <code>{@link DayOfMonth}</code> instance for testing.
     * </p>
     */
    private DayOfMonth dayOfMonth;

    /**
     * <p>
     * Represents the day used for testing.
     * </p>
     */
    private static final int DAY = 5;

    /**
     * <p>
     * Sets up test environment.
     * </p>
     */
    protected void setUp() {
        dayOfMonth = new DayOfMonth(DAY);
    }

    /**
     * <p>
     * Tears down test environment.
     * </p>
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
        return new TestSuite(DayOfMonthTest.class);
    }

    /**
     * <p>
     * Accuracy test for <code>{@link DayOfMonth#DayOfMonth(int)()}</code>.
     * </p>
     * <p>
     * Newly created instance should not be <code>null</code>.
     * </p>
     * <p>
     * Also checks the inheritance of the class.
     * </p>
     */
    public void testDayOfMonth() {
        assertNotNull("failed to create the DayOfMonth.", dayOfMonth);
        // inheritance
        assertTrue("DayOfMonth should inherit from DateUnit", dayOfMonth instanceof DateUnit);
    }

    /**
     * <p>
     * Accuracy test for <code>{@link DayOfMonth#getDay()}</code>.
     * </p>
     * <p>
     * Expects the same DAY which is set initially.
     * </p>
     */
    public void testGetDay() {
        assertEquals("failed to get the expected day", DAY, dayOfMonth.getDay());
    }

}
