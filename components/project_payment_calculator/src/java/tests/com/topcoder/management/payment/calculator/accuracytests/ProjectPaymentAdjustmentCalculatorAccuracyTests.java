/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.payment.calculator.accuracytests;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;

import junit.framework.JUnit4TestAdapter;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.persistence.ConfigurationFileManager;
import com.topcoder.management.payment.calculator.ProjectPaymentCalculator;
import com.topcoder.management.payment.calculator.impl.ProjectPaymentAdjustmentCalculator;


/**
 * <p>
 * Accuracy unit test for {@link ProjectPaymentAdjustmentCalculator}.
 * </p>
 *
 * @author lqz
 * @version 1.0
 */
public class ProjectPaymentAdjustmentCalculatorAccuracyTests {
    /**
     * <p>
     * Represents the ProjectPaymentAdjustmentCalculator instance to test.
     * </p>
     */
    private ProjectPaymentAdjustmentCalculator impl;

    /**
     * <p>
     * Adapter for earlier versions of JUnit.
     * </p>
     *
     * @return a test suite.
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(ProjectPaymentAdjustmentCalculatorAccuracyTests.class);
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
        AccuracyHelper.clearDB();
        AccuracyHelper.prepareDB();
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
        AccuracyHelper.clearDB();
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
        impl =
            new ProjectPaymentAdjustmentCalculator("accuracy/ProjectPaymentAdjustmentCalculator.properties",
                "com.topcoder.management.payment.calculator.impl.ProjectPaymentAdjustmentCalculator");
    }

    /**
     * <p>
     * Cleans up the unit tests.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @After
    public void tearDown() throws Exception {
        impl = null;
    }

    /**
     * <p>
     * Accuracy test for {@link ProjectPaymentAdjustmentCalculator#ProjectPaymentAdjustmentCalculator()}.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_ProjectPaymentAdjustmentCalculator() throws Exception {
        impl =
            new ProjectPaymentAdjustmentCalculator("accuracy/ProjectPaymentAdjustmentCalculator.properties",
                "com.topcoder.management.payment.calculator.impl.ProjectPaymentAdjustmentCalculator");
    }

    /**
     * <p>
     * Accuracy test for {@link ProjectPaymentAdjustmentCalculator#ProjectPaymentAdjustmentCalculator()}.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_ProjectPaymentAdjustmentCalculator2() throws Exception {
        impl = new ProjectPaymentAdjustmentCalculator();
    }

    /**
     * <p>
     * Accuracy test for {@link ProjectPaymentAdjustmentCalculator#ProjectPaymentAdjustmentCalculator()}.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_ProjectPaymentAdjustmentCalculator3() throws Exception {

        ConfigurationFileManager configFileManager =
            new ConfigurationFileManager("accuracy/ProjectPaymentAdjustmentCalculator.properties");
        ConfigurationObject configObject =
            configFileManager
                .getConfiguration("com.topcoder.management.payment.calculator.impl.ProjectPaymentAdjustmentCalculator");
        ConfigurationObject config =
            configObject.getChild("com.topcoder.management.payment.calculator.impl.ProjectPaymentAdjustmentCalculator");
        impl = new ProjectPaymentAdjustmentCalculator(config);
    }

    /**
     * <p>
     * Accuracy test for {@link ProjectPaymentAdjustmentCalculator#ProjectPaymentAdjustmentCalculator()}.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_ProjectPaymentAdjustmentCalculator4() throws Exception {
        ProjectPaymentCalculator obj = new ProjectPaymentAdjustmentCalculator();
        impl = new ProjectPaymentAdjustmentCalculator(obj);
    }

    /**
     * <p>
     * Accuracy test for {@link ProjectPaymentAdjustmentCalculator#ProjectPaymentAdjustmentCalculator()}.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_ProjectPaymentAdjustmentCalculator5() throws Exception {
        ProjectPaymentCalculator obj = new ProjectPaymentAdjustmentCalculator();
        impl =
            new ProjectPaymentAdjustmentCalculator(obj, "accuracy/ProjectPaymentAdjustmentCalculator.properties",
                "com.topcoder.management.payment.calculator.impl.ProjectPaymentAdjustmentCalculator");
    }

    /**
     * <p>
     * Accuracy test for {@link ProjectPaymentAdjustmentCalculator#ProjectPaymentAdjustmentCalculator()}.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_ProjectPaymentAdjustmentCalculator6() throws Exception {
        ProjectPaymentCalculator obj = new ProjectPaymentAdjustmentCalculator();
        ConfigurationFileManager configFileManager =
            new ConfigurationFileManager("accuracy/ProjectPaymentAdjustmentCalculator.properties");
        ConfigurationObject configObject =
            configFileManager
                .getConfiguration("com.topcoder.management.payment.calculator.impl.ProjectPaymentAdjustmentCalculator");
        ConfigurationObject config =
            configObject.getChild("com.topcoder.management.payment.calculator.impl.ProjectPaymentAdjustmentCalculator");
        impl = new ProjectPaymentAdjustmentCalculator(obj, config);
    }

    /**
     * <p>
     * Accuracy test for {@link ProjectPaymentAdjustmentCalculator#getDefaultPayments()}.
     * </p>
     * The payment should be fixed.
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_getDefaultPayments2() throws Exception {
        int projectId = 1000;
        Map<Long, BigDecimal> ret =
            impl.getDefaultPayments(projectId, Arrays.asList(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 14L, 18L));
        assertEquals(6, ret.size());

        // the payment for role 2 is directly fixed to 60
        assertEquals(60.0, ret.get(2L).doubleValue(), 1e-10);
        assertEquals(30, ret.get(18L).doubleValue(), 1e-10);
        assertEquals(85, ret.get(4L).doubleValue(), 1e-10);

        // the payment for role 8 is multiplied by 3
        assertEquals(30, ret.get(8L).doubleValue(), 1e-10);
        assertEquals(25, ret.get(9L).doubleValue(), 1e-10);
        assertEquals(300, ret.get(14L).doubleValue(), 1e-10);
    }
}
