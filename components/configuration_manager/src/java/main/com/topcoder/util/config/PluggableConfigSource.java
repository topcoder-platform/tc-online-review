/*
 * Copyright (C) 2003, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.config;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * <p>
 * A pluggable source of configuration data. An implementations of this interface should have a public non-argument
 * constructor and should support their configuration with <code>Properties</code>.
 * </p>
 *
 * <p>
 * <em>Changes in 2.2:</em>
 * <ol>
 * <li>Added a generic parameter to the return type of method <code>getProperties()</code>.</li>
 * <li>Added thread safety information.</li>
 * </ol>
 * </p>
 *
 * <p>
 * <strong>Thread Safety: </strong> Implementations of this interface are not required to be thread safe.
 * </p>
 *
 * @author isv, WishingBone, saarixx, sparemax
 * @version 2.2
 * @since Configuration Manager 2.1
 */
public interface PluggableConfigSource {

    /**
     * Configure this source with initial parameters from given <code>Properties
     * </code>. This method is immediately invoked by <code>
     * PluggableConfigProperties</code>
     * after instantiating the <code>
     * PluggableConfigSource</code> object.
     *
     * @param props
     *            a <code>Properties</code> containing parameters to configure this source
     * @throws ConfigParserException
     *             if any exception related to underlying data source occurs
     * @throws NullPointerException
     *             if given <code>props</code> is <code>null
     *         </code>
     */
    void configure(Properties props) throws ConfigParserException;

    /**
     * Saves properties and their values starting from given "root"-property to underlying persistent storage. After
     * completing this operation this source of configuration properties should contain only properties from
     * properties "tree" specified by given <code>root</code>.
     *
     * @param root
     *            a "root"-property that is an entry-point to whole properties hierarchy tree
     * @throws IOException
     *             if any exception disabling successful saving of properties data occurs
     */
    void save(Property root) throws IOException;

    /**
     * <p>
     * Gets the list of properties contained within this source of configuration data.
     * </p>
     *
     * <p>
     * <em>Changes in 2.2:</em>
     * <ol>
     * <li>Added a generic parameter to the return type.</li>
     * </ol>
     * </p>
     *
     * @return a <code>List</code> of properties
     * @throws ConfigParserException
     *             if any exception related to underlying data source occurs
     */
    List<Property> getProperties() throws ConfigParserException;

}
