/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.distribution;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * This class is a container for distribution script execution context
 * information. Currently it just holds a list of defined context variables.
 * Variable names and values are strings, variable values cannot be null. This
 * class is used by DistributionTool and DistributionScriptCommand to share
 * information between command executions.
 * </p>
 *
 * <p>
 * Thread Safety: This class has mutable variables collection, thus it's not
 * thread safe. But in this component it's always accessed from a single thread
 * only, thus it's used in thread safe manner.
 * </p>
 *
 * @author saarixx, TCSDEVELOPER
 * @version 1.0
 */
public class DistributionScriptExecutionContext {
    /**
     * <p>
     * The execution context variables. Map keys are variable names, map values
     * are variable values. Collection instance is initialized in the
     * constructor and never changed after that. Cannot be null. Cannot contain
     * null/empty key or null value. Is used in setVariable() and getVariable().
     * </p>
     */
    private final Map<String, String> variables;

    /**
     * <p>
     * Creates an instance of DistributionScriptExecutionContext.
     * </p>
     */
    public DistributionScriptExecutionContext() {
        variables = new HashMap<String, String>();
    }

    /**
     * <p>
     * Sets the specified value to a variable with the given name or undefines a
     * variable with the given name (if value is null).
     * </p>
     *
     * @param value
     *            the new value of the variable (null if variable must be
     *            undefined)
     * @param name
     *            the name of the variable
     *
     * @throws IllegalArgumentException
     *             if name is null or empty
     */
    public void setVariable(String name, String value) {
        Util.checkNonNullNonEmpty(name, "name");

        if (value == null) {
            variables.remove(name);
        } else {
            variables.put(name, value);
        }
    }

    /**
     * <p>
     * Retrieves the value of the variable with the given name. Returns null if
     * variable with the given name is not defined.
     * </p>
     *
     * @param name
     *            the name of the variable
     *
     * @return the value of the variable (null if variable is not defined)
     *
     * @throws IllegalArgumentException
     *             if name is null or empty
     */
    public String getVariable(String name) {
        Util.checkNonNullNonEmpty(name, "name");

        return variables.get(name);
    }
}
