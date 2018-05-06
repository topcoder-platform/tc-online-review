/**
 *
 * Copyright (c) 2006, TopCoder, Inc. All rights reserved
 */
package com.cronos.onlinereview.login.stresstests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * This test case aggregates all Stress test cases.
 * </p>
 *
 * @author TopCoder
 * @version 1.0
 */
public class StressTests extends TestCase {

    /**
     * Aggregates all stress test cases.
     *
     * @return Test
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        suite.addTestSuite(StressTestLoginActionsLogin.class);

        suite.addTestSuite(StressTestLoginActionsLogout.class);
        suite.addTestSuite(AuthCookieManagerStressTest.class);
        return suite;
    }
}
