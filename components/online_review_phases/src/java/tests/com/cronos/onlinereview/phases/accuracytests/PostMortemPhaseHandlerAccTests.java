/*
 * Copyright (C) 2009 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases.accuracytests;

import com.cronos.onlinereview.phases.PostMortemPhaseHandler;

import com.topcoder.management.deliverable.Submission;
import com.topcoder.management.deliverable.Upload;
import com.topcoder.management.phase.OperationCheckResult;
import com.topcoder.management.phase.PhaseHandler;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.review.data.Review;
import com.topcoder.management.scorecard.data.Scorecard;

import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.PhaseStatus;
import com.topcoder.project.phases.Project;

import java.sql.Connection;

import java.util.Date;


/**
 * Accuracy tests for the new added phase handler <code>PostMortemPhaseHandler</code> in version 1.1.
 *
 * @author myxgyy
 * @version 1.0
 */
public class PostMortemPhaseHandlerAccTests extends BaseTestCase {
    /** Target instance. */
    private PostMortemPhaseHandler handler;

    /**
     * sets up the environment required for test cases for this class.
     *
     * @throws Exception to JUnit.
     */
    protected void setUp() throws Exception {
        super.setUp();
        handler = new PostMortemPhaseHandler(PHASE_HANDLER_NAMESPACE);
    }

    /**
     * cleans up the environment required for test cases for this class.
     *
     * @throws Exception to JUnit.
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Tests the default constructor of PostMortemPhaseHandler.
     *
     * @throws Exception to JUnit.
     */
    public void testDefaultConstructor() throws Exception {
        PhaseHandler h = new PostMortemPhaseHandler();
        assertNotNull("PostMortemPhaseHandler should be correctly created.", h);
    }

    /**
     * Tests the canPerform with Scheduled statuses.
     *
     * @throws Exception to JUnit.
     */
    public void testCanPerform() throws Exception {
        Project project = super.setupPhasesWithPostMortem();
        Phase[] phases = project.getAllPhases();
        Phase postMortemPhase = phases[11];

        // test with scheduled status.
        postMortemPhase.setPhaseStatus(PhaseStatus.SCHEDULED);

        // time has not passed
        OperationCheckResult result = handler.canPerform(postMortemPhase);

        assertFalse("Not the expected checking result", result.isSuccess());
        assertEquals("Wrong message",  "Dependency Approval phase is not yet ended.",  result.getMessage());

        // time has passed, but dependency not met.
        postMortemPhase.setActualStartDate(new Date());
        result = handler.canPerform(postMortemPhase);

        assertFalse("Not the expected checking result", result.isSuccess());
        assertEquals("Wrong message",  "Dependency Approval phase is not yet ended.",  result.getMessage());

        // set the number of required post-mortem reviewer to 1
        postMortemPhase.setAttribute("Reviewer Number", "1");

        // time has passed and dependency met
        postMortemPhase.getAllDependencies()[0].getDependency().setPhaseStatus(PhaseStatus.CLOSED);
         result = handler.canPerform(postMortemPhase);

        assertTrue("Not the expected checking result", result.isSuccess());
        assertEquals("Wrong message",  null,  result.getMessage());
    }

    /**
     * Tests the canPerform with Open statuses.
     *
     * @throws Exception to JUnit.
     */
    public void testCanPerform1() throws Exception {
        Project project = super.setupPhasesWithPostMortem();
        Phase[] phases = project.getAllPhases();
        Phase postMortemPhase = phases[11];

        // change dependency type to false
        postMortemPhase.getAllDependencies()[0].setDependentStart(false);

        // test with open status.
        postMortemPhase.setPhaseStatus(PhaseStatus.OPEN);

        // dependencies not met
        OperationCheckResult result = handler.canPerform(postMortemPhase);

        assertFalse("Not the expected checking result", result.isSuccess());
        assertEquals("Wrong message",  "Dependency Approval phase is not yet ended.",  result.getMessage());

        // remove all dependencies
        for (int i = 0; i < postMortemPhase.getAllDependencies().length; ++i) {
            postMortemPhase.removeDependency(postMortemPhase.getAllDependencies()[i]);
        }

        // set the number of required post-mortem reviewer to 1
        postMortemPhase.setAttribute("Reviewer Number", "1");

        // dependencies met but without post-mortem reviewer
        result = handler.canPerform(postMortemPhase);

        assertFalse("Not the expected checking result", result.isSuccess());
        assertEquals("Wrong message",  "Phase end time is not yet reached",  result.getMessage());

        // insert post-mortem reviewer
        Resource postMortemReviewer = createResource(101, postMortemPhase.getId(), project.getId(), 14);

        Connection conn = getConnection();

        // insert records
        insertResources(conn, new Resource[] { postMortemReviewer });

        // we need to insert an external reference id
        // which references to resource's user id in resource_info table
        insertResourceInfo(conn, postMortemReviewer.getId(), 1, "1001");

        // there is not scorecard filled
        result = handler.canPerform(postMortemPhase);

        assertFalse("Not the expected checking result", result.isSuccess());
        assertEquals("Wrong message",  "Phase end time is not yet reached",  result.getMessage());

        // insert a scorecard here
        Upload upload = createUpload(101, project.getId(), postMortemReviewer.getId(), 4, 1, "parameter");
        Submission submission = createSubmission(101, upload.getId(), 1);
        submission.setUpload(upload);

        Scorecard scorecard1 = createScorecard(1, 1, 2, 1, "name", "1.0", 75.0f, 100.0f);

        // scorecard not committed
        Review postMortemScorecard = createReview(11, postMortemReviewer.getId(), submission.getId(),
                scorecard1.getId(), false, 90.0f);

        this.insertUploads(conn, new Upload[] { upload });
        this.insertSubmissions(conn, new Submission[] { submission });
        this.insertScorecards(conn, new Scorecard[] { scorecard1 });
        this.insertReviews(conn, new Review[] { postMortemScorecard });

        // scorecard not committed return false
        result = handler.canPerform(postMortemPhase);

        assertFalse("Not the expected checking result", result.isSuccess());
        assertEquals("Wrong message",  "Phase end time is not yet reached",  result.getMessage());
    }

    /**
     * Tests the canPerform with Open statuses.
     *
     * @throws Exception to JUnit.
     */
    public void testCanPerform2() throws Exception {
        Project project = super.setupPhasesWithPostMortem();
        Phase[] phases = project.getAllPhases();
        Phase postMortemPhase = phases[11];

        // test with open status.
        postMortemPhase.setPhaseStatus(PhaseStatus.OPEN);

        // time passed
        postMortemPhase.setActualStartDate(new Date(System.currentTimeMillis() - 1000));
        postMortemPhase.setActualEndDate(new Date());
        // remove all dependencies
        for (int i = 0; i < postMortemPhase.getAllDependencies().length; ++i) {
            postMortemPhase.removeDependency(postMortemPhase.getAllDependencies()[i]);
        }

        // set the number of required post-mortem reviewer to 1
        postMortemPhase.setAttribute("Reviewer Number", "1");

        // insert post-mortem reviewer
        Resource postMortemReviewer = createResource(101, postMortemPhase.getId(), project.getId(), 16);

        Connection conn = getConnection();

        // insert records
        insertResources(conn, new Resource[] { postMortemReviewer });

        // we need to insert an external reference id
        // which references to resource's user id in resource_info table
        insertResourceInfo(conn, postMortemReviewer.getId(), 1, "1001");

        // insert a scorecard here
        Upload upload = createUpload(101, project.getId(), postMortemReviewer.getId(), 4, 1, "parameter");
        Submission submission = createSubmission(101, upload.getId(), 1);
        submission.setUpload(upload);

        Scorecard scorecard1 = createScorecard(1, 1, 2, 1, "name", "1.0", 75.0f, 100.0f);

        // insert a committed scorecard
        Review postMortemScorecard = createReview(11, postMortemReviewer.getId(), submission.getId(),
                scorecard1.getId(), true, 90.0f);

        this.insertUploads(conn, new Upload[] { upload });
        this.insertSubmissions(conn, new Submission[] { submission });
        this.insertScorecards(conn, new Scorecard[] { scorecard1 });
        this.insertReviews(conn, new Review[] { postMortemScorecard });

        // scorecard committed
        OperationCheckResult result = handler.canPerform(postMortemPhase);

        assertTrue("Not the expected checking result", result.isSuccess());
        assertEquals("Wrong message",  null,  result.getMessage());
    }
}
