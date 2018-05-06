/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.scheduling;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Test;

/**
 * <p>
 * Unit test cases for Day.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 3.0
 */
public class DayTests extends TestCase {
    /**
     * <p>
     * Returns all tests.
     * </p>
     *
     * @return all tests
     */
    public static Test suite() {
        return new TestSuite(DayTests.class);
    }

    /**
     * <p>
     * Tests ctor Day#Day() for accuracy.
     * </p>
     *
     * <p>
     * It verifies the newly created Day instance should not be null.
     * </p>
     */
    public void testCtor() {
        assertNotNull("Failed to create a new Day instance.", new Day());
    }

}