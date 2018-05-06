/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.errorhandling;

/**
 * <p>
 * <code>CustomException</code> is a custom exception which extends
 * <code>BaseNonCriticalException</code>.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 2.0
 */
public class CustomException extends BaseNonCriticalException {
    /**
     * Represents the application code.
     */
    public static final String APPCODE = "UserHandler";

    /**
     * Represents the module code.
     */
    public static final String MODCODE = "MY_MODULE";

    /**
     * Represents the error code.
     */
    public static final String ERRCODE = "e1421_m";

    /**
     * <p>
     * Constructs a new <code>CustomException</code> with the given
     * <code>message</code>.
     * </p>
     *
     * @param message Useful message containing a description of why the
     *            exception was thrown
     */
    public CustomException(String message) {
        // call generic constructor
        this(message, null, new ExceptionData());
    }

    /**
     * <p>
     * Constructs a new <code>CustomException</code> with the given
     * <code>message</code> and <code>cause</code>.
     * </p>
     *
     * @param message Useful message containing a description of why the
     *            exception was thrown
     * @param cause The initial throwable reason which triggered this exception
     *            to be thrown
     */
    public CustomException(String message, Throwable cause) {
        // call generic constructor
        this(message, cause, new ExceptionData());
    }

    /**
     * <p>
     * Constructs a new <code>CustomException</code> with the given
     * <code>message</code> and <code>cause</code>, as well as the
     * additional <code>data</code> to attach to the custom exception.
     * </p>
     *
     * @param message Useful message containing a description of why the
     *            exception was thrown
     * @param cause The initial throwable reason which triggered this exception
     *            to be thrown
     * @param data the additional data to attach to the exception
     */
    public CustomException(String message, Throwable cause, ExceptionData data) {
        // call super constructor after setting the app, module and error codes
        super(message, cause, getData(data));
    }

    /**
     * <p>
     * A private static method used to get the <code>ExceptionData</code>
     * instance after setting the app, module and error codes. If
     * <code>data</code> is null, a new <code>ExceptionData()</code> is
     * returned.
     * </p>
     *
     * @param data the <code>ExceptionData</code> instance to be set
     * @return the data after setting its app, module and error codes, or a new
     *         <code>ExceptionData()</code> if parameter data is null
     */
    private static ExceptionData getData(ExceptionData data) {
        if (data == null) {
            return new ExceptionData();
        } else {
            return data.setApplicationCode(APPCODE).setModuleCode(MODCODE).setErrorCode(ERRCODE);
        }
    }
}
