/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.assignment.accuracytests.algorithm;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.management.review.application.*;
import com.topcoder.management.review.assignment.ReviewAssignmentAlgorithmException;
import com.topcoder.management.review.assignment.accuracytests.TestDataFactory;
import com.topcoder.management.review.assignment.accuracytests.User;
import com.topcoder.management.review.assignment.algorithm.BruteForceBasedReviewAssignmentAlgorithm;
import com.topcoder.util.log.Log;
import junit.framework.JUnit4TestAdapter;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * <p>A test case for {@link BruteForceBasedReviewAssignmentAlgorithm} class.</p>
 *
 * @author isv
 * @version 1.0
 */
public class BruteForceBasedReviewAssignmentAlgorithmAccuracyTest {

    /**
     * <p>A <code>BruteForceBasedReviewAssignmentAlgorithm</code> instance to run the test against.</p>
     */
    private BruteForceBasedReviewAssignmentAlgorithm testedInstance;

    /**
     * <p>Gets the test suite with the tests for this test case.</p>
     *
     * @return a <code>Test</code> providing the test suite for this test case.
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(BruteForceBasedReviewAssignmentAlgorithmAccuracyTest.class);
    }

    /**
     * <p>Constructs new <code>BruteForceBasedReviewAssignmentAlgorithmAccuracyTest</code> instance. This implementation
     * does nothing.</p>
     */
    public BruteForceBasedReviewAssignmentAlgorithmAccuracyTest() {
    }

    /**
     * <p>Sets up the test environment for the test case.</p>
     *
     * @throws Exception if an unexpected error occurs.
     */
    @Before
    public void setUp() throws Exception {
        this.testedInstance = new BruteForceBasedReviewAssignmentAlgorithmSubclass();
    }

    /**
     * <p>Tears down the environment for the test case.</p>
     */
    @After
    public void tearDown() {
        this.testedInstance = null;
    }

    /**
     * <p>Tests the {@link BruteForceBasedReviewAssignmentAlgorithm#assign(ReviewAuction, List)} method for accuracy.
     * </p>
     * 
     * <p>Calls the method for review auction which does not have any review applications and expects the method to 
     * return empty resulting map.</p>
     *
     * @throws Exception if an unexpected error occurs.
     */
    @Test
    public void testAssign_NoReviewApplications() throws Exception {
        testAssign(100002, new HashMap<Long, Long>());
    }

    /**
     * <p>Tests the {@link BruteForceBasedReviewAssignmentAlgorithm#assign(ReviewAuction, List)} method for accuracy.
     * </p>
     *
     * <p>Calls the method for review auction with number of review applications exactly matching the number of open
     * review positions so that there is just a single review application for each open review position and expects the
     * method to return correct resulting map.</p>
     * 
     * <p>Additional condition is that each single reviewer applied just for a single review application role.</p>
     * 
     * @throws Exception if an unexpected error occurs.
     */
    @Test
    public void testAssign_ExactReviewApplicationsNumber() throws Exception {
        Map<Long, Long> expectedAssignments = new HashMap<Long, Long>();
        expectedAssignments.put(132456L, 3L);
        expectedAssignments.put(132457L, 4L);
        expectedAssignments.put(132458L, 5L);
        testAssign(100011, expectedAssignments);
    }

    /**
     * <p>Tests the {@link BruteForceBasedReviewAssignmentAlgorithm#assign(ReviewAuction, List)} method for accuracy.
     * </p>
     *
     * <p>Calls the method for review auction with excessive number of review applications so that there is more than 
     * one review application for each open review position and expects the method to return correct resulting map based
     * on reviewers feedback ratings.</p>
     * 
     * <p>Additional condition is that each single reviewer applied just for a single review application role.</p>
     *
     * @throws Exception if an unexpected error occurs.
     */
    @Test
    public void testAssign_ExcessiveReviewApplicationsNumber() throws Exception {
        Map<Long, Long> expectedAssignments = new HashMap<Long, Long>();
        expectedAssignments.put(132457L, 1L);
        expectedAssignments.put(124764L, 2L);
        expectedAssignments.put(124835L, 2L);
        testAssign(100003, expectedAssignments);
    }

    /**
     * <p>Tests the {@link BruteForceBasedReviewAssignmentAlgorithm#assign(ReviewAuction, List)} method for accuracy.
     * </p>
     *
     * <p>Calls the method for review auction with insufficient number of review applications so that there are no 
     * review applications at all for some of the open review positions and expects the method to return correct 
     * resulting map based on reviewers feedback ratings just for those review positions which have review applications 
     * for.</p>
     * 
     * <p>Additional condition is that each single reviewer applied just for a single review application role.</p>
     *
     * @throws Exception if an unexpected error occurs.
     */
    @Test
    public void testAssign_PartialReviewApplicationsNumber() throws Exception {
        Map<Long, Long> expectedAssignments = new HashMap<Long, Long>();
        expectedAssignments.put(132457L, 1L);
        testAssign(100010, expectedAssignments);
    }

    /**
     * <p>Tests the {@link BruteForceBasedReviewAssignmentAlgorithm#assign(ReviewAuction, List)} method for accuracy.
     * </p>
     *
     * <p>Calls the method for review auction with excessive number of review applications so that there is more than
     * one review application for each open review position and expects the method to return correct resulting map based
     * on reviewers feedback ratings.</p>
     *
     * <p>Additional condition is that each single reviewer applied for multiple review application roles open for the
     * review auction.</p>
     *
     * @throws Exception if an unexpected error occurs.
     */
    @Test
    public void testAssign_MultipleReviewApplicationsNumber() throws Exception {
        Map<Long, Long> expectedAssignments = new HashMap<Long, Long>();
        expectedAssignments.put(124764L, 1L);
        expectedAssignments.put(132457L, 2L);
        expectedAssignments.put(124835L, 2L);
        testAssign(100015, expectedAssignments);
    }

    /**
     * <p>Tests the {@link BruteForceBasedReviewAssignmentAlgorithm#configure(ConfigurationObject)} method for 
     * accuracy.</p>
     *
     * <p>Passes configuration object with logger name set and verifies that the log is initialized.</p>
     * 
     * <p>Side effect is that {@link BruteForceBasedReviewAssignmentAlgorithm#getLog()} method is also tested.</p>
     * 
     * @throws Exception if an unexpected error occurs.
     */
    @Test
    public void testConfigure_LoggerSet() throws Exception {
        this.testedInstance.configure(TestDataFactory.getAlgorithmConfig(false, false, false, false));
        Log log = ((BruteForceBasedReviewAssignmentAlgorithmSubclass) this.testedInstance).getLog();
        Assert.assertNotNull("Log is not initialized", log);

    }

    /**
     * <p>Tests the {@link BruteForceBasedReviewAssignmentAlgorithm#configure(ConfigurationObject)} method for 
     * accuracy.</p>
     *
     * <p>Passes configuration object with logger name not set and verifies that the log is not initialized.</p>
     *
     * <p>Side effect is that {@link BruteForceBasedReviewAssignmentAlgorithm#getLog()} method is also tested.</p>
     *
     * @throws Exception if an unexpected error occurs.
     */
    @Test
    public void testConfigure_LoggerNotSet() throws Exception {
        this.testedInstance.configure(TestDataFactory.getAlgorithmConfig(true, false, false, false));
        Log log = ((BruteForceBasedReviewAssignmentAlgorithmSubclass) this.testedInstance).getLog();
        Assert.assertNull("Log is initialized", log);
    }

    /**
     * <p>Tests the {@link BruteForceBasedReviewAssignmentAlgorithm#finalize(ReviewAuction, List, Map)} method for 
     * accuracy.</p>
     *
     * <p>Simply verifies that method executes with no exception.</p>
     *
     * @throws Exception if an unexpected error occurs.
     */
    @Test
    public void testFinalize() throws Exception {
        final long auctionId = 100003;
        ReviewAuction auction = TestDataFactory.getReviewAuction(auctionId);
        List<ReviewApplication> reviewApplications = TestDataFactory.getReviewApplications(auctionId);
        Map<ReviewApplication, ReviewApplicationRole> result = this.testedInstance.assign(auction, reviewApplications);

        ((BruteForceBasedReviewAssignmentAlgorithmSubclass) this.testedInstance).finalize(auction,
                reviewApplications, result);

    }

    /**
     * <p>Tests the {@link BruteForceBasedReviewAssignmentAlgorithm#prepare(ReviewAuction, List)} method for accuracy.
     * </p>
     *
     * <p>Simply verifies that method executes with no exception.</p>
     *
     * @throws Exception if an unexpected error occurs.
     */
    @Test
    public void testPrepare() throws Exception {
        final long auctionId = 100003;
        ReviewAuction auction = TestDataFactory.getReviewAuction(auctionId);
        List<ReviewApplication> reviewApplications = TestDataFactory.getReviewApplications(auctionId);
        ((BruteForceBasedReviewAssignmentAlgorithmSubclass) this.testedInstance).prepare(auction, reviewApplications);
    }

    /**
     * <p>Tests the {@link BruteForceBasedReviewAssignmentAlgorithm#assign(ReviewAuction, List)} method for accuracy.
     * </p>
     * 
     * <p>Runs the methods against the specified review auction and review applications for that review auction and 
     * verifies that correct assignments are returned.</p>
     * 
     * @param auctionId a <code>long</code> providing the ID of a review auction to be used for testing the assignment 
     *        for.
     * @param expectedAssignments a <code>Map</code> mapping the user IDs to review application role IDs as expected by 
     *        the test.  
     * @throws Exception if an unexpected error occurs.
     */
    private void testAssign(long auctionId, Map<Long, Long> expectedAssignments) throws Exception {
        ReviewAuction auction = TestDataFactory.getReviewAuction(auctionId);
        List<ReviewApplication> reviewApplications = TestDataFactory.getReviewApplications(auctionId);
        Map<ReviewApplication, ReviewApplicationRole> result = this.testedInstance.assign(auction, reviewApplications);
        verifyAssignments(auction, result, expectedAssignments);
    }

    /**
     * <p>Verifies the correctness of review application role assignments.</p>
     * 
     * @param auction a <code>ReviewAuction</code> providing the details for the auction. 
     * @param assignments a <code>Map</code> mapping the review applications to review application roles. 
     * @param expectedAssignments a <code>Map</code> mapping the user IDs to review application role IDs as expected by 
     *        the test.  
     */
    private void verifyAssignments(ReviewAuction auction, 
                                   Map<ReviewApplication, ReviewApplicationRole> assignments,
                                   Map<Long, Long> expectedAssignments) {
        System.out.println("ACCURACY TEST. Testing following assignments:\n" + assignments 
                + "\nagainst following expected user to role mapping: " + expectedAssignments);
        
        Assert.assertNotNull("Null result is returned for review auction " + auction.getId(), assignments);
        Assert.assertEquals("Wrong number of assignments for review auction " + auction.getId(),
                expectedAssignments.size(), assignments.size());
        for (Map.Entry<Long, Long> entry : expectedAssignments.entrySet()) {
            Long userId = entry.getKey();
            Long reviewApplicationRoleId = entry.getValue();
            boolean found = false;

            Iterator<Map.Entry<ReviewApplication, ReviewApplicationRole>> assignmentsIterator =
                    assignments.entrySet().iterator();
            while (assignmentsIterator.hasNext()) {
                Map.Entry<ReviewApplication, ReviewApplicationRole> assignment = assignmentsIterator.next();
                ReviewApplication reviewApplication = assignment.getKey();
                ReviewApplicationRole reviewApplicationRole = assignment.getValue();

                if (reviewApplication.getUserId() == userId) {
                    found = true;
                    assignmentsIterator.remove();
                    
                    Assert.assertEquals("Wrong review application role is assigned to user " + userId 
                            + " for review auction " + auction.getId(), 
                            reviewApplicationRoleId.longValue(), reviewApplicationRole.getId());
                }
            }

            Assert.assertTrue("User: " + userId + " is not assigned a review application role: "
                    + reviewApplicationRoleId + " for review auction " + auction.getId(), found);

        }
    }

    /**
     * <p>A subclass of {@link BruteForceBasedReviewAssignmentAlgorithm} to be used for instantiating the instances of
     * the tested class.</p>
     */
    private static class BruteForceBasedReviewAssignmentAlgorithmSubclass
            extends BruteForceBasedReviewAssignmentAlgorithm {

        /**
         * <p>Constructs new <code>BruteForceBasedReviewAssignmentAlgorithmSubclass</code> instance. This implementation 
         * does nothing.</p>
         */
        private BruteForceBasedReviewAssignmentAlgorithmSubclass() {
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
            double ratingSum = 0;
            for (Map.Entry<ReviewApplication, ReviewApplicationRole> kvp : assignment.entrySet()) {
                if (kvp.getValue() != null) {
                    long userId = kvp.getKey().getUserId();
                    double rating = 0.5;
                    if (userId == User.SUPER.getUserId()) {
                        rating = 2;
                    } else if (userId == User.HUNG.getUserId()) {
                        rating = 2;
                    } else if (userId == User.REASSEMBLER.getUserId()) {
                        rating = 2;
                    } else if (userId == User.USER.getUserId()) {
                        rating = 1;
                    } else if (userId == User.PARTHA.getUserId()) {
                        rating = 1;
                    } else if (userId == User.WYZMO.getUserId()) {
                        rating = 1;
                    } else if (userId == User.HEFFAN.getUserId()) {
                        rating = 0.5;
                    } else if (userId == User.SANDKING.getUserId()) {
                        rating = 0.5;
                    } else if (userId == User.YOSHI.getUserId()) {
                        rating = 0.5;
                    }

                    ratingSum += rating;
                }
            }
            return ratingSum;
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
         * This method performs all the necessary finalization operations after the all calls of measureQuality() method 
         * are done. It will be called after brute force ends.
         *
         * @param reviewAuction Review auction.
         * @param reviewApplications Review applications.
         * @param finalAssignment Final assignment (the assignment finally chosen by assign() method). It maps each 
         *        assigned review application to assigned role.
         * @throws IllegalArgumentException If any argument is null or reviewApplications contains null or 
         *         finalAssignment contains null key.
         * @throws ReviewAssignmentAlgorithmException If any error occurs.
         */
        @Override
        protected void finalize(ReviewAuction reviewAuction, List<ReviewApplication> reviewApplications,
                                Map<ReviewApplication, ReviewApplicationRole> finalAssignment)
                throws ReviewAssignmentAlgorithmException {
            super.finalize(reviewAuction, reviewApplications, finalAssignment);
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
