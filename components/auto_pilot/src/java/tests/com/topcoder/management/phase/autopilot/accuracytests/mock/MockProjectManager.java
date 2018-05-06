/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.management.phase.autopilot.accuracytests.mock;

import com.topcoder.management.project.Project;
import com.topcoder.management.project.ProjectCategory;
import com.topcoder.management.project.ProjectManager;
import com.topcoder.management.project.ProjectStatus;
import com.topcoder.management.project.PersistenceException;
import com.topcoder.management.project.ProjectType;
import com.topcoder.management.project.ProjectPropertyType;
import com.topcoder.management.phase.autopilot.accuracytests.TestDataFactory;
import com.topcoder.search.builder.filter.Filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Hashtable;

/**
 * <p>A mock implementation of {@link ProjectManager} class to be used for testing. Overrides the protected methods
 * declared by a super-class. The overridden methods are declared with package private access so only the test cases
 * could invoke them. The overridden methods simply call the corresponding method of a super-class.
 *
 * @author isv
 * @version 1.0
 */
public class MockProjectManager implements ProjectManager {

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
     * @see ProjectManager#createProject(Project, String)
     */
    public void createProject(Project project0, String string0) {
        if (MockProjectManager.globalException != null) {
            throw new RuntimeException("The test may not be configured properly", MockProjectManager.globalException);
        }

        String methodName = "createProject_Project_String";

        Throwable exception = (Throwable) MockProjectManager.throwExceptions.get(methodName);
        if (exception != null) {
            throw new RuntimeException("The test may not be configured properly", exception);
        }

        HashMap arguments = new HashMap();
        arguments.put("1", project0);
        arguments.put("2", string0);
        List args = (List) MockProjectManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            args = new ArrayList();
            MockProjectManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through {@link
     * #setMethodResult(String, Object)} method.</p>
     *
     * @see ProjectManager#updateProject(Project, String, String)
     */
    public void updateProject(Project project0, String string0, String string1) {
        if (MockProjectManager.globalException != null) {
            throw new RuntimeException("The test may not be configured properly", MockProjectManager.globalException);
        }

        String methodName = "updateProject_Project_String_String";

        Throwable exception = (Throwable) MockProjectManager.throwExceptions.get(methodName);
        if (exception != null) {
            throw new RuntimeException("The test may not be configured properly", exception);
        }

        HashMap arguments = new HashMap();
        arguments.put("1", project0);
        arguments.put("2", string0);
        arguments.put("3", string1);
        List args = (List) MockProjectManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            args = new ArrayList();
            MockProjectManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through {@link
     * #setMethodResult(String, Object)} method.</p>
     *
     * @see ProjectManager#getProject(long)
     */
    public Project getProject(long long0) {
        if (MockProjectManager.globalException != null) {
            throw new RuntimeException("The test may not be configured properly", MockProjectManager.globalException);
        }

        String methodName = "getProject_long";

        Throwable exception = (Throwable) MockProjectManager.throwExceptions.get(methodName);
        if (exception != null) {
            throw new RuntimeException("The test may not be configured properly", exception);
        }

        HashMap arguments = new HashMap();
        arguments.put("1", new Long(long0));
        List args = (List) MockProjectManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            args = new ArrayList();
            MockProjectManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (Project) MockProjectManager.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through {@link
     * #setMethodResult(String, Object)} method.</p>
     *
     * @see ProjectManager#searchProjects(Filter)
     */
    public Project[] searchProjects(Filter filter0) {
        if (MockProjectManager.globalException != null) {
            throw new RuntimeException("The test may not be configured properly", MockProjectManager.globalException);
        }

        String methodName = "searchProjects_Filter";

        Throwable exception = (Throwable) MockProjectManager.throwExceptions.get(methodName);
        if (exception != null) {
            throw new RuntimeException("The test may not be configured properly", exception);
        }

        HashMap arguments = new HashMap();
        arguments.put("1", filter0);
        List args = (List) MockProjectManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            args = new ArrayList();
            MockProjectManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (Project[]) MockProjectManager.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through {@link
     * #setMethodResult(String, Object)} method.</p>
     *
     * @see ProjectManager#getUserProjects(long)
     */
    public Project[] getUserProjects(long long0) {
        if (MockProjectManager.globalException != null) {
            throw new RuntimeException("The test may not be configured properly", MockProjectManager.globalException);
        }

        String methodName = "getUserProjects_long";

        Throwable exception = (Throwable) MockProjectManager.throwExceptions.get(methodName);
        if (exception != null) {
            throw new RuntimeException("The test may not be configured properly", exception);
        }

        HashMap arguments = new HashMap();
        arguments.put("1", new Long(long0));
        List args = (List) MockProjectManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            args = new ArrayList();
            MockProjectManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (Project[]) MockProjectManager.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through {@link
     * #setMethodResult(String, Object)} method.</p>
     *
     * @see ProjectManager#getAllProjectCategories()
     */
    public ProjectCategory[] getAllProjectCategories() {
        if (MockProjectManager.globalException != null) {
            throw new RuntimeException("The test may not be configured properly", MockProjectManager.globalException);
        }

        String methodName = "getAllProjectCategories";

        Throwable exception = (Throwable) MockProjectManager.throwExceptions.get(methodName);
        if (exception != null) {
            throw new RuntimeException("The test may not be configured properly", exception);
        }

        HashMap arguments = new HashMap();
        List args = (List) MockProjectManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            args = new ArrayList();
            MockProjectManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (ProjectCategory[]) MockProjectManager.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through {@link
     * #setMethodResult(String, Object)} method.</p>
     *
     * @see ProjectManager#getAllProjectStatuses()
     */
    public ProjectStatus[] getAllProjectStatuses() {
        if (MockProjectManager.globalException != null) {
            throw new RuntimeException("The test may not be configured properly", MockProjectManager.globalException);
        }

        String methodName = "getAllProjectStatuses";

        Throwable exception = (Throwable) MockProjectManager.throwExceptions.get(methodName);
        if (exception != null) {
            throw new RuntimeException("The test may not be configured properly", exception);
        }

        HashMap arguments = new HashMap();
        List args = (List) MockProjectManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            args = new ArrayList();
            MockProjectManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (ProjectStatus[]) MockProjectManager.methodResults.get(methodName);

    }

    /**
     * <p>Sets the result to be returned by the specified method.</p>
     *
     * @param methodSignature a <code>String</code> uniquelly distinguishing the target method among other methods
     * declared by the implemented interface/class.
     * @param result an <code>Object</code> representing the result to be returned by specified method.
     */
    public static void setMethodResult(String methodSignature, Object result) {
        MockProjectManager.methodResults.put(methodSignature, result);
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
        Map arguments = (Map) MockProjectManager.methodArguments.get(methodSignature);
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
        return (List) MockProjectManager.methodArguments.get(methodSignature);
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
            MockProjectManager.throwExceptions.put(methodSignature, exception);
        } else {
            MockProjectManager.throwExceptions.remove(methodSignature);
        }
    }

    /**
     * <p>Sets the exception to be thrown when the specified method is called.</p>
     *
     * @param exception a <code>Throwable</code> representing the exception to be thrown whenever any method is called.
     * If this argument is <code>null</code> then no exception will be thrown.
     */
    public static void throwGlobalException(Throwable exception) {
        MockProjectManager.globalException = exception;
    }

    /**
     * <p>Releases the state of <code>MockProjectManager</code> so all collected method arguments, configured method
     * results and exceptions are lost.</p>
     */
    public static void releaseState() {
        MockProjectManager.methodArguments.clear();
        MockProjectManager.methodResults.clear();
        MockProjectManager.throwExceptions.clear();
        MockProjectManager.globalException = null;
    }

    /**
     * <p>Initializes the initial state for all created instances of <code>MockProjectManager</code> class.</p>
     */
    public static void init() {
        setMethodResult("searchProjects_Filter", TestDataFactory.getActiveProjects());
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

    /**
     * <p> Retrieves an array of project instance from the persistence given their ids. The project instances are
     * retrieved with their properties. </p>
     *
     * @param ids The ids of the projects to be retrieved.
     * @return An array of project instances.
     * @throws IllegalArgumentException if the input id is less than or equal to zero.
     * @throws PersistenceException if error occurred while accessing the database.
     */
    public Project[] getProjects(long[] ids) throws PersistenceException {
        return new Project[0];
    }

    /**
     * Gets an array of all project types in the persistence. The project types are stored in 'project_type_lu' table.
     *
     * @return An array of all project types in the persistence.
     * @throws PersistenceException if error occurred while accessing the database.
     */
    public ProjectType[] getAllProjectTypes() throws PersistenceException {
        return new ProjectType[0];
    }

    /**
     * Gets an array of all project property type in the persistence. The project property types are stored in
     * 'project_info_type_lu' table.
     *
     * @return An array of all scorecard assignments in the persistence.
     * @throws PersistenceException if error occurred while accessing the database.
     */
    public ProjectPropertyType[] getAllProjectPropertyTypes() throws PersistenceException {
        return new ProjectPropertyType[0];
    }
}
