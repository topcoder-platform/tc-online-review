/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.distribution;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.util.errorhandling.ExceptionData;

/**
 * <p>
 * Unit tests for <code>InputFileNotFoundException</code> class.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class InputFileNotFoundExceptionTest extends TestCase {
    /**
     * <p>
     * The error message used for testing.
     * </p>
     */
    private static final String MESSAGE = "the error message";

    /**
     * <p>
     * The ExceptionData used for testing.
     * </p>
     */
    private static final ExceptionData DATA = new ExceptionData();

    /**
     * <p>
     * The inner exception for testing.
     * </p>
     */
    private static final Throwable CAUSE = new Exception();

    /**
     * <p>
     * Returns the test suite of this class.
     * </p>
     *
     * @return the test suite of this class.
     */
    public static Test suite() {
        return new TestSuite(InputFileNotFoundExceptionTest.class);
    }

    /**
     * <p>
     * Accuracy test for constructor <code>InputFileNotFoundException(String)</code>.
     * </p>
     * <p>
     * Verify that the exception can be created successfully.
     * </p>
     */
    public void testCtorStr() {
        InputFileNotFoundException exception = new InputFileNotFoundException(MESSAGE);
        assertNotNull("Unable to create InputFileNotFoundException instance.", exception);
        assertTrue("Should be instance of DistributionScriptCommandExecutionException.",
                exception instanceof DistributionScriptCommandExecutionException);
        assertEquals("The error message should match.", MESSAGE, exception.getMessage());
    }

    /**
     * <p>
     * Accuracy test for constructor <code>InputFileNotFoundException(String)</code>.
     * </p>
     * <p>
     * Verify that the exception can be created successfully with null message.
     * </p>
     */
    public void testCtorStr_Null() {
        InputFileNotFoundException exception = new InputFileNotFoundException((String) null);
        assertNotNull("Unable to create InputFileNotFoundException instance.", exception);
        assertTrue("Should be instance of DistributionScriptCommandExecutionException.",
                exception instanceof DistributionScriptCommandExecutionException);
        assertNull("The error message should match.", exception.getMessage());
    }

    /**
     * <p>
     * Accuracy test for constructor <code>InputFileNotFoundException(String, ExceptionData)</code>.
     * </p>
     * <p>
     * Verify that the exception can be created successfully.
     * </p>
     */
    public void testCtorStrExp() {
        InputFileNotFoundException exception = new InputFileNotFoundException(MESSAGE, DATA);
        assertNotNull("Unable to create InputFileNotFoundException instance.", exception);
        assertTrue("Should be instance of DistributionScriptCommandExecutionException.",
                exception instanceof DistributionScriptCommandExecutionException);
        assertEquals("The error message should match.", MESSAGE, exception.getMessage());
    }

    /**
     * <p>
     * Accuracy test for constructor <code>InputFileNotFoundException(String, ExceptionData)</code>.
     * </p>
     * <p>
     * Verify that the exception can be created successfully with null message and exception data.
     * </p>
     */
    public void testCtorStrExp_Null() {
        InputFileNotFoundException exception = new InputFileNotFoundException(null, (ExceptionData) null);
        assertNotNull("Unable to create InputFileNotFoundException instance.", exception);
        assertTrue("Should be instance of DistributionScriptCommandExecutionException.",
                exception instanceof DistributionScriptCommandExecutionException);
        assertNull("The error message should match.", exception.getMessage());
    }

    /**
     * <p>
     * Accuracy test for constructor <code>InputFileNotFoundException(String, Throwable)</code>.
     * </p>
     * <p>
     * Verify that the exception can be created successfully.
     * </p>
     */
    public void testCtorStrThrowable() {
        InputFileNotFoundException exception = new InputFileNotFoundException(MESSAGE, CAUSE);
        assertNotNull("Unable to create InputFileNotFoundException instance.", exception);
        assertTrue("Should be instance of DistributionScriptCommandExecutionException.",
                exception instanceof DistributionScriptCommandExecutionException);
        assertEquals("The error message should match.", MESSAGE, exception.getMessage());
        assertEquals("The inner cause should match.", CAUSE, exception.getCause());
    }

    /**
     * <p>
     * Accuracy test for constructor <code>InputFileNotFoundException(String, Throwable)</code>.
     * </p>
     * <p>
     * Verify that the exception can be created successfully with null message and cause.
     * </p>
     */
    public void testCtorStrThrowable_Null() {
        InputFileNotFoundException exception = new InputFileNotFoundException(null, (Throwable) null);
        assertNotNull("Unable to create InputFileNotFoundException instance.", exception);
        assertTrue("Should be instance of DistributionScriptCommandExecutionException.",
                exception instanceof DistributionScriptCommandExecutionException);
        assertNull("The error message should match.", exception.getMessage());
        assertNull("The inner cause should match.", exception.getCause());
    }

    /**
     * <p>
     * Accuracy test for constructor <code>InputFileNotFoundException(String, Throwable,
     * ExceptionData)</code>.
     * </p>
     * <p>
     * Verify that the exception can be created successfully.
     * </p>
     */
    public void testCtorStrThrowableExp() {
        InputFileNotFoundException exception = new InputFileNotFoundException(MESSAGE, CAUSE, DATA);
        assertNotNull("Unable to create InputFileNotFoundException instance.", exception);
        assertTrue("Should be instance of DistributionScriptCommandExecutionException.",
                exception instanceof DistributionScriptCommandExecutionException);
        assertEquals("The error message should match.", MESSAGE, exception.getMessage());
        assertEquals("The inner cause should match.", CAUSE, exception.getCause());
    }

    /**
     * <p>
     * Accuracy test for constructor <code>InputFileNotFoundException(String, Throwable,
     * ExceptionData)</code>.
     * </p>
     * <p>
     * Verify that the exception can be created successfully with null arguments.
     * </p>
     */
    public void testCtorStrThrowableExp_Null() {
        InputFileNotFoundException exception = new InputFileNotFoundException(null, null, null);
        assertNotNull("Unable to create InputFileNotFoundException instance.", exception);
        assertTrue("Should be instance of DistributionScriptCommandExecutionException.",
                exception instanceof DistributionScriptCommandExecutionException);
        assertNull("The error message should match.", exception.getMessage());
        assertNull("The inner cause should match.", exception.getCause());
    }
}
