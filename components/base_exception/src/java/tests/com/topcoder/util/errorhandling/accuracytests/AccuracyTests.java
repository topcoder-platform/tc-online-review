/**
 * Copyright (c) 2007, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.errorhandling.accuracytests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * <p>
 * This test case aggregates all Accuracy test cases.
 * </p>
 *
 * @author TopCoder
 * @version 1.0
 */
public class AccuracyTests extends TestCase {
    /**
     * TODO
     *
     * @return  TODO
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        //suite.addTest(XXX.suite());
        suite.addTestSuite(ExceptionUtilsTestCases.class);
        suite.addTestSuite(BaseRuntimeExceptionTestCases.class);
        suite.addTestSuite(BaseNonCriticalExceptionTestCases.class);
        suite.addTestSuite(BaseCriticalExceptionTestCases.class);
        suite.addTestSuite(BaseExceptionTestCases.class);
        suite.addTestSuite(BaseErrorTestCases.class);
        suite.addTestSuite(ExceptionDataTestCases.class);
        suite.addTestSuite(CauseUtilsTestCases.class);
        return suite;
    }
}
