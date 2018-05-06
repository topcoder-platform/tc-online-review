/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.processor.ipserver;

import com.topcoder.util.errorhandling.BaseException;


/**
 * <p>
 * The ConfigurationException is used to signal configuration related problems.
 * </p>
 *
 * <p>
 * It is used to wrap any exceptions related to the configuration data or that indicate a problem with the
 * configuration file: configuration manager exceptions indicating bad or missing file, reflection exceptions
 * indicating bad class names. It may also be used to indicate missing properties.
 * </p>
 *
 * <p>
 * The message passed in to one of the constructors should be something meaningful to help the user to find the
 * problem. In addition to the message, the actual exception (if any) is passed in as an argument of one of the
 * constructors.
 * </p>
 *
 * <p>
 * <em>Thread Safety: </em>this class has been made thread safe by being immutable.
 * </p>
 *
 * @author visualage, zsudraco
 * @version 1.0
 * @copyright Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
public class ConfigurationException extends BaseException {
    /**
     * <p>
     * Construct a new ConfigurationException with the given error message.
     * </p>
     *
     * <p>
     * The given message should be a string describing the error that caused this exception to be thrown.
     * </p>
     *
     * @param message the message describing the exception.
     */
    public ConfigurationException(String message) {
        super(message);
    }

    /**
     * <p>
     * Construct a new ConfigurationException with given error message and cause of the error.
     * </p>
     *
     * <p>
     * The given message should be a string describing the error that caused this exception to be thrown; the given
     * cause should be the actual cause of this exception.
     * </p>
     *
     * @param message the message describing the exception.
     * @param cause the cause of the exception.
     */
    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
