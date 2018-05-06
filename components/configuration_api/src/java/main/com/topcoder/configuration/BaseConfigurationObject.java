/*
 * Copyright (C) 2007, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.configuration;

import java.util.Date;

/**
 * <p>
 * An abstract adapter implementation of ConfigurationObject interface. All the methods in this class always throw
 * UnsupportedOperationException, except the <code>clone</code> method. This class exists as convenience for
 * creating custom ConfigurationObject.
 * </p>
 *
 * <p>
 * Extend this class to create a custom ConfigurationObject and override the only the methods which can be supported
 * by certain configuration strategy.
 * </p>
 *
 * <p>
 * <em>Changes in 1.1:</em>
 * <ol>
 * <li>Added getPropertyValue() overload with generic parameter that is expected to return value of specific type.</li>
 * <li>Added getPropertyValues() overload with generic parameter that is expected to return array of specific type.</li>
 * <li>Added getPropertyValue() and getPropertyValues() overloads that accept "required" parameter and throw
 * exception if required property is missing.</li>
 * <li>getPropertyValue() and getPropertyValues() methods without "required" parameter were updated to delegate
 * execution to overloads using required=false parameter value.</li>
 * <li>Added getXXXProperty() methods that additionally can perform parsing of Integer, Long, Double, Date and Class
 * instance from String property values.</li>
 * </ol>
 * </p>
 *
 * <p>
 * Thread safe: This class has no state, and thus it is thread safe.
 * </p>
 *
 * @author maone, haozhangr, saarixx, sparemax
 * @version 1.1
 */
public abstract class BaseConfigurationObject implements ConfigurationObject {
    /**
     * Empty protected constructor.
     *
     */
    protected BaseConfigurationObject() {
        // Empty
    }

    /**
     * <p>
     * Get the name of the ConfigurationObject, always throws UnsupportedOperationException.
     * </p>
     *
     * @return N/A
     * @throws UnsupportedOperationException
     *             Always thrown.
     * @throws ConfigurationAccessException
     *             Never thrown, kept for sub-class to use it.
     */
    public String getName() throws ConfigurationAccessException {
        throw new UnsupportedOperationException("The getName method is not supported.");
    }

    /**
     * <p>
     * Determine whether property with given key exists in the configuration object. Always throws
     * UnsupportedOperationException.
     * </p>
     *
     * @return true if the specified property is contained, false otherwise.
     * @param key
     *            the key of property
     * @throws UnsupportedOperationException
     *             Always thrown.
     * @throws ConfigurationAccessException
     *             Never thrown, kept for sub-class to use it.
     */
    public boolean containsProperty(String key) throws ConfigurationAccessException {
        throw new UnsupportedOperationException("The containsProperty method is not supported.");
    }

    /**
     * <p>
     * Get the property value for the given key. Note: if the key contains more than one values, the value returned
     * depends on specific implementation. Always throws UnsupportedOperationException.
     * </p>
     *
     * <p>
     * <em>Changes in 1.1:</em>
     * <ol>
     * <li>Delegates execution to the namesake generic method.</li>
     * </ol>
     * </p>
     *
     * @param key
     *            the key of property.
     *
     * @return the value associated with the key, or null if there is no such key.
     *
     * @throws UnsupportedOperationException
     *             Always thrown.
     * @throws ConfigurationAccessException
     *             Never thrown, kept for sub-class to use it.
     */
    public Object getPropertyValue(String key) throws ConfigurationAccessException {
        Object result = null;

        try {
            result = getPropertyValue(key, Object.class);
        } catch (PropertyTypeMismatchException e) {
            // Ignore (won't happen)
        }
        return result;
    }

    /**
     * <p>
     * Get the property value for the given key.
     * </p>
     *
     * <p>
     * Note: if the key contains more than one values, the value returned depends on specific implementation. Always
     * throws UnsupportedOperationException.
     * </p>
     *
     * @param <T>
     *            the expected type of the property value.
     * @param key
     *            the key of property.
     * @param clazz
     *            the expected type of each property value.
     *
     * @return the value associated with the key, or null if there is no such key.
     *
     * @throws UnsupportedOperationException
     *             Always thrown.
     * @throws PropertyTypeMismatchException
     *             Never thrown, kept for subclass to use it.
     * @throws ConfigurationAccessException
     *             Never thrown, kept for sub-class to use it.
     * @since 1.1
     */
    public <T> T getPropertyValue(String key, Class<T> clazz) throws PropertyTypeMismatchException,
        ConfigurationAccessException {
        T result = null;

        try {
            result = getPropertyValue(key, clazz, false);
        } catch (PropertyNotFoundException e) {
            // Ignore (won't happen)
        }
        return result;
    }

    /**
     * <p>
     * Get the number of values contained by the specific property. Always throws UnsupportedOperationException.
     * </p>
     *
     * @return the number of values contained in the property, or -1 if there is no such property.
     * @param key
     *            the key specifying the property
     * @throws UnsupportedOperationException
     *             Always thrown.
     * @throws ConfigurationAccessException
     *             Never thrown, kept for sub-class to use it.
     */
    public int getPropertyValuesCount(String key) throws ConfigurationAccessException {
        throw new UnsupportedOperationException("The getPropertValuesCount method is not supported.");
    }

    /**
     * <p>
     * Get all the property values associated with given key. Always throws UnsupportedOperationException.
     * </p>
     *
     * <p>
     * <em>Changes in 1.1:</em>
     * <ol>
     * <li>Delegates execution to the namesake generic method.</li>
     * </ol>
     * </p>
     *
     * @param key
     *            the key used to specify property
     *
     * @return an array containing all the property values, or null if there is no such property key.
     *
     * @throws UnsupportedOperationException
     *             Always thrown.
     * @throws ConfigurationAccessException
     *             Never thrown, kept for sub-class to use it.
     */
    public Object[] getPropertyValues(String key) throws ConfigurationAccessException {
        Object[] result = null;

        try {
            result = getPropertyValues(key, Object.class);
        } catch (PropertyTypeMismatchException e) {
            // Ignore (won't happen)
        }
        return result;
    }

    /**
     * Gets all the property values of specific type associated with given key.
     *
     * @param <T>
     *            the expected type of each property value.
     * @param key
     *            the key used to specify property.
     * @param clazz
     *            the expected type of each property value.
     *
     * @return an array containing all the property values, or null if there is no such property key.
     *
     * @throws UnsupportedOperationException
     *             Always thrown.
     * @throws PropertyTypeMismatchException
     *             Never thrown, kept for subclass to use it.
     * @throws ConfigurationAccessException
     *             Never thrown, kept for subclass to use it.
     *
     * @since 1.1
     */
    public <T> T[] getPropertyValues(String key, Class<T> clazz) throws PropertyTypeMismatchException,
        ConfigurationAccessException {
        T[] result = null;

        try {
            result = getPropertyValues(key, clazz, false);
        } catch (PropertyNotFoundException e) {
            // Ignore (won't happen)
        }
        return result;
    }

    /**
     * Get the property value for the given key. If the property is required, but missing, PropertyNotFoundException
     * is thrown.
     *
     * <p>
     * Note, if the key contains more than one value, the value returned depends on specific implementation.
     * </p>
     *
     * @param <T>
     *            the expected type of each property value.
     * @param key
     *            the key of property.
     * @param clazz
     *            the expected type of each property value.
     * @param required
     *            true if property is required, false otherwise.
     *
     * @return the value associated with the key, or null if there is no such key and property is not required.
     *
     * @throws UnsupportedOperationException
     *             Always thrown.
     * @throws PropertyTypeMismatchException
     *             Never thrown, kept for subclass to use it.
     * @throws PropertyNotFoundException
     *             Never thrown, kept for subclass to use it.
     * @throws ConfigurationAccessException
     *             Never thrown, kept for subclass to use it.
     *
     * @since 1.1
     */
    public <T> T getPropertyValue(String key, Class<T> clazz, boolean required) throws PropertyTypeMismatchException,
        PropertyNotFoundException, ConfigurationAccessException {
        throw new UnsupportedOperationException("The getPropertyValue method is not supported.");
    }

    /**
     * Gets all the property values of specific type associated with given key. If the property is required, but
     * missing, PropertyNotFoundException is thrown.
     *
     * @param <T>
     *            the expected type of each property value.
     * @param key
     *            the key used to specify property.
     * @param required
     *            true if property is required, false otherwise.
     * @param clazz
     *            the expected type of each property value.
     *
     * @return an array containing all the property values, or null if there is no such key and property is not
     *         required.
     *
     * @throws UnsupportedOperationException
     *             Always thrown.
     * @throws PropertyTypeMismatchException
     *             Never thrown, kept for subclass to use it.
     * @throws PropertyNotFoundException
     *             Never thrown, kept for subclass to use it.
     * @throws ConfigurationAccessException
     *             Never thrown, kept for subclass to use it.
     *
     * @since 1.1
     */
    public <T> T[] getPropertyValues(String key, Class<T> clazz, boolean required)
        throws PropertyTypeMismatchException, PropertyNotFoundException, ConfigurationAccessException {
        throw new UnsupportedOperationException("The getPropertyValues method is not supported.");
    }

    /**
     * Gets the integer property value for the given key. If property value has String type, it is parsed as integer.
     * If the property is required, but missing, PropertyNotFoundException is thrown.
     *
     * <p>
     * Note, if the key contains more than one value, the value returned depends on specific implementation.
     * </p>
     *
     * @param key
     *            the key of property.
     * @param required
     *            true if property is required, false otherwise.
     *
     * @return the integer value associated with the key, or null if there is no such key and property is not
     *         required.
     *
     * @throws UnsupportedOperationException
     *             Always thrown.
     * @throws PropertyTypeMismatchException
     *             Never thrown, kept for subclass to use it.
     * @throws PropertyNotFoundException
     *             Never thrown, kept for subclass to use it.
     * @throws ConfigurationAccessException
     *             Never thrown, kept for subclass to use it.
     *
     * @since 1.1
     */
    public Integer getIntegerProperty(String key, boolean required) throws PropertyTypeMismatchException,
        PropertyNotFoundException, ConfigurationAccessException {
        throw new UnsupportedOperationException("The getIntegerProperty method is not supported.");
    }

    /**
     * Gets the long integer property value for the given key. If property value has String type, it is parsed as long
     * integer. If the property is required, but missing, PropertyNotFoundException is thrown.
     *
     * <p>
     * Note, if the key contains more than one value, the value returned depends on specific implementation.
     * </p>
     *
     * @param key
     *            the key of property.
     * @param required
     *            true if property is required, false otherwise.
     *
     * @return the long integer value associated with the key, or null if there is no such key and property is not
     *         required.
     *
     * @throws UnsupportedOperationException
     *             Always thrown.
     * @throws PropertyTypeMismatchException
     *             Never thrown, kept for subclass to use it.
     * @throws PropertyNotFoundException
     *             Never thrown, kept for subclass to use it.
     * @throws ConfigurationAccessException
     *             Never thrown, kept for subclass to use it.
     *
     * @since 1.1
     */
    public Long getLongProperty(String key, boolean required) throws PropertyTypeMismatchException,
        PropertyNotFoundException, ConfigurationAccessException {
        throw new UnsupportedOperationException("The getLongProperty method is not supported.");
    }

    /**
     * Gets the double property value for the given key. If property value has String type, it is parsed as double. If
     * the property is required, but missing, PropertyNotFoundException is thrown.
     *
     * <p>
     * Note, if the key contains more than one value, the value returned depends on specific implementation.
     * </p>
     *
     * @param key
     *            the key of property.
     * @param required
     *            true if property is required, false otherwise.
     *
     * @return the double value associated with the key, or null if there is no such key and property is not required.
     *
     * @throws UnsupportedOperationException
     *             Always thrown.
     * @throws PropertyTypeMismatchException
     *             Never thrown, kept for subclass to use it.
     * @throws PropertyNotFoundException
     *             Never thrown, kept for subclass to use it.
     * @throws ConfigurationAccessException
     *             Never thrown, kept for subclass to use it.
     *
     * @since 1.1
     */
    public Double getDoubleProperty(String key, boolean required) throws PropertyTypeMismatchException,
        PropertyNotFoundException, ConfigurationAccessException {
        throw new UnsupportedOperationException("The getDoubleProperty method is not supported.");
    }

    /**
     * Gets the date/time property value for the given key. If property value has String type, it is parsed as Date
     * using the specified format. If the property is required, but missing, PropertyNotFoundException is thrown.
     *
     * <p>
     * Note, if the key contains more than one value, the value returned depends on specific implementation.
     * </p>
     *
     * @param key
     *            the key of property
     * @param required
     *            true if property is required, false otherwise
     * @param format
     *            the expected date/time format string
     *
     * @return the date/time value associated with the key, or null if there is no such key and property is not
     *         required
     *
     * @throws UnsupportedOperationException
     *             Always thrown.
     * @throws PropertyTypeMismatchException
     *             Never thrown, kept for subclass to use it.
     * @throws PropertyNotFoundException
     *             Never thrown, kept for subclass to use it.
     * @throws ConfigurationAccessException
     *             Never thrown, kept for subclass to use it.
     *
     * @since 1.1
     */
    public Date getDateProperty(String key, String format, boolean required) throws PropertyTypeMismatchException,
        PropertyNotFoundException, ConfigurationAccessException {
        throw new UnsupportedOperationException("The getDateProperty method is not supported.");
    }

    /**
     * Gets the class property value for the given key. Property value can be Class<?> instance or String with full
     * class name. If the property is required, but missing, PropertyNotFoundException is thrown.
     *
     * <p>
     * Note, if the key contains more than one value, the value returned depends on specific implementation.
     * </p>
     *
     * @param key
     *            the key of property.
     * @param required
     *            true if property is required, false otherwise.
     *
     * @return the class value associated with the key, or null if there is no such key and property is not required.
     *
     * @throws UnsupportedOperationException
     *             Always thrown.
     * @throws PropertyTypeMismatchException
     *             Never thrown, kept for subclass to use it.
     * @throws PropertyNotFoundException
     *             Never thrown, kept for subclass to use it.
     * @throws ConfigurationAccessException
     *             Never thrown, kept for subclass to use it.
     *
     * @since 1.1
     */
    public Class<?> getClassProperty(String key, boolean required) throws PropertyTypeMismatchException,
        PropertyNotFoundException, ConfigurationAccessException {
        throw new UnsupportedOperationException("The getClassProperty method is not supported.");
    }

    /**
     * <p>
     * Set a key/value pair to the configuration object. If the key already exists, old values will be overridden.
     * Always throws UnsupportedOperationException.
     * </p>
     *
     * @return the old values of the property, or null if the key is new.
     * @param key
     *            the key of property
     * @param value
     *            the value of the property.
     * @throws UnsupportedOperationException
     *             Always thrown.
     * @throws InvalidConfigurationException
     *             Never thrown, kept for sub-class to use it.
     * @throws ConfigurationAccessException
     *             Never thrown, kept for sub-class to use it.
     */
    public Object[] setPropertyValue(String key, Object value) throws InvalidConfigurationException,
        ConfigurationAccessException {
        throw new UnsupportedOperationException("The getPropertyValues method is not supported.");
    }

    /**
     * <p>
     * Set a key/values pair to the configuration object. If the key already exists, old values will be overridden.
     * Always throws UnsupportedOperationException.
     * </p>
     *
     * @return the old values of the property, or null if the key is new.
     * @param key
     *            the key of property
     * @param values
     *            the values to be set
     * @throws UnsupportedOperationException
     *             Always thrown.
     * @throws InvalidConfigurationException
     *             Never thrown, kept for sub-class to use it.
     * @throws ConfigurationAccessException
     *             Never thrown, kept for sub-class to use it.
     */
    public Object[] setPropertyValues(String key, Object[] values) throws InvalidConfigurationException,
        ConfigurationAccessException {
        throw new UnsupportedOperationException("The setPropertyValues method is not supported.");
    }

    /**
     * <p>
     * Remove property with given key. If the key doens't exist, nothing happened. Always throws
     * UnsupportedOperationException.
     * </p>
     *
     * @return the removed property value, or null if no such property.
     * @param key
     *            the key of the property to be removed
     * @throws UnsupportedOperationException
     *             Always thrown.
     * @throws ConfigurationAccessException
     *             Never thrown, kept for sub-class to use it.
     */
    public Object[] removeProperty(String key) throws ConfigurationAccessException {
        throw new UnsupportedOperationException("The removeProperty method is not supported.");
    }

    /**
     * <p>
     * Clear all the properties. Always throws UnsupportedOperationException.
     * </p>
     *
     * @throws UnsupportedOperationException
     *             Always thrown.
     * @throws ConfigurationAccessException
     *             Never thrown, kept for sub-class to use it.
     */
    public void clearProperties() throws ConfigurationAccessException {
        throw new UnsupportedOperationException("The clearProperties method is not supported.");
    }

    /**
     * <p>
     * Get all the property keys matched the pattern. Always throws UnsupportedOperationException.
     * </p>
     *
     * @return an array of matched property keys.
     * @param pattern
     *            a regular expression used to match property keys.
     * @throws UnsupportedOperationException
     *             Always thrown.
     * @throws ConfigurationAccessException
     *             Never thrown, kept for sub-class to use it.
     */
    public String[] getPropertyKeys(String pattern) throws ConfigurationAccessException {
        throw new UnsupportedOperationException("The getPropertyKeys method is not supported.");
    }

    /**
     * <p>
     * Get all the property keys contained by the ConfigurationObject. Always throws UnsupportedOperationException.
     * </p>
     *
     * @return an array of all property keys.
     * @throws UnsupportedOperationException
     *             Always thrown.
     * @throws ConfigurationAccessException
     *             Never thrown, kept for sub-class to use it.
     */
    public String[] getAllPropertyKeys() throws ConfigurationAccessException {
        throw new UnsupportedOperationException("The getAllPropertyKeys method is not supported.");
    }

    /**
     * <p>
     * Determine whether it contains a child ConfigurationObject with given name. Always throws
     * UnsupportedOperationException.
     * </p>
     *
     * @return a boolean value indicating whether the specified child is contained.
     * @param name
     *            the name of the child
     * @throws UnsupportedOperationException
     *             Always thrown.
     * @throws ConfigurationAccessException
     *             Never thrown, kept for sub-class to use it.
     */
    public boolean containsChild(String name) throws ConfigurationAccessException {
        throw new UnsupportedOperationException("The containsChild method is not supported.");
    }

    /**
     * <p>
     * Get a child ConfigurationObject with given name. Always throws UnsupportedOperationException
     * </p>
     *
     * @return the child ConfigurationObject, or null if there no such child name.
     * @param name
     *            the name of the child
     * @throws UnsupportedOperationException
     *             Always thrown.
     * @throws ConfigurationAccessException
     *             Never thrown, kept for sub-class to use it.
     */
    public ConfigurationObject getChild(String name) throws ConfigurationAccessException {
        throw new UnsupportedOperationException("The getChild method is not supported.");
    }

    /**
     * <p>
     * Add a child, if there is already a child with such name, the old one will be removed. Always throws
     * UnsupportedOperationException.
     * </p>
     *
     * @return the old child with given name, or null if the name is new.
     * @param child
     *            the child to be added.
     * @throws UnsupportedOperationException
     *             Always thrown.
     * @throws InvalidConfigurationException
     *             Never thrown, kept for sub-class to use it.
     * @throws ConfigurationAccessException
     *             Never thrown, kept for sub-class to use it.
     */
    public ConfigurationObject addChild(ConfigurationObject child) throws InvalidConfigurationException,
        ConfigurationAccessException {
        throw new UnsupportedOperationException("The addChild method is not supported.");
    }

    /**
     * <p>
     * Remove a child with given name. Always throws UnsupportedOperationException.
     * </p>
     *
     * @return the removed child, or null if there is no such name.
     * @param name
     *            the name of the child to be removed.
     * @throws UnsupportedOperationException
     *             Always thrown.
     * @throws ConfigurationAccessException
     *             Never thrown, kept for sub-class to use it.
     */
    public ConfigurationObject removeChild(String name) throws ConfigurationAccessException {
        throw new UnsupportedOperationException("The removeChild method is not supported.");
    }

    /**
     * <p>
     * Clear all the children resides in the ConfigurationObject. Always throws UnsupportedOperationException.
     * </p>
     *
     * @throws UnsupportedOperationException
     *             Always thrown.
     * @throws ConfigurationAccessException
     *             Never thrown, kept for sub-class to use it.
     */
    public void clearChildren() throws ConfigurationAccessException {
        throw new UnsupportedOperationException("The clearChildren method is not supported.");
    }

    /**
     * <p>
     * Get all the children whose names are matched to the given pattern. Always throws UnsupportedOperationException.
     * </p>
     *
     * @return the matched children
     * @param pattern
     *            a regular expression used to match children names.
     * @throws UnsupportedOperationException
     *             Always thrown.
     * @throws ConfigurationAccessException
     *             Never thrown, kept for sub-class to use it.
     */
    public ConfigurationObject[] getChildren(String pattern) throws ConfigurationAccessException {
        throw new UnsupportedOperationException("The getChildren method is not supported.");
    }

    /**
     * <p>
     * Get all the child names contained in it. Always throws UnsupportedOperationException.
     * </p>
     *
     * @return all the children names.
     * @throws UnsupportedOperationException
     *             Always thrown.
     * @throws ConfigurationAccessException
     *             Never thrown, kept for sub-class to use it.
     */
    public String[] getAllChildrenNames() throws ConfigurationAccessException {
        throw new UnsupportedOperationException("The getAllChildrenNames method is not supported.");
    }

    /**
     * <p>
     * Get all the children contained in it. Always throws UnsupportedOperationException.
     * </p>
     *
     * @return all the children.
     * @throws UnsupportedOperationException
     *             Always thrown.
     * @throws ConfigurationAccessException
     *             Never thrown, kept for sub-class to use it.
     */
    public ConfigurationObject[] getAllChildren() throws ConfigurationAccessException {
        throw new UnsupportedOperationException("The getAllChildren method is not supported.");
    }

    /**
     * <p>
     * Get all descendants of this ConfigurationObject, not including itself. Always throws
     * UnsupportedOperationException.
     * </p>
     *
     * @return all the Descendants of the current ConfigurationObject
     * @throws UnsupportedOperationException
     *             Always thrown.
     * @throws ConfigurationAccessException
     *             Never thrown, kept for sub-class to use it.
     */
    public ConfigurationObject[] getAllDescendants() throws ConfigurationAccessException {
        throw new UnsupportedOperationException("The getAllDescendants method is not supported.");
    }

    /**
     * <p>
     * Get all descendants, whose name matched to given pattern, of this ConfigurationObject, not including itself.
     * Always throws UnsupportedOperationException.
     * </p>
     *
     * @return an array of matched Descendants.
     * @param pattern
     *            a regex used to match descendants' names.
     * @throws UnsupportedOperationException
     *             Always thrown.
     * @throws ConfigurationAccessException
     *             Never thrown, kept for sub-class to use it.
     */
    public ConfigurationObject[] getDescendants(String pattern) throws ConfigurationAccessException {
        throw new UnsupportedOperationException("The getDescendants method is not supported.");
    }

    /**
     * <p>
     * Get descendants specified by the given path. The path may contain wildcards, like * and ?. The path should be
     * separated by slash or back slash characters. So if the descendant name contains slashes, this method may return
     * invalid results.
     * </p>
     *
     * <p>
     * Note:
     * <li>Heading or trailing slashes will be ignored.</li>
     * <li>Continuous slashes will be considered as one.</li>
     * Always throws UnsupportedOperationException.
     * </p>
     *
     * @return all the descendants specified by the path, or an empty array if nothing is matched.
     * @param path
     *            the path used to retrieved descendants.
     * @throws UnsupportedOperationException
     *             Always thrown.
     * @throws ConfigurationAccessException
     *             Never thrown, kept for sub-class to use it.
     */
    public ConfigurationObject[] findDescendants(String path) throws ConfigurationAccessException {
        throw new UnsupportedOperationException("The findDescendants method is not supported.");
    }

    /**
     * <p>
     * Delete descendants specified by the given path. The path may contain wildcards, like * and ?. The path should
     * be separated by slash or back slash characters. So if the descendant name contains slashes, this method may
     * return invalid results.
     * </p>
     *
     * <p>
     * Note:
     * <li>Heading or trailing slashes will be ignored.</li>
     * <li>Continuous slashes will be considered as one.</li>
     * Always throws UnsupportedOperationException.
     * </p>
     *
     * @return all the descendants specified by the path, or an empty array if nothing is matched.
     * @param path
     *            the path used to retrieved descendants.
     * @throws IllegalArgumentException
     *             if the path is null.
     * @throws ConfigurationAccessException
     *             Never thrown, kept for sub-class to use it.
     */
    public ConfigurationObject[] deleteDescendants(String path) throws ConfigurationAccessException {
        throw new UnsupportedOperationException("The deleteDescendants method is not supported.");
    }

    /**
     * <p>
     * Set the property values in the descendants specified by the path. Always throws UnsupportedOperationException.
     * </p>
     *
     * @param path
     *            the path specifying the descendants.
     * @param key
     *            the key of the property
     * @param value
     *            the new value of the property.
     * @throws UnsupportedOperationException
     *             Always thrown.
     * @throws InvalidConfigurationException
     *             Never thrown, kept for sub-class to use it.
     * @throws ConfigurationAccessException
     *             Never thrown, kept for sub-class to use it.
     */
    public void setPropertyValue(String path, String key, Object value) throws InvalidConfigurationException,
        ConfigurationAccessException {
        throw new UnsupportedOperationException("The setPropertyValue method is not supported.");
    }

    /**
     * <p>
     * Set the property values in the descendants specified by the path, Always throws UnsupportedOperationException.
     * </p>
     *
     * @param path
     *            the path specifying the descendants.
     * @param key
     *            the key of the property
     * @param values
     *            the new values of the property.
     * @throws UnsupportedOperationException
     *             Always thrown.
     * @throws InvalidConfigurationException
     *             Never thrown, kept for sub-class to use it.
     * @throws ConfigurationAccessException
     *             Never thrown, kept for sub-class to use it.
     */
    public void setPropertyValues(String path, String key, Object[] values) throws InvalidConfigurationException,
        ConfigurationAccessException {
        throw new UnsupportedOperationException("The setPropertyValues method is not supported.");
    }

    /**
     * <p>
     * Remove the property with given key from the descendants specified by the path, Always throws
     * UnsupportedOperationException.
     * </p>
     *
     * @param path
     *            the path specifying the descendants.
     * @param key
     *            the key of the property
     * @throws UnsupportedOperationException
     *             Always thrown.
     * @throws ConfigurationAccessException
     *             Never thrown, kept for sub-class to use it.
     */
    public void removeProperty(String path, String key) throws ConfigurationAccessException {
        throw new UnsupportedOperationException("The removeProperty method is not supported.");
    }

    /**
     * <p>
     * Clear the property with given key from the descendants specified by the path, Always throws
     * UnsupportedOperationException.
     * </p>
     *
     * @param path
     *            the path specifying the descendants.
     * @throws UnsupportedOperationException
     *             Always thrown.
     * @throws ConfigurationAccessException
     *             Never thrown, kept for sub-class to use it.
     */
    public void clearProperties(String path) throws ConfigurationAccessException {
        throw new UnsupportedOperationException("The clearProperties method is not supported.");
    }

    /**
     * <p>
     * Add a child to the descendants specified by the path, Always throws UnsupportedOperationException.
     * </p>
     *
     * @param path
     *            the path specifying the descendants.
     * @param child
     *            the child to be added.
     * @throws UnsupportedOperationException
     *             Always thrown.
     * @throws InvalidConfigurationException
     *             Never thrown, kept for sub-class to use it.
     * @throws ConfigurationAccessException
     *             Never thrown, kept for sub-class to use it.
     */
    public void addChild(String path, ConfigurationObject child) throws InvalidConfigurationException,
        ConfigurationAccessException {
        throw new UnsupportedOperationException("The addChild method is not supported.");
    }

    /**
     * <p>
     * Remove a child from the descendants specified by the path, Always throws UnsupportedOperationException.
     * </p>
     *
     * @param path
     *            the path specifying the descendants.
     * @param name
     *            the name of child to be removed.
     * @throws UnsupportedOperationException
     *             Always thrown.
     * @throws ConfigurationAccessException
     *             Never thrown, kept for sub-class to use it.
     */
    public void removeChild(String path, String name) throws ConfigurationAccessException {
        throw new UnsupportedOperationException("The removeChild method is not supported.");
    }

    /**
     * <p>
     * Clear all children from the descendants specified by the path. Always throws UnsupportedOperationException.
     * </p>
     *
     * @param path
     *            the path specifying the descendants.
     * @throws UnsupportedOperationException
     *             Always thrown.
     * @throws ConfigurationAccessException
     *             Never thrown, kept for sub-class to use it.
     */
    public void clearChildren(String path) throws ConfigurationAccessException {
        throw new UnsupportedOperationException("The clearChildren method is not supported.");
    }

    /**
     * <p>
     * Process descendants by the given processor. Always throws UnsupportedOperationException.
     * </p>
     *
     * @param path
     *            path to find descendants
     * @param processor
     *            processor used to process all descendants
     * @throws IllegalArgumentException
     *             if any argument is null
     * @throws ConfigurationAccessException
     *             if any error occurs while accessing the configuration
     * @throws ProcessException
     *             propagated from processor
     * @throws UnsupportedOperationException
     *             always throw
     */
    public void processDescendants(String path, Processor processor) throws ConfigurationAccessException,
        ProcessException {
        throw new UnsupportedOperationException("The processDescendants method is not supported.");
    }

    /**
     * <p>
     * Clone the current instance.
     * </p>
     *
     * @return the cloned instance
     */
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            // never occur
            return null;
        }
    }
}
