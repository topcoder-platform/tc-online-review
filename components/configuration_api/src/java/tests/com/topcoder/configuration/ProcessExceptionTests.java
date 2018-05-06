/*
 * Copyright (C) 2007, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.configuration;

import com.topcoder.util.errorhandling.ExceptionData;

import junit.framework.TestCase;


/**
 * The unit test of ProcessException.
 *
 * <p>
 * <em>Changes in 1.1:</em>
 * <ol>
 * <li>Added test cases for new constructors.</li>
 * <li>Removed useless test case testProcessException.</li>
 * <li>Fixed documentation for testProcessExceptionStringThrowable.</li>
 * </ol>
 * </p>
 *
 * @author haozhangr, sparemax
 * @version 1.1
 */
public class ProcessExceptionTests extends TestCase {
    /** Represents the error message used for testing. */
    private static final String ERROR_MESSAGE = "test error message";

    /**
     * Represents the exception data.
     *
     * @since 1.1
     */
    private static final ExceptionData EXCEPTION_DATA = new ExceptionData();

    /**
     * Represents the application code set in exception data.
     *
     * @since 1.1
     */
    private static final String APPLICATION_CODE = "Accuracy";

    /**
     * <p>
     * Initializes the exception data.
     * </p>
     *
     * @since 1.1
     */
    static {
        EXCEPTION_DATA.setApplicationCode(APPLICATION_CODE);
    }

    /**
     * <p>
     * Inheritance test for class <code>ProcessException</code>.
     * </p>
     *
     * <p>
     * Verifies <code>ProcessException</code>. subclasses <code>ConfigurationException</code>.
     * </p>
     */
    public void testProcessExceptionInheritance() {
        assertTrue("ProcessException does not subclass ConfigurationException.",
            new ProcessException(ERROR_MESSAGE) instanceof ConfigurationException);
    }

    /**
     * <p>
     * Accuracy test for constructor <code>ProcessException(String)</code>.
     * </p>
     *
     * <p>
     * Verifies the error message is properly propagated.
     * </p>
     */
    public void testProcessExceptionString() {
        ProcessException e = new ProcessException(ERROR_MESSAGE);

        assertEquals("Error message is not properly propagated to super class.", ERROR_MESSAGE, e.getMessage());
    }

    /**
     * <p>
     * Accuracy test for constructor <code>ProcessException(String, Throwable)</code>.
     * </p>
     *
     * <p>
     * Verifies the error message and cause are properly propagated.
     * </p>
     */
    public void testProcessExceptionStringThrowable() {
        Throwable cause = new Exception();
        ProcessException e = new ProcessException(ERROR_MESSAGE, cause);

        assertEquals("Error message is not properly propagated to super class.", ERROR_MESSAGE, e.getMessage());
        assertSame("Error cause is not properly propagated to super class.", cause, e.getCause());
    }

    /**
     * <p>
     * Accuracy test for constructor <code>ProcessException(String, ExceptionData)</code>.
     * </p>
     *
     * <p>
     * Verifies the error message and exception data are properly propagated.
     * </p>
     *
     * @since 1.1
     */
    public void testProcessExceptionStringExceptionData() {
        ProcessException e = new ProcessException(ERROR_MESSAGE, EXCEPTION_DATA);

        assertEquals("Error message is not properly propagated to super class.", ERROR_MESSAGE, e.getMessage());
        assertSame("Exception data is not properly propagated to super class.",
            APPLICATION_CODE, e.getApplicationCode());
    }

    /**
     * <p>
     * Accuracy test for constructor <code>ProcessException(String, Throwable, ExceptionData)</code>.
     * </p>
     *
     * <p>
     * Verifies the error message, exception data and cause are properly propagated.
     * </p>
     *
     * @since 1.1
     */
    public void testProcessExceptionStringThrowableExceptionData() {
        Throwable cause = new Exception();
        ProcessException e = new ProcessException(ERROR_MESSAGE, cause, EXCEPTION_DATA);

        assertEquals("Error message is not properly propagated to super class.", ERROR_MESSAGE, e.getMessage());
        assertSame("Error cause is not properly propagated to super class.", cause, e.getCause());
        assertSame("Exception data is not properly propagated to super class.",
            APPLICATION_CODE, e.getApplicationCode());
    }
}
