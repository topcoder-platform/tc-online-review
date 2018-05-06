/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.security.authenticationfactory;

/**
 * <p>
 * Authenticator interface defines the contract to authenticate a principal, which holds the
 * necessary information for specific authentication implementation, and return a response
 * indicating the authentication is successful or failed. In current release,
 * AbstractAuthenticator implements this interface to provide some common functionalities that can
 * be shared among all implementations - such as caching authentication response etc.
 * </p>
 * <p>
 * This interface expect user provide a configuration namespace using Java reflection technology to
 * construct itself.
 * </p>
 *
 * <p>All implementation of this interface are expected to thread safe.</p>
 *
 * @author TCSDESIGNER, TCSDEVELOPER
 * @version 2.0
 */
public interface Authenticator {
    /**
     * <p>
     * Authenticate a principal, which holds the necessary information for specific authentication
     * implementation,  and return a response indicating the authentication is successful or
     * failed.
     * </p>
     *
     * @param principal the principal to authenticate.
     * @return the authentication response.
     *
     * @throws NullPointerException if the given principal is null.
     * @throws MissingPrincipalKeyException if certain key is missing in the given principal.
     * @throws InvalidPrincipalException if the principal is invalid, e.g. the type of a certain key's
     *         value is invalid.
     * @throws AuthenticateException if error occurs during the authentication.
     */
    public Response authenticate(Principal principal) throws AuthenticateException;
}