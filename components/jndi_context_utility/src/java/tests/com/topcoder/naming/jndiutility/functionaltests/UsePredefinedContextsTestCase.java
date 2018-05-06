/*
 * Copyright (C) 2003 TopCoder Inc., All Rights Reserved.
 *
 * @(#) UsePredefinedContextsTestCase.java
 *
 * 1.0 05/14/2003
 */
package com.topcoder.naming.jndiutility.functionaltests;

import com.topcoder.naming.jndiutility.JNDIUtils;

import java.util.Properties;

import javax.naming.Context;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * This test case tests the methods provided by JNDIUtils class allowing to create Context using configuration
 * parameters stored in configuration file, save the JNDI Context connection info in configuration file for further
 * retrieving, get the default context.
 *
 * @author isv
 * @version 1.0 05/14/2003
 */
public class UsePredefinedContextsTestCase extends TestCase {
    /**
     * Creates a new UsePredefinedContextsTestCase object.
     *
     * @param name name of test case
     */
    public UsePredefinedContextsTestCase(String name) {
        super(name);
    }

    /**
     * Return suite of this test case.
     *
     * @return suite of this test case.
     */
    public static Test suite() {
        return new TestSuite(UsePredefinedContextsTestCase.class);
    }

    /**
     * Tests JNDIUtils.getDefaultContext().
     *
     * @throws Exception if error occurs
     */
    public void test_get_default_context() throws Exception {
        Context ctx = null;

        ctx = JNDIUtils.getDefaultContext();

        assertNotNull("Default context should be available in current " + "configuration", ctx);
    }

    /**
     * Tests JNDIUtils.getContext(String).
     *
     * @throws Exception if error occurs
     */
    public void get_context_by_name() throws Exception {
        Context ctx1 = null;
        Context ctx2 = null;

        ctx1 = JNDIUtils.getDefaultContext();
        ctx2 = JNDIUtils.getContext("default");

        assertTrue("The default context should be created with two ways",
            ctx1.getNameInNamespace().equals(ctx2.getNameInNamespace()));

        ctx1 = JNDIUtils.getContext("ldap");
        ctx2 = JNDIUtils.getContext("ldap");

        assertNotNull("The context defined in configuration file should be " + "created", ctx1);
        assertNotNull("The context defined in configuration file should be " + "created", ctx2);
        assertTrue("Any time the same context name is specified the same " + "context should be returned",
            ctx1.getNameInNamespace().equals(ctx2.getNameInNamespace()));

        // test incorrect arguments handling
        try {
            JNDIUtils.getContext((String) null);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // should land here
        }

        try {
            JNDIUtils.getContext("");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // should land here
        }

        try {
            JNDIUtils.getContext("      ");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Tests JNDIUtils.saveContextConfig(String,Properties). This test requires accessibility of following URL
     * : ldap://openldap.org/dc=OpenLDAP,dc=org
     *
     * @throws Exception if error occurs
     */
    public void test_save_context() throws Exception {
        Context ctx1 = null;
        Context ctx2 = null;
        Properties props = null;

        props = new Properties();

        props.put("factory", "com.sun.jndi.ldap.LdapCtxFactory");
        props.put("url", "ldap://openldap.org/dc=OpenLDAP,dc=org");

        JNDIUtils.saveContextConfig("new_ldap_context", props);

        ctx1 = JNDIUtils.getContext("new_ldap_context");

        assertNotNull("Properly defined and saved Context should be created", ctx1);

        ctx2 = JNDIUtils.getContext("new_ldap_context");

        assertNotNull("Properly defined and saved Context should be created", ctx2);

        assertTrue("Any time the properly defined and saved Context is created" + " it should be the same Context",
            ctx1.getNameInNamespace().equals(ctx2.getNameInNamespace()));

        // test incorrect arguments handling
        try {
            JNDIUtils.saveContextConfig((String) null, props);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // should land here
        }

        try {
            JNDIUtils.saveContextConfig("", props);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // should land here
        }

        try {
            JNDIUtils.saveContextConfig("      ", props);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // should land here
        }

        try {
            JNDIUtils.saveContextConfig((String) null, (Properties) null);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // should land here
        }

        try {
            JNDIUtils.saveContextConfig("", (Properties) null);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // should land here
        }

        try {
            JNDIUtils.saveContextConfig("    ", (Properties) null);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }
}
