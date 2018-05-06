/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.payment.calculator.stresstests;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import junit.framework.JUnit4TestAdapter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.topcoder.management.payment.calculator.impl.DefaultProjectPaymentCalculator;


/**
 * <p>
 * Stress tests for <code>{@link DefaultProjectPaymentCalculator}</code> class.
 * </p>
 *
 * @author gjw99
 * @version 1.0
 */
public class DefaultProjectPaymentCalculatorTests extends BaseStressTest {
    /**
     * <p>
     * Represents the <code>DefaultProjectPaymentCalculator</code> instance used in tests.
     * </p>
     */
    private DefaultProjectPaymentCalculator instance;

    /**
     * <p>
     * Represents the <code>Connection</code> instance used in tests.
     * </p>
     */
    private Connection conn;

    /**
     * <p>
     * Adapter for earlier versions of JUnit.
     * </p>
     *
     * @return a test suite.
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(DefaultProjectPaymentCalculatorTests.class);
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
        instance =
            new DefaultProjectPaymentCalculator("test_files/stress/stress.properties",
                DefaultProjectPaymentCalculator.class.getName());
        conn = TestHelper.getConnection();
        TestHelper.clearDB(conn);
        TestHelper.loadDB(conn);
    }

    /**
     * <p>
     * Tears down the unit tests.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @After
    public void tearDown() throws Exception {
        instance = null;
        TestHelper.clearDB(conn);

        if ((conn != null) && !conn.isClosed()) {
            conn.close();
        }
    }

    /**
     * <p>
     * Stress test for the getDefaultPayments.
     * </p>
     *
     * @throws Throwable
     *             to jUnit
     */
    @Test
    public void test_getDefaultPayments() throws Throwable {
        Thread[] thread = new Thread[testCount];

        for (int i = 0; i < testCount; i++) {
            thread[i] = new TestThread(i) {
                public void run() {
                    try {
                        Map<Long, BigDecimal> result =
                            instance.getDefaultPayments(1, Arrays.asList(new Long[] {1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L,
                                9L, 10L}));
                        assertEquals("default payments not found", 4, result.size());
                        assertEquals("default payments not found", 0, result.get(2L).compareTo(new BigDecimal(0)));
                        assertEquals("default payments not found", 0, result.get(4L).compareTo(new BigDecimal(0)));
                        assertEquals("default payments not found", 0, result.get(8L).compareTo(new BigDecimal(10)));
                        assertEquals("default payments not found", 0, result.get(9L).compareTo(new BigDecimal(0)));
                    } catch (Exception e) {
                        lastError = e;
                    }
                }
            };
            thread[i].start();
        }

        for (int i = 0; i < testCount; i++) {
            // wait to end
            thread[i].join();
        }

        if (lastError != null) {
            throw lastError;
        }

        System.out.println("Run getDefaultPayments()  " + testCount + " times takes " + (new Date().getTime() - start)
            + "ms");
    }

    /**
     * <p>
     * Stress test for the getDefaultPayment.
     * </p>
     *
     * @throws Throwable
     *             to jUnit
     */
    @Test
    public void test_getDefaultPayment1() throws Throwable {
        Thread[] thread = new Thread[testCount];

        for (int i = 0; i < testCount; i++) {
            thread[i] = new TestThread(i) {
                public void run() {
                    try {
                        BigDecimal result = instance.getDefaultPayment(1, 8, new BigDecimal(1000), 2);
                        assertEquals("default payment is wrong", 0, result.compareTo(new BigDecimal(130)));
                    } catch (Exception e) {
                        lastError = e;
                    }
                }
            };
            thread[i].start();
        }

        for (int i = 0; i < testCount; i++) {
            // wait to end
            thread[i].join();
        }

        if (lastError != null) {
            throw lastError;
        }

        System.out.println("Run getDefaultPayment()  " + testCount + " times takes " + (new Date().getTime() - start)
            + "ms");
    }

    /**
     * <p>
     * Stress test for the getDefaultPayment.
     * </p>
     *
     * @throws Throwable
     *             to jUnit
     */
    @Test
    public void test_getDefaultPayment2() throws Throwable {
        Thread[] thread = new Thread[testCount];

        for (int i = 0; i < testCount; i++) {
            thread[i] = new TestThread(i) {
                public void run() {
                    try {
                        BigDecimal result = instance.getDefaultPayment(1, 1, new BigDecimal(1500), 2);
                        assertEquals("default payment is wrong", 0, result.compareTo(new BigDecimal(1510)));
                    } catch (Exception e) {
                        lastError = e;
                    }
                }
            };
            thread[i].start();
        }

        for (int i = 0; i < testCount; i++) {
            // wait to end
            thread[i].join();
        }

        if (lastError != null) {
            throw lastError;
        }

        System.out.println("Run getDefaultPayment()  " + testCount + " times takes " + (new Date().getTime() - start)
            + "ms");
    }

    /**
     * <p>
     * Stress test for the getDefaultPayment.
     * </p>
     *
     * @throws Throwable
     *             to jUnit
     */
    @Test
    public void test_getDefaultPayment3() throws Throwable {
        Thread[] thread = new Thread[testCount];

        for (int i = 0; i < testCount; i++) {
            thread[i] = new TestThread(i) {
                public void run() {
                    try {
                        BigDecimal result = instance.getDefaultPayment(1, 4, new BigDecimal(500), 2);
                        assertEquals("default payment is wrong", 0, result.compareTo(new BigDecimal(110)));
                    } catch (Exception e) {
                        lastError = e;
                    }
                }
            };
            thread[i].start();
        }

        for (int i = 0; i < testCount; i++) {
            // wait to end
            thread[i].join();
        }

        if (lastError != null) {
            throw lastError;
        }

        System.out.println("Run getDefaultPayment()  " + testCount + " times takes " + (new Date().getTime() - start)
            + "ms");
    }

    /**
     * <p>
     * Stress test for the getDefaultPayment.
     * </p>
     *
     * @throws Throwable
     *             to jUnit
     */
    @Test
    public void test_getDefaultPayment4() throws Throwable {
        Thread[] thread = new Thread[testCount];

        for (int i = 0; i < testCount; i++) {
            thread[i] = new TestThread(i) {
                public void run() {
                    try {
                        BigDecimal result = instance.getDefaultPayment(1, 5, new BigDecimal(500), 2);
                        assertNull("default payment doesn't exist", result);
                    } catch (Exception e) {
                        lastError = e;
                    }
                }
            };
            thread[i].start();
        }

        for (int i = 0; i < testCount; i++) {
            // wait to end
            thread[i].join();
        }

        if (lastError != null) {
            throw lastError;
        }

        System.out.println("Run getDefaultPayment()  " + testCount + " times takes " + (new Date().getTime() - start)
            + "ms");
    }

    /**
     * <p>
     * Stress test for the getDefaultPayment.
     * </p>
     *
     * @throws Throwable
     *             to jUnit
     */
    @Test
    public void test_getDefaultPayment5() throws Throwable {
        Thread[] thread = new Thread[testCount];

        for (int i = 0; i < testCount; i++) {
            thread[i] = new TestThread(i) {
                public void run() {
                    try {
                        BigDecimal result = instance.getDefaultPayment(1, 9, new BigDecimal(500), 2);
                        assertEquals("default payment is wrong", 0, result.compareTo(new BigDecimal(25)));
                    } catch (Exception e) {
                        lastError = e;
                    }
                }
            };
            thread[i].start();
        }

        for (int i = 0; i < testCount; i++) {
            // wait to end
            thread[i].join();
        }

        if (lastError != null) {
            throw lastError;
        }

        System.out.println("Run getDefaultPayment()  " + testCount + " times takes " + (new Date().getTime() - start)
            + "ms");
    }
}
