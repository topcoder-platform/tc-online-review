/**
 *
 * Copyright (c) 2006, TopCoder, Inc. All rights reserved
 */
package com.topcoder.configuration.accuracytests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * This test case aggregates all Accuracy test cases.
 * </p>
 *
 * @author TopCoder
 * @version 1.0
 */
public class AccuracyTests extends TestCase {

    public static Test suite() {
        final TestSuite suite = new TestSuite();

        suite.addTestSuite(ConfigurationExceptionAccuracyTests.class);
        suite.addTestSuite(ConfigurationAccessExceptionAccuracyTests.class);
        suite.addTestSuite(InvalidConfigurationExceptionAccuracyTests.class);
        suite.addTestSuite(ProcessExceptionAccuracyTests.class);
        suite.addTestSuite(PropertyTypeMismatchExceptionAccuracyTests.class);
        suite.addTestSuite(PropertyNotFoundExceptionAccuracyTests.class);

        suite.addTestSuite(BaseConfigurationObjectAccuracyTests.class);
        suite.addTestSuite(TemplateConfigurationObjectAccuracyTests.class);

        suite.addTestSuite(DefaultConfigurationObjectAccuracyTests.class);

        suite.addTestSuite(SynchronizedConfigurationObjectAccuracyTests.class);
        return suite;
    }
}