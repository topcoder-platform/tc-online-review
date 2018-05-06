/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.external.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.cronos.onlinereview.external.ConfigException;
import com.cronos.onlinereview.external.ExternalObject;
import com.cronos.onlinereview.external.RetrievalException;
import com.topcoder.db.connectionfactory.DBConnectionFactory;

/**
 * <p>
 * The mock class of the BaseDBRetrieval class.
 * </p>
 *
 * @author oodinary
 * @author FireIce
 * @version 1.1
 * @since 1.0
 */
public class MockBaseDBRetrieval extends BaseDBRetrieval {

    /**
     * <p>
     * The default id of the projectImpl.
     * </p>
     */
    private static final long DEFAULT_ID = 123;

    /**
     * <p>
     * The default version id of the projectImpl.
     * </p>
     */
    private static final long DEFAULT_VERSION_ID = 100;

    /**
     * <p>
     * The default version string of the projectImpl.
     * </p>
     */
    private static final String DEFAULT_VERSION_STRING = "version 1.0";

    /**
     * <p>
     * The string denotes the database column name of the component id.
     * </p>
     */
    private static final String COMPONENT_ID_STRING = "component_id";

    /**
     * <p>
     * The ctor delegates to its super(DBConnectionFactory, String).
     * </p>
     *
     * @param connFactory
     *            the connection factory to use with this object.
     * @param connName
     *            the connection name to use when creating connections.
     * @throws IllegalArgumentException
     *             if either parameter is null.
     * @throws ConfigException
     *             if the connection name doesn't correspond to a connection the factory knows about.
     */
    public MockBaseDBRetrieval(DBConnectionFactory connFactory, String connName) throws ConfigException {
        super(connFactory, connName);
    }

    /**
     * <p>
     * The ctor delegates to its super(String).
     * </p>
     *
     * @param namespace
     *            the ConfigManager namespace to retrieve the DBConnectionFactory and optional connection name.
     * @throws IllegalArgumentException
     *             if the parameter is null or empty after trim.
     * @throws ConfigException
     *             if the namespace could not be found, or if the connection factory could not be instantiated with the
     *             given namespace, or if the connection name is unknown to the connection factory.
     */
    public MockBaseDBRetrieval(String namespace) throws ConfigException {
        super(namespace);
    }

    /**
     * <p>
     * The retrieveObjects method delegates to its super.retrieveObjects(PreparedStatement).
     * </p>
     *
     * @return map from Long(id) to ExternalObject.
     * @param ps
     *            the prepared statement to execute as a query.
     * @throws RetrievalException
     *             if any exception occurred during processing; it will wrap the underlying exception.
     */
    public Map retrieveObjects(PreparedStatement ps) throws RetrievalException {
        return super.retrieveObjects(ps);
    }

    /**
     * <p>
     * The createObject method implements the super's abstract method.
     * </p>
     *
     * @return an ExternalObject populated with the columns of the given result set.
     * @param rs
     *            a result set row which contains the columns needed to instantiate the desired ExternalObject
     *            implementation.
     */
    protected ExternalObject createObject(ResultSet rs) {
        long id = DEFAULT_ID;
        try {
            id = rs.getLong(COMPONENT_ID_STRING);
        } catch (SQLException e) {
            // Ignore.
        }

        return new ExternalProjectImpl(id, DEFAULT_VERSION_ID, DEFAULT_VERSION_STRING);
    }

    /**
     * <p>
     * The getConnection method delegates to its super.getConnection().
     * </p>
     *
     * @return a new connection from the connection factory.
     * @throws RetrievalException
     *             if a connection could not be created.
     */
    public Connection getConnection() throws RetrievalException {
        return super.getConnection();
    }

    /**
     * <p>
     * The close method delegates to its super.close(PreparedStatement, Connection).
     * </p>
     *
     * @param ps
     *            the prepared statement to close. May be null.
     * @param connection
     *            the connection to close. May be null.
     * @throws RetrievalException
     *             if an exception occurred while closing any of the parameters. it will wrap the actual exception.
     */
    public void close(PreparedStatement ps, Connection connection) throws RetrievalException {
        super.close(ps, connection);
    }
}
