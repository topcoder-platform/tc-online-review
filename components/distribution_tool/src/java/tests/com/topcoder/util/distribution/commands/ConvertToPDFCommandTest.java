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
 * Unit tests for <code>ConvertToPDFCommand</code> class.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class ConvertToPDFCommandTest extends BaseTest {
    /**
     * <p>
     * The source file for test.
     * </p>
     */
    private static final String TEST_SOURCE_FILE = "test_files/test_convert_file.rtf";

    /**
     * <p>
     * The destination file name for test.
     * </p>
     */
    private static final String TEST_DEST_FILE = "test_files/test_convert_files/files/{FILENAME}.pdf";

    /**
     * <p>
     * The ConvertToPDFCommand instance for test.
     * </p>
     */
    private ConvertToPDFCommand target;

    /**
     * <p>
     * Returns the test suite of this class.
     * </p>
     *
     * @return the test suite of this class.
     */
    public static Test suite() {
        return new TestSuite(ConvertToPDFCommandTest.class);
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
        target = new ConvertToPDFCommand(null, new ArrayList<CommandExecutionCondition>(), TEST_SOURCE_FILE,
                TEST_DEST_FILE);

        deleteFolder("test_files/test_convert_files/files/");

        File file = new File("test_files/tmp.pdf");
        if (file.isFile()) {
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
        target = null;
        deleteFolder("test_files/test_convert_files/files/");

        File file = new File("test_files/tmp.pdf");
        if (file.isFile()) {
            file.delete();
        }
    }

    /**
     * Test accuracy of constructor ConvertToPDFCommand(Log log, List<CommandExecutionCondition> conditions, String
     * sourcePath, String destPath).
     */
    public void testCtor_Accuracy() {
        target = new ConvertToPDFCommand(null, new ArrayList<CommandExecutionCondition>(), "src", "dest");

        assertNotNull("Unable to create the instance.", target);
        assertTrue("The object should be instance of BaseDistributionScriptCommand",
                target instanceof BaseDistributionScriptCommand);
    }

    /**
     * Test failure of constructor ConvertToPDFCommand(Log log, List<CommandExecutionCondition> conditions, String
     * sourcePath, String destPath) when conditions is null, IllegalArgumentException is expected.
     */
    public void testCtor_Fail1() {
        try {
            new ConvertToPDFCommand(null, null, "src", "dest");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test failure of constructor ConvertToPDFCommand(Log log, List<CommandExecutionCondition> conditions, String
     * sourcePath, String destPath) when conditions contains null, IllegalArgumentException is expected.
     */
    public void testCtor_Fail2() {
        List<CommandExecutionCondition> conditions = new ArrayList<CommandExecutionCondition>();
        conditions.add(null);
        try {
            new ConvertToPDFCommand(null, conditions, "src", "dest");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test failure of constructor ConvertToPDFCommand(Log log, List<CommandExecutionCondition> conditions, String
     * sourcePath, String destPath) when sourcePath is null, IllegalArgumentException is expected.
     */
    public void testCtor_Fail3() {
        List<CommandExecutionCondition> conditions = new ArrayList<CommandExecutionCondition>();
        try {
            new ConvertToPDFCommand(null, conditions, null, "dest");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test failure of constructor ConvertToPDFCommand(Log log, List<CommandExecutionCondition> conditions, String
     * sourcePath, String destPath) when sourcePath is empty, IllegalArgumentException is expected.
     */
    public void testCtor_Fail4() {
        List<CommandExecutionCondition> conditions = new ArrayList<CommandExecutionCondition>();
        try {
            new ConvertToPDFCommand(null, conditions, "\t\t", "dest");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test failure of constructor ConvertToPDFCommand(Log log, List<CommandExecutionCondition> conditions, String
     * sourcePath, String destPath) when destPath is null, IllegalArgumentException is expected.
     */
    public void testCtor_Fail5() {
        List<CommandExecutionCondition> conditions = new ArrayList<CommandExecutionCondition>();
        try {
            new ConvertToPDFCommand(null, conditions, "src", null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test failure of constructor ConvertToPDFCommand(Log log, List<CommandExecutionCondition> conditions, String
     * sourcePath, String destPath) when destPath is empty, IllegalArgumentException is expected.
     */
    public void testCtor_Fail6() {
        List<CommandExecutionCondition> conditions = new ArrayList<CommandExecutionCondition>();
        try {
            new ConvertToPDFCommand(null, conditions, "src", "   ");
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
        target = new ConvertToPDFCommand(null, new ArrayList<CommandExecutionCondition>(), TEST_SOURCE_FILE,
                TEST_DEST_FILE);
        DistributionScriptExecutionContext context = new DistributionScriptExecutionContext();

        new File("test_files/test_convert_files/files/").mkdirs();

        target.executeCommand(context);

        String destName = TEST_DEST_FILE.replace("{FILENAME}", CommandsUtil.getFileName(TEST_SOURCE_FILE)).replace(
                "{EXT}", CommandsUtil.getExtension(TEST_SOURCE_FILE));

        assertTrue("The pdf file should be generated.", isPDF(destName));
    }

    /**
     * Test accuracy of method executeCommand(DistributionScriptExecutionContext context). In this case, we pass in a
     * .pdf filet to convert, it will just copy the file to destination path.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testExecuteCommand_Accuracy1() throws Exception {
        target = new ConvertToPDFCommand(null, new ArrayList<CommandExecutionCondition>(),
                "test_files/test_convert.pdf", "test_files/tmp.pdf");
        DistributionScriptExecutionContext context = new DistributionScriptExecutionContext();

        new File("test_files/test_convert_files/files/").mkdirs();

        target.executeCommand(context);

        assertTrue("The pdf file should be generated.", isPDF("test_files/tmp.pdf"));
    }

    /**
     * Test failure of method executeCommand(DistributionScriptExecutionContext context) when context is null,
     * IllegalArgumentException is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testExecuteCommand_Fail() throws Exception {
        target = new ConvertToPDFCommand(null, new ArrayList<CommandExecutionCondition>(), "src", "dest");
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
        target = new ConvertToPDFCommand(null, new ArrayList<CommandExecutionCondition>(), "no source file", "dest");
        try {
            target.executeCommand(new DistributionScriptExecutionContext());
            fail("InputFileNotFoundException is expected.");
        } catch (InputFileNotFoundException e) {
            // good
        }
    }
}
