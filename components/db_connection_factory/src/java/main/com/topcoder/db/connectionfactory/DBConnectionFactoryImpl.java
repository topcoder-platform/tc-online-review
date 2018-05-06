/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.db.connectionfactory;

import com.topcoder.configuration.ConfigurationAccessException;
import com.topcoder.configuration.ConfigurationObject;

import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.config.ConfigManagerException;
import com.topcoder.util.config.Property;
import com.topcoder.util.config.UnknownNamespaceException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import java.sql.Connection;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * A default implementation of {@link DBConnectionFactory} interface.
 * </p>
 * <p>
 * This class implements a simple mapping from the names to <code>ConnectionProducer</code>s
 * which are used to create the connections to the databases. Any of registered producers can be set
 * to be used to create the connections by default.
 * </p>
 * <p>
 * This class provides the constructors allowing to instantiate and initialize the factory instance
 * either programmatically or from the parameters specified by the configuration file. A
 * non-argument constructor may be used to create the empty factory which may be populated at
 * runtime through appropriate mutator methods. The constructor accepting a single
 * <code>String</code> argument may be used to instantiate and initialize the factory from the
 * parameters provided by a specified configuration namespace.
 * </p>
 * <p>
 * <b> Main Changes in version 1.1: </b><br>
 * (1) Two extra methods declared in DBConnectionFactory interface to allow clients obtaining
 * connections that are authenticated with user-provided credentials rather than credentials
 * recorded in component configuration are implemented in this class. <br>
 * (2) All the NullPointerException thrown are replaced by IllegalArgumentException in this version.
 * <br>
 * (3) Two extra constructors are added in this version to support creating the factory based on the
 * configuration described with a ConfigurationObject. The original constructor reading
 * configuration from config-manager is deprecated.
 * </p>
 * <p>
 * <b>Sample Configuration:</b>
 * </p>
 *
 * <pre>
 *   &lt;Config name=&quot;com.topcoder.db.connectionfactory.DBConnectionFactoryImpl&quot;&gt;
 *       &lt;Property name=&quot;connections&quot;&gt;
 *           &lt;Property name=&quot;default&quot;&gt;
 *               &lt;Value&gt;MySqlJDBCConnection&lt;/Value&gt;
 *           &lt;/Property&gt;
 *           &lt;Property name=&quot;MySqlJDBCConnection&quot;&gt;
 *               &lt;Property name=&quot;producer&quot;&gt;
 *                   &lt;Value&gt;com.topcoder.db.connectionfactory.producers.JDBCConnectionProducer&lt;/Value&gt;
 *               &lt;/Property&gt;
 *               &lt;Property name=&quot;parameters&quot;&gt;
 *                  &lt;!---Here is the configuration for creating ConnectionProducer--&gt;
 *               &lt;/Property&gt;
 *            &lt;/Property&gt;
 *        &lt;/Property&gt;
 *      &lt;/Property&gt;
 *   &lt;/Config&gt;
 * </pre>
 *
 * <p>
 * Note: The Property and the ConfigurationObject used to initialize this class have the similar
 * formats (see sample above). But the configuration from ConfigurationObject exists in the
 * "parameters" child of the root configuration object.
 * </p>
 * <p>
 * Side Note : Empty String means the length of string after trimming equals to zero.
 * </p>
 * <p>
 * <strong>Thread Safety:</strong> this class is thread-safe. To maintain the thread safety the
 * methods affecting the state of the factory, like {@link #add(String,
 * com.topcoder.db.connectionfactory.producers.ConnectionProducer)}, {@link #clear()}, {@link
 * #remove(String)}, {@link #setDefault(String)} are declared with 'synchronized' access. The thread
 * safety is implemented at instance level rather than at instance field's level since, most likely,
 * the mutator methods will be called at the construction and initialization time. They are not
 * expected to be called intensively during the lifetime of a factory instance. Note that if the
 * users use the iterator returned by listConnectionProducerNames to traverse the names and they
 * still want the thread safety when traversing, they must perform synchronized(this factory) during
 * the whole traverse.
 * </p>
 *
 * @author isv, qiucx0161, nhzp339, magicpig
 * @version 1.1
 * @since 1.0
 */
public class DBConnectionFactoryImpl implements DBConnectionFactory {
    /**
     * <p>
     * The default namespace to read the configuration from the ConfigurationObject passed into the
     * constructors. It's immutable and will be used by the new constructor {@link
     * #DBConnectionFactoryImpl(com.topcoder.configuration.ConfigurationObject)}.
     * </p>
     *
     * @since 1.1
     */
    public static final String DEFAULT_NAMESPACE = DBConnectionFactoryImpl.class.getName();

    /**
     * The array which contains one Class of Property.
     *
     * @since 1.1
     */
    private static final Class[] PROPERTY_CLASS_ARRAY = new Class[]{Property.class };

    /**
     * The array which contains one Class of ConfigurationObject.
     *
     * @since 1.1
     */
    private static final Class[] CONFIGURATIONOBJECT_CLASS_ARRAY = new Class[]{ConfigurationObject.class };

    /**
     * Represent the connections string.
     */
    private static final String CONNECTIONS = "connections";

    /**
     * Represent the default string.
     */
    private static final String DEFAULT = "default";

    /**
     * Represent the producer string.
     */
    private static final String PRODUCER = "producer";

    /**
     * Represent the parameters string.
     */
    private static final String PARAMETERS = "parameters";

    /**
     * <p>
     * A <code>Map</code> mapping the <code>String</code> names to a
     * <code>ConnectionProducer</code> instances. This map can be empty but can't be
     * <code>null</code>; the keys are non-null, non-empty strings; the values are non-null
     * ConnectionProducer instances.
     * </p>
     * <p>
     * This map is initialized when constructing and is updated through the methods like {@link
     * #add(String, com.topcoder.db.connectionfactory.producers.ConnectionProducer)}, {@link
     * #remove(String)}, {@link #clear()}.
     * </p>
     */
    private final Map connectionProducers = new HashMap();

    /**
     * <p>
     * A <code>String</code> specifying the name of the registered <code>ConnectionProducer</code>
     * to be used by default.
     * </p>
     * <p>
     * This field is initialized by {@link #DBConnectionFactoryImpl(String)} constructor and the two
     * constructor with ConfigurationObject parameter. It may be updated through {@link
     * #setDefault(String)}, {@link #remove(String)} methods during the lifetime of the factory
     * instance while {@link #clear()} method will nullify this field.
     * </p>
     * <p>
     * This field can't be empty string but can be <code>null</code> indicating no default
     * producer is specified. It will be used in methods creating connections from the default
     * producer and can be gotten via the {@link #getDefault()} method. The value of this field
     * should be consistent with the 'connectionProducers' mapping. Whenever this field is set to
     * non-null value the corresponding entry should exist within 'connectionProducers' mapping. If
     * such an entry is removed then this field should be nullified.
     * </p>
     */
    private String defaultProducerName = null;

    /**
     * <p>
     * Constructs new <code>DBConnectionFactoryImpl</code>. Such a new
     * <code>DBConnectionFactoryImpl</code> will have no <code>ConnectionProducer</code>s
     * registered and the default connection is non-determined.
     * </p>
     * <p>
     * This constructor can be used to instantiate the factory to be populated programmatically.
     * </p>
     */
    public DBConnectionFactoryImpl() {
        // empty
    }

    /**
     * <p>
     * Constructs new <code>DBConnectionFactoryImpl</code> initialized with the configuration
     * parameters provided by specified configuration namespace. The factory is populated with the
     * <code>ConnectionProducer</code>s constructed from the configuration parameters provided by
     * specified configuration namespace and mapped to corresponding names.
     * </p>
     * <p>
     * This constructor may be used to instantiate the factory initialized from the parameters
     * provided by specified configuration namespace.
     * </p>
     *
     * @param namespace
     *            a <code>String</code> specifying the configuration namespace to be used to
     *            initialize this factory.
     * @throws IllegalArgumentException
     *             if specified argument is <code>null</code> or an empty string.
     * @throws ConfigurationException
     *             if any error occurs while reading the configuration properties and initializing
     *             the state of the factory, including if specified namespace is not loaded to a
     *             <code>Configuration Manager</code>.
     * @throws UnknownConnectionException
     *             if the producers provided don't contain the default producer specified.
     * @deprecated
     */
    public DBConnectionFactoryImpl(String namespace) throws ConfigurationException, UnknownConnectionException {
        DBConnectionFactoryHelper.checkNullOrEmpty("namespace", namespace);

        String defaultProducer = null;

        try {
            // Get the ConfigManager instance
            ConfigManager cm = ConfigManager.getInstance();

            // Check the namespace exist or not.
            if (!cm.existsNamespace(namespace)) {
                throw new UnknownNamespaceException("The namespace " + namespace + " is not defined in the ConfigManager.");
            } else {
                cm.refresh(namespace);
            }

            // Get all nested sub-properties providing the
            // details for configured connections
            List list = cm.getPropertyObject(namespace, CONNECTIONS).list();

            // Iterate over the properties
            for (Iterator it = list.iterator(); it.hasNext();) {
                Property property = (Property) it.next();

                // "default" is a reserved property name to specify
                // the producer to be used by default
                String name = property.getName();

                if (name.equals(DEFAULT)) {
                    defaultProducer = property.getValue();
                } else {
                    // Get the name of class implementing the
                    // ConnectionProducer interface
                    String producerClassName = property.getValue(PRODUCER);

                    // Get the parameters to be used to initialize
                    // the ConnectionProducer
                    Property parameters = property.getProperty(PARAMETERS);

                    // creates ConnectionProducer through reflection
                    ConnectionProducer producer = (ConnectionProducer) createObject(producerClassName,
                        PROPERTY_CLASS_ARRAY, new Object[]{parameters });

                    // Register the ConnectionProducer under specified name
                    add(name, producer);
                }
            }
        } catch (ConfigManagerException cme) {
            throw new ConfigurationException("Error occurs in the ConfigManager- ", cme);
        } catch (InstantiationException ie) {
            throw new ConfigurationException("Error occurs when creating the producer- ", ie);
        } catch (IllegalAccessException iae) {
            throw new ConfigurationException("Error occurs when creating the producer- ", iae);
        } catch (ClassNotFoundException cnfe) {
            throw new ConfigurationException("The configured class name can not be found- ", cnfe);
        } catch (Exception ex) {
            throw new ConfigurationException("Unexpected errors- ", ex);
        }

        // If the default ConnectionProducer has been
        // specified then mark it to be used by default
        if (defaultProducer != null) {
            // fix bug in v1.0
            if (defaultProducer.trim().length() == 0) {
                throw new ConfigurationException("The value for property [" + DEFAULT
                    + "] should not be empty string.");
            }

            setDefault(defaultProducer);
        }
    }

    /**
     * <p>
     * Creates new <code>DBConnectionFactoryImpl</code> based on the configuration specified in a
     * ConfigurationObject instance from the default namespace. The ConfigurationObject instance
     * will have the format consistent with Configuration Persistence component.
     * </p>
     *
     * @param configurationObject
     *            The root configuration object.
     * @throws IllegalArgumentException
     *             If the configurationObject parameter is <code>null</code>.
     * @throws UnknownConnectionException
     *             if the producers provided don't contain the default producer specified
     * @throws ConfigurationException
     *             if any error occurs while reading the configuration and initializing the state of
     *             the factory or the configuration is malformed.
     * @since 1.1
     */
    public DBConnectionFactoryImpl(ConfigurationObject configurationObject) throws ConfigurationException,
        UnknownConnectionException {
        this(configurationObject, DEFAULT_NAMESPACE);
    }

    /**
     * <p>
     * Creates new <code>DBConnectionFactoryImpl</code> based on the configuration specified in a
     * ConfigurationObject instance from the specified namespace. The ConfigurationObject instance
     * will have the format consistent with Configuration Persistence component.
     * </p>
     *
     * @param configurationObject
     *            the root configuration object.
     * @param namespace
     *            The namespace storing the info for this factory
     * @throws IllegalArgumentException
     *             If the any parameter is <code>null</code> or the namespace is an empty string.
     * @throws UnknownConnectionException
     *             if the producers provided don't contain the default producer specified
     * @throws ConfigurationException
     *             if any error occurs while reading the configuration and initializing the state of
     *             the factory or the configuration is malformed.
     * @since 1.1
     */
    public DBConnectionFactoryImpl(ConfigurationObject configurationObject, String namespace)
        throws ConfigurationException, UnknownConnectionException {
        DBConnectionFactoryHelper.checkNull("configurationObject", configurationObject);
        DBConnectionFactoryHelper.checkNullOrEmpty("namespace", namespace);

        try {
            // there must be a ConfigurationObject for the given namespace
            ConfigurationObject childObject = configurationObject.getChild(namespace);

            if (childObject == null) {
                throw new ConfigurationException("There is no child ConfigurationObject with given name["
                    + namespace + "] in the given configurationObject");
            }

            // there must be a ConfigurationObject for CONNECTIONS
            ConfigurationObject connectionObject = childObject.getChild(CONNECTIONS);

            if (connectionObject == null) {
                throw new ConfigurationException("There is no child ConfigurationObject with given name["
                    + CONNECTIONS + "] in the [" + namespace + "] ConfigurationObject");
            }

            // all the producer will be retrieved and stored
            ConfigurationObject[] allProducers = connectionObject.getAllChildren();

            for (int i = 0; i < allProducers.length; i++) {
                Object producerValue = allProducers[i].getPropertyValue(PRODUCER);

                if (!(producerValue instanceof String)) {
                    throw new ConfigurationException("The value of property [" + PRODUCER
                        + "] should exist as String.");
                }

                // creates ConnectionProducer through reflection
                ConnectionProducer producer = (ConnectionProducer) createObject((String) producerValue,
                    CONFIGURATIONOBJECT_CLASS_ARRAY, new Object[]{allProducers[i] });

                add(allProducers[i].getName(), producer);
            }

            // the default produce will be stored if it is specified
            Object deaultValue = connectionObject.getPropertyValue(DEFAULT);

            if (deaultValue instanceof String) {
                String deaultStringValue = (String) deaultValue;

                if (deaultStringValue.trim().length() == 0) {
                    throw new ConfigurationException("The value of property[" + DEFAULT
                        + "] should be not be empty String.");
                }

                setDefault(deaultStringValue);
            } else if (deaultValue != null) {
                throw new ConfigurationException("The value of property[" + DEFAULT
                    + "] should be String if it is not null.");
            }
        } catch (ConfigurationAccessException cae) {
            throw new ConfigurationException("Error occurs while access configuration", cae);
        } catch (ClassNotFoundException cnfe) {
            throw new ConfigurationException("Error occurs while creating ConnectionProducer", cnfe);
        } catch (SecurityException se) {
            throw new ConfigurationException("Error occurs while creating ConnectionProducer", se);
        } catch (NoSuchMethodException nsme) {
            throw new ConfigurationException("Error occurs while creating ConnectionProducer", nsme);
        } catch (InstantiationException ie) {
            throw new ConfigurationException("Error occurs while creating ConnectionProducer", ie);
        } catch (IllegalAccessException iae) {
            throw new ConfigurationException("Error occurs while creating ConnectionProducer", iae);
        } catch (InvocationTargetException ite) {
            throw new ConfigurationException("Error occurs while creating ConnectionProducer", ite);
        } catch (ClassCastException cce) {
            throw new ConfigurationException("The created instance is not of ConnectionProducer", cce);
        } catch (ExceptionInInitializerError eiie) {
            throw new ConfigurationException("Error occurs while creating ConnectionProducer", eiie);
        } catch (LinkageError le) {
            throw new ConfigurationException("Error occurs while creating ConnectionProducer", le);
        }
    }

    /**
     * <p>
     * Creates an object through reflection with the given parameters.
     * </p>
     *
     * @param className
     *            the Class whose instance is created
     * @param paramClassArray
     *            the parameters' Class array used in reflection
     * @param paramArray
     *            the parameters' value used in reflection
     * @return the created object
     * @throws InvocationTargetException
     *             when error occurs during creating
     * @throws IllegalAccessException
     *             when error occurs during creating
     * @throws InstantiationException
     *             when error occurs during creating
     * @throws NoSuchMethodException
     *             when error occurs during creating
     * @throws SecurityException
     *             when error occurs during creating
     * @throws ClassNotFoundException
     *             when error occurs during creating
     * @throws ExceptionInInitializerError
     *             when error occurs during creating
     * @throws LinkageError
     *             when error occurs during creating
     * @since 1.1
     */
    private static Object createObject(String className, Class[] paramClassArray, Object[] paramArray)
        throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException,
        ClassNotFoundException {
        Class theClass = Class.forName(className);

        Constructor constructor = theClass.getConstructor(paramClassArray);

        return constructor.newInstance(paramArray);
    }

    /**
     * <p>
     * Creates the connection to a database using the <code>ConnectionProducer</code> set as
     * default. The operation may fail if a default producer is not specified.
     * </p>
     *
     * @return a <code>Connection</code> providing the connection to a database. Will not be
     *         <code>null</code>.
     * @throws DBConnectionException
     *             if any error occurs while creating a connection.
     * @throws NoDefaultConnectionException
     *             if the default connection producer is not configured within this factory.
     */
    public synchronized Connection createConnection() throws NoDefaultConnectionException, DBConnectionException {
        Object producer = connectionProducers.get(defaultProducerName);

        if (producer == null) {
            throw new NoDefaultConnectionException(
                "The default connection producer is not configured to this factory.");
        }

        return ((ConnectionProducer) producer).createConnection();
    }

    /**
     * <p>
     * Creates the connection to a database using the <code>ConnectionProducer</code> registered
     * under specified name within this factory. The operation may fail if there is no
     * <code>ConnectionProducer</code> mapped to specified name.
     * </p>
     *
     * @param name
     *            the specified name of ConnectionProducer
     * @return a <code>Connection</code> providing the connection to a database. Will not be
     *         <code>null</code>.
     * @throws DBConnectionException
     *             if any error occurs while creating a connection.
     * @throws UnknownConnectionException
     *             if there is no <code>ConnectionProducer</code> registered under specified name
     *             within this factory. <br>
     * @throws IllegalArgumentException
     *             if specified argument is <code>null</code> or an empty string.
     */
    public synchronized Connection createConnection(String name) throws UnknownConnectionException,
        DBConnectionException {
        DBConnectionFactoryHelper.checkNullOrEmpty("name", name);

        // Get ConnectionProducer mapped to specified name
        Object conn = connectionProducers.get(name);

        if (conn == null) {
            throw new UnknownConnectionException(name, "Can not retrieve the ConnectionProducer via " + name);
        }

        return ((ConnectionProducer) conn).createConnection();
    }

    /**
     * <p>
     * Creates a connection to a database authenticated with the specified username and password
     * using the default producer.
     * </p>
     *
     * @param username
     *            The username with which to connect to the database. Can be any String instance
     *            including <code>null</code> and empty string.
     * @param password
     *            The user's password. Can be any String instance including <code>null</code> and
     *            empty string.
     * @return a Connection providing the connection to a database. Will not be <code>null</code>.
     * @throws DBConnectionException
     *             if any error occurs while creating the connection.
     * @throws NoDefaultConnectionException
     *             if the default connection is not configured within this factory
     * @since 1.1
     */
    public synchronized Connection createConnection(String username, String password)
        throws NoDefaultConnectionException, DBConnectionException {
        Object producer = connectionProducers.get(defaultProducerName);

        if (producer == null) {
            throw new NoDefaultConnectionException(
                "The default connection producer is not configured within this factory.");
        }

        return ((ConnectionProducer) producer).createConnection(username, password);
    }

    /**
     * <p>
     * Creates a connection to a database authenticated with the specified username and password
     * from a producer with the specified name.
     * </p>
     *
     * @param name
     *            the name of the connection configured within this factory. Can't be
     *            <code>null</code> or empty string.
     * @param username
     *            The username with which to connect to the database. Can be any String instance
     *            including <code>null</code> and empty string.
     * @param password
     *            The user's password. Can be any String instance including <code>null</code> and
     *            empty string.
     * @return a Connection providing the connection to a database. Will not be <code>null</code>.
     * @throws DBConnectionException
     *             if any error occurs while creating the connection.
     * @throws IllegalArgumentException
     *             If the name parameter is <code>null</code> or an empty string.
     * @throws UnknownConnectionException
     *             if a connection with specified name is not configured within this factory.
     * @since 1.1
     */
    public synchronized Connection createConnection(String name, String username, String password)
        throws UnknownConnectionException, DBConnectionException {
        DBConnectionFactoryHelper.checkNullOrEmpty("name", name);

        // Get ConnectionProducer mapped to specified name
        Object conn = connectionProducers.get(name);

        if (conn == null) {
            throw new UnknownConnectionException(name, "Can not retrieve the ConnectionProducer via " + name);
        }

        return ((ConnectionProducer) conn).createConnection(username, password);
    }

    /**
     * <p>
     * Registers the specified <code>ConnectionProducer</code> under the specified name within
     * this factory. Nothing happens if specified name is already mapped to another
     * <code>ConnectionProducer</code>.
     * </p>
     *
     * @param name
     *            a <code>String</code> specifying the name to map the specified
     *            <code>ConnectionProducer</code> to.
     * @param producer
     *            a <code>ConnectionProducer</code> to be registered within this factory.
     * @return <code>true</code> if there is no other <code>ConnectionProducer</code> mapped to
     *         specified name already; <code>false</code> otherwise.
     * @throws IllegalArgumentException
     *             if any argument is <code>null</code>, or the specified 'name' is an empty
     *             string.
     */
    public synchronized boolean add(String name, ConnectionProducer producer) {
        DBConnectionFactoryHelper.checkNullOrEmpty("name", name);
        DBConnectionFactoryHelper.checkNull("producer", producer);

        if (connectionProducers.containsKey(name)) {
            return false;
        }

        connectionProducers.put(name, producer);

        return true;
    }

    /**
     * <p>
     * Removes a <code>ConnectionProducer</code> registered under specified name from this
     * factory. If specified producer is currently set to be used by default then the default
     * producer becomes non-determined.
     * </p>
     *
     * @param name
     *            a <code>String</code> specifying the name of registered
     *            <code>ConnectionProducer</code> to remove from this factory.
     * @return a <code>ConnectionProducer</code> corresponding to specified name or
     *         <code>null</code> if such a producer is not registered.
     * @throws IllegalArgumentException
     *             if specified argument is <code>null</code> or an empty string.
     */
    public synchronized ConnectionProducer remove(String name) {
        DBConnectionFactoryHelper.checkNullOrEmpty("name", name);

        if (name.equals(defaultProducerName)) {
            defaultProducerName = null;
        }

        return (ConnectionProducer) connectionProducers.remove(name);
    }

    /**
     * <p>
     * Checks if this this factory contains a <code>ConnectionProducer</code> registered under
     * specified name or not.
     * </p>
     *
     * @param name
     *            a <code>String</code> providing the name of configured
     *            <code>ConnectionProducer</code> to check for.
     * @return <code>true</code> if this factory contains a <code>ConnectionProducer</code>
     *         mapped to specified name; <code>false</code> otherwise.
     * @throws IllegalArgumentException
     *             if specified argument is <code>null</code> or an empty string.
     */
    public synchronized boolean contains(String name) {
        DBConnectionFactoryHelper.checkNullOrEmpty("name", name);

        return connectionProducers.containsKey(name);
    }

    /**
     * <p>
     * Gets the name of the <code>ConnectionProducer</code> registered within this factory which
     * is used by default.
     * </p>
     *
     * @return a <code>String</code> providing the name of default <code>ConnectionProducer</code>
     *         registered within this factory or <code>null</code> if such a default
     *         <code>ConnectionProducer</code> is not specified.
     */
    public synchronized String getDefault() {
        return defaultProducerName;
    }

    /**
     * <p>
     * Sets the name of the <code>ConnectionProducer</code> registered within this factory to be
     * used by default. There should be a <code>ConnectionProducer</code> registered under
     * specified name within this factory. The <code> null</code> argument value indicates that no
     * <code>ConnectionProducer</code> should be used by default.
     * </p>
     *
     * @param name
     *            a <code>String</code> providing the name of default
     *            <code>ConnectionProducer</code> registered within
     * @throws IllegalArgumentException
     *             if specified 'name' is an empty string
     * @throws UnknownConnectionException
     *             if specified name does not correspond to any ConnectionProducer registered to
     *             this factory.
     */
    public synchronized void setDefault(String name) throws UnknownConnectionException {
        if (name == null) {
            defaultProducerName = null;

            return;
        }

        if (name.trim().length() == 0) {
            throw new IllegalArgumentException("The name should not be empty.");
        }

        if (!connectionProducers.containsKey(name)) {
            throw new UnknownConnectionException(name, "The name [" + name
                + "] does not exist in connectionProducers.");
        }

        defaultProducerName = name;
    }

    /**
     * <p>
     * Gets the iterator over the names of <code>ConnectionProducers</code> registered within this
     * factory. The returned iterator is a fail-fast iterator.
     * </p>
     * <p>
     * Note that if the users use the iterator returned by this method to traverse the names and
     * they still want the thread safety when traversing, they must perform synchronized(this
     * factory) during the whole traverse.
     * </p>
     *
     * @return an <code>Iterator</code> over the names of <code>ConnectionProducer</code>s
     *         registered within this factory. Will never be <code>null</code>.
     */
    public synchronized Iterator listConnectionProducerNames() {
        return connectionProducers.keySet().iterator();
    }

    /**
     * <p>
     * Gets the <code>ConnectionProducer</code> registered within this factory under specified
     * name.
     * </p>
     *
     * @param name
     *            a <code>String</code> providing the name of <code>ConnectionProducer</code> to
     *            get.
     * @return a <code>ConnectionProducer</code> matching the specified name or <code>null</code>
     *         if such a producer is not registered within this factory
     * @throws IllegalArgumentException
     *             if specified argument is <code>null</code> or an empty string.
     */
    public synchronized ConnectionProducer get(String name) {
        DBConnectionFactoryHelper.checkNullOrEmpty("name", name);

        return (ConnectionProducer) connectionProducers.get(name);
    }

    /**
     * <p>
     * Removes all <code>ConnectionProducer</code>s registered within this factory. The producer
     * to be used by default becomes non-determined.
     * </p>
     */
    public synchronized void clear() {
        this.connectionProducers.clear();
        this.defaultProducerName = null;
    }
}
