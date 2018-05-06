/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.distribution.accuracytests.commands;

import com.topcoder.util.distribution.DistributionScriptCommandExecutionException;
import com.topcoder.util.distribution.DistributionScriptExecutionContext;
import com.topcoder.util.distribution.commands.BaseDistributionScriptCommand;
import com.topcoder.util.distribution.commands.CommandExecutionCondition;
import com.topcoder.util.log.Log;

import java.util.List;

/**
 * Simple wrapper, makes it possible to test replaceVariableFields method.
 *
 * @author orange_cloud
 * @version 1.0
 */
public class MockBaseDistributionScriptCommand extends BaseDistributionScriptCommand {
    /**
     * Simply calls constructor from the super-class.
     *
     * @param log        log
     * @param conditions conditions
     */
    public MockBaseDistributionScriptCommand(Log log, List<CommandExecutionCondition> conditions) {
        super(log, conditions);
    }

    /**
     * <p> Executes this command. This method is executed when all conditions are met only. </p>
     *
     * @param context the distribution script execution context
     * @throws IllegalArgumentException if context is null
     * @throws DistributionScriptCommandExecutionException
     *                                  if some other error occurred
     */
    @Override
    protected void executeCommand(DistributionScriptExecutionContext context) throws
        DistributionScriptCommandExecutionException {
        throw new UnsupportedOperationException("Method is not implemented yet.");
    }

    /**
     * Wrapper for protected method from superclass.
     *
     * @param value   the string value to be processed
     * @param context the distribution script execution context
     * @return converted text
     */
    public String replaceVariableFields(String value, DistributionScriptExecutionContext context) {
        return super.replaceVariableFields(value, context);
    }
}
