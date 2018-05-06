/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.accuracytests.message;

import com.topcoder.file.transfer.message.FileSystemMessage;
import com.topcoder.file.transfer.message.MessageType;

import junit.framework.TestCase;

/**
 * <p>
 * Accuracy Test for FileSystemMessage class.
 * </p>
 * 
 * @author arylio
 * @version 1.0
 */
public class FileSystemMessageAccuracyTest extends TestCase {
    /**
     * <p>
     * Represents the hander id of message type.
     * </p>
     */
    private String handlerID = "handlerID";

    /**
     * <p>
     * Represents the request id of message type.
     * </p>
     */
    private String requestID = "requestID";

    /**
     * <p>
     * Test ctor FileSystemMessage(String handlerID, String requestId), the
     * instance should be create with the arguments.
     * </p>
     */
    public void testCtor1() {
        FileSystemMessage msg = new MockFileSystemMessage(handlerID, requestID);
        assertNotNull("Failed to create instance of FileSystemMessage", msg);
        assertEquals(
                "Failed to create instance of FileSystemMessage correctly",
                handlerID, msg.getHandlerId());
        assertEquals(
                "Failed to create instance of FileSystemMessage correctly",
                requestID, msg.getRequestId());
    }

    /**
     * <p>
     * Test ctor FileSystemMessage(String handlerID, String requestId,
     * MessageType type), the instance should be create with the arguments.
     * </p>
     */
    public void testCtor2() {
        FileSystemMessage msg = new MockFileSystemMessage(handlerID, requestID,
                MessageType.ADD_FILE_TO_GROUP);
        assertNotNull("Failed to create instance of FileSystemMessage", msg);
        assertEquals(
                "Failed to create instance of FileSystemMessage correctly",
                handlerID, msg.getHandlerId());
        assertEquals(
                "Failed to create instance of FileSystemMessage correctly",
                requestID, msg.getRequestId());
        assertEquals(
                "Failed to create instance of FileSystemMessage correctly",
                MessageType.ADD_FILE_TO_GROUP, msg.getType());
    }
}