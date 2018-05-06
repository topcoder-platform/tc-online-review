/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.date.workdays;

import com.topcoder.util.errorhandling.BaseCriticalException;
import com.topcoder.util.errorhandling.ExceptionData;

/**
 * <p>
 * This exception is thrown if anything goes wrong in the process of loading the configuration file. So this exception
 * is thrown::
 * </p>
 *
 * <p>
 * -if the file doesn't exist;
 * </p>
 *
 * <p>
 * -if the file can't be read : SecurityManager.checkRead(java.lang.String);
 * </p>
 *
 * <p>
 * -if the configuration file is not well formed: the configuration manager throws an exception, or any other abnormal
 * situation occurs;
 * </p>
 *
 * <p>
 * -if the values in the configuration file are not in the required format (dates, hours, minutes)
 * </p>
 *
 * <p>
 * <strong> Thread Safety:</strong> This class is not thread safe because its base class is not thread safe.
 *
 * <p>
 * <strong> Change log:</strong> Made this class extend BaseCriticalException instead of BaseException. Added
 * constructors that are required according to the current TopCoder standards
 * </p>
 *
 * @author TCSDESIGNER, TCSDEVELOPER
 * @version 1.1
 */
public class ConfigurationFileException extends BaseCriticalException {
    /**
     * <p>
     * Creates a new instance of this exception with the given message.
     * </p>
     *
     * @param message
     *            the detailed error message of this exception.
     *
     * @since 1.1
     */
    public ConfigurationFileException(String message) {
        super(message);
    }

    /**
     * <p>
     * Constructor of a ConfigurationFileException.
     * </p>
     *
     * <p>
     * The ConfigurationFileException constructor is passed the exception that caused it and a short descriptive
     * message.
     * </p>
     *
     * @param message
     *            a short descriptive message.
     * @param cause
     *            the exception that caused this exception.
     */
    public ConfigurationFileException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * <p>
     * Creates a new instance of this exception with the given message and exception data.
     * </p>
     *
     * @param message
     *            the detailed error message of this exception.
     * @param data
     *            the exception data.
     *
     * @since 1.1
     */
    public ConfigurationFileException(String message, ExceptionData data) {
        super(message, data);
    }

    /**
     * <p>
     * Creates a new instance of this exception with the given message, cause and exception data.
     * </p>
     *
     * @param message
     *            the detailed error message of this exception.
     * @param cause
     *            the inner cause of this exception.
     * @param data
     *            the exception data.
     *
     * @since 1.1
     */
    public ConfigurationFileException(String message, Throwable cause, ExceptionData data) {
        super(message, cause, data);
    }
}
