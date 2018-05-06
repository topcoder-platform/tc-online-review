/*
 * Copyright (c) 2005, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.cache;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Provides unit test cases for MemoryUtilizationException.
 *
 * @author  rem
 * @version 2.0
 * @since   2.0
 */
public class MemoryUtilizationExceptionTestCase extends TestCase {

    /**
     * Returns the test suite for this class.
     *
     * @return a new TestSuite representing this class.
     */
    public static Test suite() {
        return new TestSuite(MemoryUtilizationExceptionTestCase.class);
    }

    /**
     * Tests constructor MemoryUtilizationException(String message).
     * <ul>
     * <li>Call constructor with correct argument and make sure that getMessage() and getCause() return
     *     correct values.
     * </ul>
     */
    public void testConstructor1() {
        MemoryUtilizationException ex = new MemoryUtilizationException("message");
        assertTrue("getMessage() should return message which starts with message given in constructor",
            ex.getMessage().startsWith("message"));
        assertNull("cause should be set to null by default.", ex.getCause());
    }

    /**
     * Tests constructor MemoryUtilizationException(String message, Throwable cause).
     * <ul>
     * <li>Call constructor with correct arguments and make sure getMessage() and getCause() return
     *     expected values.
     * </ul>
     */
    public void testConstructor2() {
        Throwable cause = new Throwable();
        MemoryUtilizationException ex = new MemoryUtilizationException("message", cause);
        assertTrue("getMessage() should return message which starts with message given in constructor",
            ex.getMessage().startsWith("message"));
        assertSame("getCause() should return cause set in constructor.", cause, ex.getCause());
    }
}
