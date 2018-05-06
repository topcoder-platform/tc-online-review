/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.servlet.request;

/**
 * <p>
 * This is an exception which indicates invalid content type in the request. For example, according to HTTP 1.1, the
 * content type of the request should be "multipart/form-data". Others should be treated as invalid content type.
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
public class InvalidContentTypeException extends RequestParsingException {
    /**
     * <p>
     * Creates a new instance of <code>InvalidContentTypeException</code> class with a detail error message.
     * </p>
     *
     * @param message a detail error message describing the error.
     */
    public InvalidContentTypeException(String message) {
        super(message);
    }

    /**
     * <p>
     * Creates a new instance of <code>InvalidContentTypeException</code> class with a detail error message and the
     * original exception causing the error.
     * </p>
     *
     * @param message a detail error message describing the error.
     * @param cause an exception representing the cause of the error.
     */
    public InvalidContentTypeException(String message, Throwable cause) {
        super(message, cause);
    }
}
