/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.ajax;

import junit.framework.TestCase;

/**
 * Test the class <code>RequestParsingException</code>.
 *
 * @author assistant
 * @version 1.0
 */
public class RequestParsingExceptionTest extends TestCase {

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
        assertNotNull("The constructor doesn't work.", new RequestParsingException());
    }

    /**
     * Test method for RequestParsingException(java.lang.String).
     */
    public void testConfigurationExceptionString() {
        RequestParsingException e = new RequestParsingException(MESSAGE);
        assertEquals("The message is not right.", MESSAGE, e.getMessage());
    }

    /**
     * Test method for RequestParsingException(java.lang.Throwable).
     */
    public void testConfigurationExceptionThrowable() {
        RequestParsingException e = new RequestParsingException(CAUSE);
        assertEquals("The cause is not right.", CAUSE, e.getCause());
    }

    /**
     * Test method for RequestParsingException(java.lang.String, java.lang.Throwable).
     */
    public void testConfigurationExceptionStringThrowable() {
        RequestParsingException e = new RequestParsingException(MESSAGE, CAUSE);
        assertEquals("The message is not right.", MESSAGE , e.getMessage());
        assertEquals("The cause is not right.", CAUSE, e.getCause());
    }

}
