/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.servlet.request.failuretests;

import java.util.Iterator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.servlet.request.ConfigurationException;
import com.topcoder.servlet.request.HttpRequestParser;
import com.topcoder.servlet.request.MemoryFileUpload;
import com.topcoder.util.config.ConfigManager;

/**
 * The class <code>MemoryFileUploadFailureTests</code> contains tests for the class
 * {@link <code>MemoryFileUpload</code>}.
 * @author FireIce
 * @version 2.0
 */
public class MemoryFileUploadFailureTests extends TestCase {

    /**
     * Represents the <code>MemoryFileUpload</code> instance.
     */
    private MemoryFileUpload memoryFileUpload;

    /**
     * Run the public MemoryFileUpload(String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testMemoryFileUploadIllegalArgumentExceptionNullNamespace() throws Exception {
        try {
            new MemoryFileUpload(null);
            fail("expect throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Run the public MemoryFileUpload(String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testMemoryFileUploadIllegalArgumentExceptionEmptyNamespace() throws Exception {
        try {
            new MemoryFileUpload("");
            fail("expect throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // good
        }

        try {
            new MemoryFileUpload(" ");
            fail("expect throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Run the public MemoryFileUpload(String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testMemoryFileUploadConfigurationExceptionSingleFileLimitPropertyEmpty() throws Exception {
        try {
            new MemoryFileUpload("com.topcoder.servlet.request.failuretests.SingleFileLimitPropertyEmpty");
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * Run the public MemoryFileUpload(String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testMemoryFileUploadConfigurationExceptionSingleFileLimitPropertyNotNumber() throws Exception {
        try {
            new MemoryFileUpload("com.topcoder.servlet.request.failuretests.SingleFileLimitPropertyNotNumber");
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * Run the public MemoryFileUpload(String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testMemoryFileUploadConfigurationExceptionSingleFileLimitPropertyInvalidLongFormat() throws Exception {
        try {
            new MemoryFileUpload("com.topcoder.servlet.request.failuretests.SingleFileLimitPropertyInvalidLongFormat");
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * Run the public MemoryFileUpload(String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testMemoryFileUploadConfigurationExceptionSingleFileLimitPropertyTooBig() throws Exception {
        try {
            new MemoryFileUpload("com.topcoder.servlet.request.failuretests.SingleFileLimitPropertyTooBig");
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * Run the public MemoryFileUpload(String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testMemoryFileUploadConfigurationExceptionSingleFileLimitPropertyTooSmall() throws Exception {
        try {
            new MemoryFileUpload("com.topcoder.servlet.request.failuretests.SingleFileLimitPropertyTooSmall");
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * Run the public MemoryFileUpload(String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testMemoryFileUploadConfigurationExceptionTotalFileLimitPropertyEmpty() throws Exception {
        try {
            new MemoryFileUpload("com.topcoder.servlet.request.failuretests.TotalFileLimitPropertyEmpty");
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * Run the public MemoryFileUpload(String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testMemoryFileUploadConfigurationExceptionTotalFileLimitPropertyNotNumber() throws Exception {
        try {
            new MemoryFileUpload("com.topcoder.servlet.request.failuretests.TotalFileLimitPropertyNotNumber");
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * Run the public MemoryFileUpload(String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testMemoryFileUploadConfigurationExceptionTotalFileLimitPropertyInvalidLongFormat() throws Exception {
        try {
            new MemoryFileUpload("com.topcoder.servlet.request.failuretests.TotalFileLimitPropertyInvalidLongFormat");
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * Run the public MemoryFileUpload(String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testMemoryFileUploadConfigurationExceptionTotalFileLimitPropertyTooBig() throws Exception {
        try {
            new MemoryFileUpload("com.topcoder.servlet.request.failuretests.TotalFileLimitPropertyTooBig");
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * Run the public MemoryFileUpload(String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testMemoryFileUploadConfigurationExceptionTotalFileLimitPropertyTooSmall() throws Exception {
        try {
            new MemoryFileUpload("com.topcoder.servlet.request.failuretests.TotalFileLimitPropertyTooSmall");
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * Run the FileUploadResult uploadFiles(ServletRequest, RequestParser) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testUploadFilesIllegalArgumentExcdeptionNullServletRequest() throws Exception {
        try {
            memoryFileUpload.uploadFiles(null, new HttpRequestParser());
            fail("expect throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Run the FileUploadResult uploadFiles(ServletRequest, RequestParser) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testUploadFilesIllegalArgumentExcdeptionNullRequestParser() throws Exception {
        try {
            memoryFileUpload.uploadFiles(new MockServletRequest(), null);
            fail("expect throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Run the UploadedFile getUploadedFile(String, boolean) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testGetUploadedFileUnsupportedOperationException() throws Exception {
        try {
            memoryFileUpload.getUploadedFile("test", true);
            fail("expect throw UnsupportedOperationException.");
        } catch (UnsupportedOperationException e) {
            // good
        }
    }

    /**
     * Run the UploadedFile removeUploadedFile(String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testRemoveUploadedFileUnsupportedOperationException() throws Exception {
        try {
            memoryFileUpload.removeUploadedFile("test");
            fail("expect throw UnsupportedOperationException.");
        } catch (UnsupportedOperationException e) {
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
        ConfigManager.getInstance().add("test_files/failuretests/MemoryFileUpload.xml");
        memoryFileUpload = new MemoryFileUpload("com.topcoder.servlet.request.failuretests.valid");
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
        return new TestSuite(MemoryFileUploadFailureTests.class);
    }
}
