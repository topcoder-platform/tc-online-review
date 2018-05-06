/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.errorhandling.stresstests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * This test case aggregates all Stress test cases.
 * </p>
 *
 * @author kzhu
 * @version 2.0
 */
public class StressTests extends TestCase {

    /**
     * Aggregates all Stress test cases and returns a testSuite.
     *
     * @return the test suite.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTestSuite(BaseCriticalExceptionStressTests.class);
        suite.addTestSuite(BaseErrorStressTests.class);
        suite.addTestSuite(BaseExceptionStressTests.class);
        suite.addTestSuite(BaseNonCriticalExceptionStressTests.class);
        suite.addTestSuite(BaseRuntimeExceptionStressTests.class);
        suite.addTestSuite(CauseUtilsStressTests.class);
        suite.addTestSuite(ExceptionDataStressTests.class);
        suite.addTestSuite(ExceptionUtilsStressTests.class);
        return suite;
    }
}
