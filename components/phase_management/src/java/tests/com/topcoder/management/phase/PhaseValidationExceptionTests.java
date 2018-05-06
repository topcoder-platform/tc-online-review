/*
 * Copyright (C) 2006-2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.phase;

import junit.framework.TestCase;

import com.topcoder.util.errorhandling.ExceptionData;

/**
 * Tests the PhaseValidationException class.
 * @author RachaelLCook, sokol
 * @version 1.1
 */
public class PhaseValidationExceptionTests extends TestCase {

    /**
     * <p>
     * Represents PhaseValidationException instance for testing.
     * </p>
     * @since 1.1
     */
    private PhaseValidationException exception;

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
        exception = new PhaseValidationException(message, cause, data);
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
        assertEquals("invalid return from getMessage()", new PhaseValidationException("message").getMessage(),
                "message");
    }

    /**
     * <p>
     * Tests PhaseValidationException constructor with passed message and exception data.
     * </p>
     * <p>
     * PhaseValidationException instance should be created successfully.
     * </p>
     * @since 1.1
     */
    public void testCtorMessageExceptionData() {
        exception = new PhaseValidationException(message, data);
        assertNotNull("PhaseValidationException instance should be created successfully.", exception);
        assertSame("Message should be set successfully.", message, exception.getMessage());
    }

    /**
     * <p>
     * Tests PhaseValidationException constructor with passed message, exception data and cause.
     * </p>
     * <p>
     * PhaseValidationException instance should be created successfully.
     * </p>
     * @since 1.1
     */
    public void testCtorMessageExceptionDataCause() {
        assertNotNull("PhaseValidationException instance should be created successfully.", exception);
        assertSame("Message should be set successfully.", message, exception.getMessage());
        assertSame("Cause should be set successfully.", cause, exception.getCause());
    }

    /**
     * <p>
     * Tests PhaseValidationException constructor with passed message and cause.
     * </p>
     * <p>
     * PhaseValidationException instance should be created successfully.
     * </p>
     * @since 1.1
     */
    public void testCtorMessageCause() {
        exception = new PhaseValidationException(message, cause);
        assertNotNull("PhaseValidationException instance should be created successfully.", exception);
        assertSame("Message should be set successfully.", message, exception.getMessage());
        assertSame("Cause should be set successfully.", cause, exception.getCause());
    }
}
