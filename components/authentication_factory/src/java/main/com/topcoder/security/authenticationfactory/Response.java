/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.security.authenticationfactory;

/**
 * <p>
 * Response class represents the authentication result returned from the Authenticator's authenticate method. It
 * contains a successful flag indicating the authentication succeeded or failed, an optional authentication message, and
 * an details object. The message could be possibly the error message or error code specified in the authentication, and
 * more detailed information should be assign to the details property. For example, in the HttpBasicAuthenticator, the
 * details would be of HttpResource type. Specific authenticator implementation should always design an interface for
 * the object assigned to details property, so that we don't have to modify the application when switching to another
 * authenticator of the same kind.
 * </p>
 *
 * <p>
 * This class is thread-safe, since it is immutable - NOTE, it is authenticator's responsibility to keep the details
 * object thread-safe.
 * </p>
 *
 * @author TCSDESIGNER, TCSDEVELOPER
 * @version 2.0
 */
public class Response {
    /**
     * <p>
     * Represents the authentication succeeded or not.
     * </p>
     */
    private boolean successful = false;

    /**
     * <p>
     * Represents the response message specfied in the authenticator.
     * </p>
     */
    private String message = null;

    /**
     * <p>
     * Represents the detailed response information.
     * </p>
     */
    private Object details = null;

    /**
     * <p>
     * Create a Response instance with the successful flag. Other parameter of Response will be left
     * uninitialized.
     * </p>
     *
     * @param successful indicating the authentication succeeded or not.
     */
    public Response(boolean successful) {
        this.successful = successful;
    }

    /**
     * <p>
     * Create a Response with the successful and message.Other parameter of Response will be left
     * uninitialized.
     * </p>
     *
     * @param successful indicating the authentication succeeded or not.
     * @param message the response message specfied in the authenticator
     *
     * @throws NullPointerException if message is null.
     * @throws IllegalArgumentException if message is empty string.
     */
    public Response(boolean successful, String message) {
        this(successful);
        if (message == null) {
            throw new NullPointerException("message is null.");
        }

        if (message.trim().length() == 0) {
            throw new IllegalArgumentException("message is a empty String.");
        }

        this.message = message;
    }

    /**
     * <p>
     * Create a Response instance with the successful flag, message and the details object.
     * Other parameter of Response will be left uninitialized.
     * </p>
     *
     * @param successful indicating the authentication succeeded or not.
     * @param message the response message specfied in the authenticator.
     * @param details the detailed response information.
     *
     * @throws NullPointerException if either arguments is null.
     * @throws IllegalArgumentException if message is empty string.
     */
    public Response(boolean successful, String message, Object details) {
        this(successful, message);
        if (details == null) {
            throw new NullPointerException("details is null.");
        }

        this.details = details;
    }

    /**
     * <p>
     * Create a Response instance with successful flag and details object. Other parameter
     * of Response will be left uninitialized.
     * </p>
     *
     * @param successful indicating the authentication succeeded or not.
     * @param details the detailed response information.
     *
     * @throws NullPointerException if details is null.
     */
    public Response(boolean successful, Object details) {
        this(successful);
        if (details == null) {
            throw new NullPointerException("details is null.");
        }

        this.details = details;
    }

    /**
     * <p>
     * Return a boolean indicating the authentication succeeded or not.
     * </p>
     *
     * @return true if the authentication succeeded, false otherwise.
     */
    public boolean isSuccessful() {
        return successful;
    }

    /**
     * <p>
     * Return the response message of the authentication.
     * </p>
     *
     * @return the response message of the authentication, null is possible.
     */
    public String getMessage() {
        return message;
    }

    /**
     * <p>
     * Return the detailed information of authentication.
     * </p>
     *
     * @return the detailed information of authentication, null is possible.
     */
    public Object getDetails() {
        return details;
    }
}