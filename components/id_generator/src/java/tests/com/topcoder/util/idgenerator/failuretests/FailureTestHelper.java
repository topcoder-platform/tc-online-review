/*
 * Copyright (c) 2005, TopCoder, Inc. All rights reserved.
 */
package com.topcoder.util.idgenerator.failuretests;

import java.io.FileInputStream;
import java.util.Properties;

/**
 * <p>FailureTestHelper class provides some useful helper methods.</p>
 *
 * @author cosherx
 * @version 3.0
 */
public class FailureTestHelper {
    /** The failure test properties file name. */
    private static final String CONFIG_FILE = "test_files/failure/idgenerator_failure_test.properties";

    /** The test oracle id sequence. */
    public static String oracle_sequence = null;

    /** The first test oracle exhaust id sequence. */
    public static String oracle_exhaust_1 = null;

    /** The second test oracle exhaust id sequence. */
    public static String oracle_exhaust_2 = null;

    /** The test default id sequence. */
    public static String default_sequence = null;

    /**The first test default exhaust id sequence. */
    public static String default_exhaust_1 = null;

    /** The second test default exhaust id sequence. */
    public static String default_exhaust_2 = null;
    
    /** The JNDI id-generator home name. */
    public static String idGeneratorHome = null;
    
    /**
     * Private constructor to prevent this class be instantiated.
     */
    private FailureTestHelper() {
        // empty
    }

    /**
     * Load config properties for testing.
     */
    static {
        try {
            // load the test config from file
            Properties prop = new Properties();
            prop.load(new FileInputStream(CONFIG_FILE));
            oracle_sequence = prop.getProperty("oracle_sequence");
            oracle_exhaust_1 = prop.getProperty("oracle_exhaust_1");
            oracle_exhaust_2 = prop.getProperty("oracle_exhaust_2");
            default_sequence = prop.getProperty("default_sequence");
            default_exhaust_1 = prop.getProperty("default_exhaust_1");
            default_exhaust_2 = prop.getProperty("default_exhaust_2");
            idGeneratorHome = prop.getProperty("IDGeneratorHome");            
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
