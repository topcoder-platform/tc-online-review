/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.scheduling.failuretests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * This test case aggregates all Failure test cases.
 * </p>
 *
 * @author TopCoder
 * @version 3.1
 */
public class FailureTests extends TestCase {
    /**
     * failure tests.
     *
     * @return Test instance.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTestSuite(FailureTestEmailEventHandler.class);
        suite.addTestSuite(FailureTestDependence.class);
        suite.addTestSuite(FailureTestDBScheduler.class);
        suite.addTestSuite(FailureTestDaysOfWeek.class);
        suite.addTestSuite(FailureTestDayOfYear.class);
        suite.addTestSuite(FailureTestDayOfMonth.class);
        suite.addTestSuite(FailureTestConfigManagerScheduler.class);
        suite.addTestSuite(FailureTestJob.class);
        suite.addTestSuite(FailureTestJobGroup.class);
        suite.addTestSuite(FailureTestWeekMonthOfYear.class);
        suite.addTestSuite(FailureTestWeekOfMonth.class);

        suite.addTestSuite(FailureTestScheduledEnableObjectFactoryManager.class);
        suite.addTestSuite(FailureTestConfigurationObjectScheduler.class);
        return suite;
    }
}
