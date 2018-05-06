/**
 * Copyright (c)2005, TopCoder, Inc. All rights reserved
 */
package com.topcoder.security.authenticationfactory.stresstests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * <p>This test case aggregates all Stress test cases.</p>
 *
 * @author adic
 * @version 2.0
 */
public class StressTests extends TestCase {
    
    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTestSuite(AuthenticationFactoryBenchmark.class);        
//        suite.addTestSuite(HTTPBasicAuthenticatorBenchmark.class);
//        suite.addTestSuite(HTTPBasicAuthenticatorCachedBenchmark.class);
//        suite.addTestSuite(HttpCookieBenchmark.class);
//        suite.addTestSuite(PrincipalBenchmark.class);
        return suite;
    }
    
}
