/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.distribution;

/**
 * <p>
 * This interface represents a distribution script command. Each command
 * implementation must hold parameters and logic required for performing
 * specific operation used when creating a distribution. Sequence of commands
 * form a distribution script. Implementations can use the given
 * DistributionScriptExecutionContext instance to access the current context
 * variables.
 * </p>
 *
 * <p>
 * Thread Safety: Implementations of this interface must be thread safe assuming
 * that DistributionScriptExecutionContext instance is used by the caller in
 * thread safe manner.
 * </p>
 *
 * @author saarixx, TCSDEVELOPER
 * @version 1.0
 */
public interface DistributionScriptCommand {
    /**
     * <p>
     * Executes this command.
     * </p>
     *
     * @param context
     *            the distribution script execution context
     *
     *
     * @throws IllegalArgumentException
     *             if context is null
     * @throws DistributionScriptCommandExecutionException
     *             if some other error occurred
     */
    public void execute(DistributionScriptExecutionContext context)
        throws DistributionScriptCommandExecutionException;
}
