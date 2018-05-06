/**
 *
 * Copyright (c) 2004, TopCoder, Inc. All rights reserved
 */
package com.topcoder.processor.ipserver;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.processor.ipserver.accuracytests.AccuracyTests;
import com.topcoder.processor.ipserver.failuretests.FailureTests;
import com.topcoder.processor.ipserver.stresstests.StressTests;

/**
 * <p>This test case aggregates all test cases.</p>
 *
 * @author TopCoder
 * @version 1.0
 */
public class AllTests extends TestCase {

    /**
     * Aggregate all test coses.
     *
     * @return all test suite of IPServer component
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        //unit tests
        suite.addTest(UnitTests.suite());

        //accuracy tests
        suite.addTest(AccuracyTests.suite());

        //failure tests
        suite.addTest(FailureTests.suite());

        //stress tests
        suite.addTest(StressTests.suite());

        return suite;
    }

}
