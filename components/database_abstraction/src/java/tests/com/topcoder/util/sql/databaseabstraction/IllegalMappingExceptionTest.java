/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.sql.databaseabstraction;

import junit.framework.TestCase;

/**
 * Test case for IllegalMappingException.
 *
 * @author justforplay
 * @version 1.1
 * @since 1.1
 */
public class IllegalMappingExceptionTest extends TestCase {
    /**
     * <p>
     * Instance of Exception to be used as exception cause.
     * </p>
     */
    private Exception nullException = new NullPointerException("null exception.");

    /**
     * <p>
     * Test constructor IllegalMappingException().
     * </p>
     * <p>
     * Verify: IllegalMappingException can be instantiated.
     * </p>
     */
    public void testIllegalMappingException1() {
        IllegalMappingException exception = new IllegalMappingException();
        assertNotNull("Unable to instantiated IllegalMappingException.", exception);
    }

    /**
     * <p>
     * Test constructor IllegalMappingException(String message).
     * </p>
     * <p>
     * Verify: IllegalMappingException can be instantiated correctly.
     * </p>
     */
    public void testIllegalMappingException2() {
        IllegalMappingException exception = new IllegalMappingException("TEST");
        assertNotNull("Unable to instantiated IllegalMappingException.", exception);
        assertEquals("IllegalMappingException.message is initialized correctly.", exception.getMessage(), "TEST");
    }

    /**
     * <p>
     * Test constructor IllegalMappingException(String message, Exception
     * cause).
     * </p>
     * <p>
     * Verify: IllegalMappingException can be instantiated correctly.
     * </p>
     */
    public void testIllegalMappingException3() {
        IllegalMappingException exception = new IllegalMappingException("TEST", nullException);
        assertNotNull("Unable to instantiated IllegalMappingException.", exception);
        assertEquals("IllegalMappingException.message is not initialized correctly.", exception.getMessage(), "TEST");
        assertEquals("IllegalMappingException.cause is not initialized correctly.",
                exception.getCause(), nullException);
    }

    /**
     * <p>
     * Test constructor IllegalMappingException(String message, Exception
     * cause).
     * </p>
     * <p>
     * Verify: IllegalMappingException can be instantiated correctly.
     * </p>
     */
    public void testIllegalMappingException4() {
        IllegalMappingException exception = new IllegalMappingException("   ", null);
        assertNotNull("Unable to instantiated IllegalMappingException.", exception);
        assertEquals("IllegalMappingException.message is not initialized correctly.", exception.getMessage(), "   ");
        assertNull("IllegalMappingException.cause is not initialized correctly.", exception.getCause());
    }

    /**
     * <p>
     * Test constructor IllegalMappingException(String message, Exception
     * cause).
     * </p>
     * <p>
     * Verify: IllegalMappingException can be instantiated correctly.
     * </p>
     */
    public void testIllegalMappingException5() {
        IllegalMappingException exception = new IllegalMappingException(null, null);
        assertNotNull("Unable to instantiated IllegalMappingException.", exception);
        assertNull("IllegalMappingException.message is not initialized correctly.", exception.getMessage());
        assertNull("IllegalMappingException.cause is not initialized correctly.", exception.getCause());
    }

}
