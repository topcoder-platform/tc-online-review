/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.servlet.request;

import junit.framework.TestCase;


/**
 * <p>
 * Tests functionality and error cases of <code>FileUpload</code> class.
 * </p>
 *
 * @author PE
 * @version 2.0
 */
public class FileUploadUnitTest extends TestCase {
    /** Represents the total file size limit for testing. */
    private static final long SINGLE_FILE_LIMIT = 100;

    /** Represents the total file size limit for testing. */
    private static final long TOTAL_FILE_LIMIT = 200;

    /** Represents the <code>FileUpload</code> instance used for testing. */
    private MockFileUpload fileUpload = null;

    /**
     * <p>
     * Sets up the test environment. The test instance is created. The configuration is loaded.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    protected void setUp() throws Exception {
        UnitTestHelper.clearConfig();
        UnitTestHelper.addConfig("FileUploadConfig.xml");
        fileUpload = new MockFileUpload("Valid");
    }

    /**
     * <p>
     * Tears down the test environment. The configuration is cleared.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    protected void tearDown() throws Exception {
        UnitTestHelper.clearConfig();
    }

    /**
     * <p>
     * Tests the constructor <code>FileUpload(String)</code> when the given namespace is null, IllegalArgumentException
     * is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testFileUpload_NullNamespace() throws Exception {
        try {
            new MockFileUpload(null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the constructor <code>FileUpload(String)</code> when the given namespace is empty,
     * IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testFileUpload_EmptyNamespace() throws Exception {
        try {
            new MockFileUpload(" ");
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the constructor <code>FileUpload(String)</code> when the given namespace does not exist,
     * ConfigurationException is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testFileUpload_NotExistNamespace() throws Exception {
        try {
            new MockFileUpload("NotExist");
            fail("ConfigurationException should be thrown.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the constructor <code>FileUpload(String)</code> when the single_file_limit property is missing, default
     * value will be loaded. No exception is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testFileUpload_SingleFileLimitPropertyMissing() throws Exception {
        fileUpload = new MockFileUpload("SingleFileLimitPropertyMissing");

        String value = UnitTestHelper.getParentClassPrivateField(fileUpload.getClass(), fileUpload, "singleFileLimit")
                                     .toString();
        assertEquals("The default value should be set.", (long) -1, Long.parseLong(value));
        value = UnitTestHelper.getParentClassPrivateField(fileUpload.getClass(),
                fileUpload, "totalFileLimit").toString();
        assertEquals("The totalFileLimit value should be set.", TOTAL_FILE_LIMIT, Long.parseLong(value));
    }

    /**
     * <p>
     * Tests the constructor <code>FileUpload(String)</code> when the single_file_limit property is empty,
     * ConfigurationException is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testFileUpload_SingleFileLimitPropertyEmpty() throws Exception {
        try {
            new MockFileUpload("SingleFileLimitPropertyEmpty");
            fail("ConfigurationException should be thrown.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the constructor <code>FileUpload(String)</code> when the single_file_limit property is invalid,
     * ConfigurationException is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testFileUpload_SingleFileLimitPropertyInvalid1() throws Exception {
        try {
            new MockFileUpload("SingleFileLimitPropertyInvalid1");
            fail("ConfigurationException should be thrown.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the constructor <code>FileUpload(String)</code> when the single_file_limit property is invalid,
     * ConfigurationException is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testFileUpload_SingleFileLimitPropertyInvalid2() throws Exception {
        try {
            new MockFileUpload("SingleFileLimitPropertyInvalid2");
            fail("ConfigurationException should be thrown.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the constructor <code>FileUpload(String)</code> when the single_file_limit property is invalid,
     * ConfigurationException is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testFileUpload_SingleFileLimitPropertyInvalid3() throws Exception {
        try {
            new MockFileUpload("SingleFileLimitPropertyInvalid3");
            fail("ConfigurationException should be thrown.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the constructor <code>FileUpload(String)</code> when the single_file_limit property is invalid,
     * ConfigurationException is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testFileUpload_SingleFileLimitPropertyInvalid4() throws Exception {
        try {
            new MockFileUpload("SingleFileLimitPropertyInvalid4");
            fail("ConfigurationException should be thrown.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the constructor <code>FileUpload(String)</code> when the total_file_limit property is missing, default
     * value will be loaded. No exception is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testFileUpload_TotalFileLimitPropertyMissing() throws Exception {
        fileUpload = new MockFileUpload("TotalFileLimitPropertyMissing");

        String value = UnitTestHelper.getParentClassPrivateField(fileUpload.getClass(), fileUpload, "totalFileLimit")
                                     .toString();
        assertEquals("The default value should be set.", (long) -1, Long.parseLong(value));
        value = UnitTestHelper.getParentClassPrivateField(fileUpload.getClass(), fileUpload, "singleFileLimit")
                              .toString();
        assertEquals("The singleFileLimit value should be set.", SINGLE_FILE_LIMIT, Long.parseLong(value));
    }

    /**
     * <p>
     * Tests the constructor <code>FileUpload(String)</code> when the total_file_limit property is empty,
     * ConfigurationException is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testFileUpload_TotalFileLimitPropertyEmpty() throws Exception {
        try {
            new MockFileUpload("TotalFileLimitPropertyEmpty");
            fail("ConfigurationException should be thrown.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the constructor <code>FileUpload(String)</code> when the total_file_limit property is invalid,
     * ConfigurationException is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testFileUpload_TotalFileLimitPropertyInvalid1() throws Exception {
        try {
            new MockFileUpload("TotalFileLimitPropertyInvalid1");
            fail("ConfigurationException should be thrown.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the constructor <code>FileUpload(String)</code> when the total_file_limit property is invalid,
     * ConfigurationException is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testFileUpload_TotalFileLimitPropertyInvalid2() throws Exception {
        try {
            new MockFileUpload("TotalFileLimitPropertyInvalid2");
            fail("ConfigurationException should be thrown.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the constructor <code>FileUpload(String)</code> when the total_file_limit property is invalid,
     * ConfigurationException is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testFileUpload_TotalFileLimitPropertyInvalid3() throws Exception {
        try {
            new MockFileUpload("TotalFileLimitPropertyInvalid3");
            fail("ConfigurationException should be thrown.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the constructor <code>FileUpload(String)</code> when the total_file_limit property is invalid,
     * ConfigurationException is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testFileUpload_TotalFileLimitPropertyInvalid4() throws Exception {
        try {
            new MockFileUpload("TotalFileLimitPropertyInvalid4");
            fail("ConfigurationException should be thrown.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the accuracy of constructor <code>FileUpload(String)</code>.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testFileUpload_Accuracy() throws Exception {
        assertEquals("singleFileLimit should be properly loaded.", "" + SINGLE_FILE_LIMIT,
            UnitTestHelper.getParentClassPrivateField(fileUpload.getClass(), fileUpload, "singleFileLimit").toString());
        assertEquals("totalFileLimit should be properly loaded.", "" + TOTAL_FILE_LIMIT,
            UnitTestHelper.getParentClassPrivateField(fileUpload.getClass(), fileUpload, "totalFileLimit").toString());
    }

    /**
     * <p>
     * Tests the method <code>getUniqueFileName(String)</code> when the given remoteFileName is null,
     * IllegalArgumentException is expected.
     * </p>
     */
    public void testGetUniqueFileName_NullRemoteFileName() {
        try {
            FileUpload.getUniqueFileName(null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the method <code>getUniqueFileName(String)</code> when the given remoteFileName is empty,
     * IllegalArgumentException is expected.
     * </p>
     */
    public void testGetUniqueFileName_EmptyRemoteFileName() {
        try {
            FileUpload.getUniqueFileName(" ");
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the accuracy of method <code>getUniqueFileName(String)</code>.
     * </p>
     */
    public void testGetUniqueFileName_Accuracy() {
        String remoteFileName = "remoteFileName";
        assertFalse("The unique file name should be generated.",
            remoteFileName.equals(FileUpload.getUniqueFileName(remoteFileName)));
    }

    /**
     * <p>
     * Tests the method <code>getRemoteFileName(String)</code> when the given uniqueFileName is null,
     * IllegalArgumentException is expected.
     * </p>
     */
    public void testGetRemoteFileName_NullUniqueFileName() {
        try {
            FileUpload.getRemoteFileName(null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the method <code>getRemoteFileName(String)</code> when the given uniqueFileName is empty,
     * IllegalArgumentException is expected.
     * </p>
     */
    public void testGetRemoteFileName_EmptyUniqueFileName() {
        try {
            FileUpload.getRemoteFileName(" ");
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the method <code>getRemoteFileName(String)</code> when the given uniqueFileName is invalid,
     * IllegalArgumentException is expected.
     * </p>
     */
    public void testGetRemoteFileName_InvalidUniqueFileName() {
        try {
            FileUpload.getRemoteFileName("Invalid");
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the accuracy of method <code>getRemoteFileName(String)</code>.
     * </p>
     */
    public void testGetRemoteFileName_Accuracy() {
        String remoteFileName = "remoteFileName";
        String uniqueFileName = "aaa_" + remoteFileName;
        assertEquals("The remote file name should be generated properly.", remoteFileName,
            FileUpload.getRemoteFileName(uniqueFileName));
    }

    /**
     * <p>
     * Tests the accuracy of method <code>getSingleFileLimit()</code>.
     * </p>
     */
    public void testGetSingleFileLimit_Accuracy() {
        assertEquals("The singleFileLimit should be got properly.", SINGLE_FILE_LIMIT, fileUpload.getSingleFileLimit());
    }

    /**
     * <p>
     * Tests the accuracy of method <code>getTotalFileLimit()</code>.
     * </p>
     */
    public void testGetTotalFileLimit_Accuracy() {
        assertEquals("The totalFileLimit should be got properly.", TOTAL_FILE_LIMIT, fileUpload.getTotalFileLimit());
    }

    /**
     * <p>
     * Tests the accuracy of method <code>uploadFiles(ServletRequest)</code>.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testUploadFiles_Accuracy() throws Exception {
        fileUpload.uploadFiles(null);
        assertEquals("The method of uploadFiles(ServletRequest, RequestParser) should be called.", true,
            fileUpload.isUploadFilesCalled());
    }

    /**
     * <p>
     * Tests the accuracy of method <code>getUploadedFile(String)</code>.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testGetUploadedFile_Accuracy() throws Exception {
        fileUpload.getUploadedFile(null);
        assertEquals("The method of getUploadedFile(String, boolean) should be called.", true,
            fileUpload.isGetUploadedFileCalled());
    }
}
