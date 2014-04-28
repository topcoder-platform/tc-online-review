/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.model;

import com.cronos.onlinereview.ConfigurationException;

import org.apache.commons.beanutils.LazyDynaBean;
import org.apache.commons.beanutils.MutableDynaClass;

import java.util.Properties;


/**
 * <p>
 * This class represents the dynamic model which is an extension of <code>LazyDynaBean</code>.
 * It has the name field which is used for loading corresponding model properties.
 * </p>
 * <p>
 * Thread safety: This class is not thread safe.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class DynamicModel extends LazyDynaBean {
    /**
     * Represents the serial version id.
     */
    private static final long serialVersionUID = 1L;

    /**
    * Represents the config properties.
    */
    private Properties config;

    /**
     * Represents the model name.
     */
    private String name;

    /**
     * Default constructor.
     */
    public DynamicModel() {
    }

    /**
     * Set the model attribute's types and initial values
     *
     * The initial values only support the Boolean, Long, Double, Integer, Float, and String.
     *
     * @throws ConfigurationException if any error
     */
    public void init() {
        if (config != null) {
            MutableDynaClass dynaClass = (MutableDynaClass) this.getDynaClass();

            // Add properties
            dynaClass.setRestricted(false);

            try {
                for (Object key : config.keySet()) {
                    String keyName = (String) key;

                    String value = config.getProperty((String) key);

                    String[] split = value.split(",");

                    String type = split[0];

                    if (type.contains("[]")) {
                        type = "[L" + type.replace("[]", "") + ";";
                    }

                    Class<?> clz = Class.forName(type);

                    dynaClass.add(keyName, clz);

                    if (split.length == 2) {
                        // set initial
                        if (clz == Boolean.class) {
                            set(keyName, Boolean.valueOf(split[1]));
                        } else if (clz == Long.class) {
                            set(keyName, Long.valueOf(split[1]));
                        } else if (clz == Double.class) {
                            set(keyName, Double.valueOf(split[1]));
                        } else if (clz == Integer.class) {
                            set(keyName, Integer.valueOf(split[1]));
                        } else if (clz == Float.class) {
                            set(keyName, Float.valueOf(split[1]));
                        } else {
                            set(keyName, split[1]);
                        }
                    }
                }
            } catch (ClassNotFoundException e) {
                throw new ConfigurationException("the class cast error occurs", e);
            }
        }
    }

    /**
     * Getter of config.
     * @return the config
     */
    public Properties getConfig() {
        return config;
    }

    /**
     * Setter of config.
     * @param config the config to set
     */
    public void setConfig(Properties config) {
        this.config = config;
    }

    /**
     * Getter of name.
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter of name.
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
}
