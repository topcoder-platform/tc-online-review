/**
 *
 * Copyright ?2004, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.net.ldap.sdkinterface.stresstests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.TestResult;

/**
 * <p>This test case aggregates all Stress test cases.</p>
 *
 * @author TopCoder
 * @version 1.0
 */
public class StressTests extends TestCase {

    public static Test suite() {
        final TestSuite suite = new TestSuite();
        //suite.addTest(XXX.suite());
        suite.addTest(LDAPStressTest.suite());
        return suite;
    }
}
