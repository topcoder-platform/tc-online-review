/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 *
 * TCS Project JNDI Context Utility 2.0
 *
 * @ ConfigurationManagerConfigurationStrategy.java
 */
package com.topcoder.naming.jndiutility.configstrategy;

import com.topcoder.naming.jndiutility.ConfigurationException;
import com.topcoder.naming.jndiutility.ConfigurationStrategy;
import com.topcoder.naming.jndiutility.Helper;

import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.config.ConfigManagerException;
import com.topcoder.util.config.UnknownNamespaceException;


/**
 * <p>This is a specific implementation of the <code>ConfigurationStrategy</code> interface contract for dealing
 * with <code>ConfigManager</code>.</p>
 *  <p>Here we will be able to read/write and commit string properties.</p>
 *  <p><strong>Thread-Safety</strong></p>
 *  <p>Implementation is NOT thread-safe.</p>
 *
 * @author AleaActaEst, Charizard
 * @version 2.0
 */
public class ConfigurationManagerConfigurationStrategy implements ConfigurationStrategy {
    /** Name of user used when committing changes. */
    private static final String USER = "JNDIUtil";

    /**
     * <p>This represents a specific configuration namespace that will be used by this strategy helper. This is
     * initialized in the constructor and is immutable thereafter. It is also not visible to external processes.</p>
     */
    private final String namespace;

    /**
     * <p>This is a simple constructor which takes a namespace string and saves it in the field.</p>
     *
     * @param namespace configuration namespace
     *
     * @throws IllegalArgumentException if <code>namespace</code> is <code>null</code> or empty
     */
    public ConfigurationManagerConfigurationStrategy(String namespace) {
        Helper.checkString(namespace, "namespace");
        this.namespace = namespace;
    }

    /**
     * <p>This method will fetch the string property for the given name. It will return <code>null</code> if
     * nothing is found.</p>
     *
     * @param name name of the property to fetch
     *
     * @return value of the configured property or <code>null</code> if not found
     *
     * @throws IllegalArgumentException if <code>name</code> is <code>null</code> or empty
     * @throws ConfigurationException if error occurs during the actual process of fetching the property
     */
    public String getProperty(String name) throws ConfigurationException {
        Helper.checkString(name, "name");

        ConfigManager cm = ConfigManager.getInstance();

        try {
            // first try to return temporary property
            return cm.getTemporaryString(namespace, name);
        } catch (UnknownNamespaceException e) {
            try {
                // no temporary property exist
                return cm.getString(namespace, name);
            } catch (UnknownNamespaceException ee) {
                throw new ConfigurationException("error occurred during fetching property", ee);
            }
        }
    }

    /**
     * <p>This method will write the property with the given name and value into the
     * <code>ConfigManager</code>. If property with the given name does not exist a new one will be created. Note that
     * the changes will not be permanent until the <code>commitChanges()</code> method is called, but uncommitted
     * changes will be returned by <code>getProperty(String name)</code>.</p>
     *
     * @param name property name
     * @param value property value
     *
     * @throws IllegalArgumentException if either <code>name</code> or <code>value</code> is <code>null</code> or empty
     * @throws ConfigurationException if error occurs during the actual process of setting the property
     */
    public void setProperty(String name, String value)
        throws ConfigurationException {
        Helper.checkString(name, "name");
        Helper.checkString(value, "value");

        ConfigManager cm = ConfigManager.getInstance();

        try {
            cm.setProperty(namespace, name, value);
        } catch (UnknownNamespaceException e) {
            try {
                // no temporary property exist, create for the namespace
                cm.createTemporaryProperties(namespace);
                cm.setProperty(namespace, name, value);
            } catch (UnknownNamespaceException ee) {
                throw new ConfigurationException("error occurred during setting property", ee);
            }
        }
    }

    /**
     * <p>Commits any changes recently done to the data back to the <code>ConfigManager</code>.</p>
     *
     * @throws ConfigurationException if error occurs during the actual process of committing the properties
     */
    public void commitChanges() throws ConfigurationException {
        try {
            ConfigManager.getInstance().commit(namespace, USER);
        } catch (UnknownNamespaceException e) {
            // no change at all, ignore
        } catch (ConfigManagerException e) {
            throw new ConfigurationException("error occurred during committing changes", e);
        }
    }
}
