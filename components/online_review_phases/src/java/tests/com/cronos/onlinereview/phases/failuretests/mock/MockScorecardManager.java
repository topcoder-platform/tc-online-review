/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.cronos.onlinereview.phases.failuretests.mock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.topcoder.management.scorecard.PersistenceException;
import com.topcoder.management.scorecard.ScorecardIDInfo;
import com.topcoder.management.scorecard.ScorecardManager;
import com.topcoder.management.scorecard.data.QuestionType;
import com.topcoder.management.scorecard.data.Scorecard;
import com.topcoder.management.scorecard.data.ScorecardStatus;
import com.topcoder.management.scorecard.data.ScorecardType;
import com.topcoder.management.scorecard.validation.ValidationException;
import com.topcoder.search.builder.filter.Filter;

/**
 * <p>A mock implementation of {@link ScorecardManager} class to be used for testing.
 * Overrides the protected methods declared by a super-class. The overridden methods are declared with package private access
 * so only the test cases could invoke them. The overridden methods simply call the corresponding method of a super-class.
 * </p>
 * <p>The version 1.4 add one method: getDefaultScorecardsIDInfo, to suit new ScorecardManager.</p>
 *
 * @author  isv, stevenfrog
 * @version 1.4
 */
public class MockScorecardManager implements ScorecardManager {

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
     * <p>Constructs new <code>MockScorecardManager</code> instance.</p>
     */
    public MockScorecardManager() {
    }

    /**
     * <p>Constructs new <code>MockScorecardManager</code> instance.</p>
     */
    public MockScorecardManager(String namespace) {
    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see ScorecardManager#createScorecard(Scorecard, String)
     * @throws PersistenceException
     * @throws ValidationException
     */
    public void createScorecard(Scorecard scorecard0, String string0) throws PersistenceException, ValidationException {
        if (MockScorecardManager.globalException != null) {
            if (MockScorecardManager.globalException instanceof PersistenceException) {
                throw (PersistenceException) MockScorecardManager.globalException;
            } else if (MockScorecardManager.globalException instanceof ValidationException) {
                throw (ValidationException) MockScorecardManager.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockScorecardManager.globalException);
            }
        }

        String methodName = "createScorecard_Scorecard_String";

        Throwable exception = (Throwable) MockScorecardManager.throwExceptions.get(methodName);
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
        arguments.put("1", scorecard0);
        arguments.put("2", string0);
        List args = (List) MockScorecardManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockScorecardManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see ScorecardManager#updateScorecard(Scorecard, String)
     * @throws PersistenceException
     * @throws ValidationException
     */
    public void updateScorecard(Scorecard scorecard0, String string0) throws PersistenceException, ValidationException {
        if (MockScorecardManager.globalException != null) {
            if (MockScorecardManager.globalException instanceof PersistenceException) {
                throw (PersistenceException) MockScorecardManager.globalException;
            } else if (MockScorecardManager.globalException instanceof ValidationException) {
                throw (ValidationException) MockScorecardManager.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockScorecardManager.globalException);
            }
        }

        String methodName = "updateScorecard_Scorecard_String";

        Throwable exception = (Throwable) MockScorecardManager.throwExceptions.get(methodName);
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
        arguments.put("1", scorecard0);
        arguments.put("2", string0);
        List args = (List) MockScorecardManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockScorecardManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see ScorecardManager#getScorecard(long)
     * @throws PersistenceException
     */
    public Scorecard getScorecard(long long0) throws PersistenceException {
        if (MockScorecardManager.globalException != null) {
            if (MockScorecardManager.globalException instanceof PersistenceException) {
                throw (PersistenceException) MockScorecardManager.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockScorecardManager.globalException);
            }
        }

        String methodName = "getScorecard_long";

        Throwable exception = (Throwable) MockScorecardManager.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof PersistenceException) {
                throw (PersistenceException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", new Long(long0));
        List args = (List) MockScorecardManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockScorecardManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (Scorecard) MockScorecardManager.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see ScorecardManager#searchScorecards(Filter, boolean)
     * @throws PersistenceException
     */
    public Scorecard[] searchScorecards(Filter filter0, boolean boolean0) throws PersistenceException {
        if (MockScorecardManager.globalException != null) {
            if (MockScorecardManager.globalException instanceof PersistenceException) {
                throw (PersistenceException) MockScorecardManager.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockScorecardManager.globalException);
            }
        }

        String methodName = "searchScorecards_Filter_boolean";

        Throwable exception = (Throwable) MockScorecardManager.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof PersistenceException) {
                throw (PersistenceException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", filter0);
        arguments.put("2", Boolean.valueOf(boolean0));
        List args = (List) MockScorecardManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockScorecardManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (Scorecard[]) MockScorecardManager.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see ScorecardManager#getAllScorecardTypes()
     * @throws PersistenceException
     */
    public ScorecardType[] getAllScorecardTypes() throws PersistenceException {
        if (MockScorecardManager.globalException != null) {
            if (MockScorecardManager.globalException instanceof PersistenceException) {
                throw (PersistenceException) MockScorecardManager.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockScorecardManager.globalException);
            }
        }

        String methodName = "getAllScorecardTypes";

        Throwable exception = (Throwable) MockScorecardManager.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof PersistenceException) {
                throw (PersistenceException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        List args = (List) MockScorecardManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockScorecardManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (ScorecardType[]) MockScorecardManager.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see ScorecardManager#getAllQuestionTypes()
     * @throws PersistenceException
     */
    public QuestionType[] getAllQuestionTypes() throws PersistenceException {
        if (MockScorecardManager.globalException != null) {
            if (MockScorecardManager.globalException instanceof PersistenceException) {
                throw (PersistenceException) MockScorecardManager.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockScorecardManager.globalException);
            }
        }

        String methodName = "getAllQuestionTypes";

        Throwable exception = (Throwable) MockScorecardManager.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof PersistenceException) {
                throw (PersistenceException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        List args = (List) MockScorecardManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockScorecardManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (QuestionType[]) MockScorecardManager.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see ScorecardManager#getAllScorecardStatuses()
     * @throws PersistenceException
     */
    public ScorecardStatus[] getAllScorecardStatuses() throws PersistenceException {
        if (MockScorecardManager.globalException != null) {
            if (MockScorecardManager.globalException instanceof PersistenceException) {
                throw (PersistenceException) MockScorecardManager.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockScorecardManager.globalException);
            }
        }

        String methodName = "getAllScorecardStatuses";

        Throwable exception = (Throwable) MockScorecardManager.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof PersistenceException) {
                throw (PersistenceException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        List args = (List) MockScorecardManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockScorecardManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (ScorecardStatus[]) MockScorecardManager.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see ScorecardManager#getScorecards(long[], boolean)
     * @throws PersistenceException
     */
    public Scorecard[] getScorecards(long[] longA0, boolean boolean0) throws PersistenceException {
        if (MockScorecardManager.globalException != null) {
            if (MockScorecardManager.globalException instanceof PersistenceException) {
                throw (PersistenceException) MockScorecardManager.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockScorecardManager.globalException);
            }
        }

        String methodName = "getScorecards_long[]_boolean";

        Throwable exception = (Throwable) MockScorecardManager.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof PersistenceException) {
                throw (PersistenceException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", longA0);
        arguments.put("2", Boolean.valueOf(boolean0));
        List args = (List) MockScorecardManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockScorecardManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (Scorecard[]) MockScorecardManager.methodResults.get(methodName);

    }

    /**
     * <p>Sets the result to be returned by the specified method.</p>
     *
     * @param methodSignature a <code>String</code> uniquelly distinguishing the target method among other methods
     *        declared by the implemented interface/class.
     * @param result an <code>Object</code> representing the result to be returned by specified method.
     */
    public static void setMethodResult(String methodSignature, Object result) {
        MockScorecardManager.methodResults.put(methodSignature, result);
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
        Map arguments = (Map) MockScorecardManager.methodArguments.get(methodSignature);
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
        return (List) MockScorecardManager.methodArguments.get(methodSignature);
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
            MockScorecardManager.throwExceptions.put(methodSignature, exception);
        } else {
            MockScorecardManager.throwExceptions.remove(methodSignature);
        }
    }

    /**
     * <p>Sets the exception to be thrown when the specified method is called.</p>
     *
     * @param exception a <code>Throwable</code> representing the exception to be thrown whenever any method is
     *        called. If this argument is <code>null</code> then no exception will be thrown.
     */
    public static void throwGlobalException(Throwable exception) {
        MockScorecardManager.globalException = exception;
    }

    /**
     * <p>Releases the state of <code>MockScorecardManager</code> so all collected method arguments,
     * configured method results and exceptions are lost.</p>
     */
    public static void releaseState() {
        MockScorecardManager.methodArguments.clear();
        MockScorecardManager.methodResults.clear();
        MockScorecardManager.throwExceptions.clear();
        MockScorecardManager.globalException = null;
    }

    /**
     * <p>Initializes the initial state for all created instances of <code>MockScorecardManager</code> class.</p>
     */
    public static void init() {
    }

    /**
     * <p>Get the default scorecard ID info.</p>
     * @param arg0 the id
     * @return scorecard ID info
     * @throws PersistenceException if any error occurs
     */
    public ScorecardIDInfo[] getDefaultScorecardsIDInfo(long arg0) throws PersistenceException {
        return null;
    }

}
