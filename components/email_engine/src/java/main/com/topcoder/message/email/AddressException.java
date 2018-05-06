/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 *
 * @(#)AddressException.java
 */
package com.topcoder.message.email;

import com.topcoder.util.errorhandling.BaseException;

/**
 * <p>Custom exception to encapsulate errors related to invalid addresses and invalid address types.</p>
 *
 * @since 3.0
 *
 * @author  BEHiker57W
 * @author  smell
 * @version 3.0
 */
public class AddressException extends BaseException {

    /**
     * <p>Constructs a new <code>AddressException</code>, with the specified detail message.</p>
     *
     * @param message   the detail message
     */
    public AddressException(String message) {
        super(message);
    }

    /**
     * <p>Constructs a new <code>AddressException</code>, with the specified detail message and cause.</p>
     *
     * @param message   the detail message
     * @param cause     <code>Throwable</code> cause of this exception
     */
    public AddressException(String message, Throwable cause) {
        super(message, cause);
    }

}
