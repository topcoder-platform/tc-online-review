/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.distribution.commands;

import com.topcoder.util.distribution.DistributionScriptExecutionContext;

/**
 * <p>
 * This interface represents a command execution condition. This condition must
 * be met in case if the associated script command should be executed. This
 * interface defines only a single method for checking whether the condition is
 * met.
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
public interface CommandExecutionCondition {
    /**
     * <p>
     * Checks whether this command execution condition is met.
     * </p>
     *
     * @param context
     *            the distribution script execution context
     * @return true if the condition is met, false otherwise
     *
     * @throws IllegalArgumentException
     *             if context is null
     */
    public boolean check(DistributionScriptExecutionContext context);
}
