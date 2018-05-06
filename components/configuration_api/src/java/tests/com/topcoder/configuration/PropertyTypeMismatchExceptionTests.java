/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.configuration;

import junit.framework.TestCase;

import com.topcoder.util.errorhandling.ExceptionData;

/**
 * The unit test of PropertyTypeMismatchException.
 *
 * @author sparemax
 * @version 1.1
 * @since 1.1
 */
public class PropertyTypeMismatchExceptionTests extends TestCase {
    /** Represents the error message used for testing. */
    private static final String ERROR_MESSAGE = "test error message";

    /** Represents the exception data. */
    private static final ExceptionData EXCEPTION_DATA = new ExceptionData();

    /** Represents the application code set in exception data. */
    private static final String APPLICATION_CODE = "Accuracy";

    /**
     * <p>
     * Initializes the exception data.
     * </p>
     */
    static {
        EXCEPTION_DATA.setApplicationCode(APPLICATION_CODE);
    }

    /**
     * <p>
     * Inheritance test for class <code>PropertyTypeMismatchException</code>.
     * </p>
     *
     * <p>
     * Verifies <code>PropertyTypeMismatchException</code>. subclasses <code>ConfigurationException</code>.
     * </p>
     */
    public void testPropertyTypeMismatchExceptionInheritance() {
        assertTrue("PropertyTypeMismatchException does not subclass ConfigurationException.",
            new PropertyTypeMismatchException(ERROR_MESSAGE) instanceof ConfigurationException);
    }

    /**
     * <p>
     * Accuracy test for constructor <code>PropertyTypeMismatchException(String)</code>.
     * </p>
     *
     * <p>
     * Verifies the error message is properly propagated.
     * </p>
     */
    public void testPropertyTypeMismatchExceptionString() {
        PropertyTypeMismatchException e = new PropertyTypeMismatchException(ERROR_MESSAGE);

        assertEquals("Error message is not properly propagated to super class.", ERROR_MESSAGE, e.getMessage());
    }

    /**
     * <p>
     * Accuracy test for constructor <code>PropertyTypeMismatchException(String, Throwable)</code>.
     * </p>
     *
     * <p>
     * Verifies the error message and cause are properly propagated.
     * </p>
     */
    public void testPropertyTypeMismatchExceptionStringThrowable() {
        Throwable cause = new Exception();
        PropertyTypeMismatchException e = new PropertyTypeMismatchException(ERROR_MESSAGE, cause);

        assertEquals("Error message is not properly propagated to super class.", ERROR_MESSAGE, e.getMessage());
        assertSame("Error cause is not properly propagated to super class.", cause, e.getCause());
    }

    /**
     * <p>
     * Accuracy test for constructor <code>PropertyTypeMismatchException(String, ExceptionData)</code>.
     * </p>
     *
     * <p>
     * Verifies the error message and exception data are properly propagated.
     * </p>
     */
    public void testPropertyTypeMismatchExceptionStringExceptionData() {
        PropertyTypeMismatchException e = new PropertyTypeMismatchException(ERROR_MESSAGE, EXCEPTION_DATA);

        assertEquals("Error message is not properly propagated to super class.", ERROR_MESSAGE, e.getMessage());
        assertSame("Exception data is not properly propagated to super class.",
            APPLICATION_CODE, e.getApplicationCode());
    }

    /**
     * <p>
     * Accuracy test for constructor <code>PropertyTypeMismatchException(String, Throwable, ExceptionData)</code>.
     * </p>
     *
     * <p>
     * Verifies the error message, exception data and cause are properly propagated.
     * </p>
     */
    public void testPropertyTypeMismatchExceptionStringThrowableExceptionData() {
        Throwable cause = new Exception();
        PropertyTypeMismatchException e = new PropertyTypeMismatchException(ERROR_MESSAGE, cause, EXCEPTION_DATA);

        assertEquals("Error message is not properly propagated to super class.", ERROR_MESSAGE, e.getMessage());
        assertSame("Error cause is not properly propagated to super class.", cause, e.getCause());
        assertSame("Exception data is not properly propagated to super class.",
            APPLICATION_CODE, e.getApplicationCode());
    }
}
