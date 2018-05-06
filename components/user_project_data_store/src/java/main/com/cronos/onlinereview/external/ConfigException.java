/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.external;

/**
 * <p>
 * This exception represents a problem with the configuration in the <b>User Project Data Store</b> component.
 * </p>
 * <p>
 * It is thrown by the <code>{@link UserRetrieval}</code> and <code>{@link ProjectRetrieval}</code> interfaces (and
 * their implementing and base classes.) It can be used with a message, and with or without an underlying cause.
 * </p>
 *
 * @author dplass, oodinary
 * @author FireIce
 * @version 1.1
 * @since 1.0
 */
public class ConfigException extends UserProjectDataStoreException {

    /**
     * <p>
     * Creates this exception with the given message.
     * </p>
     *
     * @param message
     *            The message of this exception. May be null or empty after trim.
     */
    public ConfigException(String message) {
        super(message);
    }

    /**
     * <p>
     * Creates this exception with the given message and underlying cause.
     * </p>
     *
     * @param message
     *            The message of this exception. May be null or empty after trim.
     * @param cause
     *            the underlying cause of the exception.
     */
    public ConfigException(String message, Throwable cause) {
        super(message, cause);
    }
}
