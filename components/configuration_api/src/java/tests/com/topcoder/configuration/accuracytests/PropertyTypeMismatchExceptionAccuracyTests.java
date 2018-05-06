/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.configuration.accuracytests;

import com.topcoder.configuration.ConfigurationException;
import com.topcoder.configuration.PropertyTypeMismatchException;
import com.topcoder.util.errorhandling.ExceptionData;

import junit.framework.TestCase;

/**
 * Accuracy test for PropertyTypeMismatchException class.
 *
 *
 * @author TCSDEVELOPER
 * @version 1.1
 * @since 1.1
 */
public class PropertyTypeMismatchExceptionAccuracyTests extends TestCase {
    /** Represents the error message used for testing. */
    private static final String ERROR_MESSAGE = "test error message";

    /**
     * Represents the exception data.
     *
     * @since 1.1
     */
    private static final ExceptionData DATA = new ExceptionData();

    /**
     * Represents the application code set in exception data.
     *
     * @since 1.1
     */
    private static final String CODE = "ID";

    /**
     * <p>
     * Initializes the exception data.
     * </p>
     *
     * @since 1.1
     */
    static {
        DATA.setApplicationCode(CODE);
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
        PropertyTypeMismatchException spe = new PropertyTypeMismatchException(ERROR_MESSAGE);
        assertNotNull("Unable to instantiate PropertyTypeMismatchException.", spe);
        assertEquals("Error message is not properly propagated to super class.", ERROR_MESSAGE, spe.getMessage());
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
    public void testPropertyTypeMismatchExceptionStringThrowable() {
        Throwable cause = new Exception();
        PropertyTypeMismatchException spe = new PropertyTypeMismatchException(ERROR_MESSAGE, cause);
        assertNotNull("Unable to instantiate PropertyTypeMismatchException.", spe);
    }

    /**
     * Accuracy test for ConfigurationException(String message, ExceptionData data).
     *
     * @since 1.1
     */
    public void testCtor3() {
        PropertyTypeMismatchException e = new PropertyTypeMismatchException(ERROR_MESSAGE, DATA);

        assertEquals("The message is incorrect.", ERROR_MESSAGE, e.getMessage());
        assertNull("The cause is incorrect.", e.getCause());
        assertSame("The data is incorrect.", CODE, e.getApplicationCode());
    }

    /**
     * Accuracy test for PropertyTypeMismatchException(String message, Throwable cause, ExceptionData data).
     *
     * @since 1.1
     */
    public void testCtor4() {
        Throwable cause = new Exception();
        PropertyTypeMismatchException e = new PropertyTypeMismatchException(ERROR_MESSAGE, cause, DATA);

        assertEquals("The message is incorrect.", ERROR_MESSAGE, e.getMessage());
        assertSame("The cause is incorrect.", cause, e.getCause());
        assertSame("The data is incorrect.", CODE, e.getApplicationCode());
    }
}
