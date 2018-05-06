/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.naming.jndiutility.stresstests;

import java.io.File;
import java.util.Random;

import javax.naming.Context;
import javax.naming.Name;

import junit.framework.TestCase;

import com.topcoder.naming.jndiutility.JNDIUtils;

/**
 * Stress tests for the JNDIUtils class. to run this test ,you should define the <code>TEST_CONTEXT</code>
 * param and modify the JNDIUtils.properties.
 * @author adic,netsafe
 * @version 2.0
 */
public class JNDIUtilsTestCase extends TestCase {

    /**
     * Time to run in loop each method. 
     */
    private static long TIME = 1000;

    /**
     * the default Context name for testing. <b>Note:</b> all the file in the context folder should be
     * deleted.
     */
    private final static String TEST_CONTEXT = "stress";

    /**
     * the default INITIAL_CONTEXT_FACTORY for testing. 
     */
    private final static String INITIAL_CONTEXT_FACTORY="com.sun.jndi.fscontext.RefFSContextFactory";

    /**
     * the default dir for the jndi factory.
     */
    private final static String TEST_DIR = "test_files" + File.separator + "stress" ;
    /**
     * the default PROVIDER_URL for testing(Because Some JDNIUtils methods require default jndi factory).
     */
    private final static String PROVIDER_URL = "file://" + new File(TEST_DIR).getAbsolutePath();

    /**
     * A Context instance.
     */
    private Context ctx = null;
    
    /**
     * A Random instance.
     */
    private Random rnd = null;

    /**
     * Setup tests.
     *
     * @exception propagate exceptions to JUnit
     */
    public void setUp() throws Exception {
        
        System.setProperty(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
        System.setProperty(Context.PROVIDER_URL, PROVIDER_URL);
        ctx = JNDIUtils.getContext(TEST_CONTEXT);
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
     * @exception propagate exceptions to JUnit
     */
    public void tearDown()throws Exception {
        TestHelper.deltree(ctx.getNameInNamespace());
    }

    /**
     * Test the createName method.
     *
     * @exception propagate exceptions to JUnit
     */
    public void testCreateName() throws Exception {
        // warmup
        for (int i = 0; i < 1000; i++) {
            JNDIUtils.createName("abc" + rnd.nextLong());
        }
        // run method for TIME milliseconds
        long start = System.currentTimeMillis();
        long count = 0;
        while (System.currentTimeMillis() - start < TIME) {            
            JNDIUtils.createName("abc" + rnd.nextLong());
            count++;
        }
        // display benchmark
        System.out.println("createName(String): " + count * 1000 / TIME + " operations / sec");
    }
    
    /**
     * Test the createName method.
     *
     * @exception propagate exceptions to JUnit
     */
    public void testCreateName2() throws Exception {
        // warmup
        for (int i = 0; i < 1000; i++) {
            JNDIUtils.createName("abc" + rnd.nextLong(), '/');
        }
        // run method for TIME milliseconds
        long start = System.currentTimeMillis();
        long count = 0;
        while (System.currentTimeMillis() - start < TIME) {            
            JNDIUtils.createName("abc" + rnd.nextLong(), '/');
            count++;
        }
        // display benchmark
        System.out.println("createName(String, char): " + count * 1000 / TIME + " operations / sec");
    }
    
    /**
     * Test the createName method.
     *
     * @exception propagate exceptions to JUnit
     */
    public void testCreateName3() throws Exception {
        // warmup
        for (int i = 0; i < 1000; i++) {
            JNDIUtils.createName(ctx, "abc" + rnd.nextLong());
        }
        // run method for TIME milliseconds
        long start = System.currentTimeMillis();
        long count = 0;
        while (System.currentTimeMillis() - start < TIME) {            
            JNDIUtils.createName(ctx, "abc" + rnd.nextLong());
            count++;
        }
        // display benchmark
        System.out.println("createName(Context, String): " + count * 1000 / TIME + " operations / sec");
    }
    
    /**
     * Test the createString method.
     *
     * @exception propagate exceptions to JUnit
     */
    public void testCreateString() throws Exception {
        // warmup
        for (int i = 0; i < 1000; i++) {
            Name name = JNDIUtils.createName("abc" + rnd.nextLong(), '.');
            JNDIUtils.createString(name, '/');
        }
        // run method for TIME milliseconds
        long start = System.currentTimeMillis();
        long count = 0;
        while (System.currentTimeMillis() - start < TIME) {            
            Name name = JNDIUtils.createName("abc" + rnd.nextLong(), '.');
            JNDIUtils.createString(name, '/');
            count++;
        }
        // display benchmark
        System.out.println("createString(Name, char): " + count * 1000 / TIME + " operations / sec");
    }
    
    /**
     * Test the createSubcontext method.
     *
     * @exception propagate exceptions to JUnit
     */
    /*public void testCreateSubcontext() throws Exception {
        // warmup
        for (int i = 0; i < 1000; i++) {
            JNDIUtils.createSubcontext("abc" + rnd.nextLong());
        }
        // run method for TIME milliseconds
        long start = System.currentTimeMillis();
        long count = 0;
        while (System.currentTimeMillis() - start < TIME) {            
            JNDIUtils.createSubcontext("abc" + rnd.nextLong());
            count++;
        }
        // display benchmark
        System.out.println("createSubcontext(String): " + count * 1000 / TIME + " operations / sec");
    }*/
    
    /**
     * Test the createSubcontext method.
     *
     * @exception propagate exceptions to JUnit
     */
    /*public void testCreateSubcontext2() throws Exception {
        // warmup
        for (int i = 0; i < 1000; i++) {
            Name name = JNDIUtils.createName("abc" + rnd.nextLong(), '.');
            JNDIUtils.createSubcontext(name);
        }
        // run method for TIME milliseconds
        long start = System.currentTimeMillis();
        long count = 0;
        while (System.currentTimeMillis() - start < TIME) {            
            Name name = JNDIUtils.createName("abc" + rnd.nextLong(), '.');
            JNDIUtils.createSubcontext(name);
            count++;
        }
        // display benchmark
        System.out.println("createSubcontext(Name): " + count * 1000 / TIME + " operations / sec");
    }*/

    /**
     * Test the createSubcontext method.
     *
     * @exception propagate exceptions to JUnit
     */
    public void testCreateSubcontext3() throws Exception {
        // warmup
        for (int i = 0; i < 10; i++) {
            JNDIUtils.createSubcontext(ctx, "abc" + rnd.nextLong());
        }
        // run method for TIME milliseconds
        long start = System.currentTimeMillis();
        long count = 0;
        while (System.currentTimeMillis() - start < TIME) {            
            JNDIUtils.createSubcontext(ctx, "abc" + rnd.nextLong());
            count++;
        }
        // display benchmark
        System.out.println("createSubcontext(Context, String): " + count * 1000 / TIME + " operations / sec");
    }

    /**
     * Test the createSubcontext method.
     *
     * @exception propagate exceptions to JUnit
     */
    public void testCreateSubcontext4() throws Exception {
        // warmup
        for (int i = 0; i < 10; i++) {
            Name name = JNDIUtils.createName("abc" + rnd.nextLong(), '.');
            JNDIUtils.createSubcontext(ctx, name);
        }
        // run method for TIME milliseconds
        long start = System.currentTimeMillis();
        long count = 0;
        while (System.currentTimeMillis() - start < TIME) {            
            Name name = JNDIUtils.createName("abc" + rnd.nextLong(), '.');
            JNDIUtils.createSubcontext(ctx, name);
            count++;
        }
        // display benchmark
        System.out.println("createSubcontext(Context, Name): " + count * 1000 / TIME + " operations / sec");
    }
    
    /**
     * Test the createDump method.
     *
     * @exception propagate exceptions to JUnit
     */
    public void testDump() throws Exception {
        // time method
        long start = System.currentTimeMillis();
        JNDIUtils.dump(ctx, new MyContextRenderer(), true);
        long stop = System.currentTimeMillis();
        // display benchmark
        System.out.println("dump(Context, ContextRenderer, booolean): " + (stop - start) + "ms");
    }
    
    /**
     * Test the createDump method.
     *
     * @exception propagate exceptions to JUnit
     */
    public void testDump2() throws Exception {
        Context sub = JNDIUtils.createSubcontext(ctx, "abc");
        
        for (int i = 0; i < 100; i++) {
            JNDIUtils.createSubcontext(sub, "abc" + rnd.nextLong());
        }
        // time method
        long start = System.currentTimeMillis();
        JNDIUtils.dump(ctx, "abc", new MyContextRenderer(), true);
        long stop = System.currentTimeMillis();
        // display benchmark
        System.out.println("dump(Context, String, ContextRenderer, booolean): " + (stop - start) + "ms");
    }
        
    /**
     * Test the createDump method.
     *
     * @exception propagate exceptions to JUnit
     */
    public void testDump3() throws Exception {
        Context sub = JNDIUtils.createSubcontext(ctx, "abc");
        for (int i = 0; i < 100; i++) {
            JNDIUtils.createSubcontext(sub, "abc" + rnd.nextLong());
        }
        Name name = JNDIUtils.createName("abc");
        // time method
        long start = System.currentTimeMillis();
        JNDIUtils.dump(ctx, name, new MyContextRenderer(), true);
        long stop = System.currentTimeMillis();
        // display benchmark
        System.out.println("dump(Context, Name, ContextRenderer, booolean): " + (stop - start) + "ms");
    }
    
    /**
     * Test the getDefaultContext method.
     *
     * @exception propagate exceptions to JUnit
     */
    public void testGetDefaultContext() throws Exception {
        // run method for TIME milliseconds
        long start = System.currentTimeMillis();
        long count = 0;
        while (System.currentTimeMillis() - start < TIME) {            
            JNDIUtils.getDefaultContext();
            count++;
        }
        // display benchmark
        System.out.println("getDefaultContext: " + count * 1000 / TIME + " operations / sec");
    }
    
    /**
     * Test the getContext method.
     *
     * @exception propagate exceptions to JUnit
     */
    public void testGetContext() throws Exception {
        // run method for TIME milliseconds
        long start = System.currentTimeMillis();
        JNDIUtils.getContext("ldap");
        long stop = System.currentTimeMillis();
        // display benchmark
        System.out.println("getContext(\"ldap\"): " + (stop - start) + "ms");
    }
     
    /**
     * Test the getGetObject method.
     *
     * @exception propagate exceptions to JUnit
     */
    public void testGetObject() throws Exception {
        // run method for TIME milliseconds
        long start = System.currentTimeMillis();
        long count = 0;
        String s = "queue";
        Name name = JNDIUtils.createName(ctx, s);
        while (System.currentTimeMillis() - start < TIME) {                        
            JNDIUtils.getObject(ctx, name);
            count++;
        }
        // display benchmark
        System.out.println("getGetObject(Context, Name): " + count * 1000 / TIME + " operations / sec");
    }

    /**
     * Test the getGetObject method.
     *
     * @exception propagate exceptions to JUnit
     */
    public void testGetObject2() throws Exception {
        // run method for TIME milliseconds
        long start = System.currentTimeMillis();
        long count = 0;
        String s = "queue";
        while (System.currentTimeMillis() - start < TIME) {                        
            JNDIUtils.getObject(ctx, s);
            count++;
        }
        // display benchmark
        System.out.println("getGetObject(Context, String): " + count * 1000 / TIME + " operations / sec");
    }

    /**
     * Test the getGetConnection method.
     *
     * @exception propagate exceptions to JUnit
     */
    public void testGetConnection() throws Exception {
        // run method for TIME milliseconds
        long start = System.currentTimeMillis();
        long count = 0;
        String s = "connection";
        while (System.currentTimeMillis() - start < TIME) {                        
            JNDIUtils.getConnection(ctx, s);
            count++;
        }
        // display benchmark
        System.out.println("getGetConnection(Context, String): " + count * 1000 / TIME + " operations / sec");
    }

    /**
     * Test the getGetConnection method.
     *
     * @exception propagate exceptions to JUnit
     */
    public void testGetQueue() throws Exception {
        // run method for TIME milliseconds
        long start = System.currentTimeMillis();
        long count = 0;
        String s = "queue";
        while (System.currentTimeMillis() - start < TIME) {                        
            JNDIUtils.getQueue(ctx, s);
            count++;
        }
        // display benchmark
        System.out.println("getGetQueue(Context, String): " + count * 1000 / TIME + " operations / sec");
    }

    /**
     * Test the getGetConnection method.
     *
     * @exception propagate exceptions to JUnit
     */
    public void testGetTopic() throws Exception {
        // run method for TIME milliseconds
        long start = System.currentTimeMillis();
        long count = 0;
        String s = "topic";
        while (System.currentTimeMillis() - start < TIME) {                        
            JNDIUtils.getTopic(ctx, s);
            count++;
        }
        // display benchmark
        System.out.println("getGetTopic(Context, String): " + count * 1000 / TIME + " operations / sec");
    }

    /**
     * Test the getGetConnection method.
     *
     * @exception propagate exceptions to JUnit
     */
    public void testGetConnection2() throws Exception {
        // run method for TIME milliseconds
        long start = System.currentTimeMillis();
        long count = 0;
        String s = "connection";
        Name name = JNDIUtils.createName(ctx, s);
        while (System.currentTimeMillis() - start < TIME) {                        
            JNDIUtils.getConnection(ctx, name);           
            count++;
        }
        // display benchmark
        System.out.println("getGetConnection(Context, String): " + count * 1000 / TIME + " operations / sec");
    }

    /**
     * Test the getGetConnection method.
     *
     * @exception propagate exceptions to JUnit
     */
    public void testGetQueue2() throws Exception {
        // run method for TIME milliseconds
        long start = System.currentTimeMillis();
        long count = 0;
        String s = "queue";
        Name name = JNDIUtils.createName(ctx, s);
        while (System.currentTimeMillis() - start < TIME) {                        
            JNDIUtils.getQueue(ctx, name);
            count++;
        }
        // display benchmark
        System.out.println("getGetQueue(Context, String): " + count * 1000 / TIME + " operations / sec");
    }

    /**
     * Test the getGetConnection method.
     *
     * @exception propagate exceptions to JUnit
     */
    public void testGetTopic2() throws Exception {
        // run method for TIME milliseconds
        long start = System.currentTimeMillis();
        long count = 0;
        String s = "topic";
        Name name = JNDIUtils.createName(ctx, s);
        while (System.currentTimeMillis() - start < TIME) {                        
            JNDIUtils.getTopic(ctx, name);
            count++;
        }
        // display benchmark
        System.out.println("getGetTopic(Context, String): " + count * 1000 / TIME + " operations / sec");
    }
  
}

