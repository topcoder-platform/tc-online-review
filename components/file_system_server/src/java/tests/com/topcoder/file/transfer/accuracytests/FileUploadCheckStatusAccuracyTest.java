/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.accuracytests;

import com.topcoder.file.transfer.FileUploadCheckStatus;

import junit.framework.TestCase;

/**
 * <p>
 * Accuracy test for FileUploadCheckStatus class.
 * </p>
 * 
 * @author arylio
 * @version 1.0
 */
public class FileUploadCheckStatusAccuracyTest extends TestCase {
    /**
     * <p>
     * Test static member UPLOAD_ACCEPTED, the value should be
     * "UPLOAD_ACCEPTED".
     * </p>
     */
    public void testUPLOAD_ACCEPTED() {
        assertEquals("UPLOAD_ACCEPTED is not initailzie correctly.",
                "UPLOAD_ACCEPTED", FileUploadCheckStatus.UPLOAD_ACCEPTED
                        .getValue());
    }

    /**
     * <p>
     * Test static member UPLOAD_NOT_ACCEPTED, the value should be
     * "UPLOAD_NOT_ACCEPTED".
     * </p>
     */
    public void testUPLOAD_NOT_ACCEPTED() {
        assertEquals("UPLOAD_NOT_ACCEPTED is not initailzie correctly.",
                "UPLOAD_NOT_ACCEPTED",
                FileUploadCheckStatus.UPLOAD_NOT_ACCEPTED.getValue());
    }
}