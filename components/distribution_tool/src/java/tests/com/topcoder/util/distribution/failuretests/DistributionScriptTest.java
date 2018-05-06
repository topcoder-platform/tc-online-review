/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.distribution.failuretests;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import com.topcoder.util.distribution.DistributionScript;
import com.topcoder.util.distribution.DistributionScriptCommand;

/**
 * Failure tests for <code>DistributionScript</code>.
 * @author assistant
 * @version 1.0
 */
public class DistributionScriptTest extends TestCase {

	/**
	 * Instance to test.
	 */
	private DistributionScript instance;

	/**
	 * Sets up the environment.
	 * @throws Exception to JUnit
	 */
	protected void setUp() throws Exception {
		super.setUp();
		instance = new DistributionScript();
	}

	/**
	 * Test method for {@link com.topcoder.util.distribution.DistributionScript
	 * #setCommands(java.util.List)}.
	 */
	public void testSetCommands_Null() {
		try {
			instance.setCommands(null);
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
			// good
		}
	}

	/**
	 * Test method for {@link com.topcoder.util.distribution.DistributionScript
	 * #setCommands(java.util.List)}.
	 */
	public void testSetCommands_ContainsNull() {
		try {
			instance.setCommands(Arrays.asList(new DistributionScriptCommand[] {null}));
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
			// good
		}
	}

	/**
	 * Test method for {@link com.topcoder.util.distribution.DistributionScript
	 * #setRequiredParams(java.util.List)}.
	 */
	public void testSetRequiredParams_Null() {
		try {
			instance.setRequiredParams(null);
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
			// good
		}
	}

	/**
	 * Test method for {@link com.topcoder.util.distribution.DistributionScript
	 * #setRequiredParams(java.util.List)}.
	 */
	public void testSetRequiredParams_ContainsNull() {
		try {
			instance.setRequiredParams(Arrays.asList(new String[] {null}));
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
			// good
		}
	}

	/**
	 * Test method for {@link com.topcoder.util.distribution.DistributionScript
	 * #setRequiredParams(java.util.List)}.
	 */
	public void testSetRequiredParams_ContainsEmpty() {
		try {
			instance.setRequiredParams(Arrays.asList(new String[] {"\t \n"}));
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
			// good
		}
	}

	/**
	 * Test method for {@link com.topcoder.util.distribution.DistributionScript
	 * #setDefaultParams(java.util.Map)}.
	 */
	public void testSetDefaultParams_Null() {
		try {
			instance.setDefaultParams(null);
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
			// good
		}
	}

	/**
	 * Test method for {@link com.topcoder.util.distribution.DistributionScript
	 * #setDefaultParams(java.util.Map)}.
	 */
	public void testSetDefaultParams_NullKey() {
		Map<String, String> map = new HashMap<String, String>();
		map.put(null, "test");
		try {
			instance.setDefaultParams(map);
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
			// good
		}
	}

	/**
	 * Test method for {@link com.topcoder.util.distribution.DistributionScript
	 * #setDefaultParams(java.util.Map)}.
	 */
	public void testSetDefaultParams_EmptyKey() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("\t \n", "test");
		try {
			instance.setDefaultParams(map);
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
			// good
		}
	}

	/**
	 * Test method for {@link com.topcoder.util.distribution.DistributionScript
	 * #setDefaultParams(java.util.Map)}.
	 */
	public void testSetDefaultParams_NullValue() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("test", null);
		try {
			instance.setDefaultParams(map);
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
			// good
		}
	}

	/**
	 * Test method for {@link com.topcoder.util.distribution.DistributionScript
	 * #setScriptFolder(java.lang.String)}.
	 */
	public void testSetScriptFolder_Null() {
		try {
			instance.setScriptFolder(null);
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
			// good
		}
	}

	/**
	 * Test method for {@link com.topcoder.util.distribution.DistributionScript
	 * #setScriptFolder(java.lang.String)}.
	 */
	public void testSetScriptFolder_Empty() {
		try {
			instance.setScriptFolder(" \t");
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
			// good
		}
	}
}
