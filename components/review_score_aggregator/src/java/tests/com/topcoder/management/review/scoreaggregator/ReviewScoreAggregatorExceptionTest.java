/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.scoreaggregator;

import com.topcoder.util.errorhandling.BaseException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * This test case aggregates all test cases for <code>ReviewScoreAggregatorException</code>.
 * </p>
 *
 * @author daiwb
 * @version 1.0
 */
public class ReviewScoreAggregatorExceptionTest extends TestCase {
    /**
     * Error message used for tests.
     */
    private static final String MSG = "error.";

    /**
     * Inner inner exception used for tests.
     */
    private static final Exception E = new NullPointerException();

    /**
     * Returns the test suite of this test case.
     *
     * @return the test suite of this test case.
     */
    public static Test suite() {
        return new TestSuite(ReviewScoreAggregatorExceptionTest.class);
    }

    /**
     * Test constructor <code>ReviewScoreAggregatorException(String)</code>.
     */
    public void testReviewScoreAggregatorExceptionString_Accuracy() {
        ReviewScoreAggregatorException exp = new ReviewScoreAggregatorException(MSG);
        assertNotNull("The instance should be created successfully.", exp);
        assertNotNull("The ReviewScoreAggregatorException should inherit from BaseException.", (BaseException) exp);
        assertEquals("The error message should be set successfully.", MSG, exp.getMessage());
    }

    /**
     * Test method <code>ReviewScoreAggregatorException(String, Throwable)</code>.
     */
    public void testReviewScoreAggregatorExceptionStringThrowable_Accuracy() {
        ReviewScoreAggregatorException exp = new ReviewScoreAggregatorException(MSG, E);
        assertNotNull("The instance should be created successfully.", exp);
        assertNotNull("The ReviewScoreAggregatorException should inherit from BaseException.", (BaseException) exp);
        assertTrue("The error message should be set successfully.", exp.getMessage().indexOf(MSG) >= 0);
        assertEquals("The inner exception should be set successfully.", E, exp.getCause());
    }

}
