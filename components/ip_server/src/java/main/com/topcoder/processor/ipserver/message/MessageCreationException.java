/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.processor.ipserver.message;

import com.topcoder.util.errorhandling.BaseException;

/**
 * <p>This exception is thrown if error occurs when creating message, serializing message or deserializing message,
 * It also serves as the base exception of any other custom exception thrown in this package. Since this package
 * is more like a self-contained component, this exception extends from BaseException directly.</p>
 *
 * @author visualage, zsudraco
 * @version 2.0
 * @copyright Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
public class MessageCreationException extends BaseException {

    /**
     * <p>Creates a MessageCreationException error with a detailed message.</p>
     *
     * @param message a detailed error message.
     */
    public MessageCreationException(String message) {
        super(message);
    }

    /**
     * <p>Creates a MessageCreationException error with a detailed message and the original cause.</p>
     *
     * @param message a detailed error message.
     * @param cause the original cause of this error.
     */
    public MessageCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
