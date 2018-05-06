/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.management.review.scorecalculator;

import com.topcoder.management.scorecard.data.Scorecard;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Contains the unit tests for the ScorecardStructureException class.
 *
 * @author      UFP2161
 * @copyright   Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 * @version     1.0
 */
public class ScorecardStructureExceptionTest extends TestCase {

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Static Fields

    /**
     * The error message to be used in the unit tests.
     */
    protected static final String MESSAGE = "Error Message";

    /**
     * The root cause to be used in the unit tests.
     */
    protected static final Throwable CAUSE = new Exception("Cause Message");

    /**
     * The scorecard to be used in the unit tests.
     */
    private static final Scorecard SCORECARD = new Scorecard();

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Static Methods

    /**
     * Creates a test suite for the tests in this test case.
     *
     * @return  A TestSuite for this test case.
     */
    public static Test suite() {
        return new TestSuite(ScorecardStructureExceptionTest.class);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Constructor(String, Scorecard) Tests

    /**
     * Ensures that the two parameter constructor set the message attribute properly.
     */
    public void test2CtorSetsMessage() {
        assertEquals(
                "The message was improperly set.",
                MESSAGE, new ScorecardStructureException(MESSAGE, SCORECARD).getMessage());
    }

    /**
     * Ensures that the two parameter constructor set the cause attribute properly.
     */
    public void test2CtorSetsCause() {
        assertNull(
                "The cause was improperly set.",
                new ScorecardStructureException(MESSAGE, SCORECARD).getCause());
    }

    /**
     * Ensures that the two parameter constructor set the scorecard attribute properly.
     */
    public void test2CtorSetsScorecard() {
        assertSame(
                "The scorecard was improperly set.",
                SCORECARD, new ScorecardStructureException(MESSAGE, SCORECARD).getScorecard());
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Constructor(String, Throwable, Scorecard) Tests

    /**
     * Ensures that the three parameter constructor set the message attribute properly.
     */
    public void test3CtorSetsMessage() {
        assertEquals(
                "The message was improperly set.",
                MESSAGE + ", caused by " + CAUSE.getMessage(),
                new ScorecardStructureException(MESSAGE, CAUSE, SCORECARD).getMessage());
    }

    /**
     * Ensures that the three parameter constructor set the cause attribute properly.
     */
    public void test3CtorSetsCause() {
        assertSame(
                "The cause was improperly set.",
                CAUSE, new ScorecardStructureException(MESSAGE, CAUSE, SCORECARD).getCause());
    }

    /**
     * Ensures that the three parameter constructor set the scorecard attribute properly.
     */
    public void test3CtorSetsScorecard() {
        assertSame(
                "The scorecard was improperly set.",
                SCORECARD, new ScorecardStructureException(MESSAGE, CAUSE, SCORECARD).getScorecard());
    }
}
