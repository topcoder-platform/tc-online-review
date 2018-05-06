/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.login.failuretests;

import java.io.File;
import java.util.Iterator;

import com.topcoder.util.config.ConfigManager;

/**
 * A helper to manipulate the configuration.
 *
 * @author slion
 * @version 1.0
  */
public final class CMHelper {
	/**
	 * Represents the ConfigManager instance.
	 */
	private static ConfigManager cm = ConfigManager.getInstance();

    /**
     * Invisible Constructor.
     */
    private CMHelper() {
    }
    
    /**
     * Load the XML configuration by the specified file.
     * @throws Exception to JUnit.
     */
    public static void loadConfig(String file) throws Exception {
        cm.add(new File(file).getCanonicalPath());
    }
    
    /**
     * Remove the specified namespace from the CM.
     * @throws Exception to JUnit.
     */
    public static void removeNamespace(String ns) throws Exception {
        cm.removeNamespace(ns);
    }

    /**
     * Clear the configurations obtained by ConfigManager.
     * @throws Exception to JUnit.
     */
    public static void clearCM() throws Exception {
    	for (Iterator it = cm.getAllNamespaces(); it.hasNext();) {
    		cm.removeNamespace((String) it.next());
    	}
    }
}
