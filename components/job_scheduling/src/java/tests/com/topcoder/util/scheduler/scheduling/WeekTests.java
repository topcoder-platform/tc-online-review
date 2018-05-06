/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.scheduling;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Test;

/**
 * <p>
 * Unit test cases for Week.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 3.0
 */
public class WeekTests extends TestCase {
    /**
     * <p>
     * Returns all tests.
     * </p>
     *
     * @return all tests
     */
    public static Test suite() {
        return new TestSuite(WeekTests.class);
    }

    /**
     * <p>
     * Tests ctor Week#Week() for accuracy.
     * </p>
     *
     * <p>
     * It verifies the newly created Week instance should not be null.
     * </p>
     */
    public void testCtor() {
        assertNotNull("Failed to create a new Week instance.", new Week());
    }

}