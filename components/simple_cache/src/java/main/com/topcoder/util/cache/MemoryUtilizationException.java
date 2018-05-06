/*
 * Copyright (c) 2005, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.cache;

import com.topcoder.util.errorhandling.BaseRuntimeException;

/**
 * This exception is thrown whenever memory utilization handler fails. This signals to the caller that
 * memory usage can not be computed.
 *
 * @author  AleaActaEst, rem
 * @version 2.0
 * @since   2.0
 */
public class MemoryUtilizationException extends BaseRuntimeException {

    /**
     * Creates an instance initialized with the message.
     *
     * @param  message exception message.
     */
    public MemoryUtilizationException(String message) {
        super(message);
    }

    /**
     * Creates an instance initialized with the message and the exception that this exception will
     * wrap (chain).
     *
     * @param  message exception message.
     * @param  cause cause exception to be chained.
     */
    public MemoryUtilizationException(String message, Throwable cause) {
        super(message, cause);
    }
}
