/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.configuration;

import junit.framework.TestCase;

import com.topcoder.util.errorhandling.ExceptionData;

/**
 * The unit test of PropertyNotFoundException.
 *
 * @author sparemax
 * @version 1.1
 * @since 1.1
 */
public class PropertyNotFoundExceptionTests extends TestCase {
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
     * Inheritance test for class <code>PropertyNotFoundException</code>.
     * </p>
     *
     * <p>
     * Verifies <code>PropertyNotFoundException</code>. subclasses <code>ConfigurationException</code>.
     * </p>
     */
    public void testPropertyNotFoundExceptionInheritance() {
        assertTrue("PropertyNotFoundException does not subclass ConfigurationException.",
            new PropertyNotFoundException(ERROR_MESSAGE) instanceof ConfigurationException);
    }

    /**
     * <p>
     * Accuracy test for constructor <code>PropertyNotFoundException(String)</code>.
     * </p>
     *
     * <p>
     * Verifies the error message is properly propagated.
     * </p>
     */
    public void testPropertyNotFoundExceptionString() {
        PropertyNotFoundException e = new PropertyNotFoundException(ERROR_MESSAGE);

        assertEquals("Error message is not properly propagated to super class.", ERROR_MESSAGE, e.getMessage());
    }

    /**
     * <p>
     * Accuracy test for constructor <code>PropertyNotFoundException(String, Throwable)</code>.
     * </p>
     *
     * <p>
     * Verifies the error message and cause are properly propagated.
     * </p>
     */
    public void testPropertyNotFoundExceptionStringThrowable() {
        Throwable cause = new Exception();
        PropertyNotFoundException e = new PropertyNotFoundException(ERROR_MESSAGE, cause);

        assertEquals("Error message is not properly propagated to super class.", ERROR_MESSAGE, e.getMessage());
        assertSame("Error cause is not properly propagated to super class.", cause, e.getCause());
    }

    /**
     * <p>
     * Accuracy test for constructor <code>PropertyNotFoundException(String, ExceptionData)</code>.
     * </p>
     *
     * <p>
     * Verifies the error message and exception data are properly propagated.
     * </p>
     */
    public void testPropertyNotFoundExceptionStringExceptionData() {
        PropertyNotFoundException e = new PropertyNotFoundException(ERROR_MESSAGE, EXCEPTION_DATA);

        assertEquals("Error message is not properly propagated to super class.", ERROR_MESSAGE, e.getMessage());
        assertSame("Exception data is not properly propagated to super class.",
            APPLICATION_CODE, e.getApplicationCode());
    }

    /**
     * <p>
     * Accuracy test for constructor <code>PropertyNotFoundException(String, Throwable, ExceptionData)</code>.
     * </p>
     *
     * <p>
     * Verifies the error message, exception data and cause are properly propagated.
     * </p>
     */
    public void testPropertyNotFoundExceptionStringThrowableExceptionData() {
        Throwable cause = new Exception();
        PropertyNotFoundException e = new PropertyNotFoundException(ERROR_MESSAGE, cause, EXCEPTION_DATA);

        assertEquals("Error message is not properly propagated to super class.", ERROR_MESSAGE, e.getMessage());
        assertSame("Error cause is not properly propagated to super class.", cause, e.getCause());
        assertSame("Exception data is not properly propagated to super class.",
            APPLICATION_CODE, e.getApplicationCode());
    }
}
