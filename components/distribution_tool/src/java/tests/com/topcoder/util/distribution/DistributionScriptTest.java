/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.distribution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.topcoder.util.distribution.commands.CommandExecutionCondition;
import com.topcoder.util.distribution.commands.DefineVariableCommand;
import com.topcoder.util.distribution.commands.UndefineVariableCommand;
import com.topcoder.util.log.Log;
import com.topcoder.util.log.LogManager;

/**
 * <p>
 * Unit tests for <code>DistributionScript</code> class.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class DistributionScriptTest extends BaseTest {
    /**
     * <p>
     * The Log object used for test.
     * </p>
     */
    private static final Log LOG = LogManager.getLog("Test DistributionScript");

    /**
     * <p>
     * The DistributionScript instance for test.
     * </p>
     */
    private DistributionScript target;

    /**
     * <p>
     * Returns the test suite of this class.
     * </p>
     *
     * @return the test suite of this class.
     */
    public static Test suite() {
        return new TestSuite(DistributionScriptTest.class);
    }

    /**
     * Test accuracy of constructor DistributionScript().
     *
     * @throws Exception
     *             to JUnit.
     */
    @SuppressWarnings("unchecked")
    public void testCtor_Accuracy() throws Exception {
        target = new DistributionScript();
        assertNotNull("Unable to create the instance.", target);

        List<DistributionScriptCommand> commands = (List<DistributionScriptCommand>) getPrivateField(
                DistributionScript.class, target, "commands");
        assertTrue("The variables mapping should be empty.", commands.size() == 0);

        List<String> requiredParams = (List<String>)
            getPrivateField(DistributionScript.class, target, "requiredParams");
        assertTrue("The variables mapping should be empty.", requiredParams.size() == 0);

        Map<String, String> defaultParams = (Map<String, String>) getPrivateField(DistributionScript.class, target,
                "defaultParams");
        assertTrue("The variables mapping should be empty.", defaultParams.size() == 0);
    }

    /**
     * Test accuracy of method setCommands(List<DistributionScriptCommand> commands).
     */
    public void testSetCommands_Accuracy() {
        target = new DistributionScript();
        List<DistributionScriptCommand> commands = new ArrayList<DistributionScriptCommand>();
        DistributionScriptCommand cmd1 = new DefineVariableCommand(LOG, new ArrayList<CommandExecutionCondition>(),
                "cmd1", "ttt");
        DistributionScriptCommand cmd2 = new UndefineVariableCommand(LOG, new ArrayList<CommandExecutionCondition>(),
                "cmd2");
        commands.add(cmd1);
        commands.add(cmd2);

        target.setCommands(commands);
        assertEquals("The commands should have two items now.", 2, target.getCommands().size());
        assertTrue("The command should be instance of DefineVariableCommand.",
                target.getCommands().get(0) instanceof DefineVariableCommand);
        assertTrue("The command should be instance of UndefineVariableCommand.",
                target.getCommands().get(1) instanceof UndefineVariableCommand);
    }

    /**
     * Test failure of method setCommands(List<DistributionScriptCommand> commands) when commands is null,
     * IllegalArgumentException is expected.
     */
    public void testSetCommands_Fail1() {
        target = new DistributionScript();

        try {
            target.setCommands(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test failure of method setCommands(List<DistributionScriptCommand> commands) when commands contains null,
     * IllegalArgumentException is expected.
     */
    public void testSetCommands_Fail2() {
        target = new DistributionScript();
        List<DistributionScriptCommand> commands = new ArrayList<DistributionScriptCommand>();
        DistributionScriptCommand cmd1 = new DefineVariableCommand(LOG, new ArrayList<CommandExecutionCondition>(),
                "cmd1", "ttt");
        // DistributionScriptCommand cmd2 = new UndefineVariableCommand(LOG, new ArrayList<CommandExecutionCondition>(),
        // "cmd2");
        commands.add(cmd1);
        commands.add(null);
        try {
            target.setCommands(commands);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test accuracy of method getCommands().
     */
    public void testGetCommands_Accuracy() {
        target = new DistributionScript();
        List<DistributionScriptCommand> commands = new ArrayList<DistributionScriptCommand>();
        DistributionScriptCommand cmd1 = new DefineVariableCommand(LOG, new ArrayList<CommandExecutionCondition>(),
                "cmd1", "ttt");
        DistributionScriptCommand cmd2 = new UndefineVariableCommand(LOG, new ArrayList<CommandExecutionCondition>(),
                "cmd2");
        commands.add(cmd1);
        commands.add(cmd2);

        target.setCommands(commands);
        assertEquals("The commands should have two items now.", 2, target.getCommands().size());
        assertTrue("The command should be instance of DefineVariableCommand.",
                target.getCommands().get(0) instanceof DefineVariableCommand);
        assertTrue("The command should be instance of UndefineVariableCommand.",
                target.getCommands().get(1) instanceof UndefineVariableCommand);
    }

    /**
     * Test accuracy of method setRequiredParams(List<String> params).
     */
    public void testSetRequiredParams_Accuracy() {
        target = new DistributionScript();
        List<String> params = new ArrayList<String>();
        params.add("required1");
        params.add("required2");

        target.setRequiredParams(params);
        assertEquals("The params should have two items now.", 2, target.getRequiredParams().size());
        assertTrue("The value should match.", target.getRequiredParams().get(0).equals("required1"));
        assertTrue("The value should match.", target.getRequiredParams().get(1).equals("required2"));
    }

    /**
     * Test failure of method setRequiredParams(List<String> params) when params is null,
     * IllegalArgumentException is expected.
     */
    public void testSetRequiredParams_Fail1() {
        target = new DistributionScript();

        try {
            target.setRequiredParams(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test failure of method setRequiredParams(List<String> params) when params contains null, IllegalArgumentException
     * is expected.
     */
    public void testSetRequiredParams_Fail2() {
        target = new DistributionScript();
        List<String> params = new ArrayList<String>();
        params.add("contains null");
        params.add(null);
        try {
            target.setRequiredParams(params);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test failure of method setRequiredParams(List<String> params) when params contains empty,
     * IllegalArgumentException is expected.
     */
    public void testSetRequiredParams_Fail3() {
        target = new DistributionScript();
        List<String> params = new ArrayList<String>();
        params.add("contains empty");
        params.add("   \t\t");
        try {
            target.setRequiredParams(params);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test accuracy of method getRequiredParams().
     */
    public void testGetRequiredParams_Accuracy() {
        target = new DistributionScript();
        List<String> params = new ArrayList<String>();
        params.add("required1");
        params.add("required2");

        target.setRequiredParams(params);
        assertEquals("The params should have two items now.", 2, target.getRequiredParams().size());
        assertTrue("The value should match.", target.getRequiredParams().get(0).equals("required1"));
        assertTrue("The value should match.", target.getRequiredParams().get(1).equals("required2"));
    }

    /**
     * Test accuracy of method setScriptFolder(String folder).
     */
    public void testSetScriptFolder_Accuracy() {
        target = new DistributionScript();

        target.setScriptFolder("folder");
        assertTrue("The value should match.", target.getScriptFolder().equals("folder"));
    }

    /**
     * Test failure of method setScriptFolder(String folder) when the folder is null, IllegalArgumentException is
     * expected.
     */
    public void testSetScriptFolder_Fail1() {
        target = new DistributionScript();

        try {
            target.setScriptFolder(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test failure of method setScriptFolder(String folder) when the folder is empty, IllegalArgumentException is
     * expected.
     */
    public void testSetScriptFolder_Fail2() {
        target = new DistributionScript();

        try {
            target.setScriptFolder("    ");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test accuracy of method getScriptFolder().
     */
    public void testGetScriptFolder_Accuracy() {
        target = new DistributionScript();

        target.setScriptFolder("folder");
        assertTrue("The value should match.", target.getScriptFolder().equals("folder"));
    }

    /**
     * Test accuracy of method setDefaultParams(Map<String, String> defaultParams).
     */
    public void testSetDefaultParams_Accuracy() {
        target = new DistributionScript();
        Map<String, String> params = new HashMap<String, String>();
        params.put("default1", "value1");
        params.put("default2", "value2");

        target.setDefaultParams(params);
        assertEquals("The params should have two items now.", 2, target.getDefaultParams().size());
        assertTrue("The value should match.", target.getDefaultParams().get("default1").equals("value1"));
        assertTrue("The value should match.", target.getDefaultParams().get("default2").equals("value2"));
    }

    /**
     * Test failure of method setDefaultParams(Map<String, String> defaultParams) when defaultParams is null,
     * IllegalArgumentException is expected.
     */
    public void testSetDefaultParams_Fail1() {
        target = new DistributionScript();

        try {
            target.setDefaultParams(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test failure of method setDefaultParams(Map<String, String> defaultParams) when defaultParams contains null key,
     * IllegalArgumentException is expected.
     */
    public void testSetDefaultParams_Fail2() {
        target = new DistributionScript();
        Map<String, String> params = new HashMap<String, String>();
        params.put("default1", "value1");
        params.put(null, "null key");
        try {
            target.setDefaultParams(params);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test failure of method setDefaultParams(Map<String, String> defaultParams) when defaultParams contains empty key,
     * IllegalArgumentException is expected.
     */
    public void testSetDefaultParams_Fail3() {
        target = new DistributionScript();
        Map<String, String> params = new HashMap<String, String>();
        params.put("default1", "value1");
        params.put("   ", "empty key");
        try {
            target.setDefaultParams(params);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test failure of method setDefaultParams(Map<String, String> defaultParams) when defaultParams contains null
     * value, IllegalArgumentException is expected.
     */
    public void testSetDefaultParams_Fail4() {
        target = new DistributionScript();
        Map<String, String> params = new HashMap<String, String>();
        params.put("default1", "value1");
        params.put("null value", null);
        try {
            target.setDefaultParams(params);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }
}
