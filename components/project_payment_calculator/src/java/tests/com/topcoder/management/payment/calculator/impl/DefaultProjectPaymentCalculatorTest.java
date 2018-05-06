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
import com.topcoder.management.payment.calculator.ProjectPaymentCalculatorConfigurationException;
import com.topcoder.management.payment.calculator.TestHelper;


/**
 * <p>
 * Unit test case of {@link DefaultProjectPaymentCalculator}.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class DefaultProjectPaymentCalculatorTest {
    /**
     * <p>
     * Represents the ConfigurationObject instance used for testing.
     * </p>
     */
    private ConfigurationObject config;

    /**
     * <p>
     * Represents DefaultProjectPaymentCalculator instance to test against.
     * </p>
     */
    private DefaultProjectPaymentCalculator calc;

    /**
     * <p>
     * Creates a test suite for the tests in this test case.
     * </p>
     *
     * @return a Test suite for this test case.
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(DefaultProjectPaymentCalculatorTest.class);
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

        ConfigurationObject root = cfm.getConfiguration(DefaultProjectPaymentCalculator.class.getName());
        config = root.getChild(DefaultProjectPaymentCalculator.class.getName());

        calc = new DefaultProjectPaymentCalculator(config);
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
     * Accuracy test method for {@link DefaultProjectPaymentCalculator#DefaultProjectPaymentCalculator()}.
     * </p>
     */
    @Test
    public void testCtor1() {
        Assert.assertNotNull("Unable to instantiate DefaultProjectPaymentCalculator",
            new DefaultProjectPaymentCalculator());
    }

    /**
     * <p>
     * Accuracy test method for
     * {@link DefaultProjectPaymentCalculator#DefaultProjectPaymentCalculator(java.lang.String, java.lang.String)}.
     * </p>
     */
    @Test
    public void testCtor2() {
        Assert
            .assertNotNull("Unable to instantiate DefaultProjectPaymentCalculator",
                new DefaultProjectPaymentCalculator(TestHelper.CONFIG_FILE, DefaultProjectPaymentCalculator.class
                    .getName()));
    }

    /**
     * <p>
     * Failure test method for
     * {@link DefaultProjectPaymentCalculator#DefaultProjectPaymentCalculator(java.lang.String, java.lang.String)}
     * when <code>configFilePath</code> is <code>null</code>.
     * </p>
     * <p>
     * Expects <code>IllegalArgumentException</code> is thrown.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCtor2_NullConfigFilePath() {
        new DefaultProjectPaymentCalculator(null, DefaultProjectPaymentCalculator.class.getName());
    }

    /**
     * <p>
     * Failure test method for
     * {@link DefaultProjectPaymentCalculator#DefaultProjectPaymentCalculator(java.lang.String, java.lang.String)}
     * when <code>configFilePath</code> is empty.
     * </p>
     * <p>
     * Expects <code>IllegalArgumentException</code> is thrown.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCtor2_EmptyConfigFilePath() {
        new DefaultProjectPaymentCalculator(" ", DefaultProjectPaymentCalculator.class.getName());
    }

    /**
     * <p>
     * Failure test method for
     * {@link DefaultProjectPaymentCalculator#DefaultProjectPaymentCalculator(java.lang.String, java.lang.String)}
     * when <code>namespace</code> is <code>null</code>.
     * </p>
     * <p>
     * Expects <code>IllegalArgumentException</code> is thrown.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCtor2_NullNamespace() {
        new DefaultProjectPaymentCalculator(TestHelper.CONFIG_FILE, null);
    }

    /**
     * <p>
     * Failure test method for
     * {@link DefaultProjectPaymentCalculator#DefaultProjectPaymentCalculator(java.lang.String, java.lang.String)}
     * when <code>namespace</code> is empty.
     * </p>
     * <p>
     * Expects <code>IllegalArgumentException</code> is thrown.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCtor2_EmptyNamespace() {
        new DefaultProjectPaymentCalculator(TestHelper.CONFIG_FILE, " ");
    }

    /**
     * <p>
     * Failure test method for
     * {@link DefaultProjectPaymentCalculator#DefaultProjectPaymentCalculator(java.lang.String, java.lang.String)}
     * when configuration error occurs.
     * </p>
     * <p>
     * Expects <code>ProjectPaymentCalculatorConfigurationException</code> is thrown.
     * </p>
     */
    @Test(expected = ProjectPaymentCalculatorConfigurationException.class)
    public void testCtor2_ConfigError() {
        new DefaultProjectPaymentCalculator("test_files" + File.separator + "unknown.properties",
            DefaultProjectPaymentCalculator.class.getName());
    }

    /**
     * <p>
     * Accuracy test method for
     * <code>DefaultProjectPaymentCalculator#DefaultProjectPaymentCalculator(ConfigurationObject)</code>.
     * </p>
     */
    @Test
    public void testCtor3() {
        Assert.assertNotNull("Unable to instantiate DefaultProjectPaymentCalculator", calc);
    }

    /**
     * <p>
     * Failure test method for
     * <code>DefaultProjectPaymentCalculator#DefaultProjectPaymentCalculator(ConfigurationObject)</code> when
     * <code>configurationObject</code> is <code>null</code>.
     * </p>
     * <p>
     * Expects <code>IllegalArgumentException</code> is thrown.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCtor3_NullConfigurationObject() {
        new DefaultProjectPaymentCalculator(null);
    }

    /**
     * <p>
     * Failure test method for
     * <code>DefaultProjectPaymentCalculator#DefaultProjectPaymentCalculator(ConfigurationObject)</code> when
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
        new DefaultProjectPaymentCalculator(config);
    }

    /**
     * <p>
     * Accuracy test method for {@link DefaultProjectPaymentCalculator#getDefaultPayments(long, java.util.List)}
     * when resource role IDs to compute are Primary Screener and Reviewer.
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
     * For Primary Screener role (2), it has two uploads so the the expected payment will be:
     *
     * <pre>
     * 0.00 + (0.00 + 0.01 * 2) * 500 = 10
     * </pre>
     *
     * </p>
     * <p>
     * For Reviewer role (4), there is only one uploaded submission with status != 2 so the expected payment will
     * be:
     *
     * <pre>
     * 0.00 + (0.12 + 0.05 * 1) * 500 = 85
     * </pre>
     *
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    @Test
    public void testGetDefaultPayments1() throws Exception {
        // get default payments
        Map<Long, BigDecimal> payments =
            calc.getDefaultPayments(230, Arrays.asList(
                DefaultProjectPaymentCalculator.PRIMARY_SCREENER_RESOURCE_ROLE_ID,
                DefaultProjectPaymentCalculator.REVIEWER_RESOURCE_ROLE_ID));

        // verify
        Assert.assertEquals("Incorrect number of payments", 2, payments.size());
        Assert.assertEquals("Incorrect payments for Primary Screener", 10.0, payments.get(
            DefaultProjectPaymentCalculator.PRIMARY_SCREENER_RESOURCE_ROLE_ID).doubleValue(), 0);
        Assert.assertEquals("Incorrect payments for Reviewer", 85.0, payments.get(
            DefaultProjectPaymentCalculator.REVIEWER_RESOURCE_ROLE_ID).doubleValue(), 0);
    }

    /**
     * <p>
     * Accuracy test method for {@link DefaultProjectPaymentCalculator#getDefaultPayments(long, java.util.List)}
     * when resource role IDs to compute are Accuracy Reviewer, Failure Reviewer and Stress Reviewer.
     * </p>
     * <p>
     * Table <code>default_project_payment</code>
     *
     * <pre>
     * ------------------------------------------------------------------------------------------------------
     * | project_category_id | resource_role_id | fixed_amount | base_coefficient | incremental_coefficient |
     * ------------------------------------------------------------------------------------------------------
     * |          2          |         5        |       0      |       0.2       |           0.05          |
     * |          2          |         6        |       0      |       0.2       |           0.05          |
     * |          2          |         7        |       0      |       0.2       |           0.05          |
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
     * |    231     |          2          |
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
     * |     231    |      15       |   1   |      400     |
     * -----------------------------------------------------
     * </pre>
     *
     * </p>
     * <p>
     * For all reviewer roles (5, 6, 7), there is no uploaded submissions for the given project id, so the
     * submission count is 1.
     *
     * <pre>
     * 0.00 + (0.2 + 0.05 * 1) * 400 = 100
     * </pre>
     *
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    @Test
    public void testGetDefaultPayments2() throws Exception {
        // get default payments
        Map<Long, BigDecimal> payments =
            calc.getDefaultPayments(231, Arrays.asList(
                DefaultProjectPaymentCalculator.ACCURACY_REVIEWER_RESOURCE_ROLE_ID,
                DefaultProjectPaymentCalculator.FAILURE_REVIEWER_RESOURCE_ROLE_ID,
                DefaultProjectPaymentCalculator.STRESS_REVIEWER_RESOURCE_ROLE_ID));

        // verify
        Assert.assertEquals("Incorrect number of payments", 3, payments.size());
        Assert.assertEquals("Incorrect payments for Accuracy Reviewer", 100.0, payments.get(
            DefaultProjectPaymentCalculator.ACCURACY_REVIEWER_RESOURCE_ROLE_ID).doubleValue(), 0);
        Assert.assertEquals("Incorrect payments for Failure Reviewer", 100.0, payments.get(
            DefaultProjectPaymentCalculator.FAILURE_REVIEWER_RESOURCE_ROLE_ID).doubleValue(), 0);
        Assert.assertEquals("Incorrect payments for Stress Reviewer", 100.0, payments.get(
            DefaultProjectPaymentCalculator.STRESS_REVIEWER_RESOURCE_ROLE_ID).doubleValue(), 0);
    }

    /**
     * <p>
     * Accuracy test method for {@link DefaultProjectPaymentCalculator#getDefaultPayments(long, java.util.List)}
     * when resource role IDs to compute are Milestone Screener and Milestone Reviewer.
     * </p>
     * <p>
     * Table <code>default_project_payment</code>
     *
     * <pre>
     * ------------------------------------------------------------------------------------------------------
     * | project_category_id | resource_role_id | fixed_amount | base_coefficient | incremental_coefficient |
     * ------------------------------------------------------------------------------------------------------
     * |          2          |        19        |      10      |       0.01       |           0.02          |
     * |          2          |        20        |      15      |       0.01       |           0.03          |
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
     * |    231     |          2          |
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
     * |     231    |      15       |   1   |      400     |
     * -----------------------------------------------------
     * </pre>
     *
     * </p>
     * <p>
     * For these roles (19, 20), there is no uploaded submissions for the given project id, so the submission count
     * is 1. For milestone screener:
     *
     * <pre>
     * 10.00 + (0.01 + 0.02 * 1) * 400 = 22
     * </pre>
     *
     * For milestone reviewer:
     *
     * <pre>
     * 15.00 + (0.01 + 0.03 * 1) * 400 = 31
     * </pre>
     *
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    @Test
    public void testGetDefaultPayments3() throws Exception {
        // get default payments
        Map<Long, BigDecimal> payments =
            calc.getDefaultPayments(231, Arrays.asList(
                DefaultProjectPaymentCalculator.MILESTONE_SCREENER_RESOURCE_ROLE_ID,
                DefaultProjectPaymentCalculator.MILESTONE_REVIEWER_RESOURCE_ROLE_ID));

        // verify
        Assert.assertEquals("Incorrect number of payments", 2, payments.size());
        Assert.assertEquals("Incorrect payments for Milestone Screener", 22.0, payments.get(
            DefaultProjectPaymentCalculator.MILESTONE_SCREENER_RESOURCE_ROLE_ID).doubleValue(), 0);
        Assert.assertEquals("Incorrect payments for Milestone Reviewer", 31.0, payments.get(
            DefaultProjectPaymentCalculator.MILESTONE_REVIEWER_RESOURCE_ROLE_ID).doubleValue(), 0);
    }

    /**
     * <p>
     * Accuracy test method for {@link DefaultProjectPaymentCalculator#getDefaultPayments(long, java.util.List)}
     * when resource role IDs to compute are Primary Screener, Reviewer and Specification Reviewer and there is no
     * uploaded submission.
     * </p>
     * <p>
     * Table <code>default_project_payment</code>
     *
     * <pre>
     * ------------------------------------------------------------------------------------------------------
     * | project_category_id | resource_role_id | fixed_amount | base_coefficient | incremental_coefficient |
     * ------------------------------------------------------------------------------------------------------
     * |          2          |         2        |       0      |       0.0       |           0.05           |
     * |          2          |         4        |       0      |       0.2       |           0.05           |
     * |          2          |        18        |      30      |       0.0       |           0.00           |
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
     * |    231     |          2          |
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
     * |     231    |      15       |   1   |      400     |
     * -----------------------------------------------------
     * </pre>
     *
     * </p>
     * <p>
     * For roles 2 and 4, there is no uploaded submissions for the given project id, so the submission count is 1.
     * For role 18, submission is not needed so it is 0.
     * </p>
     * <p>
     * Primary Screener:
     *
     * <pre>
     * 0.00 + (0.0 + 0.05 * 1) * 400 = 8
     * </pre>
     *
     * Reviewer:
     *
     * <pre>
     * 0.00 + (0.2 + 0.05 * 1) * 400 = 100
     * </pre>
     *
     * Specification Reviewer:
     *
     * <pre>
     * 30.00 + (0.0 + 0.00 * 0) * 400 = 30
     * </pre>
     *
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    @Test
    public void testGetDefaultPayments4() throws Exception {
        // get default payments
        Map<Long, BigDecimal> payments =
            calc.getDefaultPayments(231, Arrays.asList(
                DefaultProjectPaymentCalculator.PRIMARY_SCREENER_RESOURCE_ROLE_ID,
                DefaultProjectPaymentCalculator.REVIEWER_RESOURCE_ROLE_ID,
                DefaultProjectPaymentCalculator.SPECIFICATION_REVIEWER_RESOURCE_ROLE_ID));

        // verify
        Assert.assertEquals("Incorrect number of payments", 3, payments.size());
        Assert.assertEquals("Incorrect payments for Primary Screener", 8.0, payments.get(
            DefaultProjectPaymentCalculator.PRIMARY_SCREENER_RESOURCE_ROLE_ID).doubleValue(), 0);
        Assert.assertEquals("Incorrect payments for Reviewer", 100.0, payments.get(
            DefaultProjectPaymentCalculator.REVIEWER_RESOURCE_ROLE_ID).doubleValue(), 0);
        Assert.assertEquals("Incorrect payments for Specification Reviewer", 30.0, payments.get(
            DefaultProjectPaymentCalculator.SPECIFICATION_REVIEWER_RESOURCE_ROLE_ID).doubleValue(), 0);
    }

    /**
     * <p>
     * Accuracy test method for {@link DefaultProjectPaymentCalculator#getDefaultPayments(long, java.util.List)}
     * when there is no record found in database for given <code>projectId</code>.
     * </p>
     * <p>
     * Expects the returned value is an empty map
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    @Test
    public void testGetDefaultPayments5() throws Exception {
        // get default payments
        Map<Long, BigDecimal> payments =
            calc.getDefaultPayments(1, Arrays.asList(DefaultProjectPaymentCalculator.PRIMARY_SCREENER_RESOURCE_ROLE_ID,
                DefaultProjectPaymentCalculator.REVIEWER_RESOURCE_ROLE_ID));

        // verify
        Assert.assertTrue("Should be empty", payments.isEmpty());
    }

    /**
     * <p>
     * Failure test method for {@link DefaultProjectPaymentCalculator#getDefaultPayments(long, java.util.List)}
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
        calc.getDefaultPayments(0, Arrays.asList(2L, 4L));
    }

    /**
     * <p>
     * Failure test method for {@link DefaultProjectPaymentCalculator#getDefaultPayments(long, java.util.List)}
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
        calc.getDefaultPayments(-23, Arrays.asList(2L, 4L));
    }

    /**
     * <p>
     * Failure test method for {@link DefaultProjectPaymentCalculator#getDefaultPayments(long, java.util.List)}
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
     * Failure test method for {@link DefaultProjectPaymentCalculator#getDefaultPayments(long, java.util.List)}
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
     * Failure test method for {@link DefaultProjectPaymentCalculator#getDefaultPayments(long, java.util.List)}
     * when <code>resourceRoleIDs</code> contains null.
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

    /**
     * <p>
     * Accuracy test method for
     * {@link DefaultProjectPaymentCalculator#getDefaultPayment(long, long, java.math.BigDecimal, int)} when
     * <code>prize</code> is 1000 and <code>submissionsCount</code> is 3.
     * </p>
     * <p>
     * Table <code>default_project_payment</code>
     *
     * <pre>
     * ------------------------------------------------------------------------------------------------------
     * | project_category_id | resource_role_id | fixed_amount | base_coefficient | incremental_coefficient |
     * ------------------------------------------------------------------------------------------------------
     * |          1          |         4        |       0      |       0.12      |           0.05           |
     * ------------------------------------------------------------------------------------------------------
     * </pre>
     *
     * </p>
     * <p>
     * Expects the payment will be: 1000 * (0.12 + 0.05 * 3) = 270
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    @Test
    public void testGetDefaultPayment1() throws Exception {
        BigDecimal payment = calc.getDefaultPayment(1, 4, new BigDecimal(1000), 3);
        Assert.assertEquals("Incorrect payment", 270.0, payment.doubleValue(), 0);
    }

    /**
     * <p>
     * Accuracy test method for
     * {@link DefaultProjectPaymentCalculator#getDefaultPayment(long, long, java.math.BigDecimal, int)} when
     * <code>prize</code> is 1000 and <code>submissionsCount</code> is 0.
     * </p>
     * <p>
     * Table <code>default_project_payment</code>
     *
     * <pre>
     * ------------------------------------------------------------------------------------------------------
     * | project_category_id | resource_role_id | fixed_amount | base_coefficient | incremental_coefficient |
     * ------------------------------------------------------------------------------------------------------
     * |          1          |         4        |       0      |       0.12      |           0.05           |
     * ------------------------------------------------------------------------------------------------------
     * </pre>
     *
     * </p>
     * <p>
     * Expects the payment will be: 1000 * (0.12 + 0.05 * 1) = 170
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    @Test
    public void testGetDefaultPayment2() throws Exception {
        BigDecimal payment = calc.getDefaultPayment(1, 4, new BigDecimal(1000), 0);
        Assert.assertEquals("Incorrect payment", 170.0, payment.doubleValue(), 0);
    }

    /**
     * <p>
     * Accuracy test method for
     * {@link DefaultProjectPaymentCalculator#getDefaultPayment(long, long, java.math.BigDecimal, int)} when there
     * is no matching record found in database.
     * </p>
     * <p>
     * Expects the returned value is null.
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    @Test
    public void testGetDefaultPayment_None() throws Exception {
        BigDecimal payment = calc.getDefaultPayment(1, 5, new BigDecimal(1000), 3);
        Assert.assertNull("Incorrect payment", payment);
    }

    /**
     * <p>
     * Failure test method for
     * {@link DefaultProjectPaymentCalculator#getDefaultPayment(long, long, java.math.BigDecimal, int)} when
     * <code>projectCategoryId</code> is zero.
     * </p>
     * <p>
     * Expects <code>IllegalArgumentException</code> is thrown.
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetDefaultPayment_ZeroProjectCategoryId() throws Exception {
        calc.getDefaultPayment(0, 4, new BigDecimal(1000), 3);
    }

    /**
     * <p>
     * Failure test method for
     * {@link DefaultProjectPaymentCalculator#getDefaultPayment(long, long, java.math.BigDecimal, int)} when
     * <code>projectCategoryId</code> is negative.
     * </p>
     * <p>
     * Expects <code>IllegalArgumentException</code> is thrown.
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetDefaultPayment_NegativeProjectCategoryId() throws Exception {
        calc.getDefaultPayment(-20, 4, new BigDecimal(1000), 3);
    }

    /**
     * <p>
     * Failure test method for
     * {@link DefaultProjectPaymentCalculator#getDefaultPayment(long, long, java.math.BigDecimal, int)} when
     * <code>resourceRoleId</code> is zero.
     * </p>
     * <p>
     * Expects <code>IllegalArgumentException</code> is thrown.
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetDefaultPayment_ZeroResourceRoleId() throws Exception {
        calc.getDefaultPayment(1, 0, new BigDecimal(1000), 3);
    }

    /**
     * <p>
     * Failure test method for
     * {@link DefaultProjectPaymentCalculator#getDefaultPayment(long, long, java.math.BigDecimal, int)} when
     * <code>resourceRoleId</code> is negative.
     * </p>
     * <p>
     * Expects <code>IllegalArgumentException</code> is thrown.
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetDefaultPayment_NegativeResourceRoleId() throws Exception {
        calc.getDefaultPayment(1, -3, new BigDecimal(1000), 3);
    }

    /**
     * <p>
     * Failure test method for
     * {@link DefaultProjectPaymentCalculator#getDefaultPayment(long, long, java.math.BigDecimal, int)} when
     * <code>prize</code> is <code>null</code>.
     * </p>
     * <p>
     * Expects <code>IllegalArgumentException</code> is thrown.
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetDefaultPayment_NullPrize() throws Exception {
        calc.getDefaultPayment(1, 4, null, 3);
    }

    /**
     * <p>
     * Failure test method for
     * {@link DefaultProjectPaymentCalculator#getDefaultPayment(long, long, java.math.BigDecimal, int)} when
     * <code>submissionsCount</code> is negative.
     * </p>
     * <p>
     * Expects <code>IllegalArgumentException</code> is thrown.
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetDefaultPayment_NegativeSubmissionsCount() throws Exception {
        calc.getDefaultPayment(1, 4, new BigDecimal(1000), -3);
    }
}
