/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.distribution.accuracytests;

import com.topcoder.util.distribution.DistributionScript;
import com.topcoder.util.distribution.DistributionScriptCommand;
import com.topcoder.util.distribution.commands.CommandExecutionCondition;
import com.topcoder.util.distribution.commands.DefineVariableCommand;
import com.topcoder.util.distribution.commands.UndefineVariableCommand;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Tests DistributionScript class.
 *
 * @author orange_cloud
 * @version 1.0
 */
public class DistributionScriptTest extends TestCase {
    /**
     * Instance to test.
     */
    private DistributionScript target;

    /**
     * <p>Returns the test suite of this class.</p>
     *
     * @return the test suite of this class.
     */
    public static Test suite() {
        return new TestSuite(DistributionScriptTest.class);
    }

    /**
     * <p>Sets up test environment.</p>
     *
     * @throws Exception to junit
     */
    public void setUp() throws Exception {
        super.setUp();
        TestHelper.clearTemp();
        target = new DistributionScript();
    }

    /**
     * <p>Tears down test environment.</p>
     *
     * @throws Exception to junit
     */
    public void tearDown() throws Exception {
        TestHelper.clearTemp();
        super.tearDown();
    }

    /**
     * Tests setter/getter for commands property.
     *
     * @throws Exception when it occurs deeper
     */
    public void testCommands() throws Exception {
        assertEquals("default value", 0, target.getCommands().size());
        target.setCommands(new ArrayList<DistributionScriptCommand>());

        target.setCommands(Arrays.<DistributionScriptCommand>asList(
            new UndefineVariableCommand(null, new ArrayList<CommandExecutionCondition>(), "abc"),
            new DefineVariableCommand(null, new ArrayList<CommandExecutionCondition>(), "abc", "xyz")));
        assertEquals("new value", 2, target.getCommands().size());
    }

    /**
     * Tests setter/getter for requiredParams property.
     *
     * @throws Exception when it occurs deeper
     */
    public void testRequiredParams() throws Exception {
        assertEquals("default value", 0, target.getRequiredParams().size());
        target.setRequiredParams(new ArrayList<String>());

        target.setRequiredParams(Arrays.asList("abc", "xyz"));
        assertEquals("new value", Arrays.asList("abc", "xyz"), target.getRequiredParams());
    }

    /**
     * Tests setter/getter for defaultParams property.
     *
     * @throws Exception when it occurs deeper
     */
    public void testDefaultParams() throws Exception {
        assertEquals("default value", 0, target.getDefaultParams().size());
        target.setDefaultParams(new HashMap<String, String>());

        Map<String, String> map = new HashMap<String, String>();
        map.put("abc", "xyz");
        map.put("pqr", "");
        target.setDefaultParams(map);
        assertEquals("new value", map, target.getDefaultParams());
    }

    /**
     * Tests setter/getter for scriptFolder property.
     *
     * @throws Exception when it occurs deeper
     */
    public void testScriptFolder() throws Exception {
        assertNull("default value", target.getScriptFolder());
        target.setScriptFolder("abc");
        assertEquals("new value", "abc", target.getScriptFolder());
    }
}
