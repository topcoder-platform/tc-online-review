/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.management.review.scorecalculator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Contains the unit tests for the CalculationException class.
 *
 * @author      UFP2161
 * @copyright   Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 * @version     1.0
 */
public class CalculationExceptionTest extends TestCase {

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
        return new TestSuite(CalculationExceptionTest.class);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Constructor(String) Tests

    /**
     * Ensures that the one parameter constructor set the message attribute properly.
     */
    public void test1CtorSetsMessage() {
        assertEquals(
                "The message was improperly set.",
                MESSAGE, new CalculationException(MESSAGE).getMessage());
    }

    /**
     * Ensures that the one parameter constructor set the cause attribute properly.
     */
    public void test1CtorSetsCause() {
        assertNull(
                "The cause was improperly set.",
                new CalculationException(MESSAGE).getCause());
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
                new CalculationException(MESSAGE, CAUSE).getMessage());
    }

    /**
     * Ensures that the two parameter constructor set the cause attribute properly.
     */
    public void test2CtorSetsCause() {
        assertSame(
                "The cause was improperly set.",
                CAUSE, new CalculationException(MESSAGE, CAUSE).getCause());
    }
}
