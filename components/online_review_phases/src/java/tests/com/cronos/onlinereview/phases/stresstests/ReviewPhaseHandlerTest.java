/*
s * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases.stresstests;

import java.sql.Connection;
import java.util.Date;

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
 * Stress tests for <code>ReviewPhaseHandler</code>.
 * </p>
 * <p>
 * Since this handler is immutable, so it's naturally thread safe. Here just do benchmark tests.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.3
 */
public class ReviewPhaseHandlerTest extends StressBaseTest {

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
     * <p>
     * Test the canPerform method.
     * </p>
     *
     * @throws Exception
     *             the exception occurs
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
            super.insertResources(conn, new Resource[]{reviewer });
            insertResourceInfo(conn, reviewer.getId(), 1, "2");
            Resource reviewer2 = createResource(7, reviewPhase.getId(), 1, 7);
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
            review2 = createReview(4, reviewer2.getId(), submission.getId(), sc2.getId(), true, 70.0f);
            insertReviews(conn, new Review[]{review, review2 });

            startRecord();
            for (int i = 0; i < FIRST_LEVEL; i++) {
                handler.canPerform(reviewPhase);
            }
            endRecord("ReviewPhaseHandler::canPerform(Phase)--(the phase status is false)", FIRST_LEVEL);
            // manually check the email
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * Tests the ReviewPhaseHandler() constructor and canPerform with Scheduled statuses.
     *
     * @throws Exception
     *             not under test.
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

            // time has passed, but dependency not met.
            reviewPhase.setActualStartDate(new Date());

            // time has passed and dependency met.
            reviewPhase.getAllDependencies()[0].getDependency().setPhaseStatus(PhaseStatus.CLOSED);

            // And we set up a active submission in screening phase
            super.setupSubmissionForReview();

            startRecord();
            for (int i = 0; i < FIRST_LEVEL; i++) {
                handler.canPerform(reviewPhase);
            }
            endRecord("ReviewPhaseHandler::canPerform(Phase)--(the phase status is true)", FIRST_LEVEL);
        } finally {
            cleanTables();
        }
    }

    /**
     * <p>
     * Test the perform method. To start the phase. reviewer has been assigned.
     * </p>
     *
     * @throws Exception
     *             the exception occurs
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
            super.insertResources(conn, new Resource[]{resource });
            insertResourceInfo(conn, resource.getId(), 1, "4");
            insertResourceInfo(conn, resource.getId(), 2, "ACRush");
            insertResourceInfo(conn, resource.getId(), 4, "3808");
            insertResourceInfo(conn, resource.getId(), 5, "100");

            Upload upload = super.createUpload(1, project.getId(), resource.getId(), 1, 1, "Paramter");
            super.insertUploads(conn, new Upload[]{upload });

            Submission submission = super.createSubmission(1, upload.getId(), 1);
            super.insertSubmissions(conn, new Submission[]{submission });

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

            // insert the reviewers
            Resource reviewer = createResource(6, reviewPhase.getId(), 1, 4);
            super.insertResources(conn, new Resource[]{reviewer });
            insertResourceInfo(conn, reviewer.getId(), 1, "2");

            startRecord();
            for (int i = 0; i < FIRST_LEVEL; i++) {
                handler.perform(reviewPhase, operator);
            }
            endRecord("ReviewPhaseHandler::perform(Phase, String)--(the phase status is true)", FIRST_LEVEL);
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
     *
     * @throws Exception
     *             the exception occurs
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
            review2 = createReview(4, reviewer2.getId(), submission.getId(), sc2.getId(), true, 70.0f);
            insertReviews(conn, new Review[]{review, review2 });

            startRecord();
            for (int i = 0; i < FIRST_LEVEL; i++) {
                handler.perform(reviewPhase, operator);
            }
            endRecord("ReviewPhaseHandler::perform(Phase, String)--(the phase status is true)", FIRST_LEVEL);

            // manually check the email
        } finally {
            cleanTables();
            closeConnection();
        }
    }

}
