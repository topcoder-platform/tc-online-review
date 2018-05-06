/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.login.failuretests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * This test case aggregates all Failure test cases.
 * </p>
 *
 * @author TopCoder
 * @version 1.0
 */
public class FailureTests extends TestCase {
    /**
     * <p>
     * The failure test suite.
     * </p>
     *
     * @return the failure test suite
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTestSuite(LoginActionsFailureTests.class);
        suite.addTestSuite(SecurityManagerAuthenticatorFailureTests.class);
        suite.addTestSuite(SecurityManagerAuthResponseParserFailureTests.class);
        suite.addTestSuite(AuthCookieManagerImplFailureTests.class);
        return suite;
    }

}
