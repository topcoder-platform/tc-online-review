/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.memoryusage.stresstests;

import com.topcoder.util.config.ConfigManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


/**
 * Stress test helper methods.
 *
 * @author mgmg
 * @version 1.0
 */
final class StressTestHelper {
    /**
     * Record the start time.
     */
    private static long startTime = 0;

    /**
     * The test count.
     */
    static final int testCount = 200;

    /**
     * Private constructor.
     */
    private StressTestHelper() {
    }

    /**
     * Load a config file to ConfigManager.
     *
     * @param filename the file to load.
     *
     * @throws Exception exception to the caller.
     */
    static void loadConfig() throws Exception {
    	clearConfig();
        ConfigManager.getInstance().add("stresstests/stress.xml");
    }

    /**
     * Clear all the configs.
     *
     * @throws Exception exception to the caller.
     */
    static void clearConfig() throws Exception {
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
     * Start to record time.
     *
     */
    static long startTimer() {
        startTime = new Date().getTime();

        return startTime;
    }

    /**
     * Get the absolutely time.
     *
     */
    static long getAbsolutelyTimer() {
        return new Date().getTime() - startTime;
    }

    /**
     * Print the time used to the console.
     *
     */
    static void printTime(String method, int times) {
        System.out.println(method + " for " + ((times < 0) ? testCount : times) + " times will take "
            + getAbsolutelyTimer() + " ms");
    }

    /**
     * Print the time used to the console.
     *
     */
    static void printTime(String method) {
        System.out.println(method + " will take " + getAbsolutelyTimer() + " ms");
    }
}
