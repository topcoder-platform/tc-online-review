/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.servlet.request;

/**
 * <p>
 * This is an exception which indicates the uploaded file exceeds the file size limit. This happens during the parsing
 * of the request.
 * </p>
 *
 * <p>
 * This exception is thrown in the uploadFiles() method of <code>FileUpload</code> class.
 * </p>
 *
 * <p>
 * <b>Thread Safety: </b> This class is immutable and therefore thread safe.
 * </p>
 *
 * @author colau, PE
 * @version 2.0
 */
public class FileSizeLimitExceededException extends RequestParsingException {
    /**
     * <p>
     * Creates a new instance of <code>FileSizeLimitExceededException</code> class with a detail error message.
     * </p>
     *
     * @param message a detail error message describing the error.
     */
    public FileSizeLimitExceededException(String message) {
        super(message);
    }

    /**
     * <p>
     * Creates a new instance of <code>FileSizeLimitExceededException</code> class with a detail error message and the
     * original exception causing the error.
     * </p>
     *
     * @param message a detail error message describing the error.
     * @param cause an exception representing the cause of the error.
     */
    public FileSizeLimitExceededException(String message, Throwable cause) {
        super(message, cause);
    }
}
