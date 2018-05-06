/*
 * Copyright (C) 2010, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.assignment;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.management.review.assignment.algorithm.UnitTestBruteForceBasedReviewAssignmentAlgorithm;
import com.topcoder.management.review.assignment.algorithm.UnitTestMaxSumOfRatingReviewAssignmentAlgorithm;
import com.topcoder.management.review.assignment.notification.UnitTestEmailBasedReviewAssignmentNotificationManager;
import com.topcoder.management.review.assignment.notification.UnitTestEmailSendingUtility;
import com.topcoder.management.review.assignment.project.UnitTestDefaultReviewAssignmentProjectManager;
import com.topcoder.management.review.assignment.utility.UnitTestReviewAssignmentUtility;

/**
 * <p>
 * This test case aggregates all Unit test cases.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class UnitTests extends TestCase {
    /**
     * Adds all test suites to the unit test suite and returns the unit test suite.
     *
     * @return the unit test suite.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        suite.addTestSuite(UnitTestReviewAssignmentConfigurationException.class);
        suite.addTestSuite(UnitTestReviewAssignmentException.class);
        suite.addTestSuite(UnitTestReviewAssignmentNotificationException.class);
        suite.addTestSuite(UnitTestReviewAssignmentProjectManagementException.class);
        suite.addTestSuite(UnitTestReviewAssignmentAlgorithmException.class);
        suite.addTestSuite(UnitTestMaxSumOfRatingReviewAssignmentAlgorithm.class);
        suite.addTestSuite(UnitTestBruteForceBasedReviewAssignmentAlgorithm.class);
        suite.addTestSuite(UnitTestEmailBasedReviewAssignmentNotificationManager.class);
        suite.addTestSuite(UnitTestEmailSendingUtility.class);
        suite.addTestSuite(UnitTestDefaultReviewAssignmentProjectManager.class);
        suite.addTestSuite(UnitTestReviewAssignmentManager.class);
        suite.addTestSuite(UnitTestReviewAssignmentJobRunner.class);
        suite.addTestSuite(UnitTestReviewAssignmentUtility.class);
        suite.addTestSuite(Demo.class);
        suite.addTestSuite(UnitTestHelper.class);
        return suite;
    }
}
