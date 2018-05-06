/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.errorhandling.stresstests;

import com.topcoder.util.errorhandling.BaseError;
import com.topcoder.util.errorhandling.ExceptionData;

import junit.framework.TestCase;


/**
 * Stress test for <code>BaseError</code>.
 *
 * @author kzhu
 * @version 2.0
 */
public class BaseErrorStressTests extends TestCase {
    /** The time to do the stress test. */
    private static final int STRESS_TIME = 200;

    /** Private instance used for test. */
    private BaseError error = null;

    /**
     * Setup test environment.
     */
    protected void setUp() {
        error = new BaseError();
    }

    /**
     * Stress test for <code>BaseError()</code>.
     */
    public void testConstructorWithNoArg() {
        long start = System.currentTimeMillis();

        for (int i = 0; i < STRESS_TIME; i++) {
            assertNotNull("The instance should be created.", new BaseError());
        }

        System.out.println("Test constructor BaseError()" + STRESS_TIME + " times costs: "
            + (System.currentTimeMillis() - start) + "ms.");
    }

    /**
     * Stress test for <code>BaseError(String)</code>.
     */
    public void testConstructorWithStringArg() {
        long start = System.currentTimeMillis();

        for (int i = 0; i < STRESS_TIME; i++) {
            assertNotNull("The instance should be created.", new BaseError("error"));
        }

        System.out.println("Test constructor BaseError(String)" + STRESS_TIME + " times costs: "
            + (System.currentTimeMillis() - start) + "ms.");
    }

    /**
     * Stress test for <code>BaseError(Throwable)</code>.
     */
    public void testConstructorWithThrowalbleArg() {
        Exception e = new Exception();
        long start = System.currentTimeMillis();

        for (int i = 0; i < STRESS_TIME; i++) {
            assertNotNull("The instance should be created.", new BaseError(e));
        }

        System.out.println("Test constructor BaseError(Throwable)" + STRESS_TIME + " times costs: "
            + (System.currentTimeMillis() - start) + "ms.");
    }

    /**
     * Stress test for <code>BaseError(String, Throwable)</code>.
     */
    public void testConstructorWithStringAndThrowableArg() {
        Exception e = new Exception();
        long start = System.currentTimeMillis();

        for (int i = 0; i < STRESS_TIME; i++) {
            assertNotNull("The instance should be created.", new BaseError("Error", e));
        }

        System.out.println("Test constructor BaseError(String, Throwable)" + STRESS_TIME + " times costs: "
            + (System.currentTimeMillis() - start) + "ms.");
    }

    /**
     * Stress test for <code>BaseError(ExceptionData)</code>.
     */
    public void testConstructorWithExceptionDataArg() {
        ExceptionData data = new ExceptionData();
        long start = System.currentTimeMillis();

        for (int i = 0; i < STRESS_TIME; i++) {
            assertNotNull("The instance should be created.", new BaseError(data));
        }

        System.out.println("Test constructor BaseError(ExceptionData)" + STRESS_TIME + " times costs: "
            + (System.currentTimeMillis() - start) + "ms.");
    }

    /**
     * Stress test for <code>BaseError(String, ExceptionData)</code>.
     */
    public void testConstructorWithStringAndExceptionDataArg() {
        ExceptionData data = new ExceptionData();
        long start = System.currentTimeMillis();

        for (int i = 0; i < STRESS_TIME; i++) {
            assertNotNull("The instance should be created.", new BaseError("Error", data));
        }

        System.out.println("Test constructor BaseError(String, ExceptionData)" + STRESS_TIME + " times costs: "
            + (System.currentTimeMillis() - start) + "ms.");
    }

    /**
     * Stress test for <code>BaseError(Throwable, ExceptionData)</code>.
     */
    public void testConstructorWithThrowableAndExceptionDataArg() {
        Exception e = new Exception();
        ExceptionData data = new ExceptionData();
        long start = System.currentTimeMillis();

        for (int i = 0; i < STRESS_TIME; i++) {
            assertNotNull("The instance should be created.", new BaseError(e, data));
        }

        System.out.println("Test constructor BaseError(Throwable, ExceptionData)" + STRESS_TIME + " times costs: "
            + (System.currentTimeMillis() - start) + "ms.");
    }

    /**
     * Stress test for <code>BaseError(String, Throwable, ExceptionData)</code>.
     */
    public void testConstructorWithStringThrowableAndExceptionDataArg() {
        Exception e = new Exception();
        ExceptionData data = new ExceptionData();
        long start = System.currentTimeMillis();

        for (int i = 0; i < STRESS_TIME; i++) {
            assertNotNull("The instance should be created.", new BaseError("Error", e, data));
        }

        System.out.println("Test constructor BaseError(String, Throwable, ExceptionData)" + STRESS_TIME
            + " times costs: " + (System.currentTimeMillis() - start) + "ms.");
    }

    /**
     * Stress test for the method <code>getInformation(Object)</code>.
     */
    public void testGetInformation() {
        error.setInformation("a", "b");

        long start = System.currentTimeMillis();

        for (int i = 0; i < STRESS_TIME; i++) {
            assertNotNull("Get information should not return null.", error.getInformation("a"));
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
            error.setInformation(i + "", "b");
        }

        System.out.println("Test setInformation " + STRESS_TIME + " times costs: "
            + (System.currentTimeMillis() - start) + "ms.");
    }

    /**
     * Stress test for the method <code>isLogged()</code>.
     */
    public void testIsLogged() {
        error.setLogged(false);

        long start = System.currentTimeMillis();

        for (int i = 0; i < STRESS_TIME; i++) {
            assertFalse("Is logged should be false.", error.isLogged());
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
            error.setLogged(true);
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

        error = new BaseError(data);

        long start = System.currentTimeMillis();

        for (int i = 0; i < STRESS_TIME; i++) {
            assertEquals("Application code should be get.", "ApplicationCode", error.getApplicationCode());
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

        error = new BaseError(data);

        long start = System.currentTimeMillis();

        for (int i = 0; i < STRESS_TIME; i++) {
            assertEquals("Application code should be get.", "ModuleCode", error.getModuleCode());
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

        error = new BaseError(data);

        long start = System.currentTimeMillis();

        for (int i = 0; i < STRESS_TIME; i++) {
            assertEquals("Application code should be get.", "ErrorCode", error.getErrorCode());
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
            assertNotNull("The creation date should be set.", error.getCreationDate());
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
            assertEquals("The thread name is wrong.", Thread.currentThread().getName(), error.getThreadName());
        }

        System.out.println("Test getThreadName " + STRESS_TIME + " times costs: "
            + (System.currentTimeMillis() - start) + "ms.");
    }
}
