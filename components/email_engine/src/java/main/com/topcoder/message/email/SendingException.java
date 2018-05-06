/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 *
 * @(#)SendingException.java
 */
package com.topcoder.message.email;

import com.topcoder.util.errorhandling.BaseException;

/**
 * <p>Exception class to encapsulate all errors that occur over the network or with (present or missing) foreign hosts
 * and with the connection and sending methods of the javamail API.</p>
 *
 * <p>Note in the javadoc that this exception may encapsulate a javax.mail.SendFailedException that contains useful
 * information about which addresses may have had the message delivered and which were invalid and the user will have to
 * examine the cause to find the information. See SendFailedException javadoc from the Javamail API
 * (http://java.sun.com/products/javamail/) for more information.</p>
 *
 * @since 3.0
 *
 * @author  BEHiker57W
 * @author  smell
 * @version 3.0
 */
public class SendingException extends BaseException {

    /**
     * <p>Constructs a new <code>SendingException</code>, with the specified detail message.</p>
     *
     * @param message   the detail message
     */
    public SendingException(String message) {
        super(message);
    }

    /**
     * <p>Constructs a new <code>SendingException</code>, with the specified detail message and cause.</p>
     *
     * @param message   the detail message
     * @param cause     <code>Throwable</code> cause of this exception
     */
    public SendingException(String message, Throwable cause) {
        super(message, cause);
    }

}
