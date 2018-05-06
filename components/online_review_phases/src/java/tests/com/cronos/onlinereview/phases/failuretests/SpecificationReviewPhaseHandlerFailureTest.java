/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases.failuretests;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;

import com.cronos.onlinereview.phases.PhaseNotSupportedException;
import com.cronos.onlinereview.phases.SpecificationReviewPhaseHandler;
import com.cronos.onlinereview.phases.SubmissionPhaseHandler;
import com.cronos.onlinereview.phases.failuretests.mock.MockResourceManager;
import com.cronos.onlinereview.phases.failuretests.mock.MockReviewManager;
import com.cronos.onlinereview.phases.failuretests.mock.MockUploadManager;
import com.topcoder.management.deliverable.Submission;
import com.topcoder.management.deliverable.SubmissionStatus;
import com.topcoder.management.deliverable.persistence.UploadPersistenceException;
import com.topcoder.management.phase.PhaseHandlingException;
import com.topcoder.management.resource.persistence.ResourcePersistenceException;
import com.topcoder.management.review.data.Comment;
import com.topcoder.management.review.data.CommentType;
import com.topcoder.management.review.data.Review;
import com.topcoder.search.builder.SearchBuilderException;

/**
 * <p>
 * Failure tests for class <code>SpecificationReviewPhaseHandler</code>.
 * </p>
 *
 * @author stevenfrog
 * @version 1.0
 */
public class SpecificationReviewPhaseHandlerFailureTest extends AbstractTestCase {
    /**
     * <p>
     * Represent the instance that used to call its method for test. It will be initialized in
     * setUp().
     * </p>
     */
    private SpecificationReviewPhaseHandler impl;

    /**
     * <p>
     * Gets the test suite for {@link SubmissionPhaseHandler} class.
     * </p>
     *
     * @return a <code>TestSuite</code> providing the tests for {@link SubmissionPhaseHandler}
     *         class.
     */
    public static Test suite() {
        return new TestSuite(SpecificationReviewPhaseHandlerFailureTest.class);
    }

    /**
     * <p>
     * Set the test environment.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void setUp() throws Exception {
        super.setUp();
        impl = new SpecificationReviewPhaseHandler(TestDataFactory.NAMESPACE);
    }

    /**
     * <p>
     * Clear the test environment.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void tearDown() throws Exception {
        super.tearDown();
        impl = null;
    }

    /**
     * <p>
     * Failure test for the constructor <code>SpecificationReviewPhaseHandler(String)</code>.<br>
     * The namespace is null.<br>
     * Expect IllegalArgumentException.
     * </p>
     */
    public void testConstructor_String_namespace_null() {
        try {
            new SpecificationReviewPhaseHandler(null);
            Assert.fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>
     * Failure test for the constructor <code>SpecificationReviewPhaseHandler(String)</code>.<br>
     * The namespace is ZERO_LENGTH_STRING.<br>
     * Expect IllegalArgumentException.
     * </p>
     */
    public void testConstructor_String_namespace_ZERO_LENGTH_STRING() {
        try {
            new SpecificationReviewPhaseHandler(TestDataFactory.ZERO_LENGTH_STRING);
            Assert.fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>
     * Failure test for the constructor <code>SpecificationReviewPhaseHandler(String)</code>.<br>
     * The namespace is WHITESPACE_ONLY_STRING.<br>
     * Expect IllegalArgumentException.
     * </p>
     */
    public void testConstructor_String_namespace_WHITESPACE_ONLY_STRING() {
        try {
            new SpecificationReviewPhaseHandler(TestDataFactory.WHITESPACE_ONLY_STRING);
            Assert.fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>
     * Failure test for the method <code>canPerform(Phase)</code>.<br>
     * The phase is null.<br>
     * Expect IllegalArgumentException.
     * </p>
     */
    public void testCanPerform_Phase_phase_null() {
        try {
            impl.canPerform(null);
            Assert.fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>
     * Failure test for the method <code>canPerform(Phase)</code>.<br>
     * The phase is registration phase.<br>
     * Expect PhaseNotSupportedException.
     * </p>
     */
    public void testCanPerform_Phase_phase_RegistrationPhase() {
        try {
            impl.canPerform(TestDataFactory.getRegistrationPhase());
            Assert.fail("PhaseNotSupportedException should have been thrown");
        } catch (PhaseNotSupportedException e) {
            // expected behavior
        } catch (Exception e) {
            Assert.fail("PhaseNotSupportedException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>
     * Failure test for the method <code>canPerform(Phase)</code>.<br>
     * The phase is specification closed review phase.<br>
     * Expect PhaseHandlingException.
     * </p>
     */
    public void testCanPerform_Phase_phase_SpecificationClosedReviewPhase() {
        try {
            impl.canPerform(TestDataFactory.getClosedSpecificationReviewPhase());
            Assert.fail("PhaseHandlingException should have been thrown");
        } catch (PhaseHandlingException e) {
            // expected behavior
        } catch (Exception e) {
            Assert.fail("PhaseHandlingException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>
     * Failure test for the method <code>canPerform(Phase)</code>.<br>
     * Configures the mock implementation <code>MockUploadManager</code> to throw an exception from
     * any method.<br>
     * Expect PhaseHandlingException.
     * </p>
     */
    public void testCanPerform_Phase_UploadManagerError_UploadPersistenceException() {
        MockUploadManager.throwGlobalException(new UploadPersistenceException("FailureTest"));
        try {
            impl.canPerform(TestDataFactory.getOpenSpecificationReviewPhase());
            Assert.fail("PhaseHandlingException should have been thrown");
        } catch (PhaseHandlingException e) {
            // expected behavior
        } catch (Exception e) {
            Assert.fail("PhaseHandlingException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>
     * Failure test for the method <code>canPerform(Phase)</code>.<br>
     * Configures the mock implementation <code>MockUploadManager</code> to throw an exception from
     * any method.<br>
     * Expect PhaseHandlingException.
     * </p>
     */
    public void testCanPerform_Phase_UploadManagerError_SearchBuilderException() {
        MockUploadManager.throwGlobalException(new SearchBuilderException("FailureTest"));
        try {
            impl.canPerform(TestDataFactory.getOpenSpecificationReviewPhase());
            Assert.fail("PhaseHandlingException should have been thrown");
        } catch (PhaseHandlingException e) {
            // expected behavior
        } catch (Exception e) {
            Assert.fail("PhaseHandlingException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>
     * Failure test for the method <code>canPerform(Phase)</code>.<br>
     * Configures the mock implementation <code>MockReviewManager</code> to make program throw an
     * exception from any method.<br>
     * Expect PhaseHandlingException.
     * </p>
     */
    public void testCanPerform_Phase_UploadManagerError_MultipleSpecificationSubmissions() {
        Submission[] submissions = new Submission[2];
        submissions[0] = new Submission();
        submissions[1] = new Submission();
        MockUploadManager.setMethodResult("searchSubmissions_Filter", submissions);

        try {
            impl.canPerform(TestDataFactory.getOpenSpecificationReviewPhase());
            Assert.fail("PhaseHandlingException should have been thrown");
        } catch (PhaseHandlingException e) {
            // expected behavior
        } catch (Exception e) {
            Assert.fail("PhaseHandlingException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>
     * Failure test for the method <code>canPerform(Phase)</code>.<br>
     * Configures the mock implementation <code>MockReviewManager</code> to make program throw an
     * exception from any method.<br>
     * Expect PhaseHandlingException.
     * </p>
     */
    public void testCanPerform_Phase_UploadManagerError_MultipleSpecificationReviews() {
        Submission[] submissions = new Submission[1];
        submissions[0] = new Submission();
        submissions[0].setId(1);
        MockUploadManager.setMethodResult("searchSubmissions_Filter", submissions);

        Review[] reviews = new Review[2];
        reviews[0] = new Review();
        reviews[1] = new Review();
        MockReviewManager.setMethodResult("searchReviews_Filter_boolean", reviews);
        try {
            impl.canPerform(TestDataFactory.getOpenSpecificationReviewPhase());
            Assert.fail("PhaseHandlingException should have been thrown");
        } catch (PhaseHandlingException e) {
            // expected behavior
        } catch (Exception e) {
            Assert.fail("PhaseHandlingException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>
     * Failure test for the method <code>perform(Phase,String)</code>.<br>
     * The phase is null.<br>
     * Expect IllegalArgumentException.
     * </p>
     */
    public void testPerform_Phase_String_phase_null() {
        try {
            impl.perform(null, TestDataFactory.OPERATOR);
            Assert.fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>
     * Failure test for the method <code>perform(Phase,String)</code>.<br>
     * The operator is null.<br>
     * Expect IllegalArgumentException.
     * </p>
     */
    public void testPerform_Phase_String_operator_null() {
        try {
            impl.perform(TestDataFactory.getReviewPhase(), null);
            Assert.fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>
     * Failure test for the method <code>perform(Phase,String)</code>.<br>
     * The operator is ZERO_LENGTH_STRING.<br>
     * Expect IllegalArgumentException.
     * </p>
     */
    public void testPerform_Phase_String_operator_ZERO_LENGTH_STRING() {
        try {
            impl.perform(TestDataFactory.getSpecificationReviewPhase(), TestDataFactory.ZERO_LENGTH_STRING);
            Assert.fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>
     * Failure test for the method <code>perform(Phase,String)</code>.<br>
     * The operator is WHITESPACE_ONLY_STRING.<br>
     * Expect IllegalArgumentException.
     * </p>
     */
    public void testPerform_Phase_String_operator_WHITESPACE_ONLY_STRING() {
        try {
            impl.perform(TestDataFactory.getSpecificationReviewPhase(),
                TestDataFactory.WHITESPACE_ONLY_STRING);
            Assert.fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>
     * Failure test for the method <code>perform(Phase,String)</code>.<br>
     * The phase is registration phase.<br>
     * Expect PhaseNotSupportedException.
     * </p>
     */
    public void testPerform_Phase_String_phase_RegistrationPhase() {
        try {
            impl.perform(TestDataFactory.getRegistrationPhase(), TestDataFactory.OPERATOR);
            Assert.fail("PhaseNotSupportedException should have been thrown");
        } catch (PhaseNotSupportedException e) {
            // expected behavior
        } catch (Exception e) {
            Assert.fail("PhaseNotSupportedException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>
     * Failure test for the method <code>perform(Phase,String)</code>.<br>
     * The phase is closed specification review phase.<br>
     * Expect PhaseHandlingException.
     * </p>
     */
    public void testPerform_Phase_String_phase_ClosedSpecificationReviewPhase() {
        try {
            impl.perform(TestDataFactory.getClosedSpecificationReviewPhase(), TestDataFactory.OPERATOR);
            Assert.fail("PhaseHandlingException should have been thrown");
        } catch (PhaseHandlingException e) {
            // expected behavior
        } catch (Exception e) {
            Assert.fail("PhaseHandlingException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>
     * Failure test for the method <code>perform(Phase,String)</code>.<br>
     * Configures the mock implementation <code>MockResourceManager</code> to make program throw an
     * exception from any method.<br>
     * Expect PhaseHandlingException.
     * </p>
     */
    public void testPerform_Phase_String_ScheduledPhase_ResourcePersistenceException() {
        MockResourceManager.throwGlobalException(new ResourcePersistenceException("FailureTest"));

        try {
            impl.perform(TestDataFactory.getScheduledSpecificationReviewPhase(), TestDataFactory.OPERATOR);
            Assert.fail("PhaseHandlingException should have been thrown");
        } catch (PhaseHandlingException e) {
            // expected behavior
        } catch (Exception e) {
            Assert.fail("PhaseHandlingException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>
     * Failure test for the method <code>perform(Phase,String)</code>.<br>
     * Configures the mock implementation <code>MockUploadManager</code> to make program throw an
     * exception from any method.<br>
     * Expect PhaseHandlingException.
     * </p>
     */
    public void testPerform_Phase_String_OpenPhase_NoSubmission() {
        try {
            impl.perform(TestDataFactory.getOpenSpecificationReviewPhase(), TestDataFactory.OPERATOR);
            Assert.fail("PhaseHandlingException should have been thrown");
        } catch (PhaseHandlingException e) {
            // expected behavior
        } catch (Exception e) {
            Assert.fail("PhaseHandlingException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>
     * Failure test for the method <code>perform(Phase,String)</code>.<br>
     * Configures the mock implementation <code>MockUploadManager</code> to make program throw an
     * exception from any method.<br>
     * Expect PhaseHandlingException.
     * </p>
     */
    public void testPerform_Phase_String_OpenPhase_ReviewNotCommitted() {
        Submission[] submissions = new Submission[1];
        submissions[0] = new Submission();
        submissions[0].setId(1);
        MockUploadManager.setMethodResult("searchSubmissions_Filter", submissions);

        try {
            impl.perform(TestDataFactory.getOpenSpecificationReviewPhase(), TestDataFactory.OPERATOR);
            Assert.fail("PhaseHandlingException should have been thrown");
        } catch (PhaseHandlingException e) {
            // expected behavior
        } catch (Exception e) {
            Assert.fail("PhaseHandlingException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>
     * Failure test for the method <code>perform(Phase,String)</code>.<br>
     * Configures the mock implementation <code>MockUploadManager</code> to make program throw an
     * exception from any method.<br>
     * Expect PhaseHandlingException.
     * </p>
     */
    public void testPerform_Phase_String_OpenPhase_ReviewForSpecificationNotExist() {
        Submission[] submissions = new Submission[1];
        submissions[0] = new Submission();
        submissions[0].setId(1);
        MockUploadManager.setMethodResult("searchSubmissions_Filter", submissions);

        Review[] reviews = new Review[0];
        MockReviewManager.setMethodResult("searchReviews_Filter_boolean", reviews);

        try {
            impl.perform(TestDataFactory.getOpenSpecificationReviewPhase(), TestDataFactory.OPERATOR);
            Assert.fail("PhaseHandlingException should have been thrown");
        } catch (PhaseHandlingException e) {
            // expected behavior
        } catch (Exception e) {
            Assert.fail("PhaseHandlingException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>
     * Failure test for the method <code>perform(Phase,String)</code>.<br>
     * Configures the mock implementation <code>MockUploadManager</code> to make program throw an
     * exception from any method.<br>
     * Expect PhaseHandlingException.
     * </p>
     */
    public void testPerform_Phase_String_OpenPhase_InvalidComment() {
        Submission[] submissions = new Submission[1];
        submissions[0] = new Submission();
        submissions[0].setId(1);
        MockUploadManager.setMethodResult("searchSubmissions_Filter", submissions);

        Review[] reviews = new Review[1];
        reviews[0] = new Review();
        reviews[0].setCommitted(true);
        Comment comment = new Comment();
        comment.setCommentType(new CommentType(1, "Specification Review Comment"));
        comment.setExtraInfo("ABC");
        reviews[0].addComment(comment);
        MockReviewManager.setMethodResult("searchReviews_Filter_boolean", reviews);

        try {
            impl.perform(TestDataFactory.getOpenSpecificationReviewPhase(), TestDataFactory.OPERATOR);
            Assert.fail("PhaseHandlingException should have been thrown");
        } catch (PhaseHandlingException e) {
            // expected behavior
        } catch (Exception e) {
            Assert.fail("PhaseHandlingException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>
     * Failure test for the method <code>perform(Phase,String)</code>.<br>
     * Configures the mock implementation <code>MockUploadManager</code> to make program throw an
     * exception from any method.<br>
     * Expect PhaseHandlingException.
     * </p>
     */
    public void testPerform_Phase_String_OpenPhase_UploadPersistenceException() {
        Submission[] submissions = new Submission[1];
        submissions[0] = new Submission();
        submissions[0].setId(1);
        MockUploadManager.setMethodResult("searchSubmissions_Filter", submissions);

        Review[] reviews = new Review[1];
        reviews[0] = new Review();
        reviews[0].setCommitted(true);
        Comment comment = new Comment();
        comment.setCommentType(new CommentType(1, "Specification Review Comment"));
        comment.setExtraInfo("Rejected");
        reviews[0].addComment(comment);
        MockReviewManager.setMethodResult("searchReviews_Filter_boolean", reviews);

        SubmissionStatus[] statuses = new SubmissionStatus[1];
        statuses[0] = new SubmissionStatus();
        statuses[0].setName("Failed Review");
        MockUploadManager.setMethodResult("getAllSubmissionStatuses", statuses);

        MockUploadManager.throwException("updateSubmission_Submission_String",
            new UploadPersistenceException("FailureTest"));

        try {
            impl.perform(TestDataFactory.getOpenSpecificationReviewPhase(), TestDataFactory.OPERATOR);
            Assert.fail("PhaseHandlingException should have been thrown");
        } catch (PhaseHandlingException e) {
            // expected behavior
        } catch (Exception e) {
            Assert.fail("PhaseHandlingException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>
     * Failure test for the method <code>perform(Phase,String)</code>.<br>
     * Configures the mock implementation <code>MockUploadManager</code> to make program throw an
     * exception from any method.<br>
     * Expect PhaseHandlingException.
     * </p>
     */
    public void testPerform_Phase_String_OpenPhase_NoSpecificationReviewComment() {
        Submission[] submissions = new Submission[1];
        submissions[0] = new Submission();
        submissions[0].setId(1);
        MockUploadManager.setMethodResult("searchSubmissions_Filter", submissions);

        Review[] reviews = new Review[1];
        reviews[0] = new Review();
        reviews[0].setCommitted(true);
        Comment comment = new Comment();
        comment.setCommentType(new CommentType(1, "Specification Submission Comment"));
        comment.setExtraInfo("Rejected");
        reviews[0].addComment(comment);
        MockReviewManager.setMethodResult("searchReviews_Filter_boolean", reviews);

        try {
            impl.perform(TestDataFactory.getOpenSpecificationReviewPhase(), TestDataFactory.OPERATOR);
            Assert.fail("PhaseHandlingException should have been thrown");
        } catch (PhaseHandlingException e) {
            // expected behavior
        } catch (Exception e) {
            Assert.fail("PhaseHandlingException was expected but the original exception is : " + e);
        }
    }

}
