/**
 * Copyright (c) 2005, TopCoder, Inc. All rights reserved
 */
package com.topcoder.security.authenticationfactory.accuracytests;


/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * This test class aggregates all accuracy test cases.
 *
 * @author tuenm
 * @version 1.0
 * @copyright Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
public class AccuracyTests extends TestCase {
    /**
     * The test suite that contains all the unit test cases.
     *
     * @return the test suite.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        suite.addTestSuite(AbstractAuthenticatorAccuracyTest.class);
        suite.addTestSuite(AuthenticationFactoryAccuracyTest.class);
        suite.addTestSuite(HTTPBasicAuthenticatorAccuracyTest.class);
        suite.addTestSuite(DefaultKeyConverterAccuracyTest.class);
        suite.addTestSuite(PrincipalAccuracyTest.class);
        suite.addTestSuite(ResponseAccuracyTest.class);
        suite.addTestSuite(TimeoutCacheFactoryAccuracyTest.class);
        suite.addTestSuite(InvalidPrincipalExceptionAccuracyTest.class);
        suite.addTestSuite(MissingPrincipalKeyExceptionAccuracyTest.class);

        return suite;
    }
}
