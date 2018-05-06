/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.servlet.request;

/**
 * <p>
 * This is an exception which indicates any errors related to persistence where the uploaded files are stored. It can
 * be used to wrap exceptions thrown from database or remote file system, for example.
 * </p>
 *
 * <p>
 * This exception is thrown for all operations involving persistence, such as uploading or downloading files.
 * </p>
 *
 * <p>
 * <b>Thread Safety: </b> This class is immutable and therefore thread safe.
 * </p>
 *
 * @author colau, PE
 * @version 2.0
 */
public class PersistenceException extends FileUploadException {
    /**
     * <p>
     * Creates a new instance of <code>PersistenceException</code> class with a detail error message.
     * </p>
     *
     * @param message a detail error message describing the error.
     */
    public PersistenceException(String message) {
        super(message);
    }

    /**
     * <p>
     * Creates a new instance of <code>PersistenceException</code> class with a detail error message and the original
     * exception causing the error.
     * </p>
     *
     * @param message a detail error message describing the error.
     * @param cause an exception representing the cause of the error.
     */
    public PersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
