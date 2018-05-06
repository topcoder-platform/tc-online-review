/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.util.memoryusage;

import com.topcoder.util.errorhandling.BaseException;

/**
 * <p>Exception thrown by the implementations of the memory analysis algorithms.
 * The algorithms rely heavily on reflection which can generate exceptions related
 * to security issues while accessing field values if the access policies are
 * modified. Propagating each of those exceptions exposes the user to too much
 * implementation details so this class serves as a wrapper for those exceptions.
 * If the user wants to get into details then the wrapped exception can be
 * obtained.</p>
 * <p><b>Thread safety:</b> This class is thread safe since it is immutable.</p>
 *
 * @author BryanChen
 * @author AleaActaEst, TexWiller
 * @version 2.0
 */
public class MemoryUsageException extends BaseException {

    /**
     * Constructor with no message and no wrapped exception.
     */
    public MemoryUsageException() {
        super();
    }

    /**
     * Constructor with message and no wrapped exception.
     *
     * @param message the message
     */
    public MemoryUsageException(String message) {
        super(message);
    }

    /**
     * Constructor with message and wrapped exception.
     *
     * @param message the message
     * @param cause the wrapped exception
     */
    public MemoryUsageException(String message, Throwable cause) {
        super(message, cause);
    }
}

