package com.topcoder.util.exec.failuretests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import com.topcoder.util.exec.*;

/**
 * <p>This test case aggregates all Executable Wrapper test cases.</p>
 *
 * The various test suites try to trigger the exceptions described in the spec.
 * Since bodrius's asynchronous process halter doesn't work, so I had to hack it to run a different java program
 * that takes a while to run yet still terminates on its own.
 * @author srowen,b0b0b0b
 * @version 1.0
 */
public class FailureTests extends TestCase {

    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTest(IllegalArgumentExceptionCases.suite());
        suite.addTest(IllegalStateExceptionCases.suite());
        suite.addTest(ExecutionExceptionCases.suite());
        suite.addTest(TimeoutExceptionCases.suite());

        return suite;
    }

}
