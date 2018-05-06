/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.file;

import com.topcoder.util.errorhandling.ExceptionData;

import junit.framework.TestCase;

/**
 * <p>
 * Title: TemplateFormatException
 * </p>
 *
 * <p>
 * Description: Test whole <code>TemplateFormatException</code> class.
 * </p>
 *
 * <p>
 * Company: TopCoder Software
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 2.0
 */
public class TestTemplateFormatException extends TestCase {
    /** Hold <code>TemplateFormatException</code> object. */
    private TemplateFormatException exception = null;

    /** Message of exception. */
    private final String message = "Some exception.";

    /** Cause of exception. */
    private Exception base = new IllegalArgumentException("argument exception");

    /**
     * Test {@link TemplateFormatException#TemplateFormatException(String)}
     * with message as parameter.
     */
    public void testConstructorWithMessage() {
        // Create exception with given message.
        exception = new TemplateFormatException(message);

        // Check whether exception was created.
        assertNotNull("The TemplateFormatException was not created.", exception);

        // Check message property.
        assertTrue("The message set up incorrectly.", exception.getMessage().startsWith(message));
    }

    /**
     * Test {@link TemplateFormatException#TemplateFormatException(Throwable)}
     * with exception as parameter.
     */
    public void testConstructorWithException() {
        // Create exception with given cause.
        exception = new TemplateFormatException(base);

        // Check whether exception was created.
        assertNotNull("The TemplateFormatException was not created.", exception);

        // Check cause property.
        assertEquals("The cause set up incorrectly.", exception.getCause(), base);
    }

    /**
     * Test {@link TemplateFormatException#TemplateFormatException(String,
     * Throwable)} with message and exception as parameters.
     */
    public void testConstructorWithMessageException() {
        // Create exception with given message and cause.
        exception = new TemplateFormatException(message, base);

        // Check whether exception was created.
        assertNotNull("The TemplateFormatException was not created.", exception);

        // Check message property.
        assertTrue("The message set up incorrectly.", exception.getMessage().startsWith(message));

        // Check cause property.
        assertEquals("The cause set up incorrectly.", exception.getCause(), base);
    }
    /**
     * Test {@link TemplateFormatException#TemplateFormatException(String,
     * Throwable,ExceptionData)} with message and exception as parameters.
     */
    public void testConstructorWithMessage_Exception_ExceptionData() {
        ExceptionData data = new ExceptionData();
        data.setApplicationCode("test");
        // Create exception with given message and cause.
        exception = new TemplateFormatException(message, base, data);
        // Check whether exception was created.
        assertNotNull("The TemplateFormatException was not created.", exception);

        // Check message property.
        assertTrue("The message set up incorrectly.", exception.getMessage().startsWith(message));

        // Check cause property.
        assertEquals("The cause set up incorrectly.", exception.getCause(), base);

        // Check data property.
        assertEquals("The cause set up incorrectly.", exception.getApplicationCode(), data.getApplicationCode());
    }
}
