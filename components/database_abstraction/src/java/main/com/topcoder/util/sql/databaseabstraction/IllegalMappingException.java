/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.sql.databaseabstraction;

/**
 * <p>
 * The IllegalMappingException is thrown when a Converter or OnDemandConverter
 * is not able to make a conversion for an implementation defined reason.
 * </p>
 * <p>
 * This exception is thrown by implementations of the Converter and
 * OnDemandConverter interfaces in the convert method, and propagates through
 * the OnDemandMapper and CustomResultSet classes.
 * </p>
 *
 * @author argolite, aubergineanode, WishingBone, justforplay
 * @version 1.1
 * @since 1.0
 */
@SuppressWarnings("serial")
public class IllegalMappingException extends Exception {

    /**
     * No argument constructor used in previous version.
     */
    public IllegalMappingException() {
    }

    /**
     * Creates a new IllegalMappingException.
     *
     * @param message
     *            Explanation of error
     * @since 1.1
     */
    public IllegalMappingException(String message) {
        super(message);
    }

    /**
     * Creates a new IllegalMappingException.
     *
     * @param message
     *            Explanation of error
     * @param cause
     *            Underlying cause of error
     * @since 1.1
     */
    public IllegalMappingException(String message, Exception cause) {
        super(message, cause);
    }
}
