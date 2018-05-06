/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.util.memoryusage;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests for ConfigurationException. Simply applies the GenericExceptionTest.
 *
 * @author TexWiller
 * @version 2.0
 */
public class ConfigurationExceptionTest extends GenericExceptionTest {

    /**
     * Standard TestCase constructor: creates a new ConfigurationExceptionTest object.
     *
     * @param testName The name to be given to this TestCase.
     */
    public ConfigurationExceptionTest(String testName) {
        super(ConfigurationException.class, testName);
    }

    /**
     * Static method expected by JUnit test runners. Returns a Test containing all
     * the testXXX methods in this class.
     *
     * @return A new Test, containing all the testXXX methods in this class.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(ConfigurationExceptionTest.class);
        return suite;
    }
}
