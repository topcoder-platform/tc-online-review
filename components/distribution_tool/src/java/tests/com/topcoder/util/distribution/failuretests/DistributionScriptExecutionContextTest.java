/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.distribution.failuretests;

import junit.framework.TestCase;

import com.topcoder.util.distribution.DistributionScriptExecutionContext;

/**
 * Failure tests for <code>DistributionScriptExecutionContext</code>.
 * @author assistant
 * @version 1.0
 */
public class DistributionScriptExecutionContextTest extends TestCase {

	/**
	 * Instance to test.
	 */
	private DistributionScriptExecutionContext instance;

	/**
	 * Sets up the environment.
	 * @throws Exception to JUnit
	 */
	protected void setUp() throws Exception {
		super.setUp();
		instance = new DistributionScriptExecutionContext();
	}

	/**
	 * Test method for {@link com.topcoder.util.distribution.DistributionScriptExecutionContext
	 * #setVariable(java.lang.String, java.lang.String)}.
	 */
	public void testSetVariable_NullName() {
		try {
			instance.setVariable(null, "test");
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
			// good
		}
	}

	/**
	 * Test method for {@link com.topcoder.util.distribution.DistributionScriptExecutionContext
	 * #setVariable(java.lang.String, java.lang.String)}.
	 */
	public void testSetVariable_EmptyName() {
		try {
			instance.setVariable(" \t", "test");
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
			// good
		}
	}

	/**
	 * Test method for {@link com.topcoder.util.distribution.DistributionScriptExecutionContext
	 * #getVariable(java.lang.String)}.
	 */
	public void testGetVariable_NullName() {
		try {
			instance.getVariable(null);
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
			// good
		}
	}

	/**
	 * Test method for {@link com.topcoder.util.distribution.DistributionScriptExecutionContext
	 * #getVariable(java.lang.String)}.
	 */
	public void testGetVariable_EmptyName() {
		try {
			instance.getVariable("\t \n");
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
			// good
		}
	}
}
