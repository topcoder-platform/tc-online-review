/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.db.connectionfactory.producers;

import com.topcoder.configuration.ConfigurationAccessException;
import com.topcoder.configuration.ConfigurationObject;

import com.topcoder.db.connectionfactory.ConnectionProducer;
import com.topcoder.db.connectionfactory.DBConnectionException;
import com.topcoder.db.connectionfactory.DBConnectionFactoryHelper;

import com.topcoder.util.config.Property;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.sql.Statement;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * <p>
 * A <code>ConnectionProducer</code> obtaining the connections to a database by the means
 * specified by <code>JDBC 1.0</code> specification, namely a <code>DriverManager</code>
 * provided with a JDBC-compliant URL string and a connection properties is used to obtain a
 * connections to a database.
 * </p>
 * <p>
 * This class provides a set of convenient constructors allowing to instantiate and initialize the
 * <code>JDBCConnectionProducer</code> either programmatically or by providing the properties from
 * configuration file. From version 1.1, the new constructor with a ConfigurationObject parameter
 * supports creating the producer based on the configuration described with a ConfigurationObject
 * instance.
 * </p>
 * <p>
 * This class requires only a JDBC-compliant URL string to be provided. Such an URL is used to
 * obtain the connection to a database from a <code>DriverManager</code>. Optionally the client
 * may specify the JDBC driver specific parameters to be passed to the JDBC driver matching the
 * specified JDBC URL. Those parameters may include the username/password pair to be used to obtain
 * a connections to a database.
 * </p>
 * <p>
 * <b> Main Changes in version 1.1: </b><br>
 * (1) The extra method declared in ConnectionProducer interface to allow clients obtaining
 * connections that are authenticated with user-provided credentials rather than credentials
 * recorded in producer configuration is implemented in this class. <br>
 * (2) All the NullPointerException thrown are replaced with IllegalArgumentException.<br>
 * (3) The old constructor with Property parameter is deprecated in this version, a new constructor
 * is added to support creating the producer based on the configuration described with a
 * ConfigurationObject instance.<br>
 * </p>
 * <p>
 * <b>Sample Configuration:</b>
 * </p>
 *
 * <pre>
 *   &lt;Property name=&quot;parameters&quot;&gt;
 *           &lt;!--Optional--&gt;
 *       &lt;Property name=&quot;jdbc_driver&quot;&gt;
 *            &lt;Value&gt;com.mysql.jdbc.Driver&lt;/Value&gt;
 *       &lt;/Property&gt;
 *       &lt;!--required--&gt;
 *       &lt;Property name=&quot;jdbc_url&quot;&gt;
 *           &lt;Value&gt;jdbc:mysql://localhost:3306/tcs&lt;/Value&gt;
 *       &lt;/Property&gt;
 *           &lt;!--Optional--&gt;
 *       &lt;Property name=&quot;user&quot;&gt;
 *           &lt;Value&gt;root&lt;/Value&gt;
 *       &lt;/Property&gt;
 *           &lt;!--Optional--&gt;
 *       &lt;Property name=&quot;password&quot;&gt;
 *            &lt;Value&gt;&lt;/Value&gt;
 *       &lt;/Property&gt;
 *  &lt;/Property&gt;
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
 * The specified Configuration(Property and ConfigurationObject) should at least contain a nested
 * sub-property named as {@link #JDBC_URL_PROPERTY} providing the JDBC URL to be used to obtain a
 * connections to a database. Optionally the {@link #JDBC_DRIVER_CLASS_PROPERTY} sub-property may
 * provide the fully-qualified name of a JDBC driver class to be loaded and further used to obtain a
 * connections to a database. All other sub-properties directly nested within specified
 * Configuration(Property and ConfigurationObject) will be interpreted as an configuration
 * properties to be used to configure the JDBC driver. These properties may specify the
 * username/password pair to be used to connect to a database or any other parameters specific to a
 * JDBC driver.
 * </p>
 * <p>
 * <strong>Thread Safety:</strong> This class is thread safe. The state of the each instance of
 * this class is never changed after the instantiation during the lifetime of such an instance.
 * </p>
 *
 * @author isv, qiucx0161, nhzp339, magicpig
 * @version 1.1
 * @since 1.0
 */
public class JDBCConnectionProducer implements ConnectionProducer {
    /**
     * <p>
     * The name for the property which stores the user name. It is immutable and will be used in
     * constructors and {@link #createConnection(String, String)} method.
     * </p>
     *
     * @since 1.1
     */
    public static final String USERNAME_PROPERTY = "user";

    /**
     * <p>
     * The name for the property which stores the password. It is immutable and will be used in
     * constructors and {@link #createConnection(String, String)} method.
     * </p>
     *
     * @since 1.1
     */
    public static final String PASSWORD_PROPERTY = "password";

    /**
     * <p>
     * A <code>String</code> specifying the name of a configuration property providing the JDBC
     * URL to be used to obtain the connections to a database. This field is immutable. It will be
     * used in constructors.
     * </p>
     * <p>
     * This is a required property. If such a property is not specified by the configuration then
     * the instantiation of <code>JDBCConnectionProducer</code> will fail.
     * </p>
     */
    public static final String JDBC_URL_PROPERTY = "jdbc_url";

    /**
     * <p>
     * A <code>String</code> specifying the name of a configuration property providing the
     * fully-qualified name of a JDBC driver class to be loaded to obtain the connections to a
     * database. This field is immutable. It will be used in constructors.
     * </p>
     * <p>
     * This is an optional property. If such a property is specified by the configuration then the
     * created <code>JDBCConnectionProducer</code> will attempt to load the specified JDBC driver.
     * </p>
     */
    public static final String JDBC_DRIVER_CLASS_PROPERTY = "jdbc_driver";

    /**
     * <p>
     * Represent the parameters string.
     * </p>
     *
     * @since 1.1
     */
    private static final String PARAMETERS = "parameters";

    /**
     * <p>
     * A <code>String</code> specifying SQL to call when a new connection is created.
     * </p>
     * <p>
     * This is an optional property.
     * </p>
     *
     * @since 1.1.1
     */
    public static final String NEW_CONNECTION_SQL_PROPERTY = "new_connection_sql";

    /**
     * <p>
     * A <code>String</code> providing the JDBC-compliant URL to be provided to a
     * <code>DriverManager</code> to obtain a connection to a database.
     * </p>
     * <p>
     * This instance variable is initialized within the constructors and is never changed during the
     * lifetime of <code>JDBCConnectionProducer</code> instance. It can't be <code>null</code>
     * and can't be empty string. It will be used in <code>createConnection</code> methods.
     * </p>
     */
    private final String jdbcUrl;

    /**
     * <p>
     * A <code>Properties</code> containing the configuration parameters to be provided to a
     * <code>DriverManager</code> to obtain a connection to a database. This field may contain the
     * username/password pair to be used to obtain a connection along with any other JDBC
     * driver-specific parameters to be used to configure the JDBC driver when obtaining a
     * connection to a database.
     * </p>
     * <p>
     * This instance variable is initialized within the constructors and is never changed during the
     * lifetime of <code>JDBCConnectionProducer</code> instance.It can't be <code>null</code>
     * but can be empty. Normally the keys and values will all be non-null and non-empty Strings,
     * but there is no hard requirement about this. It will be used in <code>createConnection</code>
     * methods.
     * </p>
     */
    private final Properties connectionProperties;

    /**
     * <p>
     * A <code>String</code> containing the SQL to call when a new connection is created.
     * </p>
     * <p>
     * This instance variable is initialized within the constructors and is never changed during the
     * lifetime of <code>JDBCConnectionProducer</code> instance. It can be <code>null</code>
     * but can't be empty.
     * </p>
     *
     * @since 1.1.1
     */
    private final String newConnectionSQL;

    /**
     * <p>
     * Constructs new <code>JDBCConnectionProducer</code> with specified JDBC URL to be used to
     * obtain a connections to a database. The provided URL should follow the format specified by
     * JDBC 1.0 specification.
     * </p>
     *
     * @param jdbcUrl
     *            a <code>String</code> providing the JDBC-compliant URL to be used to obtain a
     *            connection to a database.
     * @throws IllegalArgumentException
     *             if specified argument is <code>null</code> or an empty <code>String</code>
     */
    public JDBCConnectionProducer(String jdbcUrl) {
        DBConnectionFactoryHelper.checkNullOrEmpty("jdbcUrl", jdbcUrl);
        this.jdbcUrl = jdbcUrl;
        connectionProperties = new Properties();
        newConnectionSQL = null;
    }

    /**
     * <p>
     * Constructs new <code>JDBCConnectionProducer</code> with specified JDBC URL and a
     * username/password pair to be used to obtain a connections to a database. The provided URL
     * should follow the format specified by JDBC 1.0 specification.
     * </p>
     *
     * @param jdbcUrl
     *            a <code>String</code> providing the JDBC-compliant URL to be used to obtain a
     *            connection to a database. It can't be <code>null</code> or empty string.
     * @param username
     *            a <code>String</code> providing the username to be used to obtain a connections
     *            to a database. It can't be <code>null</code> but can be empty string.
     * @param password
     *            a <code>String</code> providing the password to be used to obtain a connections
     *            to a database. It can't be <code>null</code> but can be empty string.
     * @throws IllegalArgumentException
     *             if specified <code>jdbcUrl</code>, <code>username</code> or
     *             <code>password</code> is <code>null</code>; if specified
     *             <code>jdbcUrl</code> is an empty string
     */
    public JDBCConnectionProducer(String jdbcUrl, String username, String password) {
        this(jdbcUrl);

        DBConnectionFactoryHelper.checkNull("username", username);
        DBConnectionFactoryHelper.checkNull("password", password);

        connectionProperties.put(USERNAME_PROPERTY, username);
        connectionProperties.put(PASSWORD_PROPERTY, password);
    }

    /**
     * <p>
     * Constructs new <code>JDBCConnectionProducer</code> with specified JDBC URL and connection
     * properties to be used to obtain a connections to a database. The provided URL should follow
     * the format specified by JDBC 1.0 specification. The provided <code>Properties</code> may
     * provide the JDBC driver-specific parameters to be used to configure the driver when getting
     * the connections to a database through the {@link #createConnection()} method. If a
     * username/password pair is provided then they should be mapped to a "user" and "password" keys
     * respectively.
     * </p>
     *
     * @param jdbcUrl
     *            a <code>String</code> providing the JDBC-compliant URL to be used to obtain a
     *            connection to a database. It can't be <code>null</code> or empty string.
     * @param properties
     *            a <code>Properties</code> providing the configuration parameters to be used to
     *            obtain a connections to a database. It can't be <code>null</code> but can be
     *            empty and there are no constraints on the keys/values of properties.
     * @throws IllegalArgumentException
     *             if any argument is <code>null</code> or if the specified <code>jdbcUrl</code>
     *             is an empty string
     */
    public JDBCConnectionProducer(String jdbcUrl, Properties properties) {
        DBConnectionFactoryHelper.checkNullOrEmpty("jdbcUrl", jdbcUrl);
        DBConnectionFactoryHelper.checkNull("properties", properties);
        this.jdbcUrl = jdbcUrl;
        connectionProperties = new Properties(properties);
        newConnectionSQL = null;
    }

    /**
     * <p>
     * Constructs new <code>JDBCConnectionProducer</code> which is initialized using the specified
     * configuration properties. The purpose of this constructor is to instantiate the
     * <code>JDBCConnectionProducer</code> from the parameters provided by a configuration file.
     * Currently a <code>DBConnectionFactoryImpl</code> will invoke this constructor through Java
     * Reflection API when being initialized with the parameters from configuration file.
     * </p>
     *
     * @param properties
     *            a <code>Property</code> providing the configuration properties to be used to
     *            initialize the <code>JDBCConnectionProducer</code>. It can't be
     *            <code>null</code>. The value for {@link #JDBC_URL_PROPERTY} must be non-empty
     *            string. If the value for {@link #JDBC_DRIVER_CLASS_PROPERTY} is provided, it must
     *            be a fully qualified class name.
     * @throws IllegalArgumentException
     *             if specified argument is <code>null</code>; if it does not contain a nested
     *             property named as {@link #JDBC_URL_PROPERTY} or the value of such property is
     *             empty.
     * @throws ClassNotFoundException
     *             if the properties contain driver class name, but the class name is invalid.
     * @deprecated
     */
    public JDBCConnectionProducer(Property properties) throws ClassNotFoundException {
        DBConnectionFactoryHelper.checkNull("properties", properties);

        jdbcUrl = properties.getValue(JDBC_URL_PROPERTY);

        if (jdbcUrl == null) {
            throw new IllegalArgumentException("The jdbc url is absent.");
        }

        // added in version 1.1
        if (jdbcUrl.trim().length() == 0) {
            throw new IllegalArgumentException("The jdbc url should not be empty string.");
        }

        String driverClassName = properties.getValue(JDBC_DRIVER_CLASS_PROPERTY);

        // Initialize driver class.
        if (driverClassName != null) {
            try {
                Class.forName(driverClassName);
            } catch (Exception e) {
                throw new ClassNotFoundException("The class name " + driverClassName + " is invalid.", e);
            }
        }

        // ConnectionProperties is initialized with an empty Properties
        connectionProperties = new Properties();

        // All other directly nested sub-properties are collected into connectionProperties.
        List pro = properties.list();

        for (Iterator it = pro.iterator(); it.hasNext();) {
            Property proper = (Property) it.next();
            connectionProperties.put(proper.getName(), proper.getValue());
        }

        newConnectionSQL = properties.getValue(NEW_CONNECTION_SQL_PROPERTY);
        if (newConnectionSQL != null && newConnectionSQL.trim().length() == 0) {
            throw new IllegalArgumentException("The new connection SQL should not be empty string.");
        }
    }

    /**
     * <p>
     * Constructs a new JDBCConnectionProducer instance based on the configuration specified in a
     * ConfigurationObject instance.
     * </p>
     *
     * @param configurationObject
     *            The object recording the configuration for this producer. It can't be
     *            <code>null</code>.
     * @throws ClassNotFoundException
     *             If the properties contain driver class name, but the class name is invalid.
     * @throws IllegalArgumentException
     *             If the configurationiObject parameter is <code>null</code> or the provided
     *             configuration is malformed.
     * @throws ConfigurationAccessException
     *             if any error occurs while reading the configuration.
     * @throws LinkageError
     *             if the linkage fails when loading the driver
     * @throws ExceptionInInitializerError
     *             if the initialization fails when loading the driver
     * @since 1.1
     */
    public JDBCConnectionProducer(ConfigurationObject configurationObject) throws ConfigurationAccessException,
        ClassNotFoundException {
        // 1) deals with the value of JDBC_URL_PROPERTY
        // retrieves the PARAMETERS and delegates needed checking to the helper method
        ConfigurationObject parameters = DBConnectionFactoryHelper.getChildFromConfigurationObject(
            "configurationObject", configurationObject, PARAMETERS);
        Object value = parameters.getPropertyValue(JDBC_URL_PROPERTY);

        // the value of JDBC_URL_PROPERTY must exist as non-empty string
        if (!(value instanceof String) || (((String) value).trim().length() == 0)) {
            throw new IllegalArgumentException("Property value for key[" + JDBC_URL_PROPERTY + "] in child["
                + PARAMETERS + "] must exist as a non-empty string.");
        }

        this.jdbcUrl = (String) value;

        // 2) deals with value of JDBC_DRIVER_CLASS_PROPERTY
        value = DBConnectionFactoryHelper.getNullOrStringValueFromConfigurationObject(parameters,
            JDBC_DRIVER_CLASS_PROPERTY);

        if (value != null) {
            Class.forName((String) value);
        }

        // 3) here deals with connectionProperties
        connectionProperties = new Properties();

        String[] keys = parameters.getAllPropertyKeys();

        for (int i = 0; i < keys.length; i++) {
            value = DBConnectionFactoryHelper.getNullOrStringValueFromConfigurationObject(parameters, keys[i]);

            if (value != null) {
                connectionProperties.put(keys[i], value);
            }
        }
        
        newConnectionSQL = DBConnectionFactoryHelper.getNullOrStringValueFromConfigurationObject(parameters,
                NEW_CONNECTION_SQL_PROPERTY);
        if (newConnectionSQL != null && newConnectionSQL.trim().length() == 0) {
            throw new IllegalArgumentException("The new connection SQL should not be empty string.");
        }        
    }

    /**
     * <p>
     * Creates a connection to a database obtaining a connection from a <code>DriverManager</code>
     * providing it with an JDBC URL and connection properties specified at construction time.
     * </p>
     *
     * @return a <code>Connection</code> providing the connection to a database; Will not be
     *         <code>null</code>.
     * @throws DBConnectionException
     *             if any error occurs while creating a connection.
     */
    public Connection createConnection() throws DBConnectionException {
        return createConnection(connectionProperties);
    }

    /**
     * <p>
     * Creates a connection from a DriverManager providing it with an JDBC URL and connection
     * properties authenticated with the specified username and password.
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
     * @since 1.1
     */
    public Connection createConnection(String username, String password) throws DBConnectionException {
        Properties properties = new Properties(connectionProperties);
        properties.setProperty(USERNAME_PROPERTY, username);
        properties.setProperty(PASSWORD_PROPERTY, password);

        return createConnection(properties);
    }

    /**
     * <p>
     * This is the private method that creates Connection from the given Properties and the jdbcUrl.
     * The method also executes the configured SQL script (if provided).
     * </p>
     *
     * @param properties
     *            the Properties to create Connection from
     * @return a Connection providing the connection to a database. Will not be <code>null</code>.
     * @throws DBConnectionException
     *             if any error occurs while creating the connection.
     * @since 1.1
     */
    private Connection createConnection(Properties properties) throws DBConnectionException {
        Statement statement = null;
        try {
            Connection conn = DriverManager.getConnection(jdbcUrl, properties);
            if (newConnectionSQL != null) {
                statement = conn.createStatement();
                statement.execute(newConnectionSQL);
            }
            return conn;
        } catch (SQLException sqle) {
            throw new DBConnectionException("error occurs while creating the connection.", sqle);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException sqle2) {
                    // Just swallow this one because you don't want it
                    // to replace the one that came first (thrown above).
                }
            }
        }
    }
}
