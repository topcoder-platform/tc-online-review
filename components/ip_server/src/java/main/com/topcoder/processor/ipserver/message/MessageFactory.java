/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.processor.ipserver.message;

/**
 * <p>This interface provides the features of a message factory. The message factory can
 * create messages in two ways, either through a name, or serialization. It is also responsible to
 * serialize/deserialize a message instance properly.</p>
 *
 * <p>Implementations of this interface must be thread-safe.</p>
 *
 * @author visualage, zsudraco
 * @version 2.0
 * @copyright Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
public interface MessageFactory {
    /**
     * <p>
     * Serializes the given Message instance. The serialization is done by the serializer specified in the given
     * Message instance. Any error occurred during serialization should be wrapped into MessageSerializationException.
     * </p>
     *
     * @param message the Message instance to be serialized.
     * @return the serialized byte array from the given message.
     * @throws NullPointerException if message is null.
     * @throws MessageSerializationException if the given message cannot be serialized by the serializer specified
     * in the message instance; or any error ocurs when serializing the message.
     */
    byte[] serializeMessage(Message message) throws MessageSerializationException;

    /**
     * <p>Creates a message instance according to the given name, handler ID and request ID.</p>
     *
     * @param name the name used to identify which type of message should be created.
     * @param handlerId the handler ID of the new message instance.
     * @param requestId the request ID of the new message instance.
     * @return a created new message instance.
     * @throws NullPointerException if any argument is null.
     * @throws IllegalArgumentException if name is an empty string.
     * @throws UnknownMessageException if the name cannot be found in the factory.
     * @throws MessageCreationException if any error occurs when creating the new message instance.
     */
    Message getMessage(String name, String handlerId, String requestId) throws MessageCreationException;

    /**
     * <p>Creates a message instance according to the serialized data.</p>
     *
     * @param data the byte array representing the serialized data.
     * @return a new message instance created from the serialized data.
     * @throws NullPointerException if data is null.
     * @throws IllegalArgumentException if data is an empty byte array.
     * @throws MessageSerializationException if any error occurs when deserializing the data.
     */
    Message deserializeMessage(byte[] data) throws MessageSerializationException;
}
