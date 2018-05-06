/*
 * Copyright (C) 2006-2011 TopCoder Inc., All Rights Reserved.
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
 * All tests for SubmissionPhaseHandler class.
 * <p>
 * version 1.1 change notes: Two test methods <code>testPerformWithoutSubmission</code> and
 * <code>testPerformWithSubmissions</code> are added to test post-mortem insertion
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
public class SubmissionPhaseHandlerTest extends BaseTest {
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
        SubmissionPhaseHandler handler = new SubmissionPhaseHandler(PHASE_HANDLER_NAMESPACE);

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
        SubmissionPhaseHandler handler = new SubmissionPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            Phase phase = createPhase(1, 1, "Invalid", 2, "Submission");
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
        SubmissionPhaseHandler handler = new SubmissionPhaseHandler(PHASE_HANDLER_NAMESPACE);

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
        SubmissionPhaseHandler handler = new SubmissionPhaseHandler(PHASE_HANDLER_NAMESPACE);

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
        SubmissionPhaseHandler handler = new SubmissionPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            Phase phase = createPhase(1, 1, "Invalid", 1, "Submission");
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
        SubmissionPhaseHandler handler = new SubmissionPhaseHandler(PHASE_HANDLER_NAMESPACE);

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
        SubmissionPhaseHandler handler = new SubmissionPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            Phase phase = createPhase(1, 1, "Scheduled", 1, "Submission");
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
        SubmissionPhaseHandler handler = new SubmissionPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            Phase phase = createPhase(1, 1, "Scheduled", 1, "Submission");
            handler.perform(phase, "   ");
            fail("perform() did not throw IllegalArgumentException for empty operator.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Tests the SubmissionPhaseHandler() constructor and canPerform with Scheduled statuses.
     * @throws Exception not under test.
     */
    public void testCanPerformWithScheduled() throws Exception {
        SubmissionPhaseHandler handler = new SubmissionPhaseHandler();

        try {
            cleanTables();

            Project project = super.setupPhases();
            Phase[] phases = project.getAllPhases();
            Phase submission = phases[1];

            // test with scheduled status.
            submission.setPhaseStatus(PhaseStatus.SCHEDULED);

            // time has not passed, nor dependencies met
            assertFalse("canPerform should have returned false", handler.canPerform(submission).isSuccess());

            // time has passed, but dependency not met.
            submission.setActualStartDate(new Date(new Date().getTime() - 28 * 60 * 60 * 1000));
            assertFalse("canPerform should have returned false", handler.canPerform(submission).isSuccess());

            // time has passed and dependency met.
            submission.getAllDependencies()[0].getDependency().setPhaseStatus(PhaseStatus.CLOSED);
            assertTrue("canPerform should have returned true", handler.canPerform(submission).isSuccess());
        } finally {
            cleanTables();
        }
    }

    /**
     * Tests the SubmissionPhaseHandler() constructor and canPerform with Scheduled statuses.
     * @throws Exception not under test.
     */
    public void testCanPerformHandlerWithOpen() throws Exception {
        SubmissionPhaseHandler handler = new SubmissionPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            cleanTables();

            Project project = super.setupPhases();
            Phase[] phases = project.getAllPhases();
            Phase submissionPhase = phases[1];

            // test with open status.
            submissionPhase.setPhaseStatus(PhaseStatus.OPEN);

            // change dependency type to F2F
            submissionPhase.getAllDependencies()[0].setDependentStart(false);

            // time has not passed, nor dependencies met
            assertFalse("canPerform should have returned false", handler.canPerform(submissionPhase).isSuccess());

            // time has passed, but dependency not met.
            submissionPhase.setActualStartDate(new Date(System.currentTimeMillis() - 1000));
            submissionPhase.setActualEndDate(new Date());
            assertFalse("canPerform should have returned false", handler.canPerform(submissionPhase).isSuccess());

            // time has passed and dependency met, reviews passed.
            submissionPhase.getAllDependencies()[0].getDependency().setPhaseStatus(PhaseStatus.CLOSED);
            submissionPhase.setAttribute("Manual Screening", "Yes");
            submissionPhase.setAttribute("Submission Number", "1");

            Connection conn = getConnection();

            Phase screeningPhase = phases[2];
            long screeningPhaseId = screeningPhase.getId();
            Resource submitter = super.createResource(1, submissionPhase.getId(), 1, 1);
            Resource reviewer1 = super.createResource(2, screeningPhaseId, 1, 2);
            Resource reviewer2 = super.createResource(3, screeningPhaseId, 1, 3);

            Upload upload = createUpload(1, 1, submitter.getId(), 1, 1, "parameter");
            Submission submission = createSubmission(1, 1, 1, 1);
            Scorecard scorecard = createScorecard(1, 1, 1, 1, "name", "1.0", 75.0f, 100.0f);
            Review review = createReview(1, reviewer1.getId(), submission.getId(), scorecard.getId(), true, 80.0f);

            insertResources(conn, new Resource[] {submitter, reviewer1, reviewer2 });
            insertResourceInfo(conn, submitter.getId(), 1, "11111");
            insertResourceInfo(conn, reviewer1.getId(), 1, "11112");
            insertResourceInfo(conn, reviewer2.getId(), 1, "11113");
            insertUploads(conn, new Upload[] {upload });
            insertSubmissions(conn, new Submission[] {submission });
            insertScorecards(conn, new Scorecard[] {scorecard });
            insertReviews(conn, new Review[] {review });
            assertTrue("canPerform should have returned true", handler.canPerform(submissionPhase).isSuccess());
        } finally {
            closeConnection();
            cleanTables();
        }
    }

    /**
     * Tests the perform with Scheduled and Open statuses and checks whether a post-mortem phase is inserted when
     * there
     * is no submission.
     * @throws Exception to JUnit.
     * @since 1.1
     */
    public void testPerformWithoutSubmission() throws Exception {
        SubmissionPhaseHandler handler = new SubmissionPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            cleanTables();

            // test with scheduled status.
            Phase submissionPhase = createPhase(1, 1, "Scheduled", 2, "Submission");
            String operator = "1001";
            Connection conn = getConnection();
            insertProject(conn);
            insertProjectInfo(getConnection(), 1, new long[] {44 }, new String[] {"true" });

            // test with open status
            submissionPhase.setPhaseStatus(PhaseStatus.OPEN);
            handler.perform(submissionPhase, operator);

            // check whether the PostMortemPhaseHandler
            assertTrue("Post-mortem phase should be inserted", havePostMortemPhase(conn));
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * Tests the perform to start the phase.
     * @throws Exception to JUnit.
     * @since 1.2
     */
    public void testPerform_start() throws Exception {
        SubmissionPhaseHandler handler = new SubmissionPhaseHandler();

        try {
            cleanTables();

            Project project = setupProjectResourcesNotification("Submission");

            // test with scheduled status.
            Phase submissionPhase = project.getAllPhases()[1];
            String operator = "1001";

            handler.perform(submissionPhase, operator);

            // manually check the email
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * Tests the perform to stop the phase without phases.
     * @throws Exception to JUnit.
     * @since 1.2
     */
    public void testPerform_no_submission_stop() throws Exception {
        SubmissionPhaseHandler handler = new SubmissionPhaseHandler();

        try {
            cleanTables();

            Project project = setupProjectResourcesNotification("Submission");

            // test with scheduled status.
            Phase submissionPhase = project.getAllPhases()[1];
            submissionPhase.setPhaseStatus(PhaseStatus.OPEN);
            String operator = "1001";

            handler.perform(submissionPhase, operator);

            // manually check the email
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * Tests the perform with Open statuses and a post-mortem phase should NOT be inserted when there are
     * submissions.
     * @throws Exception to JUnit.
     * @since 1.1
     */
    public void testPerformWithSubmissions() throws Exception {
        SubmissionPhaseHandler handler = new SubmissionPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            cleanTables();

            // test with scheduled status.
            Project project = super.setupPhases();
            Phase[] phases = project.getAllPhases();
            Phase submissionPhase = phases[1];

            // test with scheduled status.
            String operator = "operator";
            Connection conn = getConnection();

            // test with open status
            submissionPhase.setPhaseStatus(PhaseStatus.OPEN);

            // create a submission
            Resource resource = super.createResource(1, 102L, 1, 1);
            super.insertResources(conn, new Resource[] {resource });

            Upload upload = super.createUpload(1, project.getId(), resource.getId(), 1, 1, "Paramter");
            super.insertUploads(conn, new Upload[] {upload });

            Submission submission = super.createSubmission(1, upload.getId(), 1, 1);
            super.insertSubmissions(conn, new Submission[] {submission });

            handler.perform(submissionPhase, operator);

            // Post-Mortem phase should NOT be inserted
            // when there is submission
            assertFalse("Post-mortem phase should NOT be inserted", havePostMortemPhase(conn));
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * Tests the perform to stop the phase with submissions.
     * @throws Exception to JUnit.
     * @since 1.2
     */
    public void testPerform_with_submission_stop() throws Exception {
        SubmissionPhaseHandler handler = new SubmissionPhaseHandler();

        try {
            cleanTables();

            Project project = setupProjectResourcesNotification("Submission");

            // test with scheduled status.
            Phase submissionPhase = project.getAllPhases()[1];
            submissionPhase.setPhaseStatus(PhaseStatus.OPEN);
            String operator = "1001";

            Connection conn = getConnection();
            // create a registration
            Resource resource = createResource(4, 101L, 1, 1);
            super.insertResources(conn, new Resource[] {resource });
            insertResourceInfo(conn, resource.getId(), 1, "4");
            insertResourceInfo(conn, resource.getId(), 2, "ACRush");
            insertResourceInfo(conn, resource.getId(), 4, "3808");
            insertResourceInfo(conn, resource.getId(), 5, "100");

            Upload upload = super.createUpload(1, project.getId(), resource.getId(), 1, 1, "Paramter");
            super.insertUploads(conn, new Upload[] {upload });

            Submission submission = super.createSubmission(1, upload.getId(), 1, 1);
            super.insertSubmissions(conn, new Submission[] {submission });

            // another register
            resource = createResource(5, 101L, 1, 1);
            super.insertResources(conn, new Resource[] {resource });
            insertResourceInfo(conn, resource.getId(), 1, "5");
            insertResourceInfo(conn, resource.getId(), 2, "UdH-WiNGeR");
            insertResourceInfo(conn, resource.getId(), 4, "3338");
            insertResourceInfo(conn, resource.getId(), 5, "90");
            upload = super.createUpload(2, project.getId(), resource.getId(), 1, 1, "Paramter");
            super.insertUploads(conn, new Upload[] {upload });

            submission = super.createSubmission(2, upload.getId(), 1, 1);
            super.insertSubmissions(conn, new Submission[] {submission });

            handler.perform(submissionPhase, operator);

            // manually check the email
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * Tests the SubmissionPhaseHandler() constructor and canPerform with Scheduled statuses.
     * @throws Exception not under test.
     */
    public void testCanPerformHandlerWithOpen1() throws Exception {
        SubmissionPhaseHandler handler = new SubmissionPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            cleanTables();

            Project project = super.setupPhases();
            Phase[] phases = project.getAllPhases();
            Phase submissionPhase = phases[1];

            // test with open status.
            submissionPhase.setPhaseStatus(PhaseStatus.OPEN);

            submissionPhase.setAttribute("Manual Screening", "Yes");

            Connection conn = getConnection();

            Phase screeningPhase = phases[2];
            long screeningPhaseId = screeningPhase.getId();
            Resource submitter = super.createResource(1, submissionPhase.getId(), 1, 1);
            Resource reviewer1 = super.createResource(2, screeningPhaseId, 1, 2);
            Resource reviewer2 = super.createResource(3, screeningPhaseId, 1, 3);

            Upload upload = createUpload(1, 1, submitter.getId(), 1, 1, "parameter");
            Submission submission = createSubmission(1, 1, 1, 1);
            Scorecard scorecard = createScorecard(1, 1, 1, 1, "name", "1.0", 75.0f, 100.0f);
            Review review = createReview(1, reviewer1.getId(), submission.getId(), scorecard.getId(), true, 80.0f);

            insertResources(conn, new Resource[] {submitter, reviewer1, reviewer2 });
            insertResourceInfo(conn, submitter.getId(), 1, "11111");
            insertResourceInfo(conn, reviewer1.getId(), 1, "11112");
            insertResourceInfo(conn, reviewer2.getId(), 1, "11113");
            insertUploads(conn, new Upload[] {upload });
            insertSubmissions(conn, new Submission[] {submission });
            insertScorecards(conn, new Scorecard[] {scorecard });
            insertReviews(conn, new Review[] {review });
            assertTrue("canPerform should have returned true", handler.canPerform(submissionPhase).isSuccess());
        } finally {
            closeConnection();
            cleanTables();
        }
    }

    /**
     * Tests the SubmissionPhaseHandler() constructor and canPerform with auto screening.
     * @throws Exception
     *             not under test.
     */
    public void testCanPerformHandlerWithOpen2() throws Exception {
        SubmissionPhaseHandler handler = new SubmissionPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            cleanTables();

            Project project = super.setupPhases();
            Phase[] phases = project.getAllPhases();
            Phase submissionPhase = phases[1];

            // test with open status.
            submissionPhase.setPhaseStatus(PhaseStatus.OPEN);
            submissionPhase.setAttribute("Submission Number", "1");

            Connection conn = getConnection();

            Phase screeningPhase = phases[2];
            long screeningPhaseId = screeningPhase.getId();
            Resource submitter = super.createResource(1, submissionPhase.getId(), 1, 1);
            Resource reviewer1 = super.createResource(2, screeningPhaseId, 1, 2);
            Resource reviewer2 = super.createResource(3, screeningPhaseId, 1, 3);

            Upload upload = createUpload(1, 1, submitter.getId(), 1, 1, "parameter");
            Submission submission = createSubmission(1, 1, 1, 1);
            Scorecard scorecard = createScorecard(1, 1, 1, 1, "name", "1.0", 75.0f, 100.0f);
            Review review = createReview(1, reviewer1.getId(), submission.getId(), scorecard.getId(), true, 80.0f);

            insertResources(conn, new Resource[] {submitter, reviewer1, reviewer2 });
            insertResourceInfo(conn, submitter.getId(), 1, "11111");
            insertResourceInfo(conn, reviewer1.getId(), 1, "11112");
            insertResourceInfo(conn, reviewer2.getId(), 1, "11113");
            insertUploads(conn, new Upload[] {upload });
            insertSubmissions(conn, new Submission[] {submission });
            insertScorecards(conn, new Scorecard[] {scorecard });
            insertReviews(conn, new Review[] {review });
            insertScreeningTask(conn, upload.getId());
            assertTrue("canPerform should have returned true", handler.canPerform(submissionPhase).isSuccess());
        } finally {
            closeConnection();
            cleanTables();
        }
    }
}
