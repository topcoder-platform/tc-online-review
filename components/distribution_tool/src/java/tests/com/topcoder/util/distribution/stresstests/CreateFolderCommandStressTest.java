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
import com.topcoder.util.distribution.commands.CreateFolderCommand;

/**
 * <p>
 * Stress tests for <code>CreateFolderCommand</code> class.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class CreateFolderCommandStressTest extends StressTestBase {
    /**
     * <p>
     * Returns the test suite of this class.
     * </p>
     *
     * @return the test suite of this class.
     */
    public static Test suite() {
        return new TestSuite(CreateFolderCommandStressTest.class);
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

        for (int i = 0; i < 10000; ++i) {
            del(STRESSTEST_BASE + "folder" + i);
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

        for (int i = 0; i < 10000; ++i) {
            del(STRESSTEST_BASE + "folder" + i);
        }
    }

    /**
     * <p>
     * Stress test for method execute(DistributionScriptExecutionContext context).
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testExecuteCommand_Stress() throws Exception {
        DistributionScriptExecutionContext context = new DistributionScriptExecutionContext();
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10000; ++i) {
            CreateFolderCommand cmd = new CreateFolderCommand(null, new ArrayList<CommandExecutionCondition>(), STRESSTEST_BASE + "folder" + i);
            cmd.execute(context);
            assertTrue("The folder should be created.", new File(STRESSTEST_BASE + "folder" + i).isDirectory());
        }
        System.out.println("Running CreateFolderCommand.execute() to create 10000 folders used "
                + (System.currentTimeMillis() - start) + "ms.");
    }
}
