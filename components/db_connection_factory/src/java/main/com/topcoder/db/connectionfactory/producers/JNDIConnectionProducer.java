/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.db.connectionfactory.producers;

import com.topcoder.configuration.ConfigurationAccessException;
import com.topcoder.configuration.ConfigurationObject;

import com.topcoder.db.connectionfactory.DBConnectionFactoryHelper;

import com.topcoder.util.config.Property;

import java.util.Hashtable;
import java.util.Iterator;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import javax.sql.DataSource;

/**
 * <p>
 * A <code>DataSourceConnectionProducer</code> obtaining the <code>DataSource</code> from a JNDI
 * context using the provided JNDI name.
 * </p>
 * <p>
 * This class provides only a set of convenient constructors allowing to instantiate and initialize
 * the <code>JNDIConnectionProducer</code> either programmatically or by providing the properties
 * from configuration file.
 * </p>
 * <p>
 * This class requires only a JNDI name to be provided. Such a name is used to locate the
 * <code>DataSource</code> within the JNDI context. Optionally the client may specify the
 * environment properties to be used to initialize the JNDI context along with the username/password
 * pair to be used to obtain a connections from a <code>DataSource</code>.
 * </p>
 * <p>
 * <b> Main Changes in version 1.1: </b><br>
 * (1) All the NullPointerException thrown are replaced with IllegalArgumentException. <br>
 * (2) The old constructor with Property parameter is deprecated in this version, a new constructor
 * is added to support creating the producer based on the configuration described with a
 * ConfigurationObject instance.
 * </p>
 * <p>
 * <b>Sample Configuration:</b>
 * </p>
 * <pre>
 *   &lt;Property name=&quot;parameters&quot;&gt;
 *       &lt;!--required--&gt;
 *       &lt;Property name=&quot;jndi_name&quot;&gt;
 *           &lt;Value&gt;java:comp/env/jdbc/tcs&lt;/Value&gt;
 *       &lt;/Property&gt;
 *       &lt;!--Optional--&gt;
 *       &lt;Property name=&quot;user&quot;&gt;
 *           &lt;Value&gt;root&lt;/Value&gt;
 *       &lt;/Property&gt;
 *       &lt;!--Optional--&gt;
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
 * The constructors which read Configuration will also read the environment(String-String pair) from
 * Configuration. The environment will be used to create the initial context. Here be careful that
 * the "_" in property name will be replaced by "." while storing in environment.
 * </p>
 * <p>
 * <strong>Thread Safety:</strong> This class does not maintain any private state. So its thread
 * safety fully depends on a thread safety of the {@link DataSourceConnectionProducer} class (which
 * is thread safe).
 * </p>
 *
 * @author isv, qiucx0161, nhzp339, magicpig
 * @version 1.1
 * @since 1.0
 */
public class JNDIConnectionProducer extends DataSourceConnectionProducer {
    /**
     * <p>
     * A <code>String</code> specifying the name of a configuration property providing the JNDI
     * name to be used to locate the <code>DataSource</code> to be used to obtain the connections
     * to a database. This field is used in the constructors. It's immutable,
     * </p>
     * <p>
     * This is a required property. If such a property is not specified by the configuration then
     * the instantiation of <code>JNDIConnectionProducer</code> will fail.
     * </p>
     */
    public static final String JNDI_NAME_PROPERTY = "jndi_name";

    /**
     * Represents character '_'.
     *
     * @since 1.1
     */
    private static final char UNDERLINE = '_';

    /**
     * Represents character '.'.
     *
     * @since 1.1
     */
    private static final char DOT = '.';

    /**
     * <p>
     * Constructs new <code>JNDIConnectionProducer</code> with specified JNDI name to be used to
     * locate the requested <code>DataSource</code> within initial JNDI context. This constructor
     * may be used to instantiate the <code> JNDIConnectionProducer</code> programmatically.
     * </p>
     * <p>
     * Note: the <code>JNDIConnectionProducer</code> created through this constructor will not
     * provide any username/password pair to a <code>DataSource</code> when obtaining the
     * connections to a database.
     * </p>
     *
     * @param jndiName
     *            a <code>String</code> providing the JNDI name to be used to locate the requested
     *            <code>DataSource </code> within JNDI context.
     * @throws IllegalArgumentException
     *             if specified argument is <code>null</code> or an empty string.
     * @throws NamingException
     *             is thrown if a naming error occurs while getting the initial JNDI context and
     *             performing a lookup for requested datasource.
     * @throws ClassCastException
     *             if the jndiName is not associated with DataSource Object.
     */
    public JNDIConnectionProducer(String jndiName) throws NamingException {
        DBConnectionFactoryHelper.checkNullOrEmpty("jndiName", jndiName);
        setDataSource((DataSource) new InitialContext().lookup(jndiName));
    }

    /**
     * <p>
     * Constructs new <code>JNDIConnectionProducer</code> with specified JNDI name to be used to
     * locate the requested <code>DataSource</code> within initial JNDI context and
     * username/password pair to be used to obtain a connections to a database. This constructor may
     * be used to instantiate the <code>JNDIConnectionProducer</code> programmatically.
     * </p>
     *
     * @param jndiName
     *            a <code>String</code> providing the JNDI name to be used to locate the requested
     *            <code>DataSource </code> within JNDI context.
     * @param username
     *            a <code>String</code> providing the username to be used to obtain a connections
     *            to a database.
     * @param password
     *            a <code>String</code> providing the password to be used to obtain a connections
     *            to a database.
     * @throws IllegalArgumentException
     *             if any parameter is <code>null</code> or if the jndiName is empty string
     * @throws NamingException
     *             if a naming error occurs while getting the initial JNDI context and performing a
     *             lookup for requested datasource.
     * @throws ClassCastException
     *             if the jndiName is not associated with DataSource Object.
     */
    public JNDIConnectionProducer(String jndiName, String username, String password) throws NamingException {
        this(jndiName);
        this.setUsername(username);
        this.setPassword(password);
    }

    /**
     * <p>
     * Constructs new <code>JNDIConnectionProducer</code> with specified JNDI name to be used to
     * locate the requested <code>DataSource</code> within initial JNDI context and specified
     * environment to be used to initialize the initial JNDI context. This constructor may be used
     * to instantiate the <code>JNDIConnectionProducer</code> programmatically.
     * </p>
     * <p>
     * Note: the <code>JNDIConnectionProducer</code> created through this constructor will not
     * provide any username/password pair to a <code>DataSource</code> when obtaining the
     * connections to a database.
     * </p>
     *
     * @param jndiName
     *            a <code>String</code> providing the JNDI name to be used to locate the requested
     *            <code>DataSource </code> within JNDI context.
     * @param environment
     *            a <code>Hashtable</code> providing the environment to be used to create the
     *            initial context. <code>Null</code> indicates an empty environment.
     * @throws IllegalArgumentException
     *             if specified <code>jndiName</code> is <code>null</code> or an empty string
     * @throws NamingException
     *             if a naming error occurs while getting the initial JNDI context and performing a
     *             lookup for requested datasource.
     * @throws ClassCastException
     *             if the jndiName is not associated with DataSource Object.
     */
    public JNDIConnectionProducer(String jndiName, Hashtable environment) throws NamingException {
        DBConnectionFactoryHelper.checkNullOrEmpty("jndiName", jndiName);
        setDataSource((DataSource) new InitialContext(environment).lookup(jndiName));
    }

    /**
     * <p>
     * Constructs new <code>JNDIConnectionProducer</code> with specified JNDI name to be used to
     * locate the requested <code>DataSource</code> within initial JNDI context and specified
     * environment to be used to initialize the initial JNDI context. The specified
     * username/password pair will be used to obtain a connections to a database. This constructor
     * may be used to instantiate the <code>JNDIConnectionProducer</code> programmatically.
     * </p>
     *
     * @param jndiName
     *            a <code>String</code> providing the JNDI name to be used to locate the requested
     *            <code>DataSource </code> within JNDI context.
     * @param environment
     *            a <code>Hashtable</code> providing the environment to be used to create the
     *            initial context. <code>Null</code> indicates an empty environment.
     * @param username
     *            a <code>String</code> providing the username to be used to obtain a connections
     *            to a database.
     * @param password
     *            a <code>String</code> providing the password to be used to obtain a connections
     *            to a database.
     * @throws IllegalArgumentException
     *             if specified <code>jndiName</code> or <code>username</code> or
     *             <code>password</code> is <code>null</code> or if the jndiName is empty.
     * @throws NamingException
     *             if a naming error occurs while getting the initial JNDI context and performing a
     *             lookup for requested datasource.
     * @throws ClassCastException
     *             if the jndiName is not associated with DataSource Object.
     */
    public JNDIConnectionProducer(String jndiName, Hashtable environment, String username, String password)
        throws NamingException {
        this(jndiName, environment);
        this.setUsername(username);
        this.setPassword(password);
    }

    /**
     * <p>
     * Constructs new <code>JNDIConnectionProducer</code> which is initialized using the specified
     * configuration properties. The purpose of this constructor is to instantiate the
     * <code>JNDIConnectionProducer</code> from the parameters provided by a configuration file.
     * Currently a <code>DBConnectionFactoryImpl</code> will invoke this constructor through Java
     * Reflection API when being initialized with the parameters from configuration file.
     * </p>
     *
     * @param properties
     *            a <code>Property</code> providing the configuration properties to be used to
     *            initialize the <code>JNDIConnectionProducer</code>.
     * @throws IllegalArgumentException
     *             if specified argument is <code>null</code> or it does not contain a nested
     *             property named as {@link #JNDI_NAME_PROPERTY}.
     * @throws NamingException
     *             if a naming error occurs while getting the initial JNDI context and peforming a
     *             lookup for requested datasource.
     * @throws ClassCastException
     *             if the jndiName retrieved from properties is not associated with DataSource
     *             Object.
     * @deprecated
     */
    public JNDIConnectionProducer(Property properties) throws NamingException {
        // Check parameter is null or not and assign the username
        // and password if it is necessary
        super(properties);

        // Parameter properties should contain jndi name.
        String jndiname = properties.getValue(JNDI_NAME_PROPERTY);

        if (jndiname == null) {
            throw new IllegalArgumentException("Parameter properties do not contain jndi name.");
        }

        // Get all the sub-properties of properties and put them
        // into Hashtable instance which will be used by InitialContext
        Hashtable env = new Hashtable();

        for (Iterator it = properties.list().iterator(); it.hasNext();) {
            Property prope = (Property) it.next();
            String name = prope.getName();

            // Replace the '_' to '.', if '_' exist.
            env.put(name.replace(UNDERLINE, DOT), prope.getValue());
        }

        // Get the datasource via the gotten jndiname
        // and save the datasource for future use
        setDataSource((DataSource) new InitialContext(env).lookup(jndiname));
    }

    /**
     * <p>
     * Constructs a new JNDIConnectionProducer instance based on the configuration specified in a
     * ConfigurationObject instance.
     * </p>
     *
     * @param configurationObject
     *            The object recording the configuration for this producer.
     * @throws NamingException
     *             if a naming error occurs while getting the initial JNDI context and performing a
     *             lookup for requested datasource.
     * @throws IllegalArgumentException
     *             If the configurationiObject param is null or the provided configuration is
     *             malformed.
     * @throws ConfigurationAccessException
     *             if any error occurs while reading the configuration.
     * @throws ClassCastException
     *             if the jndiName retrieved from configurationObject is not associated with
     *             DataSource Object.
     * @since 1.1
     */
    public JNDIConnectionProducer(ConfigurationObject configurationObject) throws ConfigurationAccessException,
        NamingException {
        super(configurationObject);

        // the checking for null parameters is done in super class
        ConfigurationObject parameters = configurationObject.getChild(PARAMETERS);
        Object jndiName = parameters.getPropertyValue(JNDI_NAME_PROPERTY);

        if (!(jndiName instanceof String)) {
            throw new IllegalArgumentException("The property[" + JNDI_NAME_PROPERTY
                + "] should exist and contain a string value.");
        }

        // a new Hashtable to hold the environment
        Hashtable env = new Hashtable();
        String[] keys = parameters.getAllPropertyKeys();

        for (int i = 0; i < keys.length; i++) {
            String value = DBConnectionFactoryHelper.getNullOrStringValueFromConfigurationObject(parameters,
                keys[i]);

            if (value != null) {
                env.put(keys[i].replace(UNDERLINE, DOT), value);
            }
        }

        setDataSource((DataSource) new InitialContext(env).lookup((String) jndiName));
    }
}
