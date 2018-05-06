/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.scheduling.accuracytests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.util.scheduler.scheduling.DateUnit;
import com.topcoder.util.scheduler.scheduling.DayOfYear;

/**
 * <p>
 * Accuracy tests of <code>{@link DayOfYear}</code> class.
 * </p>
 *
 * @author mittu
 * @version 3.0
 */
public class DayOfYearTest extends TestCase {

    /**
     * <p>
     * Represents the <code>{@link DayOfYear}</code> instance for testing.
     * </p>
     */
    private DayOfYear dayOfYear;

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
     *
     */
    protected void setUp() {
        dayOfYear = new DayOfYear(DAY);
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
        return new TestSuite(DayOfYearTest.class);
    }

    /**
     * <p>
     * Accuracy test for <code>{@link DayOfYear#DayOfYear(int)}</code>.
     * </p>
     * <p>
     * Newly created instance should not be <code>null</code>.
     * </p>
     * <p>
     * Also checks the inheritance of the class.
     * </p>
     */
    public void testDayOfYear() {
        assertNotNull("failed to create the DayOfYear.", dayOfYear);
        // inheritance
        assertTrue("DayOfYear should inherit from DateUnit", dayOfYear instanceof DateUnit);
    }

    /**
     * <p>
     * Accuracy test for <code>{@link DayOfYear#getDay()}</code>.
     * </p>
     * <p>
     * Expects the same DAY which is set initially.
     * </p>
     */
    public void testGetDay() {
        assertEquals("failed to get the expected day", DAY, dayOfYear.getDay());
    }

}
