/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.security.authenticationfactory;

/**
 * <p>
 * PrincipalKeyConverter interface defines the contract to convert the keys used by the specific
 * authenticator implementation into those in the Principal. So if the we switch to another
 * authenticator, which might use a different collection of keys, we only have to  modify the
 * configuration file, rather than change the application code.
 * </p>
 *
 * <p>
 * This interface expect user provide a configuration namespace using Java reflection technology to
 * construct itself.
 * </p>
 *
 * <p>All implementation of this interface are expected to thread safe.</p>
 * @author TCSDESIGNER, TCSDEVELOPER
 * @version 2.0
 */
public interface PrincipalKeyConverter {
    /**
     * <p>
     * Return the corresponding key in Principal of the specified key.
     * If the key cannot be recognized by the converter, the key will be returned unchanged.
     * </p>
     *
     * @param key the key used in the authenticator implementation.
     * @return the corresponding key in the principal.
     *
     * @throws NullPointerException if the given key is null.
     * @throws IllegalArgumentException if the given key is empty string.
     */
    public String convert(String key);
}