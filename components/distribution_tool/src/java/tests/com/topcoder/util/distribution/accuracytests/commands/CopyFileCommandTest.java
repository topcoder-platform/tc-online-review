/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.distribution.accuracytests.commands;

import com.topcoder.util.distribution.DistributionScriptCommand;
import com.topcoder.util.distribution.DistributionScriptExecutionContext;
import com.topcoder.util.distribution.accuracytests.TestHelper;
import com.topcoder.util.distribution.commands.CommandExecutionCondition;
import com.topcoder.util.distribution.commands.CopyFileCommand;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.ArrayList;

/**
 * Tests CopyFileCommand class.
 *
 * @author orange_cloud
 * @version 1.0
 */
public class CopyFileCommandTest extends TestCase {
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
        return new TestSuite(CopyFileCommandTest.class);
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
    public void testExecute1() throws Exception {
        String src = "%{root}/test.txt";
        String dest = "%{root}/temp/newfile";
        target = new CopyFileCommand(null, new ArrayList<CommandExecutionCondition>(), src, dest);
        DistributionScriptExecutionContext context = new DistributionScriptExecutionContext();
        context.setVariable("root", "./test_files/accuracy");
        target.execute(context);

        assertEquals("content", "my favourite file", TestHelper.readLine("test_files/accuracy/temp/newfile"));
    }

    /**
     * Tests execute method.
     *
     * @throws Exception when it occurs deeper
     */
    public void testExecute2() throws Exception {
        String src = "%{root}/test.txt";
        String dest = "%{root}/temp/{FILENAME}.{EXT}";
        target = new CopyFileCommand(null, new ArrayList<CommandExecutionCondition>(), src, dest);
        DistributionScriptExecutionContext context = new DistributionScriptExecutionContext();
        context.setVariable("root", "./test_files/accuracy");
        target.execute(context);

        assertEquals("content", "my favourite file", TestHelper.readLine("test_files/accuracy/temp/test.txt"));
    }

    /**
     * Tests execute method.
     *
     * @throws Exception when it occurs deeper
     */
    public void testExecute3() throws Exception {
        String src = "%{root}/test";
        String dest = "%{root}/temp/{FILENAME}_{EXT}";
        target = new CopyFileCommand(null, new ArrayList<CommandExecutionCondition>(), src, dest);
        DistributionScriptExecutionContext context = new DistributionScriptExecutionContext();
        context.setVariable("root", "./test_files/accuracy");
        target.execute(context);

        assertEquals("content", "yet another file", TestHelper.readLine("test_files/accuracy/temp/test_"));
    }

    /**
     * Tests execute method.
     *
     * @throws Exception when it occurs deeper
     */
    public void testExecute4() throws Exception {
        String src = "%{root}/test";
        String dest = "%{root}/temp/_{FILENAME}._{EXT}";
        target = new CopyFileCommand(null, new ArrayList<CommandExecutionCondition>(), src, dest);
        DistributionScriptExecutionContext context = new DistributionScriptExecutionContext();
        context.setVariable("root", "./test_files/accuracy");
        target.execute(context);

        assertEquals("content", "yet another file", TestHelper.readLine("test_files/accuracy/temp/_test._"));
    }

    /**
     * Tests execute method.
     *
     * @throws Exception when it occurs deeper
     */
    public void testExecute5() throws Exception {
        String src = "%{root}/.txt";
        String dest = "%{root}/temp/a{FILENAME}a.{EXT}";
        target = new CopyFileCommand(null, new ArrayList<CommandExecutionCondition>(), src, dest);
        DistributionScriptExecutionContext context = new DistributionScriptExecutionContext();
        context.setVariable("root", "./test_files/accuracy");
        target.execute(context);

        assertEquals("content", "yet another file", TestHelper.readLine("test_files/accuracy/temp/aa.txt"));
    }
}
