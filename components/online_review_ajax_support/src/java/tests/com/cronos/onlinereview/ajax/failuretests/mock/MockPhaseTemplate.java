/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */

package com.cronos.onlinereview.ajax.failuretests.mock;

import com.topcoder.project.phases.Project;
import com.topcoder.project.phases.template.PhaseTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>A mock implementation of {@link PhaseTemplate} class to be used for testing. Overrides the protected methods
 * declared by a super-class. The overridden methods are declared with package private access so only the test cases
 * could invoke them. The overridden methods simply call the corresponding method of a super-class.
 *
 * @author isv
 * @version 1.0
 */
public class MockPhaseTemplate implements PhaseTemplate {

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
     * @see PhaseTemplate#getAllTemplateNames()
     */
    public String[] getAllTemplateNames() {
        if (MockPhaseTemplate.globalException != null) {
            throw new RuntimeException("The test may not be configured properly", MockPhaseTemplate.globalException);
        }

        String methodName = "getAllTemplateNames";

        Throwable exception = (Throwable) MockPhaseTemplate.throwExceptions.get(methodName);
        if (exception != null) {
            throw new RuntimeException("The test may not be configured properly", exception);
        }

        HashMap arguments = new HashMap();
        List args = (List) MockPhaseTemplate.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            args = new ArrayList();
            MockPhaseTemplate.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (String[]) MockPhaseTemplate.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through {@link
     * #setMethodResult(String, Object)} method.</p>
     *
     * @see PhaseTemplate#applyTemplate(String, Date)
     */
    public Project applyTemplate(String string0, Date date0) {
        if (MockPhaseTemplate.globalException != null) {
            throw new RuntimeException("The test may not be configured properly", MockPhaseTemplate.globalException);
        }

        String methodName = "applyTemplate_String_Date";

        Throwable exception = (Throwable) MockPhaseTemplate.throwExceptions.get(methodName);
        if (exception != null) {
            throw new RuntimeException("The test may not be configured properly", exception);
        }

        HashMap arguments = new HashMap();
        arguments.put("1", string0);
        arguments.put("2", date0);
        List args = (List) MockPhaseTemplate.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            args = new ArrayList();
            MockPhaseTemplate.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (Project) MockPhaseTemplate.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through {@link
     * #setMethodResult(String, Object)} method.</p>
     *
     * @see PhaseTemplate#applyTemplate(String)
     */
    public Project applyTemplate(String string0) {
        if (MockPhaseTemplate.globalException != null) {
            throw new RuntimeException("The test may not be configured properly", MockPhaseTemplate.globalException);
        }

        String methodName = "applyTemplate_String";

        Throwable exception = (Throwable) MockPhaseTemplate.throwExceptions.get(methodName);
        if (exception != null) {
            throw new RuntimeException("The test may not be configured properly", exception);
        }

        HashMap arguments = new HashMap();
        arguments.put("1", string0);
        List args = (List) MockPhaseTemplate.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            args = new ArrayList();
            MockPhaseTemplate.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (Project) MockPhaseTemplate.methodResults.get(methodName);

    }

    /**
     * <p>Sets the result to be returned by the specified method.</p>
     *
     * @param methodSignature a <code>String</code> uniquelly distinguishing the target method among other methods
     * declared by the implemented interface/class.
     * @param result an <code>Object</code> representing the result to be returned by specified method.
     */
    public static void setMethodResult(String methodSignature, Object result) {
        MockPhaseTemplate.methodResults.put(methodSignature, result);
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
        Map arguments = (Map) MockPhaseTemplate.methodArguments.get(methodSignature);
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
        return (List) MockPhaseTemplate.methodArguments.get(methodSignature);
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
            MockPhaseTemplate.throwExceptions.put(methodSignature, exception);
        } else {
            MockPhaseTemplate.throwExceptions.remove(methodSignature);
        }
    }

    /**
     * <p>Sets the exception to be thrown when the specified method is called.</p>
     *
     * @param exception a <code>Throwable</code> representing the exception to be thrown whenever any method is called.
     * If this argument is <code>null</code> then no exception will be thrown.
     */
    public static void throwGlobalException(Throwable exception) {
        MockPhaseTemplate.globalException = exception;
    }

    /**
     * <p>Releases the state of <code>MockPhaseTemplate</code> so all collected method arguments, configured method
     * results and exceptions are lost.</p>
     */
    public static void releaseState() {
        MockPhaseTemplate.methodArguments.clear();
        MockPhaseTemplate.methodResults.clear();
        MockPhaseTemplate.throwExceptions.clear();
        MockPhaseTemplate.globalException = null;
    }

    /**
     * <p>Initializes the initial state for all created instances of <code>MockPhaseTemplate</code> class.</p>
     */
    public static void init() {
    }

}
