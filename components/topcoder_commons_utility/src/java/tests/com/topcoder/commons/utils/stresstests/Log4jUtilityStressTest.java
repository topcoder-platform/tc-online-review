/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.commons.utils.stresstests;

import java.util.Date;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.topcoder.commons.utils.Log4jUtility;

/**
 * <p>
 * Stress test case of {@link Log4jUtility}.
 * </p>
 * 
 * @author mumujava
 * @version 1.0
 */
public class Log4jUtilityStressTest extends BaseStressTest {

    /**
     * <p>
     * Sets up test environment.
     * </p>
     * 
     * @throws Exception
     *             to jUnit.
     */
    @Override
    public void setUp() throws Exception {
        super.setUp();

    }

    /**
     * <p>
     * Tears down test environment.
     * </p>
     * 
     * @throws Exception
     *             to jUnit.
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * <p>
     * Creates a test suite for the tests in this test case.
     * </p>
     * 
     * @return a TestSuite for this test case.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(
                Log4jUtilityStressTest.class);
        return suite;
    }

    /**
     * <p>
     * Stress test for method Log4jUtility#logEntrance().
     * 
     * </p>
     * @throws Throwable to junit
     */
    public void test_logEntrance() throws Throwable {
        Logger logger = Logger.getLogger("test");
        for (int i = 0; i < testCount; i++) {
            Log4jUtility.logEntrance(logger , "method name", new String[]{"name"}, new Object[]{"value"});
        }
        
        System.out.println("Run logEntrance for " + testCount + " times takes "
                + (new Date().getTime() - start) + "ms");
    }

    /**
     * <p>
     * Stress test for method Log4jUtility#logEntrance().
     * 
     * </p>
     * @throws Throwable to junit
     */
    public void test_logEntrance2() throws Throwable {
        Logger logger = Logger.getLogger("test");
        for (int i = 0; i < testCount; i++) {
            Log4jUtility.logEntrance(logger , "method name", new String[]{"name"}, new Object[]{"value"}, Level.DEBUG);
        }
        
        System.out.println("Run logEntrance for " + testCount + " times takes "
                + (new Date().getTime() - start) + "ms");
    }

    /**
     * <p>
     * Stress test for method Log4jUtility#logExit().
     * 
     * </p>
     * @throws Throwable to junit
     */
    public void test_logExit() throws Throwable {
        Logger logger = Logger.getLogger("test");
        for (int i = 0; i < testCount; i++) {
            Log4jUtility.logExit(logger , "method name", new Object[]{"value"});
        }
        
        System.out.println("Run logExit for " + testCount + " times takes "
                + (new Date().getTime() - start) + "ms");
    }

    /**
     * <p>
     * Stress test for method Log4jUtility#logExit().
     * 
     * </p>
     * @throws Throwable to junit
     */
    public void test_logExit2() throws Throwable {
        Logger logger = Logger.getLogger("test");
        for (int i = 0; i < testCount; i++) {
            Log4jUtility.logExit(logger , "method name", new Object[]{"value"}, new Date(), Level.DEBUG);
        }
        
        System.out.println("Run logExit for " + testCount + " times takes "
                + (new Date().getTime() - start) + "ms");
    }
    /**
     * <p>
     * Stress test for method Log4jUtility#logExit().
     * 
     * </p>
     * @throws Throwable to junit
     */
    public void test_logExit3() throws Throwable {
        Logger logger = Logger.getLogger("test");
        for (int i = 0; i < testCount; i++) {
            Log4jUtility.logExit(logger , "method name", new Object[]{"value"}, new Date());
        }
        
        System.out.println("Run logExit for " + testCount + " times takes "
                + (new Date().getTime() - start) + "ms");
    }
    
    
    /**
     * <p>
     * Stress test for method Log4jUtility#logException().
     * 
     * </p>
     * @throws Throwable to junit
     */
    public void test_logException() throws Throwable {
        Logger logger = Logger.getLogger("test");
        for (int i = 0; i < testCount; i++) {
            Log4jUtility.logException(logger , "method name", new IllegalArgumentException("error occurs"));
        }
        
        System.out.println("Run logException for " + testCount + " times takes "
                + (new Date().getTime() - start) + "ms");
    }

    /**
     * <p>
     * Stress test for method Log4jUtility#logException().
     * 
     * </p>
     * @throws Throwable to junit
     */
    public void test_logException2() throws Throwable {
        Logger logger = Logger.getLogger("test");
        for (int i = 0; i < testCount; i++) {
            Log4jUtility.logException(logger , "method name", new IllegalArgumentException("error occurs"), Level.DEBUG);
        }
        
        System.out.println("Run logException for " + testCount + " times takes "
                + (new Date().getTime() - start) + "ms");
    }
}
