/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.ajax.stresstests;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import com.topcoder.util.config.ConfigManager;


/**
 * <p>
 * Defines helper methods used in tests.
 * </p>
 *
 * @author PE
 * @version 1.0
 */
public final class StressTestHelper {
    /** Represents the time to run the testing method. */
    public static final int TIMES = 1000;

    /** Represents the data format. */
    private static final String DATE_FORMAT = "MM.dd.yyyy h:mm a";

    /** Represents the current time. */
    private static long current = -1;

    /**
     * <p>
     * Creates a new instance of <code>StressTestHelper</code> class. The private constructor prevents the creation of
     * a new instance.
     * </p>
     */
    private StressTestHelper() {
    }

    /**
     * Start to count time.
     */
    public static void start() {
        current = System.currentTimeMillis();
    }

    /**
     * Print test result.
     *
     * @param name the test name.
     */
    public static void printResult(String name) {
        System.out.println("The test " + name + " running " + TIMES + " times, took time: " +
            (System.currentTimeMillis() - current) + " ms");
    }

    /**
     * <p>
     * add the config of given config file.
     * </p>
     *
     * @throws Exception unexpected exception.
     */
    public static void addConfig() throws Exception {
        clearConfig();

        ConfigManager configManager = ConfigManager.getInstance();
        configManager.add("stresstest/scorecalculator.xml");
        configManager.add("stresstest/Object_Factory_Config.xml");
        configManager.add("stresstest/Logging_Wrapper_Config.xml");
        configManager.add("stresstest/Online_Review_Ajax_Support_Config.xml");
    }

    /**
     * <p>
     * clear the config.
     * </p>
     *
     * @throws Exception unexpected exception.
     */
    public static void clearConfig() throws Exception {
        ConfigManager configManager = ConfigManager.getInstance();

        for (Iterator iter = configManager.getAllNamespaces(); iter.hasNext();) {
            configManager.removeNamespace((String) iter.next());
        }
    }

    /**
     * Convert a date object to string.
     *
     * @param date the date to convert.
     *
     * @return the string representation.
     */
    public static String dateToString(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);

        return formatter.format(date);
    }
}
