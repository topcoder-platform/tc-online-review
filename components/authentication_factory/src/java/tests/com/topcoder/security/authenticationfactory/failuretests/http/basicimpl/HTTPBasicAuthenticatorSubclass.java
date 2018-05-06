/*
 * Copyright (C) 2005, TopCoder Inc. All rights reserved.
 */
package com.topcoder.security.authenticationfactory.failuretests.http.basicimpl;

import com.topcoder.security.authenticationfactory.AuthenticateException;
import com.topcoder.security.authenticationfactory.ConfigurationException;
import com.topcoder.security.authenticationfactory.Principal;
import com.topcoder.security.authenticationfactory.Response;
import com.topcoder.security.authenticationfactory.http.basicimpl.HTTPBasicAuthenticator;

/**
 * <p>A subclass of <code>HTTPBasicAuthenticator</code> class to be used to test the protected methods of the super
 * class. Overrides the protected methods declared by a super-class. The overridden methods are declared with package
 * private access so only the failure test cases could invoke them. The overridden methods simply call the corresponding
 * method of a super-class.
 *
 * @author isv
 * @version 1.0
 * @since Authentication Factory 2.0
 */
public class HTTPBasicAuthenticatorSubclass extends HTTPBasicAuthenticator {

    /**
     * <p>Constructs new <code>HTTPBasicAuthenticatorSubclass</code> instance. Nothing special occurs here.</p>
     *
     * @param namespace to load configuration values.
     */
    public HTTPBasicAuthenticatorSubclass(String namespace) throws ConfigurationException {
        super(namespace);
    }

    /**
     * <p>Calls corresponding method of a super class providing the specified argument. Nothing special occurs here.</p>
     *
     * @param principal to authenticate.
     */
    public Response doAuthenticate(Principal principal) throws AuthenticateException {
        return super.doAuthenticate(principal);
    }
}
