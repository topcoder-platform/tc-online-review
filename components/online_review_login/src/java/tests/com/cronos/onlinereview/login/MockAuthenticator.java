/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */



package com.cronos.onlinereview.login;

import com.topcoder.security.TCSubject;
import com.topcoder.security.authenticationfactory.AbstractAuthenticator;
import com.topcoder.security.authenticationfactory.AuthenticateException;
import com.topcoder.security.authenticationfactory.ConfigurationException;
import com.topcoder.security.authenticationfactory.InvalidPrincipalException;
import com.topcoder.security.authenticationfactory.MissingPrincipalKeyException;
import com.topcoder.security.authenticationfactory.Principal;
import com.topcoder.security.authenticationfactory.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * A mock implementation of <code>AbstractAuthenticator</code>.
 * <p>
 * It is implemented for testing purpose. It can throw different exceptions according to the given user name.
 * </p>
 * <p>
 * In this class, the user/password is prestored in Map userStore. When the login user/password is coming, it will first
 * look up the password in the userStore map. If the password gotten from userStore equals to the password from login,
 * return success response, otherwise return false response.
 * </p>
 *
 * @author maone
 * @version 1.0
 */
public class MockAuthenticator extends AbstractAuthenticator {

    /**
     * The userStore store the user/password properties for Authenticator. It is created in the constructor and can
     * never be changed after than.
     */
    private Map userStore = new HashMap();

    /**
     * Create AbstractAuthenticator concrete instance for test.
     *
     * @param namespace
     *            passed to super class
     * @throws ConfigurationException
     *             from super class
     */
    public MockAuthenticator(String namespace) throws ConfigurationException {
        super(namespace);

        for (int i = 0; i < 1000; i++) {
            userStore.put(new String("userName" + (i + 1)), new String("password" + (i + 1)));
        }
    }

    /**
     * The actual authenticate method.
     * <p>
     * Authentication can be successful if the userName is myname and password is mypw.
     * </p>
     * <p>
     * When the login user/password is coming, it will first look up the password in the userStore map.
     * </p>
     * <p>
     * If the password gotten from userStore equals to the password from login, return success response, otherwise
     * return false response.
     * </p>
     *
     * @param principal
     *            the principal instance
     * @return response
     * @throws AuthenticateException
     *             if userName contains character <code>@</code>
     * @throws InvalidPrincipalException
     *             if userName contains character <code>$</code>.
     * @throws MissingPrincipalKeyException
     *             if userName contains character <code>#</code>.
     */
    protected Response doAuthenticate(Principal principal) throws AuthenticateException {
        String userName = (String) principal.getValue("userName");
        String password = (String) principal.getValue("password");

        String psw = (String) userStore.get(userName);

        if (userName.equals("myname") && password.equals("mypw")) {
            return new Response(true, "Succeeded", new TCSubject(1234567));
        }

        if (password.equalsIgnoreCase(psw)) {
            return new Response(true, "Succeeded", new TCSubject(1234567));
        }

        if (userName.indexOf('@') >= 0) {
            throw new AuthenticateException("msg");
        }

        if (userName.indexOf('$') >= 0) {
            throw new InvalidPrincipalException("msg");
        }

        if (userName.indexOf('#') >= 0) {
            throw new MissingPrincipalKeyException("key");
        }

        return new Response(false, "Failed");
    }
}
