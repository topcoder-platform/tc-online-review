/**
 *
 * Copyright (c) 2010, TopCoder, Inc. All rights reserved
 */
package com.topcoder.management.deliverable.latetracker.accuracytests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>This test case aggregates all Accuracy test cases.</p>
 *
 * @author TopCoder
 * @version 1.2
 */
public class AccuracyTests extends TestCase {

    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTestSuite(LateDeliverablesRetrieverImplAccTests.class);
        suite.addTestSuite(EmailSendingUtilityAccTests.class);
        suite.addTestSuite(LateDeliverableProcessorImplAccTests.class);
        suite.addTestSuite(LateDeliverablesTrackingUtilityAccTests.class);
        suite.addTestSuite(NotRespondedLateDeliverablesNotificationJobRunnerAccTests.class);
        suite.addTestSuite(NotRespondedLateDeliverablesNotifierAccTests.class);
        return suite;
    }

}
