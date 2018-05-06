/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases.stresstests;

import java.sql.Connection;
import java.util.Date;

import com.cronos.onlinereview.phases.SpecificationReviewPhaseHandler;
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
 * This class is the stress test of class <code>SpecificationReviewPhaseHandler</code> in
 * multi-threaded environment.
 * </p>
 *
 * <p>
 * </b>Note:</b> According to TopCoder's convention, for the specification review phase, there would
 * not be more than 3 reviewers and each reviewer will upload only one submission (review-result)
 * and one scorecard. So I don't think it is necessary to test with a large number of reviewers or
 * submissions. Instead, this component is very likely to run under a concurrent web-environment.
 * Therefore, I wrote the test in a multi-threaded environment to guarantee that the new added
 * functionalities in version 1.4 are thread safe.
 * </p>
 *
 * @author TopCoder
 * @version 1.0
 */
public class SpecificationReviewPhaseHandlerStressTest extends StressBaseTest {

    /**
     * The last exception instance for stress test.
     */
    private Exception lastException = null;

    /**
     * The result of calling method <code>canPerform</code>.
     */
    private boolean canPerformResult;

    /**
     * The <code>SpecificationReviewPhaseHandler</code> class instance for stress test.
     */
    private SpecificationReviewPhaseHandler handler;

    /**
     * The review phase for stress test.
     */
    private Phase reviewPhase;

    /**
     * The submission phase for stress test.
     */
    private Phase submissionPhase;

    /**
     * The expected result for stress test.
     */
    private boolean expectedResult;

    /**
     * The operator for stress test.
     */
    private String operator = "1001";

    /**
     * <p>
     * Setup test environment.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void setUp() throws Exception {
        super.setUp();

        ConfigManager configManager = ConfigManager.getInstance();
        configManager.add(PHASE_HANDLER_CONFIG_FILE);
        configManager.add(DOC_GENERATOR_CONFIG_FILE);
        configManager.add(EMAIL_CONFIG_FILE);
        configManager.add(MANAGER_HELPER_CONFIG_FILE);

        for (int i = 0; i < COMPONENT_FILE_NAMES.length; i++) {
            configManager.add(COMPONENT_FILE_NAMES[i]);
        }

        handler = new SpecificationReviewPhaseHandler();
    }

    /**
     * <p>
     * Tear down the test environment.
     * </p>
     *
     * @throws Exception if any error occurs
     */
    protected void tearDown() throws Exception {
        cleanTables();
        closeConnection();
        handler = null;
    }

    // ----------------------------- Test Method ----------------------------------- //

    /**
     * <p>
     * Tests whether the method <code>canPerform</code> works correctly in multi-threaded
     * environment.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testCanPerformStress() throws Exception {
        initCanPerform();

        // get the result in single-threaded mode
        canPerformResult = runCanPerform();

        // test the single-threaded result
        assertEquals(expectedResult, canPerformResult);

        Thread[] threads = new Thread[THREADS];
        for (int i = 0; i < THREADS; i++) {
            threads[i] = new Thread(new CanPerformThread());
        }
        Date d0 = new Date();
        for (int i = 0; i < THREADS; i++) {
            threads[i].start();
        }
        for (int i = 0; i < THREADS; i++) {
            threads[i].join();
        }
        Date d1 = new Date();
        if (lastException != null) {
            System.out.println("There was exception in threads" + lastException);
            throw lastException;
        }
        System.out.println("Running " + THREADS
            + " threads to test method [SpecificationReviewPhaseHandler#canPerform]; each took "
            + (d1.getTime() - d0.getTime()) + " milliseconds");
    }

    /**
     * <p>
     * Tests whether the method <code>perform</code> works correctly in multi-threaded environment.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testPerformStress() throws Exception {
        initializePerform();

        runPerform(); // no exception is acceptable

        Thread[] threads = new Thread[THREADS];
        for (int i = 0; i < THREADS; i++) {
            threads[i] = new Thread(new PerformThread());
        }
        Date d0 = new Date();
        for (int i = 0; i < THREADS; i++) {
            threads[i].start();
        }
        for (int i = 0; i < THREADS; i++) {
            threads[i].join();
        }
        Date d1 = new Date();
        if (lastException != null) {
            System.out.println("There was exception in threads" + lastException);
            throw lastException;
        }
        System.out.println("Running " + THREADS
            + " threads to test method [SpecificationReviewPhaseHandler#perform]; each took "
            + (d1.getTime() - d0.getTime()) + " milliseconds");
    }

    // ----------------------------- Utility Method ----------------------------------- //

    /**
     * Initializes the online review environment for testing method <code>perform</code>.
     *
     * @throws Exception
     */
    private void initCanPerform() throws Exception {
        cleanTables();

        Project project = generateSpecificationPhases(PROJECT_ID, false);

        submissionPhase = project.getAllPhases()[0];
        reviewPhase = project.getAllPhases()[1];
        reviewPhase.setPhaseStatus(PhaseStatus.OPEN);
        reviewPhase.setActualStartDate(new Date());

        reviewPhase.getAllDependencies()[0].getDependency().setPhaseStatus(PhaseStatus.CLOSED);

        Connection conn = getConnection();

        long reviewPhaseId = reviewPhase.getId();
        Resource submitter = super.createResource(1, submissionPhase.getId(), 1, 1);
        Resource reviewer = super.createResource(3, reviewPhaseId, 1, 18);

        Upload upload = createUpload(1, 1, submitter.getId(), 1, 1, "parameter");
        Submission submission = createSubmission(1, 1, 1, 1);
        Scorecard scorecard = createScorecard(1, 1, 1, 1, "name", "1.0", 75.0f, 100.0f);
        Review review = createReview(1, reviewer.getId(), submission.getId(), scorecard.getId(), false, 80.0f);
        expectedResult = review.isCommitted();

        insertResources(conn, new Resource[]{submitter, reviewer });
        insertResourceInfo(conn, submitter.getId(), 1, "11111");
        insertResourceInfo(conn, reviewer.getId(), 1, "11112");
        insertUploads(conn, new Upload[]{upload });
        insertSubmissions(conn, new Submission[]{submission });
        insertScorecards(conn, new Scorecard[]{scorecard });
        insertReviews(conn, new Review[]{review });
    }

    /**
     * Initializes the online review environment for testing method <code>perform</code>.
     *
     * @throws Exception
     */
    private void initializePerform() throws Exception {
        Project project = generateSpecificationPhases(PROJECT_ID, false);

        submissionPhase = project.getAllPhases()[0];
        reviewPhase = project.getAllPhases()[1];

        Connection conn = getConnection();

        long reviewPhaseId = reviewPhase.getId();
        Resource submitter = super.createResource(1, submissionPhase.getId(), 1, 1);
        Resource reviewer = super.createResource(3, reviewPhaseId, 1, 18);

        Upload upload = createUpload(1, 1, submitter.getId(), 1, 1, "parameter");
        Submission submission = createSubmission(1, 1, 1, 1);
        Scorecard scorecard = createScorecard(1, 1, 1, 1, "name", "1.0", 75.0f, 100.0f);
        Review review = createReview(1, reviewer.getId(), submission.getId(), scorecard.getId(), true, 80.0f);

        insertResources(conn, new Resource[]{submitter, reviewer });
        insertResourceInfo(conn, submitter.getId(), 1, "11111");
        insertResourceInfo(conn, reviewer.getId(), 1, "11112");
        insertUploads(conn, new Upload[]{upload });
        insertSubmissions(conn, new Submission[]{submission });
        insertScorecards(conn, new Scorecard[]{scorecard });
        insertReviews(conn, new Review[]{review });
    }

    /**
     * <p>
     * Tests whether the method <code>canPerform</code> works correctly in single-threaded
     * environment.
     * </p>
     *
     * @return the boolean value
     * @throws Exception to JUnit
     */
    private boolean runCanPerform() throws Exception {
        return handler.canPerform(reviewPhase);
    }

    /**
     * <p>
     * Tests whether the method <code>canPerform</code> works correctly in single-threaded
     * environment.
     * </p>
     *
     * @throws Exception to JUnit
     */
    private void runPerform() throws Exception {
        handler.perform(reviewPhase, operator);
    }

    /**
     * <p>
     * Checks correctness of the content sent and received. If exception happens - writes to
     * LastException.
     * </p>
     *
     * @author TCSDEVELOPER
     * @version 1.0
     */
    private class CanPerformThread implements Runnable {
        /**
         * <p>
         * Checks correctness and, if exception happens - writes to LastException.
         * </p>
         */
        public void run() {
            try {
                // get the result in a multi-threaded mode
                boolean resultMultithreaded = runCanPerform();

                // test the result, should be equal to the single-threaded result
                assertEquals(
                    "The data sent and received in multi-threaded environment should not be the same!",
                    canPerformResult, resultMultithreaded);
            } catch (Exception e) {
                lastException = e;
            }
        }
    }

    /**
     * <p>
     * Checks correctness of the content sent and received. If exception happens - writes to
     * LastException.
     * </p>
     *
     * @author TCSDEVELOPER
     * @version 1.0
     */
    private class PerformThread implements Runnable {
        /**
         * <p>
         * Checks correctness and, if exception happens - writes to LastException.
         * </p>
         */
        public void run() {
            try {
                // since this method returns nothing, the test will expect no exception thrown.
                runPerform();
            } catch (Exception e) {
                lastException = e;
            }
        }
    }
}
