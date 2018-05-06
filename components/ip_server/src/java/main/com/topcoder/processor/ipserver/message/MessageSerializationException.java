/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.processor.ipserver.message;

/**
 * <p>This exception is thrown if any error occurs when serializing/deserializing a message.
 * It is also thrown when the message cannot be serialized/deserialized by a specific serializer.
 * Usually, it wraps an original cause of the error. It is thrown by MessageSerializer.serializeMessage/
 * deserializeMessage methods, and may be thrown by their callers.</p>
 *
 * @author visualage, zsudraco
 * @version 2.0
 * @copyright Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
public class MessageSerializationException extends MessageCreationException {

    /**
     * <p>Creates a MessageSerializationException error with a detailed message.</p>
     *
     * @param message a detailed error message.
     */
    public MessageSerializationException(String message) {
        super(message);
    }

    /**
     * <p>Creates a MessageSerializationException error with a detailed message and the original cause.</p>
     *
     * @param message a detailed error message.
     * @param cause the original cause of this error.
     */
    public MessageSerializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
