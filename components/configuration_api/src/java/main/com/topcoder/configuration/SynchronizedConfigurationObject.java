/*
 * Copyright (C) 2007, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.configuration;

import java.util.Date;

/**
 * <p>
 * A synchronized wrapper of any ConfigurationObject. Every method call should be synchronized on the inner configObj
 * before delegating to the inner ConfigurationObject. But please note that, this wrapper can only ensure the methods
 * declared in ConfigurationObject work together thread safely. Because only these methods are synchronized. And
 * extension of this class should lock the inner configObj to ensure thread safe.
 * </p>
 *
 * <p>
 * The inner ConfigurationObject can be accessed by protected getter to let subclasses to use them.
 * </p>
 *
 * <p>
 * <em>Changes in 1.1:</em>
 * <ol>
 * <li>Added getPropertyValue() overload with generic parameter that is expected to return value of specific type.</li>
 * <li>Added getPropertyValues() overload with generic parameter that is expected to return array of specific type.</li>
 * <li>Added getPropertyValue() and getPropertyValues() overloads that accept "required" parameter and throw
 * exception if required property is missing.</li>
 * <li>Added getXXXProperty() methods that additionally can perform parsing of Integer, Long, Double, Date and Class
 * instance from String property values.</li>
 * </ol>
 * </p>
 *
 * <p>
 * <em>Usage:</em> See <strong>Usage</strong> section of {@link ConfigurationObject}.
 * </p>
 *
 * <p>
 * Thread safe: this class is thread safe. All the methods are synchronized on the same object.
 * </p>
 *
 * @author maone, haozhangr, saarixx, sparemax
 * @version 1.1
 */
public class SynchronizedConfigurationObject implements ConfigurationObject {
    /**
     * <p>
     * Represents the inner ConfigurationObject. This class provides a synchronized wrapper to this configuration
     * object. All the methods calls are delegated to this config obj.
     * </p>
     *
     * <p>
     * It is initialized in the constructor, and never changed later. It cann't be null. It can be accessed by
     * protected getter.
     * </p>
     *
     */
    private final ConfigurationObject configObj;

    /**
     * <p>
     * Constructor with the inner ConfigurationObject instance.
     * </p>
     *
     * @param configObj
     *            A ConfigurationObject instance to be wrapped
     * @throws IllegalArgumentException
     *             if configObj is null.
     */
    public SynchronizedConfigurationObject(ConfigurationObject configObj) {
        Helper.checkNull(configObj, "configObj");
        this.configObj = configObj;
    }

    /**
     * <p>
     * A protected getter for inner configObj field. This method is provided for sub-classes to use the inner
     * ConfigurationObject.
     * </p>
     *
     * @return the inner ConfigurationObject instance
     */
    protected ConfigurationObject getInnerConfigurationObject() {
        return configObj;
    }

    /**
     * <p>
     * Thread safe implementation of getName method.
     * </p>
     *
     * @return the name of the inner ConfigurationObject
     * @throws ConfigurationAccessException
     *             if any error occurs while retrieving name.
     */
    public String getName() throws ConfigurationAccessException {
        synchronized (configObj) {
            return configObj.getName();
        }
    }

    /**
     * Thread safe implementation of containsProperty method.
     *
     * @return true if the specified property is contained, false otherwise.
     * @param key
     *            the key of property
     * @throws IllegalArgumentException
     *             if key is null or empty
     * @throws ConfigurationAccessException
     *             if any error occurs while query the property.
     */
    public boolean containsProperty(String key) throws ConfigurationAccessException {
        synchronized (configObj) {
            return configObj.containsProperty(key);
        }
    }

    /**
     * Thread safe implementation of getPropertyValue method.
     *
     * @return the value associated with the key, or null if there is no such key.
     * @param key the key of property.
     * @throws IllegalArgumentException if key is null or empty
     * @throws ConfigurationAccessException if any error occurs while retrieve the property value.
     */
    public Object getPropertyValue(String key)
        throws ConfigurationAccessException {
        synchronized (configObj) {
            return configObj.getPropertyValue(key);
        }
    }

    /**
     * Thread safe implementation of getPropertyValue method.
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
     * @throws IllegalArgumentException
     *             if key is null or empty, clazz is null.
     * @throws PropertyTypeMismatchException
     *             if value cannot be casted to the desired type.
     * @throws ConfigurationAccessException
     *             if any error occurs while retrieve the property value.
     *
     * @since 1.1
     */
    public <T> T getPropertyValue(String key, Class<T> clazz) throws PropertyTypeMismatchException,
        ConfigurationAccessException {
        synchronized (configObj) {
            return configObj.getPropertyValue(key, clazz);
        }
    }

    /**
     * Thread safe implementation of getPropertValuesCount method.
     *
     * @return the number of values contained in the property, or -1 if there is no such property.
     * @param key
     *            the key specifying the property
     * @throws IllegalArgumentException
     *             if key is null or empty
     * @throws ConfigurationAccessException
     *             if any error occurs while count the property values.
     */
    public int getPropertyValuesCount(String key) throws ConfigurationAccessException {
        synchronized (configObj) {
            return configObj.getPropertyValuesCount(key);
        }
    }

    /**
     * Thread safe implementation of getPropertyValues method.
     *
     * @return an array containing all the property values, or null if there is no such property key.
     * @param key
     *            the key used to specify property
     * @throws IllegalArgumentException
     *             if key is null or empty
     * @throws ConfigurationAccessException
     *             if any error occurs while retrieve the property values.
     */
    public Object[] getPropertyValues(String key) throws ConfigurationAccessException {
        synchronized (configObj) {
            return configObj.getPropertyValues(key);
        }
    }

    /**
     * <p>
     * Thread safe implementation of getPropertyValues method.
     * </p>
     *
     * @param <T>
     *            the expected type of the property value.
     * @param key
     *            the key used to specify property.
     * @param clazz
     *            the expected type of each property value.
     *
     * @return an array containing all the property values, or null if there is no such property key.
     *
     * @throws IllegalArgumentException
     *             if key is null or empty, clazz is null.
     * @throws PropertyTypeMismatchException
     *             if value cannot be casted to the desired type.
     * @throws ConfigurationAccessException
     *             if any error occurs while retrieving the property values.
     *
     * @since 1.1
     */
    public <T> T[] getPropertyValues(String key, Class<T> clazz) throws PropertyTypeMismatchException,
        ConfigurationAccessException {
        synchronized (configObj) {
            return configObj.getPropertyValues(key, clazz);
        }
    }

    /**
     * Thread safe implementation of getPropertyValue method.
     *
     * @param <T>
     *            the expected type of the property value.
     * @param key
     *            the key of property.
     * @param clazz
     *            the expected type of each property value.
     * @param required
     *            true if property is required, false otherwise.
     *
     * @return the value associated with the key, or null if there is no such key and property is not required.
     *
     * @throws IllegalArgumentException
     *             if key is null or empty.
     * @throws PropertyTypeMismatchException
     *             if value cannot be casted to the desired type.
     * @throws PropertyNotFoundException
     *             if property is required, but missing.
     * @throws ConfigurationAccessException
     *             if any error occurs while retrieving the property value.
     *
     * @since 1.1
     */
    public <T> T getPropertyValue(String key, Class<T> clazz, boolean required) throws PropertyTypeMismatchException,
        PropertyNotFoundException, ConfigurationAccessException {
        synchronized (configObj) {
            return configObj.getPropertyValue(key, clazz, required);
        }
    }

    /**
     * Thread safe implementation of getPropertyValues method.
     *
     * @param <T>
     *            the expected type of the property value.
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
     * @throws IllegalArgumentException
     *             if key is null or empty, clazz is null.
     * @throws PropertyTypeMismatchException
     *             if value cannot be casted to the desired type.
     * @throws PropertyNotFoundException
     *             if property is required, but missing.
     * @throws ConfigurationAccessException
     *             if any error occurs while retrieving the property values.
     *
     * @since 1.1
     */
    public <T> T[] getPropertyValues(String key, Class<T> clazz, boolean required)
        throws PropertyTypeMismatchException, PropertyNotFoundException, ConfigurationAccessException {
        synchronized (configObj) {
            return configObj.getPropertyValues(key, clazz, required);
        }
    }

    /**
     * Thread safe implementation of getIntegerProperty method.
     *
     * @param key
     *            the key of property.
     * @param required
     *            true if property is required, false otherwise.
     *
     * @return the integer value associated with the key, or null if there is no such key and property is not
     *         required.
     *
     * @throws IllegalArgumentException
     *             if key is null or empty.
     * @throws PropertyTypeMismatchException
     *             if value cannot be casted to Integer type or parsed from String.
     * @throws PropertyNotFoundException
     *             if property is required, but missing.
     * @throws ConfigurationAccessException
     *             if any error occurs while retrieving the property value.
     *
     * @since 1.1
     */
    public Integer getIntegerProperty(String key, boolean required) throws PropertyTypeMismatchException,
        PropertyNotFoundException, ConfigurationAccessException {
        synchronized (configObj) {
            return configObj.getIntegerProperty(key, required);
        }
    }

    /**
     * Thread safe implementation of getLongProperty method.
     *
     * @param key
     *            the key of property.
     * @param required
     *            true if property is required, false otherwise.
     *
     * @return the long integer value associated with the key, or null if there is no such key and property is not
     *         required.
     *
     * @throws IllegalArgumentException
     *             if key is null or empty.
     * @throws PropertyTypeMismatchException
     *             if value cannot be casted to Long type or parsed from String.
     * @throws PropertyNotFoundException
     *             if property is required, but missing.
     * @throws ConfigurationAccessException
     *             if any error occurs while retrieving the property value.
     */
    public Long getLongProperty(String key, boolean required) throws PropertyTypeMismatchException,
        PropertyNotFoundException, ConfigurationAccessException {
        synchronized (configObj) {
            return configObj.getLongProperty(key, required);
        }
    }

    /**
     * Thread safe implementation of getDoubleProperty method.
     *
     * @param key
     *            the key of property.
     * @param required
     *            true if property is required, false otherwise.
     *
     * @return the double value associated with the key, or null if there is no such key and property is not required.
     *
     * @throws IllegalArgumentException
     *             if key is null or empty.
     * @throws PropertyTypeMismatchException
     *             if value cannot be casted to Double type or parsed from String.
     * @throws PropertyNotFoundException
     *             if property is required, but missing.
     * @throws ConfigurationAccessException
     *             if any error occurs while retrieving the property value.
     */
    public Double getDoubleProperty(String key, boolean required) throws PropertyTypeMismatchException,
        PropertyNotFoundException, ConfigurationAccessException {
        synchronized (configObj) {
            return configObj.getDoubleProperty(key, required);
        }
    }

    /**
     * <p>
     * Thread safe implementation of getDateProperty method.
     * </p>
     *
     * @param key
     *            the key of property.
     * @param required
     *            true if property is required, false otherwise.
     * @param format
     *            the expected date/time format string.
     *
     * @return the date/time value associated with the key, or null if there is no such key and property is not
     *         required.
     *
     * @throws IllegalArgumentException
     *             if key is null or empty, format is null/empty or invalid date/time format.
     * @throws PropertyTypeMismatchException
     *             if value cannot be casted to Date type or parsed from String.
     * @throws PropertyNotFoundException
     *             if property is required, but missing.
     * @throws ConfigurationAccessException
     *             if any error occurs while retrieving the property value.
     *
     * @since 1.1
     */
    public Date getDateProperty(String key, String format, boolean required) throws PropertyTypeMismatchException,
        PropertyNotFoundException, ConfigurationAccessException {
        synchronized (configObj) {
            return configObj.getDateProperty(key, format, required);
        }
    }

    /**
     * Thread safe implementation of getClassProperty method.
     *
     * @param key
     *            the key of property.
     * @param required
     *            true if property is required, false otherwise.
     *
     * @return the class value associated with the key, or null if there is no such key and property is not required.
     *
     * @throws IllegalArgumentException
     *             if key is null or empty.
     * @throws PropertyTypeMismatchException
     *             if value cannot be casted to Class type or extracted from String as full class name.
     * @throws PropertyNotFoundException
     *             if property is required, but missing.
     * @throws ConfigurationAccessException
     *             if any error occurs while retrieving the property value.
     *
     * @since 1.1
     */
    public Class<?> getClassProperty(String key, boolean required) throws PropertyTypeMismatchException,
        PropertyNotFoundException, ConfigurationAccessException {
        synchronized (configObj) {
            return configObj.getClassProperty(key, required);
        }
    }

    /**
     * Thread safe implementation of setPropertyValue method.
     *
     * @return the old values of the property, or null if the key is new.
     * @param key
     *            the key of property
     * @param value
     *            the value of the property.
     * @throws IllegalArgumentException
     *             if key is null or empty
     * @throws InvalidConfigurationException
     *             if given key or value is not accepted by the specific implementation.
     * @throws ConfigurationAccessException
     *             if any error occurs while set the property value
     */
    public Object[] setPropertyValue(String key, Object value) throws InvalidConfigurationException,
        ConfigurationAccessException {
        synchronized (configObj) {
            return configObj.setPropertyValue(key, value);
        }
    }

    /**
     * Thread safe implementation of setPropertyValues method.
     *
     * @return the old values of the property, or null if the key is new.
     * @param key
     *            the key of property
     * @param values
     *            the array of value Object to be set
     * @throws IllegalArgumentException
     *             if key is null or empty
     * @throws InvalidConfigurationException
     *             if given key or value is not accepted by the specific implementation.
     * @throws ConfigurationAccessException
     *             if any error occurs while set the property values
     */
    public Object[] setPropertyValues(String key, Object[] values) throws InvalidConfigurationException,
        ConfigurationAccessException {
        synchronized (configObj) {
            return configObj.setPropertyValues(key, values);
        }
    }

    /**
     * Thread safe implementation of removeProperty method.
     *
     * @return the removed property value, or null if no such property.
     * @param key
     *            the key of the property to be removed
     * @throws IllegalArgumentException
     *             if key is null or empty.
     * @throws ConfigurationAccessException
     *             if any error occurs while removing the property.
     */
    public Object[] removeProperty(String key) throws ConfigurationAccessException {
        synchronized (configObj) {
            return configObj.removeProperty(key);
        }
    }

    /**
     * Thread safe implementation of clearProperties method.
     *
     * @throws ConfigurationAccessException
     *             if any error occurs while clearing properties.
     */
    public void clearProperties() throws ConfigurationAccessException {
        synchronized (configObj) {
            configObj.clearProperties();
        }
    }

    /**
     * Thread safe implementation of getPropertyKeys method.
     *
     * @return an array of matched property keys.
     * @param pattern
     *            a regular expression used to match property keys.
     * @throws IllegalArgumentException
     *             if pattern is null or an invalid regex.
     * @throws ConfigurationAccessException
     *             if any error occurs while retrieving the property keys.
     */
    public String[] getPropertyKeys(String pattern) throws ConfigurationAccessException {
        synchronized (configObj) {
            return configObj.getPropertyKeys(pattern);
        }
    }

    /**
     * Thread safe implementation of getAllPropertyKeys method.
     *
     * @return an array of all property keys.
     * @throws ConfigurationAccessException
     *             if any error occurs while retrieving the property keys.
     */
    public String[] getAllPropertyKeys() throws ConfigurationAccessException {
        synchronized (configObj) {
            return configObj.getAllPropertyKeys();
        }
    }

    /**
     * Thread safe implementation of containsChild method.
     *
     * @return a boolean value indicating whether the specified child is contained.
     * @param name
     *            the name of the child
     * @throws IllegalArgumentException
     *             if name is null or empty
     * @throws ConfigurationAccessException
     *             if any error occurs while querying with given name.
     */
    public boolean containsChild(String name) throws ConfigurationAccessException {
        synchronized (configObj) {
            return configObj.containsChild(name);
        }
    }

    /**
     * Thread safe implementation of getChild method.
     *
     * @return the child ConfigurationObject, or null if there no such child name.
     * @param name
     *            the name of the child
     * @throws IllegalArgumentException
     *             if name is null or empty
     * @throws ConfigurationAccessException
     *             if any error occurs while retrieving the child.
     */
    public ConfigurationObject getChild(String name) throws ConfigurationAccessException {
        synchronized (configObj) {
            return configObj.getChild(name);
        }
    }

    /**
     * Thread safe implementation of addChild method.
     *
     * @return the old child with given name, or null if the name is new.
     * @param child
     *            the child to be added.
     * @throws IllegalArgumentException
     *             if child argument is null.
     * @throws InvalidConfigurationException
     *             if given child is not accepted by the specific implementation.
     * @throws ConfigurationAccessException
     *             if any error occurs while adding the child.
     */
    public ConfigurationObject addChild(ConfigurationObject child) throws InvalidConfigurationException,
        ConfigurationAccessException {
        synchronized (configObj) {
            return configObj.addChild(child);
        }
    }

    /**
     * Thread safe implementation of removeChild method.
     *
     * @return the removed child, or null if there is no such name.
     * @param name
     *            the name of the child to be removed.
     * @throws IllegalArgumentException
     *             if name is null or empty.
     * @throws ConfigurationAccessException
     *             if any error occurs while adding the child.
     */
    public ConfigurationObject removeChild(String name) throws ConfigurationAccessException {
        synchronized (configObj) {
            return configObj.removeChild(name);
        }
    }

    /**
     * Thread safe implementation of clearChildren method.
     *
     * @throws ConfigurationAccessException
     *             if any error occurs while clearing nested children.
     */
    public void clearChildren() throws ConfigurationAccessException {
        synchronized (configObj) {
            configObj.clearChildren();
        }
    }

    /**
     * Thread safe implementation of getChildren method.
     *
     * @return the matched children
     * @param pattern
     *            a regular expression used to match children names.
     * @throws IllegalArgumentException
     *             if pattern is null, or an incorrect regex.
     * @throws ConfigurationAccessException
     *             if any error occurs while getting children.
     */
    public ConfigurationObject[] getChildren(String pattern) throws ConfigurationAccessException {
        synchronized (configObj) {
            return configObj.getChildren(pattern);
        }
    }

    /**
     * Thread safe implementation of getAllChildrenNames method.
     *
     * @return all the children names.
     * @throws ConfigurationAccessException
     *             if any error occurs while getting names.
     */
    public String[] getAllChildrenNames() throws ConfigurationAccessException {
        synchronized (configObj) {
            return configObj.getAllChildrenNames();
        }
    }

    /**
     * Thread safe implementation of getAllChildren method.
     *
     * @return all the children.
     * @throws ConfigurationAccessException
     *             if any error occurs while getting children.
     */
    public ConfigurationObject[] getAllChildren() throws ConfigurationAccessException {
        synchronized (configObj) {
            return configObj.getAllChildren();
        }
    }

    /**
     * Thread safe implementation of getAllDescendants method.
     *
     * @return an array of Descendants.
     * @throws ConfigurationAccessException
     *             if any error occurs while retrieving descendants.
     */
    public ConfigurationObject[] getAllDescendants() throws ConfigurationAccessException {
        synchronized (configObj) {
            return configObj.getAllDescendants();
        }
    }

    /**
     * Thread safe implementation of getDescendants method.
     *
     * @return an array of matched Descendants.
     * @param pattern
     *            a regex used to match descendants' names.
     * @throws IllegalArgumentException
     *             if given pattern is null, or an incorrect pattern.
     * @throws ConfigurationAccessException
     *             if any error occurs while retrieving descendants.
     */
    public ConfigurationObject[] getDescendants(String pattern) throws ConfigurationAccessException {
        synchronized (configObj) {
            return configObj.getDescendants(pattern);
        }
    }

    /**
     * Thread safe implementation of findDescendants method.
     *
     * @return all the descendants specified by the path, or an empty array if nothing is matched.
     * @param path
     *            the path used to retrieved descendants.
     * @throws IllegalArgumentException
     *             if the path is null.
     * @throws ConfigurationAccessException
     *             if any error occurs while finding descendants.
     */
    public ConfigurationObject[] findDescendants(String path) throws ConfigurationAccessException {
        synchronized (configObj) {
            return configObj.findDescendants(path);
        }
    }

    /**
     * Thread safe implementation of deleteDescendants method.
     *
     * @return all the descendants specified by the path, or an empty array if nothing is matched.
     * @param path
     *            the path used to retrieved descendants.
     * @throws IllegalArgumentException
     *             if the path is null.
     * @throws ConfigurationAccessException
     *             if any error occurs while deleting
     */
    public ConfigurationObject[] deleteDescendants(String path) throws ConfigurationAccessException {
        synchronized (configObj) {
            return configObj.deleteDescendants(path);
        }
    }

    /**
     * Thread safe implementation of setPropertyValue method.
     *
     * @param path
     *            the path specifying the descendants.
     * @param key
     *            the key of the property
     * @param value
     *            the new value of the property.
     * @throws IllegalArgumentException
     *             if key or path is null, or key is empty.
     * @throws InvalidConfigurationException
     *             if given key or value is not accepted by the specific implementation.
     * @throws ConfigurationAccessException
     *             if any other error occurs.
     */
    public void setPropertyValue(String path, String key, Object value) throws InvalidConfigurationException,
        ConfigurationAccessException {
        synchronized (configObj) {
            configObj.setPropertyValue(path, key, value);
        }
    }

    /**
     * Thread safe implementation of setPropertyValues method.
     *
     * @param path
     *            the path specifying the descendants.
     * @param key
     *            the key of the property
     * @param values
     *            the new values of the property.
     * @throws IllegalArgumentException
     *             if key or path is null, or key is empty.
     * @throws InvalidConfigurationException
     *             if given key or value is not accepted by inner Object.
     * @throws ConfigurationAccessException
     *             if any other error occurs.
     */
    public void setPropertyValues(String path, String key, Object[] values) throws InvalidConfigurationException,
        ConfigurationAccessException {
        synchronized (configObj) {
            configObj.setPropertyValues(path, key, values);
        }
    }

    /**
     * Thread safe implementation of removeProperty method.
     *
     * @param path
     *            the path specifying the descendants.
     * @param key
     *            the key of the property
     * @throws IllegalArgumentException
     *             if any argument is null, or key is empty.
     * @throws ConfigurationAccessException
     *             if any other error occurs.
     */
    public void removeProperty(String path, String key) throws ConfigurationAccessException {
        synchronized (configObj) {
            configObj.removeProperty(path, key);
        }
    }

    /**
     * Thread safe implementation of clearProperties method.
     *
     * @param path
     *            the path specifying the descendants.
     * @throws ConfigurationAccessException
     *             if any other error occurs.
     */
    public void clearProperties(String path) throws ConfigurationAccessException {
        synchronized (configObj) {
            configObj.clearProperties(path);
        }
    }

    /**
     * Thread safe implementation of addChild method.
     *
     * @param path
     *            the path specifying the descendants.
     * @param child
     *            the child to be added.
     * @throws IllegalArgumentException
     *             if any argument is null
     * @throws InvalidConfigurationException
     *             if given key or value is not accepted by the specific implementation.
     * @throws ConfigurationAccessException
     *             if any other error occurs.
     */
    public void addChild(String path, ConfigurationObject child) throws InvalidConfigurationException,
        ConfigurationAccessException {
        synchronized (configObj) {
            configObj.addChild(path, child);
        }
    }

    /**
     * Thread safe implementation of removeChild method.
     *
     * @param path
     *            the path specifying the descendants.
     * @param name
     *            the name of child to be removed.
     * @throws IllegalArgumentException
     *             if any argument is null, or name is empty.
     * @throws ConfigurationAccessException
     *             if any other error occurs.
     */
    public void removeChild(String path, String name) throws ConfigurationAccessException {
        synchronized (configObj) {
            configObj.removeChild(path, name);
        }
    }

    /**
     * Thread safe implementation of clearChildren method.
     *
     * @param path
     *            the path specifying the descendants.
     * @throws IllegalArgumentException
     *             if any argument is null.
     * @throws ConfigurationAccessException
     *             if any other error occurs.
     */
    public void clearChildren(String path) throws ConfigurationAccessException {
        synchronized (configObj) {
            configObj.clearChildren(path);
        }
    }

    /**
     * Thread safe implementation of clone method.
     *
     * @return the clone of the inner object.
     */
    public Object clone() {
        synchronized (configObj) {
            return new SynchronizedConfigurationObject((ConfigurationObject) configObj.clone());
        }
    }

    /**
     * Thread safe implementation of processDescendants method.
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
     */
    public void processDescendants(String path, Processor processor) throws ConfigurationAccessException,
        ProcessException {
        synchronized (configObj) {
            configObj.processDescendants(path, processor);
        }
    }
}
