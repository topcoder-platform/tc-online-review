/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.configuration.persistence;

import junit.framework.TestCase;

/**
 * <p>
 * Unit test for the InvalidConfigurationUpdateException class.
 * </p>
 *
 * @author rainday
 * @version 1.0
 */
public class InvalidConfigurationUpdateExceptionUnitTests extends TestCase {

    /**
     * <p>
     * Accuracy test the <code>InvalidConfigurationUpdateException(String)</code> constructor.
     * InvalidConfigurationUpdateException instance should be created.
     * </p>
     */
    public void testInvalidConfigurationUpdateExceptionString() {
        InvalidConfigurationUpdateException exception = new InvalidConfigurationUpdateException("Failed.");
        assertNotNull("Create InvalidConfigurationUpdateException incorrectly.", exception);
        assertEquals("Create InvalidConfigurationUpdateException incorrectly.", "Failed.", exception.getMessage());
    }

    /**
     * <p>
     * Accuracy test the <code>InvalidConfigurationUpdateException(String, Throwable)</code>
     * constructor. InvalidConfigurationUpdateException instance should be created.
     * </p>
     */
    public void testInvalidConfigurationUpdateExceptionStringThrowable() {
        IllegalArgumentException exp = new IllegalArgumentException();
        InvalidConfigurationUpdateException exception = new InvalidConfigurationUpdateException("Failed.", exp
                .getCause());
        assertNotNull("Create InvalidConfigurationUpdateException incorrectly.", exception);
        assertEquals("Create InvalidConfigurationUpdateException incorrectly.", "Failed.", exception.getMessage());
        assertEquals("Create InvalidConfigurationUpdateException incorrectly.", exp.getCause(), exception.getCause());
    }

}
