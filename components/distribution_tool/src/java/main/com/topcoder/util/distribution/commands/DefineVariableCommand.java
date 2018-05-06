/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.distribution.commands;

import java.util.List;

import com.topcoder.util.distribution.DistributionScriptExecutionContext;
import com.topcoder.util.distribution.Util;
import com.topcoder.util.log.Log;

/**
 * <p>
 * This class is an implementation of DistributionScriptCommand that defines a
 * variable in the current script execution context. It extends
 * BaseDistributionScriptCommand that provides common functionality for all
 * commands defined in this component.
 * </p>
 *
 * <p>
 * Thread Safety: This class is immutable and thread safe when
 * DistributionScriptExecutionContext instance is used by the caller in thread
 * safe manner. It uses thread safe Log instance.
 * </p>
 *
 * @author saarixx, TCSDEVELOPER
 * @version 1.0
 */
public class DefineVariableCommand extends BaseDistributionScriptCommand {
    /**
     * <p>
     * The name of the variable to be defined or redefined. Is initialized in
     * the constructor and never changed after that. Cannot be null or empty. Is
     * used in executeCommand().
     * </p>
     */
    private final String name;

    /**
     * <p>
     * The value of the variable (can contain variable fields in format
     * "%{variable_name}"). Is initialized in the constructor and never changed
     * after that. Cannot be null. Is used in executeCommand().
     * </p>
     */
    private final String value;

    /**
     * <p>
     * Creates an instance of DefineVariableCommand with the given parameters.
     * </p>
     *
     * @param log
     *            the logger used for logging executed command description and
     *            warnings (null if logging is not required)
     * @param value
     *            the value of the variable (can contain variable fields in
     *            format "%{variable_name}")
     * @param name
     *            the name of the variable to be defined or redefined
     * @param conditions
     *            the conditions that indicate when this command must be
     *            executed (all conditions are ANDed, empty list means that the
     *            command must be executed ALWAYS)
     *
     * @throws IllegalArgumentException
     *             if name is null/empty or value is null, or if conditions is
     *             null or contains null
     */
    public DefineVariableCommand(Log log,
            List<CommandExecutionCondition> conditions, String name,
            String value) {
        super(log, conditions);

        Util.checkNonNullNonEmpty(name, "name");
        Util.checkNonNull(value, "value");

        this.name = name;
        this.value = value;
    }

    /**
     * <p>
     * Executes this command. Defines or redefines the specific variable.
     * </p>
     *
     * @param context
     *            the distribution script execution context
     *
     * @throws IllegalArgumentException
     *             if context is null
     */
    protected void executeCommand(DistributionScriptExecutionContext context) {
        Util.checkNonNull(context, "context");

        // Replace variable fields in the string:
        String curValue = replaceVariableFields(value, context);
        Util.logInfo(getLog(), "Defining variable <" + name + ">=<" + curValue
                + ">");
        context.setVariable(name, curValue);
    }
}
