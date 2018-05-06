/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */

package com.cronos.onlinereview.ajax.failuretests.mock;

import com.topcoder.management.review.ReviewManager;
import com.topcoder.management.review.data.Comment;
import com.topcoder.management.review.data.CommentType;
import com.topcoder.management.review.data.Review;
import com.topcoder.search.builder.filter.Filter;
import com.cronos.onlinereview.ajax.failuretests.TestDataFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>A mock implementation of {@link ReviewManager} class to be used for testing. Overrides the protected methods
 * declared by a super-class. The overridden methods are declared with package private access so only the test cases
 * could invoke them. The overridden methods simply call the corresponding method of a super-class.
 *
 * @author isv
 * @version 1.0
 */
public class MockReviewManager implements ReviewManager {

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
     * @see ReviewManager#createReview(Review, String)
     */
    public void createReview(Review review0, String string0) {
        if (MockReviewManager.globalException != null) {
            throw new RuntimeException("The test may not be configured properly", MockReviewManager.globalException);
        }

        String methodName = "createReview_Review_String";

        Throwable exception = (Throwable) MockReviewManager.throwExceptions.get(methodName);
        if (exception != null) {
            throw new RuntimeException("The test may not be configured properly", exception);
        }

        HashMap arguments = new HashMap();
        arguments.put("1", review0);
        arguments.put("2", string0);
        List args = (List) MockReviewManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            args = new ArrayList();
            MockReviewManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through {@link
     * #setMethodResult(String, Object)} method.</p>
     *
     * @see ReviewManager#updateReview(Review, String)
     */
    public void updateReview(Review review0, String string0) {
        if (MockReviewManager.globalException != null) {
            throw new RuntimeException("The test may not be configured properly", MockReviewManager.globalException);
        }

        String methodName = "updateReview_Review_String";

        Throwable exception = (Throwable) MockReviewManager.throwExceptions.get(methodName);
        if (exception != null) {
            throw new RuntimeException("The test may not be configured properly", exception);
        }

        HashMap arguments = new HashMap();
        arguments.put("1", review0);
        arguments.put("2", string0);
        List args = (List) MockReviewManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            args = new ArrayList();
            MockReviewManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through {@link
     * #setMethodResult(String, Object)} method.</p>
     *
     * @see ReviewManager#getReview(long)
     */
    public Review getReview(long long0) {
        if (MockReviewManager.globalException != null) {
            throw new RuntimeException("The test may not be configured properly", MockReviewManager.globalException);
        }

        String methodName = "getReview_long";

        Throwable exception = (Throwable) MockReviewManager.throwExceptions.get(methodName);
        if (exception != null) {
            throw new RuntimeException("The test may not be configured properly", exception);
        }

        HashMap arguments = new HashMap();
        arguments.put("1", new Long(long0));
        List args = (List) MockReviewManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            args = new ArrayList();
            MockReviewManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (Review) MockReviewManager.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through {@link
     * #setMethodResult(String, Object)} method.</p>
     *
     * @see ReviewManager#searchReviews(Filter, boolean)
     */
    public Review[] searchReviews(Filter filter0, boolean boolean0) {
        if (MockReviewManager.globalException != null) {
            throw new RuntimeException("The test may not be configured properly", MockReviewManager.globalException);
        }

        String methodName = "searchReviews_Filter_boolean";

        Throwable exception = (Throwable) MockReviewManager.throwExceptions.get(methodName);
        if (exception != null) {
            throw new RuntimeException("The test may not be configured properly", exception);
        }

        HashMap arguments = new HashMap();
        arguments.put("1", filter0);
        arguments.put("2", Boolean.valueOf(boolean0));
        List args = (List) MockReviewManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            args = new ArrayList();
            MockReviewManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (Review[]) MockReviewManager.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through {@link
     * #setMethodResult(String, Object)} method.</p>
     *
     * @see ReviewManager#getAllCommentTypes()
     */
    public CommentType[] getAllCommentTypes() {
        if (MockReviewManager.globalException != null) {
            throw new RuntimeException("The test may not be configured properly", MockReviewManager.globalException);
        }

        String methodName = "getAllCommentTypes";

        Throwable exception = (Throwable) MockReviewManager.throwExceptions.get(methodName);
        if (exception != null) {
            throw new RuntimeException("The test may not be configured properly", exception);
        }

        HashMap arguments = new HashMap();
        List args = (List) MockReviewManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            args = new ArrayList();
            MockReviewManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (CommentType[]) MockReviewManager.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through {@link
     * #setMethodResult(String, Object)} method.</p>
     *
     * @see ReviewManager#addReviewComment(long, Comment, String)
     */
    public void addReviewComment(long long0, Comment comment0, String string0) {
        if (MockReviewManager.globalException != null) {
            throw new RuntimeException("The test may not be configured properly", MockReviewManager.globalException);
        }

        String methodName = "addReviewComment_long_Comment_String";

        Throwable exception = (Throwable) MockReviewManager.throwExceptions.get(methodName);
        if (exception != null) {
            throw new RuntimeException("The test may not be configured properly", exception);
        }

        HashMap arguments = new HashMap();
        arguments.put("1", new Long(long0));
        arguments.put("2", comment0);
        arguments.put("3", string0);
        List args = (List) MockReviewManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            args = new ArrayList();
            MockReviewManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through {@link
     * #setMethodResult(String, Object)} method.</p>
     *
     * @see ReviewManager#addItemComment(long, Comment, String)
     */
    public void addItemComment(long long0, Comment comment0, String string0) {
        if (MockReviewManager.globalException != null) {
            throw new RuntimeException("The test may not be configured properly", MockReviewManager.globalException);
        }

        String methodName = "addItemComment_long_Comment_String";

        Throwable exception = (Throwable) MockReviewManager.throwExceptions.get(methodName);
        if (exception != null) {
            throw new RuntimeException("The test may not be configured properly", exception);
        }

        HashMap arguments = new HashMap();
        arguments.put("1", new Long(long0));
        arguments.put("2", comment0);
        arguments.put("3", string0);
        List args = (List) MockReviewManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            args = new ArrayList();
            MockReviewManager.methodArguments.put(methodName, args);
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
        MockReviewManager.methodResults.put(methodSignature, result);
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
        Map arguments = (Map) MockReviewManager.methodArguments.get(methodSignature);
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
        return (List) MockReviewManager.methodArguments.get(methodSignature);
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
            MockReviewManager.throwExceptions.put(methodSignature, exception);
        } else {
            MockReviewManager.throwExceptions.remove(methodSignature);
        }
    }

    /**
     * <p>Sets the exception to be thrown when the specified method is called.</p>
     *
     * @param exception a <code>Throwable</code> representing the exception to be thrown whenever any method is called.
     * If this argument is <code>null</code> then no exception will be thrown.
     */
    public static void throwGlobalException(Throwable exception) {
        MockReviewManager.globalException = exception;
    }

    /**
     * <p>Releases the state of <code>MockReviewManager</code> so all collected method arguments, configured method
     * results and exceptions are lost.</p>
     */
    public static void releaseState() {
        MockReviewManager.methodArguments.clear();
        MockReviewManager.methodResults.clear();
        MockReviewManager.throwExceptions.clear();
        MockReviewManager.globalException = null;
    }

    /**
     * <p>Initializes the initial state for all created instances of <code>MockReviewManager</code> class.</p>
     */
    public static void init() {
        setMethodResult("getAllCommentTypes", TestDataFactory.getCommentTypes());
    }

}
