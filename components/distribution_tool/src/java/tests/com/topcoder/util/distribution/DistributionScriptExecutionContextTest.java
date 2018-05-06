/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.distribution;

import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * <p>
 * Unit tests for <code>DistributionScriptExecutionContext</code> class.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class DistributionScriptExecutionContextTest extends BaseTest {
    /**
     * <p>
     * The DistributionScriptExecutionContext instance for test.
     * </p>
     */
    private DistributionScriptExecutionContext target;

    /**
     * <p>
     * Returns the test suite of this class.
     * </p>
     *
     * @return the test suite of this class.
     */
    public static Test suite() {
        return new TestSuite(DistributionScriptExecutionContextTest.class);
    }

    /**
     * Test accuracy of constructor DistributionScriptExecutionContext().
     *
     * @throws Exception
     *             to JUnit.
     */
    @SuppressWarnings("unchecked")
    public void testCtor_Accuracy() throws Exception {
        target = new DistributionScriptExecutionContext();
        assertNotNull("Unable to create the instance.", target);

        Map<String, String> vars = (Map<String, String>) getPrivateField(DistributionScriptExecutionContext.class,
                target, "variables");
        assertTrue("The variables mapping should be empty.", vars.size() == 0);
    }

    /**
     * Test accuracy of method setVariable(String name, String value).
     */
    public void testSetVariable() {
        target = new DistributionScriptExecutionContext();

        target.setVariable("name1", "value1");
        assertEquals("The value should match.", "value1", target.getVariable("name1"));

        // set the variable to null means removing it from the context
        target.setVariable("name1", null);
        assertEquals("The value should match.", null, target.getVariable("name1"));
    }

    /**
     * Test failure of method setVariable(String name, String value) when the name is null, IllegalArgumentException is
     * expected.
     */
    public void testSetVariable_Fail1() {
        target = new DistributionScriptExecutionContext();

        try {
            target.setVariable(null, "null name");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test failure of method setVariable(String name, String value) when the name is empty, IllegalArgumentException is
     * expected.
     */
    public void testSetVariable_Fail2() {
        target = new DistributionScriptExecutionContext();

        try {
            target.setVariable(" \t", "empty name");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test accuracy of method getVariable(String name).
     */
    public void testGetVariable() {
        target = new DistributionScriptExecutionContext();

        target.setVariable("name1", "value1");
        assertEquals("The value should match.", "value1", target.getVariable("name1"));
    }

    /**
     * Test failure of method getVariable(String name) when the name is null, IllegalArgumentException is expected.
     */
    public void testGetVariable_Fail1() {
        target = new DistributionScriptExecutionContext();

        try {
            target.getVariable(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test failure of method getVariable(String name) when the name is empty, IllegalArgumentException is expected.
     */
    public void testGetVariable_Fail2() {
        target = new DistributionScriptExecutionContext();

        try {
            target.getVariable(" \t");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }
}
