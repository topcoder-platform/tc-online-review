/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases.stresstests;

import java.sql.Connection;
import java.util.Date;
import java.util.HashMap;

import com.cronos.onlinereview.phases.AbstractPhaseHandler;
import com.cronos.onlinereview.phases.FinalFixPhaseHandler;
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
 * <p>
 * Stress tests for <code>FinalFixPhaseHandler</code>.
 * </p>
 * <p>
 * Since this handler is immutable, so it's naturally thread safe. Here just do benchmark tests.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.3
 */
public class FinalFixPhaseHandlerTest extends StressBaseTest {

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
     * Tests the FinalFixPhaseHandler() constructor and canPerform with Open statuses.
     *
     * @throws Exception
     *             not under test.
     */
    public void testCanPerformHandlerWithOpen() throws Exception {
        FinalFixPhaseHandler handler = new FinalFixPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            cleanTables();
            Project project = super.setupPhases();
            Phase[] phases = project.getAllPhases();
            Phase finalFixPhase = phases[8];

            // change dependency type to F2F
            finalFixPhase.getAllDependencies()[0].setDependentStart(false);

            // test with open status.
            finalFixPhase.setPhaseStatus(PhaseStatus.OPEN);

            startRecord();
            for (int i = 0; i < FIRST_LEVEL * 10; i++) {
                handler.canPerform(finalFixPhase);
            }
            endRecord("FinalFixPhaseHandler::canPerform(Phase)--(the phase status is false)",
                FIRST_LEVEL * 10);
        } finally {
            cleanTables();
        }
    }

    /**
     * Tests the FinalFixPhaseHandler() constructor and canPerform with Scheduled statuses.
     *
     * @throws Exception
     *             not under test.
     */
    public void testCanPerformWithScheduled() throws Exception {
        FinalFixPhaseHandler handler = new FinalFixPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            cleanTables();
            Project project = super.setupPhases();
            Phase[] phases = project.getAllPhases();
            Phase finalFixPhase = phases[8];

            // test with scheduled status.
            finalFixPhase.setPhaseStatus(PhaseStatus.SCHEDULED);

            // time has passed, but dependency not met.
            finalFixPhase.setActualStartDate(new Date());

            // time has passed and dependency met, but there is no final
            // reviewer assigned
            // version 1.1 should return false here
            finalFixPhase.getAllDependencies()[0].getDependency().setPhaseStatus(PhaseStatus.CLOSED);

            // Let's add a final reviewer here
            Resource finalReviewer = createResource(101, finalFixPhase.getId(), project.getId(), 9);

            Connection conn = getConnection();

            // insert records
            insertResources(conn, new Resource[]{finalReviewer });

            // we need to insert an external reference id
            // which references to resource's user id in resource_info table
            insertResourceInfo(conn, finalReviewer.getId(), 1, "1001");

            startRecord();
            for (int i = 0; i < FIRST_LEVEL; i++) {
                handler.canPerform(finalFixPhase);
            }
            endRecord("FinalFixPhaseHandler::canPerform(Phase)--(the phase status is true)", FIRST_LEVEL);
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * Tests the perform with Scheduled status.
     *
     * @throws Exception
     *             not under test.
     */
    public void testPerformWithScheduled() throws Exception {
        FinalFixPhaseHandler handler = new FinalFixPhaseHandler(PHASE_HANDLER_NAMESPACE);

        long time = 0;
        for (int i = 0; i < FIRST_LEVEL; i++) {
            try {
                cleanTables();

                Project project = super.setupPhases();
                Phase[] phases = project.getAllPhases();
                Phase aggregationPhase = phases[6];
                Phase finalFixPhase = phases[8];
                Phase finalReviewPhase = phases[9];
                finalFixPhase.setPhaseStatus(PhaseStatus.SCHEDULED);

                // populate db with required data
                // aggregator resource
                Resource finalReviewer = createResource(11, finalReviewPhase.getId(), project.getId(), 9);
                Resource aggregator = createResource(1, aggregationPhase.getId(), project.getId(), 8);

                // reviewer resource and related review
                Upload upload1 = createUpload(1, project.getId(), aggregator.getId(), 4, 1, "parameter");
                Submission submission1 = createSubmission(1, upload1.getId(), 1);
                Scorecard scorecard1 = createScorecard(1, 1, 2, 1, "name", "1.0", 75.0f, 100.0f);
                Review review1 = createReview(1111, aggregator.getId(), submission1.getId(), scorecard1
                    .getId(), true, 80.0f);
                review1.addComment(createComment(21, aggregator.getId(), "Good Design", 1, "Comment"));

                Item[] reviewItems = new Item[2];
                reviewItems[0] = createReviewItem(111, "Answer 1", review1.getId(), 1);
                reviewItems[1] = createReviewItem(112, "Answer 2", review1.getId(), 1);

                Comment[] reviewItemComments = new Comment[2];
                reviewItemComments[0] = createComment(111, aggregator.getId(), "Item 1", 1, "Comment");
                reviewItemComments[1] = createComment(112, aggregator.getId(), "Item 2", 1, "Comment");

                Connection conn = getConnection();

                // insert records
                insertResources(conn, new Resource[]{finalReviewer, aggregator });
                insertResourceInfo(conn, finalReviewer.getId(), 1, "1001");
                insertResourceInfo(conn, aggregator.getId(), 1, "1002");
                insertUploads(conn, new Upload[]{upload1 });
                insertSubmissions(conn, new Submission[]{submission1});
                insertScorecards(conn, new Scorecard[]{scorecard1 });
                insertReviews(conn, new Review[]{review1 });

                insertComments(conn, new long[]{1003 }, new long[]{aggregator.getId() }, new long[]{review1
                    .getId() }, new String[]{"comment 1" }, new long[]{1 });
                insertScorecardQuestion(conn, 1, 1);
                insertReviewItems(conn, reviewItems);
                insertReviewItemComments(conn, reviewItemComments, new long[]{111, 112 });
                insertWinningSubmitter(conn, 12, project.getId());

                // call perform method
                String operator = "1001";
                long timeS = System.currentTimeMillis();
                handler.perform(finalFixPhase, operator);
                time += System.currentTimeMillis() - timeS;
            } finally {
                closeConnection();
                cleanTables();
            }
        }

        printResultToFile("Run FinalFixPhaseHandler::perform(Phase, String) " + FIRST_LEVEL + " times took "
            + time + "ms.");
    }

    /**
     * Test send email phase map of string object.
     *
     * @throws Exception
     *             the exception occurs
     */
    @SuppressWarnings("unchecked")
    public void testSendEmailPhaseMapOfStringObject() throws Exception {

        AbstractPhaseHandler handler = new FinalFixPhaseHandler();

        try {
            cleanTables();
            Project project = super.setupPhases();
            Phase[] phases = project.getAllPhases();
            Phase finalFixPhase = phases[8];

            // test with scheduled status.
            finalFixPhase.setPhaseStatus(PhaseStatus.SCHEDULED);

            // time has passed, but dependency not met.
            finalFixPhase.setActualStartDate(new Date());

            // time has passed and dependency met, but there is no final
            // reviewer assigned
            // version 1.1 should return false here
            finalFixPhase.getAllDependencies()[0].getDependency().setPhaseStatus(PhaseStatus.CLOSED);

            startRecord();
            for (int i = 0; i < FIRST_LEVEL; i++) {
                handler.sendEmail(finalFixPhase, new HashMap());
            }
            endRecord("AbstractPhaseHandler::sendEmail(Phase, Map)", FIRST_LEVEL);

        } finally {
            cleanTables();
            closeConnection();
        }

    }

}
