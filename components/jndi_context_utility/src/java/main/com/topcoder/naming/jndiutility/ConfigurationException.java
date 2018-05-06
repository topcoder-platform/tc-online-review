/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 *
 * TCS Project JNDI Context Utility 2.0
 *
 * @ ConfigurationException.java
 */
package com.topcoder.naming.jndiutility;

import com.topcoder.util.errorhandling.BaseException;


/**
 * <p>This is a custom exception used to signal issues with configuration data. This could be issues with required
 * properties missing from configuration for example. Another example would be the fact that we had an I/O issue with
 * reading the actual configuration.</p>
 *
 * @author AleaActaEst, Charizard
 * @version 2.0
 */
public class ConfigurationException extends BaseException {
    /**
     * <p>This is a simple default exception which simply delegates to its parent.</p>
     */
    public ConfigurationException() {
        super();
    }

    /**
     * <p>This is a simple simple exception which accepts a message about the issue(s) encountered. This simply
     * delegates to its parent.</p>
     *
     * @param message exception message
     */
    public ConfigurationException(String message) {
        super(message);
    }

    /**
     * <p>This is a simple simple exception which accepts a message about the issue(s) encountered as well as
     * the exception that caused it. This simply delegates to its parent.</p>
     *
     * @param message exception message
     * @param cause inner cause
     */
    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
