/*
 * Copyright (C) 2003 TopCoder Inc., All Rights Reserved.
 *
 * @(#) ManipulateWithNamesTestCase.java
 *
 * 1.0 05/14/2003
 */
package com.topcoder.naming.jndiutility.functionaltests;

import com.topcoder.naming.jndiutility.JNDIUtils;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingException;


/**
 * This test case tests the methods provided by JNDIUtils class allowing to manipulate with String and Name JNDI
 * names.
 *
 * @author isv
 * @version 1.0 05/14/2003
 */
public class ManipulateWithNamesTestCase extends TestCase {
    /**
     * Creates a new ManipulateWithNamesTestCase object.
     *
     * @param name name of test case
     */
    public ManipulateWithNamesTestCase(String name) {
        super(name);
    }

    /**
     * Return suite of this test case.
     *
     * @return suite of this test case.
     */
    public static Test suite() {
        return new TestSuite(ManipulateWithNamesTestCase.class);
    }

    /**
     * Tests JNDIUtils.createName(Context,String). This test requires the accessibility of following URL :
     * <code> ldap://openldap.org:389</code>
     *
     * @throws Exception if error occurs
     */
    public void test_create_name_with_context() throws Exception {
        Context ctx = null;
        Name name = null;

        ctx = JNDIUtils.getContext("ldap");

        name = JNDIUtils.createName(ctx, "cn=TCSDESIGNER,dn=OpenLDAP");

        assertNotNull("JNDIUtils.createName(Context,String) should never return" + " null", name);
        assertTrue("Properly specified LDAP name should have correct number" + " of components", name.size() == 2);

        name = JNDIUtils.createName(ctx, "cn=TCSDESIGNER");

        assertNotNull("JNDIUtils.createName(Context,String) should never return" + " null", name);
        assertTrue("Properly specified LDAP name should have correct number" + " of components", name.size() == 1);

        // test the incorrect arguments handling
        try {
            JNDIUtils.createName((Context) null, "com.topcoder.naming");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // should land here
        }

        try {
            JNDIUtils.createName((Context) null, (String) null);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // should land here
        }

        ctx = JNDIUtils.getDefaultContext();

        try {
            JNDIUtils.createName(ctx, (String) null);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // should land here
        }

        ctx = JNDIUtils.getContext("ldap");

        try {
            JNDIUtils.createName(ctx, "gk?fkm");
            fail("NamingException should be thrown since name is not valid");
        } catch (NamingException e) {
            // should land here
        }
    }

    /**
     * Tests JNDIUtils.createName(String,char).
     *
     * @throws Exception if error occurs
     */
    public void test_create_name_from_string() throws Exception {
        Name name = null;

        name = JNDIUtils.createName("com.topcoder.naming.jndiutility", '.');

        assertNotNull("JNDIUtils.createName(String,char) should never" + " return null", name);
        assertEquals("The Name should have a number of components equal to"
            + " number of components in original String", name.size(), 4);
        assertTrue("The String representation of Name should be equal to" + "slash-separated original String",
            name.toString().equals("com/topcoder/naming/jndiutility"));

        name = JNDIUtils.createName("com.topcoder.naming.jndiutility", '?');

        assertNotNull("JNDIUtils.createName(String,char) should never" + " return null", name);
        assertEquals("The Name should have a number of components equal to"
            + " 1 since the original String is not separated with" + " question character", name.size(), 1);
        assertTrue("The String representation of Name should be equal to"
            + "original String since the original String is not" + "separated with  question character",
            name.toString().equals("com.topcoder.naming.jndiutility"));

        // test the incorrect arguments handling
        try {
            JNDIUtils.createName((String) null, '.');
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Tests JNDIUtils.createString(Name,char).
     *
     * @throws Exception if error occurs
     */
    public void test_create_string_from_name() throws Exception {
        Name name = null;

        name = JNDIUtils.createName("com.topcoder.naming.jndiutility", '.');

        String string = JNDIUtils.createString(name, '.');

        assertTrue("The String should contain components of source Name" + " separated with specified character",
            string.equals("com.topcoder.naming.jndiutility"));

        string = JNDIUtils.createString(name, '/');

        assertTrue("The String should contain components of source Name" + " separated with specified character",
            string.equals("com/topcoder/naming/jndiutility"));

        string = JNDIUtils.createString(name, '?');

        assertTrue("The String should contain components of source Name" + " separated with specified character",
            string.equals("com?topcoder?naming?jndiutility"));

        // test the incorrect arguments handling
        try {
            JNDIUtils.createString((Name) null, '.');
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }
}
