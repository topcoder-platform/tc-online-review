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
 * All tests for AppealsPhaseHandler class.
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
public class AppealsPhaseHandlerTest extends BaseTest {
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
        AppealsPhaseHandler handler = new AppealsPhaseHandler(PHASE_HANDLER_NAMESPACE);

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
        AppealsPhaseHandler handler = new AppealsPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            Phase phase = createPhase(1, 1, "Invalid", 3, "Appeals");
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
        AppealsPhaseHandler handler = new AppealsPhaseHandler(PHASE_HANDLER_NAMESPACE);

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
        AppealsPhaseHandler handler = new AppealsPhaseHandler(PHASE_HANDLER_NAMESPACE);

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
        AppealsPhaseHandler handler = new AppealsPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            Phase phase = createPhase(1, 1, "Invalid", 1, "Appeals");
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
        AppealsPhaseHandler handler = new AppealsPhaseHandler(PHASE_HANDLER_NAMESPACE);

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
        AppealsPhaseHandler handler = new AppealsPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            Phase phase = createPhase(1, 1, "Scheduled", 1, "Appeals");
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
        AppealsPhaseHandler handler = new AppealsPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            Phase phase = createPhase(1, 1, "Scheduled", 1, "Appeals");
            handler.perform(phase, "   ");
            fail("perform() did not throw IllegalArgumentException for empty operator.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Tests the AppealsPhaseHandler() constructor and canPerform with Scheduled statuses.
     * @throws Exception not under test.
     */
    public void testCanPerformWithScheduled() throws Exception {
        AppealsPhaseHandler handler = new AppealsPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            cleanTables();

            Project project = super.setupPhases();
            Phase[] phases = project.getAllPhases();
            Phase appealsPhase = phases[4];

            // test with scheduled status.
            appealsPhase.setPhaseStatus(PhaseStatus.SCHEDULED);

            // time has not passed, nor dependencies met
            assertFalse("canPerform should have returned false", handler.canPerform(appealsPhase).isSuccess());

            // time has passed, but dependency not met.
            appealsPhase.setActualStartDate(new Date());
            assertFalse("canPerform should have returned false", handler.canPerform(appealsPhase).isSuccess());

            // time has passed and dependency met.
            appealsPhase.getAllDependencies()[0].getDependency().setPhaseStatus(PhaseStatus.CLOSED);
            assertTrue("canPerform should have returned true", handler.canPerform(appealsPhase).isSuccess());
        } finally {
            cleanTables();
        }
    }

    /**
     * Tests the AppealsPhaseHandler() constructor and canPerform with Open statuses.
     * @throws Exception not under test.
     */
    public void testCanPerformHandlerWithOpen() throws Exception {
        AppealsPhaseHandler handler = new AppealsPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            cleanTables();

            Project project = super.setupPhases();
            Phase[] phases = project.getAllPhases();
            Phase appealsPhase = phases[4];

            // test with open status.
            appealsPhase.setPhaseStatus(PhaseStatus.OPEN);

            // change dependency type to F2F
            appealsPhase.getAllDependencies()[0].setDependentStart(false);

            // time has not passed, dependencies not met
            assertFalse("canPerform should have returned false", handler.canPerform(appealsPhase).isSuccess());

            // time has passed, but dependency not met.
            appealsPhase.setActualStartDate(new Date(System.currentTimeMillis() - 1000));
            appealsPhase.setActualEndDate(new Date());
            assertFalse("canPerform should have returned false", handler.canPerform(appealsPhase).isSuccess());

            // time has passed and dependency met
            appealsPhase.getAllDependencies()[0].getDependency().setPhaseStatus(PhaseStatus.CLOSED);
            assertTrue("canPerform should have returned true", handler.canPerform(appealsPhase).isSuccess());
        } finally {
            cleanTables();
        }
    }

    /**
     * Tests the AppealsPhaseHandler() constructor and canPerform with Open statuses.
     * @throws Exception not under test.
     */
    public void testCanPerformCloseEarly() throws Exception {
        AppealsPhaseHandler handler = new AppealsPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            cleanTables();

            Project project = super.setupPhases();
            Phase[] phases = project.getAllPhases();
            Phase appealsPhase = phases[4];

            // test with open status.
            appealsPhase.setPhaseStatus(PhaseStatus.OPEN);

            // time is not passed, all agree to close early.
            assertTrue("canPerform should have returned true", handler.canPerform(appealsPhase).isSuccess());
        } finally {
            cleanTables();
        }
    }

    /**
     * Tests the perform with Scheduled and Open statuses.
     * @throws Exception not under test.
     */
    public void testPerform() throws Exception {
        AppealsPhaseHandler handler = new AppealsPhaseHandler(PHASE_HANDLER_NAMESPACE);

        // test with scheduled status.
        Phase appealsPhase = createPhase(1, 1, "Scheduled", 2, "Appeals");
        String operator = "operator";
        handler.perform(appealsPhase, operator);

        // test with open status
        appealsPhase.setPhaseStatus(PhaseStatus.OPEN);
        handler.perform(appealsPhase, operator);
    }

    /**
     * <p>
     * Test the perform method. To start the phase.
     * </p>
     * @throws Exception into JUnit
     * @since 1.2
     */
    public void testPerform_Start() throws Exception {
        AppealsPhaseHandler handler = new AppealsPhaseHandler(AppealsPhaseHandler.DEFAULT_NAMESPACE);
        ReviewPhaseHandler reviewPhaseHandler = new ReviewPhaseHandler(ReviewPhaseHandler.DEFAULT_NAMESPACE);

        try {
            cleanTables();

            Project project = setupProjectResourcesNotification("Appeals");

            Phase reviewPhase = project.getAllPhases()[3];
            reviewPhase.setPhaseStatus(PhaseStatus.OPEN);

            // test with scheduled status.
            Phase appealsPhase = project.getAllPhases()[4];
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

            // perform review first before appeals start
            reviewPhaseHandler.perform(reviewPhase, operator);

            handler.perform(appealsPhase, operator);

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
        AppealsPhaseHandler handler = new AppealsPhaseHandler();
        ReviewPhaseHandler reviewPhaseHandler = new ReviewPhaseHandler(ReviewPhaseHandler.DEFAULT_NAMESPACE);

        try {
            cleanTables();

            Project project = setupProjectResourcesNotification("Appeals");

            Phase reviewPhase = project.getAllPhases()[3];
            reviewPhase.setPhaseStatus(PhaseStatus.OPEN);

            // test with scheduled status.
            Phase appealsPhase = project.getAllPhases()[4];
            appealsPhase.setPhaseStatus(PhaseStatus.OPEN);

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

            // perform review first before appeals start
            reviewPhaseHandler.perform(reviewPhase, operator);

            handler.perform(appealsPhase, operator);

            // manually check the email
        } finally {
            cleanTables();
            closeConnection();
        }
    }
}
