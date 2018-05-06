/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.processor.ipserver;

import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.config.ConfigManagerException;
import com.topcoder.util.config.ConfigManagerInterface;

import java.io.IOException;

import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;


/**
 * <p>
 * The IPServerManager class is used to manage the instances of the IPServer class This is the main usage of this
 * class, it contains named IPServer instances. Note that IP server names are case sensitive. Methods to add, remove,
 * clear and get IP server instances are provided to fulfill this task.
 * </p>
 *
 * <p>
 * Using this class is not required. The user can use IP server instances without registering them with the manager.
 * This class is only provided as a convenient way to keep and access the IP server instances inside an application.
 * </p>
 *
 * <p>
 * This class has also the role of handling instantiation and configuration of the IP server implementations using a
 * configuration file, without writing any code. The user can either configure everything through code using the
 * provided API or can simply write it all in a configuration file. The manager reads the configuration file and
 * dynamically creates instances of the IP servers. It also creates the handlers for the IP server using reflection.
 * </p>
 *
 * <p>
 * This class is a singleton. There is no reason in having multiple managers since multiple IP server instances can be
 * defined.
 * </p>
 *
 * <p>
 * <em>Thread Safety: </em>this class has been made thread safe by marking the access to the ipServers map and the
 * singleton implemented as synchronized.
 * </p>
 *
 * <p>
 * Sample configuration file for this class:
 * <pre>
 * # ConfigManager can know use "," as delimiter for multiple-value properties
 * ListDelimiter=,
 * # the IP server names (required)
 * servers=server1
 * ##########################
 * # the namespace to configure the message factory
 * server1_message_factory_namespace=com.topcoder.processor.ipserver.message
 * # the listening address for server1 (optional, wildcard address used if absent)
 * server1_address=127.0.0.1
 * # the port for server1 (required)
 * server1_port=8080
 * # maximum connections for the server(optional, defaults to 0 - unlimited)
 * server1_max_connections=5000
 * # is the server started automatically at startup? (optional, false is absent)
 * server1_started=true
 * # backlog for the server started (optional, 0 means 50)
 * server1_backlog=100
 * # the names of the handlers for this server (required)
 * server1_handlers=handler1,handler2,keepalive
 * # the class name of handler1 (required)
 * server1_handler1_class=com.topcoder.processor.ipserver.Server1Handler1
 * # maximum requests for the handler (optional, defaults to 0 - unlimited)
 * server1_handler1_max_requests=30
 * # the class name of handler2 (required)
 * server1_handler2_class=com.topcoder.processor.ipserver.Server1Handler2
 * # maximum requests for the handler (optional, defaults to 0 - unlimited)
 * server1_handler2_max_requests=10
 * # special handler class that adds keep alive capability to the server
 * server1_keepalive_class=com.topcoder.processor.ipserver.keepalive.KeepAliveHandler
 * </pre>
 * </p>
 *
 * @author visualage, zsudraco
 * @version 1.0
 * @copyright Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
public class IPServerManager implements ConfigManagerInterface {
    /**
     * <p>
     * The Configuration Manager namespace used by this component.
     * </p>
     */
    public static final String NAMESPACE = "com.topcoder.processor.ipserver";

    /**
     * <p>
     * The default config manager file. This file is loaded only if the config manager namespace is not created when
     * the component is instantiated for the first time. This gives the flexibility to use other config manager file
     * locations or formats, essential in development of applications using many different components, with different
     * configuration files, to avoid the nightmare of having a lot of different config files, spread everywhere.
     * </p>
     */
    private static final String DEFAULT_CONFIG_FILE = "com/topcoder/processor/ipserver/ipserver.properties";

    /**
     * <p>
     * Constant property used for specifying ip server names in configuration file.
     * </p>
     */
    private static final String SERVER_NAMES = "servers";

    /**
     * <p>
     * suffix to specify address for ip server.
     * </p>
     */
    private static final String ADDRESS_SUFFIX = "_address";

    /**
     * <p>
     * suffix to specify port for ip server.
     * </p>
     */
    private static final String PORT_SUFFIX = "_port";

    /**
     * <p>
     * suffix to specify max connections for ip server.
     * </p>
     */
    private static final String MAX_CONNECTIONS_SUFFIX = "_max_connections";

    /**
     * <p>
     * suffix to specify message factory namespace for ip server.
     * </p>
     */
    private static final String MESSAGE_FACTORY_NAMESPACE_SUFFIX = "_message_factory_namespace";

    /**
     * <p>
     * suffix to specify backlog for ip server.
     * </p>
     */
    private static final String BACKLOG_SUFFIX = "_backlog";

    /**
     * <p>
     * suffix to specify if the server started automatically at startup for ip server.
     * </p>
     */
    private static final String STARTED_SUFFIX = "_started";

    /**
     * <p>
     * suffix to specify handler names of one specified ip server.
     * </p>
     */
    private static final String HANDLERS_SUFFIX = "_handlers";

    /**
     * <p>
     * suffix to specify fully qualified class name for one specified handler of one specified ip server.
     * </p>
     */
    private static final String CLASS_SUFFIX = "_class";

    /**
     * <p>
     * suffix to specify max request number for one specified handler of one specified ip server.
     * </p>
     */
    private static final String MAX_REQUESTS_SUFFIX = "_max_requests";

    /**
     * <p>
     * The singleton instance of this class. Created in the getInstance method.
     * </p>
     */
    private static IPServerManager instance = null;

    /**
     * <p>
     * Mapping containing the registered IP server instances. It maps names (Strings) to instances of the IPServer
     * class. A HashMap is recommended.
     * </p>
     */
    private final Map ipServers;

    /**
     * <p>
     * Stores all property names from the namespace.
     * </p>
     */
    private Vector configPropNames = null;

    /**
     * <p>
     * Private constructor that prevents outside instantiation (singleton).
     * </p>
     *
     * <p>
     * The default config manager file is loaded only if the config manager namespace is not created when the component
     * is instantiated for the first time. This gives the flexibility to use other config manager file locations or
     * formats, essential in development of applications using many different components, with different configuration
     * files, to avoid the nightmare of having a lot of different config files, spread everywhere.
     * </p>
     *
     * @throws ConfigurationException used to wrap any exceptions related to the configuration data or that indicate a
     *         problem with the configuration file: configuration manager exceptions indicating bad or missing file,
     *         missing property, reflection exceptions indicating bad class names
     * @throws IOException exception indicating a socket error while starting an IP server
     */
    private IPServerManager() throws ConfigurationException, IOException {
        this.ipServers = new HashMap();

        ConfigManager configManager = ConfigManager.getInstance();

        // Is the namespace already set
        if (!configManager.existsNamespace(NAMESPACE)) {
            try {
                // Namespace not set. Load configurations from default configuration file.
                configManager.add(NAMESPACE, DEFAULT_CONFIG_FILE, ConfigManager.CONFIG_PROPERTIES_FORMAT);
            } catch (ConfigManagerException e) {
                throw new ConfigurationException("Cannot add namesapce: " + NAMESPACE, e);
            }
        }

        // Cache all property names, to be returned when method getConfigPropNames() is called.
        this.configPropNames = new Vector();

        for (Enumeration propEnum = configManager.getPropertyNames(NAMESPACE);
                propEnum.hasMoreElements();) {
            this.configPropNames.add(propEnum.nextElement());
        }

        // All ip server names is retrieved.
        String[] names = configManager.getStringArray(NAMESPACE, SERVER_NAMES);

        if (names == null) {
            throw new ConfigurationException("Missing ipserver names property: " + SERVER_NAMES);
        }

        // Iterate through each persistence name.
        for (int i = 0; i < names.length; i++) {
            // Add server instance
            this.ipServers.put(names[i], processServer(names[i]));
        }
    }

    /**
     * <p>
     * Gets the singleton instance (singleton pattern).
     * </p>
     *
     * @return the singleton instance of this class
     *
     * @throws ConfigurationException used to wrap any exceptions related to the configuration data or that indicate a
     *         problem with the configuration file: configuration manager exceptions indicating bad or missing file,
     *         missing property, reflection exceptions indicating bad class names
     * @throws IOException exception indicating a socket error while starting an IP server
     */
    public static synchronized IPServerManager getInstance()
        throws ConfigurationException, IOException {
        if (instance == null) {
            IPServerManager.instance = new IPServerManager();
        }

        return IPServerManager.instance;
    }

    /**
     * <p>
     * Adds an IP server instance to the manager. The IP server instance is added to the map of IP server instances
     * under the given name as key. If the IP server name is used (the key already exists) then no action is taken and
     * false is returned.
     * </p>
     *
     * @param name the IP server instance name.
     * @param ipServer the IP server instance.
     *
     * @return whether the IP server instance was added or not.
     *
     * @throws NullPointerException if any argument is null.
     */
    public boolean addIPServer(String name, IPServer ipServer) {
        if (name == null) {
            throw new NullPointerException("The given name cannot be null.");
        }

        if (ipServer == null) {
            throw new NullPointerException("The given ipServer cannot be null.");
        }

        synchronized (this.ipServers) {
            // Already contains, just return false
            if (this.ipServers.containsKey(name)) {
                return false;
            }

            this.ipServers.put(name, ipServer);
        }

        return true;
    }

    /**
     * <p>
     * Removes an IP server instance from the manager. The IP server instance is removed from the map of IP server
     * instances given its name. If the IP server instance does not exist (the key does not exist) then no action is
     * performed and false is returned.
     * </p>
     *
     * @param name the name of the IP server instance to remove
     *
     * @return whether the IP server instance was removed or not
     *
     * @throws NullPointerException if the argument is null
     */
    public boolean removeIPServer(String name) {
        if (name == null) {
            throw new NullPointerException("The given name cannot be null.");
        }

        synchronized (this.ipServers) {
            return this.ipServers.remove(name) != null;
        }
    }

    /**
     * <p>
     * The clearIPServers method clears all the IP server instances from the manager. It does so by clearing the map
     * storing them.
     * </p>
     */
    public void clearIPServers() {
        synchronized (this.ipServers) {
            this.ipServers.clear();
        }
    }

    /**
     * <p>
     * Returns whether an IP server instance name is registered in this manager.
     * </p>
     *
     * @param name the name of the IP server instance to be tested
     *
     * @return whether the IP server instance name is registered or not
     *
     * @throws NullPointerException if the argument is null
     */
    public boolean containsIPServer(String name) {
        if (name == null) {
            throw new NullPointerException("The given name cannot be null.");
        }

        synchronized (this.ipServers) {
            return this.ipServers.containsKey(name);
        }
    }

    /**
     * <p>
     * Returns the list of the IP server instance names contained in the manager. It actually returns a set of keys
     * contained in the IP server instance map. Note that a copy is returned, not the set obtained from Map.keySet,
     * nor a unmodifiable set (because it is backed by the inner key set that can change).
     * </p>
     *
     * @return the list of IP server instance names (list of Strings)
     */
    public Set getIPServerNames() {
        synchronized (this.ipServers) {
            return new HashSet(this.ipServers.keySet());
        }
    }

    /**
     * <p>
     * Gets the IP server instance that is registered in the manager given its name. If the name does not exist, null
     * is returned
     * </p>
     *
     * @param name name the name of the IP server instance
     *
     * @return the IP server instance with the given name or null if there isn't an instance with that name
     *
     * @throws NullPointerException if the argument is null.
     */
    public IPServer getIPServer(String name) {
        if (name == null) {
            throw new NullPointerException("The given name cannot be null.");
        }

        synchronized (this.ipServers) {
            return (IPServer) this.ipServers.get(name);
        }
    }

    /**
     * <p>
     * Returns the namespace used by this component (the value of the NAMESPACE constant).
     * </p>
     *
     * @return the namespace
     */
    public String getNamespace() {
        return IPServerManager.NAMESPACE;
    }

    /**
     * <p>
     * Returns the configuration property names used by this component (at configuration file load time).
     * time.
     * </p>
     *
     * @return an enumeration with the property names
     */
    public Enumeration getConfigPropNames() {
        return this.configPropNames.elements();
    }

    /**
     * Return the int value of the property.
     *
     * @param propertyName the name of property
     * @param required whether this property is required
     * @param errorMessage the error message to throw if configuration errors occorred
     *
     * @return the int value of the property
     *
     * @throws ConfigurationException used to wrap any exceptions related to the configuration data or that indicate a
     *         problem with the configuration file: configuration manager exceptions indicating bad or missing file,
     *         missing property, reflection exceptions indicating bad class names
     * @throws IOException if errors occurred in ConfigManager
     */
    private int getIntValue(String propertyName, boolean required, String errorMessage)
        throws ConfigurationException, IOException {
        String property = ConfigManager.getInstance().getString(NAMESPACE, propertyName);

        if ((property == null) || (property.trim().length() == 0)) {
            if (required) {
                throw new ConfigurationException(errorMessage);
            }

            return 0;
        }

        try {
            return Integer.parseInt(property);
        } catch (NumberFormatException e) {
            throw new ConfigurationException(errorMessage);
        }
    }

    /**
     * Create IPServer instance with given server name.
     *
     * @param serverName the server name
     * @return The IPServer with given serverName
     *
     * @throws ConfigurationException used to wrap any exceptions related to the configuration data or that indicate a
     *         problem with the configuration file: configuration manager exceptions indicating bad or missing file,
     *         missing property, reflection exceptions indicating bad class names
     * @throws IOException exception indicating a socket error while starting an IP server
     */
    private IPServer processServer(String serverName) throws ConfigurationException, IOException {
        ConfigManager configManager = ConfigManager.getInstance();
        String address = configManager.getString(IPServerManager.NAMESPACE,
                serverName + IPServerManager.ADDRESS_SUFFIX);

        // check if address is valid
        if (address != null) {
            try {
                InetAddress.getByName(address);
            } catch (UnknownHostException e) {
                throw new ConfigurationException("Invalid server address: " + address, e);
            }
        }

        // port property is required
        int port = getIntValue(serverName + IPServerManager.PORT_SUFFIX, true,
                "Invalid port property value for ip server: " + serverName);

        // The port must be between 0 and 65535, inclusive.
        if ((port < 0) || (port > IOHelper.MAX_PORT)) {
            throw new ConfigurationException("Port property value out of range: " + port);
        }

        // max connections property is optional, 0 if missing
        int maxConnections = getIntValue(serverName + IPServerManager.MAX_CONNECTIONS_SUFFIX, false,
                "Invalid max connections property value for ip server: " + serverName);

        // max request cannot be negative
        if (maxConnections < 0) {
            throw new ConfigurationException("The max connections property value cannot be negative for ipserver: "
                    + serverName + " value: " + maxConnections);
        }

        // message factory namespace property is required
        String messageFactoryNamespace = configManager.getString(IPServerManager.NAMESPACE,
            serverName + IPServerManager.MESSAGE_FACTORY_NAMESPACE_SUFFIX);
        if (messageFactoryNamespace == null) {
            throw new ConfigurationException("Message factory namespace property is missing.");
        }
        if (messageFactoryNamespace.trim().length() == 0) {
            throw new ConfigurationException("Message factory namespace must not be empty.");
        }

        // backlog property is optional, 0 if missing
        int backlog = getIntValue(serverName + IPServerManager.BACKLOG_SUFFIX, false,
                "Invalid backlog property value for ip server: " + serverName);

        // strbacklog cannot be negative
        if (backlog < 0) {
            throw new ConfigurationException("The backlog property value cannot be negative for ipserver: "
                    + serverName + " value: " + backlog);
        }

        // The started property is optional, , false is absent
        String strStarted = configManager.getString(IPServerManager.NAMESPACE,
                serverName + IPServerManager.STARTED_SUFFIX);
        boolean isStarted = false;

        if (strStarted != null) {
            if (strStarted.equalsIgnoreCase("true")) {
                isStarted = true;
            } else if (!strStarted.equalsIgnoreCase("false")) {
                // Invalid started value
                throw new ConfigurationException("Invalid started value: " + strStarted);
            }
        }

        // get all handlers name of ip server
        String[] handlers = configManager.getStringArray(IPServerManager.NAMESPACE,
                serverName + IPServerManager.HANDLERS_SUFFIX);

        // handlers property is required
        if ((handlers == null) || (handlers.length == 0)) {
            throw new ConfigurationException("Missing handlers property for ip server: " + serverName);
        }

        IPServer server = new IPServer(address, port, maxConnections, backlog, messageFactoryNamespace);

        // process all handlers of this ip server one by one
        for (int j = 0; j < handlers.length; j++) {
            server.addHandler(handlers[j], processHandler(serverName, handlers[j]));
        }

        // Start ipServer with isStarted property
        if (isStarted) {
            // Needn't to create a new Thread to start the server
            server.start();
        }
        return server;
    }

    /**
     * Create handler instance with given server name and handler name.
     *
     * @param serverName the server name
     * @param handlerName the handler name
     * @return The handler with given serverName, handlerName
     *
     * @throws ConfigurationException used to wrap any exceptions related to the configuration data or that indicate a
     *         problem with the configuration file: configuration manager exceptions indicating bad or missing file,
     *         missing property, reflection exceptions indicating bad class names
     * @throws IOException exception indicating a socket error while starting an IP server
     */
    private Handler processHandler(String serverName, String handlerName) throws ConfigurationException, IOException {
        handlerName = serverName + "_" + handlerName;
        String handlerClassName = ConfigManager.getInstance().getString(IPServerManager.NAMESPACE,
                handlerName + IPServerManager.CLASS_SUFFIX);

        // handler class property is required
        if ((handlerClassName == null) || (handlerClassName.trim().length() == 0)) {
            throw new ConfigurationException("Missing handler class property for handler: " + handlerName);
        }

        int maxRequests = getIntValue(handlerName + IPServerManager.MAX_REQUESTS_SUFFIX, false,
                "Invalid max requests property value for handler: " + handlerName);

        // max request cannot be negative
        if (maxRequests < 0) {
            throw new ConfigurationException(
                "The max requests property value cannot be negative for handler: " + handlerName
                + " value: " + maxRequests);
        }

        // Create handler instance with given handler class and maxRequests properties
        try {
            if (maxRequests == 0) {
                try {
                    return (Handler) Class.forName(handlerClassName).newInstance();
                } catch (InstantiationException e) {
                    // The default constructor fails for maybe no nullary constructor,
                    // then try the constructor accepting an integer.
                } catch (IllegalAccessException e) {
                    // The default constructor fails for maybe failure to access nullary constructor,
                    // then try the below constructor accepting an integer.
                }
            }

            return (Handler) Class.forName(handlerClassName).getConstructor(
                new Class[] {int.class}).newInstance(new Object[] {new Integer(maxRequests)});
        } catch (ClassNotFoundException e) {
            throw new ConfigurationException("Cannot create Handler instance: " + handlerClassName, e);
        } catch (InstantiationException e) {
            throw new ConfigurationException("Cannot create Handler instance: " + handlerClassName, e);
        } catch (IllegalAccessException e) {
            throw new ConfigurationException("Cannot create Handler instance: " + handlerClassName, e);
        } catch (IllegalArgumentException e) {
            throw new ConfigurationException("Cannot create Handler instance: " + handlerClassName, e);
        } catch (SecurityException e) {
            throw new ConfigurationException("Cannot create Handler instance: " + handlerClassName, e);
        } catch (ExceptionInInitializerError e) {
            throw new ConfigurationException("Cannot create Handler instance: " + handlerClassName, e);
        } catch (InvocationTargetException e) {
            throw new ConfigurationException("Cannot create Handler instance: " + handlerClassName, e);
        } catch (NoSuchMethodException e) {
            throw new ConfigurationException("Cannot create Handler instance: " + handlerClassName, e);
        } catch (ClassCastException e) {
            throw new ConfigurationException("Cannot create Handler instance: " + handlerClassName, e);
        }
    }
}
