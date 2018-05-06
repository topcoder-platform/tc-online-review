/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.errorhandling;

import junit.framework.TestCase;

/**
 * Unit test for the <code>BaseCriticalException</code> class.
 *
 * @author TCSDEVELOPER
 * @version 2.0
 */
public class BaseCriticalExceptionTests extends TestCase {

    /**
     * Represents the <code>ExceptionData</code> instance used to test
     * against.
     */
    private ExceptionData data;

    /**
     * Represents the <code>Throwable</code> instance used to test against.
     */
    private Throwable throwable;

    /**
     * Set up the test environment.
     */
    protected void setUp() {
        data = new ExceptionData();
        throwable = new NullPointerException();
    }

    /**
     * Accuracy test for the constructor <code>BaseCriticalException()</code>.
     * Instance should be created successfully.
     */
    public void testCtor1() {
        BaseCriticalException exception = new BaseCriticalException();
        assertNotNull("Instance should be created", exception);
        assertTrue("BaseCriticaleException should subclass BaseException", exception instanceof BaseException);
    }

    /**
     * Accuracy test for the constructor
     * <code>BaseCriticalException(ExceptionData)</code>. Instance should be
     * created successfully.
     */
    public void testCtor2() {
        BaseCriticalException exception = new BaseCriticalException(data);
        assertNotNull("Instance should be created", exception);
        assertTrue("BaseCriticalException should subclass BaseException", exception instanceof BaseException);
        ExceptionData nullData = null;
        exception = new BaseCriticalException(nullData);
    }

    /**
     * Accuracy test for the constructor
     * <code>BaseCriticalException(String)</code>. Instance should be created
     * successfully.
     */
    public void testCtor3() {
        BaseCriticalException exception = new BaseCriticalException("test");
        assertNotNull("Instance should be created", exception);
        assertTrue("BaseCriticaleException should subclass BaseException", exception instanceof BaseException);
        assertEquals("Return value should be 'test'", "test", exception.getMessage());
    }

    /**
     * Accuracy test for the constructor
     * <code>BaseCriticalException(String,ExceptionData)</code>. Instance
     * should be created successfully.
     */
    public void testCtor4() {
        BaseCriticalException exception = new BaseCriticalException("test", data);
        assertNotNull("Instance should be created", exception);
        assertTrue("BaseCriticalException should subclass BaseException", exception instanceof BaseException);
        assertEquals("Return value should be 'test'", "test", exception.getMessage());
        ExceptionData nullData = null;
        exception = new BaseCriticalException("test", nullData);
    }

    /**
     * Accuracy test for the constructor
     * <code>BaseCriticalException(String,Throwable)</code>. Instance should
     * be created successfully.
     */
    public void testCtor5() {
        BaseCriticalException exception = new BaseCriticalException("test", throwable);
        assertNotNull("Instance should be created", exception);
        assertTrue("BasCriticalException should subclass BaseException", exception instanceof BaseException);
        assertEquals("Return value should be 'test'", "test", exception.getMessage());
        assertEquals("Cause should be set correctly", throwable, exception.getCause());
    }

    /**
     * Accuracy test for the constructor
     * <code>BaseCriticalException(String,Throwable,ExceptionData)</code>.
     * Instance should be created successfully.
     */
    public void testCtor6() {
        BaseCriticalException exception = new BaseCriticalException("test", throwable, data);
        assertNotNull("Instance should be created", exception);
        assertTrue("BaseCriticalException should subclass BaseException", exception instanceof BaseException);
        assertEquals("Return value should be 'test'", "test", exception.getMessage());
        assertEquals("Cause should be set correctly", throwable, exception.getCause());
        ExceptionData nullData = null;
        exception = new BaseCriticalException("test", throwable, nullData);
    }

    /**
     * Accuracy test for the constructor
     * <code>BaseCriticalException(Throwable)</code>. Instance should be
     * created successfully.
     */
    public void testCtor7() {
        BaseCriticalException exception = new BaseCriticalException(throwable);
        assertNotNull("Instance should be created", exception);
        assertTrue("BasCriticalException should subclass BaseException", exception instanceof BaseException);
        assertEquals("Cause should be set correctly", throwable, exception.getCause());
    }

    /**
     * Accuracy test for the constructor
     * <code>BaseCriticalException(Throwable,ExceptionData)</code>. Instance
     * should be created successfully.
     */
    public void testCtor8() {
        BaseCriticalException exception = new BaseCriticalException(throwable, data);
        assertNotNull("Instance should be created", exception);
        assertTrue("BaseCriticalException should subclass BaseException", exception instanceof BaseException);
        assertEquals("Cause should be set correctly", throwable, exception.getCause());
        ExceptionData nullData = null;
        exception = new BaseCriticalException(throwable, nullData);
    }

}
