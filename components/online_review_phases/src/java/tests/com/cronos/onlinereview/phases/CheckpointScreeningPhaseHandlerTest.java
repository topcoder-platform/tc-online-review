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
 * All tests for CheckpointScreeningPhaseHandler class.
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
public class CheckpointScreeningPhaseHandlerTest extends BaseTest {

    /**
     * <p>
     * Represents the CheckpointScreeningPhaseHandler instance for testing.
     * </p>
     */
    private CheckpointScreeningPhaseHandler handler;

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
     * Tests the CheckpointScreeningPhaseHandler() constructor, instance should be created.
     * </p>
     * @throws Exception
     *             pass any unexpected exception to JUnit.
     */
    public void testCtor() throws Exception {
        handler = new CheckpointScreeningPhaseHandler();

        assertNotNull("instance should not be null", handler);
    }

    /**
     * <p>
     * Tests the CheckpointScreeningPhaseHandler(String) constructor, instance should be created.
     * </p>
     * @throws Exception
     *             pass any unexpected exception to JUnit.
     */
    public void testCtor2() throws Exception {
        handler = new CheckpointScreeningPhaseHandler(PHASE_HANDLER_NAMESPACE);

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
        handler = new CheckpointScreeningPhaseHandler(PHASE_HANDLER_NAMESPACE);

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
        handler = new CheckpointScreeningPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            Phase phase = createPhase(1, 1, "Invalid", 3, "Checkpoint Screening");
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
        handler = new CheckpointScreeningPhaseHandler(PHASE_HANDLER_NAMESPACE);

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
        handler = new CheckpointScreeningPhaseHandler(PHASE_HANDLER_NAMESPACE);

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
        handler = new CheckpointScreeningPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            Phase phase = createPhase(1, 1, "Invalid", 1, "Checkpoint Screening");
            handler.perform(phase, "operator");
            fail("perform() did not throw PhaseHandlingException for invalid phase status.");
        } catch (PhaseHandlingException e) {
            // expected.
        }
    }

    /**
     * <p>
     * Tests the CheckpointScreeningPhaseHandler() constructor and canPerform with Scheduled statuses.
     * </p>
     * <p>
     * If the conditions meets, should return true, otherwise return false.
     * </p>
     * @throws Exception
     *             not under test.
     */
    public void testCanPerformWithScheduled() throws Exception {
        handler = new CheckpointScreeningPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            cleanTables();

            Project project = super.setupPhases();
            Phase[] phases = project.getAllPhases();
            Phase submissionPhase = phases[13];
            Phase screeningPhase = phases[14];

            // test with scheduled status.
            screeningPhase.setPhaseStatus(PhaseStatus.SCHEDULED);

            // time has not passed, nor dependencies met
            assertFalse("canPerform should have returned false", handler.canPerform(screeningPhase).isSuccess());

            // time has passed, but dependency not met.
            screeningPhase.setActualStartDate(new Date());
            assertFalse("canPerform should have returned false", handler.canPerform(screeningPhase).isSuccess());

            // time has passed and dependency met.
            screeningPhase.getAllDependencies()[0].getDependency().setPhaseStatus(PhaseStatus.CLOSED);

            // Set up a active submission for screening phase
            Connection conn = getConnection();

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

            assertTrue("canPerform should have returned true", handler.canPerform(screeningPhase).isSuccess());
        } finally {
            cleanTables();
        }
    }

    /**
     * <p>
     * Tests the CheckpointScreeningPhaseHandler() constructor and canPerform with Open statuses.
     * </p>
     * <p>
     * If the conditions meets, should return true, otherwise return false.
     * </p>
     * @throws Exception
     *             not under test.
     */
    public void testCanPerformHandlerWithOpen1() throws Exception {
        handler = new CheckpointScreeningPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            cleanTables();

            Project project = super.setupPhases();
            Phase[] phases = project.getAllPhases();
            Phase screeningPhase = phases[14];

            // test with open status.
            screeningPhase.setPhaseStatus(PhaseStatus.OPEN);

            // change dependency type to false
            screeningPhase.getAllDependencies()[0].setDependentStart(false);

            // time has not passed, dependencies not met
            assertFalse("canPerform should have returned false", handler.canPerform(screeningPhase).isSuccess());
        } finally {
            cleanTables();
        }
    }

    /**
     * <p>
     * Tests the CheckpointScreeningPhaseHandler() constructor and canPerform with Open statuses.
     * </p>
     * <p>
     * If the conditions meets, should return true, otherwise return false.
     * </p>
     * @throws Exception
     *             not under test.
     */
    public void testCanPerformHandlerWithOpen2() throws Exception {
        handler = new CheckpointScreeningPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            cleanTables();

            Project project = super.setupPhases();
            Phase[] phases = project.getAllPhases();
            Phase screeningPhase = phases[14];

            // test with open status.
            screeningPhase.setPhaseStatus(PhaseStatus.OPEN);

            // make dependency closed
            screeningPhase.getAllDependencies()[0].getDependency().setPhaseStatus(PhaseStatus.CLOSED);

            assertTrue("canPerform should have returned true", handler.canPerform(screeningPhase).isSuccess());
        } finally {
            cleanTables();
        }
    }

    /**
     * <p>
     * Tests the CheckpointScreeningPhaseHandler() constructor and canPerform with Open statuses.
     * </p>
     * <p>
     * If the conditions meets, should return true, otherwise return false.
     * </p>
     * @throws Exception
     *             not under test.
     */
    public void testCanPerformHandlerWithOpen3() throws Exception {
        handler = new CheckpointScreeningPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            cleanTables();

            Project project = super.setupPhases();
            Phase[] phases = project.getAllPhases();
            Phase submissionPhase = phases[13];
            Phase screeningPhase = phases[14];

            // test with open status.
            screeningPhase.setPhaseStatus(PhaseStatus.OPEN);

            // make dependency closed
            screeningPhase.getAllDependencies()[0].getDependency().setPhaseStatus(PhaseStatus.CLOSED);

            // Set up a active submission for screening phase
            Connection conn = getConnection();

            long screeningPhaseId = screeningPhase.getId();
            Resource submitter1 = super.createResource(1, submissionPhase.getId(), 1, 19);
            Resource submitter2 = super.createResource(2, submissionPhase.getId(), 1, 19);
            Resource screener = super.createResource(3, screeningPhaseId, 1, 20);

            Upload upload1 = createUpload(1, 1, submitter1.getId(), 1, 1, "parameter");
            Upload upload2 = createUpload(2, 1, submitter1.getId(), 1, 1, "parameter");
            Submission submission1 = createSubmission(1, 1, 1, 3);
            Submission submission2 = createSubmission(2, 1, 1, 3);
            Scorecard scorecard = createScorecard(1, 1, 1, 1, "name", "1.0", 75.0f, 100.0f);
            Review review = createReview(1, screener.getId(), submission1.getId(), scorecard.getId(), true, 80.0f);

            insertResources(conn, new Resource[] {submitter1, submitter2, screener });
            insertResourceInfo(conn, submitter1.getId(), 1, "11111");
            insertResourceInfo(conn, submitter2.getId(), 1, "11111");
            insertResourceInfo(conn, screener.getId(), 1, "11112");
            insertUploads(conn, new Upload[] {upload1, upload2 });
            insertSubmissions(conn, new Submission[] {submission1, submission2 });
            insertScorecards(conn, new Scorecard[] {scorecard });
            insertReviews(conn, new Review[] {review });

            assertFalse("canPerform should have returned false", handler.canPerform(screeningPhase).isSuccess());
        } finally {
            cleanTables();
        }
    }

    /**
     * <p>
     * Tests the CheckpointScreeningPhaseHandler() constructor and canPerform with Open statuses.
     * </p>
     * <p>
     * If the conditions meets, should return true, otherwise return false.
     * </p>
     * @throws Exception
     *             not under test.
     */
    public void testCanPerformHandlerWithOpen4() throws Exception {
        handler = new CheckpointScreeningPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            cleanTables();

            Project project = super.setupPhases();
            Phase[] phases = project.getAllPhases();
            Phase submissionPhase = phases[13];
            Phase screeningPhase = phases[14];

            // test with open status.
            screeningPhase.setPhaseStatus(PhaseStatus.OPEN);

            // make dependency closed
            screeningPhase.getAllDependencies()[0].getDependency().setPhaseStatus(PhaseStatus.CLOSED);

            // Set up a active submission for screening phase
            Connection conn = getConnection();

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

            assertTrue("canPerform should have returned true", handler.canPerform(screeningPhase).isSuccess());
        } finally {
            cleanTables();
        }
    }

    /**
     * <p>
     * Tests the CheckpointScreeningPhaseHandler() constructor and canPerform with Open statuses.
     * </p>
     * <p>
     * If the conditions meets, should return true, otherwise return false.
     * </p>
     * @throws Exception
     *             not under test.
     */
    public void testCanPerformHandlerWithOpen5() throws Exception {
        handler = new CheckpointScreeningPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            cleanTables();

            Project project = super.setupPhases();
            Phase[] phases = project.getAllPhases();
            Phase submissionPhase = phases[13];
            Phase screeningPhase = phases[14];

            // test with open status.
            screeningPhase.setPhaseStatus(PhaseStatus.OPEN);

            // make dependency closed
            screeningPhase.getAllDependencies()[0].getDependency().setPhaseStatus(PhaseStatus.CLOSED);

            // Set up a active submission for screening phase
            Connection conn = getConnection();

            long screeningPhaseId = screeningPhase.getId();
            Resource submitter = super.createResource(1, submissionPhase.getId(), 1, 19);
            Resource screener = super.createResource(2, screeningPhaseId, 1, 20);

            Upload upload = createUpload(1, 1, submitter.getId(), 1, 1, "parameter");
            Submission submission = createSubmission(1, 1, 1, 3);
            Scorecard scorecard = createScorecard(1, 1, 1, 1, "name", "1.0", 75.0f, 100.0f);
            Review review = createReview(1, screener.getId(), submission.getId(), scorecard.getId(), false, 80.0f);

            insertResources(conn, new Resource[] {submitter, screener });
            insertResourceInfo(conn, submitter.getId(), 1, "11111");
            insertResourceInfo(conn, screener.getId(), 1, "11112");
            insertUploads(conn, new Upload[] {upload });
            insertSubmissions(conn, new Submission[] {submission });
            insertScorecards(conn, new Scorecard[] {scorecard });
            insertReviews(conn, new Review[] {review });

            assertFalse("canPerform should have returned false", handler.canPerform(screeningPhase).isSuccess());
        } finally {
            cleanTables();
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
        handler = new CheckpointScreeningPhaseHandler(PHASE_HANDLER_NAMESPACE);

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
        handler = new CheckpointScreeningPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            Phase phase = createPhase(1, 1, "Scheduled", 1, "Checkpoint Screening");
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
        handler = new CheckpointScreeningPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            Phase phase = createPhase(1, 1, "Scheduled", 1, "Screening");
            handler.perform(phase, "   ");
            fail("perform() did not throw IllegalArgumentException for empty operator.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Tests the perform with Scheduled statuses.
     * @throws Exception
     *             not under test.
     */
    public void testPerformWithScheduled() throws Exception {
        handler = new CheckpointScreeningPhaseHandler();

        // test with scheduled status.
        Phase screeningPhase = createPhase(1, 1, "Scheduled", 2, "Checkpoint Screening");
        String operator = "operator";
        handler.perform(screeningPhase, operator);

        // manually verify the email.
    }

    /**
     * Tests the perform with OPEN statuses.
     * @throws Exception
     *             to JUnit.
     */
    public void testPerformWithOpen1() throws Exception {
        handler = new CheckpointScreeningPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            cleanTables();

            // test with scheduled status.
            Project project = super.setupPhases();
            Phase[] phases = project.getAllPhases();
            Phase submissionPhase = phases[13];
            Phase screenPhase = phases[14];

            screenPhase.setPhaseStatus(PhaseStatus.OPEN);

            // 1. insert submitter
            Resource submitter = createResource(102, submissionPhase.getId(), project.getId(), 19);

            Connection conn = getConnection();

            // insert records
            insertResources(conn, new Resource[] {submitter });

            // we need to insert an external reference id
            // which references to resource's user id in resource_info table
            insertResourceInfo(conn, submitter.getId(), 1, "1002");

            // 2. insert screener
            Resource screener = createResource(101, screenPhase.getId(), project.getId(), 20);

            // insert records
            insertResources(conn, new Resource[] {screener });

            // we need to insert an external reference id
            // which references to resource's user id in resource_info table
            insertResourceInfo(conn, screener.getId(), 1, "1001");

            // 3. insert submission
            Upload submitterUpload = createUpload(102, project.getId(), submitter.getId(), 1, 1, "parameter");
            Submission submission = createSubmission(102, submitterUpload.getId(), 1, 3);
            submission.setUpload(submitterUpload);

            this.insertUploads(conn, new Upload[] {submitterUpload });
            this.insertSubmissions(conn, new Submission[] {submission });

            // 4. insert a scorecard here
            Scorecard scorecard = createScorecard(1, 1, 1, 1, "name", "1.0", 75.0f, 100.0f);

            // 5. insert a screening review
            Review screenReview = createReview(11, screener.getId(), submission.getId(), scorecard.getId(), true,
                90.0f);

            this.insertScorecards(conn, new Scorecard[] {scorecard });
            this.insertReviews(conn, new Review[] {screenReview });

            handler.perform(screenPhase, "1001");
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * Tests the perform with OPEN statuses.
     * @throws Exception
     *             to JUnit.
     */
    public void testPerformWithOpen2() throws Exception {
        handler = new CheckpointScreeningPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            cleanTables();

            // test with scheduled status.
            Project project = super.setupPhases();
            Phase[] phases = project.getAllPhases();
            Phase submissionPhase = phases[13];
            Phase screenPhase = phases[14];

            screenPhase.setPhaseStatus(PhaseStatus.OPEN);

            // 1. insert submitter
            Resource submitter = createResource(102, submissionPhase.getId(), project.getId(), 19);

            Connection conn = getConnection();

            // insert records
            insertResources(conn, new Resource[] {submitter });

            // we need to insert an external reference id
            // which references to resource's user id in resource_info table
            insertResourceInfo(conn, submitter.getId(), 1, "1002");

            // 2. insert screener
            Resource screener = createResource(101, screenPhase.getId(), project.getId(), 20);

            // insert records
            insertResources(conn, new Resource[] {screener });

            // we need to insert an external reference id
            // which references to resource's user id in resource_info table
            insertResourceInfo(conn, screener.getId(), 1, "1001");

            // 3. insert submission
            Upload submitterUpload = createUpload(102, project.getId(), submitter.getId(), 1, 1, "parameter");
            Submission submission = createSubmission(102, submitterUpload.getId(), 1, 3);
            submission.setUpload(submitterUpload);

            this.insertUploads(conn, new Upload[] {submitterUpload });
            this.insertSubmissions(conn, new Submission[] {submission });

            // 4. insert a scorecard here
            Scorecard scorecard = createScorecard(1, 1, 1, 1, "name", "1.0", 75.0f, 100.0f);

            // 5. insert a screening review
            Review screenReview = createReview(11, screener.getId(), submission.getId(), scorecard.getId(), true,
                65.0f);

            this.insertScorecards(conn, new Scorecard[] {scorecard });
            this.insertReviews(conn, new Review[] {screenReview });

            handler.perform(screenPhase, "1001");
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * Tests the perform with OPEN statuses.
     * @throws Exception
     *             to JUnit.
     */
    public void testPerformWithOpen3() throws Exception {
        handler = new CheckpointScreeningPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            cleanTables();

            // test with scheduled status.
            Project project = super.setupPhases();
            Phase[] phases = project.getAllPhases();
            Phase submissionPhase = phases[13];
            Phase screenPhase = phases[14];

            screenPhase.setPhaseStatus(PhaseStatus.OPEN);

            // 1. insert submitter
            Resource submitter1 = createResource(1, submissionPhase.getId(), project.getId(), 19);
            Resource submitter2 = createResource(2, submissionPhase.getId(), project.getId(), 19);

            Connection conn = getConnection();

            // insert records
            insertResources(conn, new Resource[] {submitter1, submitter2 });

            // we need to insert an external reference id
            // which references to resource's user id in resource_info table
            insertResourceInfo(conn, submitter1.getId(), 1, "1002");
            insertResourceInfo(conn, submitter2.getId(), 1, "1002");

            // 2. insert screener
            Resource screener = createResource(101, screenPhase.getId(), project.getId(), 20);

            // insert records
            insertResources(conn, new Resource[] {screener });

            // we need to insert an external reference id
            // which references to resource's user id in resource_info table
            insertResourceInfo(conn, screener.getId(), 1, "1001");

            // 3. insert submission
            Upload upload1 = createUpload(102, project.getId(), submitter1.getId(), 1, 1, "parameter");
            Upload upload2 = createUpload(103, project.getId(), submitter2.getId(), 1, 1, "parameter");
            Submission submission1 = createSubmission(102, upload1.getId(), 1, 3);
            Submission submission2 = createSubmission(103, upload2.getId(), 1, 3);
            submission1.setUpload(upload1);
            submission2.setUpload(upload2);

            this.insertUploads(conn, new Upload[] {upload1, upload2 });
            this.insertSubmissions(conn, new Submission[] {submission1, submission2 });

            // 4. insert a scorecard here
            Scorecard scorecard = createScorecard(1, 1, 1, 1, "name", "1.0", 75.0f, 100.0f);

            // 5. insert a screening review
            Review screenReview = createReview(11, screener.getId(), submission1.getId(), scorecard.getId(), true,
                    90.0f);

            this.insertScorecards(conn, new Scorecard[] {scorecard });
            this.insertReviews(conn, new Review[] {screenReview });

            handler.perform(screenPhase, "1001");

            fail("expect PhaseHandlingException");
        } catch (PhaseHandlingException e) {
            // expected
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * Tests the perform with Scheduled statuses. without screener.
     * @throws Exception
     *             not under test.
     */
    public void testPerformToStartWithoutScreener() throws Exception {
        handler = new CheckpointScreeningPhaseHandler();

        try {
            cleanTables();

            Project project = setupPhases();

            // test with scheduled status.
            Phase screeningPhase = project.getAllPhases()[14];
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

            handler.perform(screeningPhase, operator);

            // manually check the email
        } finally {
            cleanTables();
            closeConnection();
        }
    }

}
