/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.db.connectionfactory;

import com.topcoder.configuration.ConfigurationObject;


/**
 * <p>
 * There is the mock class for testing. It will used for testing failure in creation of Object
 * through reflection.
 * </p>
 *
 * @author magicpig
 * @version 1.1
 */
public class MockCreatedClass1 {
    /**
     * Creates a new MockCreatedClass1 object.
     */
    public MockCreatedClass1() {
        this(null);
    }

    /**
     * Creates a new MockCreatedClass1 object.
     *
     * @param configurationObject the ConfigurationObject argument
     */
    private MockCreatedClass1(ConfigurationObject configurationObject) {
        // empty
    }
}
