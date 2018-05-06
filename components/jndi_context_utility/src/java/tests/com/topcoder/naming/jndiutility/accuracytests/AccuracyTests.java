/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.naming.jndiutility.accuracytests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>This test case aggregates all Accuracy test cases.</p>
 *
 * @author KKD
 * @version 2.0
 */
public class AccuracyTests extends TestCase {

    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTestSuite(ConfigurationManagerConfigurationStrategyAccuracyTest.class);
        suite.addTestSuite(XmlFileConfigurationStrategyAccuracyTest.class);
        suite.addTestSuite(JNDIUtilAccuracyTest.class);
        return suite;
    }

}
