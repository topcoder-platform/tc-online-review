/*
 * Copyright (C) 2003, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.config;

import java.io.IOException;

/**
 * <p>
 * This abstract class is the superclass of all classes representing the different sources of configuration
 * properties. While this class maintains the properties data it's subclasses are responsible for interacting with
 * underlying persistent storage to save and load properties data.
 * </p>
 *
 * <p>
 * <em>Changes in 2.2:</em>
 * <ol>
 * <li>Added refreshable property.</li>
 * <li>Made this class implement Cloneable (method clone() has been already present).</li>
 * <li>Added thread safety information.</li>
 * </ol>
 * </p>
 *
 * <p>
 * <strong>Thread Safety: </strong> Thread Safety: This class is mutable and not thread safe. It's subclasses should
 * provide synchronized save() and load() methods. Fields root and refreshable are made volatile so that they can be
 * accessed from multiple threads.
 * </p>
 *
 * @author isv, WishingBone, saarixx, sparemax
 * @version 2.2
 */
abstract class ConfigProperties implements Cloneable {
    /**
     * <p>
     * A "root" property that is an instance of <code>Property</code> class and serves as an entry point to whole
     * properties hierarchy tree. This "root" property manages all the properties contained within namespace. All
     * properties that are in namespace are nested within this "root" property.
     * </p>
     *
     * <p>
     * Such "root" property is provided to simplify the interface provided by ConfigProperties. Instead of
     * ConfigProperties having a methods to add, remove, find the properties it just provide a getRoot() method that
     * returns a "root" property that then can be used to perform any operations on a namespace's properties.
     * </p>
     *
     * <p>
     * <em>Changes in 2.2:</em>
     * <ol>
     * <li>Made volatile to avoid thread safety issues.</li>
     * </ol>
     * </p>
     *
     * @since Configuration Manager 2.1
     */
    private volatile Property root = null;

    /**
     * The list delimiter associated with the ConfigProperties.
     * ';' is used as the default delimiter.
     */
    private char listDelimiter = Helper.DEFAULT_LIST_DELIMITER;

    /**
     * <p>
     * The value indicating whether configuration properties are refreshable.
     * </p>
     *
     * <p>
     * Refreshable means that configuration should be reloaded from persistence if the user asks to do so. Is null if
     * generic parameters must be used. Has getter and setter.
     * </p>
     *
     * @since 2.2
     */
    private volatile Boolean refreshable = null;

    /**
     * Constructs an instance of <code>ConfigProperties</code>.
     */
    ConfigProperties() {
        // Empty
    }

    /**
     * Gets the "root" property that is an entry point to whole properties
     * hierarchy tree.
     *
     * @return a "root" property
     */
    Property getRoot() {
        return root;
    }

    /**
     * Gets the list delimiter.
     *
     * @return the list delimiter.
     */
    char getListDelimiter() {
        return listDelimiter;
    }

    /**
     * Saves the data(properties and their values) from properties tree into
     * persistent storage.
     *
     * @throws IOException if any exception related to underlying persistent
     *         storage occurs
     */
    protected abstract void save() throws IOException;

    /**
     * Loads the properties and their values from persistent storage.
     *
     * @throws IOException if any exception related to underlying persistent
     *         storage occurs
     */
    protected abstract void load() throws IOException;

    /**
     * Sets the "root" property.
     *
     * @param  root a new "root" property
     * @throws NullPointerException if given <code>root</code> is <code>null
     *         </code>
     */
    protected void setRoot(Property root) {
        if (root == null) {
            throw new NullPointerException("parameter root is null");
        }
        this.root = root;
    }

    /**
     * Sets the list delimiter.
     *
     * @param  listDelimiter the new list delimiter
     */
    protected void setListDelimiter(char listDelimiter) {
        this.listDelimiter = listDelimiter;
    }

    /**
     * Gets the clone copy of this object.
     *
     * @return a clone copy of this object.
     */
    public abstract Object clone();

    /**
     * <p>
     * Retrieves the value indicating whether configuration properties are refreshable.
     * </p>
     *
     * @return the value indicating whether configuration properties are refreshable.
     *
     * @since 2.2
     */
    Boolean isRefreshable() {
        return refreshable;
    }

    /**
     * <p>
     * Sets the value indicating whether configuration properties are refreshable.
     * </p>
     *
     * @param refreshable
     *            the value indicating whether configuration properties are refreshable.
     *
     * @since 2.2
     */
    void setRefreshable(Boolean refreshable) {
        this.refreshable = refreshable;
    }
}
