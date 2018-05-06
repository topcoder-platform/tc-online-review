/**
 * Copyright ? 2003, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.datavalidator;

import com.topcoder.util.datavalidator.accuracytests.AccuracyTests;
import com.topcoder.util.datavalidator.failure.FailureTests;
import com.topcoder.util.datavalidator.stresstests.StressTests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * <p>
 * This test case aggregates all test cases.
 * </p>
 *
 * @author TopCoder
 * @version 1.0
 */
public class AllTests extends TestCase {
    /**
     * TODO
     *
     * @return  TODO
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        //unit tests
        suite.addTest(UnitTests.suite());

        //functional tests
        // suite.addTest(FunctionalTests.suite());
        //accuracy tests
        suite.addTest(AccuracyTests.suite());

        //failure tests
        suite.addTest(FailureTests.suite());

        //stress tests
        suite.addTest(StressTests.suite());

        return suite;
    }
}
