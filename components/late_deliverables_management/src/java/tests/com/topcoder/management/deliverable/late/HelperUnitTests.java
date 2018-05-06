/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.late;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;

import junit.framework.JUnit4TestAdapter;

import org.junit.Before;
import org.junit.Test;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.util.log.Log;
import com.topcoder.util.log.LogFactory;

/**
 * <p>
 * Unit tests for {@link Helper} class.
 * </p>
 *
 * @author sparemax
 * @version 1.0
 */
public class HelperUnitTests {
    /**
     * <p>
     * Represents the configuration object used in tests.
     * </p>
     */
    private ConfigurationObject config;

    /**
     * <p>
     * Adapter for earlier versions of JUnit.
     * </p>
     *
     * @return a test suite.
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(HelperUnitTests.class);
    }

    /**
     * <p>
     * Sets up the unit tests.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Before
    public void setUp() throws Exception {
        config = TestsHelper.getConfig();
    }

    /**
     * <p>
     * Tests failure of <code>checkNull(Object value, String name)</code> method with <code>value</code> is
     * <code>null</code>.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_checkNull_Null() {
        Object value = null;

        Helper.checkNull(value, "name");
    }

    /**
     * <p>
     * Tests accuracy of <code>checkNull(Object value, String name)</code> method.<br>
     * Result should be correct.
     * </p>
     */
    @Test
    public void test_checkNull() {
        Object value = new Object();

        Helper.checkNull(value, "name");

        // Good
    }

    /**
     * <p>
     * Tests failure of <code>checkPositive(long value, String name)</code> method with <code>value</code> is
     * negative.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_checkPositive_Negative() {
        long value = -1;

        Helper.checkPositive(value, "name");
    }

    /**
     * <p>
     * Tests accuracy of <code>checkPositive(long value, String name)</code> method.<br>
     * Result should be correct.
     * </p>
     */
    @Test
    public void test_checkPositive() {
        long value = 1;

        Helper.checkPositive(value, "name");

        // Good
    }

    /**
     * <p>
     * Tests accuracy of <code>getLog(ConfigurationObject config)</code> method.<br>
     * Result should be correct.
     * </p>
     */
    @Test
    public void test_getLog() {
        assertNotNull("'getLog' should be correct.", Helper.getLog(config));
    }

    /**
     * <p>
     * Tests accuracy of <code>getChildConfig(ConfigurationObject config, String key)</code> method.<br>
     * Result should be correct.
     * </p>
     */
    @Test
    public void test_getChildConfig() {
        assertNotNull("'getChildConfig' should be correct.", Helper.getChildConfig(config, "objectFactoryConfig"));
    }

    /**
     * <p>
     * Tests accuracy of <code>getProperty(ConfigurationObject config, String key,
     * boolean required)</code> method.<br>
     * Result should be correct.
     * </p>
     */
    @Test
    public void test_getProperty() {
        assertEquals("'getProperty' should be correct.", "myLogger", Helper.getProperty(config, "loggerName", false));
    }

    /**
     * <p>
     * Tests accuracy of <code>concat(Object... values)</code> method.<br>
     * Result should be correct.
     * </p>
     */
    @Test
    public void test_concat() {
        String res = Helper.concat("abc", 1, null);

        assertEquals("'concat' should be correct.", "abc1null", res);
    }

    /**
     * <p>
     * Accuracy test for the method <code>logEntrance(Log logger, String signature,
     * String[] paramNames, Object[] paramValues)</code>.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_logEntrance_1() throws Exception {
        Log logger = LogFactory.getLog("loggerName");

        Helper.logEntrance(logger, "signature", null, null);
    }

    /**
     * <p>
     * Accuracy test for the method <code>logEntrance(Log logger, String signature,
     * String[] paramNames, Object[] paramValues)</code>.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_logEntrance_2() throws Exception {
        Log logger = LogFactory.getLog("loggerName");

        Helper.logEntrance(logger, "signature", new String[] {"p1", "p2"}, new String[] {"v1", "v2"});
    }

    /**
     * <p>
     * Accuracy test for the method <code>logEntrance(Log logger, String signature,
     * String[] paramNames, Object[] paramValues)</code>.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_logEntrance_3() throws Exception {
        Log logger = null;

        Helper.logEntrance(logger, "signature", new String[] {"p1", "p2"}, new String[] {"v1", "v2"});
    }

    /**
     * <p>
     * Accuracy test for the method <code>logExit(Log logger, String signature,
     * Object[] value, Date enterTimestamp)</code>.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_logExit_1() throws Exception {
        Log logger = LogFactory.getLog("loggerName");

        Helper.logExit(logger, "signature", null, new Date());
    }

    /**
     * <p>
     * Accuracy test for the method <code>logExit(Log logger, String signature,
     * Object[] value, Date enterTimestamp)</code>.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_logExit_2() throws Exception {
        Log logger = LogFactory.getLog("loggerName");

        Helper.logExit(logger, "signature", new String[] {"v1", "v2"}, new Date());
    }

    /**
     * <p>
     * Accuracy test for the method <code>logExit(Log logger, String signature,
     * Object[] value, Date enterTimestamp)</code>.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_logExit_3() throws Exception {
        Log logger = null;

        Helper.logExit(logger, "signature", new String[] {"v1", "v2"}, new Date());
    }

    /**
     * <p>
     * Accuracy test for the method <code>logException(Log log, String signature, T e, String message)</code>.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_logException_1() throws Exception {
        Log logger = LogFactory.getLog("loggerName");

        Throwable e = new Exception("Test");
        Helper.logException(logger, "signature", e, "Log message.");
    }

    /**
     * <p>
     * Accuracy test for the method <code>logException(Log log, String signature, T e, String message)</code>.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_logException_2() throws Exception {
        Log logger = null;

        Throwable e = new Exception("Test");
        Helper.logException(logger, "signature", e, "Log message.");
    }
}
