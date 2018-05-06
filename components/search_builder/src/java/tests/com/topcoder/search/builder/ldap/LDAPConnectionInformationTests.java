/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.ldap;

import com.topcoder.util.net.ldap.sdkinterface.LDAPSDK;
import com.topcoder.util.net.ldap.sdkinterface.LDAPSDKConnection;

import junit.framework.TestCase;

import java.util.Properties;


/**
 * <p>
 * Unit test cases for LDAPConnectionInformation.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.3
 */
public class LDAPConnectionInformationTests extends TestCase {
    /**
     * The host for test  LDAPConnectionInformation.
     */
    private static final String HOST = "localhost";

    /**
     * The port for test LDAPConnectionInformation.
     */
    private static final int PORT = 8888;

    /**
     * The scope for test LDAPConnectionInformation.
     */
    private static final int SCOPE = LDAPSDKConnection.SCOPE_BASE;

    /**
     * The isSecure value for test LDAPConnectionInformation.
     */
    private static final boolean ISS = false;
    /**
     * The dnroot value for test LDAPConnectionInformation.
     */
    private static final String DNROOT = "dnroot";
    /**
     * The password value for test LDAPConnectionInformation.
     */
    private static final String PASSWORD = "password";
    /**
     * The LDAPSDKFactory class name for test LDAPConnectionInformation.
     */
    private static final String CLASS_NAME = "com.topcoder.util.net.ldap.sdkinterface.netscape.NetscapeFactory";

    /**
     * the instance of LDAPConnectionInformation for test.
     */
    private static LDAPConnectionInformation ldapInfo = null;

    /**
     * LDAPSDK instance for test LDAPConnectionInformation.
     */
    private LDAPSDK factory = null;

    /**
     * setUp.
     *
     * @throws Exception to Junit
     */
    protected void setUp() throws Exception {
        factory = new LDAPSDK(CLASS_NAME);

        ldapInfo = new LDAPConnectionInformation(factory, HOST, PORT, ISS, SCOPE, DNROOT, PASSWORD);
    }

    /**
     * tearDown.
     */
    protected void tearDown() {
        //empty
    }

    /**
     * test the construct of LDAPConnectionInformation.
     *
     */
    public void testconstruct1() {
        //test construct sccess
        assertNotNull("can not construct the LDAPConnectionInformation",
            ldapInfo);
    }

    /**
     * test the construct of LDAPConnectionInformation with Exception.
     *
     */
    public void testconstruct2() {
        try {
            ldapInfo = new LDAPConnectionInformation(factory, HOST, PORT, ISS,
                    SCOPE, DNROOT, PASSWORD);
        } catch (Exception e) {
            fail("no Exception should be throw.");
        }
    }

    /**
     * test the construct of LDAPConnectionInformation with Exception.
     *
     */
    public void testconstruct3() {
        Properties properties = new Properties();
        properties.put("factory", factory);
        properties.setProperty("host", HOST);
        properties.setProperty("port", "8888");
        properties.setProperty("isSSL", "false");
        properties.setProperty("scope", "0");
        properties.setProperty("dnroot", "dnroot");
        properties.setProperty("password", "password");

        try {
            ldapInfo = new LDAPConnectionInformation(properties);
        } catch (Exception e) {
            fail("No Exception should be throw");
        }

        assertNotNull("The instance should not be null", ldapInfo);
    }

    /**
     * test the mothed getFactory.
     *
     */
    public void testgetFactory() {
        //test value of factory
        assertEquals("The same value of factory", factory, ldapInfo.getFactory());
    }

    /**
     * test the mothed getHost.
     *
     */
    public void testgetHost() {
        //test value of host
        assertEquals("The same value of HOST", HOST, ldapInfo.getHost());
    }

    /**
     * test the mothed isSecure.
     *
     */
    public void testisSecure() {
        //test value of isSecure
        assertEquals("The same value of ISS", ISS, ldapInfo.isSecure());
    }

    /**
     * test the mothed getPort.
     *
     */
    public void testgetPort() {
        //test value if port
        assertEquals("The same value of PORT", PORT, ldapInfo.getPort());
    }

    /**
     * test the mothed getScope.
     *
     */
    public void getScope() {
        //test value of scope
        assertEquals("The same value of SCOPE", SCOPE, ldapInfo.getScope());
    }
    /**
     * Test the method getDnroot.
     *
     */
    public void testGetDnRoot() {
        assertEquals("The dnroot should be retrieved correctly.", DNROOT, ldapInfo.getDnroot());
    }
    /**
     * Test the method getpassWord.
     *
     */
    public void testpassWord() {
        assertEquals("The passWord should be retrieved correctly.", PASSWORD, ldapInfo.getPassword());
    }
}
