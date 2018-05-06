/*
 * Copyright (C) 2010-2011 TopCoder Inc., All Rights Reserved.
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
 * All tests for CheckpointSubmissionPhaseHandler class.
 * <p>
 * Version 1.6.1 changes note:
 * <ul>
 * <li>Change some test because the return of canPerform change from boolean to OperationCheckResult.</li>
 * </ul>
 * </p>
 * @author TCSDEVELOPER, microsky
 * @version 1.6.1
 * @since 1.6
 */
public class CheckpointSubmissionPhaseHandlerTest extends BaseTest {

    /**
     * <p>
     * Represents the CheckpointSubmissionPhaseHandler instance for testing.
     * </p>
     */
    private CheckpointSubmissionPhaseHandler handler;

    /**
     * sets up the environment required for test cases for this class.
     * @throws Exception
     *             not under test.
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
     * <p>
     * Tests the CheckpointSubmissionPhaseHandler() constructor, instance should be created.
     * </p>
     * @throws Exception
     *             pass any unexpected exception to JUnit.
     */
    public void testCtor() throws Exception {
        handler = new CheckpointSubmissionPhaseHandler();

        assertNotNull("instance should not be null", handler);
    }

    /**
     * <p>
     * Tests the CheckpointSubmissionPhaseHandler(String) constructor, instance should be created.
     * </p>
     * @throws Exception
     *             pass any unexpected exception to JUnit.
     */
    public void testCtor2() throws Exception {
        handler = new CheckpointSubmissionPhaseHandler(PHASE_HANDLER_NAMESPACE);

        assertNotNull("instance should not be null", handler);
    }

    /**
     * <p>
     * Tests canPerform(Phase) with null phase.
     * </p>
     * <p>
     * expect throw IllegalArgumentException.
     * </p>
     * @throws Exception
     *             not under test.
     */
    public void testCanPerform() throws Exception {
        handler = new CheckpointSubmissionPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            handler.canPerform(null);
            fail("canPerform() did not throw IllegalArgumentException for null argument.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * <p>
     * Tests canPerform(Phase) with invalid phase status.
     * </p>
     * <p>
     * expect throw PhaseHandlingException.
     * </p>
     * @throws Exception
     *             not under test.
     */
    public void testCanPerformWithInvalidStatus() throws Exception {
        handler = new CheckpointSubmissionPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            Phase phase = createPhase(1, 1, "Invalid", 2, "Checkpoint Submission");
            handler.canPerform(phase);
            fail("canPerform() did not throw PhaseHandlingException for invalid phase status.");
        } catch (PhaseHandlingException e) {
            // expected.
        }
    }

    /**
     * <p>
     * Tests canPerform(Phase) with invalid phase type.
     * </p>
     * <p>
     * expect throw PhaseHandlingException.
     * </p>
     * @throws Exception
     *             not under test.
     */
    public void testCanPerformWithInvalidType() throws Exception {
        handler = new CheckpointSubmissionPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            Phase phase = createPhase(1, 1, "Scheduled", 1, "INVALID");
            handler.canPerform(phase);
            fail("canPerform() did not throw PhaseHandlingException for invalid phase type.");
        } catch (PhaseHandlingException e) {
            // expected.
        }
    }

    /**
     * <p>
     * Tests the CheckpointSubmissionPhaseHandler() constructor and canPerform with Scheduled statuses.
     * </p>
     * <p>
     * If the conditions meets, should return true, otherwise return false.
     * </p>
     * @throws Exception
     *             not under test.
     */
    public void testCanPerformWithScheduled() throws Exception {
        handler = new CheckpointSubmissionPhaseHandler();

        try {
            cleanTables();

            Project project = super.setupPhases();
            Phase[] phases = project.getAllPhases();
            Phase submission = phases[13];

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
     * <p>
     * Tests the CheckpointSubmissionPhaseHandler() constructor and canPerform with OPEN statuses.
     * </p>
     * <p>
     * If the conditions meets, should return true, otherwise return false.
     * </p>
     * @throws Exception
     *             not under test.
     */
    public void testCanPerformHandlerWithOpen1() throws Exception {
        handler = new CheckpointSubmissionPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            cleanTables();

            Project project = super.setupPhases();
            Phase[] phases = project.getAllPhases();
            Phase submissionPhase = phases[13];

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

            Phase screeningPhase = phases[14];
            long screeningPhaseId = screeningPhase.getId();
            Resource submitter = super.createResource(1, submissionPhase.getId(), 1, 19);
            Resource screener = super.createResource(2, screeningPhaseId, 1, 20);

            Upload upload = createUpload(1, 1, submitter.getId(), 1, 1, "parameter");
            Submission submission = createSubmission(1, 1, 1, 3);
            Scorecard scorecard = createScorecard(1, 1, 1, 1, "name", "1.0", 75.0f, 100.0f);
            Review review = createReview(1, screener.getId(), submission.getId(), scorecard.getId(), true, 80.0f);

            insertResources(conn, new Resource[] {submitter, screener });
            insertResourceInfo(conn, submitter.getId(), 1, "11111");
            insertResourceInfo(conn, screener.getId(), 1, "11112");
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
     * <p>
     * Tests the CheckpointSubmissionPhaseHandler() constructor and canPerform with OPEN statuses.
     * </p>
     * <p>
     * If the conditions meets, should return true, otherwise return false.
     * </p>
     * @throws Exception
     *             not under test.
     */
    public void testCanPerformHandlerWithOpen2() throws Exception {
        handler = new CheckpointSubmissionPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            cleanTables();

            Project project = super.setupPhases();
            Phase[] phases = project.getAllPhases();
            Phase submissionPhase = phases[13];

            submissionPhase.setPhaseStatus(PhaseStatus.OPEN);
            submissionPhase.getAllDependencies()[0].setDependentStart(false);
            submissionPhase.setActualStartDate(new Date(System.currentTimeMillis() - 1000));
            submissionPhase.setActualEndDate(new Date());
            submissionPhase.getAllDependencies()[0].getDependency().setPhaseStatus(PhaseStatus.CLOSED);

            Connection conn = getConnection();

            Phase screeningPhase = phases[14];
            long screeningPhaseId = screeningPhase.getId();
            Resource submitter = super.createResource(1, submissionPhase.getId(), 1, 19);
            Resource screener = super.createResource(2, screeningPhaseId, 1, 20);

            Upload upload = createUpload(1, 1, submitter.getId(), 1, 1, "parameter");
            Submission submission = createSubmission(1, 1, 1, 3);
            Scorecard scorecard = createScorecard(1, 1, 1, 1, "name", "1.0", 75.0f, 100.0f);
            Review review = createReview(1, screener.getId(), submission.getId(), scorecard.getId(), true, 80.0f);

            insertResources(conn, new Resource[] {submitter, screener });
            insertResourceInfo(conn, submitter.getId(), 1, "11111");
            insertResourceInfo(conn, screener.getId(), 1, "11112");
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
     * <p>
     * Tests the CheckpointSubmissionPhaseHandler() constructor and canPerform with OPEN statuses.
     * </p>
     * <p>
     * If the conditions meets, should return true, otherwise return false.
     * </p>
     * @throws Exception
     *             not under test.
     */
    public void testCanPerformHandlerWithOpen3() throws Exception {
        handler = new CheckpointSubmissionPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            cleanTables();

            Project project = super.setupPhases();
            Phase[] phases = project.getAllPhases();
            Phase submissionPhase = phases[13];

            submissionPhase.setPhaseStatus(PhaseStatus.OPEN);
            submissionPhase.getAllDependencies()[0].setDependentStart(false);
            submissionPhase.setActualStartDate(new Date(System.currentTimeMillis() - 1000));
            submissionPhase.setActualEndDate(new Date());
            submissionPhase.getAllDependencies()[0].getDependency().setPhaseStatus(PhaseStatus.CLOSED);
            submissionPhase.setAttribute("Manual Screening", "Yes");

            Connection conn = getConnection();

            Phase screeningPhase = phases[14];
            long screeningPhaseId = screeningPhase.getId();
            Resource submitter = super.createResource(1, submissionPhase.getId(), 1, 19);
            Resource screener = super.createResource(2, screeningPhaseId, 1, 20);

            Upload upload = createUpload(1, 1, submitter.getId(), 1, 1, "parameter");
            Submission submission = createSubmission(1, 1, 1, 3);
            Scorecard scorecard = createScorecard(1, 1, 1, 1, "name", "1.0", 75.0f, 100.0f);
            Review review = createReview(1, screener.getId(), submission.getId(), scorecard.getId(), true, 80.0f);

            insertResources(conn, new Resource[] {submitter, screener });
            insertResourceInfo(conn, submitter.getId(), 1, "11111");
            insertResourceInfo(conn, screener.getId(), 1, "11112");
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
     * <p>
     * Tests perform(Phase) with null phase.
     * </p>
     * <p>
     * expect throw IllegalArgumentException.
     * </p>
     * @throws Exception
     *             not under test.
     */
    public void testPerformWithNullPhase() throws Exception {
        handler = new CheckpointSubmissionPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            handler.perform(null, "operator");
            fail("perform() did not throw IllegalArgumentException for null argument.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * <p>
     * Tests perform(Phase) with invalid phase status.
     * </p>
     * <p>
     * expect throw PhaseHandlingException.
     * </p>
     * @throws Exception
     *             not under test.
     */
    public void testPerformWithInvalidStatus() throws Exception {
        handler = new CheckpointSubmissionPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            Phase phase = createPhase(1, 1, "Invalid", 1, "Checkpoint Submission");
            handler.perform(phase, "operator");
            fail("perform() did not throw PhaseHandlingException for invalid phase status.");
        } catch (PhaseHandlingException e) {
            // expected.
        }
    }

    /**
     * <p>
     * Tests perform(Phase) with invalid phase type.
     * </p>
     * <p>
     * expect throw PhaseHandlingException.
     * </p>
     * @throws Exception
     *             not under test.
     */
    public void testPerformWithInvalidType() throws Exception {
        handler = new CheckpointSubmissionPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            Phase phase = createPhase(1, 1, "Scheduled", 1, "INVALID");
            handler.perform(phase, "operator");
            fail("perform() did not throw PhaseHandlingException for invalid phase type.");
        } catch (PhaseHandlingException e) {
            // expected.
        }
    }

    /**
     * <p>
     * Tests perform(Phase) with null operator.
     * </p>
     * <p>
     * expect throw IllegalArgumentException.
     * </p>
     * @throws Exception
     *             not under test.
     */
    public void testPerformWithNullOperator() throws Exception {
        handler = new CheckpointSubmissionPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            Phase phase = createPhase(1, 1, "Scheduled", 1, "Checkpoint Submission");
            handler.perform(phase, null);
            fail("perform() did not throw IllegalArgumentException for null operator.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * <p>
     * Tests perform(Phase) with empty operator.
     * </p>
     * <p>
     * expect throw IllegalArgumentException.
     * </p>
     * @throws Exception
     *             not under test.
     */
    public void testPerformWithEmptyOperator() throws Exception {
        handler = new CheckpointSubmissionPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            Phase phase = createPhase(1, 1, "Scheduled", 1, "Checkpoint Submission");
            handler.perform(phase, "   ");
            fail("perform() did not throw IllegalArgumentException for empty operator.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Tests the perform with Scheduled and Open statuses and checks whether a post-mortem phase is inserted when
     * there
     * is no submission.
     * @throws Exception
     *             to JUnit.
     */
    public void testPerformWithoutSubmission() throws Exception {
        handler = new CheckpointSubmissionPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            cleanTables();

            // test with scheduled status.
            Phase submissionPhase = createPhase(1, 1, "Scheduled", 2, "Checkpoint Submission");
            String operator = "1001";
            Connection conn = getConnection();
            insertProject(conn);
            insertProjectInfo(getConnection(), 1, new long[] {44 }, new String[] {"true" });

            // test with open status
            submissionPhase.setPhaseStatus(PhaseStatus.OPEN);
            handler.perform(submissionPhase, operator);

            // manually verify the mail
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * Tests the perform to stop the phase without submissions.
     * @throws Exception
     *             to JUnit.
     */
    public void testPerformToStopWithoutSubmissions() throws Exception {
        handler = new CheckpointSubmissionPhaseHandler();

        try {
            cleanTables();

            Project project = setupPhases();

            // test with scheduled status.
            Phase submissionPhase = project.getAllPhases()[13];
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
     * @throws Exception
     *             to JUnit.
     */
    public void testPerformWithSubmissions() throws Exception {
        handler = new CheckpointSubmissionPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            cleanTables();

            // test with scheduled status.
            Project project = setupPhases();
            Phase[] phases = project.getAllPhases();
            Phase submissionPhase = phases[13];

            // test with scheduled status.
            String operator = "operator";
            Connection conn = getConnection();

            // test with open status
            submissionPhase.setPhaseStatus(PhaseStatus.OPEN);

            // create a submission
            Resource resource = super.createResource(1, 102L, 1, 19);
            super.insertResources(conn, new Resource[] {resource });

            Upload upload = super.createUpload(1, project.getId(), resource.getId(), 1, 1, "Paramter");
            super.insertUploads(conn, new Upload[] {upload });

            Submission submission = super.createSubmission(1, upload.getId(), 1, 3);
            super.insertSubmissions(conn, new Submission[] {submission });

            handler.perform(submissionPhase, operator);

            // manually check the email
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * Tests the perform to stop the phase with submissions.
     * @throws Exception
     *             to JUnit.
     */
    public void testPerform_with_submission_stop() throws Exception {
        handler = new CheckpointSubmissionPhaseHandler();

        try {
            cleanTables();

            Project project = setupPhases();

            // test with scheduled status.
            Phase submissionPhase = project.getAllPhases()[13];
            submissionPhase.setPhaseStatus(PhaseStatus.OPEN);
            String operator = "1001";

            Connection conn = getConnection();
            // create a registration
            Resource resource = createResource(4, 101L, 1, 19);
            super.insertResources(conn, new Resource[] {resource });
            insertResourceInfo(conn, resource.getId(), 1, "4");
            insertResourceInfo(conn, resource.getId(), 2, "ACRush");
            insertResourceInfo(conn, resource.getId(), 4, "3808");
            insertResourceInfo(conn, resource.getId(), 5, "100");

            Upload upload = super.createUpload(1, project.getId(), resource.getId(), 1, 1, "Paramter");
            super.insertUploads(conn, new Upload[] {upload });

            Submission submission = super.createSubmission(1, upload.getId(), 1, 3);
            super.insertSubmissions(conn, new Submission[] {submission });

            // another register
            resource = createResource(5, 101L, 1, 19);
            super.insertResources(conn, new Resource[] {resource });
            insertResourceInfo(conn, resource.getId(), 1, "5");
            insertResourceInfo(conn, resource.getId(), 2, "UdH-WiNGeR");
            insertResourceInfo(conn, resource.getId(), 4, "3338");
            insertResourceInfo(conn, resource.getId(), 5, "90");
            upload = super.createUpload(2, project.getId(), resource.getId(), 1, 1, "Paramter");
            super.insertUploads(conn, new Upload[] {upload });

            submission = super.createSubmission(2, upload.getId(), 1, 3);
            super.insertSubmissions(conn, new Submission[] {submission });

            handler.perform(submissionPhase, operator);

            // manually check the email
        } finally {
            cleanTables();
            closeConnection();
        }
    }

}
