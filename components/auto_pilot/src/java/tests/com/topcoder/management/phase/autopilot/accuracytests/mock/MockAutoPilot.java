/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.management.phase.autopilot.accuracytests.mock;

import com.topcoder.management.phase.autopilot.AutoPilot;
import com.topcoder.management.phase.autopilot.AutoPilotResult;
import com.topcoder.management.phase.autopilot.AutoPilotSource;
import com.topcoder.management.phase.autopilot.AutoPilotSourceException;
import com.topcoder.management.phase.autopilot.ConfigurationException;
import com.topcoder.management.phase.autopilot.PhaseOperationException;
import com.topcoder.management.phase.autopilot.ProjectPilot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>A mock implementation of {@link AutoPilot} class to be used for testing. Overrides the protected methods declared
 * by a super-class. The overridden methods are declared with package private access so only the test cases could invoke
 * them. The overridden methods simply call the corresponding method of a super-class.
 *
 * @author isv
 * @version 1.0
 */
public class MockAutoPilot extends AutoPilot {

    /**
     * <p>A <code>Map</code> mapping the <code>String</code> method signatures to <code>Map</code>s mapping the <code>
     * String</code> names of the arguments to <code>Object</code>s representing the values of  arguments which have
     * been provided by the caller of the method.</p>
     */
    private static Map methodArguments = new HashMap();

    /**
     * <p>A <code>Map</code> mapping the <code>String</code> method signatures to <code>Exception</code>s to be thrown
     * by methods.</p>
     */
    private static Map throwExceptions = new HashMap();

    /**
     * <p>A <code>Map</code> mapping the <code>String</code> method signatures to <code>Object</code>s to be returned by
     * methods.</p>
     */
    private static Map methodResults = new HashMap();

    /**
     * <p>A <code>Throwable</code> representing the exception to be thrown from any method of the mock class.</p>
     */
    private static Throwable globalException = null;

    /**
     * <p>Constructs new <code>MockAutoPilot</code> instance. Nothing special occurs here.</p>
     *
     * @throws ConfigurationException
     */
    public MockAutoPilot() throws ConfigurationException {
        super();
        MockAutoPilot.init();
    }

    /**
     * <p>Constructs new <code>MockAutoPilot</code> instance. Nothing special occurs here.</p>
     *
     * @throws ConfigurationException
     */
    public MockAutoPilot(String string0, String string1, String string2) throws ConfigurationException {
        super(string0, string1, string2);
        MockAutoPilot.init();
    }

    /**
     * <p>Constructs new <code>MockAutoPilot</code> instance. Nothing special occurs here.</p>
     */
    public MockAutoPilot(AutoPilotSource autoPilotSource0, ProjectPilot projectPilot0) {
        super(autoPilotSource0, projectPilot0);
        MockAutoPilot.init();
    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through {@link
     * #setMethodResult(String, Object)} method.</p>
     *
     * @see AutoPilot#getAutoPilotSource()
     */
    public AutoPilotSource getAutoPilotSource() {
        if (MockAutoPilot.globalException != null) {
            throw new RuntimeException("The test may not be configured properly", MockAutoPilot.globalException);
        }

        String methodName = "getAutoPilotSource";

        Throwable exception = (Throwable) MockAutoPilot.throwExceptions.get(methodName);
        if (exception != null) {
            throw new RuntimeException("The test may not be configured properly", exception);
        }

        HashMap arguments = new HashMap();
        List args = (List) MockAutoPilot.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockAutoPilot.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (AutoPilotSource) MockAutoPilot.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through {@link
     * #setMethodResult(String, Object)} method.</p>
     *
     * @see AutoPilot#getProjectPilot()
     */
    public ProjectPilot getProjectPilot() {
        if (MockAutoPilot.globalException != null) {
            throw new RuntimeException("The test may not be configured properly", MockAutoPilot.globalException);
        }

        String methodName = "getProjectPilot";

        Throwable exception = (Throwable) MockAutoPilot.throwExceptions.get(methodName);
        if (exception != null) {
            throw new RuntimeException("The test may not be configured properly", exception);
        }

        HashMap arguments = new HashMap();
        List args = (List) MockAutoPilot.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockAutoPilot.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (ProjectPilot) MockAutoPilot.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through {@link
     * #setMethodResult(String, Object)} method.</p>
     *
     * @throws AutoPilotSourceException
     * @throws PhaseOperationException
     * @see AutoPilot#advanceProjects(String)
     */
    public AutoPilotResult[] advanceProjects(String string0) throws AutoPilotSourceException, PhaseOperationException {
        if (MockAutoPilot.globalException != null) {
            if (MockAutoPilot.globalException instanceof AutoPilotSourceException) {
                throw (AutoPilotSourceException) MockAutoPilot.globalException;
            } else if (MockAutoPilot.globalException instanceof PhaseOperationException) {
                throw (PhaseOperationException) MockAutoPilot.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockAutoPilot.globalException);
            }
        }

        String methodName = "advanceProjects_String";

        Throwable exception = (Throwable) MockAutoPilot.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof AutoPilotSourceException) {
                throw (AutoPilotSourceException) exception;
            } else if (exception instanceof PhaseOperationException) {
                throw (PhaseOperationException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", string0);
        List args = (List) MockAutoPilot.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockAutoPilot.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (AutoPilotResult[]) MockAutoPilot.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through {@link
     * #setMethodResult(String, Object)} method.</p>
     *
     * @throws PhaseOperationException
     * @see AutoPilot#advanceProjects(long[], String)
     */
    public AutoPilotResult[] advanceProjects(long[] longA0, String string0) throws PhaseOperationException {
        if (MockAutoPilot.globalException != null) {
            if (MockAutoPilot.globalException instanceof PhaseOperationException) {
                throw (PhaseOperationException) MockAutoPilot.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockAutoPilot.globalException);
            }
        }

        String methodName = "advanceProjects_long[]_String";

        Throwable exception = (Throwable) MockAutoPilot.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof PhaseOperationException) {
                throw (PhaseOperationException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", longA0);
        arguments.put("2", string0);
        List args = (List) MockAutoPilot.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockAutoPilot.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (AutoPilotResult[]) MockAutoPilot.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through {@link
     * #setMethodResult(String, Object)} method.</p>
     *
     * @throws PhaseOperationException
     * @see AutoPilot#advanceProject(long, String)
     */
    public AutoPilotResult advanceProject(long long0, String string0) throws PhaseOperationException {
        if (MockAutoPilot.globalException != null) {
            if (MockAutoPilot.globalException instanceof PhaseOperationException) {
                throw (PhaseOperationException) MockAutoPilot.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockAutoPilot.globalException);
            }
        }

        String methodName = "advanceProject_long_String";

        Throwable exception = (Throwable) MockAutoPilot.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof PhaseOperationException) {
                throw (PhaseOperationException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", new Long(long0));
        arguments.put("2", string0);
        List args = (List) MockAutoPilot.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockAutoPilot.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (AutoPilotResult) MockAutoPilot.methodResults.get(methodName);

    }

    /**
     * <p>Sets the result to be returned by the specified method.</p>
     *
     * @param methodSignature a <code>String</code> uniquelly distinguishing the target method among other methods
     * declared by the implemented interface/class.
     * @param result an <code>Object</code> representing the result to be returned by specified method.
     */
    public static void setMethodResult(String methodSignature, Object result) {
        MockAutoPilot.methodResults.put(methodSignature, result);
    }

    /**
     * <p>Gets the value of the specified argument which has been passed to the specified method by the caller.</p>
     *
     * @param methodSignature a <code>String</code> uniquelly distinguishing the target method among other methods
     * @param argumentName a <code>String</code> providing the name of the argument to get the value for.
     * @return an <code>Object</code> (including <code>null</code>) providing the value of the specified argument which
     *         has been supplied by the caller of the specified method.
     * @throws IllegalArgumentException if the specified argument does not exist.
     */
    public static Object getMethodArgument(String methodSignature, String argumentName) {
        Map arguments = (Map) MockAutoPilot.methodArguments.get(methodSignature);
        if (!arguments.containsKey(argumentName)) {
            throw new IllegalArgumentException("The argument name " + argumentName + " is unknown.");
        }
        return arguments.get(argumentName);
    }

    /**
     * <pChecks if the specified method has been called during the test by the caller.</p>
     *
     * @param methodSignature a <code>String</code> uniquelly distinguishing the target method among other methods
     * @return <code>true</code> if specified method was called; <code>false</code> otherwise.
     */
    public static boolean wasMethodCalled(String methodSignature) {
        return methodArguments.containsKey(methodSignature);
    }

    /**
     * <p>Gets the values of the argumenta which have been passed to the specified method by the caller.</p>
     *
     * @param methodSignature a <code>String</code> uniquelly distinguishing the target method among other methods
     * @return a <code>List</code> of <code>Map</code> providing the values of the arguments on each call. which has
     *         been supplied by the caller of the specified method.
     */
    public static List getMethodArguments(String methodSignature) {
        return (List) MockAutoPilot.methodArguments.get(methodSignature);
    }

    /**
     * <p>Sets the exception to be thrown when the specified method is called.</p>
     *
     * @param methodSignature a <code>String</code> uniquelly distinguishing the target method among other methods
     * @param exception a <code>Throwable</code> representing the exception to be thrown when the specified method is
     * called. If this argument is <code>null</code> then no exception will be thrown.
     */
    public static void throwException(String methodSignature, Throwable exception) {
        if (exception != null) {
            MockAutoPilot.throwExceptions.put(methodSignature, exception);
        } else {
            MockAutoPilot.throwExceptions.remove(methodSignature);
        }
    }

    /**
     * <p>Sets the exception to be thrown when the specified method is called.</p>
     *
     * @param exception a <code>Throwable</code> representing the exception to be thrown whenever any method is called.
     * If this argument is <code>null</code> then no exception will be thrown.
     */
    public static void throwGlobalException(Throwable exception) {
        MockAutoPilot.globalException = exception;
    }

    /**
     * <p>Releases the state of <code>MockAutoPilot</code> so all collected method arguments, configured method results
     * and exceptions are lost.</p>
     */
    public static void releaseState() {
        MockAutoPilot.methodArguments.clear();
        MockAutoPilot.methodResults.clear();
        MockAutoPilot.throwExceptions.clear();
        MockAutoPilot.globalException = null;
    }

    /**
     * <p>Initializes the initial state for all created instances of <code>MockAutoPilot</code> class.</p>
     */
    public static void init() {
    }

    /**
     * <p>Checks the equality of this object to specified one.</p>
     *
     * @param obj the reference object with which to compare.
     * @return <code>true</code> if this object is the same as the obj argument; <code>false</code> otherwise.
     */
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        return this.getClass().getName().equals(obj.getClass().getName());
    }
}
