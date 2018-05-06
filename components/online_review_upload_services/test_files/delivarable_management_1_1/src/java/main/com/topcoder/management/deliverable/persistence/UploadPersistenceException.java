/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.persistence;

/**
 * <p> The UploadPersistenceException indicates that there was an error accessing or updating a persisted resource
 * store. This exception is used to wrap the internal error that occurs when accessing the persistence store. For
 * example, in the SqlUploadPersistence implementation it is used to wrap SqlExceptions. </p>
 *
 * <p> This exception is initially thrown in UploadPersistence implementations and from there passes through
 * UploadManager implementations and back to the caller. It is also thrown directly by some UploadManager
 * implementations. </p>
 *
 * <p><strong>Thread Safety:</strong> This class is not thread safe because its base class is not thread safe.</p>
 *
 * @author aubergineanode, singlewood
 * @version 1.1
 */
public class UploadPersistenceException extends PersistenceException {

    /**
     * <p>Creates a new UploadPersistenceException.</p>
     *
     * @param message Explanation of error, can be null
     */
    public UploadPersistenceException(String message) {
        super(message);
    }

    /**
     * <p>Creates a new UploadPersistenceException.</p>
     *
     * @param message Explanation of error, can be null
     * @param cause   Underlying cause of error, can be null
     */
    public UploadPersistenceException(String message, Exception cause) {
        super(message, cause);
    }
}
