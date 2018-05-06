/*
 * Copyright (C) 2006-2007 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.ajax.handlers;

import com.topcoder.util.errorhandling.BaseException;

/**
 * <p>
 * Represents an exception used to wrap any checked exception thrown by the ResourceManager class.
 * <br><br>
 * This exception is thrown by  many methods in the CommonHandler class.
 * </p>
 *
 * @author topgear
 * @author assistant
 * @author George1
 * @version 1.0.1
 */
public class ResourceException extends BaseException {

    /**
     * <p>
     * Creates a new instance of this exception class.
     * </p>
     */
    public ResourceException() {
        // do nothing
    }

    /**
     * <p>
     * Creates a new instance of this exception class with a descriptive message.
     * </p>
     *
     * @param message a descriptive message about the exception
     */
    public ResourceException(String message) {
        super(message);
    }

    /**
     * <p>
     * Creates a new instance of this exception class with the cause of the exception.
     * </p>
     *
     * @param cause the cause of this exception
     */
    public ResourceException(Throwable cause) {
        super(cause);
    }

    /**
     * <p>
     * Creates a new instance of this exception class with a descriptive message, and the cause of the exception.
     * </p>
     *
     * @param message a descriptive message about the exception
     * @param cause the cause of this exception
     */
    public ResourceException(String message, Throwable cause) {
        super(message, cause);
    }
}
