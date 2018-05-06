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
 * Unit tests for <code>UndefineVariableCommand</code> class.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class UndefineVariableCommandTest extends TestCase {
    /**
     * <p>
     * The UndefineVariableCommand instance for test.
     * </p>
     */
    private UndefineVariableCommand target;

    /**
     * <p>
     * Returns the test suite of this class.
     * </p>
     *
     * @return the test suite of this class.
     */
    public static Test suite() {
        return new TestSuite(UndefineVariableCommandTest.class);
    }

    /**
     * Test accuracy of constructor UndefineVariableCommand(Log log, List<CommandExecutionCondition> conditions, String
     * name).
     */
    public void testCtor_Accuracy() {
        target = new UndefineVariableCommand(null, new ArrayList<CommandExecutionCondition>(), "name1");

        assertNotNull("Unable to create the instance.", target);
        assertTrue("The object should be instance of BaseDistributionScriptCommand",
                target instanceof BaseDistributionScriptCommand);
    }

    /**
     * Test failure of constructor UndefineVariableCommand(Log log, List<CommandExecutionCondition> conditions, String
     * name) when conditions is null, IllegalArgumentException is expected.
     */
    public void testCtor_Fail1() {
        try {
            new UndefineVariableCommand(null, null, "name1");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test failure of constructor UndefineVariableCommand(Log log, List<CommandExecutionCondition> conditions, String
     * name) when conditions contains null, IllegalArgumentException is expected.
     */
    public void testCtor_Fail2() {
        List<CommandExecutionCondition> conditions = new ArrayList<CommandExecutionCondition>();
        conditions.add(null);
        try {
            new UndefineVariableCommand(null, conditions, "name1");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test failure of constructor UndefineVariableCommand(Log log, List<CommandExecutionCondition> conditions, String
     * name) when name is null, IllegalArgumentException is expected.
     */
    public void testCtor_Fail3() {
        List<CommandExecutionCondition> conditions = new ArrayList<CommandExecutionCondition>();
        try {
            new UndefineVariableCommand(null, conditions, null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test failure of constructor UndefineVariableCommand(Log log, List<CommandExecutionCondition> conditions, String
     * name) when name is empty, IllegalArgumentException is expected.
     */
    public void testCtor_Fail4() {
        List<CommandExecutionCondition> conditions = new ArrayList<CommandExecutionCondition>();
        try {
            new UndefineVariableCommand(null, conditions, "\t\t");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test accuracy of method executeCommand(DistributionScriptExecutionContext context).
     */
    public void testExecuteCommand_Accuracy() {
        target = new UndefineVariableCommand(null, new ArrayList<CommandExecutionCondition>(), "name1");
        DistributionScriptExecutionContext context = new DistributionScriptExecutionContext();
        context.setVariable("name1", "value1");
        target.executeCommand(context);
        assertTrue("The command should success.", context.getVariable("name1") == null);
    }

    /**
     * Test failure of method executeCommand(DistributionScriptExecutionContext context) when context is null,
     * IllegalArgumentException is expected.
     */
    public void testExecuteCommand_Fail() {
        target = new UndefineVariableCommand(null, new ArrayList<CommandExecutionCondition>(), "name1");
        try {
            target.executeCommand(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }
}
