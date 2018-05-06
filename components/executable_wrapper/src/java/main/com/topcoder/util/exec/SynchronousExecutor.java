/*
 * SynchronousExecutor.java
 *
 * Copyright (C) 2002, TopCoder, Inc. All rights reserved
 *
 */

package com.topcoder.util.exec;

import java.util.Set;
import java.util.Map;
import java.util.Iterator;
import java.io.File;

/**
 * <p>An Executor to execute command synchronously.</p>
 *
 * @author srowen
 * @author garyk
 * @version 1.0
 **/
class SynchronousExecutor implements Executor {

    private Process process;      // the process of the execution of the command
    private String[] shellCommand;  // array of the shell command
    private String[] environment;   // array of the environment settings
    private File workingDirectory;  // working directory
    private StreamToStringThread outThread;   // standard output thread
    private StreamToStringThread errorThread; // error output thread
    private boolean isProcessHalted; // flag to show if the procees is halted

    /**
     * <p>Note that this constructor should process the given command using
     * PlatformSupport to create a command appropriate for execution on the
     * current operating system.</p>
     *
     * @param parameters the parameters of the command for the execution
     */
    SynchronousExecutor(final ExecutionParameters parameters) {
        Map envpMap;  // Map of the environment settings
        Set envpSet;  // Set of the environment settings
        
        /* initialize that the process is not halted */
        isProcessHalted = false;

        /* obtain an instance of PlatformSupport */
        PlatformSupport platformSupport = PlatformSupport.getInstance();

        /* make up the shell command */
        shellCommand =     
                platformSupport.makeShellCommand(parameters.getCommand());

        /* obtain the working directory */
        workingDirectory = parameters.getWorkingDirectory();

        /* set up the environment */
        if ((envpMap = parameters.getEnvironment()) != null) {
            /* get all the entries of environment Map into a Set */
            envpSet = envpMap.entrySet();

            /* initialize the environment settings array */
            environment = new String[envpSet.size()];

            Iterator it = envpSet.iterator();
            for (int i = 0; it.hasNext(); i++) {
                /* get the next entry in the Set of environment settings */
                Map.Entry envpEntry = (Map.Entry) it.next();

                /* create a environment variable setting in format name=value */
                environment[i] = envpEntry.getKey() + "=" 
                                        + envpEntry.getValue();
            }
        }
    }

    /**
     * <p>This method invokes the command using java.lang.Runtime, then creates
     * and starts two StreamToStringThread threads to consume its standard
     * output and error stream, and then waits for the Process's completion.</p>
     *
     * <p>Watch out for the interaction between this and the halt() method.
     * It could be called before this method is called; it could be called
     * after execute() but before it waits for the Process to finish; it could
     * be called while this method waits for the Process to finish. Make sure
     * that all these are handled, and that in all cases, halt() causes this
     * method to throw an ExecutionHaltedException.</p>
     *
     * @return the execution result
     * @throws ExecutionException if any Exception is thrown during execution
     *         of the underlying Process
     * @throws ExecutionHaltedException if halt() is called before the
     *         underlying Process completes
     */
    public ExecutionResult execute() throws ExecutionException {
        int exitValue;  // exit value of the process
        String output;  // standard output string of the process
        String errorOutput;  // error output string of the process
        Thread currentThread; // the current thread

        try {
            /* 
             * try to execute the shell command with the given enviroment 
             * settings and working directory
             */            
            process = Runtime.getRuntime().exec(shellCommand, environment, 
                              workingDirectory); 
            
            /* 
             * create the standard output and error output 
             * StreamToStringThreads with their associated input streams.
             */
            outThread = new StreamToStringThread(process.getInputStream());
            errorThread = new StreamToStringThread(process.getErrorStream());
          
            /* get the current thread */
            currentThread = Thread.currentThread();
            
            /* 
             * notify the TimeoutExecutor or AsynchronousExecutor, which owns
             * the thread that encapsulates this SynchronousExecutor and 
             * is waiting for the process of the execution to start
             */            
            synchronized (currentThread) {
                currentThread.notify();
            }       	            
                
            /* start these two threads */
            outThread.start();
            errorThread.start();

            /* wait for these two threads to be done */
            outThread.join();            
            errorThread.join();
          
            /* wait for the process to finish */
            process.waitFor();
        } catch (Exception e) {
            /* destroy the current process if it is not null */
            if (process != null) {
                process.destroy();            
            }

            throw new ExecutionException(e.getMessage());
        }

        /* throw an ExecutionHaltedException if the process is halted */
        if (isProcessHalted) {
            throw new ExecutionHaltedException("The execution is halted");
        }

        /* obtain the exit value of the process */
        exitValue = process.exitValue();

        /* 
         * obtain the standard output and error output string from their 
         * threads
         */
        output = outThread.getOutput();
        errorOutput = errorThread.getOutput();

        /* 
         * throw an ExecutionHaltedException if the exitValue is not 0,
         * or the length of the errorOutput is not 0, since in Solaris
         * if the command doesn't exist, the exit value is still 0, 
         * so we need to check whether there are errors through the errorOutput
         */
        if (exitValue != 0 || errorOutput.length() != 0) {
            throw new ExecutionException("Command doesn't exist " 
                    + "or exits abnormally" );
        }

        return new ExecutionResult(exitValue, output, errorOutput);
    }

    /**
     * <p>Requests that the command that it is executing is halted 
     * immediately.</p>
     */
    public void halt() {
        isProcessHalted = true;

        /* stop the standard output thread */
        if (outThread != null) {
            outThread.stopReading();
        }

        /* stop the error output thread */        
        if (errorThread != null) {
            errorThread.stopReading();
        }

        /* destroy the current process */
        if (process != null) {
            process.destroy();
        }

    }
}
