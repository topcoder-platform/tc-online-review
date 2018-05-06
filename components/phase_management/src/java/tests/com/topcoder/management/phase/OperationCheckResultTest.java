/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.phase;

import junit.framework.TestCase;

/**
 * <p>
 * This class contains test cases for OperationCheckResult.
 * </p>
 * @author sokol
 * @version 1.0
 */
public class OperationCheckResultTest extends TestCase {

    /**
     * <p>
     * Represents OperationCheckResult instance for testing.
     * </p>
     */
    private OperationCheckResult operationCheckResult;

    /**
     * <p>
     * Represents string message for testing.
     * </p>
     */
    private String message;

    /**
     * <p>
     * Sets up the test environment.
     * </p>
     */
    protected void setUp() {
        message = "some error occurs";
        operationCheckResult = new OperationCheckResult(message);
    }

    /**
     * <p>
     * Tears down the test environment.
     * </p>
     */
    protected void tearDown() {
        message = null;
        operationCheckResult = null;
    }

    /**
     * <p>
     * Tests OperationCheckResult with passed valid message.
     * </p>
     * <p>
     * OperationCheckResult instance should be created successfully.
     * </p>
     */
    public void testConstructor() {
        assertNotNull("OperationCheckResult instance should be created successfully.", operationCheckResult);
        assertSame("Message should be set successfully.", message, operationCheckResult.getMessage());
        assertFalse("Result should not be success.", operationCheckResult.isSuccess());
    }

    /**
     * <p>
     * Tests OperationCheckResult with passed valid message and false result.
     * </p>
     * <p>
     * OperationCheckResult instance should be created successfully.
     * </p>
     */
    public void testConstructor1() {
        operationCheckResult = new OperationCheckResult(false, message);
        assertNotNull("OperationCheckResult instance should be created successfully.", operationCheckResult);
        assertSame("Message should be set successfully.", message, operationCheckResult.getMessage());
        assertFalse("Result should not be success.", operationCheckResult.isSuccess());
    }

    /**
     * <p>
     * Tests OperationCheckResult with passed null message and true result.
     * </p>
     * <p>
     * OperationCheckResult instance should be created successfully.
     * </p>
     */
    public void testConstructor2() {
        operationCheckResult = new OperationCheckResult(true, null);
        assertNotNull("OperationCheckResult instance should be created successfully.", operationCheckResult);
        assertNull("Message should be set successfully.", operationCheckResult.getMessage());
        assertTrue("Result should be success.", operationCheckResult.isSuccess());
    }

    /**
     * <p>
     * Tests OperationCheckResult with passed null message and false result.
     * </p>
     * <p>
     * IllegalArgumentException exception is expected.
     * </p>
     */
    public void testConstructor_NullMessageFalseResult() {
        try {
            operationCheckResult = new OperationCheckResult(false, null);
            fail("IllegalArgumentException exception is expected.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * <p>
     * Tests OperationCheckResult with passed not null message and true result.
     * </p>
     * <p>
     * IllegalArgumentException exception is expected.
     * </p>
     */
    public void testConstructor_NotNullMessageTrueResult() {
        try {
            operationCheckResult = new OperationCheckResult(true, message);
            fail("IllegalArgumentException exception is expected.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * <p>
     * Tests OperationCheckResult with passed empty message and false result.
     * </p>
     * <p>
     * IllegalArgumentException exception is expected.
     * </p>
     */
    public void testConstructor_EmptyMessageFalseResult() {
        try {
            operationCheckResult = new OperationCheckResult(false, "");
            fail("IllegalArgumentException exception is expected.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * <p>
     * Tests OperationCheckResult with passed empty message.
     * </p>
     * <p>
     * IllegalArgumentException exception is expected.
     * </p>
     */
    public void testConstructor_EmptyMessage() {
        try {
            operationCheckResult = new OperationCheckResult("");
            fail("IllegalArgumentException exception is expected.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * <p>
     * Tests OperationCheckResult with passed null message.
     * </p>
     * <p>
     * IllegalArgumentException exception is expected.
     * </p>
     */
    public void testConstructor_NullMessage() {
        try {
            operationCheckResult = new OperationCheckResult(null);
            fail("IllegalArgumentException exception is expected.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}
