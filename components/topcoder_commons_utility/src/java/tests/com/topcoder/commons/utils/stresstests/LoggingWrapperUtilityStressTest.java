/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.commons.utils.stresstests;

import java.util.Date;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.topcoder.commons.utils.LoggingWrapperUtility;
import com.topcoder.util.log.Level;
import com.topcoder.util.log.Log;
import com.topcoder.util.log.log4j.Log4jLogFactory;

/**
 * <p>
 * Stress test case of {@link LoggingWrapperUtility}.
 * </p>
 * 
 * @author mumujava
 * @version 1.0
 */
public class LoggingWrapperUtilityStressTest extends BaseStressTest {

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
                LoggingWrapperUtilityStressTest.class);
        return suite;
    }

    /**
     * <p>
     * Stress test for method LoggingWrapperUtility#logEntrance().
     * 
     * </p>
     * @throws Throwable to junit
     */
    public void test_logEntrance() throws Throwable {
        Log logger = new Log4jLogFactory().createLog("test");
        for (int i = 0; i < testCount; i++) {
            LoggingWrapperUtility.logEntrance(logger , "method name", new String[]{"name"}, new Object[]{"value"});
        }
        
        System.out.println("Run logEntrance for " + testCount + " times takes "
                + (new Date().getTime() - start) + "ms");
    }

    /**
     * <p>
     * Stress test for method LoggingWrapperUtility#logEntrance().
     * 
     * </p>
     * @throws Throwable to junit
     */
    public void test_logEntrance2() throws Throwable {
        Log logger = new Log4jLogFactory().createLog("test");
        for (int i = 0; i < testCount; i++) {
            LoggingWrapperUtility.logEntrance(logger , "method name", new String[]{"name"}, new Object[]{"value"},true,  Level.DEBUG);
        }
        
        System.out.println("Run logEntrance for " + testCount + " times takes "
                + (new Date().getTime() - start) + "ms");
    }

    /**
     * <p>
     * Stress test for method LoggingWrapperUtility#logExit().
     * 
     * </p>
     * @throws Throwable to junit
     */
    public void test_logExit() throws Throwable {
        Log logger = new Log4jLogFactory().createLog("test");
        for (int i = 0; i < testCount; i++) {
            LoggingWrapperUtility.logExit(logger , "method name", new Object[]{"value"});
        }
        
        System.out.println("Run logExit for " + testCount + " times takes "
                + (new Date().getTime() - start) + "ms");
    }

    /**
     * <p>
     * Stress test for method LoggingWrapperUtility#logExit().
     * 
     * </p>
     * @throws Throwable to junit
     */
    public void test_logExit2() throws Throwable {
        Log logger = new Log4jLogFactory().createLog("test");
        for (int i = 0; i < testCount; i++) {
            LoggingWrapperUtility.logExit(logger , "method name", new Object[]{"value"}, new Date(), true, Level.DEBUG);
        }
        
        System.out.println("Run logExit for " + testCount + " times takes "
                + (new Date().getTime() - start) + "ms");
    }
    /**
     * <p>
     * Stress test for method LoggingWrapperUtility#logExit().
     * 
     * </p>
     * @throws Throwable to junit
     */
    public void test_logExit3() throws Throwable {
        Log logger = new Log4jLogFactory().createLog("test");
        for (int i = 0; i < testCount; i++) {
            LoggingWrapperUtility.logExit(logger , "method name", new Object[]{"value"}, new Date());
        }
        
        System.out.println("Run logExit for " + testCount + " times takes "
                + (new Date().getTime() - start) + "ms");
    }
    
    
    /**
     * <p>
     * Stress test for method LoggingWrapperUtility#logException().
     * 
     * </p>
     * @throws Throwable to junit
     */
    public void test_logException() throws Throwable {
        Log logger = new Log4jLogFactory().createLog("test");
        for (int i = 0; i < testCount; i++) {
            LoggingWrapperUtility.logException(logger , "method name", new IllegalArgumentException("error occurs"));
        }
        
        System.out.println("Run logException for " + testCount + " times takes "
                + (new Date().getTime() - start) + "ms");
    }

    /**
     * <p>
     * Stress test for method LoggingWrapperUtility#logException().
     * 
     * </p>
     * @throws Throwable to junit
     */
    public void test_logException2() throws Throwable {
        Log logger = new Log4jLogFactory().createLog("test");
        for (int i = 0; i < testCount; i++) {
            LoggingWrapperUtility.logException(logger , "method name", new IllegalArgumentException("error occurs"), true, Level.DEBUG);
        }
        
        System.out.println("Run logException for " + testCount + " times takes "
                + (new Date().getTime() - start) + "ms");
    }
}
