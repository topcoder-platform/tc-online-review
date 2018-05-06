/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.management.review.scorecalculator;

import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.config.UnknownNamespaceException;

/**
 * <p>
 * Contains a bunch of static utility methods, mainly for argument checking and such.
 * </p>
 *
 * <p>
 * This class is public because there are multiple packages in the Review Score Calculator component.
 * </p>
 *
 * @author      UFP2161
 * @copyright   Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 * @version     1.0
 */
public final class Util {

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Constructors

    /**
     * Prevents instantiation.
     */
    private Util() {
        // Does nothing.
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Static Methods - Argument Checking

    /**
     * Checks to make sure the specified long is a positive integer.
     *
     * @param   argument
     *          The long argument to check.
     * @param   argumentName
     *          The name of the argument to use in the error message.
     *
     * @throws  IllegalArgumentException
     *          The argument is less than or equal to zero.
     */
    public static void checkNotNonPositive(long argument, String argumentName) {
        if (argument <= 0) {
            throw new IllegalArgumentException("The " + argumentName + " cannot be non-positive.");
        }
    }

    /**
     * Checks to make sure the specified long is a positive integer or zero.
     *
     * @param   argument
     *          The long argument to check.
     * @param   argumentName
     *          The name of the argument to use in the error message.
     *
     * @throws  IllegalArgumentException
     *          The argument is less than to zero.
     */
    public static void checkNotNegative(long argument, String argumentName) {
        if (argument < 0) {
            throw new IllegalArgumentException("The " + argumentName + " cannot be negative.");
        }
    }
    
    /**
     * Checks to make sure the specified long array is not null and contains all positive integers.
     *
     * @param   argument
     *          The long array argument to check.
     * @param   argumentName
     *          The name of the argument to use in the error message.
     *
     * @throws  IllegalArgumentException
     *          The argument is null or contains elements that are less than or equal to zero.
     */
    static void checkNotNonPositiveArray(long[] argument, String argumentName) {
        Util.checkNotNull(argument, argumentName);

        for (int i = 0; i < argument.length; ++i) {
            Util.checkNotNonPositive(argument[i], argumentName + "[" + i + "]");
        }
    }

    /**
     * Checks to make sure the specified object is not null.
     *
     * @param   argument
     *          The object argument to check.
     * @param   argumentName
     *          The name of the argument to use in the error message.
     *
     * @throws  IllegalArgumentException
     *          The argument is null.
     */
    public static void checkNotNull(Object argument, String argumentName) {
        if (argument == null) {
            throw new IllegalArgumentException("The " + argumentName + " cannot be null.");
        }
    }

    /**
     * Checks to make sure the specified array is not null and does not contain any nulls.
     *
     * @param   argument
     *          The array argument to check.
     * @param   argumentName
     *          The name of the argument to use in the error message.
     *
     * @throws  IllegalArgumentException
     *          The argument is null or contains a null.
     */
    static void checkNotNullArray(Object[] argument, String argumentName) {
        Util.checkNotNull(argument, argumentName);

        for (int i = 0; i < argument.length; ++i) {
            Util.checkNotNull(argument[i], argumentName + "[" + i + "]");
        }
    }

    /**
     * Checks to make sure the specified string is not null or empty (after trimming).
     *
     * @param   argument
     *          The string argument to check.
     * @param   argumentName
     *          The name of the argument to use in the error message.
     *
     * @throws  IllegalArgumentException
     *          The argument is null or an empty string.
     */
    public static void checkNotNullOrEmpty(String argument, String argumentName) {
        checkNotNull(argument, argumentName);

        if (isEmpty(argument)) {
            throw new IllegalArgumentException("The " + argumentName + " cannot be an empty (trimmed) string.");
        }
    }

    /**
     * Returns whether or not the specified string is an empty string (after trimming).
     *
     * @param   argument
     *          The string argument to check.
     *
     * @return  true if the argument is an empty string; false otherwise.
     */
    static boolean isEmpty(String argument) {
        return (argument.trim().length() == 0);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Static Methods - Configuration Loading

    /**
     * Returns the optional configuration property value.
     *
     * @param   namespace
     *          The configuration namespace to load the property from.
     * @param   property
     *          The name of the property to load the value for.
     *
     * @return  The property's value, or null if it isn't configured.
     *
     * @throws  ConfigurationException
     *          The namespace does not exist in the configuration file.
     */
    public static String getOptionalProperty(String namespace, String property) throws ConfigurationException {
        try {
            return ConfigManager.getInstance().getString(namespace, property);
        } catch (UnknownNamespaceException ex) {
            throw new ConfigurationException("The namespace '" + namespace + "' is missing.", ex);
        }
    }

    /**
     * Returns the optional configuration property value.
     *
     * @param   namespace
     *          The configuration namespace to load the property from.
     * @param   property
     *          The name of the property to load the value for.
     *
     * @return  The property's value.
     *
     * @throws  ConfigurationException
     *          The namespace does not exist in the configuration file, or the property is missing from the
     *          configuration namespace, or the property is an empty string (trimmed)
     */
    public static String getRequiredProperty(String namespace, String property) throws ConfigurationException {
        final String result = getOptionalProperty(namespace, property);

        if (result == null) {
            throw new ConfigurationException(
                    "The required property '" + property + "' is missing from the namespace '" + namespace + "'.");
        }

        if (result.trim().length() == 0) {
            throw new ConfigurationException(
                    "The required property '" + property + "' from the namespace '" + namespace
                    + "' cannot be an empty string.");
        }

        return result;
    }
}
