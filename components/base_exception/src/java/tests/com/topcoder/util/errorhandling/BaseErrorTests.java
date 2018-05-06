/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.errorhandling;

import junit.framework.TestCase;

/**
 * Unit test for the <code>BaseError</code> class.
 *
 * @author TCSDEVELOPER
 * @version 2.0
 */
public class BaseErrorTests extends TestCase {
    /**
     * Represents the default <code>BaseError</code> instance used to test
     * against.
     */
    private BaseError defaultError;

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
        defaultError = new BaseError();
        throwable = new NullPointerException();
    }

    /**
     * Accuracy test for the constructor <code>BaseError()</code>. Instance
     * should be created successfully.
     */
    public void testCtor1() {
        BaseError error = new BaseError();
        assertNotNull("Instance should be created", error);
        assertTrue("BaseError should subclass Error", error instanceof Error);
        assertNotNull("The value of data should not be null", TestHelper
                .getPrivateField(BaseError.class, error, "data"));
    }

    /**
     * Accuracy test for the constructor <code>BaseError(ExceptionData)</code>.
     * Instance should be created successfully.
     */
    public void testCtor2() {
        BaseError error = new BaseError(data);
        assertNotNull("Instance should be created", error);
        assertTrue("BaseError should subclass Exception", error instanceof Error);
        assertEquals("The value of data should be correct", data, TestHelper.getPrivateField(BaseError.class, error,
                "data"));
        ExceptionData nullData = null;
        error = new BaseError(nullData);
        assertNotNull("A new ExceptionData instance should be created", TestHelper.getPrivateField(BaseError.class,
                error, "data"));
    }

    /**
     * Accuracy test for the constructor <code>BaseError(String)</code>.
     * Instance should be created successfully.
     */
    public void testCtor3() {
        BaseError error = new BaseError("test");
        assertNotNull("Instance should be created", error);
        assertTrue("BaseError should subclass Error", error instanceof Error);
        assertNotNull("The value of data should not be null", TestHelper
                .getPrivateField(BaseError.class, error, "data"));
        assertEquals("Return value should be 'test'", "test", error.getMessage());
    }

    /**
     * Accuracy test for the constructor
     * <code>BaseError(String,ExceptionData)</code>. Instance should be
     * created successfully.
     */
    public void testCtor4() {
        BaseError error = new BaseError("test", data);
        assertNotNull("Instance should be created", error);
        assertTrue("BaseError should subclass Exception", error instanceof Error);
        assertEquals("The value of data should be correct", data, TestHelper.getPrivateField(BaseError.class, error,
                "data"));
        assertEquals("Return value should be 'test'", "test", error.getMessage());
        ExceptionData nullData = null;
        error = new BaseError("test", nullData);
        assertNotNull("A new ExceptionData instance should be created", TestHelper.getPrivateField(BaseError.class,
                error, "data"));
    }

    /**
     * Accuracy test for the constructor
     * <code>BaseError(String,Throwable)</code>. Instance should be created
     * successfully.
     */
    public void testCtor5() {
        BaseError error = new BaseError("test", throwable);
        assertNotNull("Instance should be created", error);
        assertTrue("BaseError should subclass Error", error instanceof Error);
        assertNotNull("The value of data should not be null", TestHelper
                .getPrivateField(BaseError.class, error, "data"));
        assertEquals("Return value should be 'test'", "test", error.getMessage());
        assertEquals("Cause should be set correctly", throwable, error.getCause());
    }

    /**
     * Accuracy test for the constructor <code>BaseError(Throwable)</code>.
     * Instance should be created successfully.
     */
    public void testCtor6() {
        BaseError error = new BaseError(throwable);
        assertNotNull("Instance should be created", error);
        assertTrue("BaseError should subclass Error", error instanceof Error);
        assertNotNull("The value of data should not be null", TestHelper
                .getPrivateField(BaseError.class, error, "data"));
        assertEquals("Cause should be set correctly", throwable, error.getCause());
    }

    /**
     * Accuracy test for the constructor
     * <code>BaseError(Throwable,ExceptionData)</code>. Instance should be
     * created successfully.
     */
    public void testCtor7() {
        BaseError error = new BaseError(throwable, data);
        assertNotNull("Instance should be created", error);
        assertTrue("BaseError should subclass Exception", error instanceof Error);
        assertEquals("The value of data should be correct", data, TestHelper.getPrivateField(BaseError.class, error,
                "data"));
        assertEquals("Cause should be set correctly", throwable, error.getCause());
        ExceptionData nullData = null;
        error = new BaseError(throwable, nullData);
        assertNotNull("A new ExceptionData instance should be created", TestHelper.getPrivateField(BaseError.class,
                error, "data"));
    }

    /**
     * Accuracy test for the constructor
     * <code>BaseError(String,Throwable,ExceptionData)</code>. Instance
     * should be created successfully.
     */
    public void testCtor8() {
        BaseError error = new BaseError("test", throwable, data);
        assertNotNull("Instance should be created", error);
        assertTrue("BaseError should subclass Exception", error instanceof Error);
        assertEquals("The value of data should be correct", data, TestHelper.getPrivateField(BaseError.class, error,
                "data"));
        assertEquals("Return value should be 'test'", "test", error.getMessage());
        assertEquals("Cause should be set correctly", throwable, error.getCause());
        ExceptionData nullData = null;
        error = new BaseError("test", throwable, nullData);
        assertNotNull("A new ExceptionData instance should be created", TestHelper.getPrivateField(BaseError.class,
                error, "data"));
    }

    /**
     * Accuracy test for the method <code>getApplicationCode()</code>. Null
     * should be returned.
     */
    public void testGetApplicationCode() {
        assertNull("null should be returned", defaultError.getApplicationCode());
    }

    /**
     * Accuracy test for the method <code>getErrorCode()</code>. Null should
     * be returned.
     */
    public void testGetErrorCode() {
        assertNull("null should be returned", defaultError.getErrorCode());
    }

    /**
     * Accuracy test for the method <code>getModuleCode()</code>. Null should
     * be returned.
     */
    public void testGetModuleCode() {
        assertNull("null should be returned", defaultError.getModuleCode());
    }

    /**
     * Accuracy test for the method <code>getCreationDate()</code>. Correct
     * value should be returned.
     */
    public void testGetCreationDate() {
        System.out.println("Creation Date: " + defaultError.getCreationDate());
    }

    /**
     * Accuracy test for the method <code>getThreadName()</code>. Correct
     * value should be returned.
     */
    public void testGetThreadName() {
        System.out.println("Thread Name: " + defaultError.getThreadName());
    }

    /**
     * Accuracy test for the method <code>isLogged()</code>. false should be
     * returned.
     */
    public void testIsLogged() {
        assertFalse("Return value should be false", defaultError.isLogged());
    }

    /**
     * Accuracy test for the method <code>setLogged(boolean)</code>. The
     * value should be set correctly.
     */
    public void testSetLogged() {
        defaultError.setLogged(true);
        assertTrue("Return value should be true", defaultError.isLogged());
        defaultError.setLogged(false);
        assertFalse("Return value should be false", defaultError.isLogged());
    }

    /**
     * Accuracy test for the method <code>getInformation(Object)</code>. null
     * should be returned.
     */
    public void testGetInformation() {
        assertNull("Return value should be null", defaultError.getInformation("test"));
    }

    /**
     * Accuracy test for the method <code>setInformation(Object,Object)</code>.
     * Value should be set correctly.
     */
    public void testSetInformation() {
        defaultError.setInformation("test", "value");
        assertEquals("Return value should be 'value'", "value", defaultError.getInformation("test"));
        defaultError.setInformation("test", null);
        assertNull("Return value should be null", defaultError.getInformation("test"));
    }

}
