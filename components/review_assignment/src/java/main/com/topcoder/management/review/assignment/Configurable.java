/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.assignment;

import com.topcoder.configuration.ConfigurationObject;

/**
 * <p>
 * This interface should be extended by interfaces and implemented by classes that can be configured from
 * Configuration API object.
 * </p>
 * <p>
 * It's assumed that {@link #configure(ConfigurationObject)} method defined in this interface will be called
 * just once for each instance.
 * </p>
 * <p>
 * Thread Safety: Implementations of this interface are not required to be thread safe.
 * </p>
 *
 * @author gevak, zhongqiangzhang
 * @version 1.0
 */
public interface Configurable {
    /**
     * Configures this instance with use of the given configuration object.
     *
     * @param config
     *            the configuration object
     *
     * @throws IllegalArgumentException
     *             if config is null
     * @throws ReviewAssignmentConfigurationException
     *             if some error occurred when initializing an instance using the given configuration.
     */
    public void configure(ConfigurationObject config);
}
