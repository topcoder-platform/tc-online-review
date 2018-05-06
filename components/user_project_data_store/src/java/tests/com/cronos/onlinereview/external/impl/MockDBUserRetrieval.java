/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.external.impl;

import java.sql.Connection;
import java.sql.ResultSet;

import com.cronos.onlinereview.external.ConfigException;
import com.cronos.onlinereview.external.ExternalObject;
import com.cronos.onlinereview.external.RetrievalException;
import com.topcoder.db.connectionfactory.DBConnectionFactory;

/**
 * <p>
 * The mock class of the DBUserRetrieval class.
 * </p>
 *
 * @author oodinary
 * @author FireIce
 * @version 1.1
 * @since 1.0
 */
public class MockDBUserRetrieval extends DBUserRetrieval {

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
    public MockDBUserRetrieval(DBConnectionFactory connFactory, String connName) throws ConfigException {
        super(connFactory, connName);
    }

    /**
     * <p>
     * The ctor delegates to its super(String).
     * </p>
     *
     * @param namespace
     *            the name of the ConfigManager namespace; see BaseDBRetrieval(String) for details.
     * @throws IllegalArgumentException
     *             if the parameter is null or empty after trim.
     * @throws ConfigException
     *             if the namespace could not be found, or if the connection factory. could not be instantiated with the
     *             given namespace, or if the connection name is unknown to the connection factory.
     */
    public MockDBUserRetrieval(String namespace) throws ConfigException {
        super(namespace);
    }

    /**
     * <p>
     * The mock method just calls the super.createObject(ResultSet).
     * </p>
     *
     * @return an ExternalUserImpl with the columns of the given result set.
     * @param rs
     *            a result set row which contains the columns needed to instantiate an ExternalUserImpl object.
     * @throws RetrievalException
     *             if rs didn't contain the required columns, or if any of them could not be retrieved.
     */
    public ExternalObject createObject(ResultSet rs) throws RetrievalException {
        return super.createObject(rs);
    }

    /**
     * <p>
     * Just delegates to the super.getConnection().
     * </p>
     *
     * @return a new connection from the connection factory.
     * @throws RetrievalException
     *             if a connection could not be created.
     */
    public Connection getConnection() throws RetrievalException {
        return super.getConnection();
    }
}
