/*
 * ExecutionParameters.java
 *
 * Copyright (C) 2002, TopCoder, Inc. All rights reserved
 *
 */

package com.topcoder.util.exec;

import java.io.File;
import java.util.Map;

/**
 * <p>Encapsulates the parameters for a command execution. This includes:</p>
 *
 * <ul>
 *  <li>command and arguments</li>
 *  <li>environment</li>
 *  <li>command working directory</li>
 * </ul>
 *
 * <p>These correspond to the parameters that can be passed to Java's 
 * <code>Runtime.exec</code> method; they are encapsulated in a class here to 
 * simplify the methods in the <code>Exec</code> class, and to allow for future 
 * additions.</p>
 *
 * @see java.lang.Runtime
 * @author srowen
 * @author garyk
 * @version 1.0     
 */
public class ExecutionParameters {

    private String[] command;  // the command of the execution
    private Map environment;   // the Map for the environment
    private File workingDirectory; // the working directory

    /**
     * <p>Constructs an <code>ExecutionParameters</code> for the given 
     * command.</p>
     *
     * @param command the command of the execution
     * @throws IllegalArgumentException if command is null or empty
     */
    public ExecutionParameters(final String[] command) 
            throws IllegalArgumentException{
        setCommand(command);
    }

    /**
     * Gets the command of the execution
     *
     * @return the command of the execution
     */
    public String[] getCommand() {
        return command;
    }

    /**
     * Sets the command of the execution
     *
     * @param command the command of the execution
     * @throws IllegalArgumentException if command is null or empty
     */
    public void setCommand(final String[] command) 
            throws IllegalArgumentException{
        if (command == null || command.length == 0 || command[0] == null) {
            throw new IllegalArgumentException("command is null or empty");
        }
        
        this.command = (String[]) command.clone();
    }

    /**
     * Gets the Map for the environment
     *
     * @return the Map for the environment
     */
    public Map getEnvironment() {
        return environment;
    }

    /**
     * Sets the Map for the environment
     *
     * @param environment the Map for the environment     
     */
    public void setEnvironment(final Map environment) {
        this.environment = environment;
    }

    /**
     * Gets the working directory
     *
     * @return the working directory
     */  
    public File getWorkingDirectory() {
        return workingDirectory;
    }

    /**
     * Sets the working directory
     *
     * @param workingDirectory the working directory
     */   
    public void setWorkingDirectory(final File workingDirectory) {
        this.workingDirectory = workingDirectory;
    }
}
