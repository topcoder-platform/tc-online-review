/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.idgenerator;

import com.topcoder.util.errorhandling.BaseException;


/**
 * Thrown whenever this component cannot retrieve ID sequence configuration
 * or generate an ID (e.g. when unable to retrieve ID information from the
 * database).
 *
 * @author srowen, iggy36, gua
 * @version 3.0
 */
public class IDGenerationException extends BaseException {

    /**
     * Creates a new IDGenerationException.
     */
    public IDGenerationException() {
    }

    /**
     * Creates a new IDGenerationException with the given message.
     *
     * @param message exception message
     */
    public IDGenerationException(String message) {
        super(message);
    }

    /**
     * <p>
     * Creates a new IDGenerationException with the given message and cause.
     * </p>
     *
     * @param message exception message
     * @param cause cause of this IDGenerationException
     */
    public IDGenerationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * <p>
     * Creates a new IDGenerationException with the given cause.
     * </p>
     *
     * @param cause cause of this IDGenerationException
     */
    public IDGenerationException(Throwable cause) {
        super(cause);
    }
}

