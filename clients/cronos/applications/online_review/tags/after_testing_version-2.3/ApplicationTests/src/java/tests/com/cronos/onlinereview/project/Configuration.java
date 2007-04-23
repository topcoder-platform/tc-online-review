/*
 * Copyright (C) 2006 TopCoder, Inc. All Rights Reserved.
 */
package com.cronos.onlinereview.project;

import com.topcoder.util.config.ConfigManager;
import java.util.Iterator;

/**
 * A simplied configuration interface for the various entities to access via the TopCoder
 * Configuration Manager.
 *
 * @author TCSTESTER
 * @version 1.0
 */
public class Configuration {

    /**
     * The configuration manager instance to use. Once this is first initialized, it will be cleared and
     * loaded with preload files.
     */
    private static ConfigManager cm = null;

    /**
     * The namespace to which this instance is attached.
     */
    private String namespace = null;

    /**
     * Create the configuration instance with specified namespace. If the namespace does not exist, a file
     * will be loaded with the last portion of the namespace.
     */
    public Configuration(String namespace) {
        try {
            this.namespace = namespace;
            if (cm == null) {
                // Initialize for the first time. Clear the configuration manager and add the preload listing.
                cm = ConfigManager.getInstance();
                for (Iterator itr = cm.getAllNamespaces(); itr.hasNext();) {
                    cm.removeNamespace((String) itr.next());
                }
                cm.add("Configuration.xml");
                String[] files = cm.getStringArray(getClass().getName(), "preloads");
                for (int i = 0; i < files.length; ++i) {
                    cm.add(files[i]);
                }
            }
            if (!cm.existsNamespace(namespace)) {
                // Last portion of the namespace is used, it's virtually the class name.
                cm.add(namespace.substring(namespace.lastIndexOf('.') + 1) + ".xml");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Get a property from configuration.
     *
     * @param property the property to locate.
     *
     * @return the value for the property, or null if nothing can be loaded.
     */
    public String getProperty(String property) {
        try {
            return cm.getString(namespace, property);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
