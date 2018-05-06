/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.persistence;

import junit.framework.TestCase;

import com.topcoder.management.review.persistence.logging.LogMessage;

/**
 * <p>
 * This class contains Unit tests for LogMessage.
 * </p>
 * @author TCSDEVELOPER
 * @version 1.2
 */
public class LogMessageTest extends TestCase {

    /**
     * <p>
     * Represents LogMessage instance for testing.
     * </p>
     */
    private LogMessage logMessage;

    /**
     * <p>
     * Sets up the test environment.
     * </p>
     * @throws Exception if any error occurs
     */
    protected void setUp() throws Exception {
        logMessage = new LogMessage(1L, "admin", "message", new NullPointerException("just for testing."));
    }

    /**
     * <p>
     * Tears down the test environment.
     * </p>
     * @throws Exception if any error occurs
     */
    protected void tearDown() throws Exception {
        logMessage = null;
    }

    /**
     * <p>
     * Tests LogMessage constructor with valid arguments passed.
     * </p>
     * <p>
     * LogMessage instance should be created successfully. No exception is expected.
     * </p>
     */
    public void testConstructor1() {
        assertNotNull("LogMessage instance should be created successfully.", new LogMessage(1L, "admin", "message"));
    }

    /**
     * <p>
     * Tests LogMessage constructor with valid arguments and error passed.
     * </p>
     * <p>
     * LogMessage instance should be created successfully. No exception is expected.
     * </p>
     */
    public void testConstructor2() {
        assertNotNull("LogMessage instance should be created successfully.", new LogMessage(1L, "admin", "message",
                new NullPointerException("just for testing.")));
    }

    /**
     * <p>
     * Tests {@link LogMessage#getError()}.
     * </p>
     * <p>
     * Error should be retrieved successfully. No exception is expected.
     * </p>
     */
    public void testGetError() {
        assertNotNull("Error should be retrieved successfully.", logMessage.getError());
    }

    /**
     * <p>
     * Tests {@link LogMessage#getMessage()}.
     * </p>
     * <p>
     * Message should be retrieved successfully. No exception is expected.
     * </p>
     */
    public void testGetMessage() {
        assertNotNull("Message should be retrieved successfully.", logMessage.getMessage());
    }

    /**
     * <p>
     * Tests {@link LogMessage#getOperator()}.
     * </p>
     * <p>
     * Operator should be retrieved successfully. No exception is expected.
     * </p>
     */
    public void testGetOperator() {
        assertNotNull("Operator should be retrieved successfully.", logMessage.getOperator());
    }

    /**
     * <p>
     * Tests {@link LogMessage#getReviewId()}.
     * </p>
     * <p>
     * ReviewId should be retrieved successfully. No exception is expected.
     * </p>
     */
    public void testGetReviewId() {
        assertNotNull("ReviewId should be retrieved successfully.", logMessage.getReviewId());
    }

    /**
     * <p>
     * Tests {@link LogMessage#getLogMessage()}.
     * </p>
     * <p>
     * Log message should be retrieved successfully. No exception is expected.
     * </p>
     */
    public void testGetLogMessage() {
        assertNotNull("Log message should be retrieved successfully.", logMessage.getLogMessage());
    }

    /**
     * <p>
     * Tests {@link LogMessage#getExceptionStackTrace(Throwable)} with valid exception passed.
     * </p>
     * <p>
     * Log message should be retrieved successfully. No exception is expected.
     * </p>
     */
    public void testGetExceptionStackTrace() {
        assertNotNull("Exception stack trace should be retrieved successfully.",
                LogMessage.getExceptionStackTrace(new NullPointerException("just for testing.")));
    }

    /**
     * <p>
     * Tests {@link LogMessage#toString()}.
     * </p>
     * <p>
     * String representation of log message should be retrieved successfully. No exception is expected.
     * </p>
     */
    public void testToString() {
        assertNotNull("String representation of log message should be retrieved successfully", logMessage.toString());
    }
}
