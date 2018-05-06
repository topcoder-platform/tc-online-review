/*
 * Copyright (C) 2006-2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.phase;

import com.topcoder.util.errorhandling.BaseCriticalException;
import com.topcoder.util.errorhandling.ExceptionData;

/**
 * <p>
 * Thrown by {@link PhasePersistence PhasePersistence} instances when an error occurs related to the persistent store.
 * </p>
 * <p>
 * Changes in 1.1:
 * <ol>
 * <li>Extends BaseCriticalException instead of BaseException</li>
 * <li>Added new constructors to meet TopCoder standards</li>
 * </ol>
 * </p>
 * <p>
 * Thread Safety: This class is not thread safe because its base class is not thread safe.
 * </p>
 * @author AleaActaEst, saarixx, RachaelLCook, sokol
 * @version 1.1
 */
@SuppressWarnings("serial")
public class PhasePersistenceException extends BaseCriticalException {

    /**
     * Constructs a new exception with the specified message and cause.
     * @param message the reason for the exception
     * @param cause the internal exception that caused this exception
     */
    public PhasePersistenceException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new exception with the specified message.
     * @param message the reason for the exception
     */
    public PhasePersistenceException(String message) {
        super(message);
    }

    /**
     * Creates a new instance of this exception with the given message and exception data.
     * @param message the detailed error message of this exception
     * @param data the exception data
     * @since 1.1
     */
    public PhasePersistenceException(String message, ExceptionData data) {
        super(message, data);
    }

    /**
     * <p>
     * Creates a new instance of this exception with the given message, cause and exception data.
     * </p>
     * @param message the detailed error message of this exception
     * @param cause the inner cause of this exception
     * @param data the exception data
     * @since 1.1
     */
    public PhasePersistenceException(String message, Throwable cause, ExceptionData data) {
        super(message, cause, data);
    }
}
