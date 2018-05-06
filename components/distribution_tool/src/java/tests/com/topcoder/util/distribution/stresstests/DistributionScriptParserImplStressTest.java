/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.distribution.stresstests;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.topcoder.util.distribution.DistributionScript;
import com.topcoder.util.distribution.DistributionScriptCommand;
import com.topcoder.util.distribution.commands.BaseDistributionScriptCommand;
import com.topcoder.util.distribution.commands.CommandExecutionCondition;
import com.topcoder.util.distribution.commands.ConvertToPDFCommand;
import com.topcoder.util.distribution.commands.CopyFileCommand;
import com.topcoder.util.distribution.commands.CopyFileTemplateCommand;
import com.topcoder.util.distribution.commands.CreateFolderCommand;
import com.topcoder.util.distribution.commands.DefineVariableCommand;
import com.topcoder.util.distribution.commands.ExecuteProcessCommand;
import com.topcoder.util.distribution.commands.conditions.VariableDefinedCondition;
import com.topcoder.util.distribution.commands.conditions.VariableNotDefinedCondition;
import com.topcoder.util.distribution.parsers.DistributionScriptParserImpl;

/**
 * <p>
 * Stress tests for <code>DistributionScriptParserImpl</code> class.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class DistributionScriptParserImplStressTest extends StressTestBase {
    /**
     * <p>
     * Returns the test suite of this class.
     * </p>
     *
     * @return the test suite of this class.
     */
    public static Test suite() {
        return new TestSuite(DistributionScriptParserImplStressTest.class);
    }

    /**
     * <p>
     * Stress test of method DistributionScriptParserImpl.parseCommands().
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testParseCommands_Stress() throws Exception {
        DistributionScript script = new DistributionScript();
        InputStream input = null;
        DistributionScriptParserImpl parser = new DistributionScriptParserImpl();

        // the commands.txt contains 60000 commands
        input = new FileInputStream(STRESSTEST_BASE + "commands.txt");
        long start = System.currentTimeMillis();
        parser.parseCommands(input, script, null);
        System.out.println("Running DistributionScriptParserImpl.parseCommands() to parse 60000 commands used "
                + (System.currentTimeMillis() - start) + "ms.");
        if (input != null) {
            input.close();
        }
        List<DistributionScriptCommand> cmds = script.getCommands();
        for (int i = 0; i < 10000; ++i) {
            assertTrue("The command should be instance of DefinaVariableCommand.",
                    cmds.get(i) instanceof DefineVariableCommand);
            assertEquals("The variable name should be correct.", "x" + i, this.getPrivateField(
                    DefineVariableCommand.class, (DefineVariableCommand) cmds.get(i), "name"));
            assertEquals("The variable name should be correct.", "y" + i, this.getPrivateField(
                    DefineVariableCommand.class, (DefineVariableCommand) cmds.get(i), "value"));
        }

        for (int i = 10000; i < 20000; ++i) {
            assertTrue("The command should be instance of CreateFolderCommand.",
                    cmds.get(i) instanceof CreateFolderCommand);
            assertEquals("The folderPath name should be correct.", "folder" + (i - 10000), this.getPrivateField(
                    CreateFolderCommand.class, (CreateFolderCommand) cmds.get(i), "folderPath"));
        }

        for (int i = 20000; i < 30000; ++i) {
            assertTrue("The command should be instance of CopyFileCommand.", cmds.get(i) instanceof CopyFileCommand);
            assertEquals("The sourcePath name should be correct.", "source file" + (i - 20000), this.getPrivateField(
                    CopyFileCommand.class, (CopyFileCommand) cmds.get(i), "sourcePath"));
            assertEquals("The destPath name should be correct.", "dest file" + (i - 20000), this.getPrivateField(
                    CopyFileCommand.class, (CopyFileCommand) cmds.get(i), "destPath"));
        }

        for (int i = 30000; i < 40000; ++i) {
            assertTrue("The command should be instance of CopyFileTemplateCommand.",
                    cmds.get(i) instanceof CopyFileTemplateCommand);
            assertEquals("The sourcePath name should be correct.", "source template" + (i - 30000),
                    this.getPrivateField(CopyFileTemplateCommand.class, (CopyFileTemplateCommand) cmds.get(i),
                            "sourcePath"));
            assertEquals("The destPath name should be correct.", "dest file" + (i - 30000), this.getPrivateField(
                    CopyFileTemplateCommand.class, (CopyFileTemplateCommand) cmds.get(i), "destPath"));
        }

        for (int i = 40000; i < 50000; ++i) {
            assertTrue("The command should be instance of ConvertToPDFCommand.",
                    cmds.get(i) instanceof ConvertToPDFCommand);
            assertEquals("The sourcePath name should be correct.", "source pdf" + (i - 40000), this.getPrivateField(
                    ConvertToPDFCommand.class, (ConvertToPDFCommand) cmds.get(i), "sourcePath"));
            assertEquals("The destPath name should be correct.", "dest file" + (i - 40000), this.getPrivateField(
                    ConvertToPDFCommand.class, (ConvertToPDFCommand) cmds.get(i), "destPath"));
        }

        for (int i = 50000; i < 60000; ++i) {
            assertTrue("The command should be instance of ExecuteProcessCommand.",
                    cmds.get(i) instanceof ExecuteProcessCommand);
            assertEquals("The command name should be correct.", "command" + (i - 50000), this.getPrivateField(
                    ExecuteProcessCommand.class, (ExecuteProcessCommand) cmds.get(i), "command"));
            assertEquals("The workingPath name should be correct.", "workingPath" + (i - 50000), this.getPrivateField(
                    ExecuteProcessCommand.class, (ExecuteProcessCommand) cmds.get(i), "workingPath"));
        }
    }

    /**
     * <p>
     * Stress test of method DistributionScriptParserImpl.parseCommands().
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    @SuppressWarnings("unchecked")
    public void testParseCommands_Stress1() throws Exception {
        DistributionScript script = new DistributionScript();
        InputStream input = null;
        DistributionScriptParserImpl parser = new DistributionScriptParserImpl();

        // the conditions.txt has one command which contains 1000 conditions
        input = new FileInputStream(STRESSTEST_BASE + "conditions.txt");
        long start = System.currentTimeMillis();
        parser.parseCommands(input, script, null);
        System.out.println("Running DistributionScriptParserImpl.parseCommands() to parse command with 1000 conditions used "
                + (System.currentTimeMillis() - start) + "ms.");

        if (input != null) {
            input.close();
        }

        List<DistributionScriptCommand> cmds = script.getCommands();
        assertEquals("Only one command in this case.", 1, cmds.size());
        DistributionScriptCommand cmd = cmds.get(0);
        List<CommandExecutionCondition> conditions = (List<CommandExecutionCondition>) getPrivateField(BaseDistributionScriptCommand.class, (DefineVariableCommand) cmd, "conditions");
        assertEquals("The conditions should be parsed correctly.", 1000, conditions.size());

        for (int i = 0; i < 1000; i++) {
            if (i % 2 == 0) {
                assertTrue("The current condition should be VariableDefinedCondition.", conditions.get(i) instanceof VariableDefinedCondition);
            } else {
                assertTrue("The current condition should be VariableNotDefinedCondition.", conditions.get(i) instanceof VariableNotDefinedCondition);
            }
        }
    }
}
