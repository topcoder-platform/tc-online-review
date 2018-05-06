/*
 * ExecutionHaltedException.java
 *
 * Copyright (C) 2002, TopCoder, Inc. All rights reserved
 *
 */

package com.topcoder.util.exec;

/**
 * <p>Exception that is thrown when a command's execution is explicity halted.</p>
 *
 * @author srowen
 * @version 1.0     
 */
public class ExecutionHaltedException extends ExecutionException {

    /**
     * An empty contructor 
     */        
    public ExecutionHaltedException() {
        super();
    }

    /* 
     * A constructor with a message 
     *
     * @param message the message of the exception
     */    
    public ExecutionHaltedException(final String message) {
        super(message);
    }
}
