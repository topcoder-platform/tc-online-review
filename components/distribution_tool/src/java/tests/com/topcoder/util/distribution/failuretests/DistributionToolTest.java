/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.distribution.failuretests;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.util.distribution.DistributionScript;
import com.topcoder.util.distribution.DistributionTool;
import com.topcoder.util.distribution.DistributionToolConfigurationException;
import com.topcoder.util.distribution.MissingInputParameterException;
import com.topcoder.util.distribution.UnknownDistributionTypeException;

/**
 * Failure tests for <code>DistributionTool</code>.
 * 
 * @author assistant
 * @version 1.0
 */
public class DistributionToolTest extends TestCase {

	/**
	 * Instance to test.
	 */
	private DistributionTool instance;

	/**
	 * Sets up the environment.
	 * 
	 * @throws Exception
	 *             to JUnit
	 */
	protected void setUp() throws Exception {
		super.setUp();
		instance = new DistributionTool(
				"test_files/failure/DistributionTool.properties",
				"com.topcoder.util.distribution.DistributionTool");
	}

	/**
	 * Test method for
	 * {@link com.topcoder.util.distribution.DistributionTool #DistributionTool(java.lang.String, java.lang.String)}
	 * .
	 */
	public void testDistributionToolStringString_NullConfigFile() {
		try {
			new DistributionTool(null, "test");
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
			// good
		}
	}

	/**
	 * Test method for
	 * {@link com.topcoder.util.distribution.DistributionTool #DistributionTool(java.lang.String, java.lang.String)}
	 * .
	 */
	public void testDistributionToolStringString_EmptyConfigFile() {
		try {
			new DistributionTool("\t", "test");
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
			// good
		}
	}

	/**
	 * Test method for
	 * {@link com.topcoder.util.distribution.DistributionTool #DistributionTool(java.lang.String, java.lang.String)}
	 * .
	 */
	public void testDistributionToolStringString_NullNamespace() {
		try {
			new DistributionTool(
					"test_files/failure/DistributionTool.properties", null);
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
			// good
		}
	}

	/**
	 * Test method for
	 * {@link com.topcoder.util.distribution.DistributionTool #DistributionTool(java.lang.String, java.lang.String)}
	 * .
	 */
	public void testDistributionToolStringString_EmptyNamespace() {
		try {
			new DistributionTool(
					"test_files/failure/DistributionTool.properties", "\t\n");
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
			// good
		}
	}

	/**
	 * Test method for
	 * {@link com.topcoder.util.distribution.DistributionTool #DistributionTool(java.lang.String, java.lang.String)}
	 * .
	 */
	public void testDistributionToolStringString_FileNotFound() {
		try {
			new DistributionTool("not.exist", "test");
			fail("DistributionToolConfigurationException expected");
		} catch (DistributionToolConfigurationException e) {
			// good
		}
	}

	/**
	 * Test method for
	 * {@link com.topcoder.util.distribution.DistributionTool #DistributionTool(java.lang.String, java.lang.String)}
	 * .
	 */
	public void testDistributionToolStringString_Invalid0() {
		try {
			new DistributionTool(
					"test_files/failure/DistributionTool0.properties", "test");
			fail("DistributionToolConfigurationException expected");
		} catch (DistributionToolConfigurationException e) {
			// good
		}
	}

	/**
	 * Test method for
	 * {@link com.topcoder.util.distribution.DistributionTool #DistributionTool(java.lang.String, java.lang.String)}
	 * .
	 */
	public void testDistributionToolStringString_Invalid1() {
		try {
			new DistributionTool(
					"test_files/failure/DistributionTool1.properties", "test");
			fail("DistributionToolConfigurationException expected");
		} catch (DistributionToolConfigurationException e) {
			// good
		}
	}

	/**
	 * Test method for
	 * {@link com.topcoder.util.distribution.DistributionTool #DistributionTool(java.lang.String, java.lang.String)}
	 * .
	 */
	public void testDistributionToolStringString_Invalid2() {
		try {
			new DistributionTool(
					"test_files/failure/DistributionTool2.properties", "test");
			fail("DistributionToolConfigurationException expected");
		} catch (DistributionToolConfigurationException e) {
			// good
		}
	}

	/**
	 * Test method for
	 * {@link com.topcoder.util.distribution.DistributionTool #DistributionTool(java.lang.String, java.lang.String)}
	 * .
	 */
	public void testDistributionToolStringString_Invalid3() {
		try {
			new DistributionTool(
					"test_files/failure/DistributionTool3.properties", "test");
			fail("DistributionToolConfigurationException expected");
		} catch (DistributionToolConfigurationException e) {
			// good
		}
	}

	/**
	 * Test method for
	 * {@link com.topcoder.util.distribution.DistributionTool #DistributionTool(com.topcoder.configuration.ConfigurationObject)}
	 * .
	 */
	public void testDistributionToolConfigurationObject_Null() {
		try {
			new DistributionTool((ConfigurationObject) null);
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
			// good
		}
	}

	/**
	 * Test method for
	 * {@link com.topcoder.util.distribution.DistributionTool #DistributionTool(java.util.Map)}
	 * .
	 */
	public void testDistributionToolMap_Null() {
		try {
			new DistributionTool((Map<String, DistributionScript>) null);
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
			// good
		}
	}

	/**
	 * Test method for
	 * {@link com.topcoder.util.distribution.DistributionTool #DistributionTool(java.util.Map)}
	 * .
	 */
	public void testDistributionToolMap_Empty() {
		try {
			new DistributionTool(new HashMap<String, DistributionScript>());
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
			// good
		}
	}

	/**
	 * Test method for
	 * {@link com.topcoder.util.distribution.DistributionTool #DistributionTool(java.util.Map)}
	 * .
	 */
	public void testDistributionToolMap_NullKey() {
		Map<String, DistributionScript> map = new HashMap<String, DistributionScript>();
		map.put(null, new DistributionScript());
		try {
			new DistributionTool(map);
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
			// good
		}
	}

	/**
	 * Test method for
	 * {@link com.topcoder.util.distribution.DistributionTool #DistributionTool(java.util.Map)}
	 * .
	 */
	public void testDistributionToolMap_EmptyKey() {
		Map<String, DistributionScript> map = new HashMap<String, DistributionScript>();
		map.put("", new DistributionScript());
		try {
			new DistributionTool(map);
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
			// good
		}
	}

	/**
	 * Test method for
	 * {@link com.topcoder.util.distribution.DistributionTool #createDistribution(java.lang.String, java.util.Map)}
	 * .
	 * 
	 * @throws Exception
	 *             to JUnit
	 */
	public void testCreateDistribution_NullType()
			throws UnknownDistributionTypeException,
			MissingInputParameterException, Exception {
		try {
			instance.createDistribution(null, new HashMap<String, String>());
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
			// good
		}
	}

	/**
	 * Test method for
	 * {@link com.topcoder.util.distribution.DistributionTool #createDistribution(java.lang.String, java.util.Map)}
	 * .
	 * 
	 * @throws Exception
	 *             to JUnit
	 */
	public void testCreateDistribution_EmptyType()
			throws UnknownDistributionTypeException,
			MissingInputParameterException, Exception {
		try {
			instance.createDistribution("\t\n", new HashMap<String, String>());
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
			// good
		}
	}

	/**
	 * Test method for
	 * {@link com.topcoder.util.distribution.DistributionTool #createDistribution(java.lang.String, java.util.Map)}
	 * .
	 * 
	 * @throws Exception
	 *             to JUnit
	 */
	public void testCreateDistribution_NullKey()
			throws UnknownDistributionTypeException,
			MissingInputParameterException, Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put(null, "test");
		try {
			instance.createDistribution("est", map);
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
			// good
		}
	}

	/**
	 * Test method for
	 * {@link com.topcoder.util.distribution.DistributionTool #createDistribution(java.lang.String, java.util.Map)}
	 * .
	 * 
	 * @throws Exception
	 *             to JUnit
	 */
	public void testCreateDistribution_NullValue()
			throws UnknownDistributionTypeException,
			MissingInputParameterException, Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("test", null);
		try {
			instance.createDistribution("est", map);
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
			// good
		}
	}

	/**
	 * Test method for
	 * {@link com.topcoder.util.distribution.DistributionTool #createDistribution(java.lang.String, java.util.Map)}
	 * .
	 * 
	 * @throws Exception
	 *             to JUnit
	 */
	public void testCreateDistribution_EmptyKey()
			throws UnknownDistributionTypeException,
			MissingInputParameterException, Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("", "test");
		try {
			instance.createDistribution("est", map);
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
			// good
		}
	}

	/**
	 * Test method for
	 * {@link com.topcoder.util.distribution.DistributionTool #createDistribution(java.lang.String, java.util.Map)}
	 * .
	 * 
	 * @throws Exception
	 *             to JUnit
	 */
	public void testCreateDistribution_UnkownDistribution()
			throws UnknownDistributionTypeException,
			MissingInputParameterException, Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("test", "test");
		try {
			instance.createDistribution("est", map);
			fail("UnknownDistributionTypeException expected");
		} catch (UnknownDistributionTypeException e) {
			// good
		}
	}

	/**
	 * Test method for
	 * {@link com.topcoder.util.distribution.DistributionTool #createDistribution(java.lang.String, java.util.Map)}
	 * .
	 * @throws Exception
	 *             to JUnit
	 */
	public void testCreateDistribution_MisingRequiredParams()
			throws UnknownDistributionTypeException,
			MissingInputParameterException, Exception {
		Map<String, String> map = new HashMap<String, String>();
		try {
			instance.createDistribution("java", map);
			fail("MissingInputParameterException expected");
		} catch (MissingInputParameterException e) {
			// good
		}
	}

	/**
	 * Test method for
	 * {@link com.topcoder.util.distribution.DistributionTool #createDistribution(java.lang.String, java.util.Map)}.
	 * @throws Exception
	 *             to JUnit
	 */
	public void testCreateDistribution_CannotFindScript()
			throws UnknownDistributionTypeException,
			MissingInputParameterException, Exception {
		try {
			instance = new DistributionTool(
					"test_files/failure/DistributionTool5.properties",
					"com.topcoder.util.distribution.DistributionTool");
		} catch (DistributionToolConfigurationException e) {
			// good
		}
	}

	/**
	 * Test method for
	 * {@link com.topcoder.util.distribution.DistributionTool #createDistribution(java.lang.String, java.util.Map)}.
	 * @throws Exception
	 *             to JUnit
	 */
	public void testCreateDistribution_MalformedScript0()
			throws UnknownDistributionTypeException,
			MissingInputParameterException, Exception {
		try {
			instance = new DistributionTool(
					"test_files/failure/DistributionTool5.properties",
					"com.topcoder.util.distribution.DistributionTool");
		} catch (DistributionToolConfigurationException e) {
			// good
		}
	}

	/**
	 * Test method for
	 * {@link com.topcoder.util.distribution.DistributionTool #createDistribution(java.lang.String, java.util.Map)}.
	 * @throws Exception
	 *             to JUnit
	 */
	public void testCreateDistribution_MalformedScript1()
			throws UnknownDistributionTypeException,
			MissingInputParameterException, Exception {
		try {
			instance = new DistributionTool(
					"test_files/failure/DistributionTool6.properties",
					"com.topcoder.util.distribution.DistributionTool");
		} catch (DistributionToolConfigurationException e) {
			// good
		}
	}

	/**
	 * Test method for
	 * {@link com.topcoder.util.distribution.DistributionTool #createDistribution(java.lang.String, java.util.Map)}.
	 * @throws Exception
	 *             to JUnit
	 */
	public void testCreateDistribution_MalformedScript2()
			throws UnknownDistributionTypeException,
			MissingInputParameterException, Exception {
		try {
			instance = new DistributionTool(
					"test_files/failure/DistributionTool7.properties",
					"com.topcoder.util.distribution.DistributionTool");
		} catch (DistributionToolConfigurationException e) {
			// good
		}
	}

	/**
	 * Test method for
	 * {@link com.topcoder.util.distribution.DistributionTool #createDistribution(java.lang.String, java.util.Map)}.
	 * @throws Exception
	 *             to JUnit
	 */
	public void testCreateDistribution_MalformedScript3()
			throws UnknownDistributionTypeException,
			MissingInputParameterException, Exception {
		try {
			instance = new DistributionTool(
					"test_files/failure/DistributionTool8.properties",
					"com.topcoder.util.distribution.DistributionTool");
		} catch (DistributionToolConfigurationException e) {
			// good
		}
	}
}
