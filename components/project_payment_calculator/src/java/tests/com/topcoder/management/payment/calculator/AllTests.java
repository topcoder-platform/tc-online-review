/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.payment.calculator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.management.payment.calculator.accuracytests.AccuracyTests;
import com.topcoder.management.payment.calculator.failuretests.FailureTests;
import com.topcoder.management.payment.calculator.stresstests.StressTests;


/**
 * <p>
 * This test case aggregates all test cases.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class AllTests extends TestCase {
    /**
     * <p>
     * Creates a test suite for the tests in this test case.
     * </p>
     *
     * @return a Test suite for this test case.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        // unit tests
        suite.addTest(UnitTests.suite());

        // accuracy tests
        suite.addTest(AccuracyTests.suite());

        // failure tests
        suite.addTest(FailureTests.suite());

        // stress tests
        suite.addTest(StressTests.suite());

        return suite;
    }
}
