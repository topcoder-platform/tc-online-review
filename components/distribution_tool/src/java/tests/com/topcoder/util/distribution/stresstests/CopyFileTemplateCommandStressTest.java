/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.distribution.stresstests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.topcoder.util.distribution.DistributionScriptExecutionContext;
import com.topcoder.util.distribution.commands.CommandExecutionCondition;
import com.topcoder.util.distribution.commands.CopyFileTemplateCommand;

/**
 * <p>
 * Stress tests for <code>CopyFileTemplateCommand</code> class.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class CopyFileTemplateCommandStressTest extends StressTestBase {
    /**
     * <p>
     * Returns the test suite of this class.
     * </p>
     *
     * @return the test suite of this class.
     */
    public static Test suite() {
        return new TestSuite(CopyFileTemplateCommandStressTest.class);
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

        File file = new File(STRESSTEST_BASE + "converted_template.txt");
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
        File file = new File(STRESSTEST_BASE + "converted_template.txt");
        if (file.isFile()) {
            file.delete();
        }
    }

    /**
     * <p>
     * Stress test for method execute(DistributionScriptExecutionContext context).
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testExecuteCommand_Stress() throws Exception {
        CopyFileTemplateCommand cmd = new CopyFileTemplateCommand(null, new ArrayList<CommandExecutionCondition>(),
                STRESSTEST_BASE + "template.txt", STRESSTEST_BASE + "converted_template.txt");
        DistributionScriptExecutionContext context = new DistributionScriptExecutionContext();
        for (int i = 0; i < 10000; ++i) {
            context.setVariable("var" + i, "var value" + i);
        }

        long start = System.currentTimeMillis();
        cmd.execute(context);
        System.out.println("Running CopyFileTemplateCommand.execute() to replace 10000 variables used "
                + (System.currentTimeMillis() - start) + "ms.");

        BufferedReader fileReader = new BufferedReader(new FileReader("test_files/stresstests/converted_template.txt"));
        String line = null;
        int i = 0;
        while ((line = fileReader.readLine()) != null) {
            assertTrue("The var" + i + " should be replaced.", line.contains("var value" + i));
            i++;
        }
    }
}
