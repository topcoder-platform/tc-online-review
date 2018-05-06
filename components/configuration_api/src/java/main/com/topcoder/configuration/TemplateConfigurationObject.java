/*
 * Copyright (C) 2007, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.configuration;


/**
 * <p>
 * This class utilizes Template Method design pattern to implement some methods in ConfigurationObject interface.
 * </p>
 *
 * <p>
 * In ConfigurationObject interface, many methods operate on some descendant ConfigurationObject
 * which are found by path containing wildcard. Because the operations on a single ConfigurationObject is also defined.
 * We just first use <code>findDescendants</code> to find all the matched descendants,
 * and then invoke corresponding simple method on them. In this case, <code>findDescendants</code>
 * method and other simple methods are template methods.
 * </p>
 *
 * <p>
 * <em>Usage:</em> See <strong>Usage</strong> section of {@link ConfigurationObject}.
 * </p>
 *
 * <p>
 * Thread safe:
 * this class itself contains no state, and thread safe depends on whether template methods are thread safe.
 * </p>
 *
 * @author maone, haozhangr, saarixx, sparemax
 * @version 1.1
 */
public abstract class TemplateConfigurationObject
    extends BaseConfigurationObject {
    /**
     * Protected empty constructor only used by sub-classes.
     *
     */
    protected TemplateConfigurationObject() {
        // Empty
    }

    /**
     * Set the property values in the descendants specified by the path.
     *
     * <p> For the information to find descendants by the given path,
     * please see <code> {@link ConfigurationObject#findDescendants(String)}} </code> method.
     * For the information to set property value, please see
     * <code> {@link ConfigurationObject#setPropertyValue(String, Object)} </code> method.
     * </p>
     * @param path the path specifying the descendants.
     * @param key the key of the property
     * @param value the new value of the property.
     * @throws IllegalArgumentException if key or path is null, or key is empty.
     * @throws InvalidConfigurationException if the key or value is invalid for specific implementation.
     * @throws ConfigurationAccessException if any other error occurs.
     */
    public void setPropertyValue(String path, String key, Object value)
        throws InvalidConfigurationException, ConfigurationAccessException {
        Helper.checkNull(path, "path");
        Helper.checkStringNullOrEmpty(key, "key");

        ConfigurationObject[] objects = findDescendants(path);

        for (int i = 0; i < objects.length; i++) {
            objects[i].setPropertyValue(key, value);
        }
    }

    /**
     * Set the property values in the descendants specified by the path.
     * <p>
     * For the information to find descendants by the given path,
     * please see <code> {@link ConfigurationObject#findDescendants(String)} </code> method.
     * For the information to set property value, please see
     * <code> {@link ConfigurationObject#setPropertyValues(String, Object[])} </code> method.
     * </p>
     *
     * @param path the path specifying the descendants.
     * @param key the key of the property
     * @param values the new values of the property.
     * @throws IllegalArgumentException if path or key is null, or key is empty.
     * @throws InvalidConfigurationException if the key or value is invalid for specific implementation.
     * @throws ConfigurationAccessException if any other error occurs.
     */
    public void setPropertyValues(String path, String key, Object[] values)
        throws InvalidConfigurationException, ConfigurationAccessException {
        Helper.checkNull(path, "path");
        Helper.checkStringNullOrEmpty(key, "key");

        ConfigurationObject[] objects = findDescendants(path);

        for (int i = 0; i < objects.length; i++) {
            objects[i].setPropertyValues(key, values);
        }
    }

    /**
     * Remove the property with given key from the descendants specified by the path.
     *
     * <p>
     * For the information to find descendants by the given path,
     * please see <code> {@link ConfigurationObject#findDescendants(String)} </code> method.
     * For the information to remove property, please see
     * <code> {@link ConfigurationObject#removeProperty(String)} </code> method.
     * </p>
     *
     * @param path the path specifying the descendants.
     * @param key the key of the property
     * @throws IllegalArgumentException if any argument is null, or key is empty.
     * @throws ConfigurationAccessException if any other error occurs.
     */
    public void removeProperty(String path, String key)
        throws ConfigurationAccessException {
        Helper.checkNull(path, "path");
        Helper.checkStringNullOrEmpty(key, "key");

        ConfigurationObject[] objects = findDescendants(path);

        for (int i = 0; i < objects.length; i++) {
            objects[i].removeProperty(key);
        }
    }

    /**
     * Clear the properties from all the descendants specified by the path.
     *
     * <p>
     * For the information to find descendants by the given path,
     * please see <code> {@link ConfigurationObject#findDescendants(String)} </code> method.
     * For the information to set property value, please see
     * <code> {@link ConfigurationObject#clearProperties()} </code> method.
     * </p>
     *
     * @param path the path specifying the descendants.
     * @throws IllegalArgumentException if any argument is null
     * @throws ConfigurationAccessException if any other error occurs.
     */
    public void clearProperties(String path)
        throws ConfigurationAccessException {
        Helper.checkNull(path, "path");

        ConfigurationObject[] objects = findDescendants(path);

        for (int i = 0; i < objects.length; i++) {
            objects[i].clearProperties();
        }
    }

    /**
     * Add a child to the descendants specified by the path.
     * <p>
     * For the information to find descendants by the given path,
     * please see <code> {@link ConfigurationObject#findDescendants(String)} </code> method.
     * For the information to add child. please see
     * <code> {@link ConfigurationObject#addChild(ConfigurationObject)} </code> method.
     * </p>
     *
     * @param path the path specifying the descendants.
     * @param child the child to be added.
     * @throws IllegalArgumentException if any argument is null
     * @throws InvalidConfigurationException if the child is invalid for specific implementation.
     * @throws ConfigurationAccessException if any other error occurs.
     */
    public void addChild(String path, ConfigurationObject child)
        throws InvalidConfigurationException, ConfigurationAccessException {
        Helper.checkNull(path, "path");
        Helper.checkNull(child, "child");

        ConfigurationObject[] objects = findDescendants(path);

        for (int i = 0; i < objects.length; i++) {
            objects[i].addChild(child);
        }
    }

    /**
     * Remove a child from the descendants specified by the path.
     *
     * <p>
     * For the information to find descendants by the given path,
     * please see <code> {@link ConfigurationObject#findDescendants(String)} </code> method.
     * For the information to remove child. please see
     * <code> {@link ConfigurationObject#removeChild(String)} </code> method.
     * </p>
     *
     * @param path the path specifying the descendants.
     * @param name the name of child to be removed.
     * @throws IllegalArgumentException if any argument is null, or name is empty.
     * @throws ConfigurationAccessException if any other error occurs.
     */
    public void removeChild(String path, String name)
        throws ConfigurationAccessException {
        Helper.checkNull(path, "path");
        Helper.checkStringNullOrEmpty(name, "name");

        ConfigurationObject[] objects = findDescendants(path);

        for (int i = 0; i < objects.length; i++) {
            objects[i].removeChild(name);
        }
    }

    /**
     * Clear all children from the descendants specified by the path.
     * <p>
     * For the information to find descendants by the given path,
     * please see <code> {@link ConfigurationObject#findDescendants(String)} </code> method.
     * For the information to clear children please see
     * <code> {@link ConfigurationObject#clearChildren()} </code> method.
     * </p>
     *
     * @param path the path specifying the descendants.
     * @throws IllegalArgumentException if any argument is null.
     * @throws ConfigurationAccessException if any other error occurs.
     */
    public void clearChildren(String path) throws ConfigurationAccessException {
        Helper.checkNull(path, "path");

        ConfigurationObject[] objects = findDescendants(path);

        for (int i = 0; i < objects.length; i++) {
            objects[i].clearChildren();
        }
    }

    /**
     * Process descendants by the given processor.
     * <p>
     * All the descendants will be found by given path.
     * Please see <code> {@link  ConfigurationObject#findDescendants(String)} </code> method for
     * explanation about path.
     * </p>
     *
     * @param path path to find descendants
     * @param processor processor used to process all descendants
     * @throws IllegalArgumentException if any argument is null
     * @throws ConfigurationAccessException if any error occurs while accessing the configuration
     * @throws ProcessException propagated from processor
     */
    public void processDescendants(String path, Processor processor)
        throws ConfigurationAccessException, ProcessException {
        Helper.checkNull(path, "path");
        Helper.checkNull(processor, "processor");

        ConfigurationObject[] objects = findDescendants(path);

        for (int i = 0; i < objects.length; i++) {
            processor.process(objects[i]);
        }
    }
}
