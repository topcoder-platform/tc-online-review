/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.db.connectionfactory;

import java.sql.Connection;

/**
 * <p>
 * An interface defining the factory API that enables the clients to obtain the connections with a
 * specific database.
 * </p>
 * <p>
 * Such a factory may produce different <code>Connection</code> objects providing the connections
 * to the different databases. The configured connections are mapped to a <code>String</code>
 * names uniquely distinguishing the connection among other connections. The client may obtain a
 * connection by specifying such a name or requesting a connection which is set as default for the
 * factory.
 * </p>
 * <p>
 * Side Note: those methods create the "default connection" from an underlying implementation, not
 * the "connection with default username and password", in some cases these two conceptions
 * represent different meanings. Empty String means the length of string after trimming equals to zero.
 * </p>
 * <p>
 * <b>Thread safety:</b> The implementations of this interface must be thread safe. A client can
 * use the same instance of the factory to obtain one or more connections by several threads running
 * simultaneously.
 * </p>
 * <p>
 * Changes in version 1.1:<br>
 * (1) Two extra methods are declared to allow clients obtaining connections that are authenticated
 * with user-provided credentials rather than credentials recorded in component configuration. <br>
 * (2) All the NullPointerException thrown are replaced with IllegalArgumentException.
 * </p>
 *
 * @author isv, qiucx0161, nhzp339, magicpig
 * @version 1.1
 * @since 1.0
 */
public interface DBConnectionFactory {
    /**
     * <p>
     * Creates the default SQL Connection from the underlying implementation.
     * </p>
     *
     * @return a <code>Connection</code> providing the connection to a database. Will not be null.
     * @throws DBConnectionException
     *             if any error occurs while creating the connection.
     * @throws NoDefaultConnectionException
     *             if the default connection is not configured within this factory.
     */
    Connection createConnection() throws DBConnectionException;

    /**
     * <p>
     * Creates a SQL Connection from an underlying implementation with the specified name.
     * </p>
     *
     * @param name
     *            a <code>String</code> providing the name of the connection configured within
     *            this factory.
     * @return a <code>Connection</code> providing the connection to a database. Will not be null.
     * @throws DBConnectionException
     *             if any error occurs while creating the connection.
     * @throws UnknownConnectionException
     *             if a connection with specified name is not configured within this factory.
     * @throws IllegalArgumentException
     *             if specified <code>name</code> is <code>null</code> or an empty string
     */
    Connection createConnection(String name) throws DBConnectionException;

    /**
     * <p>
     * Creates a default connection to a database authenticated with the specified username and
     * password.
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
     * @throws NoDefaultConnectionException
     *             if the default connection is not configured within this factory
     * @since 1.1
     */
    Connection createConnection(String username, String password) throws DBConnectionException;

    /**
     * <p>
     * Creates a connection to a database authenticated with the specified username and password from
     * an underlying implementation with the specified name.
     * </p>
     *
     * @param name
     *            the name of the connection configured within this factory. Can't be null or empty
     *            string.
     * @param username
     *            The username with which to connect to the database. Can be any String instance
     *            including null and empty string.
     * @param password
     *            The user's password. Can be any String instance including null and empty string.
     * @return a Connection providing the connection to a database. Will not be null.
     * @throws DBConnectionException
     *             if any error occurs while creating the connection.
     * @throws IllegalArgumentException
     *             If the name parameter is null or empty string.
     * @throws UnknownConnectionException
     *             if a connection with specified name is not configured within this factory.
     * @since 1.1
     */
    Connection createConnection(String name, String username, String password) throws DBConnectionException;
}
