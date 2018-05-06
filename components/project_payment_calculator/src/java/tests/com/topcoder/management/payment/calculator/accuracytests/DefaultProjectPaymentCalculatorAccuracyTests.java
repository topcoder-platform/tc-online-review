/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.payment.calculator.accuracytests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
import com.topcoder.management.payment.calculator.impl.DefaultProjectPaymentCalculator;


/**
 * <p>
 * Accuracy unit test for {@link DefaultProjectPaymentCalculator}.
 * </p>
 *
 * @author lqz
 * @version 1.0
 */
public class DefaultProjectPaymentCalculatorAccuracyTests {
    /**
     * <p>
     * Represents the DefaultProjectPaymentCalculator instance to test.
     * </p>
     */
    private DefaultProjectPaymentCalculator impl;

    /**
     * <p>
     * Adapter for earlier versions of JUnit.
     * </p>
     *
     * @return a test suite.
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(DefaultProjectPaymentCalculatorAccuracyTests.class);
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
            new DefaultProjectPaymentCalculator("accuracy/DefaultProjectPaymentCalculator.properties",
                "com.topcoder.management.payment.calculator.impl.DefaultProjectPaymentCalculator");
        AccuracyHelper.clearDB();
        AccuracyHelper.prepareDB();
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
        AccuracyHelper.clearDB();
    }

    /**
     * <p>
     * Accuracy test for {@link DefaultProjectPaymentCalculator#DefaultProjectPaymentCalculator()}.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_DefaultProjectPaymentCalculator() throws Exception {
        impl =
            new DefaultProjectPaymentCalculator("accuracy/DefaultProjectPaymentCalculator.properties",
                "com.topcoder.management.payment.calculator.impl.DefaultProjectPaymentCalculator");
    }

    /**
     * <p>
     * Accuracy test for {@link DefaultProjectPaymentCalculator#DefaultProjectPaymentCalculator()}.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_DefaultProjectPaymentCalculator2() throws Exception {
        impl = new DefaultProjectPaymentCalculator();
    }

    /**
     * <p>
     * Accuracy test for {@link DefaultProjectPaymentCalculator#DefaultProjectPaymentCalculator()}.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_DefaultProjectPaymentCalculator3() throws Exception {
        ConfigurationFileManager configFileManager =
            new ConfigurationFileManager("accuracy/DefaultProjectPaymentCalculator.properties");
        ConfigurationObject configObject =
            configFileManager
                .getConfiguration("com.topcoder.management.payment.calculator.impl.DefaultProjectPaymentCalculator");
        ConfigurationObject config =
            configObject.getChild("com.topcoder.management.payment.calculator.impl.DefaultProjectPaymentCalculator");
        impl = new DefaultProjectPaymentCalculator(config);
    }

    /**
     * <p>
     * Accuracy test for {@link DefaultProjectPaymentCalculator#getDefaultPayment()}.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_getDefaultPayment() throws Exception {
        BigDecimal ret = impl.getDefaultPayment(1, 2, new BigDecimal(1000), 1);

        // the payment for the role is submission count sensitive
        assertEquals(10, ret.doubleValue(), 1e-10);
        ret = impl.getDefaultPayment(1, 2, new BigDecimal(1000), 2);
        assertEquals(20, ret.doubleValue(), 1e-10);
        ret = impl.getDefaultPayment(1, 2, new BigDecimal(1000), 3);
        assertEquals(30, ret.doubleValue(), 1e-10);

        // the payment for the role is not submission count sensitive
        ret = impl.getDefaultPayment(1, 14, new BigDecimal(1000), 1);
        assertEquals(300, ret.doubleValue(), 1e-10);
        ret = impl.getDefaultPayment(1, 14, new BigDecimal(1000), 2);
        assertEquals(300, ret.doubleValue(), 1e-10);
        ret = impl.getDefaultPayment(1, 14, new BigDecimal(1000), 3);
        assertEquals(300, ret.doubleValue(), 1e-10);

        // null is returned if no data found
        ret = impl.getDefaultPayment(2, 14, new BigDecimal(1000), 1);
        assertNull("No data found", ret);
    }

    /**
     * <p>
     * Accuracy test for {@link DefaultProjectPaymentCalculator#getDefaultPayments()}.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_getDefaultPayments2() throws Exception {
        int projectId = 1000;
        Map<Long, BigDecimal> ret =
            impl.getDefaultPayments(projectId, Arrays.asList(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 14L, 18L));
        // the data is not in the result map if no data is found for the role
        assertEquals(6, ret.size());

        // calculated by the formula: Fixed_Amount + (baseCoefficient + incrementalCoefficient * submissionsCount)
        // * prize
        assertEquals(10.0, ret.get(2L).doubleValue(), 1e-10);
        assertEquals(30, ret.get(18L).doubleValue(), 1e-10);
        assertEquals(85, ret.get(4L).doubleValue(), 1e-10);
        assertEquals(10, ret.get(8L).doubleValue(), 1e-10);
        assertEquals(25, ret.get(9L).doubleValue(), 1e-10);
        assertEquals(300, ret.get(14L).doubleValue(), 1e-10);
    }
}
