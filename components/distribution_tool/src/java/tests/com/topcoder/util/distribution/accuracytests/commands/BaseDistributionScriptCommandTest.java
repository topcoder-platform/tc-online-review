/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.distribution.accuracytests.commands;

import com.topcoder.util.distribution.DistributionScriptExecutionContext;
import com.topcoder.util.distribution.commands.CommandExecutionCondition;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.ArrayList;

/**
 * Tests BaseDistributionScriptCommand class.
 *
 * @author orange_cloud
 * @version 1.0
 */
public class BaseDistributionScriptCommandTest extends TestCase {
    /**
     * Instance to test.
     */
    private MockBaseDistributionScriptCommand target;

    /**
     * Context to be used in tests.
     */
    private DistributionScriptExecutionContext context;

    /**
     * <p>Returns the test suite of this class.</p>
     *
     * @return the test suite of this class.
     */
    public static Test suite() {
        return new TestSuite(BaseDistributionScriptCommandTest.class);
    }

    /**
     * <p>Sets up test environment.</p>
     *
     * @throws Exception to junit
     */
    public void setUp() throws Exception {
        super.setUp();
        target = new MockBaseDistributionScriptCommand(null, new ArrayList<CommandExecutionCondition>());
        context = new DistributionScriptExecutionContext();
        context.setVariable("abc", "123");
        context.setVariable("xyz", "567");
        context.setVariable("a", "1");
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
     * Tests replaceVariableFields method for valid vars.
     *
     * @throws Exception when it occurs deeper
     */
    public void testReplaceVariableFields1() throws Exception {
        assertEquals("result", "abc", target.replaceVariableFields("abc", context));
        assertEquals("result", "123", target.replaceVariableFields("%{abc}", context));
        assertEquals("result", "q123w567123t", target.replaceVariableFields("q%{abc}w%{xyz}%{abc}t", context));
        assertEquals("result", "1", target.replaceVariableFields("%{a}", context));
        assertEquals("result", "%{pqr}", target.replaceVariableFields("%{pqr}", context));
        assertEquals("result", "", target.replaceVariableFields("", context));
    }

    /**
     * Tests replaceVariableFields method for incomplete vars.
     *
     * @throws Exception when it occurs deeper
     */
    public void testReplaceVariableFields2() throws Exception {
        assertEquals("result", "%{abc", target.replaceVariableFields("%{abc", context));
        assertEquals("result", "{abc}", target.replaceVariableFields("{abc}", context));
        assertEquals("result", "%0abc}", target.replaceVariableFields("%0abc}", context));
        assertEquals("result", "%abc}", target.replaceVariableFields("%abc}", context));
        assertEquals("result", "%", target.replaceVariableFields("%", context));
        assertEquals("result", "%{", target.replaceVariableFields("%{", context));
        assertEquals("result", "%{abc123", target.replaceVariableFields("%{abc%{abc}", context));
    }

    /**
     * Tests replaceVariableFields method for empty var names.
     *
     * @throws Exception when it occurs deeper
     */
    public void testReplaceVariableFields3() throws Exception {
        assertEquals("result", "%{}1", target.replaceVariableFields("%{}%{a}", context));
        assertEquals("result", "%{   }", target.replaceVariableFields("%{   }", context));
    }
}
