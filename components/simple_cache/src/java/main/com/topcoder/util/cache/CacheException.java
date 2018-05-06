/*
 * Copyright (c) 2005, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.cache;

import com.topcoder.util.errorhandling.BaseRuntimeException;

/**
 * A general purpose run-time exception which signifies the failure to process an operation on a
 * SimpleCache. This can be due the inability to compress/decompress for example.
 *
 * @author  AleaActaEst, rem
 * @version 2.0
 * @since   2.0
 */
public class CacheException extends BaseRuntimeException {

    /**
     * Creates an instance initialized with the message.
     *
     * @param  message exception message.
     */
    public CacheException(String message) {
        super(message);
    }

    /**
     * Creates an instance initialized with the message and the exception that this exception will
     * wrap (chain).
     *
     * @param  message exception message.
     * @param  cause cause exception to be chained.
     */
    public CacheException(String message, Throwable cause) {
        super(message, cause);
    }
}
