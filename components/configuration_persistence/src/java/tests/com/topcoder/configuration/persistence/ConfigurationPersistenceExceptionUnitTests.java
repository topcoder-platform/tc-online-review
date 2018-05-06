/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.configuration.persistence;

import junit.framework.TestCase;

/**
 * <p>
 * Unit test for the ConfigurationPersistenceException class.
 * </p>
 *
 * @author rainday
 * @version 1.0
 */
public class ConfigurationPersistenceExceptionUnitTests extends TestCase {

    /**
     * <p>
     * Accuracy test the <code>ConfigurationPersistenceException(String)</code> constructor.
     * ConfigurationPersistenceException instance should be created.
     * </p>
     */
    public void testConfigurationPersistenceExceptionString() {
        ConfigurationPersistenceException exception = new ConfigurationPersistenceException("Failed.");
        assertNotNull("Create ConfigurationPersistenceException incorrectly.", exception);
        assertEquals("Create ConfigurationPersistenceException incorrectly.", "Failed.", exception.getMessage());
    }

    /**
     * <p>
     * Accuracy test the <code>ConfigurationPersistenceException(String, Throwable)</code>
     * constructor. ConfigurationPersistenceException instance should be created.
     * </p>
     */
    public void testConfigurationPersistenceExceptionStringThrowable() {
        IllegalArgumentException exp = new IllegalArgumentException();
        ConfigurationPersistenceException exception = new ConfigurationPersistenceException("Failed.", exp.getCause());
        assertNotNull("Create ConfigurationPersistenceException incorrectly.", exception);
        assertEquals("Create ConfigurationPersistenceException incorrectly.", "Failed.", exception.getMessage());
        assertEquals("Create ConfigurationPersistenceException incorrectly.", exp.getCause(), exception.getCause());
    }

}
