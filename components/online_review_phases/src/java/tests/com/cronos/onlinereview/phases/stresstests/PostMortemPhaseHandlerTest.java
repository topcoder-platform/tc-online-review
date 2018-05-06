/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases.stresstests;

import java.sql.Connection;
import java.util.Date;

import com.cronos.onlinereview.phases.PostMortemPhaseHandler;
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
 * PostMortemPhaseHandlerTest.
 *
 * @author TCSDEVELOPER
 * @version 1.3
 */
public class PostMortemPhaseHandlerTest extends StressBaseTest {

    /**
     * sets up the environment required for test cases for this class.
     *
     * @throws Exception
     *             to JUnit.
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
     * cleans up the environment required for test cases for this class.
     *
     * @throws Exception
     *             to JUnit.
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Tests the PostMortemPhaseHandler() constructor and canPerform with Open statuses.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testCanPerformWithOpen1() throws Exception {
        PostMortemPhaseHandler handler = new PostMortemPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            cleanTables();

            Project project = super.setupPhasesWithPostMortem();
            Phase[] phases = project.getAllPhases();
            Phase postMortemPhase = phases[11];

            // change dependency type to false
            postMortemPhase.getAllDependencies()[0].setDependentStart(false);

            // test with open status.
            postMortemPhase.setPhaseStatus(PhaseStatus.OPEN);

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
            insertResources(conn, new Resource[]{postMortemReviewer });

            // we need to insert an external reference id
            // which references to resource's user id in resource_info table
            insertResourceInfo(conn, postMortemReviewer.getId(), 1, "1001");

            // insert a scorecard here
            Upload upload = createUpload(101, project.getId(), postMortemReviewer.getId(), 4, 1, "parameter");
            Submission submission = createSubmission(101, upload.getId(), 1);
            submission.setUpload(upload);

            Scorecard scorecard1 = createScorecard(1, 1, 2, 1, "name", "1.0", 75.0f, 100.0f);

            // scorecard not committed
            Review postMortemScorecard = createReview(11, postMortemReviewer.getId(), submission.getId(),
                scorecard1.getId(), false, 90.0f);

            this.insertUploads(conn, new Upload[]{upload });
            this.insertSubmissions(conn, new Submission[]{submission });
            this.insertScorecards(conn, new Scorecard[]{scorecard1 });
            this.insertReviews(conn, new Review[]{postMortemScorecard });

            startRecord();
            for (int i = 0; i < FIRST_LEVEL * 10; i++) {
                handler.canPerform(postMortemPhase);
            }
            endRecord("PostMortemPhaseHandler::canPerform(Phase)--(the phase status is false)",
                FIRST_LEVEL * 10);
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * Tests the PostMortemPhaseHandler() constructor and canPerform with Scheduled statuses.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testCanPerformWithScheduled() throws Exception {
        PostMortemPhaseHandler handler = new PostMortemPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            cleanTables();

            Project project = super.setupPhasesWithPostMortem();
            Phase[] phases = project.getAllPhases();
            Phase postMortemPhase = phases[11];

            // test with scheduled status.
            postMortemPhase.setPhaseStatus(PhaseStatus.SCHEDULED);

            // time has passed, but dependency not met.
            postMortemPhase.setActualStartDate(new Date());

            // set the number of required post-mortem reviewer to 1
            postMortemPhase.setAttribute("Reviewer Number", "1");

            // time has passed and dependency met
            postMortemPhase.getAllDependencies()[0].getDependency().setPhaseStatus(PhaseStatus.CLOSED);

            startRecord();
            for (int i = 0; i < FIRST_LEVEL; i++) {
                handler.canPerform(postMortemPhase);
            }
            endRecord("PostMortemPhaseHandler::canPerform(Phase)--(the phase status is true)", FIRST_LEVEL);
        } finally {
            cleanTables();
        }
    }

    /**
     * Tests the perform with Scheduled statuses.
     *
     * @throws Exception
     *             the exception occurs
     */
    public void testPerform_start_with_reviewer() throws Exception {
        PostMortemPhaseHandler handler = new PostMortemPhaseHandler(PostMortemPhaseHandler.DEFAULT_NAMESPACE);

        try {
            cleanTables();

            Project project = setupProjectResourcesNotification("All", true);
            Phase[] phases = project.getAllPhases();
            Phase postMortemPhase = phases[11];

            // assign reviewer
            Resource postMortemReviewer = createResource(1011, postMortemPhase.getId(), project.getId(), 14);
            Connection conn = getConnection();
            insertResources(conn, new Resource[]{postMortemReviewer });
            insertResourceInfo(conn, postMortemReviewer.getId(), 1, "2");

            startRecord();
            for (int i = 0; i < FIRST_LEVEL; i++) {
                handler.perform(postMortemPhase, "1001");
            }
            endRecord("PostMortemPhaseHandler::perform(Phase, String)--(the phase status is true)",
                FIRST_LEVEL);
        } finally {
            cleanTables();
        }
    }
}
