/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.scheduling.accuracytests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.util.scheduler.scheduling.DateUnit;
import com.topcoder.util.scheduler.scheduling.Week;

/**
 * <p>
 * Accuracy tests of <code>{@link Week}</code> class.
 * </p>
 *
 * @author mittu
 * @version 3.0
 */
public class WeekTest extends TestCase {
    /**
     * <p>
     * Returns all tests.
     * </p>
     *
     * @return all tests
     */
    public static Test suite() {
        return new TestSuite(WeekTest.class);
    }

    /**
     * <p>
     * Accuracy test for <code>{@link Week#Week()}</code>.
     * </p>
     * <p>
     * Newly created instance should not be <code>null</code>.
     * </p>
     * <p>
     * Also checks the inheritance of the class.
     * </p>
     */
    public void testWeek() {
        Week week = new Week();
        assertNotNull("failed to create the Week.", week);

        // inheritance
        assertTrue("Week should inherit from DateUnit", week instanceof DateUnit);
    }

}
