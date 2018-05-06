/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.login.accuracytests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * <p>
 * This test case aggregates all Accuracy test cases.
 * </p>
 *
 * @author PE, onsky
 * @version 1.1
 * @since 1.0
 */
public class AccuracyTests extends TestCase {
    /**
     * <p>
     * Creates a test suite of the tests contained in this class.
     * </p>
     *
     * @return a test suite of the tests contained in this class.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        suite.addTestSuite(SecurityManagerAuthResponseParserAccuracyTest.class);
        suite.addTestSuite(SecurityManagerAuthenticatorAccuracyTest.class);
        suite.addTestSuite(LoginActionsAccuracyTest.class);
        suite.addTestSuite(AuthCookieManagerImplAccuracyTests.class);

        return suite;
    }
}
