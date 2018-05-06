/*
 * Copyright (c) 2007, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.scheduler.scheduling.stresstests;

import com.topcoder.util.scheduler.scheduling.stresstests.persistence.ConfigurationObjectSchedulerTests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>This test case aggregates all Stress test cases.</p>
 *
 * @author  80x86
 * @version 3.1
 */
public class StressTests extends TestCase {

    /**
     * Returns the stress test suite.
     *
     * @return stress test suite.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        //suite.addTestSuite(ConfigManagerSchedulerStressTest.class);
        //suite.addTestSuite(DBSchedulerStressTest.class);
        suite.addTestSuite(Job31Tests.class);
        suite.addTestSuite(ScheduledEnableObjectFactoryManagerTests.class);

        suite.addTestSuite(ConfigurationObjectSchedulerTests.class);

        return suite;
    }
}
