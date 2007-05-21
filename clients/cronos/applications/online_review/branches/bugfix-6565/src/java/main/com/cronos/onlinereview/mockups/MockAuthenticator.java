/*
 * Copyright (C) 2006 TopCoder Inc.  All Rights Reserved.
 */
package com.cronos.onlinereview.mockups;

import com.cronos.onlinereview.external.ExternalUser;
import com.cronos.onlinereview.external.UserRetrieval;
import com.cronos.onlinereview.external.impl.DBUserRetrieval;

import com.topcoder.security.TCSubject;
import com.topcoder.security.authenticationfactory.AbstractAuthenticator;
import com.topcoder.security.authenticationfactory.AuthenticateException;
import com.topcoder.security.authenticationfactory.ConfigurationException;
import com.topcoder.security.authenticationfactory.Principal;
import com.topcoder.security.authenticationfactory.Response;
import com.topcoder.util.errorhandling.BaseException;

/**
 * A mock implementation of <code>AbstractAuthenticator</code>.
 * <p>
 * It is implemented for testing purpose.
 * </p>
 *
 * @author George1
 * @author real_vg
 * @version 1.0
 */
public class MockAuthenticator extends AbstractAuthenticator {

    /**
     * Creates <code>AbstractAuthenticator</code> concrete instance for test.
     *
     * @param namespace
     *            passed to super class.
     * @throws ConfigurationException
     *             from super class.
     */
    public MockAuthenticator(String namespace) throws ConfigurationException {
        super(namespace);
    }

    /**
     * The actual authenticate method.
     *
     * @return response.
     * @param principal
     *            the principal instance.
     *
     * @throws AuthenticateException
     *             if any error occurs.
     */
    protected Response doAuthenticate(Principal principal) throws AuthenticateException {
        String userName = (String) principal.getValue("userName");
        String password = (String) principal.getValue("password");

        if (!userName.equalsIgnoreCase(password)) {
            return new Response(false, "Failed");
        }

        try {
            UserRetrieval ur = new DBUserRetrieval("com.topcoder.db.connectionfactory.DBConnectionFactoryImpl");

            ExternalUser user = ur.retrieveUser(userName);
            if (user != null) {
                return new Response(true, "Succeeded", new TCSubject(user.getId()));
            } else {
                return new Response(false, "Failed");
            }
        } catch (BaseException e) {
            throw new AuthenticateException("Unable to connect to user store", e);
        }
    }
}
