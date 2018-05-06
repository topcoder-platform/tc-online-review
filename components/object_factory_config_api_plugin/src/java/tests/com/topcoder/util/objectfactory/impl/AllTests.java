/*
 * Copyright (C) 2004 TopCoder Inc., All Rights Reserved.
 * AllTests.java
 * TCS Authorization 2.1
 */
package com.topcoder.util.objectfactory.impl;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.util.objectfactory.impl.accuracytests.AccuracyTests;
import com.topcoder.util.objectfactory.impl.failuretests.FailureTests;
import com.topcoder.util.objectfactory.impl.stresstests.StressTests;

/**
 * <p>This test case aggregates all test cases.</p>
 *
 * @author TopCoder
 * @version 1.0
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
