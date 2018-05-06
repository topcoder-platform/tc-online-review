/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.database;

import com.topcoder.db.connectionfactory.ConnectionProducer;
import com.topcoder.db.connectionfactory.DBConnectionException;

import com.topcoder.search.builder.ConnectionInformation;
import com.topcoder.search.builder.ConnectionStrategy;
import com.topcoder.search.builder.PersistenceOperationException;

import com.topcoder.util.sql.databaseabstraction.CustomResultSet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Iterator;
import java.util.List;


/**
 * <p>
 * The class  extends ConnectionStrategy class, and provides concrete implementations of abstract methods.
 * This class is used to search the database. All the database access in this component is done via this class.
 * Once the connection is established, it can not be changed.
 * </p>
 *
 * <p>The class is thread- safe.</p>
 *
 * @author victor_lxd, haozhangr
 * @version 1.1
 */
public class DatabaseConnectionStrategy extends ConnectionStrategy {
    /**
     * It holds the Connection object used to conduct search.
     */
    private Connection connection = null;

    /**
     * It will hold a PreparedStatement object,which can be set the excute with the paramter list.
     *
     */
    private PreparedStatement statement = null;

    /**
     * <p>
     * Create a new instance by providing the ConnectionInformation to this DatabaseConnectionStrategy.
     * </p>
     *
     * @param connectionInfo a ConnectionInformation object
     * @throws NullPointerException if any parameter is Null
     */
    public DatabaseConnectionStrategy(ConnectionInformation connectionInfo) {
        super(connectionInfo);
        connection = null;
    }

    /**
     * <p>
     * Search data store and return specified fields,all the return fields both include
     * the fields in the searchString and the list of fields.All the items list in the fields
     * should be String type.The returned object is instance of CustomResultSet.
     * </p>
     *
     * @param searchString a search string used to conduct the search
     * @param fields names of fileds should be returned
     * @return a CustomResultSet object holding search result
     * @throws NullPointerException if any parameter is Null
     * @throws IllegalArgumentException if any paramter is empty or any element in the fields parameter is empty
     * @throws PersistenceOperationException if any error when operating over data store
     */
    public synchronized Object search(String searchString, List fields)
        throws PersistenceOperationException {
        if (searchString == null) {
            throw new NullPointerException(
                "The searchString should not be null to search in DatabaseConnectionStrategy.");
        }

        if (searchString.length() == 0) {
            throw new IllegalArgumentException(
                "The searchString should not be empty to search in DatabaseConnectionStrategy.");
        }
        checkFileds(fields);

        try {
            //if connection null do connect()
            if (connection == null) {
                connect();
            }

            //get String for search
            String completeString = includeReturnFieldsInSearchString(searchString,
                    fields);

            return searchDataStore(completeString);
        } catch (PersistenceOperationException e) {
            //do not wrap this PersistenceOperationException
            throw e;
        } catch (Exception e) {
            //wrap the Exception by PersistenceOperationException
            throw new PersistenceOperationException("exception occurs while do search in DatabaseConnectionStrategy.",
                e);
        }
    }

    /**
     * <p>
     * Create a PreparedStatement using the given sql string,the PreparedStatement is create
     * base on the own connection is this class.
     * </p>
     *
     * @param statement a sql string used to construct PreparedStatement
     * @throws NullPointerException if any parameter is null
     * @throws IllegalArgumentException if statement is an empty string
     * @throws PersistenceOperationException if any error when operating over data store
     */
    public synchronized void compileStatement(String statement)
        throws PersistenceOperationException {
        if (statement == null) {
            throw new NullPointerException(
                "The statement should not be null to compileStatement in DatabaseConnectionStrategy.");
        }

        if (statement.length() == 0) {
            throw new IllegalArgumentException(
                "The statement should not be empty to compileStatement in DatabaseConnectionStrategy.");
        }

        try {
            //if connection null do connect()
            if (connection == null) {
                connect();
            }

            this.statement = connection.prepareStatement(statement);
        } catch (SQLException e) {
            //wrap the SQLException with PersistenceOperationException
            throw new PersistenceOperationException(
                "SQLException occurs while compileStatement"
                + "do get prepareStatement in DatabaseConnectionStrategy.", e);
        }
    }

    /**
     * <p>
     * Search the prepared statement with given parameters,all the paramters will be setted to the
     * statement before the search,paramters can be empty, which means just excute the statement
     * without any setting.The result is returned as a instance of CustomResultSet.
     * </p>
     *
     *
     * @param parameters parameters used to put in the prepared statement
     * @return a CustomResultSet representing the search result
     * @throws NullPointerException if any parameter is null
     * @throws PersistenceOperationException if any error when operating over data store
     */
    public synchronized CustomResultSet executePrecompiledStatement(
        List parameters) throws PersistenceOperationException {
        if (parameters == null) {
            throw new NullPointerException(
                "The list of parameters should not be null to do executePrecompiledStatement.");
        }

        try {
            int ind = 1;
            //set the paramters
            for (Iterator it = parameters.iterator(); it.hasNext(); ind++) {
                statement.setObject(ind, it.next());
            }

            return new CustomResultSet(statement.executeQuery());
        } catch (SQLException e) {
            //wrap the SQLException by PersistenceOperationException
            throw new PersistenceOperationException("SQLException occurs while do executePrecompiledStatement.",
                e);
        }
    }

    /**
     * <p>
     * Close the connection if the connection has been create.
     * </p>
     *
     * @throws PersistenceOperationException is any error occurs during the close
     *
     */
    public synchronized void close() throws PersistenceOperationException {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            //wrap the PersistenceOperationException with SQLException
            throw new PersistenceOperationException(
                "SQLException occurs while colse the connection in DatabaseConnectionStrategy.");
        }
    }

    /**
     * <p>
     * Search database and return the result,the param String as the sql to be excuted
     * the result is return as a instance of CustomResultSet.
     * </p>
     *
     * @param searchString a search string used to conduct the search
     * @return a CustomResultSet object holding search result
     * @throws NullPointerException if any parameter is Null
     * @throws IllegalArgumentException is any paramter is empty
     * @throws PersistenceOperationException if any error when operating over data store
     */
    protected synchronized Object searchDataStore(String searchString)
        throws PersistenceOperationException {
        if (searchString == null) {
            throw new NullPointerException(
                "The searchString should not be null to searchDataStore in DatabaseConnectionStrategy.");
        }

        if (searchString.length() == 0) {
            throw new IllegalArgumentException(
                "The searchString should not be empty to searchDataStore in DatabaseConnectionStrategy.");
        }

        Statement st = null;

        try {
            st = connection.createStatement();

            ResultSet reslutSet = st.executeQuery(searchString);

            return new CustomResultSet(reslutSet);
        } catch (SQLException e) {
            //wrap the SQLException with PersistenceOperationException
            throw new PersistenceOperationException("SQLException occurs while searchDataStore to get CustomResultSet",
                e);
        } finally {
            try {
                st.close();
            } catch (SQLException e) {
                //IGNORE
            }
        }
    }

    /**
     * <p>
     * Connect to database,create the connection to the database if the database has not been created before.
     * The connection to the database is only created by this mothed.
     * </p>
     *
     *
     *
     *
     * @throws PersistenceOperationException if any error when operating over data store
     */
    protected synchronized void connect() throws PersistenceOperationException {
        if (connection != null) {
            return;
        }

        try {
            ConnectionProducer dbf = (ConnectionProducer) connectionInfo.getFactory();
            connection = dbf.createConnection();
        } catch (DBConnectionException e) {
            //wrap the DBConnectionException by PersistenceOperationException
            throw new PersistenceOperationException("DBConnectionException occurs while get connection.",
                e);
        }
    }

    /**
     * <p>
     * Build a complete search string via including return fields into the search string
     * at last,the return fields will include both of those in the list and the searchString.
     * The mothed do not need synchronized.
     * </p>
     *
     *
     * @param searchString a search string used to conduct the search
     * @param fields names of fileds should be returned
     * @return constructed search string
     * @throws NullPointerException if any parameter is Null
     * @throws IllegalArgumentException is any paramter is empty or any element in the fields parameter is empty
     */
    protected String includeReturnFieldsInSearchString(String searchString,
        List fields) {
        //check searchString null
        if (searchString == null) {
            throw new NullPointerException(
                "The searchString should not be null to includeReturnFieldsInSearchString.");
        }

        //check searchString empty
        if (searchString.length() == 0) {
            throw new IllegalArgumentException(
                "The searchString should not be empty to includeReturnFieldsInSearchString.");
        }
        checkFileds(fields);

        Iterator it = fields.iterator();
        StringBuffer buffer = new StringBuffer((String) it.next());
        while (it.hasNext()) {
            buffer.append(",").append((String) it.next());
        }
        String s = buffer.toString();

        //find the 'select' token
        int index1 = searchString.toLowerCase().indexOf("select");

        //find the 'from' token
        int index2 = searchString.toLowerCase().indexOf("from");

        //if not find the 'select token'
        if ((index1 < 0) || (index2 < 0)) {
            throw new IllegalArgumentException(
                "The searchString should contain the 'select' token to includeReturnFieldsInSearchString.");
        }

        String filedNames = searchString.substring(index1 + "select".length(),
                index2).trim();

        //if select all then return the searchString
        if (filedNames.equalsIgnoreCase("*")) {
            //select all the fields
            return searchString;
        }

        //fields already exist in context
        if (filedNames.length() != 0) {
            filedNames = filedNames + ",";
        }
        //makeup the string and return
        buffer = new StringBuffer();
        buffer.append("select ").append(filedNames).append(s);
        buffer.append(" ").append(searchString.substring(index2));
        return buffer.toString();
    }
    /**
     * <p>
     * The the list of field whether it is valid.
     * The list should not be null and empty.
     * Also, all the items in the list should
     * instance of String, can not be null and empty.
     * </p>
     *
     * @param fields the list of fields to be checked
     * @throws NullPointerException if the fields is null.
     * @throws IllegalArgumentException if any of the items
     * in the fields is null of not instance of String or it is a empty String.
     */
    private void checkFileds(List fields) {
        DataBaseHelper.checkList(fields, "fields");

        //check the fields in the list empty
        for (Iterator it = fields.iterator(); it.hasNext();) {
            Object nextItem = it.next();
            if (!(nextItem instanceof String)) {
                throw new IllegalArgumentException(
                    "The value in the fields should be instance of String.");
            }

            if (((String) nextItem).length() == 0) {
                throw new IllegalArgumentException(
                    "The value in the fields should not be empty to search.");
            }
        }
    }
}
