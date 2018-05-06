/*
 * Copyright (C) 2006, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.objectfactory.impl;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * This test case aggregates all unit test cases.
 * </p>
 *
 * @author mgmg, TCSDEVELOPER
 * @version 1.1
 */
public class UnitTests extends TestCase {
    /**
     * Aggregate all unit tests.
     *
     * @return the test suite.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        suite.addTestSuite(ConfigManagerSpecificationFactoryUnitTest.class);

        suite.addTestSuite(Demo.class);

        return suite;
    }
}
