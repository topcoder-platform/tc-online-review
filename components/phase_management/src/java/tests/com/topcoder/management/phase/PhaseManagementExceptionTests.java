/*
 * Copyright (C) 2006-2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.phase;

import junit.framework.TestCase;

import com.topcoder.util.errorhandling.ExceptionData;

/**
 * Tests the PhaseManagementException class.
 * @author RachaelLCook, sokol
 * @version 1.1
 */
public class PhaseManagementExceptionTests extends TestCase {

    /**
     * <p>
     * Represents PhaseManagementException instance for testing.
     * </p>
     * @since 1.1
     */
    private PhaseManagementException exception;

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
        exception = new PhaseManagementException(message, cause, data);
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
     * Tests that the message and wrapped exception are set correctly by the constructor.
     */
    public void testMessage() {
        assertEquals("invalid return from getMessage()", new PhaseManagementException("message").getMessage(),
                "message");
        RuntimeException rex = new RuntimeException("rte");
        PhaseManagementException ex = new PhaseManagementException("message2", rex);
        assertEquals("invalid return from getCause()", ex.getCause(), rex);
    }

    /**
     * <p>
     * Tests PhaseManagementException constructor with passed message and exception data.
     * </p>
     * <p>
     * PhaseManagementException instance should be created successfully.
     * </p>
     * @since 1.1
     */
    public void testCtorMessageExceptionData() {
        exception = new PhaseManagementException(message, data);
        assertNotNull("PhaseManagementException instance should be created successfully.", exception);
        assertSame("Message should be set successfully.", message, exception.getMessage());
    }

    /**
     * <p>
     * Tests ConfigurationException constructor with passed message, exception data and cause.
     * </p>
     * <p>
     * PhaseManagementException instance should be created successfully.
     * </p>
     * @since 1.1
     */
    public void testCtorMessageExceptionDataCause() {
        assertNotNull("PhaseManagementException instance should be created successfully.", exception);
        assertSame("Message should be set successfully.", message, exception.getMessage());
        assertSame("Cause should be set successfully.", cause, exception.getCause());
    }
}
