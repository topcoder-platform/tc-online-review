/**
 * Copyright (c) 2010, TopCoder, Inc. All rights reserved
 */
package com.topcoder.management.deliverable.latetracker.failuretests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * <p>This test case aggregates all Failure test cases.</p>
 *
 * @author TopCoder
 * @version 1.1
 */
public class FailureTests extends TestCase {
    /**
     * Get all failure cases.
     *
     * @return all failure cases
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTestSuite(EmailSendingUtilityFailureTests.class);
        suite.addTestSuite(LateDeliverableProcessorImplFailureTests.class);
        suite.addTestSuite(LateDeliverablesRetrieverImplFailureTests.class);
        suite.addTestSuite(LateDeliverablesTrackerFailureTest.class);
        suite.addTestSuite(LateDeliverablesTrackingJobRunnerFailureTest.class);

        suite.addTestSuite(NotRespondedLateDeliverablesNotificationJobRunnerFailureTests.class);
        suite.addTestSuite(NotRespondedLateDeliverablesNotifierFailureTests.class);

        return suite;
    }
}
