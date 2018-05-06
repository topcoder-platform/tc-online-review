/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.assignment.algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.management.review.application.ReviewApplication;
import com.topcoder.management.review.application.ReviewApplicationResourceRole;
import com.topcoder.management.review.application.ReviewApplicationRole;
import com.topcoder.management.review.application.ReviewAuction;
import com.topcoder.management.review.application.ReviewAuctionCategory;
import com.topcoder.management.review.application.ReviewAuctionType;
import com.topcoder.management.review.assignment.BaseTestCase;
import com.topcoder.management.review.assignment.ReviewAssignmentAlgorithmException;
import com.topcoder.management.review.assignment.ReviewAssignmentConfigurationException;
import com.topcoder.util.log.Log;

/**
 * Testcases for BruteForceBasedReviewAssignmentAlgorithm class.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class UnitTestBruteForceBasedReviewAssignmentAlgorithm extends BaseTestCase {
    /**
     * Represents CustomBruteForceBasedReviewAssignmentAlgorithm instance to test.
     */
    private CustomBruteForceBasedReviewAssignmentAlgorithm instance;

    /**
     * The <code>ConfigurationObject</code> instance used for testing.
     */
    private ConfigurationObject config;

    /**
     * The <code>{@link ReviewAuction}</code> instance used for testing.
     */

    private ReviewAuction reviewAuction;

    /**
     * The map of <code>{@link ReviewApplication}</code> and <code>{@link ReviewApplicationRole}</code>
     * instance used for testing.
     */
    private Map<ReviewApplication, ReviewApplicationRole> assignment;

    /**
     * The List of <code>{@link ReviewApplication}</code> instances used for testing.
     */
    private List<ReviewApplication> reviewApplications;

    /**
     * The <code>{@link ReviewApplicationRole}</code> instance used for testing.
     */
    private ReviewApplicationRole reviewApplicationRole;

    /**
     * setUp the test environment.
     *
     * @throws Exception
     *             to JUnit.
     */
    protected void setUp() throws Exception {
        super.setUp();

        config = getConfigurationObject("test_files/config/config.properties", "algorithm",
            MaxSumOfRatingReviewAssignmentAlgorithm.class.getName());
        instance = new CustomBruteForceBasedReviewAssignmentAlgorithm();
        instance.configure(config);

        reviewAuction = new ReviewAuction();

        ReviewApplication reviewApplication = new ReviewApplication();

        List<ReviewApplicationResourceRole> resourceRoles = new ArrayList<ReviewApplicationResourceRole>();
        reviewApplicationRole = new ReviewApplicationRole(1, "Reviewer", 2, resourceRoles);

        reviewApplications = new ArrayList<ReviewApplication>();

        assignment = new HashMap<ReviewApplication, ReviewApplicationRole>();
        assignment.put(reviewApplication, reviewApplicationRole);
    }

    /**
     * tearDown the test environment.
     *
     * @throws Exception
     *             to JUnit.
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        instance = null;
    }

    /**
     * Accuracy testcase for <code>BruteForceBasedReviewAssignmentAlgorithm()</code> method.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testBruteForceBasedReviewAssignmentAlgorithmAccuracy() throws Exception {
        assertNotNull("BruteForceBasedReviewAssignmentAlgorithm() error.", instance);
    }

    /**
     * Failure tests <code>assign</code> method. with null reviewAuction, IllegalArgumentException should be
     * thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testAssignNullReviewAuction() throws Exception {
        try {
            instance.assign(null, reviewApplications);
            fail("testAssignNullReviewAuction is failure.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * Failure tests <code>assign</code> method. with null reviewApplications, IllegalArgumentException should
     * be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testAssignNullReviewApplications() throws Exception {
        try {
            instance.assign(reviewAuction, null);
            fail("testAssignNullReviewApplications is failure.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * Accuracy testcase for <code>assign</code> method. There are three users (100001, 100002, 100003)
     * to be assigned to one role and the one having id = 100001 is assigned.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testAssignAccuracy1() throws Exception {

        reviewApplications.clear();
        ReviewApplication app1 = new ReviewApplication();
        app1.setUserId(100001);
        app1.setApplicationRoleId(1);
        ReviewApplication app2 = new ReviewApplication();
        app2.setUserId(100002);
        app2.setApplicationRoleId(1);
        ReviewApplication app3 = new ReviewApplication();
        app3.setUserId(100003);
        app3.setApplicationRoleId(1);
        reviewApplications.add(app1);
        reviewApplications.add(app2);
        reviewApplications.add(app3);

        assignment.clear();
        assignment.put(app1, reviewApplicationRole);
        assignment.put(app2, reviewApplicationRole);
        assignment.put(app3, reviewApplicationRole);

        // Create ReviewAuctionType
        ReviewAuctionCategory auctionCategory = new ReviewAuctionCategory(1, "Contest Review");

        List<ReviewApplicationRole> applicationRoles = new ArrayList<ReviewApplicationRole>();

        ReviewApplicationRole rar = new ReviewApplicationRole(1, "Reviewer", 2,
            new ArrayList<ReviewApplicationResourceRole>());
        applicationRoles.add(rar);

        ReviewAuctionType auctionType = new ReviewAuctionType(1, "developement contest", auctionCategory,
            applicationRoles);

        // Set the attributes for reviewAuction
        reviewAuction.setProjectId(1);
        reviewAuction.setAuctionType(auctionType);

        List<Long> openPositions = new ArrayList<Long>();
        openPositions.add(new Long(1));
        openPositions.add(new Long(2));

        reviewAuction.setOpenPositions(openPositions);

        Map<ReviewApplication, ReviewApplicationRole> ret = instance
            .assign(reviewAuction, reviewApplications);
        assertEquals("assign(reviewAuction,reviewApplications) error.", 1, ret.size());
        for (Map.Entry<ReviewApplication, ReviewApplicationRole> kvp : ret.entrySet()) {
            ReviewApplication ra = kvp.getKey();
            ReviewApplicationRole role = kvp.getValue();
            assertEquals("assign user id error.", 100001, ra.getUserId());
            assertEquals("assign role ApplicationRoleId error.", 1, ra.getApplicationRoleId());
            assertEquals("assign id error.", 1, role.getId());
            assertEquals("assign role name error.", "Reviewer", role.getName());
            assertEquals("assign position error.", 2, role.getPositions());
        }
    }

    /**
     * Accuracy testcase for <code>assign</code> method. There are three users (100001, 100002, 100003)
     * to be assigned to multiply roles and the one having id = 100001 is assigned.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testAssignAccuracy2() throws Exception {

        reviewApplications.clear();
        ReviewApplication app1 = new ReviewApplication();
        app1.setUserId(100001);
        app1.setApplicationRoleId(1);
        ReviewApplication app2 = new ReviewApplication();
        app2.setUserId(100002);
        app2.setApplicationRoleId(1);
        ReviewApplication app3 = new ReviewApplication();
        app3.setUserId(100003);
        app3.setApplicationRoleId(1);
        reviewApplications.add(app1);
        reviewApplications.add(app2);
        reviewApplications.add(app3);

        assignment.clear();
        assignment.put(app1, reviewApplicationRole);
        assignment.put(app2, reviewApplicationRole);
        assignment.put(app3, reviewApplicationRole);

        // Create ReviewAuctionType
        ReviewAuctionCategory auctionCategory = new ReviewAuctionCategory(1, "Contest Review");

        List<ReviewApplicationRole> applicationRoles = new ArrayList<ReviewApplicationRole>();

        List<ReviewApplicationResourceRole> resourceRoles = new ArrayList<ReviewApplicationResourceRole>();
        ReviewApplicationResourceRole resourceRole1 = new ReviewApplicationResourceRole(1, true);
        ReviewApplicationResourceRole resourceRole2 = new ReviewApplicationResourceRole(2, true);
        resourceRoles.add(resourceRole1);
        resourceRoles.add(resourceRole2);

        ReviewApplicationRole rar = new ReviewApplicationRole(1, "Reviewer", 2, resourceRoles);
        ReviewApplicationRole rar1 = new ReviewApplicationRole(2, "Reviewer2", 2, resourceRoles);
        applicationRoles.add(rar);
        applicationRoles.add(rar1);

        ReviewAuctionType auctionType = new ReviewAuctionType(1, "developement contest", auctionCategory,
            applicationRoles);

        // Set the attributes for reviewAuction
        reviewAuction.setProjectId(1);
        reviewAuction.setAuctionType(auctionType);

        List<Long> openPositions = new ArrayList<Long>();
        openPositions.add(new Long(1));
        openPositions.add(new Long(2));

        reviewAuction.setOpenPositions(openPositions);

        Map<ReviewApplication, ReviewApplicationRole> ret = instance
            .assign(reviewAuction, reviewApplications);
        assertEquals("assign(reviewAuction,reviewApplications) error.", 1, ret.size());
        for (Map.Entry<ReviewApplication, ReviewApplicationRole> kvp : ret.entrySet()) {
            ReviewApplication ra = kvp.getKey();
            ReviewApplicationRole role = kvp.getValue();
            assertEquals("assign user id error.", 100001, ra.getUserId());
            assertEquals("assign role ApplicationRoleId error.", 1, ra.getApplicationRoleId());
            assertEquals("assign id error.", 1, role.getId());
            assertEquals("assign role name error.", "Reviewer", role.getName());
            assertEquals("assign position error.", 2, role.getPositions());
        }
    }

    /**
     * Failure tests <code>prepare</code> method. with null reviewAuction, IllegalArgumentException should be
     * thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testPrepareNullReviewAuction() throws Exception {
        try {
            instance.prepare(null, reviewApplications);
            fail("testPrepareNullReviewAuction is failure.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * Failure tests <code>prepare</code> method. with null reviewApplications, IllegalArgumentException
     * should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testPrepareNullReviewApplications() throws Exception {
        try {
            instance.prepare(reviewAuction, null);
            fail("testPrepareNullReviewApplications is failure.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * Accuracy testcase for <code>prepare</code> method.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testPrepareAccuracy() throws Exception {
        instance.prepare(reviewAuction, reviewApplications);
    }

    /**
     * Failure tests <code>finalize</code> method. with null reviewAuction, IllegalArgumentException should be
     * thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testFinalizeNullReviewAuction() throws Exception {
        try {
            instance.finalize(null, reviewApplications, assignment);
            fail("testFinalizeNullReviewAuction is failure.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * Failure tests <code>finalize</code> method. with null reviewApplications, IllegalArgumentException
     * should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testFinalizeNullReviewApplications() throws Exception {
        try {
            instance.finalize(reviewAuction, null, assignment);
            fail("testFinalizeNullReviewApplications is failure.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * Accuracy testcase for <code>finalize</code> method.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testFinalizeAccuracy() throws Exception {
        instance.finalize(reviewAuction, reviewApplications, assignment);
    }

    /**
     * Failure tests <code>configure(ConfigurationObject config)</code> method. with null config,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConfigureFail1() throws Exception {
        try {
            instance.configure(null);
            fail("testConfigureFail1 is failure.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the <code>configure(ConfigurationObject config)</code> method.
     * </p>
     * <p>
     * The loggerName property is empty in config, <code>ReviewAssignmentConfigurationException</code>
     * expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConfigureFail2() throws Exception {
        config.setPropertyValue("loggerName", "");

        try {
            instance.configure(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * Accuracy testcase for <code>configure(ConfigurationObject config)</code> method.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConfigureAccuracy() throws Exception {
        assertNotNull("Should not be null.", instance.getLog());

        config.removeProperty("loggerName");
        instance.configure(config);

        assertNull("Should be null.", instance.getLog());
    }

    /**
     * Accuracy testcase for <code>getLog()</code> method.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testGetLogAccuracy() throws Exception {
        assertNotNull("getLog() error.", instance.getLog());
    }

    /**
     * This class is an dummy implementation of a review assignment algorithm for testing.
     *
     * @author TCSDEVELOPER
     * @version 1.0
     *
     */
    class CustomBruteForceBasedReviewAssignmentAlgorithm extends BruteForceBasedReviewAssignmentAlgorithm {
        /**
         * This method performs all the necessary preparations before the calls of measureQuality() method. It
         * will be called before brute force begins.
         *
         * @param reviewAuction
         *            Review auction.
         * @param reviewApplications
         *            Review applications.
         *
         * @throws IllegalArgumentException
         *             If any argument is null or reviewApplications contains null.
         * @throws ReviewAssignmentAlgorithmException
         *             If any error occurs.
         */
        public void prepare(ReviewAuction reviewAuction, List<ReviewApplication> reviewApplications)
            throws ReviewAssignmentAlgorithmException {
            super.prepare(reviewAuction, reviewApplications);
        }

        /**
         * This method performs all the necessary finalization operations after the all calls of
         * measureQuality() method are done. It will be called after brute force ends.
         *
         * @param reviewAuction
         *            - Review auction.
         * @param reviewApplications
         *            - Review applications.
         * @param finalAssignment
         *            - Final assignment (the assignment finally chosen by assign() method). It maps each
         *            assigned review application to assigned role.
         *
         * @throws IllegalArgumentException
         *             - If any argument is null or reviewApplications contains null or finalAssignment
         *             contains null key.
         * @throws ReviewAssignmentAlgorithmException
         *             - If any error occurs.
         */
        public void finalize(ReviewAuction reviewAuction, List<ReviewApplication> reviewApplications,
            Map<ReviewApplication, ReviewApplicationRole> finalAssignment)
            throws ReviewAssignmentAlgorithmException {
            super.finalize(reviewAuction, reviewApplications, finalAssignment);
        }

        /**
         * This method measures the quality of the given assignment.
         *
         * @param reviewAuction
         *            - Review auction.
         * @param assignment
         *            - Maps each assigned review application to assigned role.
         * @return - Quality of the given assignment.
         *
         * @throws IllegalArgumentException
         *             - If any argument is null or assignment contains null key.
         * @throws ReviewAssignmentAlgorithmException
         *             - If any error occurs.
         */
        public double measureQuality(ReviewAuction reviewAuction,
            Map<ReviewApplication, ReviewApplicationRole> assignment)
            throws ReviewAssignmentAlgorithmException {
            return 1D;
        }

        /**
         * Gets logger used by this class for logging errors and debug information.
         *
         * @return the logger used by this class for logging errors and debug information.
         */
        public Log getLog() {
            return super.getLog();
        }
    }
}
