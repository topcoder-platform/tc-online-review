/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.objectfactory.impl;

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
     * Aggregate all the unit test cases.
     * </p>
     *
     * @return the test sutie.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        suite.addTestSuite(ConfigurationObjectSpecificationFactoryTestCase.class);
        suite.addTestSuite(Demo.class);

        return suite;
    }

}
