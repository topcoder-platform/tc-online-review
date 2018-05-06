/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.log.stresstests;

import com.topcoder.util.log.Log;
import com.topcoder.util.log.Level;
import com.topcoder.util.log.LogException;
import com.topcoder.util.log.basic.BasicLogFactory;
import com.topcoder.util.log.jdk14.Jdk14LogFactory;
import com.topcoder.util.log.log4j.Log4jLogFactory;

import junit.framework.TestCase;

/**
 * <p>
 * Stress tests for Logging Wrapper.
 * </p>
 *
 * @author smallka
 * @version 1.0
 */
public class LoggingWrapperStressTest extends TestCase {

    /** The number of instances for testing. */
    private static final int NUMBER = 100;

    /** The maximum time (milliseconds) expected. */
    private static final int MAX_TIME = 5000;

    /** The number of threads to run. */
    private static final int NUM_THREAD = 20;

    /** The strings to be used for testing. */
    private static final String[] STRINGS =
        new String[] {"foo", "bar", "blah", "mlah", "kur", "iazh", "ti", "pf"};

    /** The log levers to be used for testing. */
    private static final Level[] LEVELS =
        new Level[] {Level.OFF, Level.FINEST, Level.TRACE, Level.DEBUG, Level.CONFIG, Level.INFO, Level.WARN,
            Level.ERROR, Level.FATAL, Level.ALL};

    /**
     * Test creating a basic logger.
     */
    public void testBasicCreation() {
        BasicLogFactory factory = new BasicLogFactory();
        Log[] bl = new Log[NUMBER];
        long t = System.currentTimeMillis();
        for (int i = 0; i < NUMBER; i++) {
            bl[i] = factory.createLog(STRINGS[i % STRINGS.length]);
        }
        t = System.currentTimeMillis()-t;
        assertTrue(t < MAX_TIME);
    }

    /**
     * Test creating a JDK 1.4 logger.
     */
    public void testJDK14Creation() {
        Jdk14LogFactory factory = new Jdk14LogFactory();
        Log[] jl = new Log[NUMBER];
        long t = System.currentTimeMillis();
        for (int i = 0; i < NUMBER; i++) {
            jl[i] = factory.createLog(STRINGS[i % STRINGS.length]);
        }
        t = System.currentTimeMillis()-t;
        assertTrue(t < MAX_TIME);
    }

    /**
     * Test creating a Log4j logger.
     */
    public void testLog4jCreation() {
        Log4jLogFactory factory = new Log4jLogFactory();
        Log[] ll = new Log[NUMBER];
        long t = System.currentTimeMillis();
        for (int i = 0; i < NUMBER; i++) {
            ll[i] = factory.createLog(STRINGS[i % STRINGS.length]);
        }
        t = System.currentTimeMillis()-t;
        assertTrue(t < MAX_TIME);
    }

    /**
     * Test the LogException class.
     */
    public void testLogException() {
        // test creation
        LogException[] le = new LogException[NUMBER];
        long t = System.currentTimeMillis();
        for (int i = 0; i < NUMBER; i++) {
            le[i] = new LogException(new Exception(STRINGS[i % STRINGS.length]));
        }
        t = System.currentTimeMillis()-t;
        assertTrue(t < MAX_TIME);
    }

    /**
     * Test logging 1000 messages using the basic logger.
     *
     * @throws Exception to JUnit
     */
    public void testBasicLogger() throws Exception {
        Log bl = new BasicLogFactory().createLog("basic");
        for (int i = 0; i < LEVELS.length; i++) {
            long t = System.currentTimeMillis();
            for (int j = 0; j < 1000; j++) {
                bl.log(LEVELS[i], STRINGS[(i+j) % STRINGS.length] + "#" + j);
            }
            t = System.currentTimeMillis()-t;
            assertTrue(t < MAX_TIME);
        }
    }

    /**
     * Test logging 10000 messages using the JDK 1.4 logger.
     *
     * @throws Exception to JUnit
     */
    public void testJDK14Logger() throws Exception {
        Log jl = new Jdk14LogFactory().createLog("jdk14");
        for (int i = 0; i < LEVELS.length; i++) {
            long t = System.currentTimeMillis();
            for (int j = 0; j < 10000; j++) {
                jl.log(LEVELS[i], STRINGS[(i+j) % STRINGS.length] + "#" + j);
            }
            t = System.currentTimeMillis()-t;
            assertTrue(t < MAX_TIME*5);
        }
    }

    /**
     * Test logging 10000 messages using the Log4j logger.
     *
     * @throws Exception to JUnit
     */
    public void testLog4jLogger() throws Exception {
        Log ll = new Log4jLogFactory().createLog("log4j");
        for (int i = 0; i < LEVELS.length; i++) {
            long t = System.currentTimeMillis();
            for (int j = 0; j < 10000; j++) {
                ll.log(LEVELS[i], STRINGS[(i+j) % STRINGS.length] + "#" + j);
            }
            t = System.currentTimeMillis()-t;
            assertTrue(t < MAX_TIME*5);
        }
    }

    /**
     * Test logging 10000 messages using the basic logger and multiple threads.
     *
     * @throws Exception to JUnit
     */
    public void testBasicLoggerMultithreaded() throws Exception {
        final Log bl = new BasicLogFactory().createLog("basic");
        class BasicLogThread extends Thread {
            public BasicLogThread(String name) {
                super(name);
            }
            public void run() {
                for (int i = 0; i < LEVELS.length; i++) {
                    for (int j = 0; j < 10000/NUM_THREAD; j++) {
                        bl.log(LEVELS[i], getName() + ":" + STRINGS[(i+j) % STRINGS.length] + "#" + j);
                    }
                }
            }
        }
        BasicLogThread[] threads = new BasicLogThread[NUM_THREAD];
        for (int i = 0; i < NUM_THREAD; i++) {
            threads[i] = new BasicLogThread(STRINGS[i % STRINGS.length]);
            threads[i].start();
        }
        long t = System.currentTimeMillis();
        try {
            for (int i = 0; i < NUM_THREAD; i++) {
                threads[i].join();
            }
        } catch (InterruptedException e) {
        }
        t = System.currentTimeMillis()-t;
        assertTrue(t < MAX_TIME);
    }

    /**
     * Test logging 10000 messages using the JDK 1.4 logger and multiple threads.
     *
     * @throws Exception to JUnit
     */
    public void testJDK14LoggerMultithreaded() throws Exception {
        final Log jl = new Jdk14LogFactory().createLog("jdk14");
        class Jdk14LogThread extends Thread {
            public Jdk14LogThread(String name) {
                super(name);
            }
            public void run() {
                for (int i = 0; i < LEVELS.length; i++) {
                    for (int j = 0; j < 10000/NUM_THREAD; j++) {
                        jl.log(LEVELS[i], getName() + ":" + STRINGS[(i+j) % STRINGS.length] + "#" + j);
                    }
                }
            }
        }
        Jdk14LogThread[] threads = new Jdk14LogThread[NUM_THREAD];
        for (int i = 0; i < NUM_THREAD; i++) {
            threads[i] = new Jdk14LogThread(STRINGS[i % STRINGS.length]);
            threads[i].start();
        }
        long t = System.currentTimeMillis();
        try {
            for (int i = 0; i < NUM_THREAD; i++) {
                threads[i].join();
            }
        } catch (InterruptedException e) {
        }
        t = System.currentTimeMillis()-t;
        assertTrue(t < MAX_TIME*5);
    }

    /**
     * Test logging 10000 messages using the Log4j logger and multiple threads.
     *
     * @throws Exception to JUnit
     */
    public void testLog4jLoggerMultithreaded() throws Exception {
        final Log ll = new Log4jLogFactory().createLog("log4j");
        class Log4jLogThread extends Thread {
            public Log4jLogThread(String name) {
                super(name);
            }
            public void run() {
                for (int i = 0; i < LEVELS.length; i++) {
                    for (int j = 0; j < 10000/NUM_THREAD; j++) {
                        ll.log(LEVELS[i], getName() + ":" + STRINGS[(i+j) % STRINGS.length] + "#" + j);
                    }
                }
            }
        }
        Log4jLogThread[] threads = new Log4jLogThread[NUM_THREAD];
        for (int i = 0; i < NUM_THREAD; i++) {
            threads[i] = new Log4jLogThread(STRINGS[i % STRINGS.length]);
            threads[i].start();
        }
        long t = System.currentTimeMillis();
        try {
            for (int i = 0; i < NUM_THREAD; i++) {
                threads[i].join();
            }
        } catch (InterruptedException e) {
        }
        t = System.currentTimeMillis()-t;
        System.out.println("Log4j MultiThreaded is: " + t);
        assertTrue(t < MAX_TIME*5);
    }
}
