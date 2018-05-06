/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.errorhandling;

import junit.framework.TestCase;

/**
 * Unit test for the <code>BaseException</code> class.
 *
 * @author TCSDEVELOPER
 * @version 2.0
 */
public class BaseExceptionTests extends TestCase {

    /**
     * Represents the default <code>BaseException</code> instance used to test
     * against.
     */
    private BaseException defaultException;

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
        defaultException = new BaseException();
        throwable = new NullPointerException();
    }

    /**
     * Accuracy test for the constructor <code>BaseException()</code>.
     * Instance should be created successfully.
     */
    public void testCtor1() {
        BaseException exception = new BaseException();
        assertNotNull("Instance should be created", exception);
        assertTrue("BaseException should subclass Exception", exception instanceof Exception);
        assertNotNull("The value of data should not be null", TestHelper.getPrivateField(BaseException.class,
                exception, "data"));
    }

    /**
     * Accuracy test for the constructor
     * <code>BaseException(ExceptionData)</code>. Instance should be created
     * successfully.
     */
    public void testCtor2() {
        BaseException exception = new BaseException(data);
        assertNotNull("Instance should be created", exception);
        assertTrue("BaseException should subclass Exception", exception instanceof Exception);
        assertEquals("The value of data should be correct", data, TestHelper.getPrivateField(BaseException.class,
                exception, "data"));
        ExceptionData nullData = null;
        exception = new BaseException(nullData);
        assertNotNull("A new ExceptionData instance should be created", TestHelper.getPrivateField(BaseException.class,
                exception, "data"));
    }

    /**
     * Accuracy test for the constructor <code>BaseException(String)</code>.
     * Instance should be created successfully.
     */
    public void testCtor3() {
        BaseException exception = new BaseException("test");
        assertNotNull("Instance should be created", exception);
        assertTrue("BaseException should subclass Exception", exception instanceof Exception);
        assertNotNull("The value of data should not be null", TestHelper.getPrivateField(BaseException.class,
                exception, "data"));
        assertEquals("Return value should be 'test'", "test", exception.getMessage());
    }

    /**
     * Accuracy test for the constructor
     * <code>BaseException(String,ExceptionData)</code>. Instance should be
     * created successfully.
     */
    public void testCtor4() {
        BaseException exception = new BaseException("test", data);
        assertNotNull("Instance should be created", exception);
        assertTrue("BaseException should subclass Exception", exception instanceof Exception);
        assertEquals("The value of data should be correct", data, TestHelper.getPrivateField(BaseException.class,
                exception, "data"));
        assertEquals("Return value should be 'test'", "test", exception.getMessage());
        ExceptionData nullData = null;
        exception = new BaseException("test", nullData);
        assertNotNull("A new ExceptionData instance should be created", TestHelper.getPrivateField(BaseException.class,
                exception, "data"));
    }

    /**
     * Accuracy test for the constructor
     * <code>BaseException(String,Throwable)</code>. Instance should be
     * created successfully.
     */
    public void testCtor5() {
        BaseException exception = new BaseException("test", throwable);
        assertNotNull("Instance should be created", exception);
        assertTrue("BaseException should subclass Exception", exception instanceof Exception);
        assertNotNull("The value of data should not be null", TestHelper.getPrivateField(BaseException.class,
                exception, "data"));
        assertEquals("Return value should be 'test'", "test", exception.getMessage());
        assertEquals("Cause should be set correctly", throwable, exception.getCause());
    }

    /**
     * Accuracy test for the constructor
     * <code>BaseException(String,Throwable,ExceptionData)</code>. Instance
     * should be created successfully.
     */
    public void testCtor6() {
        BaseException exception = new BaseException("test", throwable, data);
        assertNotNull("Instance should be created", exception);
        assertTrue("BaseException should subclass Exception", exception instanceof Exception);
        assertEquals("The value of data should be correct", data, TestHelper.getPrivateField(BaseException.class,
                exception, "data"));
        assertEquals("Return value should be 'test'", "test", exception.getMessage());
        assertEquals("Cause should be set correctly", throwable, exception.getCause());
    }

    /**
     * Accuracy test for the constructor <code>BaseException(Throwable)</code>.
     * Instance should be created successfully.
     */
    public void testCtor7() {
        BaseException exception = new BaseException(throwable);
        assertNotNull("Instance should be created", exception);
        assertTrue("BaseException should subclass Exception", exception instanceof Exception);
        assertNotNull("The value of data should not be null", TestHelper.getPrivateField(BaseException.class,
                exception, "data"));
        assertEquals("Cause should be set correctly", throwable, exception.getCause());
    }

    /**
     * Accuracy test for the constructor
     * <code>BaseException(Throwable,ExceptionData)</code>. Instance should
     * be created successfully.
     */
    public void testCtor8() {
        BaseException exception = new BaseException(throwable, data);
        assertNotNull("Instance should be created", exception);
        assertTrue("BaseException should subclass Exception", exception instanceof Exception);
        assertEquals("The value of data should be correct", data, TestHelper.getPrivateField(BaseException.class,
                exception, "data"));
        assertEquals("Cause should be set correctly", throwable, exception.getCause());
        ExceptionData nullData = null;
        exception = new BaseException(throwable, nullData);
        assertNotNull("A new ExceptionData instance should be created", TestHelper.getPrivateField(BaseException.class,
                exception, "data"));
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
