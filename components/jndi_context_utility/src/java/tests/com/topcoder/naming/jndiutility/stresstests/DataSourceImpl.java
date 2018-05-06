/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.naming.jndiutility.stresstests;

import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLWarning;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.Map;

import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.Referenceable;
import javax.naming.StringRefAddr;
import javax.sql.DataSource;


/**
 * A datasource object. NOTE: This is a mock object an provides no DataSource funtionality.
 *
 * @author TCSDEVELOPER
 * @version 2.0
 */
public class DataSourceImpl implements Referenceable, DataSource {
    /** The name of the DataSource */
    private String name;

    /**
     * Constructor
     *
     * @param name a name
     */
    public DataSourceImpl(String name) {
        this.name = name;
    }

    /**
     * Return a String describing this DataSource
     *
     * @return a String describing this DataSource
     */
    public String toString() {
        return name;
    }

    /**
     * Return a Reference for this object
     *
     * @return a Reference for this object
     *
     * @throws NamingException if a NamingException occurs
     */
    public Reference getReference() throws NamingException {
        return new Reference(DataSource.class.getName(), new StringRefAddr("datasource", name),
            TestObjectFactory.class.getName(), null);
    }

    // Start of MOCK methods
    public Connection getConnection() {
        return new Connection() {
                public void clearWarnings() {
                }

                public void close() {
                }

                public void commit() {
                }

                public Statement createStatement() {
                    return null;
                }

                public Statement createStatement(int arg1, int arg2) {
                    return null;
                }

                public Statement createStatement(int arg1, int arg2, int arg3) {
                    return null;
                }

                public boolean getAutoCommit() {
                    return false;
                }

                public String getCatalog() {
                    return "Catalog";
                }

                public int getHoldability() {
                    return 0;
                }

                public DatabaseMetaData getMetaData() {
                    return null;
                }

                public int getTransactionIsolation() {
                    return 1;
                }

                public Map getTypeMap() {
                    return null;
                }

                public SQLWarning getWarnings() {
                    return null;
                }

                public boolean isClosed() {
                    return false;
                }

                public boolean isReadOnly() {
                    return false;
                }

                public String nativeSQL(String arg) {
                    return null;
                }

                public CallableStatement prepareCall(String SQl) {
                    return null;
                }

                public CallableStatement prepareCall(String SQl, int arg1, int arg2) {
                    return null;
                }

                public CallableStatement prepareCall(String SQl, int arg1, int arg2, int arg3) {
                    return null;
                }

                public PreparedStatement prepareStatement(String sql) {
                    return null;
                }

                public PreparedStatement prepareStatement(String sql, int arg1) {
                    return null;
                }

                public PreparedStatement prepareStatement(String sql, int[] arg1) {
                    return null;
                }

                public PreparedStatement prepareStatement(String sql, int arg1, int arg2) {
                    return null;
                }

                public PreparedStatement prepareStatement(String sql, int arg1, int arg2, int arg3) {
                    return null;
                }

                public PreparedStatement prepareStatement(String sql, String[] arg1) {
                    return null;
                }

                public void releaseSavepoint(Savepoint savepoint) {
                }

                public void rollback() {
                }

                public void rollback(Savepoint s) {
                }

                public void setAutoCommit(boolean arg) {
                }

                public void setCatalog(String arg) {
                }

                public void setHoldability(int arg) {
                }

                public void setReadOnly(boolean arg) {
                }

                public void setTransactionIsolation(int level) {
                }

                public void setTypeMap(Map map) {
                }

                public Savepoint setSavepoint() {
                    return null;
                }

                public Savepoint setSavepoint(String arg) {
                    return null;
                }
            };
    }

    /**
     * Get a Connection
     *
     * @param name a name
     * @param password a password
     *
     * @return a Connection
     */
    public Connection getConnection(String name, String password) {
        return null;
    }

    /**
     * Get the login timeout
     *
     * @return the login timeout
     */
    public int getLoginTimeout() {
        return 0;
    }

    /**
     * Get the log writer
     *
     * @return a PrintWriter
     */
    public PrintWriter getLogWriter() {
        return null;
    }

    /**
     * Set th login timeout
     *
     * @param seconds the timeout
     */
    public void setLoginTimeout(int seconds) {
    }

    /**
     * Set the log writer
     *
     * @param p a PrintWriter
     */
    public void setLogWriter(PrintWriter p) {
    }

    // End of MOCK methods
}
