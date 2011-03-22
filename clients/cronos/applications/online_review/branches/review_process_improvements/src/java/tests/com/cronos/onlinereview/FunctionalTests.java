/**
 *
 * Copyright (c) 2010, TopCoder, Inc. All rights reserved
 */
package com.cronos.onlinereview;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>This test case aggregates all functional test cases.</p>
 *
 * <p>
 * Version 1.1 (Online Review Update Review Management Process assembly 2) change notes:
 * - Add <code>NewAppealsTests</code> and <code>PrimaryAppealsResponseTests</code> test suites.
 * </p>
 *
 * @author TopCoder, TCSASSEMBER
 * @version 1.1
 */
public class FunctionalTests extends TestCase {

    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTestSuite(LoginTests.class);
        suite.addTestSuite(ManageProjectTests.class);
        suite.addTestSuite(LinkProjectsTests.class);
        suite.addTestSuite(OpenPhaseTests.class);
        //suite.addTestSuite(EditProjectTests.class);
        suite.addTestSuite(NewAppealsTests.class);
        suite.addTestSuite(PrimaryAppealsResponseTests.class);
        return suite;
    }
}
