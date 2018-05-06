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
 * Unit tests for <code>DistributionScriptCommandExecutionException</code> class.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class DistributionScriptCommandExecutionExceptionTest extends TestCase {
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
        return new TestSuite(DistributionScriptCommandExecutionExceptionTest.class);
    }

    /**
     * <p>
     * Accuracy test for constructor <code>DistributionScriptCommandExecutionException(String)</code>.
     * </p>
     * <p>
     * Verify that the exception can be created successfully.
     * </p>
     */
    public void testCtorStr() {
        DistributionScriptCommandExecutionException exception = new
            DistributionScriptCommandExecutionException(MESSAGE);
        assertNotNull("Unable to create DistributionScriptCommandExecutionException instance.", exception);
        assertTrue("Should be instance of DistributionToolException.", exception instanceof DistributionToolException);
        assertEquals("The error message should match.", MESSAGE, exception.getMessage());
    }

    /**
     * <p>
     * Accuracy test for constructor <code>DistributionScriptCommandExecutionException(String)</code>.
     * </p>
     * <p>
     * Verify that the exception can be created successfully with null message.
     * </p>
     */
    public void testCtorStr_Null() {
        DistributionScriptCommandExecutionException exception = new DistributionScriptCommandExecutionException(
                (String) null);
        assertNotNull("Unable to create DistributionScriptCommandExecutionException instance.", exception);
        assertTrue("Should be instance of DistributionToolException.", exception instanceof DistributionToolException);
        assertNull("The error message should match.", exception.getMessage());
    }

    /**
     * <p>
     * Accuracy test for constructor <code>DistributionScriptCommandExecutionException(String, ExceptionData)</code>.
     * </p>
     * <p>
     * Verify that the exception can be created successfully.
     * </p>
     */
    public void testCtorStrExp() {
        DistributionScriptCommandExecutionException exception = new DistributionScriptCommandExecutionException(
                MESSAGE, DATA);
        assertNotNull("Unable to create DistributionScriptCommandExecutionException instance.", exception);
        assertTrue("Should be instance of DistributionToolException.", exception instanceof DistributionToolException);
        assertEquals("The error message should match.", MESSAGE, exception.getMessage());
    }

    /**
     * <p>
     * Accuracy test for constructor <code>DistributionScriptCommandExecutionException(String, ExceptionData)</code>.
     * </p>
     * <p>
     * Verify that the exception can be created successfully with null message and exception data.
     * </p>
     */
    public void testCtorStrExp_Null() {
        DistributionScriptCommandExecutionException exception = new DistributionScriptCommandExecutionException(null,
                (ExceptionData) null);
        assertNotNull("Unable to create DistributionScriptCommandExecutionException instance.", exception);
        assertTrue("Should be instance of DistributionToolException.", exception instanceof DistributionToolException);
        assertNull("The error message should match.", exception.getMessage());
    }

    /**
     * <p>
     * Accuracy test for constructor <code>DistributionScriptCommandExecutionException(String, Throwable)</code>.
     * </p>
     * <p>
     * Verify that the exception can be created successfully.
     * </p>
     */
    public void testCtorStrThrowable() {
        DistributionScriptCommandExecutionException exception = new DistributionScriptCommandExecutionException(
                MESSAGE, CAUSE);
        assertNotNull("Unable to create DistributionScriptCommandExecutionException instance.", exception);
        assertTrue("Should be instance of DistributionToolException.", exception instanceof DistributionToolException);
        assertEquals("The error message should match.", MESSAGE, exception.getMessage());
        assertEquals("The inner cause should match.", CAUSE, exception.getCause());
    }

    /**
     * <p>
     * Accuracy test for constructor <code>DistributionScriptCommandExecutionException(String, Throwable)</code>.
     * </p>
     * <p>
     * Verify that the exception can be created successfully with null message and cause.
     * </p>
     */
    public void testCtorStrThrowable_Null() {
        DistributionScriptCommandExecutionException exception = new DistributionScriptCommandExecutionException(null,
                (Throwable) null);
        assertNotNull("Unable to create DistributionScriptCommandExecutionException instance.", exception);
        assertTrue("Should be instance of DistributionToolException.", exception instanceof DistributionToolException);
        assertNull("The error message should match.", exception.getMessage());
        assertNull("The inner cause should match.", exception.getCause());
    }

    /**
     * <p>
     * Accuracy test for constructor <code>DistributionScriptCommandExecutionException(String, Throwable,
     * ExceptionData)</code>.
     * </p>
     * <p>
     * Verify that the exception can be created successfully.
     * </p>
     */
    public void testCtorStrThrowableExp() {
        DistributionScriptCommandExecutionException exception = new DistributionScriptCommandExecutionException(
                MESSAGE, CAUSE, DATA);
        assertNotNull("Unable to create DistributionScriptCommandExecutionException instance.", exception);
        assertTrue("Should be instance of DistributionToolException.", exception instanceof DistributionToolException);
        assertEquals("The error message should match.", MESSAGE, exception.getMessage());
        assertEquals("The inner cause should match.", CAUSE, exception.getCause());
    }

    /**
     * <p>
     * Accuracy test for constructor <code>DistributionScriptCommandExecutionException(String, Throwable,
     * ExceptionData)</code>.
     * </p>
     * <p>
     * Verify that the exception can be created successfully with null arguments.
     * </p>
     */
    public void testCtorStrThrowableExp_Null() {
        DistributionScriptCommandExecutionException exception = new DistributionScriptCommandExecutionException(null,
                null, null);
        assertNotNull("Unable to create DistributionScriptCommandExecutionException instance.", exception);
        assertTrue("Should be instance of DistributionToolException.", exception instanceof DistributionToolException);
        assertNull("The error message should match.", exception.getMessage());
        assertNull("The inner cause should match.", exception.getCause());
    }
}
