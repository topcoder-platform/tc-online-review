/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.servlet.request.accuracytests;

import com.topcoder.servlet.request.MemoryFileUpload;


/**
 * <p>
 * Tests functionality of <code>MemoryUploadedFile</code> class.
 * </p>
 *
 * @author lyt
 * @version 2.0
 */
public class MemoryFileUploadAccuracyTest extends FileUploadAccuracyTest {
    /**
     * <p>
     * Sets up the test environment. The test instance is created. The configuration is loaded.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    protected void setUp() throws Exception {
        super.setUp();
        AccuracyTestHelper.addConfig("accuracytests/MemoryFileUploadConfig.xml");

        fileUpload = new MemoryFileUpload("MemoryFileUploadConfig");
    }

    /**
     * <p>
     * Tears down the test environment. The configuration is cleared.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        AccuracyTestHelper.clearConfig();
    }

    /**
     * <p>
     * Tests the accuracy of constructor <code>MemoryFileUpload(String)</code>.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testMemoryFileUpload_Accuracy() throws Exception {
        assertEquals("singleFileLimit should be properly loaded.", SINGLE_FILE_LIMIT,
            ((Long) AccuracyTestHelper.getPrivateField(fileUpload.getClass().getSuperclass(), fileUpload,
                "singleFileLimit")).longValue());
        assertEquals("totalFileLimit should be properly loaded.", TOTAL_FILE_LIMIT,
            ((Long) AccuracyTestHelper.getPrivateField(fileUpload.getClass().getSuperclass(), fileUpload,
                "totalFileLimit")).longValue());
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