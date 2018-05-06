/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.servlet.request;

import com.topcoder.servlet.request.accuracytests.AccuracyTests;
import com.topcoder.servlet.request.failuretests.FailureTests;
import com.topcoder.servlet.request.stresstests.StressTests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * <p>
 * This test case aggregates all test cases.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 2.0
 */
public class AllTests extends TestCase {
    /**
     * <p>
     * Creates a test suite of the tests contained in this class.
     * </p>
     *
     * @return a test suite of the tests contained in this class.
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
