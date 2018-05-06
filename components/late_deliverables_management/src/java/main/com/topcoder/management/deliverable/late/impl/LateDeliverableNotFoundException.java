/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.late.impl;

import com.topcoder.util.errorhandling.ExceptionData;

/**
 * <p>
 * This exception is thrown by LateDeliverableManagerImpl and implementations of LateDeliverablePersistence when late
 * deliverable with the specified ID doesn't exist in persistence.
 * </p>
 *
 * <p>
 * <strong>Thread Safety: </strong> This class is not thread safe because its base class is not thread safe.
 * </p>
 *
 * @author saarixx, sparemax
 * @version 1.0
 */
@SuppressWarnings("serial")
public class LateDeliverableNotFoundException extends LateDeliverablePersistenceException {
    /**
     * <p>
     * The ID of the late deliverable that doesn't exist.
     * </p>
     *
     * <p>
     * Is initialized in the constructor and never changed after that. Can be any value. Has a getter.
     * </p>
     */
    private final long lateDeliverableId;

    /**
     * <p>
     * Constructor with error message and ID of the late deliverable.
     * </p>
     *
     * @param message
     *            the explanation of the error. Can be empty string or <code>null</code> (useless, but allowed).
     * @param lateDeliverableId
     *            the ID of the late deliverable that doesn't exist.
     */
    public LateDeliverableNotFoundException(String message, long lateDeliverableId) {
        super(message);

        this.lateDeliverableId = lateDeliverableId;
    }

    /**
     * <p>
     * Constructor with error message, inner cause and ID of the late deliverable.
     * </p>
     *
     * @param message
     *            the explanation of the error. Can be empty string or <code>null</code> (useless, but allowed).
     * @param cause
     *            the underlying cause of the error. Can be <code>null</code>, which means that initial exception is
     *            nonexistent or unknown.
     * @param lateDeliverableId
     *            the ID of the late deliverable that doesn't exist.
     */
    public LateDeliverableNotFoundException(String message, Throwable cause, long lateDeliverableId) {
        super(message, cause);

        this.lateDeliverableId = lateDeliverableId;
    }

    /**
     * <p>
     * Constructor with error message, exception data and ID of the late deliverable.
     * </p>
     *
     * @param message
     *            the explanation of the error. Can be empty string or <code>null</code> (useless, but allowed).
     * @param data
     *            the additional data to attach to the exception. If this is <code>null</code>, a new ExceptionData()
     *            will be automatically used instead.
     * @param lateDeliverableId
     *            the ID of the late deliverable that doesn't exist.
     */
    public LateDeliverableNotFoundException(String message, ExceptionData data, long lateDeliverableId) {
        super(message, data);

        this.lateDeliverableId = lateDeliverableId;
    }

    /**
     * <p>
     * Constructor with error message, inner cause, exception data and ID of the late deliverable.
     * </p>
     *
     * @param message
     *            the explanation of the error. Can be empty string or <code>null</code> (useless, but allowed).
     * @param cause
     *            the underlying cause of the error. Can be <code>null</code>, which means that initial exception is
     *            nonexistent or unknown.
     * @param data
     *            the additional data to attach to the exception. If this is <code>null</code>, a new ExceptionData()
     *            will be automatically used instead.
     * @param lateDeliverableId
     *            the ID of the late deliverable that doesn't exist.
     */
    public LateDeliverableNotFoundException(String message, Throwable cause, ExceptionData data,
        long lateDeliverableId) {
        super(message, cause, data);

        this.lateDeliverableId = lateDeliverableId;
    }

    /**
     * <p>
     * Gets the ID of the late deliverable that doesn't exist.
     * </p>
     *
     * @return the ID of the late deliverable that doesn't exist.
     */
    public long getLateDeliverableId() {
        return lateDeliverableId;
    }
}
