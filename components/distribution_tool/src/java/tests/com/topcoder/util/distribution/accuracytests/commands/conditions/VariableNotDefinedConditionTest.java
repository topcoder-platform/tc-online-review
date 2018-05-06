/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.distribution.accuracytests.commands.conditions;

import com.topcoder.util.distribution.DistributionScriptExecutionContext;
import com.topcoder.util.distribution.commands.CommandExecutionCondition;
import com.topcoder.util.distribution.commands.conditions.VariableNotDefinedCondition;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests for VariableNotDefinedCondition class.
 *
 * @author orange_cloud
 * @version 1.0
 */
public class VariableNotDefinedConditionTest extends TestCase {
    /**
     * Instance to test.
     */
    private CommandExecutionCondition target;

    /**
     * <p>Returns the test suite of this class.</p>
     *
     * @return the test suite of this class.
     */
    public static Test suite() {
        return new TestSuite(VariableNotDefinedConditionTest.class);
    }

    /**
     * <p>Sets up test environment.</p>
     *
     * @throws Exception to junit
     */
    public void setUp() throws Exception {
        super.setUp();
        target = new VariableNotDefinedCondition("abc");
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
     * Tests check method.
     *
     * @throws Exception when it occurs deeper
     */
    public void testCheck() throws Exception {
        DistributionScriptExecutionContext context = new DistributionScriptExecutionContext();
        context.setVariable("abc", "123");
        context.setVariable("xyz", "567");
        assertFalse("defined", target.check(context));
        context.setVariable("abc", null);
        assertTrue("not defined", target.check(context));
    }
}
