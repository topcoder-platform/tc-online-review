/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.processor.ipserver;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

import com.topcoder.processor.ipserver.keepalive.KeepAliveHandler;
import com.topcoder.util.config.ConfigManager;

/**
 * A helper class provides some convenient methods which will be shared by all the tests. A utility class implements the
 * singleton design pattern.
 * @author zsudraco, FireIce
 * @version 2.0.1
 * @copyright Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
public class ConfigHelper {
    /** The file to configure ipserver manager. */
    public static final String IPSERVER_MANAGER_CONFIG_FILE = "test_files/IPServerManagerConfig.xml";

    /** The file to configure message factory. */
    public static final String MESSAGE_FACTORY_CONFIG_FILE = "test_files/MessageFactoryConfig.xml";

    /** The namespace to configure ipserver manager. */
    public static final String IPSERVER_MANAGER_NAMESPACE = IPServerManager.NAMESPACE;

    /** The namespace to configure message factory. */
    public static final String MESSAGE_FACTORY_NAMESPACE = "com.topcoder.processor.ipserver.message";

    /** The connection id used for testing. */
    public static final String CONNECTION_ID = "test connection id";

    /** The singleton instance of this class. */
    private static ConfigHelper instance = null;

    /** Represents a mock ServerSocket instance for other unit tests class. */
    private ServerSocket server = null;

    /** Represents a mock Socket instance for other unit tests class. */
    private Socket socket = null;

    /** Represents Connection instance for other unit tests class. */
    private Connection connection = null;

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
            prop.load(new FileInputStream("test_files/test.properties"));

            this.address = prop.getProperty("address");
            this.port = Integer.parseInt(prop.getProperty("port"));

            server = new ServerSocket(this.port, 0, InetAddress.getLocalHost());

            socket = new Socket(server.getInetAddress().getHostAddress(), this.port);

            // Needn't to start server, mock socket is only for testing
            server.close();
            this.ipServer = new IPServer(this.address, this.port, this.maxConnections, 0, MESSAGE_FACTORY_NAMESPACE);
            this.connection = new Connection(CONNECTION_ID, this.ipServer, this.socket);
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
    }

    /**
     * <p>
     * Get the Connection instance that can be used by other unit tests.
     * </p>
     * @return connection instance
     */
    Connection getConnection() {
        return this.connection;
    }

    /**
     * <p>
     * Get the socket instance that can be used by other unit tests.
     * </p>
     * @return socket instance
     */
    Socket getSocket() {
        return this.socket;
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
            server.addHandler(KeepAliveHandler.KEEP_ALIVE_ID, new KeepAliveHandler());
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

    /**
     * <p>
     * Get the field value of an object.
     * </p>
     * @param obj
     *            the object where to get the field value.
     * @param fieldName
     *            the name of the field.
     * @return the field value
     * @throws Exception
     *             any exception occurs.
     */
    static Object getFieldValue(Object obj, String fieldName) throws Exception {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(obj);
    }
}
