/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.servlet.request;

import com.topcoder.util.errorhandling.BaseException;


/**
 * <p>
 * This is the base exception class for the File Upload component. All exception classes should extend from it.
 * </p>
 *
 * <p>
 * <b>Thread Safety: </b> This class is immutable and therefore thread safe.
 * </p>
 *
 * @author colau, PE
 * @version 2.0
 */
public class FileUploadException extends BaseException {
    /**
     * <p>
     * Creates a new instance of <code>FileUploadException</code> class with a detail error message.
     * </p>
     *
     * @param message a detail error message describing the error.
     */
    public FileUploadException(String message) {
        super(message);
    }

    /**
     * <p>
     * Creates a new instance of <code>FileUploadException</code> class with a detail error message and the original
     * exception causing the error.
     * </p>
     *
     * @param message a detail error message describing the error.
     * @param cause an exception representing the cause of the error.
     */
    public FileUploadException(String message, Throwable cause) {
        super(message, cause);
    }
}
