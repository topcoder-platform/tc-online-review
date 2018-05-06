/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.db.connectionfactory;

import com.topcoder.configuration.ConfigurationAccessException;
import com.topcoder.configuration.ConfigurationObject;


/**
 * <p>
 * This is the helper class added in version 1.1. It contains useful methods for checking arguments
 * and for reducing code redundancy.
 * </p>
 *
 * @author magicpig
 * @version 1.1
 */
public final class DBConnectionFactoryHelper {
    /**
     * Private constructor to prevent this class being instantiated.
     */
    private DBConnectionFactoryHelper() {
        // empty
    }

    /**
     * <p>
     * Check whether the given paramValue is not null.
     * </p>
     *
     * @param paramName the name of the value
     * @param paramValue the value to check
     *
     * @throws IllegalArgumentException if the given paramValue is null
     */
    public static void checkNull(String paramName, Object paramValue) {
        if (paramValue == null) {
            throw new IllegalArgumentException("The " + paramName + " should be not null.");
        }
    }

    /**
     * <p>
     * Checks whether the given paramValue String is not null and not empty.
     * </p>
     *
     * @param paramName the name of the paramValue
     * @param paramValue the String value to check
     *
     * @throws IllegalArgumentException if the given paramValue String is null or empty string
     *         (trimmed)
     */
    public static void checkNullOrEmpty(String paramName, String paramValue) {
        checkNull(paramName, paramValue);

        if (paramValue.trim().length() == 0) {
            throw new IllegalArgumentException("The " + paramName + " should not be empty.");
        }
    }

    /**
     * <p>
     * Gets the child ConfigurationObject for key from given configurationObject.
     * </p>
     *
     * @param name the name of the configurationObject
     * @param configurationObject the ConfigurationObject to get child ConfigurationObject from
     * @param key the name of the child ConfigurationObject to get
     *
     * @return the retrieved child ConfigurationObject
     *
     * @throws ConfigurationAccessException if any error occurs while retrieving the child from
     *         configurationObject
     * @throws IllegalArgumentException if the configurationObject is null or the
     *         ConfigurationObject instance for key is null; if the key is null or empty
     */
    public static ConfigurationObject getChildFromConfigurationObject(String name,
        ConfigurationObject configurationObject, String key)
        throws ConfigurationAccessException {
        checkNull(name, configurationObject);
        checkNullOrEmpty("key", key);

        ConfigurationObject child = configurationObject.getChild(key);

        if (child == null) {
            throw new IllegalArgumentException("ConfigurationObject child for name[" + key +
                "] does not exit.");
        }

        return child;
    }

    /**
     * <p>
     * This method gets null or String value from the given ConfigurationObject.
     * </p>
     *
     * @param configurationObject the ConfigurationObject instance to get value from
     * @param key the name of the property whose value to get
     *
     * @return the retrieved value
     *
     * @throws ConfigurationAccessException if any error occurs while retrieving the value from
     *         configurationObject
     * @throws IllegalArgumentException if the retrieved value is non-null non-String object;if the
     *         key is null or empty
     */
    public static String getNullOrStringValueFromConfigurationObject(
        ConfigurationObject configurationObject, String key)
        throws ConfigurationAccessException {
        checkNull("configurationObject", configurationObject);
        checkNullOrEmpty("key", key);

        Object value = configurationObject.getPropertyValue(key);

        if ((value != null) && !(value instanceof String)) {
            throw new IllegalArgumentException("The value of property [" + key +
                "] should not be non-null non-String object.");
        }

        return (String) value;
    }
}
