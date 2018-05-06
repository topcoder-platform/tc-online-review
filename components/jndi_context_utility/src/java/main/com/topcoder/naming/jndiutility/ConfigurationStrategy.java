/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 *
 * TCS Project JNDI Context Utility 2.0
 *
 * @ ConfigurationStrategy.java
 */
package com.topcoder.naming.jndiutility;

/**
 * <p>This is a generic interface for simple configuration functionality that is used by this component. We
 * basically have the ability to fetch a named property value and we have the ability to write/overwrite a specific
 * property and then commit it to the configuration medium.</p>
 *  <p><strong>Thread-Safety:</strong></p>
 *  <p>Implementations as NOT expected to be thread safe.</p>
 *
 * @author AleaActaEst, Charizard
 * @version 2.0
 */
public interface ConfigurationStrategy {
    /**
     * <p>This method will fetch the string property for the given name. It will return null if nothing is
     * found.</p>
     *
     * @param name name of the property to fetch
     *
     * @return value of the configured property or <code>null</code> if not found
     *
     * @throws IllegalArgumentException if <code>name</code> is <code>null</code> or empty
     * @throws ConfigurationException if error occurs during the actual process of fetching the property
     */
    public String getProperty(String name) throws ConfigurationException;

    /**
     * <p>This method will write the property with the given name and value into the configuration medium. If
     * property with the given name does not exist a new one will be created. Note that the changes will not be
     * permanent until the <code>commitChanges()</code> method is called, but uncommitted changes will be returned by
     * <code>getProperty(String name)</code>.</p>
     *
     * @param name property name
     * @param value property value
     *
     * @throws IllegalArgumentException if either <code>name</code> or <code>value</code> is <code>null</code> or empty
     * @throws ConfigurationException if error occurs during the actual process of setting the property
     */
    public void setProperty(String name, String value)
        throws ConfigurationException;

    /**
     * <p>Commits any changes recently done to the data back to the configuration.</p>
     *
     * @throws ConfigurationException if error occurs during the actual process of committing the properties
     */
    public void commitChanges() throws ConfigurationException;
}
