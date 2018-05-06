/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.commons.utils;

import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * <p>
 * Accuracy tests for this component.
 * </p>
 *
 * @author dingying131
 * @version 1.0
 */
public class AccuracyTests {

    /**
     * <p>
     * Private constructor.It does nothing.
     * </p>
     */
    private AccuracyTests() {
    }

    /**
     * <p>
     * Test suite for all tests.
     * </p>
     *
     * @return Test suit
     */
    public static Test suite() {

        TestSuite suite = new TestSuite("Accuracy tests");
        suite.addTest(ExceptionHelperAccuracyTests.suite());
        suite.addTest(JDBCUtilityAccuracyTests.suite());
        suite.addTest(Log4jUtilityAccuracyTests.suite());
        suite.addTest(LoggingUtilityHelperAccuracyTests.suite());
        suite.addTest(LoggingWrapperUtilityAccuracyTests.suite());
        suite.addTest(ParameterCheckUtilityAccuracyTests.suite());
        suite.addTest(PropertiesUtilityAccuracyTests.suite());
        suite.addTest(ValidationUtilityAccuracyTests.suite());
        return suite;
    }
}
