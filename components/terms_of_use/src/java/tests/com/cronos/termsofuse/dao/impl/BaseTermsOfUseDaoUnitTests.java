/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.termsofuse.dao.impl;

import static org.junit.Assert.assertNotNull;
import junit.framework.JUnit4TestAdapter;

import org.junit.Before;
import org.junit.Test;

import com.cronos.termsofuse.TestsHelper;
import com.cronos.termsofuse.dao.TermsOfUseDaoConfigurationException;
import com.topcoder.configuration.ConfigurationObject;

/**
 * <p>
 * Unit tests for {@link BaseTermsOfUseDao} class.
 * </p>
 *
 * @author sparemax
 * @version 1.0
 */
public class BaseTermsOfUseDaoUnitTests {
    /**
     * <p>
     * Represents the <code>BaseTermsOfUseDao</code> instance used in tests.
     * </p>
     */
    private BaseTermsOfUseDao instance;

    /**
     * <p>
     * Represents the configuration object used in tests.
     * </p>
     */
    private ConfigurationObject configurationObject;

    /**
     * <p>
     * Adapter for earlier versions of JUnit.
     * </p>
     *
     * @return a test suite.
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(BaseTermsOfUseDaoUnitTests.class);
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
        configurationObject = TestsHelper.getConfig(TestsHelper.CONFIG_USER_TERMS);

        instance = new MockTermsOfUseDao(configurationObject);
    }

    /**
     * <p>
     * Accuracy test for the constructor <code>BaseTermsOfUseDao(ConfigurationObject configurationObject)</code>.<br>
     * Instance should be correctly created.
     * </p>
     */
    @Test
    public void testCtor() {
        instance = new MockTermsOfUseDao(configurationObject);

        assertNotNull("'dbConnectionFactory' should be correct.", instance.getDBConnectionFactory());
        assertNotNull("'log' should be correct.", instance.getLog());
    }

    /**
     * <p>
     * Failure test for the constructor <code>BaseTermsOfUseDao(ConfigurationObject configurationObject)</code> with
     * configurationObject is <code>null</code>.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCtor_configurationObjectNull() {
        configurationObject = null;

        new MockTermsOfUseDao(configurationObject);
    }

    /**
     * <p>
     * Failure test for the constructor <code>BaseTermsOfUseDao(ConfigurationObject configurationObject)</code> with
     * 'dbConnectionFactoryConfig' is missing.<br>
     * <code>TermsOfUseDaoConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test(expected = TermsOfUseDaoConfigurationException.class)
    public void testCtor_dbConnectionFactoryConfigMissing() throws Exception {
        configurationObject.removeChild("dbConnectionFactoryConfig");

        new MockTermsOfUseDao(configurationObject);
    }

    /**
     * <p>
     * Failure test for the constructor <code>BaseTermsOfUseDao(ConfigurationObject configurationObject)</code> with
     * failed to create the db connection factor.<br>
     * <code>TermsOfUseDaoConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test(expected = TermsOfUseDaoConfigurationException.class)
    public void testCtor_dbConnectionFactoryConfigInvalid1() throws Exception {
        configurationObject.getChild("dbConnectionFactoryConfig")
            .removeChild("com.topcoder.db.connectionfactory.DBConnectionFactoryImpl");

        new MockTermsOfUseDao(configurationObject);
    }

    /**
     * <p>
     * Failure test for the constructor <code>BaseTermsOfUseDao(ConfigurationObject configurationObject)</code> with
     * failed to create the db connection factor.<br>
     * <code>TermsOfUseDaoConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test(expected = TermsOfUseDaoConfigurationException.class)
    public void testCtor_dbConnectionFactoryConfigInvalid2() throws Exception {
        configurationObject.getChild("dbConnectionFactoryConfig")
            .getChild("com.topcoder.db.connectionfactory.DBConnectionFactoryImpl")
            .getChild("connections").setPropertyValue("default", "not_exist");

        new MockTermsOfUseDao(configurationObject);
    }

    /**
     * <p>
     * Failure test for the constructor <code>BaseTermsOfUseDao(ConfigurationObject configurationObject)</code> with
     * 'loggerName' is missing.<br>
     * <code>TermsOfUseDaoConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test(expected = TermsOfUseDaoConfigurationException.class)
    public void testCtor_loggerNameMissing() throws Exception {
        configurationObject.removeProperty("loggerName");

        new MockTermsOfUseDao(configurationObject);
    }

    /**
     * <p>
     * Failure test for the constructor <code>BaseTermsOfUseDao(ConfigurationObject configurationObject)</code> with
     * 'loggerName' is not a String.<br>
     * <code>TermsOfUseDaoConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test(expected = TermsOfUseDaoConfigurationException.class)
    public void testCtor_loggerNameNotString() throws Exception {
        configurationObject.setPropertyValue("loggerName", 1);

        new MockTermsOfUseDao(configurationObject);
    }

    /**
     * <p>
     * Accuracy test for the method <code>getDBConnectionFactory()</code>.<br>
     * The value should be properly retrieved.
     * </p>
     */
    @Test
    public void test_getDBConnectionFactory() {
        assertNotNull("'getDBConnectionFactory' should be correct.", instance.getDBConnectionFactory());
    }

    /**
     * <p>
     * Accuracy test for the method <code>getLog()</code>.<br>
     * The value should be properly retrieved.
     * </p>
     */
    @Test
    public void test_getLog() {
        assertNotNull("'getLog' should be correct.", instance.getLog());
    }
}
