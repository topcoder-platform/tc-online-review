/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.commons.utils;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * This test case aggregates all Unit test cases.
 * </p>
 *
 * @author sparemax
 * @version 1.0
 */
public class UnitTests extends TestCase {
    /**
     * <p>
     * All unit test cases.
     * </p>
     *
     * @return The test suite.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        suite.addTest(Demo.suite());
        suite.addTest(HelperUnitTests.suite());

        suite.addTest(ParameterCheckUtilityUnitTests.suite());
        suite.addTest(ValidationUtilityUnitTests.suite());
        suite.addTest(PropertiesUtilityUnitTests.suite());
        suite.addTest(JDBCUtilityUnitTests.suite());
        suite.addTest(Log4jUtilityUnitTests.suite());
        suite.addTest(LoggingWrapperUtilityUnitTests.suite());
        suite.addTest(LoggingUtilityHelperUnitTests.suite());
        suite.addTest(ExceptionHelperUnitTests.suite());

        return suite;
    }
}