/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.distribution.failuretests;

import junit.framework.TestCase;

import com.topcoder.util.distribution.commands.conditions.VariableDefinedCondition;

/**
 * Failure tests for <code>VariableDefinedCondition</code>.
 * @author assistant
 * @version 1.0
 */
public class VariableDefinedConditionTest extends TestCase {

	/**
	 * Instance to test.
	 */
	private VariableDefinedCondition instance;
	
	/**
	 * Sets up the environment.
	 * @throws Exception to JUnit
	 */
	protected void setUp() throws Exception {
		super.setUp();
		instance = new VariableDefinedCondition("test");
	}

	/**
	 * Test method for {@link com.topcoder.util.distribution.commands.conditions.VariableDefinedCondition
	 * #VariableDefinedCondition(java.lang.String)}.
	 */
	public void testVariableDefinedCondition_Null() {
		try {
			new VariableDefinedCondition(null);
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
			// good
		}
	}

	/**
	 * Test method for {@link com.topcoder.util.distribution.commands.conditions.VariableDefinedCondition
	 * #VariableDefinedCondition(java.lang.String)}.
	 */
	public void testVariableDefinedCondition_Empty() {
		try {
			new VariableDefinedCondition("\t \n");
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
			// good
		}
	}

	/**
	 * Test method for {@link com.topcoder.util.distribution.commands.conditions.VariableDefinedCondition
	 * #check(com.topcoder.util.distribution.DistributionScriptExecutionContext)}.
	 */
	public void testCheck() {
		try {
			instance.check(null);
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
			// good
		}
	}

}
