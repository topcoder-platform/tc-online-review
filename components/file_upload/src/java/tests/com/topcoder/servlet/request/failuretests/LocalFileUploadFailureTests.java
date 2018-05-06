/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.servlet.request.failuretests;

import java.util.Iterator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.servlet.request.ConfigurationException;
import com.topcoder.servlet.request.FileDoesNotExistException;
import com.topcoder.servlet.request.HttpRequestParser;
import com.topcoder.servlet.request.LocalFileUpload;
import com.topcoder.util.config.ConfigManager;

/**
 * The class <code>LocalFileUploadFailureTests</code> contains tests for the class
 * {@link <code>LocalFileUpload</code>}.
 * @author FireIce
 * @version 2.0
 */
public class LocalFileUploadFailureTests extends TestCase {

    /**
     * Represents the <code>LocalFileUpload</code> instance used in tests.
     */
    private LocalFileUpload localFileUpload;

    /**
     * Run the public LocalFileUpload(String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testOneArgLocalFileUploadIllegalArgumentExceptionNullNamespace() throws Exception {
        try {
            new LocalFileUpload(null);
            fail("expect throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Run the public LocalFileUpload(String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testOneArgLocalFileUploadIllegalArgumentExceptionEmptyNamespace() throws Exception {
        try {
            new LocalFileUpload("");
            fail("expect throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // good
        }

        try {
            new LocalFileUpload(" ");
            fail("expect throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Run the public LocalFileUpload(String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testOneArgLocalFileUploadConfigurationExceptionSingleFileLimitPropertyEmpty() throws Exception {
        try {
            new LocalFileUpload("com.topcoder.servlet.request.failuretests.SingleFileLimitPropertyEmpty");
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * Run the public LocalFileUpload(String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testOneArgLocalFileUploadConfigurationExceptionSingleFileLimitPropertyNotNumber() throws Exception {
        try {
            new LocalFileUpload("com.topcoder.servlet.request.failuretests.SingleFileLimitPropertyNotNumber");
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * Run the public LocalFileUpload(String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testOneArgLocalFileUploadConfigurationExceptionSingleFileLimitPropertyInvalidLongFormat()
        throws Exception {
        try {
            new LocalFileUpload("com.topcoder.servlet.request.failuretests.SingleFileLimitPropertyInvalidLongFormat");
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * Run the public LocalFileUpload(String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testOneArgLocalFileUploadConfigurationExceptionSingleFileLimitPropertyTooBig() throws Exception {
        try {
            new LocalFileUpload("com.topcoder.servlet.request.failuretests.SingleFileLimitPropertyTooBig");
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * Run the public LocalFileUpload(String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testOneArgLocalFileUploadConfigurationExceptionSingleFileLimitPropertyTooSmall() throws Exception {
        try {
            new LocalFileUpload("com.topcoder.servlet.request.failuretests.SingleFileLimitPropertyTooSmall");
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * Run the public LocalFileUpload(String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testOneArgLocalFileUploadConfigurationExceptionTotalFileLimitPropertyEmpty() throws Exception {
        try {
            new LocalFileUpload("com.topcoder.servlet.request.failuretests.TotalFileLimitPropertyEmpty");
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * Run the public LocalFileUpload(String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testOneArgLocalFileUploadConfigurationExceptionTotalFileLimitPropertyNotNumber() throws Exception {
        try {
            new LocalFileUpload("com.topcoder.servlet.request.failuretests.TotalFileLimitPropertyNotNumber");
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * Run the public LocalFileUpload(String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testOneArgLocalFileUploadConfigurationExceptionTotalFileLimitPropertyInvalidLongFormat()
        throws Exception {
        try {
            new LocalFileUpload("com.topcoder.servlet.request.failuretests.TotalFileLimitPropertyInvalidLongFormat");
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * Run the public LocalFileUpload(String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testOneArgLocalFileUploadConfigurationExceptionTotalFileLimitPropertyTooBig() throws Exception {
        try {
            new LocalFileUpload("com.topcoder.servlet.request.failuretests.TotalFileLimitPropertyTooBig");
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * Run the public LocalFileUpload(String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testOneArgLocalFileUploadConfigurationExceptionTotalFileLimitPropertyTooSmall() throws Exception {
        try {
            new LocalFileUpload("com.topcoder.servlet.request.failuretests.TotalFileLimitPropertyTooSmall");
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * Run the LocalFileUpload(String) constructor test.
     * @throws Exception
     */
    public void testOneArgLocalFileUploadConfigurationExceptionAllowedDirsPropertyMissing() throws Exception {
        try {
            new LocalFileUpload("com.topcoder.servlet.request.failuretests.AllowedDirsPropertyMissing");
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * Run the LocalFileUpload(String) constructor test.
     * @throws Exception
     */
    public void testOneArgLocalFileUploadConfigurationExceptionAllowedDirsPropertyContiansEmptyValue() throws Exception {
        try {
            new LocalFileUpload("com.topcoder.servlet.request.failuretests.AllowedDirsPropertyContiansEmptyValue");
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * Run the LocalFileUpload(String) constructor test.
     * @throws Exception
     */
    public void testOneArgLocalFileUploadConfigurationExceptionDefaultDirPropertyMissing() throws Exception {
        try {
            new LocalFileUpload("com.topcoder.servlet.request.failuretests.DefaultDirPropertyMissing");
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * Run the LocalFileUpload(String) constructor test.
     * @throws Exception
     */
    public void testOneArgLocalFileUploadConfigurationExceptionDefaultDirPropertyEmpty() throws Exception {
        try {
            new LocalFileUpload("com.topcoder.servlet.request.failuretests.DefaultDirPropertyEmpty");
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * Run the LocalFileUpload(String) constructor test.
     * @throws Exception
     */
    public void testOneArgLocalFileUploadConfigurationExceptionOverwritePropertyMissing() throws Exception {
        try {
            new LocalFileUpload("com.topcoder.servlet.request.failuretests.OverwritePropertyMissing");
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * Run the LocalFileUpload(String) constructor test.
     * @throws Exception
     */
    public void testOneArgLocalFileUploadConfigurationExceptionOverwritePropertyEmpty() throws Exception {
        try {
            new LocalFileUpload("com.topcoder.servlet.request.failuretests.OverwritePropertyEmpty");
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * Run the public LocalFileUpload(String, String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testTwoArgsLocalFileUploadIllegalArgumentExceptionNullNamespace() throws Exception {
        try {
            new LocalFileUpload(null, "test_files/");
            fail("expect throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Run the public LocalFileUpload(String, String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testTwoArgsLocalFileUploadIllegalArgumentExceptionEmptyNamespace() throws Exception {
        try {
            new LocalFileUpload("", "test_files/");
            fail("expect throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // good
        }

        try {
            new LocalFileUpload(" ", "test_files/");
            fail("expect throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Run the public LocalFileUpload(String, String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testTwoArgsLocalFileUploadIllegalArgumentExceptionNullDefaultDir() throws Exception {
        try {
            new LocalFileUpload("com.topcoder.servlet.request.failuretests.valid", null);
            fail("expect throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Run the public LocalFileUpload(String, String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testTwoArgsLocalFileUploadIllegalArgumentExceptionEmptyDefaultDir() throws Exception {
        try {
            new LocalFileUpload("com.topcoder.servlet.request.failuretests.valid", "");
            fail("expect throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // good
        }

        try {
            new LocalFileUpload("com.topcoder.servlet.request.failuretests.valid", " ");
            fail("expect throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Run the public LocalFileUpload(String, String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testTwoArgsLocalFileUploadConfigurationExceptionSingleFileLimitPropertyEmpty() throws Exception {
        try {
            new LocalFileUpload("com.topcoder.servlet.request.failuretests.SingleFileLimitPropertyEmpty", "test_files/");
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * Run the public LocalFileUpload(String, String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testTwoArgsLocalFileUploadConfigurationExceptionSingleFileLimitPropertyNotNumber() throws Exception {
        try {
            new LocalFileUpload("com.topcoder.servlet.request.failuretests.SingleFileLimitPropertyNotNumber",
                "test_files/");
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * Run the public LocalFileUpload(String, String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testTwoArgsLocalFileUploadConfigurationExceptionSingleFileLimitPropertyInvalidLongFormat()
        throws Exception {
        try {
            new LocalFileUpload("com.topcoder.servlet.request.failuretests.SingleFileLimitPropertyInvalidLongFormat",
                "test_files/");
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * Run the public LocalFileUpload(String, String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testTwoArgsLocalFileUploadConfigurationExceptionSingleFileLimitPropertyTooBig() throws Exception {
        try {
            new LocalFileUpload("com.topcoder.servlet.request.failuretests.SingleFileLimitPropertyTooBig",
                "test_files/");
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * Run the public LocalFileUpload(String, String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testTwoArgsLocalFileUploadConfigurationExceptionSingleFileLimitPropertyTooSmall() throws Exception {
        try {
            new LocalFileUpload("com.topcoder.servlet.request.failuretests.SingleFileLimitPropertyTooSmall",
                "test_files/");
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * Run the public LocalFileUpload(String, String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testTwoArgsLocalFileUploadConfigurationExceptionTotalFileLimitPropertyEmpty() throws Exception {
        try {
            new LocalFileUpload("com.topcoder.servlet.request.failuretests.TotalFileLimitPropertyEmpty", "test_files/");
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * Run the public LocalFileUpload(String, String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testTwoArgsLocalFileUploadConfigurationExceptionTotalFileLimitPropertyNotNumber() throws Exception {
        try {
            new LocalFileUpload("com.topcoder.servlet.request.failuretests.TotalFileLimitPropertyNotNumber",
                "test_files/");
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * Run the public LocalFileUpload(String, String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testTwoArgsLocalFileUploadConfigurationExceptionTotalFileLimitPropertyInvalidLongFormat()
        throws Exception {
        try {
            new LocalFileUpload("com.topcoder.servlet.request.failuretests.TotalFileLimitPropertyInvalidLongFormat",
                "test_files/");
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * Run the public LocalFileUpload(String, String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testTwoArgsLocalFileUploadConfigurationExceptionTotalFileLimitPropertyTooBig() throws Exception {
        try {
            new LocalFileUpload("com.topcoder.servlet.request.failuretests.TotalFileLimitPropertyTooBig", "test_files/");
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * Run the public LocalFileUpload(String, String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testTwoArgsLocalFileUploadConfigurationExceptionTotalFileLimitPropertyTooSmall() throws Exception {
        try {
            new LocalFileUpload("com.topcoder.servlet.request.failuretests.TotalFileLimitPropertyTooSmall",
                "test_files/");
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * Run the LocalFileUpload(String, String) constructor test.
     * @throws Exception
     */
    public void testTwoArgsLocalFileUploadConfigurationExceptionAllowedDirsPropertyMissing() throws Exception {
        try {
            new LocalFileUpload("com.topcoder.servlet.request.failuretests.AllowedDirsPropertyMissing", "test_files/");
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * Run the LocalFileUpload(String, String) constructor test.
     * @throws Exception
     */
    public void testTwoArgsLocalFileUploadConfigurationExceptionAllowedDirsPropertyContiansEmptyValue()
        throws Exception {
        try {
            new LocalFileUpload("com.topcoder.servlet.request.failuretests.AllowedDirsPropertyContiansEmptyValue",
                "test_files/");
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * Run the LocalFileUpload(String, String) constructor test.
     * @throws Exception
     */
    public void testTwoArgsLocalFileUploadConfigurationExceptionDefaultDirPropertyMissing() throws Exception {
        new LocalFileUpload("com.topcoder.servlet.request.failuretests.DefaultDirPropertyMissing", "test_files/");
    }

    /**
     * Run the LocalFileUpload(String, String) constructor test.
     * @throws Exception
     */
    public void testTwoArgsLocalFileUploadConfigurationExceptionDefaultDirPropertyEmpty() throws Exception {
        new LocalFileUpload("com.topcoder.servlet.request.failuretests.DefaultDirPropertyEmpty", "test_files/");
    }

    /**
     * Run the LocalFileUpload(String, String) constructor test.
     * @throws Exception
     */
    public void testTwoArgsLocalFileUploadConfigurationExceptionOverwritePropertyMissing() throws Exception {
        try {
            new LocalFileUpload("com.topcoder.servlet.request.failuretests.OverwritePropertyMissing", "test_files/");
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * Run the LocalFileUpload(String, String) constructor test.
     * @throws Exception
     */
    public void testTwoArgsLocalFileUploadConfigurationExceptionOverwritePropertyEmpty() throws Exception {
        try {
            new LocalFileUpload("com.topcoder.servlet.request.failuretests.OverwritePropertyEmpty", "test_files/");
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    // TODO
    /**
     * Run the public LocalFileUpload(String, String, boolean) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testThreeArgsLocalFileUploadIllegalArgumentExceptionNullNamespace() throws Exception {
        try {
            new LocalFileUpload(null, "test_files/", true);
            fail("expect throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Run the public LocalFileUpload(String, String, boolean) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testThreeArgsLocalFileUploadIllegalArgumentExceptionEmptyNamespace() throws Exception {
        try {
            new LocalFileUpload("", "test_files/", true);
            fail("expect throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // good
        }

        try {
            new LocalFileUpload(" ", "test_files/", true);
            fail("expect throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Run the public LocalFileUpload(String, String, boolean) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testThreeArgsLocalFileUploadIllegalArgumentExceptionNullDefaultDir() throws Exception {
        try {
            new LocalFileUpload("com.topcoder.servlet.request.failuretests.valid", null, true);
            fail("expect throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Run the public LocalFileUpload(String, String, boolean) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testThreeArgsLocalFileUploadIllegalArgumentExceptionEmptyDefaultDir() throws Exception {
        try {
            new LocalFileUpload("com.topcoder.servlet.request.failuretests.valid", "", true);
            fail("expect throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // good
        }

        try {
            new LocalFileUpload("com.topcoder.servlet.request.failuretests.valid", " ", true);
            fail("expect throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Run the public LocalFileUpload(String, String, boolean) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testThreeArgsLocalFileUploadConfigurationExceptionSingleFileLimitPropertyEmpty() throws Exception {
        try {
            new LocalFileUpload("com.topcoder.servlet.request.failuretests.SingleFileLimitPropertyEmpty",
                "test_files/", true);
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * Run the public LocalFileUpload(String, String, boolean) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testThreeArgsLocalFileUploadConfigurationExceptionSingleFileLimitPropertyNotNumber() throws Exception {
        try {
            new LocalFileUpload("com.topcoder.servlet.request.failuretests.SingleFileLimitPropertyNotNumber",
                "test_files/", true);
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * Run the public LocalFileUpload(String, String, boolean) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testThreeArgsLocalFileUploadConfigurationExceptionSingleFileLimitPropertyInvalidLongFormat()
        throws Exception {
        try {
            new LocalFileUpload("com.topcoder.servlet.request.failuretests.SingleFileLimitPropertyInvalidLongFormat",
                "test_files/", true);
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * Run the public LocalFileUpload(String, String, boolean) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testThreeArgsLocalFileUploadConfigurationExceptionSingleFileLimitPropertyTooBig() throws Exception {
        try {
            new LocalFileUpload("com.topcoder.servlet.request.failuretests.SingleFileLimitPropertyTooBig",
                "test_files/", true);
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * Run the public LocalFileUpload(String, String, boolean) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testThreeArgsLocalFileUploadConfigurationExceptionSingleFileLimitPropertyTooSmall() throws Exception {
        try {
            new LocalFileUpload("com.topcoder.servlet.request.failuretests.SingleFileLimitPropertyTooSmall",
                "test_files/", true);
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * Run the public LocalFileUpload(String, String, boolean) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testThreeArgsLocalFileUploadConfigurationExceptionTotalFileLimitPropertyEmpty() throws Exception {
        try {
            new LocalFileUpload("com.topcoder.servlet.request.failuretests.TotalFileLimitPropertyEmpty", "test_files/",
                true);
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * Run the public LocalFileUpload(String, String, boolean) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testThreeArgsLocalFileUploadConfigurationExceptionTotalFileLimitPropertyNotNumber() throws Exception {
        try {
            new LocalFileUpload("com.topcoder.servlet.request.failuretests.TotalFileLimitPropertyNotNumber",
                "test_files/", true);
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * Run the public LocalFileUpload(String, String, boolean) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testThreeArgsLocalFileUploadConfigurationExceptionTotalFileLimitPropertyInvalidLongFormat()
        throws Exception {
        try {
            new LocalFileUpload("com.topcoder.servlet.request.failuretests.TotalFileLimitPropertyInvalidLongFormat",
                "test_files/", true);
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * Run the public LocalFileUpload(String, String, boolean) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testThreeArgsLocalFileUploadConfigurationExceptionTotalFileLimitPropertyTooBig() throws Exception {
        try {
            new LocalFileUpload("com.topcoder.servlet.request.failuretests.TotalFileLimitPropertyTooBig",
                "test_files/", true);
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * Run the public LocalFileUpload(String, String, boolean) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testThreeArgsLocalFileUploadConfigurationExceptionTotalFileLimitPropertyTooSmall() throws Exception {
        try {
            new LocalFileUpload("com.topcoder.servlet.request.failuretests.TotalFileLimitPropertyTooSmall",
                "test_files/", true);
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * Run the LocalFileUpload(String, String, boolean) constructor test.
     * @throws Exception
     */
    public void testThreeArgsLocalFileUploadConfigurationExceptionAllowedDirsPropertyMissing() throws Exception {
        try {
            new LocalFileUpload("com.topcoder.servlet.request.failuretests.AllowedDirsPropertyMissing", "test_files/",
                true);
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * Run the LocalFileUpload(String, String, boolean) constructor test.
     * @throws Exception
     */
    public void testThreeArgsLocalFileUploadConfigurationExceptionAllowedDirsPropertyContiansEmptyValue()
        throws Exception {
        try {
            new LocalFileUpload("com.topcoder.servlet.request.failuretests.AllowedDirsPropertyContiansEmptyValue",
                "test_files/", true);
            fail("expect throw ConfigurationException.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * Run the LocalFileUpload(String, String, boolean) constructor test.
     * @throws Exception
     */
    public void testThreeArgsLocalFileUploadConfigurationExceptionDefaultDirPropertyMissing() throws Exception {
        new LocalFileUpload("com.topcoder.servlet.request.failuretests.DefaultDirPropertyMissing", "test_files/", true);
    }

    /**
     * Run the LocalFileUpload(String, String, boolean) constructor test.
     * @throws Exception
     */
    public void testThreeArgsLocalFileUploadConfigurationExceptionDefaultDirPropertyEmpty() throws Exception {
        new LocalFileUpload("com.topcoder.servlet.request.failuretests.DefaultDirPropertyEmpty", "test_files/", true);
    }

    /**
     * Run the LocalFileUpload(String, String, boolean) constructor test.
     * @throws Exception
     */
    public void testThreeArgsLocalFileUploadConfigurationExceptionOverwritePropertyMissing() throws Exception {
        new LocalFileUpload("com.topcoder.servlet.request.failuretests.OverwritePropertyMissing", "test_files/", true);
    }

    /**
     * Run the LocalFileUpload(String, String, boolean) constructor test.
     * @throws Exception
     */
    public void testThreeArgsLocalFileUploadConfigurationExceptionOverwritePropertyEmpty() throws Exception {
        new LocalFileUpload("com.topcoder.servlet.request.failuretests.OverwritePropertyEmpty", "test_files/", true);
    }

    /**
     * Run the FileUploadResult uploadFiles(ServletRequest, RequestParser) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testUploadFilesIllegalArgumentExcdeptionNullServletRequest() throws Exception {
        try {
            localFileUpload.uploadFiles(null, new HttpRequestParser());
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
            localFileUpload.uploadFiles(new MockServletRequest(), null);
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
    public void testGetUploadedFileIllegalArgumentExceptionNullFileId() throws Exception {
        try {
            localFileUpload.getUploadedFile(null, true);
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
    public void testGetUploadedFileIllegalArgumentExceptionEmptyFileId() throws Exception {
        try {
            localFileUpload.getUploadedFile("", true);
            fail("expect throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // good
        }

        try {
            localFileUpload.getUploadedFile(" ", true);
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
    public void testGetUploadedFileIllegalArgumentExceptionFileNotFound() throws Exception {
        try {
            localFileUpload.getUploadedFile("NotFound", true);
            fail("expect throw FileDoesNotExistException.");
        } catch (FileDoesNotExistException e) {
            // good
        }
    }

    /**
     * Run the UploadedFile removeUploadedFile(String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testRemoveUploadedFileIllegalArgumentExceptionNullFileId() throws Exception {
        try {
            localFileUpload.removeUploadedFile(null);
            fail("expect throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Run the UploadedFile removeUploadedFile(String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testRemoveUploadedFileIllegalArgumentExceptionEmptyFileId() throws Exception {
        try {
            localFileUpload.removeUploadedFile("");
            fail("expect throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // good
        }

        try {
            localFileUpload.removeUploadedFile(" ");
            fail("expect throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Run the UploadedFile removeUploadedFile(String) method test.
     * @throws Exception
     *             to JUnit
     */
    public void testRemoveUploadedFileIllegalArgumentExceptionFileNotFound() throws Exception {
        try {
            localFileUpload.removeUploadedFile("NotFound");
            fail("expect throw FileDoesNotExistException.");
        } catch (FileDoesNotExistException e) {
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
        ConfigManager.getInstance().add("test_files/failuretests/LocalFileUpload.xml");
        localFileUpload = new LocalFileUpload("com.topcoder.servlet.request.failuretests.valid");
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
        return new TestSuite(LocalFileUploadFailureTests.class);
    }
}
