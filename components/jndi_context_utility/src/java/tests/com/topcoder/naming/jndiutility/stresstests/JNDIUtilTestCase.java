/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.naming.jndiutility.stresstests;

import java.io.File;
import java.util.Random;

import javax.naming.Context;
import javax.naming.Name;

import junit.framework.TestCase;

import com.topcoder.naming.jndiutility.JNDIUtil;


/**
 * Stress tests for the JNDIUtil class.
 * @author netsafe
 * @version 2.0
 */
public class JNDIUtilTestCase extends TestCase {

    /**
     * Time to run in loop each method.
     */
    private static long TIME = 1000;

    /**
     * the default INITIAL_CONTEXT_FACTORY for testing.
     */
    private final static String INITIAL_CONTEXT_FACTORY = "com.sun.jndi.fscontext.RefFSContextFactory";

    /**
     * the default dir for the jndi factory.
     */
    private final static String TEST_DIR = "test_files" + File.separator + "stress" + File.separator + "tests";

    /**
     * the default PROVIDER_URL for testing(Because Some JDNIUtils methods require default jndi factory).
     */
    private final static String PROVIDER_URL = "file://" + new File(TEST_DIR).getAbsolutePath();

    /**
     * the test config file.
     */
    final static String CONFIGURATION_FILE = "test_files/stress/JNDIStressTest.xml";

    /**
     * A Context instance.
     */
    private Context ctx = null;

    /**
     * A Random instance.
     */
    private Random rnd = null;

    /**
     * the JNDIUtil instance for testing.
     */
    private JNDIUtil jndiUtil = null;

    /**
     * Setup tests.
     * @exception propagate
     *                exceptions to JUnit
     */
    public void setUp() throws Exception {

        System.setProperty(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
        System.setProperty(Context.PROVIDER_URL, PROVIDER_URL);

        jndiUtil = new JNDIUtil(new File(CONFIGURATION_FILE));
        ctx = jndiUtil.getContext("default");
        ctx.unbind("queue");
        ctx.unbind("topic");
        ctx.unbind("connection");
        ctx.bind("queue", new QueueImpl("queue"));
        ctx.bind("topic", new TopicImpl("topic"));
        ctx.bind("connection", new DataSourceImpl("connection"));
        rnd = new Random(1234);
    }

    /**
     * Cleanup.
     */
    public void tearDown() throws Exception {
        TestHelper.deltree(TEST_DIR);
    }

    /**
     * Test the createName method.
     * @exception propagate
     *                exceptions to JUnit
     */
    public void testCreateName() throws Exception {
        // warmup
        for (int i = 0; i < 1000; i++) {
            jndiUtil.createName("abc" + rnd.nextLong());
        }
        // run method for TIME milliseconds
        long start = System.currentTimeMillis();
        long count = 0;
        while (System.currentTimeMillis() - start < TIME) {
            jndiUtil.createName("abc" + rnd.nextLong());
            count++;
        }
        // display benchmark
        System.out.println("createName(String): " + count * 1000 / TIME + " operations / sec");
    }

    /**
     * Test the createName method.
     * @exception propagate
     *                exceptions to JUnit
     */
    public void testCreateName2() throws Exception {
        // warmup
        for (int i = 0; i < 1000; i++) {
            jndiUtil.createName("abc" + rnd.nextLong());
        }
        // run method for TIME milliseconds
        long start = System.currentTimeMillis();
        long count = 0;
        while (System.currentTimeMillis() - start < TIME) {
            jndiUtil.createName("abc" + rnd.nextLong());
            count++;
        }
        // display benchmark
        System.out.println("createName( char): " + count * 1000 / TIME + " operations / sec");
    }

    /**
     * Test the createName method.
     * @exception propagate
     *                exceptions to JUnit
     */
    public void testCreateName3() throws Exception {
        // warmup
        for (int i = 0; i < 1000; i++) {
            jndiUtil.createName("abc" + rnd.nextLong());
        }
        // run method for TIME milliseconds
        long start = System.currentTimeMillis();
        long count = 0;
        while (System.currentTimeMillis() - start < TIME) {
            jndiUtil.createName("abc" + rnd.nextLong());
            count++;
        }
        // display benchmark
        System.out.println("createName( String): " + count * 1000 / TIME + " operations / sec");
    }

    /**
     * Test the createSubcontext method.
     * @throws Exception
     *             to JUnit
     */
    public void testCreateSubcontext() throws Exception {

        // run method for TIME milliseconds
        long start = System.currentTimeMillis();
        long count = 0;
        while (System.currentTimeMillis() - start < TIME) {
            jndiUtil.createSubcontext("abc" + rnd.nextLong());
            count++;
        }
        // display benchmark
        System.out.println("createSubcontext(String): " + count * 1000 / TIME + " operations / sec");
    }

    /**
     * Test the createDump method.
     * @exception propagate
     *                exceptions to JUnit
     */
    public void testDump() throws Exception {
        // time method
        long start = System.currentTimeMillis();
        jndiUtil.dump(new MyContextRenderer(), true);
        long stop = System.currentTimeMillis();
        // display benchmark
        System.out.println("dump( ContextRenderer, booolean): " + (stop - start) + "ms");
    }

    /**
     * Test the createDump method.
     * @exception propagate
     *                exceptions to JUnit
     */
    public void testDump2() throws Exception {
        jndiUtil.createSubcontext("abc");
        for (int i = 0; i < 100; i++) {
            jndiUtil.createSubcontext("abc" + rnd.nextLong());
        }
        // time method
        long start = System.currentTimeMillis();
        jndiUtil.dump("abc", new MyContextRenderer(), true);
        long stop = System.currentTimeMillis();
        // display benchmark
        System.out.println("dump( String, ContextRenderer, booolean): " + (stop - start) + "ms");
    }

    /**
     * Test the createDump method.
     * @exception propagate
     *                exceptions to JUnit
     */
    public void testDump3() throws Exception {
        jndiUtil.createSubcontext("abc");
        for (int i = 0; i < 100; i++) {
            jndiUtil.createSubcontext("abc" + rnd.nextLong());
        }
        Name name = jndiUtil.createName("abc");
        // time method
        long start = System.currentTimeMillis();
        jndiUtil.dump(name, new MyContextRenderer(), true);
        long stop = System.currentTimeMillis();
        // display benchmark
        System.out.println("dump( Name, ContextRenderer, booolean): " + (stop - start) + "ms");
    }

    /**
     * Test the getDefaultContext method.
     * @exception propagate
     *                exceptions to JUnit
     */
    public void testGetDefaultContext() throws Exception {
        // run method for TIME milliseconds
        long start = System.currentTimeMillis();
        long count = 0;
        while (System.currentTimeMillis() - start < TIME) {
            jndiUtil.getContext("default");
            count++;
        }
        // display benchmark
        System.out.println("getDefaultContext: " + count * 1000 / TIME + " operations / sec");
    }

    /**
     * Test the getContext method.
     * @exception propagate
     *                exceptions to JUnit
     */
    public void testGetContext() throws Exception {
        // run method for TIME milliseconds
        long start = System.currentTimeMillis();
        jndiUtil.getContext("ldap");
        long stop = System.currentTimeMillis();
        // display benchmark
        System.out.println("getContext(\"ldap\"): " + (stop - start) + "ms");
    }

    /**
     * Test the getGetObject method.
     * @exception propagate
     *                exceptions to JUnit
     */
    public void testGetObject() throws Exception {
        // run method for TIME milliseconds
        long start = System.currentTimeMillis();
        long count = 0;
        String s = "queue";
        Name name = jndiUtil.createName(s);
        while (System.currentTimeMillis() - start < TIME) {
            jndiUtil.getObject(name);
            count++;
        }
        // display benchmark
        System.out.println("getGetObject( Name): " + count * 1000 / TIME + " operations / sec");
    }

    /**
     * Test the getGetObject method.
     * @exception propagate
     *                exceptions to JUnit
     */
    public void testGetObject2() throws Exception {
        // run method for TIME milliseconds
        long start = System.currentTimeMillis();
        long count = 0;
        String s = "queue";
        while (System.currentTimeMillis() - start < TIME) {
            jndiUtil.getObject(s);
            count++;
        }
        // display benchmark
        System.out.println("getGetObject( String): " + count * 1000 / TIME + " operations / sec");
    }

    /**
     * Test the getGetConnection method.
     * @exception propagate
     *                exceptions to JUnit
     */
    public void testGetConnection() throws Exception {
        // run method for TIME milliseconds
        long start = System.currentTimeMillis();
        long count = 0;
        String s = "connection";
        while (System.currentTimeMillis() - start < TIME) {
            jndiUtil.getConnection(s);
            count++;
        }
        // display benchmark
        System.out.println("getGetConnection( String): " + count * 1000 / TIME + " operations / sec");
    }

    /**
     * Test the getGetConnection method.
     * @exception propagate
     *                exceptions to JUnit
     */
    public void testGetQueue() throws Exception {
        // run method for TIME milliseconds
        long start = System.currentTimeMillis();
        long count = 0;
        String s = "queue";
        while (System.currentTimeMillis() - start < TIME) {
            jndiUtil.getQueue(s);
            count++;
        }
        // display benchmark
        System.out.println("getGetQueue( String): " + count * 1000 / TIME + " operations / sec");
    }

    /**
     * Test the getGetConnection method.
     * @exception propagate
     *                exceptions to JUnit
     */
    public void testGetTopic() throws Exception {
        // run method for TIME milliseconds
        long start = System.currentTimeMillis();
        long count = 0;
        String s = "topic";
        while (System.currentTimeMillis() - start < TIME) {
            jndiUtil.getTopic(s);
            count++;
        }
        // display benchmark
        System.out.println("getGetTopic(String): " + count * 1000 / TIME + " operations / sec");
    }

    /**
     * Test the getGetConnection method.
     * @exception propagate
     *                exceptions to JUnit
     */
    public void testGetConnection2() throws Exception {
        // run method for TIME milliseconds
        long start = System.currentTimeMillis();
        long count = 0;
        String s = "connection";
        Name name = jndiUtil.createName(s);
        while (System.currentTimeMillis() - start < TIME) {
            jndiUtil.getConnection(name);
            count++;
        }
        // display benchmark
        System.out.println("getGetConnection( String): " + count * 1000 / TIME + " operations / sec");
    }

    /**
     * Test the getGetConnection method.
     * @exception propagate
     *                exceptions to JUnit
     */
    public void testGetQueue2() throws Exception {
        // run method for TIME milliseconds
        long start = System.currentTimeMillis();
        long count = 0;
        String s = "queue";
        Name name = jndiUtil.createName(s);
        while (System.currentTimeMillis() - start < TIME) {
            jndiUtil.getQueue(name);
            count++;
        }
        // display benchmark
        System.out.println("getGetQueue( String): " + count * 1000 / TIME + " operations / sec");
    }

    /**
     * Test the getGetConnection method.
     * @exception propagate
     *                exceptions to JUnit
     */
    public void testGetTopic2() throws Exception {
        // run method for TIME milliseconds
        long start = System.currentTimeMillis();
        long count = 0;
        String s = "topic";
        Name name = jndiUtil.createName(s);
        while (System.currentTimeMillis() - start < TIME) {
            jndiUtil.getTopic(name);
            count++;
        }
        // display benchmark
        System.out.println("getGetTopic(String): " + count * 1000 / TIME + " operations / sec");
    }

}
