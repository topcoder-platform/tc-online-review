/*
 * Copyright (C) 2007 - 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.configuration.accuracytests;

import com.topcoder.configuration.ConfigurationException;
import com.topcoder.util.errorhandling.BaseCriticalException;
import com.topcoder.util.errorhandling.ExceptionData;

import junit.framework.TestCase;

/**
 * Accuracy test for ConfigurationException class.
 *
 * Changes in 1.1: update the BaseException to BaseCriticalException, and test cases for added ctors.
 *
 * @author KKD, TCSDEVELOPER
 * @version 1.1
 * @since 1.0
 */
public class ConfigurationExceptionAccuracyTests extends TestCase {
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
     * Inheritance test for class <code>ConfigurationException</code>.
     * </p>
     *
     * <p>
     * Verifies <code>ConfigurationException</code>. subclasses <code>BaseCriticalException</code>.
     * </p>
     */
    public void testConfigurationExceptionInheritance() {
        assertTrue("ConfigurationException does not subclass BaseCriticalException.", new ConfigurationException(
            ERROR_MESSAGE) instanceof BaseCriticalException);
    }

    /**
     * <p>
     * Accuracy test for constructor <code>ConfigurationException(String)</code>.
     * </p>
     *
     * <p>
     * Verifies the instance could be successfully created.
     * </p>
     */
    public void testCtor1() {
        ConfigurationException spe = new ConfigurationException(ERROR_MESSAGE);
        assertNotNull("Unable to instantiate ConfigurationException.", spe);
    }

    /**
     * <p>
     * Accuracy test for constructor <code>ConfigurationException(String message, Throwable cause)</code>.
     * </p>
     *
     * <p>
     * Verifies the error message is properly propagated.
     * </p>
     */
    public void testCtor2() {
        Throwable cause = new Exception();
        ConfigurationException spe = new ConfigurationException(ERROR_MESSAGE, cause);
        assertNotNull("Unable to instantiate ConfigurationException.", spe);
    }

    /**
     * Accuracy test for ConfigurationException(String message, ExceptionData data).
     *
     * @since 1.1
     */
    public void testCtor3() {
        ConfigurationException e = new ConfigurationException(ERROR_MESSAGE, DATA);

        assertEquals("The message is incorrect.", ERROR_MESSAGE, e.getMessage());
        assertNull("The cause is incorrect.", e.getCause());
        assertSame("The data is incorrect.", CODE, e.getApplicationCode());
    }

    /**
     * Accuracy test for ConfigurationException(String message, Throwable cause, ExceptionData data).
     *
     * @since 1.1
     */
    public void testCtor4() {
        Throwable cause = new Exception();
        ConfigurationException e = new ConfigurationException(ERROR_MESSAGE, cause, DATA);

        assertEquals("The message is incorrect.", ERROR_MESSAGE, e.getMessage());
        assertSame("The cause is incorrect.", cause, e.getCause());
        assertSame("The data is incorrect.", CODE, e.getApplicationCode());
    }
}