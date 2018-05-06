/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder;

import com.topcoder.util.errorhandling.BaseException;


/**
 * <p>
 * This is a custom exception. All the other custom exceptions defined for this component
 * inherit from this exception.
 * </p>
 *
 * @author ShindouHikaru, TCSDEVELOPER
 * @version 1.3
 */
public class SearchBuilderException extends BaseException {
    /**
     * <p>Creates a new instance of this custom exception. This constructor has one argument: message-a
     * descriptive message.</p>
     *
     *
     *
     * @param message a descriptive message to describe why this exception is generated
     */
    public SearchBuilderException(String message) {
        super(message);
    }

    /**
     * <p>Creates a new instance of this custom exception. This constructor has two arguments: message-a
     * descriptive message; cause - the exception(or a chain of exceptions) that generated this exception.</p>
     *
     *
     *
     * @param message a descriptive message to describe why this exception is generated
     * @param cause the exception(or a chain of exceptions) that generated this exception.
     */
    public SearchBuilderException(String message, Throwable cause) {
        super(message, cause);
    }
}
