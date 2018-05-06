/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.assignment;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.management.project.Project;
import com.topcoder.management.project.ProjectCategory;
import com.topcoder.management.project.ProjectStatus;
import com.topcoder.management.review.application.ReviewApplication;
import com.topcoder.management.review.application.ReviewApplicationResourceRole;
import com.topcoder.management.review.application.ReviewApplicationRole;
import com.topcoder.management.review.application.ReviewAuction;
import com.topcoder.management.review.application.ReviewAuctionCategory;
import com.topcoder.management.review.application.ReviewAuctionType;
import com.topcoder.management.review.assignment.algorithm.BruteForceBasedReviewAssignmentAlgorithm;
import com.topcoder.management.review.assignment.algorithm.MaxSumOfRatingReviewAssignmentAlgorithm;
import com.topcoder.management.review.assignment.notification.EmailBasedReviewAssignmentNotificationManager;

/**
 * Tests the demo of this component.
 *
 * @author gevak, TCSDEVELOPER
 * @version 1.0
 */
public class Demo extends BaseTestCase {
    /**
     * <p>
     * Sets up the test environment.
     * </p>
     *
     * @throws Exception
     *             throws exception if any.
     */
    protected void setUp() throws Exception {
        super.setUp();
        new File("guard.tmp").delete();
    }

    /**
     * <p>
     * Tears down the test environment.
     * </p>
     *
     * @throws Exception
     *             throws exception if any.
     */
    protected void tearDown() throws Exception {
        super.tearDown();

        new File("guard.tmp").delete();
    }

    /**
     * Tests the API usage of this component.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testAPIUsage() throws Exception {
        // How to assign the reviewer.
        ReviewAuction reviewAuction = new ReviewAuction();
        Map<ReviewApplication, ReviewApplicationRole> assignment =
            new HashMap<ReviewApplication, ReviewApplicationRole>();
        ReviewApplicationRole reviewApplicationRole = new ReviewApplicationRole(1, "Reviewer", 2, null);
        List<ReviewApplication> reviewApplications = new ArrayList<ReviewApplication>();

        reviewApplications.clear();
        ReviewApplication app1 = new ReviewApplication();
        app1.setUserId(1);
        app1.setApplicationRoleId(1);
        ReviewApplication app2 = new ReviewApplication();
        app2.setUserId(2);
        app2.setApplicationRoleId(1);
        ReviewApplication app3 = new ReviewApplication();
        app3.setUserId(3);
        app3.setApplicationRoleId(1);
        reviewApplications.add(app1);
        reviewApplications.add(app2);
        reviewApplications.add(app3);

        assignment.clear();
        assignment.put(app1, reviewApplicationRole);
        assignment.put(app2, reviewApplicationRole);
        assignment.put(app3, reviewApplicationRole);

        ReviewAuctionCategory auctionCategory = new ReviewAuctionCategory(1, "Contest Review");
        List<ReviewApplicationRole> applicationRoles = new ArrayList<ReviewApplicationRole>();

        ReviewApplicationRole rar = new ReviewApplicationRole(1, "Reviewer", 2,
            new ArrayList<ReviewApplicationResourceRole>());
        applicationRoles.add(rar);

        ReviewAuctionType auctionType = new ReviewAuctionType(1, "developement contest", auctionCategory,
            applicationRoles);

        reviewAuction.setProjectId(1);
        reviewAuction.setAuctionType(auctionType);

        List<Long> openPositions = new ArrayList<Long>();
        openPositions.add(new Long(1));
        openPositions.add(new Long(2));

        reviewAuction.setOpenPositions(openPositions);

        BruteForceBasedReviewAssignmentAlgorithm algo = new MaxSumOfRatingReviewAssignmentAlgorithm();
        ConfigurationObject algoConfig = getConfigurationObject("test_files/config/config.properties",
            "algorithm", MaxSumOfRatingReviewAssignmentAlgorithm.class.getName());
        algo.configure(algoConfig);

        algo.assign(reviewAuction, reviewApplications);

        // How to execute assignment manager.
        ConfigurationObject ramConfig = getConfigurationObject("test_files/config/config.properties",
            "assignment", ReviewAssignmentManager.class.getName());

        ReviewAssignmentManager ram = new ReviewAssignmentManager(ramConfig);
        ram.execute();

        // How to notify member.
        ReviewAssignmentNotificationManager ranm = new EmailBasedReviewAssignmentNotificationManager();

        ConfigurationObject ranmConfig = getConfigurationObject("test_files/config/config.properties",
            "notification", EmailBasedReviewAssignmentNotificationManager.class.getName());
        ranm.configure(ranmConfig);

        reviewAuction = new ReviewAuction();
        openPositions = new ArrayList<Long>();
        openPositions.add(new Long(2));
        reviewAuction.setOpenPositions(openPositions);
        ReviewApplication reviewApplication = new ReviewApplication();
        reviewApplication.setUserId(100008);

        assignment = new HashMap<ReviewApplication, ReviewApplicationRole>();
        assignment.put(reviewApplication, reviewApplicationRole);

        List<Long> unassignedUserIds = new ArrayList<Long>();
        unassignedUserIds.add(new Long(12345));
        ProjectStatus projectStatus = new ProjectStatus(1, "developement");
        Project project = new Project(1, ProjectCategory.BANNERS_ICONS, projectStatus);
        project.setProperty("Project Name", "Review Assignment");
        project.setProperty("Project Version", "1.0.0");

        // Show how to use notify
        ranm.notifyApprovedApplicants(reviewAuction, project, assignment);

        ranm.notifyRejectedApplicants(reviewAuction, project, unassignedUserIds);

        ranm.notifyManagers(reviewAuction, project, assignment);
    }
}
