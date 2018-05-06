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
import java.sql.SQLException;

import javax.sql.DataSource;

/**
 * <p>
 * An implementation of <code>ConnectionProducer</code> obtaining the connections from a
 * <code>DataSource</code>.
 * </p>
 * <p>
 * In order to operate properly this class requires the <code>DataSource</code> to be provided.
 * Optionally a username/password pair may be provided to specify a user on whose behalf the
 * connection is to be made.
 * </p>
 * <p>
 * This is a base class for the connection producers obtaining the connections to a database from a
 * <code>DataSource</code>, optionally providing the username and password. The subclasses are
 * responsible only for proper initialization of a <code>DataSource</code>, username and
 * password.
 * </p>
 * <p>
 * This class provides a set of constructors allowing to instantiate and initialize the instance
 * either programmatically or through the configuration properties. The public constructors are
 * intended to be called both by a clients and a subclasses while the protected constructors are
 * intended to be called by subclasses only.
 * </p>
 * <p>
 * <b> Main Changes in version 1.1: </b><br>
 * (1) The extra method declared in ConnectionProducer interface to allow clients obtaining
 * connections that are authenticated with user-provided credentials rather than credentials
 * recorded in producer configuration is implemented in this class. <br>
 * (2) All the NullPointerException thrown are replaced with IllegalArgumentException. <br>
 * (3) The old constructor with Property parameter is deprecated in this version, a new protected
 * constructor is added to support creating the producer based on the configuration described with a
 * ConfigurationObject instance.
 * </p>
 * <p>
 * <b>Sample Configuration:</b>
 * </p>
 *
 * <pre>
 *   &lt;Property name=&quot;parameters&quot;&gt;
 *      &lt;!--Optional--&gt;
 *      &lt;Property name=&quot;user&quot;&gt;
 *            &lt;Value&gt;root&lt;/Value&gt;
 *    &lt;/Property&gt;
 *    &lt;!--Optional--&gt;
 *    &lt;Property name=&quot;password&quot;&gt;
 *            &lt;Value&gt;123456&lt;/Value&gt;
 *     &lt;/Property&gt;
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
 * <strong>Thread Safety:</strong> This class is thread safe. In most cases the state of the
 * <code>DataSourceConnectionProducer</code> instance is not changed during its lifetime. The
 * mutator methods are protected and must be called only by a subclasses. To maintain the thread
 * safety these mutator methods are declared with 'synchronized' access. The thread safety of
 * <code>createConnection()</code> method fully depends on a thread safety of an underlying <code>
 * DataSource</code>.
 * </p>
 *
 * @author isv, qiucx0161, nhzp339, magicpig
 * @version 1.1
 * @since 1.0
 */
public class DataSourceConnectionProducer implements ConnectionProducer {
    /**
     * <p>
     * A <code>String</code> specifying the name of a configuration property providing the
     * username to be provided to a <code>DataSource</code> to obtain a connection to a database.
     * This field is immutable. It will be used in constructors.
     * </p>
     * <p>
     * This is an optional property. If such a property is not specified by the constructor,
     * configuration or by a subclass then the connections are obtained without providing any
     * username to a <code>DataSource</code>.
     * </p>
     */
    public static final String USERNAME_PROPERTY = "username";

    /**
     * <p>
     * A <code>String</code> specifying the name of a configuration property providing the
     * password to be provided to a <code>DataSource</code> to obtain a connection to a database.
     * This field is immutable. It will be used in constructors.
     * </p>
     * <p>
     * This is an optional property. If such a property is not specified by the constructor,
     * configuration or by a subclass then the connections are obtained without providing any
     * password to a <code>DataSource</code>.
     * </p>
     */
    public static final String PASSWORD_PROPERTY = "password";

    /**
     * <p>
     * Represent the parameters string.
     * </p>
     *
     * @since 1.1
     */
    protected static final String PARAMETERS = "parameters";

    /**
     * <p>
     * A <code>DataSource</code> to be used by this <code>DataSourceConnectionProducer</code> to
     * obtain a connections to a database.
     * </p>
     * <p>
     * The value of this instance field should never be <code>null</code> when the two
     * <code>createConnection</code> methods are called.
     * </p>
     * <p>
     * This field must be initialized either by the public constructors provided by this class or
     * set by the subclasses through <code>setDataSource(DataSource)</code> method before a first
     * call to <code>createConnection </code> method is serviced. Normally it's never changed after
     * set at the first time, but it's not a hard requirement.
     * </p>
     * <p>
     * The clients may access this <code>DataSource</code> through <code>getDataSource()</code>
     * method.
     * </p>
     */
    private DataSource dataSource = null;

    /**
     * <p>
     * A <code>String</code> providing the username to be used by this
     * <code>DataSourceConnectionProducer</code> to obtain a connection to a database.
     * </p>
     * <p>
     * The value of this instance field may be <code>null</code> if no username is required to
     * obtain a connection to a database. It can be empty string. It will be used in
     * <code>createConnection</code> methods.
     * </p>
     * <p>
     * This field may be set either by the public constructors provided by this class or set by a
     * subclasses through <code>setUsername(String)</code> method. The field may also be
     * initialized by the constructors accepting a <code>Property</code> instance and accepting
     * <code>ConfigurationObject</code> if this property or configurationObject provides a nested
     * property named as {@link #USERNAME_PROPERTY}. Normally it's never changed after set at the
     * first time, but it's not a hard requirement.
     * </p>
     */
    private String username = null;

    /**
     * <p>
     * A <code>String</code> providing the password to be used by this
     * <code>DataSourceConnectionProducer</code> to obtain a connection to a database.
     * </p>
     * <p>
     * The value of this instance field may be <code>null</code> if no password is required to
     * obtain a connection to a database. It can be empty string. It will be used in
     * <code>createConnection</code> method.
     * </p>
     * <p>
     * This field may be set either by the public constructors provided by this class or set by a
     * subclasses through <code>setPassword(String)</code> methodThe field may also be initialized
     * by the constructors accepting a <code> Property</code> instance and accepting
     * <code>ConfigurationObject</code> if this property or configurationObject provides a nested
     * property named as {@link #PASSWORD_PROPERTY}. Normally it's never changed after set at the
     * first time, but it's not a hard requirement.
     * </p>
     */
    private String password = null;

    /**
     * <p>
     * Constructs new <code>DataSourceConnectionProducer</code>. The state of this instance is
     * not initialized. This constructor is expected to be called by a subclasses followed by a
     * calls to mutator methods to set the <code>DataSource</code>, username and password to be
     * used to obtain a connections to a database.
     * </p>
     */
    protected DataSourceConnectionProducer() {
        // empty
    }

    /**
     * <p>
     * Constructs new <code>DataSourceConnectionProducer</code> initialized with configuration
     * properties provided by specified <code>Property</code> instance. This constructor is
     * expected to be called from the constructors of subclasses when these producers are
     * instantiated via Java Reflection API by <code>DBConnectionFactoryImpl</code>.
     * </p>
     *
     * @param properties
     *            a <code>Property</code> providing the configuration properties to be used to
     *            initialize this <code>DataSourceConnectionProducer</code>.
     * @throws IllegalArgumentException
     *             if specified argument is <code>null</code>.
     * @deprecated
     */
    protected DataSourceConnectionProducer(Property properties) {
        DBConnectionFactoryHelper.checkNull("properties", properties);
        username = properties.getValue(USERNAME_PROPERTY);
        password = properties.getValue(PASSWORD_PROPERTY);
    }

    /**
     * <p>
     * Constructs a new DataSourceConnectionProducer instance based on the configuration specified
     * in a ConfigurationObject instance. This constructor is expected to be called from the
     * constructors of subclasses.
     * </p>
     *
     * @param configurationObject
     *            The object recording the configuration for this producer.
     * @throws IllegalArgumentException
     *             If the configurationiObject parameter is <code>null</code> or the provided
     *             configuration is malformed(the value for USERNAME_PROPERTY and PASSWORD_PROPERTY
     *             should not be non-null non-String object)
     * @throws ConfigurationAccessException
     *             if any error occurs while reading the configuration.
     * @since 1.1
     */
    protected DataSourceConnectionProducer(ConfigurationObject configurationObject)
        throws ConfigurationAccessException {
        // retrieves the PARAMETERS and delegates needed checking to the helper method
        ConfigurationObject parameters = DBConnectionFactoryHelper.getChildFromConfigurationObject(
            "configurationObject", configurationObject, PARAMETERS);

        // gets the two values from PARAMETERS, all the checking is done in method
        // getNullOrStringValueFromConfigurationObject
        username = DBConnectionFactoryHelper.getNullOrStringValueFromConfigurationObject(parameters,
            USERNAME_PROPERTY);
        password = DBConnectionFactoryHelper.getNullOrStringValueFromConfigurationObject(parameters,
            PASSWORD_PROPERTY);
    }

    /**
     * <p>
     * Constructs new <code>DataSourceConnectionProducer</code> with specified
     * <code>DataSource</code> to be used to obtain a connections to a database.
     * </p>
     * <p>
     * This constructor may be used either by a subclasses or by a clients wishing to create a
     * producer programmatically providing the <code>DataSource</code>.
     * </p>
     *
     * @param dataSource
     *            a <code>DataSource</code> to be used to obtain a connections to a database.
     * @throws IllegalArgumentException
     *             if specified argument is <code>null</code>.
     */
    public DataSourceConnectionProducer(DataSource dataSource) {

        // Set the given dataSource to the dataSource field
        // after checking it is not null.
        setDataSource(dataSource);
    }

    /**
     * <p>
     * Constructs new <code>DataSourceConnectionProducer</code> with specified
     * <code>DataSource</code>, username and password to be used to obtain a connections to a
     * database. Of specified arguments only a <code>dataSource</code> is required. The
     * <code>username</code> and <code>password</code> may be <code>null</code>. This is a
     * solely responsibility of a caller to provide the username/password pair properly.
     * </p>
     * <p>
     * This constructor may be used either by a subclasses or by a clients wishing to create a
     * producer programmatically at runtime.
     * </p>
     *
     * @param dataSource
     *            a <code>DataSource</code> to be used to obtain a connections to a database.
     * @param username
     *            a <code>String</code> providing the username to be used to obtain a connection
     *            to a database. Can be <code>null</code> or empty <code>String</code>.
     * @param password
     *            a <code>String</code> providing the password to be used to obtain a connection
     *            to a database. Can be <code>null</code> or empty <code>String</code>.
     * @throws IllegalArgumentException
     *             if <code>dataSource</code> is <code>null</code>.
     */
    public DataSourceConnectionProducer(DataSource dataSource, String username, String password) {
        this(dataSource);
        this.username = username;
        this.password = password;
    }

    /**
     * <p>
     * Creates a connection to a database obtaining a connection from an underlying
     * <code>DataSource</code>. If any of username or password has been specified before then
     * they are provided to a <code>DataSource</code>.
     * </p>
     *
     * @return a <code>Connection</code> providing the connection to a database. Will never be
     *         <code>null</code>.
     * @throws DBConnectionException
     *             if any error occurs while creating a connection.
     */
    public synchronized Connection createConnection() throws DBConnectionException {
        return createConnection(username, password);
    }

    /**
     * <p>
     * Creates a connection from the underlying DataSource authenticated with the specified username
     * and password.
     * </p>
     *
     * @param username
     *            The username with which to connect to the database. Can be any String instance
     *            including <code>null</code> and empty string.
     * @param password
     *            The user's password. Can be any String instance including <code>null</code> and
     *            empty string.
     * @return a Connection providing the connection to a database. Will not be <code>null</code>..
     * @throws DBConnectionException
     *             if any error occurs while creating the connection.
     * @since 1.1
     */
    public synchronized Connection createConnection(String username, String password) throws DBConnectionException {
        // Check the dataSource is initialized or not.
        if (dataSource == null) {
            throw new DBConnectionException("The dataSource hadn't been configured yet",
                new IllegalStateException("The status is invalid for calling createConnection"));
        }

        try {
            return ((username == null) && (password == null)) ? dataSource.getConnection() : dataSource
                .getConnection(username, password);
        } catch (SQLException sqle) {
            throw new DBConnectionException("Error occurs while creating connection", sqle);
        }
    }

    /**
     * <p>
     * Gets the <code>DataSource</code> which is used by this
     * <code>DataSourceConnectionProducer</code> to obtain a connections to a database.
     * </p>
     *
     * @return a <code>DataSource</code> which is used to obtain a connections to a database or
     *         <code>null</code> if such a <code>DataSource</code> hadn't been configured yet.
     */
    public synchronized DataSource getDataSource() {
        return dataSource;
    }

    /**
     * <p>
     * Sets the <code>DataSource</code> to be used by this
     * <code>DataSourceConnectionProducer</code> to obtain a connections to a database.
     * </p>
     * <p>
     * Note: this method is intended to be called by subclasses only. Most likely the subclasses
     * will call this method from their constructors after performing a steps to obtain a
     * <code>DataSource</code>. While the subclasses are not recommended to call this method
     * after instantiation they are not prohibited to do so.
     * </p>
     *
     * @param dataSource
     *            a <code>DataSource</code> to be used to obtain a connections to a database.
     * @throws IllegalArgumentException
     *             if specified argument is <code>null</code>.
     */
    protected synchronized void setDataSource(DataSource dataSource) {
        DBConnectionFactoryHelper.checkNull("dataSource", dataSource);
        this.dataSource = dataSource;
    }

    /**
     * <p>
     * Sets the username to be used by this <code>DataSourceConnectionProducer</code> to obtain a
     * connections to a database.
     * </p>
     * <p>
     * Note: This method is intended to be called by subclasses only. While the subclasses are not
     * recommended to call this method after instantiation they are not prohibited to do so.
     * </p>
     *
     * @param username
     *            a <code>String</code> providing the username to be used to obtain a connections
     *            to a database.
     * @throws IllegalArgumentException
     *             if specified <code>username</code> is <code>null</code>.
     */
    protected synchronized void setUsername(String username) {
        DBConnectionFactoryHelper.checkNull("username", username);
        this.username = username;
    }

    /**
     * <p>
     * Sets the password to be used by this <code>DataSourceConnectionProducer</code> to obtain a
     * connections to a database.
     * </p>
     * <p>
     * Note: This method is intended to be called by subclasses only. While the subclasses are not
     * recommended to call this method after instantiation they are not prohibited to do so.
     * </p>
     *
     * @param password
     *            a <code>String</code> providing the password to be used to obtain a connections
     *            to a database.
     * @throws IllegalArgumentException
     *             if specified <code>username</code> is <code>null</code>.
     */
    protected synchronized void setPassword(String password) {
        DBConnectionFactoryHelper.checkNull("password", password);
        this.password = password;
    }
}
