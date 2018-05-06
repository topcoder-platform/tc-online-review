/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */

package com.cronos.onlinereview.ajax.failuretests.mock;

import com.topcoder.management.phase.HandlerRegistryInfo;
import com.topcoder.management.phase.PhaseHandler;
import com.topcoder.management.phase.PhaseManager;
import com.topcoder.management.phase.PhaseOperationEnum;
import com.topcoder.management.phase.PhaseValidator;
import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.PhaseStatus;
import com.topcoder.project.phases.PhaseType;
import com.topcoder.project.phases.Project;
import com.cronos.onlinereview.ajax.failuretests.TestDataFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>A mock implementation of {@link PhaseManager} class to be used for testing. Overrides the protected methods
 * declared by a super-class. The overridden methods are declared with package private access so only the test cases
 * could invoke them. The overridden methods simply call the corresponding method of a super-class.
 *
 * @author isv
 * @version 1.0
 */
public class MockPhaseManager implements PhaseManager {

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
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through {@link
     * #setMethodResult(String, Object)} method.</p>
     *
     * @see PhaseManager#updatePhases(Project, String)
     */
    public void updatePhases(Project project0, String string0) {
        if (MockPhaseManager.globalException != null) {
            throw new RuntimeException("The test may not be configured properly", MockPhaseManager.globalException);
        }

        String methodName = "updatePhases_Project_String";

        Throwable exception = (Throwable) MockPhaseManager.throwExceptions.get(methodName);
        if (exception != null) {
            throw new RuntimeException("The test may not be configured properly", exception);
        }

        HashMap arguments = new HashMap();
        arguments.put("1", project0);
        arguments.put("2", string0);
        List args = (List) MockPhaseManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            args = new ArrayList();
            MockPhaseManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through {@link
     * #setMethodResult(String, Object)} method.</p>
     *
     * @see PhaseManager#getPhases(long)
     */
    public Project getPhases(long long0) {
        if (MockPhaseManager.globalException != null) {
            throw new RuntimeException("The test may not be configured properly", MockPhaseManager.globalException);
        }

        String methodName = "getPhases_long";

        Throwable exception = (Throwable) MockPhaseManager.throwExceptions.get(methodName);
        if (exception != null) {
            throw new RuntimeException("The test may not be configured properly", exception);
        }

        HashMap arguments = new HashMap();
        arguments.put("1", new Long(long0));
        List args = (List) MockPhaseManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            args = new ArrayList();
            MockPhaseManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (Project) MockPhaseManager.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through {@link
     * #setMethodResult(String, Object)} method.</p>
     *
     * @see PhaseManager#getPhases(long[])
     */
    public Project[] getPhases(long[] longA0) {
        if (MockPhaseManager.globalException != null) {
            throw new RuntimeException("The test may not be configured properly", MockPhaseManager.globalException);
        }

        String methodName = "getPhases_long[]";

        Throwable exception = (Throwable) MockPhaseManager.throwExceptions.get(methodName);
        if (exception != null) {
            throw new RuntimeException("The test may not be configured properly", exception);
        }

        HashMap arguments = new HashMap();
        arguments.put("1", longA0);
        List args = (List) MockPhaseManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            args = new ArrayList();
            MockPhaseManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (Project[]) MockPhaseManager.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through {@link
     * #setMethodResult(String, Object)} method.</p>
     *
     * @see PhaseManager#getAllPhaseTypes()
     */
    public PhaseType[] getAllPhaseTypes() {
        if (MockPhaseManager.globalException != null) {
            throw new RuntimeException("The test may not be configured properly", MockPhaseManager.globalException);
        }

        String methodName = "getAllPhaseTypes";

        Throwable exception = (Throwable) MockPhaseManager.throwExceptions.get(methodName);
        if (exception != null) {
            throw new RuntimeException("The test may not be configured properly", exception);
        }

        HashMap arguments = new HashMap();
        List args = (List) MockPhaseManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            args = new ArrayList();
            MockPhaseManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (PhaseType[]) MockPhaseManager.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through {@link
     * #setMethodResult(String, Object)} method.</p>
     *
     * @see PhaseManager#getAllPhaseStatuses()
     */
    public PhaseStatus[] getAllPhaseStatuses() {
        if (MockPhaseManager.globalException != null) {
            throw new RuntimeException("The test may not be configured properly", MockPhaseManager.globalException);
        }

        String methodName = "getAllPhaseStatuses";

        Throwable exception = (Throwable) MockPhaseManager.throwExceptions.get(methodName);
        if (exception != null) {
            throw new RuntimeException("The test may not be configured properly", exception);
        }

        HashMap arguments = new HashMap();
        List args = (List) MockPhaseManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            args = new ArrayList();
            MockPhaseManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (PhaseStatus[]) MockPhaseManager.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through {@link
     * #setMethodResult(String, Object)} method.</p>
     *
     * @see PhaseManager#canStart(Phase)
     */
    public boolean canStart(Phase phase0) {
        if (MockPhaseManager.globalException != null) {
            throw new RuntimeException("The test may not be configured properly", MockPhaseManager.globalException);
        }

        String methodName = "canStart_Phase";

        Throwable exception = (Throwable) MockPhaseManager.throwExceptions.get(methodName);
        if (exception != null) {
            throw new RuntimeException("The test may not be configured properly", exception);
        }

        HashMap arguments = new HashMap();
        arguments.put("1", phase0);
        List args = (List) MockPhaseManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            args = new ArrayList();
            MockPhaseManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return ((Boolean) MockPhaseManager.methodResults.get(methodName)).booleanValue();

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through {@link
     * #setMethodResult(String, Object)} method.</p>
     *
     * @see PhaseManager#start(Phase, String)
     */
    public void start(Phase phase0, String string0) {
        if (MockPhaseManager.globalException != null) {
            throw new RuntimeException("The test may not be configured properly", MockPhaseManager.globalException);
        }

        String methodName = "start_Phase_String";

        Throwable exception = (Throwable) MockPhaseManager.throwExceptions.get(methodName);
        if (exception != null) {
            throw new RuntimeException("The test may not be configured properly", exception);
        }

        HashMap arguments = new HashMap();
        arguments.put("1", phase0);
        arguments.put("2", string0);
        List args = (List) MockPhaseManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            args = new ArrayList();
            MockPhaseManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through {@link
     * #setMethodResult(String, Object)} method.</p>
     *
     * @see PhaseManager#canEnd(Phase)
     */
    public boolean canEnd(Phase phase0) {
        if (MockPhaseManager.globalException != null) {
            throw new RuntimeException("The test may not be configured properly", MockPhaseManager.globalException);
        }

        String methodName = "canEnd_Phase";

        Throwable exception = (Throwable) MockPhaseManager.throwExceptions.get(methodName);
        if (exception != null) {
            throw new RuntimeException("The test may not be configured properly", exception);
        }

        HashMap arguments = new HashMap();
        arguments.put("1", phase0);
        List args = (List) MockPhaseManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            args = new ArrayList();
            MockPhaseManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return ((Boolean) MockPhaseManager.methodResults.get(methodName)).booleanValue();

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through {@link
     * #setMethodResult(String, Object)} method.</p>
     *
     * @see PhaseManager#end(Phase, String)
     */
    public void end(Phase phase0, String string0) {
        if (MockPhaseManager.globalException != null) {
            throw new RuntimeException("The test may not be configured properly", MockPhaseManager.globalException);
        }

        String methodName = "end_Phase_String";

        Throwable exception = (Throwable) MockPhaseManager.throwExceptions.get(methodName);
        if (exception != null) {
            throw new RuntimeException("The test may not be configured properly", exception);
        }

        HashMap arguments = new HashMap();
        arguments.put("1", phase0);
        arguments.put("2", string0);
        List args = (List) MockPhaseManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            args = new ArrayList();
            MockPhaseManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through {@link
     * #setMethodResult(String, Object)} method.</p>
     *
     * @see PhaseManager#canCancel(Phase)
     */
    public boolean canCancel(Phase phase0) {
        if (MockPhaseManager.globalException != null) {
            throw new RuntimeException("The test may not be configured properly", MockPhaseManager.globalException);
        }

        String methodName = "canCancel_Phase";

        Throwable exception = (Throwable) MockPhaseManager.throwExceptions.get(methodName);
        if (exception != null) {
            throw new RuntimeException("The test may not be configured properly", exception);
        }

        HashMap arguments = new HashMap();
        arguments.put("1", phase0);
        List args = (List) MockPhaseManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            args = new ArrayList();
            MockPhaseManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return ((Boolean) MockPhaseManager.methodResults.get(methodName)).booleanValue();

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through {@link
     * #setMethodResult(String, Object)} method.</p>
     *
     * @see PhaseManager#cancel(Phase, String)
     */
    public void cancel(Phase phase0, String string0) {
        if (MockPhaseManager.globalException != null) {
            throw new RuntimeException("The test may not be configured properly", MockPhaseManager.globalException);
        }

        String methodName = "cancel_Phase_String";

        Throwable exception = (Throwable) MockPhaseManager.throwExceptions.get(methodName);
        if (exception != null) {
            throw new RuntimeException("The test may not be configured properly", exception);
        }

        HashMap arguments = new HashMap();
        arguments.put("1", phase0);
        arguments.put("2", string0);
        List args = (List) MockPhaseManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            args = new ArrayList();
            MockPhaseManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

    }

    /**
     * <p>Sets the result to be returned by the specified method.</p>
     *
     * @param methodSignature a <code>String</code> uniquelly distinguishing the target method among other methods
     * declared by the implemented interface/class.
     * @param result an <code>Object</code> representing the result to be returned by specified method.
     */
    public static void setMethodResult(String methodSignature, Object result) {
        MockPhaseManager.methodResults.put(methodSignature, result);
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
        Map arguments = (Map) MockPhaseManager.methodArguments.get(methodSignature);
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
        return (List) MockPhaseManager.methodArguments.get(methodSignature);
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
            MockPhaseManager.throwExceptions.put(methodSignature, exception);
        } else {
            MockPhaseManager.throwExceptions.remove(methodSignature);
        }
    }

    /**
     * <p>Sets the exception to be thrown when the specified method is called.</p>
     *
     * @param exception a <code>Throwable</code> representing the exception to be thrown whenever any method is called.
     * If this argument is <code>null</code> then no exception will be thrown.
     */
    public static void throwGlobalException(Throwable exception) {
        MockPhaseManager.globalException = exception;
    }

    /**
     * <p>Releases the state of <code>MockPhaseManager</code> so all collected method arguments, configured method
     * results and exceptions are lost.</p>
     */
    public static void releaseState() {
        MockPhaseManager.methodArguments.clear();
        MockPhaseManager.methodResults.clear();
        MockPhaseManager.throwExceptions.clear();
        MockPhaseManager.globalException = null;
    }

    /**
     * <p>Initializes the initial state for all created instances of <code>MockPhaseManager</code> class.</p>
     */
    public static void init() {
        setMethodResult("getAllPhaseTypes", TestDataFactory.getPhaseTypes());
        setMethodResult("getAllPhaseStatuses", TestDataFactory.getPhaseStatuses());
    }

    public PhaseHandler[] getAllHandlers() {
        // TODO Auto-generated method stub
        return null;
    }

    public HandlerRegistryInfo[] getHandlerRegistrationInfo(PhaseHandler arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    public PhaseValidator getPhaseValidator() {
        // TODO Auto-generated method stub
        return null;
    }

    public void registerHandler(PhaseHandler arg0, PhaseType arg1, PhaseOperationEnum arg2) {
        // TODO Auto-generated method stub
        
    }

    public void setPhaseValidator(PhaseValidator arg0) {
        // TODO Auto-generated method stub
        
    }

    public PhaseHandler unregisterHandler(PhaseType arg0, PhaseOperationEnum arg1) {
        // TODO Auto-generated method stub
        return null;
    }

}
