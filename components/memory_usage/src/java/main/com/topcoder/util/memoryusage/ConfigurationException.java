/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.util.memoryusage;

import com.topcoder.util.errorhandling.BaseException;

/**
 * <p>This exception is raised while reading the component's configuration
 * from the configuration files. It can be due to an error from the
 * underlying ConfigManager, or to an error in the configuration
 * file itself.</p>
 *
 * <p><b>Thread safety:</b> This class is thread safe since it is immutable.</p>
 *
 * @author AleaActaEst, TexWiller
 * @version 2.0
 * @since 2.0
 */
public class ConfigurationException extends BaseException {

    /**
     * Constructor with no message, and no wrapped exception.
     */
    public ConfigurationException() {
        super();
    }

    /**
     * Constructor with message and no wrapped exception.
     *
     * @param message The message of the exception. Can be <code>null</code>
     * or empty.
     */
    public ConfigurationException(String message) {
        super(message);
    }

    /**
     * Constructor with message and wrapped exception.
     *
     * @param message The message of the exception. Can be <code>null</code>
     * or empty.
     * @param wrappedException The exception causing this exception. Can be
     * <code>null</code>
     */
    public ConfigurationException(String message, Throwable wrappedException) {
        super(message, wrappedException);
    }
}

