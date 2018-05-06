/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.management.review.scorecalculator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Contains the unit tests for the ConfigurationException class.
 *
 * @author      UFP2161
 * @copyright   Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 * @version     1.0
 */
public class ConfigurationExceptionTest extends TestCase {

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Static Fields

    /**
     * The error message to be used in the unit tests.
     */
    private static final String MESSAGE = "Error Message";

    /**
     * The root cause to be used in the unit tests.
     */
    private static final Throwable CAUSE = new Exception("Cause Message");

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Static Methods

    /**
     * Creates a test suite for the tests in this test case.
     *
     * @return  A TestSuite for this test case.
     */
    public static Test suite() {
        return new TestSuite(ConfigurationExceptionTest.class);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Constructor(String) Tests

    /**
     * Ensures that the one parameter constructor set the message attribute properly.
     */
    public void test1CtorSetsMessage() {
        assertEquals(
                "The message was improperly set.",
                MESSAGE, new ConfigurationException(MESSAGE).getMessage());
    }

    /**
     * Ensures that the one parameter constructor set the cause attribute properly.
     */
    public void test1CtorSetsCause() {
        assertNull(
                "The cause was improperly set.",
                new ConfigurationException(MESSAGE).getCause());
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Constructor(String, Throwable) Tests

    /**
     * Ensures that the two parameter constructor set the message attribute properly.
     */
    public void test2CtorSetsMessage() {
        assertEquals(
                "The message was improperly set.",
                MESSAGE + ", caused by " + CAUSE.getMessage(),
                new ConfigurationException(MESSAGE, CAUSE).getMessage());
    }

    /**
     * Ensures that the two parameter constructor set the cause attribute properly.
     */
    public void test2CtorSetsCause() {
        assertSame(
                "The cause was improperly set.",
                CAUSE, new ConfigurationException(MESSAGE, CAUSE).getCause());
    }
}
