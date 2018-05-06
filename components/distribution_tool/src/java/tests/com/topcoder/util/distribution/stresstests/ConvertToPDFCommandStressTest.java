/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.distribution.stresstests;

import java.io.File;
import java.util.ArrayList;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.topcoder.util.distribution.DistributionScriptExecutionContext;
import com.topcoder.util.distribution.commands.CommandExecutionCondition;
import com.topcoder.util.distribution.commands.ConvertToPDFCommand;

/**
 * <p>
 * Stress tests for <code>ConvertToPDFCommand</code> class.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class ConvertToPDFCommandStressTest extends StressTestBase {
    /**
     * <p>
     * Returns the test suite of this class.
     * </p>
     *
     * @return the test suite of this class.
     */
    public static Test suite() {
        return new TestSuite(ConvertToPDFCommandStressTest.class);
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
        File file;
        for (int i = 0; i < 1000; ++i) {
            file = new File(STRESSTEST_BASE + "converted_file" + i + ".pdf");
            if (file.isFile()) {
                file.delete();
            }
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
        File file;
        for (int i = 0; i < 1000; ++i) {
            file = new File(STRESSTEST_BASE + "converted_file" + i + ".pdf");
            if (file.isFile()) {
                file.delete();
            }
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
        DistributionScriptExecutionContext context = new DistributionScriptExecutionContext();
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000; ++i) {
            ConvertToPDFCommand cmd = new ConvertToPDFCommand(null, new ArrayList<CommandExecutionCondition>(),
                    STRESSTEST_BASE + "stresstest_rs.rtf", STRESSTEST_BASE + "converted_file" + i + ".pdf");
            cmd.execute(context);
            assertTrue("The pdf file should be converted successfully.", checkPDF(STRESSTEST_BASE + "converted_file" + i
                    + ".pdf"));
        }
        System.out.println("Running ConvertToPDFCommand.execute() to convert 1000 files used "
                + (System.currentTimeMillis() - start) + "ms.");
    }
}
