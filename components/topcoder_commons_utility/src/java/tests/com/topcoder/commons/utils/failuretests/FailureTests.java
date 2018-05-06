/*
 * Copyright (C) 2009 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.commons.utils.failuretests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Failure tests suite.
 * 
 * @author Beijing2008
 * @version 1.0
 */
public class FailureTests extends TestCase {
    /**
     * <p>
     * Aggregates all failure test cases.
     * </p>
     * 
     * @return the test suite
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTestSuite(ValidationUtilityFailureTest.class);
        suite.addTestSuite(JDBCUtilityFailureTest.class);
        suite.addTestSuite(ParameterCheckUtilityFailureTest.class);
        suite.addTestSuite(PropertiesUtilityFailureTest.class);

        return suite;
    }
}
