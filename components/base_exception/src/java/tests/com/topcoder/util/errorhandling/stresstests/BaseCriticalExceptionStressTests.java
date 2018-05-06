/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.errorhandling.stresstests;

import com.topcoder.util.errorhandling.BaseCriticalException;
import com.topcoder.util.errorhandling.ExceptionData;

import junit.framework.TestCase;


/**
 * Stress test for <code>BaseCriticalException</code>.
 *
 * @author kzhu
 * @version 2.0
 */
public class BaseCriticalExceptionStressTests extends TestCase {
    /** The time to do the stress test. */
    private static final int STRESS_TIME = 200;

    /**
     * Stress test for <code>BaseCriticalException()</code>.
     */
    public void testConstructorWithNoArg() {
        long start = System.currentTimeMillis();

        for (int i = 0; i < STRESS_TIME; i++) {
            assertNotNull("The instance should be created.", new BaseCriticalException());
        }

        System.out.println("Test constructor BaseCriticalException()" + STRESS_TIME + " times costs: "
            + (System.currentTimeMillis() - start) + "ms.");
    }

    /**
     * Stress test for <code>BaseCriticalException(String)</code>.
     */
    public void testConstructorWithStringArg() {
        long start = System.currentTimeMillis();

        for (int i = 0; i < STRESS_TIME; i++) {
            assertNotNull("The instance should be created.", new BaseCriticalException("error"));
        }

        System.out.println("Test constructor BaseCriticalException(String)" + STRESS_TIME + " times costs: "
            + (System.currentTimeMillis() - start) + "ms.");
    }

    /**
     * Stress test for <code>BaseCriticalException(Throwable)</code>.
     */
    public void testConstructorWithThrowalbleArg() {
        Exception e = new Exception();
        long start = System.currentTimeMillis();

        for (int i = 0; i < STRESS_TIME; i++) {
            assertNotNull("The instance should be created.", new BaseCriticalException(e));
        }

        System.out.println("Test constructor BaseCriticalException(Throwable)" + STRESS_TIME + " times costs: "
            + (System.currentTimeMillis() - start) + "ms.");
    }

    /**
     * Stress test for <code>BaseCriticalException(String, Throwable)</code>.
     */
    public void testConstructorWithStringAndThrowableArg() {
        Exception e = new Exception();
        long start = System.currentTimeMillis();

        for (int i = 0; i < STRESS_TIME; i++) {
            assertNotNull("The instance should be created.", new BaseCriticalException("Error", e));
        }

        System.out.println("Test constructor BaseCriticalException(String, Throwable)" + STRESS_TIME + " times costs: "
            + (System.currentTimeMillis() - start) + "ms.");
    }

    /**
     * Stress test for <code>BaseCriticalException(ExceptionData)</code>.
     */
    public void testConstructorWithExceptionDataArg() {
        ExceptionData data = new ExceptionData();
        long start = System.currentTimeMillis();

        for (int i = 0; i < STRESS_TIME; i++) {
            assertNotNull("The instance should be created.", new BaseCriticalException(data));
        }

        System.out.println("Test constructor BaseCriticalException(ExceptionData)" + STRESS_TIME + " times costs: "
            + (System.currentTimeMillis() - start) + "ms.");
    }

    /**
     * Stress test for <code>BaseCriticalException(String, ExceptionData)</code>.
     */
    public void testConstructorWithStringAndExceptionDataArg() {
        ExceptionData data = new ExceptionData();
        long start = System.currentTimeMillis();

        for (int i = 0; i < STRESS_TIME; i++) {
            assertNotNull("The instance should be created.", new BaseCriticalException("Error", data));
        }

        System.out.println("Test constructor BaseCriticalException(String, ExceptionData)" + STRESS_TIME
            + " times costs: " + (System.currentTimeMillis() - start) + "ms.");
    }

    /**
     * Stress test for <code>BaseCriticalException(Throwable, ExceptionData)</code>.
     */
    public void testConstructorWithThrowableAndExceptionDataArg() {
        Exception e = new Exception();
        ExceptionData data = new ExceptionData();
        long start = System.currentTimeMillis();

        for (int i = 0; i < STRESS_TIME; i++) {
            assertNotNull("The instance should be created.", new BaseCriticalException(e, data));
        }

        System.out.println("Test constructor BaseCriticalException(Throwable, ExceptionData)" + STRESS_TIME
            + " times costs: " + (System.currentTimeMillis() - start) + "ms.");
    }

    /**
     * Stress test for <code>BaseCriticalException(String, Throwable, ExceptionData)</code>.
     */
    public void testConstructorWithStringThrowableAndExceptionDataArg() {
        Exception e = new Exception();
        ExceptionData data = new ExceptionData();
        long start = System.currentTimeMillis();

        for (int i = 0; i < STRESS_TIME; i++) {
            assertNotNull("The instance should be created.", new BaseCriticalException("Error", e, data));
        }

        System.out.println("Test constructor BaseCriticalException(String, Throwable, ExceptionData)" + STRESS_TIME
            + " times costs: " + (System.currentTimeMillis() - start) + "ms.");
    }
}
