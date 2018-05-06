/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.cronos.onlinereview.phases.failuretests.mock;

import com.cronos.onlinereview.autoscreening.management.ScreeningManager;
import com.cronos.onlinereview.autoscreening.management.PersistenceException;
import com.cronos.onlinereview.autoscreening.management.ScreeningTaskAlreadyExistsException;
import com.cronos.onlinereview.autoscreening.management.ScreeningTaskDoesNotExistException;
import com.cronos.onlinereview.autoscreening.management.ScreeningTask;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>A mock implementation of {@link ScreeningManager} class to be used for testing.
 * Overrides the protected methods declared by a super-class. The overridden methods are declared with package private access
 * so only the test cases could invoke them. The overridden methods simply call the corresponding method of a super-class.
 *
 * @author  isv
 * @version 1.0
 */
public class MockScreeningManager implements ScreeningManager {

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
     * <p>Constructs new <code>MockScreeningManager</code> instance.</p>
     */
    public MockScreeningManager() {
    }

    /**
     * <p>Constructs new <code>MockScreeningManager</code> instance.</p>
     */
    public MockScreeningManager(String namespace) {
    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see ScreeningManager#initiateScreening(long, String)
     * @throws PersistenceException
     * @throws ScreeningTaskAlreadyExistsException
     */
    public void initiateScreening(long long0, String string0) throws PersistenceException, ScreeningTaskAlreadyExistsException {
        if (MockScreeningManager.globalException != null) {
            if (MockScreeningManager.globalException instanceof PersistenceException) {
                throw (PersistenceException) MockScreeningManager.globalException;
            } else if (MockScreeningManager.globalException instanceof ScreeningTaskAlreadyExistsException) {
                throw (ScreeningTaskAlreadyExistsException) MockScreeningManager.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockScreeningManager.globalException);
            }
        }

        String methodName = "initiateScreening_long_String";

        Throwable exception = (Throwable) MockScreeningManager.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof PersistenceException) {
                throw (PersistenceException) exception;
            } else if (exception instanceof ScreeningTaskAlreadyExistsException) {
                throw (ScreeningTaskAlreadyExistsException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", new Long(long0));
        arguments.put("2", string0);
        List args = (List) MockScreeningManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockScreeningManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see ScreeningManager#getScreeningDetails(long)
     * @throws PersistenceException
     * @throws ScreeningTaskDoesNotExistException
     */
    public ScreeningTask getScreeningDetails(long long0) throws PersistenceException, ScreeningTaskDoesNotExistException {
        if (MockScreeningManager.globalException != null) {
            if (MockScreeningManager.globalException instanceof PersistenceException) {
                throw (PersistenceException) MockScreeningManager.globalException;
            } else if (MockScreeningManager.globalException instanceof ScreeningTaskDoesNotExistException) {
                throw (ScreeningTaskDoesNotExistException) MockScreeningManager.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockScreeningManager.globalException);
            }
        }

        String methodName = "getScreeningDetails_long";

        Throwable exception = (Throwable) MockScreeningManager.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof PersistenceException) {
                throw (PersistenceException) exception;
            } else if (exception instanceof ScreeningTaskDoesNotExistException) {
                throw (ScreeningTaskDoesNotExistException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", new Long(long0));
        List args = (List) MockScreeningManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockScreeningManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (ScreeningTask) MockScreeningManager.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see ScreeningManager#getScreeningTasks(long[])
     * @throws PersistenceException
     * @throws ScreeningTaskDoesNotExistException
     */
    public ScreeningTask[] getScreeningTasks(long[] longA0) throws PersistenceException, ScreeningTaskDoesNotExistException {
        if (MockScreeningManager.globalException != null) {
            if (MockScreeningManager.globalException instanceof PersistenceException) {
                throw (PersistenceException) MockScreeningManager.globalException;
            } else if (MockScreeningManager.globalException instanceof ScreeningTaskDoesNotExistException) {
                throw (ScreeningTaskDoesNotExistException) MockScreeningManager.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockScreeningManager.globalException);
            }
        }

        String methodName = "getScreeningTasks_long[]";

        Throwable exception = (Throwable) MockScreeningManager.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof PersistenceException) {
                throw (PersistenceException) exception;
            } else if (exception instanceof ScreeningTaskDoesNotExistException) {
                throw (ScreeningTaskDoesNotExistException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", longA0);
        List args = (List) MockScreeningManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockScreeningManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (ScreeningTask[]) MockScreeningManager.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see ScreeningManager#getScreeningTasks(long[], boolean)
     * @throws PersistenceException
     * @throws ScreeningTaskDoesNotExistException
     */
    public ScreeningTask[] getScreeningTasks(long[] longA0, boolean boolean0) throws PersistenceException, ScreeningTaskDoesNotExistException {
        if (MockScreeningManager.globalException != null) {
            if (MockScreeningManager.globalException instanceof PersistenceException) {
                throw (PersistenceException) MockScreeningManager.globalException;
            } else if (MockScreeningManager.globalException instanceof ScreeningTaskDoesNotExistException) {
                throw (ScreeningTaskDoesNotExistException) MockScreeningManager.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockScreeningManager.globalException);
            }
        }

        String methodName = "getScreeningTasks_long[]_boolean";

        Throwable exception = (Throwable) MockScreeningManager.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof PersistenceException) {
                throw (PersistenceException) exception;
            } else if (exception instanceof ScreeningTaskDoesNotExistException) {
                throw (ScreeningTaskDoesNotExistException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", longA0);
        arguments.put("2", Boolean.valueOf(boolean0));
        List args = (List) MockScreeningManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockScreeningManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (ScreeningTask[]) MockScreeningManager.methodResults.get(methodName);

    }

    /**
     * <p>Sets the result to be returned by the specified method.</p>
     *
     * @param methodSignature a <code>String</code> uniquelly distinguishing the target method among other methods
     *        declared by the implemented interface/class.
     * @param result an <code>Object</code> representing the result to be returned by specified method.
     */
    public static void setMethodResult(String methodSignature, Object result) {
        MockScreeningManager.methodResults.put(methodSignature, result);
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
        Map arguments = (Map) MockScreeningManager.methodArguments.get(methodSignature);
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
        return (List) MockScreeningManager.methodArguments.get(methodSignature);
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
            MockScreeningManager.throwExceptions.put(methodSignature, exception);
        } else {
            MockScreeningManager.throwExceptions.remove(methodSignature);
        }
    }

    /**
     * <p>Sets the exception to be thrown when the specified method is called.</p>
     *
     * @param exception a <code>Throwable</code> representing the exception to be thrown whenever any method is
     *        called. If this argument is <code>null</code> then no exception will be thrown.
     */
    public static void throwGlobalException(Throwable exception) {
        MockScreeningManager.globalException = exception;
    }

    /**
     * <p>Releases the state of <code>MockScreeningManager</code> so all collected method arguments,
     * configured method results and exceptions are lost.</p>
     */
    public static void releaseState() {
        MockScreeningManager.methodArguments.clear();
        MockScreeningManager.methodResults.clear();
        MockScreeningManager.throwExceptions.clear();
        MockScreeningManager.globalException = null;
    }

    /**
     * <p>Initializes the initial state for all created instances of <code>MockScreeningManager</code> class.</p>
     */
    public static void init() {
    }

}
