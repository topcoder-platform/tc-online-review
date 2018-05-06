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

/**
 * <p>
 * Unit tests for <code>DefineVariableCommand</code> class.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class DefineVariableCommandTest extends TestCase {
    /**
     * <p>
     * The DefineVariableCommand instance for test.
     * </p>
     */
    private DefineVariableCommand target;

    /**
     * <p>
     * Returns the test suite of this class.
     * </p>
     *
     * @return the test suite of this class.
     */
    public static Test suite() {
        return new TestSuite(DefineVariableCommandTest.class);
    }

    /**
     * Test accuracy of constructor DefineVariableCommand(Log log, List<CommandExecutionCondition> conditions, String
     * name, String value).
     */
    public void testCtor_Accuracy() {
        target = new DefineVariableCommand(null, new ArrayList<CommandExecutionCondition>(), "name1", "value1");

        assertNotNull("Unable to create the instance.", target);
        assertTrue("The object should be instance of BaseDistributionScriptCommand",
                target instanceof BaseDistributionScriptCommand);
    }

    /**
     * Test failure of constructor DefineVariableCommand(Log log, List<CommandExecutionCondition> conditions, String
     * name, String value) when conditions is null, IllegalArgumentException is expected.
     */
    public void testCtor_Fail1() {
        try {
            new DefineVariableCommand(null, null, "name1", "value1");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test failure of constructor DefineVariableCommand(Log log, List<CommandExecutionCondition> conditions, String
     * name, String value) when conditions contains null, IllegalArgumentException is expected.
     */
    public void testCtor_Fail2() {
        List<CommandExecutionCondition> conditions = new ArrayList<CommandExecutionCondition>();
        conditions.add(null);
        try {
            new DefineVariableCommand(null, conditions, "name1", "value1");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test failure of constructor DefineVariableCommand(Log log, List<CommandExecutionCondition> conditions, String
     * name, String value) when name is null, IllegalArgumentException is expected.
     */
    public void testCtor_Fail3() {
        List<CommandExecutionCondition> conditions = new ArrayList<CommandExecutionCondition>();
        try {
            new DefineVariableCommand(null, conditions, null, "value1");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test failure of constructor DefineVariableCommand(Log log, List<CommandExecutionCondition> conditions, String
     * name, String value) when name is empty, IllegalArgumentException is expected.
     */
    public void testCtor_Fail4() {
        List<CommandExecutionCondition> conditions = new ArrayList<CommandExecutionCondition>();
        try {
            new DefineVariableCommand(null, conditions, "\t\t", "value1");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test failure of constructor DefineVariableCommand(Log log, List<CommandExecutionCondition> conditions, String
     * name, String value) when value is null, IllegalArgumentException is expected.
     */
    public void testCtor_Fail5() {
        List<CommandExecutionCondition> conditions = new ArrayList<CommandExecutionCondition>();
        try {
            new DefineVariableCommand(null, conditions, "name1", null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test accuracy of method executeCommand(DistributionScriptExecutionContext context).
     */
    public void testExecuteCommand_Accuracy() {
        target = new DefineVariableCommand(null, new ArrayList<CommandExecutionCondition>(), "name1", "value1");
        DistributionScriptExecutionContext context = new DistributionScriptExecutionContext();
        target.executeCommand(context);
        assertTrue("The command should success.", context.getVariable("name1").equals("value1"));
    }

    /**
     * Test failure of method executeCommand(DistributionScriptExecutionContext context) when context is null,
     * IllegalArgumentException is expected.
     */
    public void testExecuteCommand_Fail() {
        target = new DefineVariableCommand(null, new ArrayList<CommandExecutionCondition>(), "name1", "value1");
        try {
            target.executeCommand(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }
}
