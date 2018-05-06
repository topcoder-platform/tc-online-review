/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.cronos.onlinereview.phases.failuretests.mock;

import com.cronos.onlinereview.external.UserRetrieval;
import com.cronos.onlinereview.external.RetrievalException;
import com.cronos.onlinereview.external.ExternalUser;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

/**
 * <p>A mock implementation of {@link UserRetrieval} class to be used for testing.
 * Overrides the protected methods declared by a super-class. The overridden methods are declared with package private access
 * so only the test cases could invoke them. The overridden methods simply call the corresponding method of a super-class.
 *
 * @author  isv
 * @version 1.0
 */
public class MockUserRetrieval implements UserRetrieval {

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
     * <p>Constructs new <code>MockUserRetrieval</code> instance.</p>
     */
    public MockUserRetrieval() {
    }

    /**
     * <p>Constructs new <code>MockUserRetrieval</code> instance.</p>
     */
    public MockUserRetrieval(String namespace) {
    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see UserRetrieval#retrieveUser(long)
     * @throws RetrievalException
     */
    public ExternalUser retrieveUser(long long0) throws RetrievalException {
        if (MockUserRetrieval.globalException != null) {
            if (MockUserRetrieval.globalException instanceof RetrievalException) {
                throw (RetrievalException) MockUserRetrieval.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockUserRetrieval.globalException);
            }
        }

        String methodName = "retrieveUser_long";

        Throwable exception = (Throwable) MockUserRetrieval.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof RetrievalException) {
                throw (RetrievalException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", new Long(long0));
        List args = (List) MockUserRetrieval.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockUserRetrieval.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (ExternalUser) MockUserRetrieval.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see UserRetrieval#retrieveUser(String)
     * @throws RetrievalException
     */
    public ExternalUser retrieveUser(String string0) throws RetrievalException {
        if (MockUserRetrieval.globalException != null) {
            if (MockUserRetrieval.globalException instanceof RetrievalException) {
                throw (RetrievalException) MockUserRetrieval.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockUserRetrieval.globalException);
            }
        }

        String methodName = "retrieveUser_String";

        Throwable exception = (Throwable) MockUserRetrieval.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof RetrievalException) {
                throw (RetrievalException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", string0);
        List args = (List) MockUserRetrieval.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockUserRetrieval.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (ExternalUser) MockUserRetrieval.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see UserRetrieval#retrieveUsers(long[])
     * @throws RetrievalException
     */
    public ExternalUser[] retrieveUsers(long[] longA0) throws RetrievalException {
        if (MockUserRetrieval.globalException != null) {
            if (MockUserRetrieval.globalException instanceof RetrievalException) {
                throw (RetrievalException) MockUserRetrieval.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockUserRetrieval.globalException);
            }
        }

        String methodName = "retrieveUsers_long[]";

        Throwable exception = (Throwable) MockUserRetrieval.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof RetrievalException) {
                throw (RetrievalException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", longA0);
        List args = (List) MockUserRetrieval.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockUserRetrieval.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (ExternalUser[]) MockUserRetrieval.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see UserRetrieval#retrieveUsers(String[])
     * @throws RetrievalException
     */
    public ExternalUser[] retrieveUsers(String[] stringA0) throws RetrievalException {
        if (MockUserRetrieval.globalException != null) {
            if (MockUserRetrieval.globalException instanceof RetrievalException) {
                throw (RetrievalException) MockUserRetrieval.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockUserRetrieval.globalException);
            }
        }

        String methodName = "retrieveUsers_String[]";

        Throwable exception = (Throwable) MockUserRetrieval.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof RetrievalException) {
                throw (RetrievalException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", stringA0);
        List args = (List) MockUserRetrieval.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockUserRetrieval.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (ExternalUser[]) MockUserRetrieval.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see UserRetrieval#retrieveUsersIgnoreCase(String[])
     * @throws RetrievalException
     */
    public ExternalUser[] retrieveUsersIgnoreCase(String[] stringA0) throws RetrievalException {
        if (MockUserRetrieval.globalException != null) {
            if (MockUserRetrieval.globalException instanceof RetrievalException) {
                throw (RetrievalException) MockUserRetrieval.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockUserRetrieval.globalException);
            }
        }

        String methodName = "retrieveUsersIgnoreCase_String[]";

        Throwable exception = (Throwable) MockUserRetrieval.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof RetrievalException) {
                throw (RetrievalException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", stringA0);
        List args = (List) MockUserRetrieval.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockUserRetrieval.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (ExternalUser[]) MockUserRetrieval.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see UserRetrieval#retrieveUsersByName(String, String)
     * @throws RetrievalException
     */
    public ExternalUser[] retrieveUsersByName(String string0, String string1) throws RetrievalException {
        if (MockUserRetrieval.globalException != null) {
            if (MockUserRetrieval.globalException instanceof RetrievalException) {
                throw (RetrievalException) MockUserRetrieval.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockUserRetrieval.globalException);
            }
        }

        String methodName = "retrieveUsersByName_String_String";

        Throwable exception = (Throwable) MockUserRetrieval.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof RetrievalException) {
                throw (RetrievalException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", string0);
        arguments.put("2", string1);
        List args = (List) MockUserRetrieval.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockUserRetrieval.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (ExternalUser[]) MockUserRetrieval.methodResults.get(methodName);

    }

    /**
     * <p>Sets the result to be returned by the specified method.</p>
     *
     * @param methodSignature a <code>String</code> uniquelly distinguishing the target method among other methods
     *        declared by the implemented interface/class.
     * @param result an <code>Object</code> representing the result to be returned by specified method.
     */
    public static void setMethodResult(String methodSignature, Object result) {
        MockUserRetrieval.methodResults.put(methodSignature, result);
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
        Map arguments = (Map) MockUserRetrieval.methodArguments.get(methodSignature);
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
        return (List) MockUserRetrieval.methodArguments.get(methodSignature);
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
            MockUserRetrieval.throwExceptions.put(methodSignature, exception);
        } else {
            MockUserRetrieval.throwExceptions.remove(methodSignature);
        }
    }

    /**
     * <p>Sets the exception to be thrown when the specified method is called.</p>
     *
     * @param exception a <code>Throwable</code> representing the exception to be thrown whenever any method is
     *        called. If this argument is <code>null</code> then no exception will be thrown.
     */
    public static void throwGlobalException(Throwable exception) {
        MockUserRetrieval.globalException = exception;
    }

    /**
     * <p>Releases the state of <code>MockUserRetrieval</code> so all collected method arguments,
     * configured method results and exceptions are lost.</p>
     */
    public static void releaseState() {
        MockUserRetrieval.methodArguments.clear();
        MockUserRetrieval.methodResults.clear();
        MockUserRetrieval.throwExceptions.clear();
        MockUserRetrieval.globalException = null;
    }

    /**
     * <p>Initializes the initial state for all created instances of <code>MockUserRetrieval</code> class.</p>
     */
    public static void init() {
    }

}
