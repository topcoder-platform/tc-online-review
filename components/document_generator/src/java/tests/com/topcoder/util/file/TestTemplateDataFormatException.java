/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.file;

import com.topcoder.util.errorhandling.ExceptionData;

import junit.framework.TestCase;

/**
 * <p>
 * Title: TemplateDataFormatException
 * </p>
 * <p>
 * Description: Test whole <code>TemplateDataFormatException</code> class.
 * </p>
 * <p>
 * Company: TopCoder Software
 * </p>
 * @author TCSDEVELOPER
 * @version 2.0
 */
public class TestTemplateDataFormatException extends TestCase {
    /** Hold <code>TemplateDataFormatException</code> object. */
    private TemplateDataFormatException exception = null;

    /** Message of exception. */
    private final String message = "Some exception.";

    /** Cause of exception. */
    private Exception base = new IllegalArgumentException("argument exception");

    /**
     * Test {@link TemplateDataFormatException#TemplateDataFormatException(String)} with message as parameter.
     */
    public void testConstructorWithMessage() {
        // Create exception with given message.
        exception = new TemplateDataFormatException(message);

        // Check whether exception was created.
        assertNotNull("The TemplateDataFormatException was not created.", exception);

        // Check message property.
        assertTrue("The message set up incorrectly.", exception.getMessage().startsWith(message));
    }

    /**
     * Test {@link TemplateDataFormatException#TemplateDataFormatException(Throwable)} with exception as
     * parameter.
     */
    public void testConstructorWithException() {
        // Create exception with given cause.
        exception = new TemplateDataFormatException(base);

        // Check whether exception was created.
        assertNotNull("The TemplateDataFormatException was not created.", exception);

        // Check cause property.
        assertEquals("The cause set up incorrectly.", exception.getCause(), base);
    }

    /**
     * Test {@link TemplateDataFormatException#TemplateDataFormatException(String, Throwable)} with message
     * and exception as parameters.
     */
    public void testConstructorWithMessageException() {
        // Create exception with given message and cause.
        exception = new TemplateDataFormatException(message, base);

        // Check whether exception was created.
        assertNotNull("The TemplateDataFormatException was not created.", exception);

        // Check message property.
        assertTrue("The message set up incorrectly.", exception.getMessage().startsWith(message));

        // Check cause property.
        assertEquals("The cause set up incorrectly.", exception.getCause(), base);
    }

    /**
     * Test {@link TemplateDataFormatException#TemplateDataFormatException(String, Throwable,ExceptionData)}
     * with message and exception as parameters.
     */
    public void testConstructorWithMessage_Exception_ExceptionData() {
        ExceptionData data = new ExceptionData();
        data.setApplicationCode("test");
        // Create exception with given message and cause.
        exception = new TemplateDataFormatException(message, base, data);

        // Check whether exception was created.
        assertNotNull("The TemplateDataFormatException was not created.", exception);

        // Check message property.
        assertTrue("The message set up incorrectly.", exception.getMessage().startsWith(message));

        // Check cause property.
        assertEquals("The cause set up incorrectly.", exception.getCause(), base);
        // Check data property.
        assertEquals("The cause set up incorrectly.", exception.getApplicationCode(), data
                .getApplicationCode());
    }
}
