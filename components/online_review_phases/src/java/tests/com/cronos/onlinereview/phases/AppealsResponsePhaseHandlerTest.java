/*
 * Copyright (C) 2006-2011 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import java.sql.Connection;
import java.util.Date;

import com.topcoder.management.deliverable.Submission;
import com.topcoder.management.deliverable.Upload;
import com.topcoder.management.phase.PhaseHandlingException;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.review.data.Comment;
import com.topcoder.management.review.data.Item;
import com.topcoder.management.review.data.Review;
import com.topcoder.management.scorecard.data.Scorecard;
import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.PhaseStatus;
import com.topcoder.project.phases.Project;
import com.topcoder.util.config.ConfigManager;

/**
 * All tests for AppealsResponsePhaseHandler class.
 * <P>
 * Version 1.1 change notes: test cases have been updated to test the new logic: When Appeals Response phase is
 * stopping, if no submissions have passed review, a Post-Mortem phase is inserted that depends on the end of the
 * finished Appeals Response phase.
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
public class AppealsResponsePhaseHandlerTest extends BaseTest {

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
        AppealsResponsePhaseHandler handler = new AppealsResponsePhaseHandler(
                        PHASE_HANDLER_NAMESPACE);
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
        AppealsResponsePhaseHandler handler = new AppealsResponsePhaseHandler(
                        PHASE_HANDLER_NAMESPACE);
        try {
            Phase phase = createPhase(1, 1, "Invalid", 3, "Appeals Response");
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
        AppealsResponsePhaseHandler handler = new AppealsResponsePhaseHandler(
                        PHASE_HANDLER_NAMESPACE);
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
        AppealsResponsePhaseHandler handler = new AppealsResponsePhaseHandler(
                        PHASE_HANDLER_NAMESPACE);
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
        AppealsResponsePhaseHandler handler = new AppealsResponsePhaseHandler(
                        PHASE_HANDLER_NAMESPACE);
        try {
            Phase phase = createPhase(1, 1, "Invalid", 1, "Appeals Response");
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
        AppealsResponsePhaseHandler handler = new AppealsResponsePhaseHandler(
                        PHASE_HANDLER_NAMESPACE);
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
        AppealsResponsePhaseHandler handler = new AppealsResponsePhaseHandler(
                        PHASE_HANDLER_NAMESPACE);
        try {
            Phase phase = createPhase(1, 1, "Scheduled", 1, "Appeals Response");
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
        AppealsResponsePhaseHandler handler = new AppealsResponsePhaseHandler(
                        PHASE_HANDLER_NAMESPACE);
        try {
            Phase phase = createPhase(1, 1, "Scheduled", 1, "Appeals Response");
            handler.perform(phase, "   ");
            fail("perform() did not throw IllegalArgumentException for empty operator.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Tests the AppealsResponsePhaseHandler() constructor and canPerform with
     * Scheduled statuses.
     * @throws Exception not under test.
     */
    public void testCanPerformWithScheduled() throws Exception {
        AppealsResponsePhaseHandler handler = new AppealsResponsePhaseHandler(
                        PHASE_HANDLER_NAMESPACE);

        try {
            cleanTables();
            Project project = super.setupPhases();
            Phase[] phases = project.getAllPhases();
            Phase appealsResponsePhase = phases[5];

            // test with scheduled status.
            appealsResponsePhase.setPhaseStatus(PhaseStatus.SCHEDULED);

            // time has not passed, nor dependencies met
            assertFalse("canPerform should have returned false", handler
                            .canPerform(appealsResponsePhase).isSuccess());

            // time has passed, but dependency not met.
            appealsResponsePhase.setActualStartDate(new Date());
            assertFalse("canPerform should have returned false", handler
                            .canPerform(appealsResponsePhase).isSuccess());

            // time has passed and dependency met.
            appealsResponsePhase.getAllDependencies()[0].getDependency()
                            .setPhaseStatus(PhaseStatus.CLOSED);
            assertTrue("canPerform should have returned true", handler
                            .canPerform(appealsResponsePhase).isSuccess());
        } finally {
            cleanTables();
        }
    }

    /**
     * Tests the AppealsResponsePhaseHandler() constructor and canPerform with
     * Open statuses.
     * @throws Exception not under test.
     */
    public void testCanPerformHandlerWithOpen() throws Exception {
        AppealsResponsePhaseHandler handler = new AppealsResponsePhaseHandler(
                        PHASE_HANDLER_NAMESPACE);

        try {
            cleanTables();
            Project project = super.setupPhases();
            Phase[] phases = project.getAllPhases();
            Phase appealsResponsePhase = phases[5];

            // change dependency type to F2F
            appealsResponsePhase.getAllDependencies()[0].setDependentStart(false);

            // test with open status.
            appealsResponsePhase.setPhaseStatus(PhaseStatus.OPEN);

            // time has not passed, dependencies not met
            assertFalse("canPerform should have returned false", handler
                            .canPerform(appealsResponsePhase).isSuccess());
        } finally {
            cleanTables();
        }
    }

    /**
     * Tests the perform with Scheduled statuses.
     * @throws Exception not under test.
     */
    public void testPerformWithScheduled() throws Exception {
        AppealsResponsePhaseHandler handler = new AppealsResponsePhaseHandler(
                        PHASE_HANDLER_NAMESPACE);

        // test with scheduled status.
        Phase appealsResponsePhase = createPhase(1, 1, "Scheduled", 2,
                        "Appeals Response");
        String operator = "operator";
        handler.perform(appealsResponsePhase, operator);
    }

    /**
     * <p>
     * Test the perform method. To start the phase.
     * </p>
     * @throws Exception into JUnit
     * @since 1.2
     */
    public void testPerform_Start() throws Exception {
        AppealsResponsePhaseHandler handler =
            new AppealsResponsePhaseHandler(AppealsResponsePhaseHandler.DEFAULT_NAMESPACE);
        ReviewPhaseHandler reviewPhaseHandler = new ReviewPhaseHandler(ReviewPhaseHandler.DEFAULT_NAMESPACE);

        try {
            cleanTables();

            Project project = setupProjectResourcesNotification("AppealsResponse");

            Phase reviewPhase = project.getAllPhases()[3];
            reviewPhase.setPhaseStatus(PhaseStatus.OPEN);

            // test with scheduled status.
            Phase appealsResponsePhase = project.getAllPhases()[5];

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

            handler.perform(appealsResponsePhase, operator);

            // manually check the email
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * Tests the AppealsResponsePhaseHandler() constructor and perform() with
     * Open status and no winners.
     * @throws Exception to JUnit.
     * @version 1.1
     */
    public void testPerformWithOpenNoWinner() throws Exception {
        AppealsResponsePhaseHandler handler = new AppealsResponsePhaseHandler(
                        PHASE_HANDLER_NAMESPACE);

        try {
            cleanTables();
            Project project = super.setupPhases();
            insertProjectInfo(getConnection(), project.getId(), new long[] {44 }, new String[] {"true" });
            Phase[] phases = project.getAllPhases();
            Phase appealsResponsePhase = phases[5];
            Phase submissionPhase = phases[1];
            Phase reviewPhase = phases[3];

            // test with open status.
            appealsResponsePhase.setPhaseStatus(PhaseStatus.OPEN);

            // insert database records.
            Resource submitter1 = createResource(1, submissionPhase.getId(),
                            project.getId(), 1);
            Resource submitter2 = createResource(2, submissionPhase.getId(),
                            project.getId(), 1);
            Resource reviewer1 = createResource(3, reviewPhase.getId(), project
                            .getId(), 4);
            Upload upload1 = createUpload(1, project.getId(), submitter1
                            .getId(), 1, 1, "parameter");
            Upload upload2 = createUpload(2, project.getId(), submitter2
                            .getId(), 1, 1, "parameter");
            Submission submission1 = createSubmission(1, upload1.getId(), 1, 1);
            Submission submission2 = createSubmission(2, upload2.getId(), 1, 1);
            submission1.setPlacement(new Long(1));
            submission2.setPlacement(new Long(2));
            submission1.setUpload(upload1);
            submission2.setUpload(upload2);

            Scorecard scorecard = createScorecard(1, 1, 2, 1, "name", "1.0",
                            75.0f, 100.0f);
            // assuming 2 reviewers * 2 submissions, so 4 reviews in all are
            // needed
            Review review1 = createReview(1, reviewer1.getId(), submission1
                            .getId(), scorecard.getId(), true, 60.0f);
            Review review2 = createReview(2, reviewer1.getId(), submission1
                            .getId(), scorecard.getId(), true, 70.0f);
            Review review3 = createReview(3, reviewer1.getId(), submission2
                            .getId(), scorecard.getId(), true, 50.0f);
            Review review4 = createReview(4, reviewer1.getId(), submission2
                            .getId(), scorecard.getId(), true, 60.0f);

            Connection conn = getConnection();
            insertResources(conn, new Resource[] {submitter1, submitter2, reviewer1 });

            insertResourceInfo(conn, submitter1.getId(), 1, "111");
            insertResourceInfo(conn, submitter2.getId(), 1, "112");
            insertResourceInfo(conn, reviewer1.getId(), 1, "113");

            insertUploads(conn, new Upload[] {upload1, upload2 });
            insertSubmissions(conn, new Submission[] {submission1, submission2 });
            insertScorecards(conn, new Scorecard[] {scorecard });
            insertReviews(conn, new Review[] {review1, review2, review3, review4 });

            // call perform method
            String operator = "1001";
            handler.perform(appealsResponsePhase, operator);

            // ensure "Final Score" property of submitter resources has been
            // updated
            submission1 = handler.getManagerHelper().getUploadManager().getSubmission(submission1.getId());
            submission2 = handler.getManagerHelper().getUploadManager().getSubmission(submission2.getId());

            assertEquals("Final Score not set", submission1.getFinalScore(),
                            65.0);
            assertEquals("Final Score not set", submission2.getFinalScore(),
                            55.0);

            // ensure submission status has been updated
            assertEquals("Submission Status not set", submission1
                            .getSubmissionStatus().getId(), 3);
            assertEquals("Submission Status not set", submission2
                            .getSubmissionStatus().getId(), 3);

            // ensure no winners for the project
            com.topcoder.management.project.Project project1 = handler
                            .getManagerHelper().getProjectManager().getProject(1);
            assertNull("No winner expected", project1.getProperty("Winner External Reference ID"));
            assertNull("No runner up expected", project1.getProperty("Runner-up External Reference ID"));

            // Added in version 1.1
            assertTrue("The post-mortem phase should be inserted.", havePostMortemPhase(conn));
        } finally {
            closeConnection();
            cleanTables();
        }
    }

    /**
     * Tests the AppealsResponsePhaseHandler() constructor and perform() with
     * Open status and winners.
     * @throws Exception to JUnit.
     * @version 1.1
     */
    public void testPerformWithOpenWithWinner() throws Exception {
        AppealsResponsePhaseHandler handler = new AppealsResponsePhaseHandler(
                        PHASE_HANDLER_NAMESPACE);

        try {
            cleanTables();
            Project project = super.setupPhases();
            Phase[] phases = project.getAllPhases();
            Phase appealsResponsePhase = phases[5];
            Phase submissionPhase = phases[1];
            Phase reviewPhase = phases[3];

            // test with open status.
            appealsResponsePhase.setPhaseStatus(PhaseStatus.OPEN);

            // insert database records.
            Resource submitter1 = createResource(1, submissionPhase.getId(),
                            project.getId(), 1);
            Resource submitter2 = createResource(2, submissionPhase.getId(),
                            project.getId(), 1);
            Resource submitter3 = createResource(3, submissionPhase.getId(),
                            project.getId(), 1);
            Resource reviewer1 = createResource(4, reviewPhase.getId(), project
                            .getId(), 4);
            Upload upload1 = createUpload(1, project.getId(), submitter1
                            .getId(), 1, 1, "parameter");
            Upload upload2 = createUpload(2, project.getId(), submitter2
                            .getId(), 1, 1, "parameter");
            Upload upload3 = createUpload(3, project.getId(), submitter3
                            .getId(), 1, 1, "parameter");
            Submission submission1 = createSubmission(1, upload1.getId(), 1, 1);
            Submission submission2 = createSubmission(2, upload2.getId(), 1, 1);
            Submission submission3 = createSubmission(3, upload3.getId(), 1, 1);
            Scorecard scorecard = createScorecard(1, 1, 2, 1, "name", "1.0", 75.0f, 100.0f);
            // assuming 2 reviewers * 3 submissions, so 6 reviews in all are
            // needed
            Review review1 = createReview(1, reviewer1.getId(), submission1
                            .getId(), scorecard.getId(), true, 90.0f);
            Review review2 = createReview(2, reviewer1.getId(), submission1
                            .getId(), scorecard.getId(), true, 90.0f);
            Review review3 = createReview(3, reviewer1.getId(), submission2
                            .getId(), scorecard.getId(), true, 80.0f);
            Review review4 = createReview(4, reviewer1.getId(), submission2
                            .getId(), scorecard.getId(), true, 84.0f);
            Review review5 = createReview(5, reviewer1.getId(), submission3
                            .getId(), scorecard.getId(), true, 80.0f);
            Review review6 = createReview(6, reviewer1.getId(), submission3
                            .getId(), scorecard.getId(), true, 80.0f);

            Connection conn = getConnection();
            insertResources(conn, new Resource[] {submitter1, submitter2, submitter3, reviewer1 });
            insertResourceInfo(conn, submitter1.getId(), 1, ""
                            + submitter1.getId());
            insertResourceInfo(conn, submitter2.getId(), 1, ""
                            + submitter2.getId());
            insertResourceInfo(conn, submitter3.getId(), 1, ""
                            + submitter3.getId());
            insertResourceInfo(conn, reviewer1.getId(), 1, ""
                            + reviewer1.getId());

            insertUploads(conn, new Upload[] {upload1, upload2, upload3 });
            insertSubmissions(conn, new Submission[] {submission1, submission2, submission3 });
            insertScorecards(conn, new Scorecard[] {scorecard });
            insertReviews(conn, new Review[] {review1, review2, review3, review4, review5, review6 });

            // call perform method
            String operator = "1001";
            handler.canPerform(appealsResponsePhase);
            handler.perform(appealsResponsePhase, operator);

            // ensure "Final Score" and "Placement" property of submitter
            // resources has been updated
            submission1 = handler.getManagerHelper().getUploadManager()
                            .getSubmission(submission1.getId());
            submission2 = handler.getManagerHelper().getUploadManager()
                            .getSubmission(submission2.getId());
            submission3 = handler.getManagerHelper().getUploadManager()
                            .getSubmission(submission3.getId());
            assertEquals("Final Score not set", submission1.getFinalScore(),
                            90.0);
            assertEquals("Placement not set", submission1.getPlacement(),
                            new Long(1));

            assertEquals("Final Score not set", submission2.getFinalScore(),
                            82.0);
            assertEquals("Placement not set", submission2.getPlacement(),
                            new Long(2));

            assertEquals("Final Score not set", submission3.getFinalScore(),
                            80.0);
            assertEquals("Placement not set", submission3.getPlacement(),
                            new Long(3));

            // ensure submission status has been updated to Completed Without
            // Win
            submission3 = handler.getManagerHelper().getUploadManager()
                            .getSubmission(submission3.getId());
            assertEquals("Submission Status not set", submission3
                            .getSubmissionStatus().getId(), 4);

            // ensure winner and runner up for the project
            com.topcoder.management.project.Project project1 = handler
                            .getManagerHelper().getProjectManager().getProject(1);
            assertEquals("winner expected", project1
                            .getProperty("Winner External Reference ID"), "" + submitter1.getId());
            assertEquals("runner up expected", project1
                            .getProperty("Runner-up External Reference ID"), "" + submitter2.getId());
        } finally {
            closeConnection();
            cleanTables();
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
        AppealsResponsePhaseHandler handler =
            new AppealsResponsePhaseHandler();
        ReviewPhaseHandler reviewPhaseHandler = new ReviewPhaseHandler();

        try {
            cleanTables();

            Project project = setupProjectResourcesNotification("AppealsResponse");

            Phase reviewPhase = project.getAllPhases()[3];
            reviewPhase.setPhaseStatus(PhaseStatus.OPEN);

            // test with scheduled status.
            Phase appealsResponsePhase = project.getAllPhases()[5];
            appealsResponsePhase.setPhaseStatus(PhaseStatus.OPEN);
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

            handler.perform(appealsResponsePhase, operator);

            // manually check the email
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * Tests the AppealsResponsePhaseHandler() constructor and canPerform with Open statuses.
     * @throws Exception
     *             not under test.
     */
    public void testCanPerform_NoReviewFound() throws Exception {
        AppealsResponsePhaseHandler handler = new AppealsResponsePhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            cleanTables();
            Project project = super.setupPhases();
            Phase[] phases = project.getAllPhases();
            Phase appealsResponsePhase = phases[5];

            // test with open status.
            appealsResponsePhase.setPhaseStatus(PhaseStatus.OPEN);

            assertTrue("canPerform should have returned true", handler.canPerform(appealsResponsePhase)
                .isSuccess());
        } finally {
            cleanTables();
        }
    }

    /**
     * Tests the AppealsResponsePhaseHandler() constructor and canPerform with Open statuses.
     * @throws Exception
     *             not under test.
     */
    public void testCanPerform_Comments() throws Exception {
        AppealsResponsePhaseHandler handler = new AppealsResponsePhaseHandler();
        ReviewPhaseHandler reviewPhaseHandler = new ReviewPhaseHandler();

        try {
            cleanTables();

            Project project = setupProjectResourcesNotification("AppealsResponse");

            Phase reviewPhase = project.getAllPhases()[3];
            reviewPhase.setPhaseStatus(PhaseStatus.OPEN);

            // test with scheduled status.
            Phase appealsResponsePhase = project.getAllPhases()[5];
            appealsResponsePhase.setPhaseStatus(PhaseStatus.OPEN);
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
            review.addComment(createComment(21, reviewer.getId(), "Good Design", 4, "Appeal"));
            review.addComment(createComment(22, reviewer.getId(), "Good Design", 5, "Appeal Response"));
            review2 = createReview(4, reviewer2.getId(), submission.getId(), sc2.getId(), true, 70.0f);
            insertReviews(conn, new Review[] {review, review2 });

            Item[] reviewItems = new Item[2];
            reviewItems[0] = createReviewItem(11, "Answer 1", review.getId(), 1);
            reviewItems[1] = createReviewItem(12, "Answer 2", review.getId(), 1);

            Comment[] reviewItemComments = new Comment[2];
            reviewItemComments[0] = createComment(11, reviewer.getId(), "Item 1", 4, "Appeal");
            reviewItemComments[1] = createComment(12, reviewer.getId(), "Item 2", 5, "Appeal Response");

            insertComments(conn, new long[] {103, 104 }, new long[] {reviewer.getId(), reviewer.getId() },
                new long[] {
                    review.getId(), review.getId() }, new String[] {"comment 1", "comment 4" }, new long[] {4, 5 });
            insertScorecardQuestion(conn, 1, 1);
            insertReviewItems(conn, reviewItems);
            insertReviewItemComments(conn, reviewItemComments, new long[] {11, 12 });

            // perform review first before appeals start
            reviewPhaseHandler.perform(reviewPhase, operator);

            assertTrue("canPerform should have returned true", handler.canPerform(appealsResponsePhase)
                .isSuccess());
        } finally {
            cleanTables();
        }
    }

    /**
     * Tests the AppealsResponsePhaseHandler() constructor and canPerform with Open statuses.
     * @throws Exception
     *             not under test.
     */
    public void testCanPerform_Comments2() throws Exception {
        AppealsResponsePhaseHandler handler = new AppealsResponsePhaseHandler();
        ReviewPhaseHandler reviewPhaseHandler = new ReviewPhaseHandler();

        try {
            cleanTables();

            Project project = setupProjectResourcesNotification("AppealsResponse");

            Phase reviewPhase = project.getAllPhases()[3];
            reviewPhase.setPhaseStatus(PhaseStatus.OPEN);

            // test with scheduled status.
            Phase appealsResponsePhase = project.getAllPhases()[5];
            appealsResponsePhase.setPhaseStatus(PhaseStatus.OPEN);
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
            review.addComment(createComment(21, reviewer.getId(), "Good Design", 4, "Appeal"));
            review2 = createReview(4, reviewer2.getId(), submission.getId(), sc2.getId(), true, 70.0f);
            insertReviews(conn, new Review[] {review, review2 });

            Item[] reviewItems = new Item[2];
            reviewItems[0] = createReviewItem(11, "Answer 1", review.getId(), 1);
            reviewItems[1] = createReviewItem(12, "Answer 2", review.getId(), 1);

            Comment[] reviewItemComments = new Comment[2];
            reviewItemComments[0] = createComment(11, reviewer.getId(), "Item 1", 4, "Appeal");
            reviewItemComments[1] = createComment(12, reviewer.getId(), "Item 2", 5, "Appeal Response");

            insertComments(conn, new long[] {103 }, new long[] {reviewer.getId() }, new long[] {review.getId() },
                    new String[] {"comment 1" }, new long[] {4 });
            insertScorecardQuestion(conn, 1, 1);
            insertReviewItems(conn, reviewItems);
            insertReviewItemComments(conn, reviewItemComments, new long[] {11, 12 });

            // perform review first before appeals start
            reviewPhaseHandler.perform(reviewPhase, operator);

            assertFalse("canPerform should have returned false", handler.canPerform(appealsResponsePhase)
                .isSuccess());
        } finally {
            cleanTables();
        }
    }

    /**
     * Tests the AppealsResponsePhaseHandler() constructor and perform() with Open status and winners.
     * @throws Exception
     *             to JUnit.
     * @version 1.3
     */
    public void testPerform_noReviews() throws Exception {
        AppealsResponsePhaseHandler handler = new AppealsResponsePhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            cleanTables();
            Project project = super.setupPhases();
            Phase[] phases = project.getAllPhases();
            Phase appealsResponsePhase = phases[5];
            Phase submissionPhase = phases[1];
            Phase reviewPhase = phases[3];

            // test with open status.
            appealsResponsePhase.setPhaseStatus(PhaseStatus.OPEN);

            // insert database records.
            Resource submitter1 = createResource(1, submissionPhase.getId(), project.getId(), 1);
            Resource reviewer1 = createResource(4, reviewPhase.getId(), project.getId(), 4);
            Upload upload1 = createUpload(1, project.getId(), submitter1.getId(), 1, 1, "parameter");
            Submission submission1 = createSubmission(1, upload1.getId(), 1, 1);
            Scorecard scorecard = createScorecard(1, 1, 2, 1, "name", "1.0", 75.0f, 100.0f);

            Connection conn = getConnection();
            insertResources(conn, new Resource[] {submitter1, reviewer1 });
            insertResourceInfo(conn, submitter1.getId(), 1, "" + submitter1.getId());
            insertResourceInfo(conn, reviewer1.getId(), 1, "" + reviewer1.getId());

            insertUploads(conn, new Upload[] {upload1 });
            insertSubmissions(conn, new Submission[] {submission1 });
            insertScorecards(conn, new Scorecard[] {scorecard });

            // call perform method
            String operator = "1001";
            handler.canPerform(appealsResponsePhase);
            handler.perform(appealsResponsePhase, operator);

            fail("expect PhaseHandlingException");
        } catch (PhaseHandlingException e) {
            // expected
        } finally {
            closeConnection();
            cleanTables();
        }
    }

}
