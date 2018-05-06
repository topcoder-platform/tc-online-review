/*
 * Copyright (C) 2009-2011 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import com.topcoder.management.deliverable.Submission;
import com.topcoder.management.deliverable.Upload;
import com.topcoder.management.phase.PhaseHandlingException;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.review.data.Review;
import com.topcoder.management.scorecard.data.Scorecard;

import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.PhaseStatus;
import com.topcoder.project.phases.Project;

import com.topcoder.util.config.ConfigManager;

import java.sql.Connection;

import java.util.Date;

/**
 * All tests for ApprovalPhaseHandler class.
 * <p>
 * Version 1.1 changes notes: test cases have been added to test the rejected / approval cases of approval phase.
 * When approval review is rejected, a new final fix / final review should be inserted.
 * </p>
 * <p>
 * Version 1.2 change notes : since the email-templates and role-supported has been enhanced. The test cases will
 * try to do on that way while for email content, please check it manually.
 * </p>
 * <p>
 * Version 1.6.1 changes note:
 * <ul>
 * <li>Change some test because the return of canPerform change from boolean to OperationCheckResult.</li>
 * </ul>
 * </p>
 * @author bose_java, waits, microsky
 * @version 1.6.1
 */
public class ApprovalPhaseHandlerTest extends BaseTest {
    /**
     * sets up the environment required for test cases for this class.
     * @throws Exception not under test.
     */
    protected void setUp() throws Exception {
        super.setUp();

        ConfigManager configManager = ConfigManager.getInstance();

        configManager.add(PHASE_HANDLER_CONFIG_FILE);

        configManager.add(EMAIL_CONFIG_FILE);
        configManager.add(MANAGER_HELPER_CONFIG_FILE);

        // add the component configurations as well
        for (int i = 0; i < COMPONENT_FILE_NAMES.length; i++) {
            configManager.add(COMPONENT_FILE_NAMES[i]);
        }
    }

    /**
     * cleans up the environment required for test cases for this class.
     * @throws Exception not under test.
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Tests canPerform(Phase) with null phase.
     * @throws Exception not under test.
     */
    public void testCanPerform() throws Exception {
        ApprovalPhaseHandler handler = new ApprovalPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            handler.canPerform(null);
            fail("canPerform() did not throw IllegalArgumentException for null argument.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Tests canPerform(Phase) with invalid phase status.
     * @throws Exception not under test.
     */
    public void testCanPerformWithInvalidStatus() throws Exception {
        ApprovalPhaseHandler handler = new ApprovalPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            Phase phase = createPhase(1, 1, "Invalid", 3, "Approval");
            handler.canPerform(phase);
            fail("canPerform() did not throw PhaseHandlingException for invalid phase status.");
        } catch (PhaseHandlingException e) {
            // expected.
        }
    }

    /**
     * Tests canPerform(Phase) with invalid phase type.
     * @throws Exception not under test.
     */
    public void testCanPerformWithInvalidType() throws Exception {
        ApprovalPhaseHandler handler = new ApprovalPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            Phase phase = createPhase(1, 1, "Scheduled", 1, "INVALID");
            handler.canPerform(phase);
            fail("canPerform() did not throw PhaseHandlingException for invalid phase type.");
        } catch (PhaseHandlingException e) {
            // expected.
        }
    }

    /**
     * Tests perform(Phase) with null phase.
     * @throws Exception not under test.
     */
    public void testPerformWithNullPhase() throws Exception {
        ApprovalPhaseHandler handler = new ApprovalPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            handler.perform(null, "operator");
            fail("perform() did not throw IllegalArgumentException for null argument.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Tests perform(Phase) with invalid phase status.
     * @throws Exception not under test.
     */
    public void testPerformWithInvalidStatus() throws Exception {
        ApprovalPhaseHandler handler = new ApprovalPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            Phase phase = createPhase(1, 1, "Invalid", 1, "Approval");
            handler.perform(phase, "operator");
            fail("perform() did not throw PhaseHandlingException for invalid phase status.");
        } catch (PhaseHandlingException e) {
            // expected.
        }
    }

    /**
     * Tests perform(Phase) with invalid phase type.
     * @throws Exception not under test.
     */
    public void testPerformWithInvalidType() throws Exception {
        ApprovalPhaseHandler handler = new ApprovalPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            Phase phase = createPhase(1, 1, "Scheduled", 1, "INVALID");
            handler.perform(phase, "operator");
            fail("perform() did not throw PhaseHandlingException for invalid phase type.");
        } catch (PhaseHandlingException e) {
            // expected.
        }
    }

    /**
     * Tests perform(Phase) with null operator.
     * @throws Exception not under test.
     */
    public void testPerformWithNullOperator() throws Exception {
        ApprovalPhaseHandler handler = new ApprovalPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            Phase phase = createPhase(1, 1, "Scheduled", 1, "Approval");
            handler.perform(phase, null);
            fail("perform() did not throw IllegalArgumentException for null operator.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Tests perform(Phase) with empty operator.
     * @throws Exception not under test.
     */
    public void testPerformWithEmptyOperator() throws Exception {
        ApprovalPhaseHandler handler = new ApprovalPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            Phase phase = createPhase(1, 1, "Scheduled", 1, "Approval");
            handler.perform(phase, "   ");
            fail("perform() did not throw IllegalArgumentException for empty operator.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Tests the ApprovalPhaseHandler() constructor and canPerform with Scheduled statuses.
     * <p>
     * Version 1.1 changes note: Test updated according to new logic and requirement.
     * </p>
     * @throws Exception to JUnit.
     */
    public void testCanPerformWithScheduled() throws Exception {
        ApprovalPhaseHandler handler = new ApprovalPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            cleanTables();

            Project project = super.setupPhases();
            Phase[] phases = project.getAllPhases();
            Phase approvalPhase = phases[10];

            // test with scheduled status.
            approvalPhase.setPhaseStatus(PhaseStatus.SCHEDULED);

            // time has not passed, nor dependencies met
            assertFalse("canPerform should have returned false", handler.canPerform(approvalPhase).isSuccess());

            // time has passed, but dependency not met.
            approvalPhase.setActualStartDate(new Date());
            assertFalse("canPerform should have returned false", handler.canPerform(approvalPhase).isSuccess());

            // set the number of required approver to 1
            approvalPhase.setAttribute("Reviewer Number", "1");

            // time has passed and dependency met, but have no approver set
            approvalPhase.getAllDependencies()[0].getDependency().setPhaseStatus(PhaseStatus.CLOSED);
            assertFalse("canPerform should have returned false when there is no approver.",
                handler.canPerform(approvalPhase).isSuccess());

            // Let's add an approver here
            Resource approver = createResource(101, approvalPhase.getId(), project.getId(), 10);

            Connection conn = getConnection();

            // insert records
            insertResources(conn, new Resource[] {approver });

            // we need to insert an external reference id
            // which references to resource's user id in resource_info table
            insertResourceInfo(conn, approver.getId(), 1, "1001");
            assertTrue("canPerform should have returned true when there is an approver.",
                handler.canPerform(approvalPhase).isSuccess());
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * Tests the ApprovalPhaseHandler() constructor and canPerform with Open statuses.
     * @throws Exception not under test.
     */
    public void testCanPerformHandlerWithOpen() throws Exception {
        ApprovalPhaseHandler handler = new ApprovalPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            cleanTables();

            Project project = super.setupPhases();
            Phase[] phases = project.getAllPhases();
            Phase approvalPhase = phases[10];

            // change dependency type to F2F
            approvalPhase.getAllDependencies()[0].setDependentStart(false);

            // test with open status.
            approvalPhase.setPhaseStatus(PhaseStatus.OPEN);

            // time has not passed, dependencies not met
            assertFalse("canPerform should have returned false", handler.canPerform(approvalPhase).isSuccess());
        } finally {
            cleanTables();
        }
    }

    /**
     * Tests the perform with Scheduled statuses.
     * @throws Exception not under test.
     * @since 1.2
     */
    public void testPerform_start_noApprovals() throws Exception {
        ApprovalPhaseHandler handler = new ApprovalPhaseHandler(ApprovalPhaseHandler.DEFAULT_NAMESPACE);

        try {
            cleanTables();

            Project project = super.setupProjectResourcesNotification("Approval");
            Phase[] phases = project.getAllPhases();
            Phase approvalPhase = phases[10];

            String operator = "operator";
            handler.perform(approvalPhase, operator);

            // manually check the email
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * Tests the perform with Scheduled statuses.
     * @throws Exception not under test.
     * @since 1.2
     */
    public void testPerform_start_withApprovals() throws Exception {
        ApprovalPhaseHandler handler = new ApprovalPhaseHandler(ApprovalPhaseHandler.DEFAULT_NAMESPACE);

        try {
            cleanTables();

            Project project = super.setupProjectResourcesNotification("Approval");
            Phase[] phases = project.getAllPhases();
            Phase approvalPhase = phases[10];

            Resource approval = createResource(100233, approvalPhase.getId(), project.getId(), 10);
            Connection conn = getConnection();
            insertResources(conn, new Resource[] {approval });
            insertResourceInfo(conn, approval.getId(), 1, "2");
            approvalPhase.setAttribute("Reviewer Number", "1");

            String operator = "operator";
            handler.perform(approvalPhase, operator);

            // manually check the email
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * Test the method perform with a rejected approval review, the new final review / final fix phases should be
     * inserted.
     * @throws Exception to JUnit
     * @since 1.1
     */
    public void testPerformWithOpen_rejected() throws Exception {
        ApprovalPhaseHandler handler = new ApprovalPhaseHandler(ApprovalPhaseHandler.DEFAULT_NAMESPACE);

        try {
            cleanTables();

            Project project = super.setupProjectResourcesNotification("Approval");
            Phase[] phases = project.getAllPhases();
            Phase approvalPhase = phases[10];
            approvalPhase.setPhaseStatus(PhaseStatus.OPEN);

            Connection conn = getConnection();

            // populate db with required data final reviewer resource
            Resource finalReviewer = createResource(102, 110L, project.getId(), 9);

            insertResources(conn, new Resource[] {finalReviewer });
            insertResourceInfo(conn, finalReviewer.getId(), 1, "2");

            // populate db with required data for approver resource
            Resource approver = createResource(101, approvalPhase.getId(), project.getId(), 10);
            Upload appUpload = createUpload(1, project.getId(), approver.getId(), 4, 1, "parameter");
            Submission appSubmission = createSubmission(1, appUpload.getId(), 1, 1);

            // reviewer resource and related review
            Scorecard scorecard1 = createScorecard(1001, 1, 2, 1, "name", "1.0", 75.0f, 100.0f);
            Review frWorksheet = createReview(1111, approver.getId(), appSubmission.getId(), scorecard1.getId(),
                true,
                    90.0f);

            // add a rejected comment
            frWorksheet.addComment(createComment(11, approver.getId(), "Rejected", 12, "Approval Review Comment"));

            // insert records
            insertResources(conn, new Resource[] {approver });
            insertResourceInfo(conn, approver.getId(), 1, "2");
            insertUploads(conn, new Upload[] {appUpload });
            insertSubmissions(conn, new Submission[] {appSubmission });
            insertResourceSubmission(conn, approver.getId(), appSubmission.getId());
            insertScorecards(conn, new Scorecard[] {scorecard1 });
            insertReviews(conn, new Review[] {frWorksheet });
            insertCommentsWithExtraInfo(conn, new long[] {1 }, new long[] {approver.getId() },
                new long[] {frWorksheet.getId() }, new String[] {"Rejected Comment" }, new long[] {12 },
                new String[] {"Rejected" });
            insertScorecardQuestion(conn, 1, scorecard1.getId());

            // no exception should be thrown.
            String operator = "1001";

            // set the number of required approval reviewer to 1
            approvalPhase.setAttribute("Reviewer Number", "1");

            handler.canPerform(approvalPhase);
            handler.perform(approvalPhase, operator);

            assertTrue("A new final fix phase should be inserted.", haveNewFinalFixPhase(conn));
            assertTrue("A new final review phase should be inserted.", haveNewFinalReviewPhase(conn));
        } finally {
            closeConnection();
            cleanTables();
        }
    }

    /**
     * Test the method perform with an approved approval review, the final review / final fix phases should NOT be
     * inserted.
     * @throws Exception to JUnit
     * @since 1.1
     */
    public void testPerformWithOpen_approved() throws Exception {
        ApprovalPhaseHandler handler = new ApprovalPhaseHandler();

        try {
            cleanTables();

            Project project = super.setupProjectResourcesNotification("Approval");
            Phase[] phases = project.getAllPhases();
            Phase approvalPhase = phases[10];
            approvalPhase.setPhaseStatus(PhaseStatus.OPEN);

            Connection conn = getConnection();

            // populate db with required data final reviewer resource
            Resource finalReviewer = createResource(102, 111L, project.getId(), 9);

            insertResources(conn, new Resource[] {finalReviewer });
            insertResourceInfo(conn, finalReviewer.getId(), 1, "100002");

            // populate db with required data for approver resource
            Resource approver = createResource(101, approvalPhase.getId(), project.getId(), 10);
            Upload appUpload = createUpload(1, project.getId(), approver.getId(), 4, 1, "parameter");
            Submission appSubmission = createSubmission(1, appUpload.getId(), 1, 1);

            // reviewer resource and related review
            Scorecard scorecard1 = createScorecard(1, 1, 2, 1, "name", "1.0", 75.0f, 100.0f);
            Review frWorksheet = createReview(11, approver.getId(), appSubmission.getId(), scorecard1.getId(),
                true,
                    90.0f);

            // add a rejected comment
            frWorksheet.addComment(createComment(1, approver.getId(), "Approved", 12, "Approval Review Comment"));

            // insert records
            insertResources(conn, new Resource[] {approver });
            insertResourceInfo(conn, approver.getId(), 1, "100001");
            insertUploads(conn, new Upload[] {appUpload });
            insertSubmissions(conn, new Submission[] {appSubmission });
            insertResourceSubmission(conn, approver.getId(), appSubmission.getId());
            insertScorecards(conn, new Scorecard[] {scorecard1 });
            insertReviews(conn, new Review[] {frWorksheet });
            insertCommentsWithExtraInfo(conn, new long[] {1 }, new long[] {approver.getId() },
                new long[] {frWorksheet.getId() }, new String[] {"Approved Comment" }, new long[] {12 },
                new String[] {"Approved" });
            insertScorecardQuestion(conn, 1, 1);

            // no exception should be thrown.
            String operator = "1001";

            // set the number of required approval reviewer to 1
            approvalPhase.setAttribute("Reviewer Number", "1");

            handler.canPerform(approvalPhase);
            handler.perform(approvalPhase, operator);

            assertFalse("A new final fix phase should NOT be inserted.", haveNewFinalFixPhase(conn));
            assertFalse("A new final review phase should NOT be inserted.", haveNewFinalReviewPhase(conn));
        } finally {
            closeConnection();
            cleanTables();
        }
    }

    /**
     * Test the method perform with an approved approval review, the final review / final fix phases should NOT be
     * inserted.
     * @throws Exception to JUnit
     * @since 1.3
     */
    public void testPerformWithOpen_approved_otherfixes() throws Exception {
        ApprovalPhaseHandler handler = new ApprovalPhaseHandler();

        try {
            cleanTables();

            Project project = super.setupProjectResourcesNotification("Approval");
            Phase[] phases = project.getAllPhases();
            Phase approvalPhase = phases[10];
            approvalPhase.setPhaseStatus(PhaseStatus.OPEN);

            Connection conn = getConnection();

            // populate db with required data final reviewer resource
            Resource finalReviewer = createResource(102, 111L, project.getId(), 9);

            insertResources(conn, new Resource[] {finalReviewer });
            insertResourceInfo(conn, finalReviewer.getId(), 1, "100002");

            // populate db with required data for approver resource
            Resource approver = createResource(101, approvalPhase.getId(), project.getId(), 10);
            Upload appUpload = createUpload(1, project.getId(), approver.getId(), 4, 1, "parameter");
            Submission appSubmission = createSubmission(1, appUpload.getId(), 1, 1);

            // reviewer resource and related review
            Scorecard scorecard1 = createScorecard(1, 1, 2, 1, "name", "1.0", 75.0f, 100.0f);
            Review frWorksheet = createReview(11, approver.getId(), appSubmission.getId(), scorecard1.getId(),
                true,
                    90.0f);

            // add a approved comment
            frWorksheet.addComment(createComment(1, approver.getId(), "Approved", 12, "Approval Review Comment"));

            // insert records
            insertResources(conn, new Resource[] {approver });
            insertResourceInfo(conn, approver.getId(), 1, "100001");
            insertUploads(conn, new Upload[] {appUpload });
            insertSubmissions(conn, new Submission[] {appSubmission });
            insertResourceSubmission(conn, approver.getId(), appSubmission.getId());
            insertScorecards(conn, new Scorecard[] {scorecard1 });
            insertReviews(conn, new Review[] {frWorksheet });
            insertCommentsWithExtraInfo(conn, new long[] {1 }, new long[] {approver.getId() },
                new long[] {frWorksheet.getId() }, new String[] {"Approved Comment" }, new long[] {12 },
                new String[] {"Approved" });
            insertCommentsWithExtraInfo(conn, new long[] {2 }, new long[] {approver.getId() },
                    new long[] {frWorksheet.getId() }, new String[] {"Approved Comment" }, new long[] {13 },
                    new String[] {"Required" });
            insertScorecardQuestion(conn, 1, 1);

            // no exception should be thrown.
            String operator = "1001";

            // set the number of required approval reviewer to 1
            approvalPhase.setAttribute("Reviewer Number", "1");

            handler.canPerform(approvalPhase);
            handler.perform(approvalPhase, operator);

            assertFalse("A new final fix phase should NOT be inserted.", haveNewFinalFixPhase(conn));
            assertFalse("A new final review phase should NOT be inserted.", haveNewFinalReviewPhase(conn));
        } finally {
            closeConnection();
            cleanTables();
        }
    }
}
