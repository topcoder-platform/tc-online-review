/*
 * Copyright (c) 2005, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.cache;

import com.topcoder.util.errorhandling.BaseRuntimeException;

/**
 * This is a run-time conversion exception which signals the failure of the byte array to object
 * (and vice versa) conversion process.
 *
 * @author  AleaActaEst, rem
 * @version 2.0
 * @since   2.0
 */
public class ObjectByteConversionException extends BaseRuntimeException {

    /**
     * Creates an instance initialized with the message.
     *
     * @param  message exception message.
     */
    public ObjectByteConversionException(String message) {
        super(message);
    }

    /**
     * Creates an instance initialized with the message and the exception that this exception will
     * wrap (chain).
     *
     * @param  message exception message.
     * @param  cause cause exception to be chained.
     */
    public ObjectByteConversionException(String message, Throwable cause) {
        super(message, cause);
    }
}
