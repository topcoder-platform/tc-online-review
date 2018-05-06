/*
 * ExecutionTimedOutException.java
 *
 * Copyright (C) 2002, TopCoder, Inc. All rights reserved
 *
 */

package com.topcoder.util.exec;

/**
 * <p>Exception that is thrown when a command is executed with a timeout,
 * and it does not complete before the timeout expires.</p>
 *
 * @author srowen
 * @version 1.0     
 */
public class ExecutionTimedOutException extends ExecutionException {

    /**
     * An empty contructor 
     */    
    public ExecutionTimedOutException() {
        super();
    }

    /* 
     * A constructor with a message 
     *
     * @param message the message of the exception
     */    
    public ExecutionTimedOutException(final String message) {
        super(message);
    }
}
