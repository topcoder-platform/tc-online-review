/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder;

import com.topcoder.search.builder.accuracytests.AccuracyTests;
import com.topcoder.search.builder.failuretests.FailureTests;
import com.topcoder.search.builder.stresstests.StressTests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * <p>
 * Unit test cases for AllTests.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.1
 */
public class AllTests extends TestCase {
    /**
     * all test suite.
     * @return the test suite.
     */
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
