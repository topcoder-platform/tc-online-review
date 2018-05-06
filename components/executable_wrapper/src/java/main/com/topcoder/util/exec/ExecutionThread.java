/*
 * ExecutionThread.java
 *
 * Copyright (C) 2002, TopCoder, Inc. All rights reserved
 *
 */

package com.topcoder.util.exec;

/**
 * <p>A class that encapsulates an Executor in an individual thread, and then
 * can be controlled by the main thread. It is used by the TimeExecutor and 
 * AsychronousExecutor.</p>
 *
 * @author srowen
 * @author garyk
 * @version 1.0     
 */
class ExecutionThread extends Thread {

    private Executor executor;  // the executor to execute the command
    private ExecutionResult execResult;  // the result of the execution
    private ExecutionException execException; // the execution exception
    private long start; // start time of the execution
    private long end;   // end time of the execution
    private Thread currentThread;  // current thread of the execution

    /**
     * Creates an ExecutionThread that will manage execution via a
     * SynchronousExecutor
     *
     * @param executor the Executor to be managed
     */
    ExecutionThread(final Executor executor) {
        this.executor = executor;
        execResult = null;
        execException = null;
        start = end = 0;
        currentThread = null;
    }

    /**
     * Simply calls execute() on the underlying executor, and stores its
     * return value or thrown exception.
     */
    public void run() {
        /* get the current thread */
        currentThread = Thread.currentThread();

        /* record start time */
        start = System.currentTimeMillis();

        /* try to execute the command */
        try {
        	execResult = executor.execute();
        } catch (ExecutionException ee) {
            /* store the ExecutionException */
            execException = ee;
        }

        /* record end time */
        end = System.currentTimeMillis();
    }

    /**
     * Checks if the execution is done or not
     *
     * @return true iff command has completed
     */    
    boolean isDone() {
        return (currentThread != null && !currentThread.isAlive());
    }

    /**
     * Gets the start time of the execution in milliseconds
     *
     * @return the start time of the execution in milliseconds
     */    
    long getStart() {
        return start;
    }

    /**
     * Gets the end time in milliseconds
     *
     * @return the end time of the execution in milliseconds
     */    
    long getEnd() {
        return end;
    }

    /**
     * Tells the underlying executor to halt.
     */
    void halt() {
        executor.halt();
    }

    /**
     * <p>Returns the result returns by the underlying executor, or null if
     * there is none (because an exception was thrown). If the underlying
     * executor isn't done yet, this throws an IllegalStateException.</p>
     *
     * @return execution result returned from the command
     * @throws IllegalStateException if command is not done executing
     */
    ExecutionResult getExecutionResult() {
        if (isDone()) {
            return execResult;
        } else {
            throw new IllegalStateException("The execution of the command " + 
                    "is not done");
        }
    }

    /**
     * <p>Returns the exception thrown by the underlying executor, or null if
     * there is none. If the underlying executor isn't done yet, this throws
     * an IllegalStateException.</p>
     *
     * @return exception thrown by the command
     * @throws IllegalStateException if command is not done executing
     */
    ExecutionException getExecutionException() {
        if (isDone()) {
            return execException;
        } else {
            throw new IllegalStateException("The execution of the command " + 
                    "is not done");
        }
    }
}