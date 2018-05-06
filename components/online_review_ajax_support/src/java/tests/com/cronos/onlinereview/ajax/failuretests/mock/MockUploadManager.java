/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */

package com.cronos.onlinereview.ajax.failuretests.mock;

import com.topcoder.management.deliverable.Submission;
import com.topcoder.management.deliverable.SubmissionStatus;
import com.topcoder.management.deliverable.Upload;
import com.topcoder.management.deliverable.UploadManager;
import com.topcoder.management.deliverable.UploadStatus;
import com.topcoder.management.deliverable.UploadType;
import com.topcoder.management.deliverable.persistence.UploadPersistenceException;
import com.topcoder.search.builder.filter.Filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>A mock implementation of {@link UploadManager} class to be used for testing. Overrides the protected methods
 * declared by a super-class. The overridden methods are declared with package private access so only the test cases
 * could invoke them. The overridden methods simply call the corresponding method of a super-class.
 *
 * @author isv
 * @version 1.0
 */
public class MockUploadManager implements UploadManager {

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
     * @see UploadManager#createUpload(Upload, String)
     */
    public void createUpload(Upload upload0, String string0) {
        if (MockUploadManager.globalException != null) {
            throw new RuntimeException("The test may not be configured properly", MockUploadManager.globalException);
        }

        String methodName = "createUpload_Upload_String";

        Throwable exception = (Throwable) MockUploadManager.throwExceptions.get(methodName);
        if (exception != null) {
            throw new RuntimeException("The test may not be configured properly", exception);
        }

        HashMap arguments = new HashMap();
        arguments.put("1", upload0);
        arguments.put("2", string0);
        List args = (List) MockUploadManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            args = new ArrayList();
            MockUploadManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through {@link
     * #setMethodResult(String, Object)} method.</p>
     *
     * @see UploadManager#updateUpload(Upload, String)
     */
    public void updateUpload(Upload upload0, String string0) {
        if (MockUploadManager.globalException != null) {
            throw new RuntimeException("The test may not be configured properly", MockUploadManager.globalException);
        }

        String methodName = "updateUpload_Upload_String";

        Throwable exception = (Throwable) MockUploadManager.throwExceptions.get(methodName);
        if (exception != null) {
            throw new RuntimeException("The test may not be configured properly", exception);
        }

        HashMap arguments = new HashMap();
        arguments.put("1", upload0);
        arguments.put("2", string0);
        List args = (List) MockUploadManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            args = new ArrayList();
            MockUploadManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through {@link
     * #setMethodResult(String, Object)} method.</p>
     *
     * @see UploadManager#getUpload(long)
     */
    public Upload getUpload(long long0) {
        if (MockUploadManager.globalException != null) {
            throw new RuntimeException("The test may not be configured properly", MockUploadManager.globalException);
        }

        String methodName = "getUpload_long";

        Throwable exception = (Throwable) MockUploadManager.throwExceptions.get(methodName);
        if (exception != null) {
            throw new RuntimeException("The test may not be configured properly", exception);
        }

        HashMap arguments = new HashMap();
        arguments.put("1", new Long(long0));
        List args = (List) MockUploadManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            args = new ArrayList();
            MockUploadManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (Upload) MockUploadManager.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through {@link
     * #setMethodResult(String, Object)} method.</p>
     *
     * @see UploadManager#searchUploads(Filter)
     */
    public Upload[] searchUploads(Filter filter0) {
        if (MockUploadManager.globalException != null) {
            throw new RuntimeException("The test may not be configured properly", MockUploadManager.globalException);
        }

        String methodName = "searchUploads_Filter";

        Throwable exception = (Throwable) MockUploadManager.throwExceptions.get(methodName);
        if (exception != null) {
            throw new RuntimeException("The test may not be configured properly", exception);
        }

        HashMap arguments = new HashMap();
        arguments.put("1", filter0);
        List args = (List) MockUploadManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            args = new ArrayList();
            MockUploadManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (Upload[]) MockUploadManager.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through {@link
     * #setMethodResult(String, Object)} method.</p>
     *
     * @see UploadManager#getAllUploadTypes()
     */
    public UploadType[] getAllUploadTypes() {
        if (MockUploadManager.globalException != null) {
            throw new RuntimeException("The test may not be configured properly", MockUploadManager.globalException);
        }

        String methodName = "getAllUploadTypes";

        Throwable exception = (Throwable) MockUploadManager.throwExceptions.get(methodName);
        if (exception != null) {
            throw new RuntimeException("The test may not be configured properly", exception);
        }

        HashMap arguments = new HashMap();
        List args = (List) MockUploadManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            args = new ArrayList();
            MockUploadManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (UploadType[]) MockUploadManager.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through {@link
     * #setMethodResult(String, Object)} method.</p>
     *
     * @see UploadManager#getAllUploadStatuses()
     */
    public UploadStatus[] getAllUploadStatuses() {
        if (MockUploadManager.globalException != null) {
            throw new RuntimeException("The test may not be configured properly", MockUploadManager.globalException);
        }

        String methodName = "getAllUploadStatuses";

        Throwable exception = (Throwable) MockUploadManager.throwExceptions.get(methodName);
        if (exception != null) {
            throw new RuntimeException("The test may not be configured properly", exception);
        }

        HashMap arguments = new HashMap();
        List args = (List) MockUploadManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            args = new ArrayList();
            MockUploadManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (UploadStatus[]) MockUploadManager.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through {@link
     * #setMethodResult(String, Object)} method.</p>
     *
     * @see UploadManager#createSubmission(Submission, String)
     */
    public void createSubmission(Submission submission0, String string0) {
        if (MockUploadManager.globalException != null) {
            throw new RuntimeException("The test may not be configured properly", MockUploadManager.globalException);
        }

        String methodName = "createSubmission_Submission_String";

        Throwable exception = (Throwable) MockUploadManager.throwExceptions.get(methodName);
        if (exception != null) {
            throw new RuntimeException("The test may not be configured properly", exception);
        }

        HashMap arguments = new HashMap();
        arguments.put("1", submission0);
        arguments.put("2", string0);
        List args = (List) MockUploadManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            args = new ArrayList();
            MockUploadManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through {@link
     * #setMethodResult(String, Object)} method.</p>
     *
     * @see UploadManager#updateSubmission(Submission, String)
     */
    public void updateSubmission(Submission submission0, String string0) {
        if (MockUploadManager.globalException != null) {
            throw new RuntimeException("The test may not be configured properly", MockUploadManager.globalException);
        }

        String methodName = "updateSubmission_Submission_String";

        Throwable exception = (Throwable) MockUploadManager.throwExceptions.get(methodName);
        if (exception != null) {
            throw new RuntimeException("The test may not be configured properly", exception);
        }

        HashMap arguments = new HashMap();
        arguments.put("1", submission0);
        arguments.put("2", string0);
        List args = (List) MockUploadManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            args = new ArrayList();
            MockUploadManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through {@link
     * #setMethodResult(String, Object)} method.</p>
     *
     * @see UploadManager#getSubmission(long)
     */
    public Submission getSubmission(long long0) {
        if (MockUploadManager.globalException != null) {
            throw new RuntimeException("The test may not be configured properly", MockUploadManager.globalException);
        }

        String methodName = "getSubmission_long";

        Throwable exception = (Throwable) MockUploadManager.throwExceptions.get(methodName);
        if (exception != null) {
            throw new RuntimeException("The test may not be configured properly", exception);
        }

        HashMap arguments = new HashMap();
        arguments.put("1", new Long(long0));
        List args = (List) MockUploadManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            args = new ArrayList();
            MockUploadManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (Submission) MockUploadManager.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through {@link
     * #setMethodResult(String, Object)} method.</p>
     *
     * @see UploadManager#searchSubmissions(Filter)
     */
    public Submission[] searchSubmissions(Filter filter0) {
        if (MockUploadManager.globalException != null) {
            throw new RuntimeException("The test may not be configured properly", MockUploadManager.globalException);
        }

        String methodName = "searchSubmissions_Filter";

        Throwable exception = (Throwable) MockUploadManager.throwExceptions.get(methodName);
        if (exception != null) {
            throw new RuntimeException("The test may not be configured properly", exception);
        }

        HashMap arguments = new HashMap();
        arguments.put("1", filter0);
        List args = (List) MockUploadManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            args = new ArrayList();
            MockUploadManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (Submission[]) MockUploadManager.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through {@link
     * #setMethodResult(String, Object)} method.</p>
     *
     * @see UploadManager#getAllSubmissionStatuses()
     */
    public SubmissionStatus[] getAllSubmissionStatuses() {
        if (MockUploadManager.globalException != null) {
            throw new RuntimeException("The test may not be configured properly", MockUploadManager.globalException);
        }

        String methodName = "getAllSubmissionStatuses";

        Throwable exception = (Throwable) MockUploadManager.throwExceptions.get(methodName);
        if (exception != null) {
            throw new RuntimeException("The test may not be configured properly", exception);
        }

        HashMap arguments = new HashMap();
        List args = (List) MockUploadManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            args = new ArrayList();
            MockUploadManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (SubmissionStatus[]) MockUploadManager.methodResults.get(methodName);

    }

    /**
     * <p>Sets the result to be returned by the specified method.</p>
     *
     * @param methodSignature a <code>String</code> uniquelly distinguishing the target method among other methods
     * declared by the implemented interface/class.
     * @param result an <code>Object</code> representing the result to be returned by specified method.
     */
    public static void setMethodResult(String methodSignature, Object result) {
        MockUploadManager.methodResults.put(methodSignature, result);
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
        Map arguments = (Map) MockUploadManager.methodArguments.get(methodSignature);
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
        return (List) MockUploadManager.methodArguments.get(methodSignature);
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
            MockUploadManager.throwExceptions.put(methodSignature, exception);
        } else {
            MockUploadManager.throwExceptions.remove(methodSignature);
        }
    }

    /**
     * <p>Sets the exception to be thrown when the specified method is called.</p>
     *
     * @param exception a <code>Throwable</code> representing the exception to be thrown whenever any method is called.
     * If this argument is <code>null</code> then no exception will be thrown.
     */
    public static void throwGlobalException(Throwable exception) {
        MockUploadManager.globalException = exception;
    }

    /**
     * <p>Releases the state of <code>MockUploadManager</code> so all collected method arguments, configured method
     * results and exceptions are lost.</p>
     */
    public static void releaseState() {
        MockUploadManager.methodArguments.clear();
        MockUploadManager.methodResults.clear();
        MockUploadManager.throwExceptions.clear();
        MockUploadManager.globalException = null;
    }

    /**
     * <p>Initializes the initial state for all created instances of <code>MockUploadManager</code> class.</p>
     */
    public static void init() {
    }

    public void createSubmissionStatus(SubmissionStatus arg0, String arg1) throws UploadPersistenceException {
        // TODO Auto-generated method stub
        
    }

    public void createUploadStatus(UploadStatus arg0, String arg1) throws UploadPersistenceException {
        // TODO Auto-generated method stub
        
    }

    public void createUploadType(UploadType arg0, String arg1) throws UploadPersistenceException {
        // TODO Auto-generated method stub
        
    }

    public void removeSubmission(Submission arg0, String arg1) throws UploadPersistenceException {
        // TODO Auto-generated method stub
        
    }

    public void removeSubmissionStatus(SubmissionStatus arg0, String arg1) throws UploadPersistenceException {
        // TODO Auto-generated method stub
        
    }

    public void removeUpload(Upload arg0, String arg1) throws UploadPersistenceException {
        // TODO Auto-generated method stub
        
    }

    public void removeUploadStatus(UploadStatus arg0, String arg1) throws UploadPersistenceException {
        // TODO Auto-generated method stub
        
    }

    public void removeUploadType(UploadType arg0, String arg1) throws UploadPersistenceException {
        // TODO Auto-generated method stub
        
    }

    public void updateSubmissionStatus(SubmissionStatus arg0, String arg1) throws UploadPersistenceException {
        // TODO Auto-generated method stub
        
    }

    public void updateUploadStatus(UploadStatus arg0, String arg1) throws UploadPersistenceException {
        // TODO Auto-generated method stub
        
    }

    public void updateUploadType(UploadType arg0, String arg1) throws UploadPersistenceException {
        // TODO Auto-generated method stub
        
    }

}
