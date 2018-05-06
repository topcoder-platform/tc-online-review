/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.scoreaggregator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * This test case aggregates all test cases for <code>ReviewScoreAggregatorConfigException</code>.
 * </p>
 *
 * @author daiwb
 * @version 1.0
 */
public class ReviewScoreAggregatorConfigExceptionTest extends TestCase {
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
        return new TestSuite(ReviewScoreAggregatorConfigExceptionTest.class);
    }

    /**
     * Test constructor <code>ReviewScoreAggregatorConfigException(String)</code>.
     */
    public void testReviewScoreAggregatorConfigExceptionString_Accuracy() {
        ReviewScoreAggregatorConfigException exp = new ReviewScoreAggregatorConfigException(MSG);
        assertNotNull("The instance should be created successfully.", exp);
        assertNotNull("The ReviewScoreAggregatorConfigException should inherit from ReviewScoreAggregatorException.",
                (ReviewScoreAggregatorException) exp);
        assertEquals("The error message should be set successfully.", MSG, exp.getMessage());
    }

    /**
     * Test constructor <code>ReviewScoreAggregatorConfigException(String, Throwable)</code>.
     */
    public void testReviewScoreAggregatorConfigExceptionStringThrowable_Accuracy() {
        ReviewScoreAggregatorConfigException exp = new ReviewScoreAggregatorConfigException(MSG, E);
        assertNotNull("The instance should be created successfully.", exp);
        assertNotNull("The ReviewScoreAggregatorConfigException should inherit from ReviewScoreAggregatorException.",
                (ReviewScoreAggregatorException) exp);
        assertTrue("The error message should be set successfully.", exp.getMessage().indexOf(MSG) >= 0);
        assertEquals("The inner exception should be set successfully.", E, exp.getCause());
    }

}
