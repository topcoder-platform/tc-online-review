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
 * All tests for CheckpointReviewPhaseHandler class.
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
public class CheckpointReviewPhaseHandlerTest extends BaseTest {
    /**
     * <p>
     * Represents the CheckpointReviewPhaseHandler instance for testing.
     * </p>
     */
    private CheckpointReviewPhaseHandler handler;

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
     * Tests the CheckpointReviewPhaseHandler() constructor, instance should be created.
     * </p>
     * @throws Exception
     *             pass any unexpected exception to JUnit.
     */
    public void testCtor() throws Exception {
        handler = new CheckpointReviewPhaseHandler();

        assertNotNull("instance should not be null", handler);
    }

    /**
     * <p>
     * Tests the CheckpointReviewPhaseHandler(String) constructor, instance should be created.
     * </p>
     * @throws Exception
     *             pass any unexpected exception to JUnit.
     */
    public void testCtor2() throws Exception {
        handler = new CheckpointReviewPhaseHandler(PHASE_HANDLER_NAMESPACE);

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
        handler = new CheckpointReviewPhaseHandler(PHASE_HANDLER_NAMESPACE);

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
        handler = new CheckpointReviewPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            Phase phase = createPhase(1, 1, "Invalid", 3, "Checkpoint Review");
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
        handler = new CheckpointReviewPhaseHandler(PHASE_HANDLER_NAMESPACE);

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
     * Tests perform(Phase) with null phase.
     * </p>
     * <p>
     * expect throw IllegalArgumentException.
     * </p>
     * @throws Exception
     *             not under test.
     */
    public void testPerformWithNullPhase() throws Exception {
        handler = new CheckpointReviewPhaseHandler(PHASE_HANDLER_NAMESPACE);

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
        handler = new CheckpointReviewPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            Phase phase = createPhase(1, 1, "Invalid", 1, "Checkpoint Review");
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
        handler = new CheckpointReviewPhaseHandler(PHASE_HANDLER_NAMESPACE);

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
        handler = new CheckpointReviewPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            Phase phase = createPhase(1, 1, "Scheduled", 1, "Review");
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
        handler = new CheckpointReviewPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            Phase phase = createPhase(1, 1, "Scheduled", 1, "Review");
            handler.perform(phase, "   ");
            fail("perform() did not throw IllegalArgumentException for empty operator.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * <p>
     * Tests the CheckpointReviewPhaseHandler() constructor and canPerform with Scheduled statuses.
     * </p>
     * <p>
     * <ul>
     * <li>If time not passed, should return false</li>
     * <li>If time passed, but dependency not meet, should return false.</li>
     * <li>If time passed and dependency met, and there is a submission for screening phase, should return true.
     * </ul>
     * </p>
     * @throws Exception
     *             not under test.
     */
    public void testCanPerformWithScheduled() throws Exception {
        handler = new CheckpointReviewPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            cleanTables();

            Project project = setupPhases();
            Phase[] phases = project.getAllPhases();
            Phase submissionPhase = phases[13];
            Phase reviewPhase = phases[15];

            // test with scheduled status.
            reviewPhase.setPhaseStatus(PhaseStatus.SCHEDULED);

            // time has not passed, nor dependencies met
            assertFalse("canPerform should have returned false", handler.canPerform(reviewPhase).isSuccess());

            // time has passed, but dependency not met.
            reviewPhase.setActualStartDate(new Date());
            assertFalse("canPerform should have returned false", handler.canPerform(reviewPhase).isSuccess());

            // time has passed and dependency met.
            reviewPhase.getAllDependencies()[0].getDependency().setPhaseStatus(PhaseStatus.CLOSED);

            // And we set up a active submission in screening phase
            Connection conn = getConnection();

            Resource submitter = createResource(1, submissionPhase.getId(), 1, 19);

            Upload upload = createUpload(1, 1, submitter.getId(), 1, 1, "parameter");
            Submission submission = createSubmission(1, 1, 1, 3);

            insertResources(conn, new Resource[] {submitter });
            insertResourceInfo(conn, submitter.getId(), 1, "11111");
            insertUploads(conn, new Upload[] {upload });
            insertSubmissions(conn, new Submission[] {submission });

            assertTrue("canPerform should have returned true", handler.canPerform(reviewPhase).isSuccess());
        } finally {
            cleanTables();
        }
    }

    /**
     * <p>
     * Tests the CheckpointReviewPhaseHandler() constructor and canPerform with Scheduled statuses.
     * </p>
     * <p>
     * <ul>
     * <li>If time not passed, should return false</li>
     * <li>If time passed, but dependency not meet, should return false.</li>
     * <li>If time passed and dependency met, but there is no submissions for screening phase, should return false.
     * </ul>
     * </p>
     * @throws Exception
     *             not under test.
     */
    public void testCanPerformWithScheduled2() throws Exception {
        handler = new CheckpointReviewPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            cleanTables();

            Project project = setupPhases();
            Phase[] phases = project.getAllPhases();
            Phase reviewPhase = phases[15];

            // test with scheduled status.
            reviewPhase.setPhaseStatus(PhaseStatus.SCHEDULED);

            // time has not passed, nor dependencies met
            assertFalse("canPerform should have returned false", handler.canPerform(reviewPhase).isSuccess());

            // time has passed, but dependency not met.
            reviewPhase.setActualStartDate(new Date());
            assertFalse("canPerform should have returned false", handler.canPerform(reviewPhase).isSuccess());

            // time has passed and dependency met.
            reviewPhase.getAllDependencies()[0].getDependency().setPhaseStatus(PhaseStatus.CLOSED);

            assertFalse("canPerform should have returned false", handler.canPerform(reviewPhase).isSuccess());
        } finally {
            cleanTables();
        }
    }

    /**
     * <p>
     * Tests the CheckpointReviewPhaseHandler() constructor and canPerform with Open statuses.
     * </p>
     * <p>
     * <ul>
     * <li>If time not passed, should return false</li>
     * </ul>
     * </p>
     * @throws Exception
     *             not under test.
     */
    public void testCanPerformHandlerWithOpen() throws Exception {
        handler = new CheckpointReviewPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            cleanTables();

            Project project = setupPhases();
            Phase[] phases = project.getAllPhases();
            Phase reviewPhase = phases[15];

            // test with open status.
            reviewPhase.setPhaseStatus(PhaseStatus.OPEN);

            // change dependency type to F2F
            reviewPhase.getAllDependencies()[0].setDependentStart(false);

            // time has not passed, dependencies not met
            assertFalse("canPerform should have returned false", handler.canPerform(reviewPhase).isSuccess());
        } finally {
            cleanTables();
        }
    }

    /**
     * <p>
     * Test the canPerform method.
     * </p>
     * <p>
     * If all conditions meet, should return true.
     * </p>
     * @throws Exception
     *             into JUnit
     */
    public void testCanPerformAccuracy1() throws Exception {
        handler = new CheckpointReviewPhaseHandler();

        try {
            cleanTables();

            Project project = setupPhases();

            // test with scheduled status.
            Phase reviewPhase = project.getAllPhases()[15];
            reviewPhase.setPhaseStatus(PhaseStatus.OPEN);

            Connection conn = getConnection();

            // insert the reviewers
            Resource reviewer = createResource(6, reviewPhase.getId(), 1, 21);
            insertResources(conn, new Resource[] {reviewer });
            insertResourceInfo(conn, reviewer.getId(), 1, "2");

            // create a registration
            Resource resource = createResource(4, 101L, 1, 19);
            insertResources(conn, new Resource[] {resource });
            insertResourceInfo(conn, resource.getId(), 1, "4");
            insertResourceInfo(conn, resource.getId(), 2, "ACRush");
            insertResourceInfo(conn, resource.getId(), 4, "3808");
            insertResourceInfo(conn, resource.getId(), 5, "100");

            // insert upload/submission
            Upload upload = createUpload(1, project.getId(), resource.getId(), 1, 1, "Parameter");
            insertUploads(conn, new Upload[] {upload });

            Submission submission = createSubmission(1, upload.getId(), 1, 3);
            insertSubmissions(conn, new Submission[] {submission });
            // insertScorecards
            Scorecard sc = this.createScorecard(1, 1, 2, 1, "name", "1.0", 75.0f, 100.0f);
            insertScorecards(conn, new Scorecard[] {sc });
            Review review = createReview(1, reviewer.getId(), submission.getId(), sc.getId(), true, 77.0f);
            insertReviews(conn, new Review[] {review });

            // another register
            resource = createResource(5, 101L, 1, 19);
            insertResources(conn, new Resource[] {resource });
            insertResourceInfo(conn, resource.getId(), 1, "5");
            insertResourceInfo(conn, resource.getId(), 2, "UdH-WiNGeR");
            insertResourceInfo(conn, resource.getId(), 4, "3338");
            insertResourceInfo(conn, resource.getId(), 5, "90");
            upload = createUpload(2, project.getId(), resource.getId(), 1, 1, "Parameter");
            insertUploads(conn, new Upload[] {upload });

            submission = createSubmission(2, upload.getId(), 1, 3);
            insertSubmissions(conn, new Submission[] {submission });
            sc = this.createScorecard(2, 1, 2, 1, "name", "1.0", 75.0f, 100.0f);
            insertScorecards(conn, new Scorecard[] {sc });
            review = createReview(2, reviewer.getId(), submission.getId(), sc.getId(), true, 77.0f);
            insertReviews(conn, new Review[] {review });

            assertTrue("expect true", handler.canPerform(reviewPhase).isSuccess());

            // manually check the email
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * <p>
     * Test the canPerform method.
     * </p>
     * <p>
     * If all condition meets, but the review is not committed, the phase can not be stopped.
     * </p>
     * @throws Exception
     *             into JUnit
     */
    public void testCanPerformAccuracy2() throws Exception {
        handler = new CheckpointReviewPhaseHandler();

        try {
            cleanTables();

            Project project = setupPhases();

            // test with scheduled status.
            Phase reviewPhase = project.getAllPhases()[15];
            reviewPhase.setPhaseStatus(PhaseStatus.OPEN);

            Connection conn = getConnection();

            // insert the reviewers
            Resource reviewer = createResource(6, reviewPhase.getId(), 1, 21);
            insertResources(conn, new Resource[] {reviewer });
            insertResourceInfo(conn, reviewer.getId(), 1, "2");

            // create a registration
            Resource resource = createResource(4, 101L, 1, 19);
            insertResources(conn, new Resource[] {resource });
            insertResourceInfo(conn, resource.getId(), 1, "4");
            insertResourceInfo(conn, resource.getId(), 2, "ACRush");
            insertResourceInfo(conn, resource.getId(), 4, "3808");
            insertResourceInfo(conn, resource.getId(), 5, "100");

            // insert upload/submission
            Upload upload = createUpload(1, project.getId(), resource.getId(), 1, 1, "Parameter");
            insertUploads(conn, new Upload[] {upload });

            Submission submission = createSubmission(1, upload.getId(), 1, 3);
            insertSubmissions(conn, new Submission[] {submission });
            // insertScorecards
            Scorecard sc = this.createScorecard(1, 1, 2, 1, "name", "1.0", 75.0f, 100.0f);
            insertScorecards(conn, new Scorecard[] {sc });
            Review review = createReview(1, reviewer.getId(), submission.getId(), sc.getId(), false, 77.0f);
            insertReviews(conn, new Review[] {review });

            assertFalse("expect false", handler.canPerform(reviewPhase).isSuccess());

            // manually check the email
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * <p>
     * Test the canPerform method.
     * </p>
     * <p>
     * PhaseHandlingException expected for illegal case.
     * </p>
     * @throws Exception
     *             into JUnit
     */
    public void testCanPerformAccuracy3() throws Exception {
        handler = new CheckpointReviewPhaseHandler();

        try {
            cleanTables();

            Project project = setupPhases();

            // test with scheduled status.
            Phase reviewPhase = project.getAllPhases()[15];
            reviewPhase.setPhaseStatus(PhaseStatus.OPEN);

            Connection conn = getConnection();

            // insert the reviewers
            Resource reviewer = createResource(6, reviewPhase.getId(), 1, 21);
            insertResources(conn, new Resource[] {reviewer });
            insertResourceInfo(conn, reviewer.getId(), 1, "2");

            // insert another reviewer
            reviewer = createResource(7, reviewPhase.getId(), 1, 21);
            insertResources(conn, new Resource[] {reviewer });
            insertResourceInfo(conn, reviewer.getId(), 1, "2");

            // create a registration
            Resource resource = createResource(4, 101L, 1, 19);
            insertResources(conn, new Resource[] {resource });
            insertResourceInfo(conn, resource.getId(), 1, "4");
            insertResourceInfo(conn, resource.getId(), 2, "ACRush");
            insertResourceInfo(conn, resource.getId(), 4, "3808");
            insertResourceInfo(conn, resource.getId(), 5, "100");

            // insert upload/submission
            Upload upload = createUpload(1, project.getId(), resource.getId(), 1, 1, "Parameter");
            insertUploads(conn, new Upload[] {upload });

            Submission submission = createSubmission(1, upload.getId(), 1, 3);
            insertSubmissions(conn, new Submission[] {submission });
            // insertScorecards
            Scorecard sc = this.createScorecard(1, 1, 2, 1, "name", "1.0", 75.0f, 100.0f);
            insertScorecards(conn, new Scorecard[] {sc });
            Review review = createReview(1, reviewer.getId(), submission.getId(), sc.getId(), true, 77.0f);
            insertReviews(conn, new Review[] {review });

            handler.canPerform(reviewPhase);

            fail("expect PhaseHandlingException");
        } catch (PhaseHandlingException e) {
            // expected
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * <p>
     * Test the canPerform method.
     * </p>
     * <p>
     * PhaseHandlingException expected for illegal case.
     * </p>
     * @throws Exception
     *             into JUnit
     */
    public void testCanPerformAccuracy4() throws Exception {
        handler = new CheckpointReviewPhaseHandler();

        try {
            cleanTables();

            Project project = setupPhases();

            // test with scheduled status.
            Phase reviewPhase = project.getAllPhases()[15];
            reviewPhase.setPhaseStatus(PhaseStatus.OPEN);

            Connection conn = getConnection();

            // insert the reviewers
            Resource reviewer = createResource(6, reviewPhase.getId(), 1, 21);
            insertResources(conn, new Resource[] {reviewer });
            insertResourceInfo(conn, reviewer.getId(), 1, "2");

            // create a registration
            Resource resource = createResource(4, 101L, 1, 19);
            insertResources(conn, new Resource[] {resource });
            insertResourceInfo(conn, resource.getId(), 1, "4");
            insertResourceInfo(conn, resource.getId(), 2, "ACRush");
            insertResourceInfo(conn, resource.getId(), 4, "3808");
            insertResourceInfo(conn, resource.getId(), 5, "100");

            // insert upload/submission
            Upload upload = createUpload(1, project.getId(), resource.getId(), 1, 1, "Parameter");
            insertUploads(conn, new Upload[] {upload });

            Submission submission = createSubmission(1, upload.getId(), 1, 3);
            insertSubmissions(conn, new Submission[] {submission });
            // insertScorecards
            Scorecard sc = this.createScorecard(1, 1, 2, 1, "name", "1.0", 75.0f, 100.0f);
            insertScorecards(conn, new Scorecard[] {sc });
            Review review = createReview(1, reviewer.getId(), submission.getId(), sc.getId(), true, 77.0f);
            insertReviews(conn, new Review[] {review });

            // another register
            resource = createResource(5, 101L, 1, 19);
            insertResources(conn, new Resource[] {resource });
            insertResourceInfo(conn, resource.getId(), 1, "5");
            insertResourceInfo(conn, resource.getId(), 2, "UdH-WiNGeR");
            insertResourceInfo(conn, resource.getId(), 4, "3338");
            insertResourceInfo(conn, resource.getId(), 5, "90");
            upload = createUpload(2, project.getId(), resource.getId(), 1, 1, "Parameter");
            insertUploads(conn, new Upload[] {upload });

            submission = createSubmission(2, upload.getId(), 1, 3);
            insertSubmissions(conn, new Submission[] {submission });

            handler.canPerform(reviewPhase);

            assertFalse("expect false", handler.canPerform(reviewPhase).isSuccess());
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * <p>
     * Test the perform method. To start the phase. No reviewer has been assigned.
     * </p>
     * @throws Exception
     *             into JUnit
     */
    public void testPerform_Start_noReviewer() throws Exception {
        handler = new CheckpointReviewPhaseHandler(CheckpointReviewPhaseHandler.DEFAULT_NAMESPACE);

        try {
            cleanTables();

            Project project = setupPhases();

            // test with scheduled status.
            Phase reviewPhase = project.getAllPhases()[15];
            String operator = "1001";

            Connection conn = getConnection();

            // create a registration
            Resource resource = createResource(4, 101L, 1, 19);
            insertResources(conn, new Resource[] {resource });
            insertResourceInfo(conn, resource.getId(), 1, "4");
            insertResourceInfo(conn, resource.getId(), 2, "ACRush");
            insertResourceInfo(conn, resource.getId(), 4, "3808");
            insertResourceInfo(conn, resource.getId(), 5, "100");

            Upload upload = createUpload(1, project.getId(), resource.getId(), 1, 1, "Parameter");
            insertUploads(conn, new Upload[] {upload });

            Submission submission = createSubmission(1, upload.getId(), 1, 3);
            insertSubmissions(conn, new Submission[] {submission });

            handler.perform(reviewPhase, operator);

            // manually check the email
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * <p>
     * Test the perform method. To start the phase. reviewer has been assigned.
     * </p>
     * @throws Exception
     *             into JUnit
     */
    public void testPerform_Start_withReviewer() throws Exception {
        handler = new CheckpointReviewPhaseHandler();

        try {
            cleanTables();

            Project project = setupPhases();

            // test with scheduled status.
            Phase reviewPhase = project.getAllPhases()[15];
            String operator = "1001";

            Connection conn = getConnection();

            // create a registration
            Resource resource = createResource(4, 101L, 1, 19);
            insertResources(conn, new Resource[] {resource });
            insertResourceInfo(conn, resource.getId(), 1, "4");
            insertResourceInfo(conn, resource.getId(), 2, "ACRush");
            insertResourceInfo(conn, resource.getId(), 4, "3808");
            insertResourceInfo(conn, resource.getId(), 5, "100");

            Upload upload = createUpload(1, project.getId(), resource.getId(), 1, 1, "Parameter");
            insertUploads(conn, new Upload[] {upload });

            Submission submission = createSubmission(1, upload.getId(), 1, 3);
            insertSubmissions(conn, new Submission[] {submission });

            // insert the reviewers
            Resource reviewer = createResource(6, reviewPhase.getId(), 1, 21);
            insertResources(conn, new Resource[] {reviewer });
            insertResourceInfo(conn, reviewer.getId(), 1, "2");

            handler.perform(reviewPhase, operator);

            // manually check the email
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * <p>
     * Test the perform method. To stop the phase.
     * </p>
     * @throws Exception
     *             into JUnit
     */
    public void testPerformToStop() throws Exception {
        handler = new CheckpointReviewPhaseHandler();

        try {
            cleanTables();

            Project project = setupPhases();

            // test with scheduled status.
            Phase reviewPhase = project.getAllPhases()[15];
            reviewPhase.setPhaseStatus(PhaseStatus.OPEN);
            String operator = "1001";

            Connection conn = getConnection();

            // insert the reviewers
            Resource reviewer = createResource(6, reviewPhase.getId(), 1, 21);
            insertResources(conn, new Resource[] {reviewer });
            insertResourceInfo(conn, reviewer.getId(), 1, "2");

            // create a registration
            Resource resource = createResource(4, 101L, 1, 19);
            insertResources(conn, new Resource[] {resource });
            insertResourceInfo(conn, resource.getId(), 1, "4");
            insertResourceInfo(conn, resource.getId(), 2, "ACRush");
            insertResourceInfo(conn, resource.getId(), 4, "3808");
            insertResourceInfo(conn, resource.getId(), 5, "100");

            // insert upload/submission
            Upload upload = createUpload(1, project.getId(), resource.getId(), 1, 1, "Parameter");
            insertUploads(conn, new Upload[] {upload });

            Submission submission = createSubmission(1, upload.getId(), 1, 3);
            insertSubmissions(conn, new Submission[] {submission });
            // insert scorecard
            Scorecard sc = this.createScorecard(1, 1, 2, 1, "name", "1.0", 75.0f, 100.0f);
            insertScorecards(conn, new Scorecard[] {sc });
            Review review = createReview(1, reviewer.getId(), submission.getId(), sc.getId(), true, 77.0f);
            insertReviews(conn, new Review[] {review });

            // another register
            resource = createResource(5, 101L, 1, 19);
            insertResources(conn, new Resource[] {resource });
            insertResourceInfo(conn, resource.getId(), 1, "5");
            insertResourceInfo(conn, resource.getId(), 2, "UdH-WiNGeR");
            insertResourceInfo(conn, resource.getId(), 4, "3338");
            insertResourceInfo(conn, resource.getId(), 5, "90");
            upload = createUpload(2, project.getId(), resource.getId(), 1, 1, "Parameter");
            insertUploads(conn, new Upload[] {upload });

            submission = createSubmission(2, upload.getId(), 1, 3);
            insertSubmissions(conn, new Submission[] {submission });
            sc = this.createScorecard(2, 1, 2, 1, "name", "1.0", 75.0f, 100.0f);
            insertScorecards(conn, new Scorecard[] {sc });
            review = createReview(2, reviewer.getId(), submission.getId(), sc.getId(), true, 77.0f);
            insertReviews(conn, new Review[] {review });

            handler.perform(reviewPhase, operator);

            // manually check the email
        } finally {
            cleanTables();
            closeConnection();
        }
    }

}
