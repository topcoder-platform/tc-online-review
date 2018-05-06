/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.accuracytests.message;

import com.topcoder.file.transfer.message.FileSystemMessage;
import com.topcoder.file.transfer.message.MessageType;

/**
 * <p>
 * A mock implementation of abstract FileSystemMessage class.
 * Used to test FileSystemMessage.
 * </p>
 * 
 * @author arylio
 * @version 1.0
 */
public class MockFileSystemMessage extends FileSystemMessage {
    /**
     * <p>
     * Creates an instance with the given arguments.
     * </p>
     * 
     * @param handlerID the id of the handler
     * @param requestID the id of the request
     */
    MockFileSystemMessage(String handlerID, String requestID) {
        super(handlerID, requestID);
    }

    /**
     * <p>
     * Creates an instance with the given arguments.
     * </p>
     * 
     * @param handlerID the id of the handler
     * @param requestID the id of the request
     * @param type the type of the message
     */
    MockFileSystemMessage(String handlerID, String requestID, MessageType type) {
        super(handlerID, requestID, type);
    }
}