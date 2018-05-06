/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.distribution.accuracytests.commands;

import com.topcoder.util.distribution.DistributionScriptCommand;
import com.topcoder.util.distribution.DistributionScriptExecutionContext;
import com.topcoder.util.distribution.accuracytests.TestHelper;
import com.topcoder.util.distribution.commands.CommandExecutionCondition;
import com.topcoder.util.distribution.commands.ConvertToPDFCommand;
import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.File;
import java.util.ArrayList;

/**
 * Tests ConvertToPDFCommand class.
 *
 * @author orange_cloud
 * @version 1.0
 */
public class ConvertToPDFCommandTest extends TestCase {
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
        return new TestSetup(new TestSuite(ConvertToPDFCommandTest.class)) {
            /**
             * <p>Sets up test environment.</p>
             *
             * @throws Exception to junit
             */
            public void setUp() throws Exception {
                TestHelper.clearTemp("test_files/accuracy/pdf");
            }
        };
    }

    /**
     * <p>Sets up test environment.</p>
     *
     * @throws Exception to junit
     */
    public void setUp() throws Exception {
        super.setUp();
    }

    /**
     * <p>Tears down test environment.</p>
     *
     * @throws Exception to junit
     */
    public void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Tests execute method.
     *
     * @throws Exception when it occurs deeper
     */
    public void testExecute1() throws Exception {
        String from = "test_files/accuracy/to_convert.html";
//        String to = "test_files/accuracy/pdf/dir/dir/html.pdf";
        String to = "test_files/accuracy/pdf/html.pdf";
        target = new ConvertToPDFCommand(null, new ArrayList<CommandExecutionCondition>(), from, to);
        DistributionScriptExecutionContext context = new DistributionScriptExecutionContext();
        target.execute(context);

        assertTrue("check that file exists", new File(to).isFile());
        // it has to be visually verified after tests completed also 
    }

    /**
     * Tests execute method when file is only copied.
     *
     * @throws Exception when it occurs deeper
     */
    public void testExecute2() throws Exception {
        String from = "test_files/accuracy/to_convert.pdf";
        String to = "test_files/accuracy/pdf/pdf.pdf";
        target = new ConvertToPDFCommand(null, new ArrayList<CommandExecutionCondition>(), from, to);
        DistributionScriptExecutionContext context = new DistributionScriptExecutionContext();
        target.execute(context);

        // check result
        assertEquals("contents", "not a pdf, actually", TestHelper.readLine(to));
    }
}
