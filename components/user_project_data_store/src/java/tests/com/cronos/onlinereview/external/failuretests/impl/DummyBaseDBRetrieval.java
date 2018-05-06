/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.external.failuretests.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.cronos.onlinereview.external.ConfigException;
import com.cronos.onlinereview.external.ExternalObject;
import com.cronos.onlinereview.external.RetrievalException;
import com.cronos.onlinereview.external.impl.BaseDBRetrieval;
import com.cronos.onlinereview.external.impl.ExternalProjectImpl;
import com.topcoder.db.connectionfactory.DBConnectionFactory;

/**
 * <p>
 * The mock class of the BaseDBRetrieval class.
 * </p>
 *
 * @author idx, liulike
 * @version 1.1
 */
public class DummyBaseDBRetrieval extends BaseDBRetrieval {

    /** Default id. */
    private static final long DEFAULT_ID = 123;

    /** Default version id. */
    private static final long DEFAULT_VERSION_ID = 100;

    /** Default version string. */
    private static final String DEFAULT_VERSION_STRING = "version 1.0";

    /** Component id string. */
    private static final String COMPONENTIDSTRING = "component_id";

    /**
     * <p>
     * Mock constructor.
     * </p>
     *
     * @param connFactory the connection factory to use with this object.
     * @param connName the connection name to use when creating connections.
     * @throws IllegalArgumentException if either parameter is null or connName
     *             is empty String.
     * @throws ConfigException if the connection name doesn't correspond to a
     *             connection the factory knows about.
     */
    public DummyBaseDBRetrieval(DBConnectionFactory connFactory, String connName) throws ConfigException {

        super(connFactory, connName);
    }

    /**
     * <p>
     * Mock constructor.
     * </p>
     *
     * @param namespace the ConfigManager namespace to retrieve the
     *            DBConnectionFactory and optional connection name.
     * @throws IllegalArgumentException if the parameter is null or empty after
     *             trim.
     * @throws ConfigException if the namespace could not be found, or if the
     *             connection factory could not be instantiated with the given
     *             namespace, or if the connection name is unknown to the
     *             connection factory.
     */
    public DummyBaseDBRetrieval(String namespace) throws ConfigException {

        super(namespace);
    }

    /**
     * <p>
     * Mock method of <code>retrieveObjects(PreparedStatement)</code>.
     * </p>
     *
     * @return map from Long(id) to ExternalObject.
     * @param ps the prepared statement to execute as a query.
     * @throws RetrievalException if any exception occurred during processing;
     *             it will wrap the underlying exception.
     */
    public Map retrieveObjects(PreparedStatement ps) throws RetrievalException {

        return super.retrieveObjects(ps);
    }

    /**
     * <p>
     * Mock method of <code>createObject(ResultSet)</code>.
     * </p>
     *
     * @return an ExternalObject populated with the columns of the given result
     *         set.
     * @param rs a result set row which contains the columns needed to
     *            instantiate the desired ExternalObject implementation.
     * @throws RetrievalException if rs didn't contain required columns, or if
     *             any of them could not be retrieved.
     */
    protected ExternalObject createObject(ResultSet rs) throws RetrievalException {

        long id = DEFAULT_ID;
        try {
            id = rs.getLong(COMPONENTIDSTRING);
        } catch (SQLException sqle) {
            // Ignore.
        }

        return new ExternalProjectImpl(id, DEFAULT_VERSION_ID, DEFAULT_VERSION_STRING);
    }

    /**
     * <p>
     * Mock method of <code>getConnection()</code>.
     * </p>
     *
     * @return a new connection from the connection factory.
     * @throws RetrievalException if a connection could not be created.
     */
    public Connection getConnection() throws RetrievalException {

        return super.getConnection();
    }

    /**
     * <p>
     * Mock method of <code>close(PreparedStatement, Connection)</code>.
     * </p>
     *
     * @param ps the prepared statement to close. May be null.
     * @param connection the connection to close. May be null.
     * @throws RetrievalException if an exception occurred while closing any of
     *             the parameters. it will wrap the actual exception.
     */
    public void close(PreparedStatement ps, Connection connection) throws RetrievalException {

        super.close(ps, connection);
    }
}
