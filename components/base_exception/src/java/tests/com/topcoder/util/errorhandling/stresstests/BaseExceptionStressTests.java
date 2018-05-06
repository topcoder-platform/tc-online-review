/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.errorhandling.stresstests;

import com.topcoder.util.errorhandling.BaseException;
import com.topcoder.util.errorhandling.ExceptionData;

import junit.framework.TestCase;


/**
 * Stress test for <code>BaseException</code>.
 *
 * @author kzhu
 * @version 2.0
 */
public class BaseExceptionStressTests extends TestCase {
    /** The time to do the stress test. */
    private static final int STRESS_TIME = 200;

    /** Private instance used for test. */
    private BaseException exception = null;

    /**
     * Setup test environment.
     */
    protected void setUp() {
        exception = new BaseException();
    }

    /**
     * Stress test for <code>BaseException()</code>.
     */
    public void testConstructorWithNoArg() {
        long start = System.currentTimeMillis();

        for (int i = 0; i < STRESS_TIME; i++) {
            assertNotNull("The instance should be created.", new BaseException());
        }

        System.out.println("Test constructor BaseException()" + STRESS_TIME + " times costs: "
            + (System.currentTimeMillis() - start) + "ms.");
    }

    /**
     * Stress test for <code>BaseException(String)</code>.
     */
    public void testConstructorWithStringArg() {
        long start = System.currentTimeMillis();

        for (int i = 0; i < STRESS_TIME; i++) {
            assertNotNull("The instance should be created.", new BaseException("exception"));
        }

        System.out.println("Test constructor BaseException(String)" + STRESS_TIME + " times costs: "
            + (System.currentTimeMillis() - start) + "ms.");
    }

    /**
     * Stress test for <code>BaseException(Throwable)</code>.
     */
    public void testConstructorWithThrowalbleArg() {
        Exception e = new Exception();
        long start = System.currentTimeMillis();

        for (int i = 0; i < STRESS_TIME; i++) {
            assertNotNull("The instance should be created.", new BaseException(e));
        }

        System.out.println("Test constructor BaseException(Throwable)" + STRESS_TIME + " times costs: "
            + (System.currentTimeMillis() - start) + "ms.");
    }

    /**
     * Stress test for <code>BaseException(String, Throwable)</code>.
     */
    public void testConstructorWithStringAndThrowableArg() {
        Exception e = new Exception();
        long start = System.currentTimeMillis();

        for (int i = 0; i < STRESS_TIME; i++) {
            assertNotNull("The instance should be created.", new BaseException("Error", e));
        }

        System.out.println("Test constructor BaseException(String, Throwable)" + STRESS_TIME + " times costs: "
            + (System.currentTimeMillis() - start) + "ms.");
    }

    /**
     * Stress test for <code>BaseException(ExceptionData)</code>.
     */
    public void testConstructorWithExceptionDataArg() {
        ExceptionData data = new ExceptionData();
        long start = System.currentTimeMillis();

        for (int i = 0; i < STRESS_TIME; i++) {
            assertNotNull("The instance should be created.", new BaseException(data));
        }

        System.out.println("Test constructor BaseException(ExceptionData)" + STRESS_TIME + " times costs: "
            + (System.currentTimeMillis() - start) + "ms.");
    }

    /**
     * Stress test for <code>BaseException(String, ExceptionData)</code>.
     */
    public void testConstructorWithStringAndExceptionDataArg() {
        ExceptionData data = new ExceptionData();
        long start = System.currentTimeMillis();

        for (int i = 0; i < STRESS_TIME; i++) {
            assertNotNull("The instance should be created.", new BaseException("Error", data));
        }

        System.out.println("Test constructor BaseException(String, ExceptionData)" + STRESS_TIME + " times costs: "
            + (System.currentTimeMillis() - start) + "ms.");
    }

    /**
     * Stress test for <code>BaseException(Throwable, ExceptionData)</code>.
     */
    public void testConstructorWithThrowableAndExceptionDataArg() {
        Exception e = new Exception();
        ExceptionData data = new ExceptionData();
        long start = System.currentTimeMillis();

        for (int i = 0; i < STRESS_TIME; i++) {
            assertNotNull("The instance should be created.", new BaseException(e, data));
        }

        System.out.println("Test constructor BaseException(Throwable, ExceptionData)" + STRESS_TIME + " times costs: "
            + (System.currentTimeMillis() - start) + "ms.");
    }

    /**
     * Stress test for <code>BaseException(String, Throwable, ExceptionData)</code>.
     */
    public void testConstructorWithStringThrowableAndExceptionDataArg() {
        Exception e = new Exception();
        ExceptionData data = new ExceptionData();
        long start = System.currentTimeMillis();

        for (int i = 0; i < STRESS_TIME; i++) {
            assertNotNull("The instance should be created.", new BaseException("Error", e, data));
        }

        System.out.println("Test constructor BaseException(String, Throwable, ExceptionData)" + STRESS_TIME
            + " times costs: " + (System.currentTimeMillis() - start) + "ms.");
    }

    /**
     * Stress test for the method <code>getInformation(Object)</code>.
     */
    public void testGetInformation() {
        exception.setInformation("a", "b");

        long start = System.currentTimeMillis();

        for (int i = 0; i < STRESS_TIME; i++) {
            assertNotNull("Get information should not return null.", exception.getInformation("a"));
        }

        System.out.println("Test getInformation " + STRESS_TIME + " times costs: "
            + (System.currentTimeMillis() - start) + "ms.");
    }

    /**
     * Stress test for the method <code>setInformation(Object, Object)</code>.
     */
    public void testSetInformation() {
        long start = System.currentTimeMillis();

        for (int i = 0; i < STRESS_TIME; i++) {
            exception.setInformation(i + "", "b");
        }

        System.out.println("Test setInformation " + STRESS_TIME + " times costs: "
            + (System.currentTimeMillis() - start) + "ms.");
    }

    /**
     * Stress test for the method <code>isLogged()</code>.
     */
    public void testIsLogged() {
        exception.setLogged(false);

        long start = System.currentTimeMillis();

        for (int i = 0; i < STRESS_TIME; i++) {
            assertFalse("Is logged should be false.", exception.isLogged());
        }

        System.out.println("Test isLogged " + STRESS_TIME + " times costs: " + (System.currentTimeMillis() - start)
            + "ms.");
    }

    /**
     * Stress test for the method <code>setLogged(boolean)</code>.
     */
    public void testSetLogged() {
        long start = System.currentTimeMillis();

        for (int i = 0; i < STRESS_TIME; i++) {
            exception.setLogged(true);
        }

        System.out.println("Test setLogged " + STRESS_TIME + " times costs: " + (System.currentTimeMillis() - start)
            + "ms.");
    }

    /**
     * Stress test for the method <code>getApplicationCode()</code>.
     */
    public void testGetApplicationCode() {
        ExceptionData data = new ExceptionData();
        data.setApplicationCode("ApplicationCode");

        exception = new BaseException(data);

        long start = System.currentTimeMillis();

        for (int i = 0; i < STRESS_TIME; i++) {
            assertEquals("Application code should be get.", "ApplicationCode", exception.getApplicationCode());
        }

        System.out.println("Test getApplicationCode " + STRESS_TIME + " times costs: "
            + (System.currentTimeMillis() - start) + "ms.");
    }

    /**
     * Stress test for the method <code>getModuleCode()</code>.
     */
    public void testGetModuleCode() {
        ExceptionData data = new ExceptionData();
        data.setModuleCode("ModuleCode");

        exception = new BaseException(data);

        long start = System.currentTimeMillis();

        for (int i = 0; i < STRESS_TIME; i++) {
            assertEquals("Application code should be get.", "ModuleCode", exception.getModuleCode());
        }

        System.out.println("Test getModuleCode " + STRESS_TIME + " times costs: "
            + (System.currentTimeMillis() - start) + "ms.");
    }

    /**
     * Stress test for the method <code>getErrorCode()</code>.
     */
    public void testGetErrorCode() {
        ExceptionData data = new ExceptionData();
        data.setErrorCode("ErrorCode");

        exception = new BaseException(data);

        long start = System.currentTimeMillis();

        for (int i = 0; i < STRESS_TIME; i++) {
            assertEquals("Application code should be get.", "ErrorCode", exception.getErrorCode());
        }

        System.out.println("Test getErrorCode " + STRESS_TIME + " times costs: " + (System.currentTimeMillis() - start)
            + "ms.");
    }

    /**
     * Stress test for the method <code>getCreationDate()</code>.
     */
    public void testGetCreationDate() {
        long start = System.currentTimeMillis();

        for (int i = 0; i < STRESS_TIME; i++) {
            assertNotNull("The creation date should be set.", exception.getCreationDate());
        }

        System.out.println("Test getCreationDate " + STRESS_TIME + " times costs: "
            + (System.currentTimeMillis() - start) + "ms.");
    }

    /**
     * Stress test for the method <code>getThreadName()</code>.
     */
    public void testGetThreadName() {
        long start = System.currentTimeMillis();

        for (int i = 0; i < STRESS_TIME; i++) {
            assertEquals("The thread name is wrong.", Thread.currentThread().getName(), exception.getThreadName());
        }

        System.out.println("Test getThreadName " + STRESS_TIME + " times costs: "
            + (System.currentTimeMillis() - start) + "ms.");
    }
}
