/**
 * Copyright &copy; TopCoder Inc, 2004. All rights reserved.
 */
package com.topcoder.util.net.ldap.sdkinterface.failuretests;
import com.topcoder.util.net.ldap.sdkinterface.*;

import junit.framework.TestCase;

/**
 * Failure test for LDAPSDK class
 *
 * @author Standlove
 */
public class LDAPSDKFailureTest extends TestCase {
    /**
     * com.topcoder.util.net.ldap.sdkinterface.netscape.Test the ctor with illegal arguments
     */
    public void testCtorFailure() throws Exception {
        try {
            new LDAPSDK(null);
            fail("the given class name is null");
        } catch (NullPointerException npe) {
        }

        try {
            new LDAPSDK(" ");
            fail("the given class name is empty");
        } catch (IllegalArgumentException iae) {
        }

        try {
            new LDAPSDK("non-exist");
            fail("the class can not be found");
        } catch (ClassNotFoundException e) {
        }

        try {
            new LDAPSDK("java.lang.String");
            fail("the class is not an LDAPSDKFactory");
        } catch (ClassCastException e) {
        }
    }
}