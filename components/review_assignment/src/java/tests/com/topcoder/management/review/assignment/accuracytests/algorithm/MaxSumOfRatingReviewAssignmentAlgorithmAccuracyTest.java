/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.assignment.accuracytests.algorithm;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.management.review.application.ReviewApplication;
import com.topcoder.management.review.application.ReviewApplicationRole;
import com.topcoder.management.review.application.ReviewAuction;
import com.topcoder.management.review.assignment.ReviewAssignmentAlgorithmException;
import com.topcoder.management.review.assignment.accuracytests.AccuracyTestsHelper;
import com.topcoder.management.review.assignment.accuracytests.TestDataFactory;
import com.topcoder.management.review.assignment.accuracytests.User;
import com.topcoder.management.review.assignment.algorithm.MaxSumOfRatingReviewAssignmentAlgorithm;
import com.topcoder.util.log.Log;
import junit.framework.JUnit4TestAdapter;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>A test case for {@link MaxSumOfRatingReviewAssignmentAlgorithm} class.</p>
 * 
 * @author isv
 * @version 1.0
 */
public class MaxSumOfRatingReviewAssignmentAlgorithmAccuracyTest {

    /**
     * <p>A <code>MaxSumOfRatingReviewAssignmentAlgorithm</code> instance to run the test against.</p>
     */
    private MaxSumOfRatingReviewAssignmentAlgorithm testedInstance;

    /**
     * <p>Gets the test suite with the tests for this test case.</p>
     *
     * @return a <code>Test</code> providing the test suite for this test case.
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(MaxSumOfRatingReviewAssignmentAlgorithmAccuracyTest.class);
    }

    /**
     * <p>Constructs new <code>MaxSumOfRatingReviewAssignmentAlgorithmAccuracyTest</code> instance. This implementation
     * does nothing.</p>
     */
    public MaxSumOfRatingReviewAssignmentAlgorithmAccuracyTest() {
    }

    /**
     * <p>Sets up the test environment for the test case.</p>
     *
     * @throws Exception if an unexpected error occurs.
     */
    @Before
    public void setUp() throws Exception {
        this.testedInstance = new MaxSumOfRatingReviewAssignmentAlgorithmSubclass();
    }

    /**
     * <p>Tears down the environment for the test case.</p>
     */
    @After
    public void tearDown() {
        this.testedInstance = null;
    }

    /**
     * <p>Tests the {@link MaxSumOfRatingReviewAssignmentAlgorithm#configure(ConfigurationObject)} method for
     * accuracy.</p>
     *
     * <p>Passes configuration object with required parameters set only and verifies that the instance is initialized 
     * correctly.</p>
     *
     * @throws Exception if an unexpected error occurs.
     */
    @Test
    public void testConfigure_RequiredParametersOnly() throws Exception {
        this.testedInstance.configure(TestDataFactory.getAlgorithmConfig(true, true, true, true));
        
        Log log = ((MaxSumOfRatingReviewAssignmentAlgorithmSubclass) this.testedInstance).getLog();
        Assert.assertNull("Log is initialized", log);
        
        Object defaultRating = AccuracyTestsHelper.getField(this.testedInstance, "defaultRating");
        Assert.assertNotNull("Default rating is not set", defaultRating);
        Assert.assertEquals("Default rating is not set to default value", 
                1.0D, Double.parseDouble(defaultRating.toString()), 0.01);

        Object minimumFeedbacks = AccuracyTestsHelper.getField(this.testedInstance, "minimumFeedbacks");
        Assert.assertNotNull("Minimum feedbacks is not set", minimumFeedbacks);
        Assert.assertEquals("Minimum feedbacks is not set to default value",
                4, Integer.parseInt(minimumFeedbacks.toString()));

        Object connectionName = AccuracyTestsHelper.getField(this.testedInstance, "connectionName");
        Assert.assertNull("Connection name is set to non-null value", connectionName);

        Object dbConnectionFactory = AccuracyTestsHelper.getField(this.testedInstance, "dbConnectionFactory");
        Assert.assertNotNull("DB connection factory is not set", dbConnectionFactory);
    }

    /**
     * <p>Tests the {@link MaxSumOfRatingReviewAssignmentAlgorithm#configure(ConfigurationObject)} method for
     * accuracy.</p>
     *
     * <p>Passes configuration object with both required and optional parameters set and verifies that the instance is 
     * initialized correctly.</p>
     *
     * @throws Exception if an unexpected error occurs.
     */
    @Test
    public void testConfigure_AllParameters() throws Exception {
        this.testedInstance.configure(TestDataFactory.getAlgorithmConfig(false, false, false, false));

        Log log = ((MaxSumOfRatingReviewAssignmentAlgorithmSubclass) this.testedInstance).getLog();
        Assert.assertNotNull("Log is not initialized", log);

        Object defaultRating = AccuracyTestsHelper.getField(this.testedInstance, "defaultRating");
        Assert.assertNotNull("Default rating is not set", defaultRating);
        Assert.assertEquals("Default rating is not set to configured value",
                1.5D, Double.parseDouble(defaultRating.toString()), 0.01);

        Object minimumFeedbacks = AccuracyTestsHelper.getField(this.testedInstance, "minimumFeedbacks");
        Assert.assertNotNull("Minimum feedbacks is not set", minimumFeedbacks);
        Assert.assertEquals("Minimum feedbacks is not set to configured value",
                2, Integer.parseInt(minimumFeedbacks.toString()));

        Object connectionName = AccuracyTestsHelper.getField(this.testedInstance, "connectionName");
        Assert.assertEquals("Connection name is not set", "accuracyDB", connectionName);

        Object dbConnectionFactory = AccuracyTestsHelper.getField(this.testedInstance, "dbConnectionFactory");
        Assert.assertNotNull("DB connection factory is not set", dbConnectionFactory);
    }

    /**
     * <p>Tests the {@link MaxSumOfRatingReviewAssignmentAlgorithm#prepare(ReviewAuction, List)} method for accuracy.
     * </p>
     *
     * <p>Verifies that method executes with no exception and that ratingPerUser property is initialized correctly.</p>
     *
     * @throws Exception if an unexpected error occurs.
     */
    @Test
    public void testPrepare() throws Exception {
        this.testedInstance.configure(TestDataFactory.getAlgorithmConfig(false, false, false, false));

        final long auctionId = 100017;
        ReviewAuction auction = TestDataFactory.getReviewAuction(auctionId);
        List<ReviewApplication> reviewApplications = TestDataFactory.getReviewApplications(auctionId);
        ((MaxSumOfRatingReviewAssignmentAlgorithmSubclass) this.testedInstance).prepare(auction, reviewApplications);

        Object ratingPerUser = AccuracyTestsHelper.getField(this.testedInstance, "ratingPerUser");
        Assert.assertNotNull("Rating per user mapping is not set", ratingPerUser);
        
        Map<Long, Double> expectedRatingMap = new HashMap<Long, Double>();
        // Users who have been rated more than minimum allowed number of times
        expectedRatingMap.put(132457L, 2D);
        expectedRatingMap.put(132458L, 1D);
        expectedRatingMap.put(132456L, 0D);
        expectedRatingMap.put(124764L, 2D);
        expectedRatingMap.put(124772L, 1D);
        expectedRatingMap.put(124776L, 0D);
        expectedRatingMap.put(124835L, 2D);
        expectedRatingMap.put(124856L, 1D);
        expectedRatingMap.put(124916L, 0D);
        // Users who have never been rated
        expectedRatingMap.put(124861L, 1.5D);
        expectedRatingMap.put(124857L, 1.5D);
        // Users who have been rated less than minimum allowed number of times
        expectedRatingMap.put(124834L, 1.5D);
                
        Assert.assertEquals("Incorrect rating per user mapping is set", expectedRatingMap, ratingPerUser);
    }

    /**
     * <p>Tests the {@link MaxSumOfRatingReviewAssignmentAlgorithm#measureQuality(ReviewAuction, Map)} method for 
     * accuracy.</p>
     *
     * <p>Passes the review application roles assignment which contains any sort of users (having or no having review 
     * feedbacks) and verifies that method calculates the quality of such an assignment correctly according to user 
     * ratings and default rating values.</p>
     *
     * @throws Exception if an unexpected error occurs.
     */
    @Test
    public void testMeasureQuality() throws Exception {
        this.testedInstance.configure(TestDataFactory.getAlgorithmConfig(false, false, false, false));

        final long auctionId = 100017;
        ReviewAuction auction = TestDataFactory.getReviewAuction(auctionId);
        List<ReviewApplication> reviewApplications = TestDataFactory.getReviewApplications(auctionId);

        ((MaxSumOfRatingReviewAssignmentAlgorithmSubclass) this.testedInstance).prepare(auction, reviewApplications);
        
        // Prepare assignment and calculate expected quality
        Map<ReviewApplication, ReviewApplicationRole> assignment
                = new HashMap<ReviewApplication, ReviewApplicationRole>();
        double expectedQuality = 0;
        for (ReviewApplication reviewApplication : reviewApplications) {
            // Actually review application role is not important here
            assignment.put(reviewApplication, auction.getAuctionType().getApplicationRoles().get(0));
            long userId = reviewApplication.getUserId();
            if (userId == User.SUPER.getUserId()) {
                expectedQuality += 2;
            } else if (userId == User.HUNG.getUserId()) {
                expectedQuality += 2;
            } else if (userId == User.REASSEMBLER.getUserId()) {
                expectedQuality += 2;
            } else if (userId == User.USER.getUserId()) {
                expectedQuality += 1;
            } else if (userId == User.PARTHA.getUserId()) {
                expectedQuality += 1;
            } else if (userId == User.WYZMO.getUserId()) {
                expectedQuality += 1;
            } else if (userId == User.HEFFAN.getUserId()) {
                expectedQuality += 0;
            } else if (userId == User.SANDKING.getUserId()) {
                expectedQuality += 0;
            } else if (userId == User.YOSHI.getUserId()) {
                expectedQuality += 0;
            } else if (userId == User.CARTAJS.getUserId()) {
                expectedQuality += 1.5;
            } else if (userId == User.KSMITH.getUserId()) {
                expectedQuality += 1.5;
            } else if (userId == User.LIGHTSPEED.getUserId()) {
                expectedQuality += 1.5;
            }
        }

        double actualQuality = ((MaxSumOfRatingReviewAssignmentAlgorithmSubclass) this.testedInstance).measureQuality(
                auction, assignment);
        
        Assert.assertEquals("Wrong assignment quality is returned", expectedQuality, actualQuality, 0.01);
    }

    /**
     * <p>A subclass of {@link MaxSumOfRatingReviewAssignmentAlgorithm} to be used for instantiating the instances of
     * the tested class and testing protected methods.</p>
     */
    private static class MaxSumOfRatingReviewAssignmentAlgorithmSubclass 
            extends MaxSumOfRatingReviewAssignmentAlgorithm {

        /**
         * <p>Constructs new <code>MaxSumOfRatingReviewAssignmentAlgorithmSubclass</code> instance. This implementation
         * does nothing.</p>
         */
        private MaxSumOfRatingReviewAssignmentAlgorithmSubclass() {
        }

        /**
         * <p>Gets logger.</p>
         *
         * @return the logger.
         */
        @Override
        protected Log getLog() {
            return super.getLog();
        }

        /**
         * This method measures the quality of the given assignment.
         *
         * @param reviewAuction Review auction.
         * @param assignment Maps each assigned review application to assigned role.
         * @return Quality of the given assignment.
         */
        @Override
        protected double measureQuality(ReviewAuction reviewAuction,
                                        Map<ReviewApplication, ReviewApplicationRole> assignment)
                throws ReviewAssignmentAlgorithmException {
            return super.measureQuality(reviewAuction, assignment);
        }

        /**
         * This method performs all the necessary preparations before the calls of measureQuality() method. It will be
         * called before brute force begins.
         *
         * @param reviewAuction Review auction.
         * @param reviewApplications Review applications.
         * @throws IllegalArgumentException If any argument is null or reviewApplications contains null.
         * @throws ReviewAssignmentAlgorithmException If any error occurs.
         */
        @Override
        protected void prepare(ReviewAuction reviewAuction, List<ReviewApplication> reviewApplications)
                throws ReviewAssignmentAlgorithmException {
            super.prepare(reviewAuction, reviewApplications);
        }
    }
}
