/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.failuretests.ldap;

import com.topcoder.search.builder.ldap.LDAPConnectionInformation;
import com.topcoder.util.net.ldap.sdkinterface.LDAPSDK;

import junit.framework.TestCase;

/**
 * Test the class <code>LDAPConnectionInformation</code>.
 *
 * @author assistant
 * @version 1.3
 *
 */
public class LDAPConnectionInformationTest13 extends TestCase {

    /**
     * The LDAPSDKFactory class name for test LDAPConnectionInformation.
     */
    private static final String CLASS_NAME = "com.topcoder.util.net.ldap.sdkinterface.netscape.NetscapeFactory";

    /**
     * LDAPSDK instance for test LDAPConnectionInformation.
     */
    private LDAPSDK factory = null;

    /**
     * Set up the environment.
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        super.setUp();

        factory = new LDAPSDK(CLASS_NAME);
    }

    /**
     * Test method for LDAPConnectionInformation(LDAPSDK, java.lang.String,
     * int, boolean, int, java.lang.String, java.lang.String).
     */
    public void testLDAPConnectionInformationNullSDK() {
        try {
            new LDAPConnectionInformation(null, "a" , 1, false, 1, "a", "a");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for LDAPConnectionInformation(LDAPSDK, java.lang.String,
     * int, boolean, int, java.lang.String, java.lang.String).
     */
    public void testLDAPConnectionInformationNullHost() {
        try {
            new LDAPConnectionInformation(factory, null , 1, false, 1, "a", "a");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for LDAPConnectionInformation(LDAPSDK, java.lang.String,
     * int, boolean, int, java.lang.String, java.lang.String).
     */
    public void testLDAPConnectionInformationEmptyHost() {
        try {
            new LDAPConnectionInformation(factory, " " , 1, false, 1, "a", "a");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for LDAPConnectionInformation(LDAPSDK, java.lang.String,
     * int, boolean, int, java.lang.String, java.lang.String).
     */
    public void testLDAPConnectionInformationZeroPort() {
        try {
            new LDAPConnectionInformation(factory, "a" , 0, false, 1, "a", "a");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for LDAPConnectionInformation(LDAPSDK, java.lang.String,
     * int, boolean, int, java.lang.String, java.lang.String).
     */
    public void testLDAPConnectionInformationNegativePort() {
        try {
            new LDAPConnectionInformation(factory, "a" , -1, false, 1, "a", "a");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for LDAPConnectionInformation(LDAPSDK, java.lang.String,
     * int, boolean, int, java.lang.String, java.lang.String).
     */
    public void testLDAPConnectionInformationNullRoot() {
        try {
            new LDAPConnectionInformation(factory, "a" , 1, false, 1, null, "a");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for LDAPConnectionInformation(LDAPSDK, java.lang.String,
     * int, boolean, int, java.lang.String, java.lang.String).
     */
    public void testLDAPConnectionInformationNullPass() {
        try {
            new LDAPConnectionInformation(factory, "a" , 1, false, 1, "a", null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

}
