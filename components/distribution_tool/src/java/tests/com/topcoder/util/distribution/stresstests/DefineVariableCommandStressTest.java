/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.distribution.stresstests;

import java.util.ArrayList;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.util.distribution.DistributionScriptExecutionContext;
import com.topcoder.util.distribution.commands.CommandExecutionCondition;
import com.topcoder.util.distribution.commands.DefineVariableCommand;

/**
 * <p>
 * Stress tests for <code>DefineVariableCommand</code> class.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class DefineVariableCommandStressTest extends TestCase {
    /**
     * <p>
     * Returns the test suite of this class.
     * </p>
     *
     * @return the test suite of this class.
     */
    public static Test suite() {
        return new TestSuite(DefineVariableCommandStressTest.class);
    }

    /**
     * <p>
     * Stress test for method execute(DistributionScriptExecutionContext context).
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testExecute_Stress() throws Exception {
        DistributionScriptExecutionContext context = new DistributionScriptExecutionContext();
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10000; ++i) {
            DefineVariableCommand cmd = new DefineVariableCommand(null, new ArrayList<CommandExecutionCondition>(), "name" + i, "value" + i);
            cmd.execute(context);
        }
        System.out.println("Running DefineVariableCommand.execute() to define 10000 variables used "
                + (System.currentTimeMillis() - start) + "ms.");
        for (int i = 0; i < 10000; ++i) {
            assertEquals("The variable should be defined.", "value" + i, context.getVariable("name" + i));
        }
    }

}
