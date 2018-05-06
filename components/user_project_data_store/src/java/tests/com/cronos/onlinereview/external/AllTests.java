/**
 *
 * Copyright (c) 2007, TopCoder, Inc. All rights reserved
 */
package com.cronos.onlinereview.external;

import com.cronos.onlinereview.external.accuracytests.AccuracyTests;
import com.cronos.onlinereview.external.failuretests.FailureTests;
import com.cronos.onlinereview.external.stresstests.StressTests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>This test case aggregates all test cases.</p>
 *
 * @author TopCoder
 * @version 1.1
 */
public class AllTests extends TestCase {

    public static Test suite() {
        final TestSuite suite = new TestSuite();

        // unit tests
        suite.addTest(UnitTests.suite());
        suite.addTest(AccuracyTests.suite());
        suite.addTest(FailureTests.suite());
        suite.addTest(StressTests.suite());

        return suite;
    }
}
