/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.security.authenticationfactory;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.util.errorhandling.BaseException;
/**
 * <p>
 * This class contains the unit tests for ConfigurationException.</p>
 *
 * @author TCSDEVELOPER
 * @version 2.0
 */
public class ConfigurationExceptionTest extends TestCase {

    /**
     *<p>
     * Creates a test suite of the tests contained in this class.</p>
     *
     * @return a test suite of the tests contained in this class.
     */
    public static Test suite() {
        return new TestSuite(ConfigurationExceptionTest.class);
    } // end suite()

    /**
     *<p>
     * Tests accuracy of the method ConfigurationException(String message).</p>
     *
     */
    public void testConfigurationExceptionTwoAccuracy() {
        ConfigurationException ce = new ConfigurationException("ce");
        assertTrue(ce instanceof BaseException);
        assertTrue(ce.getMessage().indexOf("ce") != -1);
    } // end testConfigurationExceptionTwoAccuracy()

    /**
     *<p>
     * Tests accuracy of the method
     * ConfigurationException(String message, Throwable cause).</p>
     *
     */
    public void testConfigurationExceptionForAccuracy() {
        ConfigurationException ce =
            new ConfigurationException("ce", new IllegalArgumentException());

        assertTrue(ce.getMessage().indexOf("ce") != -1);
        assertTrue(ce.getCause() instanceof IllegalArgumentException);
    } // end testConfigurationExceptionForAccuracy()
} // end ConfigurationExceptionTest
