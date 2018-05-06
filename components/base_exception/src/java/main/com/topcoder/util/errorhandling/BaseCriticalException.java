/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.errorhandling;

/**
 * <p>
 * This exception is the superclass for any TCS exceptions that are considered
 * 'critical'.
 * </p>
 * <p>
 * This is to be extended by exceptions which cannot be realistically handled
 * within an application - for example, if a database dies or network connection
 * can't be established. These may require the application to halt and human
 * intervention to take place.
 * </p>
 * <p>
 * Currently, it just passes each constructor's parameters to its superclass,
 * but has been split from <code>BaseException</code> to allow its logic to be
 * different from <code>BaseNonCriticalException</code> in the future.
 * </p>
 * <p>
 * <b>Thread safety</b>: This class is not threadsafe - access and modification
 * of the logged flag and key/value information is unrestricted, which could
 * give undefined results if used concurrently. The application throwing the
 * exception should handle it in a threadsafe manner.
 * </p>
 *
 * @author sql_lall, TCSDEVELOPER
 * @version 2.0
 */
public class BaseCriticalException extends BaseException {

    /**
     * <p>
     * Default no-argument constructor.
     * </p>
     */
    public BaseCriticalException() {
        // call the constructor on BaseException: super();
        super();
    }

    /**
     * <p>
     * Message-only constructor, initializes a new critical exception with the
     * given <code>message</code>.
     * </p>
     *
     * @param message Useful message containing a description of why the
     *            exception was thrown - may be null.
     */
    public BaseCriticalException(String message) {
        // call the constructor on BaseException: super(message);
        super(message);
    }

    /**
     * <p>
     * Cause-only constructor, initializes a new critical exception with the
     * given <code>cause</code>.
     * </p>
     *
     * @param cause The initial throwable reason which triggered this exception
     *            to be thrown - may be null.
     */
    public BaseCriticalException(Throwable cause) {
        // call the constructor on BaseException: super(cause);
        super(cause);
    }

    /**
     * <p>
     * Constructor which takes a <code>message</code> and a <code>cause</code>
     * detailing why the critical exception occurs.
     * </p>
     *
     * @param message Useful message containing a description of why the
     *            exception was thrown - may be null.
     * @param cause The initial throwable reason which triggered this exception
     *            to be thrown - may be null.
     */
    public BaseCriticalException(String message, Throwable cause) {
        // call the constructor on BaseException: super(message,cause);
        super(message, cause);
    }

    /**
     * <p>
     * Data-only constructor, initializes a new critical exception with the
     * given <code>data</code>.
     *
     * @param data The additional data to attach to the exception - if this is
     *            null, a new <code>ExceptionData()</code> is used instead.
     */
    public BaseCriticalException(ExceptionData data) {
        // call the constructor on BaseException: super(data);
        super(data);
    }

    /**
     * <p>
     * Constructs the critical exception taking both a <code>message</code>
     * and the additional <code>data</code> to attach to the critical
     * exception.
     * </p>
     *
     * @param message Useful message containing a description of why the
     *            exception was thrown - may be null.
     * @param data The additional data to attach to the exception - if this is
     *            null, a new <code>ExceptionData()</code> is used instead.
     */
    public BaseCriticalException(String message, ExceptionData data) {
        // call the constructor on BaseException: super(message,data);
        super(message, data);
    }

    /**
     * <p>
     * Constructs the critical exception taking both an initial
     * <code>cause</code> and the the additional <code>data</code> to attach
     * to the critical exception.
     * </p>
     *
     * @param cause The initial throwable reason which triggered this exception
     *            to be thrown - may be null.
     * @param data The additional data to attach to the exception - if this is
     *            null, a new <code>ExceptionData()</code> is used instead.
     */
    public BaseCriticalException(Throwable cause, ExceptionData data) {
        // call the constructor on BaseException: super(cause,data);
        super(cause, data);
    }

    /**
     * <p>
     * Constructs the critical exception taking both a <code>message</code>
     * and a <code>cause</code>, as well as the additional <code>data</code>
     * to attach to the critical exception.
     * </p>
     *
     * @param message Useful message containing a description of why the
     *            exception was thrown - may be null.
     * @param cause The initial throwable reason which triggered this exception
     *            to be thrown - may be null.
     * @param data The additional data to attach to the exception - if this is
     *            null, a new ExceptionData() is used instead.
     */
    public BaseCriticalException(String message, Throwable cause, ExceptionData data) {
        // call the constructor on BaseException: super(message,cause,data);
        super(message, cause, data);
    }
}
