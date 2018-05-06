/*
 * Copyright (C) 2003 TopCoder Inc., All Rights Reserved.
 *
 * @(#) GetResourcesTestCase.java
 *
 * 1.0 05/14/2003
 */
package com.topcoder.naming.jndiutility.functionaltests;

import com.topcoder.naming.jndiutility.JNDIUtils;

import javax.naming.Context;
import javax.naming.Name;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * This test case tests the methods provided by JNDIUtils class allowing to get several kinds of resource from
 * specified Context. Those resources are JMS Topics and Queue, JDBC DataSource connections and objects bound to
 * context.
 *
 * @author isv
 * @version 1.0 05/14/2003
 */
public class GetResourcesTestCase extends TestCase {
    /**
     * Creates a new GetResourcesTestCase object.
     *
     * @param name name of test case
     */
    public GetResourcesTestCase(String name) {
        super(name);
    }

    /**
     * Return suite of this test case.
     *
     * @return suite of this test case.
     */
    public static Test suite() {
        return new TestSuite(GetResourcesTestCase.class);
    }

    /**
     * Tests JNDIUtils.getObject() methods.
     *
     * @throws Exception if error occurs
     */
    public void test_get_object() throws Exception {
        // test incorrect arguments handling
        try {
            JNDIUtils.getObject((String) null);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // should land here
        }

        try {
            JNDIUtils.getObject((String) null, Object.class);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // should land here
        }

        try {
            JNDIUtils.getObject("remote", (Class) null);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Tests JNDIUtils.getQueue() methods.
     *
     * @throws Exception if error occurs
     */
    public void test_get_queue() throws Exception {
        Context ctx = null;
        Name name = null;

        // test incorrect arguments handling
        ctx = JNDIUtils.getDefaultContext();
        name = JNDIUtils.createName(ctx, "com");

        try {
            JNDIUtils.getQueue((Context) null, "java:env/jms/queue");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // should land here
        }

        try {
            JNDIUtils.getQueue((Context) null, (String) null);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // should land here
        }

        try {
            JNDIUtils.getQueue(ctx, (String) null);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // should land here
        }

        try {
            JNDIUtils.getQueue((Context) null, name);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // should land here
        }

        try {
            JNDIUtils.getQueue((Context) null, (Name) null);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // should land here
        }

        try {
            JNDIUtils.getQueue(ctx, (Name) null);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Tests JNDIUtils.getTopic() methods.
     *
     * @throws Exception if error occurs
     */
    public void test_get_topic() throws Exception {
        Context ctx = null;
        Name name = null;

        // test incorrect arguments handling
        ctx = JNDIUtils.getDefaultContext();
        name = JNDIUtils.createName(ctx, "com");

        try {
            JNDIUtils.getTopic((Context) null, "java:env/jms/topic");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // should land here
        }

        try {
            JNDIUtils.getTopic((Context) null, (String) null);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // should land here
        }

        try {
            JNDIUtils.getTopic(ctx, (String) null);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // should land here
        }

        try {
            JNDIUtils.getTopic((Context) null, name);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // should land here
        }

        try {
            JNDIUtils.getTopic((Context) null, (Name) null);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // should land here
        }

        try {
            JNDIUtils.getTopic(ctx, (Name) null);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Tests JNDIUtils.getConnection() methods.
     *
     * @throws Exception if error occurs
     */
    public void test_get_connection() throws Exception {
        Context ctx = null;
        Name name = null;

        // test incorrect arguments handling
        ctx = JNDIUtils.getDefaultContext();
        name = JNDIUtils.createName(ctx, "com");

        try {
            JNDIUtils.getConnection((Context) null, "java:env/jdbc/testSource");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // should land here
        }

        try {
            JNDIUtils.getConnection((Context) null, (String) null);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // should land here
        }

        try {
            JNDIUtils.getConnection(ctx, (String) null);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // should land here
        }

        try {
            JNDIUtils.getConnection((Context) null, name);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // should land here
        }

        try {
            JNDIUtils.getConnection((Context) null, (Name) null);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // should land here
        }

        try {
            JNDIUtils.getConnection(ctx, (Name) null);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }
}
