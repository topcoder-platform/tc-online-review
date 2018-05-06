/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.config;

import java.util.List;
import java.util.Properties;

/**
 * <p>
 * A mock implementation of <code>PluggableConfigSource</code>. Used for testing.
 * </p>
 *
 * @author sparemax
 * @version 2.2
 * @since 2.2
 */
public class MockPluggableConfigSource2 implements PluggableConfigSource {
    /**
     * <p>
     * Constructs a new <code>MockPluggableConfigSource2</code> instance.
     * </p>
     *
     * @param prop
     *            the configuration parameters.
     */
    public MockPluggableConfigSource2(Properties prop) {
        // Empty
    }

    /**
     * Configure this source with initial parameters from given <code>Properties
     * </code>. This method is immediately invoked by <code>
     * PluggableConfigProperties</code>
     * after instantiating the <code>
     * PluggableConfigSource</code> object.
     *
     * @param props
     *            a <code>Properties</code> containing parameters to configure this source
     */
    public void configure(Properties props) {
        // Empty
    }

    /**
     * Saves properties and their values starting from given "root"-property to underlying persistent storage. After
     * completing this operation this source of configuration properties should contain only properties from
     * properties "tree" specified by given <code>root</code>.
     *
     * @param root
     *            a "root"-property that is an entry-point to whole properties hierarchy tree
     */
    public void save(Property root) {
        // Empty
    }

    /**
     * <p>
     * Gets the list of properties contained within this source of configuration data.
     * </p>
     *
     * @return a <code>List</code> of properties
     */
    public List<Property> getProperties() {
        return null;
    }
}
