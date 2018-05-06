/*
 * Copyright (C) 2009 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases.accuracytests;

import com.cronos.onlinereview.phases.ScreeningPhaseHandler;

import com.topcoder.management.deliverable.Submission;
import com.topcoder.management.deliverable.Upload;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.review.data.Review;
import com.topcoder.management.scorecard.data.Scorecard;

import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.PhaseStatus;
import com.topcoder.project.phases.Project;

import java.sql.Connection;


/**
 * Accuracy tests for change functions in version 1.1 of ScreeningPhaseHandler class.
 *
 * @author myxgyy
 * @version 1.0
 */
public class ScreeningPhaseHandlerAccTestsV11 extends BaseTestCase {
    /** Target instance. */
    private ScreeningPhaseHandler handler;

    /**
     * Sets up the environment required for test cases for this class.
     *
     * @throws Exception not under test.
     */
    protected void setUp() throws Exception {
        super.setUp();
        handler = new ScreeningPhaseHandler(PHASE_HANDLER_NAMESPACE);
    }

    /**
     * Cleans up the environment required for test cases for this class.
     *
     * @throws Exception not under test.
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Tests the perform. No submission passes review, Post-mortem phase should be inserted.
     *
     * @throws Exception to JUnit.
     */
    public void testPerform() throws Exception {
        // test with scheduled status.
        Project project = super.setupPhases();
        Phase[] phases = project.getAllPhases();
        Phase screenPhase = phases[2];
        insertProjectInfo(getConnection(), 1, new long[] {44}, new String[] {"true"});

        screenPhase.setPhaseStatus(PhaseStatus.OPEN);

        // 1. insert submitter
        Resource submitter = createResource(102, 102, project.getId(), 1);

        Connection conn = getConnection();

        // insert records
        insertResources(conn, new Resource[] { submitter });

        // we need to insert an external reference id
        // which references to resource's user id in resource_info table
        insertResourceInfo(conn, submitter.getId(), 1, "1002");

        // 2. insert primary screener
        Resource primaryScreener = createResource(101, screenPhase.getId(), project.getId(), 2);

        // insert records
        insertResources(conn, new Resource[] { primaryScreener });

        // we need to insert an external reference id
        // which references to resource's user id in resource_info table
        insertResourceInfo(conn, primaryScreener.getId(), 1, "1001");

        // 3. insert submission
        Upload submitterUpload = createUpload(102, project.getId(), submitter.getId(), 1, 1, "parameter");
        Submission submission = createSubmission(102, submitterUpload.getId(), 1);
        submission.setInitialScore(89.92d);
        submission.setFinalScore(93.92d);
        submission.setScreeningScore(100.00d);
        submission.setPlacement(new Long(2));
        submission.setUpload(submitterUpload);

        this.insertUploads(conn, new Upload[] { submitterUpload });
        this.insertSubmissions(conn, new Submission[] { submission });

        // 4. insert a scorecard here
        Scorecard scorecard = createScorecard(1, 1, 1, 1, "name", "1.0", 75.0f, 100.0f);

        // 5. insert a screening review
        Review screenReview = createReview(11, primaryScreener.getId(), submission.getId(), scorecard.getId(), true,
                65.0f);

        this.insertScorecards(conn, new Scorecard[] { scorecard });
        this.insertReviews(conn, new Review[] { screenReview });

        handler.canPerform(screenPhase);

        // score not passed
        handler.perform(screenPhase, "1001");

        // check whether the post-mortem phase is inserted
        assertTrue("Post-mortem phase should be inserted", havePostMortemPhase(conn));
    }
}
