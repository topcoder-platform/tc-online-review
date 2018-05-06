/*
 * Copyright (C) 2006-2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.phase.accuracytests;

import junit.framework.TestCase;

import com.topcoder.management.phase.PhaseHandlingException;
import com.topcoder.management.phase.PhaseManagementException;
import com.topcoder.util.errorhandling.ExceptionData;

/**
 * <p>
 * Accuracy test fixture for PhaseHandlingException class.
 * </p>
 *
 * @author stevenfrog
 * @version 1.1
 */
public class PhaseHandlingExceptionAccTest extends TestCase {
	/**
	 * <p>
	 * The default error message used for tests.
	 * </p>
	 */
	private static final String ERROR_MESSAGE = "error";

	/**
	 * <p>
	 * Tests the constructor with message.
	 * </p>
	 */
	public void testConstructor_Message() {
		Exception exception = new PhaseHandlingException(ERROR_MESSAGE);
		assertTrue("Incorrect inheritance.", exception instanceof PhaseManagementException);
		assertEquals("Incorrect error message.", ERROR_MESSAGE, exception.getMessage());
		assertNull("Incorrect cause.", exception.getCause());
	}

	/**
	 * <p>
	 * Tests the constructor with message and cause.
	 * </p>
	 */
	public void testConstructor_MessageCause() {
		Throwable cause = new Throwable();
		Exception exception = new PhaseHandlingException(ERROR_MESSAGE, cause);
		assertTrue("Incorrect inheritance.", exception instanceof PhaseManagementException);
		assertEquals("Incorrect error message.", ERROR_MESSAGE + ", caused by null", exception
			.getMessage());
		assertEquals("Incorrect cause.", cause, exception.getCause());
	}

	/**
	 * <p>
	 * Tests the constructor with message and cause.
	 * </p>
	 */
	public void testConstructor_MessageData() {
		ExceptionData data = new ExceptionData();
		data.setApplicationCode("AppCode");
		PhaseHandlingException exception = new PhaseHandlingException(ERROR_MESSAGE, data);
		assertTrue("Incorrect inheritance.", exception instanceof PhaseManagementException);
		assertEquals("Incorrect error message.", ERROR_MESSAGE, exception.getMessage());
		assertEquals("Incorrect application code.", "AppCode", exception.getApplicationCode());
	}

	/**
	 * <p>
	 * Tests the constructor with message and cause.
	 * </p>
	 */
	public void testConstructor_MessageCauseData() {
		Throwable cause = new Throwable();
		ExceptionData data = new ExceptionData();
		data.setApplicationCode("AppCode");
		PhaseHandlingException exception = new PhaseHandlingException(ERROR_MESSAGE, cause, data);
		assertTrue("Incorrect inheritance.", exception instanceof PhaseManagementException);
		assertEquals("Incorrect error message.", ERROR_MESSAGE, exception.getMessage());
		assertEquals("Incorrect cause.", cause, exception.getCause());
		assertEquals("Incorrect application code.", "AppCode", exception.getApplicationCode());
	}
}