/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.idgenerator;

import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.topcoder.db.connectionfactory.UnknownConnectionException;

import com.topcoder.util.config.ConfigManagerException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import java.util.Collection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * The DB helper class centralizes db operations for generating ids.
 */
final class DBHelper {
    /** The default namespace used by DBConnection component to get the Connection. */
    private static final String DEFAULT_DBFACTORY_NAMESPACE =
        "com.topcoder.db.connectionfactory.DBConnectionFactoryImpl";

    /** The default connection name of the id generator sequence. */
    private static final String DEFAULT_CONNECTION_NAME = "DefaultSequence";

    /** The default select sql sentence used for retrieving data from table. */
    private static final String DEFAULT_SELECT_NEXT_BLOCK = "SELECT next_block_start, block_size, exhausted FROM " +
            "id_sequences WHERE name = ? FOR UPDATE";

    /** The default sql sentence to update the next_block_start of the table. */
    private static final String DEFAULT_UPDATE_NEXT_BLOCK_START = "UPDATE id_sequences SET next_block_start = ? " +
            "WHERE name = ?";

    /** The default sql sentence to set the exausted to 1. */
    private static final String DEFAULT_UPDATE_EXHAUSTED = "UPDATE id_sequences SET exhausted = 1 WHERE name = ?";

    /** The key to the select sql sentence or the corresponding statement used for retrieving data from table. */
    public static final String SELECT_NEXT_BLOCK_KEY = "select_next_block";

    /** The key to the sql sentence or the corresponding statement to update the next_block_start of the table. */
    public static final String UPDATE_NEXT_BLOCK_START_KEY = "update_next_block_start";

    /** The key to sql sentence or the corresponding statement to set the exausted to 1. */
    public static final String UPDATE_EXHAUSTED_KEY = "update_exhausted";

    /** Reprents whether a database connection should be reused whenever possible. */
    private final boolean reuseConnection;

    /** the db connection factory used to create database connections */
    private DBConnectionFactory factory;

    /** the namespace to initializes the db connection factory */
    private final String dbFactoryNamespace;

    /** the connection name of the id generator sequence. */
    private final String connectionName;

    /** the sql sentences support for generating ids */
    private Map sqlSentences = new HashMap();

    /** The map which contains all prepared statements required to generating ids. */
    private Map preparedStatements = new HashMap();

    /** The connection to the database; */
    private Connection conn;

    /**
     * Initializes the db helper from a configuration namespace.
     *
     * @param namespace the configuration namespace
     *
     * @throws ConfigManagerException if failed to retreive configuration settings.
     */
    public DBHelper(String namespace) throws ConfigManagerException {
        sqlSentences.put(SELECT_NEXT_BLOCK_KEY,
            IDGeneratorHelper.getConfigurationSetting(namespace, SELECT_NEXT_BLOCK_KEY, DEFAULT_SELECT_NEXT_BLOCK));
        sqlSentences.put(UPDATE_NEXT_BLOCK_START_KEY,
            IDGeneratorHelper.getConfigurationSetting(namespace, UPDATE_NEXT_BLOCK_START_KEY,
                DEFAULT_UPDATE_NEXT_BLOCK_START));
        sqlSentences.put(UPDATE_EXHAUSTED_KEY,
            IDGeneratorHelper.getConfigurationSetting(namespace, UPDATE_EXHAUSTED_KEY, DEFAULT_UPDATE_EXHAUSTED));

        String reuseConnectionValue = IDGeneratorHelper.getConfigurationSetting(namespace, "reuse_connection", "false");
        reuseConnection = Boolean.valueOf(reuseConnectionValue).booleanValue();

        dbFactoryNamespace = IDGeneratorHelper.getConfigurationSetting(namespace, "db_factory_namespace",
                DEFAULT_DBFACTORY_NAMESPACE);
        connectionName = IDGeneratorHelper.getConfigurationSetting(namespace, "connection_name", DEFAULT_CONNECTION_NAME);
    }

    /**
     * Initializes the db helper with default configuration settings.
     */
    public DBHelper() {
        sqlSentences.put(SELECT_NEXT_BLOCK_KEY, DEFAULT_SELECT_NEXT_BLOCK);
        sqlSentences.put(UPDATE_NEXT_BLOCK_START_KEY, DEFAULT_UPDATE_NEXT_BLOCK_START);
        sqlSentences.put(UPDATE_EXHAUSTED_KEY, DEFAULT_UPDATE_EXHAUSTED);
        dbFactoryNamespace = DEFAULT_DBFACTORY_NAMESPACE;
        connectionName = DEFAULT_CONNECTION_NAME;
        reuseConnection = false;
    }

    /**
     * Executes a sql statement. If the connection is to be reused, it first tries to execute the  prepared statement
     * according to the key. If failed the first time, it will recreate an database connection and prepare the
     * statements against the newly created database connection and then try again. If the connection is not to be
     * reused, it just executes the corresponding sql clause.
     *
     * @param key the key to the sql statement
     * @param parameters the parameters of the sql statement
     *
     * @return the result set for a select statement, or an integer value for an update statement.
     *
     * @throws SQLException if any error occurs while accessing database.
     * @throws IDGenerationException if the connection to the database cannot be created.
     */
    public Object execute(String key, Object[] parameters)
        throws SQLException, IDGenerationException {
        if (!reuseConnection) {
            return tryExecute(key, parameters);
        }

        try {
            return tryExecute(key, parameters);
        } catch (SQLException e) {
            prepareStatements();
        }

        return tryExecute(key, parameters);
    }

    /**
     * Commits all the changes to the database.
     */
    public void commit() {
        if (conn != null) {
            try {
                conn.commit();
            } catch (SQLException e) {
                System.out.println(e);
                //ignores
            }
        }
    }

    /**
     * Rollbacks all the changes made to the database.
     */
    public void rollback() {
        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException e) {
                //ignores
            }
        }
    }

    /**
     * Releases all the database resources.
     * 
     * @param force whether to release the resources even reuseConnection is true.
     */
    public void releaseDatabaseResources(boolean force) {
        if (!force && reuseConnection) {
            return;
        }
        
        for (Iterator it = preparedStatements.values().iterator(); it.hasNext();) {
            IDGeneratorHelper.closeStatement((PreparedStatement) it.next());
        }

        IDGeneratorHelper.close(conn);
        conn = null;
    }

    /**
     * Executes a sql statement.
     *
     * @param key the key to the sql statement
     * @param parameters the parameters of the sql statement
     *
     * @return a map array containing data in the result set for a select statement, or an integer value for an update statement.
     *
     * @throws SQLException if any error occurs while accessing database.
     * @throws IDGenerationException if the connection to database cannot be created.
     * @throws IllegalArgumentException if the key is null or empty or not a key to a desired sql statement.
     */
    private Object tryExecute(String key, Object[] parameters)
        throws SQLException, IDGenerationException {
        if ((key == null) || (key.trim().length() == 0)) {
            throw new IllegalArgumentException("The key should not be null or empty!");
        }

        if (!sqlSentences.containsKey(key)) {
            throw new IllegalArgumentException("The key is not for a required sql statement supporting id generation.");
        }

        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            if (!reuseConnection) {
                if (conn == null) {
                    conn = createConnection();
                }
                stmt = conn.prepareStatement((String) sqlSentences.get(key));
                preparedStatements.put(key, stmt);
            } else {
                stmt = (PreparedStatement) preparedStatements.get(key);

                if (stmt == null) {
                    prepareStatements();
                    stmt = (PreparedStatement) preparedStatements.get(key);
                }
            }

            for (int i = 0; i < parameters.length; i++) {
                stmt.setObject(i + 1, parameters[i]);
            }

            if (stmt.execute()) {
                rs = stmt.getResultSet();
                ResultSetMetaData rsmd = rs.getMetaData();
                Collection result = new ArrayList();
                while (rs.next()) {
                    Map row = new HashMap();
                    for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                        row.put(rsmd.getColumnName(i), rs.getObject(i));
                    }
                    result.add(row);
                }
                
                return result.toArray(new Map[0]);
            }
            
            return new Integer(stmt.getUpdateCount());
        } finally {
            IDGeneratorHelper.closeResultSet(rs);
        }
    }

    /**
     * Prepares the sql statements
     * 
     * @throws SQLException if any database related error occurs.
     * @throws IDGenerationException if the connection to database cannot be created.
     */
    private void prepareStatements() throws SQLException, IDGenerationException {
        conn = createConnection();
        
        for (Iterator it = sqlSentences.keySet().iterator(); it.hasNext();) {
            String key = (String) it.next();
            PreparedStatement stmt = conn.prepareStatement((String) sqlSentences.get(key));
            preparedStatements.put(key, stmt);
        }
    }

    /**
     * Creates a connection to the database. If failed to get from the connection name, the default connection will be
     * used.
     *
     * @return the connection created.
     *
     * @throws IDGenerationException if any database related error occurs
     */
    private Connection createConnection() throws IDGenerationException {
        try {
            if (factory == null) {
                factory = new DBConnectionFactoryImpl(dbFactoryNamespace);
            }

            Connection conn;

            try {
                conn = factory.createConnection(connectionName);
            } catch (UnknownConnectionException e) {
                conn = factory.createConnection();
            }

            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            return conn;
        } catch (Exception e) {
            throw new IDGenerationException("The connection to the database cannot be created!", e);
        }
    }
}
