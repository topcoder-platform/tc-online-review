/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.distribution.accuracytests.commands;

import com.topcoder.util.distribution.DistributionScriptCommand;
import com.topcoder.util.distribution.DistributionScriptExecutionContext;
import com.topcoder.util.distribution.accuracytests.TestHelper;
import com.topcoder.util.distribution.commands.CommandExecutionCondition;
import com.topcoder.util.distribution.commands.CopyFileTemplateCommand;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.ArrayList;

/**
 * Tests CopyFileTemplateCommand class.
 *
 * @author orange_cloud
 * @version 1.0
 */
public class CopyFileTemplateCommandTest extends TestCase {
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
        return new TestSuite(CopyFileTemplateCommandTest.class);
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
        String src = "%{root}/template.txt";
        String dest = "%{root}/temp/newfile.txt";
        target = new CopyFileTemplateCommand(null, new ArrayList<CommandExecutionCondition>(), src, dest);
        DistributionScriptExecutionContext context = new DistributionScriptExecutionContext();
        context.setVariable("root", "./test_files/accuracy");
        context.setVariable("abc", "123");
        context.setVariable("xyz", "000");
        target.execute(context);

        assertEquals("content", "1 123 2 %{a", TestHelper.readLine("test_files/accuracy/temp/newfile.txt"));
    }

    /**
     * Tests execute method.
     *
     * @throws Exception when it occurs deeper
     */
    public void testExecute2() throws Exception {
        String src = "%{root}/template.txt";
        String dest = "%{root}/temp/{FILENAME}.{EXT}";
        target = new CopyFileTemplateCommand(null, new ArrayList<CommandExecutionCondition>(), src, dest);
        DistributionScriptExecutionContext context = new DistributionScriptExecutionContext();
        context.setVariable("root", "./test_files/accuracy");
        context.setVariable("abc", "000");
        context.setVariable("xyz", "123");
        target.execute(context);

        assertEquals("content", "1 000 2 %{a", TestHelper.readLine("test_files/accuracy/temp/template.txt"));
    }

    /**
     * Tests execute method.
     *
     * @throws Exception when it occurs deeper
     */
    public void testExecute3() throws Exception {
        String src = "%{root}/test";
        String dest = "%{root}/temp/{FILENAME}{EXT}";
        target = new CopyFileTemplateCommand(null, new ArrayList<CommandExecutionCondition>(), src, dest);
        DistributionScriptExecutionContext context = new DistributionScriptExecutionContext();
        context.setVariable("root", "./test_files/accuracy");
        target.execute(context);

        assertEquals("content", "yet another file", TestHelper.readLine("test_files/accuracy/temp/test"));
    }

    /**
     * Tests execute method.
     *
     * @throws Exception when it occurs deeper
     */
    public void testExecute4() throws Exception {
        String src = "%{root}/test";
        String dest = "%{root}/temp/{FILENAME}.{EXT}";
        target = new CopyFileTemplateCommand(null, new ArrayList<CommandExecutionCondition>(), src, dest);
        DistributionScriptExecutionContext context = new DistributionScriptExecutionContext();
        context.setVariable("root", "./test_files/accuracy");
        target.execute(context);

        assertEquals("content", "yet another file", TestHelper.readLine("test_files/accuracy/temp/test."));
    }

    /**
     * Tests execute method.
     *
     * @throws Exception when it occurs deeper
     */
    public void testExecute5() throws Exception {
        String src = "%{root}/.txt";
        String dest = "%{root}/temp/{FILENAME}.{EXT}";
        target = new CopyFileTemplateCommand(null, new ArrayList<CommandExecutionCondition>(), src, dest);
        DistributionScriptExecutionContext context = new DistributionScriptExecutionContext();
        context.setVariable("root", "./test_files/accuracy");
        target.execute(context);

        assertEquals("content", "yet another file", TestHelper.readLine("test_files/accuracy/temp/.txt"));
    }
}
