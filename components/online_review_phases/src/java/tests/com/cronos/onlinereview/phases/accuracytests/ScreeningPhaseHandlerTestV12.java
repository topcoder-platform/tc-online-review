/**
 * 
 */

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
 * Accuracy tests for V1.2 <code>ScreeningPhaseHandler</code>.
 *
 * @author assistant
 * @version 1.2
 */
public class ScreeningPhaseHandlerTestV12 extends BaseTestCase {
    /** Instance to test. */
    private ScreeningPhaseHandler instance;

    /**
     * Sets up the environment.
     *
     * @throws java.lang.Exception to JUnit
     */
    protected void setUp() throws Exception {
        super.setUp();
        instance = new ScreeningPhaseHandler();
    }

    /**
     * Cleans up the environment.
     *
     * @throws java.lang.Exception to JUnit
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test method for {@link com.cronos.onlinereview.phases.ScreeningPhaseHandler
     * #perform(com.topcoder.project.phases.Phase, java.lang.String)}.
     *
     * @throws Exception to JUnit
     */
    public void testPerform_1() throws Exception {
        Project project = setupProjectResourcesNotification("Screening", true);
        Phase submission = project.getAllPhases()[2];
        submission.setPhaseStatus(PhaseStatus.OPEN);

        Connection conn = getConnection();

        //insert screener
        Resource screener = createResource(6, submission.getId(), 1, 3);
        super.insertResources(conn, new Resource[] { screener });
        insertResourceInfo(conn, screener.getId(), 1, "2");

        // create a registrant
        Resource resource = createResource(4, 101L, 1, 1);
        insertResources(conn, new Resource[] { resource });
        insertResourceInfo(conn, resource.getId(), 1, "4");
        insertResourceInfo(conn, resource.getId(), 2, "ACRush");
        insertResourceInfo(conn, resource.getId(), 4, "3808");
        insertResourceInfo(conn, resource.getId(), 5, "100");

        Upload upload = super.createUpload(1, project.getId(), resource.getId(), 1, 1, "Paramter");
        super.insertUploads(conn, new Upload[] { upload });

        Submission sub = super.createSubmission(1, upload.getId(), 1);
        super.insertSubmissions(conn, new Submission[] { sub });

        Scorecard scorecard = createScorecard(1, 1, 1, 1, "name", "1.0", 75.0f, 100.0f);

        //insert a screening review
        Review screenReview = createReview(11, screener.getId(), sub.getId(), scorecard.getId(), true, 90.0f);

        this.insertScorecards(conn, new Scorecard[] { scorecard });
        this.insertReviews(conn, new Review[] { screenReview });

        instance.perform(submission, "1001");

        // the subject should be Phase End: Online Review Phases
        // there should be one submission information in the email
        // manager/observer/reviewer should receive the email
        closeConnection();
    }

    /**
     * Test method for {@link com.cronos.onlinereview.phases.ScreeningPhaseHandler
     * #perform(com.topcoder.project.phases.Phase, java.lang.String)}.
     *
     * @throws Exception to JUnit
     */
    public void testPerform_2() throws Exception {
        Project project = setupProjectResourcesNotification("Screening", true);
        Phase submission = project.getAllPhases()[2];
        submission.setPhaseStatus(PhaseStatus.OPEN);

        Connection conn = getConnection();

        //insert screener
        Resource screener = createResource(6, submission.getId(), 1, 3);
        super.insertResources(conn, new Resource[] { screener });
        insertResourceInfo(conn, screener.getId(), 1, "2");

        // create a registrant
        Resource resource = createResource(4, 101L, 1, 1);
        insertResources(conn, new Resource[] { resource });
        insertResourceInfo(conn, resource.getId(), 1, "4");
        insertResourceInfo(conn, resource.getId(), 2, "ACRush");
        insertResourceInfo(conn, resource.getId(), 4, "3808");
        insertResourceInfo(conn, resource.getId(), 5, "100");

        Upload upload = super.createUpload(1, project.getId(), resource.getId(), 1, 1, "Paramter");
        super.insertUploads(conn, new Upload[] { upload });

        Submission sub = super.createSubmission(1, upload.getId(), 1);
        super.insertSubmissions(conn, new Submission[] { sub });

        Scorecard scorecard = createScorecard(1, 1, 1, 1, "name", "1.0", 75.0f, 100.0f);

        //insert a screening review
        Review screenReview = createReview(11, screener.getId(), sub.getId(), scorecard.getId(), true, 0.0f);

        this.insertScorecards(conn, new Scorecard[] { scorecard });
        this.insertReviews(conn, new Review[] { screenReview });

        instance.perform(submission, "1001");

        // the subject should be Phase End: Online Review Phases
        // there should be one submission information in the email
        // manager/observer/reviewer should receive the email
        closeConnection();
    }

    /**
     * Test method for {@link com.cronos.onlinereview.phases.ScreeningPhaseHandler
     * #perform(com.topcoder.project.phases.Phase, java.lang.String)}.
     *
     * @throws Exception to JUnit
     */
    public void testPerform_3() throws Exception {
        // in this case, the phase starts without screener
        Project project = setupProjectResourcesNotification("Screening", true);
        Phase submission = project.getAllPhases()[2];

        Connection conn = getConnection();

        // create a registrant
        Resource resource = createResource(4, 101L, 1, 1);
        insertResources(conn, new Resource[] { resource });
        insertResourceInfo(conn, resource.getId(), 1, "4");
        insertResourceInfo(conn, resource.getId(), 2, "ACRush");
        insertResourceInfo(conn, resource.getId(), 4, "3808");
        insertResourceInfo(conn, resource.getId(), 5, "100");

        // open screening
        submission.setPhaseStatus(PhaseStatus.SCHEDULED);

        Upload upload = super.createUpload(1, project.getId(), resource.getId(), 1, 1, "Paramter");
        super.insertUploads(conn, new Upload[] { upload });

        Submission sub = super.createSubmission(1, upload.getId(), 1);
        super.insertSubmissions(conn, new Submission[] { sub });

        instance.perform(submission, "1001");

        // the subject should be Phase End: Online Review Phases
        // there should be one submission information in the email
        // manager/observer/reviewer should receive the email
        closeConnection();
    }

    /**
     * Test method for {@link com.cronos.onlinereview.phases.ScreeningPhaseHandler
     * #perform(com.topcoder.project.phases.Phase, java.lang.String)}.
     *
     * @throws Exception to JUnit
     */
    public void testPerform_4() throws Exception {
        // in this case, the phase starts with screener
        Project project = setupProjectResourcesNotification("Screening", true);
        Phase submission = project.getAllPhases()[2];

        Connection conn = getConnection();

        // create a registrant
        Resource resource = createResource(4, 101L, 1, 1);
        insertResources(conn, new Resource[] { resource });
        insertResourceInfo(conn, resource.getId(), 1, "4");
        insertResourceInfo(conn, resource.getId(), 2, "ACRush");
        insertResourceInfo(conn, resource.getId(), 4, "3808");
        insertResourceInfo(conn, resource.getId(), 5, "100");

        // open screening
        submission.setPhaseStatus(PhaseStatus.SCHEDULED);

        Upload upload = super.createUpload(1, project.getId(), resource.getId(), 1, 1, "Paramter");
        super.insertUploads(conn, new Upload[] { upload });

        Submission sub = super.createSubmission(1, upload.getId(), 1);
        super.insertSubmissions(conn, new Submission[] { sub });

        //insert screener
        Resource screener = createResource(6, submission.getId(), 1, 3);
        super.insertResources(conn, new Resource[] { screener });
        insertResourceInfo(conn, screener.getId(), 1, "2");

        instance.perform(submission, "1001");

        // the subject should be Phase End: Online Review Phases
        // there should be one submission information in the email
        // manager/observer/reviewer should receive the email
        closeConnection();
    }
}
