/*
 * Copyright (C) 2006, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review;

import com.topcoder.util.errorhandling.ExceptionData;

/**
 * This exception will be thrown by ReviewInformixPersistence if application tries to get, update or remove a
 * non-existing review entity
 *
 * <p>
 * <em>Changes in 1.2:</em>
 * <ol>
 * <li>Renamed "msg" parameters to "message".</li>
 * <li>Added constructors that accept Throwable or ExceptionData.</li>
 * <li>Added thread safety information.</li>
 * </ol>
 * </p>
 *
 * <p>
 * <strong>Thread Safety: </strong> This class is not thread safe because its base class is not thread safe.
 * </p>
 *
 * @author woodjhon, urtks, saarixx, sparemax
 * @version 1.2
 */
@SuppressWarnings("serial")
public class ReviewEntityNotFoundException extends ReviewPersistenceException {

    /**
     * Represents the entity id that is not found in the persistence. It's set in the constructor. Can be any long
     * value.
     *
     * <p>
     * <em>Changes in 1.2:</em>
     * <ol>
     * <li>Made final.</li>
     * <li>Removed default value.</li>
     * </ol>
     * </p>
     */
    private final long id;

    /**
     * Creates a new instance of this exception with the given message and the entity id that is not found in the
     * persistence.
     *
     * <p>
     * <em>Changes in 1.2:</em>
     * <ol>
     * <li>Renamed "msg" parameter to "message".</li>
     * </ol>
     * </p>
     *
     * @param message
     *            the error message.
     * @param id
     *            the entity id that is not found in the persistence.
     */
    public ReviewEntityNotFoundException(String message, long id) {
        super(message);

        this.id = id;
    }

    /**
     * Create the instance with error message, cause exception and the entity id that is not found in the persistence.
     *
     * @param id
     *            the entity id that is not found in the persistence.
     * @param message
     *            the error message.
     * @param cause
     *            the cause exception.
     *
     * @since 1.2
     */
    public ReviewEntityNotFoundException(String message, Throwable cause, long id) {
        super(message, cause);

        this.id = id;
    }

    /**
     * Creates a new instance of this exception with the given message, exception data and the entity id that is not
     * found in the persistence.
     *
     * @param id
     *            the entity id that is not found in the persistence.
     * @param message
     *            the detailed error message of this exception.
     * @param data
     *            the exception data.
     *
     * @since 1.2
     */
    public ReviewEntityNotFoundException(String message, ExceptionData data, long id) {
        super(message, data);

        this.id = id;
    }

    /**
     * Creates a new instance of this exception with the given message, cause, exception data and the entity id that
     * is not found in the persistence.
     *
     * @param id
     *            the entity id that is not found in the persistence.
     * @param message
     *            the detailed error message of this exception.
     * @param cause
     *            the inner cause of this exception.
     * @param data
     *            the exception data.
     *
     * @since 1.2
     */
    public ReviewEntityNotFoundException(String message, Throwable cause, ExceptionData data, long id) {
        super(message, cause, data);

        this.id = id;
    }

    /**
     * Gets the entity id that is not found in the persistence.
     *
     * @return the entity id that is not found in the persistence.
     */
    public long getId() {
        return id;
    }
}
