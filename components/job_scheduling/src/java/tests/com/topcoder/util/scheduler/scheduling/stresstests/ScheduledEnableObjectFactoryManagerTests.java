/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.util.scheduler.scheduling.stresstests;

import junit.framework.TestCase;

import com.topcoder.util.scheduler.scheduling.ScheduledEnableObjectFactory;
import com.topcoder.util.scheduler.scheduling.ScheduledEnableObjectFactoryManager;

/**
 * Unit test cases for class <code>ScheduledEnableObjectFactoryManager</code>.
 * 
 * @author 80x86
 * @version 3.1
 */
public class ScheduledEnableObjectFactoryManagerTests extends TestCase {

    /**
     * Represents one <code>ScheduledEnableObjectFactory</code> instance for testing.
     */
    private ScheduledEnableObjectFactory factory1 = null;

    /**
     * Represents another <code>ScheduledEnableObjectFactory</code> instance for testing.
     */
    private ScheduledEnableObjectFactory factory2 = null;

    /**
     * <p>
     * Sets up the test environment.
     * </p>
     * 
     * @throws Exception
     *             if there is any problem.
     */
    protected void setUp() throws Exception {
        factory1 = new CustomScheduledEnableObjectFactoryStress();
        factory2 = new CustomScheduledEnableObjectFactoryStress();
        // clear the factories
        ScheduledEnableObjectFactoryManager.clearScheduledEnableObjectFactories();
        // add factories
        ScheduledEnableObjectFactoryManager.addScheduledEnableObjectFactory("key1", factory1);
        ScheduledEnableObjectFactoryManager.addScheduledEnableObjectFactory("key2", factory2);
    }

    /**
     * <p>
     * Stress test for method <code>getScheduledEnableObjectFactory(String)</code>.
     * </p>
     */
    public void testGetScheduledEnableObjectFactory() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < StressTestHelper31.REPEAT_COUNT; i++) {
            assertSame("Fails to get the ScheduledEnableObjectFactory", factory1, ScheduledEnableObjectFactoryManager
                    .getScheduledEnableObjectFactory("key1"));
        }
        long end = System.currentTimeMillis();
        System.out.println("Calling getScheduledEnableObjectFactory for " + StressTestHelper31.REPEAT_COUNT
                + " times takes " + (end - start) + " ms.");
    }

    /**
     * <p>
     * Stress test for method <code>addScheduledEnableObjectFactory(String, ScheduledEnableObjectFactory)</code>.
     * </p>
     */
    public void testAddScheduledEnableObjectFactory() {
        ScheduledEnableObjectFactory factory = new CustomScheduledEnableObjectFactoryStress();
        long start = System.currentTimeMillis();
        for (int i = 0; i < StressTestHelper31.REPEAT_COUNT; i++) {
            ScheduledEnableObjectFactoryManager.addScheduledEnableObjectFactory("key" + i, factory);
        }
        long end = System.currentTimeMillis();
        System.out.println("Calling addScheduledEnableObjectFactory for " + StressTestHelper31.REPEAT_COUNT
                + " times takes " + (end - start) + " ms.");
    }

    /**
     * <p>
     * Stress test for method <code>removeScheduledEnableObjectFactory(String)</code>.
     * </p>
     */
    public void testRemoveScheduledEnableObjectFactory() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < StressTestHelper31.REPEAT_COUNT; i++) {
            ScheduledEnableObjectFactoryManager.removeScheduledEnableObjectFactory("key1");
        }
        long end = System.currentTimeMillis();
        System.out.println("Calling removeScheduledEnableObjectFactory for " + StressTestHelper31.REPEAT_COUNT
                + " times takes " + (end - start) + " ms.");
    }

    /**
     * <p>
     * Stress test for method <code>getScheduledEnableObjectFactoryNames</code>.
     * </p>
     */
    public void testGetScheduledEnableObjectFactoryNames() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < StressTestHelper31.REPEAT_COUNT; i++) {
            String[] names = ScheduledEnableObjectFactoryManager.getScheduledEnableObjectFactoryNames();
            assertEquals("Fails to get the ScheduledEnableObjectFactory names.", 2, names.length);
        }
        long end = System.currentTimeMillis();
        System.out.println("Calling getScheduledEnableObjectFactoryNames for " + StressTestHelper31.REPEAT_COUNT
                + " times takes " + (end - start) + " ms.");
    }

    /**
     * <p>
     * Stress test for method <code>clearScheduledEnableObjectFactories()</code>.
     * </p>
     * <p>
     * It tests that the factories map is cleared.
     * </p>
     */
    public void testClearScheduledEnableObjectFactories() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < StressTestHelper31.REPEAT_COUNT; i++) {
            ScheduledEnableObjectFactoryManager.clearScheduledEnableObjectFactories();
        }
        long end = System.currentTimeMillis();
        System.out.println("Calling clearScheduledEnableObjectFactories for " + StressTestHelper31.REPEAT_COUNT
                + " times takes " + (end - start) + " ms.");
    }
}
