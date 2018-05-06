/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.distribution.failuretests;

import java.io.ByteArrayInputStream;

import junit.framework.TestCase;

import com.topcoder.util.distribution.DistributionScript;
import com.topcoder.util.distribution.DistributionScriptParsingException;
import com.topcoder.util.distribution.parsers.DistributionScriptParserImpl;

/**
 * Failure tests for <code>DistributionScriptParserImpl</code>.
 * @author assistant
 * @version 1.0
 */
public class DistributionScriptParserImplTest extends TestCase {

	/**
	 * Instance to test.
	 */
	private DistributionScriptParserImpl instance;

	/**
	 * Sets up the environment.
	 * @throws Exception to JUnit
	 */
	protected void setUp() throws Exception {
		super.setUp();
		instance = new DistributionScriptParserImpl();
	}

	/**
	 * Test method for {@link com.topcoder.util.distribution.parsers.DistributionScriptParserImpl
	 * #parseCommands(java.io.InputStream, com.topcoder.util.distribution.DistributionScript,
	 * com.topcoder.util.log.Log)}.
	 * @throws Exception to JUnit
	 */
	public void testParseCommands_NullStream() throws Exception {
		try {
			instance.parseCommands(null, new DistributionScript(), null);
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
			// good
		}
	}

	/**
	 * Test method for {@link com.topcoder.util.distribution.parsers.DistributionScriptParserImpl
	 * #parseCommands(java.io.InputStream, com.topcoder.util.distribution.DistributionScript,
	 * com.topcoder.util.log.Log)}.
	 * @throws Exception to JUnit
	 */
	public void testParseCommands_NullScript() throws Exception {
		try {
			instance.parseCommands(new ByteArrayInputStream("test".getBytes()), null, null);
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
			// good
		}
	}

	/**
	 * Test method for {@link com.topcoder.util.distribution.parsers.DistributionScriptParserImpl
	 * #parseCommands(java.io.InputStream, com.topcoder.util.distribution.DistributionScript,
	 * com.topcoder.util.log.Log)}.
	 * @throws Exception to JUnit
	 */
	public void testParseCommands_ParseError() throws Exception {
		try {
			instance.parseCommands(new ByteArrayInputStream("test".getBytes()), new DistributionScript(), null);
			fail("DistributionScriptParsingException expected");
		} catch (DistributionScriptParsingException e) {
			// good
		}
	}
}
