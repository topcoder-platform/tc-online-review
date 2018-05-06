/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.processor.ipserver;

import com.topcoder.util.errorhandling.BaseException;


/**
 * <p>
 * The ProcessingException exception is used to wrap any implementation specific exception that may occur while
 * servicing notifications in the handler subclasses.
 * </p>
 *
 * <p>
 * For example, some handler implementation may work with files or with databases. This means I/O or SQL exceptions may
 * occur. Such failures must be reported to the user, and that's what this exception is used for.
 * </p>
 *
 * <p>
 * Note that typically exceptions will be reported to the client, meaning the exception will be wrapped in a Message
 * subclass. This exception is only meant to be used to fatal exception that should halt the IP server.
 * </p>
 *
 * <p>
 * <em>Thread Safety: </em>this class has been made thread safe by being immutable.
 * </p>
 *
 * @author visualage, zsudraco
 * @version 1.0
 * @copyright Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
public class ProcessingException extends BaseException {
    /**
     * <p>
     * Construct a new ProcessingException with the given error message.
     * </p>
     *
     * <p>
     * The given message should be a string describing the error that caused this exception to be thrown.
     * </p>
     *
     * @param message the message describing the exception.
     */
    public ProcessingException(String message) {
        super(message);
    }

    /**
     * <p>
     * Construct a new ProcessingException with given error message and cause of the error.
     * </p>
     *
     * <p>
     * The given message should be a string describing the error that caused this exception to be thrown; the given
     * cause should be the actual cause of this exception.
     * </p>
     *
     * @param message the message describing the exception.
     * @param cause the cause of the exception.
     */
    public ProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
