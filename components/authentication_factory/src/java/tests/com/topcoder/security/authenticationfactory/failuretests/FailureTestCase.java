/*
 * Copyright (C) 2005, TopCoder Inc. All rights reserved.
 */
package com.topcoder.security.authenticationfactory.failuretests;

import junit.framework.TestCase;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Iterator;

import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.config.UnknownNamespaceException;
import com.topcoder.util.config.ConfigManagerException;

/**
 * <p>A parent class for failure test cases providing a set of convenient methods for getting the configuration
 * properties and manipulating with configuration namespaces.</p>
 *
 * @author isv
 * @version 1.0
 * @since Authentication Factory 2.0
 */
public class FailureTestCase extends TestCase {

    /**
     * <p>A <code>File</code> referencing the directory under which the test files are to be placed.</p>
     */
    public final static File TEST_FILES_ROOT = new File("test_files");

    /**
     * <p>A <code>File</code> referencing the directory under which the failure test files are to be placed.</p>
     */
    public final static File FAILURE_ROOT = new File("failure");

    /**
     * <p>A <code>File</code> referencing the directory under which the failure test files are to be placed.</p>
     */
    public final static File FAILURE_TEST_FILES_ROOT = new File(TEST_FILES_ROOT, "failure");

    /**
     * <p>A <code>Properties</code> providing the configuration properties for failure tests.</p>
     */
    private Properties properties = null;

    /**
     * <p>Constructs new <code>FailureTestCase</code>. The properties are initialized from the <code>
     * FailureTestsConfig.properties</code> file and the server instance is initialized but not started.</p>
     */
    protected FailureTestCase() {
//        properties = new Properties();
//        try {
//            properties.load(new FileInputStream(new File(FAILURE_TEST_FILES_ROOT, "FailureTestsConfig.properties")));
//        } catch (IOException e) {
//            System.err.println("FailureTestsConfig.properties is not loaded.\n" + e);
//        }
    }

    /**
     * <p>Gets the value for specified configuration property.</p>
     *
     * @param key a <code>String</code> name of the requested property.
     * @return a <code>String</code> value of the property or <code>null</code> for unknown property names.
     */
    protected String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     *
     * @param namespace a <code>String</code> referencing the configuration namespace to remove from configuration
     *        manager.
     */
    protected final void releaseNamespace(String namespace) {
        ConfigManager configManager = ConfigManager.getInstance();
        if (configManager.existsNamespace(namespace)) {
            try {
                configManager.removeNamespace(namespace);
            } catch (UnknownNamespaceException e) {}
        }
    }

    /**
     *
     */
    protected final void releaseNamespaces() {
        ConfigManager configManager = ConfigManager.getInstance();
        Iterator iterator = configManager.getAllNamespaces();
        while (iterator.hasNext()) {
            try {
                configManager.removeNamespace((String) iterator.next());
            } catch (UnknownNamespaceException e) {}
        }
    }

    /**
     * <p>A helper method to be used to initialize the specified configuration namespace with the configuration
     * properties provided by specified file. If specified namespace is already loaded to <code>ConfigurationManager
     * </code> then it is re-loaded with new configuration properties.</p>
     *
     * @param namespace a <code>String</code> providing the namespace to load configuration for.
     * @param filename a <code>String</code> providing the name of the file to load configuration file from.
     * @param format a <code>String</code> specifying the format of the configuration file.
     */
    protected final void loadConfiguration(String namespace, String filename, String format) {
        ConfigManager configManager = ConfigManager.getInstance();
        if (configManager.existsNamespace(namespace)) {
            try {
                configManager.removeNamespace(namespace);
            } catch (UnknownNamespaceException e) {}
        }

        try {
            configManager.add(namespace, filename.replace('\\', '/'), format);
        } catch (ConfigManagerException e) {
            System.err.println("An error occurred while loading the configuration namespace '"
                + namespace  + "' from file : " + filename + "\n\n" + e);
        }
    }

    /**
     * <p>A helper method to be used to initialize the specified configuration namespace with the configuration
     * properties provided by specified file. If specified namespace is already loaded to <code>ConfigurationManager
     * </code> then it is re-loaded with new configuration properties.</p>
     *
     * @param file a <code>File</code> providing the name of the file to load configuration file from.
     */
    protected final void loadConfiguration(File file) {
        ConfigManager configManager = ConfigManager.getInstance();

        try {
            configManager.add(file.getPath().replace('\\', '/'));
        } catch (ConfigManagerException e) {
            System.err.println("An error occurred while loading the configuration file '"
                + file.getPath()  + "\n\n" + e);
        }
    }

    /**
     * <p>A helper method to be used to <code>nullify</code> the singleton instance. The method uses a <code>Java
     * Reflection API</code> to access the field and initialize the field with <code>null</code> value. The operation
     * may fail if a <code>SecurityManager</code> prohibits such sort of accessing.</p>
     *
     * @param clazz a <code>Class</code> representing the class of the <code>Singleton</code> instance.
     * @param instanceName a <code>String</code> providing the name of the static field holding the reference to the
     *        singleton instance.
     */
    protected final void releaseSingletonInstance(Class clazz, String instanceName) {
        try {
            Field instanceField = clazz.getDeclaredField(instanceName);
            boolean accessibility = instanceField.isAccessible();
            instanceField.setAccessible(true);

            if (instanceField.getType().getName().equals(clazz.getName())) {
                if (Modifier.isStatic(instanceField.getModifiers())) {
                    instanceField.set(null, null);
                } else {
                    System.err.println("An error occurred while trying to release the singleton instance - the "
                        + " '" + instanceName + "' field is not static");
                }
            } else {
                System.err.println("An error occurred while trying to release the singleton instance - the type of "
                    + " '" + instanceName + "' field is not equal to specified one : " + clazz.getName());
            }

            instanceField.setAccessible(accessibility);
        } catch (Exception e) {
            System.err.println("An error occurred while trying to release the singleton instance : " + e);
        }
    }
}

