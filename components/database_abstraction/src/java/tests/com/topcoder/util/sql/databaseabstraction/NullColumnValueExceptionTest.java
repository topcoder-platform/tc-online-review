/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.sql.databaseabstraction;

import junit.framework.TestCase;

/**
 * Test case for {@link NullColumnValueException}.
 *
 * @author suhugo
 * @version 2.0
 * @since 2.0
 */
public class NullColumnValueExceptionTest extends TestCase {
    /**
     * <p>
     * Instance of Exception to be used as exception cause.
     * </p>
     */
    private Exception nullException = new NullPointerException("null exception.");

    /**
     * <p>
     * Test constructor NullColumnValueException().
     * </p>
     * <p>
     * Verify: NullColumnValueException can be instantiated.
     * </p>
     */
    public void testNullColumnValueException1() {
        NullColumnValueException exception = new NullColumnValueException();
        assertNotNull("Unable to instantiated NullColumnValueException.", exception);
    }

    /**
     * <p>
     * Test constructor NullColumnValueException(String message).
     * </p>
     * <p>
     * Verify: NullColumnValueException can be instantiated correctly.
     * </p>
     */
    public void testNullColumnValueException2() {
        NullColumnValueException exception = new NullColumnValueException("TEST");
        assertNotNull("Unable to instantiated NullColumnValueException.", exception);
        assertEquals("NullColumnValueException.message is initialized correctly.", exception.getMessage(), "TEST");
    }

    /**
     * <p>
     * Test constructor NullColumnValueException(String message, Exception
     * cause).
     * </p>
     * <p>
     * Verify: NullColumnValueException can be instantiated correctly.
     * </p>
     */
    public void testNullColumnValueException3() {
        NullColumnValueException exception = new NullColumnValueException("TEST", nullException);
        assertNotNull("Unable to instantiated NullColumnValueException.", exception);
        assertEquals("NullColumnValueException.message is not initialized correctly.", exception.getMessage(), "TEST");
        assertEquals("NullColumnValueException.cause is not initialized correctly.", exception.getCause(),
                nullException);
    }

    /**
     * <p>
     * Test constructor NullColumnValueException(String message, Exception
     * cause).
     * </p>
     * <p>
     * Verify: NullColumnValueException can be instantiated correctly.
     * </p>
     */
    public void testNullColumnValueException4() {
        NullColumnValueException exception = new NullColumnValueException("   ", null);
        assertNotNull("Unable to instantiated NullColumnValueException.", exception);
        assertEquals("NullColumnValueException.message is not initialized correctly.", exception.getMessage(), "   ");
        assertNull("NullColumnValueException.cause is not initialized correctly.", exception.getCause());
    }

    /**
     * <p>
     * Test constructor NullColumnValueException(String message, Exception
     * cause).
     * </p>
     * <p>
     * Verify: NullColumnValueException can be instantiated correctly.
     * </p>
     */
    public void testNullColumnValueException5() {
        NullColumnValueException exception = new NullColumnValueException(null, null);
        assertNotNull("Unable to instantiated NullColumnValueException.", exception);
        assertNull("NullColumnValueException.message is not initialized correctly.", exception.getMessage());
        assertNull("NullColumnValueException.cause is not initialized correctly.", exception.getCause());
    }
}
