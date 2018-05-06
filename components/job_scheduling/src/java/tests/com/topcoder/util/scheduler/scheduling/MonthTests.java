/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.scheduling;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Test;

/**
 * <p>
 * Unit test cases for Month.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 3.0
 */
public class MonthTests extends TestCase {
    /**
     * <p>
     * Returns all tests.
     * </p>
     *
     * @return all tests
     */
    public static Test suite() {
        return new TestSuite(MonthTests.class);
    }

    /**
     * <p>
     * Tests ctor Month#Month() for accuracy.
     * </p>
     *
     * <p>
     * It verifies the newly created Month instance should not be null.
     * </p>
     */
    public void testCtor() {
        assertNotNull("Failed to create a new Month instance.", new Month());
    }

}