/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.commons.utils;

import java.util.Properties;

/**
 * <p>
 * The custom retriever.
 * </p>
 *
 * @author sparemax
 * @version 1.0
 */
public class CustomRetriever implements Retriever {
    /**
     * <p>
     * The first parameter.
     * </p>
     */
    private int param1;

    /**
     * <p>
     * The second parameter.
     * </p>
     */
    private String param2;

    /**
     * <p>
     * Creates an instance of CustomRetriever.
     * </p>
     *
     * @param properties
     *            the configuration object.
     *
     * @throws ConfigurationException
     *             if any error occurs.
     */
    public CustomRetriever(Properties properties) {
        param1 = PropertiesUtility.getIntegerProperty(properties, "param1", true, ConfigurationException.class);
        param2 = PropertiesUtility.getStringProperty(properties, "param2", false, ConfigurationException.class);
    }

    /**
     * <p>
     * Gets the first parameter.
     * </p>
     *
     * @return the first parameter.
     */
    public int getParam1() {
        return param1;
    }

    /**
     * <p>
     * Gets the second parameter.
     * </p>
     *
     * @return the second parameter.
     */
    public String getParam2() {
        return param2;
    }

}
