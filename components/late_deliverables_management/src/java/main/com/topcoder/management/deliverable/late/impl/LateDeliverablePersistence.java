/*
 * Copyright (C) 2010-2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.late.impl;

import java.util.List;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.management.deliverable.late.LateDeliverable;
import com.topcoder.management.deliverable.late.LateDeliverableManagementConfigurationException;
import com.topcoder.management.deliverable.late.LateDeliverableType;

/**
 * <p>
 * This interface represents a late deliverable persistence. Currently it defines just a single method for updating
 * late deliverable in persistence and retrieving all late deliverable types from persistence.
 * </p>
 *
 * <p>
 * <em>Changes in version 1.0.6:</em>
 * <ol>
 * <li>Added getLateDeliverableTypes() method.</li>
 * <li>Updated throws documentation of update() method.</li>
 * <li>Updated class documentation.</li>
 * </ol>
 * </p>
 *
 * <p>
 * <strong>Thread Safety: </strong> Implementations of this interface are required to be thread safe when configure()
 * method is called just once right after instantiation and entities passed to them are used by the caller in thread
 * safe manner.
 * </p>
 *
 * @author saarixx, sparemax
 * @version 1.0.6
 */
public interface LateDeliverablePersistence {
    /**
     * <p>
     * Configures this instance with use of the given configuration object.
     * </p>
     *
     * @param configuration
     *            the configuration object.
     *
     * @throws IllegalArgumentException
     *             if configuration is null (is not thrown by implementations that don't use any configuration
     *             parameters).
     * @throws LateDeliverableManagementConfigurationException
     *             if some error occurred when initializing an instance using the given configuration (is not thrown
     *             by implementations that don't use any configuration parameters).
     */
    public void configure(ConfigurationObject configuration);

    /**
     * <p>
     * Updates the given late deliverable in persistence.
     * </p>
     *
     * <p>
     * <em>Changes in version 1.0.6:</em>
     * <ol>
     * <li>Updated throws documentation for IllegalArgumentException.</li>
     * </ol>
     * </p>
     *
     * @param lateDeliverable
     *            the late deliverable with updated data.
     *
     * @throws IllegalArgumentException
     *             if lateDeliverable is null, lateDeliverable.getId() &lt;= 0, lateDeliverable.getDeadline() is null,
     *             lateDeliverable.getCreateDate() is null, lateDeliverable.getType() is null,
     *             lateDeliverable.getType().getId() &lt;= 0.
     * @throws IllegalStateException
     *             if persistence was not configured properly.
     * @throws LateDeliverableNotFoundException
     *             if late deliverable with ID equal to lateDeliverable.getId() doesn't exist in persistence.
     * @throws LateDeliverablePersistenceException
     *             if some other error occurred when accessing the persistence.
     */
    public void update(LateDeliverable lateDeliverable) throws LateDeliverablePersistenceException;

    /**
     * <p>
     * Retrieves all existing late deliverable types from persistence.
     * </p>
     *
     * @return the retrieved late deliverable types (not null, doesn't contain null).
     *
     * @throws IllegalStateException
     *             if persistence was not configured properly.
     * @throws LateDeliverablePersistenceException
     *             if some error occurred when accessing the persistence.
     *
     * @since 1.0.6
     */
    public List<LateDeliverableType> getLateDeliverableTypes() throws LateDeliverablePersistenceException;
}
