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
public class MockCreatedClass2 {

    /**
     * Creates a new MockCreatedClass2 object.
     *
     * @param configurationObject the ConfigurationObject argument
     *
     * @throws IllegalArgumentException in any time once it is called
     */
    public MockCreatedClass2(ConfigurationObject configurationObject) {
        throw new IllegalArgumentException("For test");
    }
}
