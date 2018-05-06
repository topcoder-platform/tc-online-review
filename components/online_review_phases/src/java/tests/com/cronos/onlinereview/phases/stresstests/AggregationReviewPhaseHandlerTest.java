/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases.stresstests;

import java.sql.Connection;
import java.util.Date;

import com.cronos.onlinereview.phases.AggregationReviewPhaseHandler;
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
 * Stress tests for <code>AggregationReviewPhaseHandler</code>.
 * </p>
 * <p>
 * Since this handler is immutable, so it's naturally thread safe. Here just do benchmark tests.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.3
 */
public class AggregationReviewPhaseHandlerTest extends StressBaseTest {

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
     * Tests the AggregationReviewPhaseHandler() constructor and canPerform with Open statuses.
     *
     * @throws Exception
     *             not under test.
     */
    public void testCanPerformHandlerWithOpen() throws Exception {
        AggregationReviewPhaseHandler handler = new AggregationReviewPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            cleanTables();
            Project project = super.setupPhases();
            Phase[] phases = project.getAllPhases();
            Phase aggregationReviewPhase = phases[7];

            // change dependency type to F2F
            aggregationReviewPhase.getAllDependencies();

            aggregationReviewPhase.getAllDependencies()[0].setDependentStart(false);

            // test with open status.
            aggregationReviewPhase.setPhaseStatus(PhaseStatus.OPEN);

            // time has not passed, dependencies not met
            startRecord();
            for (int i = 0; i < FIRST_LEVEL * 5; i++) {
                handler.canPerform(aggregationReviewPhase);
            }
            endRecord("AggregationReviewPhaseHandler::canPerform(Phase)--(the phase status is false)",
                FIRST_LEVEL * 5);
        } finally {
            cleanTables();
        }
    }

    /**
     * Tests the AggregationReviewPhaseHandler() constructor and canPerform with Scheduled statuses.
     *
     * @throws Exception
     *             not under test.
     */
    public void testCanPerformWithScheduled() throws Exception {
        AggregationReviewPhaseHandler handler = new AggregationReviewPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            cleanTables();
            Project project = super.setupPhases();
            Phase[] phases = project.getAllPhases();
            Phase aggregationReviewPhase = phases[7];

            // test with scheduled status.
            aggregationReviewPhase.setPhaseStatus(PhaseStatus.SCHEDULED);

            // time has passed, but dependency not met.
            aggregationReviewPhase.setActualStartDate(new Date());

            // time has passed and dependency met.
            aggregationReviewPhase.getAllDependencies()[0].getDependency().setPhaseStatus(PhaseStatus.CLOSED);

            startRecord();
            for (int i = 0; i < FIRST_LEVEL * 5; i++) {
                handler.canPerform(aggregationReviewPhase);
            }
            endRecord("AggregationReviewPhaseHandler::canPerform(Phase)--(the phase status is true)",
                FIRST_LEVEL * 5);

        } finally {
            cleanTables();
        }
    }

    /**
     * Tests the perform with Open status. Tests with a rejected comment in aggregation worksheet
     * such that a new aggregation/review cycle is created, new aggregator resource is created.
     *
     * @throws Exception
     *             the exception occurs
     */
    public void testPerformWithOpen() throws Exception {
        AggregationReviewPhaseHandler handler = new AggregationReviewPhaseHandler(PHASE_HANDLER_NAMESPACE);
        try {
            cleanTables();
            Project project = super.setupPhases();
            Phase[] phases = project.getAllPhases();
            Phase aggregationPhase = phases[6];
            Phase aggregationReviewPhase = phases[7];
            aggregationReviewPhase.setPhaseStatus(PhaseStatus.OPEN);

            // populate db with required data
            // aggregator resource
            Resource aggregator = createResource(101, aggregationPhase.getId(), project.getId(), 8);
            Upload aggUpload = createUpload(1, project.getId(), aggregator.getId(), 4, 1, "parameter");
            Submission aggSubmission = createSubmission(1, aggUpload.getId(), 1);

            // reviewer resource and related review
            Scorecard scorecard1 = createScorecard(1, 1, 2, 1, "name", "1.0", 75.0f, 100.0f);
            Review aggWorksheet = createReview(11, aggregator.getId(), aggSubmission.getId(), scorecard1
                .getId(), true, 90.0f);
            // add a rejected comment
            aggWorksheet.addComment(createComment(1, aggregator.getId(), "Rejected", 1,
                "Aggregation Review Comment"));

            Connection conn = getConnection();

            // insert records
            insertResources(conn, new Resource[]{aggregator });

            // Added in version 1.1: we need to insert an external reference id
            // which references to resource's user id in resource_info table
            insertResourceInfo(conn, aggregator.getId(), 1, "1001");

            insertUploads(conn, new Upload[]{aggUpload });
            insertSubmissions(conn, new Submission[]{aggSubmission });
            insertResourceSubmission(conn, aggregator.getId(), aggSubmission.getId());
            insertScorecards(conn, new Scorecard[]{scorecard1 });
            insertReviews(conn, new Review[]{aggWorksheet });
            insertCommentsWithExtraInfo(conn, new long[]{1 }, new long[]{aggregator.getId() },
                new long[]{aggWorksheet.getId() }, new String[]{"Rejected COmment" }, new long[]{7 },
                new String[]{"Rejected" });
            insertScorecardQuestion(conn, 1, 1);

            startRecord();
            for (int i = 0; i < FIRST_LEVEL; i++) {
                handler.perform(aggregationReviewPhase, "1001");
            }
            endRecord("AggregationReviewPhaseHandler::perform(Phase, String)", FIRST_LEVEL);

        } finally {
            closeConnection();
            cleanTables();
        }
    }

}
