/*
 * Copyright (C) 2006-2011 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.topcoder.management.deliverable.Submission;
import com.topcoder.management.deliverable.Upload;
import com.topcoder.management.phase.PhaseHandlingException;
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
 * All tests for FinalFixPhaseHandler class.
 * <p>
 * version 1.1 changes note : Fixed test issues according to the new logic and requirement:
 * <li>Check final reviewer requirement in canPerform</li>
 * </p>
 * <p>
 * version 1.4 changes note : Add some new test cases.
 * </p>
 * <p>
 * Version 1.6.1 changes note:
 * <ul>
 * <li>Change some test because the return of canPerform change from boolean to OperationCheckResult.</li>
 * <li>PhasesHelper does not have method getFinalReviewWorksheet now, So create new private method .</li>
 * </ul>
 * </p>
 * @author bose_java, waits, myxgyy, microsky
 * @version 1.6.1
 */
public class FinalFixPhaseHandlerTest extends BaseTest {

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
     * Tests canPerform(Phase) with null phase.
     * @throws Exception
     *             not under test.
     */
    public void testCanPerform() throws Exception {
        FinalFixPhaseHandler handler = new FinalFixPhaseHandler(PHASE_HANDLER_NAMESPACE);
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
        FinalFixPhaseHandler handler = new FinalFixPhaseHandler(PHASE_HANDLER_NAMESPACE);
        try {
            Phase phase = createPhase(1, 1, "Invalid", 3, "Final Fix");
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
        FinalFixPhaseHandler handler = new FinalFixPhaseHandler(PHASE_HANDLER_NAMESPACE);
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
        FinalFixPhaseHandler handler = new FinalFixPhaseHandler(PHASE_HANDLER_NAMESPACE);
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
        FinalFixPhaseHandler handler = new FinalFixPhaseHandler(PHASE_HANDLER_NAMESPACE);
        try {
            Phase phase = createPhase(1, 1, "Invalid", 1, "Final Fix");
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
        FinalFixPhaseHandler handler = new FinalFixPhaseHandler(PHASE_HANDLER_NAMESPACE);
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
        FinalFixPhaseHandler handler = new FinalFixPhaseHandler(PHASE_HANDLER_NAMESPACE);
        try {
            Phase phase = createPhase(1, 1, "Scheduled", 1, "Final Fix");
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
        FinalFixPhaseHandler handler = new FinalFixPhaseHandler(PHASE_HANDLER_NAMESPACE);
        try {
            Phase phase = createPhase(1, 1, "Scheduled", 1, "Final Fix");
            handler.perform(phase, "   ");
            fail("perform() did not throw IllegalArgumentException for empty operator.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Tests the FinalFixPhaseHandler() constructor and canPerform with Scheduled statuses.
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

            // time has not passed, nor dependencies met
            assertFalse("canPerform should have returned false", handler.canPerform(finalFixPhase).isSuccess());

            // time has passed, but dependency not met.
            finalFixPhase.setActualStartDate(new Date());
            assertFalse("canPerform should have returned false", handler.canPerform(finalFixPhase).isSuccess());

            // time has passed and dependency met, but there is no final
            // reviewer assigned
            // version 1.1 should return false here
            finalFixPhase.getAllDependencies()[0].getDependency().setPhaseStatus(PhaseStatus.CLOSED);
            assertFalse("canPerform should have returned false when there is no final reviewer", handler
                    .canPerform(finalFixPhase).isSuccess());

            // Let's add a final reviewer here
            Resource finalReviewer = createResource(101, finalFixPhase.getId(), project.getId(), 9);

            Connection conn = getConnection();

            // insert records
            insertResources(conn, new Resource[] {finalReviewer });

            // we need to insert an external reference id
            // which references to resource's user id in resource_info table
            insertResourceInfo(conn, finalReviewer.getId(), 1, "1001");
            assertFalse("canPerform should have returned true when final reviewer is assigned.", handler
                    .canPerform(finalFixPhase).isSuccess());
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * Tests the FinalFixPhaseHandler() constructor and canPerform with Open statuses.
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

            // time has not passed, dependencies not met
            assertFalse("canPerform should have returned false", handler.canPerform(finalFixPhase).isSuccess());
        } finally {
            cleanTables();
        }
    }

    /**
     * Tests the perform with Open statuses.
     * @throws Exception
     *             not under test.
     */
    public void testPerform() throws Exception {
        FinalFixPhaseHandler handler = new FinalFixPhaseHandler();

        // test with open status
        Phase finalFixPhase = createPhase(1, 1, "Open", 2, "Final Fix");
        String operator = "operator";
        handler.perform(finalFixPhase, operator);
    }

    /**
     * Tests the perform with Scheduled status.
     * @throws Exception
     *             not under test.
     */
    public void testPerformWithScheduled1() throws Exception {
        FinalFixPhaseHandler handler = new FinalFixPhaseHandler(PHASE_HANDLER_NAMESPACE);
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
            Submission submission1 = createSubmission(1, upload1.getId(), 1, 1);
            Scorecard scorecard1 = createScorecard(1, 1, 2, 1, "name", "1.0", 75.0f, 100.0f);
            Review review1 = createReview(1111, aggregator.getId(), submission1.getId(), scorecard1.getId(), true,
                    80.0f);
            review1.addComment(createComment(21, aggregator.getId(), "Good Design", 1, "Comment"));

            Item[] reviewItems = new Item[1];
            reviewItems[0] = createReviewItem(11, "Answer 1", review1.getId(), 1);

            Comment[] reviewItemComments = new Comment[1];
            reviewItemComments[0] = createComment(11, aggregator.getId(), "Item 1", 1, "Comment");

            Connection conn = getConnection();

            // insert records
            insertResources(conn, new Resource[] {finalReviewer, aggregator });
            insertResourceInfo(conn, finalReviewer.getId(), 1, "1001");
            insertResourceInfo(conn, aggregator.getId(), 1, "1002");
            insertUploads(conn, new Upload[] {upload1 });
            insertSubmissions(conn, new Submission[] {submission1 });
            insertScorecards(conn, new Scorecard[] {scorecard1 });
            insertReviews(conn, new Review[] {review1 });

            insertComments(conn, new long[] {103 }, new long[] {aggregator.getId() },
                new long[] {review1.getId() },
                    new String[] {"comment 1" }, new long[] {1 });
            insertScorecardQuestion(conn, 1, 1);
            insertReviewItems(conn, reviewItems);
            insertReviewItemComments(conn, reviewItemComments, new long[] {11 });
            insertWinningSubmitter(conn, 12, project.getId());

            // check no final worksheet exists before calling perform method
            Review finalWorksheet = getFinalReviewWorksheet(conn, handler.getManagerHelper(),
                    finalReviewPhase.getId());
            assertNull("No final worksheet should exist before this test", finalWorksheet);

            // call perform method
            String operator = "1001";
            handler.perform(finalFixPhase, operator);

            // final worksheet should be created after the perform call.
            finalWorksheet = getFinalReviewWorksheet(conn, handler.getManagerHelper(),
                finalReviewPhase.getId());
            assertNotNull("Final worksheet should exist after perform()", finalWorksheet);
            assertEquals("review items not copied", finalWorksheet.getAllItems().length, reviewItems.length);
            assertEquals("review item comments not accepted should not be copied", 0, finalWorksheet.getItem(0)
                    .getAllComments().length);
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * Tests the perform with Scheduled status.
     * @throws Exception
     *             not under test.
     * @since 1.4
     */
    public void testPerformWithScheduled2() throws Exception {
        FinalFixPhaseHandler handler = new FinalFixPhaseHandler(PHASE_HANDLER_NAMESPACE);
        try {
            cleanTables();
            Project project = super.setupPhasesWithoutAggregation();
            Phase[] phases = project.getAllPhases();
            Phase reviewPhase = phases[3];
            Phase finalFixPhase = phases[6];
            Phase finalReviewPhase = phases[7];
            finalFixPhase.setPhaseStatus(PhaseStatus.SCHEDULED);

            // populate db with required data
            Resource finalReviewer = createResource(11, finalReviewPhase.getId(), project.getId(), 9);

            Connection conn = getConnection();

            // insert records
            insertResources(conn, new Resource[] {finalReviewer });
            insertResourceInfo(conn, finalReviewer.getId(), 1, "1001");
            // insert the reviewers
            Resource reviewer = createResource(6, reviewPhase.getId(), 1, 6);
            super.insertResources(conn, new Resource[] {reviewer });
            insertResourceInfo(conn, reviewer.getId(), 1, "2");

            // create a registration
            insertWinningSubmitter(conn, 5, project.getId());

            // insert upload/submission
            Upload upload = super.createUpload(1, project.getId(), 5, 1, 1, "Parameter");
            super.insertUploads(conn, new Upload[] {upload });

            Submission submission = super.createSubmission(1, upload.getId(), 1, 1);
            super.insertSubmissions(conn, new Submission[] {submission });
            // insertScorecards
            Scorecard sc = this.createScorecard(1, 1, 2, 1, "name", "1.0", 75.0f, 100.0f);
            insertScorecards(conn, new Scorecard[] {sc });
            Review review = createReview(1, reviewer.getId(), submission.getId(), sc.getId(), true, 77.0f);
            insertReviews(conn, new Review[] {review });
            review.addComment(createComment(21, reviewer.getId(), "Good Submission", 1, "Comment"));

            Item[] reviewItems = new Item[1];
            reviewItems[0] = createReviewItem(11, "Answer 1", review.getId(), 1);

            Comment[] reviewItemComments = new Comment[1];
            reviewItemComments[0] = createComment(11, reviewer.getId(), "Item 1", 1, "Comment");

            insertComments(conn, new long[] {21 }, new long[] {reviewer.getId() }, new long[] {review.getId() },
                new String[] {"Good Submission" }, new long[] {1 });
            insertScorecardQuestion(conn, 1, 1);
            insertReviewItems(conn, reviewItems);
            insertReviewItemComments(conn, reviewItemComments, new long[] {11 });
            // check no final worksheet exists before calling perform method
            Review finalWorksheet = getFinalReviewWorksheet(conn, handler.getManagerHelper(),
                    finalReviewPhase.getId());
            assertNull("No final worksheet should exist before this test", finalWorksheet);

            // call perform method
            String operator = "1001";
            handler.perform(finalFixPhase, operator);

            // final worksheet should be created after the perform call.
            finalWorksheet = getFinalReviewWorksheet(conn, handler.getManagerHelper(),
                finalReviewPhase
                    .getId());
            assertNotNull("Final worksheet should exist after perform()", finalWorksheet);
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * Tests the perform with Scheduled status. It tests the case the previous final review exists.
     * @throws Exception
     *             not under test.
     * @since 1.4
     */
    public void testPerformWithScheduled3() throws Exception {
        FinalFixPhaseHandler handler = new FinalFixPhaseHandler(PHASE_HANDLER_NAMESPACE);
        try {
            cleanTables();
            Project project = super.setupPhasesForDoubleFinalFix();
            Phase[] phases = project.getAllPhases();
            Phase reviewPhase = phases[3];
            Phase finalFixPhase = phases[6];
            Phase finalReviewPhase = phases[7];
            Phase finalFixPhase2 = phases[8];
            Phase finalReviewPhase2 = phases[9];
            finalFixPhase.setPhaseStatus(PhaseStatus.SCHEDULED);

            // populate db with required data
            Resource finalReviewer = createResource(11, finalReviewPhase.getId(), project.getId(), 9);
            Resource finalReviewer1 = createResource(12, finalReviewPhase2.getId(), project.getId(), 9);

            Connection conn = getConnection();

            // insert records
            insertResources(conn, new Resource[] {finalReviewer, finalReviewer1 });
            insertResourceInfo(conn, finalReviewer.getId(), 1, "1001");
            insertResourceInfo(conn, finalReviewer1.getId(), 1, "1002");
            // insert the reviewers
            Resource reviewer = createResource(6, reviewPhase.getId(), 1, 6);
            super.insertResources(conn, new Resource[] {reviewer });
            insertResourceInfo(conn, reviewer.getId(), 1, "2");

            // create a registration
            insertWinningSubmitter(conn, 5, project.getId());

            // insert upload/submission
            Upload upload = super.createUpload(1, project.getId(), 5, 1, 1, "Parameter");
            super.insertUploads(conn, new Upload[] {upload });

            Submission submission = super.createSubmission(1, upload.getId(), 1, 1);
            super.insertSubmissions(conn, new Submission[] {submission });
            // insertScorecards
            Scorecard sc = this.createScorecard(1, 1, 2, 1, "name", "1.0", 75.0f, 100.0f);
            insertScorecards(conn, new Scorecard[] {sc });
            Review review = createReview(1, reviewer.getId(), submission.getId(), sc.getId(), true, 77.0f);
            insertReviews(conn, new Review[] {review });
            review.addComment(createComment(21, reviewer.getId(), "Good Submission", 1, "Comment"));

            Item[] reviewItems = new Item[1];
            reviewItems[0] = createReviewItem(1001, "Answer 1", review.getId(), 1);

            Comment[] reviewItemComments = new Comment[1];
            reviewItemComments[0] = createComment(1001, reviewer.getId(), "Item 1", 1, "Comment");

            insertComments(conn, new long[] {21 }, new long[] {reviewer.getId() }, new long[] {review.getId() },
                new String[] {"Good Submission" }, new long[] {1 });
            insertScorecardQuestion(conn, 1, 1);
            insertReviewItems(conn, reviewItems);
            insertReviewItemComments(conn, reviewItemComments, new long[] {1001 });

            String operator = "1001";
            // insert a final review first
            handler.perform(finalFixPhase, operator);

            handler.perform(finalFixPhase2, operator);

            Review finalWorksheet = getFinalReviewWorksheet(conn,
                handler.getManagerHelper(), finalReviewPhase.getId());
            assertNotNull("Final worksheet should exist after perform()", finalWorksheet);
            assertFalse("not committed", finalWorksheet.isCommitted());
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * Tests the perform with Scheduled status. Verify the final fix status has been updated to deleted.
     * @throws Exception
     *             not under test.
     * @since 1.4
     */
    public void testPerformWithScheduled4() throws Exception {
        FinalFixPhaseHandler handler = new FinalFixPhaseHandler(PHASE_HANDLER_NAMESPACE);
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
            Submission submission1 = createSubmission(1, upload1.getId(), 1, 1);
            Scorecard scorecard1 = createScorecard(1, 1, 2, 1, "name", "1.0", 75.0f, 100.0f);
            Review review1 = createReview(1111, aggregator.getId(), submission1.getId(), scorecard1.getId(), true,
                    80.0f);
            review1.addComment(createComment(21, aggregator.getId(), "Good Design", 1, "Comment"));

            Item[] reviewItems = new Item[1];
            reviewItems[0] = createReviewItem(1001, "Answer 1", review1.getId(), 1);

            Comment[] reviewItemComments = new Comment[1];
            reviewItemComments[0] = createComment(1001, aggregator.getId(), "Item 1", 1, "Comment");

            // insert final fix
            Upload upload2 = createUpload(2, project.getId(), 12, 3, 1, "parameter");
            Submission submission2 = createSubmission(2, upload2.getId(), 1, 1);
            Connection conn = getConnection();

            // insert records
            insertWinningSubmitter(conn, 12, project.getId());
            insertResources(conn, new Resource[] {finalReviewer, aggregator });
            insertResourceInfo(conn, finalReviewer.getId(), 1, "1001");
            insertResourceInfo(conn, aggregator.getId(), 1, "1002");
            insertUploads(conn, new Upload[] {upload1, upload2 });
            insertSubmissions(conn, new Submission[] {submission1, submission2 });
            insertScorecards(conn, new Scorecard[] {scorecard1 });
            insertReviews(conn, new Review[] {review1 });

            insertComments(conn, new long[] {21 }, new long[] {aggregator.getId() },
                new long[] {review1.getId() },
                    new String[] {"comment 1" }, new long[] {1 });
            insertScorecardQuestion(conn, 1, 1);
            insertReviewItems(conn, reviewItems);
            insertReviewItemComments(conn, reviewItemComments, new long[] {1001 });

            // check no final worksheet exists before calling perform method
            Review finalWorksheet = getFinalReviewWorksheet(conn, handler.getManagerHelper(),
                    finalReviewPhase.getId());
            assertNull("No final worksheet should exist before this test", finalWorksheet);

            // call perform method
            String operator = "1001";
            handler.perform(finalFixPhase, operator);

            // final worksheet should be created after the perform call.
            finalWorksheet = getFinalReviewWorksheet(conn, handler.getManagerHelper(),
                finalReviewPhase
                    .getId());
            assertNotNull("Final worksheet should exist after perform()", finalWorksheet);
            assertEquals("review items not copied", finalWorksheet.getAllItems().length, reviewItems.length);
            assertEquals("review item comments not accepted should not be copied", 0, finalWorksheet.getItem(0)
                    .getAllComments().length);
            // final fix should be updated to deleted
            assertEquals("should be deleted", "Deleted", getUploadStatus(conn, upload2.getId()));
        } finally {
            // cleanTables();
            closeConnection();
        }
    }

    /**
     * Gets upload status.
     * @param con the database connection
     * @param id id of upload
     * @return status of upload
     * @throws Exception to JUnit.
     * @since 1.4
     */
    private String getUploadStatus(Connection con, long id) throws Exception {
        PreparedStatement preparedStmt = null;

        try {
            String selectPostPhase = "select us.name from upload u, upload_status_lu"
                + " us Where u.upload_id = ? and u.upload_status_id = us.upload_status_id";

            preparedStmt = con.prepareStatement(selectPostPhase);

            preparedStmt.setLong(1, id);

            ResultSet result = preparedStmt.executeQuery();

            if (result.next()) {
                return result.getString(1);
            }
            return null;
        } finally {
            closeStatement(preparedStmt);
        }
    }

    /**
     * Tests the perform with Scheduled status. No winner for project, PhaseHandlingException expected.
     * @throws Exception
     *             not under test.
     * @since 1.4
     */
    public void testPerformWithScheduled_NoWinner() throws Exception {
        FinalFixPhaseHandler handler = new FinalFixPhaseHandler(PHASE_HANDLER_NAMESPACE);
        try {
            cleanTables();
            Project project = super.setupPhasesWithoutAggregation();
            Phase[] phases = project.getAllPhases();
            Phase finalFixPhase = phases[6];
            Phase finalReviewPhase = phases[7];
            finalFixPhase.setPhaseStatus(PhaseStatus.SCHEDULED);

            // populate db with required data
            Resource finalReviewer = createResource(11, finalReviewPhase.getId(), project.getId(), 9);

            Connection conn = getConnection();

            // insert records
            insertResources(conn, new Resource[] {finalReviewer });
            insertResourceInfo(conn, finalReviewer.getId(), 1, "1001");

            // call perform method
            String operator = "1001";
            handler.perform(finalFixPhase, operator);

            fail("expect PhaseHandlingException");
        } catch (PhaseHandlingException e) {
            // expected
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * Tests the perform with Scheduled status. Multiple final fix exist, PhaseHandlingException expected.
     * @throws Exception
     *             not under test.
     * @since 1.4
     */
    public void testPerformWithScheduled_MultipleFinalFix() throws Exception {
        FinalFixPhaseHandler handler = new FinalFixPhaseHandler(PHASE_HANDLER_NAMESPACE);
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
            Submission submission1 = createSubmission(1, upload1.getId(), 1, 1);
            Scorecard scorecard1 = createScorecard(1, 1, 2, 1, "name", "1.0", 75.0f, 100.0f);
            Review review1 = createReview(1111, aggregator.getId(), submission1.getId(), scorecard1.getId(), true,
                    80.0f);
            review1.addComment(createComment(21, aggregator.getId(), "Good Design", 1, "Comment"));

            Item[] reviewItems = new Item[1];
            reviewItems[0] = createReviewItem(11, "Answer 1", review1.getId(), 1);

            Comment[] reviewItemComments = new Comment[1];
            reviewItemComments[0] = createComment(11, aggregator.getId(), "Item 1", 1, "Comment");

            // insert final fix
            Upload upload2 = createUpload(2, project.getId(), 12, 3, 1, "parameter");
            Submission submission2 = createSubmission(2, upload2.getId(), 1, 1);
            Upload upload3 = createUpload(3, project.getId(), 12, 3, 1, "parameter");
            Submission submission3 = createSubmission(3, upload3.getId(), 1, 1);
            Connection conn = getConnection();

            // insert records
            insertWinningSubmitter(conn, 12, project.getId());
            insertResources(conn, new Resource[] {finalReviewer, aggregator });
            insertResourceInfo(conn, finalReviewer.getId(), 1, "1001");
            insertResourceInfo(conn, aggregator.getId(), 1, "1002");
            insertUploads(conn, new Upload[] {upload1, upload2, upload3 });
            insertSubmissions(conn, new Submission[] {submission1, submission2, submission3 });
            insertScorecards(conn, new Scorecard[] {scorecard1 });
            insertReviews(conn, new Review[] {review1 });

            insertComments(conn, new long[] {103 }, new long[] {aggregator.getId() },
                new long[] {review1.getId() },
                    new String[] {"comment 1" }, new long[] {1 });
            insertScorecardQuestion(conn, 1, 1);
            insertReviewItems(conn, reviewItems);
            insertReviewItemComments(conn, reviewItemComments, new long[] {11 });

            // call perform method
            String operator = "1001";
            handler.perform(finalFixPhase, operator);

            fail("expect PhaseHandlingException");
        } catch (PhaseHandlingException e) {
            // expected
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * Tests the perform with Scheduled status.
     * @throws Exception
     *             not under test.
     */
    public void testPerform_exception1() throws Exception {
        FinalFixPhaseHandler handler = new FinalFixPhaseHandler(PHASE_HANDLER_NAMESPACE);
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
            Resource aggregator = createResource(1, aggregationPhase.getId(), project.getId(), 8);

            // reviewer resource and related review
            Upload upload1 = createUpload(1, project.getId(), aggregator.getId(), 4, 1, "parameter");
            Submission submission1 = createSubmission(1, upload1.getId(), 1, 1);
            Scorecard scorecard1 = createScorecard(1, 1, 2, 1, "name", "1.0", 75.0f, 100.0f);
            Review review1 = createReview(1111, aggregator.getId(), submission1.getId(), scorecard1.getId(), true,
                    80.0f);
            review1.addComment(createComment(21, aggregator.getId(), "Good Design", 1, "Comment"));

            Item[] reviewItems = new Item[2];
            reviewItems[0] = createReviewItem(11, "Answer 1", review1.getId(), 1);
            reviewItems[1] = createReviewItem(12, "Answer 2", review1.getId(), 1);

            Comment[] reviewItemComments = new Comment[2];
            reviewItemComments[0] = createComment(11, aggregator.getId(), "Item 1", 1, "Comment");
            reviewItemComments[1] = createComment(12, aggregator.getId(), "Item 2", 1, "Comment");

            Connection conn = getConnection();

            // insert records
            insertResources(conn, new Resource[] {aggregator });
            insertResourceInfo(conn, aggregator.getId(), 1, "1002");
            insertUploads(conn, new Upload[] {upload1 });
            insertSubmissions(conn, new Submission[] {submission1 });
            insertScorecards(conn, new Scorecard[] {scorecard1 });
            insertReviews(conn, new Review[] {review1 });

            insertComments(conn, new long[] {103 }, new long[] {aggregator.getId() },
                new long[] {review1.getId() },
                    new String[] {"comment 1" }, new long[] {1 });
            insertScorecardQuestion(conn, 1, 1);
            insertReviewItems(conn, reviewItems);
            insertReviewItemComments(conn, reviewItemComments, new long[] {11, 12 });
            insertWinningSubmitter(conn, 12, project.getId());

            // check no final worksheet exists before calling perform method
            Review finalWorksheet = getFinalReviewWorksheet(conn, handler.getManagerHelper(),
                    finalReviewPhase.getId());
            assertNull("No final worksheet should exist before this test", finalWorksheet);

            // call perform method
            String operator = "1001";
            handler.perform(finalFixPhase, operator);

            fail("expect PhaseHandlingException");
        } catch (PhaseHandlingException e) {
            // expected
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * Tests the perform with Scheduled status. No aggregated review scorecard exist,
     * PhaseHandlingException expected.
     * @throws Exception
     *             not under test.
     * @since 1.4
     */
    public void testPerformWithScheduled_NoAggregatedReviewScorecard() throws Exception {
        FinalFixPhaseHandler handler = new FinalFixPhaseHandler(PHASE_HANDLER_NAMESPACE);
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
            Submission submission = createSubmission(1, upload1.getId(), 1, 1);
            Scorecard scorecard = createScorecard(1, 1, 2, 1, "name", "1.0", 75.0f, 100.0f);
            Connection conn = getConnection();

            // insert records
            insertResources(conn, new Resource[] {finalReviewer, aggregator });
            insertResourceInfo(conn, finalReviewer.getId(), 1, "1001");
            insertResourceInfo(conn, aggregator.getId(), 1, "1002");
            insertUploads(conn, new Upload[] {upload1 });
            insertSubmissions(conn, new Submission[] {submission });
            insertScorecards(conn, new Scorecard[] {scorecard });
            insertWinningSubmitter(conn, 12, project.getId());

            // call perform method
            String operator = "1001";
            handler.perform(finalFixPhase, operator);
            fail("expect PhaseHandlingException");
        } catch (PhaseHandlingException e) {
            // expected
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * Tests the perform with Scheduled status. No winning submission exist,
     * PhaseHandlingException expected.
     * @throws Exception
     *             not under test.
     * @since 1.4
     */
    public void testPerformWithScheduled_NoWinningSubmission() throws Exception {
        FinalFixPhaseHandler handler = new FinalFixPhaseHandler(PHASE_HANDLER_NAMESPACE);
        try {
            cleanTables();
            Project project = super.setupPhasesWithoutAggregation();
            Phase[] phases = project.getAllPhases();
            Phase reviewPhase = phases[3];
            Phase finalFixPhase = phases[6];
            Phase finalReviewPhase = phases[7];
            finalFixPhase.setPhaseStatus(PhaseStatus.SCHEDULED);

            // populate db with required data
            Resource finalReviewer = createResource(11, finalReviewPhase.getId(), project.getId(), 9);

            Connection conn = getConnection();

            // insert records
            insertResources(conn, new Resource[] {finalReviewer });
            insertResourceInfo(conn, finalReviewer.getId(), 1, "1001");
            // insert the reviewers
            Resource reviewer = createResource(6, reviewPhase.getId(), 1, 6);
            super.insertResources(conn, new Resource[] {reviewer });
            insertResourceInfo(conn, reviewer.getId(), 1, "2");

            // create a registration
            insertWinningSubmitter(conn, 5, project.getId());

            // call perform method
            String operator = "1001";
            handler.perform(finalFixPhase, operator);

            fail("expect PhaseHandlingException");
        } catch (PhaseHandlingException e) {
            // expected
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * returns the final review worksheet for the given final review phase id.
     * @param managerHelper ManagerHelper instance.
     * @param finalReviewPhaseId final review phase id.
     * @return the final review worksheet, or null if not existing.
     * @throws PhaseHandlingException if an error occurs when retrieving data.
     * @throws SQLException if an error occurs when looking up resource role id.
     */
    private Review getFinalReviewWorksheet(Connection conn, ManagerHelper managerHelper, long finalReviewPhaseId)
        throws PhaseHandlingException, SQLException {
        Review[] reviews = PhasesHelper.searchReviewsForResourceRoles(conn, managerHelper, finalReviewPhaseId,
                new String[] {"Final Reviewer" }, null);

        if (reviews.length == 0) {
            return null;
        } else if (reviews.length == 1) {
            return reviews[0];
        } else {
            throw new PhaseHandlingException("Multiple final review worksheets found");
        }
    }
}
