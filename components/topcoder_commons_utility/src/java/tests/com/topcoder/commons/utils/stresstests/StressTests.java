/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.commons.utils.stresstests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * This test case aggregates all Stress test cases.
 * </p>
 * 
 * @author mumujava
 * @version 1.0
 */
public class StressTests extends TestCase {

    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTest(Log4jUtilityStressTest.suite());
        suite.addTest(LoggingWrapperUtilityStressTest.suite());
        suite.addTest(PropertiesUtilityStressTest.suite());
        suite.addTest(ParameterCheckUtilityStressTest.suite());
        suite.addTest(ValidationUtilityStressTest.suite());
        suite.addTest(JDBCUtilityStressTest.suite());
        return suite;
    }
}
