/**
 *
 * Copyright (c) 2010, TopCoder, Inc. All rights reserved
 */

package com.topcoder.management.deliverable.latetracker.stresstests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * This test case aggregates all Stress test cases.
 * </p>
 *
 * @author TopCoder
 * @version 1.0
 */
public class StressTests extends TestCase {
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        suite.addTest(LateDeliverablesTrackerStressTests.suite());
        suite.addTest(LateDeliverablesTrackingJobRunnerStressTests.suite());
        suite.addTest(NotRespondedLateDeliverablesNotificationJobRunnerStressTests.suite());

        return suite;
    }
}
