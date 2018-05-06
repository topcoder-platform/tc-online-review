/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.management.review.scorecalculator;

import com.topcoder.management.review.data.Review;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Contains the unit tests for the ReviewStructureException class.
 *
 * @author      UFP2161
 * @copyright   Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 * @version     1.0
 */
public class ReviewStructureExceptionTest extends TestCase {

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
     * The review to be used in the unit tests.
     */
    private static final Review REVIEW = new Review();

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Static Methods

    /**
     * Creates a test suite for the tests in this test case.
     *
     * @return  A TestSuite for this test case.
     */
    public static Test suite() {
        return new TestSuite(ReviewStructureExceptionTest.class);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Constructor(String, Review) Tests

    /**
     * Ensures that the two parameter constructor set the message attribute properly.
     */
    public void test2CtorSetsMessage() {
        assertEquals(
                "The message was improperly set.",
                MESSAGE, new ReviewStructureException(MESSAGE, REVIEW).getMessage());
    }

    /**
     * Ensures that the two parameter constructor set the cause attribute properly.
     */
    public void test2CtorSetsCause() {
        assertNull(
                "The cause was improperly set.",
                new ReviewStructureException(MESSAGE, REVIEW).getCause());
    }

    /**
     * Ensures that the two parameter constructor set the review attribute properly.
     */
    public void test2CtorSetsReview() {
        assertSame(
                "The review was improperly set.",
                REVIEW, new ReviewStructureException(MESSAGE, REVIEW).getReview());
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Constructor(String, Throwable, Review) Tests

    /**
     * Ensures that the three parameter constructor set the message attribute properly.
     */
    public void test3CtorSetsMessage() {
        assertEquals(
                "The message was improperly set.",
                MESSAGE + ", caused by " + CAUSE.getMessage(),
                new ReviewStructureException(MESSAGE, CAUSE, REVIEW).getMessage());
    }

    /**
     * Ensures that the three parameter constructor set the cause attribute properly.
     */
    public void test3CtorSetsCause() {
        assertSame(
                "The cause was improperly set.",
                CAUSE, new ReviewStructureException(MESSAGE, CAUSE, REVIEW).getCause());
    }

    /**
     * Ensures that the three parameter constructor set the review attribute properly.
     */
    public void test3CtorSetsReview() {
        assertSame(
                "The review was improperly set.",
                REVIEW, new ReviewStructureException(MESSAGE, CAUSE, REVIEW).getReview());
    }
}
