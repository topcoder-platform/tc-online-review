/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.latetracker;

import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.management.deliverable.Deliverable;
import com.topcoder.management.deliverable.persistence.DeliverablePersistenceException;
import com.topcoder.management.deliverable.persistence.sql.SqlDeliverablePersistence;

/**
 * <p>
 * A mock implementation of <code>DeliverablePersistence</code>. Used for testing.
 * </p>
 *
 * @author sparemax
 * @version 1.2
 * @since 1.2
 */
public class MockDeliverablePersistence extends SqlDeliverablePersistence {
    /**
     * <p>
     * Represents the result.
     * </p>
     */
    private static Object result;

    /**
     * <p>
     * Constructs a new <code>MockDeliverablePersistence</code> instance.
     * </p>
     *
     * @param connectionFactory
     *            the connection factory.
     */
    public MockDeliverablePersistence(DBConnectionFactory connectionFactory) {
        super(connectionFactory);
    }

    /**
     * Loads the deliverables associated with the given deliverable id and resource id.
     *
     * @param deliverableId
     *            The id of the deliverable.
     * @param resourceId
     *            The resource id of deliverable.
     * @param phaseId
     *            The phase id of the deliverable.
     *
     * @return an empty array.
     *
     * @throws DeliverablePersistenceException
     *             if there is an error reading the persistence.
     */
    @Override
    public Deliverable[] loadDeliverables(long deliverableId, long resourceId, long phaseId)
        throws DeliverablePersistenceException {
        if (result instanceof DeliverablePersistenceException) {
            throw (DeliverablePersistenceException) result;
        }

        return new Deliverable[0];
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
        MockDeliverablePersistence.result = result;
    }
}
