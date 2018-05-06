/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.persistence;

import com.topcoder.util.errorhandling.BaseException;

/**
 * <p> The PersistenceException indicates that there was an error accessing or updating a persisted resource store. This
 * exception is used to wrap the internal error that occurs when accessing the persistence store. </p>
 *
 * <p><strong>Thread Safety:</strong> This class is not thread safe because its base class is not thread safe.</p>
 *
 * @author aubergineanode, singlewood
 * @version 1.1
 */
public class PersistenceException extends BaseException {

    /**
     * <p>Creates a new PersistenceException.</p>
     *
     * @param message Explanation of error, can be null
     */
    public PersistenceException(String message) {
        super(message);
    }

    /**
     * <p>Creates a new PersistenceException.</p>
     *
     * @param message Explanation of error, can be null
     * @param cause   Underlying cause of error, can be null
     */
    public PersistenceException(String message, Exception cause) {
        super(message, cause);
    }
}
