/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.servlet.request;

import junit.framework.TestCase;


/**
 * <p>
 * Unit test cases for <code>FileDoesNotExistException</code>.
 * </p>
 *
 * @author PE
 * @version 2.0
 */
public class FileDoesNotExistExceptionUnitTest extends TestCase {
    /**
     * <p>
     * The fileId used for testing.
     * </p>
     */
    private static final String FILEID = "fileId";

    /**
     * <p>
     * Creation test.
     * </p>
     *
     * <p>
     * Verifies the error message is properly propagated and the fileId is properly set.
     * </p>
     */
    public void testFileDoesNotExistException() {
        FileDoesNotExistException ce = new FileDoesNotExistException(FILEID);

        assertNotNull("Unable to instantiate FileDoesNotExistException.", ce);
        assertNotNull("Error message is not properly propagated to super class.", ce.getMessage());
        assertNotNull("FileId is not properly set.", ce.getFileId());
    }

    /**
     * <p>
     * Inheritance test.
     * </p>
     *
     * <p>
     * Verifies FileDoesNotExistException subclasses FileUploadException.
     * </p>
     */
    public void testFileDoesNotExistExceptionInheritance() {
        assertTrue("FileDoesNotExistException does not subclass FileUploadException.",
            new FileDoesNotExistException(FILEID) instanceof FileUploadException);
    }

    /**
     * <p>
     * Tests the accuracy of the method getFileId().
     * </p>
     */
    public void testGetFileId() {
        FileDoesNotExistException ce = new FileDoesNotExistException(FILEID);
        assertNotNull("FileId is not properly got.", ce.getFileId());
    }
}
