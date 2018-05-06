/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases.stresstests;

import java.sql.Connection;
import java.util.Date;

import com.cronos.onlinereview.phases.AppealsResponsePhaseHandler;
import com.cronos.onlinereview.phases.ReviewPhaseHandler;
import com.topcoder.management.deliverable.Submission;
import com.topcoder.management.deliverable.Upload;
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
 * AppealsResponsePhaseHandlerTest.
 *
 * @author TCSDEVELOPER
 * @version 1.3
 */
public class AppealsResponsePhaseHandlerTest extends StressBaseTest {

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

    }

    /**
     * Clean up the environment.
     *
     * @throws Exception
     *             to JUnit
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Tests the AppealsResponsePhaseHandler() constructor and canPerform with Open statuses.
     *
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

            Review review = createReview(1, reviewer.getId(), submission.getId(), sc.getId(), true, 77.0f);
            Review review2 = createReview(3, reviewer2.getId(), submission.getId(), sc2.getId(), true, 90.0f);
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
            review = createReview(2, reviewer.getId(), submission.getId(), sc.getId(), true, 77.0f);
            review.addComment(createComment(21, reviewer.getId(), "Good Design", 4, "Appeal"));
            review.addComment(createComment(22, reviewer.getId(), "Good Design", 5, "Appeal Response"));
            review2 = createReview(4, reviewer2.getId(), submission.getId(), sc2.getId(), true, 70.0f);
            insertReviews(conn, new Review[]{review, review2 });

            Item[] reviewItems = new Item[2];
            reviewItems[0] = createReviewItem(11, "Answer 1", review.getId(), 1);
            reviewItems[1] = createReviewItem(12, "Answer 2", review.getId(), 1);

            Comment[] reviewItemComments = new Comment[2];
            reviewItemComments[0] = createComment(11, reviewer.getId(), "Item 1", 4, "Appeal");
            reviewItemComments[1] = createComment(12, reviewer.getId(), "Item 2", 5, "Appeal Response");

            insertComments(conn, new long[]{103, 104 }, new long[]{reviewer.getId(), reviewer.getId() },
                new long[]{review.getId(), review.getId() }, new String[]{"comment 1", "comment 4" },
                new long[]{4, 5 });
            insertScorecardQuestion(conn, 1, 1);
            insertReviewItems(conn, reviewItems);
            insertReviewItemComments(conn, reviewItemComments, new long[]{11, 12 });

            // perform review first before appeals start
            reviewPhaseHandler.perform(reviewPhase, operator);

            startRecord();
            for (int i = 0; i < FIRST_LEVEL; i++) {
                handler.canPerform(appealsResponsePhase);
            }
            endRecord("AppealsResponsePhaseHandler::canPerform(Phase)--(the phase status is false)",
                FIRST_LEVEL);
        } finally {
            cleanTables();
        }
    }

    /**
     * Tests the AppealsResponsePhaseHandler() constructor and canPerform with
     * Scheduled statuses.
     *
     * @throws Exception
     *             the exception occurs
     */
    public void testCanPerformWithScheduled() throws Exception {
        AppealsResponsePhaseHandler handler = new AppealsResponsePhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            cleanTables();
            Project project = super.setupPhases();
            Phase[] phases = project.getAllPhases();
            Phase appealsResponsePhase = phases[5];

            // test with scheduled status.
            appealsResponsePhase.setPhaseStatus(PhaseStatus.SCHEDULED);

            // time has passed, but dependency not met.
            appealsResponsePhase.setActualStartDate(new Date());

            // time has passed and dependency met.
            appealsResponsePhase.getAllDependencies()[0].getDependency().setPhaseStatus(PhaseStatus.CLOSED);

            startRecord();
            for (int i = 0; i < FIRST_LEVEL * 5; i++) {
                handler.canPerform(appealsResponsePhase);
            }
            endRecord("AppealsResponsePhaseHandler::canPerform(Phase)--(the phase status is true)",
                FIRST_LEVEL * 5);
        } finally {
            cleanTables();
        }
    }

    /**
     * <p>
     * Test the perform method. To start the phase.
     * </p>
     *
     * @throws Exception
     *             the exception occurs
     */
    public void testPerform_Start() throws Exception {
        AppealsResponsePhaseHandler handler = new AppealsResponsePhaseHandler(
            AppealsResponsePhaseHandler.DEFAULT_NAMESPACE);
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
            super.insertUploads(conn, new Upload[]{upload});

            Submission submission = super.createSubmission(1, upload.getId(), 1);
            super.insertSubmissions(conn, new Submission[]{submission});

            // insertScorecards
            Scorecard sc = this.createScorecard(1, 1, 2, 1, "name", "1.0", 75.0f, 100.0f);
            Scorecard sc2 = this.createScorecard(3, 1, 2, 1, "name", "1.0", 75.0f, 100.0f);
            insertScorecards(conn, new Scorecard[]{sc, sc2 });

            Review review = createReview(1, reviewer.getId(), submission.getId(), sc.getId(), true, 77.0f);
            Review review2 = createReview(3, reviewer2.getId(), submission.getId(), sc2.getId(), true, 90.0f);
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
            review = createReview(2, reviewer.getId(), submission.getId(), sc.getId(), true, 77.0f);
            review2 = createReview(4, reviewer2.getId(), submission.getId(), sc2.getId(), true, 70.0f);
            insertReviews(conn, new Review[]{review, review2 });

            // perform review first before appeals start
            reviewPhaseHandler.perform(reviewPhase, operator);

            startRecord();
            for (int i = 0; i < 2; i++) {
                handler.perform(appealsResponsePhase, operator);
            }
            endRecord("AppealsResponsePhaseHandler::perform(Phase, String)--(the phase status is true)",
                FIRST_LEVEL);

            // manually check the email
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * Tests the AppealsResponsePhaseHandler() constructor and perform() with
     * Open status and no winners.
     *
     * @throws Exception
     *             the exception occurs
     */
    public void testPerformWithOpenNoWinner() throws Exception {
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
            Resource submitter2 = createResource(2, submissionPhase.getId(), project.getId(), 1);
            Resource reviewer1 = createResource(3, reviewPhase.getId(), project.getId(), 4);
            Upload upload1 = createUpload(1, project.getId(), submitter1.getId(), 1, 1, "parameter");
            Upload upload2 = createUpload(2, project.getId(), submitter2.getId(), 1, 1, "parameter");
            Submission submission1 = createSubmission(1, upload1.getId(), 1);
            Submission submission2 = createSubmission(2, upload2.getId(), 1);
            submission1.setPlacement(new Long(1));
            submission2.setPlacement(new Long(2));
            submission1.setUpload(upload1);
            submission2.setUpload(upload2);

            Scorecard scorecard = createScorecard(1, 1, 2, 1, "name", "1.0", 75.0f, 100.0f);
            // assuming 2 reviewers * 2 submissions, so 4 reviews in all are
            // needed
            Review review1 = createReview(1, reviewer1.getId(), submission1.getId(), scorecard.getId(), true,
                60.0f);
            Review review2 = createReview(2, reviewer1.getId(), submission1.getId(), scorecard.getId(), true,
                70.0f);
            Review review3 = createReview(3, reviewer1.getId(), submission2.getId(), scorecard.getId(), true,
                50.0f);
            Review review4 = createReview(4, reviewer1.getId(), submission2.getId(), scorecard.getId(), true,
                60.0f);

            Connection conn = getConnection();
            insertResources(conn, new Resource[]{submitter1, submitter2, reviewer1 });

            insertResourceInfo(conn, submitter1.getId(), 1, "111");
            insertResourceInfo(conn, submitter2.getId(), 1, "112");
            insertResourceInfo(conn, reviewer1.getId(), 1, "113");

            insertUploads(conn, new Upload[]{upload1, upload2 });
            insertSubmissions(conn, new Submission[]{submission1, submission2 });
            insertScorecards(conn, new Scorecard[]{scorecard });
            insertReviews(conn, new Review[]{review1, review2, review3, review4 });

            // call perform method
            String operator = "1001";

            startRecord();
            for (int i = 0; i < 2; i++) {
                handler.perform(appealsResponsePhase, operator);
            }
            endRecord("AppealsResponsePhaseHandler::perform(Phase, String)--(the phase status is false)",
                FIRST_LEVEL);

        } finally {
            closeConnection();
            cleanTables();
        }
    }
}
