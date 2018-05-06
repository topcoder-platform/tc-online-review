/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.persistence;

/**
 * <p> The DeliverablePersistenceException indicates that there was an error accessing or updating a persisted resource
 * store. This exception is used to wrap the internal error that occurs when accessing the persistence store. For
 * example, in the SqlDeliverablePersistence implementation it is used to wrap SqlExceptions. </p>
 *
 * <p> This exception is initially thrown in DeliverablePersistence implementations and from there passes through
 * DeliverableManager implementations and back to the caller. It is also thrown directly by some DeliverableManager
 * implementations. </p>
 *
 * <p><strong>Thread Safety:</strong> This class is not thread safe because its base class is not thread safe.</p>
 *
 * @author aubergineanode, singlewood
 * @version 1.1
 */
public class DeliverablePersistenceException extends PersistenceException {

    /**
     * <p>Creates a new DeliverablePersistenceException.</p>
     *
     * @param message Explanation of error, can be null
     */
    public DeliverablePersistenceException(String message) {
        super(message);
    }

    /**
     * <p>Creates a new DeliverablePersistenceException.</p>
     *
     * @param message Explanation of error, can be null
     * @param cause   Underlying cause of error, can be null
     */
    public DeliverablePersistenceException(String message, Exception cause) {
        super(message, cause);
    }
}
