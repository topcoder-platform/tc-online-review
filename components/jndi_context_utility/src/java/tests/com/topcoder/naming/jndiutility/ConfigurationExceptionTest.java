/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 *
 * TCS Project JNDI Context Utility 2.0 (Unit Tests)
 *
 * @ ConfigurationExceptionTest.java
 */
package com.topcoder.naming.jndiutility;

import com.topcoder.util.errorhandling.BaseException;

import junit.framework.TestCase;


/**
 * Junit test class for ConfigurationException.
 *
 * @author Charizard
 * @version 2.0
 */
public class ConfigurationExceptionTest extends TestCase {
    /** Error message used in test. */
    private static final String MESSAGE = "Error Message";

    /** Cause exception used in test. */
    private static final Throwable CAUSE = new Exception("Cause Exception");

    /**
     * Test method for {@link ConfigurationException#ConfigurationException()}. Try to instantiate with it.
     */
    public void testConfigurationExceptionAccuracy() {
        assertNotNull("failed to instantiate", new ConfigurationException());
    }

    /**
     * Test method for {@link ConfigurationException#ConfigurationException(String)}. Try to instantiate with
     * it and check the error message.
     */
    public void testConfigurationExceptionStringAccuracy() {
        ConfigurationException e = new ConfigurationException(MESSAGE);
        assertNotNull("failed to instantiate", e);
        assertEquals("wrong error message", MESSAGE, e.getMessage());
    }

    /**
     * Test method for {@link ConfigurationException#ConfigurationException(String, Throwable)}.
     */
    public void testConfigurationExceptionStringThrowableAccuracy() {
        ConfigurationException e = new ConfigurationException(MESSAGE, CAUSE);
        assertNotNull("failed to instantiate", e);
        assertEquals("wrong error message", MESSAGE, e.getMessage());
        assertEquals("wrong cause", CAUSE, e.getCause());
    }

    /**
     * Test the inheritance of ConfigurationException. Check whether it extends BaseException.
     */
    public void testInheritance() {
        assertTrue("wrong inheritance", new ConfigurationException() instanceof BaseException);
    }
}
