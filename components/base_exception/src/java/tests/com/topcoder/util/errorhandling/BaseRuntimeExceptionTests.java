/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.errorhandling;

import junit.framework.TestCase;

/**
 * Unit test for the <code>BaseRuntimeException</code> class.
 *
 * @author TCSDEVELOPER
 * @version 2.0
 */
public class BaseRuntimeExceptionTests extends TestCase {
    /**
     * Represents the default <code>BaseRuntimeException</code> instance used
     * to test against.
     */
    private BaseRuntimeException defaultException;

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
        defaultException = new BaseRuntimeException();
        throwable = new NullPointerException();
    }

    /**
     * Accuracy test for the constructor <code>BaseRuntimeException()</code>.
     * Instance should be created successfully.
     */
    public void testCtor1() {
        BaseRuntimeException exception = new BaseRuntimeException();
        assertNotNull("Instance should be created", exception);
        assertTrue("BaseRuntimeException should subclass RuntimeException", exception instanceof RuntimeException);
        assertNotNull("The value of data should not be null", TestHelper.getPrivateField(BaseRuntimeException.class,
                exception, "data"));
    }

    /**
     * Accuracy test for the constructor
     * <code>BaseRuntimeException(ExceptionData)</code>. Instance should be
     * created successfully.
     */
    public void testCtor2() {
        BaseRuntimeException exception = new BaseRuntimeException(data);
        assertNotNull("Instance should be created", exception);
        assertTrue("BaseRuntimeException should subclass RuntimeException", exception instanceof RuntimeException);
        assertEquals("The value of data should be correct", data, TestHelper.getPrivateField(
                BaseRuntimeException.class, exception, "data"));
        ExceptionData nullData = null;
        exception = new BaseRuntimeException(nullData);
        assertNotNull("A new ExceptionData instance should be created", TestHelper.getPrivateField(
                BaseRuntimeException.class, exception, "data"));
    }

    /**
     * Accuracy test for the constructor
     * <code>BaseRuntimeException(String)</code>. Instance should be created
     * successfully.
     */
    public void testCtor3() {
        BaseRuntimeException exception = new BaseRuntimeException("test");
        assertNotNull("Instance should be created", exception);
        assertTrue("BaseRuntimeException should subclass RuntimeException", exception instanceof RuntimeException);
        assertNotNull("The value of data should not be null", TestHelper.getPrivateField(BaseRuntimeException.class,
                exception, "data"));
        assertEquals("Return value should be 'test'", "test", exception.getMessage());
    }

    /**
     * Accuracy test for the constructor
     * <code>BaseRuntimeException(String,ExceptionData)</code>. Instance
     * should be created successfully.
     */
    public void testCtor4() {
        BaseRuntimeException exception = new BaseRuntimeException("test", data);
        assertNotNull("Instance should be created", exception);
        assertTrue("BaseRuntimeException should subclass RuntimeException", exception instanceof RuntimeException);
        assertEquals("The value of data should be correct", data, TestHelper.getPrivateField(
                BaseRuntimeException.class, exception, "data"));
        assertEquals("Return value should be 'test'", "test", exception.getMessage());
        ExceptionData nullData = null;
        exception = new BaseRuntimeException("test", nullData);
        assertNotNull("A new ExceptionData instance should be created", TestHelper.getPrivateField(
                BaseRuntimeException.class, exception, "data"));

    }

    /**
     * Accuracy test for the constructor
     * <code>BaseRuntimeException(String,Throwable)</code>. Instance should
     * be created successfully.
     */
    public void testCtor5() {
        BaseRuntimeException exception = new BaseRuntimeException("test", throwable);
        assertNotNull("Instance should be created", exception);
        assertTrue("BaseRuntimeException should subclass RuntimeException", exception instanceof RuntimeException);
        assertNotNull("The value of data should not be null", TestHelper.getPrivateField(BaseRuntimeException.class,
                exception, "data"));
        assertEquals("Return value should be 'test'", "test", exception.getMessage());
        assertEquals("Cause should be set correctly", throwable, exception.getCause());
    }

    /**
     * Accuracy test for the constructor
     * <code>BaseRuntimeException(String,Throwable,ExceptionData)</code>.
     * Instance should be created successfully.
     */
    public void testCtor6() {
        BaseRuntimeException exception = new BaseRuntimeException("test", throwable, data);
        assertNotNull("Instance should be created", exception);
        assertTrue("BaseRuntimeException should subclass RuntimeException", exception instanceof RuntimeException);
        assertEquals("The value of data should be correct", data, TestHelper.getPrivateField(
                BaseRuntimeException.class, exception, "data"));
        assertEquals("Return value should be 'test'", "test", exception.getMessage());
        assertEquals("Cause should be set correctly", throwable, exception.getCause());
        ExceptionData nullData = null;
        exception = new BaseRuntimeException("test", throwable, nullData);
        assertNotNull("A new ExceptionData instance should be created", TestHelper.getPrivateField(
                BaseRuntimeException.class, exception, "data"));
    }

    /**
     * Accuracy test for the constructor
     * <code>BaseRuntimeException(Throwable)</code>. Instance should be
     * created successfully.
     */
    public void testCtor7() {
        BaseRuntimeException exception = new BaseRuntimeException(throwable);
        assertNotNull("Instance should be created", exception);
        assertTrue("BaseRuntimeException should subclass RuntimeException", exception instanceof RuntimeException);
        assertNotNull("The value of data should not be null", TestHelper.getPrivateField(BaseRuntimeException.class,
                exception, "data"));
        assertEquals("Cause should be set correctly", throwable, exception.getCause());
    }

    /**
     * Accuracy test for the constructor
     * <code>BaseRuntimeException(Throwable,ExceptionData)</code>. Instance
     * should be created successfully.
     */
    public void testCtor8() {
        BaseRuntimeException exception = new BaseRuntimeException(throwable, data);
        assertNotNull("Instance should be created", exception);
        assertTrue("BaseRuntimeException should subclass RuntimeException", exception instanceof RuntimeException);
        assertEquals("The value of data should be correct", data, TestHelper.getPrivateField(
                BaseRuntimeException.class, exception, "data"));
        assertEquals("Cause should be set correctly", throwable, exception.getCause());
        ExceptionData nullData = null;
        exception = new BaseRuntimeException(throwable, nullData);
        assertNotNull("A new ExceptionData instance should be created", TestHelper.getPrivateField(
                BaseRuntimeException.class, exception, "data"));
    }

    /**
     * Accuracy test for the method <code>getApplicationCode()</code>. Null
     * should be returned.
     */
    public void testGetApplicationCode() {
        assertNull("null should be returned", defaultException.getApplicationCode());
    }

    /**
     * Accuracy test for the method <code>getErrorCode()</code>. Null should
     * be returned.
     */
    public void testGetErrorCode() {
        assertNull("null should be returned", defaultException.getErrorCode());
    }

    /**
     * Accuracy test for the method <code>getModuleCode()</code>. Null should
     * be returned.
     */
    public void testGetModuleCode() {
        assertNull("null should be returned", defaultException.getModuleCode());
    }

    /**
     * Accuracy test for the method <code>getCreationDate()</code>. Correct
     * value should be returned.
     */
    public void testGetCreationDate() {
        System.out.println("Creation Date: " + defaultException.getCreationDate());
    }

    /**
     * Accuracy test for the method <code>getThreadName()</code>. Correct
     * value should be returned.
     */
    public void testGetThreadName() {
        System.out.println("Thread Name: " + defaultException.getThreadName());
    }

    /**
     * Accuracy test for the method <code>isLogged()</code>. false should be
     * returned.
     */
    public void testIsLogged() {
        assertFalse("Return value should be false", defaultException.isLogged());
    }

    /**
     * Accuracy test for the method <code>setLogged(boolean)</code>. The
     * value should be set correctly.
     */
    public void testSetLogged() {
        defaultException.setLogged(true);
        assertTrue("Return value should be true", defaultException.isLogged());
        defaultException.setLogged(false);
        assertFalse("Return value should be false", defaultException.isLogged());
    }

    /**
     * Accuracy test for the method <code>getInformation(Object)</code>. null
     * should be returned.
     */
    public void testGetInformation() {
        assertNull("Return value should be null", defaultException.getInformation("test"));
    }

    /**
     * Accuracy test for the method <code>setInformation(Object,Object)</code>.
     * Value should be set correctly.
     */
    public void testSetInformation() {
        defaultException.setInformation("test", "value");
        assertEquals("Return value should be 'value'", "value", defaultException.getInformation("test"));
        defaultException.setInformation("test", null);
        assertNull("Return value should be null", defaultException.getInformation("test"));
    }
}
