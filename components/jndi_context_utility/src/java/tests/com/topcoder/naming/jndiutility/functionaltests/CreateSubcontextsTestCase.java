/*
 * Copyright (C) 2003 TopCoder Inc., All Rights Reserved.
 *
 * @(#) CreateSubcontextsTestCase.java
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
import javax.naming.NameNotFoundException;


/**
 * This test case tests the methods provided by JNDIUtils class allowing to create subcontexts within specified
 * Context.<p>This test case use a default context that is specified in
 * <code>com/topcoder/naming/jndiutility/JNDIUtils.properties</code> file. This default context is maintained by File
 * System JNDI Service from Sun.</p>
 *  <p>In order to run this test case an empty temporary directory should be created in the file system and
 * context.default.url property should be modified to point to this directory. The process should have a permission to
 * write to this directory.</p>
 *
 * @author isv
 * @version 1.0 05/14/2003
 */
public class CreateSubcontextsTestCase extends TestCase {
    /**
     * File separator.
     */
    private static final String FS = System.getProperty("file.separator");

    /**
     * Creates a new CreateSubcontextsTestCase object.
     *
     * @param name name of test case
     */
    public CreateSubcontextsTestCase(String name) {
        super(name);
    }

    /**
     * Return suite of this test case.
     *
     * @return suite of this test case.
     */
    public static Test suite() {
        return new TestSuite(CreateSubcontextsTestCase.class);
    }

    /**
     * Tests JNDIUtils.createSubcontext(Context,String).
     *
     * @throws Exception if error occurs
     */
    public void test_create_subcontext_with_string() throws Exception {
        Context ctx = null;

        ctx = JNDIUtils.getDefaultContext();

        Context tmp = null;

        // ensure that directory used in testing does not exist
        try {
            tmp = (Context) ctx.lookup("subdirectory");
            fail("The temporary directory should be empty before run the test");
        } catch (NameNotFoundException e) {
            // should land here
        }

        JNDIUtils.createSubcontext(ctx, "subdirectory");

        tmp = (Context) ctx.lookup("subdirectory");
        assertNotNull("The subcontext should be created", tmp);

        JNDIUtils.createSubcontext(ctx, "subdirectory" + FS + "1" + FS + 2 + FS + "3" + FS + "4");

        tmp = (Context) ctx.lookup("subdirectory" + FS + "1");
        assertNotNull("The intermediate non-existent subcontext should be " + "created", tmp);

        tmp = (Context) ctx.lookup("subdirectory" + FS + "1" + FS + "2");
        assertNotNull("The intermediate non-existent subcontext should be " + "created", tmp);

        tmp = (Context) ctx.lookup("subdirectory" + FS + "1" + FS + "2" + FS + "3");
        assertNotNull("The intermediate non-existent subcontext should be " + "created", tmp);

        tmp = (Context) ctx.lookup("subdirectory" + FS + "1" + FS + "2" + FS + "3" + FS + "4");
        assertNotNull("The target non-existent subcontext should be " + "created", tmp);

        ctx.destroySubcontext("subdirectory" + FS + "1" + FS + 2 + FS + "3" + FS + "4");
        ctx.destroySubcontext("subdirectory" + FS + "1" + FS + 2 + FS + "3");
        ctx.destroySubcontext("subdirectory" + FS + "1" + FS + 2);
        ctx.destroySubcontext("subdirectory" + FS + "1");
        ctx.destroySubcontext("subdirectory");

        // test the incorrect arguments handling
        try {
            JNDIUtils.createSubcontext((Context) null, "tmp");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // should land here
        }

        try {
            JNDIUtils.createSubcontext((Context) null, (String) null);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // should land here
        }

        try {
            JNDIUtils.createSubcontext(ctx, (String) null);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Tests JNDIUtils.createSubcontext(Context,Name).
     *
     * @throws Exception If error occurs
     */
    public void test_create_subcontext_with_name() throws Exception {
        Name name = null;
        Context ctx = null;

        ctx = JNDIUtils.getDefaultContext();

        Context tmp = null;

        // ensure that directory used in testing does not exist
        try {
            tmp = (Context) ctx.lookup("subdirectory");
            fail("The temporary directory should be empty before run the test");
        } catch (NameNotFoundException e) {
            // should land here
        }

        name = JNDIUtils.createName(ctx, "subdirectory");
        JNDIUtils.createSubcontext(ctx, name);

        tmp = (Context) ctx.lookup("subdirectory");
        assertNotNull("The subcontext should be created", tmp);

        name = JNDIUtils.createName(ctx, "subdirectory" + FS + "1" + FS + 2 + FS + "3" + FS + "4");
        JNDIUtils.createSubcontext(ctx, name);

        tmp = (Context) ctx.lookup("subdirectory" + FS + "1");
        assertNotNull("The intermediate non-existent subcontext should be " + "created", tmp);

        tmp = (Context) ctx.lookup("subdirectory" + FS + "1" + FS + "2");
        assertNotNull("The intermediate non-existent subcontext should be " + "created", tmp);

        tmp = (Context) ctx.lookup("subdirectory" + FS + "1" + FS + "2" + FS + "3");
        assertNotNull("The intermediate non-existent subcontext should be " + "created", tmp);

        tmp = (Context) ctx.lookup("subdirectory" + FS + "1" + FS + "2" + FS + "3" + FS + "4");
        assertNotNull("The target non-existent subcontext should be " + "created", tmp);

        ctx.destroySubcontext("subdirectory" + FS + "1" + FS + 2 + FS + "3" + FS + "4");
        ctx.destroySubcontext("subdirectory" + FS + "1" + FS + 2 + FS + "3");
        ctx.destroySubcontext("subdirectory" + FS + "1" + FS + 2);
        ctx.destroySubcontext("subdirectory" + FS + "1");
        ctx.destroySubcontext("subdirectory");

        // test the incorrect arguments handling
        try {
            JNDIUtils.createSubcontext((Context) null, name);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // should land here
        }

        try {
            JNDIUtils.createSubcontext((Context) null, (Name) null);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // should land here
        }

        try {
            JNDIUtils.createSubcontext(ctx, (Name) null);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }
}
