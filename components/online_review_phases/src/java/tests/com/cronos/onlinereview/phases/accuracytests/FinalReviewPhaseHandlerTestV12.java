/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases.accuracytests;

import java.sql.Connection;

import com.cronos.onlinereview.phases.FinalReviewPhaseHandler;
import com.topcoder.management.deliverable.Submission;
import com.topcoder.management.deliverable.Upload;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.review.data.Review;
import com.topcoder.management.scorecard.data.Scorecard;
import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.PhaseStatus;
import com.topcoder.project.phases.Project;


/**
 * Accuracy tests for FinalReviewPhaseHandler class.
 *
 * @author assistant
 * @version 1.2
 */
public class FinalReviewPhaseHandlerTestV12 extends BaseTestCase {
    /**
     * sets up the environment required for test cases for this class.
     *
     * @throws Exception not under test.
     */
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * cleans up the environment required for test cases for this class.
     *
     * @throws Exception not under test.
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Tests the perform with Open status.
     *
     * @throws Exception to JUnit.
     *
     * @since 1.1
     */
    public void testPerform_1() throws Exception {
        FinalReviewPhaseHandler handler = new FinalReviewPhaseHandler();

        Project project = this.setupProjectResourcesNotification("Final Review", true);
        Phase[] phases = project.getAllPhases();
        Phase finalReviewPhase = phases[9];
        finalReviewPhase.setPhaseStatus(PhaseStatus.OPEN);

        Resource finalReviewer = createResource(101, finalReviewPhase.getId(), project.getId(), 9);
        Upload frUpload = createUpload(1, project.getId(), finalReviewer.getId(), 4, 1, "parameter");
        Submission frSubmission = createSubmission(1, frUpload.getId(), 1);

        // reviewer resource and related review
        Scorecard scorecard1 = createScorecard(1000, 1, 2, 1, "name", "1.0", 75.0f, 100.0f);
        Review frWorksheet = createReview(111, finalReviewer.getId(), frSubmission.getId(), scorecard1.getId(), true,
                90.0f);

        // add a rejected comment
        frWorksheet.addComment(createComment(1111, finalReviewer.getId(), "Rejected", 10, "Final Review Comment"));

        Connection conn = getConnection();

        // insert records
        insertResources(conn, new Resource[] { finalReviewer });
        insertResourceInfo(conn, finalReviewer.getId(), 1, "2");
        insertUploads(conn, new Upload[] { frUpload });
        insertSubmissions(conn, new Submission[] { frSubmission });
        insertResourceSubmission(conn, finalReviewer.getId(), frSubmission.getId());
        insertScorecards(conn, new Scorecard[] { scorecard1 });
        insertReviews(conn, new Review[] { frWorksheet });
        insertCommentsWithExtraInfo(conn, new long[] { 1 }, new long[] { finalReviewer.getId() },
            new long[] { frWorksheet.getId() }, new String[] { "Rejected Comment" }, new long[] { 10 },
            new String[] { "Rejected" });
        insertScorecardQuestion(conn, 1, scorecard1.getId());

        handler.perform(finalReviewPhase, "1001");
    }

    /**
     * Tests the perform with Open status.
     *
     * @throws Exception to JUnit.
     *
     * @since 1.1
     */
    public void testPerform_2() throws Exception {
        FinalReviewPhaseHandler handler = new FinalReviewPhaseHandler();

        Project project = this.setupProjectResourcesNotification("Final Review", true);
        Phase[] phases = project.getAllPhases();
        Phase finalReviewPhase = phases[9];
        finalReviewPhase.setPhaseStatus(PhaseStatus.OPEN);

        Resource finalReviewer = createResource(101, finalReviewPhase.getId(), project.getId(), 9);
        Upload frUpload = createUpload(1, project.getId(), finalReviewer.getId(), 4, 1, "parameter");
        Submission frSubmission = createSubmission(1, frUpload.getId(), 1);

        // reviewer resource and related review
        Scorecard scorecard1 = createScorecard(1000, 1, 2, 1, "name", "1.0", 75.0f, 100.0f);
        Review frWorksheet = createReview(111, finalReviewer.getId(), frSubmission.getId(), scorecard1.getId(), true,
                90.0f);

        // add a rejected comment
        frWorksheet.addComment(createComment(1111, finalReviewer.getId(), "Approved", 10, "Final Review Comment"));

        Connection conn = getConnection();

        // insert records
        insertResources(conn, new Resource[] { finalReviewer });
        insertResourceInfo(conn, finalReviewer.getId(), 1, "2");
        insertUploads(conn, new Upload[] { frUpload });
        insertSubmissions(conn, new Submission[] { frSubmission });
        insertResourceSubmission(conn, finalReviewer.getId(), frSubmission.getId());
        insertScorecards(conn, new Scorecard[] { scorecard1 });
        insertReviews(conn, new Review[] { frWorksheet });
        insertCommentsWithExtraInfo(conn, new long[] { 1 }, new long[] { finalReviewer.getId() },
            new long[] { frWorksheet.getId() }, new String[] { "Rejected Comment" }, new long[] { 10 },
            new String[] { "Approved" });
        insertScorecardQuestion(conn, 1, scorecard1.getId());

        handler.perform(finalReviewPhase, "1001");
    }
}
