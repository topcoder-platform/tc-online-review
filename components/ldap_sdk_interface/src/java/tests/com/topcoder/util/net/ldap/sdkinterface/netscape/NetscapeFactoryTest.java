/*
 * TCS LDAP SDK Interface 1.0
 *
 * NetscapeFactoryTest.java
 *
 * Copyright (c), 2004. TopCoder, Inc. All rights reserved.
 *
 */
package com.topcoder.util.net.ldap.sdkinterface.netscape;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Test;

import com.topcoder.util.net.ldap.sdkinterface.LDAPSDKConnection;

/**
 * A test case testing the behavior of <code>NetscapeFactory</code> class. No special settings are required to run this
 * test.
 *
 * @author  isv
 * @version 1.0 05/18/2004
 */
public class NetscapeFactoryTest extends TestCase {

    public NetscapeFactoryTest(String testName) {
        super(testName);
    }

    /**
     * Tests the default public constructor. Since this is a default constructor the test simply verifies that nothing
     * prevents the instantiation of <code>NetscpeFactory</code>.
     */
    public void testConstructor() {
        NetscapeFactory factory = null;
        try {
            factory = new NetscapeFactory();
        } catch (Exception e) {
            fail("There shouldn't be any reasons preventing the successful instantiation of NetscapeFactory");
        }
    }

    /**
     * Tests the <code>createSSLConnection()</code> method. The test simply verifies that the successfuly created
     * instance of <code>NetscapeFactory</code> produces a new instance of <code>LDAPSDKConnection</code> each time the
     * method is called. Also verifies that the factory produces the instances of <code>NetscapeConnection</code> class.
     * The test does not check if the returned instance of <code>LDAPSDKConnection</code> is really a SSL connection
     * since the <code>LDAPSDKConnection</code> interface does not provide a methods to check that.
     */
    public void testMethodCreateSSLConnection() {
        NetscapeFactory factory = null;
        LDAPSDKConnection[] connections = new LDAPSDKConnection[10];

        factory = new NetscapeFactory();

        // Creates the specified number of connections
        for (int i = 0; i < connections.length; i++) {
            connections[i] = factory.createSSLConnection();
        }

        // Checks that all the connections are different objects and are of type NetscapeConnection
        for (int i = 0; i < connections.length - 1; i++) {
            for (int j = i + 1; j < connections.length; j++) {
                assertFalse("The method should always produce the new instance of LDAPSDKConnection",
                        connections[i] == connections[j]);
            }
        }

        // Checks that the factory rpoduces the instances of NetscapeConnection
        for (int i = 0; i < connections.length; i++) {
            assertEquals("The NetscapeFactory should produce the instances of NetscapeConnection",
                    NetscapeConnection.class.getName(), connections[i].getClass().getName());
        }
    }

    /**
     * Tests the <code>createConnection()</code> method. The test simply verifies that the successfuly created instance
     * of <code>NetscapeFactory</code> produces a new instance of <code>LDAPSDKConnection</code> each time the method is
     * called. Also verifies that the factory produces the instances of <code>NetscapeConnection</code> class.
     */
    public void testMethodCreateConnection() {
        NetscapeFactory factory = null;
        LDAPSDKConnection[] connections = new LDAPSDKConnection[10];

        factory = new NetscapeFactory();

        // Creates the specified number of connections
        for (int i = 0; i < connections.length; i++) {
            connections[i] = factory.createConnection();
        }

        // Checks that all the connections are different objects and are of type NetscapeConnection
        for (int i = 0; i < connections.length - 1; i++) {
            for (int j = i + 1; j < connections.length; j++) {
                assertFalse("The method should always produce the new instance of LDAPSDKConnection",
                        connections[i] == connections[j]);
            }
        }

        // Checks that the factory rpoduces the instances of NetscapeConnection
        for (int i = 0; i < connections.length; i++) {
            assertEquals("The NetscapeFactory should produce the instances of NetscapeConnection",
                    NetscapeConnection.class.getName(), connections[i].getClass().getName());
        }
    }


    public static Test suite() {
        return new TestSuite(NetscapeFactoryTest.class);
    }
}
