/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.termsofuse.dao;

import com.topcoder.util.errorhandling.ExceptionData;

/**
 * <p>
 * This exception is thrown, when the caller tries to create a terms of use dependency loop.
 * </p>
 *
 * <p>
 * <strong>Thread Safety: </strong> This class is not thread safe because its base class is not thread safe.
 * </p>
 *
 * @author saarixx, sparemax
 * @version 1.1
 * @since 1.1
 */
@SuppressWarnings("serial")
public class TermsOfUseCyclicDependencyException extends TermsOfUseDaoException {

    /**
     * <p>
     * Constructor with error message.
     * </p>
     *
     * @param message
     *            the explanation of the error. Can be empty string or <code>null</code> (useless, but allowed).
     */
    public TermsOfUseCyclicDependencyException(String message) {
        super(message);
    }

    /**
     * <p>
     * Constructor with error message and inner cause.
     * </p>
     *
     * @param message
     *            the explanation of the error. Can be empty string or <code>null</code> (useless, but allowed).
     * @param cause
     *            the underlying cause of the error. Can be <code>null</code>, which means that initial exception
     *            is nonexistent or unknown.
     */
    public TermsOfUseCyclicDependencyException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * <p>
     * Constructor with error message and exception data.
     * </p>
     *
     * @param message
     *            the explanation of the error. Can be empty string or <code>null</code> (useless, but allowed).
     * @param data
     *            the additional data to attach to the exception. If this is <code>null</code>, a new
     *            ExceptionData() will be automatically used instead.
     */
    public TermsOfUseCyclicDependencyException(String message, ExceptionData data) {
        super(message, data);
    }

    /**
     * <p>
     * Constructor with error message, inner cause and exception data.
     * </p>
     *
     * @param message
     *            the explanation of the error. Can be empty string or <code>null</code> (useless, but allowed).
     * @param cause
     *            the underlying cause of the error. Can be <code>null</code>, which means that initial exception
     *            is nonexistent or unknown.
     * @param data
     *            the additional data to attach to the exception. If this is <code>null</code>, a new
     *            ExceptionData() will be automatically used instead.
     */
    public TermsOfUseCyclicDependencyException(String message, Throwable cause, ExceptionData data) {
        super(message, cause, data);
    }
}
