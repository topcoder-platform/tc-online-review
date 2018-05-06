/**
 *
 * Copyright © 2004, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.net.ldap.sdkinterface;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.util.net.ldap.sdkinterface.netscape.NetscapeFactoryTest;
import com.topcoder.util.net.ldap.sdkinterface.netscape.NetscapeConnectionTest;


/**
 * <p>This test case aggregates all Unit test cases.</p>
 *
 * @author TopCoder
 * @version 1.0
 */
public class UnitTests extends TestCase {

    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTest(EntryTest.suite());
        suite.addTest(ModificationTest.suite());
        suite.addTest(LDAPSDKTest.suite());
        suite.addTest(LDAPSDKExceptionFactoryTest.suite());
        suite.addTest(UpdateTest.suite());
        suite.addTest(ValuesTest.suite());
        suite.addTest(NetscapeFactoryTest.suite());
        suite.addTest(NetscapeConnectionTest.suite());
        return suite;
    }

}
