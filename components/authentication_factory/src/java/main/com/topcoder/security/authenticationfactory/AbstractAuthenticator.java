/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.security.authenticationfactory;

import java.util.HashMap;
import java.util.Map;

import com.topcoder.util.cache.Cache;
import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.config.Property;
import com.topcoder.util.config.UnknownNamespaceException;

/**
 * <p>
 * AbstractAuthenticator abstract class implements Authenticator interface, and provides some
 * common functionalities that can be shared by specific authenticator implementations.
 * </p>
 *
 * <ol>
 * <li>
 * It defines default mappings, which is loaded from the configuration file via Configuration
 * Manager component, so that if certain mapping is missing in the authenticated Principal, the
 * default one can be used instead.
 * </li>
 * <li>
 * It provides a conversion from the specific authenticator's key to principal's key, so that if we
 * switch to another authenticator, we only have to update the conversion configuration.
 * </li>
 * <li>
 * It holds a cache to cache the response of the authenticated principal, so that we can return the
 * cached response to the same principal without do the time-consuming authentication repeatedly.
 * And the cache mechanism can be turned off / on in the configuration file.
 * </li>
 * </ol>
 *
 * <p>
 * Specific authenticator implementations are expected to override the protected doAuthenticate
 * method, and  must provide a constructor taking a namespace argument in order to be created in
 * AuthenticationFactory via Configuration Manager component. defaultMappings variable in this
 * abstract class is designed to be protected for convenience, the subclasses should NEVER modify
 * it outside the constructor to assure thread-safety.  This abstract class is thread-safe due to
 * its immutability, and subclasses should keep the contract.
 * </p>
 *
 * @author TCSDESIGNER, TCSDEVELOPER
 * @version 2.0
 */
public abstract class AbstractAuthenticator implements Authenticator {

    /**
     * <p>
     * Represents the &quot;principal_key_converter&quot; property.
     * </p>
     */
    private static final String CONVERTER = "principal_key_converter";

    /**
     * <p>
     * Represents the &quot;cache_factory&quot; property.
     * </p>
     */
    private static final String CACHE_FACTORY = "cache_factory";

    /**
     * <p>
     * Represents the default namespace of &quot;principal_key_converter&quot;.
     * </p>
     */
    private static final String DEFAULT_CONVERT = "com.topcoder.security.authenticationfactory.DefaultKeyConverter";

    /**
     * <p>
     * Represents the default mappings for the authenticator, it is initialized in the constructor
     * to empty map. Subclasses are repsonsible to populate it with configuration values defined
     * in the configuration file.  It should NEVER be modified outside the constructor.
     * </p>
     */
    protected Map defaultMappings = null;

    /**
     * <p>
     * Represents the PrincipalKeyConverter instance to convert from the authenticator's key to
     * principal's key.
     * </p>
     */
    private PrincipalKeyConverter converter = null;

    /**
     * <p>
     * Represents the cache to cache the response of the principal to authenticate.
     * It will use Pricinpal's id as key, and the authenticate Response as the value
     * in this cache.
     * </p>
     */
    private Cache cache = null;

    /**
     * <p>
     * Create an Authenticator instance from the given namespace.
     *
     * @param namespace the namespace to load configuration values.
     *
     * @throws NullPointerException if the namespace is null.
     * @throws IllegalArgumentException if the namespace is empty string.
     * @throws ConfigurationException if fail to load configuration values from the ConfigManager.
     */
    protected AbstractAuthenticator(String namespace) throws ConfigurationException {
        if (namespace == null) {
            throw new NullPointerException("namespace is null");
        }
        if (namespace.trim().length() == 0) {
            throw new IllegalArgumentException("namespace is empty string");
        }
        // initialize the defaultMappings to empty map
        defaultMappings = new HashMap();

        try {
            ConfigManager cm = ConfigManager.getInstance();

            Property py = cm.getPropertyObject(namespace, CONVERTER);
            if (py == null) {
                // if the principal_key_converter property is missing, create a
                // DefaultKeyConverter with the specified namespace.
                converter = new DefaultKeyConverter(namespace);
            } else {
                // create the converter with class and namespace
                // properties via reflection
                converter = (PrincipalKeyConverter) ConfigManagerUtil.createInstance(py);
            }

            py = cm.getPropertyObject(namespace, CACHE_FACTORY);
            if (py != null) {
                // if cach_factory property exist, create the CacheFactory with the class and
                // namespace properties via reflection
                cache = ((CacheFactory) ConfigManagerUtil.createInstance(py)).createCache();
            }
        } catch (UnknownNamespaceException une) {
            throw new ConfigurationException("namespace " + namespace + " is unknown", une);
        }
    }

    /**
     * <p>
     * Authenticate the specified Principal, and return the authentication response.
     * </p>
     *
     * @param principal the principal to authenticate.
     *
     * @return the authenticated response of the principal.
     * @throws NullPointerException if the given principal is null.
     * @throws MissingPrincipalKeyException if certain key is missing in the given principal.
     * @throws InvalidPrincipalException if the principal is invalid, e.g. the type of a certain key's
     *         value is invalid.
     * @throws AuthenticateException if error occurs during the authentication.
     */
    public Response authenticate(Principal principal) throws AuthenticateException {
        if (principal == null) {
            throw new NullPointerException("pricipal is null.");
        }

        Response response;
        if (cache != null) {
            // get it from cache, if not null return it
            response = (Response) cache.get(principal.getId());
            if (response != null) {
                return response;
            }
        }

        response = doAuthenticate(principal);
        if (cache != null) {
            // put it to cache if cache is not null
            cache.put(principal.getId(), response);
        }
        return response;
    }

    /**
     * <p>
     * Authenticate the given principal really.
     * </p>
     *
     * @param principal the principal to authenticate.
     * @return the authenticated response of the principal.
     *
     * @throws MissingPrincipalKeyException if certain key is missing in the given principal.
     * @throws InvalidPrincipalException if the principal is invalid, e.g. the type of a
     *         certain key's value is invalid.
     * @throws AuthenticateException if error occurs during the authentication
     */
    protected abstract Response doAuthenticate(Principal principal) throws AuthenticateException;

    /**
     * <p>
     * Return the PrincipalKeyConverter instance which is used by the subclasses to convert
     * authenticator's key to principal's key.
     * </p>
     *
     * @return the PrincipalKeyConverter instance.
     */
    protected PrincipalKeyConverter getConverter() {
        return converter;
    }
}
