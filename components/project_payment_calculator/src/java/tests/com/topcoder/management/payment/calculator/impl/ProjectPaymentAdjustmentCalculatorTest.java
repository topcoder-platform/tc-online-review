/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.payment.calculator.impl;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import junit.framework.JUnit4TestAdapter;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.persistence.ConfigurationFileManager;
import com.topcoder.management.payment.calculator.ProjectPaymentCalculator;
import com.topcoder.management.payment.calculator.ProjectPaymentCalculatorConfigurationException;
import com.topcoder.management.payment.calculator.TestHelper;


/**
 * <p>
 * Unit test case of {@link ProjectPaymentAdjustmentCalculator}.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class ProjectPaymentAdjustmentCalculatorTest {
    /**
     * <p>
     * Represents the ConfigurationObject instance used for testing.
     * </p>
     */
    private ConfigurationObject config;

    /**
     * <p>
     * Represents ProjectPaymentAdjustmentCalculator instance to test against.
     * </p>
     */
    private ProjectPaymentAdjustmentCalculator calc;

    /**
     * <p>
     * Creates a test suite for the tests in this test case.
     * </p>
     *
     * @return a Test suite for this test case.
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(ProjectPaymentAdjustmentCalculatorTest.class);
    }

    /**
     * <p>
     * Sets up test environment before all tests run.
     * </p>
     *
     * @throws Exception
     *             to jUnit.
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        TestHelper.initDB();
    }

    /**
     * <p>
     * Tears down test environment after all tests have run.
     * </p>
     *
     * @throws Exception
     *             to jUnit.
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        TestHelper.cleanDB();
    }

    /**
     * <p>
     * Sets up test environment.
     * </p>
     *
     * @throws Exception
     *             to jUnit.
     */
    @Before
    public void setUp() throws Exception {
        ConfigurationFileManager cfm = new ConfigurationFileManager(TestHelper.CONFIG_FILE);

        ConfigurationObject root = cfm.getConfiguration(ProjectPaymentAdjustmentCalculator.class.getName());
        config = root.getChild(ProjectPaymentAdjustmentCalculator.class.getName());

        calc = new ProjectPaymentAdjustmentCalculator(config);
    }

    /**
     * <p>
     * Tears down test environment.
     * </p>
     *
     * @throws Exception
     *             to jUnit.
     */
    @After
    public void tearDown() throws Exception {
        calc = null;
        config = null;
    }

    /**
     * <p>
     * Accuracy test method for {@link ProjectPaymentAdjustmentCalculator#ProjectPaymentAdjustmentCalculator()}.
     * </p>
     */
    @Test
    public void testCtor1() {
        Assert.assertNotNull("Unable to instantiate ProjectPaymentAdjustmentCalculator",
            new ProjectPaymentAdjustmentCalculator());
    }

    /**
     * <p>
     * Accuracy test method for <code>ProjectPaymentAdjustmentCalculator(String, String)</code>.
     * </p>
     */
    @Test
    public void testCtor2() {
        Assert.assertNotNull("Unable to instantiate ProjectPaymentAdjustmentCalculator",
            new ProjectPaymentAdjustmentCalculator(TestHelper.CONFIG_FILE, ProjectPaymentAdjustmentCalculator.class
                .getName()));
    }

    /**
     * <p>
     * Failure test method for
     * <code>ProjectPaymentAdjustmentCalculator#ProjectPaymentAdjustmentCalculator(String, String)</code> when
     * <code>configFilePath</code> is <code>null</code>.
     * </p>
     * <p>
     * Expects <code>IllegalArgumentException</code> is thrown.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCtor2_NullConfigFilePath() {
        new ProjectPaymentAdjustmentCalculator(null, ProjectPaymentAdjustmentCalculator.class.getName());
    }

    /**
     * <p>
     * Failure test method for
     * <code>ProjectPaymentAdjustmentCalculator#ProjectPaymentAdjustmentCalculator(String, String)</code> when
     * <code>configFilePath</code> is empty.
     * </p>
     * <p>
     * Expects <code>IllegalArgumentException</code> is thrown.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCtor2_EmptyConfigFilePath() {
        new ProjectPaymentAdjustmentCalculator(" ", ProjectPaymentAdjustmentCalculator.class.getName());
    }

    /**
     * <p>
     * Failure test method for
     * <code>ProjectPaymentAdjustmentCalculator#ProjectPaymentAdjustmentCalculator(String, String)</code> when
     * <code>namespace</code> is <code>null</code>.
     * </p>
     * <p>
     * Expects <code>IllegalArgumentException</code> is thrown.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCtor2_NullNamespace() {
        new ProjectPaymentAdjustmentCalculator(TestHelper.CONFIG_FILE, null);
    }

    /**
     * <p>
     * Failure test method for
     * <code>ProjectPaymentAdjustmentCalculator#ProjectPaymentAdjustmentCalculator(String, String)</code> when
     * <code>namespace</code> is empty.
     * </p>
     * <p>
     * Expects <code>IllegalArgumentException</code> is thrown.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCtor2_EmptyNamespace() {
        new ProjectPaymentAdjustmentCalculator(TestHelper.CONFIG_FILE, " ");
    }

    /**
     * <p>
     * Failure test method for
     * <code>ProjectPaymentAdjustmentCalculator#ProjectPaymentAdjustmentCalculator(String, String)</code> when
     * configuration error occurs.
     * </p>
     * <p>
     * Expects <code>ProjectPaymentCalculatorConfigurationException</code> is thrown.
     * </p>
     */
    @Test(expected = ProjectPaymentCalculatorConfigurationException.class)
    public void testCtor2_ConfigError() {
        new ProjectPaymentAdjustmentCalculator("test_files" + File.separator + "unknown.properties",
            ProjectPaymentAdjustmentCalculator.class.getName());
    }

    /**
     * <p>
     * Accuracy test method for
     * <code>ProjectPaymentAdjustmentCalculator#ProjectPaymentAdjustmentCalculator(ConfigurationObject)</code>.
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    @Test
    public void testCtor3() throws Exception {
        Assert.assertNotNull("Unable to instantiate ProjectPaymentAdjustmentCalculator", calc);

        Assert.assertTrue("Incorrect calculator type",
            TestHelper.getFieldValue(calc, "projectPaymentCalculator") instanceof DefaultProjectPaymentCalculator);
    }

    /**
     * <p>
     * Failure test method for
     * <code>ProjectPaymentAdjustmentCalculator#ProjectPaymentAdjustmentCalculator(ConfigurationObject)</code> when
     * <code>configurationObject</code> is <code>null</code>.
     * </p>
     * <p>
     * Expects <code>IllegalArgumentException</code> is thrown.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCtor3_NullConfigurationObject() {
        new ProjectPaymentAdjustmentCalculator((ConfigurationObject) null);
    }

    /**
     * <p>
     * Failure test method for
     * <code>ProjectPaymentAdjustmentCalculator#ProjectPaymentAdjustmentCalculator(ConfigurationObject)</code> when
     * object_factory_config child is not found in <code>configurationObject</code>.
     * </p>
     * <p>
     * Expects <code>ProjectPaymentCalculatorConfigurationException</code> is thrown.
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    @Test(expected = ProjectPaymentCalculatorConfigurationException.class)
    public void testCtor3_MissingObjectFactoryConfig() throws Exception {
        config.removeChild("object_factory_config");
        new ProjectPaymentAdjustmentCalculator(config);
    }

    /**
     * <p>
     * Failure test method for
     * <code>ProjectPaymentAdjustmentCalculator#ProjectPaymentAdjustmentCalculator(ConfigurationObject)</code> when
     * project_payment_calculator_key property is not found in <code>configurationObject</code>.
     * </p>
     * <p>
     * Expects <code>ProjectPaymentCalculatorConfigurationException</code> is thrown.
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    @Test(expected = ProjectPaymentCalculatorConfigurationException.class)
    public void testCtor3_MissingProjectPaymentCalculatorKey() throws Exception {
        config.removeProperty("project_payment_calculator_key");
        new ProjectPaymentAdjustmentCalculator(config);
    }

    /**
     * <p>
     * Failure test method for
     * <code>ProjectPaymentAdjustmentCalculator#ProjectPaymentAdjustmentCalculator(ConfigurationObject)</code> when
     * project_payment_calculator_key property in <code>configurationObject</code> is empty.
     * </p>
     * <p>
     * Expects <code>ProjectPaymentCalculatorConfigurationException</code> is thrown.
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    @Test(expected = ProjectPaymentCalculatorConfigurationException.class)
    public void testCtor3_EmptyProjectPaymentCalculatorKey() throws Exception {
        config.setPropertyValue("project_payment_calculator_key", " ");
        new ProjectPaymentAdjustmentCalculator(config);
    }

    /**
     * <p>
     * Failure test method for
     * <code>ProjectPaymentAdjustmentCalculator#ProjectPaymentAdjustmentCalculator(ConfigurationObject)</code> when
     * configuration error occurs.
     * </p>
     * <p>
     * Expects <code>ProjectPaymentCalculatorConfigurationException</code> is thrown.
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    @Test(expected = ProjectPaymentCalculatorConfigurationException.class)
    public void testCtor3_ConfigError() throws Exception {
        config.removeChild("db_connection_factory_config");
        new ProjectPaymentAdjustmentCalculator(config);
    }

    /**
     * <p>
     * Failure test method for
     * <code>ProjectPaymentAdjustmentCalculator#ProjectPaymentAdjustmentCalculator(ConfigurationObject)</code> when
     * the configured project payment calculator in <code>configurationObject</code> is not in type of
     * ProjectPaymentCalculator.
     * </p>
     * <p>
     * Expects <code>ProjectPaymentCalculatorConfigurationException</code> is thrown.
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    @Test(expected = ProjectPaymentCalculatorConfigurationException.class)
    public void testCtor3_InvalidType() throws Exception {
        String objectKey = (String) config.getPropertyValue("project_payment_calculator_key");
        ConfigurationObject ofConfig = config.getChild("object_factory_config");
        ConfigurationObject objectConfig = ofConfig.getChild(objectKey);
        objectConfig.setPropertyValue("type", String.class.getName());
        new ProjectPaymentAdjustmentCalculator(config);
    }

    /**
     * <p>
     * Failure test method for
     * <code>ProjectPaymentAdjustmentCalculator#ProjectPaymentAdjustmentCalculator(ConfigurationObject)</code> when
     * the configured project payment calculator key in <code>configurationObject</code> is not defined in object
     * factory configuration.
     * </p>
     * <p>
     * Expects <code>ProjectPaymentCalculatorConfigurationException</code> is thrown.
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    @Test(expected = ProjectPaymentCalculatorConfigurationException.class)
    public void testCtor3_UnknownKey() throws Exception {
        config.setPropertyValue("project_payment_calculator_key", "unknown");
        new ProjectPaymentAdjustmentCalculator(config);
    }

    /**
     * <p>
     * Failure test method for
     * <code>ProjectPaymentAdjustmentCalculator#ProjectPaymentAdjustmentCalculator(ConfigurationObject)</code> when
     * the configured project payment calculator in object factory configuration is not valid.
     * </p>
     * <p>
     * Expects <code>ProjectPaymentCalculatorConfigurationException</code> is thrown.
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    @Test(expected = ProjectPaymentCalculatorConfigurationException.class)
    public void testCtor3_IllegalReference() throws Exception {
        String objectKey = (String) config.getPropertyValue("project_payment_calculator_key");
        ConfigurationObject ofConfig = config.getChild("object_factory_config");
        ConfigurationObject objectConfig = ofConfig.getChild(objectKey);
        objectConfig.removeProperty("type");
        new ProjectPaymentAdjustmentCalculator(config);
    }

    /**
     * <p>
     * Failure test method for
     * <code>ProjectPaymentAdjustmentCalculator#ProjectPaymentAdjustmentCalculator(ConfigurationObject)</code> when
     * there is invalid specification for object factory.
     * </p>
     * <p>
     * Expects <code>ProjectPaymentCalculatorConfigurationException</code> is thrown.
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    @Test(expected = ProjectPaymentCalculatorConfigurationException.class)
    public void testCtor3_SpecificationError() throws Exception {
        String objectKey = (String) config.getPropertyValue("project_payment_calculator_key");
        ConfigurationObject ofConfig = config.getChild("object_factory_config");
        ConfigurationObject objectConfig = ofConfig.getChild(objectKey);
        objectConfig.setPropertyValue("type", new Integer(23));
        new ProjectPaymentAdjustmentCalculator(config);
    }

    /**
     * <p>
     * Accuracy test method for
     * <code>ProjectPaymentAdjustmentCalculator#ProjectPaymentAdjustmentCalculator(ProjectPaymentCalculator)</code>
     * .
     * </p>
     */
    @Test
    public void testCtor4() {
        ProjectPaymentCalculator calculator = new DefaultProjectPaymentCalculator();
        Assert.assertNotNull("Unable to instantiate ProjectPaymentAdjustmentCalculator",
            new ProjectPaymentAdjustmentCalculator(calculator));
    }

    /**
     * <p>
     * Failure test method for
     * <code>ProjectPaymentAdjustmentCalculator#ProjectPaymentAdjustmentCalculator(ProjectPaymentCalculator)</code>
     * when <code>projectPaymentCalculator</code> is <code>null</code>.
     * </p>
     * <p>
     * Expects <code>IllegalArgumentException</code> is thrown.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCtor4_NullProjectPaymentCalculator() {
        Assert.assertNotNull("Unable to instantiate ProjectPaymentAdjustmentCalculator",
            new ProjectPaymentAdjustmentCalculator((ProjectPaymentCalculator) null));
    }

    /**
     * <p>
     * Accuracy test method for
     * <code>ProjectPaymentAdjustmentCalculator(ProjectPaymentCalculator, String, String)</code>.
     * </p>
     */
    @Test
    public void testCtor5() {
        ProjectPaymentCalculator calculator = new DefaultProjectPaymentCalculator();
        Assert.assertNotNull("Unable to instantiate ProjectPaymentAdjustmentCalculator",
            new ProjectPaymentAdjustmentCalculator(calculator, TestHelper.CONFIG_FILE,
                ProjectPaymentAdjustmentCalculator.class.getName()));
    }

    /**
     * <p>
     * Failure test method for
     * <code>ProjectPaymentAdjustmentCalculator(ProjectPaymentCalculator, String, String)</code> when
     * <code>projectPaymentCalculator</code> is <code>null</code>.
     * </p>
     * <p>
     * Expects <code>IllegalArgumentException</code> is thrown.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCtor5_NullProjectPaymentCalculator() {
        Assert.assertNotNull("Unable to instantiate ProjectPaymentAdjustmentCalculator",
            new ProjectPaymentAdjustmentCalculator(null, TestHelper.CONFIG_FILE,
                ProjectPaymentAdjustmentCalculator.class.getName()));
    }

    /**
     * <p>
     * Failure test method for
     * <code>ProjectPaymentAdjustmentCalculator(ProjectPaymentCalculator, String, String)</code> when
     * <code>configFilePath</code> is <code>null</code>.
     * </p>
     * <p>
     * Expects <code>IllegalArgumentException</code> is thrown.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCtor5_NullConfigFilePath() {
        ProjectPaymentCalculator calculator = new DefaultProjectPaymentCalculator();
        Assert
            .assertNotNull("Unable to instantiate ProjectPaymentAdjustmentCalculator",
                new ProjectPaymentAdjustmentCalculator(calculator, null, ProjectPaymentAdjustmentCalculator.class
                    .getName()));
    }

    /**
     * <p>
     * Failure test method for
     * <code>ProjectPaymentAdjustmentCalculator(ProjectPaymentCalculator, String, String)</code> when
     * <code>configFilePath</code> is empty.
     * </p>
     * <p>
     * Expects <code>IllegalArgumentException</code> is thrown.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCtor5_EmptyConfigFilePath() {
        ProjectPaymentCalculator calculator = new DefaultProjectPaymentCalculator();
        Assert
            .assertNotNull("Unable to instantiate ProjectPaymentAdjustmentCalculator",
                new ProjectPaymentAdjustmentCalculator(calculator, "  ", ProjectPaymentAdjustmentCalculator.class
                    .getName()));
    }

    /**
     * <p>
     * Failure test method for
     * <code>ProjectPaymentAdjustmentCalculator(ProjectPaymentCalculator, String, String)</code> when
     * <code>namespace</code> is <code>null</code>.
     * </p>
     * <p>
     * Expects <code>IllegalArgumentException</code> is thrown.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCtor5_NullNamespace() {
        ProjectPaymentCalculator calculator = new DefaultProjectPaymentCalculator();
        Assert.assertNotNull("Unable to instantiate ProjectPaymentAdjustmentCalculator",
            new ProjectPaymentAdjustmentCalculator(calculator, TestHelper.CONFIG_FILE, null));
    }

    /**
     * <p>
     * Failure test method for
     * <code>ProjectPaymentAdjustmentCalculator(ProjectPaymentCalculator, String, String)</code> when
     * <code>namespace</code> is empty.
     * </p>
     * <p>
     * Expects <code>IllegalArgumentException</code> is thrown.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCtor5_EmptyNamespace() {
        ProjectPaymentCalculator calculator = new DefaultProjectPaymentCalculator();
        Assert.assertNotNull("Unable to instantiate ProjectPaymentAdjustmentCalculator",
            new ProjectPaymentAdjustmentCalculator(calculator, TestHelper.CONFIG_FILE, " "));
    }

    /**
     * <p>
     * Accuracy test method for
     * <code>ProjectPaymentAdjustmentCalculator(ProjectPaymentCalculator, ConfigurationObject)</code>.
     * </p>
     */
    @Test
    public void testCtor6() {
        ProjectPaymentCalculator calculator = new DefaultProjectPaymentCalculator();
        Assert.assertNotNull("Unable to instantiate ProjectPaymentAdjustmentCalculator",
            new ProjectPaymentAdjustmentCalculator(calculator, config));
    }

    /**
     * <p>
     * Failure test method for
     * <code>ProjectPaymentAdjustmentCalculator(ProjectPaymentCalculator, ConfigurationObject)</code> when
     * <code>projectPaymentCalculator</code> is <code>null</code>.
     * </p>
     * <p>
     * Expects <code>IllegalArgumentException</code> is thrown.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCtor6_NullProjectPaymentCalculator() {
        Assert.assertNotNull("Unable to instantiate ProjectPaymentAdjustmentCalculator",
            new ProjectPaymentAdjustmentCalculator(null, config));
    }

    /**
     * <p>
     * Failure test method for
     * <code>ProjectPaymentAdjustmentCalculator(ProjectPaymentCalculator, ConfigurationObject)</code> when
     * <code>configurationObject</code> is <code>null</code>.
     * </p>
     * <p>
     * Expects <code>IllegalArgumentException</code> is thrown.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCtor6_NullConfigurationObject() {
        ProjectPaymentCalculator calculator = new DefaultProjectPaymentCalculator();
        Assert.assertNotNull("Unable to instantiate ProjectPaymentAdjustmentCalculator",
            new ProjectPaymentAdjustmentCalculator(calculator, null));
    }

    /**
     * <p>
     * Failure test method for
     * <code>ProjectPaymentAdjustmentCalculator(ProjectPaymentCalculator, ConfigurationObject)</code> when
     * configuration error occurs.
     * </p>
     * <p>
     * Expects <code>ProjectPaymentCalculatorConfigurationException</code> is thrown.
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    @Test(expected = ProjectPaymentCalculatorConfigurationException.class)
    public void testCtor6_ConfigError() throws Exception {
        config.removeChild("db_connection_factory_config");
        new ProjectPaymentAdjustmentCalculator(new DefaultProjectPaymentCalculator(), config);
    }

    /**
     * <p>
     * Accuracy test method for {@link ProjectPaymentAdjustmentCalculator#getDefaultPayments(long, java.util.List)}
     * for Primary Screener (2), Reviewer (4), Accuracy Reviewer (5), Final Reviewer (9) and Aggregator (8).
     * </p>
     * <p>
     * Table <code>default_project_payment</code>
     *
     * <pre>
     * ------------------------------------------------------------------------------------------------------
     * | project_category_id | resource_role_id | fixed_amount | base_coefficient | incremental_coefficient |
     * ------------------------------------------------------------------------------------------------------
     * |          1          |         2        |       0      |       0.00       |           0.01          |
     * |          1          |         4        |       0      |       0.12       |           0.05          |
     * |          1          |         8        |      10      |       0.00       |           0.00          |
     * |          1          |         9        |       0      |       0.05       |           0.00          |
     * ------------------------------------------------------------------------------------------------------
     * </pre>
     *
     * </p>
     * <p>
     * Table <code>project</code>
     *
     * <pre>
     * ------------------------------------
     * | project_id | project_category_id |
     * ------------------------------------
     * |    230     |          1          |
     * ------------------------------------
     * </pre>
     *
     * </p>
     * <p>
     * Table <code>prize</code>
     *
     * <pre>
     * -----------------------------------------------------
     * | project_id | prize_type_id | place | prize_amount |
     * -----------------------------------------------------
     * |     230    |      15       |   1   |      500     |
     * -----------------------------------------------------
     * </pre>
     *
     * </p>
     * <p>
     * Table <code>submission</code>
     *
     * <pre>
     * ---------------------------------------------------------
     * | submission_type_id | upload_id | submission_status_id |
     * ---------------------------------------------------------
     * |          1         |    500    |          1           |
     * |          1         |    623    |          2           |
     * ---------------------------------------------------------
     * </pre>
     *
     * </p>
     * <p>
     * Table <code>upload</code>
     *
     * <pre>
     * -------------------------------------------
     * | project_id | upload_type_id | upload_id |
     * -------------------------------------------
     * |    230     |       1        |    500    |
     * |    230     |       1        |    623    |
     * -------------------------------------------
     * </pre>
     *
     * </p>
     * <p>
     * Table <code>project_payment_adjustment</code>
     *
     * <pre>
     * -------------------------------------------------------------
     * | project_id | resource_role_id | fixed_amount | multiplier |
     * -------------------------------------------------------------
     * |    230     |         2        |    14.00     |    NULL    |
     * |    230     |         4        |    22.00     |    NULL    |
     * |    230     |         5        |    NULL      |    3.00    |
     * |    230     |         8        |    NULL      |    2.00    |
     * |    230     |         9        |    NULL      |    NULL    |
     * -------------------------------------------------------------
     * </pre>
     *
     * </p>
     * <p>
     * For Primary Screener role (2), it has two uploads so the the default payment will be:
     *
     * <pre>
     * 0.00 + (0.00 + 0.01 * 2) * 500 = 10
     * </pre>
     *
     * After adjustment it will be: 14.00
     * </p>
     * <p>
     * For Reviewer role (4), there is only one uploaded submission with status != 2 so the default payment will
     * be:
     *
     * <pre>
     * 0.00 + (0.12 + 0.05 * 1) * 500 = 85
     * </pre>
     *
     * After adjustment it will be: 22.00
     * </p>
     * <p>
     * For Accuracy Reviewer role (5), it will have no default payments since this role is not associated to the
     * project
     * </p>
     * <p>
     * For Aggregator role (8), the submission count is not needed (0).
     *
     * <pre>
     * 10.00 + (0.0 + 0.00 * 0) * 500 = 10
     * </pre>
     *
     * After adjustment it will be: 2 * 10 = 20
     * </p>
     * <p>
     * For Final Reviewer role (9), the submission count is not needed (0).
     *
     * <pre>
     * 00.00 + (0.05 + 0.00 * 0) * 500 = 25
     * </pre>
     *
     * No adjustment is done for this role.
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    @Test
    public void testGetDefaultPayments() throws Exception {
        Map<Long, BigDecimal> payments =
            calc.getDefaultPayments(230, Arrays.asList(
                DefaultProjectPaymentCalculator.PRIMARY_SCREENER_RESOURCE_ROLE_ID,
                DefaultProjectPaymentCalculator.REVIEWER_RESOURCE_ROLE_ID,
                DefaultProjectPaymentCalculator.FINAL_REVIEWER_RESOURCE_ROLE_ID,
                DefaultProjectPaymentCalculator.AGGREGATOR_RESOURCE_ROLE_ID,
                DefaultProjectPaymentCalculator.ACCURACY_REVIEWER_RESOURCE_ROLE_ID));

        // verify
        Assert.assertEquals("Incorrect number of payments", 4, payments.size());
        Assert.assertEquals("Incorrect payments for Primary Screener", 14.0, payments.get(
            DefaultProjectPaymentCalculator.PRIMARY_SCREENER_RESOURCE_ROLE_ID).doubleValue(), 0);
        Assert.assertEquals("Incorrect payments for Reviewer", 22.0, payments.get(
            DefaultProjectPaymentCalculator.REVIEWER_RESOURCE_ROLE_ID).doubleValue(), 0);
        Assert.assertNull("Incorrect payments for Accuracy Reviewer", payments
            .get(DefaultProjectPaymentCalculator.ACCURACY_REVIEWER_RESOURCE_ROLE_ID));
        Assert.assertEquals("Incorrect payments for Aggregator", 20.0, payments.get(
            DefaultProjectPaymentCalculator.AGGREGATOR_RESOURCE_ROLE_ID).doubleValue(), 0);
        Assert.assertEquals("Incorrect payments for Final Reviewer", 25.0, payments.get(
            DefaultProjectPaymentCalculator.FINAL_REVIEWER_RESOURCE_ROLE_ID).doubleValue(), 0);
    }

    /**
     * <p>
     * Failure test method for {@link ProjectPaymentAdjustmentCalculator#getDefaultPayments(long, java.util.List)}
     * when <code>projectId</code> is zero.
     * </p>
     * <p>
     * Expects <code>IllegalArgumentException</code> is thrown.
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetDefaultPayments_ZeroProjectId() throws Exception {
        calc.getDefaultPayments(0, Arrays.asList(DefaultProjectPaymentCalculator.PRIMARY_SCREENER_RESOURCE_ROLE_ID,
            DefaultProjectPaymentCalculator.REVIEWER_RESOURCE_ROLE_ID,
            DefaultProjectPaymentCalculator.FINAL_REVIEWER_RESOURCE_ROLE_ID,
            DefaultProjectPaymentCalculator.AGGREGATOR_RESOURCE_ROLE_ID,
            DefaultProjectPaymentCalculator.ACCURACY_REVIEWER_RESOURCE_ROLE_ID));
    }

    /**
     * <p>
     * Failure test method for {@link ProjectPaymentAdjustmentCalculator#getDefaultPayments(long, java.util.List)}
     * when <code>projectId</code> is negative.
     * </p>
     * <p>
     * Expects <code>IllegalArgumentException</code> is thrown.
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetDefaultPayments_NegativeProjectId() throws Exception {
        calc.getDefaultPayments(-20, Arrays.asList(DefaultProjectPaymentCalculator.PRIMARY_SCREENER_RESOURCE_ROLE_ID,
            DefaultProjectPaymentCalculator.REVIEWER_RESOURCE_ROLE_ID,
            DefaultProjectPaymentCalculator.FINAL_REVIEWER_RESOURCE_ROLE_ID,
            DefaultProjectPaymentCalculator.AGGREGATOR_RESOURCE_ROLE_ID,
            DefaultProjectPaymentCalculator.ACCURACY_REVIEWER_RESOURCE_ROLE_ID));
    }

    /**
     * <p>
     * Failure test method for {@link ProjectPaymentAdjustmentCalculator#getDefaultPayments(long, java.util.List)}
     * when <code>resourceRoleIDs</code> is <code>null</code>.
     * </p>
     * <p>
     * Expects <code>IllegalArgumentException</code> is thrown.
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetDefaultPayments_NullResourceRoleIDs() throws Exception {
        calc.getDefaultPayments(230, null);
    }

    /**
     * <p>
     * Failure test method for {@link ProjectPaymentAdjustmentCalculator#getDefaultPayments(long, java.util.List)}
     * when <code>resourceRoleIDs</code> is empty.
     * </p>
     * <p>
     * Expects <code>IllegalArgumentException</code> is thrown.
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetDefaultPayments_EmptyResourceRoleIDs() throws Exception {
        calc.getDefaultPayments(230, new ArrayList<Long>());
    }

    /**
     * <p>
     * Failure test method for {@link ProjectPaymentAdjustmentCalculator#getDefaultPayments(long, java.util.List)}
     * when <code>resourceRoleIDs</code> contains <code>null</code>.
     * </p>
     * <p>
     * Expects <code>IllegalArgumentException</code> is thrown.
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetDefaultPayments_ResourceRoleIDsContainsNull() throws Exception {
        calc.getDefaultPayments(230, Arrays.asList(2L, null));
    }
}
