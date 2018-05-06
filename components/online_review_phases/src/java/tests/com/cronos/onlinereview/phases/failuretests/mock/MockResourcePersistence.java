/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.cronos.onlinereview.phases.failuretests.mock;

import com.topcoder.management.resource.persistence.ResourcePersistence;
import com.topcoder.management.resource.persistence.ResourcePersistenceException;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.resource.Notification;
import com.topcoder.management.resource.NotificationType;
import com.topcoder.management.resource.ResourceRole;
import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.util.sql.databaseabstraction.CustomResultSet;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

/**
 * <p>A mock implementation of {@link ResourcePersistence} class to be used for testing.
 * Overrides the protected methods declared by a super-class. The overridden methods are declared with package private access
 * so only the test cases could invoke them. The overridden methods simply call the corresponding method of a super-class.
 *
 * @author  isv
 * @version 1.0
 */
public class MockResourcePersistence implements ResourcePersistence {

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
     * <p>Constructs new <code>MockResourcePersistence</code> instance.</p>
     */
    public MockResourcePersistence() {
    }

    /**
     * <p>Constructs new <code>MockResourcePersistence</code> instance.</p>
     */
    public MockResourcePersistence(String namespace) {
    }

    /**
     * <p>Constructs new <code>MockResourcePersistence</code> instance.</p>
     */
    public MockResourcePersistence(DBConnectionFactory factory, String namespace) {
    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see ResourcePersistence#addResource(Resource)
     * @throws ResourcePersistenceException
     */
    public void addResource(Resource resource0) throws ResourcePersistenceException {
        if (MockResourcePersistence.globalException != null) {
            if (MockResourcePersistence.globalException instanceof ResourcePersistenceException) {
                throw (ResourcePersistenceException) MockResourcePersistence.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockResourcePersistence.globalException);
            }
        }

        String methodName = "addResource_Resource";

        Throwable exception = (Throwable) MockResourcePersistence.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof ResourcePersistenceException) {
                throw (ResourcePersistenceException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", resource0);
        List args = (List) MockResourcePersistence.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockResourcePersistence.methodArguments.put(methodName, args);
        }
        args.add(arguments);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see ResourcePersistence#deleteResource(Resource)
     * @throws ResourcePersistenceException
     */
    public void deleteResource(Resource resource0) throws ResourcePersistenceException {
        if (MockResourcePersistence.globalException != null) {
            if (MockResourcePersistence.globalException instanceof ResourcePersistenceException) {
                throw (ResourcePersistenceException) MockResourcePersistence.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockResourcePersistence.globalException);
            }
        }

        String methodName = "deleteResource_Resource";

        Throwable exception = (Throwable) MockResourcePersistence.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof ResourcePersistenceException) {
                throw (ResourcePersistenceException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", resource0);
        List args = (List) MockResourcePersistence.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockResourcePersistence.methodArguments.put(methodName, args);
        }
        args.add(arguments);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see ResourcePersistence#updateResource(Resource)
     * @throws ResourcePersistenceException
     */
    public void updateResource(Resource resource0) throws ResourcePersistenceException {
        if (MockResourcePersistence.globalException != null) {
            if (MockResourcePersistence.globalException instanceof ResourcePersistenceException) {
                throw (ResourcePersistenceException) MockResourcePersistence.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockResourcePersistence.globalException);
            }
        }

        String methodName = "updateResource_Resource";

        Throwable exception = (Throwable) MockResourcePersistence.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof ResourcePersistenceException) {
                throw (ResourcePersistenceException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", resource0);
        List args = (List) MockResourcePersistence.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockResourcePersistence.methodArguments.put(methodName, args);
        }
        args.add(arguments);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see ResourcePersistence#loadResource(long)
     * @throws ResourcePersistenceException
     */
    public Resource loadResource(long long0) throws ResourcePersistenceException {
        if (MockResourcePersistence.globalException != null) {
            if (MockResourcePersistence.globalException instanceof ResourcePersistenceException) {
                throw (ResourcePersistenceException) MockResourcePersistence.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockResourcePersistence.globalException);
            }
        }

        String methodName = "loadResource_long";

        Throwable exception = (Throwable) MockResourcePersistence.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof ResourcePersistenceException) {
                throw (ResourcePersistenceException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", new Long(long0));
        List args = (List) MockResourcePersistence.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockResourcePersistence.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (Resource) MockResourcePersistence.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see ResourcePersistence#addNotification(long, long, long, String)
     * @throws ResourcePersistenceException
     */
    public void addNotification(long long0, long long1, long long2, String string0) throws ResourcePersistenceException {
        if (MockResourcePersistence.globalException != null) {
            if (MockResourcePersistence.globalException instanceof ResourcePersistenceException) {
                throw (ResourcePersistenceException) MockResourcePersistence.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockResourcePersistence.globalException);
            }
        }

        String methodName = "addNotification_long_long_long_String";

        Throwable exception = (Throwable) MockResourcePersistence.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof ResourcePersistenceException) {
                throw (ResourcePersistenceException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", new Long(long0));
        arguments.put("2", new Long(long1));
        arguments.put("3", new Long(long2));
        arguments.put("4", string0);
        List args = (List) MockResourcePersistence.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockResourcePersistence.methodArguments.put(methodName, args);
        }
        args.add(arguments);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see ResourcePersistence#removeNotification(long, long, long, String)
     * @throws ResourcePersistenceException
     */
    public void removeNotification(long long0, long long1, long long2, String string0) throws ResourcePersistenceException {
        if (MockResourcePersistence.globalException != null) {
            if (MockResourcePersistence.globalException instanceof ResourcePersistenceException) {
                throw (ResourcePersistenceException) MockResourcePersistence.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockResourcePersistence.globalException);
            }
        }

        String methodName = "removeNotification_long_long_long_String";

        Throwable exception = (Throwable) MockResourcePersistence.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof ResourcePersistenceException) {
                throw (ResourcePersistenceException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", new Long(long0));
        arguments.put("2", new Long(long1));
        arguments.put("3", new Long(long2));
        arguments.put("4", string0);
        List args = (List) MockResourcePersistence.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockResourcePersistence.methodArguments.put(methodName, args);
        }
        args.add(arguments);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see ResourcePersistence#loadNotification(long, long, long)
     * @throws ResourcePersistenceException
     */
    public Notification loadNotification(long long0, long long1, long long2) throws ResourcePersistenceException {
        if (MockResourcePersistence.globalException != null) {
            if (MockResourcePersistence.globalException instanceof ResourcePersistenceException) {
                throw (ResourcePersistenceException) MockResourcePersistence.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockResourcePersistence.globalException);
            }
        }

        String methodName = "loadNotification_long_long_long";

        Throwable exception = (Throwable) MockResourcePersistence.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof ResourcePersistenceException) {
                throw (ResourcePersistenceException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", new Long(long0));
        arguments.put("2", new Long(long1));
        arguments.put("3", new Long(long2));
        List args = (List) MockResourcePersistence.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockResourcePersistence.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (Notification) MockResourcePersistence.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see ResourcePersistence#addNotificationType(NotificationType)
     * @throws ResourcePersistenceException
     */
    public void addNotificationType(NotificationType notificationType0) throws ResourcePersistenceException {
        if (MockResourcePersistence.globalException != null) {
            if (MockResourcePersistence.globalException instanceof ResourcePersistenceException) {
                throw (ResourcePersistenceException) MockResourcePersistence.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockResourcePersistence.globalException);
            }
        }

        String methodName = "addNotificationType_NotificationType";

        Throwable exception = (Throwable) MockResourcePersistence.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof ResourcePersistenceException) {
                throw (ResourcePersistenceException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", notificationType0);
        List args = (List) MockResourcePersistence.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockResourcePersistence.methodArguments.put(methodName, args);
        }
        args.add(arguments);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see ResourcePersistence#deleteNotificationType(NotificationType)
     * @throws ResourcePersistenceException
     */
    public void deleteNotificationType(NotificationType notificationType0) throws ResourcePersistenceException {
        if (MockResourcePersistence.globalException != null) {
            if (MockResourcePersistence.globalException instanceof ResourcePersistenceException) {
                throw (ResourcePersistenceException) MockResourcePersistence.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockResourcePersistence.globalException);
            }
        }

        String methodName = "deleteNotificationType_NotificationType";

        Throwable exception = (Throwable) MockResourcePersistence.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof ResourcePersistenceException) {
                throw (ResourcePersistenceException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", notificationType0);
        List args = (List) MockResourcePersistence.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockResourcePersistence.methodArguments.put(methodName, args);
        }
        args.add(arguments);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see ResourcePersistence#updateNotificationType(NotificationType)
     * @throws ResourcePersistenceException
     */
    public void updateNotificationType(NotificationType notificationType0) throws ResourcePersistenceException {
        if (MockResourcePersistence.globalException != null) {
            if (MockResourcePersistence.globalException instanceof ResourcePersistenceException) {
                throw (ResourcePersistenceException) MockResourcePersistence.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockResourcePersistence.globalException);
            }
        }

        String methodName = "updateNotificationType_NotificationType";

        Throwable exception = (Throwable) MockResourcePersistence.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof ResourcePersistenceException) {
                throw (ResourcePersistenceException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", notificationType0);
        List args = (List) MockResourcePersistence.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockResourcePersistence.methodArguments.put(methodName, args);
        }
        args.add(arguments);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see ResourcePersistence#loadNotificationType(long)
     * @throws ResourcePersistenceException
     */
    public NotificationType loadNotificationType(long long0) throws ResourcePersistenceException {
        if (MockResourcePersistence.globalException != null) {
            if (MockResourcePersistence.globalException instanceof ResourcePersistenceException) {
                throw (ResourcePersistenceException) MockResourcePersistence.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockResourcePersistence.globalException);
            }
        }

        String methodName = "loadNotificationType_long";

        Throwable exception = (Throwable) MockResourcePersistence.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof ResourcePersistenceException) {
                throw (ResourcePersistenceException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", new Long(long0));
        List args = (List) MockResourcePersistence.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockResourcePersistence.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (NotificationType) MockResourcePersistence.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see ResourcePersistence#addResourceRole(ResourceRole)
     * @throws ResourcePersistenceException
     */
    public void addResourceRole(ResourceRole resourceRole0) throws ResourcePersistenceException {
        if (MockResourcePersistence.globalException != null) {
            if (MockResourcePersistence.globalException instanceof ResourcePersistenceException) {
                throw (ResourcePersistenceException) MockResourcePersistence.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockResourcePersistence.globalException);
            }
        }

        String methodName = "addResourceRole_ResourceRole";

        Throwable exception = (Throwable) MockResourcePersistence.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof ResourcePersistenceException) {
                throw (ResourcePersistenceException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", resourceRole0);
        List args = (List) MockResourcePersistence.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockResourcePersistence.methodArguments.put(methodName, args);
        }
        args.add(arguments);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see ResourcePersistence#deleteResourceRole(ResourceRole)
     * @throws ResourcePersistenceException
     */
    public void deleteResourceRole(ResourceRole resourceRole0) throws ResourcePersistenceException {
        if (MockResourcePersistence.globalException != null) {
            if (MockResourcePersistence.globalException instanceof ResourcePersistenceException) {
                throw (ResourcePersistenceException) MockResourcePersistence.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockResourcePersistence.globalException);
            }
        }

        String methodName = "deleteResourceRole_ResourceRole";

        Throwable exception = (Throwable) MockResourcePersistence.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof ResourcePersistenceException) {
                throw (ResourcePersistenceException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", resourceRole0);
        List args = (List) MockResourcePersistence.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockResourcePersistence.methodArguments.put(methodName, args);
        }
        args.add(arguments);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see ResourcePersistence#updateResourceRole(ResourceRole)
     * @throws ResourcePersistenceException
     */
    public void updateResourceRole(ResourceRole resourceRole0) throws ResourcePersistenceException {
        if (MockResourcePersistence.globalException != null) {
            if (MockResourcePersistence.globalException instanceof ResourcePersistenceException) {
                throw (ResourcePersistenceException) MockResourcePersistence.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockResourcePersistence.globalException);
            }
        }

        String methodName = "updateResourceRole_ResourceRole";

        Throwable exception = (Throwable) MockResourcePersistence.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof ResourcePersistenceException) {
                throw (ResourcePersistenceException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", resourceRole0);
        List args = (List) MockResourcePersistence.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockResourcePersistence.methodArguments.put(methodName, args);
        }
        args.add(arguments);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see ResourcePersistence#loadResourceRole(long)
     * @throws ResourcePersistenceException
     */
    public ResourceRole loadResourceRole(long long0) throws ResourcePersistenceException {
        if (MockResourcePersistence.globalException != null) {
            if (MockResourcePersistence.globalException instanceof ResourcePersistenceException) {
                throw (ResourcePersistenceException) MockResourcePersistence.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockResourcePersistence.globalException);
            }
        }

        String methodName = "loadResourceRole_long";

        Throwable exception = (Throwable) MockResourcePersistence.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof ResourcePersistenceException) {
                throw (ResourcePersistenceException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", new Long(long0));
        List args = (List) MockResourcePersistence.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockResourcePersistence.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (ResourceRole) MockResourcePersistence.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see ResourcePersistence#loadResources(long[])
     * @throws ResourcePersistenceException
     */
    public Resource[] loadResources(long[] longA0) throws ResourcePersistenceException {
        if (MockResourcePersistence.globalException != null) {
            if (MockResourcePersistence.globalException instanceof ResourcePersistenceException) {
                throw (ResourcePersistenceException) MockResourcePersistence.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockResourcePersistence.globalException);
            }
        }

        String methodName = "loadResources_long[]";

        Throwable exception = (Throwable) MockResourcePersistence.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof ResourcePersistenceException) {
                throw (ResourcePersistenceException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", longA0);
        List args = (List) MockResourcePersistence.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockResourcePersistence.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (Resource[]) MockResourcePersistence.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see ResourcePersistence#loadNotificationTypes(long[])
     * @throws ResourcePersistenceException
     */
    public NotificationType[] loadNotificationTypes(long[] longA0) throws ResourcePersistenceException {
        if (MockResourcePersistence.globalException != null) {
            if (MockResourcePersistence.globalException instanceof ResourcePersistenceException) {
                throw (ResourcePersistenceException) MockResourcePersistence.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockResourcePersistence.globalException);
            }
        }

        String methodName = "loadNotificationTypes_long[]";

        Throwable exception = (Throwable) MockResourcePersistence.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof ResourcePersistenceException) {
                throw (ResourcePersistenceException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", longA0);
        List args = (List) MockResourcePersistence.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockResourcePersistence.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (NotificationType[]) MockResourcePersistence.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see ResourcePersistence#loadResourceRoles(long[])
     * @throws ResourcePersistenceException
     */
    public ResourceRole[] loadResourceRoles(long[] longA0) throws ResourcePersistenceException {
        if (MockResourcePersistence.globalException != null) {
            if (MockResourcePersistence.globalException instanceof ResourcePersistenceException) {
                throw (ResourcePersistenceException) MockResourcePersistence.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockResourcePersistence.globalException);
            }
        }

        String methodName = "loadResourceRoles_long[]";

        Throwable exception = (Throwable) MockResourcePersistence.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof ResourcePersistenceException) {
                throw (ResourcePersistenceException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", longA0);
        List args = (List) MockResourcePersistence.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockResourcePersistence.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (ResourceRole[]) MockResourcePersistence.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see ResourcePersistence#loadNotifications(long[], long[], long[])
     * @throws ResourcePersistenceException
     */
    public Notification[] loadNotifications(long[] longA0, long[] longA1, long[] longA2) throws ResourcePersistenceException {
        if (MockResourcePersistence.globalException != null) {
            if (MockResourcePersistence.globalException instanceof ResourcePersistenceException) {
                throw (ResourcePersistenceException) MockResourcePersistence.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockResourcePersistence.globalException);
            }
        }

        String methodName = "loadNotifications_long[]_long[]_long[]";

        Throwable exception = (Throwable) MockResourcePersistence.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof ResourcePersistenceException) {
                throw (ResourcePersistenceException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", longA0);
        arguments.put("2", longA1);
        arguments.put("3", longA2);
        List args = (List) MockResourcePersistence.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockResourcePersistence.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (Notification[]) MockResourcePersistence.methodResults.get(methodName);

    }

    public Notification[] loadNotifications(CustomResultSet resultSet) throws ResourcePersistenceException
    {
        return null;
    }

     public ResourceRole[] loadResourceRoles(CustomResultSet resultSet) throws ResourcePersistenceException
    {
        return null;
    }

    public NotificationType[] loadNotificationTypes(CustomResultSet resultSet) throws ResourcePersistenceException
    {
        return null;
    }

    public Resource[] loadResources(CustomResultSet resultSet) throws ResourcePersistenceException
    {
        return null;
    }

    /**
     * <p>Sets the result to be returned by the specified method.</p>
     *
     * @param methodSignature a <code>String</code> uniquelly distinguishing the target method among other methods
     *        declared by the implemented interface/class.
     * @param result an <code>Object</code> representing the result to be returned by specified method.
     */
    public static void setMethodResult(String methodSignature, Object result) {
        MockResourcePersistence.methodResults.put(methodSignature, result);
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
        Map arguments = (Map) MockResourcePersistence.methodArguments.get(methodSignature);
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
        return (List) MockResourcePersistence.methodArguments.get(methodSignature);
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
            MockResourcePersistence.throwExceptions.put(methodSignature, exception);
        } else {
            MockResourcePersistence.throwExceptions.remove(methodSignature);
        }
    }

    /**
     * <p>Sets the exception to be thrown when the specified method is called.</p>
     *
     * @param exception a <code>Throwable</code> representing the exception to be thrown whenever any method is
     *        called. If this argument is <code>null</code> then no exception will be thrown.
     */
    public static void throwGlobalException(Throwable exception) {
        MockResourcePersistence.globalException = exception;
    }

    /**
     * <p>Releases the state of <code>MockResourcePersistence</code> so all collected method arguments,
     * configured method results and exceptions are lost.</p>
     */
    public static void releaseState() {
        MockResourcePersistence.methodArguments.clear();
        MockResourcePersistence.methodResults.clear();
        MockResourcePersistence.throwExceptions.clear();
        MockResourcePersistence.globalException = null;
    }

    /**
     * <p>Initializes the initial state for all created instances of <code>MockResourcePersistence</code> class.</p>
     */
    public static void init() {
    }

}
