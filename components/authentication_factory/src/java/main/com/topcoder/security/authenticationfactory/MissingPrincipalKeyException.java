/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.security.authenticationfactory;

/**
 * <p>
 * MissingPrincipalKeyException is thrown from the Authenticator's authenticate method if certain
 * key is missing in the principal, which is required for the authentication. For example, the
 * username property is missing in NTLM authentication.
 * </p>
 *
 * @author TCSDESIGNER, TCSDEVELOPER
 * @version 2.0
 */
public class MissingPrincipalKeyException extends InvalidPrincipalException {
    /**
     * <p>
     * Represents the missing key of the principal, it can be retrieved from
     * the property getter.
     * </p>
     */
    private String key = null;

    /**
     * <p>
     * Create a MissingPrincipalKeyException instance with the missing key.
     * </p>
     *
     * @param key the missing key in the principal.
     *
     * @throws NullPointerException if the key is null.
     * @throws IllegalArgumentException if the key is empty string.
     */
    public MissingPrincipalKeyException(String key) {
        super("key " + key + " is missing");
        if (key == null) {
            throw new NullPointerException("the key is null.");
        }
        if (key.trim().length() == 0) {
            throw new IllegalArgumentException("the key is empty string.");
        }
        this.key = key;
    }

    /**
     * <p>
     * Return the missing key of principal.
     * </p>
     *
     * @return the missing key of principal.
     */
    public String getKey() {
        return key;
    }
}