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
import com.topcoder.util.distribution.commands.CopyFileCommand;

/**
 * <p>
 * Stress tests for <code>CopyFileCommand</code> class.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class CopyFileCommandStressTest extends StressTestBase {
    /**
     * <p>
     * Returns the test suite of this class.
     * </p>
     *
     * @return the test suite of this class.
     */
    public static Test suite() {
        return new TestSuite(CopyFileCommandStressTest.class);
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
            file = new File(STRESSTEST_BASE + "parse_cmd_test" + i + ".txt");
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
            file = new File(STRESSTEST_BASE + "parse_cmd_test" + i + ".txt");
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
        String content = getFileContent("stresstests/parse_cmd_test.txt");
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000; ++i) {
            CopyFileCommand cmd = new CopyFileCommand(null, new ArrayList<CommandExecutionCondition>(),
                    "test_files/stresstests/parse_cmd_test.txt", "test_files/stresstests/{FILENAME}" + i + ".{EXT}");
            cmd.execute(context);
            String destContent = getFileContent("stresstests/parse_cmd_test" + i + ".txt");
            assertTrue("The copied file should be the same with the source.", content.equals(destContent));
        }
        System.out.println("Running CopyFileCommand.execute() to copy 1000 files used " + (System.currentTimeMillis() - start)
                + "ms.");
    }
}
