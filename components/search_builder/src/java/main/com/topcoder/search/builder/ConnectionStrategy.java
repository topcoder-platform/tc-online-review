/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder;

import java.util.List;


/**
 * <p>
 * It holds the Connection object used to conduct search,all the search are do at the according classes
 * to this abstract class.
 * </p>
 *
 * @author ShindouHikaru, TCSDEVELOPER
 * @version 1.3
 * @deprecated This public class is not used any more
 */
public abstract class ConnectionStrategy {
    /**
     * It holds the ConnectionInformation object used to create a connection to the data store.
     *
     */
    protected final ConnectionInformation connectionInfo;

    /**
     * <p>Create a new instance for providing the ConnectionInformation,which contain the connection factory.</p>
     *
     * @param connectionInfo the ConnectionInformation to set to the ConnectionStrategy.
     * @throws NullPointerException if any parameter is Null
     */
    protected ConnectionStrategy(ConnectionInformation connectionInfo) {
        if (connectionInfo == null) {
            throw new NullPointerException(
                "The ConnectionInformation should not be null to construct ConnectionStrategy");
        }

        this.connectionInfo = connectionInfo;
    }

    /**
     * <p>
     * Search data store and return search result,including the connection creation and searchDataStore
     * The different class extends this abstract class may access the different datasource, database or LDAP server.
     * </p>
     *
     *
     * @param searchString a search string used to conduct the search. For example.
     * for database, it is a sql, for LDAP it is a filter string
     * @return Object holding search result
     * @throws PersistenceOperationException
     * @throws IllegalArgumentException if any parameter is Null
     * @throws IllegalArgumentException is any paramter is empty
     * @throws PersistenceOperationException if any error when operating over data store
     */
    public synchronized Object search(String searchString)
        throws PersistenceOperationException {
        if (searchString == null) {
            throw new IllegalArgumentException(
                "The searchString should not be null");
        }

        if (searchString.length() == 0) {
            throw new IllegalArgumentException(
                "The searchString should not be empty");
        }

        connect();

        return searchDataStore(searchString);
    }

    /**
     * <p>
     * Search data store and return specified fields.
     * The abstract mothed depend the subclass which extends it to access the database or LDAP
     * ,the search is base on the filter,and the return fields base on the fields.
     * </p>
     *
     *
     *
     * @param searchString a search string used to conduct the search. For example.
     * for database, it is a sql, for LDAP it is a filter string
     * @param fields fileds should be returned
     * @return Object holding search result
     * @throws PersistenceOperationException
     * @throws IllegalArgumentException if any parameter is Null or any element in fields parameter is null
     * @throws IllegalArgumentException is any paramter is empty or any element in the fields parameter is empty
     * @throws PersistenceOperationException if any error when operating over data store
     */
    public abstract Object search(String searchString, List fields)
        throws PersistenceOperationException;

    /**
     * <p>Close the connection.</p>
     *
     *
     * @throws PersistenceOperationException if any error occurs.
     *
     */
    public abstract void close() throws PersistenceOperationException;

    /**
     * <p>
     * Connect to a data store,different subclass connect different data store.
     * </p>
     *
     *
     * @throws PersistenceOperationException if any error when operating over data store
     */
    protected abstract void connect() throws PersistenceOperationException;

    /**
     * <p>
     * Search data store and return the result,depend on the searchString which is given.
     * </p>
     *
     *
     * @param searchString a search string used to conduct the search. For example.
     * for database, it is a sql, for LDAP it is a filter string
     * @return Object holding search result
     * @throws PersistenceOperationException
     * @throws IllegalArgumentException if any parameter is Null
     * @throws IllegalArgumentException is any paramter is empty
     * @throws PersistenceOperationException if any error when operating over data store
     */
    protected abstract Object searchDataStore(String searchString)
        throws PersistenceOperationException;
}
