/*
 * AsynchronousExecutorHandle.java
 *
 * Copyright (C) 2002, TopCoder, Inc. All rights reserved
 *
 */

package com.topcoder.util.exec;

/**
 * <p>A handle to an asynchronously executing command. This allows the
 * caller to manipulate and retrieve information from the executing
 * command.</p>
 *
 * <p>Note that this object should not be used simultaneously by
 * multiple threads; it should not be considered thread-safe.</p>
 *
 * @author srowen
 * @author garyk
 * @version 1.0
 */
public class AsynchronousExecutorHandle {

    /* the AsynchronousExecutor that the handle manipulates */
    private AsynchronousExecutor executor;

    /**
     * Creates an AsynchronousExecutorHandle that will manipulate and 
     * retrieve information from an AsynchronousExecutor
     *
     * @param executor the AsynchronousExecutor to be manipulated
     */
    AsynchronousExecutorHandle(final AsynchronousExecutor executor) {
        this.executor = executor;
    }

    /**
     * Returns the number of milliseconds since the AsynchronousExecutor
     * started execution, if the command that it is executing has not yet
     * completed. If it has completed, this returns the number of milliseconds
     * between the time that the AsynchronousExecutor started execution of
     * the command and the time it finished.
     *
     * @return execution time of the command if it has completed, otherwise,
     *          the time since the start time of execution 
     */
    public long getRunningTimeMS() {
        if (isDone()) {
            return executor.getEnd() - executor.getStart();
        } else {
            return System.currentTimeMillis() - executor.getStart();
        }
    }

    /**
     * Requests that the asynchronously executing command terminate immediately.</p>
     */
    public void halt() {
        executor.halt();
    }

    /**
     * Checks if the execution is done or not
     *
     * @return true iff command has completed
     */
    public boolean isDone() {
        return executor.isDone();
    }

    /**
     * Gets the execution result returned from the command
     *
     * @return execution result returned from the command, or null if it threw 
     *         an exception
     * @throws IllegalStateException if command is not done executing
     */
    public ExecutionResult getExecutionResult() throws IllegalStateException {
        return executor.getExecutionResult();
    }

    /**
     * Gets the execution exception thrown by the command
     *
     * @return exception thrown by the command, or null if no exception was 
     *         thrown
     * @throws IllegalStateException if command is not done executing
     */
    public ExecutionException getExecutionException() 
            throws IllegalStateException{
        return executor.getExecutionException();
    }
}
