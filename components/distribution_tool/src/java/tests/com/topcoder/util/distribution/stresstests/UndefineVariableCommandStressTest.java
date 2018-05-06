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
import com.topcoder.util.distribution.commands.UndefineVariableCommand;

/**
 * <p>
 * Stress tests for <code>UndefineVariableCommand</code> class.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class UndefineVariableCommandStressTest extends TestCase {
    /**
     * <p>
     * Returns the test suite of this class.
     * </p>
     *
     * @return the test suite of this class.
     */
    public static Test suite() {
        return new TestSuite(UndefineVariableCommandStressTest.class);
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
        for (int i = 0; i < 20000; ++i) {
            context.setVariable("name" + i, "value" + i);
        }
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10000; ++i) {
            UndefineVariableCommand cmd = new UndefineVariableCommand(null, new ArrayList<CommandExecutionCondition>(), "name" + i);
            cmd.execute(context);
        }
        System.out.println("Running UndefineVariableCommand.execute() to undefine 10000 variables used "
                + (System.currentTimeMillis() - start) + "ms.");
        for (int i = 0; i < 10000; ++i) {
            assertTrue("The variable should be undefined", context.getVariable("name" + i) == null);
            assertTrue("The variable should not be undefined", context.getVariable("name" + (i + 10000)) != null);
        }
    }

}
