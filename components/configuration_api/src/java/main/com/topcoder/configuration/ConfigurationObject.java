/*
 * Copyright (C) 2007, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.configuration;

import java.util.Date;

/**
 * A ConfigurationObject is an object which contains configuration information.
 *
 * <p>
 * ConfigurationObject can have zero or more properties associated with it. All the properties are consisted of a
 * String key and a set of values. Property key must be unique in the same ConfigurationObject. The values set can be
 * empty, and can contain any kind of value (including null).
 * </p>
 *
 * <p>
 * ConfigurationObject can also contain zero or more child ConfigurationObject. The children are uniquely identified
 * by their names. There is no restriction on the child-parent relationships in this interface (API definition). Some
 * implementations may only allow tree structure, or only allow DAG, and so on.
 * </p>
 *
 * <p>
 * Methods in this interface can be categorized in two dimensions. One dimension divides methods into two categories:
 * properties operation, and children operation. The other dimension divides methods by the way to search properties
 * and children. The direct way to search properties and children is to use exactly their names. The second way is to
 * use regex expression to match properties or children names, And the third way is to use wildcard match (like in
 * UNIX file system) to find children.
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
 * <em>Usage:</em><br>
 * Create ConfigurationObject:
 * <pre>
 * // create a DefaultConfigurationObject
 * ConfigurationObject defaultCo = new DefaultConfigurationObject(&quot;the name&quot;);
 *
 * // create a SynchronizedConfigurationObject with the inner object
 * ConfigurationObject synchronizedCo = new SynchronizedConfigurationObject(defaultCo);
 *
 * // DefaultConfigurationObject is also can be used as TemplateConfigurationObject
 * TemplateConfigurationObject templateCo = (TemplateConfigurationObject) defaultCo;
 * </pre>
 *
 * Manipulate ConfigurationObject properties:
 * <pre>
 * // set the value, can be null, and the old value will be returned
 * Object[] values = defaultCo.setPropertyValue(&quot;key&quot;, &quot;value&quot;);
 * // set a array of values with the key
 * values = defaultCo.setPropertyValues(&quot;key&quot;, new Object[] {&quot;value1&quot;, &quot;value2&quot;});
 *
 * // check whether a ConfigurationObject contains a key
 * boolean contained = defaultCo.containsProperty(&quot;key&quot;);
 *
 * // get all the values with the key
 * values = defaultCo.getPropertyValues(&quot;key&quot;);
 * // get the first value of the key
 * Object value = defaultCo.getPropertyValue(&quot;key&quot;, Object.class);
 * // get the count of values with the key
 * int count = defaultCo.getPropertyValuesCount(&quot;key&quot;);
 *
 * // remove the values of the key
 * defaultCo.removeProperty(&quot;key&quot;);
 * defaultCo.clearChildren();
 * // get all the keys of properties
 * String[] keys = defaultCo.getAllPropertyKeys();
 * // get the keys with the regex pattern
 * keys = defaultCo.getPropertyKeys(&quot;[a\\*b]&quot;);
 * </pre>
 *
 * Manipulate nested ConfigurationObject:
 * <pre>
 * // add a child
 * DefaultConfigurationObject child = new DefaultConfigurationObject(&quot;child&quot;);
 * defaultCo.addChild(child);
 *
 * // check contains child
 * boolean contained = defaultCo.containsChild(&quot;child&quot;);
 * // get the child by name
 * ConfigurationObject thechild = defaultCo.getChild(&quot;child&quot;);
 * // remove the child by name
 * thechild = defaultCo.removeChild(&quot;child&quot;);
 * // clear the child
 * defaultCo.clearChildren();
 *
 * // get all the children
 * ConfigurationObject[] children = defaultCo.getAllChildren();
 * // get all the children by a regex pattern
 * children = defaultCo.getChildren(&quot;[abc]&quot;);
 * </pre>
 *
 * Manipulate descendants by key aggregation:
 * <pre>
 * // get all the descendants
 * ConfigurationObject[] descendants = defaultCo.getAllDescendants();
 * // find the descendants by a path
 * descendants = defaultCo.findDescendants(&quot;path&quot;);
 * // delete the descendants by a path
 * descendants = defaultCo.deleteDescendants(&quot;path&quot;);
 * // get descendants with the regex pattern
 * descendants = defaultCo.getDescendants(&quot;pattern&quot;);
 * </pre>
 *
 * Use clone and synchronized wrapper:
 * <pre>
 * // clone the ConfigurationObject
 * ConfigurationObject clone = (ConfigurationObject) defaultCo.clone();
 *
 * ConfigurationObject synchronizedCo = new SynchronizedConfigurationObject(clone);
 *
 * SynchronizedConfigurationObject synchronizedClone = (SynchronizedConfigurationObject) synchronizedCo.clone();
 * </pre>
 *
 * Use as TemplateConfigurationObject:
 * <pre>
 * DefaultConfigurationObject child = new DefaultConfigurationObject(&quot;child&quot;);
 * TemplateConfigurationObject templateCo = (TemplateConfigurationObject) defaultCo;
 * // set the property value with a path
 * templateCo.setPropertyValue(&quot;a&quot;, &quot;key&quot;, &quot;value&quot;);
 *
 * // set the property values with a path
 * templateCo.setPropertyValues(&quot;a/b&quot;, &quot;key&quot;, new Object[] {&quot;value&quot;});
 *
 * // remove the property values with a path
 * templateCo.removeProperty(&quot;a*\\/b&quot;, &quot;key&quot;);
 *
 * // clear property with a path
 * templateCo.clearProperties(&quot;path/*c&quot;);
 * // add a child with a path
 * templateCo.addChild(&quot;path&quot;, child);
 * // remove child with a path and child name
 * templateCo.removeChild(&quot;path&quot;, child.getName());
 * // clear children with a path
 * templateCo.clearChildren(&quot;b&quot;);
 *
 * // processDescendants with a path
 * templateCo.processDescendants(&quot;path&quot;, new ProcessorMock());
 * </pre>
 *
 * Retrieval of property values with casting and parsing:
 * <pre>
 * // Create an instance of DefaultConfigurationObject
 *  ConfigurationObject config = new DefaultConfigurationObject(&quot;default&quot;);
 *
 *  // Initialize sample properties
 *  config.setPropertyValues(&quot;ints&quot;, new Object[] {1, 2, 3});
 *  config.setPropertyValues(&quot;strings&quot;, new Object[] {&quot;abc&quot;, &quot;def&quot;});
 *  config.setPropertyValue(&quot;intValue1&quot;, 5);
 *  config.setPropertyValue(&quot;intValue2&quot;, &quot;5&quot;);
 *  config.setPropertyValue(&quot;longValue&quot;, &quot;12345&quot;);
 *  config.setPropertyValue(&quot;doubleValue&quot;, &quot;1.23&quot;);
 *  Calendar calendar = Calendar.getInstance();
 *  calendar.clear();
 *  calendar.set(2011, 0 1);
 *  config.setPropertyValue(&quot;dateValue1&quot;, calendar.getTime());
 *  config.setPropertyValue(&quot;dateValue2&quot;, &quot;2011-01-01&quot;);
 *  config.setPropertyValue(&quot;class&quot;, &quot;java.lang.Integer&quot;);
 *
 *  // Retrieve the property values as integer array
 *  Integer[] intValues = config.getPropertyValues(&quot;ints&quot;, Integer.class);
 *  // intValues must contain {1, 2, 3}
 *
 *  // Retrieve the property values as string array
 *  String[] stringValues = config.getPropertyValues(&quot;strings&quot;, String.class);
 *  // stringValues must contain {&quot;abc&quot;, &quot;def&quot;}
 *
 *  // Retrieve the integer property value (without parsing support)
 *  Integer intValue = config.getPropertyValue(&quot;intValue1&quot; , Integer.class);
 *  // intValue must be equal to 5
 *
 *  // Retrieve the integer property value by parsing it from string
 *  intValue = config.getIntegerProperty(&quot;intValue2&quot;, true);
 *  // intValue must be equal to 5
 *
 *  // Retrieve the long property value by parsing it from string
 *  Long longValue = config.getLongProperty(&quot;longValue&quot;, true);
 *  // longValue must be equal to 12345
 *
 *  // Retrieve the double property value by parsing it from string
 *  Double doubleValue = config.getDoubleProperty(&quot;doubleValue&quot;, true);
 *  // doubleValue must be equal to 1.23
 *
 *  // Retrieve the date property stored as Date
 *  Date dateValue1 = config.getDateProperty(&quot;dateValue1&quot;, &quot;yyyy-MM-dd&quot;, true);
 *
 *  // Retrieve the date property stored as String
 *  Date dateValue2 = config.getDateProperty(&quot;dateValue2&quot;, &quot;yyyy-MM-dd&quot;, true);
 *
 *  // dateValue1.getTime() must be equal to dateValue2.getTime()
 *  // Both dates must represent 2011-01-01
 *
 *  // Retrieve the class property value
 *  Class&lt;?&gt; clazz = config.getClassProperty(&quot;class&quot;, true);
 *  // clazz must be equal to Integer.class
 * </pre>
 *
 * Retrieval of required property values:
 * <pre>
 * // Create an instance of DefaultConfigurationObject
 * ConfigurationObject config = new DefaultConfigurationObject(&quot;default&quot;);
 *
 * // Retrieve optional not existing property
 * Object value = config.getPropertyValue(&quot;key1&quot;, Object.class, false);
 * // value must be equal to null
 *
 * try {
 *     // Retrieve required, but not existing property
 *     value = config.getPropertyValue(&quot;key2&quot;, Object.class, true);
 *     // PropertyNotFoundException must be thrown here
 * } catch (PropertyNotFoundException e) {
 *     // Ignore
 * }
 * </pre>
 *
 * </p>
 *
 * <p>
 * Thread safe: All the implementation should treat the thread safe problem by their own.
 * </p>
 *
 * @author maone, haozhangr, saarixx, sparemax
 * @version 1.1
 */
public interface ConfigurationObject extends Cloneable {
    /**
     * Get the name of the ConfigurationObject.
     *
     * @return the name
     * @throws ConfigurationAccessException
     *             if any error occurs while retrieving name.
     */
    public String getName() throws ConfigurationAccessException;

    /**
     * Determine whether property with given key exists in the configuration object.
     *
     * @return true if the specified property is contained, false otherwise.
     * @param key
     *            the key of property
     * @throws IllegalArgumentException
     *             if key is null or empty
     * @throws ConfigurationAccessException
     *             if any error occurs while query the property.
     */
    public boolean containsProperty(String key) throws ConfigurationAccessException;

    /**
     * Get the property value for the given key.
     * <p>
     * Note:if the key contains more than one values, the value returned depends on specific implementation.
     * </p>
     *
     * @return the value associated with the key, or null if there is no such key.
     * @param key
     *            the key of property.
     * @throws IllegalArgumentException
     *             if key is null or empty
     * @throws ConfigurationAccessException
     *             if any error occurs while retrieve the property value.
     */
    public Object getPropertyValue(String key) throws ConfigurationAccessException;

    /**
     * Get the property value for the given key.
     * <p>
     * Note:if the key contains more than one values, the value returned depends on specific implementation.
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
        ConfigurationAccessException;

    /**
     * Get the number of values contained by the specific property.
     *
     * @return the number of values contained in the property, or -1 if there is no such property.
     * @param key
     *            the key specifying the property
     * @throws IllegalArgumentException
     *             if key is null or empty
     * @throws ConfigurationAccessException
     *             if any error occurs while count the property values.
     */
    public int getPropertyValuesCount(String key) throws ConfigurationAccessException;

    /**
     * Get all the property values associated with given key.
     *
     * @return an array containing all the property values, or null if there is no such property key.
     * @param key
     *            the key used to specify property
     * @throws IllegalArgumentException
     *             if key is null or empty
     * @throws ConfigurationAccessException
     *             if any error occurs while retrieve the property values.
     */
    public Object[] getPropertyValues(String key) throws ConfigurationAccessException;

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
        ConfigurationAccessException;

    /**
     * Gets the property value for the given key. If the property is required, but missing, PropertyNotFoundException
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
     * @throws IllegalArgumentException
     *             if key is null or empty, clazz is null.
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
        PropertyNotFoundException, ConfigurationAccessException;

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
        throws PropertyTypeMismatchException, PropertyNotFoundException, ConfigurationAccessException;

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
        PropertyNotFoundException, ConfigurationAccessException;

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
     * @throws IllegalArgumentException
     *             if key is null or empty.
     * @throws PropertyTypeMismatchException
     *             if value cannot be casted to Long type or parsed from String.
     * @throws PropertyNotFoundException
     *             if property is required, but missing.
     * @throws ConfigurationAccessException
     *             if any error occurs while retrieving the property value.
     *
     * @since 1.1
     */
    public Long getLongProperty(String key, boolean required) throws PropertyTypeMismatchException,
        PropertyNotFoundException, ConfigurationAccessException;

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
     * @throws IllegalArgumentException
     *             if key is null or empty.
     * @throws PropertyTypeMismatchException
     *             if value cannot be casted to Double type or parsed from String.
     * @throws PropertyNotFoundException
     *             if property is required, but missing.
     * @throws ConfigurationAccessException
     *             if any error occurs while retrieving the property value.
     *
     * @since 1.1
     */
    public Double getDoubleProperty(String key, boolean required) throws PropertyTypeMismatchException,
        PropertyNotFoundException, ConfigurationAccessException;

    /**
     * Gets the date/time property value for the given key. If property value has String type, it is parsed as Date
     * using the specified format. If the property is required, but missing, PropertyNotFoundException is thrown.
     *
     * <p>
     * Note, if the key contains more than one value, the value returned depends on specific implementation.
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
        PropertyNotFoundException, ConfigurationAccessException;

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
        PropertyNotFoundException, ConfigurationAccessException;

    /**
     * Set a key/value pair to the configuration object.
     * <p>
     * If the key already exists, old values will be overridden.
     * </p>
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
        ConfigurationAccessException;

    /**
     * Set a key/values pair to the configuration object.
     * <p>
     * If the key already exists, old values will be overridden.
     * </p>
     * NOTE: the order of properties would be kept.
     *
     * @return the old values of the property, or null if the key is new.
     * @param key
     *            the key of property
     * @param values
     *            the values array of the property.
     * @throws IllegalArgumentException
     *             if key is null or empty
     * @throws InvalidConfigurationException
     *             if given key or value is not accepted by the specific implementation.
     * @throws ConfigurationAccessException
     *             if any error occurs while set the property values
     */
    public Object[] setPropertyValues(String key, Object[] values) throws InvalidConfigurationException,
        ConfigurationAccessException;

    /**
     * Remove property with given key.
     * <p>
     * If the key doens't exist, nothing happened.
     * </p>
     *
     * @return the removed property value, or null if no such property.
     * @param key
     *            the key of the property to be removed
     * @throws IllegalArgumentException
     *             if key is null or empty.
     * @throws ConfigurationAccessException
     *             if any error occurs while removing the property.
     */
    public Object[] removeProperty(String key) throws ConfigurationAccessException;

    /**
     * Clear all the properties.
     *
     * @throws ConfigurationAccessException
     *             if any error occurs while clearing properties.
     */
    public void clearProperties() throws ConfigurationAccessException;

    /**
     * Get all the property keys matched the pattern.
     *
     * @return an array of matched property keys.
     * @param pattern
     *            a regular expression used to match property keys.
     * @throws IllegalArgumentException
     *             if pattern is null or an invalid regex.
     * @throws ConfigurationAccessException
     *             if any error occurs while retrieving the property keys.
     */
    public String[] getPropertyKeys(String pattern) throws ConfigurationAccessException;

    /**
     * Get all the property keys contained by the ConfigurationObject.
     *
     * @return an array of all property keys.
     * @throws ConfigurationAccessException
     *             if any error occurs while retrieving the property keys.
     */
    public String[] getAllPropertyKeys() throws ConfigurationAccessException;

    /**
     * Determine whether it contains a child ConfigurationObject with given name.
     *
     * @return a boolean value indicating whether the specified child is contained.
     * @param name
     *            the name of the child
     * @throws IllegalArgumentException
     *             if name is null or empty
     * @throws ConfigurationAccessException
     *             if any error occurs while querying with given name.
     */
    public boolean containsChild(String name) throws ConfigurationAccessException;

    /**
     * Get a child ConfigurationObject with given name.
     *
     * @return the child ConfigurationObject, or null if there no such child name.
     * @param name
     *            the name of the child
     * @throws IllegalArgumentException
     *             if name is null or empty
     * @throws ConfigurationAccessException
     *             if any error occurs while retrieving the child.
     */
    public ConfigurationObject getChild(String name) throws ConfigurationAccessException;

    /**
     * Add a child.
     * <p>
     * If there is already a child with such name, the old one will be removed.
     * </p>
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
        ConfigurationAccessException;

    /**
     * Remove a child with given name.
     * <p>
     * If no child with such name, return null.
     * </p>
     *
     * @return the removed child, or null if there is no such name.
     * @param name
     *            the name of the child to be removed.
     * @throws IllegalArgumentException
     *             if name is null or empty.
     * @throws ConfigurationAccessException
     *             if any error occurs while adding the child.
     */
    public ConfigurationObject removeChild(String name) throws ConfigurationAccessException;

    /**
     * Clear all the children resides in the ConfigurationObject.
     *
     * @throws ConfigurationAccessException
     *             if any error occurs while clearing nested children.
     */
    public void clearChildren() throws ConfigurationAccessException;

    /**
     * Get all the children whose names are matched to the given pattern.
     *
     * @return the matched children
     * @param pattern
     *            a regular expression used to match children names.
     * @throws IllegalArgumentException
     *             if pattern is null, or an incorrect regex.
     * @throws ConfigurationAccessException
     *             if any error occurs while getting children.
     */
    public ConfigurationObject[] getChildren(String pattern) throws ConfigurationAccessException;

    /**
     * Get all the child names contained in it.
     *
     * @return all the children names.
     * @throws ConfigurationAccessException
     *             if any error occurs while getting names.
     */
    public String[] getAllChildrenNames() throws ConfigurationAccessException;

    /**
     * Get all the children contained in it.
     *
     * @return all the children.
     * @throws ConfigurationAccessException
     *             if any error occurs while getting children.
     */
    public ConfigurationObject[] getAllChildren() throws ConfigurationAccessException;

    /**
     * Get all descendants of this ConfigurationObject. (not including itself)
     *
     * @return an array of Descendants.
     * @throws ConfigurationAccessException
     *             if any error occurs while retrieving descendants.
     */
    public ConfigurationObject[] getAllDescendants() throws ConfigurationAccessException;

    /**
     * Get all descendants, whose name matched to given pattern, of this ConfigurationObject. (not including itself)
     *
     * @return an array of matched Descendants.
     * @param pattern
     *            a regex used to match descendants' names.
     * @throws IllegalArgumentException
     *             if given pattern is null, or an incorrect pattern.
     * @throws ConfigurationAccessException
     *             if any error occurs while retrieving descendants.
     */
    public ConfigurationObject[] getDescendants(String pattern) throws ConfigurationAccessException;

    /**
     * Get descendants specified by the given path.
     *
     * @return all the descendants specified by the path, or an empty array if nothing is matched.
     * @param path
     *            the path used to retrieved descendants.
     * @throws IllegalArgumentException
     *             if the path is null.
     * @throws ConfigurationAccessException
     *             if any error occurs while finding descendants.
     */
    public ConfigurationObject[] findDescendants(String path) throws ConfigurationAccessException;

    /**
     * Delete descendants specified by the given path.
     *
     * @return all the descendants specified by the path, or an empty array if nothing is matched.
     * @param path
     *            the path used to retrieved descendants.
     * @throws IllegalArgumentException
     *             if the path is null.
     * @throws ConfigurationAccessException
     *             if any error occurs while deleting
     */
    public ConfigurationObject[] deleteDescendants(String path) throws ConfigurationAccessException;

    /**
     * Set the property values in the descendants specified by the path.
     *
     * @param path
     *            the path specifying the descendants.
     * @param key
     *            the key of the property
     * @param value
     *            the new value of the property.
     * @throws IllegalArgumentException
     *             if any path or key is null, or key is empty.
     * @throws InvalidConfigurationException
     *             if given key or value is not accepted by the specific implementation.
     * @throws ConfigurationAccessException
     *             if any other error occurs.
     */
    public void setPropertyValue(String path, String key, Object value) throws InvalidConfigurationException,
        ConfigurationAccessException;

    /**
     * Set the property values in the descendants specified by the path.
     *
     * @param path
     *            the path specifying the descendants.
     * @param key
     *            the key of the property
     * @param values
     *            the new values of the property.
     * @throws IllegalArgumentException
     *             if path or key is null, or key is empty.
     * @throws InvalidConfigurationException
     *             if given key or value is not accepted by the specific implementation.
     * @throws ConfigurationAccessException
     *             if any other error occurs.
     */
    public void setPropertyValues(String path, String key, Object[] values) throws InvalidConfigurationException,
        ConfigurationAccessException;

    /**
     * Remove the property with given key from the descendants specified by the path.
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
    public void removeProperty(String path, String key) throws ConfigurationAccessException;

    /**
     * Clear the properties from all the descendants specified by the path.
     *
     * @param path
     *            the path specifying the descendants.
     * @throws ConfigurationAccessException
     *             if any other error occurs.
     */
    public void clearProperties(String path) throws ConfigurationAccessException;

    /**
     * Add a child to the descendants specified by the path.
     *
     * @param path
     *            the path specifying the descendants.
     * @param child
     *            the child to be added.
     * @throws IllegalArgumentException
     *             if any argument is null
     * @throws InvalidConfigurationException
     *             if given child is not accepted by the specific implementation.
     * @throws ConfigurationAccessException
     *             if any other error occurs.
     */
    public void addChild(String path, ConfigurationObject child) throws InvalidConfigurationException,
        ConfigurationAccessException;

    /**
     * Remove a child from the descendants specified by the path.
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
    public void removeChild(String path, String name) throws ConfigurationAccessException;

    /**
     * Clear all children from the descendants specified by the path.
     *
     * @param path
     *            the path specifying the descendants.
     * @throws IllegalArgumentException
     *             if any argument is null.
     * @throws ConfigurationAccessException
     *             if any other error occurs.
     */
    public void clearChildren(String path) throws ConfigurationAccessException;

    /**
     * Process descendants by the given processor.
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
        ProcessException;

    /**
     * <p>
     * Clone the current instance.
     * </p>
     *
     * @return the cloned instance
     */
    public Object clone();
}
