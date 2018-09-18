/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions.login;

import com.cronos.onlinereview.login.AuthCookieManagementException;
import com.cronos.onlinereview.login.AuthResponseParsingException;
import com.cronos.onlinereview.login.Util;

import com.topcoder.security.authenticationfactory.AuthenticateException;
import com.topcoder.security.authenticationfactory.InvalidPrincipalException;
import com.topcoder.security.authenticationfactory.MissingPrincipalKeyException;
import com.topcoder.security.authenticationfactory.Principal;
import com.topcoder.security.authenticationfactory.Response;
import com.topcoder.security.TCSubject;


/**
 * <code>LogintAction</code> class defines the login function for Online Review Login component.
 * <p>
 * <b>Thread Safety:</b>Struts 2 Action objects are instantiated for each request, so there are no thread-safety issues.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class LoginAction extends BaseLoginAction {

    /**
     * Represents the key of the action error.
     */
    public static final String ACTION_ERROR_MISS_PRINCIPALKEY =
    	    "exception.com.cronos.onlinereview.login.LoginActions.login.MissingPrincipalKeyException";

    /**
     * Represents the key of the action error.
     */
    public static final String ACTION_ERROR_INVAlID_PRINCIPAL =
    	    "exception.com.cronos.onlinereview.login.LoginActions.login.InvalidPrincipalException";

    /**
     * Represents the key of the action error.
     */
    public static final String ACTION_ERROR_AUTHENTICATE =
    	    "exception.com.cronos.onlinereview.login.LoginActions.login.AuthenticateException";

    /**
     * Represents the key of the action error.
     */
    public static final String ACTION_ERROR_AUTH_RESPONSE_PARSER =
    	    "exception.com.cronos.onlinereview.login.LoginActions.login.AuthResponseParserException";

    /**
     * Represents the username field of the login form.
     */
    private String userName;

    /**
     * Represents the password field of the login form.
     */
    private String password;

    /**
     * Represents the remember me option of the login form.
     */
    private String rememberMe;

    /**
     * Represents the forward url when the login succeed.
     */
    private String forwardUrl;

    /**
     * Represents the post back of the login form.
     */
    private String postBack;

    /**
     * This method tries to log in the user.
     * <p>
     * It will first use the authenticator to authenticate the user name and password extracted from the form. If
     * authentication succeeded, it will set the login state and forward the request and response to
     * <code>success</code> page. If failed, it will forward the request and response to the <code>failure</code> page.
     * <p>
     * NOTE: If any exception occurs in it, resource key will be added to the ActionMessages, which will be saved. If
     * logger is configured, it will be employed to log any thrown exception stacktrace.
     * </p>
     *
     * @return the forward result based on the authentication response
     *
     * @throws MissingPrincipalKeyException
     *             if any principal key is missing.
     * @throws InvalidPrincipalException
     *             if principal is not accepted by authenticator.
     * @throws AuthenticateException
     *             if any other error occurred during authentication.
     * @throws AuthResponseParsingException
     *             if authResponseParser failed to set the login state.
     * @throws AuthCookieManagementException
     *             if some error occurred when setting the authentication cookie
     */
    public String execute() throws AuthenticateException, AuthResponseParsingException, AuthCookieManagementException {
        try {
            if (postBack == null) {
                return INPUT;
            }

            // create a principal with user name and password
            Principal principal = new Principal("dummy");

            principal.addMapping(Util.USERNAME, userName);
            principal.addMapping(Util.PASSWORD, password);

            // authenticate the user
            Response authResponse = authenticator.authenticate(principal);

            authResponseParser.setLoginState(principal, authResponse, request, response);

            if (authResponse.isSuccessful()) {
                // Save the cookie with user details if Remember Me feature is requested. But if impersonated login
                // is used then do not persist such cookie and erase existing one
                boolean isImpersonationUsed = (userName.indexOf("/") >= 0);
                long userId = ((TCSubject)authResponse.getDetails()).getUserId();

                if (isImpersonationUsed) {
                    this.ssoCookieService.setSSOCookie(response, userId, false);
                } else {
                    // Set auth cookie with cookie manager
                    this.ssoCookieService.setSSOCookie(response, userId, "on".equals(rememberMe));
                }

                request.getSession().removeAttribute("redirectBackUrl");

                if ((forwardUrl != null) && (forwardUrl.trim().length() != 0) && (forwardUrl.indexOf("/login") < 0)) {
                    this.setUrl(forwardUrl);

                    return "dynamic";
                } else {
                    return SUCCESS;
                }
            } else {
                // Actually in case of wrong login attempt, an attribute should be place into the request
                // This is to let use the same page in the application for the first login
                // and for every subsequent incorrect login attempt
                this.addActionError(authResponse.getMessage());
                return "failure";
            }
        } catch (MissingPrincipalKeyException e) {
            recordException(e);
            addActionError(getText(ACTION_ERROR_MISS_PRINCIPALKEY));

            throw e;
        } catch (InvalidPrincipalException e) {
            recordException(e);
            addActionError(getText(ACTION_ERROR_INVAlID_PRINCIPAL));

            throw e;
        } catch (AuthenticateException e) {
            recordException(e);
            addActionError(getText(ACTION_ERROR_AUTHENTICATE));

            throw e;
        } catch (AuthResponseParsingException e) {
            recordException(e);
            addActionError(getText(ACTION_ERROR_AUTH_RESPONSE_PARSER));

            throw e;
        } catch (AuthCookieManagementException e) {
            recordException(e);
            addActionError(e.getLocalizedMessage());
            throw e;
        } catch (ClassCastException e) {
            recordException(e);
            addActionError(e.getLocalizedMessage());
            
            throw e;
        } catch (Exception e) {
            recordException(e);
            addActionError(e.getLocalizedMessage());
            
            throw new AuthCookieManagementException("SSO Cookie Service error.", e);
        }
    }

    /**
     * Validate the login form before the login action.
     * Add the action error if the user inputs are not valid.
     */
    public void validate() {
        if (postBack == null) {
            return;
        }

        if ((userName == null) || (userName.trim().length() == 0)) {
            addActionError(getText("error.com.cronos.onlinereview.login.username"));
        }

        if ((password == null) || (password.trim().length() == 0)) {
            addActionError(getText("error.com.cronos.onlinereview.login.password"));
        }
    }

    /**
     * Getter of userName.
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Setter of userName.
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Getter of password.
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Setter of password.
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Getter of rememberMe.
     * @return the rememberMe
     */
    public String getRememberMe() {
        return rememberMe;
    }

    /**
     * Setter of rememberMe.
     * @param rememberMe the rememberMe to set
     */
    public void setRememberMe(String rememberMe) {
        this.rememberMe = rememberMe;
    }

    /**
     * Getter of forwardUrl.
     * @return the forwardUrl
     */
    public String getForwardUrl() {
        return forwardUrl;
    }

    /**
     * Setter of forwardUrl.
     * @param forwardUrl the forwardUrl to set
     */
    public void setForwardUrl(String forwardUrl) {
        this.forwardUrl = forwardUrl;
    }

    /**
     * Getter of postBack.
     * @return the postBack
     */
    public String getPostBack() {
        return postBack;
    }

    /**
     * Setter of postBack.
     * @param postBack the postBack to set
     */
    public void setPostBack(String postBack) {
        this.postBack = postBack;
    }
}
