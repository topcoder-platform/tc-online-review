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
 * @author TopCoder
 * @version 1.0
 */
public class FunctionalTests extends TestCase {

    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTestSuite(LoginTests.class);
        suite.addTestSuite(ManageProjectTests.class);
        suite.addTestSuite(LinkProjectsTests.class);
        suite.addTestSuite(OpenPhaseTests.class);
        //suite.addTestSuite(EditProjectTests.class);
        return suite;
    }
}
