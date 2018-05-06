/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.errorhandling.stresstests;

import com.topcoder.util.errorhandling.BaseNonCriticalException;
import com.topcoder.util.errorhandling.ExceptionData;

import junit.framework.TestCase;


/**
 * Stress test for <code>BaseNonCriticalException</code>.
 *
 * @author kzhu
 * @version 2.0
 */
public class BaseNonCriticalExceptionStressTests extends TestCase {
    /** The time to do the stress test. */
    private static final int STRESS_TIME = 200;

    /**
     * Stress test for <code>BaseNonCriticalException()</code>.
     */
    public void testConstructorWithNoArg() {
        long start = System.currentTimeMillis();

        for (int i = 0; i < STRESS_TIME; i++) {
            assertNotNull("The instance should be created.", new BaseNonCriticalException());
        }

        System.out.println("Test constructor BaseNonCriticalException()" + STRESS_TIME + " times costs: "
            + (System.currentTimeMillis() - start) + "ms.");
    }

    /**
     * Stress test for <code>BaseNonCriticalException(String)</code>.
     */
    public void testConstructorWithStringArg() {
        long start = System.currentTimeMillis();

        for (int i = 0; i < STRESS_TIME; i++) {
            assertNotNull("The instance should be created.", new BaseNonCriticalException("error"));
        }

        System.out.println("Test constructor BaseNonCriticalException(String)" + STRESS_TIME + " times costs: "
            + (System.currentTimeMillis() - start) + "ms.");
    }

    /**
     * Stress test for <code>BaseNonCriticalException(Throwable)</code>.
     */
    public void testConstructorWithThrowalbleArg() {
        Exception e = new Exception();
        long start = System.currentTimeMillis();

        for (int i = 0; i < STRESS_TIME; i++) {
            assertNotNull("The instance should be created.", new BaseNonCriticalException(e));
        }

        System.out.println("Test constructor BaseNonCriticalException(Throwable)" + STRESS_TIME + " times costs: "
            + (System.currentTimeMillis() - start) + "ms.");
    }

    /**
     * Stress test for <code>BaseNonCriticalException(String, Throwable)</code>.
     */
    public void testConstructorWithStringAndThrowableArg() {
        Exception e = new Exception();
        long start = System.currentTimeMillis();

        for (int i = 0; i < STRESS_TIME; i++) {
            assertNotNull("The instance should be created.", new BaseNonCriticalException("Error", e));
        }

        System.out.println("Test constructor BaseNonCriticalException(String, Throwable)" + STRESS_TIME + " times costs: "
            + (System.currentTimeMillis() - start) + "ms.");
    }

    /**
     * Stress test for <code>BaseNonCriticalException(ExceptionData)</code>.
     */
    public void testConstructorWithExceptionDataArg() {
        ExceptionData data = new ExceptionData();
        long start = System.currentTimeMillis();

        for (int i = 0; i < STRESS_TIME; i++) {
            assertNotNull("The instance should be created.", new BaseNonCriticalException(data));
        }

        System.out.println("Test constructor BaseNonCriticalException(ExceptionData)" + STRESS_TIME + " times costs: "
            + (System.currentTimeMillis() - start) + "ms.");
    }

    /**
     * Stress test for <code>BaseNonCriticalException(String, ExceptionData)</code>.
     */
    public void testConstructorWithStringAndExceptionDataArg() {
        ExceptionData data = new ExceptionData();
        long start = System.currentTimeMillis();

        for (int i = 0; i < STRESS_TIME; i++) {
            assertNotNull("The instance should be created.", new BaseNonCriticalException("Error", data));
        }

        System.out.println("Test constructor BaseNonCriticalException(String, ExceptionData)" + STRESS_TIME
            + " times costs: " + (System.currentTimeMillis() - start) + "ms.");
    }

    /**
     * Stress test for <code>BaseNonCriticalException(Throwable, ExceptionData)</code>.
     */
    public void testConstructorWithThrowableAndExceptionDataArg() {
        Exception e = new Exception();
        ExceptionData data = new ExceptionData();
        long start = System.currentTimeMillis();

        for (int i = 0; i < STRESS_TIME; i++) {
            assertNotNull("The instance should be created.", new BaseNonCriticalException(e, data));
        }

        System.out.println("Test constructor BaseNonCriticalException(Throwable, ExceptionData)" + STRESS_TIME
            + " times costs: " + (System.currentTimeMillis() - start) + "ms.");
    }

    /**
     * Stress test for <code>BaseNonCriticalException(String, Throwable, ExceptionData)</code>.
     */
    public void testConstructorWithStringThrowableAndExceptionDataArg() {
        Exception e = new Exception();
        ExceptionData data = new ExceptionData();
        long start = System.currentTimeMillis();

        for (int i = 0; i < STRESS_TIME; i++) {
            assertNotNull("The instance should be created.", new BaseNonCriticalException("Error", e, data));
        }

        System.out.println("Test constructor BaseNonCriticalException(String, Throwable, ExceptionData)" + STRESS_TIME
            + " times costs: " + (System.currentTimeMillis() - start) + "ms.");
    }
}
