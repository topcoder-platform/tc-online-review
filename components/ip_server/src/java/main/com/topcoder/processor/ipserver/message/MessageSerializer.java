/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.processor.ipserver.message;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * <p>This interface defines certain serialization/deserialization operations. Usually, for one kind of
 * serialization framework, only one serializer is needed. For example, all messages implements java.io.Serializable
 * can share the same serializer/deserializer. This interface is used by the MessageFactory when Message instances
 * are created from or serialized to a byte array.</p>
 *
 * <p>Implementation of this interface should contain a default constructor that may be used by the
 *    MessageFactory instance.</p>
 *
 * <p>Thread safety: implementation of this interface must be thread-safe.</p>
 *
 * @author visualage, zsudraco
 * @version 2.0
 * @copyright Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
public interface MessageSerializer {
    /**
     * <p>Deserializes the byte array to obtain a Message instance. The serialized data is read from the given input
     * stream. If the data cannot be deserialized by any reason, or any error occurred during deserialization, the
     * error is wrapped into MessageSerializationException.</p>
     *
     * @param input the input stream where serialized Message is read from.
     * @return the Message instance which is deserialized from the given input stream.
     * @throws NullPointerException if input is null.
     * @throws MessageSerializationException if data read from input stream cannot be deserialized by this serializer
     * or any error occurs when deserializing the data.
     */
    Message deserializeMessage(InputStream input) throws MessageSerializationException;

    /**
     * <p>Serializes the given Message instance. The serialized data is written into the given output stream.
     * Any error occurred during serialization is wrapped into MessageSerializationException.</p>
     *
     * @param message the Message instance to be serialized.
     * @param output the output stream where serialized data is written to.
     * @throws NullPointerException if any argument is null.
     * @throws MessageSerializationException if the given message cannot be serialized by this serializer or any
     * error ocurs when serializing the message.
     */
    void serializeMessage(Message message, OutputStream output) throws MessageSerializationException;
}
