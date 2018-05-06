/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.message;

/**
 * Mock implementaion of FileSystemMessage class, only for testing purpose.
 * @author FireIce
 * @version 1.0
 */
public class MockFileSystemMessage extends FileSystemMessage {

    /**
     * Creates an instance with the given arguments.
     * @param handlerId
     *            the id of the handler
     * @param requestId
     *            the id of the request
     * @throws NullPointerException
     *             if any argument is null
     */
    protected MockFileSystemMessage(String handlerId, String requestId) {
        super(handlerId, requestId);
    }

    /**
     * Creates an instance with the given arguments.
     * @param handlerId
     *            the id of the handler
     * @param requestId
     *            the id of the request
     * @param type
     *            the type of the message
     * @throws NullPointerException
     *             if any argument is null
     */
    protected MockFileSystemMessage(String handlerId, String requestId, MessageType type) {
        super(handlerId, requestId, type);
    }
}
