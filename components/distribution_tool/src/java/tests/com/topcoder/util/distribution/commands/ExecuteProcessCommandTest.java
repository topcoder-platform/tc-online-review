/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.distribution.commands;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.util.distribution.DistributionScriptExecutionContext;

/**
 * <p>
 * Unit tests for <code>ExecuteProcessCommand</code> class.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class ExecuteProcessCommandTest extends TestCase {
    /**
     * <p>
     * The ExecuteProcessCommand instance for test.
     * </p>
     */
    private ExecuteProcessCommand target;

    /**
     * <p>
     * Returns the test suite of this class.
     * </p>
     *
     * @return the test suite of this class.
     */
    public static Test suite() {
        return new TestSuite(ExecuteProcessCommandTest.class);
    }

    /**
     * <p>
     * Sets up test environment.
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    public void setUp() throws Exception {
        super.setUp();
        File file = new File("test_files/temp");
        if (file.isDirectory()) {
            file.delete();
        }
    }

    /**
     * <p>
     * Tears down test environment.
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    public void tearDown() throws Exception {
        super.tearDown();
        File file = new File("test_files/temp");
        if (file.isDirectory()) {
            file.delete();
        }
    }

    /**
     * Test accuracy of constructor ExecuteProcessCommand(Log log, List<CommandExecutionCondition> conditions, String
     * command, String workingPath).
     */
    public void testCtor_Accuracy() {
        target = new ExecuteProcessCommand(null, new ArrayList<CommandExecutionCondition>(), "cmd /c echo test", ".");

        assertNotNull("Unable to create the instance.", target);
        assertTrue("The object should be instance of BaseDistributionScriptCommand",
                target instanceof BaseDistributionScriptCommand);
    }

    /**
     * Test failure of constructor ExecuteProcessCommand(Log log, List<CommandExecutionCondition> conditions, String
     * command, String workingPath) when conditions is null, IllegalArgumentException is expected.
     */
    public void testCtor_Fail1() {
        try {
            new ExecuteProcessCommand(null, null, "cmd /c echo test", ".");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test failure of constructor ExecuteProcessCommand(Log log, List<CommandExecutionCondition> conditions, String
     * command, String workingPath) when conditions contains null, IllegalArgumentException is expected.
     */
    public void testCtor_Fail2() {
        List<CommandExecutionCondition> conditions = new ArrayList<CommandExecutionCondition>();
        conditions.add(null);
        try {
            new ExecuteProcessCommand(null, conditions, "cmd /c echo test", ".");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test failure of constructor ExecuteProcessCommand(Log log, List<CommandExecutionCondition> conditions, String
     * command, String workingPath) when command is null, IllegalArgumentException is expected.
     */
    public void testCtor_Fail3() {
        List<CommandExecutionCondition> conditions = new ArrayList<CommandExecutionCondition>();
        try {
            new ExecuteProcessCommand(null, conditions, null, "value1");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test failure of constructor ExecuteProcessCommand(Log log, List<CommandExecutionCondition> conditions, String
     * command, String workingPath) when command is empty, IllegalArgumentException is expected.
     */
    public void testCtor_Fail4() {
        List<CommandExecutionCondition> conditions = new ArrayList<CommandExecutionCondition>();
        try {
            new ExecuteProcessCommand(null, conditions, "\t\t", "value1");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test failure of constructor ExecuteProcessCommand(Log log, List<CommandExecutionCondition> conditions, String
     * command, String workingPath) when workingPath is null, IllegalArgumentException is expected.
     */
    public void testCtor_Fail5() {
        List<CommandExecutionCondition> conditions = new ArrayList<CommandExecutionCondition>();
        try {
            new ExecuteProcessCommand(null, conditions, "echo test", null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test failure of constructor ExecuteProcessCommand(Log log, List<CommandExecutionCondition> conditions, String
     * command, String workingPath) when workingPath is empty, IllegalArgumentException is expected.
     */
    public void testCtor_Fail6() {
        List<CommandExecutionCondition> conditions = new ArrayList<CommandExecutionCondition>();
        try {
            new ExecuteProcessCommand(null, conditions, "echo test", "    ");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test accuracy of method executeCommand(DistributionScriptExecutionContext context).
     *
     * @throws Exception
     *             to JUnit
     */
    public void testExecuteCommand_Accuracy() throws Exception {
        // you may need to change the command if testing under windows
        target = new ExecuteProcessCommand(null, new ArrayList<CommandExecutionCondition>(), "mkdir test_files/temp", ".");
        DistributionScriptExecutionContext context = new DistributionScriptExecutionContext();
        target.executeCommand(context);
        assertTrue("The dir shoule be created.", new File("test_files/temp").isDirectory());
    }

    /**
     * Test failure of method executeCommand(DistributionScriptExecutionContext context) when context is null,
     * IllegalArgumentException is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testExecuteCommand_Fail() throws Exception {
        target = new ExecuteProcessCommand(null, new ArrayList<CommandExecutionCondition>(), "name1", "value1");
        try {
            target.executeCommand(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }
}
