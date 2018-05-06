/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.configuration.persistence;

import junit.framework.TestCase;

/**
 * <p>
 * Unit test for the ConfigurationUpdateConflictException class.
 * </p>
 *
 * @author rainday
 * @version 1.0
 */
public class ConfigurationUpdateConflictExceptionUnitTests extends TestCase {

    /**
     * <p>
     * Accuracy test the <code>ConfigurationUpdateConflictException(String)</code> constructor.
     * ConfigurationUpdateConflictException instance should be created.
     * </p>
     */
    public void testConfigurationUpdateConflictExceptionString() {
        ConfigurationUpdateConflictException exception = new ConfigurationUpdateConflictException("Failed.");
        assertNotNull("Create ConfigurationUpdateConflictException incorrectly.", exception);
        assertEquals("Create ConfigurationUpdateConflictException incorrectly.", "Failed.", exception.getMessage());
    }

    /**
     * <p>
     * Accuracy test the <code>ConfigurationUpdateConflictException(String, Throwable)</code>
     * constructor. ConfigurationUpdateConflictException instance should be created.
     * </p>
     */
    public void testConfigurationUpdateConflictExceptionStringThrowable() {
        IllegalArgumentException exp = new IllegalArgumentException();
        ConfigurationUpdateConflictException exception = new ConfigurationUpdateConflictException("Failed.", exp
                .getCause());
        assertNotNull("Create ConfigurationUpdateConflictException incorrectly.", exception);
        assertEquals("Create ConfigurationUpdateConflictException incorrectly.", "Failed.", exception.getMessage());
        assertEquals("Create ConfigurationUpdateConflictException incorrectly.", exp.getCause(), exception.getCause());
    }

}
