/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.distribution.failuretests;

import java.util.ArrayList;
import java.util.Arrays;

import junit.framework.TestCase;

import com.topcoder.util.distribution.DistributionScriptCommandExecutionException;
import com.topcoder.util.distribution.commands.CommandExecutionCondition;
import com.topcoder.util.distribution.commands.UndefineVariableCommand;

/**
 * Failure tests for <code>UndefineVariableCommand</code>.
 * @author assistant
 * @version 1.0
 */
public class UndefineVariableCommandTest extends TestCase {

	/**
	 * Instance to test.
	 */
	private UndefineVariableCommand instance;

	/**
	 * Sets up the environment.
	 * @throws Exception to JUnit
	 */
	protected void setUp() throws Exception {
		super.setUp();
		instance = new UndefineVariableCommand(null, new ArrayList<CommandExecutionCondition>(), "test");
	}

	/**
	 * Test method for {@link com.topcoder.util.distribution.commands.UndefineVariableCommand
	 * #executeCommand(com.topcoder.util.distribution.DistributionScriptExecutionContext)}.
	 * @throws DistributionScriptCommandExecutionException to JUnit
	 */
	public void testExecuteCommand_Null() throws DistributionScriptCommandExecutionException {
		try {
			instance.execute(null);
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
			// good
		}
	}

	/**
	 * Test method for {@link com.topcoder.util.distribution.commands.UndefineVariableCommand
	 * #UndefineVariableCommand(com.topcoder.util.log.Log, java.util.List, java.lang.String)}.
	 */
	public void testUndefineVariableCommand_NullConditions() {
		try {
			new UndefineVariableCommand(null, null, "test");
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
			// good
		}
	}

	/**
	 * Test method for {@link com.topcoder.util.distribution.commands.UndefineVariableCommand
	 * #UndefineVariableCommand(com.topcoder.util.log.Log, java.util.List, java.lang.String)}.
	 */
	public void testUndefineVariableCommand_NullInConditions() {
		try {
			new UndefineVariableCommand(null, Arrays.asList(new CommandExecutionCondition[] {null}), "test");
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
			// good
		}
	}

	/**
	 * Test method for {@link com.topcoder.util.distribution.commands.UndefineVariableCommand
	 * #UndefineVariableCommand(com.topcoder.util.log.Log, java.util.List, java.lang.String)}.
	 */
	public void testUndefineVariableCommand_NullName() {
		try {
			new UndefineVariableCommand(null, new ArrayList<CommandExecutionCondition>(), null);
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
			// good
		}
	}
	/**
	 * Test method for {@link com.topcoder.util.distribution.commands.UndefineVariableCommand
	 * #UndefineVariableCommand(com.topcoder.util.log.Log, java.util.List, java.lang.String)}.
	 */
	public void testUndefineVariableCommand_EmptyName() {
		try {
			new UndefineVariableCommand(null, new ArrayList<CommandExecutionCondition>(), "\t\n");
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
			// good
		}
	}

}
