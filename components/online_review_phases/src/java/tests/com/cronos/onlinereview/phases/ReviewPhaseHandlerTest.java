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
 * All tests for ReviewPhaseHandler class.
 * <p>
 * Version 1.2 change notes : since the email-templates and role-supported has been enhanced. The test cases will
 * try to do on that way while for email content, please check it manually.
 * </p>
 * <p>
 * Version 1.6 change notes: Add test cases for testing review process of studio competitions.
 * </p>
 * <p>
 * Version 1.6.1 changes note:
 * <ul>
 * <li>Change some test because the return of canPerform change from boolean to OperationCheckResult.</li>
 * </ul>
 * </p>
 * @author bose_java, waits, TCSDEVELOPER, microsky
 * @version 1.6.1
 */
public class ReviewPhaseHandlerTest extends BaseTest {
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
        ReviewPhaseHandler handler = new ReviewPhaseHandler(PHASE_HANDLER_NAMESPACE);

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
        ReviewPhaseHandler handler = new ReviewPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            Phase phase = createPhase(1, 1, "Invalid", 3, "Review");
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
        ReviewPhaseHandler handler = new ReviewPhaseHandler(PHASE_HANDLER_NAMESPACE);

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
        ReviewPhaseHandler handler = new ReviewPhaseHandler(PHASE_HANDLER_NAMESPACE);

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
        ReviewPhaseHandler handler = new ReviewPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            Phase phase = createPhase(1, 1, "Invalid", 1, "Review");
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
        ReviewPhaseHandler handler = new ReviewPhaseHandler(PHASE_HANDLER_NAMESPACE);

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
        ReviewPhaseHandler handler = new ReviewPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            Phase phase = createPhase(1, 1, "Scheduled", 1, "Review");
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
        ReviewPhaseHandler handler = new ReviewPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            Phase phase = createPhase(1, 1, "Scheduled", 1, "Review");
            handler.perform(phase, "   ");
            fail("perform() did not throw IllegalArgumentException for empty operator.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Tests the ReviewPhaseHandler() constructor and canPerform with Scheduled statuses.
     * @throws Exception not under test.
     */
    public void testCanPerformWithScheduled() throws Exception {
        ReviewPhaseHandler handler = new ReviewPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            cleanTables();

            Project project = super.setupPhases();
            Phase[] phases = project.getAllPhases();
            Phase reviewPhase = phases[3];

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
            super.setupSubmissionForReview();

            assertTrue("canPerform should have returned true", handler.canPerform(reviewPhase).isSuccess());
        } finally {
            cleanTables();
        }
    }

    /**
     * Tests the ReviewPhaseHandler() constructor and canPerform with Open statuses.
     * @throws Exception not under test.
     */
    public void testCanPerformHandlerWithOpen() throws Exception {
        ReviewPhaseHandler handler = new ReviewPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            cleanTables();

            Project project = super.setupPhases();
            Phase[] phases = project.getAllPhases();
            Phase reviewPhase = phases[3];

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
     * Test the perform method. To start the phase. No reviewer has been assigned.
     * </p>
     * @throws Exception into JUnit
     * @since 1.2
     */
    public void testPerform_Start_noReviewer() throws Exception {
        ReviewPhaseHandler handler = new ReviewPhaseHandler(ReviewPhaseHandler.DEFAULT_NAMESPACE);

        try {
            cleanTables();

            Project project = setupProjectResourcesNotification("Review");

            // test with scheduled status.
            Phase reviewPhase = project.getAllPhases()[3];
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
     * @throws Exception into JUnit
     * @since 1.2
     */
    public void testPerform_Start_withReviewer() throws Exception {
        ReviewPhaseHandler handler = new ReviewPhaseHandler();

        try {
            cleanTables();

            Project project = setupProjectResourcesNotification("Review");

            // test with scheduled status.
            Phase reviewPhase = project.getAllPhases()[3];
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

            // insert the reviewers
            Resource reviewer = createResource(6, reviewPhase.getId(), 1, 4);
            super.insertResources(conn, new Resource[] {reviewer });
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
     * @throws Exception into JUnit
     * @since 1.2
     */
    public void testPerform_Stop() throws Exception {
        ReviewPhaseHandler handler = new ReviewPhaseHandler();

        try {
            cleanTables();

            Project project = setupProjectResourcesNotification("Review");

            // test with scheduled status.
            Phase reviewPhase = project.getAllPhases()[3];
            reviewPhase.setPhaseStatus(PhaseStatus.OPEN);
            String operator = "1001";

            Connection conn = getConnection();

            // insert the reviewers
            Resource reviewer = createResource(6, reviewPhase.getId(), 1, 4);
            super.insertResources(conn, new Resource[] {reviewer });
            insertResourceInfo(conn, reviewer.getId(), 1, "2");
            Resource reviewer2 = createResource(7, reviewPhase.getId(), 1, 4);
            super.insertResources(conn, new Resource[] {reviewer2 });
            insertResourceInfo(conn, reviewer2.getId(), 1, "3");

            // create a registration
            Resource resource = createResource(4, 101L, 1, 1);
            super.insertResources(conn, new Resource[] {resource });
            insertResourceInfo(conn, resource.getId(), 1, "4");
            insertResourceInfo(conn, resource.getId(), 2, "ACRush");
            insertResourceInfo(conn, resource.getId(), 4, "3808");
            insertResourceInfo(conn, resource.getId(), 5, "100");

            // insert upload/submission
            Upload upload = super.createUpload(1, project.getId(), resource.getId(), 1, 1, "Paramter");
            super.insertUploads(conn, new Upload[] {upload });

            Submission submission = super.createSubmission(1, upload.getId(), 1, 1);
            super.insertSubmissions(conn, new Submission[] {submission });
            // insertScorecards
            Scorecard sc = this.createScorecard(1, 1, 2, 1, "name", "1.0", 75.0f, 100.0f);
            Scorecard sc2 = this.createScorecard(3, 1, 2, 1, "name", "1.0", 75.0f, 100.0f);
            insertScorecards(conn, new Scorecard[] {sc, sc2 });
            Review review = createReview(1, reviewer.getId(), submission.getId(), sc.getId(), true, 77.0f);
            Review review2 = createReview(3, reviewer2.getId(), submission.getId(), sc2.getId(), true, 90.0f);
            insertReviews(conn, new Review[] {review, review2 });

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
            sc = this.createScorecard(2, 1, 2, 1, "name", "1.0", 75.0f, 100.0f);
            sc2 = this.createScorecard(4, 1, 2, 1, "name", "1.0", 75.0f, 100.0f);
            insertScorecards(conn, new Scorecard[] {sc, sc2 });
            review = createReview(2, reviewer.getId(), submission.getId(), sc.getId(), true, 77.0f);
            review2 = createReview(4, reviewer2.getId(), submission.getId(), sc2.getId(), true, 70.0f);
            insertReviews(conn, new Review[] {review, review2 });

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
     * @throws Exception into JUnit
     * @since 1.6
     */
    public void testPerformToStopForStudio() throws Exception {
        ReviewPhaseHandler handler = new ReviewPhaseHandler();

        try {
            cleanTables();

            Project project = setupPhasesForStudio();

            // test with scheduled status.
            Phase reviewPhase = project.getAllPhases()[3];
            reviewPhase.setPhaseStatus(PhaseStatus.OPEN);
            String operator = "1001";

            Connection conn = getConnection();

            // insert the reviewers
            Resource reviewer = createResource(6, reviewPhase.getId(), 1, 4);
            super.insertResources(conn, new Resource[] {reviewer });
            insertResourceInfo(conn, reviewer.getId(), 1, "2");
            Resource reviewer2 = createResource(7, reviewPhase.getId(), 1, 4);
            super.insertResources(conn, new Resource[] {reviewer2 });
            insertResourceInfo(conn, reviewer2.getId(), 1, "3");

            // create a registration
            Resource resource = createResource(4, 101L, 1, 1);
            super.insertResources(conn, new Resource[] {resource });
            insertResourceInfo(conn, resource.getId(), 1, "4");
            insertResourceInfo(conn, resource.getId(), 2, "ACRush");
            insertResourceInfo(conn, resource.getId(), 4, "3808");
            insertResourceInfo(conn, resource.getId(), 5, "100");

            // insert upload/submission
            Upload upload = super.createUpload(1, project.getId(), resource.getId(), 1, 1, "Parameter");
            super.insertUploads(conn, new Upload[] {upload });

            Submission submission = super.createSubmission(1, upload.getId(), 1, 1);
            super.insertSubmissions(conn, new Submission[] {submission });
            // insertScorecards
            Scorecard sc = this.createScorecard(1, 1, 2, 1, "name", "1.0", 75.0f, 100.0f);
            Scorecard sc2 = this.createScorecard(3, 1, 2, 1, "name", "1.0", 75.0f, 100.0f);
            insertScorecards(conn, new Scorecard[] {sc, sc2 });
            Review review = createReview(1, reviewer.getId(), submission.getId(), sc.getId(), true, 77.0f);
            Review review2 = createReview(3, reviewer2.getId(), submission.getId(), sc2.getId(), true, 90.0f);
            insertReviews(conn, new Review[] {review, review2 });

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
            sc = this.createScorecard(2, 1, 2, 1, "name", "1.0", 75.0f, 100.0f);
            sc2 = this.createScorecard(4, 1, 2, 1, "name", "1.0", 75.0f, 100.0f);
            insertScorecards(conn, new Scorecard[] {sc, sc2 });
            review = createReview(2, reviewer.getId(), submission.getId(), sc.getId(), true, 77.0f);
            review2 = createReview(4, reviewer2.getId(), submission.getId(), sc2.getId(), true, 70.0f);
            insertReviews(conn, new Review[] {review, review2 });

            handler.perform(reviewPhase, operator);

            // manually check the email
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * <p>
     * Test the canPerform method. No reviewer has been assigned.
     * </p>
     * @throws Exception into JUnit
     * @since 1.3
     */
    public void testCanPerform_noReviewer() throws Exception {
        ReviewPhaseHandler handler = new ReviewPhaseHandler();

        try {
            cleanTables();

            Project project = setupProjectResourcesNotification("Review");

            // test with scheduled status.
            Phase reviewPhase = project.getAllPhases()[3];
            reviewPhase.setPhaseStatus(PhaseStatus.OPEN);

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

            assertFalse(handler.canPerform(reviewPhase).isSuccess());

            // manually check the email
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * <p>
     * Test the canPerform method. reviewer has been assigned.
     * </p>
     * @throws Exception into JUnit
     * @since 1.3
     */
    public void testCanPerform_withReviewer() throws Exception {
        ReviewPhaseHandler handler = new ReviewPhaseHandler();

        try {
            cleanTables();

            Project project = setupProjectResourcesNotification("Review");

            // test with scheduled status.
            Phase reviewPhase = project.getAllPhases()[3];
            reviewPhase.setPhaseStatus(PhaseStatus.OPEN);

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

            // insert the reviewers
            Resource reviewer = createResource(6, reviewPhase.getId(), 1, 4);
            super.insertResources(conn, new Resource[] {reviewer });
            insertResourceInfo(conn, reviewer.getId(), 1, "2");

            assertFalse("reviews not match reviewers number.", handler.canPerform(reviewPhase).isSuccess());

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
     * @throws Exception into JUnit
     * @since 1.3
     */
    public void testCanPerform_accuracy() throws Exception {
        ReviewPhaseHandler handler = new ReviewPhaseHandler();

        try {
            cleanTables();

            Project project = setupProjectResourcesNotification("Review");

            // test with scheduled status.
            Phase reviewPhase = project.getAllPhases()[3];
            reviewPhase.setPhaseStatus(PhaseStatus.OPEN);

            Connection conn = getConnection();

            // insert the reviewers
            Resource reviewer = createResource(6, reviewPhase.getId(), 1, 6);
            super.insertResources(conn, new Resource[] {reviewer });
            insertResourceInfo(conn, reviewer.getId(), 1, "2");
            Resource reviewer2 = createResource(7, reviewPhase.getId(), 1, 7);
            super.insertResources(conn, new Resource[] {reviewer2 });
            insertResourceInfo(conn, reviewer2.getId(), 1, "3");

            // create a registration
            Resource resource = createResource(4, 101L, 1, 1);
            super.insertResources(conn, new Resource[] {resource });
            insertResourceInfo(conn, resource.getId(), 1, "4");
            insertResourceInfo(conn, resource.getId(), 2, "ACRush");
            insertResourceInfo(conn, resource.getId(), 4, "3808");
            insertResourceInfo(conn, resource.getId(), 5, "100");

            // insert upload/submission
            Upload upload = super.createUpload(1, project.getId(), resource.getId(), 1, 1, "Paramter");
            super.insertUploads(conn, new Upload[] {upload });

            Submission submission = super.createSubmission(1, upload.getId(), 1, 1);
            super.insertSubmissions(conn, new Submission[] {submission });
            // insertScorecards
            Scorecard sc = this.createScorecard(1, 1, 2, 1, "name", "1.0", 75.0f, 100.0f);
            Scorecard sc2 = this.createScorecard(3, 1, 2, 1, "name", "1.0", 75.0f, 100.0f);
            insertScorecards(conn, new Scorecard[] {sc, sc2 });
            Review review = createReview(1, reviewer.getId(), submission.getId(), sc.getId(), true, 77.0f);
            Review review2 = createReview(3, reviewer2.getId(), submission.getId(), sc2.getId(), true, 90.0f);
            insertReviews(conn, new Review[] {review, review2 });

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
            sc = this.createScorecard(2, 1, 2, 1, "name", "1.0", 75.0f, 100.0f);
            sc2 = this.createScorecard(4, 1, 2, 1, "name", "1.0", 75.0f, 100.0f);
            insertScorecards(conn, new Scorecard[] {sc, sc2 });
            review = createReview(2, reviewer.getId(), submission.getId(), sc.getId(), true, 77.0f);
            review2 = createReview(4, reviewer2.getId(), submission.getId(), sc2.getId(), true, 70.0f);
            insertReviews(conn, new Review[] {review, review2 });

            assertFalse("no test case uploaded", handler.canPerform(reviewPhase).isSuccess());

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
     * @throws Exception into JUnit
     * @since 1.3
     */
    public void testCanPerform_accuracy1() throws Exception {
        ReviewPhaseHandler handler = new ReviewPhaseHandler();

        try {
            cleanTables();

            Project project = setupProjectResourcesNotification("Review");

            // test with scheduled status.
            Phase reviewPhase = project.getAllPhases()[3];
            reviewPhase.setPhaseStatus(PhaseStatus.OPEN);
            reviewPhase.setAttribute("Reviewer Number", "1");

            Connection conn = getConnection();

            // insert the reviewers
            Resource reviewer = createResource(6, reviewPhase.getId(), 1, 6);
            super.insertResources(conn, new Resource[] {reviewer });
            insertResourceInfo(conn, reviewer.getId(), 1, "2");
            Resource reviewer2 = createResource(7, reviewPhase.getId(), 1, 7);
            super.insertResources(conn, new Resource[] {reviewer2 });
            insertResourceInfo(conn, reviewer2.getId(), 1, "3");

            // create a registration
            Resource resource = createResource(4, 101L, 1, 1);
            super.insertResources(conn, new Resource[] {resource });
            insertResourceInfo(conn, resource.getId(), 1, "4");
            insertResourceInfo(conn, resource.getId(), 2, "ACRush");
            insertResourceInfo(conn, resource.getId(), 4, "3808");
            insertResourceInfo(conn, resource.getId(), 5, "100");

            // insert upload/submission
            Upload upload = super.createUpload(1, project.getId(), resource.getId(), 1, 1, "Paramter");
            super.insertUploads(conn, new Upload[] {upload });

            Submission submission = super.createSubmission(1, upload.getId(), 1, 1);
            super.insertSubmissions(conn, new Submission[] {submission });
            // insertScorecards
            Scorecard sc = this.createScorecard(1, 1, 2, 1, "name", "1.0", 75.0f, 100.0f);
            Scorecard sc2 = this.createScorecard(3, 1, 2, 1, "name", "1.0", 75.0f, 100.0f);
            insertScorecards(conn, new Scorecard[] {sc, sc2 });
            Review review = createReview(1, reviewer.getId(), submission.getId(), sc.getId(), true, 77.0f);
            Review review2 = createReview(3, reviewer2.getId(), submission.getId(), sc2.getId(), true, 90.0f);
            insertReviews(conn, new Review[] {review, review2 });

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
            sc = this.createScorecard(2, 1, 2, 1, "name", "1.0", 75.0f, 100.0f);
            sc2 = this.createScorecard(4, 1, 2, 1, "name", "1.0", 75.0f, 100.0f);
            insertScorecards(conn, new Scorecard[] {sc, sc2 });
            review = createReview(2, reviewer.getId(), submission.getId(), sc.getId(), true, 77.0f);
            review2 = createReview(4, reviewer2.getId(), submission.getId(), sc2.getId(), true, 70.0f);
            insertReviews(conn, new Review[] {review, review2 });

            assertFalse("no test case uploaded", handler.canPerform(reviewPhase).isSuccess());

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
     * @throws Exception into JUnit
     * @since 1.3
     */
    public void testCanPerform_accuracy2() throws Exception {
        ReviewPhaseHandler handler = new ReviewPhaseHandler();
        try {
            cleanTables();

            Project project = setupProjectResourcesNotification("Review");

            // test with scheduled status.
            Phase reviewPhase = project.getAllPhases()[3];
            reviewPhase.setPhaseStatus(PhaseStatus.OPEN);

            Connection conn = getConnection();

            // insert the reviewers
            Resource reviewer = createResource(6, reviewPhase.getId(), 1, 6);
            super.insertResources(conn, new Resource[] {reviewer });
            insertResourceInfo(conn, reviewer.getId(), 1, "2");
            Resource reviewer2 = createResource(7, reviewPhase.getId(), 1, 7);
            super.insertResources(conn, new Resource[] {reviewer2 });
            insertResourceInfo(conn, reviewer2.getId(), 1, "3");
            Upload reviewerUpload = super.createUpload(3, project.getId(), reviewer.getId(), 1, 1, "Paramter");
            Upload reviewerUpload2 = super.createUpload(4, project.getId(), reviewer2.getId(), 1, 1, "Paramter");
            super.insertUploads(conn, new Upload[] {reviewerUpload, reviewerUpload2 });

            // create a registration
            Resource resource = createResource(4, 101L, 1, 1);
            super.insertResources(conn, new Resource[] {resource });
            insertResourceInfo(conn, resource.getId(), 1, "4");
            insertResourceInfo(conn, resource.getId(), 2, "ACRush");
            insertResourceInfo(conn, resource.getId(), 4, "3808");
            insertResourceInfo(conn, resource.getId(), 5, "100");

            // insert upload/submission
            Upload upload = super.createUpload(1, project.getId(), resource.getId(), 1, 1, "Paramter");
            super.insertUploads(conn, new Upload[] {upload });

            Submission submission = super.createSubmission(1, upload.getId(), 1, 1);
            super.insertSubmissions(conn, new Submission[] {submission });
            // insertScorecards
            Scorecard sc = this.createScorecard(1, 1, 2, 1, "name", "1.0", 75.0f, 100.0f);
            Scorecard sc2 = this.createScorecard(3, 1, 2, 1, "name", "1.0", 75.0f, 100.0f);
            insertScorecards(conn, new Scorecard[] {sc, sc2 });
            Review review = createReview(1, reviewer.getId(), submission.getId(), sc.getId(), true, 77.0f);
            Review review2 = createReview(3, reviewer2.getId(), submission.getId(), sc2.getId(), true, 90.0f);
            insertReviews(conn, new Review[] {review, review2 });

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
            sc = this.createScorecard(2, 1, 2, 1, "name", "1.0", 75.0f, 100.0f);
            sc2 = this.createScorecard(4, 1, 2, 1, "name", "1.0", 75.0f, 100.0f);
            insertScorecards(conn, new Scorecard[] {sc, sc2 });
            review = createReview(2, reviewer.getId(), submission.getId(), sc.getId(), true, 77.0f);
            review2 = createReview(4, reviewer2.getId(), submission.getId(), sc2.getId(), true, 70.0f);
            insertReviews(conn, new Review[] {review, review2 });

            assertTrue("all test cases uploaded", handler.canPerform(reviewPhase).isSuccess());

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
     * @throws Exception into JUnit
     * @since 1.3
     */
    public void testCanPerform_accuracy3() throws Exception {
        ReviewPhaseHandler handler = new ReviewPhaseHandler();
        try {
            cleanTables();

            Project project = setupProjectResourcesNotification("Review");

            // test with scheduled status.
            Phase reviewPhase = project.getAllPhases()[3];
            reviewPhase.setPhaseStatus(PhaseStatus.OPEN);

            Connection conn = getConnection();

            // insert the reviewers
            Resource reviewer = createResource(6, reviewPhase.getId(), 1, 6);
            super.insertResources(conn, new Resource[] {reviewer });
            insertResourceInfo(conn, reviewer.getId(), 1, "2");
            Resource reviewer2 = createResource(7, reviewPhase.getId(), 1, 7);
            super.insertResources(conn, new Resource[] {reviewer2 });
            insertResourceInfo(conn, reviewer2.getId(), 1, "3");

            // create a registration
            Resource resource = createResource(4, 101L, 1, 1);
            super.insertResources(conn, new Resource[] {resource });
            insertResourceInfo(conn, resource.getId(), 1, "4");
            insertResourceInfo(conn, resource.getId(), 2, "ACRush");
            insertResourceInfo(conn, resource.getId(), 4, "3808");
            insertResourceInfo(conn, resource.getId(), 5, "100");

            // insert upload/submission
            Upload upload = super.createUpload(1, project.getId(), resource.getId(), 1, 1, "Paramter");
            super.insertUploads(conn, new Upload[] {upload });

            Submission submission = super.createSubmission(1, upload.getId(), 1, 1);
            super.insertSubmissions(conn, new Submission[] {submission });
            // insertScorecards
            Scorecard sc = this.createScorecard(1, 1, 2, 1, "name", "1.0", 75.0f, 100.0f);
            Scorecard sc2 = this.createScorecard(3, 1, 2, 1, "name", "1.0", 75.0f, 100.0f);
            insertScorecards(conn, new Scorecard[] {sc, sc2 });
            Review review = createReview(1, reviewer.getId(), submission.getId(), sc.getId(), true, 77.0f);
            Review review2 = createReview(3, reviewer2.getId(), submission.getId(), sc2.getId(), true, 90.0f);
            insertReviews(conn, new Review[] {review, review2 });

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
            sc = this.createScorecard(2, 1, 2, 1, "name", "1.0", 75.0f, 100.0f);
            sc2 = this.createScorecard(4, 1, 2, 1, "name", "1.0", 75.0f, 100.0f);
            insertScorecards(conn, new Scorecard[] {sc, sc2 });
            review = createReview(2, reviewer.getId(), submission.getId(), sc.getId(), true, 77.0f);
            review2 = createReview(4, reviewer2.getId(), submission.getId(), sc2.getId(), true, 70.0f);
            insertReviews(conn, new Review[] {review, review2 });

            assertFalse("no test case uploaded", handler.canPerform(reviewPhase).isSuccess());

            // manually check the email
        } finally {
            cleanTables();
            closeConnection();
        }
    }
}
