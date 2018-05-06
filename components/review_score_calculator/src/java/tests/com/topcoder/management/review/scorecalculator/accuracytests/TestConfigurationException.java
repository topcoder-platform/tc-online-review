/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.scorecalculator.accuracytests;
import com.topcoder.management.review.scorecalculator.ConfigurationException;
import com.topcoder.util.errorhandling.BaseException;

import junit.framework.TestCase;
/**
 * Tests for ConfigurationException class.
 * @author qiucx0161
 * @version 1.0
 */
public class TestConfigurationException extends TestCase {
    /**
     * <p>
     * Error message used for testing.
     * </p>
     */
    private static final String ERROR_MESSAGE = "error message";

    /**
     * <p>
     * Cause used for testing.
     * </p>
     */
    private static final Exception CAUSE = new Exception();

    /**
     * <p>
     * Tests that ConfigurationException(String) instance is created and message argument is correctly
     * propagated.
     * </p>
     */
    public void testConstructor1_1() {
        ConfigurationException e = new ConfigurationException(ERROR_MESSAGE);

        assertNotNull("Unable to instantiate ConfigurationException", e);
        assertTrue("Exception should be extension of BaseException class", e instanceof BaseException);
        assertEquals("Error message is not properly set", ERROR_MESSAGE, e.getMessage());
    }

    /**
     * <p>
     * Tests that ConfigurationException(String, Throwable) instance is created and cause is correctly
     * propagated.
     * </p>
     */
    public void testConstructor2_1() {
        ConfigurationException e = new ConfigurationException(ERROR_MESSAGE, CAUSE);

        assertNotNull("Unable to instantiate ConfigurationException", e);
        assertEquals("Cause is not properly set", CAUSE, e.getCause());
    }
}
