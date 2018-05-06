/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.db.connectionfactory.producers;

import com.topcoder.configuration.ConfigurationAccessException;
import com.topcoder.configuration.ConfigurationObject;

import com.topcoder.db.connectionfactory.DBConnectionFactoryHelper;

import com.topcoder.util.config.Property;

import javax.sql.DataSource;


/**
 * <p>
 * A <code>DataSourceConnectionProducer</code> instantiating the <code>DataSource</code> through
 * Java Reflection API being provided with the fully-qualified name of class implementing the
 * <code>DataSource</code> interface.
 * </p>
 *
 * <p>
 * The main purpose of this class is to provide a facility to obtain a <code>DataSource</code>
 * without requiring the JNDI context (for example, for testing purposes). This class is more
 * suitable to be instantiated and initialized with the properties provided by configuration file.
 * </p>
 *
 * <p>
 * This class provides only a set of convenient constructors allowing to instantiate and initialize
 * the <code> ReflectingConnectionProducer</code> either programmatically or by providing the
 * properties from configuration file.
 * </p>
 *
 * <p>
 * This class requires only a fully-qualified name of class implementing the
 * <code>DataSource</code> interface to be provided. Such a name is used to create the
 * <code>DataSource</code> using Java Reflection API. Optionally the client may specify the
 * username/password pair to be used to obtain a connections from a <code>DataSource</code>.
 * </p>
 *
 * <p>
 * The class expects the <code>DataSource</code> implementation classes to provide a public
 * non-argument constructor in order to be instantiated through Java Reflection API.
 * </p>
 *
 * <p>
 * <b> Main Changes in version 1.1: </b><br>
 * (1) All the NullPointerException thrown are replaced with IllegalArgumentException.<br>
 * (2) The old constructor with Property parameter is deprecated in this version, a new
 * constructor is added to support creating the producer based on the configuration described with
 * a ConfigurationObject instance.
 * </p>
 *
 * <p>
 * <b>Sample Configuration:</b>
 * </p>
 * <pre>
 *    &lt;Property name=&quot;parameters&quot;&gt;
 *        &lt;!--required--&gt;
 *        &lt;Property name=&quot;datasource_class&quot;&gt;
 *             &lt;Value&gt;customPackage.customClass&lt;/Value&gt;
 *        &lt;/Property&gt;
 *            &lt;!--Optional--&gt;
 *        &lt;Property name=&quot;user&quot;&gt;
 *            &lt;Value&gt;root&lt;/Value&gt;
 *        &lt;/Property&gt;
 *            &lt;!--Optional--&gt;
 *        &lt;Property name=&quot;password&quot;&gt;
 *             &lt;Value&gt;&lt;/Value&gt;
 *        &lt;/Property&gt;
 *   &lt;/Property&gt;
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
 * <strong>Thread Safety:</strong> This class does not maintain any private state. So its thread
 * safety fully depends on a thread safety of the {@link DataSourceConnectionProducer} class
 * (which is thread safe).
 * </p>
 *
 * @author isv, qiucx0161, nhzp339, magicpig
 * @version 1.1
 *
 * @since 1.0
 */
public class ReflectingConnectionProducer extends DataSourceConnectionProducer {
    /**
     * <p>
     * A <code>String</code> specifying the name of a configuration property providing the
     * fully-qualified name of a class implementing <code>DataSource</code> interface to be used
     * by this producer to obtain a connections to a database. This field is used in the
     * constructors. It's immutable,
     * </p>
     *
     * <p>
     * This is a required property. If such a property is not specified by the configuration then
     * the instantiation of <code>ReflectingConnectionProducer</code> will fail.
     * </p>
     */
    public static final String DATASOURCE_CLASS_PROPERTY = "datasource_class";

    /**
     * <p>
     * Constructs new <code>ReflectingConnectionProducer</code> with specified fully-qualified name
     * of class implementing the <code>DataSource</code> interface to be used to instantiate the
     * <code>DataSource</code> to be used to obtain the connections to a database. This
     * constructor may be used to instantiate the <code> ReflectingConnectionProducer</code>
     * programmatically.
     * </p>
     *
     * <p>
     * Note: the <code>ReflectingConnectionProducer</code> created through this constructor will
     * not provide any username/password pair to a <code>DataSource</code> when obtaining the
     * connections to a database.
     * </p>
     *
     * @param dataSourceClass a <code>String</code> providing a fully-qualified name of a class
     *        implementing the <code> DataSource</code> interface to be used to instantiate the
     *        <code>DataSource</code> to obtain the connections to a database.
     *
     * @throws ClassNotFoundException if specified class could not be located.
     * @throws IllegalAccessException if the specified class or a default constructor is not
     *         accessible.
     * @throws InstantiationException if the instance of specified class could not be instantiated
     *         since the class is either an interface or an abstract class.
     * @throws SecurityException if there is no permission to create the instance of specified
     *         class.
     * @throws ClassCastException if specified class does not implement <code>DataSource</code>
     *         interface.
     * @throws IllegalArgumentException if specified argument is <code>null</code> or an empty
     *         <code>String</code>
     * @throws ExceptionInInitializerError if the initialization fails when creating the dataSource
     *         class
     * @throws LinkageError if the linkage fails when loading dataSource class.
     */
    public ReflectingConnectionProducer(String dataSourceClass)
        throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        DBConnectionFactoryHelper.checkNullOrEmpty("dataSourceClass", dataSourceClass);
        createAndSetDataSource(dataSourceClass);
    }

    /**
     * <p>
     * Constructs new <code>ReflectingConnectionProducer</code> with specified fully-qualified name
     * of class implementing the <code>DataSource</code> interface to be used to instantiate the
     * <code>DataSource</code> to be used to obtain the connections to a database. This
     * constructor may be used to instantiate the <code> ReflectingConnectionProducer</code>
     * programmatically.
     * </p>
     *
     * <p>
     * Note: the <code>ReflectingConnectionProducer</code> created through this constructor will
     * provide specified username/password pair to a <code>DataSource</code> when obtaining the
     * connections to a database. The specified credentials are not validated against
     * <code>null</code> values. If both of them are <code>null</code> then no username/password
     * will be provided to a <code>DataSource</code>.
     * </p>
     *
     * @param dataSourceClass a <code>String</code> providing a fully-qualified name of a class
     *        implementing the <code> DataSource</code> interface to be used to instantiate the
     *        <code>DataSource</code> to obtain the connections to a database.
     * @param username a <code>String</code> providing the username to be used to obtain a
     *        connections to a database.
     * @param password a <code>String</code> providing the password to be used to obtain a
     *        connections to a database.
     *
     * @throws ClassNotFoundException if specified class could not be located.
     * @throws IllegalAccessException if the specified class or a default constructor is not
     *         accessible.
     * @throws InstantiationException if the instance of specified class could not be instantiated
     *         since the class is either an interface or an abstract class.
     * @throws SecurityException if there is no permission to create the instance of specified
     *         class.
     * @throws ClassCastException if specified class does not implement <code>DataSource</code>
     *         interface.
     * @throws IllegalArgumentException if any argument is null or dataSourceClass is an empty
     *         <code>String</code>.
     * @throws ExceptionInInitializerError if the initialization fails when creating the dataSource
     *         class.
     * @throws LinkageError if the linkage fails when loading dataSource class.
     */
    public ReflectingConnectionProducer(String dataSourceClass, String username, String password)
        throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        this(dataSourceClass);
        this.setPassword(password);
        this.setUsername(username);
    }

    /**
     * <p>
     * Constructs new <code>ReflectingConnectionProducer</code> which is initialized using the
     * specified configuration properties. The purpose of this constructor is to instantiate the
     * <code>ReflectingConnectionProducer</code> from the parameters provided by a configuration
     * file. Currently a <code>DBConnectionFactoryImpl</code> will invoke this constructor through
     * Java Reflection API when being initialized with the parameters from configuration file.
     * </p>
     *
     * @param properties a <code>Property</code> providing the configuration properties to be used
     *        to initialize the <code>ReflectingConnectionProducer</code>.
     *
     * @throws ClassNotFoundException if specified class could not be located.
     * @throws IllegalAccessException if the specified class or a default constructor is not
     *         accessible.
     * @throws InstantiationException if the instance of specified class could not be instantiated
     *         since the class is either an interface or an abstract class.
     * @throws SecurityException if there is no permission to create the instance of specified
     *         class.
     * @throws ClassCastException if specified class does not implement DataSource interface.
     * @throws IllegalArgumentException if specified argument is <code>null</code> or specified
     *         <code>Property</code> does not contain a nested property named as {@link
     *         #DATASOURCE_CLASS_PROPERTY}.
     * @throws ExceptionInInitializerError if the initialization fails when creating the dataSource
     *         class.
     * @throws LinkageError if the linkage fails when loading dataSource class.
     *
     * @deprecated
     */
    public ReflectingConnectionProducer(Property properties)
        throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        // Check parameter is null or not and assign the username
        // and password if it is neccessary
        super(properties);

        // Get the datasource class name which will be used to create
        // datasource via reflection.
        String dataSourceClass = properties.getValue(DATASOURCE_CLASS_PROPERTY);

        if (dataSourceClass == null) {
            throw new IllegalArgumentException(
                "Parameter properties do not contain datasource class.");
        }

        createAndSetDataSource(dataSourceClass);
    }

    /**
     * <p>
     * Constructs a new ReflectingConnectionProducer instance based on the configuration specified
     * in a ConfigurationObject instance.
     * </p>
     *
     * @param configurationObject The object recording the configuration for this producer.
     *
     * @throws ClassNotFoundException if specified class could not be located.
     * @throws InstantiationException if the instance of specified class could not be instantiated
     *         since the class is either an interface or an abstract class.
     * @throws IllegalArgumentException If the configurationiObject param is null or the provided
     *         configuration is malformed.
     * @throws IllegalAccessException if the specified class or a default constructor is not
     *         accessible.
     * @throws SecurityException if there is no permission to create the instance of specified
     *         class.
     * @throws ConfigurationAccessException if any error occurs while reading the configuration.
     * @throws ClassCastException if specified class does not implement DataSource interface.
     * @throws ExceptionInInitializerError if the initialization fails when creating the dataSource
     *         class.
     * @throws LinkageError if the linkage fails when loading dataSource class.
     *
     * @since 1.1
     */
    public ReflectingConnectionProducer(ConfigurationObject configurationObject)
        throws ConfigurationAccessException, InstantiationException, IllegalAccessException,
            ClassNotFoundException {
        super(configurationObject);

        // the checking of null parameters is done in super class
        Object dataSourceClass = configurationObject.getChild(PARAMETERS).getPropertyValue(DATASOURCE_CLASS_PROPERTY);

        if (!(dataSourceClass instanceof String)) {
            throw new IllegalArgumentException("The property[" + DATASOURCE_CLASS_PROPERTY +
                "] should exist and contain an string value.");
        }

        createAndSetDataSource((String) dataSourceClass);
    }

    /**
     * <p>
     * Creates DataSource from the given dataSourceClass and sets it.
     * </p>
     *
     * @param dataSourceClass the class name of the DataSource to be created
     *
     * @throws ClassNotFoundException if specified class could not be located.
     * @throws IllegalAccessException if the specified class or a default constructor is not
     *         accessible.
     * @throws InstantiationException if the instance of specified class could not be instantiated
     *         since the class is either an interface or an abstract class.
     * @throws SecurityException if there is no permission to create the instance of specified
     *         class.
     * @throws ClassCastException if specified class does not implement DataSource interface.
     * @throws ExceptionInInitializerError if the initialization fails when creating the dataSource
     *         class.
     * @throws LinkageError if the linkage fails when loading dataSource class.
     *
     * @since 1.1
     */
    private void createAndSetDataSource(String dataSourceClass)
        throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        setDataSource((DataSource) Class.forName(dataSourceClass).newInstance());
    }
}
