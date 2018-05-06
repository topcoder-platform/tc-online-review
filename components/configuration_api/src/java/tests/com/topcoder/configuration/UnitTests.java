/*
 * Copyright (C) 2007, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.configuration;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.configuration.defaults.DefaultConfigurationObjectTestsOne;
import com.topcoder.configuration.defaults.DefaultConfigurationObjectTestsTwo;

/**
 * <p>
 * This test case aggregates all Unit test cases.
 * </p>
 *
 * @author haozhangr, sparemax
 * @version 1.1
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
        suite.addTestSuite(BaseConfigurationObjectTests.class);
        suite.addTestSuite(SynchronizedConfigurationObjectTestsOne.class);
        suite.addTestSuite(SynchronizedConfigurationObjectTestsTwo.class);
        suite.addTestSuite(TemplateConfigurationObjectTests.class);
        suite.addTestSuite(DefaultConfigurationObjectTestsOne.class);
        suite.addTestSuite(DefaultConfigurationObjectTestsTwo.class);
        suite.addTestSuite(Demo.class);
        suite.addTestSuite(HelperTests.class);

        // Exception
        suite.addTestSuite(ConfigurationAccessExceptionTests.class);
        suite.addTestSuite(ConfigurationExceptionTests.class);
        suite.addTestSuite(InvalidConfigurationExceptionTests.class);
        suite.addTestSuite(ProcessExceptionTests.class);
        suite.addTestSuite(PropertyNotFoundExceptionTests.class);
        suite.addTestSuite(PropertyTypeMismatchExceptionTests.class);

        return suite;
    }

}
