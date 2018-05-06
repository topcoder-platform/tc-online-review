/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 *
 * TCS Memory Usage Version 2.0 Accuracytests.
 *
 * @ TestHelper.java
 */
package com.topcoder.util.memoryusage.accuracytests;


import com.topcoder.util.config.ConfigManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 *<p>
 * Test Helper for the component.
 * </p>
 * @author zmg
 * @version 1.0
 */
public final class TestHelper {
    /**
     * Private test helper.
     */
    private TestHelper() {
    }

    /**
     * Clear all the configs.
     *
     * @throws Exception exception to the caller.
     */
    public static void clearConfig() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();
        Iterator it = cm.getAllNamespaces();
        List nameSpaces = new ArrayList();

        while (it.hasNext()) {
            nameSpaces.add(it.next());
        }

        for (int i = 0; i < nameSpaces.size(); i++) {
            cm.removeNamespace((String) nameSpaces.get(i));
        }
    }

    /**
     * Computes the size of a class by creating many instances of it and then
     * finding the average amount of memory taken.
     *
     * @param cls the class whose size to compute
     * @param n the number of instances to create for computing the average
     *
     * @return the size in bytes, rounded to the nearest multiple of 8
     *
     * @throws Exception shouldn't ever be thrown
     */
    public static long getAverageSize(Class cls, int n)
        throws Exception {
        Object[] a = new Object[n];

        // clear memory as much as possible.
        System.gc();
        System.gc();
        System.gc();
        System.gc();
        System.gc();

        long mem = Runtime.getRuntime().totalMemory()
            - Runtime.getRuntime().freeMemory();

        // create instances of desired class
        for (int i = 0; i < n; i++) {
            a[i] = cls.newInstance();
        }

        // clear memory again
        System.gc();
        System.gc();
        System.gc();
        System.gc();
        System.gc();

        // compute total memory used
        long mem2 = Runtime.getRuntime().totalMemory()
            - Runtime.getRuntime().freeMemory();
        long average = Math.round((1.0 * (mem2 - mem)) / n);

        // round to multiple of 8
        return (long) Math.round((1.0 * average) / 8) * 8;
    }
}
