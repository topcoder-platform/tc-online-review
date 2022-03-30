/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions.login;

import com.cronos.onlinereview.login.AuthCookieManagementException;
import com.cronos.onlinereview.login.AuthResponseParsingException;


/**
 * <code>LogoutAction</code> class defines the logout function for Online Review Login component.
 * <p>
 * <b>Thread Safety:</b>Struts 2 Action objects are instantiated for each request, so there are no thread-safety issues.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class LogoutAction extends BaseLoginAction {

    /**
     * Represents the key of the action error.
     */
    public static final String ACTION_ERROR_AUTH_RESPONSE_PARSER =
    	    "exception.com.cronos.onlinereview.login.LoginActions.logout.AuthResponseParserException";

    /**
     * This method tries to log the user out.
     * <p>
     * It will use <code>AuthResponseParser</code> to unset the login state. If the operation succeeds, it will forward
     * to <em>logout</em>.
     * </p>
     *
     * @return "logout" string.
     * @throws AuthResponseParsingException
     *             if authResponseParser failed to unset the login state.
     * @throws AuthCookieManagementException
     *             if some error occurred when removing the authentication cookie
     */
    public String execute() throws AuthResponseParsingException, AuthCookieManagementException {
        try {

            // Remove auth cookie with the manager
            ssoCookieService.removeSSOCookie(response);

            // unset login state
            authResponseParser.unsetLoginState(request, response);

            // forward to logout
            return "logout";
        } catch (AuthResponseParsingException e) {
            recordException(e);
            addActionError(getText(ACTION_ERROR_AUTH_RESPONSE_PARSER));

            throw e;
        } catch (Exception e) {
            recordException(e);
            addActionError(e.getLocalizedMessage());

            throw new AuthCookieManagementException("SSO Cookied Service error.", e);
        }
    }
}

