/*
 * Copyright (C) 2006-2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.ajax;

import com.topcoder.util.errorhandling.BaseException;

/**
 * <p>
 * Represents an exception detected during configuration.
 * This class extends the BaseException class.
 *
 * This exception is:
 * <ul>
 * <li>Thrown by all Ajax request handlers' constructors, if they fail to configure their internal state.</li>
 * <li>Thrown by AjaxSupportServlet class when it fails to load configuration data, and to create Ajax request handlers;
 *     AjaxSupportServlet's "init" method wraps this exception in a ServletException, and then thrown it.</li>
 * </ul>
 * </p>
 *
 * @author topgear
 * @author assistant
 * @author George1
 * @version 1.1
 */
public class ConfigurationException extends BaseException {

    /**
     * <p>
     * Creates a new instance of this exception class.
     * </p>
     */
    public ConfigurationException() {
        // do nothing
    }

    /**
     * <p>
     * Creates a new instance of this exception class with a descriptive message.
     * </p>
     * @param message a descriptive message about the exception
     */
    public ConfigurationException(String message) {
        super(message);
    }

    /**
     * <p>
     * Creates a new instance of this exception class with the cause of the exception.
     * </p>
     *
     * @param cause the cause of this exception
     */
    public ConfigurationException(Throwable cause) {
        super(cause);
    }

    /**
     * <p>
     * Creates a new instance of this exception class with a descriptive message, and the cause of the exception.
     * </p>
     *
     * @param message a descriptive message about the exception
     * @param cause the cause of this exception
     */
    public  ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
