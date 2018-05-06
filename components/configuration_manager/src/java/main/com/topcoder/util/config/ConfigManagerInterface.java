/*
 * Copyright (C) 2003, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.config;

import java.util.Enumeration;

/**
 * <p>
 * This is an interface that all classes which deal with the ConfigManager (including ConfigManager itself) should
 * implement.
 * </p>
 *
 * <p>
 * <em>Changes in 2.2:</em>
 * <ol>
 * <li>Added a generic parameter to the return type of method <code>getConfigPropNames()</code>.</li>
 * <li>Added thread safety information.</li>
 * </ol>
 * </p>
 *
 * <p>
 * <strong>Thread Safety: </strong> Implementations of this interface are not required to be thread safe.
 * </p>
 *
 * @author ilya, isv, WishingBone, saarixx, sparemax
 * @version 2.2
 */
public interface ConfigManagerInterface {

    /**
     * Gets the namespace for this component.
     *
     * @return namespace for this component.
     */
    String getNamespace();

    /**
     * <p>
     * Gets all known property keys for this Component.
     * </p>
     *
     * <p>
     * <em>Changes in 2.2:</em>
     * <ol>
     * <li>Added a generic parameter to the return type.</li>
     * </ol>
     * </p>
     *
     * @return all known property keys for this Component.
     */
    Enumeration<String> getConfigPropNames();

}
