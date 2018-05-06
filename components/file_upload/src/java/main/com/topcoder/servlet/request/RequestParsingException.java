/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.servlet.request;

/**
 * <p>
 * This is an exception which indicates any errors related to parsing the request, such as reading unexpected input
 * data or violating some parsing constraints.
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
public class RequestParsingException extends FileUploadException {
    /**
     * <p>
     * Creates a new instance of <code>RequestParsingException</code> class with a detail error message.
     * </p>
     *
     * @param message a detail error message describing the error.
     */
    public RequestParsingException(String message) {
        super(message);
    }

    /**
     * <p>
     * Creates a new instance of <code>RequestParsingException</code> class with a detail error message and the
     * original exception causing the error.
     * </p>
     *
     * @param message a detail error message describing the error.
     * @param cause an exception representing the cause of the error.
     */
    public RequestParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
