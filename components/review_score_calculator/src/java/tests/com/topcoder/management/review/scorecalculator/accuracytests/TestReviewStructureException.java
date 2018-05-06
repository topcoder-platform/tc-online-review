/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.scorecalculator.accuracytests;
import com.topcoder.management.review.data.Review;
import com.topcoder.management.review.scorecalculator.ReviewStructureException;
import com.topcoder.util.errorhandling.BaseException;

import junit.framework.TestCase;
/**
 * Tests for ReviewStructureException class.
 * @author qiucx0161
 * @version 1.0
 */
public class TestReviewStructureException extends TestCase {

    /**
     * Review instance for testing.
     */
    private Review review = null;

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
     * Setup the test environment.
     * @throws Exception to JUnit.
     */
    protected void setUp() throws Exception {
        super.setUp();

        review = new Review();
    }

    /**
     * Setup the test environment.
     * @throws Exception to JUnit.
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Tests ReviewStructureException(String details, Review review) method with accuracy state.
     */
    public void testReviewStructureException1Accuracy() {
        ReviewStructureException e = new ReviewStructureException(ERROR_MESSAGE, review);

        assertNotNull("Unable to instantiate CalculationException", e);
        assertTrue("Exception should be extension of BaseException class", e instanceof BaseException);
        assertEquals("Error message is not properly set", ERROR_MESSAGE, e.getMessage());
        assertEquals("Error scorecard.", review, e.getReview());
    }

    /**
     * Tests ReviewStructureException(String details, Throwable cause, Review review) method with accuracy state.
     */
    public void testReviewStructureExceptionAccuracy() {
        ReviewStructureException e = new ReviewStructureException(ERROR_MESSAGE, CAUSE, review);

        assertNotNull("Unable to instantiate CalculationException", e);
        assertEquals("Cause is not properly set", CAUSE, e.getCause());
        assertEquals("Error scorecard.", review, e.getReview());
    }
}
