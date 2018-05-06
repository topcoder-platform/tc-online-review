/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.distribution.stresstests;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.util.distribution.DistributionScriptExecutionContext;
import com.topcoder.util.distribution.commands.CommandExecutionCondition;
import com.topcoder.util.distribution.commands.conditions.VariableDefinedCondition;
import com.topcoder.util.distribution.commands.conditions.VariableNotDefinedCondition;
import com.topcoder.util.log.Log;
import com.topcoder.util.log.LogManager;

/**
 * <p>
 * Stress tests for <code>BaseDistributionScriptCommand</code> class.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class BaseDistributionScriptCommandStressTest extends TestCase {
    /**
     * <p>
     * The Log object used for test.
     * </p>
     */
    private static final Log LOG = LogManager.getLog(MockDistributionScriptCommand.class.getName());

    /**
     * <p>
     * Returns the test suite of this class.
     * </p>
     *
     * @return the test suite of this class.
     */
    public static Test suite() {
        return new TestSuite(BaseDistributionScriptCommandStressTest.class);
    }

    /**
     * <p>
     * Stress test for method BaseDistributionScriptCommand.execute(). This case is mainly
     * for the check of the conditions when executing a command.
     * </p>
     * @throws Exception to JUnit
     */
    public void testReplaceVariableFields_Stress() throws Exception {
        List<CommandExecutionCondition> cons = new ArrayList<CommandExecutionCondition>();
        for (int i = 0; i < 10000; i++) {
            if (i % 2 == 0) {
                cons.add(new VariableDefinedCondition("con" + i));
            } else {
                cons.add(new VariableNotDefinedCondition("con" + i));
            }
        }

        DistributionScriptExecutionContext context = new DistributionScriptExecutionContext();
        for (int i = 0; i < 10000; i++) {
            if (i % 2 == 0) {
                context.setVariable("con" + i, "value");
            }
        }

        MockDistributionScriptCommand mockCmd = new MockDistributionScriptCommand(LOG, cons);

        long start = System.currentTimeMillis();
        mockCmd.execute(context);
        System.out.println("Running BaseDistributionScriptCommand.replaceVariableFields() to check 10000 conditions used " + (System.currentTimeMillis() - start) + "ms.");
        assertTrue("The command should be executed.", "executed".equals(mockCmd.getMessage()));

        mockCmd = new MockDistributionScriptCommand(LOG, cons);
        context.setVariable("con1", "value");
        mockCmd.execute(context);
        // This time, check will fail
        assertNull("The command should not be executed.", mockCmd.getMessage());
    }
}
