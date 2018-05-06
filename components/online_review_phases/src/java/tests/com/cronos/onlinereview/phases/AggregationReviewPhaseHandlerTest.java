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
import com.topcoder.management.review.data.Review;
import com.topcoder.management.scorecard.data.Scorecard;
import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.PhaseStatus;
import com.topcoder.project.phases.Project;
import com.topcoder.util.config.ConfigManager;

/**
 * All tests for AggregationReviewPhaseHandler class.
 * <p>
 * Version 1.6.1 changes note:
 * <ul>
 * <li>Change some test because the return of canPerform change from boolean to OperationCheckResult.</li>
 * </ul>
 * </p>
 * @author bose_java, TCSDEVELOPER, microsky
 * @version 1.6.1
 */
public class AggregationReviewPhaseHandlerTest extends BaseTest {

    /**
     * sets up the environment required for test cases for this class.
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
     * cleans up the environment required for test cases for this class.
     * @throws Exception
     *             not under test.
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Tests the no-arg constructor.
     * @throws Exception
     *             not under test.
     */
    public void testCtor() throws Exception {
        AggregationReviewPhaseHandler handler = new AggregationReviewPhaseHandler();
        assertNotNull("instance should be created.", handler);
    }

    /**
     * Tests canPerform(Phase) with null phase.
     * @throws Exception
     *             not under test.
     */
    public void testCanPerform() throws Exception {
        AggregationReviewPhaseHandler handler = new AggregationReviewPhaseHandler(PHASE_HANDLER_NAMESPACE);
        try {
            handler.canPerform(null);
            fail("canPerform() did not throw IllegalArgumentException for null argument.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Tests canPerform(Phase) with invalid phase status.
     * @throws Exception
     *             not under test.
     */
    public void testCanPerformWithInvalidStatus() throws Exception {
        AggregationReviewPhaseHandler handler = new AggregationReviewPhaseHandler(PHASE_HANDLER_NAMESPACE);
        try {
            Phase phase = createPhase(1, 1, "Invalid", 3, "Aggregation Review");
            handler.canPerform(phase);
            fail("canPerform() did not throw PhaseHandlingException for invalid phase status.");
        } catch (PhaseHandlingException e) {
            // expected.
        }
    }

    /**
     * Tests canPerform(Phase) with invalid phase type.
     * @throws Exception
     *             not under test.
     */
    public void testCanPerformWithInvalidType() throws Exception {
        AggregationReviewPhaseHandler handler = new AggregationReviewPhaseHandler(PHASE_HANDLER_NAMESPACE);
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
     * @throws Exception
     *             not under test.
     */
    public void testPerformWithNullPhase() throws Exception {
        AggregationReviewPhaseHandler handler = new AggregationReviewPhaseHandler(PHASE_HANDLER_NAMESPACE);
        try {
            handler.perform(null, "operator");
            fail("perform() did not throw IllegalArgumentException for null argument.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Tests perform(Phase) with invalid phase status.
     * @throws Exception
     *             not under test.
     */
    public void testPerformWithInvalidStatus() throws Exception {
        AggregationReviewPhaseHandler handler = new AggregationReviewPhaseHandler(PHASE_HANDLER_NAMESPACE);
        try {
            Phase phase = createPhase(1, 1, "Invalid", 1, "Aggregation Review");
            handler.perform(phase, "operator");
            fail("perform() did not throw PhaseHandlingException for invalid phase status.");
        } catch (PhaseHandlingException e) {
            // expected.
        }
    }

    /**
     * Tests perform(Phase) with invalid phase type.
     * @throws Exception
     *             not under test.
     */
    public void testPerformWithInvalidType() throws Exception {
        AggregationReviewPhaseHandler handler = new AggregationReviewPhaseHandler(PHASE_HANDLER_NAMESPACE);
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
     * @throws Exception
     *             not under test.
     */
    public void testPerformWithNullOperator() throws Exception {
        AggregationReviewPhaseHandler handler = new AggregationReviewPhaseHandler(PHASE_HANDLER_NAMESPACE);
        try {
            Phase phase = createPhase(1, 1, "Scheduled", 1, "Aggregation Review");
            handler.perform(phase, null);
            fail("perform() did not throw IllegalArgumentException for null operator.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Tests perform(Phase) with empty operator.
     * @throws Exception
     *             not under test.
     */
    public void testPerformWithEmptyOperator() throws Exception {
        AggregationReviewPhaseHandler handler = new AggregationReviewPhaseHandler(PHASE_HANDLER_NAMESPACE);
        try {
            Phase phase = createPhase(1, 1, "Scheduled", 1, "Aggregation Review");
            handler.perform(phase, "   ");
            fail("perform() did not throw IllegalArgumentException for empty operator.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Tests the AggregationReviewPhaseHandler() constructor and canPerform with Scheduled statuses.
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

            // time has not passed, nor dependencies met
            assertFalse("canPerform should have returned false", handler.canPerform(aggregationReviewPhase)
                .isSuccess());

            // time has passed, but dependency not met.
            aggregationReviewPhase.setActualStartDate(new Date());
            assertFalse("canPerform should have returned false", handler.canPerform(aggregationReviewPhase)
                .isSuccess());

            // time has passed and dependency met.
            aggregationReviewPhase.getAllDependencies()[0].getDependency().setPhaseStatus(PhaseStatus.CLOSED);

            assertTrue("canPerform should have returned true", handler.canPerform(aggregationReviewPhase)
                .isSuccess());
        } finally {
            cleanTables();
        }
    }

    /**
     * Tests the AggregationReviewPhaseHandler() constructor and canPerform with Open statuses.
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

            aggregationReviewPhase.getAllDependencies()[0].setDependentStart(false);

            // test with open status.
            aggregationReviewPhase.setPhaseStatus(PhaseStatus.OPEN);

            // time has not passed, dependencies not met
            assertFalse("canPerform should have returned false", handler.canPerform(aggregationReviewPhase)
                .isSuccess());
        } finally {
            cleanTables();
        }
    }

    /**
     * Tests the perform with Scheduled statuses.
     * @throws Exception
     *             not under test.
     */
    public void testPerform() throws Exception {
        AggregationReviewPhaseHandler handler = new AggregationReviewPhaseHandler(PHASE_HANDLER_NAMESPACE);

        // test with scheduled status.
        Phase aggregationReviewPhase = createPhase(1, 1, "Scheduled", 2, "Aggregation Review");
        String operator = "operator";
        handler.perform(aggregationReviewPhase, operator);
    }

    /**
     * Tests the perform with Open status. Tests with a rejected comment in aggregation worksheet such that a new
     * aggregation/review cycle is created, new aggregator resource is created.
     * <p>
     * version 1.1 change notes: fix external reference failure, added user before adding resource.
     * </p>
     * @throws Exception
     *             not under test.
     * @version 1.1
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
            Submission aggSubmission = createSubmission(1, aggUpload.getId(), 1, 1);

            // reviewer resource and related review
            Scorecard scorecard1 = createScorecard(1, 1, 2, 1, "name", "1.0", 75.0f, 100.0f);
            Review aggWorksheet = createReview(11, aggregator.getId(), aggSubmission.getId(), scorecard1.getId(),
                true,
                    90.0f);
            // add a rejected comment
            aggWorksheet.addComment(createComment(1, aggregator.getId(), "Rejected", 1,
                "Aggregation Review Comment"));

            Connection conn = getConnection();

            // insert records
            insertResources(conn, new Resource[] {aggregator });

            // Added in version 1.1: we need to insert an external reference id
            // which references to resource's user id in resource_info table
            insertResourceInfo(conn, aggregator.getId(), 1, "1001");

            insertUploads(conn, new Upload[] {aggUpload });
            insertSubmissions(conn, new Submission[] {aggSubmission });
            insertResourceSubmission(conn, aggregator.getId(), aggSubmission.getId());
            insertScorecards(conn, new Scorecard[] {scorecard1 });
            insertReviews(conn, new Review[] {aggWorksheet });
            insertCommentsWithExtraInfo(conn, new long[] {1 }, new long[] {aggregator.getId() },
                new long[] {aggWorksheet
                    .getId() }, new String[] {"Rejected COmment" }, new long[] {7 }, new String[] {"Rejected" });
            insertScorecardQuestion(conn, 1, 1);

            // no exception should be thrown.
            String operator = "1001";
            handler.perform(aggregationReviewPhase, operator);

        } finally {
            closeConnection();
            cleanTables();
        }
    }

    /**
     * Tests the AggregationReviewPhaseHandler() constructor and canPerform with Open statuses.
     * @throws Exception
     *             not under test.
     */
    public void testCanPerform_aggregationWorksheetNotExist() throws Exception {
        AggregationReviewPhaseHandler handler = new AggregationReviewPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            cleanTables();
            Project project = super.setupPhases();
            Phase[] phases = project.getAllPhases();
            Phase aggregationReviewPhase = phases[6];

            // test with open status.
            aggregationReviewPhase.setPhaseStatus(PhaseStatus.OPEN);

            handler.canPerform(aggregationReviewPhase);
            fail("expect PhaseHandlingException");
        } catch (PhaseHandlingException e) {
            // expected
        } finally {
            cleanTables();
        }
    }

    /**
     * Tests the AggregationReviewPhaseHandler() constructor and canPerform with Open statuses.
     * @throws Exception
     *             not under test.
     */
    public void testCanPerform_accuracy1() throws Exception {
        AggregationReviewPhaseHandler handler = new AggregationReviewPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            cleanTables();
            Project project = super.setupPhases();
            Phase[] phases = project.getAllPhases();
            Phase reviewPhase = phases[3];
            Phase aggregationPhase = phases[6];
            Phase aggregationReviewPhase = phases[7];

            // test with open status.
            aggregationReviewPhase.setPhaseStatus(PhaseStatus.OPEN);

            // populate db with required data
            // aggregator resource
            Resource aggregator = createResource(101, aggregationPhase.getId(), project.getId(), 8);
            Resource reviewer = createResource(6, reviewPhase.getId(), 1, 4);
            Upload aggUpload = createUpload(1, project.getId(), aggregator.getId(), 4, 1, "parameter");
            Submission aggSubmission = createSubmission(1, aggUpload.getId(), 1, 1);

            // reviewer resource and related review
            Scorecard scorecard1 = createScorecard(1, 1, 2, 1, "name", "1.0", 75.0f, 100.0f);
            Review aggWorksheet = createReview(11, aggregator.getId(), aggSubmission.getId(), scorecard1.getId(),
                true,
                    90.0f);
            // add a rejected comment
            aggWorksheet.addComment(createComment(1, aggregator.getId(), "Rejected", 1,
                "Aggregation Review Comment"));

            Connection conn = getConnection();

            // insert records
            insertResources(conn, new Resource[] {reviewer, aggregator });

            // Added in version 1.1: we need to insert an external reference id
            // which references to resource's user id in resource_info table
            insertResourceInfo(conn, aggregator.getId(), 1, "1001");

            insertUploads(conn, new Upload[] {aggUpload });
            insertSubmissions(conn, new Submission[] {aggSubmission });
            insertResourceSubmission(conn, aggregator.getId(), aggSubmission.getId());
            insertScorecards(conn, new Scorecard[] {scorecard1 });
            insertReviews(conn, new Review[] {aggWorksheet });
            insertCommentsWithExtraInfo(conn, new long[] {1 }, new long[] {aggregator.getId() },
                new long[] {aggWorksheet
                    .getId() }, new String[] {"Rejected COmment" }, new long[] {7 }, new String[] {"Rejected" });
            insertScorecardQuestion(conn, 1, 1);
            insertWinningSubmitter(conn, 1, project.getId());

            assertFalse(handler.canPerform(aggregationReviewPhase).isSuccess());
        } finally {
            cleanTables();
        }
    }

    /**
     * Tests the AggregationReviewPhaseHandler() constructor and canPerform with Open statuses.
     * @throws Exception
     *             not under test.
     */
    public void testCanPerform_accuracy2() throws Exception {
        AggregationReviewPhaseHandler handler = new AggregationReviewPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            cleanTables();
            Project project = super.setupPhases();
            Phase[] phases = project.getAllPhases();
            Phase reviewPhase = phases[3];
            Phase aggregationPhase = phases[6];
            Phase aggregationReviewPhase = phases[7];

            // test with open status.
            aggregationReviewPhase.setPhaseStatus(PhaseStatus.OPEN);

            // populate db with required data
            // aggregator resource
            Resource aggregator = createResource(101, aggregationPhase.getId(), project.getId(), 8);
            System.out.println(reviewPhase.getId());
            Resource reviewer = createResource(6, reviewPhase.getId(), project.getId(), 4);
            Upload aggUpload = createUpload(1, project.getId(), aggregator.getId(), 4, 1, "parameter");
            Submission aggSubmission = createSubmission(1, aggUpload.getId(), 1, 1);

            // reviewer resource and related review
            Scorecard scorecard1 = createScorecard(1, 1, 2, 1, "name", "1.0", 75.0f, 100.0f);
            Review aggWorksheet = createReview(11, aggregator.getId(), aggSubmission.getId(), scorecard1.getId(),
                true,
                    90.0f);
            // add a rejected comment
            aggWorksheet.addComment(createComment(1, aggregator.getId(), "Rejected", 1,
                "Aggregation Review Comment"));

            Connection conn = getConnection();

            // insert records
            insertResources(conn, new Resource[] {reviewer, aggregator });

            // Added in version 1.1: we need to insert an external reference id
            // which references to resource's user id in resource_info table
            insertResourceInfo(conn, aggregator.getId(), 1, "1001");
            insertResourceInfo(conn, reviewer.getId(), 1, "2");

            insertUploads(conn, new Upload[] {aggUpload });
            insertSubmissions(conn, new Submission[] {aggSubmission });
            insertResourceSubmission(conn, aggregator.getId(), aggSubmission.getId());
            insertScorecards(conn, new Scorecard[] {scorecard1 });
            insertReviews(conn, new Review[] {aggWorksheet });
            insertCommentsWithExtraInfo(conn, new long[] {1 }, new long[] {aggregator.getId() },
                new long[] {aggWorksheet
                    .getId() }, new String[] {"Rejected COmment" }, new long[] {7 }, new String[] {"Rejected" });
            insertScorecardQuestion(conn, 1, 1);
            insertWinningSubmitter(conn, 1, project.getId());

            assertFalse(handler.canPerform(aggregationReviewPhase).isSuccess());
        } finally {
            cleanTables();
        }
    }

    /**
     * Tests the AggregationReviewPhaseHandler() constructor and canPerform with Open statuses.
     * @throws Exception
     *             not under test.
     */
    public void testCanPerform_accuracy3() throws Exception {
        AggregationReviewPhaseHandler handler = new AggregationReviewPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            cleanTables();
            Project project = super.setupPhases();
            Phase[] phases = project.getAllPhases();
            Phase reviewPhase = phases[3];
            Phase aggregationPhase = phases[6];
            Phase aggregationReviewPhase = phases[7];

            // test with open status.
            aggregationReviewPhase.setPhaseStatus(PhaseStatus.OPEN);

            // populate db with required data
            // aggregator resource
            Resource aggregator = createResource(101, aggregationPhase.getId(), project.getId(), 8);
            Resource reviewer = createResource(6, reviewPhase.getId(), project.getId(), 4);
            Upload aggUpload = createUpload(1, project.getId(), aggregator.getId(), 4, 1, "parameter");
            Submission aggSubmission = createSubmission(1, aggUpload.getId(), 1, 1);

            // reviewer resource and related review
            Scorecard scorecard1 = createScorecard(1, 1, 2, 1, "name", "1.0", 75.0f, 100.0f);
            Review aggWorksheet = createReview(11, aggregator.getId(), aggSubmission.getId(), scorecard1.getId(),
                true,
                    90.0f);
            // add a rejected comment
            aggWorksheet.addComment(createComment(1, aggregator.getId(), "Rejected", 1,
                "Aggregation Review Comment"));

            Connection conn = getConnection();

            // insert records
            insertResources(conn, new Resource[] {reviewer, aggregator });

            // Added in version 1.1: we need to insert an external reference id
            // which references to resource's user id in resource_info table
            insertResourceInfo(conn, aggregator.getId(), 1, "1001");
            insertResourceInfo(conn, reviewer.getId(), 1, "1001");

            insertUploads(conn, new Upload[] {aggUpload });
            insertSubmissions(conn, new Submission[] {aggSubmission });
            insertResourceSubmission(conn, aggregator.getId(), aggSubmission.getId());
            insertScorecards(conn, new Scorecard[] {scorecard1 });
            insertReviews(conn, new Review[] {aggWorksheet });
            insertCommentsWithExtraInfo(conn, new long[] {1 }, new long[] {aggregator.getId() },
                new long[] {aggWorksheet
                    .getId() }, new String[] {"Rejected COmment" }, new long[] {7 }, new String[] {"Rejected" });
            insertScorecardQuestion(conn, 1, 1);
            insertWinningSubmitter(conn, 1, project.getId());

            assertFalse(handler.canPerform(aggregationReviewPhase).isSuccess());
        } finally {
            cleanTables();
        }
    }

    /**
     * Tests the AggregationReviewPhaseHandler() constructor and canPerform with Open statuses.
     * @throws Exception
     *             not under test.
     */
    public void testCanPerform_accuracy() throws Exception {
        AggregationReviewPhaseHandler handler = new AggregationReviewPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            cleanTables();
            Project project = super.setupPhases();
            Phase[] phases = project.getAllPhases();
            Phase reviewPhase = phases[3];
            Phase aggregationPhase = phases[6];
            Phase aggregationReviewPhase = phases[7];

            // test with open status.
            aggregationReviewPhase.setPhaseStatus(PhaseStatus.OPEN);

            // populate db with required data
            // aggregator resource
            Resource aggregator = createResource(101, aggregationPhase.getId(), project.getId(), 8);
            Resource reviewer = createResource(6, reviewPhase.getId(), project.getId(), 4);
            Upload aggUpload = createUpload(1, project.getId(), aggregator.getId(), 4, 1, "parameter");
            Submission aggSubmission = createSubmission(1, aggUpload.getId(), 1, 1);

            // reviewer resource and related review
            Scorecard scorecard1 = createScorecard(1, 1, 2, 1, "name", "1.0", 75.0f, 100.0f);
            Review aggWorksheet = createReview(11, aggregator.getId(), aggSubmission.getId(), scorecard1.getId(),
                true,
                    90.0f);
            // add a rejected comment
            aggWorksheet.addComment(createComment(1, aggregator.getId(), "Rejected", 1,
                "Aggregation Review Comment"));

            Connection conn = getConnection();

            // insert records
            insertResources(conn, new Resource[] {reviewer, aggregator });

            // Added in version 1.1: we need to insert an external reference id
            // which references to resource's user id in resource_info table
            insertResourceInfo(conn, aggregator.getId(), 1, "1001");
            insertResourceInfo(conn, reviewer.getId(), 1, "1001");

            insertUploads(conn, new Upload[] {aggUpload });
            insertSubmissions(conn, new Submission[] {aggSubmission });
            insertResourceSubmission(conn, aggregator.getId(), aggSubmission.getId());
            insertScorecards(conn, new Scorecard[] {scorecard1 });
            insertReviews(conn, new Review[] {aggWorksheet });
            insertCommentsWithExtraInfo(conn, new long[] {1 }, new long[] {aggregator.getId() },
                new long[] {aggWorksheet
                    .getId() }, new String[] {"Rejected COmment" }, new long[] {7 }, new String[] {"Rejected" });
            insertScorecardQuestion(conn, 1, 1);
            insertWinningSubmitter(conn, 1, project.getId());
            insertCommentsWithExtraInfo(conn, new long[] {2 }, new long[] {1 },
                new long[] {aggWorksheet.getId() },
                    new String[] {"Submitter Comment" }, new long[] {8 }, new String[] {"Approved" });

            assertTrue(handler.canPerform(aggregationReviewPhase).isSuccess());
        } finally {
            cleanTables();
        }
    }

    /**
     * Tests the AggregationReviewPhaseHandler() constructor and canPerform with Open statuses. It will
     * automatically
     * approve the aggregation.
     * @throws Exception
     *             not under test.
     */
    public void testCanPerform_accuracy4() throws Exception {
        AggregationReviewPhaseHandler handler = new AggregationReviewPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            cleanTables();
            Project project = super.setupPastPhases();
            Phase[] phases = project.getAllPhases();
            Phase reviewPhase = phases[3];
            Phase aggregationPhase = phases[6];
            Phase aggregationReviewPhase = phases[7];

            // test with open status.
            aggregationReviewPhase.setPhaseStatus(PhaseStatus.OPEN);

            // populate db with required data
            // aggregator resource
            Resource aggregator = createResource(101, aggregationPhase.getId(), project.getId(), 8);
            Resource reviewer = createResource(6, reviewPhase.getId(), project.getId(), 4);
            Upload aggUpload = createUpload(1, project.getId(), aggregator.getId(), 4, 1, "parameter");
            Submission aggSubmission = createSubmission(1, aggUpload.getId(), 1, 1);

            // reviewer resource and related review
            Scorecard scorecard1 = createScorecard(1, 1, 2, 1, "name", "1.0", 75.0f, 100.0f);
            Review aggWorksheet = createReview(11, aggregator.getId(), aggSubmission.getId(), scorecard1.getId(),
                true,
                    90.0f);
            // add a rejected comment
            aggWorksheet.addComment(createComment(1, aggregator.getId(), "Rejected", 1,
                "Aggregation Review Comment"));

            Connection conn = getConnection();

            // insert records
            insertResources(conn, new Resource[] {reviewer, aggregator });

            // Added in version 1.1: we need to insert an external reference id
            // which references to resource's user id in resource_info table
            insertResourceInfo(conn, aggregator.getId(), 1, "1001");
            insertResourceInfo(conn, reviewer.getId(), 1, "1001");

            insertUploads(conn, new Upload[] {aggUpload });
            insertSubmissions(conn, new Submission[] {aggSubmission });
            insertResourceSubmission(conn, aggregator.getId(), aggSubmission.getId());
            insertScorecards(conn, new Scorecard[] {scorecard1 });
            insertReviews(conn, new Review[] {aggWorksheet });
            insertCommentsWithExtraInfo(conn, new long[] {1 }, new long[] {aggregator.getId() },
                new long[] {aggWorksheet
                    .getId() }, new String[] {"Rejected COmment" }, new long[] {7 }, new String[] {"Rejected" });
            insertScorecardQuestion(conn, 1, 1);
            insertWinningSubmitter(conn, 1, project.getId());

            assertTrue("should return true.", handler.canPerform(aggregationReviewPhase).isSuccess());
        } finally {
            cleanTables();
        }
    }

}
