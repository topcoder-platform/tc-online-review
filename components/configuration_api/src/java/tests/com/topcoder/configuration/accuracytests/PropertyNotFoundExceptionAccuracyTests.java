/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.configuration.accuracytests;

import com.topcoder.configuration.ConfigurationException;
import com.topcoder.configuration.PropertyNotFoundException;
import com.topcoder.util.errorhandling.ExceptionData;

import junit.framework.TestCase;

/**
 * Accuracy test for PropertyNotFoundException class.
 *
 *
 * @author TCSDEVELOPER
 * @version 1.1
 * @since 1.1
 */
public class PropertyNotFoundExceptionAccuracyTests extends TestCase {
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
        PropertyNotFoundException spe = new PropertyNotFoundException(ERROR_MESSAGE);
        assertNotNull("Unable to instantiate PropertyNotFoundException.", spe);
        assertEquals("Error message is not properly propagated to super class.", ERROR_MESSAGE, spe.getMessage());
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
    public void testPropertyNotFoundExceptionStringThrowable() {
        Throwable cause = new Exception();
        PropertyNotFoundException spe = new PropertyNotFoundException(ERROR_MESSAGE, cause);
        assertNotNull("Unable to instantiate PropertyNotFoundException.", spe);
    }

    /**
     * Accuracy test for ConfigurationException(String message, ExceptionData data).
     *
     * @since 1.1
     */
    public void testCtor3() {
        PropertyNotFoundException e = new PropertyNotFoundException(ERROR_MESSAGE, DATA);

        assertEquals("The message is incorrect.", ERROR_MESSAGE, e.getMessage());
        assertNull("The cause is incorrect.", e.getCause());
        assertSame("The data is incorrect.", CODE, e.getApplicationCode());
    }

    /**
     * Accuracy test for PropertyNotFoundException(String message, Throwable cause, ExceptionData data).
     *
     * @since 1.1
     */
    public void testCtor4() {
        Throwable cause = new Exception();
        PropertyNotFoundException e = new PropertyNotFoundException(ERROR_MESSAGE, cause, DATA);

        assertEquals("The message is incorrect.", ERROR_MESSAGE, e.getMessage());
        assertSame("The cause is incorrect.", cause, e.getCause());
        assertSame("The data is incorrect.", CODE, e.getApplicationCode());
    }
}
