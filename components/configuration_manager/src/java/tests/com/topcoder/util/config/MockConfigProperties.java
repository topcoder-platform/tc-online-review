/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.config;

/**
 * <p>
 * A mock implementation of ConfigProperties. Used for testing.
 * </p>
 *
 * @author sparemax
 * @version 2.2
 * @since 2.2
 */
public class MockConfigProperties extends ConfigProperties {
    /**
     * Constructs an instance of <code>MockConfigProperties</code>.
     */
    public MockConfigProperties() {
        // Empty
    }

    /**
     * Saves the data(properties and their values) from properties tree into persistent storage.
     */
    protected void save() {
        // Empty
    }

    /**
     * Loads the properties and their values from persistent storage.
     */
    protected void load() {
        // Empty
    }

    /**
     * Gets the clone copy of this object.
     *
     * @return null.
     */
    public Object clone() {
        return null;
    }
}
