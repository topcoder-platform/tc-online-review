package com.topcoder.util.exec.accuracytests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>This test case aggregates all Executable Wrapper test cases.</p>
 *
 * @author srowen
 * @version 1.0
 */
public class AccuracyTests extends TestCase {

    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTest(TestIllegalStateException.suite());
        suite.addTest(TestIllegalArgumentExceptions.suite());
        suite.addTest(TestTimedOutException.suite());
        suite.addTest(TestExecutionHalt.suite());
        suite.addTest(TestAsynchronousExecutorHandle.suite());
        suite.addTest(TestExec.suite());
        suite.addTest(TestExecutionParameters.suite());
        
        return suite;
    }

}
