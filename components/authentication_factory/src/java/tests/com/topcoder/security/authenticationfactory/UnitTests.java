/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.security.authenticationfactory;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * This test case aggregates all Unit test cases.
 *
 * @author TCSDEVELOPER
 * @version 2.0
 */
public class UnitTests extends TestCase {
    /**
     * Return the Unit test cases.
     *
     * @return a <code>Test</code> object with alle the units tests
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTest(AbstractAuthenticatorTest.suite());
        suite.addTest(AuthenticateExceptionTest.suite());
        suite.addTest(AuthenticationFactoryTest.suite());
        suite.addTest(ConfigurationExceptionTest.suite());
        suite.addTest(DefaultKeyConverterTest.suite());
        suite.addTest(HTTPBasicAuthenticatorTest.suite());
        suite.addTest(HttpCookieTest.suite());
        suite.addTest(HttpResourceImplTest.suite());
        suite.addTest(InvalidPrincipalExceptionTest.suite());
        suite.addTest(MissingPrincipalKeyExceptionTest.suite());
        suite.addTest(PrincipalTest.suite());
        suite.addTest(ResponseTest.suite());
        suite.addTest(TimeoutCacheFactoryTest.suite());

        // demo testcase
        suite.addTest(new TestSuite(DemoTest.class));

        return suite;
    }
}