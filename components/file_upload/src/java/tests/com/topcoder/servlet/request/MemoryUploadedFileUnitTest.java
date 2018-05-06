/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.servlet.request;

import junit.framework.TestCase;

import java.io.InputStream;


/**
 * <p>
 * Tests functionality and error cases of <code>MemoryUploadedFile</code> class.
 * </p>
 *
 * @author PE
 * @version 2.0
 */
public class MemoryUploadedFileUnitTest extends TestCase {
    /** Represents the remoteFileName for testing. */
    private static final String REMOTE_FILE_NAME = "remoteFileName";

    /** Represents the contentType for testing. */
    private static final String CONTENT_TYPE = "contentType";

    /** Represents the data for testing. */
    private static final byte[] DATA = new byte[] {1, 0, 1};

    /** Represents the <code>MemoryUploadedFile</code> instance used for testing. */
    private MemoryUploadedFile uploadedFile = null;

    /**
     * <p>
     * Sets up the test environment. The test instance is created.
     * </p>
     */
    protected void setUp() {
        uploadedFile = new MemoryUploadedFile(DATA, REMOTE_FILE_NAME, CONTENT_TYPE);
    }

    /**
     * <p>
     * Tests the constructor <code>MemoryUploadedFile(byte[], String, String)</code> when the given data is null,
     * IllegalArgumentException is expected.
     * </p>
     */
    public void testMemoryUploadedFile_NullData() {
        try {
            new MemoryUploadedFile(null, REMOTE_FILE_NAME, CONTENT_TYPE);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the constructor <code>MemoryUploadedFile(byte[], String, String)</code> when the given remoteFileName is
     * null, IllegalArgumentException is expected.
     * </p>
     */
    public void testMemoryUploadedFile_NullRemoteFileName() {
        try {
            new MemoryUploadedFile(DATA, null, CONTENT_TYPE);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the constructor <code>MemoryUploadedFile(byte[], String, String)</code> when the given remoteFileName is
     * empty, IllegalArgumentException is expected.
     * </p>
     */
    public void testMemoryUploadedFile_EmptyRemoteFileName() {
        try {
            new MemoryUploadedFile(DATA, " ", CONTENT_TYPE);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the accuracy of constructor <code>MemoryUploadedFile(byte[], String, String)</code>.
     * </p>
     */
    public void testMemoryUploadedFile_Accuracy() {
        assertEquals("The contentType value should be set.", CONTENT_TYPE,
            UnitTestHelper.getParentClassPrivateField(uploadedFile.getClass(), uploadedFile, "contentType"));
        assertEquals("The fileId value should be set.", null,
            UnitTestHelper.getParentClassPrivateField(uploadedFile.getClass(), uploadedFile, "fileId"));
        assertEquals("The data value should be set.", DATA,
            UnitTestHelper.getPrivateField(uploadedFile.getClass(), uploadedFile, "data"));
        assertEquals("The remoteFileName value should be set.", REMOTE_FILE_NAME,
            UnitTestHelper.getPrivateField(uploadedFile.getClass(), uploadedFile, "remoteFileName"));
    }

    /**
     * <p>
     * Tests the accuracy of method <code>getRemoteFileName()</code>.
     * </p>
     */
    public void testGetRemoteFileName_Accuracy() {
        assertEquals("The remoteFileName value should be got properly.", REMOTE_FILE_NAME,
            uploadedFile.getRemoteFileName());
    }

    /**
     * <p>
     * Tests the accuracy of method <code>getSize()</code>.
     * </p>
     */
    public void testGetSize_Accuracy() {
        assertEquals("The size value should be got properly.", DATA.length, uploadedFile.getSize());
    }

    /**
     * <p>
     * Tests the accuracy of method <code>getInputStream()</code>.
     * </p>
     *
     * @throws Exception any Exception to JUnit.
     */
    public void testGetInputStream_Accuracy() throws Exception {
        InputStream inputStream = uploadedFile.getInputStream();
        byte[] content = UnitTestHelper.readContent(inputStream);
        UnitTestHelper.assertEquals("The inputStream value should be got properly.", DATA, content);
        inputStream.close();
    }
}
