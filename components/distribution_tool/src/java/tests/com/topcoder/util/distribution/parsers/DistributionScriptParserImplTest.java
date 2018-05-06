/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.distribution.parsers;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.topcoder.util.distribution.BaseTest;
import com.topcoder.util.distribution.DistributionScript;
import com.topcoder.util.distribution.DistributionScriptCommand;
import com.topcoder.util.distribution.DistributionScriptParser;
import com.topcoder.util.distribution.DistributionScriptParsingException;
import com.topcoder.util.distribution.commands.BaseDistributionScriptCommand;
import com.topcoder.util.distribution.commands.CommandExecutionCondition;
import com.topcoder.util.distribution.commands.DefineVariableCommand;
import com.topcoder.util.distribution.commands.ExecuteProcessCommand;
import com.topcoder.util.distribution.commands.UndefineVariableCommand;
import com.topcoder.util.distribution.commands.conditions.VariableDefinedCondition;
import com.topcoder.util.distribution.commands.conditions.VariableNotDefinedCondition;
import com.topcoder.util.log.Log;
import com.topcoder.util.log.LogManager;

/**
 * <p>
 * Unit tests for <code>DistributionScriptParserImpl</code> class.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class DistributionScriptParserImplTest extends BaseTest {
    /**
     * <p>
     * Represents the Log used in tests.
     * </p>
     */
    private static final Log LOG = LogManager.getLog(DistributionScriptParserImplTest.class.getName());

    /**
     * <p>
     * The DistributionScriptParserImpl instance for test.
     * </p>
     */
    private DistributionScriptParserImpl target;

    /**
     * <p>
     * Returns the test suite of this class.
     * </p>
     *
     * @return the test suite of this class.
     */
    public static Test suite() {
        return new TestSuite(DistributionScriptParserImplTest.class);
    }

    /**
     * Test accuracy of constructor DistributionScriptParserImpl().
     */
    public void testCtor() {
        target = new DistributionScriptParserImpl();
        assertNotNull("Unable to create the instance.", target);

        assertTrue("The object should be instance of DistributionScriptParser.",
                target instanceof DistributionScriptParser);
    }

    /**
     * Test accuracy of method parseCommands(InputStream stream, DistributionScript script, Log log).
     *
     * @throws Exception
     *             to JUnit
     */
    public void testParseCommands_Accuracy1() throws Exception {
        InputStream input = new FileInputStream("test_files/scripts/test/test_parse.txt");
        DistributionScript script = new DistributionScript();
        target = new DistributionScriptParserImpl();
        target.parseCommands(input, script, null);

        assertEquals("The number of parsed commands shoule be equal.", 10, script.getCommands().size());

        // verify the parsed commands, to simplify, pick up 2 items to check
        List<DistributionScriptCommand> cmds = script.getCommands();
        assertTrue("The command type should be right.", cmds.get(0) instanceof DefineVariableCommand);
        DefineVariableCommand define = (DefineVariableCommand) cmds.get(0);
        assertEquals("The value should match.", "tests_dir", getPrivateField(DefineVariableCommand.class, define, "name"));
        assertEquals("The value should match.", "%{comp_dir}/src/java/tests/%{package/name}", getPrivateField(DefineVariableCommand.class, define, "value"));

        assertTrue("The command type should be right.", cmds.get(2) instanceof UndefineVariableCommand);
        UndefineVariableCommand undefine = (UndefineVariableCommand) cmds.get(2);
        assertEquals("The value should match.", "varToUndefine", getPrivateField(UndefineVariableCommand.class, undefine, "name"));
    }

    /**
     * Test accuracy of method parseCommands(InputStream stream, DistributionScript script, Log log).
     *
     * @throws Exception
     *             to JUnit
     */
    @SuppressWarnings("unchecked")
    public void testParseCommands_Accuracy2() throws Exception {
        InputStream input = new FileInputStream("test_files/scripts/test/dotnet.txt");
        DistributionScript script = new DistributionScript();
        target = new DistributionScriptParserImpl();
        target.parseCommands(input, script, LOG);

        // Test the dotnet command was parsed correctly as we are not able to test the execution
        // of this command.
        assertEquals("The number of parsed commands shoule be equal.", 2, script.getCommands().size());
        assertTrue("The command should be a ExecuteProcessCommand.",
                script.getCommands().get(0) instanceof ExecuteProcessCommand);

        ExecuteProcessCommand con = (ExecuteProcessCommand) script.getCommands().get(0);
        String cmd = (String) getPrivateField(ExecuteProcessCommand.class, con, "command");
        String workingPath = (String) getPrivateField(ExecuteProcessCommand.class, con, "workingPath");
        assertEquals("The value should match.", "MSBuild \"Component Sources.csproj\" /t:DesignDist", cmd);
        assertEquals("The value should match.", "%{comp_dir}", workingPath);

        // the parsed command should have two conditions
        DefineVariableCommand con1 = (DefineVariableCommand) script.getCommands().get(1);
        List<CommandExecutionCondition> conditions = (List<CommandExecutionCondition>) getPrivateField(
                BaseDistributionScriptCommand.class, con1, "conditions");
        assertEquals("The DefineVariableCommand should have two conditions.", 2, conditions.size());
        assertTrue("The first should VariableDefinedCondition.",
                conditions.get(0) instanceof VariableDefinedCondition);
        assertTrue("The first should VariableNotDefinedCondition.",
                conditions.get(1) instanceof VariableNotDefinedCondition);
    }

    /**
     * Test failure of method parseCommands(InputStream stream, DistributionScript script, Log log) when the stream is
     * null, IllegalArgumentException is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testParseCommands_Fail1() throws Exception {
        target = new DistributionScriptParserImpl();
        try {
            target.parseCommands(null, new DistributionScript(), LogManager.getLog("test_parse_commands"));
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test failure of method parseCommands(InputStream stream, DistributionScript script, Log log) when the script is
     * null, IllegalArgumentException is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testParseCommands_Fail2() throws Exception {
        target = new DistributionScriptParserImpl();
        try {
            target.parseCommands(new FileInputStream("test_files/scripts/test/test_parse.txt"), null, LogManager
                    .getLog("test_parse_commands"));
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test failure of method parseCommands(InputStream stream, DistributionScript script, Log log) when unknown command
     * detected, DistributionScriptParsingException is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testParseCommands_Fail3() throws Exception {
        target = new DistributionScriptParserImpl();
        try {
            target.parseCommands(new FileInputStream("test_files/scripts/test/unknown.txt"), new DistributionScript(),
                    null);
            fail("DistributionScriptParsingException is expected.");
        } catch (DistributionScriptParsingException e) {
            // good
        }
    }

    /**
     * Test failure of method parseCommands(InputStream stream, DistributionScript script, Log log) when the format of
     * condition is wrong, DistributionScriptParsingException is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testParseCommands_Fail4() throws Exception {
        target = new DistributionScriptParserImpl();
        try {
            target.parseCommands(new FileInputStream("test_files/scripts/test/wrong_condition_format.txt"),
                    new DistributionScript(), null);
            fail("DistributionScriptParsingException is expected.");
        } catch (DistributionScriptParsingException e) {
            // good
        }
    }

    /**
     * Test failure of method parseCommands(InputStream stream, DistributionScript script, Log log) when the format of
     * command is wrong, DistributionScriptParsingException is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testParseCommands_Fail5() throws Exception {
        target = new DistributionScriptParserImpl();
        try {
            target.parseCommands(new FileInputStream("test_files/scripts/test/wrong_cmd_format.txt"),
                    new DistributionScript(), null);
            fail("DistributionScriptParsingException is expected.");
        } catch (DistributionScriptParsingException e) {
            // good
        }
    }

    /**
     * Test failure of method parseCommands(InputStream stream, DistributionScript script, Log log) when command is
     * empty, DistributionScriptParsingException is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testParseCommands_Fail6() throws Exception {
        target = new DistributionScriptParserImpl();
        try {
            target.parseCommands(new FileInputStream("test_files/scripts/test/empty_cmd.txt"),
                    new DistributionScript(), null);
            fail("DistributionScriptParsingException is expected.");
        } catch (DistributionScriptParsingException e) {
            // good
        }
    }

    /**
     * Test failure of method parseCommands(InputStream stream, DistributionScript script, Log log) when command data is
     * empty, DistributionScriptParsingException is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testParseCommands_Fail7() throws Exception {
        target = new DistributionScriptParserImpl();
        try {
            target.parseCommands(new FileInputStream("test_files/scripts/test/empty_data.txt"),
                    new DistributionScript(), null);
            fail("DistributionScriptParsingException is expected.");
        } catch (DistributionScriptParsingException e) {
            // good
        }
    }

//    /**
//     * Test failure of method parseCommands(InputStream stream, DistributionScript script, Log log) when the format of
//     * arguments are wrong, DistributionScriptParsingException is expected.
//     *
//     * @throws Exception
//     *             to JUnit
//     */
//    public void testParseCommands_Fail8() throws Exception {
//        target = new DistributionScriptParserImpl();
//        try {
//            target.parseCommands(new FileInputStream("test_files/scripts/test/wrong_args.txt"),
//                    new DistributionScript(), null);
//            fail("DistributionScriptParsingException is expected.");
//        } catch (DistributionScriptParsingException e) {
//            // good
//        }
//    }

    /**
     * Test failure of method parseCommands(InputStream stream, DistributionScript script, Log log) when the argument of
     * command is empty, DistributionScriptParsingException is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testParseCommands_Fail9() throws Exception {
        target = new DistributionScriptParserImpl();
        try {
            target.parseCommands(new FileInputStream("test_files/scripts/test/empty_args.txt"),
                    new DistributionScript(), null);
            fail("DistributionScriptParsingException is expected.");
        } catch (DistributionScriptParsingException e) {
            // good
        }
    }
}
