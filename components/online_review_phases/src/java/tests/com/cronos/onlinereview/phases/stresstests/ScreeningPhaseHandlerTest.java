/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases.stresstests;

import java.sql.Connection;
import java.util.Date;

import com.cronos.onlinereview.phases.ScreeningPhaseHandler;
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
 * Stress tests for <code>ScreeningPhaseHandler</code>.
 * </p>
 * <p>
 * Since this handler is immutable, so it's naturally thread safe. Here just do benchmark tests.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.3
 */
public class ScreeningPhaseHandlerTest extends StressBaseTest {

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
     * Tests the perform. To stop the phase and check the email.
     *
     * @throws Exception
     *             the exception occurs
     */
    public void testCanPerform_MultipleSubmitters() throws Exception {
        ScreeningPhaseHandler handler = new ScreeningPhaseHandler();

        try {
            cleanTables();

            Project project = setupProjectResourcesNotification("Screening");

            // test with scheduled status.
            Phase screeningPhase = project.getAllPhases()[2];
            screeningPhase.setPhaseStatus(PhaseStatus.OPEN);

            Connection conn = getConnection();

            // insert screener
            Resource screener = createResource(6, screeningPhase.getId(), 1, 3);
            super.insertResources(conn, new Resource[]{screener });
            insertResourceInfo(conn, screener.getId(), 1, "2");

            // create a registration
            Resource resource = createResource(4, 103L, 1, 1);
            super.insertResources(conn, new Resource[]{resource });
            insertResourceInfo(conn, resource.getId(), 1, "4");
            insertResourceInfo(conn, resource.getId(), 2, "ACRush");
            insertResourceInfo(conn, resource.getId(), 4, "3808");
            insertResourceInfo(conn, resource.getId(), 5, "100");

            Upload upload = super.createUpload(1, project.getId(), resource.getId(), 1, 1, "Paramter");
            super.insertUploads(conn, new Upload[]{upload });

            Submission submission = super.createSubmission(1, upload.getId(), 1);
            super.insertSubmissions(conn, new Submission[]{submission });

            Scorecard scorecard = createScorecard(1, 1, 1, 1, "3803", "1.0", 80.0f, 100.0f);

            // insert a screening review
            Review screenReview = createReview(11, screener.getId(), submission.getId(), scorecard.getId(),
                true, 90.0f);

            this.insertScorecards(conn, new Scorecard[]{scorecard });
            this.insertReviews(conn, new Review[]{screenReview });

            startRecord();
            for (int i = 0; i < FIRST_LEVEL; i++) {
                handler.canPerform(screeningPhase);
            }
            endRecord(
                "ScreeningPhaseHandler::canPerform(Phase)--(the phase status is false, the PrimaryScreening is false)",
                FIRST_LEVEL);

        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * Tests the perform. To stop the phase and check the email.
     *
     * @throws Exception
     *             the exception occurs
     */
    public void testCanPerform_PrimaryScreener_reviewNotCommitted() throws Exception {
        ScreeningPhaseHandler handler = new ScreeningPhaseHandler();

        try {
            cleanTables();

            Project project = setupProjectResourcesNotification("Screening");

            // test with scheduled status.
            Phase screeningPhase = project.getAllPhases()[2];
            screeningPhase.setPhaseStatus(PhaseStatus.OPEN);

            Connection conn = getConnection();

            // insert screener
            Resource screener = createResource(6, screeningPhase.getId(), 1, 2);
            super.insertResources(conn, new Resource[]{screener });
            insertResourceInfo(conn, screener.getId(), 1, "2");

            // create a registration
            Resource resource = createResource(4, 103L, 1, 1);
            super.insertResources(conn, new Resource[]{resource });
            insertResourceInfo(conn, resource.getId(), 1, "4");
            insertResourceInfo(conn, resource.getId(), 2, "ACRush");
            insertResourceInfo(conn, resource.getId(), 4, "3808");
            insertResourceInfo(conn, resource.getId(), 5, "100");

            Upload upload = super.createUpload(1, project.getId(), resource.getId(), 1, 1, "Paramter");
            super.insertUploads(conn, new Upload[]{upload });

            Submission submission = super.createSubmission(1, upload.getId(), 1);
            super.insertSubmissions(conn, new Submission[]{submission });

            Scorecard scorecard = createScorecard(1, 1, 1, 1, "name", "1.0", 80.0f, 100.0f);

            // insert a screening review, different submission id
            Review screenReview = createReview(11, screener.getId(), submission.getId(), scorecard.getId(),
                true, 90.0f);

            this.insertScorecards(conn, new Scorecard[]{scorecard });
            this.insertReviews(conn, new Review[]{screenReview });

            startRecord();
            for (int i = 0; i < FIRST_LEVEL; i++) {
                handler.canPerform(screeningPhase);
            }
            endRecord(
                "ScreeningPhaseHandler::canPerform(Phase)--(the phase status is false, the PrimaryScreening is true)",
                FIRST_LEVEL);

        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * Tests the ScreeningPhaseHandler() constructor and canPerform with Scheduled statuses.
     *
     * @throws Exception
     *             not under test.
     */
    public void testCanPerformWithScheduled() throws Exception {
        ScreeningPhaseHandler handler = new ScreeningPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            cleanTables();

            Project project = super.setupPhases();
            Phase[] phases = project.getAllPhases();
            Phase screeningPhase = phases[2];

            // test with scheduled status.
            screeningPhase.setPhaseStatus(PhaseStatus.SCHEDULED);

            // time has passed, but dependency not met.
            screeningPhase.setActualStartDate(new Date());

            // time has passed and dependency met.
            screeningPhase.getAllDependencies()[0].getDependency().setPhaseStatus(PhaseStatus.CLOSED);

            // Set up a active submission for screening phase
            super.setupSubmissionForScreening();

            startRecord();
            for (int i = 0; i < FIRST_LEVEL; i++) {
                handler.canPerform(screeningPhase);
            }
            endRecord("ScreeningPhaseHandler::canPerform(Phase)--(the phase status is true)", FIRST_LEVEL);

        } finally {
            cleanTables();
        }
    }

    /**
     * Tests the perform. To stop the phase and check the email.
     *
     * @throws Exception
     *             the exception occurs
     */
    public void testPerform_stop() throws Exception {
        ScreeningPhaseHandler handler = new ScreeningPhaseHandler();

        Long time = 0L;
        for (int i = 0; i < FIRST_LEVEL; i++) {
            try {
                cleanTables();

                Project project = setupProjectResourcesNotification("Screening");

                // test with scheduled status.
                Phase screeningPhase = project.getAllPhases()[2];
                screeningPhase.setPhaseStatus(PhaseStatus.OPEN);

                String operator = "1001";

                Connection conn = getConnection();

                // insert screener
                Resource screener = createResource(6, screeningPhase.getId(), 1, 3);
                super.insertResources(conn, new Resource[]{screener });
                insertResourceInfo(conn, screener.getId(), 1, "2");

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

                Scorecard scorecard = createScorecard(1, 1, 1, 1, "name", "1.0", 75.0f, 100.0f);

                // insert a screening review
                Review screenReview = createReview(11, screener.getId(), submission.getId(), scorecard
                    .getId(), true, 90.0f);

                this.insertScorecards(conn, new Scorecard[]{scorecard });
                this.insertReviews(conn, new Review[]{screenReview });

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
                scorecard = createScorecard(2, 1, 1, 1, "name", "1.0", 75.0f, 100.0f);

                // 5. insert a screening review
                screenReview = createReview(12, screener.getId(), submission.getId(), scorecard.getId(),
                    true, 70.0f);

                this.insertScorecards(conn, new Scorecard[]{scorecard });
                this.insertReviews(conn, new Review[]{screenReview });

                long timeS = System.currentTimeMillis();
                handler.perform(screeningPhase, operator);
                time = System.currentTimeMillis() - timeS;
            } finally {
                cleanTables();
                closeConnection();
            }
        }

        printResultToFile("ScreeningPhaseHandler::perform(Phase, String)--(the phase status is false) "
            + FIRST_LEVEL + " times took " + time + "ms.");

    }

    /**
     * Tests the perform with Scheduled statuses.
     *
     * @throws Exception
     *             not under test.
     */
    public void testPerformWithScheduled() throws Exception {
        ScreeningPhaseHandler handler = new ScreeningPhaseHandler(ScreeningPhaseHandler.DEFAULT_NAMESPACE);

        // test with scheduled status.
        Phase screeningPhase = createPhase(1, 1, "Scheduled", 2, "Screening");
        String operator = "operator";

        startRecord();
        for (int i = 0; i < FIRST_LEVEL; i++) {
            handler.perform(screeningPhase, operator);
        }
        endRecord("ScreeningPhaseHandler::perform(Phase, String)--(the phase status is true)", FIRST_LEVEL);
    }
}
