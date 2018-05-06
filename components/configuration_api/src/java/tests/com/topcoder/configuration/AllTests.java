/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.configuration;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.configuration.accuracytests.AccuracyTests;
import com.topcoder.configuration.failuretests.FailureTests;
import com.topcoder.configuration.stresstests.StressTests;

/**
 * <p>This test case aggregates all test cases.</p>
 *
 * @author TopCoder
 * @version 1.0
 */
public class AllTests extends TestCase {

    public static Test suite() {
        final TestSuite suite = new TestSuite();

        //unit tests
        suite.addTest(UnitTests.suite());
        suite.addTest(AccuracyTests.suite());
        suite.addTest(FailureTests.suite());
        suite.addTest(StressTests.suite());

        return suite;
    }

}
