/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.distribution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * This class is a container for distribution script information. It holds the
 * list of commands in the script, the lists of required and default parameters
 * and the path of the folder where the script file is located. This class
 * provides getters and setter for all script parameters stored in private
 * fields. Shallow copies of collections are used in setters and getters.
 * </p>
 *
 * <p>
 * Thread Safety: This class is mutable and not thread safe. To use this class
 * in thread safe manner it first must be initialized via setters from a single
 * thread, and next getters can be used from multiple threads at a time.
 * </p>
 *
 * @author saarixx, TCSDEVELOPER
 * @version 1.0
 */
public class DistributionScript {
    /**
     * <p>
     * The list of commands for this script. Collection instance is initialized
     * in the constructor and can be changed in the setter only. Cannot be null.
     * Cannot contain null. Has getter and setter.
     * </p>
     */
    private List<DistributionScriptCommand> commands;

    /**
     * <p>
     * The list of names of parameters that are required for this script.
     * Collection instance is initialized in the constructor and can be changed
     * in the setter only. Cannot be null. Cannot contain null or empty. Has
     * getter and setter.
     * </p>
     */
    private List<String> requiredParams;

    /**
     * <p>
     * The mapping of optional parameter names and default parameter values.
     * These default values are used when specific parameter values are not
     * specified by the user. Collection instance is initialized in the
     * constructor and can be changed in the setter only. Cannot be null. Cannot
     * contain null/empty key or null value. Has getter and setter.
     * </p>
     */
    private Map<String, String> defaultParams;

    /**
     * <p>
     * The path of the folder in which script file is located. Can be
     * initialized in the setter only. Is null if not yet initialized. Cannot be
     * empty. Has getter and setter.
     * </p>
     */
    private String scriptFolder;

    /**
     * <p>
     * Creates an instance of DistributionScript.
     * </p>
     */
    public DistributionScript() {
        commands = new ArrayList<DistributionScriptCommand>();
        requiredParams = new ArrayList<String>();
        defaultParams = new HashMap<String, String>();
    }

    /**
     * <p>
     * Retrieves the list of commands for this script.
     * </p>
     *
     * @return the list of commands for this script
     */
    public List<DistributionScriptCommand> getCommands() {
        return new ArrayList<DistributionScriptCommand>(commands);
    }

    /**
     * <p>
     * Sets the list of commands for this script.
     * </p>
     *
     * @param commands
     *            the list of commands for this script
     *
     * @throws IllegalArgumentException
     *             if commands is null or contains null
     */
    public void setCommands(List<DistributionScriptCommand> commands) {
        Util.checkList(commands, "commands");

        this.commands = new ArrayList<DistributionScriptCommand>(commands);
    }

    /**
     * <p>
     * Retrieves the list of names of parameters that are required for this
     * script.
     * </p>
     *
     * @return the list of names of parameters that are required for this script
     */
    public List<String> getRequiredParams() {
        return new ArrayList<String>(requiredParams);
    }

    /**
     * <p>
     * Sets the list of names of parameters that are required for this script.
     * </p>
     *
     * @param requiredParams
     *            the list of names of parameters that are required for this
     *            script
     *
     * @throws IllegalArgumentException
     *             if requiredParams is null or contains null/empty
     */
    public void setRequiredParams(List<String> requiredParams) {
        Util.checkList(requiredParams, "requiredParams");

        this.requiredParams = new ArrayList<String>(requiredParams);
    }

    /**
     * <p>
     * Retrieves the mapping of optional parameter names and default parameter
     * values.
     * </p>
     *
     * @return the mapping of optional parameter names and default parameter
     *         values
     */
    public Map<String, String> getDefaultParams() {
        return new HashMap<String, String>(defaultParams);
    }

    /**
     * <p>
     * Sets the mapping of optional parameter names and default parameter
     * values.
     * </p>
     *
     * @param defaultParams
     *            the mapping of optional parameter names and default parameter
     *            values
     *
     * @throws IllegalArgumentException
     *             if defaultParams is null, contains null/empty key or null
     *             value
     */
    public void setDefaultParams(Map<String, String> defaultParams) {
        Util.checkParams(defaultParams, "defaultParams");

        this.defaultParams = new HashMap<String, String>(defaultParams);
    }

    /**
     * <p>
     * Retrieves the path of the folder in which script file is located.
     * </p>
     *
     * @return the path of the folder in which script file is located
     */
    public String getScriptFolder() {
        return this.scriptFolder;
    }

    /**
     * <p>
     * Sets the path of the folder in which script file is located.
     * </p>
     *
     * @param scriptFolder
     *            the path of the folder in which script file is located
     *
     * @throws IllegalArgumentException
     *             if scriptFolder is null or empty
     */
    public void setScriptFolder(String scriptFolder) {
        Util.checkNonNullNonEmpty(scriptFolder, "scriptFolder");

        this.scriptFolder = scriptFolder;
    }
}
