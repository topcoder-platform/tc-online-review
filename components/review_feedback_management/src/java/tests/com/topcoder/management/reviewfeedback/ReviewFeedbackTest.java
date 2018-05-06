/*
 * Copyright (C) 2012, 2013 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.reviewfeedback;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * Unit test case of {@link ReviewFeedback}.
 * </p>
 *
 * <p>
 * <em>Changes in 2.0:</em>
 * <ol>
 * <li>Removed test cases for fields: id, reviewerUserId, score, feedbackText, createUser, createDate</li>
 * <li>Added test cases for fields: comment, details</li>
 * </ol>
 * </p>
 *
 * @author amazingpig, sparemax
 * @version 2.0
 */
public class ReviewFeedbackTest extends TestCase {
    /**
     * <p>
     * Represents the ReviewFeedback instance to test against.
     * </p>
     */
    private ReviewFeedback reviewFeedback;

    /**
     * <p>
     * Creates a test suite for the tests in this test case.
     * </p>
     *
     * @return a TestSuite for this test case.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(ReviewFeedbackTest.class);
        return suite;
    }

    /**
     * <p>
     * Sets up test environment.
     * </p>
     *
     * @throws Exception to jUnit.
     */
    @Override
    protected void setUp() throws Exception {
        reviewFeedback = new ReviewFeedback();
    }

    /**
     * <p>
     * Tears down test environment.
     * </p>
     *
     * @throws Exception to jUnit.
     */
    @Override
    protected void tearDown() throws Exception {
        reviewFeedback = null;
    }

    /**
     * <p>
     * Accuracy test for constructor <code>ReviewFeedback()</code>.
     * It verifies the new instance is created.
     * </p>
     */
    public void testReviewFeedback() {
        assertNotNull("Unable to instantiate ReviewFeedback", reviewFeedback);
    }

    /**
      * <p>
      * Accuracy test method for {@link ReviewFeedback#getProjectId()}. It verifies the return value is correct.
      * </p>
      */
    public void testGetProjectId() {
        assertEquals("The default value for projectId is wrong", 0, reviewFeedback.getProjectId());

        // set a value
        reviewFeedback.setProjectId(1);
        assertEquals("The value of projectId is wrong", 1, reviewFeedback.getProjectId());
    }

    /**
     * <p>
     * Accuracy test method for {@link ReviewFeedback#setProjectId(long)}. It verifies the value is correctly set.
     * </p>
     */
    public void testSetProjectId() {
        // set a value
        reviewFeedback.setProjectId(2);
        assertEquals("The value of projectId is wrong", 2, reviewFeedback.getProjectId());
    }

    /**
     * <p>
     * Accuracy test for the method <code>getComment()</code>.<br>
     * The value should be properly retrieved.
     * </p>
     *
     * @since 2.0
     */
    public void test_getComment() {
        String value = "new_value";
        reviewFeedback.setComment(value);

        assertEquals("'getComment' should be correct.",
            value, reviewFeedback.getComment());
    }

    /**
     * <p>
     * Accuracy test for the method <code>setComment(String comment)</code>.<br>
     * The value should be properly set.
     * </p>
     *
     * @since 2.0
     */
    public void test_setComment() {
        String value = "new_value";
        reviewFeedback.setComment(value);

        assertEquals("'setComment' should be correct.",
            value, BaseUnitTest.getField(reviewFeedback, "comment"));
    }

    /**
     * <p>
     * Accuracy test for the method <code>getDetails()</code>.<br>
     * The value should be properly retrieved.
     * </p>
     *
     * @since 2.0
     */
    public void test_getDetails() {
        List<ReviewFeedbackDetail> value = new ArrayList<ReviewFeedbackDetail>();
        reviewFeedback.setDetails(value);

        assertSame("'getDetails' should be correct.",
            value, reviewFeedback.getDetails());
    }

    /**
     * <p>
     * Accuracy test for the method <code>setDetails(List&lt;ReviewFeedbackDetail&gt; details)</code>.<br>
     * The value should be properly set.
     * </p>
     *
     * @since 2.0
     */
    public void test_setDetails() {
        List<ReviewFeedbackDetail> value = new ArrayList<ReviewFeedbackDetail>();
        reviewFeedback.setDetails(value);

        assertSame("'setDetails' should be correct.",
            value, BaseUnitTest.getField(reviewFeedback, "details"));
    }
}
