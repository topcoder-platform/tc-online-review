/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.reviewfeedback;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * Unit tests for {@link ReviewFeedbackDetail} class.
 * </p>
 *
 * @author sparemax
 * @version 2.0
 * @since 2.0
 */
public class ReviewFeedbackDetailUnitTests extends TestCase {
    /**
     * <p>
     * Represents the <code>ReviewFeedbackDetail</code> instance used in tests.
     * </p>
     */
    private ReviewFeedbackDetail instance;

    /**
     * <p>
     * Creates a test suite for the tests in this test case.
     * </p>
     *
     * @return a TestSuite for this test case.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(ReviewFeedbackDetailUnitTests.class);
        return suite;
    }

    /**
     * <p>
     * Sets up the unit tests.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void setUp() throws Exception {
        instance = new ReviewFeedbackDetail();
    }

    /**
     * <p>
     * Accuracy test for the constructor <code>ReviewFeedbackDetail()</code>.<br>
     * Instance should be correctly created.
     * </p>
     */
    public void testCtor() {
        instance = new ReviewFeedbackDetail();

        assertEquals("'reviewerUserId' should be correct.", 0L, BaseUnitTest.getField(instance, "reviewerUserId"));
        assertEquals("'score' should be correct.", 0, BaseUnitTest.getField(instance, "score"));
        assertNull("'feedbackText' should be correct.", BaseUnitTest.getField(instance, "feedbackText"));
    }


    /**
     * <p>
     * Accuracy test for the method <code>getReviewerUserId()</code>.<br>
     * The value should be properly retrieved.
     * </p>
     */
    public void test_getReviewerUserId() {
        long value = 1L;
        instance.setReviewerUserId(value);

        assertEquals("'getReviewerUserId' should be correct.",
            value, instance.getReviewerUserId());
    }

    /**
     * <p>
     * Accuracy test for the method <code>setReviewerUserId(long reviewerUserId)</code>.<br>
     * The value should be properly set.
     * </p>
     */
    public void test_setReviewerUserId() {
        long value = 1L;
        instance.setReviewerUserId(value);

        assertEquals("'setReviewerUserId' should be correct.",
            value, BaseUnitTest.getField(instance, "reviewerUserId"));
    }

    /**
     * <p>
     * Accuracy test for the method <code>getScore()</code>.<br>
     * The value should be properly retrieved.
     * </p>
     */
    public void test_getScore() {
        int value = 1;
        instance.setScore(value);

        assertEquals("'getScore' should be correct.",
            value, instance.getScore());
    }

    /**
     * <p>
     * Accuracy test for the method <code>setScore(int score)</code>.<br>
     * The value should be properly set.
     * </p>
     */
    public void test_setScore() {
        int value = 1;
        instance.setScore(value);

        assertEquals("'setScore' should be correct.",
            value, BaseUnitTest.getField(instance, "score"));
    }

    /**
     * <p>
     * Accuracy test for the method <code>getFeedbackText()</code>.<br>
     * The value should be properly retrieved.
     * </p>
     */
    public void test_getFeedbackText() {
        String value = "new_value";
        instance.setFeedbackText(value);

        assertEquals("'getFeedbackText' should be correct.",
            value, instance.getFeedbackText());
    }

    /**
     * <p>
     * Accuracy test for the method <code>setFeedbackText(String feedbackText)</code>.<br>
     * The value should be properly set.
     * </p>
     */
    public void test_setFeedbackText() {
        String value = "new_value";
        instance.setFeedbackText(value);

        assertEquals("'setFeedbackText' should be correct.",
            value, BaseUnitTest.getField(instance, "feedbackText"));
    }
}