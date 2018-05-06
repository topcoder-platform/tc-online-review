/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.distribution.accuracytests.commands;

import com.topcoder.util.distribution.DistributionScriptCommand;
import com.topcoder.util.distribution.DistributionScriptExecutionContext;
import com.topcoder.util.distribution.accuracytests.TestHelper;
import com.topcoder.util.distribution.commands.CommandExecutionCondition;
import com.topcoder.util.distribution.commands.ExecuteProcessCommand;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.File;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Tests ExecuteProcessCommand class.
 *
 * @author orange_cloud
 * @version 1.0
 */
public class ExecuteProcessCommandTest extends TestCase {
    /**
     * Instance to test.
     */
    private DistributionScriptCommand target;

    /**
     * <p>Returns the test suite of this class.</p>
     *
     * @return the test suite of this class.
     */
    public static Test suite() {
        return new TestSuite(ExecuteProcessCommandTest.class);
    }

    /**
     * <p>Sets up test environment.</p>
     *
     * @throws Exception to junit
     */
    public void setUp() throws Exception {
        super.setUp();
        TestHelper.clearTemp();
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
     * Tests execute method.
     *
     * @throws Exception when it occurs deeper
     */
    public void testExecute() throws Exception {
        String dir = "%{root}/temp";
        Properties p = new Properties();
        p.load(ClassLoader.getSystemClassLoader().getResourceAsStream("accuracy/test.properties"));
        String command = p.getProperty("create_directory_command");

        target = new ExecuteProcessCommand(null, new ArrayList<CommandExecutionCondition>(), command, dir);
        DistributionScriptExecutionContext context = new DistributionScriptExecutionContext();
        context.setVariable("name", "mydir");
        context.setVariable("root", "./test_files/accuracy");
        target.execute(context);

        assertTrue("directory", new File("test_files/accuracy/temp/mydir").isDirectory());
    }

    /**
     * Tests execute method.
     *
     * @throws Exception when it occurs deeper
     */
    public void testExecute2() throws Exception {
        String dir = "%{root}";
        Properties p = new Properties();
        p.load(ClassLoader.getSystemClassLoader().getResourceAsStream("accuracy/test.properties"));
        String command = p.getProperty("call_ant_command");

        target = new ExecuteProcessCommand(null, new ArrayList<CommandExecutionCondition>(), command, dir);
        DistributionScriptExecutionContext context = new DistributionScriptExecutionContext();
        context.setVariable("root", "./test_files/accuracy");
        long startTime = System.currentTimeMillis();
        target.execute(context);
        long time = System.currentTimeMillis() - startTime;
        assertTrue("task is completed", time > 1800);
    }
}
