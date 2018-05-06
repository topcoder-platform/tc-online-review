/*
 * Copyright (c) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.objectfactory.stresstests;

import com.topcoder.util.errorhandling.BaseException;
import com.topcoder.util.objectfactory.ObjectFactory;
import com.topcoder.util.objectfactory.SpecificationFactory;
import com.topcoder.util.objectfactory.impl.ConfigManagerSpecificationFactory;
import com.topcoder.util.objectfactory.stresstests.testclasses.Bar;
import com.topcoder.util.objectfactory.stresstests.testclasses.Frac;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.net.URL;

import java.util.Random;


/**
 * <p>
 * Stress test cases for <code>ObjectFactory</code> class.
 * </p>
 *
 * @author Wendell
 * @version 2.0
 */
public class ObjectFactoryStressTest extends TestCase {
    /** Number of threads used for testing. */
    private static final int THREAD_NUM = 1000;

    /** The SpecificationFactory used for testing. */
    private SpecificationFactory sFactory;

    /** The ObjectFactory instance to test against. */
    private ObjectFactory[] oFactory;

    /**
     * Sets up the test environment.
     *
     * @throws Exception to junit.
     */
    protected void setUp() throws Exception {
        StressTestHelper.clearConfig();
        StressTestHelper.loadConfig(StressTestHelper.CONFIG);

        sFactory = new ConfigManagerSpecificationFactory(StressTestHelper.NAMESPACE);

        oFactory = new ObjectFactory[3];
        oFactory[0] = new ObjectFactory(sFactory, ObjectFactory.BOTH);
        oFactory[1] = new ObjectFactory(sFactory, ObjectFactory.REFLECTION_ONLY);
        oFactory[2] = new ObjectFactory(sFactory, ObjectFactory.SPECIFICATION_ONLY);
    }

    /**
     * Clears configuration from ConfigManager.
     */
    protected void tearDown() {
        StressTestHelper.clearConfig();
    }

    /**
     * Tests <code>createObject(String)</code> method.
     *
     * @throws Exception to junit.
     */
    public void testCreateObject1() throws Exception {
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < StressTestHelper.RUN_TIMES; i++) {
            Object object = oFactory[0].createObject(BaseException.class.getName());
            assertNotNull("failed to create a BaseException instance.", object);
            assertTrue("failed to create a BaseException instance.", object instanceof BaseException);

            object = oFactory[0].createObject("bar");
            assertNotNull("failed to create a Bar instance.", object);
            assertTrue("failed to create a Bar instance.", object instanceof Bar);
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Creating " + (StressTestHelper.RUN_TIMES * 2)
            + " objects using createObject(String) method takes " + (endTime - startTime) + "ms");
    }

    /**
     * Tests <code>createObject(Class)</code> method.
     *
     * @throws Exception to junit.
     */
    public void testCreateObject2() throws Exception {
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < StressTestHelper.RUN_TIMES; i++) {
            Object object = oFactory[0].createObject(BaseException.class);
            assertNotNull("failed to create a BaseException instance.", object);
            assertTrue("failed to create a BaseException instance.", object instanceof BaseException);
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Creating " + StressTestHelper.RUN_TIMES
            + " objects using createObject(Class) method takes " + (endTime - startTime) + "ms");
    }

    /**
     * Tests <code>createObject(String, String)</code> method.
     *
     * @throws Exception to junit.
     */
    public void testCreateObject3() throws Exception {
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < StressTestHelper.RUN_TIMES; i++) {
            Object object = oFactory[2].createObject("frac", "default");
            assertNotNull("failed to create a Frac instance.", object);
            assertTrue("failed to create a Frac instance.", object instanceof Frac);
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Creating " + StressTestHelper.RUN_TIMES
            + " objects using createObject(String, String) method takes " + (endTime - startTime) + "ms");
    }

    /**
     * Tests <code>createObject(Class, String)</code> method.
     *
     * @throws Exception to junit.
     */
    public void testCreateObject4() throws Exception {
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < StressTestHelper.RUN_TIMES; i++) {
            Object object = oFactory[0].createObject(Frac.class, "default");
            assertNotNull("failed to create a Frac instance.", object);
            assertTrue("failed to create a Frac instance.", object instanceof Frac);

            object = oFactory[1].createObject(Frac.class, "default");
            assertNotNull("failed to create a Frac instance.", object);
            assertTrue("failed to create a Frac instance.", object instanceof Frac);
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Creating " + (StressTestHelper.RUN_TIMES * 2)
            + " objects using createObject(Class, String) method takes " + (endTime - startTime) + "ms");
    }

    /**
     * Tests <code>createObject(String, String, URL, Object[], Class[], String)</code> method.
     * @throws Exception to junit.
     */
    public void testCreateObject5() throws Exception {
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < StressTestHelper.RUN_TIMES; i++) {
            Object object =
                oFactory[0].createObject(BaseException.class.getName(), "baseException",
                    new URL(StressTestHelper.JAR_PATH), new Object[] {"message"}, new Class[] {String.class},
                    ObjectFactory.BOTH);
            assertNotNull("failed to create a BaseException instance.", object);
            assertTrue("failed to create a BaseException instance.", object instanceof BaseException);
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Creating " + StressTestHelper.RUN_TIMES
            + " objects using createObject(String, String, URL, Object[], Class[], String) " + "method takes "
            + (endTime - startTime) + "ms");
    }

    /**
     * Tests <code>createObject(Class, String, URL, Object[], Class[], String)</code> method.
     * @throws Exception to junit.
     */
    public void testCreateObject6() throws Exception {
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < StressTestHelper.RUN_TIMES; i++) {
            Object object =
                oFactory[0].createObject(BaseException.class, "baseException",
                    new URL(StressTestHelper.JAR_PATH), new Object[] {"message"}, new Class[] {String.class},
                    ObjectFactory.BOTH);
            assertNotNull("failed to create a BaseException instance.", object);
            assertTrue("failed to create a BaseException instance.", object instanceof BaseException);
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Creating " + StressTestHelper.RUN_TIMES
            + " objects using createObject(Class, String, URL, Object[], Class[], String) " + "method takes "
            + (endTime - startTime) + "ms");
    }

    /**
     * Tests <code>createObject(Class, String, ClassLoader, Object[], Class[], String)</code>
     * method.
     * @throws Exception to junit.
     */
    public void testCreateObject7() throws Exception {
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < StressTestHelper.RUN_TIMES; i++) {
            Object object =
                oFactory[0].createObject(BaseException.class, "baseException", (ClassLoader) null,
                    new Object[] {"message"}, new Class[] {String.class}, ObjectFactory.BOTH);
            assertNotNull("failed to create a BaseException instance.", object);
            assertTrue("failed to create a BaseException instance.", object instanceof BaseException);
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Creating " + StressTestHelper.RUN_TIMES
            + " objects using createObject(Class, String, ClassLoader, Object[], Class[], String) "
            + "method takes " + (endTime - startTime) + "ms");
    }

    /**
     * Tests <code>createObject(String, String, ClassLoader, Object[], Class[], String)</code>
     * method.
     * @throws Exception to junit.
     */
    public void testCreateObject8() throws Exception {
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < StressTestHelper.RUN_TIMES; i++) {
            Object object =
                oFactory[0].createObject(BaseException.class.getName(), "baseException", (ClassLoader) null,
                    new Object[] {"message"}, new Class[] {String.class}, ObjectFactory.BOTH);
            assertNotNull("failed to create a BaseException instance.", object);
            assertTrue("failed to create a BaseException instance.", object instanceof BaseException);
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Creating " + StressTestHelper.RUN_TIMES
            + " objects using createObject(String, String, ClassLoader, Object[], Class[], String) "
            + "method takes " + (endTime - startTime) + "ms");
    }

    /**
     * Tests the <code>createObject</code> methods with multiple threads.
     */
    public void testCurrency() {
        CreateObjectThread[] threads = new CreateObjectThread[THREAD_NUM];

        long startTime = System.currentTimeMillis();

        // starts each thread.
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new CreateObjectThread();
            threads[i].start();
        }

        // waits the threads to finish
        for (int i = 0; i < threads.length; i++) {
            try {
                threads[i].join();
            } catch (Exception e) {
                fail("Exception caught: " + e.getMessage());
            }
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Test createObject methods with " + THREAD_NUM + " threads takes "
            + (endTime - startTime) + "ms");
    }

    /**
     * Returns the test suite of this test case.
     *
     * @return the test suite of this test case.
     */
    public static Test suite() {
        return new TestSuite(ObjectFactoryStressTest.class);
    }

    /**
     * Represents a thread that creats objects.
     *
     * @author Wendell
     * @version 2.0
     */
    private class CreateObjectThread extends Thread {
        /** The iteration number to run. */
        private final int NUM = 200;

        /** The number of operations. */
        private final int OPERATIONS = 8;

        /**
         * Runs the thread.
         */
        public void run() {
            // randomizer
            Random random = new Random();

            try {
                for (int i = 0; i < this.NUM; i++) {
                    switch (Math.abs(random.nextInt()) % this.OPERATIONS) {
                    case 0:
                        oFactory[0].createObject(BaseException.class.getName());

                        break;

                    case 1:
                        oFactory[0].createObject(BaseException.class);

                        break;

                    case 2:
                        oFactory[2].createObject("frac", "default");

                        break;

                    case 3:
                        oFactory[0].createObject(Frac.class, "default");
                        oFactory[1].createObject(Frac.class, "default");

                        break;

                    case 4:
                        oFactory[0].createObject(BaseException.class.getName(), "baseException",
                            new URL(StressTestHelper.JAR_PATH), new Object[] {"message"},
                            new Class[] {String.class}, ObjectFactory.BOTH);

                        break;

                    case 5:
                        oFactory[0].createObject(BaseException.class, "baseException",
                            new URL(StressTestHelper.JAR_PATH), new Object[] {"message"},
                            new Class[] {String.class}, ObjectFactory.BOTH);

                        break;

                    case 6:
                        oFactory[0].createObject(BaseException.class, "baseException", (ClassLoader) null,
                            new Object[] {"message"}, new Class[] {String.class}, ObjectFactory.BOTH);

                        break;

                    case 7:
                        oFactory[0].createObject(BaseException.class.getName(), "baseException",
                            (ClassLoader) null, new Object[] {"message"}, new Class[] {String.class},
                            ObjectFactory.BOTH);

                        break;

                    default:
                        break;
                    }
                }
            } catch (Exception e) {
                // ignore
            }
        }
    }
}
