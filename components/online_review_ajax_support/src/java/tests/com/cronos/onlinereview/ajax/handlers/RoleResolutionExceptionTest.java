/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.ajax.handlers;

import junit.framework.TestCase;

/**
 * Test the class <code>RoleResolutionException</code>.
 *
 * @author assistant
 * @version 1.0
 */
public class RoleResolutionExceptionTest extends TestCase {

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
        assertNotNull("The constructor doesn't work.", new ResourceException());
    }

    /**
     * Test method for RoleResolutionException(java.lang.String).
     */
    public void testConfigurationExceptionString() {
        ResourceException e = new ResourceException(MESSAGE);
        assertEquals("The message is not right.", MESSAGE, e.getMessage());
    }

    /**
     * Test method for RoleResolutionException(java.lang.Throwable).
     */
    public void testConfigurationExceptionThrowable() {
        ResourceException e = new ResourceException(CAUSE);
        assertEquals("The cause is not right.", CAUSE, e.getCause());
    }

    /**
     * Test method for RoleResolutionException(java.lang.String, java.lang.Throwable).
     */
    public void testConfigurationExceptionStringThrowable() {
        ResourceException e = new ResourceException(MESSAGE, CAUSE);
        assertEquals("The message is not right.", MESSAGE, e.getMessage());
        assertEquals("The cause is not right.", CAUSE, e.getCause());
    }

}
