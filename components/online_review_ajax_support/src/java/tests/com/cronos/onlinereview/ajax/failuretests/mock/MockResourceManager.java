/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */

package com.cronos.onlinereview.ajax.failuretests.mock;

import com.topcoder.management.resource.Notification;
import com.topcoder.management.resource.NotificationType;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.resource.ResourceManager;
import com.topcoder.management.resource.ResourceRole;
import com.topcoder.management.resource.persistence.ResourcePersistenceException;
import com.topcoder.search.builder.SearchBuilderConfigurationException;
import com.topcoder.search.builder.SearchBuilderException;
import com.topcoder.search.builder.filter.Filter;
import com.cronos.onlinereview.ajax.failuretests.TestDataFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>A mock implementation of {@link ResourceManager} class to be used for testing. Overrides the protected methods
 * declared by a super-class. The overridden methods are declared with package private access so only the test cases
 * could invoke them. The overridden methods simply call the corresponding method of a super-class.
 *
 * @author isv
 * @version 1.0
 */
public class MockResourceManager implements ResourceManager {

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
     * @see ResourceManager#updateResource(Resource, String)
     */
    public void updateResource(Resource resource0, String string0) {
        if (MockResourceManager.globalException != null) {
            throw new RuntimeException("The test may not be configured properly", MockResourceManager.globalException);
        }

        String methodName = "updateResource_Resource_String";

        Throwable exception = (Throwable) MockResourceManager.throwExceptions.get(methodName);
        if (exception != null) {
            throw new RuntimeException("The test may not be configured properly", exception);
        }

        HashMap arguments = new HashMap();
        arguments.put("1", resource0);
        arguments.put("2", string0);
        List args = (List) MockResourceManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            args = new ArrayList();
            MockResourceManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through {@link
     * #setMethodResult(String, Object)} method.</p>
     *
     * @see ResourceManager#updateResources(Resource[], long, String)
     */
    public void updateResources(Resource[] resourceA0, long long0, String string0) {
        if (MockResourceManager.globalException != null) {
            throw new RuntimeException("The test may not be configured properly", MockResourceManager.globalException);
        }

        String methodName = "updateResources_Resource[]_long_String";

        Throwable exception = (Throwable) MockResourceManager.throwExceptions.get(methodName);
        if (exception != null) {
            throw new RuntimeException("The test may not be configured properly", exception);
        }

        HashMap arguments = new HashMap();
        arguments.put("1", resourceA0);
        arguments.put("2", new Long(long0));
        arguments.put("3", string0);
        List args = (List) MockResourceManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            args = new ArrayList();
            MockResourceManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through {@link
     * #setMethodResult(String, Object)} method.</p>
     *
     * @see ResourceManager#getResource(long)
     */
    public Resource getResource(long long0) {
        if (MockResourceManager.globalException != null) {
            throw new RuntimeException("The test may not be configured properly", MockResourceManager.globalException);
        }

        String methodName = "getResource_long";

        Throwable exception = (Throwable) MockResourceManager.throwExceptions.get(methodName);
        if (exception != null) {
            throw new RuntimeException("The test may not be configured properly", exception);
        }

        HashMap arguments = new HashMap();
        arguments.put("1", new Long(long0));
        List args = (List) MockResourceManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            args = new ArrayList();
            MockResourceManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (Resource) MockResourceManager.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through {@link
     * #setMethodResult(String, Object)} method.</p>
     *
     * @see ResourceManager#searchResource(Filter)
     */
    public Resource[] searchResource(Filter filter0) {
        if (MockResourceManager.globalException != null) {
            throw new RuntimeException("The test may not be configured properly", MockResourceManager.globalException);
        }

        String methodName = "searchResource_Filter";

        Throwable exception = (Throwable) MockResourceManager.throwExceptions.get(methodName);
        if (exception != null) {
            throw new RuntimeException("The test may not be configured properly", exception);
        }

        HashMap arguments = new HashMap();
        arguments.put("1", filter0);
        List args = (List) MockResourceManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            args = new ArrayList();
            MockResourceManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (Resource[]) MockResourceManager.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through {@link
     * #setMethodResult(String, Object)} method.</p>
     *
     * @see ResourceManager#getAllResourceRoles()
     */
    public ResourceRole[] getAllResourceRoles() {
        if (MockResourceManager.globalException != null) {
            throw new RuntimeException("The test may not be configured properly", MockResourceManager.globalException);
        }

        String methodName = "getAllResourceRoles";

        Throwable exception = (Throwable) MockResourceManager.throwExceptions.get(methodName);
        if (exception != null) {
            throw new RuntimeException("The test may not be configured properly", exception);
        }

        HashMap arguments = new HashMap();
        List args = (List) MockResourceManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            args = new ArrayList();
            MockResourceManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (ResourceRole[]) MockResourceManager.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through {@link
     * #setMethodResult(String, Object)} method.</p>
     *
     * @see ResourceManager#addNotifications(long[], long, long, String)
     */
    public void addNotifications(long[] longA0, long long0, long long1, String string0) {
        if (MockResourceManager.globalException != null) {
            throw new RuntimeException("The test may not be configured properly", MockResourceManager.globalException);
        }

        String methodName = "addNotifications_long[]_long_long_String";

        Throwable exception = (Throwable) MockResourceManager.throwExceptions.get(methodName);
        if (exception != null) {
            throw new RuntimeException("The test may not be configured properly", exception);
        }

        HashMap arguments = new HashMap();
        arguments.put("1", longA0);
        arguments.put("2", new Long(long0));
        arguments.put("3", new Long(long1));
        arguments.put("4", string0);
        List args = (List) MockResourceManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            args = new ArrayList();
            MockResourceManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through {@link
     * #setMethodResult(String, Object)} method.</p>
     *
     * @see ResourceManager#removeNotifications(long[], long, long, String)
     */
    public void removeNotifications(long[] longA0, long long0, long long1, String string0) {
        if (MockResourceManager.globalException != null) {
            throw new RuntimeException("The test may not be configured properly", MockResourceManager.globalException);
        }

        String methodName = "removeNotifications_long[]_long_long_String";

        Throwable exception = (Throwable) MockResourceManager.throwExceptions.get(methodName);
        if (exception != null) {
            throw new RuntimeException("The test may not be configured properly", exception);
        }

        HashMap arguments = new HashMap();
        arguments.put("1", longA0);
        arguments.put("2", new Long(long0));
        arguments.put("3", new Long(long1));
        arguments.put("4", string0);
        List args = (List) MockResourceManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            args = new ArrayList();
            MockResourceManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through {@link
     * #setMethodResult(String, Object)} method.</p>
     *
     * @see ResourceManager#getNotifications(long, long)
     */
    public long[] getNotifications(long long0, long long1) {
        if (MockResourceManager.globalException != null) {
            throw new RuntimeException("The test may not be configured properly", MockResourceManager.globalException);
        }

        String methodName = "getNotifications_long_long";

        Throwable exception = (Throwable) MockResourceManager.throwExceptions.get(methodName);
        if (exception != null) {
            throw new RuntimeException("The test may not be configured properly", exception);
        }

        HashMap arguments = new HashMap();
        arguments.put("1", new Long(long0));
        arguments.put("2", new Long(long1));
        List args = (List) MockResourceManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            args = new ArrayList();
            MockResourceManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (long[]) MockResourceManager.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through {@link
     * #setMethodResult(String, Object)} method.</p>
     *
     * @see ResourceManager#getAllNotificationTypes()
     */
    public NotificationType[] getAllNotificationTypes() {
        if (MockResourceManager.globalException != null) {
            throw new RuntimeException("The test may not be configured properly", MockResourceManager.globalException);
        }

        String methodName = "getAllNotificationTypes";

        Throwable exception = (Throwable) MockResourceManager.throwExceptions.get(methodName);
        if (exception != null) {
            throw new RuntimeException("The test may not be configured properly", exception);
        }

        HashMap arguments = new HashMap();
        List args = (List) MockResourceManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            args = new ArrayList();
            MockResourceManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (NotificationType[]) MockResourceManager.methodResults.get(methodName);

    }

    /**
     * <p>Sets the result to be returned by the specified method.</p>
     *
     * @param methodSignature a <code>String</code> uniquelly distinguishing the target method among other methods
     * declared by the implemented interface/class.
     * @param result an <code>Object</code> representing the result to be returned by specified method.
     */
    public static void setMethodResult(String methodSignature, Object result) {
        MockResourceManager.methodResults.put(methodSignature, result);
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
        Map arguments = (Map) MockResourceManager.methodArguments.get(methodSignature);
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
        return (List) MockResourceManager.methodArguments.get(methodSignature);
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
            MockResourceManager.throwExceptions.put(methodSignature, exception);
        } else {
            MockResourceManager.throwExceptions.remove(methodSignature);
        }
    }

    /**
     * <p>Sets the exception to be thrown when the specified method is called.</p>
     *
     * @param exception a <code>Throwable</code> representing the exception to be thrown whenever any method is called.
     * If this argument is <code>null</code> then no exception will be thrown.
     */
    public static void throwGlobalException(Throwable exception) {
        MockResourceManager.globalException = exception;
    }

    /**
     * <p>Releases the state of <code>MockResourceManager</code> so all collected method arguments, configured method
     * results and exceptions are lost.</p>
     */
    public static void releaseState() {
        MockResourceManager.methodArguments.clear();
        MockResourceManager.methodResults.clear();
        MockResourceManager.throwExceptions.clear();
        MockResourceManager.globalException = null;
    }

    /**
     * <p>Initializes the initial state for all created instances of <code>MockResourceManager</code> class.</p>
     */
    public static void init() {
        setMethodResult("getAllResourceRoles", TestDataFactory.getResourceRoles());
        setMethodResult("getAllNotificationTypes", TestDataFactory.getNotificationTypes());
    }

    public void removeNotificationType(NotificationType arg0, String arg1) throws ResourcePersistenceException {
        // TODO Auto-generated method stub
        
    }

    public void removeResource(Resource arg0, String arg1) throws ResourcePersistenceException {
        // TODO Auto-generated method stub
        
    }

    public void removeResourceRole(ResourceRole arg0, String arg1) throws ResourcePersistenceException {
        // TODO Auto-generated method stub
        
    }

    public NotificationType[] searchNotificationTypes(Filter arg0) throws ResourcePersistenceException, SearchBuilderException, SearchBuilderConfigurationException {
        // TODO Auto-generated method stub
        return null;
    }

    public Notification[] searchNotifications(Filter arg0) throws ResourcePersistenceException, SearchBuilderException, SearchBuilderConfigurationException {
        // TODO Auto-generated method stub
        return null;
    }

    public ResourceRole[] searchResourceRoles(Filter arg0) throws ResourcePersistenceException, SearchBuilderException, SearchBuilderConfigurationException {
        // TODO Auto-generated method stub
        return null;
    }

    public Resource[] searchResources(Filter arg0) throws ResourcePersistenceException, SearchBuilderException, SearchBuilderConfigurationException {
        // TODO Auto-generated method stub
        return null;
    }

    public void updateNotificationType(NotificationType arg0, String arg1) throws ResourcePersistenceException {
        // TODO Auto-generated method stub
        
    }

    public void updateResourceRole(ResourceRole arg0, String arg1) throws ResourcePersistenceException {
        // TODO Auto-generated method stub
        
    }

}
