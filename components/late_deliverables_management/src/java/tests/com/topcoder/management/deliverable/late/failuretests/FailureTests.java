/**
 * Copyright (c) 2010, TopCoder, Inc. All rights reserved
 */
package com.topcoder.management.deliverable.late.failuretests;

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
		suite.addTestSuite(DatabaseLateDeliverablePersistenceFailureTest.class);
		suite.addTestSuite(LateDeliverableFilterBuilderFailureTest.class);
		suite.addTestSuite(LateDeliverableManagerImplFailureTest.class);
		return suite;
	}

}
