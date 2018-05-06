/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 *
 */
package com.cronos.onlinereview.ajax.accuracytests;

import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.config.ConfigManagerException;
import com.topcoder.util.config.UnknownNamespaceException;

import java.io.File;

import java.lang.reflect.Field;

import java.util.Iterator;


/**
 * Helpful class used in tests.
 *
 * @author assistant
 * @version 1.0
 */
final class TestHelper {
    /**
     * Private constructor.
     */
    private TestHelper() {
    }

    /**
     * Clear all the namespaces used in this component.
     */
    public static void clearNamespaces() {
        ConfigManager cm = ConfigManager.getInstance();

        for (Iterator iter = cm.getAllNamespaces(); iter.hasNext();) {
            try {
                cm.removeNamespace((String) iter.next());
            } catch (UnknownNamespaceException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Load the configurable file.
     *
     * @param file the configurable file.
     */
    public static void loadFile(String file) {
        ConfigManager cm = ConfigManager.getInstance();

        try {
            cm.add(new File(file).getAbsolutePath());
        } catch (ConfigManagerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get the value of private field.
     *
     * @param type the class type
     * @param name the name of the field.
     * @param obj the object to get varibale
     *
     * @return the variable value
     *
     * @throws Exception to Junit.
     */
    public static Object getVariable(Class type, String name, Object obj)
        throws Exception {
        Field field = type.getDeclaredField(name);

        try {
            field.setAccessible(true);

            return field.get(obj);
        } finally {
            field.setAccessible(false);
        }
    }
}
