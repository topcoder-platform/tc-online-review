/*
 * Copyright (C) 2003 TopCoder Inc., All Rights Reserved.
 *
 * @(#) GetObjectConnectionQueueTopicTestCase.java
 *
 * 1.0 08/08/2003
 */
package com.topcoder.naming.jndiutility;

import junit.framework.TestCase;

import java.sql.Connection;

import javax.jms.Queue;
import javax.jms.Topic;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingException;


/**
 * This test, will test the getObject(), getQueue(), getTopic() and getConnection() methods.
 *
 * @author preben
 * @version 1.0 05/14/2003
 */
public class GetObjectConnectionQueueTopicTestCase extends TestCase {
    /**
     * Constant string used in test.
     */
    static final String QUEUE = "queue";

    /**
     * Constant string used in test.
     */
    static final String TOPIC = "topic";

    /**
     * Constant string used in test.
     */
    static final String CONNECTION = "connection";

    /**
     * Constant string used in test.
     */
    static final String NE = "did not throw NamingException";

    /**
     * Constant string used in test.
     */
    static final String CCE = "did not throw ClassCastException";

    /**
     * Set up the test environment.
     *
     * @throws Exception if any exception occurs
     */
    public void setUp() throws Exception {
        Context defaultContext = JNDIUtils.getDefaultContext();
        defaultContext.bind(QUEUE, new QueueImpl("queue"));
        defaultContext.bind(TOPIC, new TopicImpl("topic"));
        defaultContext.bind(CONNECTION, new DataSourceImpl(CONNECTION));
    }

    /**
     * Tear down the test environment.
     *
     * @throws Exception if any exception occurs
     */
    public void tearDown() throws Exception {
        Context defaultContext = JNDIUtils.getDefaultContext();
        defaultContext.unbind(QUEUE);
        defaultContext.unbind(TOPIC);
        defaultContext.unbind(CONNECTION);
    }

    /**
     * Test the getObject(Context,String) method.
     *
     * @throws Exception if any exception occurs
     */
    public void testGetObjectContextStringNormalUse() throws Exception {
        Context context = JNDIUtils.getDefaultContext();
        Object o = JNDIUtils.getObject(context, QUEUE);
        assertNotNull(o);
        assertEquals(o.toString(), QUEUE);

        //Lookup a name that do not exist
        try {
            JNDIUtils.getObject(context, "blabblajlkej");
            fail(NE);
        } catch (NamingException e) {
            // should land here
        }
    }

    /**
     * Test the getObject(Context,Name) method.
     *
     * @throws Exception if any exception occurs
     */
    public void testGetObjectContextNameNormalUse() throws Exception {
        Context context = JNDIUtils.getDefaultContext();
        Name name = JNDIUtils.createName(context, QUEUE);
        Object o = JNDIUtils.getObject(context, name);
        assertNotNull(o);
        assertEquals(o.toString(), QUEUE);

        //Lookup a name that do not exist
        try {
            JNDIUtils.getObject(context, "blabblajlkej");
            fail(NE);
        } catch (NamingException e) {
            // should land here
        }
    }

    /**
     * Test the getObject(Context,Name,Class) method.
     *
     * @throws Exception if any exception occurs
     */
    public void testGetObjectContextNameClassNormalUse()
        throws Exception {
        Context context = JNDIUtils.getDefaultContext();
        Class clazz = Queue.class;
        Name name = JNDIUtils.createName(context, QUEUE);
        Object o = JNDIUtils.getObject(context, name, clazz);
        assertNotNull(o);
        assertEquals(o.toString(), QUEUE);

        //Look up object of wrong class
        try {
            JNDIUtils.getObject(context, name, String.class);
            fail(CCE);
        } catch (ClassCastException e) {
            // should land here
        }

        //Lookup a name that do not exist
        try {
            JNDIUtils.getObject(context, "blabblajlkej", clazz);
            fail(NE);
        } catch (NamingException e) {
            // should land here
        }
    }

    /**
     * Test the getObject(Context,String,Class) method.
     *
     * @throws Exception if any exception occurs
     */
    public void testGetObjectContextStringClassNormalUse()
        throws Exception {
        Context context = JNDIUtils.getDefaultContext();
        Class clazz = Queue.class;
        Object o = JNDIUtils.getObject(context, QUEUE, clazz);
        assertNotNull(o);
        assertEquals(o.toString(), QUEUE);

        //Look up object of wrong class
        try {
            JNDIUtils.getObject(context, QUEUE, String.class);
            fail(CCE);
        } catch (ClassCastException e) {
            // should land here
        }

        //Lookup a name that do not exist
        try {
            JNDIUtils.getObject(context, "blabblajlkej", clazz);
            fail(NE);
        } catch (NamingException e) {
            // should land here
        }
    }

    /**
     * Test of the getQueue(Context,String) method.
     *
     * @throws Exception if any exception occurs
     */
    public void testGetQueueContextString() throws Exception {
        Context context = JNDIUtils.getDefaultContext();

        try {
            JNDIUtils.getQueue(context, "blakljl");
            fail(NE);
        } catch (NamingException e) {
            // should land here
        }

        assertNotNull(JNDIUtils.getQueue(context, QUEUE));
        assertNull(JNDIUtils.getQueue(context, TOPIC));
        assertTrue(JNDIUtils.getQueue(context, QUEUE) instanceof Queue);
    }

    /**
     * Test of the getQueue(Context,Name) method.
     *
     * @throws Exception if any exception occurs
     */
    public void testGetQueueContextName() throws Exception {
        Context context = JNDIUtils.getDefaultContext();
        Name name = JNDIUtils.createName(context, QUEUE);
        Name nameOfTopic = JNDIUtils.createName(context, TOPIC);
        Name illegal = JNDIUtils.createName(context, "illegal");

        try {
            JNDIUtils.getQueue(context, illegal);
            fail(NE);
        } catch (NamingException e) {
            // should land here
        }

        assertNotNull(JNDIUtils.getQueue(context, name));
        assertNull(JNDIUtils.getQueue(context, nameOfTopic));
        assertTrue(JNDIUtils.getQueue(context, name) instanceof Queue);
    }

    /**
     * Test of the getTopic(Context,String) method.
     *
     * @throws Exception if any exception occurs
     */
    public void testGetTopicContextString() throws Exception {
        Context context = JNDIUtils.getDefaultContext();

        try {
            JNDIUtils.getTopic(context, "blakljl");
            fail(NE);
        } catch (NamingException e) {
            // should land here
        }

        assertNotNull(JNDIUtils.getTopic(context, TOPIC));
        assertNull(JNDIUtils.getTopic(context, QUEUE));
        assertTrue(JNDIUtils.getTopic(context, TOPIC) instanceof Topic);
    }

    /**
     * Test of the getTopic(Context,Name) method.
     *
     * @throws Exception if any exception occurs
     */
    public void testGetTopicContextName() throws Exception {
        Context context = JNDIUtils.getDefaultContext();
        Name name = JNDIUtils.createName(context, TOPIC);
        Name nameOfQueue = JNDIUtils.createName(context, QUEUE);
        Name illegal = JNDIUtils.createName(context, "illegal");

        try {
            JNDIUtils.getTopic(context, illegal);
            fail(NE);
        } catch (NamingException e) {
            // should land here
        }

        assertNotNull(JNDIUtils.getTopic(context, name));
        assertNull(JNDIUtils.getTopic(context, nameOfQueue));
        assertTrue(JNDIUtils.getTopic(context, name) instanceof Topic);
    }

    /**
     * Test of the getConnection(Context,String) method.
     *
     * @throws Exception if any exception occurs
     */
    public void testGetConnectionContextString() throws Exception {
        Context context = JNDIUtils.getDefaultContext();

        try {
            JNDIUtils.getConnection(context, "blakljl");
            fail(NE);
        } catch (NamingException e) {
            // should land here
        }

        assertNotNull(JNDIUtils.getConnection(context, CONNECTION));
    }

    /**
     * Test of the getConnection(Context,Name) method.
     *
     * @throws Exception if any exception occurs
     */
    public void testGetConnectionContextName() throws Exception {
        Context context = JNDIUtils.getDefaultContext();
        Name name = JNDIUtils.createName(context, CONNECTION);
        Name nameOfQueue = JNDIUtils.createName(context, QUEUE);
        Name illegal = JNDIUtils.createName(context, "illegal");

        try {
            JNDIUtils.getConnection(context, illegal);
            fail(NE);
        } catch (NamingException e) {
            // should land here
        }

        assertNotNull(JNDIUtils.getConnection(context, name));
        assertNull(JNDIUtils.getConnection(context, nameOfQueue));
        assertTrue((JNDIUtils.getConnection(context, name)) instanceof Connection);
    }
}
