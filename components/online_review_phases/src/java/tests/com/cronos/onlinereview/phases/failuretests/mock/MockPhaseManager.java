/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.cronos.onlinereview.phases.failuretests.mock;

import com.topcoder.management.phase.PhaseManager;
import com.topcoder.management.phase.PhaseManagementException;
import com.topcoder.management.phase.PhaseHandler;
import com.topcoder.management.phase.PhaseOperationEnum;
import com.topcoder.management.phase.HandlerRegistryInfo;
import com.topcoder.management.phase.PhaseValidator;
import com.topcoder.project.phases.Project;
import com.topcoder.project.phases.PhaseType;
import com.topcoder.project.phases.PhaseStatus;
import com.topcoder.project.phases.Phase;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

/**
 * <p>A mock implementation of {@link PhaseManager} class to be used for testing.
 * Overrides the protected methods declared by a super-class. The overridden methods are declared with package private access
 * so only the test cases could invoke them. The overridden methods simply call the corresponding method of a super-class.
 *
 * @author  isv
 * @version 1.0
 */
public class MockPhaseManager implements PhaseManager {

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
     * <p>Constructs new <code>MockPhaseManager</code> instance.</p>
     */
    public MockPhaseManager() {
    }

    /**
     * <p>Constructs new <code>MockPhaseManager</code> instance.</p>
     */
    public MockPhaseManager(String namespace) {
    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see PhaseManager#updatePhases(Project, String)
     * @throws PhaseManagementException
     */
    public void updatePhases(Project project0, String string0) throws PhaseManagementException {
        if (MockPhaseManager.globalException != null) {
            if (MockPhaseManager.globalException instanceof PhaseManagementException) {
                throw (PhaseManagementException) MockPhaseManager.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockPhaseManager.globalException);
            }
        }

        String methodName = "updatePhases_Project_String";

        Throwable exception = (Throwable) MockPhaseManager.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof PhaseManagementException) {
                throw (PhaseManagementException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", project0);
        arguments.put("2", string0);
        List args = (List) MockPhaseManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockPhaseManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see PhaseManager#getPhases(long)
     * @throws PhaseManagementException
     */
    public Project getPhases(long long0) throws PhaseManagementException {
        if (MockPhaseManager.globalException != null) {
            if (MockPhaseManager.globalException instanceof PhaseManagementException) {
                throw (PhaseManagementException) MockPhaseManager.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockPhaseManager.globalException);
            }
        }

        String methodName = "getPhases_long";

        Throwable exception = (Throwable) MockPhaseManager.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof PhaseManagementException) {
                throw (PhaseManagementException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", new Long(long0));
        List args = (List) MockPhaseManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockPhaseManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (Project) MockPhaseManager.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see PhaseManager#getPhases(long[])
     * @throws PhaseManagementException
     */
    public Project[] getPhases(long[] longA0) throws PhaseManagementException {
        if (MockPhaseManager.globalException != null) {
            if (MockPhaseManager.globalException instanceof PhaseManagementException) {
                throw (PhaseManagementException) MockPhaseManager.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockPhaseManager.globalException);
            }
        }

        String methodName = "getPhases_long[]";

        Throwable exception = (Throwable) MockPhaseManager.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof PhaseManagementException) {
                throw (PhaseManagementException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", longA0);
        List args = (List) MockPhaseManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockPhaseManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (Project[]) MockPhaseManager.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see PhaseManager#getAllPhaseTypes()
     * @throws PhaseManagementException
     */
    public PhaseType[] getAllPhaseTypes() throws PhaseManagementException {
        if (MockPhaseManager.globalException != null) {
            if (MockPhaseManager.globalException instanceof PhaseManagementException) {
                throw (PhaseManagementException) MockPhaseManager.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockPhaseManager.globalException);
            }
        }

        String methodName = "getAllPhaseTypes";

        Throwable exception = (Throwable) MockPhaseManager.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof PhaseManagementException) {
                throw (PhaseManagementException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        List args = (List) MockPhaseManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockPhaseManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (PhaseType[]) MockPhaseManager.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see PhaseManager#getAllPhaseStatuses()
     * @throws PhaseManagementException
     */
    public PhaseStatus[] getAllPhaseStatuses() throws PhaseManagementException {
        if (MockPhaseManager.globalException != null) {
            if (MockPhaseManager.globalException instanceof PhaseManagementException) {
                throw (PhaseManagementException) MockPhaseManager.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockPhaseManager.globalException);
            }
        }

        String methodName = "getAllPhaseStatuses";

        Throwable exception = (Throwable) MockPhaseManager.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof PhaseManagementException) {
                throw (PhaseManagementException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        List args = (List) MockPhaseManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockPhaseManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (PhaseStatus[]) MockPhaseManager.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see PhaseManager#canStart(Phase)
     */
    public boolean canStart(Phase phase0) {
        if (MockPhaseManager.globalException != null) {
            throw new RuntimeException("The test may not be configured properly", MockPhaseManager.globalException);
        }

        String methodName = "canStart_Phase";

        Throwable exception = (Throwable) MockPhaseManager.throwExceptions.get(methodName);
        if (exception != null) {
            throw new RuntimeException("The test may not be configured properly", exception);
        }

        HashMap arguments = new HashMap();
        arguments.put("1", phase0);
        List args = (List) MockPhaseManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockPhaseManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return ((Boolean) MockPhaseManager.methodResults.get(methodName)).booleanValue();

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see PhaseManager#start(Phase, String)
     * @throws PhaseManagementException
     */
    public void start(Phase phase0, String string0) throws PhaseManagementException {
        if (MockPhaseManager.globalException != null) {
            if (MockPhaseManager.globalException instanceof PhaseManagementException) {
                throw (PhaseManagementException) MockPhaseManager.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockPhaseManager.globalException);
            }
        }

        String methodName = "start_Phase_String";

        Throwable exception = (Throwable) MockPhaseManager.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof PhaseManagementException) {
                throw (PhaseManagementException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", phase0);
        arguments.put("2", string0);
        List args = (List) MockPhaseManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockPhaseManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see PhaseManager#canEnd(Phase)
     * @throws PhaseManagementException
     */
    public boolean canEnd(Phase phase0) throws PhaseManagementException {
        if (MockPhaseManager.globalException != null) {
            if (MockPhaseManager.globalException instanceof PhaseManagementException) {
                throw (PhaseManagementException) MockPhaseManager.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockPhaseManager.globalException);
            }
        }

        String methodName = "canEnd_Phase";

        Throwable exception = (Throwable) MockPhaseManager.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof PhaseManagementException) {
                throw (PhaseManagementException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", phase0);
        List args = (List) MockPhaseManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockPhaseManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return ((Boolean) MockPhaseManager.methodResults.get(methodName)).booleanValue();

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see PhaseManager#end(Phase, String)
     * @throws PhaseManagementException
     */
    public void end(Phase phase0, String string0) throws PhaseManagementException {
        if (MockPhaseManager.globalException != null) {
            if (MockPhaseManager.globalException instanceof PhaseManagementException) {
                throw (PhaseManagementException) MockPhaseManager.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockPhaseManager.globalException);
            }
        }

        String methodName = "end_Phase_String";

        Throwable exception = (Throwable) MockPhaseManager.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof PhaseManagementException) {
                throw (PhaseManagementException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", phase0);
        arguments.put("2", string0);
        List args = (List) MockPhaseManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockPhaseManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see PhaseManager#canCancel(Phase)
     * @throws PhaseManagementException
     */
    public boolean canCancel(Phase phase0) throws PhaseManagementException {
        if (MockPhaseManager.globalException != null) {
            if (MockPhaseManager.globalException instanceof PhaseManagementException) {
                throw (PhaseManagementException) MockPhaseManager.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockPhaseManager.globalException);
            }
        }

        String methodName = "canCancel_Phase";

        Throwable exception = (Throwable) MockPhaseManager.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof PhaseManagementException) {
                throw (PhaseManagementException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", phase0);
        List args = (List) MockPhaseManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockPhaseManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return ((Boolean) MockPhaseManager.methodResults.get(methodName)).booleanValue();

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see PhaseManager#cancel(Phase, String)
     * @throws PhaseManagementException
     */
    public void cancel(Phase phase0, String string0) throws PhaseManagementException {
        if (MockPhaseManager.globalException != null) {
            if (MockPhaseManager.globalException instanceof PhaseManagementException) {
                throw (PhaseManagementException) MockPhaseManager.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly", MockPhaseManager.globalException);
            }
        }

        String methodName = "cancel_Phase_String";

        Throwable exception = (Throwable) MockPhaseManager.throwExceptions.get(methodName);
        if (exception != null) {
            if (exception instanceof PhaseManagementException) {
                throw (PhaseManagementException) exception;
            } else {
                throw new RuntimeException("The test may not be configured properly", exception);
            }
        }

        HashMap arguments = new HashMap();
        arguments.put("1", phase0);
        arguments.put("2", string0);
        List args = (List) MockPhaseManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockPhaseManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see PhaseManager#registerHandler(PhaseHandler, PhaseType, PhaseOperationEnum)
     */
    public void registerHandler(PhaseHandler phaseHandler0, PhaseType phaseType0, PhaseOperationEnum phaseOperationEnum0) {
        if (MockPhaseManager.globalException != null) {
            throw new RuntimeException("The test may not be configured properly", MockPhaseManager.globalException);
        }

        String methodName = "registerHandler_PhaseHandler_PhaseType_PhaseOperationEnum";

        Throwable exception = (Throwable) MockPhaseManager.throwExceptions.get(methodName);
        if (exception != null) {
            throw new RuntimeException("The test may not be configured properly", exception);
        }

        HashMap arguments = new HashMap();
        arguments.put("1", phaseHandler0);
        arguments.put("2", phaseType0);
        arguments.put("3", phaseOperationEnum0);
        List args = (List) MockPhaseManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockPhaseManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see PhaseManager#unregisterHandler(PhaseType, PhaseOperationEnum)
     */
    public PhaseHandler unregisterHandler(PhaseType phaseType0, PhaseOperationEnum phaseOperationEnum0) {
        if (MockPhaseManager.globalException != null) {
            throw new RuntimeException("The test may not be configured properly", MockPhaseManager.globalException);
        }

        String methodName = "unregisterHandler_PhaseType_PhaseOperationEnum";

        Throwable exception = (Throwable) MockPhaseManager.throwExceptions.get(methodName);
        if (exception != null) {
            throw new RuntimeException("The test may not be configured properly", exception);
        }

        HashMap arguments = new HashMap();
        arguments.put("1", phaseType0);
        arguments.put("2", phaseOperationEnum0);
        List args = (List) MockPhaseManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockPhaseManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (PhaseHandler) MockPhaseManager.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see PhaseManager#getAllHandlers()
     */
    public PhaseHandler[] getAllHandlers() {
        if (MockPhaseManager.globalException != null) {
            throw new RuntimeException("The test may not be configured properly", MockPhaseManager.globalException);
        }

        String methodName = "getAllHandlers";

        Throwable exception = (Throwable) MockPhaseManager.throwExceptions.get(methodName);
        if (exception != null) {
            throw new RuntimeException("The test may not be configured properly", exception);
        }

        HashMap arguments = new HashMap();
        List args = (List) MockPhaseManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockPhaseManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (PhaseHandler[]) MockPhaseManager.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see PhaseManager#getHandlerRegistrationInfo(PhaseHandler)
     */
    public HandlerRegistryInfo[] getHandlerRegistrationInfo(PhaseHandler phaseHandler0) {
        if (MockPhaseManager.globalException != null) {
            throw new RuntimeException("The test may not be configured properly", MockPhaseManager.globalException);
        }

        String methodName = "getHandlerRegistrationInfo_PhaseHandler";

        Throwable exception = (Throwable) MockPhaseManager.throwExceptions.get(methodName);
        if (exception != null) {
            throw new RuntimeException("The test may not be configured properly", exception);
        }

        HashMap arguments = new HashMap();
        arguments.put("1", phaseHandler0);
        List args = (List) MockPhaseManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockPhaseManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (HandlerRegistryInfo[]) MockPhaseManager.methodResults.get(methodName);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see PhaseManager#setPhaseValidator(PhaseValidator)
     */
    public void setPhaseValidator(PhaseValidator phaseValidator0) {
        if (MockPhaseManager.globalException != null) {
            throw new RuntimeException("The test may not be configured properly", MockPhaseManager.globalException);
        }

        String methodName = "setPhaseValidator_PhaseValidator";

        Throwable exception = (Throwable) MockPhaseManager.throwExceptions.get(methodName);
        if (exception != null) {
            throw new RuntimeException("The test may not be configured properly", exception);
        }

        HashMap arguments = new HashMap();
        arguments.put("1", phaseValidator0);
        List args = (List) MockPhaseManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockPhaseManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

    }

    /**
     * <p>A mock implementation of the method. The method either throws an exception which might have been specified
     * through {@link #throwException(String, Throwable)} method or return a result specified through
     * {@link #setMethodResult(String, Object)} method.</p>
     *
     * @see PhaseManager#getPhaseValidator()
     */
    public PhaseValidator getPhaseValidator() {
        if (MockPhaseManager.globalException != null) {
            throw new RuntimeException("The test may not be configured properly", MockPhaseManager.globalException);
        }

        String methodName = "getPhaseValidator";

        Throwable exception = (Throwable) MockPhaseManager.throwExceptions.get(methodName);
        if (exception != null) {
            throw new RuntimeException("The test may not be configured properly", exception);
        }

        HashMap arguments = new HashMap();
        List args = (List) MockPhaseManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockPhaseManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

        return (PhaseValidator) MockPhaseManager.methodResults.get(methodName);

    }

    /**
     * <p>Sets the result to be returned by the specified method.</p>
     *
     * @param methodSignature a <code>String</code> uniquelly distinguishing the target method among other methods
     *        declared by the implemented interface/class.
     * @param result an <code>Object</code> representing the result to be returned by specified method.
     */
    public static void setMethodResult(String methodSignature, Object result) {
        MockPhaseManager.methodResults.put(methodSignature, result);
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
        Map arguments = (Map) MockPhaseManager.methodArguments.get(methodSignature);
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
        return (List) MockPhaseManager.methodArguments.get(methodSignature);
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
            MockPhaseManager.throwExceptions.put(methodSignature, exception);
        } else {
            MockPhaseManager.throwExceptions.remove(methodSignature);
        }
    }

    /**
     * <p>Sets the exception to be thrown when the specified method is called.</p>
     *
     * @param exception a <code>Throwable</code> representing the exception to be thrown whenever any method is
     *        called. If this argument is <code>null</code> then no exception will be thrown.
     */
    public static void throwGlobalException(Throwable exception) {
        MockPhaseManager.globalException = exception;
    }

    /**
     * <p>Releases the state of <code>MockPhaseManager</code> so all collected method arguments,
     * configured method results and exceptions are lost.</p>
     */
    public static void releaseState() {
        MockPhaseManager.methodArguments.clear();
        MockPhaseManager.methodResults.clear();
        MockPhaseManager.throwExceptions.clear();
        MockPhaseManager.globalException = null;
    }

    /**
     * <p>Initializes the initial state for all created instances of <code>MockPhaseManager</code> class.</p>
     */
    public static void init() {
    }

}
