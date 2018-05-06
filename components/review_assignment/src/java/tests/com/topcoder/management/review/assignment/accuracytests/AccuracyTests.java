/**
 *
 * Copyright (c) 2013, TopCoder, Inc. All rights reserved
 */
package com.topcoder.management.review.assignment.accuracytests;

import com.topcoder.management.review.assignment.accuracytests.algorithm.BruteForceBasedReviewAssignmentAlgorithmAccuracyTest;
import com.topcoder.management.review.assignment.accuracytests.algorithm.MaxSumOfRatingReviewAssignmentAlgorithmAccuracyTest;
import com.topcoder.management.review.assignment.accuracytests.notification.EmailBasedReviewAssignmentNotificationManagerAccuracyTest;
import com.topcoder.management.review.assignment.accuracytests.notification.EmailSendingUtilityAccuracyTest;
import com.topcoder.management.review.assignment.accuracytests.project.DefaultReviewAssignmentProjectManagerAccuracyTest;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>This test case aggregates all Accuracy test cases.</p>
 *
 * @author TopCoder
 * @version 1.0
 */
public class AccuracyTests extends TestCase {

    public static Test suite() {
        final TestSuite suite = new TestSuite();

        suite.addTest(ReviewAssignmentConfigurationExceptionAccuracyTest.suite());
        suite.addTest(ReviewAssignmentExceptionAccuracyTest.suite());
        suite.addTest(ReviewAssignmentAlgorithmExceptionAccuracyTest.suite());
        suite.addTest(ReviewAssignmentNotificationExceptionAccuracyTest.suite());
        suite.addTest(ReviewAssignmentProjectManagementExceptionAccuracyTest.suite());
        suite.addTest(BruteForceBasedReviewAssignmentAlgorithmAccuracyTest.suite());
        suite.addTest(MaxSumOfRatingReviewAssignmentAlgorithmAccuracyTest.suite());
        suite.addTest(EmailSendingUtilityAccuracyTest.suite());
        suite.addTest(EmailBasedReviewAssignmentNotificationManagerAccuracyTest.suite());
        suite.addTest(DefaultReviewAssignmentProjectManagerAccuracyTest.suite());
        suite.addTest(ReviewAssignmentManagerAccuracyTest.suite());
        suite.addTest(ReviewAssignmentJobRunnerAccuracyTest.suite());

        suite.addTest(BruteForceBasedReviewAssignmentAlgorithmAccuracyTests.suite());
        suite.addTest(MaxSumOfRatingReviewAssignmentAlgorithmAccuracyTests.suite());
        suite.addTest(EmailSendingUtilityAccuracyTests.suite());
        suite.addTest(EmailBasedReviewAssignmentNotificationManagerAccuracyTests.suite());
        suite.addTest(DefaultReviewAssignmentProjectManagerAccuracyTests.suite());
        suite.addTest(ReviewAssignmentManagerAccuracyTests.suite());

        return suite;
    }

}
