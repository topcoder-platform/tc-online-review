/*
 * Copyright (C) 2009 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases.accuracytests;

import com.cronos.onlinereview.phases.FinalReviewPhaseHandler;

import com.topcoder.management.deliverable.Submission;
import com.topcoder.management.deliverable.Upload;
import com.topcoder.management.phase.OperationCheckResult;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.review.data.Review;
import com.topcoder.management.scorecard.data.Scorecard;

import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.PhaseStatus;
import com.topcoder.project.phases.Project;

import java.sql.Connection;

import java.util.Date;


/**
 * Accuracy tests for change functions in version 1.1 of FinalReviewPhaseHandler class .
 *
 * @author myxgyy
 * @version 1.0
 */
public class FinalReviewPhaseHandlerAccTestsV11 extends BaseTestCase {
    /** Target instance. */
    private FinalReviewPhaseHandler handler;

    /**
     * Sets up the environment required for test cases for this class.
     *
     * @throws Exception not under test.
     */
    protected void setUp() throws Exception {
        super.setUp();
        handler = new FinalReviewPhaseHandler(PHASE_HANDLER_NAMESPACE);
    }

    /**
     * cleans up the environment required for test cases for this class.
     *
     * @throws Exception not under test.
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Tests the canPerform with Scheduled statuses.
     *
     * @throws Exception not under test.
     */
    public void testCanPerform1() throws Exception {
        Project project = super.setupPhases();
        Phase[] phases = project.getAllPhases();
        Phase finalReviewPhase = phases[9];

        // test with scheduled status.
        finalReviewPhase.setPhaseStatus(PhaseStatus.SCHEDULED);

        // time has not passed, nor dependencies met
        OperationCheckResult result = handler.canPerform(finalReviewPhase);

        assertFalse("Not the expected checking result", result.isSuccess());
        assertEquals("Wrong message",  "Dependency Final Fix phase is not yet ended.",  result.getMessage());

        // time has passed, but dependency not met.
        finalReviewPhase.setActualStartDate(new Date());
         result = handler.canPerform(finalReviewPhase);

        assertFalse("Not the expected checking result", result.isSuccess());
        assertEquals("Wrong message",  "Dependency Final Fix phase is not yet ended.",  result.getMessage());

        // time has passed and dependency met.
        finalReviewPhase.getAllDependencies()[0].getDependency().setPhaseStatus(PhaseStatus.CLOSED);
         result = handler.canPerform(finalReviewPhase);

        assertTrue("Not the expected checking result", result.isSuccess());
        assertEquals("Wrong message",  null,  result.getMessage());
    }

    /**
     * Tests the canPerform with Open statuses.
     *
     * @throws Exception not under test.
     */
    public void testCanPerform2() throws Exception {
        Project project = super.setupPhases();
        Phase[] phases = project.getAllPhases();
        Phase finalReviewPhase = phases[9];

        // change dependency type to F2F
        finalReviewPhase.getAllDependencies()[0].setDependentStart(false);

        // test with open status.
        finalReviewPhase.setPhaseStatus(PhaseStatus.OPEN);

        // time has not passed, dependencies not met
        OperationCheckResult result = handler.canPerform(finalReviewPhase);

        assertFalse("Not the expected checking result", result.isSuccess());
        assertEquals("Wrong message",  "Dependency Final Fix phase is not yet ended.",  result.getMessage());
    }

    /**
     * Tests the perform with Open status.
     *
     * @throws Exception to JUnit.
     */
    public void testPerform1() throws Exception {
        Project project = super.setupPhases();
        Phase[] phases = project.getAllPhases();
        Phase finalReviewPhase = phases[9];
        finalReviewPhase.setPhaseStatus(PhaseStatus.OPEN);

        // populate db with required data
        // final reviewer resource
        Resource finalReviewer = createResource(101, finalReviewPhase.getId(), project.getId(), 9);
        Upload frUpload = createUpload(1, project.getId(), finalReviewer.getId(), 4, 1, "parameter");
        Submission frSubmission = createSubmission(1, frUpload.getId(), 1);

        // reviewer resource and related review
        Scorecard scorecard1 = createScorecard(1, 1, 2, 1, "name", "1.0", 75.0f, 100.0f);
        Review frWorksheet = createReview(11, finalReviewer.getId(), frSubmission.getId(), scorecard1.getId(), true,
                90.0f);
        // add a rejected comment
        frWorksheet.addComment(createComment(1, finalReviewer.getId(), "Rejected", 10, "Final Review Comment"));

        Connection conn = getConnection();

        // insert records
        insertResources(conn, new Resource[] { finalReviewer });
        insertResourceInfo(conn, finalReviewer.getId(), 1, "100001");
        insertUploads(conn, new Upload[] { frUpload });
        insertSubmissions(conn, new Submission[] { frSubmission });
        insertResourceSubmission(conn, finalReviewer.getId(), frSubmission.getId());
        insertScorecards(conn, new Scorecard[] { scorecard1 });
        insertReviews(conn, new Review[] { frWorksheet });
        insertCommentsWithExtraInfo(conn, new long[] { 1 }, new long[] { finalReviewer.getId() },
            new long[] { frWorksheet.getId() }, new String[] { "Rejected COmment" }, new long[] { 10 },
            new String[] { "Rejected" });
        insertScorecardQuestion(conn, 1, 1);

        // no exception should be thrown.
        String operator = "1001";

        handler.perform(finalReviewPhase, operator);

        assertTrue("Final fix phase should be inserted.", haveNewFinalFixPhase(conn));
    }

    /**
     * Tests the perform with Open status.
     *
     * @throws Exception to JUnit.
     */
    public void testPerform2() throws Exception {
        Project project = super.setupPhases();
        Phase[] phases = project.getAllPhases();
        Phase finalReviewPhase = phases[9];
        finalReviewPhase.setPhaseStatus(PhaseStatus.OPEN);

        // populate db with required data
        // final reviewer resource
        Resource finalReviewer = createResource(101, finalReviewPhase.getId(), project.getId(), 9);
        Upload frUpload = createUpload(1, project.getId(), finalReviewer.getId(), 4, 1, "parameter");
        Submission frSubmission = createSubmission(1, frUpload.getId(), 1);

        // reviewer resource and related review
        Scorecard scorecard1 = createScorecard(1, 1, 2, 1, "name", "1.0", 75.0f, 100.0f);
        Review frWorksheet = createReview(11, finalReviewer.getId(), frSubmission.getId(), scorecard1.getId(), true,
                90.0f);
        // add a Approved comment
        frWorksheet.addComment(createComment(1, finalReviewer.getId(), "Approved", 10, "Final Review Comment"));

        Connection conn = getConnection();

        // insert records
        insertResources(conn, new Resource[] { finalReviewer });
        insertResourceInfo(conn, finalReviewer.getId(), 1, "100001");
        insertUploads(conn, new Upload[] { frUpload });
        insertSubmissions(conn, new Submission[] { frSubmission });
        insertResourceSubmission(conn, finalReviewer.getId(), frSubmission.getId());
        insertScorecards(conn, new Scorecard[] { scorecard1 });
        insertReviews(conn, new Review[] { frWorksheet });
        insertCommentsWithExtraInfo(conn, new long[] { 1 }, new long[] { finalReviewer.getId() },
            new long[] { frWorksheet.getId() }, new String[] { "Approved Comment" }, new long[] { 10 },
            new String[] { "Approved" });
        insertScorecardQuestion(conn, 1, 1);

        // no exception should be thrown.
        String operator = "1001";

        handler.canPerform(finalReviewPhase);
        handler.perform(finalReviewPhase, operator);

        assertTrue("Approval phase should be inserted.", haveApprovalPhase(conn));
    }
}
