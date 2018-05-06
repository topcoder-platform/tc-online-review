/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.latetracker;

import com.topcoder.configuration.ConfigurationObject;

/**
 * <p>
 * This interface should be extended by interfaces and implemented by classes that can be
 * configured from Configuration API object. It's assumed that
 * {@link #configure(ConfigurationObject)} method defined in this interface will be called
 * just once for each instance.
 * </p>
 * <p>
 * Thread Safety: Implementations of this interface are not required to be thread safe.
 * </p>
 *
 * @author saarixx, myxgyy
 * @version 1.0
 */
public interface Configurable {
    /**
     * Configures this instance with use of the given configuration object.
     *
     * @param config
     *            the configuration object.
     * @throws IllegalArgumentException
     *             if <code>config</code> is <code>null</code>.
     * @throws LateDeliverablesTrackerConfigurationException
     *             if some error occurred when initializing an instance using the given
     *             configuration.
     */
    public void configure(ConfigurationObject config);
}
