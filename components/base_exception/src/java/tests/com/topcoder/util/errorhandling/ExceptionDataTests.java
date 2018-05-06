/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.errorhandling;

import junit.framework.TestCase;

/**
 * Unit test for the <code>ExceptionData</code> class.
 *
 * @author TCSDEVELOPER
 * @version 2.0
 */
public class ExceptionDataTests extends TestCase {
    /**
     * Represents the <code>ExceptionData</code> instance used to test
     * against.
     */
    private ExceptionData data;

    /**
     * Set up the test environment.
     */
    protected void setUp() {
        data = new ExceptionData();
    }

    /**
     * Accuracy test for the constructor <code>ExceptionData()</code>.
     * Instance should be created successfully.
     */
    public void testCtor() {
        assertNotNull("Instance should be created", data);
        assertTrue("The logged flag should be false", TestHelper.getPrivateField(ExceptionData.class, data, "logged")
                .equals(Boolean.valueOf(false)));
        assertNull("errorCode should be null", TestHelper.getPrivateField(ExceptionData.class, data, "errorCode"));
        assertNull("applicationCode should be null", TestHelper.getPrivateField(ExceptionData.class, data,
                "applicatioinCode"));
        assertNull("moduleCode should be null", TestHelper.getPrivateField(ExceptionData.class, data, "moduleCode"));
        assertNotNull("creationDate should be non-null", TestHelper.getPrivateField(ExceptionData.class, data,
                "creationDate"));
        assertNotNull("threadName should be non-null", TestHelper.getPrivateField(ExceptionData.class, data,
                "threadName"));
    }

    /**
     * Accuracy test for the method <code>getApplicationCode()</code>. null
     * should be returned.
     */
    public void testGetApplicationCode() {
        assertNull("null should be returned", data.getApplicationCode());
    }

    /**
     * Accuracy test for the method <code>setApplicationCode(String)</code>.
     * The value set should be returned correctly.
     */
    public void testSetApplicationCode() {
        data.setApplicationCode("test");
        assertEquals("test should be returned", "test", data.getApplicationCode());
        data.setApplicationCode("");
        assertEquals("An empty string should be returned", "", data.getApplicationCode());
        data.setApplicationCode(null);
        assertNull("null should be returned", data.getApplicationCode());
    }

    /**
     * Accuracy test for the method <code>getErrorCode()</code>. null should
     * be returned.
     */
    public void testGetErrorCode() {
        assertNull("null should be returned", data.getErrorCode());
    }

    /**
     * Accuracy test for the method <code>setErrorCode(String)</code>. The
     * value set should be returned correctly.
     */
    public void testSetErrorCode() {
        data.setErrorCode("test");
        assertEquals("test should be returned", "test", data.getErrorCode());
        data.setErrorCode("");
        assertEquals("An empty string should be returned", "", data.getErrorCode());
        data.setErrorCode(null);
        assertNull("null should be returned", data.getErrorCode());
    }

    /**
     * Accuracy test for the method <code>getModuleCode()</code>. null should
     * be returned.
     */
    public void testGetModuleCode() {
        assertNull("null should be returned", data.getModuleCode());
    }

    /**
     * Accuracy test for the method <code>setModuleCode(String)</code>. The
     * value set should be returned correctly.
     */
    public void testSetModuleCode() {
        data.setModuleCode("test");
        assertEquals("test should be returned", "test", data.getModuleCode());
        data.setModuleCode("");
        assertEquals("An empty string should be returned", "", data.getModuleCode());
        data.setModuleCode(null);
        assertNull("null should be returned", data.getModuleCode());
    }

    /**
     * Accuracy test for the method <code>isLogged()</code>. false should be
     * returned.
     */
    public void testIsLogged() {
        assertFalse("false should be returned", data.isLogged());
    }

    /**
     * Accuracy test for the method <code>setLogged(boolean)</code>. The
     * value set should be returned correctly.
     */
    public void testSetLogged() {
        data.setLogged(true);
        assertTrue("true should be returned", data.isLogged());
        data.setLogged(false);
        assertFalse("false should be returned", data.isLogged());
    }

    /**
     * Accuracy test for the method <code>getCreationDate()</code>. The
     * creation date of data instance should be returned.
     */
    public void testGetCreationDate() {
        System.out.println("Creation date: " + data.getCreationDate());
    }

    /**
     * Accuracy test for the method <code>getThreadName()</code>. The thread
     * name of data instance should be returned.
     */
    public void testGetThreadName() {
        System.out.println("Current thread name: " + data.getThreadName());
    }

    /**
     * Accuracy test for the method <code>getInformation(Object)</code>. Null
     * value should be returned.
     */
    public void testGetInformation() {
        assertNull("Return value should be null", data.getInformation("test"));
    }

    /**
     * Accuracy test for the method <code>setInformation(Object,Object)</code>.
     * Value should be set correctly.
     */
    public void testSetInformation() {
        data.setInformation("test", "value");
        assertEquals("Return value should be 'value'", "value", data.getInformation("test"));
        assertNull("Return value should be null", data.getInformation("invalid"));
        assertNull("Return value should be null", data.getInformation(null));
        data.setInformation("test", null);
        assertNull("Return value should be null", data.getInformation("test"));
        data.setInformation(null, "nullValue");
        assertEquals("Return value should be 'nullValue'", "nullValue", data.getInformation(null));
        data.setInformation(null, null);
        assertNull("Return value should be null", data.getInformation(null));
    }

}
