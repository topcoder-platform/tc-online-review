/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.configuration.persistence;

import junit.framework.TestCase;

/**
 * <p>
 * Unit test for the ConfigurationParserException class.
 * </p>
 *
 * @author rainday
 * @version 1.0
 */
public class ConfigurationParserExceptionUnitTests extends TestCase {

    /**
     * <p>
     * Accuracy test the <code>ConfigurationParserException(String)</code> constructor.
     * ConfigurationParserException instance should be created.
     * </p>
     */
    public void testConfigurationParserExceptionString() {
        ConfigurationParserException exception = new ConfigurationParserException("Failed.");
        assertNotNull("Create ConfigurationParserException incorrectly.", exception);
        assertEquals("Create ConfigurationParserException incorrectly.", "Failed.", exception.getMessage());
    }

    /**
     * <p>
     * Accuracy test the <code>ConfigurationParserException(String, Throwable)</code> constructor.
     * ConfigurationParserException instance should be created.
     * </p>
     */
    public void testConfigurationParserExceptionStringThrowable() {
        IllegalArgumentException exp = new IllegalArgumentException();
        ConfigurationParserException exception = new ConfigurationParserException("Failed.", exp.getCause());
        assertNotNull("Create ConfigurationParserException incorrectly.", exception);
        assertEquals("Create ConfigurationParserException incorrectly.", "Failed.", exception.getMessage());
        assertEquals("Create ConfigurationParserException incorrectly.", exp.getCause(), exception.getCause());
    }

}
