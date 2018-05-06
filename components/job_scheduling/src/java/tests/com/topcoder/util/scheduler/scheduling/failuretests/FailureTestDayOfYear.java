/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.scheduling.failuretests;

import com.topcoder.util.scheduler.scheduling.DayOfYear;

import junit.framework.TestCase;

/**
 * This class contains unit tests for DayOfYear class.
 *
 * @author TCSDEVELOPER
 * @version 3.0
 */
public class FailureTestDayOfYear extends TestCase {
    /**
     * Tests <code>DayOfYear(int day)</code> method for failure with invalid
     * Day. IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testDayOfYearInvalidDay() throws Exception {
        try {
            new DayOfYear(0);
            fail("testDayOfYearInvalidDay is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }
}
