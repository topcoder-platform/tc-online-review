/*
 * Copyright (C) 2012 TopCoder Inc., All Rights Reserved.
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
import com.topcoder.management.review.assignment.BaseTestCase;
import com.topcoder.management.review.assignment.ReviewAssignmentAlgorithmException;
import com.topcoder.management.review.assignment.ReviewAssignmentConfigurationException;

/**
 * Testcases for MaxSumOfRatingReviewAssignmentAlgorithm class.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class UnitTestMaxSumOfRatingReviewAssignmentAlgorithm extends BaseTestCase {

    /**
     * The <code>{@link CustomMaxSumOfRatingReviewAssignmentAlgorithm}</code> instance used for testing
     * protected methods in MaxSumOfRatingReviewAssignmentAlgorithm.
     */
    private CustomMaxSumOfRatingReviewAssignmentAlgorithm instance;

    /**
     * The <code>{@link MaxSumOfRatingReviewAssignmentAlgorithm}</code> instance used for testing.
     */
    private MaxSumOfRatingReviewAssignmentAlgorithm target = null;

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
        instance = new CustomMaxSumOfRatingReviewAssignmentAlgorithm();
        instance.configure(config);

        target = new MaxSumOfRatingReviewAssignmentAlgorithm();

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
     * Accuracy testcase for <code>MaxSumOfRatingReviewAssignmentAlgorithm()</code> method.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testMaxSumOfRatingReviewAssignmentAlgorithmAccuracy() throws Exception {
        assertNotNull("MaxSumOfRatingReviewAssignmentAlgorithm() error.", instance);
    }

    /**
     * Failure tests <code>measureQuality</code> method. with null reviewAuction, IllegalArgumentException
     * should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testMeasureQualityNullReviewAuction() throws Exception {
        try {
            instance.measureQuality(null, assignment);
            fail("testMeasureQualityNullReviewAuction is failure.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * Failure tests <code>measureQuality</code> method. with null assignment, IllegalArgumentException should
     * be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testMeasureQualityNullAssignment() throws Exception {
        try {
            instance.measureQuality(reviewAuction, null);
            fail("testMeasureQualityNullReviewAuction is failure.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * Failure tests <code>measureQuality</code> method. with null key in assignment, IllegalArgumentException
     * should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testMeasureQualityNullInAssignmentKey() throws Exception {
        assignment.put(null, this.reviewApplicationRole);
        try {
            instance.measureQuality(reviewAuction, assignment);
            fail("testMeasureQualityNullReviewAuction is failure.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * Accuracy testcase for <code>measureQuality</code> method. It uses default rating.
     * The project id is 1 and there is no review feedback.
     * So it has three default rating, which is 1 for user 100001, 100002, 100003 and the sum is 3.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testMeasureQualityAccuracy1() throws Exception {
        reviewAuction.setProjectId(1);
        reviewApplications.clear();
        ReviewApplication app1 = new ReviewApplication();
        app1.setUserId(100001);
        ReviewApplication app2 = new ReviewApplication();
        app2.setUserId(100002);
        ReviewApplication app3 = new ReviewApplication();
        app3.setUserId(100003);
        reviewApplications.add(app1);
        reviewApplications.add(app2);
        reviewApplications.add(app3);
        instance.prepare(reviewAuction, reviewApplications);

        ReviewApplicationRole rar = new ReviewApplicationRole(1, "Reviewer", 2,
            new ArrayList<ReviewApplicationResourceRole>());

        Map<ReviewApplication, ReviewApplicationRole> ass = new HashMap<ReviewApplication, ReviewApplicationRole>();
        ass.put(app1, rar);
        ass.put(app2, rar);
        ass.put(app3, rar);

        double ret = instance.measureQuality(reviewAuction, ass);

        assertEquals("measureQuality(reviewAuction) error.", 3.0D, ret);
    }

    /**
     * Accuracy testcase for <code>measureQuality</code> method. It uses default rating.
     * The project id is 1 and there is no review feedback.
     * So it has three default rating, which is 1 for user 100001, 100002 and the sum is 2.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testMeasureQualityAccuracy2() throws Exception {
        reviewAuction.setProjectId(1);
        reviewApplications.clear();
        ReviewApplication app1 = new ReviewApplication();
        app1.setUserId(100001);
        ReviewApplication app2 = new ReviewApplication();
        app2.setUserId(100002);
        reviewApplications.add(app1);
        reviewApplications.add(app2);
        instance.prepare(reviewAuction, reviewApplications);

        ReviewApplicationRole rar = new ReviewApplicationRole(1, "Reviewer", 2,
            new ArrayList<ReviewApplicationResourceRole>());

        assignment.clear();
        assignment.put(app1, rar);
        assignment.put(app2, rar);

        double ret = instance.measureQuality(reviewAuction, assignment);

        assertEquals("measureQuality(reviewAuction) error.", 2.0D, ret);
    }

    /**
     * Accuracy testcase for <code>measureQuality</code> method. It does not use default rating.
     * The project id is 300001 and there is no review feedback. minimumFeedbacks is 2.
     * the average rating for 100001 and 100002 is (90+90)/2=90 and (90+70)/2=80 and the sum is 170.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testMeasureQualityAccuracy3() throws Exception {
        reviewAuction.setProjectId(300001);
        reviewApplications.clear();
        ReviewApplication app1 = new ReviewApplication();
        app1.setUserId(100001);
        ReviewApplication app2 = new ReviewApplication();
        app2.setUserId(100002);
        reviewApplications.add(app1);
        reviewApplications.add(app2);
        instance.prepare(reviewAuction, reviewApplications);

        ReviewApplicationRole rar = new ReviewApplicationRole(1, "Reviewer", 2,
            new ArrayList<ReviewApplicationResourceRole>());

        assignment.clear();
        assignment.put(app1, rar);
        assignment.put(app2, rar);

        double ret = instance.measureQuality(reviewAuction, assignment);

        assertEquals("measureQuality(reviewAuction) error.", 170D, ret);
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
     * Failure tests <code>prepare</code> method. with null element in the reviewApplications,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testPrepareNullInReviewApplications() throws Exception {
        reviewApplications.add(null);
        try {
            instance.prepare(reviewAuction, reviewApplications);
            fail("testPrepareNullInReviewApplications is failure.");
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
     * Failure tests <code>configure(ConfigurationObject config)</code> method. with null config,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConfigureFail1() throws Exception {
        try {
            instance.configure(null);
            fail("testConfigureNullConfig is failure.");
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
            target.configure(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the <code>configure(ConfigurationObject config)</code> method.
     * </p>
     * <p>
     * The loggerName property is not type of String in config,
     * <code>ReviewAssignmentConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConfigureFail3() throws Exception {
        config.setPropertyValue("loggerName", new Exception());

        try {
            target.configure(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the <code>configure(ConfigurationObject config)</code> method.
     * </p>
     * <p>
     * The minimumFeedbacks property is not proper, <code>ReviewAssignmentConfigurationException</code>
     * expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConfigureFail4() throws Exception {
        config.setPropertyValue("minimumFeedbacks", "a");

        try {
            target.configure(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the <code>configure(ConfigurationObject config)</code> method.
     * </p>
     * <p>
     * The minimumFeedbacks property is not proper, <code>ReviewAssignmentConfigurationException</code>
     * expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConfigureFail5() throws Exception {
        config.setPropertyValue("minimumFeedbacks", "a");

        try {
            target.configure(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the <code>configure(ConfigurationObject config)</code> method.
     * </p>
     * <p>
     * The minimumFeedbacks property is not positive, <code>ReviewAssignmentConfigurationException</code>
     * expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConfigureFail6() throws Exception {
        config.setPropertyValue("minimumFeedbacks", "-1");

        try {
            target.configure(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the <code>configure(ConfigurationObject config)</code> method.
     * </p>
     * <p>
     * The defaultRating property is not proper, <code>ReviewAssignmentConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConfigureFail7() throws Exception {
        config.setPropertyValue("defaultRating", "b");

        try {
            target.configure(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the <code>configure(ConfigurationObject config)</code> method.
     * </p>
     * <p>
     * The defaultRating property is not positive, <code>ReviewAssignmentConfigurationException</code>
     * expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConfigureFail8() throws Exception {
        config.setPropertyValue("defaultRating", "-1");

        try {
            target.configure(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the <code>configure(ConfigurationObject config)</code> method.
     * </p>
     * <p>
     * The connectionName property is empty, <code>ReviewAssignmentConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConfigureFail9() throws Exception {
        config.setPropertyValue("connectionName", "  ");

        try {
            target.configure(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the <code>configure(ConfigurationObject config)</code> method.
     * </p>
     * <p>
     * The dbConnectionFactoryConfig property is lost, <code>ReviewAssignmentConfigurationException</code>
     * expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConfigureFail11() throws Exception {
        config.removeChild("dbConnectionFactoryConfig");

        try {
            target.configure(config);
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
    public void testConfigureAccuracy1() throws Exception {
        target.configure(config);

        assertNotNull("dbConnectionFactory field should not be null", getField(target, "dbConnectionFactory"));

        assertEquals("connectionName field should be informix_connection", "informix_connection",
            getField(target, "connectionName"));

        assertEquals("minimumFeedbacks field should be 2", 2,
            Integer.parseInt(getField(target, "minimumFeedbacks").toString()));

        assertEquals("defaultRating field should be 1.0", 1.0,
            Double.parseDouble(getField(target, "defaultRating").toString()));
    }

    /**
     * Accuracy testcase for <code>configure(ConfigurationObject config)</code> method.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConfigureAccuracy2() throws Exception {
        config.removeProperty("minimumFeedbacks");
        config.removeProperty("defaultRating");
        target.configure(config);

        assertNotNull("dbConnectionFactory field should not be null", getField(target, "dbConnectionFactory"));

        assertEquals("connectionName field should be informix_connection", "informix_connection",
            getField(target, "connectionName"));

        assertEquals("minimumFeedbacks field should be 4", 4,
            Integer.parseInt(getField(target, "minimumFeedbacks").toString()));

        assertEquals("defaultRating field should be 1.0", 1.0,
            Double.parseDouble(getField(target, "defaultRating").toString()));
    }

    /**
     * Mock class extends from MaxSumOfRatingReviewAssignmentAlgorithm used for test.
     *
     * @author TCSDEVELOPER
     * @version 1.0
     */
    class CustomMaxSumOfRatingReviewAssignmentAlgorithm extends MaxSumOfRatingReviewAssignmentAlgorithm {

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
            return super.measureQuality(reviewAuction, assignment);
        }
    }
}
