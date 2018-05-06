package com.topcoder.security.authenticationfactory.accuracytests;

import com.topcoder.security.authenticationfactory.ConfigurationException;
import com.topcoder.security.authenticationfactory.http.basicimpl.HTTPBasicAuthenticator;

import java.util.Map;


/**
 * A sub class of HTTPBasicAuthenticator for testing purpose.
 */
public class SubHTTPBasicAuthenticator extends HTTPBasicAuthenticator {
    /**
     * Create a HttpBasicAuthenticator with the given namespace.
     *
     * @param namespace the namespace to load configuration values.
     * @throws ConfigurationException if the namespace is null.
     */
    public SubHTTPBasicAuthenticator(String namespace)
        throws ConfigurationException {
        super(namespace);
    }

    /**
     * Get the default mapping for testing.
     *
     * @return The default mappings.
     */
    public Map getDefaultMappings() {
        return defaultMappings;
    }
}
