/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.security.authenticationfactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.config.Property;
import com.topcoder.util.config.UnknownNamespaceException;


/**
 * <p>
 * AuthenticationFactory class maintains a collection of Authenticator instances, which are created
 * from the configuration values via the Configuration Manager. Each Authenticator has a unique
 * name to identify it. AuthenticationFactory provides a method to retrieve the Authenticator by
 * its name. If the configuration values are changed, refresh can be called to recreate the
 * Authenticators, so that we don't have to restart the application to be ware of the change.
 * </p>
 *
 * <p>
 * This class is thread-safe.
 * </p>
 *
 * @author TCSDESIGNER, TCSDEVELOPER
 * @version 2.0
 */
public class AuthenticationFactory {
    /**
     * <p>
     * The namespace for AuthenticationFactory.
     * </p>
     */
    private static final String NAMESPACE = "com.topcoder.security.authenticationfactory.AuthenticationFactory";

    /**
     * <p>
     * The property name.
     * </p>
     */
    private static final String AUTHENTICATOR = "authenticators";

    /**
     * <p>
     * Represents the singleton AuthenticationFactory instance.
     * </p>
     */
    private static AuthenticationFactory instance = null;

    /**
     * <p>
     * Represents the authenticators created from the configuration file via Configuration Manager.
     * The key is the name of Authenticator, and the value is the created Authenticator instance.
     * It is initialized in the constructor, and re-populated in the refresh method.
     * </p>
     */
    private Map authenticators = null;

    /**
     * <p>
     * Create an AuthenticationFactory instance.
     * </p>
     *
     * @throws ConfigurationException if any problem occurs when loading the configuration
     *         values, or creating the Authenticators, such as the required properties are missing, etc.
     */
    private AuthenticationFactory() throws ConfigurationException {
        authenticators = new HashMap();
        refresh();
    }

    /**
     * <p>
     * Return the singleton instance of AuthenticationFactory.
     * </p>
     *
     * @return the singleton AuthenticationFactory instance.
     * @throws ConfigurationException if any problem occurs when loading the configuration
     *         values, or creating the Authenticators, such as the required properties are missing, etc.
     */
    public static synchronized AuthenticationFactory getInstance() throws ConfigurationException {
        if (instance == null) {
            instance = new AuthenticationFactory();
        }

        return instance;
    }

    /**
     * <p>
     * Return the corresponding Authenticator instance to the specified authenticator's name. Null
     * is returned if there is no corresponding Authenticator.
     * </p>
     *
     * @param authName the name of the Authenticator to return.
     * @return the corresponding Authenticator to the name, or null if not defined.
     *
     * @throws NullPointerException if the given authName is null.
     * @throws IllegalArgumentException if the given authName is empty string.
     */
    public Authenticator getAuthenticator(String authName) {
        if (authName == null) {
            throw new NullPointerException("authName is null");
        }
        if (authName.trim().length() == 0) {
            throw new IllegalArgumentException("authName is empty string");
        }

        synchronized (authenticators) {
            return (Authenticator) authenticators.get(authName);
        }
    }

    /**
     * <p>
     * Reload the configuration values to recreate the Authenticators. This method should be called
     * if the configuration is changed.
     *
     * @throws ConfigurationException if any problem occurs when loading the configuration
     *         values, or creating the Authenticators, such as the required properties are missing, etc.
     *         Refer to the comment in configuration file for more details.
     */
    public void refresh() throws ConfigurationException {
        synchronized (authenticators) {
            try {
                // make it empty
                authenticators.clear();
                ConfigManager cm = ConfigManager.getInstance();

                Property authPy = cm.getPropertyObject(NAMESPACE, AUTHENTICATOR);
                if (authPy == null) {
                    throw new ConfigurationException("required property {authenticators} is missing");
                }

                // get the list of all authenticators
                List properties = authPy.list();
                for (int i = 0; i < properties.size(); ++i) {
                    Property pt = (Property) properties.get(i);

                    String name = pt.getName();
                    // check if authenticator name is empty string
                    if (name.trim().length() == 0) {
                        throw new ConfigurationException("name property is empty string");
                    }

                    // create the authenticator according to py
                    Authenticator auth = (Authenticator) ConfigManagerUtil.createInstance(pt);

                    // put it to map
                    authenticators.put(name, auth);
                }
            } catch (UnknownNamespaceException une) {
                throw new ConfigurationException("namespace " + NAMESPACE + " is unknown", une);
            }
        }
    }
}