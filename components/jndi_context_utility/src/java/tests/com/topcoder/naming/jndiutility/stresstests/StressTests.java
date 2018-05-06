/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.naming.jndiutility.stresstests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * This test case aggregates all Stress test cases.
 *
 * @author adic
 * @version 2.0
 */
public class StressTests extends TestCase {

    /**
     * Builds the stress tests suite.
     * 
     * @return the test suite
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTest(new TestSuite(JNDIUtilsTestCase.class));
        suite.addTest(new TestSuite(JNDIUtilTestCase.class));
        return suite;
    }
}
