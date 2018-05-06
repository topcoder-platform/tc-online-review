/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.distribution.accuracytests.parsers;

import com.topcoder.util.distribution.DistributionScript;
import com.topcoder.util.distribution.DistributionScriptCommand;
import com.topcoder.util.distribution.DistributionScriptCommandExecutionException;
import com.topcoder.util.distribution.DistributionScriptExecutionContext;
import com.topcoder.util.distribution.DistributionScriptParser;
import com.topcoder.util.distribution.accuracytests.TestHelper;
import com.topcoder.util.distribution.commands.ExecuteProcessCommand;
import com.topcoder.util.distribution.parsers.DistributionScriptParserImpl;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Tests DistributionScriptParserImpl class.
 *
 * @author orange_cloud
 * @version 1.0
 */
public class DistributionScriptParserImplTest extends TestCase {
    /**
     * Instance to test.
     */
    private DistributionScriptParser target;

    /**
     * DistributionScript instance to use in tests.
     */
    private DistributionScript script;

    /**
     * Script data to read.
     */
    private InputStream in;

    /**
     * <p>Returns the test suite of this class.</p>
     *
     * @return the test suite of this class.
     */
    public static Test suite() {
        return new TestSuite(DistributionScriptParserImplTest.class);
    }

    /**
     * <p>Sets up test environment.</p>
     *
     * @throws Exception to junit
     */
    public void setUp() throws Exception {
        super.setUp();
        TestHelper.clearTemp();
        target = new DistributionScriptParserImpl();
        script = new DistributionScript();
    }

    /**
     * <p>Tears down test environment.</p>
     *
     * @throws Exception to junit
     */
    public void tearDown() throws Exception {
        TestHelper.clearTemp();
        if (in != null) {
            in.close();
        }
        super.tearDown();
    }

    /**
     * Tests parseCommands method when script is empty.
     *
     * @throws Exception when it occurs deeper
     */
    public void testParseCommands1() throws Exception {
        in = new FileInputStream("test_files/accuracy/script_empty.txt");
        target.parseCommands(in, script, null);
        assertEquals("number of commands", 0, script.getCommands().size());
    }

    /**
     * Tests parseCommands method for different conditions.
     *
     * @throws Exception when it occurs deeper
     */
    public void testParseCommands2() throws Exception {
        in = new FileInputStream("test_files/accuracy/script_conditions.txt");
        target.parseCommands(in, script, null);

        assertEquals("number of commands", 1, script.getCommands().size());
        DistributionScriptExecutionContext context = new DistributionScriptExecutionContext();

        // nothing happens
        script.getCommands().get(0).execute(context);

        context.setVariable("abc", "000");
        try {
            // error expected because command is executed
            script.getCommands().get(0).execute(context);
            fail("DistributionScriptCommandExecutionException expected.");
        } catch (DistributionScriptCommandExecutionException e) {
            // success
        }

        // nothing happens
        context.setVariable("xyz", "123");
        script.getCommands().get(0).execute(context);

        // nothing happens
        context.setVariable("xyz", null);
        context.setVariable("pqr", "123");
        script.getCommands().get(0).execute(context);
    }

    /**
     * Tests parseCommands method for define command.
     *
     * @throws Exception when it occurs deeper
     */
    public void testParseCommands3() throws Exception {
        in = new FileInputStream("test_files/accuracy/script_define.txt");
        target.parseCommands(in, script, null);

        assertEquals("number of commands", 3, script.getCommands().size());

        DistributionScriptCommand command = script.getCommands().get(0);
        DistributionScriptExecutionContext context = new DistributionScriptExecutionContext();
        context.setVariable("abc", "123");
        command.execute(context);
        assertEquals("result", "000", context.getVariable("pqr"));

        command = script.getCommands().get(1);
        command.execute(context);
        assertEquals("result", "", context.getVariable("abc"));

        command = script.getCommands().get(2);
        command.execute(context);
        assertEquals("result", "1=2=3", context.getVariable("xyz"));
    }

    /**
     * Tests parseCommands method for undefine command.
     *
     * @throws Exception when it occurs deeper
     */
    public void testParseCommands4() throws Exception {
        in = new FileInputStream("test_files/accuracy/script_undefine.txt");
        target.parseCommands(in, script, null);

        assertEquals("number of commands", 1, script.getCommands().size());

        DistributionScriptCommand command = script.getCommands().get(0);
        DistributionScriptExecutionContext context = new DistributionScriptExecutionContext();
        context.setVariable("abc", "123");
        command.execute(context);
        assertNull("result", context.getVariable("abc"));
    }

    /**
     * Tests parseCommands method for create_folder command.
     *
     * @throws Exception when it occurs deeper
     */
    public void testParseCommands5() throws Exception {
        in = new FileInputStream("test_files/accuracy/script_create_folder.txt");
        target.parseCommands(in, script, null);

        assertEquals("number of commands", 1, script.getCommands().size());

        DistributionScriptCommand command = script.getCommands().get(0);
        DistributionScriptExecutionContext context = new DistributionScriptExecutionContext();
        context.setVariable("abc", "123");
        context.setVariable("root", "./test_files/accuracy");
        command.execute(context);
        assertTrue("directory", new File("test_files/accuracy/temp/mydir").isDirectory());
    }

    /**
     * Tests parseCommands method for copy_file command.
     *
     * @throws Exception when it occurs deeper
     */
    public void testParseCommands6() throws Exception {
        in = new FileInputStream("test_files/accuracy/script_copy_file.txt");
        target.parseCommands(in, script, null);

        assertEquals("number of commands", 1, script.getCommands().size());

        DistributionScriptCommand command = script.getCommands().get(0);
        DistributionScriptExecutionContext context = new DistributionScriptExecutionContext();
        context.setVariable("abc", "123");
        context.setVariable("root", "./test_files/accuracy");
        command.execute(context);
        assertEquals("content", "my favourite file", TestHelper.readLine("test_files/accuracy/temp/newfile"));
    }

    /**
     * Tests parseCommands method for file_template command.
     *
     * @throws Exception when it occurs deeper
     */
    public void testParseCommands7() throws Exception {
        in = new FileInputStream("test_files/accuracy/script_file_template.txt");
        target.parseCommands(in, script, null);

        assertEquals("number of commands", 1, script.getCommands().size());

        DistributionScriptCommand command = script.getCommands().get(0);
        DistributionScriptExecutionContext context = new DistributionScriptExecutionContext();
        context.setVariable("abc", "123");
        context.setVariable("root", "./test_files/accuracy");
        command.execute(context);
        assertEquals("content", "1 123 2 %{a", TestHelper.readLine("test_files/accuracy/temp/newfile.txt"));
    }

    /**
     * Tests parseCommands method for execute command.
     *
     * @throws Exception when it occurs deeper
     */
    public void testParseCommands8() throws Exception {
        in = new FileInputStream("test_files/accuracy/script_execute.txt");
        target.parseCommands(in, script, null);

        assertEquals("number of commands", 1, script.getCommands().size());
        assertTrue("command", script.getCommands().get(0) instanceof ExecuteProcessCommand);
    }

    /**
     * Tests parseCommands method for convert_to_pdf command.
     *
     * @throws Exception when it occurs deeper
     */
    public void testParseCommands9() throws Exception {
        in = new FileInputStream("test_files/accuracy/script_convert_to_pdf.txt");
        target.parseCommands(in, script, null);

        assertEquals("number of commands", 1, script.getCommands().size());
        DistributionScriptCommand command = script.getCommands().get(0);
        DistributionScriptExecutionContext context = new DistributionScriptExecutionContext();
        command.execute(context);
        assertEquals("content", "not a pdf, actually", TestHelper.readLine("test_files/accuracy/temp/test.pdf"));
    }
}
