/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.errorhandling;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import com.topcoder.util.errorhandling.accuracytests.AccuracyTests;
import com.topcoder.util.errorhandling.failuretests.FailureTests;
import com.topcoder.util.errorhandling.stresstests.StressTests;

/**
 * <p>
 * This test case aggregates all test cases.
 * </p>
 * @author FireIce
 * @version 1.0
 */
public class AllTests extends TestCase {

    /**
     * <p>
     * Aggregates all the test cases.
     * </p>
     * @return the test case suite.
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
