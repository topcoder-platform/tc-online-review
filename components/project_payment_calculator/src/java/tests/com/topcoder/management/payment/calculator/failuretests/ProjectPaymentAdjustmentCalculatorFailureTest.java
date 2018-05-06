/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.payment.calculator.failuretests;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import junit.framework.JUnit4TestAdapter;

import org.junit.Test;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.persistence.XMLFilePersistence;
import com.topcoder.management.payment.calculator.ProjectPaymentCalculatorConfigurationException;
import com.topcoder.management.payment.calculator.impl.ProjectPaymentAdjustmentCalculator;


/**
 * Failure test for ProjectPaymentAdjustmentCalculator.
 *
 * @author progloco
 * @version 1.0
 */
public class ProjectPaymentAdjustmentCalculatorFailureTest {

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
        return new JUnit4TestAdapter(ProjectPaymentAdjustmentCalculatorFailureTest.class);
    }

    /**
     * <p>
     * Failure test constructor.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCtor_filename_null() {
        new ProjectPaymentAdjustmentCalculator(null, ProjectPaymentAdjustmentCalculator.DEFAULT_CONFIG_NAMESPACE);
    }

    /**
     * <p>
     * Failure test constructor.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCtor_filename_empty() {
        new ProjectPaymentAdjustmentCalculator("", ProjectPaymentAdjustmentCalculator.DEFAULT_CONFIG_NAMESPACE);
    }

    /**
     * <p>
     * Failure test constructor.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCtor_namespace_null() {
        new ProjectPaymentAdjustmentCalculator(ProjectPaymentAdjustmentCalculator.DEFAULT_CONFIG_FILENAME, null);
    }

    /**
     * <p>
     * Failure test constructor.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCtor_namespace_empty() {
        new ProjectPaymentAdjustmentCalculator(ProjectPaymentAdjustmentCalculator.DEFAULT_CONFIG_FILENAME, " ");
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
        new ProjectPaymentAdjustmentCalculator((ConfigurationObject) null);
    }

    /**
     * <p>
     * Failure test for the constructor. ProjectPaymentCalculator is invalid.
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
            persistence.loadFile(ProjectPaymentAdjustmentCalculator.DEFAULT_CONFIG_NAMESPACE, new File(TEST_DIR
                + "invalid_ad_1.xml"));
        obj = obj.getChild(ProjectPaymentAdjustmentCalculator.DEFAULT_CONFIG_NAMESPACE);
        new ProjectPaymentAdjustmentCalculator(obj);
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
    public void testCtor_invalid_2() throws Exception {
        XMLFilePersistence persistence = new XMLFilePersistence();
        // Get configuration
        ConfigurationObject obj =
            persistence.loadFile(ProjectPaymentAdjustmentCalculator.DEFAULT_CONFIG_NAMESPACE, new File(TEST_DIR
                + "invalid_ad_2.xml"));
        obj = obj.getChild(ProjectPaymentAdjustmentCalculator.DEFAULT_CONFIG_NAMESPACE);
        new ProjectPaymentAdjustmentCalculator(obj);
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
            persistence.loadFile(ProjectPaymentAdjustmentCalculator.DEFAULT_CONFIG_NAMESPACE, new File(TEST_DIR
                + "config.xml"));
        obj = obj.getChild(ProjectPaymentAdjustmentCalculator.DEFAULT_CONFIG_NAMESPACE);
        ProjectPaymentAdjustmentCalculator instance = new ProjectPaymentAdjustmentCalculator(obj);
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
            persistence.loadFile(ProjectPaymentAdjustmentCalculator.DEFAULT_CONFIG_NAMESPACE, new File(TEST_DIR
                + "config.xml"));
        obj = obj.getChild(ProjectPaymentAdjustmentCalculator.DEFAULT_CONFIG_NAMESPACE);
        ProjectPaymentAdjustmentCalculator instance = new ProjectPaymentAdjustmentCalculator(obj);
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
            persistence.loadFile(ProjectPaymentAdjustmentCalculator.DEFAULT_CONFIG_NAMESPACE, new File(TEST_DIR
                + "config.xml"));
        obj = obj.getChild(ProjectPaymentAdjustmentCalculator.DEFAULT_CONFIG_NAMESPACE);
        ProjectPaymentAdjustmentCalculator instance = new ProjectPaymentAdjustmentCalculator(obj);
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
            persistence.loadFile(ProjectPaymentAdjustmentCalculator.DEFAULT_CONFIG_NAMESPACE, new File(TEST_DIR
                + "config.xml"));
        obj = obj.getChild(ProjectPaymentAdjustmentCalculator.DEFAULT_CONFIG_NAMESPACE);
        ProjectPaymentAdjustmentCalculator instance = new ProjectPaymentAdjustmentCalculator(obj);
        instance.getDefaultPayments(1, Arrays.asList((Long) null));
    }
}
