/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.distribution.commands;

import java.util.List;

import com.topcoder.util.distribution.DistributionScriptCommandExecutionException;
import com.topcoder.util.distribution.DistributionScriptExecutionContext;
import com.topcoder.util.log.Log;

/**
 * <p>
 * A mock implementation of <code>BaseDistributionScriptCommand</code> used to test the abstract class.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class MockCommand extends BaseDistributionScriptCommand {
    /**
     * <p>
     * Represent the flag if the command is executed.
     * </p>
     */
    private boolean executed;

    /**
     * <p>
     * The constructor.
     * </p>
     *
     * @param log
     *            the log
     * @param conditions
     *            the list of conditions
     */
    protected MockCommand(Log log, List<CommandExecutionCondition> conditions) {
        super(log, conditions);

        executed = false;
    }

    /**
     * <p>
     * The implementation of the abstract method executeCommand.
     * </p>
     *
     * @param context
     *            the context of this command
     *
     * @throws DistributionScriptCommandExecutionException
     *             if error occurs while executing the command
     */
    @Override
    protected void executeCommand(DistributionScriptExecutionContext context)
            throws DistributionScriptCommandExecutionException {
        executed = true;
    }

    /**
     * <p>
     * Return the namesake field.
     * </p>
     *
     * @return the namesake field
     */
    public boolean isExecuted() {
        return executed;
    }
}
