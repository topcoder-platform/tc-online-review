/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.external.impl;

import java.sql.ResultSet;

import com.cronos.onlinereview.external.ConfigException;
import com.cronos.onlinereview.external.ExternalObject;
import com.cronos.onlinereview.external.RetrievalException;
import com.topcoder.db.connectionfactory.DBConnectionFactory;

/**
 * <p>
 * The mock class of the DBProjectRetrieval class.
 * </p>
 *
 * @author oodinary
 * @author FireIce
 * @version 1.1
 * @since 1.0
 */
public class MockDBProjectRetrieval extends DBProjectRetrieval {

    /**
     * <p>
     * The ctor delegates to its super(DBConnectionFactory, String, int).
     * </p>
     *
     * @param connFactory
     *            the connection factory to use with this object.
     * @param connName
     *            the connection name to use when creating connections.
     * @param forumType
     *            the forum type code to use in the retrieve queries.
     * @throws IllegalArgumentException
     *             if connFactory or connName is null or if forumType is negative.
     * @throws ConfigException
     *             if the connection name doesn't correspond to a connection the factory knows about.
     */
    public MockDBProjectRetrieval(DBConnectionFactory connFactory, String connName, int forumType)
            throws ConfigException {
        super(connFactory, connName, forumType);
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
     *             if the namespace could not be found, or if the connection factory could not be instantiated with the
     *             given namespace, or if the connection name is unknown to the connection factory, or if the
     *             "forumType" property was not positive.
     */
    public MockDBProjectRetrieval(String namespace) throws ConfigException {
        super(namespace);
    }

    /**
     * <p>
     * The mock method just calls the super.createObject(ResultSet).
     * </p>
     *
     * @return an ExternalProjectImpl with the columns of the given result set.
     * @param rs
     *            a result set row which contains the columns needed to instantiate an ExternalProjectImpl object.
     * @throws RetrievalException
     *             if rs didn't contain the required columns, or if any of them could not be retrieved.
     */
    public ExternalObject createObject(ResultSet rs) throws RetrievalException {
        return super.createObject(rs);
    }
}
