/*
 * Exec.java
 *
 * Copyright (C) 2002, TopCoder, Inc. All rights reserved
 *
 */

package com.topcoder.util.exec;

/**
 * <p>Utility class that provides access to command execution functionality.</p>
 *
 * @author srowen
 * @author garyk
 * @version 1.0     
 */
public class Exec {

    private Exec() {
    }

    /**
     * <p>Executes a command synchronously. This is a convenience method that 
     * calls the analagous execute() method with an ExecutionParameters object
     * with just the given command. Therefore this executes using the current 
     * process's environment and working directory.</p>
     *
     * @param command the command of the execution
     * @return the execution result
     * @throws IllegalArgumentException if command is null or empty
     * @throws ExecutionException if any exception occured during the execution
     */
    public static ExecutionResult execute(final String[] command)
            throws ExecutionException, IllegalArgumentException {
        return execute(new ExecutionParameters(command));
    }

    /**
     * <p>Executes a command synchronously, using the given parameters.</p>
     *
     * @param parameters the parameters of the execution
     * @return the execution result
     * @throws IllegalArgumentException if parameters is null
     * @throws ExecutionException if any exception occured during the execution
     */
    public static ExecutionResult execute(final ExecutionParameters parameters)
            throws ExecutionException, IllegalArgumentException {
        if (parameters == null) {
            throw new IllegalArgumentException("parameters is null");
        }

        Executor executor = new SynchronousExecutor(parameters);
        return executor.execute();
    }

    /**
     * <p>Executes a command synchronously, with the given timeout. This is a 
     * convenience method that calls the analagous execute() method with an 
     * ExecutionParameters object with just the given command. Therefore this 
     * executes using the current process's environment and working 
     * directory.</p>
     *
     * @param command the command of the execution
     * @param timeoutMS the timeout for the execution in milliseconds
     * @return the execution result
     * @throws IllegalArgumentException if command is null or empty,
     *         or timeoutMS is nonpositive
     * @throws ExecutionException if any exception occured during the execution
     */
    public static ExecutionResult execute(final String[] command, 
                                          final long timeoutMS) 
            throws ExecutionException, IllegalArgumentException {
        return execute(new ExecutionParameters(command), timeoutMS);
    }

    /**
     * <p>Executes a command synchronously, with the given timeout, and
     * using the given parameters.</p>
     *
     * @param parameters the parameters of the execution
     * @param timeoutMS the timeout for the execution in milliseconds
     * @return the execution result
     * @throws IllegalArgumentException if parameters is null,
     *         or timeoutMS is nonpositive
     * @throws ExecutionException if any exception occured during the execution
     */
    public static ExecutionResult execute(final ExecutionParameters parameters, 
                                          final long timeoutMS)
            throws ExecutionException, IllegalArgumentException {
        if (parameters == null) {
            throw new IllegalArgumentException("parameters is null");
        }

        if (timeoutMS <= 0) {
            throw new IllegalArgumentException("timeoutMS is not positive");
        }

        Executor executor = new TimeoutExecutor(parameters, timeoutMS);
        return executor.execute();
    }

    /**
     * <p>Executes a command asynchronously. This is a convenience method that 
     * calls the analagous execute() method with an ExecutionParameters object
     * with just the given command. Therefore this executes using the current 
     * process's environment and working directory.</p>
     *
     * @param command the command of the execution
     * @return a handle to an asynchronously executing command
     * @throws IllegalArgumentException if command is null or empty
     * @throws ExecutionException if any exception occured during the execution
     */
    public static AsynchronousExecutorHandle executeAsynchronously(
            final String[] command) 
            throws ExecutionException, IllegalArgumentException {
        return executeAsynchronously(new ExecutionParameters(command));
    }

    /**
     * <p>Executes a command asynchronously, using the given parameters.</p>
     *
     * @param parameters the parameters of the execution
     * @return a handle to an asynchronously executing command
     * @throws IllegalArgumentException if parameters is null
     * @throws ExecutionException if any exception occured during the execution
     */
    public static AsynchronousExecutorHandle executeAsynchronously(
            final ExecutionParameters parameters) 
            throws ExecutionException, IllegalArgumentException {
        if (parameters == null) {
            throw new IllegalArgumentException("parameters is null");
        }

        Executor executor = new AsynchronousExecutor(parameters);
        
        executor.execute();

        return new AsynchronousExecutorHandle((AsynchronousExecutor) executor);
    }

    /**
     * <p>Executes a command asynchronously, with the given timeout. This is a 
     * convenience method that calls the analagous execute() method with an 
     * ExecutionParameters object with just the given command. Therefore this 
     * executes using the current process's environment and working directory.</p>
     * 
     * @param command the command of the execution
     * @param timeoutMS the timeout for the execution in milliseconds
     * @return a handle to an asynchronously executing command
     * @throws IllegalArgumentException if command is null or empty,
     *         or timeoutMS is nonpositive
     * @throws ExecutionException if any exception occured during the execution
     */
    public static AsynchronousExecutorHandle executeAsynchronously(
            final String[] command, final long timeoutMS)
            throws ExecutionException, IllegalArgumentException {
        return executeAsynchronously(new ExecutionParameters(command),         
                timeoutMS);
    }

    /**
     * <p>Executes a command asynchronously, with the given timeout, and
     * using the given parameters.</p>
     *
     * @param parameters the parameters of the execution
     * @param timeoutMS the timeout for the execution in milliseconds
     * @return a handle to an asynchronously executing command
     * @throws IllegalArgumentException if parameters is null,
     *         or timeoutMS is nonpositive
     * @throws ExecutionException if any exception occured during the execution
     */
    public static AsynchronousExecutorHandle executeAsynchronously(
            final ExecutionParameters parameters, final long timeoutMS)
            throws ExecutionException, IllegalArgumentException {
        if (parameters == null) {
            throw new IllegalArgumentException("parameters is null");
        }

        if (timeoutMS <= 0) {
            throw new IllegalArgumentException("timeoutMS is not positive");
        }

        Executor executor = new AsynchronousExecutor(parameters, 
                                    timeoutMS);
        executor.execute();

        return new AsynchronousExecutorHandle((AsynchronousExecutor) executor);
    }
}