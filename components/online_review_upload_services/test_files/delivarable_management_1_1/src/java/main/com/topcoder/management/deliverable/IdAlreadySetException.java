/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable;

/**
 * <p> The IdAlreadySetException is used to signal that the id of one of the modeling classes has already been set. This
 * is used to prevent the id being changed once it has been set. This exception is initially thrown in the 3 setId
 * methods of the modeling classes. </p>
 *
 * <p><strong>Thread Safety:</strong> This class is not thread safe because its base class is not thread safe.</p>
 *
 * @author aubergineanode, singlewood
 * @version 1.1
 */
public class IdAlreadySetException extends IllegalStateException {

    /**
     * <p>Creates a new IdAlreadySetException.</p>
     *
     * @param message Explanation of error
     */
    public IdAlreadySetException(String message) {
        super(message);
    }
}
