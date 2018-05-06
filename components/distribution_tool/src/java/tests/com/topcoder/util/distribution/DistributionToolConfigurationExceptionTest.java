/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.distribution;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.util.errorhandling.BaseRuntimeException;
import com.topcoder.util.errorhandling.ExceptionData;

/**
 * <p>
 * Unit tests for <code>DistributionToolConfigurationException</code> class.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class DistributionToolConfigurationExceptionTest extends TestCase {
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
        return new TestSuite(DistributionToolConfigurationExceptionTest.class);
    }

    /**
     * <p>
     * Accuracy test for constructor <code>DistributionToolConfigurationException(String)</code>.
     * </p>
     * <p>
     * Verify that the exception can be created successfully.
     * </p>
     */
    public void testCtorStr() {
        DistributionToolConfigurationException exception = new DistributionToolConfigurationException(MESSAGE);
        assertNotNull("Unable to create DistributionToolConfigurationException instance.", exception);
        assertTrue("Should be instance of DistributionToolConfigurationException.",
                exception instanceof BaseRuntimeException);
        assertEquals("The error message should match.", MESSAGE, exception.getMessage());
    }

    /**
     * <p>
     * Accuracy test for constructor <code>DistributionToolConfigurationException(String)</code>.
     * </p>
     * <p>
     * Verify that the exception can be created successfully with null message.
     * </p>
     */
    public void testCtorStr_Null() {
        DistributionToolConfigurationException exception = new DistributionToolConfigurationException((String) null);
        assertNotNull("Unable to create DistributionToolConfigurationException instance.", exception);
        assertTrue("Should be instance of DistributionToolConfigurationException.",
                exception instanceof BaseRuntimeException);
        assertNull("The error message should match.", exception.getMessage());
    }

    /**
     * <p>
     * Accuracy test for constructor <code>DistributionToolConfigurationException(String, ExceptionData)</code>.
     * </p>
     * <p>
     * Verify that the exception can be created successfully.
     * </p>
     */
    public void testCtorStrExp() {
        DistributionToolConfigurationException exception = new DistributionToolConfigurationException(MESSAGE, DATA);
        assertNotNull("Unable to create DistributionToolConfigurationException instance.", exception);
        assertTrue("Should be instance of DistributionToolConfigurationException.",
                exception instanceof BaseRuntimeException);
        assertEquals("The error message should match.", MESSAGE, exception.getMessage());
    }

    /**
     * <p>
     * Accuracy test for constructor <code>DistributionToolConfigurationException(String, ExceptionData)</code>.
     * </p>
     * <p>
     * Verify that the exception can be created successfully with null message and exception data.
     * </p>
     */
    public void testCtorStrExp_Null() {
        DistributionToolConfigurationException exception = new DistributionToolConfigurationException(null,
                (ExceptionData) null);
        assertNotNull("Unable to create DistributionToolConfigurationException instance.", exception);
        assertTrue("Should be instance of DistributionToolConfigurationException.",
                exception instanceof BaseRuntimeException);
        assertNull("The error message should match.", exception.getMessage());
    }

    /**
     * <p>
     * Accuracy test for constructor <code>DistributionToolConfigurationException(String, Throwable)</code>.
     * </p>
     * <p>
     * Verify that the exception can be created successfully.
     * </p>
     */
    public void testCtorStrThrowable() {
        DistributionToolConfigurationException exception = new DistributionToolConfigurationException(MESSAGE, CAUSE);
        assertNotNull("Unable to create DistributionToolConfigurationException instance.", exception);
        assertTrue("Should be instance of DistributionToolConfigurationException.",
                exception instanceof BaseRuntimeException);
        assertEquals("The error message should match.", MESSAGE, exception.getMessage());
        assertEquals("The inner cause should match.", CAUSE, exception.getCause());
    }

    /**
     * <p>
     * Accuracy test for constructor <code>DistributionToolConfigurationException(String, Throwable)</code>.
     * </p>
     * <p>
     * Verify that the exception can be created successfully with null message and cause.
     * </p>
     */
    public void testCtorStrThrowable_Null() {
        DistributionToolConfigurationException exception = new DistributionToolConfigurationException(null,
                (Throwable) null);
        assertNotNull("Unable to create DistributionToolConfigurationException instance.", exception);
        assertTrue("Should be instance of DistributionToolConfigurationException.",
                exception instanceof BaseRuntimeException);
        assertNull("The error message should match.", exception.getMessage());
        assertNull("The inner cause should match.", exception.getCause());
    }

    /**
     * <p>
     * Accuracy test for constructor <code>DistributionToolConfigurationException(String, Throwable,
     * ExceptionData)</code>.
     * </p>
     * <p>
     * Verify that the exception can be created successfully.
     * </p>
     */
    public void testCtorStrThrowableExp() {
        DistributionToolConfigurationException exception = new DistributionToolConfigurationException(MESSAGE, CAUSE,
                DATA);
        assertNotNull("Unable to create DistributionToolConfigurationException instance.", exception);
        assertTrue("Should be instance of DistributionToolConfigurationException.",
                exception instanceof BaseRuntimeException);
        assertEquals("The error message should match.", MESSAGE, exception.getMessage());
        assertEquals("The inner cause should match.", CAUSE, exception.getCause());
    }

    /**
     * <p>
     * Accuracy test for constructor <code>DistributionToolConfigurationException(String, Throwable,
     * ExceptionData)</code>.
     * </p>
     * <p>
     * Verify that the exception can be created successfully with null arguments.
     * </p>
     */
    public void testCtorStrThrowableExp_Null() {
        DistributionToolConfigurationException exception = new DistributionToolConfigurationException(null, null, null);
        assertNotNull("Unable to create DistributionToolConfigurationException instance.", exception);
        assertTrue("Should be instance of DistributionToolConfigurationException.",
                exception instanceof BaseRuntimeException);
        assertNull("The error message should match.", exception.getMessage());
        assertNull("The inner cause should match.", exception.getCause());
    }
}
