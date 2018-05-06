/*
 * Copyright (C) 2009 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases.accuracytests;

import com.cronos.onlinereview.phases.ApprovalPhaseHandler;

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
 * Accuracy tests for change functions in version 1.1 of
 * ApprovalPhaseHandler class.
 *
 * @author myxgyy
 * @version 1.0
 */
public class ApprovalPhaseHandlerAccTestsV11 extends BaseTestCase {
    /** Target instance. */
    private ApprovalPhaseHandler handler;

    /**
     * sets up the environment required for test cases for this class.
     *
     * @throws Exception not under test.
     */
    protected void setUp() throws Exception {
        super.setUp();
        handler = new ApprovalPhaseHandler(PHASE_HANDLER_NAMESPACE);
    }

    /**
     * cleans up the environment required for test cases for this
     * class.
     *
     * @throws Exception not under test.
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Tests the canPerform with scheduled statuses.
     *
     * @throws Exception to JUnit.
     */
    public void testCanPerform1() throws Exception {
        Project project = super.setupPhases();
        Phase[] phases = project.getAllPhases();
        Phase approvalPhase = phases[10];

        // test with scheduled status.
        approvalPhase.setPhaseStatus(PhaseStatus.SCHEDULED);

        // time has not passed, nor dependencies met
        OperationCheckResult result = handler.canPerform(approvalPhase);

        assertFalse("Not the expected checking result", result.isSuccess());
        assertEquals("Wrong message",  "Dependency Final Review phase is not yet ended.",  result.getMessage());

        // time has passed, but dependency not met.
        approvalPhase.setActualStartDate(new Date());
         result = handler.canPerform(approvalPhase);

        assertFalse("Not the expected checking result", result.isSuccess());
        assertEquals("Wrong message",  "Dependency Final Review phase is not yet ended.",  result.getMessage());

        // set the number of required approver to 1
        approvalPhase.setAttribute("Reviewer Number", "1");

        // time has passed and dependency met, but have no approver set
        approvalPhase.getAllDependencies()[0].getDependency()
                                             .setPhaseStatus(PhaseStatus.CLOSED);
         result = handler.canPerform(approvalPhase);

        assertFalse("Not the expected checking result", result.isSuccess());
        assertEquals("Wrong message",  "There are not enough approvers assigned for the project",  result.getMessage());

        Resource approver = createResource(101, approvalPhase.getId(),
                project.getId(), 10);

        Connection conn = getConnection();

        insertResources(conn, new Resource[] {approver});
        insertResourceInfo(conn, approver.getId(), 1, "1001");

         result = handler.canPerform(approvalPhase);

        assertTrue("Not the expected checking result", result.isSuccess());
        assertEquals("Wrong message",  null,  result.getMessage());
    }

    /**
     * Tests the canPerform with open statuses.
     *
     * @throws Exception not under test.
     */
    public void testCanPerform2() throws Exception {
        Project project = super.setupPhases();
        Phase[] phases = project.getAllPhases();
        Phase approvalPhase = phases[10];

        // change dependency type to F2F
        approvalPhase.getAllDependencies()[0].setDependentStart(false);

        // test with open status.
        approvalPhase.setPhaseStatus(PhaseStatus.OPEN);

        // time has not passed, dependencies not met
        OperationCheckResult result = handler.canPerform(approvalPhase);

        assertFalse("Not the expected checking result", result.isSuccess());
        assertEquals("Wrong message",  "Dependency Final Review phase is not yet ended.",  result.getMessage());
    }

    /**
     * Tests the perform with scheduled statuses.
     *
     * @throws Exception not under test.
     */
    public void testPerform1() throws Exception {
        // test with scheduled status.
        Phase approvalPhase = createPhase(1, 1, "Scheduled", 2, "Approval");
        String operator = "1000001";
        Connection conn = getConnection();
        handler.perform(approvalPhase, operator);

        assertFalse("A new final fix phase should not be inserted.",
            haveNewFinalFixPhase(conn));
        assertFalse("A new final review phase should not be inserted.",
            haveNewFinalReviewPhase(conn));
    }

    /**
     * Test the method perform with an approved approval review, the
     * final review / final fix phases should NOT be inserted.
     *
     * @throws Exception to JUnit
     */
    public void testPerform2() throws Exception {
        Project project = super.setupPhases();
        Phase[] phases = project.getAllPhases();
        Phase approvalPhase = phases[10];
        approvalPhase.setPhaseStatus(PhaseStatus.OPEN);
        approvalPhase.setAttribute("Reviewer Number", "1");

        Connection conn = getConnection();

        // populate db with required data final reviewer resource
        Resource finalReviewer = createResource(102, 111, project.getId(), 9);

        insertResources(conn, new Resource[] {finalReviewer});
        insertResourceInfo(conn, finalReviewer.getId(), 1, "100002");

        // populate db with required data for approver resource
        Resource approver = createResource(101, approvalPhase.getId(),
                project.getId(), 10);
        Upload appUpload = createUpload(1, project.getId(), approver.getId(),
                4, 1, "parameter");
        Submission appSubmission = createSubmission(1, appUpload.getId(), 1);

        // reviewer resource and related review
        Scorecard scorecard1 = createScorecard(1, 1, 2, 1, "name", "1.0",
                75.0f, 100.0f);
        Review frWorksheet = createReview(11, approver.getId(),
                appSubmission.getId(), scorecard1.getId(), true, 90.0f);
        // add a approved comment
        frWorksheet.addComment(createComment(1, approver.getId(), "Approved",
                12, "Approval Review Comment"));

        // insert records
        insertResources(conn, new Resource[] {approver});
        insertResourceInfo(conn, approver.getId(), 1, "100001");
        insertUploads(conn, new Upload[] {appUpload});
        insertSubmissions(conn, new Submission[] {appSubmission});
        insertResourceSubmission(conn, approver.getId(), appSubmission.getId());
        insertScorecards(conn, new Scorecard[] {scorecard1});
        insertReviews(conn, new Review[] {frWorksheet});
        insertCommentsWithExtraInfo(conn, new long[] {1},
            new long[] {approver.getId()}, new long[] {frWorksheet.getId()},
            new String[] {"Approved Comment"}, new long[] {12},
            new String[] {"Approved"});
        insertScorecardQuestion(conn, 1, 1);

        // no exception should be thrown.
        String operator = "1001";

        handler.canPerform(approvalPhase);
        handler.perform(approvalPhase, operator);

        assertFalse("A new final fix phase should NOT be inserted.",
            haveNewFinalFixPhase(conn));
        assertFalse("A new final review phase should NOT be inserted.",
            haveNewFinalReviewPhase(conn));
    }
}
