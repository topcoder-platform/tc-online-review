/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases.stresstests;

import java.sql.Connection;
import java.util.Date;

import com.cronos.onlinereview.phases.ApprovalPhaseHandler;
import com.topcoder.management.deliverable.Submission;
import com.topcoder.management.deliverable.Upload;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.review.data.Review;
import com.topcoder.management.scorecard.data.Scorecard;
import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.PhaseStatus;
import com.topcoder.project.phases.Project;
import com.topcoder.util.config.ConfigManager;

/**
 * <p>
 * Stress tests for <code>ApprovalPhaseHandler</code>.
 * </p>
 * <p>
 * Since this handler is immutable, so it's naturally thread safe. Here just do benchmark tests.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.3
 */
public class ApprovalPhaseHandlerTest extends StressBaseTest {

    /**
     * Set up the environment.
     *
     * @throws Exception
     *             to JUnit
     */
    protected void setUp() throws Exception {
        super.setUp();
        ConfigManager configManager = ConfigManager.getInstance();

        configManager.add(PHASE_HANDLER_CONFIG_FILE);

        configManager.add(DOC_GENERATOR_CONFIG_FILE);
        configManager.add(EMAIL_CONFIG_FILE);
        configManager.add(MANAGER_HELPER_CONFIG_FILE);

        // add the component configurations as well
        for (int i = 0; i < COMPONENT_FILE_NAMES.length; i++) {
            configManager.add(COMPONENT_FILE_NAMES[i]);
        }
    }

    /**
     * Clean up the environment.
     *
     * @throws Exception
     *             to JUnit
     */
    protected void tearDown() throws Exception {

        super.tearDown();
    }

    /**
     * Tests the ApprovalPhaseHandler() constructor and canPerform with Open statuses.
     *
     * @throws Exception
     *             not under test.
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

            startRecord();
            for (int i = 0; i < FIRST_LEVEL * 10; i++) {
                handler.canPerform(approvalPhase);
            }
            endRecord("ApprovalPhaseHandler::canPerform(Phase)--(the phase status is false)",
                FIRST_LEVEL * 10);
        } finally {
            cleanTables();
        }
    }

    /**
     * Tests the ApprovalPhaseHandler() constructor and canPerform with Open statuses.
     *
     * @throws Exception
     *             not under test.
     */
    public void testCanPerformHandlerWithClosed() throws Exception {
        ApprovalPhaseHandler handler = new ApprovalPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            cleanTables();

            Project project = super.setupPhases();
            Phase[] phases = project.getAllPhases();
            Phase appealsPhase = phases[10];

            // test with scheduled status.
            appealsPhase.setPhaseStatus(PhaseStatus.SCHEDULED);

            // time has passed, but dependency not met.
            appealsPhase.setActualStartDate(new Date());

            // time has passed and dependency met.
            appealsPhase.getAllDependencies()[0].getDependency().setPhaseStatus(PhaseStatus.CLOSED);

            startRecord();
            for (int i = 0; i < FIRST_LEVEL; i++) {
                handler.canPerform(appealsPhase);
            }
            endRecord("ApprovalPhaseHandler::canPerform(Phase)--(the phase status is true)", FIRST_LEVEL);
        } finally {
            cleanTables();
        }
    }

    /**
     * Tests the perform with Scheduled statuses.
     *
     * @throws Exception
     *             not under test.
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
            insertResources(conn, new Resource[]{approval });
            insertResourceInfo(conn, approval.getId(), 1, "2");
            approvalPhase.setAttribute("Reviewer Number", "1");

            String operator = "operator";

            startRecord();
            for (int i = 0; i < FIRST_LEVEL; i++) {
                handler.perform(approvalPhase, operator);
            }
            endRecord("ApprovalPhaseHandler::perform(Phase, String)--(the phase status is true)", FIRST_LEVEL);
            // manually check the email
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * Test the method perform with an approved approval review, the final review / final fix phases
     * should NOT be inserted.
     *
     * @throws Exception
     *             the exception occurs
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

            insertResources(conn, new Resource[]{finalReviewer });
            insertResourceInfo(conn, finalReviewer.getId(), 1, "100002");

            // populate db with required data for approver resource
            Resource approver = createResource(101, approvalPhase.getId(), project.getId(), 10);
            Upload appUpload = createUpload(1, project.getId(), approver.getId(), 4, 1, "parameter");
            Submission appSubmission = createSubmission(1, appUpload.getId(), 1);

            // reviewer resource and related review
            Scorecard scorecard1 = createScorecard(1, 1, 2, 1, "name", "1.0", 75.0f, 100.0f);
            Review frWorksheet = createReview(11, approver.getId(), appSubmission.getId(),
                scorecard1.getId(), true, 90.0f);

            // add a approved comment
            frWorksheet.addComment(createComment(1, approver.getId(), "Approved", 12,
                "Approval Review Comment"));

            // insert records
            insertResources(conn, new Resource[]{approver });
            insertResourceInfo(conn, approver.getId(), 1, "100001");
            insertUploads(conn, new Upload[]{appUpload });
            insertSubmissions(conn, new Submission[]{appSubmission });
            insertResourceSubmission(conn, approver.getId(), appSubmission.getId());
            insertScorecards(conn, new Scorecard[]{scorecard1 });
            insertReviews(conn, new Review[]{frWorksheet });
            insertCommentsWithExtraInfo(conn, new long[]{1 }, new long[]{approver.getId() },
                new long[]{frWorksheet.getId() }, new String[]{"Approved Comment" }, new long[]{12 },
                new String[]{"Approved" });
            insertCommentsWithExtraInfo(conn, new long[]{2 }, new long[]{approver.getId() },
                new long[]{frWorksheet.getId() }, new String[]{"Approved Comment" }, new long[]{13 },
                new String[]{"Required" });
            insertScorecardQuestion(conn, 1, 1);

            // no exception should be thrown.
            String operator = "1001";

            // set the number of required approval reviewer to 1
            approvalPhase.setAttribute("Reviewer Number", "1");

            startRecord();
            for (int i = 0; i < FIRST_LEVEL; i++) {
                handler.perform(approvalPhase, operator);
            }
            endRecord("ApprovalPhaseHandler::perform(Phase, String)--"
                + "(the phase status is false, the other fixes required is true.)", FIRST_LEVEL);
        } finally {
            closeConnection();
            cleanTables();
        }
    }

    /**
     * Test the method perform with a rejected approval review, the new final review / final fix
     * phases should be
     * inserted.
     *
     * @throws Exception
     *             the exception occurs
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
            Resource finalReviewer = createResource(102, 111L, project.getId(), 9);

            insertResources(conn, new Resource[]{finalReviewer });
            insertResourceInfo(conn, finalReviewer.getId(), 1, "100002");

            // populate db with required data for approver resource
            Resource approver = createResource(101, approvalPhase.getId(), project.getId(), 10);
            Upload appUpload = createUpload(1, project.getId(), approver.getId(), 4, 1, "parameter");
            Submission appSubmission = createSubmission(1, appUpload.getId(), 1);

            // reviewer resource and related review
            Scorecard scorecard1 = createScorecard(1, 1, 2, 1, "name", "1.0", 75.0f, 100.0f);
            Review frWorksheet = createReview(11, approver.getId(), appSubmission.getId(),
                scorecard1.getId(), true, 90.0f);

            // add a approved comment
            frWorksheet.addComment(createComment(1, approver.getId(), "Approved", 12,
                "Approval Review Comment"));

            // insert records
            insertResources(conn, new Resource[]{approver });
            insertResourceInfo(conn, approver.getId(), 1, "100001");
            insertUploads(conn, new Upload[]{appUpload });
            insertSubmissions(conn, new Submission[]{appSubmission });
            insertResourceSubmission(conn, approver.getId(), appSubmission.getId());
            insertScorecards(conn, new Scorecard[]{scorecard1 });
            insertReviews(conn, new Review[]{frWorksheet });
            insertCommentsWithExtraInfo(conn, new long[]{1 }, new long[]{approver.getId() },
                new long[]{frWorksheet.getId() }, new String[]{"Approved Comment" }, new long[]{12 },
                new String[]{"Approved" });
            insertCommentsWithExtraInfo(conn, new long[]{2 }, new long[]{approver.getId() },
                new long[]{frWorksheet.getId() }, new String[]{"Approved Comment" }, new long[]{13 },
                new String[]{"Required" });
            insertScorecardQuestion(conn, 1, 1);

            // no exception should be thrown.
            String operator = "1001";

            // set the number of required approval reviewer to 1
            approvalPhase.setAttribute("Reviewer Number", "1");

            startRecord();
            for (int i = 0; i < FIRST_LEVEL; i++) {
                handler.perform(approvalPhase, operator);
            }
            endRecord("ApprovalPhaseHandler::perform(Phase, String)--"
                + "(the phase status is false, the other fixes required is true.)", FIRST_LEVEL);
        } finally {
            closeConnection();
            cleanTables();
        }
    }
}
