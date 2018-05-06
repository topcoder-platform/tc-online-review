/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.config;

import junit.framework.TestCase;

/**
 * <p>
 * Shows usage for the component.
 * </p>
 *
 * @author saarixx, sparemax
 * @version 2.2
 * @since 2.2
 */
public class Demo extends TestCase {
    /**
     * <p>
     * Represents the iterations.
     * </p>
     */
    private static final int ITERATIONS = 1000;

    /**
     * <p>
     * Represents the namespace.
     * </p>
     */
    private static final String NAMESPACE = "com.topcoder.selfservice.cm_test";

    /**
     * <p>
     * Demo API usage of this component.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testDemo() throws Exception {
        // First an instance of ConfigManager should be obtained. This will also
        // load the predefined set of namespaces and their properties into memory
        ConfigManager manager = ConfigManager.getInstance();

        // Check if configuration is refreshable by default
        boolean configRefreshableByDefault = manager.isConfigRefreshableByDefault();

        // Set configuration refreshable by default to "false"
        manager.setConfigRefreshableByDefault(false);

        // any additional namespace may be loaded then
        manager.add("com.topcoder.currency", "currency.xml", ConfigManager.CONFIG_XML_FORMAT);

        // once the namespace properties are loaded they can be queried
        String value = manager.getString("com.topcoder.currency", "countries.USA.currency");

        value = manager.getString("com.sample.first", "property1");
        // value must be equal to "Configuration Manager"

        // multiple values of same property are also supported
        String[] values = manager.getStringArray("com.topcoder.currency", "countries.USA.name");

        // a new properties may be defined and stored for further use
        manager.createTemporaryProperties("com.topcoder.currency");
        manager.setProperty("com.topcoder.currency", "countries.Germany", "value");
        manager.addToProperty("com.topcoder.currency", "countries.USA.currency.name", "US dollar");
        manager.commit("com.topcoder.currency", "user");

        // removing of properties
        manager.createTemporaryProperties("com.topcoder.currency");
        manager.removeProperty("com.topcoder.currency", "countries.Utopia");
        manager.commit("com.topcoder.currency", "user");

        // removing of values of properties
        manager.createTemporaryProperties("com.topcoder.currency");
        manager.removeValue("com.topcoder.currency", "countries.USA.currency.name", "US dollar");
        manager.commit("com.topcoder.currency", "user");

        // refresh one namespace (if it’s refreshable)
        manager.refresh("com.topcoder.currency");

        // removing namespaces from in-memory set of loaded namespaces
        manager.removeNamespace("com.topcoder.currency");

        // checking the permission of current thread to load and modify namespaces
        try {
            manager.add("namespace.owned.by.some.class", "somefile.xml", ConfigManager.CONFIG_XML_FORMAT);
            manager.createTemporaryProperties("namespace.owned.by.some.class");
            manager.setProperty("namespace.owned.by.some.class", "new.property", "value");
            manager.commit("namespace.owned.by.some.class", "user");
        } catch (ConfigLockedException e) {
            System.err.println("Hey! Ask a namespace owner to load or " + "modify the namespace");
        }

        // addition of pluggable sources of configuration properties
        manager.add("pluggable.namespace", "somefile.config", ConfigManager.CONFIG_PLUGGABLE_FORMAT);

        // Demo for escaping in ".properties" files, supposed the
        manager.add("EscapeEnhancement", "test_files/TestEscape.properties", ConfigManager.CONFIG_PROPERTIES_FORMAT);
        // string s is "hello"
        String s = manager.getString("EscapeEnhancement", " \n\rProp3 ==!\t\\:!# ");

        // refresh all refreshable namespaces
        manager.refreshAll();
    }

    /**
     * <p>
     * Demo usage from multiple threads.
     * </p>
     */
    public void testThreads() {
        /**
         *
         * <p>
         * An implementation of Runnable.
         * </p>
         *
         * @author sparemax
         * @version 2.2
         * @since 2.2
         */
        Runnable runnable = new Runnable() {
            /**
             * <p>
             * The <code>run</code> method to be called in that separately executing thread.
             * </p>
             */
            public void run() {
                for (int i = 0; i < ITERATIONS; i++) {
                    // Refresh
                    refresh();
                }

                sleep(1000);

                System.out.println("runnable finished");
            }
        };
        Thread thread1 = new Thread(runnable);
        Thread thread2 = new Thread(runnable);
        thread1.start();
        thread2.start();

        sleep(5000);
    }


    /**
     * <p>
     * Reloads configuration information.
     * </p>
     */
    private static void refresh() {
        try {
            ConfigManager mgr = ConfigManager.getInstance();
            if (mgr.existsNamespace(NAMESPACE)) {
                mgr.refresh(NAMESPACE);
                System.out.println("finished refreshing");
            } else {
                System.out.println("NAMESPACE DOES NOT EXIST");
                return;
            }
        } catch (ConfigManagerException e) {
            System.out.println("problem refreshing CM: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * <p>
     * Causes the currently executing thread to sleep.
     * </p>
     *
     * @param millis
     *            the length of time to sleep in milliseconds.
     */
    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ie) {
            // Ignore
        }
    }
}
