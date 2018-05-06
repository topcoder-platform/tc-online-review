/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.distribution.accuracytests;

import com.topcoder.util.distribution.DistributionScriptExecutionContext;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests DistributionScriptExecutionContext class.
 *
 * @author orange_cloud
 * @version 1.0
 */
public class DistributionScriptExecutionContextTest extends TestCase {
    /**
     * Instance to test.
     */
    private DistributionScriptExecutionContext target;

    /**
     * <p>Returns the test suite of this class.</p>
     *
     * @return the test suite of this class.
     */
    public static Test suite() {
        return new TestSuite(DistributionScriptExecutionContextTest.class);
    }

    /**
     * <p>Sets up test environment.</p>
     *
     * @throws Exception to junit
     */
    public void setUp() throws Exception {
        super.setUp();
        TestHelper.clearTemp();
        target = new DistributionScriptExecutionContext();
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
     * Tests setVariable/getVariable methods.
     *
     * @throws Exception when it occurs deeper
     */
    public void testVariables() throws Exception {
        assertEquals("value", null, target.getVariable("abc"));

        target.setVariable("abc", "123");
        assertEquals("value", null, target.getVariable("xyz"));
        assertEquals("value", "123", target.getVariable("abc"));

        target.setVariable("xyz", "");
        assertEquals("value", "", target.getVariable("xyz"));
    }
}
