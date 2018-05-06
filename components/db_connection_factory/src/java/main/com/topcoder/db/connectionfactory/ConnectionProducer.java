/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.db.connectionfactory;

import java.sql.Connection;

/**
 * <p>
 * The interface specifying the API for the producers of a connections to a specific databases. Such
 * a producer is responsible for creating a connections to a specific database.
 * </p>
 * <p>
 * As of <code>DB Connection Factory 1.0</code> the <code>ConnectionProducer</code>s are used
 * by a <code>DBConnectionFactoryImpl</code> to configure and create the connections to a
 * databases. The implementations which are expected to be used to configure the
 * <code>DBConnectionFactoryImpl</code> through configuration file are required to provide a
 * public constructor accepting a <code>Property</code> instance providing the configuration
 * parameters to be used to initialize the producer. <b>But this is deprecated in current version.</b>
 * </p>
 * <p>
 * As of <code>DB Connection Factory 1.1</code>, the implementations which are expected to be
 * used to configure the <code>DBConnectionFactoryImpl</code> through configuration object should
 * add a new constructor with a <code>ConfigurationObject</code> parameter, which creates the producer
 * based on the configuration stored in the <code>ConfigurationObject</code> instance. That
 * constructor should propagate the <code>ConfigurationAccessException</code> when reading the
 * configuration.
 * </p>
 * <p>
 * Changes in version 1.1: <br>
 * An extra method is declared to allow clients obtaining connections that are authenticated with
 * user-provided credentials rather than credentials recorded in producer configuration.
 * </p>
 * <p>
 * Side Note : Empty String means the length of string after trimming equals to zero.
 * </p>
 * <p>
 * <strong>Thread Safety:</strong> The implementations are expected to operate in a thread-safe
 * manner.
 * </p>
 *
 * @author isv, qiucx0161, nhzp339, magicpig
 * @version 1.1
 * @since 1.0
 */
public interface ConnectionProducer {
    /**
     * <p>
     * Create a default connection to a database. The implementations may chose either to create a
     * new connection on each request or return a connection from a connection pool.
     * </p>
     * <p>
     * Side Note : This method creates the "default connection", not the "connection with default
     * username and password", in some cases these two conceptions represent different meanings.
     * </p>
     *
     * @return a <code>Connection</code> providing the connection to a database. Will not be null.
     * @throws DBConnectionException
     *             if any error occurs while creating a connection.
     */
    Connection createConnection() throws DBConnectionException;

    /**
     * <p>
     * Creates a connection to a database authenticated with the specified username and password.
     * The implementations may chose either to create a new connection on each request or return a
     * connection from a connection pool.
     * </p>
     *
     * @param username
     *            The username with which to connect to the database. Can be any String instance
     *            including null and empty string.
     * @param password
     *            The user's password. Can be any String instance including null and empty string.
     * @return a Connection providing the connection to a database. Will not be null.
     * @throws DBConnectionException
     *             if any error occurs while creating the connection.
     * @since 1.1
     */
    Connection createConnection(String username, String password) throws DBConnectionException;
}
