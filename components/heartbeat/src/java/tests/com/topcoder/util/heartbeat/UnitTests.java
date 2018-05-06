package com.topcoder.util.heartbeat;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>This test case aggregates all unit test cases.</p>
 *
 * @author TopCoder Software
 * @version 1.0
 */
public class UnitTests extends TestCase {

    public static Test suite() {
        final TestSuite suite = new TestSuite();
        //suite.addTest(XXX.suite());
        suite.addTest(new TestSuite(OutputStreamHeartBeatTestCase.class));
        suite.addTest(new TestSuite(SocketHeartBeatTestCase.class));
        suite.addTest(new TestSuite(URLHeartBeatTestCase.class));
        suite.addTest(new TestSuite(ManualTriggerTestCase.class));
        suite.addTest(new TestSuite(TimerTriggerTaskTestCase.class));
        suite.addTest(new TestSuite(TimerTriggerTestCase.class));
        suite.addTest(new TestSuite(HeartBeatManagerTestCase.class));
        return suite;
    }

}
