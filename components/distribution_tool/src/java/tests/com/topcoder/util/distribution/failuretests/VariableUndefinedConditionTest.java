/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.distribution.failuretests;

import junit.framework.TestCase;

import com.topcoder.util.distribution.commands.conditions.VariableNotDefinedCondition;

/**
 * Failure tests for <code>VariableUndefinedCondition</code>.
 * @author assistant
 * @version 1.0
 */
public class VariableUndefinedConditionTest extends TestCase {

	/**
	 * Instance to test.
	 */
	private VariableNotDefinedCondition instance;
	
	/**
	 * Sets up the environment.
	 * @throws Exception to JUnit
	 */
	protected void setUp() throws Exception {
		super.setUp();
		instance = new VariableNotDefinedCondition("test");
	}

	/**
	 * Test method for {@link com.topcoder.util.distribution.commands.conditions.VariableUndefinedCondition
	 * #VariableUndefinedCondition(java.lang.String)}.
	 */
	public void testVariableUndefinedCondition_Null() {
		try {
			new VariableNotDefinedCondition(null);
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
			// good
		}
	}

	/**
	 * Test method for {@link com.topcoder.util.distribution.commands.conditions.VariableUndefinedCondition
	 * #VariableUndefinedCondition(java.lang.String)}.
	 */
	public void testVariableUndefinedCondition_Empty() {
		try {
			new VariableNotDefinedCondition("\t \n");
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
			// good
		}
	}

	/**
	 * Test method for {@link com.topcoder.util.distribution.commands.conditions.VariableUndefinedCondition
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
