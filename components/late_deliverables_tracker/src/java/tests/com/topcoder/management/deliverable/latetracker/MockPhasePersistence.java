/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.latetracker;

import com.topcoder.management.phase.ConfigurationException;
import com.topcoder.management.phase.PhasePersistenceException;
import com.topcoder.management.phase.db.InformixPhasePersistence;
import com.topcoder.project.phases.Phase;

/**
 * <p>
 * A mock implementation of <code>PhasePersistence</code>. Used for testing.
 * </p>
 *
 * @author sparemax
 * @version 1.2
 * @since 1.2
 */
public class MockPhasePersistence extends InformixPhasePersistence {
    /**
     * <p>
     * Represents the result.
     * </p>
     */
    private static Object result;

    /**
     * <p>
     * Constructs a new <code>MockPhasePersistence</code> instance.
     * </p>
     *
     * @param namespace
     *            the namespace.
     *
     * @throws ConfigurationException
     *             if any required configuration parameter is missing, or if any of the supplied parameters are
     *             invalid.
     */
    public MockPhasePersistence(String namespace) throws ConfigurationException {
        super(namespace);
    }

    /**
     * <p>
     * Reads a specific phase from the data store. If the phase with given id doesn't exist, <code>null</code> value
     * will be returned.
     * </p>
     *
     * @param phaseId
     *            id of phase to fetch
     *
     * @return null.
     *
     * @throws PhasePersistenceException
     *             if database related error occurs.
     */
    @Override
    public Phase getPhase(long phaseId) throws PhasePersistenceException {
        if (result instanceof PhasePersistenceException) {
            throw (PhasePersistenceException) result;
        }

        return null;
    }

    /**
     * <p>
     * Sets the result.
     * </p>
     *
     * @param result
     *            the result.
     */
    public static void setResult(Object result) {
        MockPhasePersistence.result = result;
    }
}
