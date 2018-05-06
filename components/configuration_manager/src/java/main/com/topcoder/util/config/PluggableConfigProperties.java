/*
 * Copyright (C) 2003, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.config;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * <p>
 * An extension of <code>ConfigProperties</code> providing the ability to use pluggable sources of configuration
 * properties.
 * </p>
 *
 * <p>
 * <em>Changes in 2.2:</em>
 * <ol>
 * <li>Made save() and load() methods synchronized.</li>
 * <li>Added support for "IsRefreshable" configuration parameter.</li>
 * </ol>
 * </p>
 *
 * <p>
 * <strong>Thread Safety: </strong> This class is mutable, but partially thread safe since its save() and load()
 * methods are synchronized. I.e. its save() and load() methods can be safely called from multiple threads at a time.
 * </p>
 *
 * @author isv, WishingBone, saarixx, sparemax
 * @version 2.2
 * @since Configuration Manager 2.1
 */
class PluggableConfigProperties extends ConfigProperties {

    /**
     * An underlying source of configuration data elements.
     */
    private PluggableConfigSource source = null;

    /**
     * A private no-arg constructor (for clone).
     */
    private PluggableConfigProperties() {
        // Empty
    }

    /**
     * <p>
     * Constructs a new instance of <code>PluggableConfigProperties</code> interacting with
     * <code>PluggableConfigSource</code> described by properties containing within given file. Loads the
     * <code>Properties
     * </code> object from given <code>file</code>, instantiates and configure <code>PluggableConfigSource</code>
     * object with created <code>Properties
     * </code>.
     * </p>
     *
     * <p>
     * <em>Changes in 2.2:</em>
     * <ol>
     * <li>Changed visibility to package.</li>
     * </ol>
     * </p>
     *
     * @param file
     *            a file with definition and initial properties of <code>
     *         PluggableConfigSouirce</code>
     * @throws ConfigManagerException
     *             if any exception preventing from successful loading properties from given file, instantiation of
     *             <code>PluggableConfigSource</code> occurs
     * @throws IOException
     *             if any exception related to underlying persistent storage occurs
     */
    PluggableConfigProperties(URL file) throws IOException {
        // load properties
        Properties properties = Helper.loadProperties(file);

        source = Helper.createObj(PluggableConfigSource.class, properties, "classname");
        // configure
        source.configure(properties);
        load();
    }

    /**
     * <p>
     * Saves the data(properties and their values) from properties tree into
     * persistent storage.
     * </p>
     *
     * <p>
     * <em>Changes in 2.2:</em>
     * <ol>
     * <li>Made this method synchronized.</li>
     * <li>Saving refreshable property to the source.</li>
     * </ol>
     * </p>
     *
     * @throws IOException if any exception related to underlying persistent
     *         storage occurs
     */
    @Override
    protected synchronized void save() throws IOException {
        Property rootProperty = getRoot();

        // NEW in 2.2
        Boolean refreshable = isRefreshable();
        if (refreshable != null) {
            rootProperty.addProperty(Helper.KEY_REFRESHABLE, refreshable.toString());
        }

        source.save(rootProperty);

        if (refreshable != null) {
            rootProperty.removeProperty(Helper.KEY_REFRESHABLE); // NEW in 2.2
        }
    }

    /**
     * <p>
     * Loads the properties and their values from persistent storage.
     * </p>
     *
     * <p>
     * <em>Changes in 2.2:</em>
     * <ol>
     * <li>Made this method synchronized.</li>
     * <li>Loading refreshable property from the source.</li>
     * </ol>
     * </p>
     *
     * @throws IOException
     *             if any exception related to underlying persistent storage occurs
     * @throws ConfigParserException
     *             if any exception related to underlying data source occurs
     */
    @Override
    protected synchronized void load() throws IOException {
        Boolean refreshable = null;

        List<Property> list = source.getProperties();
        Property root = new Property();
        for (Iterator<Property> itr = list.iterator(); itr.hasNext();) {
            Property property = itr.next();

            // NEW in 2.2
            if (Helper.KEY_REFRESHABLE.equals(property.getName())) {
                String propertyValue = property.getValue();
                if (propertyValue == null) {
                    throw new ConfigParserException("The property value cannot be null.");
                }
                refreshable = Boolean.parseBoolean(propertyValue);
            } else {
                root.addProperty(property);
            }
        }
        setRoot(root);

        // Set refreshable flag
        setRefreshable(refreshable);
    }

    /**
     * <p>
     * Gets the clone copy of this object.
     * </p>
     *
     * <p>
     * <em>Changes in 2.2:</em>
     * <ol>
     * <li>Setting refreshable property.</li>
     * </ol>
     * </p>
     *
     * @return a clone copy of this object
     */
    @Override
    public Object clone() {
        PluggableConfigProperties properties = new PluggableConfigProperties();
        properties.source = source;
        properties.setRoot((Property) getRoot().clone());

        properties.setRefreshable(isRefreshable()); // NEW in 2.2

        return properties;
    }

}
