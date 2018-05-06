/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.db.connectionfactory;

import com.topcoder.db.connectionfactory.accuracytests.AccuracyTests;
import com.topcoder.db.connectionfactory.failuretests.FailureTests;
import com.topcoder.db.connectionfactory.stresstests.StressTests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * <p>
 * This test case aggregates all test cases.
 * </p>
 *
 * @author magicpig
 * @version 1.1
 */
public class AllTests extends TestCase {
    /**
     * Aggregates all test cases.
     *
     * @return the test suite
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
