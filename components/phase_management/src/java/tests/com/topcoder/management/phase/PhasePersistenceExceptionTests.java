/*
 * Copyright (C) 2006-2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.phase;

import junit.framework.TestCase;

import com.topcoder.util.errorhandling.ExceptionData;

/**
 * Tests the PhasePersistenceException class.
 * @author RachaelLCook, sokol
 * @version 1.1
 */
public class PhasePersistenceExceptionTests extends TestCase {

    /**
     * <p>
     * Represents PhasePersistenceException instance for testing.
     * </p>
     * @since 1.1
     */
    private PhasePersistenceException exception;

    /**
     * <p>
     * Represents error message for testing.
     * </p>
     * @since 1.1
     */
    private String message;

    /**
     * <p>
     * Represents error cause for testing.
     * </p>
     * @since 1.1
     */
    private Throwable cause;

    /**
     * <p>
     * Represents exception data for testing.
     * </p>
     * @since 1.1
     */
    private ExceptionData data;

    /**
     * <p>
     * Sets up the test environment.
     * </p>
     */
    protected void setUp() {
        message = "just for testing.";
        data = new ExceptionData();
        cause = new NullPointerException();
        exception = new PhasePersistenceException(message, cause, data);
    }

    /**
     * <p>
     * Tears down the test environment.
     * </p>
     */
    protected void tearDown() {
        message = null;
        cause = null;
        data = null;
        exception = null;
    }

    /**
     * Tests that the message was set correctly.
     */
    public void testMessage() {
        assertEquals("invalid return from getMessage()", new PhasePersistenceException("message").getMessage(),
                "message");
    }

    /**
     * <p>
     * Tests PhasePersistenceException constructor with passed message and exception data.
     * </p>
     * <p>
     * PhasePersistenceException instance should be created successfully.
     * </p>
     * @since 1.1
     */
    public void testCtorMessageExceptionData() {
        exception = new PhasePersistenceException(message, data);
        assertNotNull("PhasePersistenceException instance should be created successfully.", exception);
        assertSame("Message should be set successfully.", message, exception.getMessage());
    }

    /**
     * <p>
     * Tests PhasePersistenceException constructor with passed message, exception data and cause.
     * </p>
     * <p>
     * PhasePersistenceException instance should be created successfully.
     * </p>
     * @since 1.1
     */
    public void testCtorMessageExceptionDataCause() {
        assertNotNull("PhasePersistenceException instance should be created successfully.", exception);
        assertSame("Message should be set successfully.", message, exception.getMessage());
        assertSame("Cause should be set successfully.", cause, exception.getCause());
    }

    /**
     * <p>
     * Tests PhasePersistenceException constructor with passed message and cause.
     * </p>
     * <p>
     * PhasePersistenceException instance should be created successfully.
     * </p>
     * @since 1.1
     */
    public void testCtorMessageCause() {
        exception = new PhasePersistenceException(message, cause);
        assertNotNull("PhasePersistenceException instance should be created successfully.", exception);
        assertSame("Message should be set successfully.", message, exception.getMessage());
        assertSame("Cause should be set successfully.", cause, exception.getCause());
    }
}
