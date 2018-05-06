/*
 * ExecutionResult.java
 *
 * Copyright (C) 2002, TopCoder, Inc. All rights reserved
 *
 */

package com.topcoder.util.exec;

/**
 * <p>Encapsulates the result of a command execution. This includes the exit
 * status, output, and error output.</p>
 *
 * @author srowen
 * @author garyk
 * @version 1.0     
 */
public class ExecutionResult {

    private int exitStatus;  // the exit status of the execution
    private String out;  // the standard output string of the execution
    private String err;  // the error output string of the execution

    /*
     * An ExecutionResult constructor
     *
     * @param exitStaus the exit status of the execution
     * @param out the standard output string of the execution
     * @param err the error output string of the execution
     */
    public ExecutionResult(final int exitStatus, final String out, 
                           final String err) {
        this.exitStatus = exitStatus;
        this.out = out;
        this.err = err;
    }

    /*
     * Gets the exit status of the execution
     *
     * @return the exit status of the execution
     */
    public int getExitStatus() {
        return exitStatus;
    }

    /*
     * Gets the standard output string of the execution
     *
     * @return the standard output string of the execution
     */    
    public String getOut() {
        return out;
    }

    /*
     * Gets the error output string of the execution
     *
     * @return the error output string of the execution
     */    
    public String getErr() {
        return err;
    }
}
