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
 * All tests for AggregationPhaseHandler class.
 * <p>
 * Version 1.2 change notes : since the email-templates and role-supported has been enhanced. The test cases will
 * try to do on that way while for email content, please check it manually.
 * </p>
 * <p>
 * Version 1.4 change notes : add some new test cases.
 * </p>
 * <p>
 * Version 1.6.1 changes note:
 * <ul>
 * <li>Change some test because the return of canPerform change from boolean to OperationCheckResult.</li>
 * </ul>
 * </p>
 * @author bose_java, waits, myxgyy, microsky
 * @version 1.6.1
 */
public class AggregationPhaseHandlerTest extends BaseTest {
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
     * Tests no-arg constructor.
     * @throws Exception not under test.
     */
    public void testCtor() throws Exception {
        AggregationPhaseHandler handler = new AggregationPhaseHandler();

        assertNotNull("instance should not be null", handler);
    }

    /**
     * Tests canPerform(Phase) with null phase.
     * @throws Exception not under test.
     */
    public void testCanPerform() throws Exception {
        AggregationPhaseHandler handler = new AggregationPhaseHandler(PHASE_HANDLER_NAMESPACE);

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
        AggregationPhaseHandler handler = new AggregationPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            Phase phase = createPhase(1, 1, "Invalid", 3, "Aggregation");
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
        AggregationPhaseHandler handler = new AggregationPhaseHandler(PHASE_HANDLER_NAMESPACE);

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
        AggregationPhaseHandler handler = new AggregationPhaseHandler(PHASE_HANDLER_NAMESPACE);

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
        AggregationPhaseHandler handler = new AggregationPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            Phase phase = createPhase(1, 1, "Invalid", 1, "Aggregation");
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
        AggregationPhaseHandler handler = new AggregationPhaseHandler(PHASE_HANDLER_NAMESPACE);

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
        AggregationPhaseHandler handler = new AggregationPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            Phase phase = createPhase(1, 1, "Scheduled", 1, "Aggregation");
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
        AggregationPhaseHandler handler = new AggregationPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            Phase phase = createPhase(1, 1, "Scheduled", 1, "Aggregation");
            handler.perform(phase, "   ");
            fail("perform() did not throw IllegalArgumentException for empty operator.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Tests the AggregationPhaseHandler() constructor and canPerform with Scheduled statuses.
     * @throws Exception not under test.
     */
    public void testCanPerformWithScheduled() throws Exception {
        AggregationPhaseHandler handler = new AggregationPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            cleanTables();

            Project project = super.setupPhases();
            Phase[] phases = project.getAllPhases();
            Phase aggregationPhase = phases[6];

            // test with scheduled status.
            aggregationPhase.setPhaseStatus(PhaseStatus.SCHEDULED);

            // time has not passed, nor dependencies met
            assertFalse("canPerform should have returned false", handler.canPerform(aggregationPhase).isSuccess());

            // time has passed, but dependency not met.
            aggregationPhase.setActualStartDate(new Date());
            assertFalse("canPerform should have returned false", handler.canPerform(aggregationPhase).isSuccess());

            // time has passed and dependency met, but no winner
            aggregationPhase.getAllDependencies()[0].getDependency().setPhaseStatus(PhaseStatus.CLOSED);
            assertFalse("canPerform should have returned false", handler.canPerform(aggregationPhase).isSuccess());

            // insert winner.
            Connection conn = getConnection();
            insertWinningSubmitter(conn, 1, project.getId());

            Resource aggregator = createResource(3, aggregationPhase.getId(), project.getId(), 8);

            // insert an aggregator
            insertResources(conn, new Resource[] {aggregator });
            super.insertResourceInfo(conn, aggregator.getId(), 1, "1234");

            assertTrue("canPerform should have returned true", handler.canPerform(aggregationPhase).isSuccess());
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * Tests the AggregationPhaseHandler() constructor and canPerform with Open statuses.
     * @throws Exception not under test.
     */
    public void testCanPerformHandlerWithOpen() throws Exception {
        AggregationPhaseHandler handler = new AggregationPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            cleanTables();

            Project project = super.setupPhases();
            Phase[] phases = project.getAllPhases();
            Phase aggregationPhase = phases[6];

            // change dependency type to F2F
            aggregationPhase.getAllDependencies()[0].setDependentStart(false);

            // test with open status.
            aggregationPhase.setPhaseStatus(PhaseStatus.OPEN);

            // time has not passed, dependencies not met
            assertFalse("canPerform should have returned false", handler.canPerform(aggregationPhase).isSuccess());
        } finally {
            cleanTables();
        }
    }

    /**
     * Tests the AggregationPhaseHandler() constructor and canPerform with Open statuses.
     * @throws Exception
     *             not under test.
     */
    public void testCanPerformHandlerWithOpen1() throws Exception {
        AggregationPhaseHandler handler = new AggregationPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            cleanTables();

            Project project = super.setupPhases();
            Phase[] phases = project.getAllPhases();
            Phase aggregationPhase = phases[6];

            // test with open status.
            aggregationPhase.setPhaseStatus(PhaseStatus.OPEN);

            // time has not passed, dependencies not met
            assertFalse("canPerform should have returned false", handler.canPerform(aggregationPhase).isSuccess());
        } finally {
            cleanTables();
        }
    }

    /**
     * Tests the perform with Open statuses.
     * @throws Exception not under test.
     */
    public void testPerform() throws Exception {
        AggregationPhaseHandler handler = new AggregationPhaseHandler(PHASE_HANDLER_NAMESPACE);

        // test with open status.
        Phase aggregationPhase = createPhase(1, 1, "Open", 2, "Aggregation");
        String operator = "operator";
        handler.perform(aggregationPhase, operator);
    }

    /**
     * Tests the perform with Scheduled status.
     * @throws Exception not under test.
     */
    public void testPerform_start() throws Exception {
        AggregationPhaseHandler handler = new AggregationPhaseHandler(AggregationPhaseHandler.DEFAULT_NAMESPACE);
        AppealsResponsePhaseHandler aphandler =
            new AppealsResponsePhaseHandler(AppealsResponsePhaseHandler.DEFAULT_NAMESPACE);
        ReviewPhaseHandler reviewPhaseHandler = new ReviewPhaseHandler(ReviewPhaseHandler.DEFAULT_NAMESPACE);

        try {
            cleanTables();

            Project project = setupProjectResourcesNotification("Aggregation");

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

            handler.perform(aggregationPhase, operator);

            // manually check the email
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * Tests the perform with Scheduled status. It tests the case the previous aggregation exists.
     * @throws Exception not under test.
     */
    public void testPerform_start_PreviousAggregationExists() throws Exception {
        AggregationPhaseHandler handler = new AggregationPhaseHandler(AggregationPhaseHandler.DEFAULT_NAMESPACE);
        AppealsResponsePhaseHandler aphandler =
            new AppealsResponsePhaseHandler(AppealsResponsePhaseHandler.DEFAULT_NAMESPACE);
        ReviewPhaseHandler reviewPhaseHandler = new ReviewPhaseHandler(ReviewPhaseHandler.DEFAULT_NAMESPACE);

        try {
            cleanTables();

            Project project = setupPhasesForDoubleAggregation();

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

            handler.perform(aggregationPhase, operator);

            aggregationPhase = project.getAllPhases()[7];
            // create aggregator
            aggregator = createResource(11, aggregationPhase.getId(), 1, 8);
            super.insertResources(conn, new Resource[] {aggregator });
            insertResourceInfo(conn, aggregator.getId(), 1, "2");
            handler.perform(aggregationPhase, operator);
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * Tests the perform with Scheduled status. No aggregator exists, PhaseHandlingException expected.
     * @throws Exception not under test.
     * @since 1.4
     */
    public void testPerformStart_NoAggregator() throws Exception {
        AggregationPhaseHandler handler = new AggregationPhaseHandler(AggregationPhaseHandler.DEFAULT_NAMESPACE);
        AppealsResponsePhaseHandler aphandler =
            new AppealsResponsePhaseHandler(AppealsResponsePhaseHandler.DEFAULT_NAMESPACE);
        ReviewPhaseHandler reviewPhaseHandler = new ReviewPhaseHandler(ReviewPhaseHandler.DEFAULT_NAMESPACE);

        try {
            cleanTables();

            Project project = setupProjectResourcesNotification("Aggregation");

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

            handler.perform(aggregationPhase, operator);

            fail("should have thrown PhaseHandlingException");
        } catch (PhaseHandlingException e) {
            // pass
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * Tests the perform with Open status.
     * @throws Exception not under test.
     */
    public void testPerform_stop() throws Exception {
        AggregationPhaseHandler handler = new AggregationPhaseHandler(AggregationPhaseHandler.DEFAULT_NAMESPACE);
        AppealsResponsePhaseHandler aphandler =
            new AppealsResponsePhaseHandler(AppealsResponsePhaseHandler.DEFAULT_NAMESPACE);
        ReviewPhaseHandler reviewPhaseHandler = new ReviewPhaseHandler(ReviewPhaseHandler.DEFAULT_NAMESPACE);

        try {
            cleanTables();

            Project project = setupProjectResourcesNotification("Aggregation");

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
            handler.perform(aggregationPhase, operator);

            // stop now
            aggregationPhase.setPhaseStatus(PhaseStatus.OPEN);
            handler.perform(aggregationPhase, operator);

            // manually check the email
        } finally {
            cleanTables();
            closeConnection();
        }
    }

}
