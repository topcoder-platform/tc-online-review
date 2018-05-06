/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.servlet.request.failuretests;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.util.Properties;

import com.topcoder.processor.ipserver.ConfigurationException;
import com.topcoder.processor.ipserver.IPServer;
import com.topcoder.processor.ipserver.IPServerManager;

import com.topcoder.util.config.ConfigManager;

/**
 * A helper class provides some convenient methods which will be shared by all the tests. A utility class implements the
 * singleton design pattern.
 * @author FireIce
 * @version 2.0
 */
public class ConfigHelper {
    /**
     * Represents the server file location.
     */
    public static final String SERVER_FILE_LOCATION = "test_files/failuretests/server/";

    /**
     * Represents the cilent file location.
     */
    public static final String CLIENT_FILE_LOCATION = "test_files/failuretests/client/";

    /**
     * Represents the xml file for server files.
     */
    public static final File SERVER_FILES_FILE = new File(SERVER_FILE_LOCATION + "files.xml");

    /**
     * Represents the xml file for server groups.
     */
    public static final File SERVER_GROUPS_FILE = new File(SERVER_FILE_LOCATION + "groups.xml");

    /** The file to configure ipserver manager. */
    public static final String IPSERVER_MANAGER_CONFIG_FILE = "test_files/failuretests/IPServerManager.xml";

    /** The file to configure message factory. */
    public static final String MESSAGE_FACTORY_CONFIG_FILE = "test_files/failuretests/MessageFactory.xml";

    /**
     * the file to configure DBConnectionFactoryImpl.
     */
    public static final String DBCONNECTION_FACTORY_CONFIG_FILE = "test_files/failuretests/DBConnectionFactoryImpl.xml";

    /**
     * the namespace to configure dbconnection factory.
     */
    public static final String DBCONNECTION_FACTORY_NAMESPACE = "com.topcoder.db.connectionfactory.DBConnectionFactoryImpl";

    /** The namespace to configure ipserver manager. */
    public static final String IPSERVER_MANAGER_NAMESPACE = IPServerManager.NAMESPACE;

    /** The namespace to configure message factory. */
    public static final String MESSAGE_FACTORY_NAMESPACE = "com.topcoder.processor.ipserver.message";

    /** The connection id used for testing. */
    public static final String CONNECTION_ID = "test connection id";

    /** The idgenerator namespace used in test. */
    public static final String IDGENERATOR_NAMESPACE = "unit_test_id_sequence";

    /** The singleton instance of this class. */
    private static ConfigHelper instance = null;

    /** Represents the maxRequests used for Handler test. */
    private int maxRequests = 100;

    /** Represents the maxConnections used for Handler test. */
    private int maxConnections = 100;

    /** The IPServer instance used for testing. */
    private IPServer ipServer = null;

    /** The address used for testing. */
    private String address = null;

    /** The port used for testing. */
    private int port = -1;

    /**
     * Private constructor to ensure singleton.
     */
    private ConfigHelper() {
        try {
            // load the config from file
            Properties prop = new Properties();
            prop.load(new FileInputStream("test_files/failuretests/test.properties"));

            this.address = prop.getProperty("address");
            this.port = Integer.parseInt(prop.getProperty("port"));
            this.ipServer = new IPServer(this.address, this.port, this.maxConnections, 0, MESSAGE_FACTORY_NAMESPACE);
        } catch (IOException ex) {
            System.out.println(ex);
        } catch (ConfigurationException ex) {
            System.out.println(ex);
        }
    }

    /**
     * Return the singleton instance of this class.
     * @return the singleton instance
     */
    static synchronized ConfigHelper getInstance() {
        if (instance == null) {
            instance = new ConfigHelper();
        }

        return instance;
    }

    /**
     * Load namespaces for the tests.
     * @throws Exception
     *             to JUnit
     */
    static void loadNamespaces() throws Exception {
        releaseNamespaces();
        ConfigManager cm = ConfigManager.getInstance();
        cm.add(IPSERVER_MANAGER_NAMESPACE, IPSERVER_MANAGER_CONFIG_FILE, ConfigManager.CONFIG_XML_FORMAT);
        cm.add(MESSAGE_FACTORY_NAMESPACE, MESSAGE_FACTORY_CONFIG_FILE, ConfigManager.CONFIG_XML_FORMAT);
        cm.add(DBCONNECTION_FACTORY_NAMESPACE, DBCONNECTION_FACTORY_CONFIG_FILE,
            ConfigManager.CONFIG_MULTIPLE_XML_FORMAT);
    }

    /**
     * Release namespaces in configuratin manager.
     * @throws Exception
     *             to JUnit
     */
    static void releaseNamespaces() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();
        if (cm.existsNamespace(IPSERVER_MANAGER_NAMESPACE)) {
            cm.removeNamespace(IPSERVER_MANAGER_NAMESPACE);
        }
        if (cm.existsNamespace(MESSAGE_FACTORY_NAMESPACE)) {
            cm.removeNamespace(MESSAGE_FACTORY_NAMESPACE);
        }
        if (cm.existsNamespace(DBCONNECTION_FACTORY_NAMESPACE)) {
            cm.removeNamespace(DBCONNECTION_FACTORY_NAMESPACE);
        }
    }

    /**
     * <p>
     * Get the IPServer instance that can be used by other unit tests.
     * </p>
     * @return IPServer instance
     */
    IPServer getIPServer() {
        return this.ipServer;
    }

    /**
     * <p>
     * Get the max requests of Handler that can be used by other unit tests.
     * </p>
     * @return max requests of Handler
     */
    int getMaxRequests() {
        return this.maxRequests;
    }

    /**
     * <p>
     * Get the max connections of IPServer that can be used by other unit tests.
     * </p>
     * @return max connections of IPServer
     */
    int getMaxConnections() {
        return this.maxConnections;
    }

    /**
     * <p>
     * Get the port of IPServer that can be used by other unit tests.
     * </p>
     * @return port of IPServer
     */
    int getPort() {
        return this.port;
    }

    /**
     * <p>
     * Get the address of IPServer that can be used by other unit tests.
     * </p>
     * @return address of IPServer
     */
    String getAddress() {
        return this.address;
    }

    /**
     * This method start the given server.
     * @param server
     *            the server will be start
     * @throws IOException
     *             to JUnit
     */
    static void startServer(IPServer server) throws IOException {
        if (!server.isStarted()) {
            server.start();
        }
    }

    /**
     * This method ensure that the given server is stopped.
     * @param server
     *            the server will be stopped.
     */
    static void stopServer(IPServer server) {
        if (server.isStarted()) {
            server.stop();
        }

        // Loop until server is really stopped
        while (server.isStarted()) {
            synchronized (server) {
                try {
                    server.wait(100);
                } catch (InterruptedException e) {
                    // Clear the interrupted status
                    Thread.interrupted();
                }
            }
        }
    }
}
