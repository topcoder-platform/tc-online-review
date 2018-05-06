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
 * Unit tests for <code>MissingInputParameterException</code> class.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class MissingInputParameterExceptionTest extends TestCase {
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
        return new TestSuite(MissingInputParameterExceptionTest.class);
    }

    /**
     * <p>
     * Accuracy test for constructor <code>MissingInputParameterException(String)</code>.
     * </p>
     * <p>
     * Verify that the exception can be created successfully.
     * </p>
     */
    public void testCtorStr() {
        MissingInputParameterException exception = new MissingInputParameterException(MESSAGE);
        assertNotNull("Unable to create MissingInputParameterException instance.", exception);
        assertTrue("Should be instance of DistributionToolException.", exception instanceof DistributionToolException);
        assertEquals("The error message should match.", MESSAGE, exception.getMessage());
    }

    /**
     * <p>
     * Accuracy test for constructor <code>MissingInputParameterException(String)</code>.
     * </p>
     * <p>
     * Verify that the exception can be created successfully with null message.
     * </p>
     */
    public void testCtorStr_Null() {
        MissingInputParameterException exception = new MissingInputParameterException((String) null);
        assertNotNull("Unable to create MissingInputParameterException instance.", exception);
        assertTrue("Should be instance of DistributionToolException.", exception instanceof DistributionToolException);
        assertNull("The error message should match.", exception.getMessage());
    }

    /**
     * <p>
     * Accuracy test for constructor <code>MissingInputParameterException(String, ExceptionData)</code>.
     * </p>
     * <p>
     * Verify that the exception can be created successfully.
     * </p>
     */
    public void testCtorStrExp() {
        MissingInputParameterException exception = new MissingInputParameterException(MESSAGE, DATA);
        assertNotNull("Unable to create MissingInputParameterException instance.", exception);
        assertTrue("Should be instance of DistributionToolException.", exception instanceof DistributionToolException);
        assertEquals("The error message should match.", MESSAGE, exception.getMessage());
    }

    /**
     * <p>
     * Accuracy test for constructor <code>MissingInputParameterException(String, ExceptionData)</code>.
     * </p>
     * <p>
     * Verify that the exception can be created successfully with null message and exception data.
     * </p>
     */
    public void testCtorStrExp_Null() {
        MissingInputParameterException exception = new MissingInputParameterException(null, (ExceptionData) null);
        assertNotNull("Unable to create MissingInputParameterException instance.", exception);
        assertTrue("Should be instance of DistributionToolException.", exception instanceof DistributionToolException);
        assertNull("The error message should match.", exception.getMessage());
    }

    /**
     * <p>
     * Accuracy test for constructor <code>MissingInputParameterException(String, Throwable)</code>.
     * </p>
     * <p>
     * Verify that the exception can be created successfully.
     * </p>
     */
    public void testCtorStrThrowable() {
        MissingInputParameterException exception = new MissingInputParameterException(MESSAGE, CAUSE);
        assertNotNull("Unable to create MissingInputParameterException instance.", exception);
        assertTrue("Should be instance of DistributionToolException.", exception instanceof DistributionToolException);
        assertEquals("The error message should match.", MESSAGE, exception.getMessage());
        assertEquals("The inner cause should match.", CAUSE, exception.getCause());
    }

    /**
     * <p>
     * Accuracy test for constructor <code>MissingInputParameterException(String, Throwable)</code>.
     * </p>
     * <p>
     * Verify that the exception can be created successfully with null message and cause.
     * </p>
     */
    public void testCtorStrThrowable_Null() {
        MissingInputParameterException exception = new MissingInputParameterException(null, (Throwable) null);
        assertNotNull("Unable to create MissingInputParameterException instance.", exception);
        assertTrue("Should be instance of DistributionToolException.", exception instanceof DistributionToolException);
        assertNull("The error message should match.", exception.getMessage());
        assertNull("The inner cause should match.", exception.getCause());
    }

    /**
     * <p>
     * Accuracy test for constructor <code>MissingInputParameterException(String, Throwable,
     * ExceptionData)</code>.
     * </p>
     * <p>
     * Verify that the exception can be created successfully.
     * </p>
     */
    public void testCtorStrThrowableExp() {
        MissingInputParameterException exception = new MissingInputParameterException(MESSAGE, CAUSE, DATA);
        assertNotNull("Unable to create MissingInputParameterException instance.", exception);
        assertTrue("Should be instance of DistributionToolException.", exception instanceof DistributionToolException);
        assertEquals("The error message should match.", MESSAGE, exception.getMessage());
        assertEquals("The inner cause should match.", CAUSE, exception.getCause());
    }

    /**
     * <p>
     * Accuracy test for constructor <code>MissingInputParameterException(String, Throwable,
     * ExceptionData)</code>.
     * </p>
     * <p>
     * Verify that the exception can be created successfully with null arguments.
     * </p>
     */
    public void testCtorStrThrowableExp_Null() {
        MissingInputParameterException exception = new MissingInputParameterException(null, null, null);
        assertNotNull("Unable to create MissingInputParameterException instance.", exception);
        assertTrue("Should be instance of DistributionToolException.", exception instanceof DistributionToolException);
        assertNull("The error message should match.", exception.getMessage());
        assertNull("The inner cause should match.", exception.getCause());
    }
}
