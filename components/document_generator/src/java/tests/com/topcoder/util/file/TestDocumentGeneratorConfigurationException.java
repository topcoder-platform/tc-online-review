/*
 * Copyright (C) 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.file;

import com.topcoder.util.errorhandling.ExceptionData;

import junit.framework.TestCase;

/**
 * Unit test for DocumentGeneratorConfigurationException.java class.
 * @author TCSDEVELOPER
 * @version 3.0
 */
public class TestDocumentGeneratorConfigurationException extends TestCase {
    /** Hold <code>DocumentGeneratorConfigurationException</code> object. */
    private DocumentGeneratorConfigurationException exception = null;

    /** Message of exception. */
    private final String message = "Some exception.";

    /** Cause of exception. */
    private Exception base = new IllegalArgumentException("argument exception");

    /**
     * Test {@link DocumentGeneratorConfigurationException#DocumentGeneratorConfigurationException(String)}
     * with message as parameter.
     */
    public void testConstructorWithMessage() {
        // Create exception with given message.
        exception = new DocumentGeneratorConfigurationException(message);

        // Check whether exception was created.
        assertNotNull("The DocumentGeneratorConfigurationException was not created.", exception);

        // Check message property.
        assertTrue("The message set up incorrectly.", exception.getMessage().startsWith(message));
    }

    /**
     * Test {@link DocumentGeneratorConfigurationException#DocumentGeneratorConfigurationException(Throwable)}
     * with exception as parameter.
     */
    public void testConstructorWithException() {
        // Create exception with given cause.
        exception = new DocumentGeneratorConfigurationException(base);

        // Check whether exception was created.
        assertNotNull("The DocumentGeneratorConfigurationException was not created.", exception);

        // Check cause property.
        assertEquals("The cause set up incorrectly.", exception.getCause(), base);
    }

    /**
     * Test {@link DocumentGeneratorConfigurationException#DocumentGeneratorConfigurationException(String,
     * Throwable)} with message and exception as parameters.
     */
    public void testConstructorWithMessageException() {
        // Create exception with given message and cause.
        exception = new DocumentGeneratorConfigurationException(message, base);

        // Check whether exception was created.
        assertNotNull("The DocumentGeneratorConfigurationException was not created.", exception);

        // Check message property.
        assertTrue("The message set up incorrectly.", exception.getMessage().startsWith(message));

        // Check cause property.
        assertEquals("The cause set up incorrectly.", exception.getCause(), base);
    }

    /**
     * Test {@link DocumentGeneratorConfigurationException#DocumentGeneratorConfigurationException(String,
     * Throwable,ExceptionData)} with message and exception as parameters.
     */
    public void testConstructorWithMessage_Exception_ExceptionData() {
        ExceptionData data = new ExceptionData();
        data.setApplicationCode("test");
        // Create exception with given message and cause.
        exception = new DocumentGeneratorConfigurationException(message, base, data);
        // Check whether exception was created.
        assertNotNull("The DocumentGeneratorConfigurationException was not created.", exception);

        // Check message property.
        assertTrue("The message set up incorrectly.", exception.getMessage().startsWith(message));

        // Check cause property.
        assertEquals("The cause set up incorrectly.", exception.getCause(), base);

        // Check data property.
        assertEquals("The cause set up incorrectly.", exception.getApplicationCode(), data
            .getApplicationCode());
    }
}
