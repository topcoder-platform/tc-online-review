/*
* Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.distribution.stresstests;

import java.util.List;

import com.topcoder.util.distribution.DistributionScriptCommandExecutionException;
import com.topcoder.util.distribution.DistributionScriptExecutionContext;
import com.topcoder.util.distribution.commands.BaseDistributionScriptCommand;
import com.topcoder.util.distribution.commands.CommandExecutionCondition;
import com.topcoder.util.log.Log;

/**
 * <p>
 * The mock of <code>BaseDistributionScriptCommand</code> used to test.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class MockDistributionScriptCommand extends BaseDistributionScriptCommand {
    /**
     * <p>
     * The message used to verify the test result.
     * </p>
     */
    private String message;

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
    protected MockDistributionScriptCommand(Log log, List<CommandExecutionCondition> conditions) {
        super(log, conditions);
    }

    /**
     * <p>
     * The implementation of the abstract method executeCommand.
     * </p>
     *
     * @param context the context of this command
     */
    @Override
    protected void executeCommand(DistributionScriptExecutionContext context)
            throws DistributionScriptCommandExecutionException {
        message = "executed";
    }

    /**
     * <p>
     * The getter for the message.
     * </p>
     *
     * @return the message
     */
    public String getMessage() {
        return this.message;
    }
}
