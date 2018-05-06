/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.distribution.commands;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.util.distribution.DistributionScriptExecutionContext;
import com.topcoder.util.distribution.commands.conditions.VariableDefinedCondition;
import com.topcoder.util.distribution.commands.conditions.VariableNotDefinedCondition;
import com.topcoder.util.log.Log;
import com.topcoder.util.log.LogManager;

/**
 * <p>
 * Unit tests for <code>BaseDistributionScriptCommand</code> class.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class BaseDistributionScriptCommandTest extends TestCase {
    /**
     * <p>
     * The Log object used for test.
     * </p>
     */
    private static final Log LOG = LogManager.getLog("MockCommand");

    /**
     * <p>
     * The MockCommand instance for test.
     * </p>
     */
    private MockCommand target;

    /**
     * <p>
     * Returns the test suite of this class.
     * </p>
     *
     * @return the test suite of this class.
     */
    public static Test suite() {
        return new TestSuite(BaseDistributionScriptCommandTest.class);
    }

    /**
     * Test accuracy of constructor BaseDistributionScriptCommand(Log log, List<CommandExecutionCondition> conditions).
     */
    public void testCtor_Accuracy() {
        target = new MockCommand(null, new ArrayList<CommandExecutionCondition>());

        assertNotNull("Unable to create the instance.", target);
        assertTrue("The object should be instance of BaseDistributionScriptCommand",
                target instanceof BaseDistributionScriptCommand);
    }

    /**
     * Test failure of constructor BaseDistributionScriptCommand(Log log, List<CommandExecutionCondition> conditions)
     * when conditions is null, IllegalArgumentException is expected.
     */
    public void testCtor_Fail1() {
        try {
            new MockCommand(LOG, null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test failure of constructor BaseDistributionScriptCommand(Log log, List<CommandExecutionCondition> conditions)
     * when conditions contains null, IllegalArgumentException is expected.
     */
    public void testCtor_Fail2() {
        List<CommandExecutionCondition> conditions = new ArrayList<CommandExecutionCondition>();
        conditions.add(null);
        try {
            new MockCommand(LOG, conditions);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test accuracy of method getLog().
     */
    public void testGetLog() {
        target = new MockCommand(null, new ArrayList<CommandExecutionCondition>());
        assertNull("The log should be null.", target.getLog());

        target = new MockCommand(LOG, new ArrayList<CommandExecutionCondition>());
        assertEquals("The log should be identical.", LOG, target.getLog());
    }

    /**
     * Test accuracy of method execute(DistributionScriptExecutionContext context).
     *
     * @throws Exception
     *             to JUnit
     */
    public void testExecute() throws Exception {
        List<CommandExecutionCondition> cons = new ArrayList<CommandExecutionCondition>();
        VariableDefinedCondition con1 = new VariableDefinedCondition("con1");
        VariableNotDefinedCondition con2 = new VariableNotDefinedCondition("con2");
        cons.add(con1);
        cons.add(con2);

        target = new MockCommand(LOG, cons);

        DistributionScriptExecutionContext context = new DistributionScriptExecutionContext();
        // to pass the check, set 'con1', don't set 'con2'
        context.setVariable("con1", "value1");

        target.execute(context);

        assertTrue("The command should be executed.", target.isExecuted());

        target = new MockCommand(LOG, cons);
        // this time, set 'con2'
        context.setVariable("con2", "value2");

        target.execute(context);

        assertFalse("The command should not be executed.", target.isExecuted());
    }

    /**
     * Test accuracy of method execute(DistributionScriptExecutionContext context) when context is null,
     * IllegalArgumentException is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testExecute_Fail() throws Exception {
        target = new MockCommand(null, new ArrayList<CommandExecutionCondition>());
        try {
            target.execute(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test accuracy of method String replaceVariableFields(String value, DistributionScriptExecutionContext context)
     */
    public void testReplaceVariableFields_Accuracy1() {
        target = new MockCommand(null, new ArrayList<CommandExecutionCondition>());
        DistributionScriptExecutionContext context = new DistributionScriptExecutionContext();
        context.setVariable("var1", "value1");
        context.setVariable("var2", "value2");

        String res = target.replaceVariableFields("The %{var1}, %{var2}, %{var1} will be replaced.", context);
        assertTrue("The var1 will be replaced with 'value1'", res.contains("value1"));
        assertTrue("The var2 will be replaced with 'value2'", res.contains("value2"));

        // two occurrences of 'var1'
        assertTrue("The value should match.", res.lastIndexOf("value1") > res.indexOf("value1"));
    }

    /**
     * Test accuracy of method String replaceVariableFields(String value, DistributionScriptExecutionContext context)
     */
    public void testReplaceVariableFields_Accuracy2() {
        target = new MockCommand(null, new ArrayList<CommandExecutionCondition>());
        DistributionScriptExecutionContext context = new DistributionScriptExecutionContext();
        context.setVariable("var1", "value1");
        context.setVariable("var2", "value2");

        String res = target.replaceVariableFields("The %{ not enclosed, % at last %", context);
        assertTrue("The value should match.", res.equals("The %{ not enclosed, % at last %"));
    }

    /**
     * Test accuracy of method String replaceVariableFields(String value, DistributionScriptExecutionContext context)
     */
    public void testReplaceVariableFields_Accuracy3() {
        target = new MockCommand(LOG, new ArrayList<CommandExecutionCondition>());
        DistributionScriptExecutionContext context = new DistributionScriptExecutionContext();
        context.setVariable("var1", "value1");
        context.setVariable("var2", "value2");

        String res = target.replaceVariableFields("%{var1}, %{var3}", context);
        assertTrue("The value should match.", res.equals("value1, %{var3}"));
    }
}
