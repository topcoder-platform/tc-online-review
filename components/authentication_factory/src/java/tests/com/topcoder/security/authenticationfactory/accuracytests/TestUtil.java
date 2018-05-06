package com.topcoder.security.authenticationfactory.accuracytests;

import com.topcoder.util.config.ConfigManager;

import java.util.Iterator;


/**
 * Contains some utility method for testing.
 */
public class TestUtil {

    /**
     * The folder contains test file for accuracy test.
     */
    public static final String ACCURACY_TEST_DIR = "accuracy/";

    /**
     * Empty constructor.
     */
    private TestUtil() {
    }

    /**
     * Clear all namespaces in the ConfigManager and load the specified configuration file.
     *
     * @param filename the config file to load.
     */
    public static void loadConfigFile(String filename) {
        try {
            clearAllNamespace();

            ConfigManager cm = ConfigManager.getInstance();
            cm.add(filename);
        } catch (Exception e) {
            // nothing
        }
    }

    /**
     * Clear all namespaces exist in ConfigManager.
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
}
