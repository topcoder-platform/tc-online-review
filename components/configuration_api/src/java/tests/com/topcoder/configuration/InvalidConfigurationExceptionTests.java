/*
 * Copyright (C) 2007, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.configuration;

import com.topcoder.util.errorhandling.ExceptionData;

import junit.framework.TestCase;


/**
 * <p>
 * The unit test of InvalidConfigurationException.
 * </p>
 *
 * <p>
 * <em>Changes in 1.1:</em>
 * <ol>
 * <li>Added test cases for new constructors.</li>
 * <li>Removed useless test case testInvalidConfigurationException.</li>
 * <li>Fixed documentation for testInvalidConfigurationExceptionStringThrowable.</li>
 * </ol>
 * </p>
 *
 * @author haozhangr, sparemax
 * @version 1.1
 */
public class InvalidConfigurationExceptionTests extends TestCase {
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
     * Inheritance test for class <code>InvalidConfigurationException</code>.
     * </p>
     *
     * <p>
     * Verifies <code>InvalidConfigurationException</code>. subclasses <code>ConfigurationException</code>.
     * </p>
     */
    public void testInvalidConfigurationExceptionInheritance() {
        assertTrue("InvalidConfigurationException does not subclass ConfigurationException.",
            new InvalidConfigurationException(ERROR_MESSAGE) instanceof ConfigurationException);
    }

    /**
     * <p>
     * Accuracy test for constructor <code>InvalidConfigurationException(String)</code>.
     * </p>
     *
     * <p>
     * Verifies the error message is properly propagated.
     * </p>
     */
    public void testInvalidConfigurationExceptionString() {
        InvalidConfigurationException e = new InvalidConfigurationException(ERROR_MESSAGE);

        assertEquals("Error message is not properly propagated to super class.", ERROR_MESSAGE, e.getMessage());
    }

    /**
     * <p>
     * Accuracy test for constructor <code>InvalidConfigurationException(String, Throwable)</code>.
     * </p>
     *
     * <p>
     * Verifies the error message and cause are properly propagated.
     * </p>
     */
    public void testInvalidConfigurationExceptionStringThrowable() {
        Throwable cause = new Exception();
        InvalidConfigurationException e = new InvalidConfigurationException(ERROR_MESSAGE, cause);

        assertEquals("Error message is not properly propagated to super class.", ERROR_MESSAGE, e.getMessage());
        assertSame("Error cause is not properly propagated to super class.", cause, e.getCause());
    }

    /**
     * <p>
     * Accuracy test for constructor <code>InvalidConfigurationException(String, ExceptionData)</code>.
     * </p>
     *
     * <p>
     * Verifies the error message and exception data are properly propagated.
     * </p>
     *
     * @since 1.1
     */
    public void testInvalidConfigurationExceptionStringExceptionData() {
        InvalidConfigurationException e = new InvalidConfigurationException(ERROR_MESSAGE, EXCEPTION_DATA);

        assertEquals("Error message is not properly propagated to super class.", ERROR_MESSAGE, e.getMessage());
        assertSame("Exception data is not properly propagated to super class.",
            APPLICATION_CODE, e.getApplicationCode());
    }

    /**
     * <p>
     * Accuracy test for constructor <code>InvalidConfigurationException(String, Throwable, ExceptionData)</code>.
     * </p>
     *
     * <p>
     * Verifies the error message, exception data and cause are properly propagated.
     * </p>
     *
     * @since 1.1
     */
    public void testInvalidConfigurationExceptionStringThrowableExceptionData() {
        Throwable cause = new Exception();
        InvalidConfigurationException e = new InvalidConfigurationException(ERROR_MESSAGE, cause, EXCEPTION_DATA);

        assertEquals("Error message is not properly propagated to super class.", ERROR_MESSAGE, e.getMessage());
        assertSame("Error cause is not properly propagated to super class.", cause, e.getCause());
        assertSame("Exception data is not properly propagated to super class.",
            APPLICATION_CODE, e.getApplicationCode());
    }
}
