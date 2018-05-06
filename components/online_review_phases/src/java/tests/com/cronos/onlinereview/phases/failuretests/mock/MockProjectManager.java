/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.cronos.onlinereview.phases.failuretests.mock;

import com.topcoder.management.project.ProjectManager;
import com.topcoder.management.project.Project;
import com.topcoder.management.project.PersistenceException;
import com.topcoder.management.project.ValidationException;
import com.topcoder.management.project.ProjectType;
import com.topcoder.management.project.ProjectCategory;
import com.topcoder.management.project.ProjectStatus;
import com.topcoder.management.project.ProjectPropertyType;
import com.topcoder.search.builder.filter.Filter;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

/**
 * <p>A mock implementation of {@link ProjectManager} class to be used for testing.
 * Overrides the protected methods declared by a super-class. The overridden methods are declared with package private access
 * so only the test cases could invoke them. The overridden methods simply call the corresponding method of a super-class.
 *
 * @author  isv
 * @version 1.0
 */
public class MockProjectManager implements ProjectManager {

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
     * <p>Constructs new <code>MockProjectManager</code> instance.</p>
     */
    public MockProjectManager() {
    }

    /**
     * <p>Constructs new <code>MockProjectManager</code> instance.</p>
     */
    public MockProjectManager(String namespace) {
    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see ProjectManager#createProject(Project, String)
     * @throws PersistenceException
     * @throws ValidationException
     */
    public void createProject(Project project0, String string0) throws PersistenceException, ValidationException {
        if (MockProjectManager.globalException != null) {
            if (MockProjectManager.globalException instanceof PersistenceException) {
                throw (PersistenceException) MockProjectManager.globalException;
            } else if (MockProjectManager.globalException instanceof ValidationException) {
                throw (ValidationException) MockProjectManager.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockProjectManager.globalException);
            }
        }

        String methodName = "createProject_Project_String";

        Throwable exception = (Throwable) MockProjectManager.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof PersistenceException) {
                throw (PersistenceException) exception;
            } else if (exception instanceof ValidationException) {
                throw (ValidationException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", project0);
        arguments.put("2", string0);
        List args = (List) MockProjectManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockProjectManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see ProjectManager#updateProject(Project, String, String)
     * @throws PersistenceException
     * @throws ValidationException
     */
    public void updateProject(Project project0, String string0, String string1) throws PersistenceException, ValidationException {
        if (MockProjectManager.globalException != null) {
            if (MockProjectManager.globalException instanceof PersistenceException) {
                throw (PersistenceException) MockProjectManager.globalException;
            } else if (MockProjectManager.globalException instanceof ValidationException) {
                throw (ValidationException) MockProjectManager.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockProjectManager.globalException);
            }
        }

        String methodName = "updateProject_Project_String_String";

        Throwable exception = (Throwable) MockProjectManager.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof PersistenceException) {
                throw (PersistenceException) exception;
            } else if (exception instanceof ValidationException) {
                throw (ValidationException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", project0);
        arguments.put("2", string0);
        arguments.put("3", string1);
        List args = (List) MockProjectManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockProjectManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see ProjectManager#getProject(long)
     * @throws PersistenceException
     */
    public Project getProject(long long0) throws PersistenceException {
        if (MockProjectManager.globalException != null) {
            if (MockProjectManager.globalException instanceof PersistenceException) {
                throw (PersistenceException) MockProjectManager.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockProjectManager.globalException);
            }
        }

        String methodName = "getProject_long";

        Throwable exception = (Throwable) MockProjectManager.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof PersistenceException) {
                throw (PersistenceException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", new Long(long0));
        List args = (List) MockProjectManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockProjectManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (Project) MockProjectManager.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see ProjectManager#getProjects(long[])
     * @throws PersistenceException
     */
    public Project[] getProjects(long[] longA0) throws PersistenceException {
        if (MockProjectManager.globalException != null) {
            if (MockProjectManager.globalException instanceof PersistenceException) {
                throw (PersistenceException) MockProjectManager.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockProjectManager.globalException);
            }
        }

        String methodName = "getProjects_long[]";

        Throwable exception = (Throwable) MockProjectManager.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof PersistenceException) {
                throw (PersistenceException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", longA0);
        List args = (List) MockProjectManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockProjectManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (Project[]) MockProjectManager.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see ProjectManager#searchProjects(Filter)
     * @throws PersistenceException
     */
    public Project[] searchProjects(Filter filter0) throws PersistenceException {
        if (MockProjectManager.globalException != null) {
            if (MockProjectManager.globalException instanceof PersistenceException) {
                throw (PersistenceException) MockProjectManager.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockProjectManager.globalException);
            }
        }

        String methodName = "searchProjects_Filter";

        Throwable exception = (Throwable) MockProjectManager.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof PersistenceException) {
                throw (PersistenceException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", filter0);
        List args = (List) MockProjectManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockProjectManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (Project[]) MockProjectManager.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see ProjectManager#getUserProjects(long)
     * @throws PersistenceException
     */
    public Project[] getUserProjects(long long0) throws PersistenceException {
        if (MockProjectManager.globalException != null) {
            if (MockProjectManager.globalException instanceof PersistenceException) {
                throw (PersistenceException) MockProjectManager.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockProjectManager.globalException);
            }
        }

        String methodName = "getUserProjects_long";

        Throwable exception = (Throwable) MockProjectManager.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof PersistenceException) {
                throw (PersistenceException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", new Long(long0));
        List args = (List) MockProjectManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockProjectManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (Project[]) MockProjectManager.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see ProjectManager#getAllProjectTypes()
     * @throws PersistenceException
     */
    public ProjectType[] getAllProjectTypes() throws PersistenceException {
        if (MockProjectManager.globalException != null) {
            if (MockProjectManager.globalException instanceof PersistenceException) {
                throw (PersistenceException) MockProjectManager.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockProjectManager.globalException);
            }
        }

        String methodName = "getAllProjectTypes";

        Throwable exception = (Throwable) MockProjectManager.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof PersistenceException) {
                throw (PersistenceException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        List args = (List) MockProjectManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockProjectManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (ProjectType[]) MockProjectManager.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see ProjectManager#getAllProjectCategories()
     * @throws PersistenceException
     */
    public ProjectCategory[] getAllProjectCategories() throws PersistenceException {
        if (MockProjectManager.globalException != null) {
            if (MockProjectManager.globalException instanceof PersistenceException) {
                throw (PersistenceException) MockProjectManager.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockProjectManager.globalException);
            }
        }

        String methodName = "getAllProjectCategories";

        Throwable exception = (Throwable) MockProjectManager.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof PersistenceException) {
                throw (PersistenceException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        List args = (List) MockProjectManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockProjectManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (ProjectCategory[]) MockProjectManager.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see ProjectManager#getAllProjectStatuses()
     * @throws PersistenceException
     */
    public ProjectStatus[] getAllProjectStatuses() throws PersistenceException {
        if (MockProjectManager.globalException != null) {
            if (MockProjectManager.globalException instanceof PersistenceException) {
                throw (PersistenceException) MockProjectManager.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockProjectManager.globalException);
            }
        }

        String methodName = "getAllProjectStatuses";

        Throwable exception = (Throwable) MockProjectManager.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof PersistenceException) {
                throw (PersistenceException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        List args = (List) MockProjectManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockProjectManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (ProjectStatus[]) MockProjectManager.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see ProjectManager#getAllProjectPropertyTypes()
     * @throws PersistenceException
     */
    public ProjectPropertyType[] getAllProjectPropertyTypes() throws PersistenceException {
        if (MockProjectManager.globalException != null) {
            if (MockProjectManager.globalException instanceof PersistenceException) {
                throw (PersistenceException) MockProjectManager.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockProjectManager.globalException);
            }
        }

        String methodName = "getAllProjectPropertyTypes";

        Throwable exception = (Throwable) MockProjectManager.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof PersistenceException) {
                throw (PersistenceException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        List args = (List) MockProjectManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockProjectManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (ProjectPropertyType[]) MockProjectManager.methodResults.get(methodName);

    }

    /**
     * <p>
     * Retrieves an array of project instance from the persistence whose
	 * create date is within current - days 
     * </p>
     * @param days last 'days' 
	 * @return An array of project instances.
     * @throws PersistenceException if error occurred while accessing the
     *             database.
     */
	public Project[] getProjectsByCreateDate(int days) throws PersistenceException 
    {
        return new Project[0];
    }

    /**
     * <p>Sets the result to be returned by the specified method.</p>
     *
     * @param methodSignature a <code>String</code> uniquelly distinguishing the target method among other methods
     *        declared by the implemented interface/class.
     * @param result an <code>Object</code> representing the result to be returned by specified method.
     */
    public static void setMethodResult(String methodSignature, Object result) {
        MockProjectManager.methodResults.put(methodSignature, result);
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
        Map arguments = (Map) MockProjectManager.methodArguments.get(methodSignature);
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
        return (List) MockProjectManager.methodArguments.get(methodSignature);
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
            MockProjectManager.throwExceptions.put(methodSignature, exception);
        } else {
            MockProjectManager.throwExceptions.remove(methodSignature);
        }
    }

    /**
     * <p>Sets the exception to be thrown when the specified method is called.</p>
     *
     * @param exception a <code>Throwable</code> representing the exception to be thrown whenever any method is
     *        called. If this argument is <code>null</code> then no exception will be thrown.
     */
    public static void throwGlobalException(Throwable exception) {
        MockProjectManager.globalException = exception;
    }

    /**
     * <p>Releases the state of <code>MockProjectManager</code> so all collected method arguments,
     * configured method results and exceptions are lost.</p>
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
    }

}
