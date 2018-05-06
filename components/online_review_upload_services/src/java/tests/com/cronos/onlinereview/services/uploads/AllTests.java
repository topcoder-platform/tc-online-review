/**
 *
 * Copyright (c) 2007, TopCoder, Inc. All rights reserved
 */
package com.cronos.onlinereview.services.uploads;

import com.cronos.onlinereview.services.uploads.accuracytests.AccuracyTests;
import com.cronos.onlinereview.services.uploads.failuretests.FailureTests;
import com.cronos.onlinereview.services.uploads.stresstests.StressTests;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>This test case aggregates all test cases.</p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class AllTests extends TestCase {

    /**
     * <p>Creates test suite for all tests.</p>
     * 
     * @return test suite for all tests
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
