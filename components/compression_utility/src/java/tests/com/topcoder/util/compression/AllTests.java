package com.topcoder.util.compression;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * <p>
 * This test case aggregates all test cases for this package.
 * </p>
 *
 * @author TopCoder Software
 * @version 1.0
 */
public class AllTests extends TestCase {
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        //compression tests
        //unit tests
        suite.addTest(com.topcoder.util.compression.UnitTests.suite());

        //functional tests
        suite.addTest(com.topcoder.util.compression.functionaltests.FunctionalTests.suite());

        //accuracy tests
        suite.addTest(com.topcoder.util.compression.accuracytests.AccuracyTests.suite());

        //failure tests
        suite.addTest(com.topcoder.util.compression.failuretests.FailureTests.suite());

        //stress tests
        suite.addTest(com.topcoder.util.compression.stresstests.StressTests.suite());

        //archiving tests
        //unit tests
        suite.addTest(com.topcoder.util.archiving.UnitTests.suite());

        //accuracy tests
        suite.addTest(com.topcoder.util.archiving.accuracytests.AccuracyTests.suite());

        //failure tests
        suite.addTest(com.topcoder.util.archiving.failuretests.FailureTests.suite());

        //stress tests
        suite.addTest(com.topcoder.util.archiving.stresstests.StressTests.suite());

        return suite;
    }
}








