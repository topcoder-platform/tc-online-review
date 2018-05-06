/*
 * Copyright (C) 2006, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.objectfactory.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.topcoder.util.config.ConfigManager;

/**
 * Test Helper for the component.
 *
 * @author mgmg, TCSDEVELOPER
 * @version 2.2
 */
public final class TestHelper {
    /**
     * Private test helper.
     */
    private TestHelper() {
    }

    /**
     * Get the URL string of the file.
     *
     * @param fileName
     *            the file name
     * @return the url string.
     */
    public static String getURLString(String fileName) {
        return "file:///" + new File(fileName).getAbsolutePath();
    }

    /**
     * Load a config file to ConfigManager.
     *
     * @throws Exception
     *             exception to the caller.
     */
    public static void loadSingleConfigFile() throws Exception {
        clearConfig();
        ConfigManager.getInstance().add("config.xml");
    }

    /**
     * Clear all the configs.
     *
     * @throws Exception
     *             exception to the caller.
     */
    public static void clearConfig() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();
        Iterator<String> it = cm.getAllNamespaces();
        List<String> nameSpaces = new ArrayList<String>();

        while (it.hasNext()) {
            nameSpaces.add(it.next());
        }

        for (int i = 0; i < nameSpaces.size(); i++) {
            cm.removeNamespace(nameSpaces.get(i));
        }
    }
}
