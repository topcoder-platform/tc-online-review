/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.naming.jndiutility.failuretests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * This test case aggregates all Failure test cases.
 * </p>
 * 
 * @author kaqi072821
 * @version 1.0
 */
public class FailureTests extends TestCase {

    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTest(ContextConsoleRendererTest.suite());
        suite.addTest(ContextXMLRendererTest.suite());
        suite.addTest(JNDIUtilsTest.suite());
        suite.addTestSuite(ConfigurationManagerConfigurationStrategyFailureTest.class);
        suite.addTestSuite(XmlFileConfigurationStrategyTest.class);
        suite.addTestSuite(JNDIUtilFailureTest.class);
        return suite;
    }

}
