/**
 * Copyright (c) 2013, TopCoder, Inc. All rights reserved
 */
package com.topcoder.management.review.assignment.accuracytests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import junit.framework.JUnit4TestAdapter;

import org.junit.Before;
import org.junit.Test;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.management.project.ProjectManager;
import com.topcoder.management.review.application.ReviewApplication;
import com.topcoder.management.review.application.ReviewApplicationManager;
import com.topcoder.management.review.application.ReviewApplicationStatus;
import com.topcoder.management.review.application.ReviewAuction;
import com.topcoder.management.review.application.ReviewAuctionManager;
import com.topcoder.management.review.assignment.ReviewAssignmentAlgorithm;
import com.topcoder.management.review.assignment.ReviewAssignmentManager;
import com.topcoder.management.review.assignment.ReviewAssignmentNotificationManager;
import com.topcoder.management.review.assignment.ReviewAssignmentProjectManager;

/**
 * <p>
 * Unit tests for {@link ReviewAssignmentManager} class. <br/>
 * </p>
 *
 * @author KennyAlive
 * @version 1.0
 */
public class ReviewAssignmentManagerAccuracyTests {
    /**
     * Constant for configuration namespace for accuracy tests.
     */
    private static final String CONFIGURATION = "ReviewAssignmentManager";

    /**
     * The {@code  ReviewAssignmentManager} instance used for testing.
     */
    private ReviewAssignmentManager instance;

    /**
     * The configuration for ReviewAssignmentManager.
     */
    private ConfigurationObject configuration;

    /**
     * Creates a suite with all test methods for JUnix3.x runner.
     *
     * @return a test suite.
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(ReviewAssignmentManagerAccuracyTests.class);
    }

    /**
     * Sets up the test environment.
     *
     * @throws Exception
     *             to JUnit
     */
    @Before
    public void setUp() throws Exception {
        Helper.preloadConfiguration();
        configuration = Helper.getConfiguration(CONFIGURATION);
    }

    /**
     * Accuracy test for {@code configure} method.
     *
     * @throws Exception
     *             to JUnit
     */
    @Test
    public void test_configure_1() throws Exception {
        try {
            Helper.clearDB();
            Helper.insertDB();

            instance = new ReviewAssignmentManager(configuration);

            ReviewApplicationStatus pendingReviewApplicationStatus = (ReviewApplicationStatus) Helper.getField(
                    instance,
                    "pendingReviewApplicationStatus");
            assertNotNull("The pendingReviewApplicationStatus should not be null", pendingReviewApplicationStatus);

            ReviewApplicationStatus approvedReviewApplicationStatus = (ReviewApplicationStatus) Helper.getField(
                    instance,
                    "approvedReviewApplicationStatus");
            assertNotNull("The approvedReviewApplicationStatus should not be null", approvedReviewApplicationStatus);

            ReviewApplicationStatus rejectedReviewApplicationStatus = (ReviewApplicationStatus) Helper.getField(
                    instance,
                    "rejectedReviewApplicationStatus");
            assertNotNull("The rejectedReviewApplicationStatus should not be null", rejectedReviewApplicationStatus);

            ReviewAuctionManager reviewAuctionManager = (ReviewAuctionManager) Helper.getField(instance,
                    "reviewAuctionManager");
            assertNotNull("The reviewAuctionManager should not be null", reviewAuctionManager);

            ReviewApplicationManager reviewApplicationManager = (ReviewApplicationManager) Helper.getField(instance,
                    "reviewApplicationManager");
            assertNotNull("The reviewApplicationManager should not be null", reviewApplicationManager);

            ProjectManager projectManager = (ProjectManager) Helper.getField(instance,
                    "projectManager");
            assertNotNull("The projectManager should not be null", projectManager);

            ReviewAssignmentAlgorithm reviewAssignmentAlgorithm = (ReviewAssignmentAlgorithm) Helper.getField(instance,
                    "reviewAssignmentAlgorithm");
            assertNotNull("The reviewAssignmentAlgorithm should not be null", reviewAssignmentAlgorithm);

            ReviewAssignmentProjectManager reviewAssignmentProjectManager = (ReviewAssignmentProjectManager) Helper
                    .getField(instance, "reviewAssignmentProjectManager");
            assertNotNull("The reviewAssignmentProjectManager should not be null", reviewAssignmentProjectManager);

            ReviewAssignmentNotificationManager reviewAssignmentNotificationManager = (ReviewAssignmentNotificationManager) Helper
                    .getField(instance, "reviewAssignmentNotificationManager");
            assertNotNull("The reviewAssignmentNotificationManager should not be null",
                    reviewAssignmentNotificationManager);
        } finally {
            Helper.clearDB();
        }
    }

    /**
     * Accuracy test for {@code execute} method.
     *
     * Preconditions: single applicatation
     *
     * Expected behavior: no errors, correct emails on smpt server, the reviewer should be added to the project
     *
     * @throws Exception
     *             to JUnit
     */
    @Test
    public void test_execute_1() throws Exception {
        try {
            Helper.clearDB();
            Helper.insertDB();
            instance = new ReviewAssignmentManager(configuration);

            ReviewAuctionManager auctionManager = (ReviewAuctionManager) Helper.getField(instance,
                    "reviewAuctionManager");

            long projectId = 11L;
            ReviewAuction auction = Helper.createReviewAuction_singleRole(1L);
            auction.setProjectId(projectId);
            auctionManager.createAuction(auction);

            ReviewApplicationManager applicationManager = (ReviewApplicationManager) Helper.getField(instance,
                    "reviewApplicationManager");

            ReviewApplication application = Helper.createReviewApplication(1L, 2L, 1L);
            application.setAuctionId(auction.getId());
            applicationManager.createApplication(application);

            long resourcesCount = Helper.getProjectResourcesCount(projectId);

            instance.execute();

            long addedReviewersCount = Helper.getProjectResourcesCount(projectId) - resourcesCount;
            assertEquals("The reviewers count should be correct", 1L, addedReviewersCount);
        } finally {
            Helper.clearDB();
        }
    }

    /**
     * Accuracy test for {@code execute} method.
     *
     * Preconditions: two applications
     *
     * Expected behavior: no errors, correct emails on smpt server, correct emails on smpt server, the first
     * applicaton should be rejected, the second application should be selected
     *
     * @throws Exception
     *             to JUnit
     */
    @Test
    public void test_execute_2() throws Exception {
        try {
            Helper.clearDB();
            Helper.insertDB();
            instance = new ReviewAssignmentManager(configuration);

            ReviewAuctionManager auctionManager = (ReviewAuctionManager) Helper.getField(instance,
                    "reviewAuctionManager");

            long projectId = 11L;
            ReviewAuction auction = Helper.createReviewAuction_singleRole(1L);
            auction.setProjectId(projectId);
            auctionManager.createAuction(auction);

            ReviewApplicationManager applicationManager = (ReviewApplicationManager) Helper.getField(instance,
                    "reviewApplicationManager");

            ReviewApplication application1 = Helper.createReviewApplication(1L, 4L, 1L);
            ReviewApplication application2 = Helper.createReviewApplication(2L, 2L, 1L);
            application1.setAuctionId(auction.getId());
            application2.setAuctionId(auction.getId());
            applicationManager.createApplication(application1);
            applicationManager.createApplication(application2);

            long resourcesCount = Helper.getProjectResourcesCount(projectId);

            instance.execute();

            long addedReviewersCount = Helper.getProjectResourcesCount(projectId) - resourcesCount;
            assertEquals("The reviewers count should be correct", 1L, addedReviewersCount);
        } finally {
            Helper.clearDB();
        }
    }
}
