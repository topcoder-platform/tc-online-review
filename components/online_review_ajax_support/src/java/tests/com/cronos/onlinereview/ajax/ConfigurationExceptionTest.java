/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.ajax;

import junit.framework.TestCase;

/**
 * Test the class <code>ConfigurationException</code>.
 *
 * @author assistant
 * @version 1.0
 */
public class ConfigurationExceptionTest extends TestCase {

    /**
     * Represents the exception message.
     */
    private static final String MESSAGE = "exception message";

    /**
     * Represents the exception cause.
     */
    private static final Exception CAUSE = new Exception();

    /**
     * Test method for ConfigurationException().
     */
    public void testConfigurationException() {
        assertNotNull("The constructor doesn't work.", new ConfigurationException());
    }

    /**
     * Test method for ConfigurationException(java.lang.String).
     */
    public void testConfigurationExceptionString() {
        ConfigurationException e = new ConfigurationException(MESSAGE);
        assertEquals("The message is not right.", MESSAGE, e.getMessage());
    }

    /**
     * Test method for ConfigurationException(java.lang.Throwable).
     */
    public void testConfigurationExceptionThrowable() {
        ConfigurationException e = new ConfigurationException(CAUSE);
        assertEquals("The cause is not right.", CAUSE, e.getCause());
    }

    /**
     * Test method for ConfigurationException(java.lang.String, java.lang.Throwable).
     */
    public void testConfigurationExceptionStringThrowable() {
        ConfigurationException e = new ConfigurationException(MESSAGE, CAUSE);
        assertEquals("The message is not right.", MESSAGE , e.getMessage());
        assertEquals("The cause is not right.", CAUSE, e.getCause());
    }

}
