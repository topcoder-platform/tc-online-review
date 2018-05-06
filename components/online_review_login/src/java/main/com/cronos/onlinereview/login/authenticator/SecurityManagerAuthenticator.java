/*
 * Copyright (C) 2006-2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.login.authenticator;

import com.cronos.onlinereview.login.ConfigurationException;
import com.cronos.onlinereview.login.Util;

import com.topcoder.security.GeneralSecurityException;
import com.topcoder.security.TCSubject;
import com.topcoder.security.authenticationfactory.AbstractAuthenticator;
import com.topcoder.security.authenticationfactory.AuthenticateException;
import com.topcoder.security.authenticationfactory.InvalidPrincipalException;
import com.topcoder.security.authenticationfactory.MissingPrincipalKeyException;
import com.topcoder.security.authenticationfactory.Principal;
import com.topcoder.security.authenticationfactory.Response;
import com.topcoder.security.login.AuthenticationException;
import com.topcoder.security.login.LoginRemote;
import com.topcoder.security.login.LoginRemoteHome;

import java.rmi.RemoteException;

import javax.ejb.CreateException;

/**
 * This class is used to authenticate user.
 * <p>
 * It utilizes TCS Security Manager Component to do the real authentication. So the authentication work is only a
 * delegation to the <code>LoginBean</code> in Security Manager Component. The login bean will be retrieved by the bean
 * name via TCS JNDI Context Component.
 * </p>
 * <p>
 * This class derives from <code>AbstractAuthenticator</code>. So it can be plugged into TCS Authentication Factory
 * Component.
 * </p>
 * <p>
 * This class is thread safe since it does not contain any mutable inner state.
 * </p>
 *
 * @author woodjohn, maone, isv, TCSASSEMBLER
 * @version 2.0
 */
public class SecurityManagerAuthenticator extends AbstractAuthenticator {

    /**
     * The remote home instance for the <code>LoginBean</code>. It's used to create the remote LoginBean to authenticate
     * the user in the authenticate method.
     * <p>
     * This variable is set in the constructor, non-null.
     * </p>
     */
    private final LoginRemoteHome loginRemoteHome;

    /**
     * Create the instance from given namespace.
     * <p>
     * It will use <em>context_name</em> property to get <code>Context</code> from JNDIUtils. If <em>context_name</em>
     * is not configured, default context from JNDIUtils will be used. And login remote home will be retrieved from the
     * context via <em>login_bean_name</em> property. For more details about configuration. please see the Component
     * Specification.
     * </p>
     *
     * @param namespace
     *            the namespace to create the instance
     * @throws IllegalArgumentException
     *             if namespace is null or empty
     * @throws ConfigurationException
     *             if any other error occurred.
     * @throws com.topcoder.security.authenticationfactory.ConfigurationException
     *             if the super constructor fails.
     */
    public SecurityManagerAuthenticator(String namespace)
            throws ConfigurationException, com.topcoder.security.authenticationfactory.ConfigurationException {

        super(Util.validateNotNullOrEmpty(namespace, "namespace"));

        // retrieve the login remote home class name from the configuration
        try {
            String homeInterfaceClassName = Util.getRequiredPropertyString(namespace, "login_bean_name");
            Object namedObject = Class.forName(homeInterfaceClassName).newInstance();
            if (!LoginRemoteHome.class.isInstance(namedObject)) {
                throw new ConfigurationException("The named object is not instanceof " + LoginRemoteHome.class);
            }

            this.loginRemoteHome = (LoginRemoteHome) namedObject;
        } catch (ClassNotFoundException e) {
            throw new ConfigurationException("Cannot get loginRemoteHome.", e);
        } catch (InstantiationException e) {
            throw new ConfigurationException("Cannot get loginRemoteHome.", e);
        } catch (IllegalAccessException e) {
            throw new ConfigurationException("Cannot get loginRemoteHome.", e);
        }

    }

    /**
     * Authenticate the principal.
     * <p>
     * It utilizes TCS Security Manager Component to do the real authentication. So the authentication work is only a
     * delegation to the <code>LoginBean</code> in Security Manager Component.
     * </p>
     * <p>
     * The <em>userName</em> and <em>password</em> value contained in the given principal will be used to authenticate
     * the user. If authentication successes, a successful <code>Response</code> containing a <code>TCSubject</code> for
     * the user will be returned. Otherwise, a failed <code>Response</code> will be returned.
     * </p>
     *
     * @param principal
     *            the principal to authenticate (expected to be non-null)
     * @return the authentication response
     * @throws MissingPrincipalKeyException
     *             if userName or password is missing
     * @throws InvalidPrincipalException
     *             if userName or password is not of type string, or empty string
     * @throws AuthenticateException
     *             if any other error occurred
     */
    protected Response doAuthenticate(Principal principal) throws AuthenticateException {
        try {

            // retrieve user name and password from principal
            String userName = getPrincipalValue(principal, Util.USERNAME);
            String password = getPrincipalValue(principal, Util.PASSWORD);

            // try to log in with user name and password.
            LoginRemote loginRemote = (LoginRemote) loginRemoteHome.create();
            TCSubject tcSubject = loginRemote.login(userName, password);

            return new Response(true, "Succeeded", tcSubject);
        } catch (AuthenticationException e) {
            return new Response(false, e.getMessage());
        } catch (CreateException e) {
            throw new AuthenticateException("Can't create loginRemote instance.", e);
        } catch (GeneralSecurityException e) {
            throw new AuthenticateException("Can't login the user.", e);
        } catch (RemoteException e) {
            throw new AuthenticateException("Can't login the user.", e);
        }
    }

    /**
     * Get value for the given <code>key</code> from the <code>principal</code>.
     *
     * @param principal
     *            the Principal to retrieve value from
     * @param key
     *            the key associated with the required value
     * @return the String value
     * @throws MissingPrincipalKeyException
     *             if there is no value associated with the key
     * @throws InvalidPrincipalException
     *             if the value is not type of string, or empty
     */
    private static String getPrincipalValue(Principal principal, String key) {
        Object value = principal.getValue(key);

        if (value == null) {
            throw new MissingPrincipalKeyException(key);
        }

        if (!(value instanceof String) || ((String) value).trim().length() == 0) {
            throw new InvalidPrincipalException(key + " should be non-empty String.");
        }

        return (String) value;
    }
}
