package com.topcoder.util.exec.functionaltests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>This test case aggregates all Executable Wrapper test cases.</p>
 *
 * @author srowen
 * @version 1.0
 */
public class FunctionalTests extends TestCase {

    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTest(ExecuteSynchronouslyTestCase.suite());
        suite.addTest(ExecuteSynchronouslyWithTimeoutTestCase.suite());
        suite.addTest(ExecuteAsynchronouslyTestCase.suite());
        suite.addTest(ExecuteAsynchronouslyWithTimeoutTestCase.suite());
        suite.addTest(RegisterPlatformSupportTestCase.suite());
        return suite;
    }

}
