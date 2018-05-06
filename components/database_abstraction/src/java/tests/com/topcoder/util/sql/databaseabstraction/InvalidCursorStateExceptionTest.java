/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.sql.databaseabstraction;

import junit.framework.TestCase;

/**
 * Test case for InvalidCursorStateException.
 *
 * @author justforplay
 * @version 1.1
 * @since 1.1
 */
public class InvalidCursorStateExceptionTest extends TestCase {
    /**
     * <p>
     * Instance of Exception to be used as exception cause.
     * </p>
     */
    private Exception nullException = new NullPointerException("null exception.");

    /**
     * <p>
     * Test constructor InvalidCursorStateException().
     * </p>
     * <p>
     * Verify: InvalidCursorStateException can be instantiated.
     * </p>
     */
    public void testInvalidCursorStateException1() {
        InvalidCursorStateException exception = new InvalidCursorStateException();
        assertNotNull("Unable to instantiated InvalidCursorStateException.", exception);
    }

    /**
     * <p>
     * Test constructor InvalidCursorStateException(String message).
     * </p>
     * <p>
     * Verify: InvalidCursorStateException can be instantiated correctly.
     * </p>
     */
    public void testInvalidCursorStateException2() {
        InvalidCursorStateException exception = new InvalidCursorStateException("TEST");
        assertNotNull("Unable to instantiated InvalidCursorStateException.", exception);
        assertEquals("InvalidCursorStateException.message is initialized correctly.", exception.getMessage(), "TEST");
    }

    /**
     * <p>
     * Test constructor InvalidCursorStateException(String message, Exception
     * cause).
     * </p>
     * <p>
     * Verify: InvalidCursorStateException can be instantiated correctly.
     * </p>
     */
    public void testInvalidCursorStateException3() {
        InvalidCursorStateException exception = new InvalidCursorStateException("TEST", nullException);
        assertNotNull("Unable to instantiated InvalidCursorStateException.", exception);
        assertEquals("InvalidCursorStateException.message is not initialized correctly.", exception.getMessage(),
                "TEST");
        assertEquals("InvalidCursorStateException.cause is not initialized correctly.", exception.getCause(),
                nullException);
    }

    /**
     * <p>
     * Test constructor InvalidCursorStateException(String message, Exception
     * cause).
     * </p>
     * <p>
     * Verify: InvalidCursorStateException can be instantiated correctly.
     * </p>
     */
    public void testInvalidCursorStateException4() {
        InvalidCursorStateException exception = new InvalidCursorStateException("   ", null);
        assertNotNull("Unable to instantiated InvalidCursorStateException.", exception);
        assertEquals("InvalidCursorStateException.message is not initialized correctly.",
                exception.getMessage(), "   ");
        assertNull("InvalidCursorStateException.cause is not initialized correctly.", exception.getCause());
    }

    /**
     * <p>
     * Test constructor InvalidCursorStateException(String message, Exception
     * cause).
     * </p>
     * <p>
     * Verify: InvalidCursorStateException can be instantiated correctly.
     * </p>
     */
    public void testInvalidCursorStateException5() {
        InvalidCursorStateException exception = new InvalidCursorStateException(null, null);
        assertNotNull("Unable to instantiated InvalidCursorStateException.", exception);
        assertNull("InvalidCursorStateException.message is not initialized correctly.", exception.getMessage());
        assertNull("InvalidCursorStateException.cause is not initialized correctly.", exception.getCause());
    }

}
