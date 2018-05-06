package com.topcoder.security.authenticationfactory.stresstests;

import com.topcoder.security.authenticationfactory.AbstractAuthenticator;
import com.topcoder.security.authenticationfactory.ConfigurationException;
import com.topcoder.security.authenticationfactory.Principal;
import com.topcoder.security.authenticationfactory.Response;

public class DummyAuthenticator extends AbstractAuthenticator {
	
	public DummyAuthenticator(String namespace) throws ConfigurationException {
		super(namespace);
	}

	protected Response doAuthenticate(Principal principal) {
		return null;
	}

}
