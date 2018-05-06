/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.ajax;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import com.cronos.onlinereview.ajax.accuracytests.AccuracyTests;
import com.cronos.onlinereview.ajax.failuretests.FailureTests;
import com.cronos.onlinereview.ajax.stresstests.StressTests;

/**
 * <p>This test case aggregates all test cases.</p>
 *
 * @author assistant
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
