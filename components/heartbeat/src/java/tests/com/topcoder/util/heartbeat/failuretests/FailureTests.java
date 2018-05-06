package com.topcoder.util.heartbeat.failuretests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>This test case aggregates all failure test cases.</p>
 *
 * @author TopCoder Software
 * @version 1.0
 */
public class FailureTests extends TestCase {

    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTest(new TestSuite(MyFailureTests.class));
        return suite;
    }

}
