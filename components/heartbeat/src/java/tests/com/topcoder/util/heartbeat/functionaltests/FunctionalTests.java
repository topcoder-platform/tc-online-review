package com.topcoder.util.heartbeat.functionaltests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>This test case aggregates all functional test cases.</p>
 *
 * @author TopCoder Software
 * @version 1.0
 */
public class FunctionalTests extends TestCase {

    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTest(new TestSuite(HeartBeatManagerTest.class));
        suite.addTest(new TestSuite(ManualTriggerTest.class));
        suite.addTest(new TestSuite(OutputStreamHeartBeatTest.class));
        suite.addTest(new TestSuite(TimerTriggerTest.class));
        suite.addTest(new TestSuite(URLHeartBeatTest.class));
        return suite;
    }

}
