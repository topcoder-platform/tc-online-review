/*
 * Copyright (C) 2004 TopCoder Inc., All Rights Reserved.
 * AllTests.java
 * TCS Authorization 2.1
 */
package com.topcoder.util.sql.databaseabstraction;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.util.sql.databaseabstraction.accuracytests.AccuracyTests;
import com.topcoder.util.sql.databaseabstraction.failuretests.FailureTests;
import com.topcoder.util.sql.databaseabstraction.stresstests.StressTests;

/**
 * <p>This test case aggregates all test cases.</p>
 *
 * @author TopCoder
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
