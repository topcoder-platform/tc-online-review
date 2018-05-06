/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.log;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.util.log.accuracytests.AccuracyTests;
import com.topcoder.util.log.failuretests.FailureTests;
import com.topcoder.util.log.stresstests.StressTests;

/**
 * <p>This test case aggregates all test cases.</p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class AllTests extends TestCase {

    /**
     * <p>
     * Test suite for all tests case.
     * </p>
     *
     * @return the test suite.
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