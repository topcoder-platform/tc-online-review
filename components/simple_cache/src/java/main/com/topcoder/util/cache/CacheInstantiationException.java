/*
 * Copyright (c) 2005, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.cache;

/**
 * A general purpose run-time exception which signifies the failure to create a SimpleCache.
 * This can be due the inability to instantiate necessary components for the operation of the cache
 * or simply due to configuration issues.
 *
 * @author  AleaActaEst, rem
 * @version 2.0
 * @since   2.0
 */
public class CacheInstantiationException extends CacheException {

    /**
     * Creates an instance initialized with the message.
     *
     * @param  message exception message.
     */
    public CacheInstantiationException(String message) {
        super(message);
    }

    /**
     * Creates an instance initialized with the message and the exception that this exception will
     * wrap (chain).
     *
     * @param  message exception message.
     * @param  cause cause exception to be chained.
     */
    public CacheInstantiationException(String message, Throwable cause) {
        super(message, cause);
    }
}
