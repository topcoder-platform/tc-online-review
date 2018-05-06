/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.distribution.accuracytests.commands;

import com.topcoder.util.distribution.DistributionScriptCommand;
import com.topcoder.util.distribution.DistributionScriptExecutionContext;
import com.topcoder.util.distribution.accuracytests.TestHelper;
import com.topcoder.util.distribution.commands.CommandExecutionCondition;
import com.topcoder.util.distribution.commands.UndefineVariableCommand;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.ArrayList;

/**
 * Tests UndefineVariableCommand class.
 *
 * @author orange_cloud
 * @version 1.0
 */
public class UndefineVariableCommandTest extends TestCase {
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
        return new TestSuite(UndefineVariableCommandTest.class);
    }

    /**
     * <p>Sets up test environment.</p>
     *
     * @throws Exception to junit
     */
    public void setUp() throws Exception {
        super.setUp();
        TestHelper.clearTemp();
    }

    /**
     * <p>Tears down test environment.</p>
     *
     * @throws Exception to junit
     */
    public void tearDown() throws Exception {
        TestHelper.clearTemp();
        super.tearDown();
    }

    /**
     * Tests execute method.
     *
     * @throws Exception when it occurs deeper
     */
    public void testExecute() throws Exception {
        target = new UndefineVariableCommand(null, new ArrayList<CommandExecutionCondition>(), "abc");
        DistributionScriptExecutionContext context = new DistributionScriptExecutionContext();
        context.setVariable("abc", "000");
        context.setVariable("xyz", "123");
        target.execute(context);

        assertNull("variable", context.getVariable("abc"));
    }
}
