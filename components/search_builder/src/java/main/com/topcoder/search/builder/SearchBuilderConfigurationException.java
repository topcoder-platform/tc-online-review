/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder;


/**
 * <p>
 * It is thrown if any error occurs when reading the configuration file,for construct the manager
 * via the namespace from the configration file.
 * </p>
 *
 * @author ShindouHikaru, TCSDEVELOPER
 * @version 1.3
 */
public class SearchBuilderConfigurationException extends SearchBuilderException {
    /**
     * <p>
     * Creates a new instance of this custom exception. This constructor has one argument: message-a
     * descriptive message.
     * </p>
     *
     *
     * @param message a descriptive message to describe why this exception is generated
     */
    public SearchBuilderConfigurationException(String message) {
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
    public SearchBuilderConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
