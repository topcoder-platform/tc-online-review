/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.processor.ipserver;

import junit.framework.TestCase;


/**
 * <p>
 * Unit test cases for ConfigurationException.
 * </p>
 *
 * <p>
 * This class is pretty simple. The test cases simply verifies the exception can be instantiated with the error message
 * and cause properly propagated.
 * </p>
 *
 * @author zsudraco
 * @version 2.0
 * @copyright Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
public class ConfigurationExceptionTest extends TestCase {
    /** The error message used for testing. */
    private static final String ERROR_MESSAGE = "test error message";

    /** An exception instance used to create the ConfigurationException. */
    private final Throwable cause = new NullPointerException();

    /**
     * Test ConfigurationException constructor with correct message, the message can be retrieved correctly later.
     */
    public void testCtor1() {
        ConfigurationException cde = new ConfigurationException(ConfigurationExceptionTest.ERROR_MESSAGE);

        assertNotNull("Unable to instantiate ConfigurationExceptionTest.", cde);
        assertEquals("Error message is not properly propagated to super class.",
            ConfigurationExceptionTest.ERROR_MESSAGE, cde.getMessage());
    }

    /**
     * Test ConfigurationException constructor with null reason, no exception is expected.
     */
    public void testCtor1WithNullReason() {
        // No exception is expected
        new ConfigurationException(null);
    }

    /**
     * Test ConfigurationException constructor with correct error message, cause, the message and cause can be
     * retrieved correctly later.
     */
    public void testCtor2() {
        ConfigurationException ce = new ConfigurationException(ConfigurationExceptionTest.ERROR_MESSAGE, cause);

        assertNotNull("Unable to instantiate ConfigurationExceptionTest.", ce);
        assertTrue("Error message is not properly propagated to super class.",
            ce.getMessage().indexOf(ConfigurationExceptionTest.ERROR_MESSAGE) >= 0);
        assertEquals("The inner exception should match", ce.getCause(), cause);
    }

    /**
     * Test ConfigurationException constructor with null reason, no exception is expected.
     */
    public void testCtor2WithNullReason() {
        // No exception
        new ConfigurationException(null, cause);
    }

    /**
     * Test ConfigurationException constructor with error message and null inner exception, no exception is expected.
     */
    public void testCtor2WithNullCause() {
        // No exception
        new ConfigurationException(ConfigurationExceptionTest.ERROR_MESSAGE, null);
    }
}
