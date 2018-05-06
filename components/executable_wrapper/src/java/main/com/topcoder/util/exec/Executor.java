/*
 * Executor.java
 *
 * Copyright (C) 2002, TopCoder, Inc. All rights reserved
 *
 */

package com.topcoder.util.exec;

/**
 * <p>Subclasses of this class knows how to execute a command out of JVM, 
 * or halt the execution.</p>
 *
 * @author srowen
 * @version 1.0     
 */
interface Executor {

    /**
     * Invokes execution of the command encapsulated by an Executor.</p>
     *
     * @return the execution result
     * @throws ExecutionException if any exception occured during the execution
     */
    ExecutionResult execute() throws ExecutionException;

    /**
     * <p>Requests that the executor halt the command that it is executing
     * immediately.</p>
     */
    void halt();

}
