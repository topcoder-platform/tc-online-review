/*
 * AsynchronousExecutor.java
 *
 * Copyright (C) 2002, TopCoder, Inc. All rights reserved
 *
 */

package com.topcoder.util.exec;

/**
 * <p>An Executor to execute command asynchronously.</p>
 *
 * @author srowen
 * @author garyk
 * @version 1.0
 **/
class AsynchronousExecutor implements Executor {

    /* the thread that encapsulates an Executor */
    private ExecutionThread execThread;

    /**
     * Creates an AsynchronousExecutor that will manage execution via a
     * SynchronousExecutor, whose execution is managed by an ExecutionThread.
     *
     * @param parameters the parameters to create an SynchronousExecutor
     */
    AsynchronousExecutor(final ExecutionParameters parameters) {
        /* initialize the execution thread with the SynchronousExecutor */
        Executor executor = new SynchronousExecutor(parameters);
        execThread = new ExecutionThread(executor);
    }

    /**
     * Creates an AsynchronousExecutor that will manage execution via a
     * TimeoutExecutor, whose execution is managed by an ExecutionThread.
     *
     * @param parameters the parameters to create an SynchronousExecutor
     * @param timeoutMS the timeout for the execution in milliseconds
     */
    AsynchronousExecutor(final ExecutionParameters parameters, 
                         final long timeoutMS) {
        /* initialize the execution thread with the TimeoutExecutor */
        Executor executor = new TimeoutExecutor(parameters, timeoutMS);
        execThread = new ExecutionThread(executor);
    }

    /**
     * Starts the execution thread and returns null.
     *
     * @return null
     * @throws ExecutionException if the execution thread is interrupted
     *         while it is waiting
     */
    public ExecutionResult execute() throws ExecutionException {
        /*
         * put the start() and wait() in the synchronize block is to make 
         * sure that wait() is always called before notify(), otherwise,
         * if notify() is called before wait(), it will be a deadlock.
         */
        synchronized (execThread) {  
            /* start the thread and execution */
            execThread.start();

            /* 
             * try to wait until the process of the execution starts. This will 
             * prevent the thread of user application from trying to access the
             * methods, such as isDone(), getStart() and etc. when the process 
             * hasn't started.
             */
            try {
                execThread.wait();
            } catch (InterruptedException ie) {
                throw new ExecutionException(ie.getMessage());
            }
        }

        return null;
    }

    /**
     * Asks the execution thread to halt, and waits for it to do so.
     */
    public void halt() {
        /* tell the thread to interrupt execution */
        execThread.halt();

        /* wait for the thread to finish */
        try {
            execThread.join();	
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if the execution is done or not
     *
     * @return true iff command has completed
     */
    boolean isDone() {
        return execThread.isDone();
    }

    /**
     * Gets the start time of the execution in milliseconds
     *
     * @return the start time of the execution in milliseconds
     */
    long getStart() {
        return execThread.getStart();
    }

    /**
     * Gets the end time in milliseconds
     *
     * @return the end time of the execution in milliseconds
     */
    long getEnd() {
        return execThread.getEnd();
    }

    /**
     * Gets the execution result returned from the command
     *
     * @return execution result returned from the command
     * @throws IllegalStateException if command is not done executing
     */
    ExecutionResult getExecutionResult() throws IllegalStateException {
        return execThread.getExecutionResult();
    }

    /**
     * Gets the execution exception thrown by the command
     *
     * @return exception thrown by the command
     * @throws IllegalStateException if command is not done executing
     */
    ExecutionException getExecutionException() throws IllegalStateException {
        return execThread.getExecutionException();
    }
}