/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.collection.typesafeenum;

import com.topcoder.util.collection.typesafeenum.accuracytests.AccuracyTests;
import com.topcoder.util.collection.typesafeenum.failuretests.FailureTests;
import com.topcoder.util.collection.typesafeenum.stresstests.StressTests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * This test case aggregates all test cases.
 *
 * @author TCSDEVELOPER
 * @version 1.1
 */
public class AllTests extends TestCase {

    /**
     * Aggregates all test cases.
     *
     * @return the aggregated all test cases
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTest(UnitTests.suite());
        suite.addTest(AccuracyTests.suite());
        suite.addTest(FailureTests.suite());
        suite.addTest(StressTests.suite());
        return suite;
    }
}
