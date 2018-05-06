/**
 *
 * Copyright ?2004, TopCoder, Inc. All rights reserved
 */
 package com.topcoder.util.net.ldap.sdkinterface.failuretests;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.TestResult;

/**
 * <p>This test case aggregates all Failure test cases.</p>
 *
 * @author TopCoder
 * @version 1.0
 */
public class FailureTests extends TestCase {

    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTestSuite(EntryFailureTest.class);
        suite.addTestSuite(LDAPSDKFailureTest.class);
        suite.addTestSuite(UpdateFailureTest.class);
        suite.addTestSuite(ValuesFailureTest.class);
        suite.addTestSuite(NetscapeConnectionFailureTest.class);
        return suite;
    }

}
