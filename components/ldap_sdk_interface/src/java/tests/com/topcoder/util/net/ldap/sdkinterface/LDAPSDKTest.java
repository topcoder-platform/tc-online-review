/*
 * TCS LDAP SDK Interface 1.0
 *
 * LDAPSDKTest.java
 *
 * Copyright (c), 2004. TopCoder, Inc. All rights reserved.
 *
 */
package com.topcoder.util.net.ldap.sdkinterface;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Test;

import com.topcoder.util.net.ldap.sdkinterface.netscape.NetscapeFactory;

/**
 * A test case testing the behavior of <code>LDAPSDKExceptionFactory</code> class. No special settings are required to
 * run this test.
 *
 * @author  isv
 * @version 1.0 05/18/2004
 */
public class LDAPSDKTest extends TestCase {

    public LDAPSDKTest(String testName) {
        super(testName);
    }

    /**
     * Tests the constructor accepting the name of class implementing <code>LDAPSDKFactory</code> interface. The test
     * simply verifies that the constructor does not throw any exception being provided with correct class name. Also
     * thet test verifies invalid arguments handling by the constructor.
     *
     * @throws ClassNotFoundException this is an unexpected exception that is not a result of test failure
     * @throws InstantiationException this is an unexpected exception that is not a result of test failure
     * @throws IllegalAccessException this is an unexpected exception that is not a result of test failure
     */
    public void testConstructor_String() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        LDAPSDK sdk = null;

        try {
            sdk = new LDAPSDK(NetscapeFactory.class.getName());
        } catch(Exception e) {
            fail("Nothing should prevent the instantiation of LDAPSDK with correct class name");
        }

        // Tests invalid argument handling
        try {
            sdk = new LDAPSDK("Non-existing class");
            fail("ClassNotFoundException should be thrown");
        } catch (ClassNotFoundException e) {}

        try {
            sdk = new LDAPSDK("java.lang.String");
            fail("ClassCastException should be thrown");
        } catch (ClassCastException e) {}

        try {
            sdk = new LDAPSDK((String) null);
            fail("NullPointerException should be thrown");
        } catch (NullPointerException e) {}

        try {
            sdk = new LDAPSDK("");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {}
    }

    /**
     * Tests the <code>createConnection()</code> method. The test simply verifies that the successfuly created instance
     * of <code>LDAPSDK</code> produces a new instance of <code>LDAPSDKConnection</code> each time the method is called.
     *
     * @throws ClassNotFoundException this is an unexpected exception that is not a result of test failure
     * @throws InstantiationException this is an unexpected exception that is not a result of test failure
     * @throws IllegalAccessException this is an unexpected exception that is not a result of test failure
     */
    public void testMethodCreateConnection() throws IllegalAccessException, InstantiationException,
            ClassNotFoundException {
        LDAPSDK sdk = null;
        LDAPSDKConnection[] connections = new LDAPSDKConnection[10];

        sdk = new LDAPSDK(NetscapeFactory.class.getName());

        // Creates the specified number of connections
        for(int i = 0; i < connections.length; i++) {
            connections[i] = sdk.createConnection();
        }

        // Checks that all the connections are different objects
        for(int i = 0; i < connections.length - 1; i++) {
            for (int j = i + 1; j < connections.length; j++) {
                assertFalse("The method should always produce the new instance of LDAPSDKConnection",
                        connections[i] == connections[j]);
            }
        }
    }

    /**
     * Tests the <code>createSSLConnection()</code> method. The test simply verifies that the successfuly created
     * instance of <code>LDAPSDK</code> produces a new instance of <code>LDAPSDKConnection</code> each time the method
     * is called. The test does not check if the returned instance of <code>LDAPSDKConnection</code> is really a SSL
     * connection since the <code>LDAPSDKConnection</code> interface does not provide a methods to check that.
     *
     * @throws ClassNotFoundException this is an unexpected exception that is not a result of test failure
     * @throws InstantiationException this is an unexpected exception that is not a result of test failure
     * @throws IllegalAccessException this is an unexpected exception that is not a result of test failure
     */
    public void testMethodCreateSSLConnection() throws IllegalAccessException, InstantiationException,
            ClassNotFoundException {
        LDAPSDK sdk = null;
        LDAPSDKConnection[] connections = new LDAPSDKConnection[10];

        sdk = new LDAPSDK(NetscapeFactory.class.getName());

        // Creates the specified number of connections
        for(int i = 0; i < connections.length; i++) {
            connections[i] = sdk.createSSLConnection();
        }

        // Checks that all the connections are different objects
        for(int i = 0; i < connections.length - 1; i++) {
            for (int j = i + 1; j < connections.length; j++) {
                assertFalse("The method should always produce the new instance of LDAPSDKConnection",
                        connections[i] == connections[j]);
            }
        }
    }


    public static Test suite() {
        return new TestSuite(LDAPSDKTest.class);
    }
}
