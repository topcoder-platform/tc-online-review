/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases.accuracytests;

import java.sql.Connection;
import java.util.Date;

import com.cronos.onlinereview.phases.SpecificationReviewPhaseHandler;
import com.topcoder.management.deliverable.Submission;
import com.topcoder.management.deliverable.Upload;
import com.topcoder.management.phase.OperationCheckResult;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.review.data.Review;
import com.topcoder.management.scorecard.data.Scorecard;
import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.PhaseStatus;
import com.topcoder.project.phases.Project;

/**
 * <p>
 * Accuracy tests for the {@link SpecificationReviewPhaseHandler} class.
 * </p>
 *
 * @author akinwale
 * @version 1.4
 */
public class SpecificationReviewPhaseHandlerAccuracyTests extends BaseTestCase {
    /**
     * <p>
     * The {@link SpecificationReviewPhaseHandler} instance to be tested.
     * </p>
     */
    private SpecificationReviewPhaseHandler handler;

    /**
     * <p>
     * Setup for each test in the test case.
     * </p>
     *
     * @throws Exception
     *             exception to pass to JUnit
     */
    public void setUp() throws Exception {
        super.setUp();
        handler = new SpecificationReviewPhaseHandler();
    }

    /**
     * <p>
     * Tests that the canPerform(Phase) method works properly.
     * </p>
     *
     * @throws Exception
     */
    public void testCanPerform_Open() throws Exception {
//        Project project = setupPhasesWithSpecificationPhases(false);
//
//        Phase submissionPhase = project.getAllPhases()[0];
//
//        // Test with the OPEN status
//        Phase reviewPhase = project.getAllPhases()[1];
//        reviewPhase.setPhaseStatus(PhaseStatus.OPEN);
//        reviewPhase.setActualStartDate(new Date());
//        reviewPhase.getAllDependencies()[0].getDependency().setPhaseStatus(PhaseStatus.CLOSED);
//
//        Connection conn = getConnection();
//        long reviewPhaseId = reviewPhase.getId();
//        Resource submitter = super.createResource(1, submissionPhase.getId(), 1, 17);
//        Resource reviewer = super.createResource(2, reviewPhaseId, 1, 18);
//
//        Upload upload = createUpload(1, 1, submitter.getId(), 1, 1, "uploadParam");
//        Submission submission = createSubmission(1, 1, 1, 2);
//        Scorecard scorecard = createScorecard(1, 1, 1, 1, "Scorecard Name", "5.0", 82.0f, 95.0f);
//        Review review = createReview(1, reviewer.getId(), submission.getId(), scorecard.getId(), false, 76.0f);
//
//        insertResources(conn, new Resource[] {submitter, reviewer});
//        insertResourceInfo(conn, submitter.getId(), 1, "101");
//        insertResourceInfo(conn, reviewer.getId(), 1, "102");
//        insertUploads(conn, new Upload[] {upload});
//        insertSubmissions(conn, new Submission[] {submission});
//        insertScorecards(conn, new Scorecard[] {scorecard});
//        insertReviews(conn, new Review[] {review});
//
//        assertEquals("canPerform does not work properly", review.isCommitted(), handler.canPerform(reviewPhase));
    }

    /**
     * <p>
     * Tests that the canPerform(Phase) method works properly.
     * </p>
     *
     * @throws Exception
     */
    public void testCanPerform_Open_NoSubmission() throws Exception {
        Project project = setupPhasesWithSpecificationPhases(false);

        Phase[] phases = project.getAllPhases();
        Phase submissionPhase = phases[0];
        Phase reviewPhase = phases[1];
        reviewPhase.setPhaseStatus(PhaseStatus.OPEN);
        reviewPhase.setActualStartDate(new Date());

        // Phase dependency met
        reviewPhase.getAllDependencies()[0].getDependency().setPhaseStatus(PhaseStatus.CLOSED);
        Connection conn = getConnection();

        long reviewPhaseId = reviewPhase.getId();
        Resource submitter = super.createResource(1, submissionPhase.getId(), 1, 17);
        Resource reviewer = super.createResource(2, reviewPhaseId, 1, 18);

        insertResources(conn, new Resource[] {submitter, reviewer});
        insertResourceInfo(conn, submitter.getId(), 1, "101");
        insertResourceInfo(conn, reviewer.getId(), 1, "102");
        OperationCheckResult result = handler.canPerform(reviewPhase);

        assertFalse("Not the expected checking result", result.isSuccess());
        assertEquals("Wrong message",  "Specification review is not yet committed",  result.getMessage());
    }

    /**
     * <p>
     * Tests that the canPerform(Phase) method works properly.
     * </p>
     *
     * @throws Exception
     */
    public void testCanPerform_Open_ReviewNotCommitted() throws Exception {
        Project project = setupPhasesWithSpecificationPhases(false);

        Phase[] phases = project.getAllPhases();
        Phase submissionPhase = phases[0];
        Phase reviewPhase = phases[1];
        reviewPhase.setPhaseStatus(PhaseStatus.OPEN);
        reviewPhase.setActualStartDate(new Date());

        // Phase dependency met
        reviewPhase.getAllDependencies()[0].getDependency().setPhaseStatus(PhaseStatus.CLOSED);

        Connection conn = getConnection();

        long reviewPhaseId = reviewPhase.getId();
        Resource submitter = super.createResource(1, submissionPhase.getId(), 1, 17);
        Resource reviewer = super.createResource(2, reviewPhaseId, 1, 18);

        Upload upload = createUpload(1, 1, submitter.getId(), 1, 1, "uploadParam");
        Submission submission = createSubmission(1, 1, 1);
        Scorecard scorecard = createScorecard(1, 1, 1, 1, "Scorecard Name", "5.0", 82.0f, 95.0f);
        Review review = createReview(1, reviewer.getId(), submission.getId(), scorecard.getId(), false, 76.0f);

        insertResources(conn, new Resource[] {submitter, reviewer});
        insertResourceInfo(conn, submitter.getId(), 1, "101");
        insertResourceInfo(conn, reviewer.getId(), 1, "102");
        insertUploads(conn, new Upload[] {upload});
        insertSubmissions(conn, new Submission[] {submission});
        insertScorecards(conn, new Scorecard[] {scorecard});
        insertReviews(conn, new Review[] {review});

        String error = "canPerform does not work properly";
        OperationCheckResult result = handler.canPerform(reviewPhase);

        assertFalse("Not the expected checking result", result.isSuccess());
        assertEquals("Wrong message",  "Specification review is not yet committed",  result.getMessage());
        assertEquals(error, review.isCommitted(), handler.canPerform(reviewPhase).isSuccess());
    }

    /**
     * <p>
     * Tests that the canPerform(Phase) method works properly.
     *
     * @throws Exception
     *             exception to pass to JUnit
     */
    public void testCanPerform_Scheduled() throws Exception {
//        Project project = super.setupPhasesWithSpecificationPhases(false);
//        Phase[] phases = project.getAllPhases();
//        Phase submissionPhase = phases[0];
//        Phase reviewPhase = phases[1];
//
//        // Test with the SCHEDULED status
//        submissionPhase.setPhaseStatus(PhaseStatus.CLOSED);
//        reviewPhase.setPhaseStatus(PhaseStatus.SCHEDULED);
//
//        // Phase has started and dependency met
//        reviewPhase.setActualStartDate(new Date());
//        reviewPhase.getAllDependencies()[0].getDependency().setPhaseStatus(PhaseStatus.CLOSED);
//
//        Connection conn = getConnection();
//        conn.setAutoCommit(true);
//
//        Resource submitter = super.createResource(1, submissionPhase.getId(), 1, 17);
//        Upload upload = createUpload(1, project.getId(), submitter.getId(), 1, 1, "parameter");
//        Submission submission = createSubmission(1, upload.getId(), 1, 2);
//
//        insertResources(conn, new Resource[] {submitter});
//        insertUploads(conn, new Upload[] {upload});
//        insertSubmissions(conn, new Submission[] {submission});
//
//        OperationCheckResult result = handler.canPerform(reviewPhase);
//
//        assertTrue("Not the expected checking result", result.isSuccess());
//        assertEquals("Wrong message",  "XXXXXXXX",  result.getMessage());
    }

    /**
     * <p>
     * Tests that the canPerform(Phase) method works properly.
     * </p>
     *
     * @throws Exception
     *             exception to pass to JUnit
     */
    public void testCanPerform_Scheduled_NotYetStarted() throws Exception {
        Project project = super.setupPhasesWithSpecificationPhases(false);
        Phase[] phases = project.getAllPhases();
        Phase reviewPhase = phases[1];

        // Set the phase status to scheduled
        reviewPhase.setPhaseStatus(PhaseStatus.SCHEDULED);

        // Dependency met but phase not yet started
        reviewPhase.getAllDependencies()[0].getDependency().setPhaseStatus(PhaseStatus.CLOSED);

        OperationCheckResult result = handler.canPerform(reviewPhase);

        assertFalse("Not the expected checking result", result.isSuccess());
        assertEquals("Wrong message",  "Phase start time is not yet reached.",  result.getMessage());
    }

    /**
     * <p>
     * Tests that the canPerform(Phase) method works properly.
     * </p>
     *
     * @throws Exception
     */
    public void testCanPerform_Scheduled_DependencyNotMet() throws Exception {
        Project project = super.setupPhasesWithSpecificationPhases(false);
        Phase[] phases = project.getAllPhases();
        Phase reviewPhase = phases[1];

        // Set the phase status to scheduled
        reviewPhase.setPhaseStatus(PhaseStatus.SCHEDULED);

        // Phase started but dependency not met
        reviewPhase.setActualStartDate(new Date());

        OperationCheckResult result = handler.canPerform(reviewPhase);

        assertFalse("Not the expected checking result", result.isSuccess());
        assertEquals("Wrong message",  "Dependency Specification Submission phase is not yet ended.",  result.getMessage());
    }

    /**
     * <p>
     * Tests that the canPerform(Phase) method works properly.
     * </p>
     *
     * @throws Exception
     */
    public void testCanPerform_Scheduled_NoSubmission() throws Exception {
        Project project = super.setupPhasesWithSpecificationPhases(false);
        Phase[] phases = project.getAllPhases();
        Phase reviewPhase = phases[1];

        // Set the phase status to scheduled
        reviewPhase.setPhaseStatus(PhaseStatus.SCHEDULED);
        reviewPhase.setActualStartDate(new Date());

        // Dependency met but no submissions
        reviewPhase.getAllDependencies()[0].getDependency().setPhaseStatus(PhaseStatus.CLOSED);

        OperationCheckResult result = handler.canPerform(reviewPhase);

        assertFalse("Not the expected checking result", result.isSuccess());
        assertEquals("Wrong message",  "Specification submission doesn't exist.",  result.getMessage());
    }


    /**
     * <p>
     * Tests that the perform(Phase, String) method works properly.
     * </p>
     *
     * @throws Exception
     *             exception to pass to JUnit
     */
    public void testPerform() throws Exception {
        Project project = setupPhasesWithSpecificationPhases(false);

        // test with scheduled status
        Phase submissionPhase = project.getAllPhases()[0];
        submissionPhase.setPhaseStatus(PhaseStatus.CLOSED);

        Phase reviewPhase = project.getAllPhases()[1];
        String operator = "901";

        Connection conn = getConnection();
        long reviewPhaseId = reviewPhase.getId();
        Resource submitter = super.createResource(1, submissionPhase.getId(), 1, 17);
        Resource reviewer = super.createResource(3, reviewPhaseId, 1, 18);

        Upload upload = createUpload(1, project.getId(), submitter.getId(), 1, 1, "parameter");
        Submission submission = createSubmission(1, upload.getId(), 1, 2);
        Scorecard scorecard = createScorecard(1, 1, 1, 1, "Scorecard Name", "5.0", 82.0f, 95.0f);
        Review review = createReview(1, reviewer.getId(), submission.getId(), scorecard.getId(), false, 76.0f);

        insertResources(conn, new Resource[] {submitter, reviewer});
        insertResourceInfo(conn, submitter.getId(), 1, "101");
        insertResourceInfo(conn, reviewer.getId(), 1, "102");
        insertUploads(conn, new Upload[] {upload});
        insertSubmissions(conn, new Submission[] {submission});
        insertScorecards(conn, new Scorecard[] {scorecard});
        insertReviews(conn, new Review[] {review});

        handler.perform(reviewPhase, operator);
        // Email needs to be checked manually
    }

    /**
     * <p>
     * Tests that the perform(Phase, String) method works properly.
     * </p>
     *
     * @throws Exception
     *             exception to pass to JUnit
     */
    public void testPerform_Open() throws Exception {
//        Project project = setupPhasesWithSpecificationPhases(false);
//        Phase submissionPhase = project.getAllPhases()[0];
//        submissionPhase.setPhaseStatus(PhaseStatus.CLOSED);
//
//        // Test with OPEN status
//        Phase reviewPhase = project.getAllPhases()[1];
//        reviewPhase.setPhaseStatus(PhaseStatus.OPEN);
//        String operator = "901";
//
//        Connection conn = getConnection();
//        long reviewPhaseId = reviewPhase.getId();
//        Resource submitter = super.createResource(1, submissionPhase.getId(), 1, 17);
//        Resource reviewer = super.createResource(3, reviewPhaseId, 1, 18);
//
//        Upload upload = createUpload(1, 1, submitter.getId(), 1, 1, "param");
//        Submission submission = createSubmission(1, 1, 1, 2);
//        Scorecard scorecard = createScorecard(1, 1, 1, 1, "Scorecard Name", "5.0", 82.0f, 95.0f);
//        Review review = createReview(1, reviewer.getId(), submission.getId(), scorecard.getId(), true, 76.0f);
//        // add a rejected comment
//        review.addComment(createComment(1111, reviewer.getId(), "Approved", 14,
//            "Specification Review Comment"));
//        insertResources(conn, new Resource[] {submitter, reviewer});
//        insertResourceInfo(conn, submitter.getId(), 1, "101");
//        insertResourceInfo(conn, reviewer.getId(), 1, "102");
//        insertUploads(conn, new Upload[] {upload});
//        insertSubmissions(conn, new Submission[] {submission});
//        insertScorecards(conn, new Scorecard[] {scorecard});
//        insertReviews(conn, new Review[] {review});
//        insertCommentsWithExtraInfo(conn, new long[] {1}, new long[] {reviewer.getId()},
//            new long[] {review.getId()}, new String[] {"Approved Comment"}, new long[] {14},
//            new String[] {"Approved"});
//
//        handler.perform(reviewPhase, operator);
        // Email needs to be checked manually
    }
}
