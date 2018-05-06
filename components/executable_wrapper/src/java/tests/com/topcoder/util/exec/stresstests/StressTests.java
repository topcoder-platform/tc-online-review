/**
 * StressTests.java
 *
 * Copyright © 2003, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.exec.stresstests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.TestResult;

/**
 * <p>This test case aggregates all Executable Wrapper test cases.</p>
 *
 * @author srowen
 * @author adic
 * @version 1.0
 */
public class StressTests extends TestCase {

    public static Test suite() {
        final TestSuite suite = new TestSuite();

        suite.addTest(LargeOutputSynchronousTestCase.suite());
        suite.addTest(LargeOutputSynchronousWithTimeoutTestCase.suite());
        suite.addTest(LargeOutputAsynchronousTestCase.suite());
        suite.addTest(LargeOutputAsynchronousWithTimeoutTestCase.suite());

        suite.addTest(ManyRunsSynchronousTestCase.suite());
        suite.addTest(ManyRunsSynchronousThreadedTestCase.suite());
        suite.addTest(ManyRunsAsynchronousTestCase.suite());
        suite.addTest(ManyRunsMixedLargeOutputTestCase.suite());

        return suite;
    }
}
