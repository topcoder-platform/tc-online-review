/*
 * Copyright (C) 2007, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.configuration.defaults;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import com.topcoder.configuration.ConfigurationAccessException;
import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.Helper;
import com.topcoder.configuration.InvalidConfigurationException;
import com.topcoder.configuration.PropertyNotFoundException;
import com.topcoder.configuration.PropertyTypeMismatchException;
import com.topcoder.configuration.TemplateConfigurationObject;

/**
 * <p>
 * It is the main class of this component, also the default implementation of ConfigurationObject. It extends from
 * TemplateConfigurationObject to utilize the implemented methods in it.
 * </p>
 *
 * <p>
 * This class uses a Map in memory to hold properties. The key of Map is a String representing the property key. The
 * value of Map is a List instance containing all the property values(null is allowed).
 * </p>
 *
 * <p>
 * And also a Map in memory is used to hold child ConfigurationObjects. The key of this Map is a String representing
 * the name of child object. And the value is the child instance. The relationship graph of this implementation should
 * always be a DAG.
 * </p>
 *
 * <p>
 * Besides ConfigurationObject interface, this class also implements Serializable and Clonable interfaces. To support
 * Serializable interface, it just ensures that all the coming properties values and child objects are instances of
 * Serializable. To support Clonable interface, because we want to keep DAG structure, a clone overload with a cache
 * parameter is provided. In this case, all the children should be instance of DefaultConfigurationObject.
 * </p>
 *
 * <p>
 * <em>Changes in 1.1:</em>
 * <ol>
 * <li>Specified generic parameters for all generic types.</li>
 * <li>getPropertyValue() and getPropertyValues() methods were updated to use generic parameters for return value
 * casting. "required" parameters were added to these methods.</li>
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
 * Thread safe: This class is mutable and not thread safe.
 * </p>
 *
 * @author maone, haozhangr, saarixx, sparemax
 * @version 1.1
 */
@SuppressWarnings("serial")
public class DefaultConfigurationObject extends TemplateConfigurationObject implements Cloneable, Serializable {
    /**
     * The regex pattern used to split a String by slashes.
     */
    private static final String SPLIT_SLASH_PATTERN = "[\\\\/]";

    /**
     * The name of the configurationObject.
     *
     * <p>
     * It is initialized in the constructor, and never changed later. It can be got from the getter. It shouldn't be
     * null or empty.
     * </p>
     *
     */
    private final String name;

    /**
     * Represents all the properties of this configuration object.
     * <p>
     * The key of map is a String representing the property key. The value of map is a List representing the property
     * values. A single property can contains zero or more values. The elements in the list should be null or
     * Serializable instances. Because null element is allowed, so used List should allow null value, like ArrayList.
     * </p>
     *
     * <p>
     * The key cann't be null or empty. The value of map cann't be null. But the element in the value can be null.
     * </p>
     *
     * <p>
     * The map itself is initialized in the constructor or changed when do clone, actually it should be final, but
     * since the clone need to be deep copy so just remove the final for the clone, it can not be changed in any other
     * way. And the content can be updated by many methods.
     * </p>
     *
     * <p>
     * <em>Changes in 1.1:</em>
     * <ol>
     * <li>Specified generic parameters for the collection.</li>
     * </ol>
     * </p>
     */
    private Map<String, List<Object>> properties;

    /**
     * Represents all the properties of this configuration object.
     * <p>
     * The key of map is a String representing the child name, which cann't be null or empty. The value of map is a
     * ConfigurationObject instance, which cann't be null.
     * </p>
     *
     * <p>
     * The map itself is initialized in the constructor or changed when do clone, actually it should be final, but
     * since the clone need to be deep copy so just remove the final for the clone, it can not be changed in any other
     * way. And the content can be updated by many methods.
     * </p>
     *
     * <p>
     * <em>Changes in 1.1:</em>
     * <ol>
     * <li>Specified generic parameters for the collection.</li>
     * </ol>
     * </p>
     *
     */
    private Map<String, ConfigurationObject> children;

    /**
     * Constructor with the name of the DefaultConfigurationObject.
     *
     * <p>
     * <em>Changes in 1.1:</em>
     * <ol>
     * <li>Used generic types in the code.</li>
     * </ol>
     * </p>
     *
     * @param name
     *            the name of this ConfigurationObject.
     * @throws IllegalArgumentException
     *             if name is null or empty.
     */
    public DefaultConfigurationObject(String name) {
        Helper.checkStringNullOrEmpty(name, "name");
        this.name = name;
        this.properties = new HashMap<String, List<Object>>();
        this.children = new HashMap<String, ConfigurationObject>();
    }

    /**
     * Get the name of the ConfigurationObject.
     *
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Determine whether property with given key exists in the configuration object.
     *
     * @return true if the specified property is contained, false otherwise.
     * @param key
     *            the key of property
     * @throws IllegalArgumentException
     *             if key is null or empty
     */
    public boolean containsProperty(String key) {
        Helper.checkStringNullOrEmpty(key, "key");

        return properties.containsKey(key);
    }

    /**
     * Get the property value for the given key. If the property is required, but missing, PropertyNotFoundException
     * is thrown.
     *
     * <p>
     * Note: if the key contains more than one value, the value returned is the first value.
     * </p>
     *
     * <p>
     * <em>Changes in 1.1:</em>
     * <ol>
     * <li>Added generic parameter "T". Made method return "T" instead of "Object".</li>
     * <li>Added "required" parameter.</li>
     * </ol>
     * </p>
     *
     * @param <T>
     *            the expected type of the property value.
     * @param key
     *            the key of property.
     * @param clazz
     *            the expected type of each property value.
     * @param required
     *            true if property is required, false otherwise
     *
     * @return the first value associated with the key, or null if there is no such key.
     *
     * @throws IllegalArgumentException
     *             if key is null or empty, clazz is null.
     * @throws PropertyTypeMismatchException
     *             if value cannot be casted to the desired type.
     * @throws PropertyNotFoundException
     *             if property is required, but missing.
     */
    public <T> T getPropertyValue(String key, Class<T> clazz, boolean required) throws PropertyTypeMismatchException,
        PropertyNotFoundException {
        Helper.checkStringNullOrEmpty(key, "key");
        Helper.checkNull(clazz, "clazz");

        List<Object> values = properties.get(key);

        if ((values == null) || (values.size() == 0)) {
            if (required) {
                throw new PropertyNotFoundException("The property '" + key + "' is required, but missing.");
            }
            return null;
        }

        try {
            // Get the first value from the list:
            return clazz.cast(values.get(0));
        } catch (ClassCastException e) {
            throw new PropertyTypeMismatchException("The value cannot be casted to the desired type.", e);
        }
    }

    /**
     * Get the number of values contained by the specific property.
     *
     * <p>
     * <em>Changes in 1.1:</em>
     * <ol>
     * <li>Used generic types in the code.</li>
     * </ol>
     * </p>
     *
     * @param key
     *            the key specifying the property
     *
     * @return the number of values contained in the property, or -1 if there is no such property.
     *
     * @throws IllegalArgumentException
     *             if key is null or empty
     */
    public int getPropertyValuesCount(String key) {
        Helper.checkStringNullOrEmpty(key, "key");

        List<Object> values = properties.get(key);

        return (values == null) ? (-1) : values.size();
    }

    /**
     * Get all the property values associated with given key.
     *
     * <p>
     * <em>Changes in 1.1:</em>
     * <ol>
     * <li>Added generic parameter "T". Made method return "T[]" instead of "Object[]".</li>
     * <li>Added parameters "required" and "clazz".</li>
     * </ol>
     * </p>
     *
     * @param <T>
     *            the expected type of the property value.
     * @param key
     *            the key used to specify property.
     * @param clazz
     *            the expected type of each property value.
     * @param required
     *            true if property is required, false otherwise.
     *
     * @return an array containing all the property values, or null if there is no such property key.
     *
     * @throws IllegalArgumentException
     *             if key is null or empty, or clazz is null.
     * @throws PropertyTypeMismatchException
     *             if value cannot be casted to the desired type.
     * @throws PropertyNotFoundException
     *             if property is required, but missing.
     */
    @SuppressWarnings("unchecked")
    public <T> T[] getPropertyValues(String key, Class<T> clazz, boolean required)
        throws PropertyTypeMismatchException, PropertyNotFoundException {
        Helper.checkStringNullOrEmpty(key, "key");
        Helper.checkNull(clazz, "clazz");

        List<Object> values = properties.get(key);

        if (values == null) {
            if (required) {
                throw new PropertyNotFoundException("The property '" + key + "' is required, but missing.");
            }
            return null;
        }
        int valuesNum = values.size();
        // Create array instance:
        T[] result = (T[]) Array.newInstance(clazz, valuesNum);
        for (int i = 0; i < valuesNum; i++) {
            try {
                // Set casted value to the result array
                result[i] = clazz.cast(values.get(i));
            } catch (ClassCastException e) {
                throw new PropertyTypeMismatchException("The value cannot be casted to the desired type.",
                    e);
            }
        }

        return result;
    }

    /**
     * Gets the integer property value for the given key. If property value has String type, it is parsed as integer.
     * If the property is required, but missing, PropertyNotFoundException is thrown.
     *
     * <p>
     * Note: if the key contains more than one value, the value returned is the first value.
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
     *
     * @since 1.1
     */
    public Integer getIntegerProperty(String key, boolean required) throws PropertyTypeMismatchException,
        PropertyNotFoundException {
        Helper.checkStringNullOrEmpty(key, "key");

        Object value = getPropertyValue(key, Object.class, required);
        try {
            if (value instanceof String) {
                // Parse from string
                return Integer.valueOf((String) value);
            }
            return (Integer) value;
        } catch (NumberFormatException e) {
            throw new PropertyTypeMismatchException("The value cannot be parsed from String.", e);
        } catch (ClassCastException e) {
            throw new PropertyTypeMismatchException("The value cannot be casted to Integer type.", e);
        }
    }

    /**
     * Gets the long integer property value for the given key. If property value has String type, it is parsed as long
     * integer. If the property is required, but missing, PropertyNotFoundException is thrown.
     *
     * <p>
     * Note: if the key contains more than one value, the value returned is the first value.
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
     *
     * @since 1.1
     */
    public Long getLongProperty(String key, boolean required) throws PropertyTypeMismatchException,
        PropertyNotFoundException {
        Helper.checkStringNullOrEmpty(key, "key");

        Object value = getPropertyValue(key, Object.class, required);
        try {
            if (value instanceof String) {
                // Parse from string
                return Long.valueOf((String) value);
            }
            return (Long) value;
        } catch (NumberFormatException e) {
            throw new PropertyTypeMismatchException("The value cannot be parsed from String.", e);
        } catch (ClassCastException e) {
            throw new PropertyTypeMismatchException("The value cannot be casted to Long type.", e);
        }
    }

    /**
     * Gets the double property value for the given key. If property value has String type, it is parsed as double. If
     * the property is required, but missing, PropertyNotFoundException is thrown.
     *
     * <p>
     * Note: if the key contains more than one value, the value returned is the first value.
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
     *
     * @since 1.1
     */
    public Double getDoubleProperty(String key, boolean required) throws PropertyTypeMismatchException,
        PropertyNotFoundException {
        Helper.checkStringNullOrEmpty(key, "key");

        Object value = getPropertyValue(key, Object.class, required);
        try {
            if (value instanceof String) {
                // Parse from string
                String valueStr = (String) value;

                ParsePosition parsePosition = new ParsePosition(0);
                Number number = NumberFormat.getInstance(Locale.US).parse(valueStr, parsePosition);
                if (parsePosition.getIndex() != valueStr.length()) {
                    throw new PropertyTypeMismatchException("The value cannot be parsed from String.");
                }
                return number.doubleValue();
            }
            return (Double) value;
        } catch (ClassCastException e) {
            throw new PropertyTypeMismatchException("The value cannot be casted to Double type.", e);
        }
    }

    /**
     * Gets the date/time property value for the given key. If property value has String type, it is parsed as Date
     * using the specified format. If the property is required, but missing, PropertyNotFoundException is thrown.
     *
     * <p>
     * Note: if the key contains more than one value, the value returned is the first value.
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
     *
     * @since 1.1
     */
    public Date getDateProperty(String key, String format, boolean required) throws PropertyTypeMismatchException,
        PropertyNotFoundException {
        Helper.checkStringNullOrEmpty(key, "key");
        Helper.checkStringNullOrEmpty(format, "format");
        DateFormat dateFormat = new SimpleDateFormat(format, Locale.US);

        Object value = getPropertyValue(key, Object.class, required);
        try {
            if (value instanceof String) {
                // Parse from string
                String valueStr = (String) value;

                ParsePosition parsePosition = new ParsePosition(0);
                Date result = dateFormat.parse(valueStr, parsePosition);
                if ((result == null) || (parsePosition.getIndex() != valueStr.length())) {
                    throw new PropertyTypeMismatchException("The value cannot be parsed from String.");
                }
                return result;
            }
            return (Date) value;
        } catch (ClassCastException e) {
            throw new PropertyTypeMismatchException("The value cannot be casted to Date type.", e);
        }
    }

    /**
     * Gets the class property value for the given key. Property value can be Class<?> instance or String with full
     * class name. If the property is required, but missing, PropertyNotFoundException is thrown.
     *
     * <p>
     * Note: if the key contains more than one value, the value returned is the first value.
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
     *
     * @since 1.1
     */
    public Class<?> getClassProperty(String key, boolean required) throws PropertyTypeMismatchException,
        PropertyNotFoundException {
        Helper.checkStringNullOrEmpty(key, "key");

        Object value = getPropertyValue(key, Object.class, required);
        try {
            if (value instanceof String) {
                // Parse from string
                return Class.forName((String) value);
            }
            return (Class<?>) value;
        } catch (ClassNotFoundException e) {
            throw new PropertyTypeMismatchException("The value cannot be parsed from String.", e);
        } catch (ClassCastException e) {
            throw new PropertyTypeMismatchException("The value cannot be casted to Class type.", e);
        }
    }

    /**
     * <p>
     * Set a key/value pair to the configuration object. If the key already exists, old values will be overridden.
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
     *             if given value is not null and not a Serializable instance.
     */
    public Object[] setPropertyValue(String key, Object value) throws InvalidConfigurationException {
        return setPropertyValues(key, new Object[] {value});
    }

    /**
     * Set a key/values pair to the configuration object.
     * <p>
     * If the key already exists, old values will be overridden.
     * </p>
     *
     * <p>
     * <em>Changes in 1.1:</em>
     * <ol>
     * <li>Used generic types in the code.</li>
     * <li>Used the helper method getPropertyValuesHelper.</li>
     * </ol>
     * </p>
     *
     * @return the old values of the property, or null if the key is new.
     * @param key
     *            the key of property
     * @param values
     *            the array of values should be set
     * @throws IllegalArgumentException
     *             if key is null or empty
     * @throws InvalidConfigurationException
     *             if given values contains element which is not null and not a Serializable instance.
     */
    public Object[] setPropertyValues(String key, Object[] values) throws InvalidConfigurationException {
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                checkNullOrSerializable(values[i], "values[" + i + "]");
            }
        }

        // get the old values which should be returned
        Object[] result = getPropertyValuesHelper(key);

        // override the old value with the new one, if the values is null, just using empty
        List<Object> list = (values == null) ? new ArrayList<Object>() : Arrays.asList(values);
        properties.put(key, list);

        return result;
    }

    /**
     * Remove property with given key.
     * <p>
     * If the key doens't exist, nothing happened.
     * </p>
     *
     * <p>
     * <em>Changes in 1.1:</em>
     * <ol>
     * <li>Used generic types in the code.</li>
     * <li>Used the helper method getPropertyValuesHelper.</li>
     * </ol>
     * </p>
     *
     * @return the removed property value, or null if no such property.
     * @param key
     *            the key of the property to be removed
     * @throws IllegalArgumentException
     *             if key is null or empty.
     */
    public Object[] removeProperty(String key) {
        // get the old values which should be returned
        Object[] result = getPropertyValuesHelper(key);
        properties.remove(key);

        return result;
    }

    /**
     * Clear all the properties.
     *
     */
    public void clearProperties() {
        properties.clear();
    }

    /**
     * Get all the property keys matched the pattern.
     *
     * <p>
     * <em>Changes in 1.1:</em>
     * <ol>
     * <li>Used generic types in the code.</li>
     * </ol>
     * </p>
     *
     * @param pattern
     *            a regular expression used to match property keys.
     *
     * @return an array of matched property keys.
     *
     * @throws IllegalArgumentException
     *             if pattern is null or an invalid regex.
     */
    public String[] getPropertyKeys(String pattern) {
        Helper.checkNull(pattern, "pattern");

        List<String> list = new ArrayList<String>();

        // since we should use the pattern several times, pre-compile it is a efficient way
        Pattern p = Pattern.compile(pattern);

        // using the pattern to check each key
        for (Iterator<String> it = properties.keySet().iterator(); it.hasNext();) {
            String key = (String) it.next();

            if (p.matcher(key).matches()) {
                list.add(key);
            }
        }

        return (String[]) list.toArray(new String[list.size()]);
    }

    /**
     * Get all the property keys contained by the ConfigurationObject.
     *
     * @return an array of all property keys.
     */
    public String[] getAllPropertyKeys() {
        return (String[]) properties.keySet().toArray(new String[properties.size()]);
    }

    /**
     * <p>
     * Add a child, If there is already a child with such name, the old one will be removed.
     * </p>
     *
     * @return the old child with given name, or null if the name is new.
     * @param child
     *            the child to be added.
     * @throws IllegalArgumentException
     *             if child argument is null.
     * @throws InvalidConfigurationException
     *             if cyclic relationship would occur after adding the child, or if the child is not an instance of
     *             DefaultConfigurationObject.
     */
    public ConfigurationObject addChild(ConfigurationObject child) throws InvalidConfigurationException {
        Helper.checkNull(child, "child");

        if (child == this) {
            throw new InvalidConfigurationException("The child to be added can not be the same as itself.");
        }

        // the child added should be a DefaultConfigurationObject instance
        if (!(child instanceof DefaultConfigurationObject)) {
            throw new InvalidConfigurationException("The child to be added should be a DefaultConfigurationObject.");
        }

        // cast to DefaultConfigurationObject, since DefaultConfigurationObject.getAllDescendants not throw
        // the ConfigurationAccessException
        ConfigurationObject[] descendants = ((DefaultConfigurationObject) child).getAllDescendants();

        // this can not be Descendant if its child
        for (int i = 0; i < descendants.length; i++) {
            if (descendants[i] == this) {
                throw new InvalidConfigurationException("cyclic relationship occurs when add the child.");
            }
        }

        try {
            ConfigurationObject oldChild = (ConfigurationObject) children.get(child.getName());
            children.put(child.getName(), child);
            return oldChild;
        } catch (ConfigurationAccessException e) {
            // the ConfigurationAccessException is not thrown by DefaultConfigurationObject
            return null;
        }
    }

    /**
     * Determine whether it contains a child ConfigurationObject with given name.
     *
     * @return a boolean value indicating whether the specified child is contained.
     * @param name
     *            the name of the child
     * @throws IllegalArgumentException
     *             if name is null or empty
     */
    public boolean containsChild(String name) {
        Helper.checkStringNullOrEmpty(name, "name");

        return children.containsKey(name);
    }

    /**
     * Get a child ConfigurationObject with given name.
     *
     * @return the child ConfigurationObject, or null if there no such child name.
     * @param name
     *            the name of the child
     * @throws IllegalArgumentException
     *             if name is null or empty
     */
    public ConfigurationObject getChild(String name) {
        Helper.checkStringNullOrEmpty(name, "name");

        return (ConfigurationObject) children.get(name);
    }

    /**
     * Remove a child with given name.
     *
     * @return the removed child, or null if there is no such name.
     * @param name
     *            the name of the child to be removed.
     * @throws IllegalArgumentException
     *             if name is null or empty.
     */
    public ConfigurationObject removeChild(String name) {
        Helper.checkStringNullOrEmpty(name, "name");

        return (ConfigurationObject) children.remove(name);
    }

    /**
     * Clear all the children resides in the ConfigurationObject.
     *
     */
    public void clearChildren() {
        children.clear();
    }

    /**
     * Get all the children whose names are matched to the given pattern.
     *
     * <p>
     * <em>Changes in 1.1:</em>
     * <ol>
     * <li>Used generic types in the code.</li>
     * </ol>
     * </p>
     *
     * @param pattern
     *            a regular expression used to match children names.
     *
     * @return the matched children
     *
     * @throws IllegalArgumentException
     *             if pattern is null, or an incorrect regex.
     */
    public ConfigurationObject[] getChildren(String pattern) {
        Helper.checkNull(pattern, "pattern");

        List<ConfigurationObject> list = new ArrayList<ConfigurationObject>();

        // since we should use the pattern several times, pre-compile it is a efficient way
        Pattern p = Pattern.compile(pattern);

        // using the pattern to check each key
        for (Iterator<Entry<String, ConfigurationObject>> it = children.entrySet().iterator(); it.hasNext();) {
            Entry<String, ConfigurationObject> entry = it.next();

            if (p.matcher((String) entry.getKey()).matches()) {
                list.add(entry.getValue());
            }
        }

        return (ConfigurationObject[]) list.toArray(new ConfigurationObject[list.size()]);
    }

    /**
     * Get all the child names contained in it.
     *
     * @return all the children names.
     */
    public String[] getAllChildrenNames() {
        return (String[]) children.keySet().toArray(new String[children.size()]);
    }

    /**
     * Get all the children contained in it.
     *
     * @return all the children.
     */
    public ConfigurationObject[] getAllChildren() {
        return (ConfigurationObject[]) children.values().toArray(new ConfigurationObject[children.size()]);
    }

    /**
     * Get all descendants of this ConfigurationObject.
     *
     * @return an array of Descendants.
     */
    public ConfigurationObject[] getAllDescendants() {
        return getTheDescendants(null);
    }

    /**
     * Get all descendants, whose name matched to given pattern, of this ConfigurationObject.
     *
     * @return an array of matched Descendants.
     * @param pattern
     *            a regex used to match descendants' names.
     * @throws IllegalArgumentException
     *             if given pattern is null, or an incorrect pattern.
     */
    public ConfigurationObject[] getDescendants(String pattern) {
        Helper.checkNull(pattern, "pattern");
        return getTheDescendants(pattern);
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
     * </p>
     *
     * @return all the descendants specified by the path, or an empty array if nothing is matched.
     * @param path
     *            the path used to retrieved descendants.
     * @throws IllegalArgumentException
     *             if the path is null.
     */
    public ConfigurationObject[] findDescendants(String path) {
        return findOrDeleteDescendants(path, false);
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
     * </p>
     *
     * @return all the descendants specified by the path, or an empty array if nothing is matched.
     * @param path
     *            the path used to retrieved descendants.
     * @throws IllegalArgumentException
     *             if the path is null.
     */
    public ConfigurationObject[] deleteDescendants(String path) {
        return findOrDeleteDescendants(path, true);
    }

    /**
     * Implementation of clone methods, all the children are deep copied and the properties are only shadow copied.
     *
     * <p>
     * <em>Changes in 1.1:</em>
     * <ol>
     * <li>Used generic types in the code.</li>
     * </ol>
     * </p>
     *
     * @return the clone of the DefaultConfigurationObject
     */
    public Object clone() {
        return clone(new HashMap<ConfigurationObject, ConfigurationObject>());
    }

    /**
     * A Helper method that used to deep copy the whole DAG of the DefaultConfigurationObject.
     *
     * <p>
     * <em>Changes in 1.1:</em>
     * <ol>
     * <li>Specified generic parameters for cache parameter type.</li>
     * <li>Used generic types in the code.</li>
     * </ol>
     * </p>
     *
     * @return the clone
     * @param cache
     *            the cache contained all the cloned objects.
     */
    private DefaultConfigurationObject clone(Map<ConfigurationObject, ConfigurationObject> cache) {
        DefaultConfigurationObject clone = null;

        clone = (DefaultConfigurationObject) super.clone();

        // clone the properties
        clone.properties = new HashMap<String, List<Object>>(this.properties);

        clone.children = new HashMap<String, ConfigurationObject>();

        // iterate every child to clone it.
        for (Iterator<ConfigurationObject> itr = children.values().iterator(); itr.hasNext();) {
            DefaultConfigurationObject child = (DefaultConfigurationObject) itr.next();

            // if the child is not cloned yet, clone it, and put it into the cache.
            if (!cache.containsKey(child)) {
                cache.put(child, child.clone(cache));
            }

            // retrieve the child's clone from cache
            clone.children.put(child.getName(), cache.get(child));
        }

        return clone;
    }

    /**
     * <p>
     * Gets all the property values associated with given key.
     * </p>
     *
     * <p>
     * It ignores the ConfigurationAccessException.
     * </p>
     *
     * @param key
     *            the key used to specify property
     *
     * @return an array containing all the property values, or null if there is no such property key.
     *
     * @throws IllegalArgumentException
     *             if key is null or empty
     *
     * @since 1.1
     */
    private Object[] getPropertyValuesHelper(String key) {
        Object[] result = null;

        try {
            result = getPropertyValues(key);
        } catch (ConfigurationAccessException e) {
            // Ignore (won't happen)
        }
        return result;
    }

    /**
     * Get the Descendants of the current DefaultConfigurationObject, the pattern may be used if it is not null.
     *
     * <p>
     * <em>Changes in 1.1:</em>
     * <ol>
     * <li>Used generic types in the code.</li>
     * </ol>
     * </p>
     *
     * @param pattern
     *            a regex used to match descendants' names, but also may be null, which means not use pattern.
     * @return an array of matched Descendants.
     */
    private ConfigurationObject[] getTheDescendants(String pattern) {
        Pattern p = null;
        if (pattern != null) {
            p = Pattern.compile(pattern);
        }
        Set<ConfigurationObject> visited = new HashSet<ConfigurationObject>();
        Set<ConfigurationObject> result = new HashSet<ConfigurationObject>();

        // using linked list is more efficient, since it is implemented like a queue
        LinkedList<ConfigurationObject> queue = new LinkedList<ConfigurationObject>();
        visited.add(this);
        queue.addLast(this);

        while (queue.size() > 0) {
            // get the first head item of the queue to broadcast
            ConfigurationObject nextObject = (ConfigurationObject) queue.removeFirst();
            ConfigurationObject[] currentChildren = null;

            try {
                currentChildren = nextObject.getAllChildren();
            } catch (ConfigurationAccessException e) {
                // ConfigurationAccessException will never occur in DefaultConfigurationObject
                // since it is do the operation in memory
            }

            for (int i = 0; i < currentChildren.length; i++) {
                // if already visited, then just ignore it, only treat those not contains
                if (!visited.contains(currentChildren[i])) {
                    visited.add(currentChildren[i]);
                    queue.addLast(currentChildren[i]);
                    if (p == null || p.matcher(((DefaultConfigurationObject) currentChildren[i]).getName()).matches()) {
                        result.add(currentChildren[i]);
                    }
                }
            }
        }

        return (ConfigurationObject[]) result.toArray(new ConfigurationObject[result.size()]);
    }

    /**
     * <p>
     * Slash trimmed the String, not only the white spaces are ignore at the both end, also ignore the slashes at the
     * both end.
     * </p>
     *
     * <p>
     * <em>Changes in 1.1:</em>
     * <ol>
     * <li>Made this method static.</li>
     * </ol>
     * </p>
     *
     * @param str
     *            the String to be slash trimmed
     * @return the String after slash trimmed.
     */
    private static String trimSlashes(String str) {
        int len = str.length();
        int st = 0;

        while ((st < len) && ((str.charAt(st) <= ' ') || (str.charAt(st) == '\\') || (str.charAt(st) == '/'))) {
            st++;
        }

        while ((st < len)
            && ((str.charAt(len - 1) <= ' ') || (str.charAt(len - 1) == '\\') || (str.charAt(len - 1) == '/'))) {
            len--;
        }

        return ((st > 0) || (len < str.length())) ? str.substring(st, len) : str;
    }

    /**
     * <p>
     * Remove the Continuous slashes in the given String, Continuous slashes will be considered as one.
     * </p>
     *
     * <p>
     * <em>Changes in 1.1:</em>
     * <ol>
     * <li>Made this method static.</li>
     * <li>Using StringBuilder instead of StringBuilder according to JDK doc recommendation.</li>
     * </ol>
     * </p>
     *
     * @param str
     *            the given String
     * @return the String after remove the Continuous slashes
     */
    private static String removeContinuousSlashes(String str) {
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            if (i > 0 && (str.charAt(i - 1) == '/' || str.charAt(i - 1) == '\\')
                && (str.charAt(i) == '/' || str.charAt(i) == '\\')) {
                continue;
            }
            buffer.append(str.charAt(i));
        }
        return buffer.toString();
    }

    /**
     * <p>
     * Find the Descendants of this ConfigurationObject with the path, at the same time delete them if they are
     * required to be deleted.
     * </p>
     *
     * <p>
     * <em>Changes in 1.1:</em>
     * <ol>
     * <li>Used generic types in the code.</li>
     * </ol>
     * </p>
     *
     * @param path
     *            the path used to retrieved descendants.
     * @param shouldDelete
     *            whether the descendants with the path should be deleted
     * @return all the descendants specified by the path, or an empty array if nothing is matched.
     */
    private ConfigurationObject[] findOrDeleteDescendants(String path, boolean shouldDelete) {
        Helper.checkNull(path, "path");
        // trim the path by remove the whitespace, slashes, and
        // remove the Continuous slashes, Continuous slashes will be considered as one
        path = removeContinuousSlashes(trimSlashes(path));

        String[] paths = path.split(SPLIT_SLASH_PATTERN);

        // here should use set, since the graph it not a tree,
        // so the same node may be reached twice at one layer
        Set<ConfigurationObject> current = new HashSet<ConfigurationObject>();

        // hold the node to process for the next path
        Set<ConfigurationObject> next = new HashSet<ConfigurationObject>();
        current.add(this);

        for (int i = 0; i < paths.length; i++) {
            for (Iterator<ConfigurationObject> currentOnes = current.iterator(); currentOnes.hasNext();) {
                ConfigurationObject currentChild = (ConfigurationObject) currentOnes.next();

                try {
                    ConfigurationObject[] currentChildren = currentChild.getAllChildren();

                    for (int j = 0; j < currentChildren.length; j++) {
                        // search the node alone the path
                        if (wildMatch(paths[i], currentChildren[j].getName())) {
                            next.add(currentChildren[j]);

                            // if this node is the target one and should be deleted, just delete it
                            if ((i == (paths.length - 1)) && shouldDelete) {
                                currentChild.removeChild(currentChildren[j].getName());
                            }
                        }
                    }
                } catch (ConfigurationAccessException e) {
                    // the ConfigurationAccessException will never occur in DefaultConfigurationObject
                }
            }

            // avoid to create many sets
            current.clear();
            current.addAll(next);
            next.clear();
        }

        return (ConfigurationObject[]) current.toArray(new ConfigurationObject[current.size()]);
    }

    /**
     * <p>
     * Wild regular expression match with the pattern and target String, it is most like the <code>XPath</code>
     * rule:
     * <li>that * can represent any segment of letter</li>
     * <li>? can represent can single letter</li>.
     *
     * Note: the pattern and String are ensured to be not null, and the String is not empty. since the name of the
     * child can not be empty.
     * </p>
     *
     * <p>
     * Here we use dynamic programming strategy, which will reduce the complication of the match algorithm, the worst
     * case length(pattern) * length(str).
     * </p>
     *
     * <p>
     * <em>Changes in 1.1:</em>
     * <ol>
     * <li>Made this method static.</li>
     * </ol>
     * </p>
     *
     * @param pattern
     *            the pattern used to comparison
     * @param str
     *            the String that to be matched with the given pattern
     * @return true if the String is wild matched with the pattern, otherwise false
     */
    private static boolean wildMatch(String pattern, String str) {
        boolean[][] status = new boolean[pattern.length() + 1][str.length() + 1];

        for (int i = 0; i <= pattern.length(); i++) {
            for (int j = 0; j <= str.length(); j++) {
                status[i][j] = false;
            }
        }

        status[pattern.length()][str.length()] = true;

        // the follow steps are using dynamic programming,
        // status[i][j] == true means pattern.subString(i) matches str.subString(j)
        for (int i = pattern.length() - 1; i >= 0; i--) {
            for (int j = str.length() - 1; j >= 0; j--) {
                char p = pattern.charAt(i);
                char s = str.charAt(j);

                // '*' can be used to match any letters
                if (p == '*') {
                    status[i][j] |= status[i + 1][j + 1];
                    status[i][j] |= status[i + 1][j];
                    status[i][j] |= status[i][j + 1];
                } else if ((p == '?') || (p == s)) {
                    status[i][j] |= status[i + 1][j + 1];
                }
            }
        }

        // return whether the whole String can match
        return status[0][0];
    }

    /**
     * <p>
     * Checks whether the given Object is null or an instance of Serializable.
     * </p>
     *
     * <p>
     * <em>Changes in 1.1:</em>
     * <ol>
     * <li>Made this method static.</li>
     * </ol>
     * </p>
     *
     * @param arg
     *            the argument to check
     * @param name
     *            the name of the argument to check
     *
     * @throws InvalidConfigurationException
     *             if the given Object is null
     */
    private static void checkNullOrSerializable(Object arg, String name) throws InvalidConfigurationException {
        if (arg != null && !(arg instanceof Serializable)) {
            throw new InvalidConfigurationException(name + "should be null or a Serializable instance.");
        }
    }
}
