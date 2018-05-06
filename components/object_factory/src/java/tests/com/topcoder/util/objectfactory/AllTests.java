/*
 * Copyright (C) 2006, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.objectfactory;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.util.objectfactory.accuracytests.AccuracyTests;
import com.topcoder.util.objectfactory.failuretests.FailureTests;
import com.topcoder.util.objectfactory.stresstests.StressTests;

/**
 * <p>This test case aggregates all test cases.</p>
 *
 * @author GavinWang
 * @version 2.1
 */
public class AllTests extends TestCase {

    /**
     * <p>
     * Return all tests.
     * </p>
     *
     * @return all tests
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
