/*
 * Copyright (c) 2005, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.cache;

import com.topcoder.util.errorhandling.BaseException;

/**
 * This exception signals the failure of the compression/decompression process.
 *
 * @author  AleaActaEst, rem
 * @version 2.0
 * @since   2.0
 */
public class CompressionException extends BaseException {

    /**
     * Creates an instance initialized with the message.
     *
     * @param  message exception message.
     */
    public CompressionException(String message) {
        super(message);
    }

    /**
     * Creates an instance initialized with the message and the exception that this exception will wrap (chain).
     *
     * @param  message exception message.
     * @param  cause cause exception to be chained.
     */
    public CompressionException(String message, Throwable cause) {
        super(message, cause);
    }
}
