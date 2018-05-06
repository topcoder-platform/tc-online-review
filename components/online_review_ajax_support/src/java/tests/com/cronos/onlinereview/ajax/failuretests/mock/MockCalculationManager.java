/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */

package com.cronos.onlinereview.ajax.failuretests.mock;

import com.topcoder.management.review.scorecalculator.CalculationManager;
import com.topcoder.management.review.scorecalculator.ConfigurationException;
//import com.topcoder.management.review.scorecalculator.ScoreCalculator;
import com.topcoder.management.review.data.Review;
import com.topcoder.management.scorecard.data.Scorecard;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

/**
 * <p>A mock implementation of {@link CalculationManager} class to be used for testing.
 * Overrides the protected methods declared by a super-class. The overridden methods are declared with package private access
 * so only the test cases could invoke them. The overridden methods simply call the corresponding method of a super-class.
 *
 * @author  isv
 * @version 1.0
 */
public class MockCalculationManager extends CalculationManager {

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
     * <p>Constructs new <code>MockCalculationManager</code> instance. Nothing special occurs here.</p>
     * @throws ConfigurationException 
     */
    public MockCalculationManager() throws ConfigurationException {
        super();
        MockCalculationManager.init();
    }

    /**
     * <p>Constructs new <code>MockCalculationManager</code> instance. Nothing special occurs here.</p>
     * @throws ConfigurationException 
     */
    public MockCalculationManager(String string0) throws ConfigurationException {
        super(string0);
        MockCalculationManager.init();
    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see CalculationManager#getScore(Scorecard, Review)
     */
    public float getScore(Scorecard scorecard0, Review review0) {
        if (MockCalculationManager.globalException != null) {
            throw new RuntimeException("The test may not be configured properly", MockCalculationManager.globalException);
        }

        String methodName = "getScore_Scorecard_Review";

        Throwable exception = (Throwable) MockCalculationManager.throwExceptions.get(methodName);
        if (exception != null) {
            throw new RuntimeException("The test may not be configured properly", exception);
        }

        HashMap arguments = new HashMap();
        arguments.put("1", scorecard0);
        arguments.put("2", review0);
        List args = (List) MockCalculationManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            args = new ArrayList();
            MockCalculationManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return ((Float) MockCalculationManager.methodResults.get(methodName)).floatValue();

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see CalculationManager#addScoreCalculator(long, coreCalculator)
     */
/*
    public void addScoreCalculator(long long0, ScoreCalculator scoreCalculator0) {
        if (MockCalculationManager.globalException != null) {
            throw new RuntimeException("The test may not be configured properly", MockCalculationManager.globalException);
        }

        String methodName = "addScoreCalculator_long_ScoreCalculator";

        Throwable exception = (Throwable) MockCalculationManager.throwExceptions.get(methodName);
        if (exception != null) {
            throw new RuntimeException("The test may not be configured properly", exception);
        }

        HashMap arguments = new HashMap();
        arguments.put("1", new Long(long0));
        arguments.put("2", scoreCalculator0);
        List args = (List) MockCalculationManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            args = new ArrayList();
            MockCalculationManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

    }
*/

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see CalculationManager#removeScoreCalculator(long)
     */
/*
    public ScoreCalculator removeScoreCalculator(long long0) {
        if (MockCalculationManager.globalException != null) {
            throw new RuntimeException("The test may not be configured properly", MockCalculationManager.globalException);
        }

        String methodName = "removeScoreCalculator_long";

        Throwable exception = (Throwable) MockCalculationManager.throwExceptions.get(methodName);
        if (exception != null) {
            throw new RuntimeException("The test may not be configured properly", exception);
        }

        HashMap arguments = new HashMap();
        arguments.put("1", new Long(long0));
        List args = (List) MockCalculationManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            args = new ArrayList();
            MockCalculationManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (ScoreCalculator) MockCalculationManager.methodResults.get(methodName);

    }
*/

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see CalculationManager#getScoreCalculator(long)
     */
/*
    public ScoreCalculator getScoreCalculator(long long0) {
        if (MockCalculationManager.globalException != null) {
            throw new RuntimeException("The test may not be configured properly", MockCalculationManager.globalException);
        }

        String methodName = "getScoreCalculator_long";

        Throwable exception = (Throwable) MockCalculationManager.throwExceptions.get(methodName);
        if (exception != null) {
            throw new RuntimeException("The test may not be configured properly", exception);
        }

        HashMap arguments = new HashMap();
        arguments.put("1", new Long(long0));
        List args = (List) MockCalculationManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            args = new ArrayList();
            MockCalculationManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (ScoreCalculator) MockCalculationManager.methodResults.get(methodName);

    }
*/

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see CalculationManager#clearScoreCalculators()
     */
    public void clearScoreCalculators() {
        if (MockCalculationManager.globalException != null) {
            throw new RuntimeException("The test may not be configured properly", MockCalculationManager.globalException);
        }

        String methodName = "clearScoreCalculators";

        Throwable exception = (Throwable) MockCalculationManager.throwExceptions.get(methodName);
        if (exception != null) {
            throw new RuntimeException("The test may not be configured properly", exception);
        }

        HashMap arguments = new HashMap();
        List args = (List) MockCalculationManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            args = new ArrayList();
            MockCalculationManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

    }

    /**
     * <p>Sets the result to be returned by the specified method.</p>
     *
     * @param methodSignature a <code>String</code> uniquelly distinguishing the target method among other methods
     *        declared by the implemented interface/class.
     * @param result an <code>Object</code> representing the result to be returned by specified method.
     */
    public static void setMethodResult(String methodSignature, Object result) {
        MockCalculationManager.methodResults.put(methodSignature, result);
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
        Map arguments = (Map) MockCalculationManager.methodArguments.get(methodSignature);
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
        return (List) MockCalculationManager.methodArguments.get(methodSignature);
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
            MockCalculationManager.throwExceptions.put(methodSignature, exception);
        } else {
            MockCalculationManager.throwExceptions.remove(methodSignature);
        }
    }

    /**
     * <p>Sets the exception to be thrown when the specified method is called.</p>
     *
     * @param exception a <code>Throwable</code> representing the exception to be thrown whenever any method is
     *        called. If this argument is <code>null</code> then no exception will be thrown.
     */
    public static void throwGlobalException(Throwable exception) {
        MockCalculationManager.globalException = exception;
    }

    /**
     * <p>Releases the state of <code>MockCalculationManager</code> so all collected method arguments,
     * configured method results and exceptions are lost.</p>
     */
    public static void releaseState() {
        MockCalculationManager.methodArguments.clear();
        MockCalculationManager.methodResults.clear();
        MockCalculationManager.throwExceptions.clear();
        MockCalculationManager.globalException = null;
    }

    /**
     * <p>Initializes the initial state for all created instances of <code>MockCalculationManager</code> class.</p>
     */
    public static void init() {
    }

}
