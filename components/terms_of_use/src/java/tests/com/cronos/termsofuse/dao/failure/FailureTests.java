/**
 * Copyright (c) 2011, TopCoder, Inc. All rights reserved
 */
package com.cronos.termsofuse.dao.failure;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * This test case aggregates all Failure test cases.
 * </p>
 * 
 * @author mumujava
 * @version 1.0
 */
public class FailureTests extends TestCase {

	public static Test suite() {
		final TestSuite suite = new TestSuite();
		suite.addTestSuite(ProjectTermsOfUseDaoImplFailureTest.class);
		suite.addTestSuite(TermsOfUseDaoImplFailureTest.class);
		suite.addTestSuite(UserTermsOfUseDaoImplFailureTest.class);
		return suite;
	}

}
