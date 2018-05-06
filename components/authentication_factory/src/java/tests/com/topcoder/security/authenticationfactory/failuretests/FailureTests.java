/**
 *
 * Copyright © 2003, TopCoder, Inc. All rights reserved
 */
 package com.topcoder.security.authenticationfactory.failuretests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import com.topcoder.security.authenticationfactory.failuretests.http.basicimpl.HttpResourceImplV11FailureTests;
import com.topcoder.security.authenticationfactory.failuretests.http.basicimpl.HTTPBasicAuthenticatorFailureTest;
import com.topcoder.security.authenticationfactory.failuretests.http.HttpCookieV11FailureTests;

/**
 * <p>This test case aggregates all Failure test cases.</p>
 *
 * @author TopCoder
 * @version 1.0
 */
public class FailureTests extends TestCase {

    public static Test suite() {
        final TestSuite suite = new TestSuite();

        suite.addTest(AuthenticationFactoryFailureTest.suite());
        suite.addTest(DefaultKeyConverterFailureTest.suite());
        suite.addTest(MissingPrincipalKeyExceptionFailureTest.suite());
        suite.addTest(PrincipalFailureTest.suite());
        suite.addTest(ResponseFailureTest.suite());
        suite.addTest(TimeoutCacheFactoryFailureTest.suite());
        suite.addTest(AbstractAuthenticatorFailureTest.suite());
        suite.addTest(new TestSuite(HttpResourceImplV11FailureTests.class));
        suite.addTest(new TestSuite(HttpCookieV11FailureTests.class));
        suite.addTest(HTTPBasicAuthenticatorFailureTest.suite());

        return suite;
    }

}
