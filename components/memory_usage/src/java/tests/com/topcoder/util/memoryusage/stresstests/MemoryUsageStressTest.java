/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.memoryusage.stresstests;

import com.topcoder.util.memoryusage.MemoryUsage;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Stress test for MemoryUsage class.
 *
 * @author mgmg
 * @version 1.0
 */
public class MemoryUsageStressTest extends TestCase {
    /**
     * The test instance.
     */
    private MemoryUsage instance = null;

    /**
     * Create the test environment.
     */
    public void setUp() throws Exception {
        StressTestHelper.loadConfig();
        instance = new MemoryUsage("com.topcoder.util.memoryusage.MemoryUsage");
    }

    /**
     * Clean the test environment.
     */
    public void tearDown() throws Exception {
        StressTestHelper.clearConfig();
    }

    /**
     * Stress test for constructor.
     */
    public void testMemoryUsageConstructor() throws Exception {
        MemoryUsage usage = null;
        StressTestHelper.startTimer();

        for (int i = 0; i < StressTestHelper.testCount; i++) {
            usage = new MemoryUsage("com.topcoder.util.memoryusage.MemoryUsage");
        }

        StressTestHelper.printTime("MemoryUsage##ctor", -1);
    }

    /**
     * Stress test for getEmbeddedObjects.
     */
    public void testGetEmbeddedObjects() throws Exception {
        Object[] result = null;
        Class[] clazz = new Class[] {ArrayList.class, HashMap.class, String.class};
        Object[] objs = new Object[StressTestHelper.testCount];

        for (int i = 0; i < StressTestHelper.testCount; i++) {
            objs[i] = clazz[i % 3].newInstance();
        }

        StressTestHelper.startTimer();

        for (int i = 0; i < StressTestHelper.testCount; i++) {
            result = MemoryUsage.getEmbeddedObjects(objs);
        }

        StressTestHelper.printTime("MemoryUsage.getEmbeddedObjects", -1);
    }

    /**
     * Stress test for getShallowMemoryUsage.
     */
    public void testGetShallowMemoryUsage() throws Exception {
        Object[] objs = new Object[StressTestHelper.testCount];

        for (int i = 0; i < StressTestHelper.testCount; i++) {
            switch (i % 3) {
            case 1:
                objs[i] = new ArrayList();

                for (int j = 0; j < ((i / 10) + 3); j++) {
                    ((ArrayList) objs[i]).add(new Object());
                }

                break;

            case 2:
                objs[i] = new HashMap();

                for (int j = 0; j < ((i / 10) + 3); j++) {
                    ((HashMap) objs[i]).put(new Integer(j), new Object());
                }

                break;

            default:
                objs[i] = new Integer(i).toString();

                break;
            }
        }

        StressTestHelper.startTimer();

        for (int i = 0; i < StressTestHelper.testCount; i++) {
            instance.getShallowMemoryUsage(objs);
        }

        StressTestHelper.printTime("MemoryUsage.getShallowMemoryUsage", -1);
    }

    /**
     * Stress test for getDeepMemoryUsage.
     */
    public void testGetDeepMemoryUsage() throws Exception {
        Object[] objs = new Object[StressTestHelper.testCount];

        for (int i = 0; i < StressTestHelper.testCount; i++) {
            switch (i % 3) {
            case 1:
                objs[i] = new ArrayList();

                for (int j = 0; j < ((i / 10) + 3); j++) {
                    ((ArrayList) objs[i]).add(new Object());
                }

                break;

            case 2:
                objs[i] = new HashMap();

                for (int j = 0; j < ((i / 10) + 3); j++) {
                    ((HashMap) objs[i]).put(new Integer(j), new Object());
                }

                break;

            default:
                objs[i] = new Integer(i).toString();

                break;
            }
        }

        StressTestHelper.startTimer();

        for (int i = 0; i < StressTestHelper.testCount; i++) {
            instance.getDeepMemoryUsage(objs);
        }

        StressTestHelper.printTime("MemoryUsage.getDeepMemoryUsage", -1);
    }
}
