/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.processor.ipserver.message;

/**
 * <p>This exception is thrown if a name of message type cannot be found in the message factory.
 * It is thrown by MessageFactory.getMessage methods.</p>
 *
 * @author visualage, zsudraco
 * @version 2.0
 * @copyright Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
public class UnknownMessageException extends MessageCreationException {

    /**
     * <p>Creates a UnknownMessageException error with a detailed message.</p>
     *
     * @param message a detailed error message.
     */
    public UnknownMessageException(String message) {
        super(message);
    }

    /**
     * <p>Creates a UnknownMessageException error with a detailed message and the original cause.</p>
     *
     * @param message a detailed error message.
     * @param cause the original cause of this error.
     */
    public UnknownMessageException(String message, Throwable cause) {
        super(message, cause);
    }
}
