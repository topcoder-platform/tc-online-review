/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.scheduling.accuracytests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.util.scheduler.scheduling.DateUnit;
import com.topcoder.util.scheduler.scheduling.Day;

/**
 * <p>
 * Accuracy tests of <code>{@link Day}</code> class.
 * </p>
 *
 * @author mittu
 * @version 3.0
 */
public class DayTest extends TestCase {
    /**
     * <p>
     * Returns all tests.
     * </p>
     *
     * @return all tests
     */
    public static Test suite() {
        return new TestSuite(DayTest.class);
    }

    /**
     * <p>
     * Accuracy test for <code>{@link Day#Day()}</code>.
     * </p>
     * <p>
     * Newly created instance should not be <code>null</code>.
     * </p>
     * <p>
     * Also checks the inheritance of the class.
     * </p>
     */
    public void testDay() {
        Day day = new Day();
        assertNotNull("failed to create the Day.", day);

        // inheritance
        assertTrue("Day should inherit from DateUnit", day instanceof DateUnit);
    }

}
