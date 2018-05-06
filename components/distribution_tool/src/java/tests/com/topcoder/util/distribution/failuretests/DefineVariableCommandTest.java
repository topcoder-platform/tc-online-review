/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.distribution.failuretests;

import java.util.ArrayList;
import java.util.Arrays;

import junit.framework.TestCase;

import com.topcoder.util.distribution.commands.CommandExecutionCondition;
import com.topcoder.util.distribution.commands.DefineVariableCommand;

/**
 * Failure tests for <code>DefineVariableCommand</code>.
 * @author assistant
 * @version 1.0
 */
public class DefineVariableCommandTest extends TestCase {

	/**
	 * Instance to test.
	 */
	private DefineVariableCommand instance;

	/**
	 * Sets up the environment.
	 * @throws Exception to JUnit
	 */
	protected void setUp() throws Exception {
		super.setUp();
		instance = new DefineVariableCommand(null, new ArrayList<CommandExecutionCondition>(), "test", "test");
	}

	/**
	 * Test method for {@link com.topcoder.util.distribution.commands.ConvertToPDFCommand
	 * #executeCommand(com.topcoder.util.distribution.DistributionScriptExecutionContext)}.
	 */
	public void testExecuteCommand_NullConditions() {
		try {
			new DefineVariableCommand(null, null, "test", "test");
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
			// good
		}
	}

	/**
	 * Test method for {@link com.topcoder.util.distribution.commands.DefineVariableCommand
	 * #executeCommand(com.topcoder.util.distribution.DistributionScriptExecutionContext)}.
	 */
	public void testExecuteCommand_ContainsNull() {
		try {
			new DefineVariableCommand(null, Arrays.asList(new CommandExecutionCondition[] {null}), "test", "test");
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
			// good
		}
	}

	/**
	 * Test method for {@link com.topcoder.util.distribution.commands.DefineVariableCommand
	 * #executeCommand(com.topcoder.util.distribution.DistributionScriptExecutionContext)}.
	 */
	public void testExecuteCommand_NullSource() {
		try {
			new DefineVariableCommand(null, new ArrayList<CommandExecutionCondition>(), null, "test");
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
			// good
		}
	}

	/**
	 * Test method for {@link com.topcoder.util.distribution.commands.DefineVariableCommand
	 * #executeCommand(com.topcoder.util.distribution.DistributionScriptExecutionContext)}.
	 */
	public void testExecuteCommand_EmptySource() {
		try {
			new DefineVariableCommand(null, new ArrayList<CommandExecutionCondition>(), "\t\n", "test");
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
			// good
		}
	}

	/**
	 * Test method for {@link com.topcoder.util.distribution.commands.DefineVariableCommand
	 * #executeCommand(com.topcoder.util.distribution.DistributionScriptExecutionContext)}.
	 */
	public void testExecuteCommand_NullDest() {
		try {
			new DefineVariableCommand(null, new ArrayList<CommandExecutionCondition>(), "test", null);
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
			// good
		}
	}

	/**
	 * Test method for {@link com.topcoder.util.distribution.commands.DefineVariableCommand
	 * #executeCommand(com.topcoder.util.distribution.DistributionScriptExecutionContext)}.
	 * @throws Exception to JUnit
	 */
	public void testDefineVariableCommand_Null() throws Exception {
		try {
			instance.execute(null);
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
			// good
		}
	}

}
