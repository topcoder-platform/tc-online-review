/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.external.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.cronos.onlinereview.external.ConfigException;
import com.cronos.onlinereview.external.ExternalObject;
import com.cronos.onlinereview.external.RetrievalException;
import com.cronos.onlinereview.external.UserProjectDataStoreHelper;
import com.topcoder.db.connectionfactory.ConfigurationException;
import com.topcoder.db.connectionfactory.DBConnectionException;
import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.topcoder.db.connectionfactory.UnknownConnectionException;
import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.config.UnknownNamespaceException;

/**
 * <p>
 * Abstract base class for implementing the database (JDBC) versions of the "retrieval" interfaces of the <b>User
 * Project Data Store</b> component.
 * </p>
 * <p>
 * Utility methods are provided to get and close a connection, and to bulk create objects from a result set (via a
 * prepared statement.)
 * </p>
 * <p>
 * <b>Thread Safety</b>: This class is immutable and therefore thread-safe.
 * </p>
 *
 * @author dplass, oodinary
 * @author FireIce
 * @version 1.1
 * @since 1.0
 */
public abstract class BaseDBRetrieval {

    /**
     * <p>
     * The name of the namespace that the calling program can populate which contains DBConnectionFactory and other
     * configuration values.
     * </p>
     */
    public static final String NAMESPACE = "com.cronos.onlinereview.external";

    /**
     * <p>
     * The name of the connection name in the config file.
     * </p>
     */
    private static final String CONNNAME_STRING = "connName";

    /**
     * <p>
     * The database connection factory to use in getConnection.
     * </p>
     * <p>
     * Will be configured in one of the constructors and used in getConnection only.
     * </p>
     * <p>
     * Will never be null.
     * </p>
     */
    private final DBConnectionFactory connFactory;

    /**
     * <p>
     * The name of the connection to use from the connection factory.
     * </p>
     * <p>
     * Sets in one of the constructors and used in getConnection only.
     * </p>
     * <p>
     * May be null, in which case the default connection should be used.
     * </p>
     */
    private final String connName;

    /**
     * <p>
     * Constructs this object with the given parameters.
     * </p>
     *
     * @param connFactory
     *            the connection factory to use with this object.
     * @param connName
     *            the connection name to use when creating connections.
     * @throws IllegalArgumentException
     *             if either parameter is <code>null</code> or connName is empty after trimmed.
     * @throws ConfigException
     *             if the connection name doesn't correspond to a connection the factory knows about.
     */
    protected BaseDBRetrieval(DBConnectionFactory connFactory, String connName) throws ConfigException {
        UserProjectDataStoreHelper.validateNull(connFactory, "connFactory");
        UserProjectDataStoreHelper.validateStringEmptyNull(connName, "connName");

        // Tests whether the connection name is correct to the connection factory.
        Connection conn = null;
        try {
            conn = connFactory.createConnection(connName);
        } catch (DBConnectionException e) {
            throw new ConfigException("The connection name doesn't correspond to a connection the "
                    + "factory knows about", e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    // Ignore here.
                }
            }
        }

        // Sets the fields.
        this.connFactory = connFactory;
        this.connName = connName;
    }

    /**
     * <p>
     * Constructs this object with the given namespace.
     * </p>
     * <p>
     * The namespace must define a DB connection factory using its standard configuration. Also, if the 'connName'
     * property is defined, this is the connection name to use in the getConnection method. Otherwise the connection
     * name will be left null and the default connection name will be used in <code>getConnection()</code>.
     * </p>
     *
     * @param namespace
     *            the ConfigManager namespace to retrieve the DBConnectionFactory and optional connection name.
     * @throws IllegalArgumentException
     *             if the parameter is <code>null</code> or empty after trim.
     * @throws ConfigException
     *             if the namespace could not be found, or if the connection factory could not be instantiated with the
     *             given namespace, or if the connection name is unknown to the connection factory.
     */
    protected BaseDBRetrieval(String namespace) throws ConfigException {
        UserProjectDataStoreHelper.validateStringEmptyNull(namespace, "namespace");

        DBConnectionFactoryImpl connFactoryImpl;
        String connectionName;

        // Gets the DBConnectionFactory and ConnectionName from the configure file.
        // And does the configure validation.
        try {
            connFactoryImpl = new DBConnectionFactoryImpl(namespace);
            connectionName = (String) ConfigManager.getInstance().getProperty(namespace, CONNNAME_STRING);

            if (connectionName != null && !connFactoryImpl.contains(connectionName)) {
                throw new ConfigException("The connection name is unknown to the connection factory.");
            }
        } catch (UnknownNamespaceException e) {
            throw new ConfigException("The namespace could not be found.", e);
        } catch (UnknownConnectionException e) {
            throw new ConfigException("Error occurs due to the unknown connection.", e);
        } catch (ConfigurationException e) {
            throw new ConfigException("Error occurs while reading the configuration properties and "
                    + "initializing the state of the factory.", e);
        } catch (IllegalArgumentException e) {
            throw new ConfigException("The connection name defined can not be empty.", e);
        }

        // Sets the fields.
        this.connFactory = connFactoryImpl;
        this.connName = connectionName;
    }

    /**
     * <p>
     * Executes the prepared statement as a query. And iterate over the result set, get objects from the ResultSet, then
     * add them to the Map and return.
     * </p>
     *
     * @param ps
     *            the prepared statement to execute as a query.
     * @return map from Long(id) to ExternalObject.
     * @throws RetrievalException
     *             if any exception occurred during processing; it will wrap the underlying exception.
     */
    protected Map retrieveObjects(PreparedStatement ps) throws RetrievalException {
        Map map = new HashMap();

        ResultSet rs = null;
        try {
            // Executes the PreparedStatement to get the ResultSet.
            rs = ps.executeQuery();

            ExternalObject externalObject;
            while (rs.next()) {
                // Creates an ExternalObject from the columns in the given result set.
                // Then puts it into the map.
                externalObject = createObject(rs);
                map.put(externalObject.getId(), externalObject);
            }
        } catch (SQLException e) {
            throw new RetrievalException("Error occurs during the retrieve objects processing.", e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    // ignored
                }
            }
        }

        return map;
    }

    /**
     * <p>
     * Creates an <code>{@link ExternalObject}</code> from the columns in the given result set.
     * </p>
     * <p>
     * This method is called by <code>retrieveObjects</code>.
     * </p>
     *
     * @param rs
     *            a result set row which contains the columns needed to instantiate the desired
     *            <code>ExternalObject</code> implementation.
     * @return an <code>ExternalObject</code> populated with the columns of the given result set.
     * @throws RetrievalException
     *             if rs didn't contain required columns, or if any of them could not be retrieved.
     */
    protected abstract ExternalObject createObject(ResultSet rs) throws RetrievalException;

    /**
     * <p>
     * Creates a new connection from the <code>{@link DBConnectionFactory}</code> in this object, using the configured
     * connection name. Always create a new connection.
     * </p>
     *
     * @return a new connection from the connection factory.
     * @throws RetrievalException
     *             if a connection could not be created.
     */
    protected Connection getConnection() throws RetrievalException {
        try {
            if (connName != null) {
                return this.connFactory.createConnection(this.connName);
            } else {
                return this.connFactory.createConnection();
            }
        } catch (DBConnectionException e) {
            throw new RetrievalException("The connection could not be created.", e);
        }
    }

    /**
     * <p>
     * Closes the requested resources, if each one is not null. Tries to close the prepared statement first, and then
     * the connection.
     * </p>
     * <p>
     * Nulls are allowed for both arguments.
     * </p>
     *
     * @param ps
     *            the prepared statement to close. May be null.
     * @param connection
     *            the connection to close. May be null.
     * @throws RetrievalException
     *             if an exception occurred while closing any of the parameters. it will wrap the actual exception.
     */
    protected void close(PreparedStatement ps, Connection connection) throws RetrievalException {
        Exception retException = null;
        // Tries to close the PreparedStatement.
        try {
            if (ps != null) {
                ps.close();
            }
        } catch (SQLException e) {
            retException = e;
        }

        // Tries to close the Connection.
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            retException = e;
        }

        // If any Exception caught, just throws it at the end.
        if (retException != null) {
            throw new RetrievalException("Error occurs while closing the resources.", retException);
        }
    }
}
