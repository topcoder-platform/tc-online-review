/**
 * Copyright (c) 2006, TopCoder, Inc. All rights reserved
 */
package com.cronos.onlinereview.ajax.stresstests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * <p>
 * This test case aggregates all Stress test cases.
 * </p>
 *
 * @author PE
 * @version 1.0
 */
public class StressTests extends TestCase {
    /**
     * Aggregates the test cases.
     *
     * @return the test suite.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        suite.addTestSuite(LoadTimelineTemplateHandlerStressTest.class);
        suite.addTestSuite(PlaceAppealHandlerStressTest.class);
        suite.addTestSuite(ResolveAppealHandlerStressTest.class);
        suite.addTestSuite(SetScorecardStatusHandlerStressTest.class);
        suite.addTestSuite(SetTimelineNotificationHandlerStressTest.class);

        return suite;
    }
}
