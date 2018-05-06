/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.distribution.commands.conditions;

import com.topcoder.util.distribution.DistributionScriptExecutionContext;
import com.topcoder.util.distribution.Util;
import com.topcoder.util.distribution.commands.CommandExecutionCondition;

/**
 * <p>
 * This class is an implementation of CommandExecutionCondition that checks
 * whether variable with specific name is not defined in the current execution
 * context.
 * </p>
 *
 * <p>
 * Thread Safety: This class is immutable and thread safe when
 * DistributionScriptExecutionContext instance is used by the caller in thread
 * safe manner.
 * </p>
 *
 * @author saarixx, TCSDEVELOPER
 * @version 1.0
 */
public class VariableNotDefinedCondition implements CommandExecutionCondition {
    /**
     * <p>
     * The name of the variable to be checked by this condition. Is initialized
     * in the constructor and never changed after that. Cannot be null or empty.
     * Is used in check().
     * </p>
     */
    private final String name;

    /**
     * <p>
     * Creates an instance of VariableNotDefinedCondition.
     * </p>
     *
     * @param name
     *            the name of the variable
     *
     * @throws IllegalArgumentException
     *             if name is null or empty
     */
    public VariableNotDefinedCondition(String name) {
        Util.checkNonNullNonEmpty(name, "name");

        this.name = name;
    }

    /**
     * <p>
     * Checks whether this command execution condition is met, i.e. whether a
     * variable with specific name is not defined.
     * </p>
     *
     * @param context
     *            the distribution script execution context
     * @return true if the condition is met (variable is not defined), false
     *         otherwise
     *
     * @throws IllegalArgumentException
     *             if context is null
     */
    public boolean check(DistributionScriptExecutionContext context) {
        Util.checkNonNull(context, "context");

        return context.getVariable(name) == null;
    }
}
