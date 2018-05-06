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
 * Unit tests for <code>PDFConversionException</code> class.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class PDFConversionExceptionTest extends TestCase {
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
        return new TestSuite(PDFConversionExceptionTest.class);
    }

    /**
     * <p>
     * Accuracy test for constructor <code>PDFConversionException(String)</code>.
     * </p>
     * <p>
     * Verify that the exception can be created successfully.
     * </p>
     */
    public void testCtorStr() {
        PDFConversionException exception = new PDFConversionException(MESSAGE);
        assertNotNull("Unable to create PDFConversionException instance.", exception);
        assertTrue("Should be instance of DistributionScriptCommandExecutionException.",
                exception instanceof DistributionScriptCommandExecutionException);
        assertEquals("The error message should match.", MESSAGE, exception.getMessage());
    }

    /**
     * <p>
     * Accuracy test for constructor <code>PDFConversionException(String)</code>.
     * </p>
     * <p>
     * Verify that the exception can be created successfully with null message.
     * </p>
     */
    public void testCtorStr_Null() {
        PDFConversionException exception = new PDFConversionException((String) null);
        assertNotNull("Unable to create PDFConversionException instance.", exception);
        assertTrue("Should be instance of DistributionScriptCommandExecutionException.",
                exception instanceof DistributionScriptCommandExecutionException);
        assertNull("The error message should match.", exception.getMessage());
    }

    /**
     * <p>
     * Accuracy test for constructor <code>PDFConversionException(String, ExceptionData)</code>.
     * </p>
     * <p>
     * Verify that the exception can be created successfully.
     * </p>
     */
    public void testCtorStrExp() {
        PDFConversionException exception = new PDFConversionException(MESSAGE, DATA);
        assertNotNull("Unable to create PDFConversionException instance.", exception);
        assertTrue("Should be instance of DistributionScriptCommandExecutionException.",
                exception instanceof DistributionScriptCommandExecutionException);
        assertEquals("The error message should match.", MESSAGE, exception.getMessage());
    }

    /**
     * <p>
     * Accuracy test for constructor <code>PDFConversionException(String, ExceptionData)</code>.
     * </p>
     * <p>
     * Verify that the exception can be created successfully with null message and exception data.
     * </p>
     */
    public void testCtorStrExp_Null() {
        PDFConversionException exception = new PDFConversionException(null, (ExceptionData) null);
        assertNotNull("Unable to create PDFConversionException instance.", exception);
        assertTrue("Should be instance of DistributionScriptCommandExecutionException.",
                exception instanceof DistributionScriptCommandExecutionException);
        assertNull("The error message should match.", exception.getMessage());
    }

    /**
     * <p>
     * Accuracy test for constructor <code>PDFConversionException(String, Throwable)</code>.
     * </p>
     * <p>
     * Verify that the exception can be created successfully.
     * </p>
     */
    public void testCtorStrThrowable() {
        PDFConversionException exception = new PDFConversionException(MESSAGE, CAUSE);
        assertNotNull("Unable to create PDFConversionException instance.", exception);
        assertTrue("Should be instance of DistributionScriptCommandExecutionException.",
                exception instanceof DistributionScriptCommandExecutionException);
        assertEquals("The error message should match.", MESSAGE, exception.getMessage());
        assertEquals("The inner cause should match.", CAUSE, exception.getCause());
    }

    /**
     * <p>
     * Accuracy test for constructor <code>PDFConversionException(String, Throwable)</code>.
     * </p>
     * <p>
     * Verify that the exception can be created successfully with null message and cause.
     * </p>
     */
    public void testCtorStrThrowable_Null() {
        PDFConversionException exception = new PDFConversionException(null, (Throwable) null);
        assertNotNull("Unable to create PDFConversionException instance.", exception);
        assertTrue("Should be instance of DistributionScriptCommandExecutionException.",
                exception instanceof DistributionScriptCommandExecutionException);
        assertNull("The error message should match.", exception.getMessage());
        assertNull("The inner cause should match.", exception.getCause());
    }

    /**
     * <p>
     * Accuracy test for constructor <code>PDFConversionException(String, Throwable,
     * ExceptionData)</code>.
     * </p>
     * <p>
     * Verify that the exception can be created successfully.
     * </p>
     */
    public void testCtorStrThrowableExp() {
        PDFConversionException exception = new PDFConversionException(MESSAGE, CAUSE, DATA);
        assertNotNull("Unable to create PDFConversionException instance.", exception);
        assertTrue("Should be instance of DistributionScriptCommandExecutionException.",
                exception instanceof DistributionScriptCommandExecutionException);
        assertEquals("The error message should match.", MESSAGE, exception.getMessage());
        assertEquals("The inner cause should match.", CAUSE, exception.getCause());
    }

    /**
     * <p>
     * Accuracy test for constructor <code>PDFConversionException(String, Throwable,
     * ExceptionData)</code>.
     * </p>
     * <p>
     * Verify that the exception can be created successfully with null arguments.
     * </p>
     */
    public void testCtorStrThrowableExp_Null() {
        PDFConversionException exception = new PDFConversionException(null, null, null);
        assertNotNull("Unable to create PDFConversionException instance.", exception);
        assertTrue("Should be instance of DistributionScriptCommandExecutionException.",
                exception instanceof DistributionScriptCommandExecutionException);
        assertNull("The error message should match.", exception.getMessage());
        assertNull("The inner cause should match.", exception.getCause());
    }
}
