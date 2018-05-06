/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.distribution.failuretests;

import java.util.ArrayList;
import java.util.Arrays;

import junit.framework.TestCase;

import com.topcoder.util.distribution.DistributionScriptExecutionContext;
import com.topcoder.util.distribution.InputFileNotFoundException;
import com.topcoder.util.distribution.commands.CommandExecutionCondition;
import com.topcoder.util.distribution.commands.CopyFileCommand;

/**
 * Failure tests for <code>CopyFileCommand</code>.
 * @author assistant
 * @version 1.0
 */
public class CopyFileCommandTest extends TestCase {

	/**
	 * Instance to test.
	 */
	private CopyFileCommand instance;

	/**
	 * Sets up the environment.
	 * @throws Exception to JUnit
	 */
	protected void setUp() throws Exception {
		super.setUp();
		instance = new CopyFileCommand(null, new ArrayList<CommandExecutionCondition>(), "test", "test");
	}

	/**
	 * Test method for {@link com.topcoder.util.distribution.commands.CopyFileCommand
	 * #executeCommand(com.topcoder.util.distribution.DistributionScriptExecutionContext)}.
	 */
	public void testExecuteCommand_NullConditions() {
		try {
			new CopyFileCommand(null, null, "test", "test");
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
			// good
		}
	}

	/**
	 * Test method for {@link com.topcoder.util.distribution.commands.CopyFileCommand
	 * #executeCommand(com.topcoder.util.distribution.DistributionScriptExecutionContext)}.
	 */
	public void testExecuteCommand_ContainsNull() {
		try {
			new CopyFileCommand(null, Arrays.asList(new CommandExecutionCondition[] {null}), "test", "test");
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
			// good
		}
	}

	/**
	 * Test method for {@link com.topcoder.util.distribution.commands.CopyFileCommand
	 * #executeCommand(com.topcoder.util.distribution.DistributionScriptExecutionContext)}.
	 */
	public void testExecuteCommand_NullSource() {
		try {
			new CopyFileCommand(null, new ArrayList<CommandExecutionCondition>(), null, "test");
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
			// good
		}
	}

	/**
	 * Test method for {@link com.topcoder.util.distribution.commands.CopyFileCommand
	 * #executeCommand(com.topcoder.util.distribution.DistributionScriptExecutionContext)}.
	 */
	public void testExecuteCommand_EmptySource() {
		try {
			new CopyFileCommand(null, new ArrayList<CommandExecutionCondition>(), "\t\n", "test");
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
			// good
		}
	}

	/**
	 * Test method for {@link com.topcoder.util.distribution.commands.CopyFileCommand
	 * #executeCommand(com.topcoder.util.distribution.DistributionScriptExecutionContext)}.
	 */
	public void testExecuteCommand_NullDest() {
		try {
			new CopyFileCommand(null, new ArrayList<CommandExecutionCondition>(), "test", null);
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
			// good
		}
	}

	/**
	 * Test method for {@link com.topcoder.util.distribution.commands.CopyFileCommand
	 * #executeCommand(com.topcoder.util.distribution.DistributionScriptExecutionContext)}.
	 */
	public void testExecuteCommand_EmptyDest() {
		try {
			new CopyFileCommand(null, new ArrayList<CommandExecutionCondition>(), "test", "\t\n");
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
			// good
		}
	}

	/**
	 * Test method for {@link com.topcoder.util.distribution.commands.CopyFileCommand
	 * #executeCommand(com.topcoder.util.distribution.DistributionScriptExecutionContext)}.
	 * @throws Exception to JUnit
	 */
	public void testCopyFileCommand_Null() throws Exception {
		try {
			instance.execute(null);
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
			// good
		}
	}

	/**
	 * Test method for {@link com.topcoder.util.distribution.commands.CopyFileCommand
	 * #executeCommand(com.topcoder.util.distribution.DistributionScriptExecutionContext)}.
	 * @throws Exception to JUnit
	 */
	public void testCopyFileCommand_InputFileNotFound() throws Exception {
		try {
			instance.execute(new DistributionScriptExecutionContext());
			fail("InputFileNotFoundException expected");
		} catch (InputFileNotFoundException e) {
			// good
		}
	}

}
