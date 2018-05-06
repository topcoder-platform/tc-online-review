/*
 * Copyright (C) 2003, 2007 TopCoder Inc., All Rights Reserved.
 *
 * TCS Project JNDI Context Utility 2.0 (Unit Tests)
 *
 * @ UnitTests.java
 */
package com.topcoder.naming.jndiutility;

import com.topcoder.naming.jndiutility.configstrategy.ConfigurationManagerConfigurationStrategyTest;
import com.topcoder.naming.jndiutility.configstrategy.XmlFileConfigurationStrategyTest;
import com.topcoder.naming.jndiutility.functionaltests.CreateSubcontextsTestCase;
import com.topcoder.naming.jndiutility.functionaltests.DumpContextTestCase;
import com.topcoder.naming.jndiutility.functionaltests.GetResourcesTestCase;
import com.topcoder.naming.jndiutility.functionaltests.ManipulateWithNamesTestCase;
import com.topcoder.naming.jndiutility.functionaltests.UsePredefinedContextsTestCase;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * <p>This test case aggregates all Unit test cases.</p>
 *
 * @author preben
 * @author Charizard
 * @version 2.0
 *
 * @since 1.0
 */
public class UnitTests extends TestCase {
    /**
     * This aggregates all the Units Test.
     *
     * @return A new TestSuite
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();
        // Unit tests in version 1.0
        suite.addTestSuite(JNDIUtilsMainMethodTestCase.class);
        suite.addTestSuite(JNDIUtilsExceptionsTestCase.class);
        suite.addTestSuite(GetObjectConnectionQueueTopicTestCase.class);
        suite.addTestSuite(CreateContextTestCase.class);
        suite.addTestSuite(ContextRendererTestCase.class);
        suite.addTestSuite(DumpContextTestCase.class);
        suite.addTestSuite(ContextRendererExceptionTestCase.class);

        // Functional tests from version 1.0
        suite.addTest(UsePredefinedContextsTestCase.suite());
        suite.addTest(CreateSubcontextsTestCase.suite());
        suite.addTest(GetResourcesTestCase.suite());
        suite.addTest(ManipulateWithNamesTestCase.suite());
        suite.addTest(DumpContextTestCase.suite());

        // New test cases in version 2.0
        suite.addTestSuite(ConfigurationExceptionTest.class);
        suite.addTestSuite(HelperTest.class);
        suite.addTestSuite(ConfigurationManagerConfigurationStrategyTest.class);
        suite.addTestSuite(XmlFileConfigurationStrategyTest.class);
        suite.addTestSuite(JNDIUtilTest.class);
        suite.addTestSuite(JNDIUtilsTest.class);

        // Demo
        suite.addTestSuite(Demo.class);

        return suite;
    }
}
