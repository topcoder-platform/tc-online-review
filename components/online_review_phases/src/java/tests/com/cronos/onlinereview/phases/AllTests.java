/*
 * Copyright (C) 2009-2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import com.cronos.onlinereview.phases.accuracytests.AccuracyTests;
import com.cronos.onlinereview.phases.failuretests.FailureTests;
import com.cronos.onlinereview.phases.stresstests.StressTests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * This test case aggregates all test cases.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.4
 */
public class AllTests extends TestCase {
    /**
     * Gets all test suites.
     *
     * @return all test suites.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        suite.addTest(UnitTests.suite());
        suite.addTest(AccuracyTests.suite());
        suite.addTest(FailureTests.suite());
        suite.addTest(StressTests.suite());

        return suite;
    }

}
