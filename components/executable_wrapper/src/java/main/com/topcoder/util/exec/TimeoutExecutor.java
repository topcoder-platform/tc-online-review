/*
 * TimeoutExecutor.java
 *
 * Copyright (C) 2002, TopCoder, Inc. All rights reserved
 *
 */

package com.topcoder.util.exec;

/**
 * <p>An Executor to execute command synchronously with timeout.</p>            
 *
 * @author srowen
 * @author garyk
 * @version 1.0     
 **/
class TimeoutExecutor implements Executor {

    /* the thread that encapsulates an Executor */    
    private ExecutionThread execThread;
    private long timeout; // the timeout for the execution in milliseconds

    /**
     * Creates an TimeoutExecutor that will manage execution via a
     * SynchronousExecutor, whose execution is managed by an ExecutionThread.
     *
     * @param parameters the parameters to create an SynchronousExecutor
     * @param timeoutMS the timeout for the execution in milliseconds
     */    
    TimeoutExecutor(final ExecutionParameters parameters, 
                    final long timeoutMS) {
        /* initialize the execution thread with the executor */
        Executor executor = new SynchronousExecutor(parameters);
        execThread = new ExecutionThread(executor);

        timeout = timeoutMS;
    }

    /**
     * <p>Uses an ExecutionThread to manage a SynchronousExecutor's execution
     * of the command. This method starts, and then joins on that thread with 
     * the given timeout. If the thread is not done by the time this thread 
     * continues (that is, it timed out), then an ExecutionTimedOutException 
     * is thrown.</p>
     *
     * <p>If the SynchronousExecutor completes, but produced an exception, it
     * is rethrown here. If not, then the result from the SynchronousExecutor
     * is returned.</p>
     *
     * @return the execution result
     * @throws ExecutionException if this thread is interrupted while joining
     *         on the execution thread
     * @throws ExecutionTimedOutException if the command does not complete
     *         within the given timeout period
     */
    public ExecutionResult execute() throws ExecutionException {
        /* the exception produced in the SynchronousExecutor */
        ExecutionException execException;   
        long interval;  // the execution time from start to end
        Thread currentThread; // the current thread

        try {
            /*
             * put the start() and wait() in the synchronize block is to make 
             * sure that wait() is always called before notify(), otherwise,
             * if notify() is called before wait(), it will be a deadlock.
             */
            synchronized (execThread) {
                /* start the thread and execution */
                execThread.start();                

                /* 
                 * try to wait until the process of the execution starts. This 
                 * will prevent the thread of user application from trying to 
                 * access the method halt() when the process hasn't started.
                 * And it is also needed for the AsynchronousExecutor, which
                 * contains a TimeoutExecutor in its thread 
                 */                            
                execThread.wait();                
            }       	

            /* get the current thread */
            currentThread = Thread.currentThread();
            
            /* 
             * notify the AsynchronousExecutor, which is waiting for the 
             * process of the execution to start
             */
            synchronized (currentThread) {
                currentThread.notify();
            }

            /* try to wait, up to timeout, for completion */            
            execThread.join(timeout);
        } catch (InterruptedException ie) {
            throw new ExecutionException(ie.getMessage());
        }

        /* obtain the interval of the execution */
        interval = execThread.getEnd() - execThread.getStart();

        /* 
         * if the interval is more than timeout or less than 0(since the end
         * time is initialized as 0, and then that the interval is less than 0 
         * means the end time hasn't obtained), then it is timeout and throws 
         * an ExecutionTimedOutException
         */
        if (interval < 0 || interval > timeout) {
            throw new ExecutionTimedOutException("The execution of the command"
                    + " is time out");
        }

        /* obtain the ExecutionException if any */
        execException = execThread.getExecutionException();

        /* 
         * if no ExecutionException exists, return the execution result,
         * otherwise, throw that ExecutionException
         */
        if (execException == null) {
            return execThread.getExecutionResult();
        } else {
            throw execException;
        }       
    }

    /**
     * Asks the execution thread to halt, and waits for it to do so.
     */
    public void halt() {
        execThread.halt();
    }
}
