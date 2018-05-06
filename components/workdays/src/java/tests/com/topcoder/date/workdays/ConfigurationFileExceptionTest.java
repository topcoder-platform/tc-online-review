/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.date.workdays;

import com.topcoder.util.errorhandling.ExceptionData;

import junit.framework.TestCase;


/**
 * This class is used to check the behavior of the <code>ConfigurationFileException</code> class, including tests of
 * Constructing a ConfigurationFileException instance.
 *
 * @author TCSDEVELOPER
 * @version 1.1
 */
public class ConfigurationFileExceptionTest extends TestCase {
    /**
     * The error message used for testing.
     */
    private static final String MESSAGE = "the error message";

    /**
     * The inner exception for testing.
     */
    private static final Throwable CAUSE = new Exception();

    /**
     * The exception data used for testing.
     */
    private static final ExceptionData DATA = new ExceptionData();

    /**
     * Represents an instance of <code>ConfigurationFileException</code>.
     */
    private ConfigurationFileException instance;


    /**
     * Tests the constructor of <code>ConfigurationFileException</code> with message.
     * <p>
     * The message should be properly set.
     */
    public void testCtorMsg() {
        instance = new ConfigurationFileException(MESSAGE);
        assertEquals("The message should be properly set.", MESSAGE, instance.getMessage());
    }


    /**
     * Tests the constructor of <code>ConfigurationFileException</code> with message and cause.
     * <p>
     * The message and cause should be properly set.
     */
    public void testCtorMsgCause() {
        instance = new ConfigurationFileException(MESSAGE, CAUSE);
        assertEquals("The message should be properly set.", MESSAGE, instance.getMessage());
        assertEquals("The cause should be properly set.", CAUSE, instance.getCause());
    }

    /**
     * Tests the constructor of <code>ConfigurationFileException</code> with message and data.
     * <p>
     * The message and data should be properly set.
     */
    public void testCtorMsgData() {
        instance = new ConfigurationFileException(MESSAGE, DATA);
        assertEquals("The message should be properly set.", MESSAGE, instance.getMessage());
    }

    /**
     * Tests the constructor of <code>ConfigurationFileException</code> with message, cause and data.
     * <p>
     * The message, cause and data should be properly set.
     */
    public void testCtorMsgCauseData() {
        instance = new ConfigurationFileException(MESSAGE, CAUSE, DATA);
        assertNotNull("The object should have been created.", instance);
        assertEquals("The message should be properly set.", MESSAGE, instance.getMessage());
        assertEquals("The cause should be properly set.", CAUSE, instance.getCause());
    }
}
