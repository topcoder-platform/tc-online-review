/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases.stresstests;

import java.sql.Connection;
import java.util.Date;

import com.cronos.onlinereview.phases.AggregationPhaseHandler;
import com.cronos.onlinereview.phases.AppealsResponsePhaseHandler;
import com.cronos.onlinereview.phases.ReviewPhaseHandler;
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
 * Stress tests for <code>AggregationPhaseHandler</code>.
 * </p>
 * <p>
 * Since this handler is immutable, so it's naturally thread safe. Here just do benchmark tests.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.3
 */
public class AggregationPhaseHandlerTest extends StressBaseTest {



    /**
     * Represents the handler to test.
     */
    private AggregationPhaseHandler handler;

    /**
     * sets up the environment required for test cases for this class.
     *
     * @throws Exception
     *             not under test.
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
        handler = new AggregationPhaseHandler(AggregationPhaseHandler.DEFAULT_NAMESPACE);


    }

    /**
     * cleans up the environment required for test cases for this class.
     *
     * @throws Exception
     *             not under test.
     */
    protected void tearDown() throws Exception {

        super.tearDown();
    }

    /**
     * Test method for canPerform(Phase).
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCanPerformFalse() throws Exception {

        try {
            cleanTables();

            Project project = super.setupPhases();
            Phase[] phases = project.getAllPhases();
            Phase aggregationPhase = phases[6];

            // test with scheduled status.
            aggregationPhase.setPhaseStatus(PhaseStatus.SCHEDULED);

            // time has passed, but dependency not met.
            aggregationPhase.setActualStartDate(new Date());

            // time has passed and dependency met, but no winner
            aggregationPhase.getAllDependencies()[0].getDependency().setPhaseStatus(PhaseStatus.CLOSED);

            startRecord();
            for (int i = 0; i < FIRST_LEVEL; i++) {
                handler.canPerform(aggregationPhase);
            }
            endRecord("AggregationPhaseHandler::canPerform(Phase)--(the return value is false)", FIRST_LEVEL);
            // manually check the email
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * Test can perform2.
     *
     * @throws Exception
     *             the exception occurs
     */
    public void testCanPerformTrue() throws Exception {
        try {
            cleanTables();

            Project project = super.setupPhases();
            Phase[] phases = project.getAllPhases();
            Phase aggregationPhase = phases[6];

            // test with scheduled status.
            aggregationPhase.setPhaseStatus(PhaseStatus.SCHEDULED);

            // time has passed, but dependency not met.
            aggregationPhase.setActualStartDate(new Date());

            // time has passed and dependency met, but no winner
            aggregationPhase.getAllDependencies()[0].getDependency().setPhaseStatus(PhaseStatus.CLOSED);

            // insert winner.
            Connection conn = getConnection();
            insertWinningSubmitter(conn, 1, project.getId());

            Resource aggregator = createResource(3, aggregationPhase.getId(), project.getId(), 8);

            // insert an aggregator
            insertResources(conn, new Resource[]{aggregator });
            super.insertResourceInfo(conn, aggregator.getId(), 1, "1234");

            startRecord();
            for (int i = 0; i < FIRST_LEVEL; i++) {
                handler.canPerform(aggregationPhase);
            }
            endRecord("AggregationPhaseHandler::canPerform(Phase)--(the return value is true)", FIRST_LEVEL);
            // manually check the email
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * Test method for perform(Phase, String).
     *
     * @throws Exception
     *             to JUnit
     */
    public void testPerform_start() throws Exception {


        long time = 0;
        for (int i = 0; i < FIRST_LEVEL; i++) {
            try {
                cleanTables();
                String operator = "1001";
                Phase phase = setData(operator);
                long timeS = System.currentTimeMillis();
                handler.perform(phase, operator);
                time += System.currentTimeMillis() - timeS;
            } finally {
                cleanTables();
                closeConnection();
            }
        }
        printResultToFile("Run AggregationPhaseHandler::perform(Phase, String)--(the phase status is start) "
            + FIRST_LEVEL + " times took " + time + "ms.");
        // manually check the email



    }

    /**
     * Tests the perform with Open status.
     *
     * @throws Exception
     *             not under test.
     */
    public void testPerform_stop() throws Exception {
        AggregationPhaseHandler handler = new AggregationPhaseHandler(
            AggregationPhaseHandler.DEFAULT_NAMESPACE);
        AppealsResponsePhaseHandler aphandler = new AppealsResponsePhaseHandler(
            AppealsResponsePhaseHandler.DEFAULT_NAMESPACE);
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
            super.insertResources(conn, new Resource[]{reviewer });
            insertResourceInfo(conn, reviewer.getId(), 1, "2");

            Resource reviewer2 = createResource(7, reviewPhase.getId(), 1, 4);
            super.insertResources(conn, new Resource[]{reviewer2 });
            insertResourceInfo(conn, reviewer2.getId(), 1, "3");

            // create a registration
            Resource resource = createResource(4, 101L, 1, 1);
            super.insertResources(conn, new Resource[]{resource });
            insertResourceInfo(conn, resource.getId(), 1, "4");
            insertResourceInfo(conn, resource.getId(), 2, "ACRush");
            insertResourceInfo(conn, resource.getId(), 4, "3808");
            insertResourceInfo(conn, resource.getId(), 5, "100");

            // insert upload/submission
            Upload upload = super.createUpload(1, project.getId(), resource.getId(), 1, 1, "Paramter");
            super.insertUploads(conn, new Upload[]{upload });

            Submission submission = super.createSubmission(1, upload.getId(), 1);
            super.insertSubmissions(conn, new Submission[]{submission });

            // insertScorecards
            Scorecard sc = this.createScorecard(1, 1, 2, 1, "name", "1.0", 75.0f, 100.0f);
            Scorecard sc2 = this.createScorecard(3, 1, 2, 1, "name", "1.0", 75.0f, 100.0f);
            insertScorecards(conn, new Scorecard[]{sc, sc2 });

            Review review = createReview(1000, reviewer.getId(), submission.getId(), sc.getId(), true, 77.0f);
            Review review2 = createReview(3000, reviewer2.getId(), submission.getId(), sc2.getId(), true,
                90.0f);
            insertReviews(conn, new Review[]{review, review2 });

            // another register
            resource = createResource(5, 101L, 1, 1);
            super.insertResources(conn, new Resource[]{resource });
            insertResourceInfo(conn, resource.getId(), 1, "5");
            insertResourceInfo(conn, resource.getId(), 2, "UdH-WiNGeR");
            insertResourceInfo(conn, resource.getId(), 4, "3338");
            insertResourceInfo(conn, resource.getId(), 5, "90");
            upload = super.createUpload(2, project.getId(), resource.getId(), 1, 1, "Paramter");
            super.insertUploads(conn, new Upload[]{upload });

            submission = super.createSubmission(2, upload.getId(), 1);
            super.insertSubmissions(conn, new Submission[]{submission });
            sc = this.createScorecard(2, 1, 2, 1, "name", "1.0", 75.0f, 100.0f);
            sc2 = this.createScorecard(4, 1, 2, 1, "name", "1.0", 75.0f, 100.0f);
            insertScorecards(conn, new Scorecard[]{sc, sc2 });
            review = createReview(2000, reviewer.getId(), submission.getId(), sc.getId(), true, 77.0f);
            review2 = createReview(4000, reviewer2.getId(), submission.getId(), sc2.getId(), true, 70.0f);
            insertReviews(conn, new Review[]{review, review2 });

            // perform review first before appeals start
            reviewPhaseHandler.perform(reviewPhase, operator);

            aphandler.perform(appealsResponsePhase, operator);

            Phase aggregationPhase = project.getAllPhases()[6];
            // create aggregator
            Resource aggregator = createResource(10, aggregationPhase.getId(), 1, 8);
            super.insertResources(conn, new Resource[]{aggregator });
            insertResourceInfo(conn, aggregator.getId(), 1, "2");

            startRecord();
            for (int i = 0; i < FIRST_LEVEL; i++) {
                handler.perform(aggregationPhase, operator);
            }
            endRecord("AggregationPhaseHandler::perform(Phase, String)--(the phase status is stop)",
                FIRST_LEVEL);

            // manually check the email
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * Set the data.
     *
     * @return the phase object
     * @throws Exception
     *             the exception occurs
     */
    private Phase setData(String operator) throws Exception {
        AppealsResponsePhaseHandler aphandler = new AppealsResponsePhaseHandler(
            AppealsResponsePhaseHandler.DEFAULT_NAMESPACE);
        ReviewPhaseHandler reviewPhaseHandler = new ReviewPhaseHandler(ReviewPhaseHandler.DEFAULT_NAMESPACE);
        Project project = setupProjectResourcesNotification("Aggregation");

        Phase reviewPhase = project.getAllPhases()[3];
        reviewPhase.setPhaseStatus(PhaseStatus.OPEN);

        // test with scheduled status.
        Phase appealsResponsePhase = project.getAllPhases()[5];
        appealsResponsePhase.setPhaseStatus(PhaseStatus.OPEN);

        Connection conn = getConnection();

        // insert the reviewers
        Resource reviewer = createResource(6, reviewPhase.getId(), 1, 4);
        super.insertResources(conn, new Resource[]{reviewer });
        insertResourceInfo(conn, reviewer.getId(), 1, "2");

        Resource reviewer2 = createResource(7, reviewPhase.getId(), 1, 4);
        super.insertResources(conn, new Resource[]{reviewer2 });
        insertResourceInfo(conn, reviewer2.getId(), 1, "3");

        // create a registration
        Resource resource = createResource(4, 101L, 1, 1);
        super.insertResources(conn, new Resource[]{resource });
        insertResourceInfo(conn, resource.getId(), 1, "4");
        insertResourceInfo(conn, resource.getId(), 2, "ACRush");
        insertResourceInfo(conn, resource.getId(), 4, "3808");
        insertResourceInfo(conn, resource.getId(), 5, "100");

        // insert upload/submission
        Upload upload = super.createUpload(1, project.getId(), resource.getId(), 1, 1, "Paramter");
        super.insertUploads(conn, new Upload[]{upload });

        Submission submission = super.createSubmission(1, upload.getId(), 1);
        super.insertSubmissions(conn, new Submission[]{submission });

        // insertScorecards
        Scorecard sc = this.createScorecard(1, 1, 2, 1, "name", "1.0", 75.0f, 100.0f);
        Scorecard sc2 = this.createScorecard(3, 1, 2, 1, "name", "1.0", 75.0f, 100.0f);
        insertScorecards(conn, new Scorecard[]{sc, sc2 });

        Review review = createReview(1000, reviewer.getId(), submission.getId(), sc.getId(), true, 77.0f);
        Review review2 = createReview(3000, reviewer2.getId(), submission.getId(), sc2.getId(), true, 90.0f);
        insertReviews(conn, new Review[]{review, review2 });

        // another register
        resource = createResource(5, 101L, 1, 1);
        super.insertResources(conn, new Resource[]{resource });
        insertResourceInfo(conn, resource.getId(), 1, "5");
        insertResourceInfo(conn, resource.getId(), 2, "UdH-WiNGeR");
        insertResourceInfo(conn, resource.getId(), 4, "3338");
        insertResourceInfo(conn, resource.getId(), 5, "90");
        upload = super.createUpload(2, project.getId(), resource.getId(), 1, 1, "Paramter");
        super.insertUploads(conn, new Upload[]{upload });

        submission = super.createSubmission(2, upload.getId(), 1);
        super.insertSubmissions(conn, new Submission[]{submission });
        sc = this.createScorecard(2, 1, 2, 1, "name", "1.0", 75.0f, 100.0f);
        sc2 = this.createScorecard(4, 1, 2, 1, "name", "1.0", 75.0f, 100.0f);
        insertScorecards(conn, new Scorecard[]{sc, sc2 });
        review = createReview(2000, reviewer.getId(), submission.getId(), sc.getId(), true, 77.0f);
        review2 = createReview(4000, reviewer2.getId(), submission.getId(), sc2.getId(), true, 70.0f);
        insertReviews(conn, new Review[]{review, review2 });

        // perform review first before appeals start
        reviewPhaseHandler.perform(reviewPhase, operator);

        aphandler.perform(appealsResponsePhase, operator);

        Phase aggregationPhase = project.getAllPhases()[6];
        // create aggregator
        Resource aggregator = createResource(10, aggregationPhase.getId(), 1, 8);
        super.insertResources(conn, new Resource[]{aggregator });
        insertResourceInfo(conn, aggregator.getId(), 1, "2");
        return aggregationPhase;
    }

}
