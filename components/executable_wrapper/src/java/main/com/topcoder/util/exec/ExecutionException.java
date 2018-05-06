/*
 * ExecutionException.java
 *
 * Copyright (C) 2002, TopCoder, Inc. All rights reserved
 *
 */

package com.topcoder.util.exec;

/**
 * <p>Generic exception that is thrown when anything goes wrong during 
 * execution of a command.</p>
 *
 * @author srowen
 * @version 1.0
 **/
public class ExecutionException extends Exception {

    /**
     * An empty contructor 
     */
    public ExecutionException() {
        super();
    }

    /* 
     * A constructor with a message 
     *
     * @param message the message of the exception
     */
    public ExecutionException(final String message) {
        super(message);
    }
}
