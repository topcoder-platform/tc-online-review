/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.cronos.onlinereview.phases.failuretests.mock;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * <p>A mock implementation of {@link Connection} class to be used for testing.
 * Overrides the protected methods declared by a super-class. The overridden methods are declared with package private access
 * so only the test cases could invoke them. The overridden methods simply call the corresponding method of a super-class.
 *
 * @author  isv, moon.river
 * @since 1.0
 * @version 1.3
 */
public class MockConnection implements Connection {

    /**
     * <p>A <code>Map</code> mapping the <code>String</code> method signatures to <code>Map</code>s mapping the <code>
     * String</code> names of the arguments to <code>Object</code>s representing the values of  arguments which have been 
     * provided by the caller of the method.</p>
     */
    private static Map methodArguments = new HashMap();

    /**
     * <p>A <code>Map</code> mapping the <code>String</code> method signatures to <code>Exception</code>s to be thrown by
     * methods.</p>
     */
    private static Map throwExceptions = new HashMap();

    /**
     * <p>A <code>Map</code> mapping the <code>String</code> method signatures to <code>Object</code>s to be
     * returned by methods.</p>
     */
    private static Map methodResults = new HashMap();

    /**
     * <p>A <code>Throwable</code> representing the exception to be thrown from any method of the mock class.</p>
     */
    private static Throwable globalException = null;

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see Connection#getHoldability()
     * @throws SQLException
     */
    public int getHoldability() throws SQLException {
        if (MockConnection.globalException != null) {
            if (MockConnection.globalException instanceof SQLException) {
                throw (SQLException) MockConnection.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockConnection.globalException);
            }
        }

        String methodName = "getHoldability";

        Throwable exception = (Throwable) MockConnection.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof SQLException) {
                throw (SQLException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        List args = (List) MockConnection.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockConnection.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return ((Integer) MockConnection.methodResults.get(methodName)).intValue();

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see Connection#getTransactionIsolation()
     * @throws SQLException
     */
    public int getTransactionIsolation() throws SQLException {
        if (MockConnection.globalException != null) {
            if (MockConnection.globalException instanceof SQLException) {
                throw (SQLException) MockConnection.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockConnection.globalException);
            }
        }

        String methodName = "getTransactionIsolation";

        Throwable exception = (Throwable) MockConnection.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof SQLException) {
                throw (SQLException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        List args = (List) MockConnection.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockConnection.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return ((Integer) MockConnection.methodResults.get(methodName)).intValue();

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see Connection#clearWarnings()
     * @throws SQLException
     */
    public void clearWarnings() throws SQLException {
        if (MockConnection.globalException != null) {
            if (MockConnection.globalException instanceof SQLException) {
                throw (SQLException) MockConnection.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockConnection.globalException);
            }
        }

        String methodName = "clearWarnings";

        Throwable exception = (Throwable) MockConnection.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof SQLException) {
                throw (SQLException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        List args = (List) MockConnection.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockConnection.methodArguments.put(methodName, args);
        }
        args.add(arguments);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see Connection#close()
     * @throws SQLException
     */
    public void close() throws SQLException {
        if (MockConnection.globalException != null) {
            if (MockConnection.globalException instanceof SQLException) {
                throw (SQLException) MockConnection.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockConnection.globalException);
            }
        }

        String methodName = "close";

        Throwable exception = (Throwable) MockConnection.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof SQLException) {
                throw (SQLException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        List args = (List) MockConnection.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockConnection.methodArguments.put(methodName, args);
        }
        args.add(arguments);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see Connection#commit()
     * @throws SQLException
     */
    public void commit() throws SQLException {
        if (MockConnection.globalException != null) {
            if (MockConnection.globalException instanceof SQLException) {
                throw (SQLException) MockConnection.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockConnection.globalException);
            }
        }

        String methodName = "commit";

        Throwable exception = (Throwable) MockConnection.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof SQLException) {
                throw (SQLException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        List args = (List) MockConnection.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockConnection.methodArguments.put(methodName, args);
        }
        args.add(arguments);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see Connection#rollback()
     * @throws SQLException
     */
    public void rollback() throws SQLException {
        if (MockConnection.globalException != null) {
            if (MockConnection.globalException instanceof SQLException) {
                throw (SQLException) MockConnection.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockConnection.globalException);
            }
        }

        String methodName = "rollback";

        Throwable exception = (Throwable) MockConnection.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof SQLException) {
                throw (SQLException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        List args = (List) MockConnection.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockConnection.methodArguments.put(methodName, args);
        }
        args.add(arguments);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see Connection#getAutoCommit()
     * @throws SQLException
     */
    public boolean getAutoCommit() throws SQLException {
        if (MockConnection.globalException != null) {
            if (MockConnection.globalException instanceof SQLException) {
                throw (SQLException) MockConnection.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockConnection.globalException);
            }
        }

        String methodName = "getAutoCommit";

        Throwable exception = (Throwable) MockConnection.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof SQLException) {
                throw (SQLException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        List args = (List) MockConnection.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockConnection.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return ((Boolean) MockConnection.methodResults.get(methodName)).booleanValue();

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see Connection#isClosed()
     * @throws SQLException
     */
    public boolean isClosed() throws SQLException {
        if (MockConnection.globalException != null) {
            if (MockConnection.globalException instanceof SQLException) {
                throw (SQLException) MockConnection.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockConnection.globalException);
            }
        }

        String methodName = "isClosed";

        Throwable exception = (Throwable) MockConnection.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof SQLException) {
                throw (SQLException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        List args = (List) MockConnection.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockConnection.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return ((Boolean) MockConnection.methodResults.get(methodName)).booleanValue();

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see Connection#isReadOnly()
     * @throws SQLException
     */
    public boolean isReadOnly() throws SQLException {
        if (MockConnection.globalException != null) {
            if (MockConnection.globalException instanceof SQLException) {
                throw (SQLException) MockConnection.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockConnection.globalException);
            }
        }

        String methodName = "isReadOnly";

        Throwable exception = (Throwable) MockConnection.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof SQLException) {
                throw (SQLException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        List args = (List) MockConnection.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockConnection.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return ((Boolean) MockConnection.methodResults.get(methodName)).booleanValue();

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see Connection#setHoldability(int)
     * @throws SQLException
     */
    public void setHoldability(int int0) throws SQLException {
        if (MockConnection.globalException != null) {
            if (MockConnection.globalException instanceof SQLException) {
                throw (SQLException) MockConnection.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockConnection.globalException);
            }
        }

        String methodName = "setHoldability_int";

        Throwable exception = (Throwable) MockConnection.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof SQLException) {
                throw (SQLException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", new Integer(int0));
        List args = (List) MockConnection.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockConnection.methodArguments.put(methodName, args);
        }
        args.add(arguments);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see Connection#setTransactionIsolation(int)
     * @throws SQLException
     */
    public void setTransactionIsolation(int int0) throws SQLException {
        if (MockConnection.globalException != null) {
            if (MockConnection.globalException instanceof SQLException) {
                throw (SQLException) MockConnection.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockConnection.globalException);
            }
        }

        String methodName = "setTransactionIsolation_int";

        Throwable exception = (Throwable) MockConnection.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof SQLException) {
                throw (SQLException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", new Integer(int0));
        List args = (List) MockConnection.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockConnection.methodArguments.put(methodName, args);
        }
        args.add(arguments);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see Connection#setAutoCommit(boolean)
     * @throws SQLException
     */
    public void setAutoCommit(boolean boolean0) throws SQLException {
        if (MockConnection.globalException != null) {
            if (MockConnection.globalException instanceof SQLException) {
                throw (SQLException) MockConnection.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockConnection.globalException);
            }
        }

        String methodName = "setAutoCommit_boolean";

        Throwable exception = (Throwable) MockConnection.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof SQLException) {
                throw (SQLException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", Boolean.valueOf(boolean0));
        List args = (List) MockConnection.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockConnection.methodArguments.put(methodName, args);
        }
        args.add(arguments);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see Connection#setReadOnly(boolean)
     * @throws SQLException
     */
    public void setReadOnly(boolean boolean0) throws SQLException {
        if (MockConnection.globalException != null) {
            if (MockConnection.globalException instanceof SQLException) {
                throw (SQLException) MockConnection.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockConnection.globalException);
            }
        }

        String methodName = "setReadOnly_boolean";

        Throwable exception = (Throwable) MockConnection.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof SQLException) {
                throw (SQLException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", Boolean.valueOf(boolean0));
        List args = (List) MockConnection.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockConnection.methodArguments.put(methodName, args);
        }
        args.add(arguments);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see Connection#getCatalog()
     * @throws SQLException
     */
    public String getCatalog() throws SQLException {
        if (MockConnection.globalException != null) {
            if (MockConnection.globalException instanceof SQLException) {
                throw (SQLException) MockConnection.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockConnection.globalException);
            }
        }

        String methodName = "getCatalog";

        Throwable exception = (Throwable) MockConnection.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof SQLException) {
                throw (SQLException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        List args = (List) MockConnection.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockConnection.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (String) MockConnection.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see Connection#setCatalog(String)
     * @throws SQLException
     */
    public void setCatalog(String string0) throws SQLException {
        if (MockConnection.globalException != null) {
            if (MockConnection.globalException instanceof SQLException) {
                throw (SQLException) MockConnection.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockConnection.globalException);
            }
        }

        String methodName = "setCatalog_String";

        Throwable exception = (Throwable) MockConnection.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof SQLException) {
                throw (SQLException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", string0);
        List args = (List) MockConnection.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockConnection.methodArguments.put(methodName, args);
        }
        args.add(arguments);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see Connection#getMetaData()
     * @throws SQLException
     */
    public DatabaseMetaData getMetaData() throws SQLException {
        if (MockConnection.globalException != null) {
            if (MockConnection.globalException instanceof SQLException) {
                throw (SQLException) MockConnection.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockConnection.globalException);
            }
        }

        String methodName = "getMetaData";

        Throwable exception = (Throwable) MockConnection.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof SQLException) {
                throw (SQLException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        List args = (List) MockConnection.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockConnection.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (DatabaseMetaData) MockConnection.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see Connection#getWarnings()
     * @throws SQLException
     */
    public SQLWarning getWarnings() throws SQLException {
        if (MockConnection.globalException != null) {
            if (MockConnection.globalException instanceof SQLException) {
                throw (SQLException) MockConnection.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockConnection.globalException);
            }
        }

        String methodName = "getWarnings";

        Throwable exception = (Throwable) MockConnection.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof SQLException) {
                throw (SQLException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        List args = (List) MockConnection.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockConnection.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (SQLWarning) MockConnection.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see Connection#setSavepoint()
     * @throws SQLException
     */
    public Savepoint setSavepoint() throws SQLException {
        if (MockConnection.globalException != null) {
            if (MockConnection.globalException instanceof SQLException) {
                throw (SQLException) MockConnection.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockConnection.globalException);
            }
        }

        String methodName = "setSavepoint";

        Throwable exception = (Throwable) MockConnection.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof SQLException) {
                throw (SQLException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        List args = (List) MockConnection.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockConnection.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (Savepoint) MockConnection.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see Connection#releaseSavepoint(Savepoint)
     * @throws SQLException
     */
    public void releaseSavepoint(Savepoint savepoint0) throws SQLException {
        if (MockConnection.globalException != null) {
            if (MockConnection.globalException instanceof SQLException) {
                throw (SQLException) MockConnection.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockConnection.globalException);
            }
        }

        String methodName = "releaseSavepoint_Savepoint";

        Throwable exception = (Throwable) MockConnection.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof SQLException) {
                throw (SQLException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", savepoint0);
        List args = (List) MockConnection.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockConnection.methodArguments.put(methodName, args);
        }
        args.add(arguments);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see Connection#rollback(Savepoint)
     * @throws SQLException
     */
    public void rollback(Savepoint savepoint0) throws SQLException {
        if (MockConnection.globalException != null) {
            if (MockConnection.globalException instanceof SQLException) {
                throw (SQLException) MockConnection.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockConnection.globalException);
            }
        }

        String methodName = "rollback_Savepoint";

        Throwable exception = (Throwable) MockConnection.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof SQLException) {
                throw (SQLException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", savepoint0);
        List args = (List) MockConnection.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockConnection.methodArguments.put(methodName, args);
        }
        args.add(arguments);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see Connection#createStatement()
     * @throws SQLException
     */
    public Statement createStatement() throws SQLException {
        if (MockConnection.globalException != null) {
            if (MockConnection.globalException instanceof SQLException) {
                throw (SQLException) MockConnection.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockConnection.globalException);
            }
        }

        String methodName = "createStatement";

        Throwable exception = (Throwable) MockConnection.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof SQLException) {
                throw (SQLException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        List args = (List) MockConnection.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockConnection.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (Statement) MockConnection.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see Connection#createStatement(int, int)
     * @throws SQLException
     */
    public Statement createStatement(int int0, int int1) throws SQLException {
        if (MockConnection.globalException != null) {
            if (MockConnection.globalException instanceof SQLException) {
                throw (SQLException) MockConnection.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockConnection.globalException);
            }
        }

        String methodName = "createStatement_int_int";

        Throwable exception = (Throwable) MockConnection.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof SQLException) {
                throw (SQLException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", new Integer(int0));
        arguments.put("2", new Integer(int1));
        List args = (List) MockConnection.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockConnection.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (Statement) MockConnection.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see Connection#createStatement(int, int, int)
     * @throws SQLException
     */
    public Statement createStatement(int int0, int int1, int int2) throws SQLException {
        if (MockConnection.globalException != null) {
            if (MockConnection.globalException instanceof SQLException) {
                throw (SQLException) MockConnection.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockConnection.globalException);
            }
        }

        String methodName = "createStatement_int_int_int";

        Throwable exception = (Throwable) MockConnection.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof SQLException) {
                throw (SQLException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", new Integer(int0));
        arguments.put("2", new Integer(int1));
        arguments.put("3", new Integer(int2));
        List args = (List) MockConnection.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockConnection.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (Statement) MockConnection.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see Connection#getTypeMap()
     * @throws SQLException
     */
    public Map getTypeMap() throws SQLException {
        if (MockConnection.globalException != null) {
            if (MockConnection.globalException instanceof SQLException) {
                throw (SQLException) MockConnection.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockConnection.globalException);
            }
        }

        String methodName = "getTypeMap";

        Throwable exception = (Throwable) MockConnection.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof SQLException) {
                throw (SQLException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        List args = (List) MockConnection.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockConnection.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (Map) MockConnection.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see Connection#setTypeMap(Map)
     * @throws SQLException
     */
    public void setTypeMap(Map map0) throws SQLException {
        if (MockConnection.globalException != null) {
            if (MockConnection.globalException instanceof SQLException) {
                throw (SQLException) MockConnection.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockConnection.globalException);
            }
        }

        String methodName = "setTypeMap_Map";

        Throwable exception = (Throwable) MockConnection.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof SQLException) {
                throw (SQLException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", map0);
        List args = (List) MockConnection.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockConnection.methodArguments.put(methodName, args);
        }
        args.add(arguments);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see Connection#nativeSQL(String)
     * @throws SQLException
     */
    public String nativeSQL(String string0) throws SQLException {
        if (MockConnection.globalException != null) {
            if (MockConnection.globalException instanceof SQLException) {
                throw (SQLException) MockConnection.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockConnection.globalException);
            }
        }

        String methodName = "nativeSQL_String";

        Throwable exception = (Throwable) MockConnection.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof SQLException) {
                throw (SQLException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", string0);
        List args = (List) MockConnection.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockConnection.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (String) MockConnection.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see Connection#prepareCall(String)
     * @throws SQLException
     */
    public CallableStatement prepareCall(String string0) throws SQLException {
        if (MockConnection.globalException != null) {
            if (MockConnection.globalException instanceof SQLException) {
                throw (SQLException) MockConnection.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockConnection.globalException);
            }
        }

        String methodName = "prepareCall_String";

        Throwable exception = (Throwable) MockConnection.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof SQLException) {
                throw (SQLException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", string0);
        List args = (List) MockConnection.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockConnection.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (CallableStatement) MockConnection.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see Connection#prepareCall(String, int, int)
     * @throws SQLException
     */
    public CallableStatement prepareCall(String string0, int int0, int int1) throws SQLException {
        if (MockConnection.globalException != null) {
            if (MockConnection.globalException instanceof SQLException) {
                throw (SQLException) MockConnection.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockConnection.globalException);
            }
        }

        String methodName = "prepareCall_String_int_int";

        Throwable exception = (Throwable) MockConnection.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof SQLException) {
                throw (SQLException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", string0);
        arguments.put("2", new Integer(int0));
        arguments.put("3", new Integer(int1));
        List args = (List) MockConnection.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockConnection.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (CallableStatement) MockConnection.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see Connection#prepareCall(String, int, int, int)
     * @throws SQLException
     */
    public CallableStatement prepareCall(String string0, int int0, int int1, int int2) throws SQLException {
        if (MockConnection.globalException != null) {
            if (MockConnection.globalException instanceof SQLException) {
                throw (SQLException) MockConnection.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockConnection.globalException);
            }
        }

        String methodName = "prepareCall_String_int_int_int";

        Throwable exception = (Throwable) MockConnection.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof SQLException) {
                throw (SQLException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", string0);
        arguments.put("2", new Integer(int0));
        arguments.put("3", new Integer(int1));
        arguments.put("4", new Integer(int2));
        List args = (List) MockConnection.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockConnection.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (CallableStatement) MockConnection.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see Connection#prepareStatement(String)
     * @throws SQLException
     */
    public PreparedStatement prepareStatement(String string0) throws SQLException {
        if (MockConnection.globalException != null) {
            if (MockConnection.globalException instanceof SQLException) {
                throw (SQLException) MockConnection.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockConnection.globalException);
            }
        }

        String methodName = "prepareStatement_String";

        Throwable exception = (Throwable) MockConnection.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof SQLException) {
                throw (SQLException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", string0);
        List args = (List) MockConnection.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockConnection.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (PreparedStatement) MockConnection.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see Connection#prepareStatement(String, int)
     * @throws SQLException
     */
    public PreparedStatement prepareStatement(String string0, int int0) throws SQLException {
        if (MockConnection.globalException != null) {
            if (MockConnection.globalException instanceof SQLException) {
                throw (SQLException) MockConnection.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockConnection.globalException);
            }
        }

        String methodName = "prepareStatement_String_int";

        Throwable exception = (Throwable) MockConnection.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof SQLException) {
                throw (SQLException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", string0);
        arguments.put("2", new Integer(int0));
        List args = (List) MockConnection.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockConnection.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (PreparedStatement) MockConnection.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see Connection#prepareStatement(String, int, int)
     * @throws SQLException
     */
    public PreparedStatement prepareStatement(String string0, int int0, int int1) throws SQLException {
        if (MockConnection.globalException != null) {
            if (MockConnection.globalException instanceof SQLException) {
                throw (SQLException) MockConnection.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockConnection.globalException);
            }
        }

        String methodName = "prepareStatement_String_int_int";

        Throwable exception = (Throwable) MockConnection.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof SQLException) {
                throw (SQLException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", string0);
        arguments.put("2", new Integer(int0));
        arguments.put("3", new Integer(int1));
        List args = (List) MockConnection.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockConnection.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (PreparedStatement) MockConnection.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see Connection#prepareStatement(String, int, int, int)
     * @throws SQLException
     */
    public PreparedStatement prepareStatement(String string0, int int0, int int1, int int2) throws SQLException {
        if (MockConnection.globalException != null) {
            if (MockConnection.globalException instanceof SQLException) {
                throw (SQLException) MockConnection.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockConnection.globalException);
            }
        }

        String methodName = "prepareStatement_String_int_int_int";

        Throwable exception = (Throwable) MockConnection.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof SQLException) {
                throw (SQLException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", string0);
        arguments.put("2", new Integer(int0));
        arguments.put("3", new Integer(int1));
        arguments.put("4", new Integer(int2));
        List args = (List) MockConnection.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockConnection.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (PreparedStatement) MockConnection.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see Connection#prepareStatement(String, int[])
     * @throws SQLException
     */
    public PreparedStatement prepareStatement(String string0, int[] intA0) throws SQLException {
        if (MockConnection.globalException != null) {
            if (MockConnection.globalException instanceof SQLException) {
                throw (SQLException) MockConnection.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockConnection.globalException);
            }
        }

        String methodName = "prepareStatement_String_int[]";

        Throwable exception = (Throwable) MockConnection.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof SQLException) {
                throw (SQLException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", string0);
        arguments.put("2", intA0);
        List args = (List) MockConnection.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockConnection.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (PreparedStatement) MockConnection.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see Connection#setSavepoint(String)
     * @throws SQLException
     */
    public Savepoint setSavepoint(String string0) throws SQLException {
        if (MockConnection.globalException != null) {
            if (MockConnection.globalException instanceof SQLException) {
                throw (SQLException) MockConnection.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockConnection.globalException);
            }
        }

        String methodName = "setSavepoint_String";

        Throwable exception = (Throwable) MockConnection.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof SQLException) {
                throw (SQLException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", string0);
        List args = (List) MockConnection.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockConnection.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (Savepoint) MockConnection.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see Connection#prepareStatement(String, String[])
     * @throws SQLException
     */
    public PreparedStatement prepareStatement(String string0, String[] stringA0) throws SQLException {
        if (MockConnection.globalException != null) {
            if (MockConnection.globalException instanceof SQLException) {
                throw (SQLException) MockConnection.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockConnection.globalException);
            }
        }

        String methodName = "prepareStatement_String_String[]";

        Throwable exception = (Throwable) MockConnection.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof SQLException) {
                throw (SQLException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", string0);
        arguments.put("2", stringA0);
        List args = (List) MockConnection.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockConnection.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (PreparedStatement) MockConnection.methodResults.get(methodName);

    }

    /**
     * <p>Sets the result to be returned by the specified method.</p>
     *
     * @param methodSignature a <code>String</code> uniquelly distinguishing the target method among other methods
     *        declared by the implemented interface/class.
     * @param result an <code>Object</code> representing the result to be returned by specified method.
     */
    public static void setMethodResult(String methodSignature, Object result) {
        MockConnection.methodResults.put(methodSignature, result);
    }

    /**
     * <p>Gets the value of the specified argument which has been passed to the specified method by the caller.</p>
     *
     * @param  methodSignature a <code>String</code> uniquelly distinguishing the target method among other methods
     * @param  argumentName a <code>String</code> providing the name of the argument to get the value for.
     * @return an <code>Object</code> (including <code>null</code>) providing the value of the specified argument
     *         which has been supplied by the caller of the specified method.
     * @throws IllegalArgumentException if the specified argument does not exist.
     */
    public static Object getMethodArgument(String methodSignature, String argumentName) {
        Map arguments = (Map) MockConnection.methodArguments.get(methodSignature);
        if (!arguments.containsKey(argumentName)) {
            throw new IllegalArgumentException("The argument name " + argumentName + " is unknown.");
        }
        return arguments.get(argumentName);
    }

    /**
     * <pChecks if the specified method has been called during the test by the caller.</p>
     *
     * @param  methodSignature a <code>String</code> uniquelly distinguishing the target method among other methods
     * @return <code>true</code> if specified method was called; <code>false</code> otherwise.
     */
    public static boolean wasMethodCalled(String methodSignature) {
        return methodArguments.containsKey(methodSignature);
    }

    /**
     * <p>Gets the values of the argumenta which have been passed to the specified method by the caller.</p>
     *
     * @param  methodSignature a <code>String</code> uniquelly distinguishing the target method among other methods
     * @return a <code>List</code> of <code>Map</code> providing the values of the arguments on each call.
     *         which has been supplied by the caller of the specified method.
     */
    public static List getMethodArguments(String methodSignature) {
        return (List) MockConnection.methodArguments.get(methodSignature);
    }

    /**
     * <p>Sets the exception to be thrown when the specified method is called.</p>
     *
     * @param methodSignature a <code>String</code> uniquelly distinguishing the target method among other methods
     * @param exception a <code>Throwable</code> representing the exception to be thrown when the specified method is
     *        called. If this argument is <code>null</code> then no exception will be thrown.
     */
    public static void throwException(String methodSignature, Throwable exception) {
        if (exception != null) {
            MockConnection.throwExceptions.put(methodSignature, exception);
        } else {
            MockConnection.throwExceptions.remove(methodSignature);
        }
    }

    /**
     * <p>Sets the exception to be thrown when the specified method is called.</p>
     *
     * @param exception a <code>Throwable</code> representing the exception to be thrown whenever any method is
     *        called. If this argument is <code>null</code> then no exception will be thrown.
     */
    public static void throwGlobalException(Throwable exception) {
        MockConnection.globalException = exception;
    }

    /**
     * <p>Releases the state of <code>MockConnection</code> so all collected method arguments,
     * configured method results and exceptions are lost.</p>
     */
    public static void releaseState() {
        MockConnection.methodArguments.clear();
        MockConnection.methodResults.clear();
        MockConnection.throwExceptions.clear();
        MockConnection.globalException = null;
    }

    /**
     * <p>Initializes the initial state for all created instances of <code>MockConnection</code> class.</p>
     */
    public static void init() {
    }

    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    public Blob createBlob() throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    public Clob createClob() throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    public Properties getClientInfo() throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    public String getClientInfo(String name) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean isValid(int timeout) throws SQLException {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        // TODO Auto-generated method stub
        return false;
    }

    public <T> T unwrap(Class<T> iface) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

}
