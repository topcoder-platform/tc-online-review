/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.payment.calculator.failuretests;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

import junit.framework.JUnit4TestAdapter;

import org.junit.Test;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.persistence.XMLFilePersistence;
import com.topcoder.management.payment.calculator.ProjectPaymentCalculatorConfigurationException;
import com.topcoder.management.payment.calculator.impl.DefaultProjectPaymentCalculator;


/**
 * Failure test for DefaultProjectPaymentCalculator.
 *
 * @author progloco
 * @version 1.0
 */
public class DefaultProjectPaymentCalculatorFailureTest {

    /**
     * Test directory.
     */
    private static final String TEST_DIR = "test_files" + File.separator + "failure" + File.separator;

    /**
     * <p>
     * Adapter for earlier versions of JUnit.
     * </p>
     *
     * @return a test suite.
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(DefaultProjectPaymentCalculatorFailureTest.class);
    }

    /**
     * <p>
     * Failure test constructor.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCtor_filename_null() {
        new DefaultProjectPaymentCalculator(null, DefaultProjectPaymentCalculator.DEFAULT_CONFIG_NAMESPACE);
    }

    /**
     * <p>
     * Failure test constructor.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCtor_filename_empty() {
        new DefaultProjectPaymentCalculator("", DefaultProjectPaymentCalculator.DEFAULT_CONFIG_NAMESPACE);
    }

    /**
     * <p>
     * Failure test constructor.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCtor_namespace_null() {
        new DefaultProjectPaymentCalculator(DefaultProjectPaymentCalculator.DEFAULT_CONFIG_FILENAME, null);
    }

    /**
     * <p>
     * Failure test constructor.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCtor_namespace_empty() {
        new DefaultProjectPaymentCalculator(DefaultProjectPaymentCalculator.DEFAULT_CONFIG_FILENAME, " ");
    }

    /**
     * <p>
     * Failure test for the constructor.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCtor_null_argument() throws Exception {
        new DefaultProjectPaymentCalculator(null);
    }

    /**
     * <p>
     * Failure test for the constructor. db_connection_factory_config is missing.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    @Test(expected = ProjectPaymentCalculatorConfigurationException.class)
    public void testCtor_invalid_1() throws Exception {
        XMLFilePersistence persistence = new XMLFilePersistence();
        // Get configuration
        ConfigurationObject obj =
            persistence.loadFile(DefaultProjectPaymentCalculator.DEFAULT_CONFIG_NAMESPACE, new File(TEST_DIR
                + "invalid_1.xml"));
        obj = obj.getChild(DefaultProjectPaymentCalculator.DEFAULT_CONFIG_NAMESPACE);
        new DefaultProjectPaymentCalculator(obj);
    }

    /**
     * <p>
     * Failure test for the constructor. Connection not found.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    @Test(expected = ProjectPaymentCalculatorConfigurationException.class)
    public void testCtor_invalid_2() throws Exception {
        XMLFilePersistence persistence = new XMLFilePersistence();
        // Get configuration
        ConfigurationObject obj =
            persistence.loadFile(DefaultProjectPaymentCalculator.DEFAULT_CONFIG_NAMESPACE, new File(TEST_DIR
                + "invalid_2.xml"));
        obj = obj.getChild(DefaultProjectPaymentCalculator.DEFAULT_CONFIG_NAMESPACE);
        new DefaultProjectPaymentCalculator(obj);
    }

    /**
     * <p>
     * Failure test for getDefaultPayments method. projectId is 0.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetDefaultPayments_invalid_1() throws Exception {
        XMLFilePersistence persistence = new XMLFilePersistence();
        // Get configuration
        ConfigurationObject obj =
            persistence.loadFile(DefaultProjectPaymentCalculator.DEFAULT_CONFIG_NAMESPACE, new File(TEST_DIR
                + "config.xml"));
        obj = obj.getChild(DefaultProjectPaymentCalculator.DEFAULT_CONFIG_NAMESPACE);
        DefaultProjectPaymentCalculator instance = new DefaultProjectPaymentCalculator(obj);
        instance.getDefaultPayments(0, Arrays.asList(1L));
    }

    /**
     * <p>
     * Failure test for getDefaultPayments method. projectId is -1.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetDefaultPayments_invalid_2() throws Exception {
        XMLFilePersistence persistence = new XMLFilePersistence();
        // Get configuration
        ConfigurationObject obj =
            persistence.loadFile(DefaultProjectPaymentCalculator.DEFAULT_CONFIG_NAMESPACE, new File(TEST_DIR
                + "config.xml"));
        obj = obj.getChild(DefaultProjectPaymentCalculator.DEFAULT_CONFIG_NAMESPACE);
        DefaultProjectPaymentCalculator instance = new DefaultProjectPaymentCalculator(obj);
        instance.getDefaultPayments(-1, Arrays.asList(1L));
    }

    /**
     * <p>
     * Failure test for getDefaultPayments method. resourceRoleIDs is empty.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetDefaultPayments_invalid_3() throws Exception {
        XMLFilePersistence persistence = new XMLFilePersistence();
        // Get configuration
        ConfigurationObject obj =
            persistence.loadFile(DefaultProjectPaymentCalculator.DEFAULT_CONFIG_NAMESPACE, new File(TEST_DIR
                + "config.xml"));
        obj = obj.getChild(DefaultProjectPaymentCalculator.DEFAULT_CONFIG_NAMESPACE);
        DefaultProjectPaymentCalculator instance = new DefaultProjectPaymentCalculator(obj);
        instance.getDefaultPayments(1, new ArrayList<Long>());
    }

    /**
     * <p>
     * Failure test for getDefaultPayments method. resourceRoleIDs contains null.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetDefaultPayments_invalid_4() throws Exception {
        XMLFilePersistence persistence = new XMLFilePersistence();
        // Get configuration
        ConfigurationObject obj =
            persistence.loadFile(DefaultProjectPaymentCalculator.DEFAULT_CONFIG_NAMESPACE, new File(TEST_DIR
                + "config.xml"));
        obj = obj.getChild(DefaultProjectPaymentCalculator.DEFAULT_CONFIG_NAMESPACE);
        DefaultProjectPaymentCalculator instance = new DefaultProjectPaymentCalculator(obj);
        instance.getDefaultPayments(1, Arrays.asList((Long) null));
    }

    /**
     * <p>
     * Failure test for getDefaultPayment method. projectCategoryId is 0.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetDefaultPayment_invalid_1() throws Exception {
        XMLFilePersistence persistence = new XMLFilePersistence();
        // Get configuration
        ConfigurationObject obj =
            persistence.loadFile(DefaultProjectPaymentCalculator.DEFAULT_CONFIG_NAMESPACE, new File(TEST_DIR
                + "config.xml"));
        obj = obj.getChild(DefaultProjectPaymentCalculator.DEFAULT_CONFIG_NAMESPACE);
        DefaultProjectPaymentCalculator instance = new DefaultProjectPaymentCalculator(obj);
        instance.getDefaultPayment(0, 1, BigDecimal.ONE, 1);
    }

    /**
     * <p>
     * Failure test for getDefaultPayment method. projectCategoryId is 0.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetDefaultPayment_invalid_2() throws Exception {
        XMLFilePersistence persistence = new XMLFilePersistence();
        // Get configuration
        ConfigurationObject obj =
            persistence.loadFile(DefaultProjectPaymentCalculator.DEFAULT_CONFIG_NAMESPACE, new File(TEST_DIR
                + "config.xml"));
        obj = obj.getChild(DefaultProjectPaymentCalculator.DEFAULT_CONFIG_NAMESPACE);
        DefaultProjectPaymentCalculator instance = new DefaultProjectPaymentCalculator(obj);
        instance.getDefaultPayment(1, 0, BigDecimal.ONE, 1);
    }

    /**
     * <p>
     * Failure test for getDefaultPayment method. prize is null.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetDefaultPayment_invalid_3() throws Exception {
        XMLFilePersistence persistence = new XMLFilePersistence();
        // Get configuration
        ConfigurationObject obj =
            persistence.loadFile(DefaultProjectPaymentCalculator.DEFAULT_CONFIG_NAMESPACE, new File(TEST_DIR
                + "config.xml"));
        obj = obj.getChild(DefaultProjectPaymentCalculator.DEFAULT_CONFIG_NAMESPACE);
        DefaultProjectPaymentCalculator instance = new DefaultProjectPaymentCalculator(obj);
        instance.getDefaultPayment(1, 1, null, 1);
    }

    /**
     * <p>
     * Failure test for getDefaultPayment method. submissionsCount is -1.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetDefaultPayment_invalid_4() throws Exception {
        XMLFilePersistence persistence = new XMLFilePersistence();
        // Get configuration
        ConfigurationObject obj =
            persistence.loadFile(DefaultProjectPaymentCalculator.DEFAULT_CONFIG_NAMESPACE, new File(TEST_DIR
                + "config.xml"));
        obj = obj.getChild(DefaultProjectPaymentCalculator.DEFAULT_CONFIG_NAMESPACE);
        DefaultProjectPaymentCalculator instance = new DefaultProjectPaymentCalculator(obj);
        instance.getDefaultPayment(1, 1, BigDecimal.ONE, -1);
    }
}
