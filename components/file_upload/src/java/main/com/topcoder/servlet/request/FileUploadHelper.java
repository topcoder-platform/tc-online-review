/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.servlet.request;

import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.config.UnknownNamespaceException;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;


/**
 * <p>
 * Defines utilities for persistence classes.
 * </p>
 *
 * @author PE
 * @version 2.0
 */
final class FileUploadHelper {
    /**
     * Private constructor to prevent this class be instantiated.
     */
    private FileUploadHelper() {
        // empty
    }

    /**
     * <p>
     * Validates the value of a variable with type <code>Object</code>. The value cannot be <code>null</code>.
     * </p>
     *
     * @param value value of the variable.
     * @param name name of the variable.
     *
     * @return the object to validate.
     *
     * @throws IllegalArgumentException if <code>value</code> is <code>null</code>.
     */
    static Object validateNotNull(Object value, String name) {
        if (value == null) {
            throw new IllegalArgumentException(name + " cannot be null.");
        }

        return value;
    }

    /**
     * <p>
     * Validates the value of a string variable. The value cannot be <code>null</code> or empty string.
     * </p>
     *
     * @param value value of the variable.
     * @param name name of the variable.
     *
     * @return the object to validate.
     *
     * @throws IllegalArgumentException if <code>value</code> is <code>null</code> or is empty string.
     */
    static String validateString(String value, String name) {
        validateNotNull(value, name);

        if (value.trim().length() == 0) {
            throw new IllegalArgumentException(name + " cannot be empty string.");
        }

        return value;
    }

    /**
     * <p>
     * Close the output stream.
     * </p>
     *
     * @param output the output stream to be closed.
     */
    static void closeOutputStream(OutputStream output) {
        try {
            output.close();
        } catch (IOException e) {
            // ignore
        }
    }

    /**
     * <p>
     * Get the string array value in <code>ConfigManager</code> with specified namespace and name of property.
     * </p>
     *
     * @param namespace the namespace of the property.
     * @param name the name of the property.
     *
     * @return the string array value in <code>ConfigManager</code> with specified namespace and name of property.
     *
     * @throws ConfigurationException if the namespace doesn't exist, or the property doesn't exist, or some property
     *         value in the string array is empty string.
     */
    static String[] getStringArrayPropertyValue(String namespace, String name) throws ConfigurationException {
        try {
            String[] values = ConfigManager.getInstance().getStringArray(namespace, name);

            // if the value is required and doesn't exist, throw exception.
            if (values == null) {
                throw new ConfigurationException("The required parameter " + name + " in namespace " + namespace
                    + " doesn't exist.");
            }

            for (int i = 0; i < values.length; i++) {
                if (values[i].trim().length() == 0) {
                    throw new ConfigurationException("The value in the string array can not be empty string.");
                }
            }

            return values;
        } catch (UnknownNamespaceException une) {
            throw new ConfigurationException("The namespace with the name of " + namespace + " doesn't exist.", une);
        }
    }

    /**
     * <p>
     * Get the string value in <code>ConfigManager</code> with specified namespace and name of property.
     * </p>
     *
     * @param namespace the namespace of the property.
     * @param name the name of the property.
     * @param required whether the property is required.
     *
     * @return the string value in <code>ConfigManager</code> with specified namespace and name of property.
     *
     * @throws ConfigurationException if the namespace doesn't exist, or the property doesn't exist when it is
     *         required, or the property value is an empty string.
     */
    static String getStringPropertyValue(String namespace, String name, boolean required)
        throws ConfigurationException {
        try {
            String value = ConfigManager.getInstance().getString(namespace, name);

            // if the value is required and doesn't exist, throw exception.
            if (required && (value == null)) {
                throw new ConfigurationException("The required parameter " + name + " in namespace " + namespace
                    + " doesn't exist.");
            }

            if ((value != null) && (value.trim().length() == 0)) {
                throw new ConfigurationException("The parameter " + name + " in namespace " + namespace
                    + " has empty string value.");
            }

            return (value == null) ? null : value.trim();
        } catch (UnknownNamespaceException une) {
            throw new ConfigurationException("The namespace with the name of " + namespace + " doesn't exist.", une);
        }
    }

    /**
     * <p>
     * Get the long value in <code>ConfigManager</code> with specified namespace and name of property.
     * </p>
     *
     * @param namespace the namespace of the property.
     * @param name the name of the property.
     *
     * @return the long value in <code>ConfigManager</code> with specified namespace and name of property. If the
     *         property doesn't exist, -1 will be returned instead.
     *
     * @throws ConfigurationException if the namespace doesn't exist, or the property value is not properly formatted.
     */
    static long getLongPropertyValue(String namespace, String name) throws ConfigurationException {
        String value = getStringPropertyValue(namespace, name, false);

        if (value == null) {
            return -1;
        }

        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            throw new ConfigurationException("The long value is not properly formatted.", e);
        }
    }

    /**
     * <p>
     * Get the boolean value in <code>ConfigManager</code> with specified namespace and name of property.
     * </p>
     *
     * @param namespace the namespace of the property.
     * @param name the name of the property.
     *
     * @return the boolean value in <code>ConfigManager</code> with specified namespace and name of property.
     *
     * @throws ConfigurationException if the namespace doesn't exist, or the property value is not properly formatted.
     */
    static boolean getBooleanPropertyValue(String namespace, String name) throws ConfigurationException {
        String value = getStringPropertyValue(namespace, name, true);

        return Boolean.valueOf(value).booleanValue();
    }

    /**
     * <p>
     * Validates the input string is a correct name of the directory.
     * </p>
     *
     * @param dir the input string to validate.
     *
     * @throws ConfigurationException if the input string is not a correct name of the directory.
     */
    static void validateDirectory(String dir) throws ConfigurationException {
        if (!new File(dir).isDirectory()) {
            throw new ConfigurationException("The string " + dir + " is not a correct name of the directory.");
        }
    }
}
