/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.security.authenticationfactory;

import com.topcoder.util.errorhandling.BaseException;


/**
 * <p>
 * ConfigurationException could be thrown if any class fails to load the configuration values from
 * the configuration file, or the loaded configuration values are invalid.
 * </p>
 *
 * @author TCSDESIGNER, TCSDEVELOPER
 * @version 2.0
 */
public class ConfigurationException extends BaseException {
    /**
     * <p>
     * Create ConfigurationException with error message.
     * </p>
     *
     * @param message the error message.
     */
    public ConfigurationException(String message) {
        super(message);
    }

    /**
     * <p>
     * Create ConfigurationException with error message and inner exception.
     * </p>
     *
     * @param message the error message.
     * @param cause the inner cause.
     */
    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
