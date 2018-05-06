/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.config.accuracytests;

import com.topcoder.util.config.PluggableConfigPropertiesAccuracyTests;
import com.topcoder.util.config.PropConfigPropertiesAccuracyTests;
import com.topcoder.util.config.XMLConfigPropertiesAccuracyTests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>This test case aggregates all accuracy test cases.</p>
 *
 * @author TCSDEVELOPER
 * @version 2.2
 */
public class AccuracyTests extends TestCase {
    /**
     * <p>
     * All accuracy test cases.
     * </p>
     *
     * @return The test suite.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTest(new TestSuite(AccTestConfigManager.class));
        suite.addTest(new TestSuite(EscapeTest.class));
        suite.addTest(ConfigManagerAccuracyTests.suite());
        suite.addTest(PluggableConfigPropertiesAccuracyTests.suite());
        suite.addTest(PropConfigPropertiesAccuracyTests.suite());
        suite.addTest(XMLConfigPropertiesAccuracyTests.suite());

        return suite;
    }

}
