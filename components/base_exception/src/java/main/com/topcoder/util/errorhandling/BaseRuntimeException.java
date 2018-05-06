/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.errorhandling;

import java.util.Date;

/**
 * <p>
 * <code>BaseRuntimeException</code> is the eventual superclass for any custom
 * runtime exceptions used within TC software components - it provides all the
 * logic of Java 1.4 runtime exceptions (message, stack trace,..).
 * </p>
 * <p>
 * <b>Changes for v2.0: </b> It includes a collection of additional information
 * about the runtime exception, such as module and error codes for
 * identification, a transient logged flag, trigger date/time/thread as well as
 * unlimited key/value pairs. This runtime exception is similar to
 * <code>BaseNonCriticalException</code>, except that methods are not
 * required to declare that they throw <code>BaseRuntimeExceptions</code>.
 * </p>
 * <p>
 * <b>Thread safety</b>: This class is not threadsafe - access and modification
 * of the logged flag and key/value information is unrestricted, which could
 * give undefined results if used concurrently. The application throwing the
 * exception should handle it in a threadsafe manner.
 * </p>
 * <p>
 * Note - it is recommended that custom exceptions do not throw exceptions from
 * their constructors.
 * </p>
 *
 * @author srowen, Sleeve
 * @author sql_lall, TCSDEVELOPER
 * @version 2.0
 * @since 1.0
 */
public class BaseRuntimeException extends RuntimeException {

    /**
     * <p>
     * Storage container for the extra information attached to this runtime
     * exception.
     * </p>
     * <p>
     * This is initialized on construction, either to a new
     * <code>ExceptionData()</code> or to a given runtime exception data
     * parameter. Afterwards it will be non-null and immutable though its
     * internal values may change.
     * </p>
     * <p>
     * It is then used within any set/get method, the call is delegated to this
     * member which updates its state.
     * </p>
     *
     * @since 2.0
     */
    private final ExceptionData data;

    /**
     * <p>
     * Default no-argument constructor.
     * </p>
     * <p>
     * This also initializes the internal data member to a new <code>ExceptionData()</code>
     * instance.
     * </p>
     * <p>
     * <b>Changes to v2.0: </b> The internal data member is initialized to a new
     * <code>ExceptionData()</code>.
     * </p>
     *
     * @since 1.0
     */
    public BaseRuntimeException() {
        this(new ExceptionData());
    }

    /**
     * <p>
     * Message-only constructor, initializes a new runtime exception with the
     * given <code>message</code>.
     * </p>
     * <p>
     * This also initializes the internal data member to a new <code>ExceptionData()</code>
     * instance.
     * </p>
     * <p>
     * <b>Changes to v2.0: </b> The internal data member is initialized to a new
     * <code>ExceptionData()</code>.
     * </p>
     *
     * @param message Useful message containing a description of why the runtime
     *            exception was thrown - may be null.
     * @since 1.0
     */
    public BaseRuntimeException(String message) {
        this(message, new ExceptionData());
    }

    /**
     * <p>
     * Cause-only constructor, initializes a new runtime exception with the
     * given <code>cause</code>.
     * </p>
     * <p>
     * This also initializes the internal data member to a new <code>ExceptionData()</code>
     * instance.
     * </p>
     * <p>
     * <b>Changes to v2.0: </b> The internal data member is initialized to a new
     * <code>ExceptionData()</code>.
     * </p>
     *
     * @param cause The initial throwable reason which triggered this runtime
     *            exception to be thrown - may be null.
     * @since 1.0
     */
    public BaseRuntimeException(Throwable cause) {
        this(cause, new ExceptionData());
    }

    /**
     * <p>
     * Constructor which takes a <code>message</code> and a <code>cause</code>
     * detailing why the runtime exception occurs.
     * </p>
     * <p>
     * This also initializes the internal data member to a new <code>ExceptionData()</code>
     * instance.
     * </p>
     * <p>
     * <b>Changes to v2.0: </b> The internal data member is initialized to a new
     * <code>ExceptionData()</code>.
     * </p>
     *
     * @param message Useful message containing a description of why the runtime
     *            exception was thrown - may be null.
     * @param cause The initial throwable reason which triggered this runtime
     *            exception to be thrown - may be null.
     * @since 1.0
     */
    public BaseRuntimeException(String message, Throwable cause) {
        this(message, cause, new ExceptionData());
    }

    /**
     * <p>
     * Constructor that takes only the additional <code>data</code> to attach
     * to the runtime exception.
     * </p>
     * <p>
     * This sets the data member to the given parameter. If the data parameter
     * is null, a new <code>ExceptionData()</code> is used instead.
     * </p>
     *
     * @param data The additional data to attach to the runtime exception - if
     *            this is null, a new <code>ExceptionData()</code> is used
     *            instead.
     * @since 2.0
     */
    public BaseRuntimeException(ExceptionData data) {
        super();
        // set data field to data parameter if it is not null
        // a new ExceptionData() is used if data parameter is null
        this.data = (data != null) ? data : new ExceptionData();
    }

    /**
     * <p>
     * Constructs the runtime exception taking both a <code>message</code> and
     * the additional <code>data</code> to attach to it.
     * </p>
     * <p>
     * This sets the data member to the given parameter. If the data parameter
     * is null, a new <code>ExceptionData()</code> is used instead.
     * </p>
     *
     * @param message Useful message containing a description of why the runtime
     *            exception was thrown - may be null.
     * @param data The additional data to attach to the runtime exception - if
     *            this is null, a new <code>ExceptionData()</code> is used
     *            instead.
     * @since 2.0
     */
    public BaseRuntimeException(String message, ExceptionData data) {
        super(message);
        // set data field to data parameter if it is not null
        // a new ExceptionData() is used if data parameter is null
        this.data = (data != null) ? data : new ExceptionData();
    }

    /**
     * <p>
     * Constructs the runtime exception taking an original <code>cause</code>,
     * as well as the additional <code>data</code> to attach to it.
     * </p>
     * <p>
     * This sets the data member to the given parameter. If the data parameter
     * is null, a new <code>ExceptionData()</code> is used instead.
     * </p>
     *
     * @param cause The initial throwable reason which triggered this runtime
     *            exception to be thrown - may be null.
     * @param data The additional data to attach to the runtime exception - if
     *            this is null, a new <code>ExceptionData()</code> is used
     *            instead.
     * @since 2.0
     */
    public BaseRuntimeException(Throwable cause, ExceptionData data) {
        super(cause);
        // set data field to data parameter if it is not null
        // a new ExceptionData() is used if data parameter is null
        this.data = (data != null) ? data : new ExceptionData();
    }

    /**
     * <p>
     * Constructs the runtime exception taking both a <code>message</code> and
     * a <code>cause</code>, as well as the additional <code>data</code> to
     * attach to it.
     * </p>
     * <p>
     * This sets the data member to the given parameter. If the data parameter
     * is null, a new <code>ExceptionData()</code> is used instead.
     * </p>
     *
     * @param message Useful message containing a description of why the runtime
     *            exception was thrown - may be null.
     * @param cause The initial throwable reason which triggered this runtime
     *            exception to be thrown - may be null.
     * @param data The additional data to attach to the runtime exception - if
     *            this is null, a new <code>ExceptionData()</code> is used
     *            instead.
     * @since 2.0
     */
    public BaseRuntimeException(String message, Throwable cause, ExceptionData data) {
        super(message, cause);
        // set data field to data parameter if it is not null
        // a new ExceptionData() is used if data parameter is null
        this.data = (data != null) ? data : new ExceptionData();
    }

    /**
     * <p>
     * Returns a piece of information attached to the runtime exception with the
     * given <code>key</code>.
     * </p>
     *
     * @param key The key used to locate the information being received - may be
     *            null.
     * @return The value associated to the key, or null if not found.
     * @since 2.0
     */
    public Object getInformation(Object key) {
        // delegates to the data member to get the information related to the
        // key
        return data.getInformation(key);
    }

    /**
     * <p>
     * Sets a certain <code>value</code> for the runtime exception, given a
     * <code>key</code> to identify it.
     * </p>
     *
     * @param key The key whose value is to be set - may be null.
     * @param value The value to associate to the key - may be null.
     * @since 2.0
     */
    public void setInformation(Object key, Object value) {
        // delegates to the data member to add a (key,value) pair
        data.setInformation(key, value);
    }

    /**
     * <p>
     * Returns whether the runtime exception instance has been logged.
     * </p>
     *
     * @return The value of the stored logged flag, indicating whether the
     *         runtime exception has been logged.
     * @since 2.0
     */
    public boolean isLogged() {
        // delegates to the data member to get the logged flag
        return data.isLogged();
    }

    /**
     * <p>
     * Sets the logged flag on the runtime exception to the parameter.
     * </p>
     *
     * @param logged whether or not the runtime exception has been logged
     * @since 2.0
     */
    public void setLogged(boolean logged) {
        // delegates to the data member to set the logged flag
        data.setLogged(logged);
    }

    /**
     * <p>
     * Returns the application code attached to the runtime exception.
     * </p>
     *
     * @return The application code attached to the runtime exception - this may
     *         be null.
     * @since 2.0
     */
    public String getApplicationCode() {
        // delegates to the data member to get the application code
        return data.getApplicationCode();
    }

    /**
     * <p>
     * Returns the module code attached to the runtime exception.
     * </p>
     *
     * @return The module code attached to the runtime exception - this may be
     *         null.
     * @since 2.0
     */
    public String getModuleCode() {
        // delegates to the data member to get the module code
        return data.getModuleCode();
    }

    /**
     * <p>
     * Returns the error code attached to the runtime exception.
     * </p>
     *
     * @return The error code attached to the runtime exception - this may be
     *         null.
     * @since 2.0
     */
    public String getErrorCode() {
        // delegates to the data member to get the error code
        return data.getErrorCode();
    }

    /**
     * <p>
     * Returns the <code>Date</code> instance corresponding to the date and
     * time when the runtime exception was initialized - will not be null.
     * </p>
     *
     * @return the date and time the runtime exception was first created.
     * @since 2.0
     */
    public Date getCreationDate() {
        // delegates to the data member to get the date and time
        return data.getCreationDate();
    }

    /**
     * <p>
     * Returns the name of the thread which the runtime exception was raised in -
     * will not be null.
     * </p>
     *
     * @return the name of the thread throwing the runtime exception.
     * @since 2.0
     */
    public String getThreadName() {
        // delegates to the data member to get the thread name
        return data.getThreadName();
    }

}
