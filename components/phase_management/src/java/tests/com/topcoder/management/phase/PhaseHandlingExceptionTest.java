/*
 * Copyright (C) 2006-2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.phase;

import junit.framework.TestCase;

import com.topcoder.util.errorhandling.ExceptionData;

/**
 * <p>
 * Tests the PhaseHandlingException class.
 * </p>
 * @author sokol
 * @version 1.0
 */
public class PhaseHandlingExceptionTest extends TestCase {

    /**
     * <p>
     * Represents PhaseHandlingException instance for testing.
     * </p>
     */
    private PhaseHandlingException exception;

    /**
     * <p>
     * Represents error message for testing.
     * </p>
     */
    private String message;

    /**
     * <p>
     * Represents error cause for testing.
     * </p>
     */
    private Throwable cause;

    /**
     * <p>
     * Represents exception data for testing.
     * </p>
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
        exception = new PhaseHandlingException(message, cause, data);
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
     * <p>
     * Tests PhaseHandlingException constructor with passed message.
     * </p>
     * <p>
     * PhaseHandlingException instance should be created successfully.
     * </p>
     */
    public void testCtorMessage() {
        exception = new PhaseHandlingException(message);
        assertNotNull("PhaseHandlingException instance should be created successfully.", exception);
        assertSame("Message should be set successfully.", message, exception.getMessage());
    }

    /**
     * <p>
     * Tests PhaseHandlingException constructor with passed message and exception data.
     * </p>
     * <p>
     * PhaseHandlingException instance should be created successfully.
     * </p>
     */
    public void testCtorMessageExceptionData() {
        exception = new PhaseHandlingException(message, data);
        assertNotNull("PhaseHandlingException instance should be created successfully.", exception);
        assertSame("Message should be set successfully.", message, exception.getMessage());
    }

    /**
     * <p>
     * Tests ConfigurationException constructor with passed message and cause.
     * </p>
     * <p>
     * PhaseHandlingException instance should be created successfully.
     * </p>
     */
    public void testCtorMessageCause() {
        exception = new PhaseHandlingException(message, cause);
        assertNotNull("PhaseHandlingException instance should be created successfully.", exception);
        assertSame("Message should be set successfully.", message, exception.getMessage());
        assertSame("Cause should be set successfully.", cause, exception.getCause());
    }

    /**
     * <p>
     * Tests ConfigurationException constructor with passed message, exception data and cause.
     * </p>
     * <p>
     * PhaseHandlingException instance should be created successfully.
     * </p>
     */
    public void testCtorMessageExceptionDataCause() {
        assertNotNull("PhaseHandlingException instance should be created successfully.", exception);
        assertSame("Message should be set successfully.", message, exception.getMessage());
        assertSame("Cause should be set successfully.", cause, exception.getCause());
    }
}
