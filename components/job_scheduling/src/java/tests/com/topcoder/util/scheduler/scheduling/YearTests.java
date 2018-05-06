/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.scheduling;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Test;

/**
 * <p>
 * Unit test cases for Year.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 3.0
 */
public class YearTests extends TestCase {
    /**
     * <p>
     * Returns all tests.
     * </p>
     *
     * @return all tests
     */
    public static Test suite() {
        return new TestSuite(YearTests.class);
    }

    /**
     * <p>
     * Tests ctor Year#Year() for accuracy.
     * </p>
     *
     * <p>
     * It verifies the newly created Year instance should not be null.
     * </p>
     */
    public void testCtor() {
        assertNotNull("Failed to create a new Year instance.", new Year());
    }

}