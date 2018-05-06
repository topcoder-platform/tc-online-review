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
import com.topcoder.util.distribution.InputFileNotFoundException;

/**
 * <p>
 * Unit tests for <code>CopyFileTemplateCommand</code> class.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class CopyFileTemplateCommandTest extends BaseTest {
    /**
     * <p>
     * The source file for test.
     * </p>
     */
    private static final String TEST_SOURCE_FILE = "test_files/template.version";

    /**
     * <p>
     * The destination file name for test.
     * </p>
     */
    private static final String TEST_DEST_FILE = "test_files/test_copy_template/files/{FILENAME}.{EXT}";

    /**
     * <p>
     * The CopyFileTemplateCommand instance for test.
     * </p>
     */
    private CopyFileTemplateCommand target;

    /**
     * <p>
     * Returns the test suite of this class.
     * </p>
     *
     * @return the test suite of this class.
     */
    public static Test suite() {
        return new TestSuite(CopyFileTemplateCommandTest.class);
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
        target = new CopyFileTemplateCommand(null, new ArrayList<CommandExecutionCondition>(), TEST_SOURCE_FILE,
                TEST_DEST_FILE);

        deleteFolder("test_files/test_copy_template/files/");
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
        deleteFolder("test_files/test_copy_template/files/");
    }

    /**
     * Test accuracy of constructor CopyFileTemplateCommand(Log log, List<CommandExecutionCondition> conditions, String
     * sourcePath, String destPath).
     */
    public void testCtor_Accuracy() {
        target = new CopyFileTemplateCommand(null, new ArrayList<CommandExecutionCondition>(), "src", "dest");

        assertNotNull("Unable to create the instance.", target);
        assertTrue("The object should be instance of BaseDistributionScriptCommand",
                target instanceof BaseDistributionScriptCommand);
    }

    /**
     * Test failure of constructor CopyFileTemplateCommand(Log log, List<CommandExecutionCondition> conditions, String
     * sourcePath, String destPath) when conditions is null, IllegalArgumentException is expected.
     */
    public void testCtor_Fail1() {
        try {
            new CopyFileTemplateCommand(null, null, "src", "dest");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test failure of constructor CopyFileTemplateCommand(Log log, List<CommandExecutionCondition> conditions, String
     * sourcePath, String destPath) when conditions contains null, IllegalArgumentException is expected.
     */
    public void testCtor_Fail2() {
        List<CommandExecutionCondition> conditions = new ArrayList<CommandExecutionCondition>();
        conditions.add(null);
        try {
            new CopyFileTemplateCommand(null, conditions, "src", "dest");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test failure of constructor CopyFileTemplateCommand(Log log, List<CommandExecutionCondition> conditions, String
     * sourcePath, String destPath) when sourcePath is null, IllegalArgumentException is expected.
     */
    public void testCtor_Fail3() {
        List<CommandExecutionCondition> conditions = new ArrayList<CommandExecutionCondition>();
        try {
            new CopyFileTemplateCommand(null, conditions, null, "dest");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test failure of constructor CopyFileTemplateCommand(Log log, List<CommandExecutionCondition> conditions, String
     * sourcePath, String destPath) when sourcePath is empty, IllegalArgumentException is expected.
     */
    public void testCtor_Fail4() {
        List<CommandExecutionCondition> conditions = new ArrayList<CommandExecutionCondition>();
        try {
            new CopyFileTemplateCommand(null, conditions, "\t\t", "dest");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test failure of constructor CopyFileTemplateCommand(Log log, List<CommandExecutionCondition> conditions, String
     * sourcePath, String destPath) when destPath is null, IllegalArgumentException is expected.
     */
    public void testCtor_Fail5() {
        List<CommandExecutionCondition> conditions = new ArrayList<CommandExecutionCondition>();
        try {
            new CopyFileTemplateCommand(null, conditions, "src", null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test failure of constructor CopyFileTemplateCommand(Log log, List<CommandExecutionCondition> conditions, String
     * sourcePath, String destPath) when destPath is null, IllegalArgumentException is expected.
     */
    public void testCtor_Fail6() {
        List<CommandExecutionCondition> conditions = new ArrayList<CommandExecutionCondition>();
        try {
            new CopyFileTemplateCommand(null, conditions, "src", "   ");
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
        target = new CopyFileTemplateCommand(null, new ArrayList<CommandExecutionCondition>(), TEST_SOURCE_FILE,
                TEST_DEST_FILE);
        DistributionScriptExecutionContext context = new DistributionScriptExecutionContext();
        context.setVariable("Component Name", "Test Copy Template");
        context.setVariable("component_name", "xxx");
        context.setVariable("package.name", "yyy");
        context.setVariable("package/name", "zzz");
        context.setVariable("version.major", "1");
        context.setVariable("version.minor", "2");
        context.setVariable("version.micro", "3");
        context.setVariable("version.build", "4");

        new File("test_files/test_copy_template/files/").mkdirs();

        target.executeCommand(context);
        // String srcContent = getFileAsString(TEST_SOURCE_FILE);

        String destName = TEST_DEST_FILE.replace("{FILENAME}", CommandsUtil.getFileName(TEST_SOURCE_FILE)).replace(
                "{EXT}", CommandsUtil.getExtension(TEST_SOURCE_FILE));
    }

    /**
     * Test failure of method executeCommand(DistributionScriptExecutionContext context) when context is null,
     * IllegalArgumentException is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testExecuteCommand_Fail() throws Exception {
        target = new CopyFileTemplateCommand(null, new ArrayList<CommandExecutionCondition>(), "src", "dest");
        try {
            target.executeCommand(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test failure of method executeCommand(DistributionScriptExecutionContext context) when the source file doesn't
     * exist.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testExecuteCommand_Fail1() throws Exception {
        target = new CopyFileTemplateCommand(null, new ArrayList<CommandExecutionCondition>(),
                "no source file", "dest");
        try {
            target.executeCommand(new DistributionScriptExecutionContext());
            fail("InputFileNotFoundException is expected.");
        } catch (InputFileNotFoundException e) {
            // good
        }
    }
}
