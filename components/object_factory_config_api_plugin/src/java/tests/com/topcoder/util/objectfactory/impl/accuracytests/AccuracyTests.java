/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.objectfactory.impl.accuracytests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>This test case aggregates all accuracy test cases.</p>
 *
 * @author 80x86
 * @version 1.1
 */
public class AccuracyTests extends TestCase {
    /**
     * Returns the test suite.
     *
     * @return the test suite.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        suite.addTestSuite(ConfigurationObjectSpecificationFactoryAccuracyTests.class);

        return suite;
    }
}
