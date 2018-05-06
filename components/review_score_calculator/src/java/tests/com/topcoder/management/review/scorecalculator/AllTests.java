/**
 *
 * Copyright (c) 2006, TopCoder, Inc. All rights reserved
 */
package com.topcoder.management.review.scorecalculator;

import com.topcoder.management.review.scorecalculator.accuracytests.AccuracyTests;
import com.topcoder.management.review.scorecalculator.failuretests.FailureTests;
import com.topcoder.management.review.scorecalculator.stresstests.StressTests;

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
     * Aggregates all test cases.
     *
     * @return Test
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        // add unit test suite.
        suite.addTest(UnitTests.suite());

        // add failure test suite.
        suite.addTest(FailureTests.suite());

        // add stress test suite.
        suite.addTest(StressTests.suite());

        // add accuracy test suite.
        suite.addTest(AccuracyTests.suite());

        return suite;
    }

}
