/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.message;

import com.topcoder.processor.ipserver.message.serializable.SimpleSerializableMessage;

/**
 * This is a base class for the ResponseMessage and RequestMessage. It provides the request type field. It extends
 * SimpleSerializableMessage, in order for the subclasses to be serialized using SerializableMessageSerializer. The
 * class is immutable.
 * @author Luca, FireIce
 * @version 1.0
 */
public abstract class FileSystemMessage extends SimpleSerializableMessage {

    /**
     * Represents the request type of this message. initialized in the constructor and never changed later. Can be null.
     * Accessible through the getter.
     */
    private final MessageType type;

    /**
     * Creates an instance with the given arguments.
     * @param handlerId
     *            the id of the handler
     * @param requestId
     *            the id of the request
     * @throws NullPointerException
     *             if any argument is null
     */
    protected FileSystemMessage(String handlerId, String requestId) {
        super(handlerId, requestId);
        type = null;
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
    protected FileSystemMessage(String handlerId, String requestId, MessageType type) {
        super(handlerId, requestId);
        if (type == null) {
            throw new NullPointerException("type is null");
        }
        this.type = type;
    }

    /**
     * Returns the request type of this message. It can return null.
     * @return the type reference
     */
    public MessageType getType() {
        return type;
    }
}
