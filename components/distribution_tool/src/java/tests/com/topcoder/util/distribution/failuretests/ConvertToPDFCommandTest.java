/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.distribution.failuretests;

import java.util.ArrayList;
import java.util.Arrays;

import junit.framework.TestCase;

import com.topcoder.util.distribution.DistributionScriptExecutionContext;
import com.topcoder.util.distribution.InputFileNotFoundException;
import com.topcoder.util.distribution.PDFConversionException;
import com.topcoder.util.distribution.commands.CommandExecutionCondition;
import com.topcoder.util.distribution.commands.ConvertToPDFCommand;

/**
 * Failure tests for <code>ConvertToPDFCommand</code>.
 * @author assistant
 * @version 1.0
 */
public class ConvertToPDFCommandTest extends TestCase {

	/**
	 * Instance to test.
	 */
	private ConvertToPDFCommand instance;

	/**
	 * Sets up the environment.
	 * @throws Exception to JUnit
	 */
	protected void setUp() throws Exception {
		super.setUp();
		instance = new ConvertToPDFCommand(null, new ArrayList<CommandExecutionCondition>(), "test", "test");
	}

	/**
	 * Test method for {@link com.topcoder.util.distribution.commands.ConvertToPDFCommand
	 * #executeCommand(com.topcoder.util.distribution.DistributionScriptExecutionContext)}.
	 */
	public void testExecuteCommand_NullConditions() {
		try {
			new ConvertToPDFCommand(null, null, "test", "test");
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
			// good
		}
	}

	/**
	 * Test method for {@link com.topcoder.util.distribution.commands.ConvertToPDFCommand
	 * #executeCommand(com.topcoder.util.distribution.DistributionScriptExecutionContext)}.
	 */
	public void testExecuteCommand_ContainsNull() {
		try {
			new ConvertToPDFCommand(null, Arrays.asList(new CommandExecutionCondition[] {null}), "test", "test");
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
			// good
		}
	}

	/**
	 * Test method for {@link com.topcoder.util.distribution.commands.ConvertToPDFCommand
	 * #executeCommand(com.topcoder.util.distribution.DistributionScriptExecutionContext)}.
	 */
	public void testExecuteCommand_NullSource() {
		try {
			new ConvertToPDFCommand(null, new ArrayList<CommandExecutionCondition>(), null, "test");
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
			// good
		}
	}

	/**
	 * Test method for {@link com.topcoder.util.distribution.commands.ConvertToPDFCommand
	 * #executeCommand(com.topcoder.util.distribution.DistributionScriptExecutionContext)}.
	 */
	public void testExecuteCommand_EmptySource() {
		try {
			new ConvertToPDFCommand(null, new ArrayList<CommandExecutionCondition>(), "\t\n", "test");
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
			// good
		}
	}

	/**
	 * Test method for {@link com.topcoder.util.distribution.commands.ConvertToPDFCommand
	 * #executeCommand(com.topcoder.util.distribution.DistributionScriptExecutionContext)}.
	 */
	public void testExecuteCommand_NullDest() {
		try {
			new ConvertToPDFCommand(null, new ArrayList<CommandExecutionCondition>(), "test", null);
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
			// good
		}
	}

	/**
	 * Test method for {@link com.topcoder.util.distribution.commands.ConvertToPDFCommand
	 * #executeCommand(com.topcoder.util.distribution.DistributionScriptExecutionContext)}.
	 */
	public void testExecuteCommand_EmptyDest() {
		try {
			new ConvertToPDFCommand(null, new ArrayList<CommandExecutionCondition>(), "test", "\t\n");
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
			// good
		}
	}

	/**
	 * Test method for {@link com.topcoder.util.distribution.commands.ConvertToPDFCommand
	 * #executeCommand(com.topcoder.util.distribution.DistributionScriptExecutionContext)}.
	 * @throws Exception to JUnit
	 */
	public void testConvertToPDFCommand_Null() throws Exception {
		try {
			instance.execute(null);
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
			// good
		}
	}

	/**
	 * Test method for {@link com.topcoder.util.distribution.commands.ConvertToPDFCommand
	 * #executeCommand(com.topcoder.util.distribution.DistributionScriptExecutionContext)}.
	 * @throws Exception to JUnit
	 */
	public void testConvertToPDFCommand_InputFileNotFound() throws Exception {
		try {
			instance.execute(new DistributionScriptExecutionContext());
			fail("InputFileNotFoundException expected");
		} catch (InputFileNotFoundException e) {
			// good
		}
	}

}
