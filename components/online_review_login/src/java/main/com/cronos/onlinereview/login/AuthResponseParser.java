/*
 * Copyright (C) 2006-2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.login;

import com.topcoder.security.authenticationfactory.Principal;
import com.topcoder.security.authenticationfactory.Response;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This interface defines the contract of keeping track of the user log state stored in the request or session.
 * <p>
 * In order to integrate other already existing <code>Authenticator</code> implementation into this component,
 * application user must also provide the corresponding implementation of this interface to manage the log states. The
 * <code>setLoginState</code> method is used to set the user log state (failed or succeeded, etc). The
 * <code>unsetLoginState</code> method is used to unset the user log state.
 * </p>
 * <p>
 * NOTE: The implementation of this interface is required to be thread safe. The constructor of the implementation must
 * take no argument or string namespace argument so that <code>LoginActions</code> can create it via reflection at the
 * construction time.
 * </p>
 *
 * @author woodjohn, maone, TCSASSEMBLER
 * @version 2.0
 */
public interface AuthResponseParser {

    /**
     * Set the login state on the request or session.
     * <p>
     * Note that null principal or authResponse may be allowed by particular implementation.
     * </p>
     *
     * @param principal
     *            the principal corresponding to the authentication response
     * @param authResponse
     *            the authentication response
     * @param request
     *            the http request
     * @param response
     *            the http response
     * @throws IllegalArgumentException
     *             if http request or response is null
     * @throws AuthResponseParsingException
     *             if any other error occurred
     */
    public void setLoginState(Principal principal, Response authResponse, HttpServletRequest request,
                              HttpServletResponse response)
            throws AuthResponseParsingException;

    /**
     * Unset the user login state.
     *
     * @param request
     *            the http request
     * @param response
     *            the http response
     * @throws IllegalArgumentException
     *             if http request or response is null
     * @throws AuthResponseParsingException
     *             if any other error occurred
     */
    public void unsetLoginState(HttpServletRequest request, HttpServletResponse response)
            throws AuthResponseParsingException;
}
