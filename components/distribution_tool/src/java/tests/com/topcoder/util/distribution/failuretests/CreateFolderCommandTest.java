/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.distribution.failuretests;

import java.util.ArrayList;
import java.util.Arrays;

import junit.framework.TestCase;

import com.topcoder.util.distribution.DistributionScriptCommandExecutionException;
import com.topcoder.util.distribution.commands.CommandExecutionCondition;
import com.topcoder.util.distribution.commands.CreateFolderCommand;

/**
 * Failure tests for <code>CreateFolderCommand</code>.
 * @author assistant
 * @version 1.0
 */
public class CreateFolderCommandTest extends TestCase {

	/**
	 * Instance to test.
	 */
	private CreateFolderCommand instance;

	/**
	 * Sets up the environment.
	 * @throws Exception to JUnit
	 */
	protected void setUp() throws Exception {
		super.setUp();
		instance = new CreateFolderCommand(null, new ArrayList<CommandExecutionCondition>(), "test");
	}

	/**
	 * Test method for {@link com.topcoder.util.distribution.commands.CreateFolderCommand
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
	 * Test method for {@link com.topcoder.util.distribution.commands.CreateFolderCommand
	 * #CreateFolderCommand(com.topcoder.util.log.Log, java.util.List, java.lang.String)}.
	 */
	public void testCreateFolderCommand_NullConditions() {
		try {
			new CreateFolderCommand(null, null, "test");
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
			// good
		}
	}

	/**
	 * Test method for {@link com.topcoder.util.distribution.commands.CreateFolderCommand
	 * #CreateFolderCommand(com.topcoder.util.log.Log, java.util.List, java.lang.String)}.
	 */
	public void testCreateFolderCommand_NullInConditions() {
		try {
			new CreateFolderCommand(null, Arrays.asList(new CommandExecutionCondition[] {null}), "test");
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
			// good
		}
	}

	/**
	 * Test method for {@link com.topcoder.util.distribution.commands.CreateFolderCommand
	 * #CreateFolderCommand(com.topcoder.util.log.Log, java.util.List, java.lang.String)}.
	 */
	public void testCreateFolderCommand_NullName() {
		try {
			new CreateFolderCommand(null, new ArrayList<CommandExecutionCondition>(), null);
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
			// good
		}
	}
	/**
	 * Test method for {@link com.topcoder.util.distribution.commands.CreateFolderCommand
	 * #CreateFolderCommand(com.topcoder.util.log.Log, java.util.List, java.lang.String)}.
	 */
	public void testCreateFolderCommand_EmptyName() {
		try {
			new CreateFolderCommand(null, new ArrayList<CommandExecutionCondition>(), "\t\n");
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
			// good
		}
	}

}
