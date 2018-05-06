/*
 * Copyright (C) 2010, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.latetracker;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.management.deliverable.latetracker.notification
    .NotRespondedLateDeliverablesNotificationExceptionTests;
import com.topcoder.management.deliverable.latetracker.notification
    .NotRespondedLateDeliverablesNotificationJobRunnerTests;
import com.topcoder.management.deliverable.latetracker.notification.NotRespondedLateDeliverablesNotifierTests;
import com.topcoder.management.deliverable.latetracker.processors.LateDeliverableProcessorImplTests;
import com.topcoder.management.deliverable.latetracker.retrievers.LateDeliverablesRetrieverImplTests;
import com.topcoder.management.deliverable.latetracker.utility.LateDeliverablesTrackingUtilityTests;

/**
 * <p>
 * This test case aggregates all Unit test cases.
 * </p>
 *
 * <p>
 * <em>Changes in version 1.3:</em>
 * <ol>
 * <li>Added LateDeliverableTypeUnitTests.</li>
 * </ol>
 * </p>
 *
 * @author myxgyy, sparemax
 * @version 1.3
 */
public class UnitTests extends TestCase {
    /**
     * Adds all test suites to the unit test suite and returns the unit test suite.
     *
     * @return the unit test suite.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        suite.addTestSuite(Demo.class);
        suite.addTestSuite(HelperTests.class);

        suite.addTestSuite(LateDeliverableTypeUnitTests.class);
        suite.addTestSuite(LateDeliverablesTrackingJobRunnerTests.class);
        suite.addTestSuite(LateDeliverablesTrackerTests.class);
        suite.addTestSuite(LateDeliverableTests.class);
        suite.addTestSuite(LateDeliverablesProcessingExceptionTests.class);
        suite.addTestSuite(LateDeliverablesRetrievalExceptionTests.class);
        suite.addTestSuite(LateDeliverablesTrackerConfigurationExceptionTests.class);
        suite.addTestSuite(LateDeliverablesTrackingExceptionTests.class);
        suite.addTestSuite(EmailSendingExceptionTests.class);

        suite.addTestSuite(LateDeliverablesRetrieverImplTests.class);

        suite.addTestSuite(EmailSendingUtilityTests.class);
        suite.addTestSuite(LateDeliverableProcessorImplTests.class);

        suite.addTestSuite(LateDeliverablesTrackingUtilityTests.class);

        suite.addTestSuite(NotRespondedLateDeliverablesNotificationExceptionTests.class);
        suite.addTestSuite(NotRespondedLateDeliverablesNotificationJobRunnerTests.class);
        suite.addTestSuite(NotRespondedLateDeliverablesNotifierTests.class);

        return suite;
    }
}
