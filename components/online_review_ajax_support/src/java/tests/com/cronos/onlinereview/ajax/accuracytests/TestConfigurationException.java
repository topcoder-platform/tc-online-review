/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.ajax.accuracytests;

import com.cronos.onlinereview.ajax.ConfigurationException;

import com.topcoder.util.errorhandling.BaseException;

import junit.framework.TestCase;


/**
 * Tests for ConfigurationException class.
 *
 * @author assistant
 * @version 1.0
 */
public class TestConfigurationException extends TestCase {
    /**
     * Tests ConfigurationException() method with accuracy state.
     */
    public void testConfigurationException1Accuracy() {
        ConfigurationException ce = new ConfigurationException();
        assertNotNull("creting ConfigurationException fails.", ce);
        assertTrue(ce instanceof BaseException);
    }

    /**
     * Tests ConfigurationException(String message) method with accuracy state.
     */
    public void testConfigurationException2Accuracy() {
        ConfigurationException ce = new ConfigurationException("msg");
        assertNotNull("creting ConfigurationException fails.", ce);
        assertTrue("creting ConfigurationException fails.", ce instanceof BaseException);
        assertEquals("creting ConfigurationException fails.", "msg", ce.getMessage());
    }

    /**
     * Tests ConfigurationException(Throwable cause) method with accuracy state.
     */
    public void testConfigurationException3Accuracy() {
        Exception e = new IllegalArgumentException("msg");
        ConfigurationException ce = new ConfigurationException(e);
        assertNotNull("creting ConfigurationException fails.", ce);
        assertTrue("creting ConfigurationException fails.", ce instanceof BaseException);
        assertEquals("creting ConfigurationException fails.", e, ce.getCause());
    }

    /**
     * Tests ConfigurationException(String message, Throwable cause) method with accuracy state.
     */
    public void testConfigurationException4Accuracy() {
        Exception e = new IllegalArgumentException("msg2");
        ConfigurationException ce = new ConfigurationException("msg", e);
        assertNotNull("creting ConfigurationException fails.", ce);
        assertTrue("creting ConfigurationException fails.", ce instanceof BaseException);
        assertEquals("creting ConfigurationException fails.", "msg", ce.getMessage());
        assertEquals("creting ConfigurationException fails.", e, ce.getCause());
    }
}
