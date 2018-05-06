/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.cronos.onlinereview.phases.failuretests.mock;

import com.topcoder.management.deliverable.persistence.UploadPersistence;
import com.topcoder.management.deliverable.persistence.UploadPersistenceException;
import com.topcoder.management.deliverable.SubmissionType;
import com.topcoder.management.deliverable.UploadType;
import com.topcoder.management.deliverable.UploadStatus;
import com.topcoder.management.deliverable.SubmissionStatus;
import com.topcoder.management.deliverable.Upload;
import com.topcoder.management.deliverable.Submission;
import com.topcoder.util.sql.databaseabstraction.CustomResultSet;
import com.topcoder.db.connectionfactory.DBConnectionFactory;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

/**
 * <p>A mock implementation of {@link UploadPersistence} class to be used for testing.
 * Overrides the protected methods declared by a super-class. The overridden methods are declared with package private access
 * so only the test cases could invoke them. The overridden methods simply call the corresponding method of a super-class.
 * </p>
 * <p>The version 1.4 add some methods: addSubmissionType, getAllSubmissionTypeIds, loadSubmissionType,
 * loadSubmissionTypes, removeSubmissionType, updateSubmissionType, to suit new UploadManager.</p>
 *
 * @author  isv, stevenfrog
 * @version 1.4
 */
public class MockUploadPersistence implements UploadPersistence {

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
     * <p>Constructs new <code>MockUploadPersistence</code> instance.</p>
     */
    public MockUploadPersistence() {
    }

    /**
     * <p>Constructs new <code>MockUploadPersistence</code> instance.</p>
     */
    public MockUploadPersistence(String namespace) {
    }

    /**
     * <p>Constructs new <code>MockUploadPersistence</code> instance.</p>
     */
    public MockUploadPersistence(DBConnectionFactory factory, String namespace) {
    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see UploadPersistence#addUploadType(UploadType)
     * @throws UploadPersistenceException
     */
    public void addUploadType(UploadType uploadType0) throws UploadPersistenceException {
        if (MockUploadPersistence.globalException != null) {
            if (MockUploadPersistence.globalException instanceof UploadPersistenceException) {
                throw (UploadPersistenceException) MockUploadPersistence.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockUploadPersistence.globalException);
            }
        }

        String methodName = "addUploadType_UploadType";

        Throwable exception = (Throwable) MockUploadPersistence.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof UploadPersistenceException) {
                throw (UploadPersistenceException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", uploadType0);
        List args = (List) MockUploadPersistence.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockUploadPersistence.methodArguments.put(methodName, args);
        }
        args.add(arguments);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see UploadPersistence#removeUploadType(UploadType)
     * @throws UploadPersistenceException
     */
    public void removeUploadType(UploadType uploadType0) throws UploadPersistenceException {
        if (MockUploadPersistence.globalException != null) {
            if (MockUploadPersistence.globalException instanceof UploadPersistenceException) {
                throw (UploadPersistenceException) MockUploadPersistence.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockUploadPersistence.globalException);
            }
        }

        String methodName = "removeUploadType_UploadType";

        Throwable exception = (Throwable) MockUploadPersistence.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof UploadPersistenceException) {
                throw (UploadPersistenceException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", uploadType0);
        List args = (List) MockUploadPersistence.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockUploadPersistence.methodArguments.put(methodName, args);
        }
        args.add(arguments);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see UploadPersistence#updateUploadType(UploadType)
     * @throws UploadPersistenceException
     */
    public void updateUploadType(UploadType uploadType0) throws UploadPersistenceException {
        if (MockUploadPersistence.globalException != null) {
            if (MockUploadPersistence.globalException instanceof UploadPersistenceException) {
                throw (UploadPersistenceException) MockUploadPersistence.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockUploadPersistence.globalException);
            }
        }

        String methodName = "updateUploadType_UploadType";

        Throwable exception = (Throwable) MockUploadPersistence.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof UploadPersistenceException) {
                throw (UploadPersistenceException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", uploadType0);
        List args = (List) MockUploadPersistence.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockUploadPersistence.methodArguments.put(methodName, args);
        }
        args.add(arguments);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see UploadPersistence#loadUploadType(long)
     * @throws UploadPersistenceException
     */
    public UploadType loadUploadType(long long0) throws UploadPersistenceException {
        if (MockUploadPersistence.globalException != null) {
            if (MockUploadPersistence.globalException instanceof UploadPersistenceException) {
                throw (UploadPersistenceException) MockUploadPersistence.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockUploadPersistence.globalException);
            }
        }

        String methodName = "loadUploadType_long";

        Throwable exception = (Throwable) MockUploadPersistence.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof UploadPersistenceException) {
                throw (UploadPersistenceException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", new Long(long0));
        List args = (List) MockUploadPersistence.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockUploadPersistence.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (UploadType) MockUploadPersistence.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see UploadPersistence#loadUploadTypes(long[])
     * @throws UploadPersistenceException
     */
    public UploadType[] loadUploadTypes(long[] longA0) throws UploadPersistenceException {
        if (MockUploadPersistence.globalException != null) {
            if (MockUploadPersistence.globalException instanceof UploadPersistenceException) {
                throw (UploadPersistenceException) MockUploadPersistence.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockUploadPersistence.globalException);
            }
        }

        String methodName = "loadUploadTypes_long[]";

        Throwable exception = (Throwable) MockUploadPersistence.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof UploadPersistenceException) {
                throw (UploadPersistenceException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", longA0);
        List args = (List) MockUploadPersistence.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockUploadPersistence.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (UploadType[]) MockUploadPersistence.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see UploadPersistence#getAllUploadTypeIds()
     * @throws UploadPersistenceException
     */
    public long[] getAllUploadTypeIds() throws UploadPersistenceException {
        if (MockUploadPersistence.globalException != null) {
            if (MockUploadPersistence.globalException instanceof UploadPersistenceException) {
                throw (UploadPersistenceException) MockUploadPersistence.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockUploadPersistence.globalException);
            }
        }

        String methodName = "getAllUploadTypeIds";

        Throwable exception = (Throwable) MockUploadPersistence.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof UploadPersistenceException) {
                throw (UploadPersistenceException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        List args = (List) MockUploadPersistence.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockUploadPersistence.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (long[]) MockUploadPersistence.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see UploadPersistence#addUploadStatus(UploadStatus)
     * @throws UploadPersistenceException
     */
    public void addUploadStatus(UploadStatus uploadStatus0) throws UploadPersistenceException {
        if (MockUploadPersistence.globalException != null) {
            if (MockUploadPersistence.globalException instanceof UploadPersistenceException) {
                throw (UploadPersistenceException) MockUploadPersistence.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockUploadPersistence.globalException);
            }
        }

        String methodName = "addUploadStatus_UploadStatus";

        Throwable exception = (Throwable) MockUploadPersistence.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof UploadPersistenceException) {
                throw (UploadPersistenceException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", uploadStatus0);
        List args = (List) MockUploadPersistence.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockUploadPersistence.methodArguments.put(methodName, args);
        }
        args.add(arguments);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see UploadPersistence#removeUploadStatus(UploadStatus)
     * @throws UploadPersistenceException
     */
    public void removeUploadStatus(UploadStatus uploadStatus0) throws UploadPersistenceException {
        if (MockUploadPersistence.globalException != null) {
            if (MockUploadPersistence.globalException instanceof UploadPersistenceException) {
                throw (UploadPersistenceException) MockUploadPersistence.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockUploadPersistence.globalException);
            }
        }

        String methodName = "removeUploadStatus_UploadStatus";

        Throwable exception = (Throwable) MockUploadPersistence.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof UploadPersistenceException) {
                throw (UploadPersistenceException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", uploadStatus0);
        List args = (List) MockUploadPersistence.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockUploadPersistence.methodArguments.put(methodName, args);
        }
        args.add(arguments);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see UploadPersistence#updateUploadStatus(UploadStatus)
     * @throws UploadPersistenceException
     */
    public void updateUploadStatus(UploadStatus uploadStatus0) throws UploadPersistenceException {
        if (MockUploadPersistence.globalException != null) {
            if (MockUploadPersistence.globalException instanceof UploadPersistenceException) {
                throw (UploadPersistenceException) MockUploadPersistence.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockUploadPersistence.globalException);
            }
        }

        String methodName = "updateUploadStatus_UploadStatus";

        Throwable exception = (Throwable) MockUploadPersistence.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof UploadPersistenceException) {
                throw (UploadPersistenceException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", uploadStatus0);
        List args = (List) MockUploadPersistence.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockUploadPersistence.methodArguments.put(methodName, args);
        }
        args.add(arguments);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see UploadPersistence#loadUploadStatus(long)
     * @throws UploadPersistenceException
     */
    public UploadStatus loadUploadStatus(long long0) throws UploadPersistenceException {
        if (MockUploadPersistence.globalException != null) {
            if (MockUploadPersistence.globalException instanceof UploadPersistenceException) {
                throw (UploadPersistenceException) MockUploadPersistence.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockUploadPersistence.globalException);
            }
        }

        String methodName = "loadUploadStatus_long";

        Throwable exception = (Throwable) MockUploadPersistence.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof UploadPersistenceException) {
                throw (UploadPersistenceException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", new Long(long0));
        List args = (List) MockUploadPersistence.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockUploadPersistence.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (UploadStatus) MockUploadPersistence.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see UploadPersistence#loadUploadStatuses(long[])
     * @throws UploadPersistenceException
     */
    public UploadStatus[] loadUploadStatuses(long[] longA0) throws UploadPersistenceException {
        if (MockUploadPersistence.globalException != null) {
            if (MockUploadPersistence.globalException instanceof UploadPersistenceException) {
                throw (UploadPersistenceException) MockUploadPersistence.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockUploadPersistence.globalException);
            }
        }

        String methodName = "loadUploadStatuses_long[]";

        Throwable exception = (Throwable) MockUploadPersistence.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof UploadPersistenceException) {
                throw (UploadPersistenceException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", longA0);
        List args = (List) MockUploadPersistence.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockUploadPersistence.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (UploadStatus[]) MockUploadPersistence.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see UploadPersistence#getAllUploadStatusIds()
     * @throws UploadPersistenceException
     */
    public long[] getAllUploadStatusIds() throws UploadPersistenceException {
        if (MockUploadPersistence.globalException != null) {
            if (MockUploadPersistence.globalException instanceof UploadPersistenceException) {
                throw (UploadPersistenceException) MockUploadPersistence.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockUploadPersistence.globalException);
            }
        }

        String methodName = "getAllUploadStatusIds";

        Throwable exception = (Throwable) MockUploadPersistence.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof UploadPersistenceException) {
                throw (UploadPersistenceException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        List args = (List) MockUploadPersistence.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockUploadPersistence.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (long[]) MockUploadPersistence.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see UploadPersistence#addSubmissionStatus(SubmissionStatus)
     * @throws UploadPersistenceException
     */
    public void addSubmissionStatus(SubmissionStatus submissionStatus0) throws UploadPersistenceException {
        if (MockUploadPersistence.globalException != null) {
            if (MockUploadPersistence.globalException instanceof UploadPersistenceException) {
                throw (UploadPersistenceException) MockUploadPersistence.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockUploadPersistence.globalException);
            }
        }

        String methodName = "addSubmissionStatus_SubmissionStatus";

        Throwable exception = (Throwable) MockUploadPersistence.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof UploadPersistenceException) {
                throw (UploadPersistenceException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", submissionStatus0);
        List args = (List) MockUploadPersistence.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockUploadPersistence.methodArguments.put(methodName, args);
        }
        args.add(arguments);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see UploadPersistence#removeSubmissionStatus(SubmissionStatus)
     * @throws UploadPersistenceException
     */
    public void removeSubmissionStatus(SubmissionStatus submissionStatus0) throws UploadPersistenceException {
        if (MockUploadPersistence.globalException != null) {
            if (MockUploadPersistence.globalException instanceof UploadPersistenceException) {
                throw (UploadPersistenceException) MockUploadPersistence.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockUploadPersistence.globalException);
            }
        }

        String methodName = "removeSubmissionStatus_SubmissionStatus";

        Throwable exception = (Throwable) MockUploadPersistence.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof UploadPersistenceException) {
                throw (UploadPersistenceException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", submissionStatus0);
        List args = (List) MockUploadPersistence.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockUploadPersistence.methodArguments.put(methodName, args);
        }
        args.add(arguments);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see UploadPersistence#updateSubmissionStatus(SubmissionStatus)
     * @throws UploadPersistenceException
     */
    public void updateSubmissionStatus(SubmissionStatus submissionStatus0) throws UploadPersistenceException {
        if (MockUploadPersistence.globalException != null) {
            if (MockUploadPersistence.globalException instanceof UploadPersistenceException) {
                throw (UploadPersistenceException) MockUploadPersistence.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockUploadPersistence.globalException);
            }
        }

        String methodName = "updateSubmissionStatus_SubmissionStatus";

        Throwable exception = (Throwable) MockUploadPersistence.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof UploadPersistenceException) {
                throw (UploadPersistenceException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", submissionStatus0);
        List args = (List) MockUploadPersistence.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockUploadPersistence.methodArguments.put(methodName, args);
        }
        args.add(arguments);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see UploadPersistence#loadSubmissionStatus(long)
     * @throws UploadPersistenceException
     */
    public SubmissionStatus loadSubmissionStatus(long long0) throws UploadPersistenceException {
        if (MockUploadPersistence.globalException != null) {
            if (MockUploadPersistence.globalException instanceof UploadPersistenceException) {
                throw (UploadPersistenceException) MockUploadPersistence.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockUploadPersistence.globalException);
            }
        }

        String methodName = "loadSubmissionStatus_long";

        Throwable exception = (Throwable) MockUploadPersistence.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof UploadPersistenceException) {
                throw (UploadPersistenceException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", new Long(long0));
        List args = (List) MockUploadPersistence.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockUploadPersistence.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (SubmissionStatus) MockUploadPersistence.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see UploadPersistence#loadSubmissionStatuses(long[])
     * @throws UploadPersistenceException
     */
    public SubmissionStatus[] loadSubmissionStatuses(long[] longA0) throws UploadPersistenceException {
        if (MockUploadPersistence.globalException != null) {
            if (MockUploadPersistence.globalException instanceof UploadPersistenceException) {
                throw (UploadPersistenceException) MockUploadPersistence.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockUploadPersistence.globalException);
            }
        }

        String methodName = "loadSubmissionStatuses_long[]";

        Throwable exception = (Throwable) MockUploadPersistence.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof UploadPersistenceException) {
                throw (UploadPersistenceException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", longA0);
        List args = (List) MockUploadPersistence.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockUploadPersistence.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (SubmissionStatus[]) MockUploadPersistence.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see UploadPersistence#getAllSubmissionStatusIds()
     * @throws UploadPersistenceException
     */
    public long[] getAllSubmissionStatusIds() throws UploadPersistenceException {
        if (MockUploadPersistence.globalException != null) {
            if (MockUploadPersistence.globalException instanceof UploadPersistenceException) {
                throw (UploadPersistenceException) MockUploadPersistence.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockUploadPersistence.globalException);
            }
        }

        String methodName = "getAllSubmissionStatusIds";

        Throwable exception = (Throwable) MockUploadPersistence.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof UploadPersistenceException) {
                throw (UploadPersistenceException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        List args = (List) MockUploadPersistence.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockUploadPersistence.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (long[]) MockUploadPersistence.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see UploadPersistence#addUpload(Upload)
     * @throws UploadPersistenceException
     */
    public void addUpload(Upload upload0) throws UploadPersistenceException {
        if (MockUploadPersistence.globalException != null) {
            if (MockUploadPersistence.globalException instanceof UploadPersistenceException) {
                throw (UploadPersistenceException) MockUploadPersistence.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockUploadPersistence.globalException);
            }
        }

        String methodName = "addUpload_Upload";

        Throwable exception = (Throwable) MockUploadPersistence.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof UploadPersistenceException) {
                throw (UploadPersistenceException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", upload0);
        List args = (List) MockUploadPersistence.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockUploadPersistence.methodArguments.put(methodName, args);
        }
        args.add(arguments);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see UploadPersistence#removeUpload(Upload)
     * @throws UploadPersistenceException
     */
    public void removeUpload(Upload upload0) throws UploadPersistenceException {
        if (MockUploadPersistence.globalException != null) {
            if (MockUploadPersistence.globalException instanceof UploadPersistenceException) {
                throw (UploadPersistenceException) MockUploadPersistence.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockUploadPersistence.globalException);
            }
        }

        String methodName = "removeUpload_Upload";

        Throwable exception = (Throwable) MockUploadPersistence.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof UploadPersistenceException) {
                throw (UploadPersistenceException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", upload0);
        List args = (List) MockUploadPersistence.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockUploadPersistence.methodArguments.put(methodName, args);
        }
        args.add(arguments);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see UploadPersistence#updateUpload(Upload)
     * @throws UploadPersistenceException
     */
    public void updateUpload(Upload upload0) throws UploadPersistenceException {
        if (MockUploadPersistence.globalException != null) {
            if (MockUploadPersistence.globalException instanceof UploadPersistenceException) {
                throw (UploadPersistenceException) MockUploadPersistence.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockUploadPersistence.globalException);
            }
        }

        String methodName = "updateUpload_Upload";

        Throwable exception = (Throwable) MockUploadPersistence.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof UploadPersistenceException) {
                throw (UploadPersistenceException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", upload0);
        List args = (List) MockUploadPersistence.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockUploadPersistence.methodArguments.put(methodName, args);
        }
        args.add(arguments);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see UploadPersistence#loadUpload(long)
     * @throws UploadPersistenceException
     */
    public Upload loadUpload(long long0) throws UploadPersistenceException {
        if (MockUploadPersistence.globalException != null) {
            if (MockUploadPersistence.globalException instanceof UploadPersistenceException) {
                throw (UploadPersistenceException) MockUploadPersistence.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockUploadPersistence.globalException);
            }
        }

        String methodName = "loadUpload_long";

        Throwable exception = (Throwable) MockUploadPersistence.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof UploadPersistenceException) {
                throw (UploadPersistenceException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", new Long(long0));
        List args = (List) MockUploadPersistence.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockUploadPersistence.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (Upload) MockUploadPersistence.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see UploadPersistence#loadUploads(long[])
     * @throws UploadPersistenceException
     */
    public Upload[] loadUploads(long[] longA0) throws UploadPersistenceException {
        if (MockUploadPersistence.globalException != null) {
            if (MockUploadPersistence.globalException instanceof UploadPersistenceException) {
                throw (UploadPersistenceException) MockUploadPersistence.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockUploadPersistence.globalException);
            }
        }

        String methodName = "loadUploads_long[]";

        Throwable exception = (Throwable) MockUploadPersistence.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof UploadPersistenceException) {
                throw (UploadPersistenceException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", longA0);
        List args = (List) MockUploadPersistence.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockUploadPersistence.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (Upload[]) MockUploadPersistence.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see UploadPersistence#addSubmission(Submission)
     * @throws UploadPersistenceException
     */
    public void addSubmission(Submission submission0) throws UploadPersistenceException {
        if (MockUploadPersistence.globalException != null) {
            if (MockUploadPersistence.globalException instanceof UploadPersistenceException) {
                throw (UploadPersistenceException) MockUploadPersistence.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockUploadPersistence.globalException);
            }
        }

        String methodName = "addSubmission_Submission";

        Throwable exception = (Throwable) MockUploadPersistence.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof UploadPersistenceException) {
                throw (UploadPersistenceException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", submission0);
        List args = (List) MockUploadPersistence.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockUploadPersistence.methodArguments.put(methodName, args);
        }
        args.add(arguments);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see UploadPersistence#removeSubmission(Submission)
     * @throws UploadPersistenceException
     */
    public void removeSubmission(Submission submission0) throws UploadPersistenceException {
        if (MockUploadPersistence.globalException != null) {
            if (MockUploadPersistence.globalException instanceof UploadPersistenceException) {
                throw (UploadPersistenceException) MockUploadPersistence.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockUploadPersistence.globalException);
            }
        }

        String methodName = "removeSubmission_Submission";

        Throwable exception = (Throwable) MockUploadPersistence.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof UploadPersistenceException) {
                throw (UploadPersistenceException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", submission0);
        List args = (List) MockUploadPersistence.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockUploadPersistence.methodArguments.put(methodName, args);
        }
        args.add(arguments);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see UploadPersistence#updateSubmission(Submission)
     * @throws UploadPersistenceException
     */
    public void updateSubmission(Submission submission0) throws UploadPersistenceException {
        if (MockUploadPersistence.globalException != null) {
            if (MockUploadPersistence.globalException instanceof UploadPersistenceException) {
                throw (UploadPersistenceException) MockUploadPersistence.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockUploadPersistence.globalException);
            }
        }

        String methodName = "updateSubmission_Submission";

        Throwable exception = (Throwable) MockUploadPersistence.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof UploadPersistenceException) {
                throw (UploadPersistenceException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", submission0);
        List args = (List) MockUploadPersistence.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockUploadPersistence.methodArguments.put(methodName, args);
        }
        args.add(arguments);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see UploadPersistence#loadSubmission(long)
     * @throws UploadPersistenceException
     */
    public Submission loadSubmission(long long0) throws UploadPersistenceException {
        if (MockUploadPersistence.globalException != null) {
            if (MockUploadPersistence.globalException instanceof UploadPersistenceException) {
                throw (UploadPersistenceException) MockUploadPersistence.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockUploadPersistence.globalException);
            }
        }

        String methodName = "loadSubmission_long";

        Throwable exception = (Throwable) MockUploadPersistence.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof UploadPersistenceException) {
                throw (UploadPersistenceException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", new Long(long0));
        List args = (List) MockUploadPersistence.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockUploadPersistence.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (Submission) MockUploadPersistence.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see UploadPersistence#loadSubmissions(long[])
     * @throws UploadPersistenceException
     */
    public Submission[] loadSubmissions(long[] longA0) throws UploadPersistenceException {
        if (MockUploadPersistence.globalException != null) {
            if (MockUploadPersistence.globalException instanceof UploadPersistenceException) {
                throw (UploadPersistenceException) MockUploadPersistence.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockUploadPersistence.globalException);
            }
        }

        String methodName = "loadSubmissions_long[]";

        Throwable exception = (Throwable) MockUploadPersistence.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof UploadPersistenceException) {
                throw (UploadPersistenceException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", longA0);
        List args = (List) MockUploadPersistence.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockUploadPersistence.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (Submission[]) MockUploadPersistence.methodResults.get(methodName);

    }

    /**
     * <p>Sets the result to be returned by the specified method.</p>
     *
     * @param methodSignature a <code>String</code> uniquelly distinguishing the target method among other methods
     *        declared by the implemented interface/class.
     * @param result an <code>Object</code> representing the result to be returned by specified method.
     */
    public static void setMethodResult(String methodSignature, Object result) {
        MockUploadPersistence.methodResults.put(methodSignature, result);
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
        Map arguments = (Map) MockUploadPersistence.methodArguments.get(methodSignature);
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
        return (List) MockUploadPersistence.methodArguments.get(methodSignature);
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
            MockUploadPersistence.throwExceptions.put(methodSignature, exception);
        } else {
            MockUploadPersistence.throwExceptions.remove(methodSignature);
        }
    }

    /**
     * <p>Sets the exception to be thrown when the specified method is called.</p>
     *
     * @param exception a <code>Throwable</code> representing the exception to be thrown whenever any method is
     *        called. If this argument is <code>null</code> then no exception will be thrown.
     */
    public static void throwGlobalException(Throwable exception) {
        MockUploadPersistence.globalException = exception;
    }

    /**
     * <p>Releases the state of <code>MockUploadPersistence</code> so all collected method arguments,
     * configured method results and exceptions are lost.</p>
     */
    public static void releaseState() {
        MockUploadPersistence.methodArguments.clear();
        MockUploadPersistence.methodResults.clear();
        MockUploadPersistence.throwExceptions.clear();
        MockUploadPersistence.globalException = null;
    }

    /**
     * <p>Initializes the initial state for all created instances of <code>MockUploadPersistence</code> class.</p>
     */
    public static void init() {
    }

    /**
     * <p>Load submissions.</p>
     * @param resultSet the custom result set
     * @return the submissions
     */
	public Submission[] loadSubmissions(CustomResultSet resultSet) throws UploadPersistenceException {
		throw new IllegalStateException("not implemented");
	}

	/**
	 * <p>Load uploads</p>
	 * @param resultSet the custom result set
     * @return the uploads
	 */
	public Upload[] loadUploads(CustomResultSet resultSet) throws UploadPersistenceException {
		throw new IllegalStateException("not implemented");
	}

	/**
	 * <p>Add submission type.</p>
	 * @param arg0 the submission type
	 * @throws UploadPersistenceException if any error occurs
	 */
    public void addSubmissionType(SubmissionType arg0) throws UploadPersistenceException {
        // Empty
    }

    /**
     * <p>get all submission type ids.</p>
     * @return the ids
     * @throws UploadPersistenceException if any error occurs
     */
    public long[] getAllSubmissionTypeIds() throws UploadPersistenceException {
        return null;
    }

    /**
     * <p>load submission type.</p>
     * @param arg0 the id
     * @return the type
     * @throws UploadPersistenceException if any error occurs
     */
    public SubmissionType loadSubmissionType(long arg0) throws UploadPersistenceException {
        return null;
    }

    /**
     * <p>load submission types.</p>
     * @param arg0 the ids
     * @return the types
     * @throws UploadPersistenceException if any error occurs
     */
    public SubmissionType[] loadSubmissionTypes(long[] arg0) throws UploadPersistenceException {
        return null;
    }

    /**
     * <p>remove submission type.</p>
     * @param arg0 the submission type
     * @throws UploadPersistenceException if any error occurs
     */
    public void removeSubmissionType(SubmissionType arg0) throws UploadPersistenceException {
        // Empty
    }

    /**
     * <p>update submission type.</p>
     * @param arg0 the submission type
     * @throws UploadPersistenceException if any error occurs
     */
    public void updateSubmissionType(SubmissionType arg0) throws UploadPersistenceException {
        // Empty
    }

}
