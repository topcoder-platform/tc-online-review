/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.db.connectionfactory.jdbc2;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLWarning;
import java.sql.Statement;

import java.util.Map;

import com.topcoder.db.connectionfactory.DummyConnection;


/**
 * <p>
 * Dummy connection used in the test for JDBC 2.0 in JDK 1.3.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public final class DummyConnectionImpl implements Connection, DummyConnection {
    /**
     * <p>
     * Information stored in this connection.
     * </p>
     */
    private String information;

    /**
     * <p>
     * Create a new instance of <code>DummyConnectionImpl</code> specifying the information stored in this connection.
     * </p>
     *
     * @param info information stored in this connection
     */
    public DummyConnectionImpl(String info) {
        information = info;
    }

    /**
     * <p>
     * Get the information stored in this conection.
     * </p>
     *
     * @return information stored in this conection
     */
    public String getInformation() {
        return information;
    }

    /**
     * <p>
     * Dummy implementation of <code>Connection</code>.
     * </p>
     *
     * @return 0
     */
    public int getTransactionIsolation() {
        return 0;
    }

    /**
     * <p>
     * Dummy implementation of <code>Connection</code>.
     * </p>
     */
    public void clearWarnings() {
    }

    /**
     * <p>
     * Dummy implementation of <code>Connection</code>.
     * </p>
     */
    public void close() {
    }

    /**
     * <p>
     * Dummy implementation of <code>Connection</code>.
     * </p>
     */
    public void commit() {
    }

    /**
     * <p>
     * Dummy implementation of <code>Connection</code>.
     * </p>
     */
    public void rollback() {
    }

    /**
     * <p>
     * Dummy implementation of <code>Connection</code>.
     * </p>
     *
     * @return <code>false</code>
     */
    public boolean getAutoCommit() {
        return false;
    }

    /**
     * <p>
     * Dummy implementation of <code>Connection</code>.
     * </p>
     *
     * @return <code>false</code>
     */
    public boolean isClosed() {
        return false;
    }

    /**
     * <p>
     * Dummy implementation of <code>Connection</code>.
     * </p>
     *
     * @return <code>false</code>
     */
    public boolean isReadOnly() {
        return false;
    }

    /**
     * <p>
     * Dummy implementation of <code>Connection</code>.
     * </p>
     *
     * @param level dummy argument
     */
    public void setTransactionIsolation(int level) {
    }

    /**
     * <p>
     * Dummy implementation of <code>Connection</code>.
     * </p>
     *
     * @param autoCommit dummy argument
     */
    public void setAutoCommit(boolean autoCommit) {
    }

    /**
     * <p>
     * Dummy implementation of <code>Connection</code>.
     * </p>
     *
     * @param readOnly dummy argument
     */
    public void setReadOnly(boolean readOnly) {
    }

    /**
     * <p>
     * Dummy implementation of <code>Connection</code>.
     * </p>
     *
     * @return <code>null</code>
     */
    public String getCatalog() {
        return null;
    }

    /**
     * <p>
     * Dummy implementation of <code>Connection</code>.
     * </p>
     *
     * @param catalog dummy argument
     */
    public void setCatalog(String catalog) {
    }

    /**
     * <p>
     * Dummy implementation of <code>Connection</code>.
     * </p>
     *
     * @return <code>null</code>
     */
    public DatabaseMetaData getMetaData() {
        return null;
    }

    /**
     * <p>
     * Dummy implementation of <code>Connection</code>.
     * </p>
     *
     * @return <code>null</code>
     */
    public SQLWarning getWarnings() {
        return null;
    }

    /**
     * <p>
     * Dummy implementation of <code>Connection</code>.
     * </p>
     *
     * @return <code>null</code>
     */
    public Statement createStatement() {
        return null;
    }

    /**
     * <p>
     * Dummy implementation of <code>Connection</code>.
     * </p>
     *
     * @param resultSetType dummy argument
     * @param resultSetConcurrency dummy argument
     *
     * @return <code>null</code>
     */
    public Statement createStatement(int resultSetType, int resultSetConcurrency) {
        return null;
    }

    /**
     * <p>
     * Dummy implementation of <code>Connection</code>.
     * </p>
     *
     * @return <code>null</code>
     */
    public Map getTypeMap() {
        return null;
    }

    /**
     * <p>
     * Dummy implementation of <code>Connection</code>.
     * </p>
     *
     * @param map dummy argument
     */
    public void setTypeMap(Map map) {
    }

    /**
     * <p>
     * Dummy implementation of <code>Connection</code>.
     * </p>
     *
     * @param sql dummy argument
     *
     * @return <code>null</code>
     */
    public String nativeSQL(String sql) {
        return null;
    }

    /**
     * <p>
     * Dummy implementation of <code>Connection</code>.
     * </p>
     *
     * @param sql dummy argument
     *
     * @return <code>null</code>
     */
    public CallableStatement prepareCall(String sql) {
        return null;
    }

    /**
     * <p>
     * Dummy implementation of <code>Connection</code>.
     * </p>
     *
     * @param sql dummy argument
     * @param resultSetType dummy argument
     * @param resultSetConcurrency dummy argument
     *
     * @return <code>null</code>
     */
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) {
        return null;
    }

    /**
     * <p>
     * Dummy implementation of <code>Connection</code>.
     * </p>
     *
     * @param sql dummy argument
     *
     * @return <code>null</code>
     */
    public PreparedStatement prepareStatement(String sql) {
        return null;
    }

    /**
     * <p>
     * Dummy implementation of <code>Connection</code>.
     * </p>
     *
     * @param sql dummy argument
     * @param resultSetType dummy argument
     * @param resultSetConcurrency dummy argument
     *
     * @return <code>null</code>
     */
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) {
        return null;
    }
}





