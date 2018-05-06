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
 * All tests for FinalReviewPhaseHandler class.
 * <p>
 * Version 1.1 change notes: tests have been added to test the new logic: When Final Review phase is stopping, if
 * final review is approved, an Approval phase is inserted that depends on the end of the finished Final Review
 * phase.
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
public class FinalReviewPhaseHandlerTest extends BaseTest {
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
        FinalReviewPhaseHandler handler = new FinalReviewPhaseHandler(PHASE_HANDLER_NAMESPACE);

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
        FinalReviewPhaseHandler handler = new FinalReviewPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            Phase phase = createPhase(1, 1, "Invalid", 3, "Final Review");
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
        FinalReviewPhaseHandler handler = new FinalReviewPhaseHandler(PHASE_HANDLER_NAMESPACE);

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
        FinalReviewPhaseHandler handler = new FinalReviewPhaseHandler(PHASE_HANDLER_NAMESPACE);

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
        FinalReviewPhaseHandler handler = new FinalReviewPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            Phase phase = createPhase(1, 1, "Invalid", 1, "Final Review");
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
        FinalReviewPhaseHandler handler = new FinalReviewPhaseHandler(PHASE_HANDLER_NAMESPACE);

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
        FinalReviewPhaseHandler handler = new FinalReviewPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            Phase phase = createPhase(1, 1, "Scheduled", 1, "Final Review");
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
        FinalReviewPhaseHandler handler = new FinalReviewPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            Phase phase = createPhase(1, 1, "Scheduled", 1, "Final Review");
            handler.perform(phase, "   ");
            fail("perform() did not throw IllegalArgumentException for empty operator.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Tests the FinalReviewPhaseHandler() constructor and canPerform with Scheduled statuses.
     * @throws Exception not under test.
     */
    public void testCanPerformWithScheduled() throws Exception {
        FinalReviewPhaseHandler handler = new FinalReviewPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            cleanTables();

            Project project = super.setupPhases();
            Phase[] phases = project.getAllPhases();
            Phase finalReviewPhase = phases[9];

            // test with scheduled status.
            finalReviewPhase.setPhaseStatus(PhaseStatus.SCHEDULED);

            // time has not passed, nor dependencies met
            assertFalse("canPerform should have returned false", handler.canPerform(finalReviewPhase).isSuccess());

            // time has passed, but dependency not met.
            finalReviewPhase.setActualStartDate(new Date());
            assertFalse("canPerform should have returned false", handler.canPerform(finalReviewPhase).isSuccess());

            // time has passed and dependency met.
            finalReviewPhase.getAllDependencies()[0].getDependency().setPhaseStatus(PhaseStatus.CLOSED);
            assertTrue("canPerform should have returned true", handler.canPerform(finalReviewPhase).isSuccess());
        } finally {
            cleanTables();
        }
    }

    /**
     * Tests the FinalReviewPhaseHandler() constructor and canPerform with Open statuses.
     * @throws Exception not under test.
     */
    public void testCanPerformHandlerWithOpen() throws Exception {
        FinalReviewPhaseHandler handler = new FinalReviewPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            cleanTables();

            Project project = super.setupPhases();
            Phase[] phases = project.getAllPhases();
            Phase finalReviewPhase = phases[9];

            // change dependency type to F2F
            finalReviewPhase.getAllDependencies()[0].setDependentStart(false);

            // test with open status.
            finalReviewPhase.setPhaseStatus(PhaseStatus.OPEN);

            // time has not passed, dependencies not met
            assertFalse("canPerform should have returned false", handler.canPerform(finalReviewPhase).isSuccess());
        } finally {
            cleanTables();
        }
    }

    /**
     * Tests the perform with Scheduled statuses.
     * @throws Exception not under test.
     */
    public void testPerform() throws Exception {
        FinalReviewPhaseHandler handler = new FinalReviewPhaseHandler(PHASE_HANDLER_NAMESPACE);

        // test with scheduled status.
        Phase finalReviewPhase = createPhase(1, 1, "Scheduled", 2, "Final Review");
        String operator = "operator";
        handler.perform(finalReviewPhase, operator);
    }

    /**
     * Tests the perform with Scheduled status.
     * @throws Exception not under test.
     */
    public void testPerform_start_noFinalReviewer() throws Exception {
        AggregationPhaseHandler aggregatehandler =
            new AggregationPhaseHandler(AggregationPhaseHandler.DEFAULT_NAMESPACE);
        AppealsResponsePhaseHandler aphandler =
            new AppealsResponsePhaseHandler(AppealsResponsePhaseHandler.DEFAULT_NAMESPACE);
        ReviewPhaseHandler reviewPhaseHandler = new ReviewPhaseHandler(ReviewPhaseHandler.DEFAULT_NAMESPACE);
        FinalReviewPhaseHandler handler = new FinalReviewPhaseHandler(FinalReviewPhaseHandler.DEFAULT_NAMESPACE);

        try {
            cleanTables();

            Project project = setupProjectResourcesNotification("Final Review");

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

            Review review = createReview(1000, reviewer.getId(), submission.getId(), sc.getId(), true, 77.0f);
            Review review2 = createReview(3000, reviewer2.getId(), submission.getId(), sc2.getId(), true, 90.0f);
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
            review = createReview(2000, reviewer.getId(), submission.getId(), sc.getId(), true, 77.0f);
            review2 = createReview(4000, reviewer2.getId(), submission.getId(), sc2.getId(), true, 70.0f);
            insertReviews(conn, new Review[] {review, review2 });

            // perform review first before appeals start
            reviewPhaseHandler.perform(reviewPhase, operator);

            aphandler.perform(appealsResponsePhase, operator);

            Phase aggregationPhase = project.getAllPhases()[6];

            // create aggregator
            Resource aggregator = createResource(10, aggregationPhase.getId(), 1, 8);
            super.insertResources(conn, new Resource[] {aggregator });
            insertResourceInfo(conn, aggregator.getId(), 1, "2");

            // start aggregation
            aggregatehandler.perform(aggregationPhase, operator);

            // stop now
            aggregationPhase.setPhaseStatus(PhaseStatus.OPEN);
            aggregatehandler.perform(aggregationPhase, operator);

            // final review phase
            Phase finalReviewPhase = project.getAllPhases()[9];
            handler.perform(finalReviewPhase, operator);

            // manually check the email
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * Tests the perform with Scheduled status.
     * @throws Exception not under test.
     */
    public void testPerform_start_withFinalReviewer() throws Exception {
        AggregationPhaseHandler aggregatehandler =
            new AggregationPhaseHandler(AggregationPhaseHandler.DEFAULT_NAMESPACE);
        AppealsResponsePhaseHandler aphandler =
            new AppealsResponsePhaseHandler(AppealsResponsePhaseHandler.DEFAULT_NAMESPACE);
        ReviewPhaseHandler reviewPhaseHandler = new ReviewPhaseHandler(ReviewPhaseHandler.DEFAULT_NAMESPACE);
        FinalReviewPhaseHandler handler = new FinalReviewPhaseHandler(FinalReviewPhaseHandler.DEFAULT_NAMESPACE);

        try {
            cleanTables();

            Project project = setupProjectResourcesNotification("Final Review");

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

            Review review = createReview(1000, reviewer.getId(), submission.getId(), sc.getId(), true, 77.0f);
            Review review2 = createReview(3000, reviewer2.getId(), submission.getId(), sc2.getId(), true, 90.0f);
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
            review = createReview(2000, reviewer.getId(), submission.getId(), sc.getId(), true, 77.0f);
            review2 = createReview(4000, reviewer2.getId(), submission.getId(), sc2.getId(), true, 70.0f);
            insertReviews(conn, new Review[] {review, review2 });

            // perform review first before appeals start
            reviewPhaseHandler.perform(reviewPhase, operator);

            aphandler.perform(appealsResponsePhase, operator);

            Phase aggregationPhase = project.getAllPhases()[6];

            // create aggregator
            Resource aggregator = createResource(10, aggregationPhase.getId(), 1, 8);
            super.insertResources(conn, new Resource[] {aggregator });
            insertResourceInfo(conn, aggregator.getId(), 1, "2");

            // start aggregation
            aggregatehandler.perform(aggregationPhase, operator);

            // stop now
            aggregationPhase.setPhaseStatus(PhaseStatus.OPEN);
            aggregatehandler.perform(aggregationPhase, operator);

            // final review phase
            Phase finalReviewPhase = project.getAllPhases()[9];

            Resource finalReviwer = createResource(13, finalReviewPhase.getId(), 1, 9);
            super.insertResources(conn, new Resource[] {finalReviwer });
            insertResourceInfo(conn, finalReviwer.getId(), 1, "2");

            handler.perform(finalReviewPhase, operator);

            // manually check the email
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * Tests the perform with Open status.
     * @throws Exception to JUnit.
     * @since 1.1
     * @version 1.2
     */
    public void testPerformWithOpen_rejected() throws Exception {
        FinalReviewPhaseHandler handler = new FinalReviewPhaseHandler();

        try {
            cleanTables();

            Project project = this.setupProjectResourcesNotification("Final Review");
            Phase[] phases = project.getAllPhases();
            Phase finalReviewPhase = phases[9];
            finalReviewPhase.setPhaseStatus(PhaseStatus.OPEN);

            // populate db with required data
            // final reviewer resource
            Resource finalReviewer = createResource(101, finalReviewPhase.getId(), project.getId(), 9);
            Upload frUpload = createUpload(1, project.getId(), finalReviewer.getId(), 4, 1, "parameter");
            Submission frSubmission = createSubmission(1, frUpload.getId(), 1, 1);

            // reviewer resource and related review
            Scorecard scorecard1 = createScorecard(1000, 1, 2, 1, "name", "1.0", 75.0f, 100.0f);
            Review frWorksheet = createReview(111, finalReviewer.getId(), frSubmission.getId(),
                scorecard1.getId(),
                    true, 90.0f);

            // add a rejected comment
            frWorksheet.addComment(createComment(1111, finalReviewer.getId(), "Rejected", 10,
                "Final Review Comment"));

            Connection conn = getConnection();

            // insert records
            insertResources(conn, new Resource[] {finalReviewer });
            insertResourceInfo(conn, finalReviewer.getId(), 1, "2");
            insertUploads(conn, new Upload[] {frUpload });
            insertSubmissions(conn, new Submission[] {frSubmission });
            insertResourceSubmission(conn, finalReviewer.getId(), frSubmission.getId());
            insertScorecards(conn, new Scorecard[] {scorecard1 });
            insertReviews(conn, new Review[] {frWorksheet });
            insertCommentsWithExtraInfo(conn, new long[] {1 }, new long[] {finalReviewer.getId() },
                new long[] {frWorksheet.getId() }, new String[] {"Rejected COmment" }, new long[] {10 },
                new String[] {"Rejected" });
            insertScorecardQuestion(conn, 1, scorecard1.getId());

            // no exception should be thrown.
            String operator = "1001";

            handler.perform(finalReviewPhase, operator);
        } finally {
            closeConnection();
            cleanTables();
        }
    }

    /**
     * Tests the perform with Open status.
     * @throws Exception to JUnit.
     * @since 1.1
     * @version 1.2
     */
    public void testPerformWithOpen_approved() throws Exception {
        FinalReviewPhaseHandler handler = new FinalReviewPhaseHandler(FinalReviewPhaseHandler.DEFAULT_NAMESPACE);

        try {
            cleanTables();

            Project project = this.setupProjectResourcesNotification("Final Review");
            insertProjectInfo(getConnection(), project.getId(), new long[] {41 }, new String[] {"true" });
            Phase[] phases = project.getAllPhases();
            Phase finalReviewPhase = phases[9];
            finalReviewPhase.setPhaseStatus(PhaseStatus.OPEN);

            // populate db with required data
            // final reviewer resource
            Resource finalReviewer = createResource(101, finalReviewPhase.getId(), project.getId(), 9);
            Upload frUpload = createUpload(1, project.getId(), finalReviewer.getId(), 4, 1, "parameter");
            Submission frSubmission = createSubmission(1, frUpload.getId(), 1, 1);

            // reviewer resource and related review
            Scorecard scorecard1 = createScorecard(1, 1, 2, 1, "name", "1.0", 75.0f, 100.0f);
            Review frWorksheet = createReview(11, finalReviewer.getId(), frSubmission.getId(), scorecard1.getId(),
                    true, 90.0f);

            // add a Approved comment
            frWorksheet
                .addComment(createComment(1, finalReviewer.getId(), "Approved", 10, "Final Review Comment"));

            Connection conn = getConnection();

            // insert records
            insertResources(conn, new Resource[] {finalReviewer });
            insertResourceInfo(conn, finalReviewer.getId(), 1, "100001");
            insertUploads(conn, new Upload[] {frUpload });
            insertSubmissions(conn, new Submission[] {frSubmission });
            insertResourceSubmission(conn, finalReviewer.getId(), frSubmission.getId());
            insertScorecards(conn, new Scorecard[] {scorecard1 });
            insertReviews(conn, new Review[] {frWorksheet });
            insertCommentsWithExtraInfo(conn, new long[] {1 }, new long[] {finalReviewer.getId() },
                new long[] {frWorksheet.getId() }, new String[] {"Approved Comment" }, new long[] {10 },
                new String[] {"Approved" });
            insertScorecardQuestion(conn, 1, 1);

            // no exception should be thrown.
            String operator = "1001";

            handler.canPerform(finalReviewPhase);
            handler.perform(finalReviewPhase, operator);

            assertTrue("Approval phase should be inserted.", haveApprovalPhase(conn));

            // manually check the email
        } finally {
            cleanTables();
            closeConnection();
        }
    }
}