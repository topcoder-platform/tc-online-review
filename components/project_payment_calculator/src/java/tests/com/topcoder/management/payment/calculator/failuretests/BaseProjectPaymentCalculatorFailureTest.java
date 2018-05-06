/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.payment.calculator.failuretests;

import junit.framework.JUnit4TestAdapter;

import org.junit.Test;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.defaults.DefaultConfigurationObject;
import com.topcoder.management.payment.calculator.ProjectPaymentCalculatorConfigurationException;
import com.topcoder.management.payment.calculator.impl.BaseProjectPaymentCalculator;


/**
 * Failure test for BaseProjectPaymentCalculator.
 *
 * @author progloco
 * @version 1.0
 */
public class BaseProjectPaymentCalculatorFailureTest {

    /**
     * <p>
     * Adapter for earlier versions of JUnit.
     * </p>
     *
     * @return a test suite.
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(BaseProjectPaymentCalculatorFailureTest.class);
    }

    /**
     * <p>
     * Failure test for constructor. Argument is null.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCtor_null_argument() {
        new BaseProjectPaymentCalculator(null) {
        };
    }

    /**
     * <p>
     * Failure test for constructor. Missing DB configuration.
     * </p>
     */
    @Test(expected = ProjectPaymentCalculatorConfigurationException.class)
    public void testCtor_missing_db_config() {
        ConfigurationObject configurationObject = new DefaultConfigurationObject("test");
        new BaseProjectPaymentCalculator(configurationObject) {
        };
    }

    /**
     * <p>
     * Failure test for constructor. Logger name is empty.
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    @Test(expected = ProjectPaymentCalculatorConfigurationException.class)
    public void testCtor_empty_logger_name() throws Exception {
        ConfigurationObject configurationObject = new DefaultConfigurationObject("test");
        configurationObject.addChild(new DefaultConfigurationObject("db_connection_factory_config"));
        configurationObject.setPropertyValue("logger_name", " ");
        new BaseProjectPaymentCalculator(configurationObject) {
        };
    }
}
