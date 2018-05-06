/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.configuration.accuracytests;

import com.topcoder.configuration.BaseConfigurationObject;
import com.topcoder.configuration.ConfigurationObject;

import junit.framework.TestCase;

/**
 * <p>
 * Accuracy test of the abstract class BaseConfigurationObject.
 * </p>
 *
 * @author KKD
 * @since 1.0
 */
public class BaseConfigurationObjectAccuracyTests extends TestCase {
    /**
     * The accuracy test of the constructor of BaseConfigurationObject, it is the empty class.
     *
     */
    public void test_accuracy_ctor() {
        assertNotNull("can not create the BaseConfigurationObject.", new BaseConfigurationObjectMock());
    }

    /**
     * Test inheritance of BaseConfigurationObject, it is implements ConfigurationObject interface.
     *
     */
    public void test_inheritance() {
        assertTrue("BaseConfigurationObject should implements ConfigurationObject interface.",
            new BaseConfigurationObjectMock() instanceof ConfigurationObject);
    }

    /**
     * Test the method clone for accuracy.
     */
    public void test_accuracy_clone() {
        BaseConfigurationObject instance = new BaseConfigurationObjectMock();
        Object clone = instance.clone();
        assertNotNull("can not clone the BaseConfigurationObject.", clone);
        assertTrue("clone should return another instance.", clone != instance);
    }
}