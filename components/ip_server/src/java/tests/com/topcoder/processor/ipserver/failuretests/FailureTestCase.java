/*
 * Copyright (C) 2005, TopCoder Inc. All rights reserved.
 */
package com.topcoder.processor.ipserver.failuretests;

import com.topcoder.processor.ipserver.ConfigurationException;
import com.topcoder.processor.ipserver.Connection;
import com.topcoder.processor.ipserver.IPServer;

import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.config.ConfigManagerException;
import com.topcoder.util.config.UnknownNamespaceException;

import junit.framework.TestCase;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import java.net.Socket;

import java.util.Properties;


/**
 * <p>
 * A parent class for failure test cases providing a set of convenient methods for getting the configuration properties
 * and starting/stopping the server instance.
 * </p>
 *
 * @author isv, brain_cn
 * @version 2.0
 */
public class FailureTestCase extends TestCase {
    /**
     * <p>
     * A <code>File</code> referencing the directory under which the test files are to be placed.
     * </p>
     */
    public final static File TEST_FILES_ROOT = new File("test_files");

    /**
     * <p>
     * A <code>File</code> referencing the directory under which the failure test files are to be placed.
     * </p>
     */
    public final static File FAILURE_TEST_FILES_ROOT = new File(TEST_FILES_ROOT, "failure");

    /** The message factory namespace. */
    protected static final String MESSAGE_NAMESPACE = "com.topcoder.processor.ipserver.message";

    /**
     * <p>
     * A <code>Properties</code> providing the configuration properties for failure tests.
     * </p>
     */
    private Properties properties = null;

    /**
     * <p>
     * A sample instance of <code>IPServer</code> used for testing.
     * </p>
     */
    private IPServer server = null;

    /**
     * <p>
     * Constructs new <code>FailureTestCase</code>. The properties are initialized from the <code>
     * FailureTestsConfig.properties</code> file and the server instance is initialized but not started.
     * </p>
     */
    protected FailureTestCase() {
        try {
            loadNamespaces();
        } catch (Exception e) {
            e.printStackTrace();
        }
        properties = new Properties();

        try {
            properties.load(new FileInputStream(new File(FAILURE_TEST_FILES_ROOT, "FailureTestsConfig.properties")));
        } catch (IOException e) {
            System.err.println("FailureTestsConfig.properties is not loaded.\n" + e);
        }

        try {
            server =
                new IPServer(null, Integer.parseInt(getProperty("server_port")), 5, 5,
                        MESSAGE_NAMESPACE);
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * The test namespace for message factory.
     */
    static final String[] NAMESPACES = new String[] {
            "com.topcoder.processor.ipserver.message"
    };
    
    /**
     * The test namespace for message factory.
     */
    static final String[] CONFIG_FILES = new String[] {
            "MessageFactoryConfig.xml"
    };

    /**
     * Load test namespaces
     * @throws Exception to JUnit
     */
    protected static void loadNamespaces() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();
        for (int i = 0; i < NAMESPACES.length; i++) {
            if (!cm.existsNamespace(NAMESPACES[i])) {
                cm.add("failure/" + CONFIG_FILES[i]);
            }
        }
    }

    /**
     * <p>
     * Starts the sample server.
     * </p>
     *
     * @throws IOException
     */
    protected final void startServer() throws IOException {
        server.start();
    }

    /**
     * <p>
     * Stops the sample server.
     * </p>
     */
    protected final void stopServer() {
        if (server.isStarted()) {
            server.stop();
        }

        while (server.isStarted()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        }
    }

    /**
     * <p>
     * Gets the sample server.
     * </p>
     *
     * @return an <code>IPServer</code> representing the sample server.
     */
    protected final IPServer getServer() {
        return server;
    }

    /**
     * <p>
     * Gets the value for specified configuration property.
     * </p>
     *
     * @param key a <code>String</code> name of the requested property.
     *
     * @return a <code>String</code> value of the property or <code>null</code> for unknown property names.
     */
    protected String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * Release all test namespaces.
     *
     * @param namespace a <code>String</code> referencing the configuration namespace to remove from configuration
     *        manager.
     */
    protected final void releaseNamespace(String namespace) {
        ConfigManager configManager = ConfigManager.getInstance();

        if (configManager.existsNamespace(namespace)) {
            try {
                configManager.removeNamespace(namespace);
            } catch (UnknownNamespaceException e) {
            }
        }
    }

    /**
     * <p>
     * A helper method to be used to initialize the specified configuration namespace with the configuration properties
     * provided by specified file. If specified namespace is already loaded to <code>ConfigurationManager </code> then
     * it is re-loaded with new configuration properties.
     * </p>
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
            } catch (UnknownNamespaceException e) {
            }
        }

        try {
            configManager.add(namespace, filename, format);
        } catch (ConfigManagerException e) {
            System.err.println("An error occurred while loading the configuration namespace '" + namespace
                + "' from file : " + filename + "\n\n" + e);
        }
    }

    /**
     * <p>
     * A helper method to be used to <code>nullify</code> the singleton instance. The method uses a <code>Java
     * Reflection API</code> to access the field and initialize the field with <code>null</code> value. The operation
     * may fail if a <code>SecurityManager</code> prohibits such sort of accessing.
     * </p>
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
                    System.err.println("An error occurred while trying to release the singleton instance - the " + " '"
                        + instanceName + "' field is not static");
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

    /**
     * <p>
     * Gets the constructor for <code>Connection</code> class via <code>Java Reflection API</code>.
     * </p>
     *
     * @return a <code>Constructor</code> provided by <code>Connection</code> class.
     */
    protected static final Constructor getConnectionConstructor() {
        Class connectionClass = Connection.class;
        Constructor constructor = connectionClass.getDeclaredConstructors()[0];

        return constructor;
    }

    /**
     * <p>
     * A helper method to be used to instantiate the <code>Connection</code> object with specified parameters through
     * the <code>Java Reflection API</code>.
     * </p>
     *
     * @param id a <code>String</code> ID to be passed to constructor.
     * @param server an <code>IPServer</code> to be passed to constructor.
     * @param socket a <code>Socket</code> to be passed to constructor.
     *
     * @return the connection instance with given arguments
     */
    protected static final Connection createConnection(String id, IPServer server, Socket socket) {
        Constructor constructor = getConnectionConstructor();
        boolean accessibility = constructor.isAccessible();
        constructor.setAccessible(true);

        Object[] parameters = new Object[] {id, server, socket};

        try {
            return (Connection) constructor.newInstance(parameters);
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        } catch (InstantiationException e) {
        } finally {
            try {
                // Restore the original accessibility of the constructor
                constructor.setAccessible(accessibility);
            } catch (Exception e) {
            }
        }

        return null;
    }
}
