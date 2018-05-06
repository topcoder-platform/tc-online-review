/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.errorhandling;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * This test case aggregates all unit test cases.
 *
 * @author TCSDEVELOPER
 * @version 2.0
 */
public class UnitTests extends TestCase {

    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTestSuite(BaseCriticalExceptionTests.class);
        suite.addTestSuite(BaseErrorTests.class);
        suite.addTestSuite(BaseExceptionTests.class);
        suite.addTestSuite(BaseNonCriticalExceptionTests.class);
        suite.addTestSuite(BaseRuntimeExceptionTests.class);
        suite.addTestSuite(CauseUtilsTests.class);
        suite.addTestSuite(ComponentDemo.class);
        suite.addTestSuite(ExceptionDataTests.class);
        suite.addTestSuite(ExceptionUtilsTests.class);
        return suite;
    }

}
