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
 * DefaultKeyConverter class implements the PrincipalKeyConverter interface, it is able to load the
 * key mappings from the configuration file via the Configuration Manager component.
 * </p>
 *
 * <p>
 * This class is thread-safe since it is immutable.
 * </p>
 *
 * @author TCSDESIGNER, TCSDEVELOPER
 * @version 2.0
 */
public class DefaultKeyConverter implements PrincipalKeyConverter {
    /**
     * <p>
     * The property to get the key mappings. This is optional.
     * </p>
     */
    private static final String MAPPINGS = "mappings";

    /**
     * <p>
     * Represents the key mappings, the key of the map is authenticator's key, and the value of the
     * map is principal's key.
     * </p>
     */
    private Map mappings = null;

    /**
     * <p>
     * Create a DefaultKeyConverter instance with the given namespace.
     * </p>
     *
     * @param namespace the namespace to load key mappings.
     *
     * @throws NullPointerException if the namespace is null.
     * @throws IllegalArgumentException if the namespace is empty string.
     * @throws ConfigurationException if failed to mappings from the configuration manager.
     */
    public DefaultKeyConverter(String namespace) throws ConfigurationException {
        // check parameter
        if (namespace == null) {
            throw new NullPointerException("namespace is null");
        }
        if (namespace.trim().length() == 0) {
            throw new IllegalArgumentException("namespace is empty string");
        }

        mappings = new HashMap();
        try {
            ConfigManager cm = ConfigManager.getInstance();
            Property py = cm.getPropertyObject(namespace, MAPPINGS);

            // if MAPPINGS is null, just return, coz this property is optional
            if (py == null) {
                return;
            }

            List properties = py.list();
            for (int i = 0; i < properties.size(); ++i) {
                Property sub = (Property) properties.get(i);

                String name = sub.getName();
                // check if name is a empty string
                if (name.trim().length() == 0) {
                    throw new ConfigurationException("name property is empty string");
                }

                String value = sub.getValue();
                // check if value is emptry string
                if (value.trim().length() == 0) {
                    throw new ConfigurationException("value of name property is empty string");
                }

                mappings.put(name, value);
            }
        } catch (UnknownNamespaceException une) {
            throw new ConfigurationException("the namespace " + namespace + " is unknown", une);
        }
    }

    /**
     * <p>
     * Return the converted principal's key from the given authenticator's key.
     * If no corresponding value found, will return key value.
     * </p>
     *
     * @param key the authenticator's key.
     * @return the converted principal's key.
     *
     * @throws NullPointerException
     *             if the key is null.
     * @throws IllegalArgumentException
     *             if the key is emtpy string.
     */
    public String convert(String key) {
        if (key == null) {
            throw new NullPointerException("key is null.");
        }
        if (key.trim().length() == 0) {
            throw new IllegalArgumentException("key is empty string.");
        }
        String value = (String) mappings.get(key);

        // if given key exist in mappings, return the corresponding value,
        // otherwise, return the key unchanged
        return value != null ? value : key;
    }
}