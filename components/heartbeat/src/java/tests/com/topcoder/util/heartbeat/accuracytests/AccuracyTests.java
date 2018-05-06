package com.topcoder.util.heartbeat.accuracytests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>This test case aggregates all accuracy test cases.</p>
 *
 * @author TopCoder Software
 * @version 1.0
 */
public class AccuracyTests extends TestCase {

    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTest(new TestSuite(HeartBeatManagerTestCase.class));
        suite.addTest(new TestSuite(ManualTriggerTestCase.class));
        suite.addTest(new TestSuite(TimerTriggerTestCase.class));
        suite.addTest(new TestSuite(OutputStreamHeartBeatTestCase.class));
        suite.addTest(new TestSuite(SocketHeartBeatTestCase.class));
        suite.addTest(new TestSuite(URLHeartBeatTestCase.class));
        return suite;
    }

}
