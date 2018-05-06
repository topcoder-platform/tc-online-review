/*
 * Copyright (C) 2003-2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * <p>
 * Custom implementation of PluggableConfigSource for testing purpose only.
 * </p>
 * <p>
 * Version 2.2 change notes:
 * <ul>
 * <li>Moved into separate class</li>
 * <li>Added parameter isRefreshable to support refreshable properties</li>
 * </ul>
 * </p>
 * @author WishingBone, TCSDEVELOPER
 * @version 2.2
 */
class AccuracyPluggableConfigSource implements PluggableConfigSource {

    /**
     * <p>
     * Represents value when isRefreshable is false.
     * </p>
     */
    public static final int IS_NOT_REFRESHABLE = 1;

    /**
     * <p>
     * Represents value when isRefreshable is true.
     * </p>
     */
    public static final int IS_REFRESHABLE = 0;

    /**
     * <p>
     * Represents value when Property isRefreshable is not present.
     * </p>
     */
    public static final int NOT_PRESENT_IS_REFRESHABLE = 4;

    /**
     * <p>
     * Represents value when Property isRefreshable has null value.
     * </p>
     */
    public static final int NULL_VALUE_IS_REFRESHABLE = 2;

    /**
     * Whether to throw exception on method invocation.
     */
    private static boolean exceptional = false;

    /**
     * The configure properties.
     */
    private static Properties props = null;

    /**
     * <p>
     * Represents flag whether properties is refreshable or not.
     * </p>
     */
    private static int refreshable = NOT_PRESENT_IS_REFRESHABLE;

    /**
     * The saved root of the property tree.
     */
    private static Property root = null;

    /**
     * Configure the source.
     * @param props the properties to configure the source.
     * @throws ConfigParserException if it is unable to parse config.
     */
    public void configure(Properties props) throws ConfigParserException {
        if (exceptional) {
            throw new ConfigParserException();
        }
        AccuracyPluggableConfigSource.props = props;
    }

    /**
     * Get a list of properties from the underlying source.
     */
    public List<Property> getProperties() throws ConfigParserException {
        if (exceptional) {
            throw new ConfigParserException();
        }
        List<Property> list = new ArrayList<Property>();
        list.add(new Property("prop1", "value1"));
        list.add(new Property("prop2", "value2"));
        list.add(new Property("prop3", "value3"));
        Property refreshableProperty = new Property("IsRefreshable");
        switch (refreshable) {
        case IS_REFRESHABLE:
            refreshableProperty.setValue("true");
            list.add(refreshableProperty);
            break;
        case IS_NOT_REFRESHABLE:
            refreshableProperty.setValue("false");
            list.add(refreshableProperty);
            break;
        case NULL_VALUE_IS_REFRESHABLE:
            list.add(refreshableProperty);
            break;
        default:
            // not present
            break;
        }
        return list;
    }

    /**
     * Save the property tree to underlying source.
     * @param root the root of the property tree.
     * @throws IOException if underlying source is unable to save.
     */
    public void save(Property root) throws IOException {
        if (exceptional) {
            throw new IOException();
        }
        AccuracyPluggableConfigSource.root = (Property) root.clone();
    }

    /**
     * Set fields to default.
     */
    public static void clear() {
        exceptional = false;
        props = null;
        root = null;
    }

    /**
     * Get the configure properties.
     * @return the configure properties.
     */
    public static Properties getProps() {
        return props;
    }

    /**
     * Get the saved root of the property tree.
     * @return the saved root of the property tree.
     */
    public static Property getRoot() {
        return root;
    }

    /**
     * Set whether to throw exception on method invocation.
     * @param exceptional whether to throw exception on method invocation.
     */
    public static void setExceptional(boolean exceptional) {
        AccuracyPluggableConfigSource.exceptional = exceptional;
    }

    /**
     * <p>
     * Sets isRefreshable flag.
     * </p>
     * @param refreshable the refreshable flag to be set
     */
    public static void setRefreshable(int refreshable) {
        AccuracyPluggableConfigSource.refreshable = refreshable;
    }
}