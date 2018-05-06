/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.servlet.request.accuracytests;

import com.topcoder.servlet.request.UnitTestHelper;
import com.topcoder.servlet.request.UploadedFile;

import junit.framework.TestCase;


/**
 * <p>
 * Tests functionality of <code>FileUpload</code> class.
 * </p>
 *
 * @author lyt
 * @version 2.0
 */
public class UploadedFileAccuracyTest extends TestCase {
    /** Represents the contentType for testing. */
    private static final String CONTENT_TYPE = "contentType";

    /** Represents the fileId for testing. */
    private static final String FILEID = "lyt";

    /** Represents the <code>LocalUploadedFile</code> instance used for testing. */
    private UploadedFile uploadedFile = null;

    /**
     * <p>
     * Tests the accuracy of constructor <code>UploadedFile(String, String)</code>.
     * </p>
     */
    public void testUploadedFile_Accuracy() {
        // check the values
        assertEquals("The fileId value should be set.", FILEID,
            UnitTestHelper.getPrivateField(uploadedFile.getClass().getSuperclass(), uploadedFile, "fileId"));
        assertEquals("The contentType value should be set.", CONTENT_TYPE,
            UnitTestHelper.getPrivateField(uploadedFile.getClass().getSuperclass(), uploadedFile, "contentType"));
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
}
