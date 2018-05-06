/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.phase.accuracytests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.topcoder.management.phase.OperationCheckResult;

import junit.framework.JUnit4TestAdapter;
import junit.framework.TestCase;

/**
 * <p>
 * Accuracy tests for class <code>OperationCheckResult</code>.
 * </p>
 *
 * @author stevenfrog
 * @version 1.1
 */
public class OperationCheckResultAccTest extends TestCase {
	/**
	 * <p>
	 * Represent the OperationCheckResult instance that used to call its method for test. It will be
	 * initialized in setUp().
	 * </p>
	 */
	private OperationCheckResult impl;

	/**
	 * <p>
	 * Creates a test suite for the tests in this test case.
	 * </p>
	 *
	 * @return a Test suite for this test case.
	 */
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(OperationCheckResultAccTest.class);
	}

	/**
	 * <p>
	 * Set the test environment.
	 * </p>
	 *
	 * @throws Exception
	 *             to JUnit
	 */
	@Before
	public void setUp() throws Exception {
		impl = new OperationCheckResult(true, "msg");
	}

	/**
	 * <p>
	 * Clear the test environment.
	 * </p>
	 *
	 * @throws Exception
	 *             to JUnit
	 */
	@After
	public void tearDown() throws Exception {
		impl = null;
	}

	/**
	 * <p>
	 * Inheritance test, verifies <code>OperationCheckResult</code> subclasses should be correct.
	 * </p>
	 */
	@Test
	public void testInheritance() {
		assertTrue("The instance's subclass is not correct.", impl instanceof Object);
	}

	/**
	 * <p>
	 * Accuracy test for the constructor <code>OperationCheckResult(boolean, String)</code>.<br>
	 * Instance should be created successfully.
	 * </p>
	 */
	@Test
	public void testConstructor() {
		assertNotNull("The instance should be created successfully", impl);
		assertTrue("The success flag should be true", impl.isSuccess());
		assertEquals("The message should be same as ", "msg", impl.getMessage());
	}

	/**
	 * <p>
	 * Accuracy test for the constructor <code>OperationCheckResult(String)</code>.<br>
	 * Instance should be created successfully.
	 * </p>
	 */
	@Test
	public void testConstructor2() {
		impl = new OperationCheckResult("msg");
		assertNotNull("The instance should be created successfully", impl);
		assertFalse("The success flag should be false", impl.isSuccess());
		assertEquals("The message should be same as ", "msg", impl.getMessage());
	}

	/**
	 * <p>
	 * Accuracy test the method of <code>isSuccess()</code>.<br>
	 * </p>
	 */
	public void testIsSuccess() {
		assertTrue("The success flag should be true", impl.isSuccess());
	}

	/**
	 * <p>
	 * Accuracy test the method of <code>getMessage()</code>.<br>
	 * </p>
	 */
	public void testGetMessage() {
		assertEquals("The message should be same as ", "msg", impl.getMessage());
	}

}
