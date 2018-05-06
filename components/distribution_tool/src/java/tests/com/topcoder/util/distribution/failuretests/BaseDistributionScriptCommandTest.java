/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.distribution.failuretests;

import java.util.ArrayList;

import com.topcoder.util.distribution.DistributionScriptCommandExecutionException;
import com.topcoder.util.distribution.DistributionScriptExecutionContext;
import com.topcoder.util.distribution.commands.BaseDistributionScriptCommand;
import com.topcoder.util.distribution.commands.CommandExecutionCondition;

import junit.framework.TestCase;

/**
 * Failure tests for <code>BaseDistributionScriptCommand</code>.
 * @author assistant
 * @version 1.0
 */
public class BaseDistributionScriptCommandTest extends TestCase {


	/**
	 * Test method for {@link com.topcoder.util.distribution.commands.BaseDistributionScriptCommand
	 * #execute(com.topcoder.util.distribution.DistributionScriptExecutionContext)}.
	 * @throws Exception to JUnit
	 */
	public void testExecute_Null() throws Exception {
		try {
			new BaseDistributionScriptCommand(null, new ArrayList<CommandExecutionCondition>()) {
				@Override
				protected void executeCommand(
						DistributionScriptExecutionContext context)
						throws DistributionScriptCommandExecutionException {
				}}.execute(null);
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
			// good
		}
	}

}
