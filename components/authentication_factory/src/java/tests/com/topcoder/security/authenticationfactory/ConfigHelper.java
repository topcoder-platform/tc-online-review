/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.security.authenticationfactory;

import java.util.Iterator;

import com.topcoder.util.config.ConfigManager;

/**
 * Here defined some helper function while do test.
 *
 * @author TCSDEVELOPER
 * @version 2.0
 */
public class ConfigHelper {

    /**
     * Empty constructor.
     */
    private ConfigHelper() {

    }

    /**
     * Clear all namespaces in ConfigManager, and load config.
     * @param filename the config to load.
     */
    public static void cleanAndLoadConfig(String filename) {
        try {
            ConfigHelper.clearAllNamespace();
            ConfigManager cm = ConfigManager.getInstance();
            cm.add(filename);
        } catch (Exception e) {
            // nothing
        }
    }

    /**
     * clear all namespace exist in ConfigManager.
     */
    public static void clearAllNamespace() {
        try {
            ConfigManager cm = ConfigManager.getInstance();
            Iterator it = cm.getAllNamespaces();
            while (it.hasNext()) {
                cm.removeNamespace((String) it.next());
            }
        } catch (Exception e) {
            // nothing
        }
    }

    /**
     * Get the port used in test.
     * @return the port.
     */
    public static int getPort() {
        String port = System.getProperty("testport");
        int p = 8080;
        if (port != null) {
            try {
                p = Integer.parseInt(port);
            } catch (NumberFormatException nfe) {
                return p;
            }
        }

        return p;
    }
}
