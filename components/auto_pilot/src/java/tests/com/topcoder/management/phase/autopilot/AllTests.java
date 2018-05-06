/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.phase.autopilot;

import com.topcoder.management.phase.autopilot.accuracytests.AccuracyTests;
import com.topcoder.management.phase.autopilot.failuretests.FailureTests;
import com.topcoder.management.phase.autopilot.stresstests.StressTests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


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
     * Aggregates all test cases.
     *
     * @return the test suite aggregates all test cases.
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
