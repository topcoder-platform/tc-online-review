/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.distribution.commands.conditions;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.util.distribution.DistributionScriptExecutionContext;
import com.topcoder.util.distribution.commands.CommandExecutionCondition;

/**
 * <p>
 * Unit tests for <code>VariableNotDefinedCondition</code> class.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class VariableNotDefinedConditionTest extends TestCase {
    /**
     * <p>
     * The VariableNotDefinedCondition instance for test.
     * </p>
     */
    private VariableNotDefinedCondition target;

    /**
     * <p>
     * Returns the test suite of this class.
     * </p>
     *
     * @return the test suite of this class.
     */
    public static Test suite() {
        return new TestSuite(VariableNotDefinedConditionTest.class);
    }

    /**
     * Test accuracy of constructor VariableNotDefinedCondition(String name).
     */
    public void testCtor_Accuracy() {
        target = new VariableNotDefinedCondition("test");

        assertNotNull("Unable to create the instance.", target);
        assertTrue("The object should be instance of CommandExecutionCondition.",
                target instanceof CommandExecutionCondition);
    }

    /**
     * Test failure of constructor VariableNotDefinedCondition(String name) when the name is null,
     * IllegalArgumentException is expected.
     */
    public void testCtor_Fail1() {
        try {
            new VariableNotDefinedCondition(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test failure of constructor VariableNotDefinedCondition(String name) when the name is empty,
     * IllegalArgumentException is expected.
     */
    public void testCtor_Fail2() {
        try {
            new VariableNotDefinedCondition(" \t ");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test accuracy of method check(DistributionScriptExecutionContext context).
     */
    public void testCheck_Accuracy() {
        target = new VariableNotDefinedCondition("test");

        DistributionScriptExecutionContext context = new DistributionScriptExecutionContext();
        context.setVariable("test", "test check");

        assertFalse("The value should match.", target.check(context));
        context.setVariable("test", null);
        assertTrue("The value should match.", target.check(context));
    }

    /**
     * Test failure of method check(DistributionScriptExecutionContext context) when the context is null,
     * IllegalArgumentException is expected.
     */
    public void testCheck_Fail() {
        target = new VariableNotDefinedCondition("test");

        try {
            target.check(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }
}
