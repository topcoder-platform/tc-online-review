/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.assignment;

import java.util.ArrayList;
import java.util.Collection;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.management.phase.PhaseManager;
import com.topcoder.management.project.ProjectManager;
import com.topcoder.util.log.Log;
import com.topcoder.util.log.LogManager;

/**
 * Unit tests for <code>{@link Helper}</code> class.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class UnitTestHelper extends BaseTestCase {
    /**
     * The <code>{@link Log}</code> instance used for testing.
     */
    private Log log;

    /**
     * The <code>ConfigurationObject</code> instance used for testing.
     */
    private ConfigurationObject config;

    /**
     * <p>
     * Sets up the test environment.
     * </p>
     *
     * @throws Exception
     *             throws exception if any.
     */
    protected void setUp() throws Exception {
        super.setUp();
        // Get logger
        log = LogManager.getLog("my_logger");
        config = getConfigurationObject("test_files/config/config.properties", "assignment",
            ReviewAssignmentManager.class.getName());
    }

    /**
     * <p>
     * Tears down the test environment.
     * </p>
     *
     * @throws Exception
     *             throws exception if any.
     */
    protected void tearDown() throws Exception {
    }

    /**
     * <p>
     * Tests failure of <code>checkNullIAE(Log logger, String signature, Object obj, String varName)</code>
     * method with <code>obj</code> is null.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    public void test_checkNullIAE() {
        try {
            Helper.checkNullIAE(null, "name", null, "name");

            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }

    /**
     * <p>
     * Tests failure of
     * <code>checkNullEmptyIAE(Log logger, String signature, String str, String varName)</code> method with
     * <code>logger</code> is null.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    public void test_checkNullEmptyIAE1() {
        try {
            Helper.checkNullEmptyIAE(null, "name", null, "name");

            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }

    /**
     * <p>
     * Tests failure of
     * <code>checkNullEmptyIAE(Log logger, String signature, String str, String varName)</code> method with
     * <code>str</code> is empty.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    public void test_checkNullEmptyIAE2() {
        try {
            Helper.checkNullEmptyIAE(log, "name", "", "name");

            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }

    /**
     * <p>
     * Tests failure of
     * <code>checkNullEmptyIAE(Log logger, String signature, String str, String varName)</code> method with
     * <code>str</code> is empty.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    public void test_checkNullEmptyIAE3() {
        try {
            Helper.checkNullEmptyIAE(log, "name", "  \n ", "name");

            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }

    /**
     * <p>
     * Tests failure of
     * <code>checkListIAE(Log logger, String signature, Collection<T> value, String name)</code> method with
     * <code>value</code> is null.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    public void test_checkListIAE1() {
        try {
            Helper.checkListIAE(log, "signature", null, "name");

            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }

    /**
     * <p>
     * Tests failure of
     * <code>checkListIAE(Log logger, String signature, Collection<T> value, String name)</code> method with
     * <code>value</code> contains null.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    public void test_checkListIAE2() {
        Collection<String> value = new ArrayList<String>();
        value.add(null);
        try {
            Helper.checkListIAE(log, "signature", value, "name");

            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }

    /**
     * <p>
     * Tests accuracy of <code>closeStatement(Log log, String signature, Statement statement)</code> method.
     * </p>
     */
    public void test_closeStatement() {
        Helper.closeStatement(log, "signature", null);
    }

    /**
     * <p>
     * Tests accuracy of <code>closeResultSet(Log log, String signature, ResultSet rs)</code> method.
     * </p>
     */
    public void closeResultSet() {
        Helper.closeResultSet(log, "signature", null);
    }

    /**
     * <p>
     * Tests accuracy of <code>closeConnection(Log log, String signature, Connection connection)</code>
     * method.
     * </p>
     */
    public void test_closeConnection() {
        Helper.closeConnection(log, "signature", null);
    }

    /**
     * <p>
     * Tests accuracy of <code>getLog(ConfigurationObject config)</code> method.
     * </p>
     */
    public void test_getLogger() {
        assertNotNull("should be null logger.", Helper.getLog(config));
    }

    /**
     * <p>
     * Tests failure of <code>checkState(Object, String, Log, String)</code> method with <code>value</code> is
     * <code>null</code>.<br>
     * <code>IllegalStateException</code> is expected.
     * </p>
     */
    public void test_checkState_Null() {
        try {
            Helper.checkState(null, "name", log, "name");

            fail("IllegalStateException is expected.");
        } catch (IllegalStateException e) {
            // Good
        }
    }

    /**
     * <p>
     * Tests accuracy of <code>checkState(Object, String, Log, String)</code> method.<br>
     * Result should be correct.
     * </p>
     */
    public void test_checkState() {
        Helper.checkState(new Object(), "name", log, "name");

        // Good
    }

    /**
     * <p>
     * Tests accuracy of <code>getPropertyValue(ConfigurationObject, String, boolean,
     * boolean)</code> method.<br>
     * Result should be correct.
     * </p>
     */
    public void test_getPropertyValue_1() {
        assertEquals("wrong value", "testlog", Helper.getPropertyValue(config, "loggerName", false));
    }

    /**
     * <p>
     * Tests accuracy of <code>getPropertyValue(ConfigurationObject, String, boolean,
     * boolean)</code> method.
     * </p>
     * <p>
     * The specified key does not exist when <code>isRquired</code> is <code>false</code>, <code>null</code>
     * expected.
     * </p>
     */
    public void test_getPropertyValue_2() {
        assertNull("should be null", Helper.getPropertyValue(config, "not exist", false));
    }

    /**
     * <p>
     * Tests accuracy of <code>getPropertyValues(ConfigurationObject, String)</code> method.
     * </p>
     * <p>
     * The specified key does not exist. <code>ReviewAssignmentConfigurationException</code> expected.
     * </p>
     */
    public void test_getPropertyValues1() {
        try {
            Helper.getPropertyValues(config, "not exist");

            fail("ReviewAssignmentConfigurationException is expected.");
        } catch (ReviewAssignmentConfigurationException e) {
            // Good
        }
    }

    /**
     * <p>
     * Tests failure of <code>getPropertyValue(ConfigurationObject, String, boolean,
     * boolean)</code> method.
     * </p>
     * <p>
     * The specified key does not exist when <code>isRquired</code> is <code>true</code>,
     * <code>ReviewAssignmentConfigurationException</code> expected.
     * </p>
     */
    public void test_getPropertyValue_3() {
        try {
            Helper.getPropertyValue(config, "not exist", true);

            fail("ReviewAssignmentConfigurationException is expected.");
        } catch (ReviewAssignmentConfigurationException e) {
            // Good
        }
    }

    /**
     * <p>
     * Tests failure of <code>getPropertyValue(ConfigurationObject, String, boolean,
     * boolean)</code> method.
     * </p>
     * <p>
     * The value is empty when <code>canBeEmpty</code> is <code>false</code>,
     * <code>ReviewAssignmentConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_getPropertyValue_5() throws Exception {
        config.setPropertyValue("loggerName", "");

        try {
            Helper.getPropertyValue(config, "loggerName", true);

            fail("ReviewAssignmentConfigurationException is expected.");
        } catch (ReviewAssignmentConfigurationException e) {
            // Good
        }
    }

    /**
     * <p>
     * Tests failure of <code>getPropertyValue(ConfigurationObject, String, boolean,
     * boolean)</code> method.
     * </p>
     * <p>
     * The value not type of <code>String</code>, <code>ReviewAssignmentConfigurationException</code>
     * expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_getPropertyValue_6() throws Exception {
        config.setPropertyValue("loggerName", new Exception());

        try {
            Helper.getPropertyValue(config, "loggerName", true);

            fail("ReviewAssignmentConfigurationException is expected.");
        } catch (ReviewAssignmentConfigurationException e) {
            // Good
        }
    }

    /**
     * <p>
     * Tests accuracy of <code>getChildConfig(ConfigurationObject, String)</code> method.<br>
     * Result should be correct.
     * </p>
     */
    public void test_getChildConfig_1() {
        assertNotNull("should not be null", Helper.getChildConfig(config, "objectFactoryConfig"));
    }

    /**
     * <p>
     * Tests failure of <code>getChildConfig(ConfigurationObject, String)</code> method.
     * </p>
     * <p>
     * The child config does not exist, <code>ReviewAssignmentConfigurationException</code> expected.
     * </p>
     */
    public void test_getChildConfig_2() {
        try {
            Helper.getChildConfig(config, "not exist");

            fail("ReviewAssignmentConfigurationException is expected.");
        } catch (ReviewAssignmentConfigurationException e) {
            // Good
        }
    }

    /**
     * <p>
     * Tests accuracy of <code>createObjectFactory(ConfigurationObject)</code> method.
     * </p>
     */
    public void test_createObjectFactory() {
        assertNotNull("should not be null", Helper.createObjectFactory(config));
    }

    /**
     * <p>
     * Tests accuracy of <code>createObject(ConfigurationObject, ObjectFactory, String, Class)</code> method.<br>
     * Object should be created correctly.
     * </p>
     */
    public void test_createObject_1() {
        assertNotNull("should not be null", Helper.createObject(config, Helper.createObjectFactory(config),
            "projectManagerKey", ProjectManager.class));
    }

    /**
     * <p>
     * Tests failure of <code>createObject(ConfigurationObject, ObjectFactory, String, Class)</code> method.
     * </p>
     * <p>
     * The object created is not desired type, <code>ReviewAssignmentConfigurationException</code> expected.
     * </p>
     */
    public void test_createObject_2() {
        try {
            Helper.createObject(config, Helper.createObjectFactory(config), "projectManagerKey",
                PhaseManager.class);

            fail("ReviewAssignmentConfigurationException is expected.");
        } catch (ReviewAssignmentConfigurationException e) {
            // Good
        }
    }

    /**
     * <p>
     * Tests accuracy of <code>logEntrance(Log, String, String[], Object[])</code> method.
     * </p>
     * <p>
     * Log should be done.
     * </p>
     */
    public void test_logEntrance_1() {
        Helper.logEntrance(log, "name", new String[] {"name1"}, new Object[] {"value1"});
        // log to console
    }

    /**
     * <p>
     * Tests accuracy of <code>logEntrance(Log, String, String[], Object[])</code> method.
     * </p>
     * <p>
     * The log is null, no log done.
     * </p>
     */
    public void test_logEntrance_2() {
        Helper.logEntrance(null, "name", new String[] {"name1"}, new Object[] {"value1"});
    }

    /**
     * <p>
     * Tests accuracy of <code>logExit(Log, String, Object, long)</code> method.
     * </p>
     * <p>
     * Log should be done.
     * </p>
     */
    public void test_logExit_1() {
        Helper.logExit(log, "name", "value", System.currentTimeMillis());
        // log to console
    }

    /**
     * <p>
     * Tests accuracy of <code>logExit(Log, String, Object, long)</code> method.
     * </p>
     * <p>
     * The log is null, no log done.
     * </p>
     */
    public void test_logExit_2() {
        Helper.logExit(null, "name", "value", System.currentTimeMillis());
    }

    /**
     * <p>
     * Tests accuracy of <code>logException(Log, String, T)</code> method.
     * </p>
     * <p>
     * Log should be done.
     * </p>
     */
    public void test_logException_1() {
        Helper.logException(log, "name", new Exception("message"));
        // log to console
    }

    /**
     * <p>
     * Tests accuracy of <code>logException(Log, String, T)</code> method.
     * </p>
     * <p>
     * The log is null, no log done.
     * </p>
     */
    public void test_logException_2() {
        Helper.logException(null, "name", new Exception("message"));
    }

    /**
     * <p>
     * Tests accuracy of <code>logInfo(Log, String)</code> method.
     * </p>
     * <p>
     * Log should be done.
     * </p>
     */
    public void test_logInfo_1() {
        Helper.logInfo(log, "name");
        // log to console
    }

    /**
     * <p>
     * Tests accuracy of <code>logInfo(Log, String)</code> method.
     * </p>
     * <p>
     * The log is null, no log done.
     * </p>
     */
    public void test_logInfo_2() {
        Helper.logInfo(null, "name");
    }

    /**
     * <p>
     * Tests accuracy of <code>logWarn(Log, String)</code> method.
     * </p>
     * <p>
     * Log should be done.
     * </p>
     */
    public void test_logWarn_1() {
        Helper.logWarn(log, "name");
        // log to console
    }

    /**
     * <p>
     * Tests accuracy of <code>logWarn(Log, String)</code> method.
     * </p>
     * <p>
     * The log is null, no log done.
     * </p>
     */
    public void test_logWarn_2() {
        Helper.logWarn(null, "name");
    }

    /**
     * <p>
     * Tests accuracy of <code>convertLong(Collection<Long> longs) </code> method.
     * </p>
     */
    public void test_convertLong() {
        Collection<Long> longs = new ArrayList<Long>();
        longs.add(new Long(1));
        longs.add(new Long(2));
        long[] converted = Helper.convertLong(longs);
        assertTrue("Should be same size.", converted.length == longs.size());
    }
}
