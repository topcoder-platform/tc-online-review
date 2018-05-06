/*
 * Copyright (C) 2003, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.config;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * This test case aggregates all unit test cases.
 * </p>
 *
 * @author sparemax
 * @version 2.2
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

        suite.addTest(new TestSuite(Demo.class));
        suite.addTest(new TestSuite(HelperTestCase.class));

        suite.addTest(new TestSuite(DefaultConfigManagerTestCase.class));
        suite.addTest(new TestSuite(ConfigManagerFixTestCase.class));
        suite.addTest(new TestSuite(ConfigManagerJarTestCase.class));
        suite.addTest(new TestSuite(ConfigManagerTestCase.class));
        suite.addTest(new TestSuite(ConfigPropertiesTestCase.class));
        suite.addTest(new TestSuite(PropConfigPropertiesTestCase.class));
        suite.addTest(new TestSuite(XMLConfigPropertiesTestCase.class));
        suite.addTest(new TestSuite(XMLConfigPropertiesMultipleTestCase.class));
        suite.addTest(new TestSuite(PluggableConfigPropertiesTestCase.class));
        suite.addTest(new TestSuite(PropertyTestCase.class));
        suite.addTest(new TestSuite(NamespaceTestCase.class));
        suite.addTest(new TestSuite(ExceptionTestCase.class));

        suite.addTest(new TestSuite(EscapeEnhancementTest.class));

        return suite;
    }

}
