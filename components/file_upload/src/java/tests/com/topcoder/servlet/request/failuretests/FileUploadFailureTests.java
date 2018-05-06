/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.servlet.request.failuretests;

import java.util.Iterator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.servlet.request.ConfigurationException;
import com.topcoder.util.config.ConfigManager;

/**
 * The class <code>FileUploadFailureTests</code> contains tests for the class {@link <code>FileUpload</code>}.
 * @author FireIce
 * @version 2.0
 */
public class FileUploadFailureTests extends TestCase {

    /**
     * Run the public FileUpload(String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testFileUploadIllegalArgumentExceptionNullNamespace() throws Exception {
        try {
            new DefaultFileUpload(null);
            fail("expect throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Run the public FileUpload(String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testFileUploadIllegalArgumentExceptionEmptyNamespace() throws Exception {
        try {
            new DefaultFileUpload("");
            fail("expect throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // good
        }

        try {
            new DefaultFileUpload(" ");
            fail("expect throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Run the public FileUpload(String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testFileUploadConfigurationExceptionSingleFileLimitPropertyEmpty() throws Exception {
        try {
            new DefaultFileUpload("com.topcoder.servlet.request.failuretests.SingleFileLimitPropertyEmpty");
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * Run the public FileUpload(String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testFileUploadConfigurationExceptionSingleFileLimitPropertyNotNumber() throws Exception {
        try {
            new DefaultFileUpload("com.topcoder.servlet.request.failuretests.SingleFileLimitPropertyNotNumber");
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * Run the public FileUpload(String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testFileUploadConfigurationExceptionSingleFileLimitPropertyInvalidLongFormat() throws Exception {
        try {
            new DefaultFileUpload("com.topcoder.servlet.request.failuretests.SingleFileLimitPropertyInvalidLongFormat");
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * Run the public FileUpload(String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testFileUploadConfigurationExceptionSingleFileLimitPropertyTooBig() throws Exception {
        try {
            new DefaultFileUpload("com.topcoder.servlet.request.failuretests.SingleFileLimitPropertyTooBig");
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * Run the public FileUpload(String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testFileUploadConfigurationExceptionSingleFileLimitPropertyTooSmall() throws Exception {
        try {
            new DefaultFileUpload("com.topcoder.servlet.request.failuretests.SingleFileLimitPropertyTooSmall");
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * Run the public FileUpload(String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testFileUploadConfigurationExceptionTotalFileLimitPropertyEmpty() throws Exception {
        try {
            new DefaultFileUpload("com.topcoder.servlet.request.failuretests.TotalFileLimitPropertyEmpty");
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * Run the public FileUpload(String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testFileUploadConfigurationExceptionTotalFileLimitPropertyNotNumber() throws Exception {
        try {
            new DefaultFileUpload("com.topcoder.servlet.request.failuretests.TotalFileLimitPropertyNotNumber");
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * Run the public FileUpload(String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testFileUploadConfigurationExceptionTotalFileLimitPropertyInvalidLongFormat() throws Exception {
        try {
            new DefaultFileUpload("com.topcoder.servlet.request.failuretests.TotalFileLimitPropertyInvalidLongFormat");
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * Run the public FileUpload(String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testFileUploadConfigurationExceptionTotalFileLimitPropertyTooBig() throws Exception {
        try {
            new DefaultFileUpload("com.topcoder.servlet.request.failuretests.TotalFileLimitPropertyTooBig");
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * Run the public FileUpload(String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testFileUploadConfigurationExceptionTotalFileLimitPropertyTooSmall() throws Exception {
        try {
            new DefaultFileUpload("com.topcoder.servlet.request.failuretests.TotalFileLimitPropertyTooSmall");
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * Perform pre-test initialization.
     * @throws Exception
     *             if the initialization fails for some reason
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();

        ConfigManager.getInstance().add("test_files/failuretests/FileUploadConfiguration.xml");
    }

    /**
     * Perform post-test clean-up.
     * @throws Exception
     *             if the clean-up fails for some reason
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        ConfigManager configManager = ConfigManager.getInstance();
        for (Iterator iter = configManager.getAllNamespaces(); iter.hasNext();) {
            configManager.removeNamespace((String) iter.next());
        }
    }

    /**
     * Aggregates all tests in this class.
     * @return test suite aggregating all tests.
     */
    public static Test suite() {
        return new TestSuite(FileUploadFailureTests.class);
    }
}
