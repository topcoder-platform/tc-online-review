/**
 * Copyright (c) 2013, TopCoder, Inc. All rights reserved
 */
package com.topcoder.management.review.assignment.accuracytests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.JUnit4TestAdapter;

import org.junit.Before;
import org.junit.Test;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.management.review.application.ReviewApplication;
import com.topcoder.management.review.application.ReviewApplicationRole;
import com.topcoder.management.review.application.ReviewAuction;
import com.topcoder.management.review.assignment.ReviewAssignmentAlgorithmException;
import com.topcoder.management.review.assignment.algorithm.MaxSumOfRatingReviewAssignmentAlgorithm;
import com.topcoder.util.log.Log;

/**
 * <p>
 * Unit tests for {@link MaxSumOfRatingReviewAssignmentAlgorithm} class. <br/>
 * </p>
 *
 * @author KennyAlive
 * @version 1.0
 */
public class MaxSumOfRatingReviewAssignmentAlgorithmAccuracyTests {
    /**
     * Constant for configuration namespace for accuracy tests.
     */
    private static final String CONFIGURATION = "Algorithm";

    /**
     * Constant for configuration namespace for accuracy tests.
     */
    private static final String CONFIGURATION2 = "Algorithm2";

    /**
     * Default rating.
     */
    private static double DEFAULT_RATING = 1.0;

    /**
     * The {@code  MaxSumOfRatingReviewAssignmentAlgorithm} instance used for testing.
     */
    private MyMaxSumOfRatingReviewAssignmentAlgorithm instance;

    /**
     * Creates a suite with all test methods for JUnix3.x runner.
     *
     * @return a test suite.
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(MaxSumOfRatingReviewAssignmentAlgorithmAccuracyTests.class);
    }

    /**
     * Sets up the test environment.
     *
     * @throws Exception
     *             to JUnit
     */
    @Before
    public void setUp() throws Exception {
        instance = new MyMaxSumOfRatingReviewAssignmentAlgorithm();
    }

    /**
     * Accuracy test for constructor.
     */
    @Test
    public void test_constructor_MaxSumOfRatingReviewAssignmentAlgorithm() {
        assertNull("The dbConnectionFactory should be null", Helper.getSuperField(instance, "dbConnectionFactory"));
        assertNull("The connectionName should be null", Helper.getSuperField(instance, "connectionName"));
        assertNull("The ratingPerUser should be null", Helper.getSuperField(instance, "ratingPerUser"));
    }

    /**
     * Accuracy test for {@code configure} method.
     *
     * @throws Exception
     *             to JUnit
     */
    @Test
    public void test_configure_1() throws Exception {
        ConfigurationObject configuration = Helper.getConfiguration(CONFIGURATION);
        instance.configure(configuration);

        assertNotNull("The logger should not be null", instance.getLog());

        DBConnectionFactory factory = (DBConnectionFactory) Helper.getSuperField(instance, "dbConnectionFactory");
        assertNotNull("The dbConnectionFactor should not be null", factory);

        String connectionName = (String) Helper.getSuperField(instance, "connectionName");
        assertNotNull("The connectionName should not be null", connectionName);

        int minimumFeedbacks = (Integer) Helper.getSuperField(instance, "minimumFeedbacks");
        assertEquals("Default minimumFeedbacks value should be correct", 4, minimumFeedbacks);

        double defaultRating = (Double) Helper.getSuperField(instance, "defaultRating");
        assertEquals("Default defaultRating value should be correct", 1.0, defaultRating, 1e-4);
    }

    /**
     * Accuracy test for {@code configure} method.
     *
     * @throws Exception
     *             to JUnit
     */
    @Test
    public void test_configure_2() throws Exception {
        ConfigurationObject configuration = Helper.getConfiguration(CONFIGURATION2);
        instance.configure(configuration);

        int minimumFeedbacks = (Integer) Helper.getSuperField(instance, "minimumFeedbacks");
        assertEquals("minimumFeedbacks value should be correct", 5, minimumFeedbacks);

        double defaultRating = (Double) Helper.getSuperField(instance, "defaultRating");
        assertEquals("defaultRating value should be correct", 0.8, defaultRating, 1e-4);
    }

    /**
     * Accuracy test for {@code prepare} method.
     *
     * Preconditions: there are no reviewers ratings in db, there are no applications
     *
     * Expected behavior: empty ratingPerUser map
     *
     * @throws Exception
     *             to JUnit
     */
    @Test
    public void test_prepare_1() throws Exception {
        ConfigurationObject configuration = Helper.getConfiguration(CONFIGURATION);
        instance.configure(configuration);

        ReviewAuction auction = Helper.createReviewAuction_singleRole(1L);
        List<ReviewApplication> applications = new ArrayList<ReviewApplication>();

        instance.prepare(auction, applications);

        @SuppressWarnings("unchecked")
        Map<Long, Double> ratingPerUser = (Map<Long, Double>) Helper.getSuperField(instance, "ratingPerUser");

        assertNotNull("The ratingPerUser should not be null", ratingPerUser);
        assertTrue("The ratingPerUser should be empty", ratingPerUser.isEmpty());
    }

    /**
     * Accuracy test for {@code prepare} method.
     *
     * Preconditions: reviewers rating test data, application from the user that has no feedbacks.
     *
     * Expected behavior: default rating for the user.
     *
     * @throws Exception
     *             to JUnit
     */
    @Test
    public void test_prepare_2() throws Exception {
        try {
            Helper.clearDB();
            Helper.insertDB();

            ConfigurationObject configuration = Helper.getConfiguration(CONFIGURATION);
            instance.configure(configuration);

            long userId = 123L;

            ReviewAuction auction = Helper.createReviewAuction_singleRole(1L);
            ReviewApplication application1 = Helper.createReviewApplication(1L, userId, 1L);

            instance.prepare(auction, Arrays.asList(application1));

            @SuppressWarnings("unchecked")
            Map<Long, Double> ratingPerUser = (Map<Long, Double>) Helper.getSuperField(instance, "ratingPerUser");

            assertNotNull("The ratingPerUser should not be null", ratingPerUser);
            assertTrue("The rating should be assigned", ratingPerUser.keySet().contains(userId));
            assertEquals("The rating should be set to default rating", DEFAULT_RATING, ratingPerUser.get(userId), 1e-4);
        } finally {
            Helper.clearDB();
        }
    }

    /**
     * Accuracy test for {@code prepare} method.
     *
     * Preconditions: reviewers rating test data, application from the user that has less feedbacks than configurable
     * amount.
     *
     * Expected behavior: default rating for the user
     *
     * @throws Exception
     *             to JUnit
     */
    @Test
    public void test_prepare_3() throws Exception {
        try {
            Helper.clearDB();
            Helper.insertDB();

            ConfigurationObject configuration = Helper.getConfiguration(CONFIGURATION);
            instance.configure(configuration);

            long userId = 1L;
            long projectId = 11L;

            ReviewAuction auction = Helper.createReviewAuction_singleRole(1L);
            auction.setProjectId(projectId);
            ReviewApplication application1 = Helper.createReviewApplication(1L, userId, 1L);

            instance.prepare(auction, Arrays.asList(application1));

            @SuppressWarnings("unchecked")
            Map<Long, Double> ratingPerUser = (Map<Long, Double>) Helper.getSuperField(instance, "ratingPerUser");

            assertNotNull("The ratingPerUser should not be null", ratingPerUser);
            assertTrue("The rating should be assigned", ratingPerUser.keySet().contains(userId));
            assertEquals("The rating should be set to default rating", DEFAULT_RATING, ratingPerUser.get(userId), 1e-4);
        } finally {
            Helper.clearDB();
        }
    }

    /**
     * Accuracy test for {@code prepare} method.
     *
     * Preconditions: reviewers rating test data, application from the user that has enough feedback to get
     * non-default rating.
     *
     * Expected behavior: correct rating value
     *
     * @throws Exception
     *             to JUnit
     */
    @Test
    public void test_prepare_4() throws Exception {
        try {
            Helper.clearDB();
            Helper.insertDB();

            ConfigurationObject configuration = Helper.getConfiguration(CONFIGURATION);
            instance.configure(configuration);

            long userId = 2L;
            long projectId = 11L;

            ReviewAuction auction = Helper.createReviewAuction_singleRole(1L);
            auction.setProjectId(projectId);
            ReviewApplication application1 = Helper.createReviewApplication(1L, userId, 1L);

            instance.prepare(auction, Arrays.asList(application1));

            @SuppressWarnings("unchecked")
            Map<Long, Double> ratingPerUser = (Map<Long, Double>) Helper.getSuperField(instance, "ratingPerUser");

            assertNotNull("The ratingPerUser should not be null", ratingPerUser);
            assertTrue("The rating should be assigned", ratingPerUser.keySet().contains(userId));
            assertEquals("The rating should be set to default rating", 88.75, ratingPerUser.get(userId), 1e-4);
        } finally {
            Helper.clearDB();
        }
    }

    /**
     * Accuracy test for {@code prepare} method.
     *
     * Preconditions: reviewers rating test data, application from the user that has enough feedback to get
     * non-default rating but in different from auction's project category.
     *
     * Expected behavior: default rating for the user
     *
     * @throws Exception
     *             to JUnit
     */
    @Test
    public void test_prepare_5() throws Exception {
        try {
            Helper.clearDB();
            Helper.insertDB();

            ConfigurationObject configuration = Helper.getConfiguration(CONFIGURATION);
            instance.configure(configuration);

            long userId = 2L;
            long projectId = 15L;

            ReviewAuction auction = Helper.createReviewAuction_singleRole(1L);
            auction.setProjectId(projectId);
            ReviewApplication application1 = Helper.createReviewApplication(1L, userId, 1L);

            instance.prepare(auction, Arrays.asList(application1));

            @SuppressWarnings("unchecked")
            Map<Long, Double> ratingPerUser = (Map<Long, Double>) Helper.getSuperField(instance, "ratingPerUser");

            assertNotNull("The ratingPerUser should not be null", ratingPerUser);
            assertTrue("The rating should be assigned", ratingPerUser.keySet().contains(userId));
            assertEquals("The rating should be set to default rating", DEFAULT_RATING, ratingPerUser.get(userId), 1e-4);
        } finally {
            Helper.clearDB();
        }
    }

    /**
     * Accuracy test for {@code prepare} method.
     *
     * Preconditions: reviewers rating test data, application from two users one of which does not have enough
     * feedback and the other have enough feedback for non-default score
     *
     * Expected behavior: 1st user: default rating, 2nd user: correct rating value
     *
     * @throws Exception
     *             to JUnit
     */
    @Test
    public void test_prepare_6() throws Exception {
        try {
            Helper.clearDB();
            Helper.insertDB();

            ConfigurationObject configuration = Helper.getConfiguration(CONFIGURATION);
            instance.configure(configuration);

            long userId1 = 2L;
            long userId2 = 3L;
            long projectId = 19L;

            ReviewAuction auction = Helper.createReviewAuction_singleRole(2L);
            auction.setProjectId(projectId);
            ReviewApplication application1 = Helper.createReviewApplication(1L, userId1, 1L);
            ReviewApplication application2 = Helper.createReviewApplication(2L, userId2, 1L);

            instance.prepare(auction, Arrays.asList(application1, application2));

            @SuppressWarnings("unchecked")
            Map<Long, Double> ratingPerUser = (Map<Long, Double>) Helper.getSuperField(instance, "ratingPerUser");

            assertNotNull("The ratingPerUser should not be null", ratingPerUser);

            assertTrue("The rating should be assigned", ratingPerUser.keySet().contains(userId1));
            assertEquals("The rating should be set to default rating", DEFAULT_RATING, ratingPerUser.get(userId1), 1e-4);

            assertTrue("The rating should be assigned", ratingPerUser.keySet().contains(userId2));
            assertEquals("The rating value should be correct", 85.0, ratingPerUser.get(userId2), 1e-4);
        } finally {
            Helper.clearDB();
        }
    }

    /**
     * Accuracy test for {@code prepare} method.
     *
     * Preconditions: reviewers rating test data, application from two three users: 1st has enough feedbacks, 2nd has
     * enough feedbacks, 3rd does review for this first time
     *
     * Expected behavior: 1st, 2nd user: correct rating, 3rd user: default rating
     *
     * @throws Exception
     *             to JUnit
     */
    @Test
    public void test_prepare_7() throws Exception {
        try {
            Helper.clearDB();
            Helper.insertDB();

            ConfigurationObject configuration = Helper.getConfiguration(CONFIGURATION);
            instance.configure(configuration);

            long userId1 = 2L;
            long userId2 = 3L;
            long userId3 = 4L;
            long projectId = 12L;

            ReviewAuction auction = Helper.createReviewAuction_singleRole(2L);
            auction.setProjectId(projectId);
            ReviewApplication application1 = Helper.createReviewApplication(1L, userId1, 1L);
            ReviewApplication application2 = Helper.createReviewApplication(2L, userId2, 1L);
            ReviewApplication application3 = Helper.createReviewApplication(3L, userId3, 1L);

            instance.prepare(auction, Arrays.asList(application1, application2, application3));

            @SuppressWarnings("unchecked")
            Map<Long, Double> ratingPerUser = (Map<Long, Double>) Helper.getSuperField(instance, "ratingPerUser");

            assertNotNull("The ratingPerUser should not be null", ratingPerUser);

            assertTrue("The rating should be assigned", ratingPerUser.keySet().contains(userId1));
            assertEquals("The rating should be set to default rating", 88.75, ratingPerUser.get(userId1), 1e-4);

            assertTrue("The rating should be assigned", ratingPerUser.keySet().contains(userId2));
            assertEquals("The rating value should be correct", 95.0, ratingPerUser.get(userId2), 1e-4);

            assertTrue("The rating should be assigned", ratingPerUser.keySet().contains(userId3));
            assertEquals("The rating value should be correct", DEFAULT_RATING, ratingPerUser.get(userId3), 1e-4);
        } finally {
            Helper.clearDB();
        }
    }

    /**
     * Accuracy test for {@code measureQuality} method.
     *
     * Preconditions: empty assignment
     *
     * Expected behavior: zero quality
     *
     * @throws Exception
     *             to JUnit
     */
    @Test
    public void test_measureQuality_1() throws Exception {
        try {
            Helper.clearDB();
            Helper.insertDB();

            ConfigurationObject configuration = Helper.getConfiguration(CONFIGURATION);
            instance.configure(configuration);

            long projectId = 11L;

            ReviewAuction auction = Helper.createReviewAuction_singleRole(2L);
            auction.setProjectId(projectId);

            Map<ReviewApplication, ReviewApplicationRole> assignment =
                    new HashMap<ReviewApplication, ReviewApplicationRole>();

            instance.prepare(auction, new ArrayList<ReviewApplication>());

            double quality = instance.measureQuality(auction, assignment);
            assertEquals("The quality should be correct", 0.0, quality, 1e-4);
        } finally {
            Helper.clearDB();
        }
    }

    /**
     * Accuracy test for {@code measureQuality} method.
     *
     * Preconditions: assignment with a single user
     *
     * Expected behavior: correct quality value
     *
     * @throws Exception
     *             to JUnit
     */
    @Test
    public void test_measureQuality_2() throws Exception {
        try {
            Helper.clearDB();
            Helper.insertDB();

            ConfigurationObject configuration = Helper.getConfiguration(CONFIGURATION);
            instance.configure(configuration);

            long projectId = 11L;
            long userId = 2L;

            ReviewAuction auction = Helper.createReviewAuction_singleRole(2L);
            auction.setProjectId(projectId);
            ReviewApplication application1 = Helper.createReviewApplication(1L, userId, 1L);
            ReviewApplicationRole role = Helper.createReviewApplicationRole(2);

            Map<ReviewApplication, ReviewApplicationRole> assignment =
                    new HashMap<ReviewApplication, ReviewApplicationRole>();

            assignment.put(application1, role);

            instance.prepare(auction, Arrays.asList(application1));
            double quality = instance.measureQuality(auction, assignment);
            assertEquals("The quality should be correct", 88.75, quality, 1e-4);
        } finally {
            Helper.clearDB();
        }
    }

    /**
     * Accuracy test for {@code measureQuality} method.
     *
     * Preconditions: assignment with two users
     *
     * Expected behavior: correct quality value
     *
     * @throws Exception
     *             to JUnit
     */
    @Test
    public void test_measureQuality_3() throws Exception {
        try {
            Helper.clearDB();
            Helper.insertDB();

            ConfigurationObject configuration = Helper.getConfiguration(CONFIGURATION);
            instance.configure(configuration);

            long projectId = 11L;
            long userId1 = 3L;
            long userId2 = 4L;

            ReviewAuction auction = Helper.createReviewAuction_singleRole(2L);
            auction.setProjectId(projectId);
            ReviewApplication application1 = Helper.createReviewApplication(1L, userId1, 1L);
            ReviewApplication application2 = Helper.createReviewApplication(2L, userId2, 1L);
            ReviewApplicationRole role = Helper.createReviewApplicationRole(2);

            Map<ReviewApplication, ReviewApplicationRole> assignment =
                    new HashMap<ReviewApplication, ReviewApplicationRole>();

            assignment.put(application1, role);
            assignment.put(application2, role);

            instance.prepare(auction, Arrays.asList(application1, application2));
            double quality = instance.measureQuality(auction, assignment);
            assertEquals("The quality should be correct", 96.0, quality, 1e-4);
        } finally {
            Helper.clearDB();
        }
    }

    /**
     * Accuracy test for {@code super.assign} method. It was tested for the base class, but also test here for
     * additional confidence.
     *
     * Preconditions: 3 applications for single-position role.
     *
     * Expected behavior: the best user is selected
     *
     * @throws Exception
     *             to JUnit
     */
    @Test
    public void test_assign_1() throws Exception {
        try {
            Helper.clearDB();
            Helper.insertDB();

            ConfigurationObject configuration = Helper.getConfiguration(CONFIGURATION);
            instance.configure(configuration);

            long projectId = 11L;
            long userId1 = 1L;
            long userId2 = 2L;
            long userId3 = 3L;

            ReviewAuction auction = Helper.createReviewAuction_singleRole(1L);
            auction.setProjectId(projectId);

            ReviewApplication application1 = Helper.createReviewApplication(1L, userId1, 1L);
            ReviewApplication application2 = Helper.createReviewApplication(2L, userId2, 1L);
            ReviewApplication application3 = Helper.createReviewApplication(3L, userId3, 1L);

            Map<ReviewApplication, ReviewApplicationRole> assignment = instance.assign(auction,
                    Arrays.asList(application1, application2, application3));

            assertNotNull("Assignment map should not be null", assignment);
            assertEquals("Single application should be selected", 1, assignment.size());

            assertTrue("The application should be corrrect", assignment.keySet().contains((application3)));

            ReviewApplicationRole applicationRole = assignment.get(application3);
            assertSame("The application role should be correct", application3.getApplicationRoleId(),
                    applicationRole.getId());
        } finally {
            Helper.clearDB();
        }
    }

    /**
     * Accuracy test for {@code super.assign} method. It was tested for the base class, but also test here for
     * additional confidence.
     *
     * Preconditions: 3 applications for 2-positions role.
     *
     * Expected behavior: two best users are selected
     *
     * @throws Exception
     *             to JUnit
     */
    @Test
    public void test_assign_2() throws Exception {
        try {
            Helper.clearDB();
            Helper.insertDB();

            ConfigurationObject configuration = Helper.getConfiguration(CONFIGURATION);
            instance.configure(configuration);

            long projectId = 11L;
            long userId1 = 1L;
            long userId2 = 2L;
            long userId3 = 3L;

            ReviewAuction auction = Helper.createReviewAuction_singleRole(2L);
            auction.setProjectId(projectId);

            ReviewApplication application1 = Helper.createReviewApplication(1L, userId1, 1L);
            ReviewApplication application2 = Helper.createReviewApplication(2L, userId2, 1L);
            ReviewApplication application3 = Helper.createReviewApplication(3L, userId3, 1L);

            Map<ReviewApplication, ReviewApplicationRole> assignment = instance.assign(auction,
                    Arrays.asList(application1, application2, application3));

            assertNotNull("Assignment map should not be null", assignment);
            assertEquals("Single application should be selected", 2, assignment.size());

            assertTrue("The application should be corrrect", assignment.keySet().contains((application2)));
            assertTrue("The application should be corrrect", assignment.keySet().contains((application3)));
        } finally {
            Helper.clearDB();
        }
    }

    /**
     * Accuracy test for {@code super.assign} method. It was tested for the base class, but also test here for
     * additional confidence.
     *
     * Preconditions: 3 applications for two roles single-position roles.
     *
     * Expected behavior: two uses are selected. Since 2 better users asked for the same role, one of them will
     * be rejected and the weakest user will be accepted (he asked for another role)
     *
     * @throws Exception
     *             to JUnit
     */
    @Test
    public void test_assign_3() throws Exception {
        try {
            Helper.clearDB();
            Helper.insertDB();

            ConfigurationObject configuration = Helper.getConfiguration(CONFIGURATION);
            instance.configure(configuration);

            long projectId = 11L;
            long userId1 = 1L;
            long userId2 = 2L;
            long userId3 = 3L;

            ReviewAuction auction = Helper.createReviewAuction_twoRoles(1L, 1L);
            auction.setProjectId(projectId);

            ReviewApplication application1 = Helper.createReviewApplication(1L, userId1, 2L);
            ReviewApplication application2 = Helper.createReviewApplication(2L, userId2, 1L);
            ReviewApplication application3 = Helper.createReviewApplication(3L, userId3, 1L);

            Map<ReviewApplication, ReviewApplicationRole> assignment = instance.assign(auction,
                    Arrays.asList(application1, application2, application3));

            assertNotNull("Assignment map should not be null", assignment);
            assertEquals("Single application should be selected", 2, assignment.size());

            assertTrue("The application should be corrrect", assignment.keySet().contains((application1)));
            assertTrue("The application should be corrrect", assignment.keySet().contains((application3)));
        } finally {
            Helper.clearDB();
        }
    }

    /**
     * This class is used in order to get access to protected methods of
     * {@code MaxSumOfRatingReviewAssignmentAlgorithm} class.
     *
     * @author KennyAlive
     * @version 1.0
     */
    private static class MyMaxSumOfRatingReviewAssignmentAlgorithm extends MaxSumOfRatingReviewAssignmentAlgorithm {
        @Override
        public Log getLog() {
            return super.getLog();
        }
        @Override
        public void prepare(ReviewAuction reviewAuction, List<ReviewApplication> reviewApplications)
                throws ReviewAssignmentAlgorithmException {
            super.prepare(reviewAuction, reviewApplications);
        }
        @Override
        public double measureQuality(ReviewAuction reviewAuction,
                Map<ReviewApplication, ReviewApplicationRole> assignment) {
            try {
                return super.measureQuality(reviewAuction, assignment);
            } catch (Throwable e) {
                return 0.0;
            }
        }
    }
}
