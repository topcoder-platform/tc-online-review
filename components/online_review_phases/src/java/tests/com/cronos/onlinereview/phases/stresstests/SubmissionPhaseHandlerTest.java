/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases.stresstests;

import java.sql.Connection;
import java.util.Date;

import com.cronos.onlinereview.phases.SubmissionPhaseHandler;
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
 * Stress tests for <code>SubmissionPhaseHandler</code>.
 * </p>
 * <p>
 * Since this handler is immutable, so it's naturally thread safe. Here just do benchmark tests.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.3
 */
public class SubmissionPhaseHandlerTest extends StressBaseTest {

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
     * Tests the SubmissionPhaseHandler() constructor and canPerform with Scheduled statuses.
     *
     * @throws Exception
     *             not under test.
     */
    public void testCanPerformHandlerWithOpen() throws Exception {
        SubmissionPhaseHandler handler = new SubmissionPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            cleanTables();

            Project project = super.setupPhases();
            Phase[] phases = project.getAllPhases();
            Phase submissionPhase = phases[1];

            // test with open status.
            submissionPhase.setPhaseStatus(PhaseStatus.OPEN);

            // change dependency type to F2F
            submissionPhase.getAllDependencies()[0].setDependentStart(false);

            // time has passed, but dependency not met.
            submissionPhase.setActualStartDate(new Date(System.currentTimeMillis() - 1000000));
            submissionPhase.setActualEndDate(new Date());

            // time has passed and dependency met, reviews passed.
            submissionPhase.getAllDependencies()[0].getDependency().setPhaseStatus(PhaseStatus.CLOSED);
            submissionPhase.setAttribute("Manual Screening", "Yes");
            submissionPhase.setAttribute("Submission Number", "1");

            Connection conn = getConnection();

            Phase screeningPhase = phases[2];
            long screeningPhaseId = screeningPhase.getId();
            Resource submitter = super.createResource(1, submissionPhase.getId(), 1, 1);
            Resource reviewer1 = super.createResource(2, screeningPhaseId, 1, 2);
            Resource reviewer2 = super.createResource(3, screeningPhaseId, 1, 3);

            Upload upload = createUpload(1, 1, submitter.getId(), 1, 1, "parameter");
            Submission submission = createSubmission(1, 1, 1);
            Scorecard scorecard = createScorecard(1, 1, 1, 1, "name", "1.0", 75.0f, 100.0f);
            Review review = createReview(1, reviewer1.getId(), submission.getId(), scorecard.getId(), true,
                80.0f);

            insertResources(conn, new Resource[]{submitter, reviewer1, reviewer2 });
            insertResourceInfo(conn, submitter.getId(), 1, "11111");
            insertResourceInfo(conn, reviewer1.getId(), 1, "11112");
            insertResourceInfo(conn, reviewer2.getId(), 1, "11113");
            insertUploads(conn, new Upload[]{upload });
            insertSubmissions(conn, new Submission[]{submission });
            insertScorecards(conn, new Scorecard[]{scorecard });
            insertReviews(conn, new Review[]{review });

            startRecord();
            for (int i = 0; i < FIRST_LEVEL; i++) {
                handler.canPerform(submissionPhase);
            }
            endRecord("SubmissionPhaseHandler::canPerform(Phase)--(the phase status is false)", FIRST_LEVEL);
        } finally {
            closeConnection();
            cleanTables();
        }
    }

    /**
     * Tests the SubmissionPhaseHandler() constructor and canPerform with Scheduled statuses.
     *
     * @throws Exception
     *             not under test.
     */
    public void testCanPerformWithScheduled() throws Exception {
        SubmissionPhaseHandler handler = new SubmissionPhaseHandler();

        try {
            cleanTables();

            Project project = super.setupPhases();
            Phase[] phases = project.getAllPhases();
            Phase submission = phases[1];

            // test with scheduled status.
            submission.setPhaseStatus(PhaseStatus.SCHEDULED);

            // time has passed, but dependency not met.
            submission.setActualStartDate(new Date(new Date().getTime() - 28 * 60 * 60 * 1000));

            // time has passed and dependency met.
            submission.getAllDependencies()[0].getDependency().setPhaseStatus(PhaseStatus.CLOSED);

            startRecord();
            for (int i = 0; i < FIRST_LEVEL * 10; i++) {
                handler.canPerform(submission);
            }
            endRecord("SubmissionPhaseHandler::canPerform(Phase)--(the phase status is true)",
                FIRST_LEVEL * 10);
        } finally {
            cleanTables();
        }
    }

    /**
     * Tests the perform with Open statuses and a post-mortem phase should be inserted when
     * there are submissions.
     *
     * @throws Exception
     *             the exception occurs
     */
    public void testPerformWithNoSubmissions() throws Exception {
        SubmissionPhaseHandler handler = new SubmissionPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            cleanTables();

            // test with scheduled status.
            Project project = super.setupPhases();
            Phase[] phases = project.getAllPhases();
            Phase submissionPhase = phases[1];

            // test with scheduled status.
            String operator = "1001";
            Connection conn = getConnection();

            // test with open status
            submissionPhase.setPhaseStatus(PhaseStatus.OPEN);

            // create a submission
            Resource resource = super.createResource(1, 102L, 1, 1);
            super.insertResources(conn, new Resource[]{resource });

            Upload upload = super.createUpload(1, project.getId(), resource.getId(), 1, 1, "Paramter");
            super.insertUploads(conn, new Upload[]{upload });

            startRecord();
            for (int i = 0; i < FIRST_LEVEL; i++) {
                handler.perform(submissionPhase, operator);
            }
            endRecord("SubmissionPhaseHandler::perform(Phase, String)", FIRST_LEVEL);

        } finally {
            cleanTables();
            closeConnection();
        }
    }
}
