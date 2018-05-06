/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.payment.calculator.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import junit.framework.Assert;
import junit.framework.JUnit4TestAdapter;

import org.junit.Test;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.defaults.DefaultConfigurationObject;
import com.topcoder.management.payment.calculator.ProjectPaymentCalculatorConfigurationException;


/**
 * <p>
 * Unit test case of {@link Helper}.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class HelperTest {
    /**
     * <p>
     * Creates a test suite for the tests in this test case.
     * </p>
     *
     * @return a Test suite for this test case.
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(HelperTest.class);
    }

    /**
     * <p>
     * Accuracy test method for <code>Helper#getStringProperty(ConfigurationObject, String, boolean)</code> when
     * property exists and has a String value.
     * </p>
     * <p>
     * Expects that the returned value is correct.
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    @Test
    public void testGetStringProperty() throws Exception {
        ConfigurationObject config = new DefaultConfigurationObject("myConfig");
        config.setPropertyValue("key", "value");

        String value = Helper.getStringProperty(config, "key", true);
        Assert.assertEquals("Incorrect property value", "value", value);
    }

    /**
     * <p>
     * Accuracy test method for <code>Helper#getStringProperty(ConfigurationObject, String, boolean)</code> when
     * optional property has an empty value.
     * </p>
     * <p>
     * Expects that returned value is null.
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    @Test
    public void testGetStringProperty_EmptyOptional() throws Exception {
        ConfigurationObject config = new DefaultConfigurationObject("myConfig");
        config.setPropertyValue("key", "  ");

        String value = Helper.getStringProperty(config, "key", false);
        Assert.assertNull("Shall be null", value);
    }

    /**
     * <p>
     * Failure test method for <code>Helper#getStringProperty(ConfigurationObject, String, boolean)</code> when
     * property exists but it does not have a String value.
     * </p>
     * <p>
     * Expects that <code>ProjectPaymentCalculatorConfigurationException</code> is thrown.
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    @Test(expected = ProjectPaymentCalculatorConfigurationException.class)
    public void testGetStringProperty_InvalidType() throws Exception {
        ConfigurationObject config = new DefaultConfigurationObject("myConfig");
        config.setPropertyValue("key", new Integer(2));

        Helper.getStringProperty(config, "key", true);
    }

    /**
     * <p>
     * Failure test method for <code>Helper#getStringProperty(ConfigurationObject, String, boolean)</code> when
     * required property does not exist.
     * </p>
     * <p>
     * Expects that <code>ProjectPaymentCalculatorConfigurationException</code> is thrown.
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    @Test(expected = ProjectPaymentCalculatorConfigurationException.class)
    public void testGetStringProperty_MissingRequired() throws Exception {
        ConfigurationObject config = new DefaultConfigurationObject("myConfig");

        Helper.getStringProperty(config, "key", true);
    }

    /**
     * <p>
     * Failure test method for <code>Helper#getStringProperty(ConfigurationObject, String, boolean)</code> when
     * required property exists but it has an empty String value.
     * </p>
     * <p>
     * Expects that <code>ProjectPaymentCalculatorConfigurationException</code> is thrown.
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    @Test(expected = ProjectPaymentCalculatorConfigurationException.class)
    public void testGetStringProperty_EmptyRequired() throws Exception {
        ConfigurationObject config = new DefaultConfigurationObject("myConfig");
        config.setPropertyValue("key", "  ");

        Helper.getStringProperty(config, "key", true);
    }

    /**
     * <p>
     * Accuracy test method for
     * {@link Helper#getChildConfiguration(com.topcoder.configuration.ConfigurationObject, java.lang.String)}.
     * </p>
     * <p>
     * Expects the returned value is correct.
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    @Test
    public void testGetChildConfiguration() throws Exception {
        ConfigurationObject config = new DefaultConfigurationObject("myConfig");
        ConfigurationObject child = new DefaultConfigurationObject("child");
        config.addChild(child);

        ConfigurationObject result = Helper.getChildConfiguration(config, "child");
        Assert.assertSame("Incorrect child", child, result);
    }

    /**
     * <p>
     * Failure test method for
     * {@link Helper#getChildConfiguration(com.topcoder.configuration.ConfigurationObject, java.lang.String)} when
     * child does not exist.
     * </p>
     * <p>
     * Expects <code>ProjectPaymentCalculatorConfigurationException</code> is thrown.
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    @Test(expected = ProjectPaymentCalculatorConfigurationException.class)
    public void testGetChildConfiguration_Missing() throws Exception {
        ConfigurationObject config = new DefaultConfigurationObject("myConfig");
        ConfigurationObject child = new DefaultConfigurationObject("child");
        config.addChild(child);

        Helper.getChildConfiguration(config, "child1");
    }

    /**
     * <p>
     * Accuracy test method for
     * <code>Helper#closeDatabaseResources(Log, String, ResultSet, PreparedStatement, Connection)</code> .
     * </p>
     * <p>
     * Verifies that all the resources are closed.
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    @Test
    public void testCloseDatabaseResources() throws Exception {
        DefaultProjectPaymentCalculator calc = new DefaultProjectPaymentCalculator();
        Connection conn = null;
        ResultSet resultSet = null;
        PreparedStatement statement = null;
        try {
            conn = calc.createConnection();
            statement = conn.prepareStatement("SELECT resource_role_id FROM default_project_payment");
            resultSet = statement.executeQuery();
            resultSet.next();
        } finally {
            Helper.closeDatabaseResources(calc.getLogger(), "test", resultSet, statement, conn);
            try {
                resultSet.next();
                Assert.fail("The result set is not closed");
            } catch (SQLException e) {
                // expected
            }

            try {
                statement.executeQuery();
                Assert.fail("The statement is not closed");
            } catch (SQLException e) {
                // expected
            }

            try {
                conn.prepareStatement("SELECT resource_role_id FROM default_project_payment");
                Assert.fail("The connection is not closed");
            } catch (SQLException e) {
                // expected
            }
        }
    }

    /**
     * <p>
     * Accuracy test method for
     * <code>Helper#closeDatabaseResources(Log, String, ResultSet, PreparedStatement, Connection)</code> when all
     * fields to close are null.
     * </p>
     * <p>
     * Expects no error occurs as nothing is done.
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    @Test
    public void testCloseDatabaseResources_Null() throws Exception {
        Helper.closeDatabaseResources(null, "test", null, null, null);
    }
}
