/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.servlet.request;

import junit.framework.TestCase;


/**
 * <p>
 * Tests functionality and error cases of <code>UploadedFile</code> class.
 * </p>
 *
 * @author PE
 * @version 2.0
 */
public class UploadedFileUnitTest extends TestCase {
    /** Represents the fileId for testing. */
    private static final String FILEID = "11";

    /** Represents the contentType for testing. */
    private static final String CONTENT_TYPE = "contentType";

    /** Represents the <code>UploadedFile</code> instance used for testing. */
    private UploadedFile uploadedFile = null;

    /**
     * <p>
     * Sets up the test environment. The test instance is created.
     * </p>
     */
    protected void setUp() {
        uploadedFile = new MockUploadedFile(FILEID, CONTENT_TYPE);
    }

    /**
     * <p>
     * Tests the constructor <code>UploadedFile(String, String)</code> when the given fileId is null, no exception is
     * expected.
     * </p>
     */
    public void testUploadedFile_NullFileId() {
        uploadedFile = new MockUploadedFile(null, CONTENT_TYPE);

        // check the values
        assertEquals("The fileId value should be set.", null,
            UnitTestHelper.getParentClassPrivateField(uploadedFile.getClass(), uploadedFile, "fileId"));
        assertEquals("The contentType value should be set.", CONTENT_TYPE,
            UnitTestHelper.getParentClassPrivateField(uploadedFile.getClass(), uploadedFile, "contentType"));
    }

    /**
     * <p>
     * Tests the constructor <code>UploadedFile(String, String)</code> when the given fileId is empty, no exception is
     * expected.
     * </p>
     */
    public void testUploadedFile_EmptyFileId() {
        uploadedFile = new MockUploadedFile(" ", CONTENT_TYPE);

        // check the values
        assertEquals("The fileId value should be set.", " ",
            UnitTestHelper.getParentClassPrivateField(uploadedFile.getClass(), uploadedFile, "fileId"));
        assertEquals("The contentType value should be set.", CONTENT_TYPE,
            UnitTestHelper.getParentClassPrivateField(uploadedFile.getClass(), uploadedFile, "contentType"));
    }

    /**
     * <p>
     * Tests the constructor <code>UploadedFile(String, String)</code> when the given contentType is null, no exception
     * is expected.
     * </p>
     */
    public void testUploadedFile_NullContentType() {
        uploadedFile = new MockUploadedFile(FILEID, null);

        // check the values
        assertEquals("The fileId value should be set.", FILEID,
            UnitTestHelper.getParentClassPrivateField(uploadedFile.getClass(), uploadedFile, "fileId"));
        assertEquals("The contentType value should be set.", null,
            UnitTestHelper.getParentClassPrivateField(uploadedFile.getClass(), uploadedFile, "contentType"));
    }

    /**
     * <p>
     * Tests the constructor <code>UploadedFile(String, String)</code> when the given contentType is empty, no
     * exception is expected.
     * </p>
     */
    public void testUploadedFile_EmptyContentType() {
        uploadedFile = new MockUploadedFile(FILEID, " ");

        // check the values
        assertEquals("The fileId value should be set.", FILEID,
            UnitTestHelper.getParentClassPrivateField(uploadedFile.getClass(), uploadedFile, "fileId"));
        assertEquals("The contentType value should be set.", " ",
            UnitTestHelper.getParentClassPrivateField(uploadedFile.getClass(), uploadedFile, "contentType"));
    }

    /**
     * <p>
     * Tests the accuracy of constructor <code>UploadedFile(String, String)</code>.
     * </p>
     */
    public void testUploadedFile_Accuracy() {
        // check the values
        assertEquals("The fileId value should be set.", FILEID,
            UnitTestHelper.getParentClassPrivateField(uploadedFile.getClass(), uploadedFile, "fileId"));
        assertEquals("The contentType value should be set.", CONTENT_TYPE,
            UnitTestHelper.getParentClassPrivateField(uploadedFile.getClass(), uploadedFile, "contentType"));
    }

    /**
     * <p>
     * Tests the accuracy of method <code>getFileId()</code>.
     * </p>
     */
    public void testGetFileId_Accuracy() {
        // check the values
        assertEquals("The fileId value should be got properly.", FILEID, uploadedFile.getFileId());
    }

    /**
     * <p>
     * Tests the accuracy of method <code>getContentType()</code>.
     * </p>
     */
    public void testGetContentType_Accuracy() {
        // check the values
        assertEquals("The t value should be got properly.", CONTENT_TYPE, uploadedFile.getContentType());
    }
}
