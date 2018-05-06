/*
 * IllegalHandlerException.java
 *
 * Copyright (C) 2004, TopCoder, Inc. All rights reserved
 */

package com.topcoder.util.classassociations;

import java.util.*;

/**
 * <p>This exception is thrown when a client attempts to add a Handler which is not
 *  allowed in the ClassAssociator.</p>
 *
 * @author TCSDEVELOPER
 * @version 1.0  3-28-2004
 */
public class IllegalHandlerException extends Exception {

    /**
     * <p>Creates an IllegalHandlerException with specified message. </p>
     *
     * @param message A <code>String</code> containing the message of this exception.
     * @throws IllegalArgumentException if given String is null.
     */
    public  IllegalHandlerException(String message) {
        // super up before verifying message because super() must be
        // the first constructor call if it is used.
        super (message);

        // throw Exception if message is null
        if (message == null) {
            throw new IllegalArgumentException(
               "IllegalHandlerException argument cannot be null.");
        }
    } // end IllegalHandlerException

 } // end IllegalHandlerException



