/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.accuracytests.validator;

import com.topcoder.file.transfer.accuracytests.AccuracyTestHelper;
import com.topcoder.file.transfer.message.MessageType;
import com.topcoder.file.transfer.message.RequestMessage;
import com.topcoder.file.transfer.validator.FreeDiskSpaceNativeChecker;
import com.topcoder.file.transfer.validator.UploadRequestValidator;

import junit.framework.TestCase;

/**
 * <p>
 * Accuracy Test for UploadRequestValidator class.
 * </p>
 * 
 * @author arylio
 * @version 1.0
 */
public class UploadRequestValidatorAccuracyTest extends TestCase {
    /**
     * <p>
     * Represents an instnace of UploadRequestValidator
     * </p>
     */
    private UploadRequestValidator validator;

    /**
     * <p>
     * Set up for each test.
     * </p>
     */
    protected void setUp() throws Exception {
        super.setUp();

        validator = new UploadRequestValidator(new FreeDiskSpaceNativeChecker(
                "test_files/accuracy/"));
    }

    /**
     * <p>
     * Test ctor UploadRequestValidator(FreeDiskSpaceChecker
     * freeDiskSpaceChecker), instance should be created.
     * </p>
     */
    public void testCtor() {
        assertNotNull("Failed to create instance of UploadRequestValidator",
                validator);
    }

    /**
     * <p>
     * Test valid(Object obj), if the lenght is large then freespace, return
     * false, other return true
     * </p>
     */
    public void testValid_True() {
        Object obj[] = new Object[2];
        obj[0] = "filename";
        obj[1] = new Long(AccuracyTestHelper.MAX_FREE_DISKSPACE);
        RequestMessage msg = new RequestMessage("1", "1",
                MessageType.CHECK_UPLOAD_FILE, obj);
        assertTrue("True is expected", validator.valid(msg));
    }

    /**
     * <p>
     * Test valid(Object obj), if the lenght is large then freespace, return
     * false, other return true
     * </p>
     */
    public void testValid_False() {
        Object obj[] = new Object[2];
        obj[0] = "filename";
        obj[1] = new Long(AccuracyTestHelper.MAX_FREE_DISKSPACE + 1);
        RequestMessage msg = new RequestMessage("1", "1",
                MessageType.CHECK_UPLOAD_FILE, obj);
        assertFalse("True is expected", validator.valid(msg));
    }

    /**
     * <p>
     * Test getMessage(Object obj), if obj is valid, null is expected,
     * otherwise, invalid message.
     * </p>
     */
    public void testGetMessage_Valid() {
        Object obj[] = new Object[2];
        obj[0] = "filename";
        obj[1] = new Long(AccuracyTestHelper.MAX_FREE_DISKSPACE);
        RequestMessage msg = new RequestMessage("1", "1",
                MessageType.CHECK_UPLOAD_FILE, obj);
        assertNull("Message should valid", validator.getMessage(msg));
    }

    /**
     * <p>
     * Test getMessage(Object obj), if obj is valid, null is expected,
     * otherwise, invalid message.
     * </p>
     */
    public void testGetMessage_Invalid() {
        Object obj[] = new Object[2];
        obj[0] = "filename";
        obj[1] = new Long(AccuracyTestHelper.MAX_FREE_DISKSPACE + 1);
        RequestMessage msg = new RequestMessage("1", "1",
                MessageType.CHECK_UPLOAD_FILE, obj);
        assertNotNull("Message should valid", validator.getMessage(msg));
    }
}