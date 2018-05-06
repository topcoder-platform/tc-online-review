/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.file;

import junit.framework.TestCase;

/**
 * <p>
 * Title: InvalidConfigException
 * </p>
 *
 * <p>
 * Description: Test whole <code>InvalidConfigException</code> class.
 * </p>
 *
 * <p>
 * Company: TopCoder Software
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 2.0
 */
public class TestInvalidConfigException extends TestCase {
    /** Hold <code>InvalidConfigException</code> object. */
    private InvalidConfigException exception = null;

    /** Message of exception. */
    private final String message = "Some exception.";

    /** Cause of exception. */
    private Exception base = new IllegalArgumentException("argument exception");

    /**
     * Test {@link InvalidConfigException#InvalidConfigException()} without
     * parameters.
     */
    public void testConstructor() {
        // Create exception.
        exception = new InvalidConfigException();

        // Check whether exception was created.
        assertNotNull("The InvalidConfigException was not created.", exception);
    }

    /**
     * Test {@link InvalidConfigException#InvalidConfigException(String)} with
     * message as parameter.
     */
    public void testConstructorWithMessage() {
        // Create exception with given message.
        exception = new InvalidConfigException(message);

        // Check whether exception was created.
        assertNotNull("The InvalidConfigException was not created.", exception);

        // Check message property.
        assertTrue("The message set up incorrectly.", exception.getMessage().startsWith(message));
    }

    /**
     * Test {@link InvalidConfigException#InvalidConfigException(Throwable)}
     * with exception as parameter.
     */
    public void testConstructorWithException() {
        // Create exception with given cause.
        exception = new InvalidConfigException(base);

        // Check whether exception was created.
        assertNotNull("The InvalidConfigException was not created.", exception);

        // Check cause property.
        assertEquals("The cause set up incorrectly.", exception.getCause(), base);
    }

    /**
     * Test {@link InvalidConfigException#InvalidConfigException(String,
     * Throwable)} with message and exception as parameters.
     */
    public void testConstructorWithMessageException() {
        // Create exception with given message and cause.
        exception = new InvalidConfigException(message, base);

        // Check whether exception was created.
        assertNotNull("The InvalidConfigException was not created.", exception);

        // Check message property.
        assertTrue("The message set up incorrectly.", exception.getMessage().startsWith(message));

        // Check cause property.
        assertEquals("The cause set up incorrectly.", exception.getCause(), base);
    }
}
