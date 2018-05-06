/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.distribution.accuracytests.commands;

import com.topcoder.util.distribution.DistributionScriptCommand;
import com.topcoder.util.distribution.DistributionScriptExecutionContext;
import com.topcoder.util.distribution.accuracytests.TestHelper;
import com.topcoder.util.distribution.commands.CommandExecutionCondition;
import com.topcoder.util.distribution.commands.CreateFolderCommand;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.File;
import java.util.ArrayList;

/**
 * Tests CreateFolderCommand class.
 *
 * @author orange_cloud
 * @version 1.0
 */
public class CreateFolderCommandTest extends TestCase {
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
        return new TestSuite(CreateFolderCommandTest.class);
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
        String dest = "%{root}/temp/newdir";
        target = new CreateFolderCommand(null, new ArrayList<CommandExecutionCondition>(), dest);
        DistributionScriptExecutionContext context = new DistributionScriptExecutionContext();
        context.setVariable("root", "./test_files/accuracy");
        target.execute(context);

        assertTrue("directory", new File("test_files/accuracy/temp/newdir").isDirectory());
    }

    /**
     * Tests execute method when name is already occupied. No error expected.
     *
     * @throws Exception when it occurs deeper
     */
    public void testExecute2() throws Exception {
        String dest = "test_files/accuracy/test";
        target = new CreateFolderCommand(null, new ArrayList<CommandExecutionCondition>(), dest);
        DistributionScriptExecutionContext context = new DistributionScriptExecutionContext();
        context.setVariable("root", "./test_files/accuracy");
        target.execute(context);

        assertFalse("no directory", new File("test_files/accuracy/temp/test").isDirectory());
    }

    /**
     * Tests execute method for a chain of directories.
     *
     * @throws Exception when it occurs deeper
     */
    public void testExecute3() throws Exception {
        String dest = "test_files/accuracy/temp/dir1/dir2/dir3";
        target = new CreateFolderCommand(null, new ArrayList<CommandExecutionCondition>(), dest);
        DistributionScriptExecutionContext context = new DistributionScriptExecutionContext();
        context.setVariable("root", "./test_files/accuracy");
        target.execute(context);

        assertTrue("directory", new File("test_files/accuracy/temp/dir1/dir2/dir3").isDirectory());
    }
}
