/*
 * Copyright (c) 2005, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.cache;

import com.topcoder.util.errorhandling.BaseRuntimeException;

/**
 * This is an exception which signifies the inability to find a matching class for a particular type.
 *
 * @author  AleaActaEst, rem
 * @version 2.0
 * @since   2.0
 */
public class TypeNotMatchedException extends BaseRuntimeException {

    /**
     * Creates an instance initialized with the message.
     *
     * @param  message exception message.
     * @throws NullPointerException if message is null.
     * @throws IllegalArgumentException if message is empty.
     */
    public TypeNotMatchedException(String message) {
        super(message);

        if (message == null) {
            throw new NullPointerException("'message' can not be null.");
        }

        if (message.trim().length() == 0) {
            throw new IllegalArgumentException("'message' can not be empty string.");
        }
    }

    /**
     * Creates an instance initialized with the message and the exception that this exception will
     * wrap (chain).
     *
     * @param  message exception message.
     * @param  cause cause exception to be chained.
     * @throws NullPointerException if message or cause is null.
     * @throws IllegalArgumentException if message is empty.
     */
    public TypeNotMatchedException(String message, Throwable cause) {
        super(message, cause);

        if (message == null) {
            throw new NullPointerException("'message' can not be null.");
        }

        if (cause == null) {
            throw new NullPointerException("'cause' can not be null.");
        }

        if (message.trim().length() == 0) {
            throw new IllegalArgumentException("'message' can not be empty.");
        }
    }
}
