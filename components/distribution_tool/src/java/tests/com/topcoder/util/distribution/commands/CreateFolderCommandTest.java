/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.distribution.commands;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.topcoder.util.distribution.BaseTest;
import com.topcoder.util.distribution.DistributionScriptExecutionContext;

/**
 * <p>
 * Unit tests for <code>CreateFolderCommand</code> class.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class CreateFolderCommandTest extends BaseTest {
    /**
     * <p>
     * The folder name used for test.
     * </p>
     */
    private static final String TEST_FOLDER = "test_files/test_dir/sub";

    /**
     * <p>
     * The CreateFolderCommand instance for test.
     * </p>
     */
    private CreateFolderCommand target;

    /**
     * <p>
     * Returns the test suite of this class.
     * </p>
     *
     * @return the test suite of this class.
     */
    public static Test suite() {
        return new TestSuite(CreateFolderCommandTest.class);
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
        target = new CreateFolderCommand(null, new ArrayList<CommandExecutionCondition>(), TEST_FOLDER);

        deleteFolder(TEST_FOLDER);
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
        target = null;
        deleteFolder(TEST_FOLDER);
    }

    /**
     * Test accuracy of constructor CreateFolderCommand(Log log, List<CommandExecutionCondition> conditions, String
     * folderPath).
     */
    public void testCtor_Accuracy() {
        target = new CreateFolderCommand(null, new ArrayList<CommandExecutionCondition>(), "test_files/test_dir");

        assertNotNull("Unable to create the instance.", target);
        assertTrue("The object should be instance of BaseDistributionScriptCommand",
                target instanceof BaseDistributionScriptCommand);
    }

    /**
     * Test failure of constructor CreateFolderCommand(Log log, List<CommandExecutionCondition> conditions, String
     * folderPath). when conditions is null, IllegalArgumentException is expected.
     */
    public void testCtor_Fail1() {
        try {
            new CreateFolderCommand(null, null, "test_files/test_dir");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test failure of constructor CreateFolderCommand(Log log, List<CommandExecutionCondition> conditions, String
     * folderPath) when conditions contains null, IllegalArgumentException is expected.
     */
    public void testCtor_Fail2() {
        List<CommandExecutionCondition> conditions = new ArrayList<CommandExecutionCondition>();
        conditions.add(null);
        try {
            new CreateFolderCommand(null, conditions, "test_files/test_dir");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test failure of constructor CreateFolderCommand(Log log, List<CommandExecutionCondition> conditions, String
     * folderPath) when folderPath is null, IllegalArgumentException is expected.
     */
    public void testCtor_Fail3() {
        List<CommandExecutionCondition> conditions = new ArrayList<CommandExecutionCondition>();
        try {
            new CreateFolderCommand(null, conditions, null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test failure of constructor CreateFolderCommand(Log log, List<CommandExecutionCondition> conditions, String
     * folderPath) when folderPath is empty, IllegalArgumentException is expected.
     */
    public void testCtor_Fail4() {
        List<CommandExecutionCondition> conditions = new ArrayList<CommandExecutionCondition>();
        try {
            new CreateFolderCommand(null, conditions, "\t\t");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test accuracy of method executeCommand(DistributionScriptExecutionContext context).
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testExecuteCommand_Accuracy() throws Exception {
        DistributionScriptExecutionContext context = new DistributionScriptExecutionContext();
        target.executeCommand(context);
        assertTrue("The folder should be created.", new File(TEST_FOLDER).isDirectory());
    }

    /**
     * Test failure of method executeCommand(DistributionScriptExecutionContext context) when context is null,
     * IllegalArgumentException is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testExecuteCommand_Fail() throws Exception {
        target = new CreateFolderCommand(null, new ArrayList<CommandExecutionCondition>(), TEST_FOLDER);
        try {
            target.executeCommand(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }
}
