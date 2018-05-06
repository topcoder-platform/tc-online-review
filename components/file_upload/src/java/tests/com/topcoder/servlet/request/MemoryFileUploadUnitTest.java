/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.servlet.request;

/**
 * <p>
 * Tests functionality and error cases of <code>MemoryFileUpload</code> class.
 * </p>
 *
 * @author PE
 * @version 2.0
 */
public class MemoryFileUploadUnitTest extends BaseFileUploadUnitTest {
    /**
     * <p>
     * Sets up the test environment. The test instance is created. The configuration is loaded.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    protected void setUp() throws Exception {
        super.setUp();
        UnitTestHelper.clearConfig();
        UnitTestHelper.addConfig("FileUploadConfig.xml");
        fileUpload = new MemoryFileUpload("Valid");
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
     * Tests the constructor <code>MemoryFileUpload(String)</code> when the given namespace is null,
     * IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testMemoryFileUpload_NullNamespace() throws Exception {
        try {
            new MemoryFileUpload(null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the constructor <code>MemoryFileUpload(String)</code> when the given namespace is empty,
     * IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testMemoryFileUpload_EmptyNamespace() throws Exception {
        try {
            new MemoryFileUpload(" ");
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the constructor <code>MemoryFileUpload(String)</code> when the given namespace does not exist,
     * ConfigurationException is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testMemoryFileUpload_NotExistNamespace() throws Exception {
        try {
            new MemoryFileUpload("NotExist");
            fail("ConfigurationException should be thrown.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the constructor <code>MemoryFileUpload(String)</code> when the single_file_limit property is missing,
     * default value will be loaded. No exception is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testMemoryFileUpload_SingleFileLimitPropertyMissing() throws Exception {
        fileUpload = new MemoryFileUpload("SingleFileLimitPropertyMissing");

        String value = UnitTestHelper.getParentClassPrivateField(fileUpload.getClass(), fileUpload, "singleFileLimit")
                                     .toString();
        assertEquals("The default value should be set.", (long) -1, Long.parseLong(value));
        value = UnitTestHelper.getParentClassPrivateField(fileUpload.getClass(),
                fileUpload, "totalFileLimit").toString();
        assertEquals("The totalFileLimit value should be set.", TOTAL_FILE_LIMIT, Long.parseLong(value));
    }

    /**
     * <p>
     * Tests the constructor <code>MemoryFileUpload(String)</code> when the single_file_limit property is empty,
     * ConfigurationException is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testMemoryFileUpload_SingleFileLimitPropertyEmpty() throws Exception {
        try {
            new MemoryFileUpload("SingleFileLimitPropertyEmpty");
            fail("ConfigurationException should be thrown.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the constructor <code>MemoryFileUpload(String)</code> when the single_file_limit property is invalid,
     * ConfigurationException is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testMemoryFileUpload_SingleFileLimitPropertyInvalid1() throws Exception {
        try {
            new MemoryFileUpload("SingleFileLimitPropertyInvalid1");
            fail("ConfigurationException should be thrown.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the constructor <code>MemoryFileUpload(String)</code> when the single_file_limit property is invalid,
     * ConfigurationException is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testMemoryFileUpload_SingleFileLimitPropertyInvalid2() throws Exception {
        try {
            new MemoryFileUpload("SingleFileLimitPropertyInvalid2");
            fail("ConfigurationException should be thrown.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the constructor <code>MemoryFileUpload(String)</code> when the single_file_limit property is invalid,
     * ConfigurationException is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testMemoryFileUpload_SingleFileLimitPropertyInvalid3() throws Exception {
        try {
            new MemoryFileUpload("SingleFileLimitPropertyInvalid3");
            fail("ConfigurationException should be thrown.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the constructor <code>MemoryFileUpload(String)</code> when the single_file_limit property is invalid,
     * ConfigurationException is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testMemoryFileUpload_SingleFileLimitPropertyInvalid4() throws Exception {
        try {
            new MemoryFileUpload("SingleFileLimitPropertyInvalid4");
            fail("ConfigurationException should be thrown.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the constructor <code>MemoryFileUpload(String)</code> when the total_file_limit property is missing,
     * default value will be loaded. No exception is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testMemoryFileUpload_TotalFileLimitPropertyMissing() throws Exception {
        fileUpload = new MemoryFileUpload("TotalFileLimitPropertyMissing");

        String value = UnitTestHelper.getParentClassPrivateField(fileUpload.getClass(), fileUpload, "totalFileLimit")
                                     .toString();
        assertEquals("The default value should be set.", (long) -1, Long.parseLong(value));
        value = UnitTestHelper.getParentClassPrivateField(fileUpload.getClass(), fileUpload, "singleFileLimit")
                              .toString();
        assertEquals("The singleFileLimit value should be set.", SINGLE_FILE_LIMIT, Long.parseLong(value));
    }

    /**
     * <p>
     * Tests the constructor <code>MemoryFileUpload(String)</code> when the total_file_limit property is empty,
     * ConfigurationException is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testMemoryFileUpload_TotalFileLimitPropertyEmpty() throws Exception {
        try {
            new MemoryFileUpload("TotalFileLimitPropertyEmpty");
            fail("ConfigurationException should be thrown.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the constructor <code>MemoryFileUpload(String)</code> when the total_file_limit property is invalid,
     * ConfigurationException is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testMemoryFileUpload_TotalFileLimitPropertyInvalid1() throws Exception {
        try {
            new MemoryFileUpload("TotalFileLimitPropertyInvalid1");
            fail("ConfigurationException should be thrown.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the constructor <code>MemoryFileUpload(String)</code> when the total_file_limit property is invalid,
     * ConfigurationException is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testMemoryFileUpload_TotalFileLimitPropertyInvalid2() throws Exception {
        try {
            new MemoryFileUpload("TotalFileLimitPropertyInvalid2");
            fail("ConfigurationException should be thrown.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the constructor <code>MemoryFileUpload(String)</code> when the total_file_limit property is invalid,
     * ConfigurationException is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testMemoryFileUpload_TotalFileLimitPropertyInvalid3() throws Exception {
        try {
            new MemoryFileUpload("TotalFileLimitPropertyInvalid3");
            fail("ConfigurationException should be thrown.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the constructor <code>MemoryFileUpload(String)</code> when the total_file_limit property is invalid,
     * ConfigurationException is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testMemoryFileUpload_TotalFileLimitPropertyInvalid4() throws Exception {
        try {
            new MemoryFileUpload("TotalFileLimitPropertyInvalid4");
            fail("ConfigurationException should be thrown.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the accuracy of constructor <code>MemoryFileUpload(String)</code>.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testMemoryFileUpload_Accuracy() throws Exception {
        assertEquals("singleFileLimit should be properly loaded.", "" + SINGLE_FILE_LIMIT,
            UnitTestHelper.getParentClassPrivateField(fileUpload.getClass(), fileUpload, "singleFileLimit").toString());
        assertEquals("totalFileLimit should be properly loaded.", "" + TOTAL_FILE_LIMIT,
            UnitTestHelper.getParentClassPrivateField(fileUpload.getClass(), fileUpload, "totalFileLimit").toString());
    }

    /**
     * <p>
     * Tests the method <code>getUploadedFile(String, boolean)</code>, UnsupportedOperationException is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testGetUploadedFile_Accuracy() throws Exception {
        try {
            fileUpload.getUploadedFile("file1", false);
            fail("UnsupportedOperationException should be thrown.");
        } catch (UnsupportedOperationException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the method <code>removeUploadedFile(String)</code>, UnsupportedOperationException is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testRemoveUploadedFile_Accuracy() throws Exception {
        try {
            fileUpload.removeUploadedFile("file1");
            fail("UnsupportedOperationException should be thrown.");
        } catch (UnsupportedOperationException e) {
            // good
        }
    }
}
