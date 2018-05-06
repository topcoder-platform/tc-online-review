/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.file;

import com.topcoder.util.errorhandling.ExceptionData;
import com.topcoder.util.file.templatesource.TemplateSourceException;
import junit.framework.TestCase;

/**
 * <p>
 * Title: TemplateSourceException
 * </p>
 *
 * <p>
 * Description: Test whole <code>TemplateSourceException</code> class.
 * </p>
 *
 * <p>
 * Company: TopCoder Software
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 2.0
 */
public class TestTemplateSourceException extends TestCase {
    /** Hold <code>TemplateSourceException</code> object. */
    private TemplateSourceException exception = null;

    /** Message of exception. */
    private final String message = "Some exception.";

    /** Cause of exception. */
    private Exception base = new IllegalArgumentException("argument exception");

    /**
     * Data of exception.
     */
    private ExceptionData data = new ExceptionData();
    /**
     * Test {@link TemplateSourceException#TemplateSourceException(String)}
     * with message as parameter.
     */
    public void testConstructorWithMessage() {
        // Create exception with given message.
        exception = new TemplateSourceException(message);

        // Check whether exception was created.
        assertNotNull("The TemplateSourceException was not created.", exception);

        // Check message property.
        assertTrue("The message set up incorrectly.", exception.getMessage().startsWith(message));
    }

    /**
     * Test {@link TemplateSourceException#TemplateSourceException(Throwable)}
     * with exception as parameter.
     */
    public void testConstructorWithException() {
        // Create exception with given cause.
        exception = new TemplateSourceException(base);

        // Check whether exception was created.
        assertNotNull("The TemplateSourceException was not created.", exception);

        // Check cause property.
        assertEquals("The cause set up incorrectly.", exception.getCause(), base);
    }

    /**
     * Test {@link TemplateSourceException#TemplateSourceException(String,
     * Throwable)} with message and exception as parameters.
     */
    public void testConstructorWithMessageException() {
        // Create exception with given message and cause.
        exception = new TemplateSourceException(message, base);

        // Check whether exception was created.
        assertNotNull("The TemplateSourceException was not created.", exception);

        // Check message property.
        assertTrue("The message set up incorrectly.", exception.getMessage().startsWith(message));

        // Check cause property.
        assertEquals("The cause set up incorrectly.", exception.getCause(), base);
    }
    /**
     * Test {@link TemplateSourceException#TemplateSourceException(String,
     * Throwable, ExceptionData)} with message and exception as parameters.
     */
    public void testConstructorWithMessage_Exception_ExceptionData() {
        // Create exception with given message, cause and data.
        exception = new TemplateSourceException(message, base, data);
        data.setApplicationCode("test");

        // Check whether exception was created.
        assertNotNull("The TemplateSourceException was not created.", exception);

        // Check message property.
        assertTrue("The message set up incorrectly.", exception.getMessage().startsWith(message));

        // Check cause property.
        assertEquals("The cause set up incorrectly.", exception.getCause(), base);

        // Check data property.
        assertEquals("The cause set up incorrectly.", exception.getApplicationCode(), data.getApplicationCode());
    }
}
